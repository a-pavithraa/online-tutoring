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
import { InputFieldsBox, StyledTableCell, StyledTableRow } from "../ui/Theme";
import httpClient from "../util/http-client";
import useQueryParam from "../util/queryparam-hook";
async function fetchStudentDetails(teacherId, gradeId, subjectId) {
  let gradeIdquery = gradeId? "&gradeId="+gradeId :'';
  let subjectIdQuery = subjectId?"&subjectId="+subjectId:'';
  const res = await httpClient.get(
    "/mapping/studentsOfTeacher?teacherId=" +
      teacherId +
      gradeIdquery
   +subjectIdQuery
  );
  return res.data;
}
const StudentsList = (props) => {
  const queryParam = useQueryParam();
  const {teacherId}=useContext(AuthContext);
 
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
  return (
    <InputFieldsBox>
   
      <TableContainer component={Paper} sx={{ maxWidth: 650 }}>
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Name</StyledTableCell>
              <StyledTableCell>E-Mail</StyledTableCell>
              <StyledTableCell>Parent Name</StyledTableCell>
              <StyledTableCell>Contact No</StyledTableCell>
              <StyledTableCell>Address</StyledTableCell>
            
            </TableRow>
          </TableHead>
          <TableBody>
            {isFetching && <CircularProgress />}
            {data &&
              data.map((row) => (
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
                </StyledTableRow>
             ))}
             {!isFetching && (!data || data.length==0) && <StyledTableRow>
              <StyledTableCell colSpan={5}>No Students found</StyledTableCell>
              </StyledTableRow>}
          </TableBody>
        </Table>
      </TableContainer>
    </InputFieldsBox>
  );
};
export default StudentsList;
