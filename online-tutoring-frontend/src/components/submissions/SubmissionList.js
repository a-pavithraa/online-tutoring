import {
    Button,
    CircularProgress,
    Fab,
    Paper,
    Table,
    TableBody,
    TableContainer,
    TableHead,
    TableRow,
  } from "@mui/material";
  import React, { useContext, useState } from "react";
  import { useQuery } from "react-query";
  
  import LoginContext from "../../store/login-context";
  import { Header, InputFieldsBox, StyledTableCell, StyledTableRow } from "../ui/Theme";
  import httpClient from "../../util/http-client";
  import useQueryParam from "../../util/queryparam-hook";
  import useMediaQuery from "@mui/material/useMediaQuery";
  import { useTheme } from '@mui/material/styles';

  import { saveAs } from 'file-saver';
  
  import DownloadIcon from "@mui/icons-material/DownloadOutlined";
import UpdateSubmissionModal from "./UpdateSubmissionModal";
  async function getAssessmentDetails(teacherId) {
   
    const res = await httpClient.get(
      `assessment/submittedAssessmentDetails?teacherId=${teacherId }`
    );
    return res.data;
  } 
  async function  downloadFile(fileName){
    console.log(fileName);
     
    return httpClient.get(`/assessment/downloadFile?documentType=AnswerSheet&documentName=${fileName}`,{
      responseType: 'blob'
    }).then(({data}) => {   
      saveAs(data, fileName);
    })};
  
 
  const SubmissionList = (props) => {
    const queryParam = useQueryParam();
    const { teacherId } = useContext(LoginContext);
    const [open, setOpen] = useState(false);
    const [rowData, setRowData] = useState();
    const [refetchedData,setRefetchedData]=useState(false);
    const handleClose = () => {
      setOpen(false);
    };
  
    const refetchData=()=>{
      refetch();
     
    }
    const updateSubmission = (rowData) => {
        setRowData(rowData);
    
      setOpen(true);
    };
    const { data, isFetching, status,refetch } = useQuery(
      ["submittedAssessments",refetchedData],
      () =>
        getAssessmentDetails(
          teacherId
        ),
      {
        refetchOnWindowFocus: false,
        staleTime: 0,
        cacheTime: 0,
        refetchInterval: 0,
      }
    );
  
    const [dialogOpen, setDialogOpen] = React.useState(false);
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
  
  
   
  
  
    return (
      <InputFieldsBox>
      
      {open && (
        <UpdateSubmissionModal
          rowData={rowData}
          open={open}
          handleClose={handleClose}
          refetch={refetchData}
          setRefetchedData={setRefetchedData}
        />
      )}
        <TableContainer component={Paper} sx={{ maxWidth: "100%",maxHeight:440 }}>
        <Header variant="h4" component="h2">
                ASSESSMENT PAPER SUBMISSIONS
              </Header>
          <Table stickyHeader  aria-label="simple table">
            <TableHead>
              <TableRow>
                <StyledTableCell>Name</StyledTableCell>
                <StyledTableCell>Answer Sheet</StyledTableCell>
                <StyledTableCell>Corrected Answer Sheet</StyledTableCell>
                <StyledTableCell>Marks</StyledTableCell>
                <StyledTableCell>&nbsp;</StyledTableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {isFetching && (
                <StyledTableRow>
                  <StyledTableCell colSpan={4}>
                    <CircularProgress />
                  </StyledTableCell>
                </StyledTableRow>
              )}
              {status === 'success' && data &&
                data.submittedAssessmentsRecords &&
                data.submittedAssessmentsRecords.map((row) => (
                  <StyledTableRow
                    key={row.assessmentId}
                    sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                  >
                    <StyledTableCell component="th" scope="row">
                      {row.studentName}
                    </StyledTableCell>
                    <StyledTableCell><Fab color="primary" size="small" component="span" aria-label="add" variant="extended" onClick={()=>downloadFile(row.answerSheet)}> <DownloadIcon /> Answer Sheet </Fab></StyledTableCell>
                    <StyledTableCell>{row.correctedAnswerSheet?<Fab color="secondary" size="small" component="span" aria-label="add" variant="extended" onClick={()=>downloadFile(row.correctedAnswerSheet)}> <DownloadIcon /> Corrected Sheet </Fab>:''}</StyledTableCell>
                    <StyledTableCell>{row.correctedAnswerSheet?row.marks:"NOT EVALUATED"}</StyledTableCell>
                    <StyledTableCell>
                    {row.correctedAnswerSheet?'':<Button sx={{color:'red'}} onClick={()=>updateSubmission(row)}>EVALUATE</Button>}
                    </StyledTableCell>
                  </StyledTableRow>
                ))}
              {!isFetching && (!data || data.length == 0) && (
                <StyledTableRow>
                  <StyledTableCell colSpan={5}>
                    No Submissions found
                  </StyledTableCell>
                </StyledTableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </InputFieldsBox>
    );
  };
  export default SubmissionList;
  