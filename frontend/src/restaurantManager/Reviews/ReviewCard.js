import React from "react";
import { Card, Col, Row } from "react-bootstrap";
import StarRatings from "react-star-ratings";
import dateFormat from "dateformat";

function ReviewCard({ review, grid }) {
  return (
    <Card
      style={{
        width: "100%",
        minHeight: "55px",
        backgroundColor: "#D9D9D9",
      }}
    >
      <Card.Body>
        <Row>
          <div
            style={{
              position: "absolute",
              top: 10,
              left: 15,
              display: "inline",
            }}
            className="col-10"
          >
            <StarRatings
              rating={review.rating}
              starRatedColor="#FE724C"
              numberOfStars={5}
              name="rating"
              starDimension="30px"
              starSpacing="2px"
              starEmptyColor="white"
            />
            <span
              style={{
                color: "#FE724C",
                verticalAlign: "middle",
                fontSize: "22px",
                fontWeight: "bold",
              }}
            >
              {" "}
              {review.rating}
            </span>
          </div>
          <div className="col-2" style={{position:"absolute",right:0}}>
            {dateFormat(review.created, "dd/mm/yyyy")}
          </div>
        </Row>
        {review.comment ? (
          <Row className="px-3" style={{marginTop:"35px"}}>
            <span style={{ color: "#272D2F" }}>{review.comment}</span>
          </Row>
        ) : (
          <></>
        )}
      </Card.Body>
    </Card>
  );
}

export default ReviewCard;
