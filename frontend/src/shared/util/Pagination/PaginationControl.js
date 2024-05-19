import React from "react";
import { PaginationControl } from "react-bootstrap-pagination-control";
import "./Pagination.css";
export default ({ page, setPage, total, limit }) => {
  return (
    <PaginationControl
      page={page}
      between={3}
      total={total}
      limit={limit}
      changePage={(page) => {
        setPage(page);
      }}
      ellipsis={3}
    />
  );
};
