import { Delete, Edit } from "@mui/icons-material";
import { left } from "@popperjs/core";
import { MDBInput } from "mdb-react-ui-kit";
import React, { useState } from "react";
import { Button, Col, Modal, Row } from "react-bootstrap";
import { Clock } from "react-bootstrap-icons";
import Card from "react-bootstrap/Card";
import { useNavigate } from "react-router-dom";
import defaultImage from "../../images/default.png";
import priceImage from "../../images/price.png";
import authService from "../../service/auth.service";
import "./RestaurantCard.css";

import menuService from "../../service/menu.service";

function MenuItem({
  menuItem,
  grid = true,
  setOrderList,
  orderList,
  setMenuItems,
  menuItems,
  setMenuItem,
  setLoading,
  alert,
  setAlert,
  showAlert,
  setShowAlert,
}) {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const [editOpen, setEditOpen] = useState(false);
  const [value, setValue] = useState(0);
  const user = authService.getCurrentUser();
  const addToOrder = (e) => {
    console.log(menuItem);
    if (value <= 0) return;
    var found = false;
    var orderListCopy = JSON.parse(JSON.stringify(orderList));
    for (var i = 0; i < orderListCopy.length; i++) {
      if (orderListCopy[i].menuItem.id == menuItem.id) {
        orderListCopy[i].count += parseInt(value);
        found = true;
        break;
      }
    }
    if (!found) {
      orderListCopy.push({ menuItem: menuItem, count: parseInt(value) });
    }
    setOrderList(orderListCopy);
  };

  const handleDeleteModal = () => {
    handleDelete();
    handleClose();
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const handleDelete = () => {
    setLoading(true);
    menuService.deleteMenuItem(menuItem.id).then((res) => {
      if (res.status == 200) {
        const updatedMenuItems = menuItems.filter((item) => item !== menuItem);
        setLoading(false);
        setMenuItems(updatedMenuItems);
        setAlert({
          ...alert,
          msg: "Successfully deleted a menu item!",
          type: "success",
        });
        setShowAlert(true);
      } else {
        setAlert({
          ...alert,
          msg: res.data,
          type: "error",
        });
        setShowAlert(true);
      }
    });
  };

  return (
    <div>
      <Modal show={showModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to delete this menu item</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteModal}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>

      <Card
        style={{
          width: "100%",
          height: "10rem",
          overflow: "hidden",
          backgroundColor: "#D9D9D9",
          padding: 0,
        }}
      >
        <Row>
          <Col className={grid ? "col-5" : "col-4 "}>
            <Card.Img
              variant="top"
              src={menuItem.image ? `${menuItem.image} ` : defaultImage}
              style={{ objectFit: "cover", height: "10rem", width: "100%" }}
            />
          </Col>
          <Col className="col-7 p-0">
            <Card.Body className="p-2">
              <Card.Title
                style={{ fontSize: "16px", fontWeight: "bold", float: left }}
              >
                {menuItem.name}
              </Card.Title>
              {user.role == "RESTAURANT_MANAGER" ? (
                <div
                  class="btn-group"
                  role="group"
                  aria-label="Basic example"
                  style={{ position: "absolute", top: 5, right: 5 }}
                >
                  <Button
                    onClick={(e) => {
                      console.log("UREDI");
                      setEditOpen(true);
                      console.log(editOpen);
                      navigate("/menu/add?menuItem=" + menuItem.id);
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
                      setShowModal(true);
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
              ) : (
                <></>
              )}
              {setOrderList != undefined && orderList != undefined ? (
                <div>
                  <div
                    style={{
                      position: "absolute",
                      top: 15,
                      right: 20,
                      display: "flex",
                    }}
                  >
                    <Row
                      style={{
                        marginBottom: "5px",
                        marginLeft: "0px",
                        height: "27px",
                      }}
                    >
                      <Col style={{ padding: "0px" }}>
                        <MDBInput
                          value={value}
                          onChange={(e) => {
                            if (e.target.value < 0) setValue(0);
                            else setValue(e.target.value);
                          }}
                          id="typeNumber"
                          type="number"
                          style={{
                            width: "70px",
                            height: "27px",
                            fontSize: "14px",
                            borderTopRightRadius: 0,
                            borderBottomRightRadius: 0,
                            boxShadow: "none",
                          }}
                        />
                      </Col>
                      <Col style={{ padding: "0px" }}>
                        <Button
                          style={{
                            width: "50px",
                            height: "26px",
                            fontSize: "16px",
                            padding: "0px",
                            borderTopLeftRadius: 0,
                            borderBottomLeftRadius: 0,
                          }}
                          onClick={(e) => addToOrder(e)}
                        >
                          Add
                        </Button>
                      </Col>
                    </Row>
                  </div>
                </div>
              ) : (
                <></>
              )}
              <Card.Text
                style={{ clear: left, fontSize: "14px", paddingTop: "10px" }}
              >
                <div style={{ color: "#606060" }}>{menuItem.description}</div>

                <br />
                <div>
                  <div
                    style={{
                      position: "absolute",
                      bottom: 23,
                      right: 5,
                      display: "flex",
                    }}
                  >
                    <Clock
                      style={{
                        verticalAlign: "middle",
                        fontSize: "18px",
                        paddingTop: "1px",
                      }}
                    ></Clock>
                    <div
                      style={{
                        paddingLeft: "2px",
                        fontWeight: "bold",
                      }}
                    >
                      Cooking time:
                    </div>
                    <div
                      style={{
                        paddingLeft: "2px",
                        color: "#606060",
                      }}
                    >
                      {menuItem.prep_time}
                    </div>
                    <div
                      style={{
                        paddingLeft: "2px",
                        color: "#606060",
                      }}
                    >
                      min
                    </div>
                  </div>
                  <div
                    style={{
                      position: "absolute",
                      bottom: 2,
                      right: 5,
                      display: "flex",
                    }}
                  >
                    <img
                      src={priceImage}
                      style={{
                        width: "22px",
                        height: "auto",
                      }}
                    />
                    {menuItem?.discount_price ? (
                      <div>
                        <del
                          style={{
                            paddingLeft: "2px",
                            fontWeight: "bold",
                          }}
                        >
                          {menuItem.price.toFixed(2)}
                        </del>
                      </div>
                    ) : (
                      <div
                        style={{
                          paddingLeft: "2px",
                          fontWeight: "bold",
                        }}
                      >
                        {menuItem.price.toFixed(2)}
                      </div>
                    )}
                    {menuItem?.discount_price ? (
                      <div
                        style={{
                          fontWeight: "bold",
                          paddingLeft: "8px",
                          color: "#fe724c",
                        }}
                      >
                        {menuItem?.discount_price.toFixed(2)}
                      </div>
                    ) : (
                      <div></div>
                    )}

                    <div
                      style={{
                        paddingLeft: "2px",
                        fontWeight: "bold",
                      }}
                    >
                      KM
                    </div>
                  </div>
                </div>
              </Card.Text>
            </Card.Body>
          </Col>
        </Row>
      </Card>
    </div>
  );
}

export default MenuItem;
