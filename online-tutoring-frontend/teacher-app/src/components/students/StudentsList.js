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
import { Header, InputFieldsBox, StyledTableCell, StyledTableContainer, StyledTableRow } from "../ui/Theme";
import httpClient from "../../util/http-client";
import useQueryParam from "../../util/queryparam-hook";
import PaginationComponent from "../ui/PaginationComponent";
import AnalyticsIcon from '@mui/icons-material/Analytics';
import StudentPerformanceModal from "./StudentPerformanceModal";
import { StripedDataGrid } from "../ui/DataGridCustomization";
async function fetchStudentDetails(teacherId, gradeId, subjectId) {
  let gradeIdquery = gradeId? "&gradeId="+gradeId :'';
  let subjectIdQuery = subjectId?"&subjectId="+subjectId:'';
  const res = await httpClient.get(
    "/mdm/mapping/studentsOfTeacher?teacherId=" +
      teacherId +
      gradeIdquery
   +subjectIdQuery
  );
  return res.data;
}
const StudentsList = (props) => {
  const queryParam = useQueryParam();
  const {teacherId}=useContext(LoginContext);
  const [open, setOpen] = useState(false);
  const [studentId,setSelectedStudentId] =useState();
  const [studentName,setSelectedStudentName] =useState();
  const { data, isFetching } = useQuery(
    "studentsOfClass",
    () =>
      fetchStudentDetails(
        teacherId,
        queryParam.get("gradeId"),
        queryParam.get("subjectId")
      ),
    {
      refetchOnWindowFocus: false,
    }
  );
  const handleClose = () => {
    setOpen(false);
  };
  const viewPerformanceModal=(studentId,studentName)=>{
    setSelectedStudentId(studentId);
    setSelectedStudentName(studentName);
    setOpen(true);

  }

  const dataLoaded =data && data.studentRecords;
  const columns = [  
  
    {
      field: 'fullName',
      headerName: 'Name',
      headerClassName: 'DataGrid-Header',
      minWidth: 160,
      flex: 1
   
    },
    {
      field: 'email',
      headerName: 'E-Mail',
      headerClassName: 'DataGrid-Header',
      minWidth: 200,
      flex: 1
     
    },
     {
          field: 'parentName',
          headerName: 'Parent Name',
          headerClassName: 'DataGrid-Header',
          minWidth: 160,
          flex: 1
         
    },
     {
          field: 'contactNo',
          headerName: 'Contact No',
          headerClassName: 'DataGrid-Header',
          minWidth: 120,
          flex: 1
         
    },
     {
              field: 'address',
              headerName: 'Address',
              headerClassName: 'DataGrid-Header',
              minWidth: 180,
              flex: 1
             
    },
    
    {
          field: 'performance',
          headerName: 'Performance',
          headerClassName: 'DataGrid-Header',
          minWidth: 120,
          flex: 1,
           sortable: false,
         renderCell: ({row}) => (  <Button startIcon={<AnalyticsIcon/>}onClick={()=>viewPerformanceModal(row.id,row.fullName.toUpperCase())}>View</Button>)
         
    }
    
  ];
  return (
    <InputFieldsBox>
     
     {open && (
        <StudentPerformanceModal
        
          open={open}
          handleClose={handleClose}
          studentId={studentId}
          studentName={studentName}
         
         
        />
      )}
      
      <Header variant="h4" component="h2">
              MY STUDENTS
            </Header>
            <StyledTableContainer component={Paper} sx={{ height: 350, }}>
            {dataLoaded && <StripedDataGrid
        rows={data.studentRecords}
        columns={columns}
        getRowId={(row) => row.fullName}
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
export default StudentsList;
