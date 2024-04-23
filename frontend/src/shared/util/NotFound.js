import React from "react";
import graphic from "../../images/delivery_boy_on_scooter.svg";
import Header from "./Header";

function NotFound({ header = true }) {
  return (
    <>
      {header ? <Header></Header> : <></>}
      <div style={{ display: "flex", justifyContent: "center" }}>
        <img src={graphic} style={{ marginTop: "60px" }} />

        <div style={{ display: "inline", marginTop: "200px" }}>
          <h1 style={{ fontSize: "100px", color: "white" }}>404</h1>

          <h2 style={{ color: "white" }}>Sorry, can't find this page...</h2>
        </div>
      </div>
    </>
  );
}

export default NotFound;
