import React from "react";
import Card from "react-bootstrap/Card";
import { Col, Row, Button, Container } from "react-bootstrap";
import defaultImage from "../../images/default.png";
import StarRatings from "react-star-ratings";
import { HeartFill } from "react-bootstrap-icons";
import { Add, Edit, Delete } from "@mui/icons-material";
import "./RestaurantCard.css";
import { right, left } from "@popperjs/core";
import authService from "../../service/auth.service";
import { useNavigate } from "react-router-dom";

function RestaurantCard({ res, grid = true }) {
  const user = authService.getCurrentUser();
  const navigate = useNavigate();

  return (
    <>
      {res ? (
        <Card
          onClick={() => navigate("/customer/restaurant?id=" + res.id)}
          style={{
            width: "100%",
            height: "10rem",
            overflow: "hidden",
            backgroundColor: "#D9D9D9",
            padding:0
          }}
          className="box"
        >
          <Row>
            <Col className={grid ? "col-5 " : "col-3 "}>
              <Card.Img
                variant="top"
                src={res.logo ? `${res.logo} ` : defaultImage}
                style={{ objectFit: "cover", height: "10rem", width: "100%" }}
              />
            </Col>
            <Col className="col-7 p-0">
              <Card.Body className="p-2">
                <Card.Title
                  style={{ fontSize: "16px", fontWeight: "bold", float: left }}
                >
                  {res.name}
                </Card.Title>
                {user.role == "ADMINISTRATOR" ? (
                  <Container
                    style={{
                      display: "flex",
                      justifyContent: "flex-end",
                      alignItems: "flex-end",
                      backgroundColor: "#D9D9D9",
                      height: "40px",
                      marginBottom: 0,
                      marginTop: 0,
                      marginRight: 0,
                      padding: 10,
                    }}
                  >
                    <div
                      class="btn-group"
                      role="group"
                      aria-label="Basic example"
                    >
                      <Button
                        onClick={(e) => {
                          console.log("UREDI");
                          e.stopPropagation();
                        }}
                        style={{
                          clear: left,
                          textAlign: "center",
                          width: "45px",
                          height: "30px",
                          margin: 2,
                          marginRight: 0,
                          padding: 0,
                        }}
                        class="rounded"
                      >
                        <Edit fontSize="small"></Edit>
                      </Button>
                      <Button
                        onClick={(e) => {
                          console.log("BRISI");
                          e.stopPropagation();
                        }}
                        style={{
                          clear: left,
                          textAlign: "center",
                          width: "45px",
                          height: "30px",
                          margin: 2,
                          padding: 0,
                          marginRight: 5,
                          marginLeft: 0,
                          backgroundColor: "#fe724c",
                          borderColor: "#fe724c",
                        }}
                        class="rounded"
                      >
                        <Delete fontSize="small"></Delete>
                      </Button>
                    </div>
                  </Container>
                ) : (
                  <></>
                )}
                <Card.Text style={{ clear: left, fontSize: "14px" }}>
                  {res.address}
                  <br />
                  {res.categories!=null && res.categories.length>0 ?
                  <>
                  <strong>Categories: </strong> 
                  {res.categories[0] + " "}
                  {res.categories.length > 1 ? "and " : ""}
                  {res.categories.length > 1 ? (
                    <u>{(res.categories.length - 1).toString() + " more"}</u>
                  ) : (
                    ""
                  )}</> : <></> }
                  <br />
                  {res.open ? (
                    <strong className="text-success">OPEN</strong>
                  ) : (
                    <strong className="text-danger">CLOSED</strong>
                  )}
                  <br />
                  <div style={{ position: "absolute", bottom: 2 }}>
                    <StarRatings
                      rating={res.rating ? res.rating : 0}
                      starRatedColor="#FE724C"
                      numberOfStars={5}
                      name="rating"
                      starDimension="20px"
                      starSpacing="2px"
                      starEmptyColor="white"
                    />
                    <span style={{ color: "#FE724C", verticalAlign: "middle" }}>
                      {" "}
                      {res.rating ? res.rating : 0}{" "}
                    </span>
                    <span style={{ color: "grey", verticalAlign: "middle" }}>
                      {"(" + res.customersRated + ")"}
                    </span>
                  </div>
                  <div style={{ position: "absolute", bottom: 2, right: 5 }}>
                    <HeartFill
                      style={{ color: "#fe724c", verticalAlign: "middle" }}
                    ></HeartFill>
                    <span style={{ color: "grey", verticalAlign: "middle" }}>
                      {" "}
                      {res.customersFavorited}
                    </span>
                  </div>
                </Card.Text>
              </Card.Body>
            </Col>
          </Row>
        </Card>
      ) : (
        <></>
      )}
    </>
  );
}

export default RestaurantCard;
