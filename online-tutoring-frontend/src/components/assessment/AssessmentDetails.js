import {
  Button,
  CircularProgress,
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
import { InputFieldsBox, StyledTableCell, StyledTableRow } from "../ui/Theme";
import httpClient from "../util/http-client";
import useQueryParam from "../util/queryparam-hook";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import useMediaQuery from "@mui/material/useMediaQuery";
import UploadAssessmentQnModal from "./UploadAssessmentQnModal";
import { useTheme } from '@mui/material/styles';
import moment from "moment";


async function getAssessmentDetails(teacherId, gradeId, subjectId) {
  let gradeIdquery = gradeId ? "&gradeId=" + gradeId : "";
  let subjectIdQuery = subjectId ? "&subjectId=" + subjectId : "";

  const res = await httpClient.get(
    "assessment/assessmentDetails?teacherId=" +
      teacherId +
      gradeIdquery +
      subjectIdQuery
  );
  return res.data;
}

function checkWhetherTodayDate(assessmentDate){
    const convertedDate = moment(assessmentDate).format("DD/MM/YYYY");
    const now = moment().format("DD/MM/YYYY");
  
    if (now === convertedDate) {
       
       return true;
     } 
     return false;
}

const AssessmentList = (props) => {
  const queryParam = useQueryParam();
  const { teacherId } = useContext(LoginContext);
  const [open, setOpen] = useState(false);
  const [assessmentId, setAssessmentId] = useState();
  const [refetchedData,setRefetchedData]=useState(false);
  const handleClose = () => {
    setOpen(false);
  };

  const refetchData=()=>{
    refetch();
   
  }
  const uploadQnPaper = () => {
    setAssessmentId(assessmentId);
    handleDialogClose();
    setOpen(true);
  };
  const { data, isFetching, status,refetch } = useQuery(
    ["assessmentDetails",refetchedData],
    () =>
      getAssessmentDetails(
        teacherId,
        queryParam.get("gradeId"),
        queryParam.get("subjectId")
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


  const handleDialogOpen = (assessmentId,assessmentDate) => {
    
  
    setAssessmentId(assessmentId);
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };


  return (
    <InputFieldsBox>
      {open && (
        <UploadAssessmentQnModal
          assessmentId={assessmentId}
          open={open}
          handleClose={handleClose}
          refetch={refetchData}
          setRefetchedData={setRefetchedData}
        />
      )}
        <Dialog
        fullScreen={fullScreen}
        open={dialogOpen}
        onClose={handleDialogClose}
        aria-labelledby="responsive-dialog-title"
      >
        <DialogTitle id="responsive-dialog-title">
          {"Upload Question Paper?"}
        </DialogTitle>
        <DialogContent sx={{background:"white",color:"black"}}>
          <DialogContentText sx={{marginTop:"5px"}}>
           <b> Uploading  will notify the students about the availability of Question Paper. Do you want to proceed?</b>
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{background:theme.palette.grey}}>
          <Button variant="contained" color="secondary" autoFocus onClick={handleDialogClose}>
            Disagree
          </Button>
          <Button variant="contained"  onClick={uploadQnPaper} autoFocus>
            Agree
          </Button>
        </DialogActions>
      </Dialog>
      <TableContainer component={Paper} sx={{ maxWidth: "100%" }}>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Name</StyledTableCell>
              <StyledTableCell>Subject</StyledTableCell>
              <StyledTableCell>Grade</StyledTableCell>
              <StyledTableCell>Date</StyledTableCell>
              <StyledTableCell>Document</StyledTableCell>
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
              data.assessmentDetailsRecordList &&
              data.assessmentDetailsRecordList.map((row) => (
                <StyledTableRow
                  key={row.assessmentId}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <StyledTableCell component="th" scope="row">
                    {row.teacher}
                  </StyledTableCell>
                  <StyledTableCell>{row.subject}</StyledTableCell>
                  <StyledTableCell>{row.grade}</StyledTableCell>
                  <StyledTableCell>{row.assessmentDate}</StyledTableCell>
                  <StyledTableCell>
                    {
                    row.qnPaperDocument?row.qnPaperDocument:
                     checkWhetherTodayDate(row.assessmentDate)? <Button
                        variant="contained"
                        onClick={() => handleDialogOpen(row.assessmentId,row.assessmentDate)}
                      >
                        Upload Paper
                      </Button>:''
                    
                    }
                  </StyledTableCell>
                </StyledTableRow>
              ))}
            {!isFetching && (!data || data.length == 0) && (
              <StyledTableRow>
                <StyledTableCell colSpan={5}>
                  No Assessments found
                </StyledTableCell>
              </StyledTableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </InputFieldsBox>
  );
};
export default AssessmentList;
