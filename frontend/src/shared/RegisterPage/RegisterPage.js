import { useState } from "react";
import SignUp from "./SignUp/SignUp";
import Login from "./Login/Login";
import Banner from "./Banner";

function RegisterPage() {
  const [page, setPage] = useState("login");
  return (
    <>
      <Banner></Banner>
      {page == "login" ? (
        <Login setPage={setPage}></Login>
      ) : (
        <SignUp setPage={setPage}></SignUp>
      )}
    </>
  );
}

export default RegisterPage;
