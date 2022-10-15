import React, { useContext, useState } from "react";
import { useQuery } from "react-query";
import {
  Header,
  InputFieldsBox,
  StyledTableContainer,
} from "../ui/Theme";
import httpClient from "../../util/http-client";
import UIContext from "../../store/ui-context";

import Paper from "@mui/material/Paper";

import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import LoginContext from "../../store/login-context";
import ScheduleAssessmentModal from "../assessment/ScheduleAssessmentModal";
import { StripedDataGrid } from "../ui/DataGridCustomization";
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import PersonSearchIcon from '@mui/icons-material/PersonSearch';

async function fetchClassesMapped(id) {
  const res = await httpClient.get(
    "/mdm/mapping/gradeAndSubjectsOfTeacher?teacherId=" + id
  );
  return res.data;
}



export const ClassesList = (props) => {
  const context = useContext(LoginContext);
  const uiContext = useContext(UIContext);
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [selectedGradeId,setSelectedGradeId]=useState();
  const [selectedSubjectId,setSelectedSubjectId]=useState();

  const handleClose = () => {
    setOpen(false);
  };
  const openEditModal = (gradeId,subjectId) => {
    setSelectedGradeId(gradeId);
    setSelectedSubjectId(subjectId);
    setOpen(true);
  };
  
  const {  data } = useQuery(
    "classesOfTeacher",
    () => fetchClassesMapped(context.teacherId),
    {
      refetchOnWindowFocus: false,
    }
  );
  const columns = [
   
  
    {
      field: 'subjectName',
      headerName: 'Subject',
      headerClassName: 'DataGrid-Header',
      minWidth: 60,
      flex: 1
   
    },
    {
      field: 'gradeName',
      headerName: 'Grade',
      headerClassName: 'DataGrid-Header',
      minWidth: 60,
      flex: 1
     
    },
    {
      field: 'assessmentDate',
      headerName: 'Students',
      type: 'date',
      headerClassName: 'DataGrid-Header',
      minWidth: 220,
      flex: 1,
       sortable: false,
         renderCell: (params) => (    <Button  color="primary" startIcon={<PersonSearchIcon/>} onClick={()=>displayStudents(params.row.subjectId,params.row.gradeId)}>
                            View Students
                      </Button>)
    
    
     
    },
    {
      field: 'qnPaperDocument',
      headerName: 'Assessment',
      headerClassName: 'DataGrid-Header',     
      sortable: false,
      minWidth: 220,
      flex: 1,
      renderCell: (params) => (   <Button   color="success" startIcon={<CalendarMonthIcon/>}  onClick={()=>openEditModal(params.row.gradeId, params.row.subjectId)}>
                        Schedule Assessment
                      </Button>)

      
      
    },
  ];

 const dataLoaded= data && data.gradeAndSubjectMappingDetails;
  const displayStudents = (subjectId,gradeId)=>{
    uiContext.setSelectedMenu(1);
    navigate("/Students?teacherId="+context.teacherId+'&subjectId='+subjectId+'&gradeId='+gradeId);
  }
  return (
    <InputFieldsBox>
      <Header variant="h4" component="h2">
              MY CLASSES
            </Header>
     {open && <ScheduleAssessmentModal subjectId={selectedSubjectId} gradeId={selectedGradeId} open={open}
                handleClose={handleClose}/>}
       <StyledTableContainer component={Paper} sx={{ height: 350, }}>
       {dataLoaded && <StripedDataGrid
        rows={data.gradeAndSubjectMappingDetails}
        columns={columns}
        getRowId={(row) => row.subjectId+'-'+row.gradeId}
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
