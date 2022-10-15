import {
  Button,
  Paper,
} from "@mui/material";
import React, { useContext, useState } from "react";
import { useQuery } from "react-query";

import LoginContext from "../../store/login-context";
import { Header, InputFieldsBox, StyledTableContainer } from "../ui/Theme";
import httpClient from "../../util/http-client";
import useQueryParam from "../../util/queryparam-hook";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import useMediaQuery from "@mui/material/useMediaQuery";
import UploadAssessmentQnModal from "./UploadAssessmentQnModal";
import { useTheme } from '@mui/material/styles';
import moment from "moment";

import DownloadIcon from "@mui/icons-material/DownloadOutlined";
import UploadIcon from "@mui/icons-material/UploadOutlined";
import { saveAs } from 'file-saver';
import { StripedDataGrid } from "../ui/DataGridCustomization";

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
async function  downloadFile(fileName){
  console.log(fileName);
   
  return httpClient.get(`/assessment/downloadFile?documentType=QuestionPaper&documentName=${fileName}`,{
    responseType: 'blob'
  }).then(({data}) => {   
    saveAs(data, fileName);
  })};

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
      refetchOnWindowFocus: false
    
    }
  );

  const [dialogOpen, setDialogOpen] = React.useState(false);
  const theme = useTheme();
  const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
 
  const dataLoaded = status === 'success' && data &&  data.assessmentDetailsRecordList;
  

  const handleDialogOpen = (assessmentId,assessmentDate) => {
    
  
    setAssessmentId(assessmentId);
    setDialogOpen(true);
  };

  const handleDialogClose = () => {
    setDialogOpen(false);
  };

  const columns = [
   
    {
      field: 'teacher',
      headerName: 'Name',
      headerClassName: 'DataGrid-Header',
      width: 180
    
    },
    {
      field: 'subject',
      headerName: 'Subject',
      headerClassName: 'DataGrid-Header',
      width: 150
   
    },
    {
      field: 'grade',
      headerName: 'Grade',
      headerClassName: 'DataGrid-Header',
      width: 140,
     
    },
    {
      field: 'assessmentDate',
      headerName: 'Date',
      type: 'date',
      headerClassName: 'DataGrid-Header',
      width: 180,
      valueFormatter: params => 
      moment(params?.value).format("DD/MM/YYYY hh:mm A"),
     
    },
    {
      field: 'qnPaperDocument',
      headerName: 'Question Paper',
      headerClassName: 'DataGrid-Header',
      description: 'This column has a value getter and is not sortable.',
      sortable: false,
      width: 180,
      renderCell: (params) => ( params.row.qnPaperDocument?<Button  color="primary" startIcon={<DownloadIcon />} onClick={()=>downloadFile(params.row.qnPaperDocument)}>
      Download
    </Button>: checkWhetherTodayDate(params.row.assessmentDate)? <Button color="secondary" startIcon={<UploadIcon />} onClick={() => handleDialogOpen(params.row.assessmentId,params.row.assessmentDate)}>
    &nbsp;&nbsp;&nbsp; Upload&nbsp;&nbsp;&nbsp;
 </Button>:'')

      
      
    },
  ];

 
  


  return (
    <InputFieldsBox sx={{ width: '70%', overflow: 'hidden' }}>
        <Header variant="h4" component="h2">
              ASSESSMENTS SCHEDULED
            </Header>
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
      <StyledTableContainer component={Paper} sx={{height: 350,width:"100%"}}>
        
    
      {dataLoaded && <StripedDataGrid
        rows={data.assessmentDetailsRecordList}
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
export default AssessmentList;
