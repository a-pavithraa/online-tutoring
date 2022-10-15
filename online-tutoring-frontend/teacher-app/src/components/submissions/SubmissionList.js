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
    Typography,
  } from "@mui/material";
  import React, { useContext, useState } from "react";
  import { useQuery } from "react-query";
  
  import LoginContext from "../../store/login-context";
  import { Header, InputFieldsBox, StyledTableCell, StyledTableContainer, StyledTableRow } from "../ui/Theme";
  import httpClient from "../../util/http-client";
  import useQueryParam from "../../util/queryparam-hook";
  import useMediaQuery from "@mui/material/useMediaQuery";
  import { useTheme } from '@mui/material/styles';

  import { saveAs } from 'file-saver';
  
  import DownloadIcon from "@mui/icons-material/DownloadOutlined";
import UpdateSubmissionModal from "./UpdateSubmissionModal";
import { StripedDataGrid } from "../ui/DataGridCustomization";
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
    const dataLoaded = data && data.submittedAssessmentsRecords;
  
    const columns = [
   
  
      {
        field: 'studentName',
        headerName: 'Name',
        flex:1,
        headerClassName: 'DataGrid-Header',
        minWidth: 200,
       
     
      },
      {
        field: 'assessmentDate',
        flex:1,
        headerName: 'Assessment Date',
        headerClassName: 'DataGrid-Header',
        minWidth: 150,
       
       
      },
      {
            field: 'answerSheet',
            flex:1,
            headerName: 'Answer Sheet',
            headerClassName: 'DataGrid-Header',
            minWidth: 200,
           
             sortable: false,
           renderCell: (params) => (  <Button  color="primary" startIcon={<DownloadIcon />} onClick={()=>downloadFile(params.row.answerSheet)}>
                      Answer Sheet
                    </Button>)
           
      },
      {
        field: 'correctedAnswerSheet',
        flex:1,
        headerName: 'Corrected Answer Sheet',
      
        headerClassName: 'DataGrid-Header',
        minWidth: 220,
     
         sortable: false,
           renderCell: (params) => (  params.row.correctedAnswerSheet?<Button  color="secondary" startIcon={<DownloadIcon />} onClick={()=>downloadFile(params.row.correctedAnswerSheet)}>
                      Corrected Sheet
                    </Button>:'')
      
      
       
      },
       {
           field: 'marks',
           flex:1,
           headerName: 'Marks',
           type:'Number',
           headerClassName: 'DataGrid-Header',
           minWidth: 120,
         
           renderCell: ({row}) => {
                   if (row.marks === 0) {
                     return 'NOT EVALUATED';
                   }
     
                  
                   return <Typography sx={{fontWeight: 'bolder'}}>{row.marks}</Typography>;
              }
          
      },
      {
            field: 'evaluation',
            headerName: 'Update',
            flex:1,
            headerClassName: 'DataGrid-Header',
            minWidth: 120,
           
             sortable: false,
               renderCell: (params) => (  params.row.correctedAnswerSheet?'':<Button sx={{color:'red'}} onClick={()=>updateSubmission(params.row)}>EVALUATE</Button>)
          
          
           
      }
    ];
    const theme = useTheme();  
   
  
  
    return (
      <InputFieldsBox>
      
      {open && (
        <UpdateSubmissionModal
          rowData={rowData}
          open={open}
          handleClose={handleClose}
         
          setRefetchedData={setRefetchedData}
        />
      )}
        
        <Header variant="h4" component="h2">
                ASSESSMENT PAPER SUBMISSIONS
              </Header>
              <StyledTableContainer component={Paper} sx={{ height: 350, }}>
       {dataLoaded && <StripedDataGrid
        rows={data.submittedAssessmentsRecords}
        columns={columns}
        getRowId={(row) => row.assessmentId}
        pageSize={5}
       
        getRowClassName={(params) =>
          params.indexRelativeToCurrentPage % 2 === 0 ? 'even' : 'odd'
        }
        rowsPerPageOptions={[5]}        
    
      />}
       
      </StyledTableContainer>
     
      </InputFieldsBox>
    );
  };
  export default SubmissionList;
  