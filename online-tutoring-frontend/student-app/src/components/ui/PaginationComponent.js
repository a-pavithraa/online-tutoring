import { TablePagination } from "@mui/material";
import React from "react";

const PaginationComponent = React.memo((props)=>{
    const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(props.rowsPerPage??5);
  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };
  return (
    <TablePagination
        rowsPerPageOptions={[5,10,20]}
        component="div"
        count={props.count}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />

  );
});

export default PaginationComponent;