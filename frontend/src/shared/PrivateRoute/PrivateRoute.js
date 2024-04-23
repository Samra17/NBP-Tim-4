import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import AuthService from "../../service/auth.service";
import Header from "../util/Header";

const PrivateRoute = (props) => {
  var user = AuthService.getCurrentUser();

  return user ? (
    <>
      <Header></Header>
      <Outlet props={props} />
    </>
  ) : (
    <Navigate to="/register"></Navigate>
  );
};

export default PrivateRoute;
