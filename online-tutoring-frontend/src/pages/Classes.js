import { Grid, Typography } from "@mui/material";
import React, { useContext, useState } from "react";
import { useQuery } from "react-query";
import {
  Header,
  InputFieldsBox,
  StyledTableCell,
  StyledTableContainer,
  StyledTableRow,
} from "../components/ui/Theme";
import httpClient from "../util/http-client";
import UIContext from "../store/ui-context";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";

import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";

import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import LoginContext from "../store/login-context";
import ScheduleAssessmentModal from "../components/assessment/ScheduleAssessmentModal";
import PaginationComponent from "../components/ui/PaginationComponent";


async function fetchClassesMapped(id) {
  const res = await httpClient.get(
    "/mdm/mapping/gradeAndSubjectsOfTeacher?teacherId=" + id
  );
  return res.data;
}



export const Classes = (props) => {
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
 const dataLoaded= data && data.gradeAndSubjectMappingRecords;
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
       <StyledTableContainer component={Paper} sx={{ maxWidth: "100%" }}>
      
        <Table stickyHeader  aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Subject</StyledTableCell>
              <StyledTableCell>Grade</StyledTableCell>
              <StyledTableCell>Students</StyledTableCell>
              <StyledTableCell>Assessment</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {dataLoaded &&
              data.gradeAndSubjectMappingRecords.map((row) => {
                return (
                  <StyledTableRow
                    key={`${row.subjectName}-${row.gradeName}`}
                    sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                  >
                    <StyledTableCell>{row.subjectName}</StyledTableCell>
                    <StyledTableCell>{row.gradeName}</StyledTableCell>
                    <StyledTableCell>
                      <Button variant="contained" color="primary" onClick={()=>displayStudents(row.subjectId,row.gradeId)}>
                        View Students
                      </Button>
                    </StyledTableCell>

                    <StyledTableCell>
                      <Button variant="contained" color="success" onClick={()=>openEditModal(row.gradeId, row.subjectId)}>
                        Schedule Assessment
                      </Button>
                    </StyledTableCell>
                  </StyledTableRow>
                );
              })}
          </TableBody>
        </Table>
      
      </StyledTableContainer>
      {dataLoaded && <PaginationComponent       
        count={data.gradeAndSubjectMappingRecords.length}
        rowsPerPage={5}
       
      />
}
    </InputFieldsBox>
  );
};
