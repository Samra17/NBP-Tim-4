import React, { useEffect } from "react";
import "./RightSideContainer.css";

function MainContainer({ collapsed, children }) {
  let initialState = false;

  useEffect(() => {
    initialState = collapsed;
  }, []);

  useEffect(() => {
    setTimeout(function () {
      var element = document.getElementById("main-container");

      if (!collapsed) {
        element.classList.remove("container-side-collapsed");
        element.classList.add("container-side-expanded");
      } else {
        element.classList.remove("container-side-expanded");
        element.classList.add("container-side-collapsed");
      }
    }, 100);
  }, [collapsed]);

  return (
    <div
      id="main-container"
      className={
        initialState
          ? "container-side-collapsed main-container"
          : "container-side-expanded main-container"
      }
    >
      <div
        style={{
          margin: "auto",
          padding: 0,
          width: "85%",
          backgroundColor: "#f1f1f1",
          borderRadius: "5px",
        }}
      >
        {children}
      </div>
    </div>
  );
}

export default MainContainer;
