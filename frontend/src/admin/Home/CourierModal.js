import * as React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import { useState, useEffect } from "react";
import DialogTitle from "@mui/material/DialogTitle";
import { DialogContentText } from "@mui/material";
import DialogContent from "@mui/material/DialogContent";
import TextField from "@mui/material/TextField";
import Form from "react-bootstrap/Form";
import {
  Col,
  ModalBody,
  ModalFooter,
  ModalTitle,
  Row,
  Modal,
  FormControl,
  FormGroup,
} from "react-bootstrap";
import LocationOn from "@mui/icons-material/LocationOn";
import MapModal from "../../shared/MapModal/MapModal";
import userService from "../../service/user.service";
import restaurantService from "../../service/restaurant.service";

function CourierModal({ show, setShow, couriers, setCouriers }) {
  const [newCourier, setnewCourier] = useState({
    firstname: "",
    lastname: "",
    username: "",
    email: "",
    password: "",
    address:"",
    mapCoordinates: ""
  });
  const [showMap, setShowMap] = useState(false);

  const setAddress = (addr, coordinates) => {
    setnewCourier({
      ...newCourier,
      address: addr,
      mapCoordinates: coordinates,
    });
  };

  const handleClose = () => {
    setShow(false);
  };

  const handleCreate = () => {
    setCouriers((oldArray) => [...oldArray, newCourier]); // privremeno da doda dok se ne povuce iz baze ponovo
    console.log("Ulazim u servis");
    userService.addCourier(newCourier).then((res) => {
      console.log(res.data);
    });
    setShow(false);
  };

  return (
    <div>
      <Modal show={show} onClose={handleClose}>
        <Modal.Header>
          <ModalTitle>Add a new courier</ModalTitle>
        </Modal.Header>
        <ModalBody>
          <Form>
            <Row>
              <Form.Group className="mb-3">
                <Form.Label>Courier firstname</Form.Label>
                <Form.Control
                  type="text"
                  autoFocus
                  margin="normal"
                  id="firstname"
                  label="Courier firstname"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewCourier((current) => ({
                      ...current,
                      ...{ firstname: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Courier lastname</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="lastname"
                  label="Courier lastname"
                  type="text"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewCourier((current) => ({
                      ...current,
                      ...{ lastname: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
            </Row>
            <Row>
              <Form.Group className="mb-3">
                <Form.Label>Courier username</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="username"
                  label="Courier username"
                  type="text"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewCourier((current) => ({
                      ...current,
                      ...{ username: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Courier email</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="email"
                  label="Courier email"
                  type="email"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewCourier((current) => ({
                      ...current,
                      ...{ email: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Courier phone number</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="phoneNumber"
                  label="Manager phone number"
                  type="number"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewCourier((current) => ({
                      ...current,
                      ...{ phoneNumber: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
            </Row>
            <Row>
              <Form.Group className="mb-3">
                <Form.Label>Courier password</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="password"
                  label="Manager password"
                  type="password"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewCourier((current) => ({
                      ...current,
                      ...{ password: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
            </Row>
            <Form.Group className="mb-3" controlId="formGroupAddress">
              <Form.Label>Address </Form.Label>
              <Row>
                <Col xs={11}>
                  <Form.Control
                    readonly
                    disabled
                    type="text"
                    name="address"
                    value={newCourier.address}
                  />
                </Col>
                <Col xs={1} className="p-0">
                  <LocationOn
                    className="clickable"
                    onClick={() => setShowMap(true)}
                  ></LocationOn>
                </Col>
              </Row>
            </Form.Group>
            <MapModal
              title="Enter courier address"
              show={showMap}
              setShow={setShowMap}
              setAddress={setAddress}
            ></MapModal>
          </Form>
        </ModalBody>
        <ModalFooter>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleCreate}>Create</Button>
        </ModalFooter>
      </Modal>
    </div>
  );
}
export default CourierModal;
