import { Grid, Typography } from "@mui/material";
import React, { useContext } from "react";
import { useQuery } from "react-query";
import {
  InputFieldsBox,
  StyledTableCell,
  StyledTableRow,
} from "../components/ui/Theme";
import httpClient from "../components/util/http-client";
import AuthContext from "../store/auth-context";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";

import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";

import { Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

const classesJson = [
  {
    id: "1",
    grade: "VI",
    subject: "Math",
    students: "View Students",
    nextAssessment: "16/08/2022",
  },
  {
    id: "2",
    grade: "VI",
    subject: "Math",
    students: "View Students",
    nextAssessment: "16/08/2022",
  },
  {
    id: "3",
    grade: "VI",
    subject: "Math",
    students: "View Students",
    nextAssessment: "16/08/2022",
  },
  {
    id: "4",
    grade: "VI",
    subject: "Math",
    students: "View Students",
    nextAssessment: "16/08/2022",
  },
];
async function fetchClassesMapped(id) {
  const res = await httpClient.get(
    "/mapping/gradeAndSubjectsOfTeacher?teacherId=" + id
  );
  return res.data;
}



export const Classes = (props) => {
  const context = useContext(AuthContext);
  const navigate = useNavigate();
  
  const { status, data, error, isFetching } = useQuery(
    "classesOfTeacher",
    () => fetchClassesMapped(context.teacherId),
    {
      refetchOnWindowFocus: false,
    }
  );
 
  const displayStudents = (subjectId,gradeId)=>{
    context.setSelectedMenu(1);
    navigate("/Students?teacherId="+context.teacherId+'&subjectId='+subjectId+'&gradeId='+gradeId);
  }
  return (
    <InputFieldsBox>
      <TableContainer component={Paper} sx={{ maxWidth: 650 }}>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Subject</StyledTableCell>
              <StyledTableCell>Grade</StyledTableCell>
              <StyledTableCell>&nbsp;</StyledTableCell>
              <StyledTableCell>&nbsp;</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data &&
              data.map((row) => {
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
                      <Button variant="contained" color="success">
                        Schedule
                      </Button>
                    </StyledTableCell>
                  </StyledTableRow>
                );
              })}
          </TableBody>
        </Table>
      </TableContainer>
    </InputFieldsBox>
  );
};
