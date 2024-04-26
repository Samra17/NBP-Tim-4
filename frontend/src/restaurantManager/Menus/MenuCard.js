import { Delete, Edit } from "@mui/icons-material";
import React, { useState } from "react";
import { Button, ButtonGroup, Col, Modal, Row } from "react-bootstrap";
import Card from "react-bootstrap/Card";
import { useNavigate } from "react-router-dom";
import "./MenuCard.css";

import { left } from "@popperjs/core";
import menuService from "../../service/menu.service";

function MenuCard({
  menu,
  menus,
  setMenus,
  alert,
  setAlert,
  showAlert,
  setShowAlert,
  setLoading,
}) {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const handleDeleteModal = () => {
    handleDelete();
    handleClose();
  };

  const handleClose = () => {
    setShowModal(false);
  };

  const handleEdit = () => {
    navigate("/menu/add?id=" + menu.id);
  };

  const handleDelete = () => {
    setLoading(true);
    menuService.deleteMenu(menu.id).then((res) => {
      if (res.status == 200) {
        document.body.style.cursor = "default";
        setAlert({
          ...alert,
          type: "success",
          msg: "Successfully deleted menu!",
        });
        setShowAlert(true);
        console.log(alert);
        console.log(showAlert);
        setMenus(menus.filter((m) => m.id != menu.id));
      } else {
        setAlert({ ...alert, type: "error", msg: res.data });
        setShowAlert(true);
      }
      setLoading(false);
    });

    console.log(menu.id);
  };

  return (
    <div>
      <Modal show={showModal} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to delete this menu</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="danger" onClick={handleDeleteModal}>
            Delete
          </Button>
        </Modal.Footer>
      </Modal>
      {menu ? (
        <Card
          onClick={() => console.log(menu.id)}
          style={{
            width: "100%",
            height: "7rem",
            overflow: "hidden",
            backgroundColor: "#D9D9D9",
          }}
          className="px-3"
        >
          <Row>
            <Col className="col-7 p-0">
              <Card.Body className="p-2">
                <Card.Title
                  style={{
                    fontSize: "20px",
                    fontWeight: "bold",
                    float: left,
                    paddingBottom: "2%",
                  }}
                >
                  {" "}
                  {menu.name}
                </Card.Title>
                <Card.Text style={{ clear: left, fontSize: "16px" }}>
                  <div style={{ display: "flex" }}>
                    {menu.active ? (
                      <div
                        style={{
                          color: "#106719",
                        }}
                      >
                        Active
                      </div>
                    ) : (
                      <div
                        style={{
                          color: "#FE724C",
                        }}
                      >
                        Inactive
                      </div>
                    )}

                    <ButtonGroup
                      style={{
                        position: "absolute",
                        bottom: "5%",
                        right: "2%",
                      }}
                    >
                      <Button
                        onClick={handleEdit}
                        type="button"
                        className="mb-3"
                        style={{ width: "fit-content" }}
                      >
                        Edit <Edit fontSize="small"></Edit>
                      </Button>

                      <Button
                        type="button"
                        className="mb-3"
                        style={{
                          width: "fit-content",
                          backgroundColor: "#fe724c",
                          border: "#fe724c",
                        }}
                        onClick={(e) => {
                          e.preventDefault();
                          e.stopPropagation();
                          setShowModal(true);
                        }}
                      >
                        Delete <Delete fontSize="small"></Delete>
                      </Button>
                    </ButtonGroup>
                  </div>
                </Card.Text>
              </Card.Body>
            </Col>
          </Row>
        </Card>
      ) : (
        <></>
      )}
    </div>
  );
}

export default MenuCard;
