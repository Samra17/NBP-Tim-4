import React from "react";
import { Row, Col } from "react-bootstrap";
import { Restaurant } from "@mui/icons-material";

function Banner() {
  return (
    <div
      style={{
        width: "40%",
        textAlign: "center",
        margin: "auto",
        paddingTop: "10%",
      }}
    >
      <h2
        className="fw-bold mb-2 text-center text-white"
        style={{ display: "inline-flex" }}
      >
        <span>
          <Restaurant
            style={{
              color: "white",
              width: "100px",
              width: "100%",
              height: "40px",
              paddingRight: "10px",
            }}
          ></Restaurant>
        </span>
        The Convenient Foodie
      </h2>
    </div>
  );
}

export default Banner;
