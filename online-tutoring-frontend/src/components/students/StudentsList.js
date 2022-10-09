import {
  CircularProgress,
  Paper,
  Table,
  TableBody,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import React, { useContext } from "react";
import { useQuery } from "react-query";
import AuthContext from "../../store/auth-context";
import LoginContext from "../../store/login-context";
import { Header, InputFieldsBox, StyledTableCell, StyledTableRow } from "../ui/Theme";
import httpClient from "../../util/http-client";
import useQueryParam from "../../util/queryparam-hook";
import PaginationComponent from "../ui/PaginationComponent";
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
 
  const { data, isFetching,status } = useQuery(
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
  const dataLoaded =data && data.studentRecords;
  return (
    <InputFieldsBox>
   
      <TableContainer component={Paper} sx={{ maxWidth: "100%" }}>
      <Header variant="h4" component="h2">
              MY STUDENTS
            </Header>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Name</StyledTableCell>
              <StyledTableCell>E-Mail</StyledTableCell>
              <StyledTableCell>Parent Name</StyledTableCell>
              <StyledTableCell>Contact No</StyledTableCell>
              <StyledTableCell>Address</StyledTableCell>
              <StyledTableCell>Performance</StyledTableCell>
            
            </TableRow>
          </TableHead>
          <TableBody>
            {isFetching &&  <StyledTableRow><StyledTableCell colSpan={4}><CircularProgress /></StyledTableCell></StyledTableRow>}
            {dataLoaded &&
              data.studentRecords.map((row) => (
                <StyledTableRow
                  key={row.fullName}
                  sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                >
                  <StyledTableCell component="th" scope="row">
                    {row.fullName}
                  </StyledTableCell>
                  <StyledTableCell>{row.email}</StyledTableCell>
                  <StyledTableCell>{row.parentName}</StyledTableCell>
                  <StyledTableCell>{row.contactNo}</StyledTableCell>
                  <StyledTableCell>{row.address}</StyledTableCell>
                  <StyledTableCell>&nbsp;</StyledTableCell>
                </StyledTableRow>
             ))}
             {!isFetching && (!data || data.studentRecords.length==0) && <StyledTableRow><StyledTableCell colSpan={6}>No Students found</StyledTableCell></StyledTableRow>}
          </TableBody>
        </Table>
      </TableContainer>
      {dataLoaded && <PaginationComponent       
        count={data.studentRecords.length}
        rowsPerPage={5}
       
      />
}
    </InputFieldsBox>
  );
};
export default StudentsList;
