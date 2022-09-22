import React from "react";
import httpClient from "../util/http-client";
import { useQuery } from "react-query";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { styled } from "@mui/system";
import { Button } from "@mui/material";
import TeacherMappingModal from "./TeacherMappingModal";
import { InputFieldsBox, StyledTableCell, StyledTableRow } from "../ui/Theme";
async function getTeachers() {
  const res = await httpClient.get("/mdm/admin/teachers");
  return res.data;
}


const Teachers = (props) => {
  const [open, setOpen] = React.useState(false);
  const [selectedTeacherId, setSelectedTeacherId] = React.useState();

  const handleClose = () => {
    setOpen(false);
  };
  const openEditModal = (id) => {
    setSelectedTeacherId(id);
    setOpen(true);
  };
  const {
    status: teachersFetchedStatus,
    data: teachers,
    error: error,
    isFetching: isTeachersFetching,
  } = useQuery("teachers", getTeachers, { refetchOnWindowFocus: false });
  return (
    <InputFieldsBox>
      <TableContainer
        component={Paper}
        sx={{ maxWidth: 650, marginTop: 20 }}
      >
        <Table aria-label="simple table">
          <TableHead>
            <TableRow>
              <StyledTableCell>ID</StyledTableCell>
              <StyledTableCell>NAME</StyledTableCell>
              <StyledTableCell>EMAIL</StyledTableCell>
              <StyledTableCell>&nbsp;</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {teachers &&
              teachers.map((row) => {
                return (
                  <StyledTableRow
                    key={row.name}
                    sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
                  >
                    <StyledTableCell component="th" scope="row">
                      {row.id}
                    </StyledTableCell>
                    <StyledTableCell>{row.name}</StyledTableCell>
                    <StyledTableCell>{row.email}</StyledTableCell>
                    <StyledTableCell>
                      {" "}
                      <Button
                        variant="contained"
                        color="primary"
                        onClick={() => openEditModal(row.id)}
                      >
                        View/Edit
                      </Button>
                    </StyledTableCell>
                  </StyledTableRow>
                );
              })}
            {open && (
              <TeacherMappingModal
                open={open}
                handleClose={handleClose}
                id={selectedTeacherId}
              />
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </InputFieldsBox>
  );
};
export default Teachers;
