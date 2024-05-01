import React from "react";
import {
  Button,
  ButtonGroup,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  ModalTitle,
} from "react-bootstrap";
import { useState } from "react";
import { Form } from "react-bootstrap";
import StarRatings from "react-star-ratings";
import restaurantService from "../../service/restaurant.service";
import userService from "../../service/user.service";
import tokenService from "../../service/token.service";

function AddReviewPopup({
  show,
  setShow,
  restaurantId,
  setAlert,
  setShowAlert,
}) {
  const [rating, setRating] = useState(1);
  const [comment, setComment] = useState();

  const submit = () => {
    if (comment && comment.length == 0)
      var req = {
        restaurantId: restaurantId,
        rating: rating,
      
      };
    else
      var req = {
        restaurantId: restaurantId,
        rating: rating,
        comment: comment,
        userId: tokenService.getUserId()
      };
    document.body.style.cursor = "wait";
    restaurantService.addReview(req).then((res) => {
      document.body.style.cursor = "default";
      if (res.status == 201) {
        setAlert({
          msg: "Successfully left a review for this restaurant!",
          type: "success",
        });
        setShowAlert(true);
      } else {
        setAlert({ msg: res.data, type: "error" });
        setShowAlert(true);
      }
      setShow(false);
    });
  };

  return (
    <Modal show={show}>
      <ModalHeader>
        <ModalTitle>Leave a review</ModalTitle>
      </ModalHeader>
      <ModalBody>
        <div style={{ textAlign: "center" }}>
          <StarRatings
            numberOfStars={5}
            rating={rating}
            isSelectable={true}
            changeRating={(v) => setRating(v)}
            starRatedColor="#FE724C"
          />
        </div>
        <Form className="mt-5">
          <Form.Control
            as="textarea"
            rows={4}
            value={comment}
            onChange={(v) => setComment(v.target.value)}
            placeholder="Leave a comment"
          ></Form.Control>
        </Form>
      </ModalBody>
      <ModalFooter>
        <ButtonGroup>
          <Button
            onClick={() => setShow(false)}
            style={{
              width: "fit-content",
            }}
          >
            Cancel{" "}
          </Button>
          <Button
            onClick={submit}
            style={{
              width: "fit-content",
              backgroundColor: "#fe724c",
              borderColor: "#fe724c",
            }}
          >
            Submit
          </Button>
        </ButtonGroup>
      </ModalFooter>
    </Modal>
  );
}

export default AddReviewPopup;
