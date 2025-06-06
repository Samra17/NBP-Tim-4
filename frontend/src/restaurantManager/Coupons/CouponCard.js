import { Delete } from "@mui/icons-material";
import { left } from "@popperjs/core";
import React, { useState } from "react";
import { Button, Col, Modal, Row } from "react-bootstrap";
import Card from "react-bootstrap/Card";
import authService from "../../service/auth.service";
import discountService from "../../service/discount.service";
import "./CouponCard.css";

function CouponCard({ coupon, coupons, setCoupons }) {
  const [showModal, setshowModal] = useState(false);

  const deleteCoupon = () => {
    setshowModal(false);
    setCoupons((current) => current.filter((coup) => coup.id !== coupon.id));
    discountService.deleteCoupon(coupon.id).then((res) => {
      if (res.status == 200) {
        const updatedItems = coupons.map(item =>
          item.id === coupon.id ? { ...item, quantity: 0 } : item
        );
        setCoupons(updatedItems)
      } else console.log(res);
    });
  };
  const closeModal = () => {
    setshowModal(false);
  };

  const openModal = () => {
    setshowModal(true);
  };

  return (
    <>
      {coupon ? (
        <div>
          <Modal show={showModal} onHide={closeModal}>
            <Modal.Header closeButton>
              <Modal.Title>Confirmation</Modal.Title>
            </Modal.Header>
            <Modal.Body>Are you sure you want to deactivate this coupon?</Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={closeModal}>
                Cancel
              </Button>
              <Button variant="danger" onClick={deleteCoupon}>
                Deactivate
              </Button>
            </Modal.Footer>
          </Modal>
          <Card
            style={{
              width: "100%",
              height: "10rem",
              overflow: "hidden",
              backgroundColor: "#D9D9D9",
            }}
            className="box"
          >
            <Row>
              <Col className="col-7 p-0">
                <Card.Body className="p-2">
                  <Card.Title
                    style={{
                      fontSize: "20px",
                      fontWeight: "bold",
                      float: left,
                      paddingBottom: "8%",
                    }}
                  >
                    Coupon code: {coupon.code}
                  </Card.Title>
                  <Card.Text style={{ clear: left, fontSize: "16px" }}>
                    Discount percentage: {coupon.discountPercent}%
                    <br />
                    Amount left: {coupon.quantity}
                    <div
                      style={{
                        position: "absolute",
                        bottom: "5%",
                        right: "5%",
                      }}
                    > {coupon.quantity > 0 ?
                      <Button
                        onClick={(e) => {
                          openModal();
                          e.stopPropagation();
                        }}
                        style={{
                          backgroundColor: "#fe724c",
                          borderColor: "#fe724c",
                          color: "white",
                        }}
                        class="rounded"
                      >
                        {" "}
                        Deactivate<Delete fontSize="small"></Delete>
                      </Button> : <></>}
                    </div>
                  </Card.Text>
                </Card.Body>
              </Col>
            </Row>
          </Card>
        </div>
      ) : (
        <></>
      )}
    </>
  );
}

export default CouponCard;
