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

function RestaurantModal({ show, setShow, restaurants, setRestaurants }) {
  const [newManager, setnewManager] = useState({
    firstname: "",
    lastname: "",
    username: "",
    email: "",
    password: "",
  });
  const [newRestaurant, setnewRestaurant] = useState({
    name: "",
    address: "",
    mapCoordinates: "",
    manager: "",
    categories: [],
    rating: 0,
    customersFavorited: 0,
    managerUUID: "",
  });
  const [showMap, setShowMap] = useState(false);

  const setAddress = (addr, coordinates) => {
    setnewRestaurant({
      ...newRestaurant,
      address: addr,
      mapCoordinates: coordinates,
    });
  };

  const handleClose = () => {
    setShow(false);
  };

  const handleCreate = () => {
    let restoran = newRestaurant;
    restoran = {
      ...restoran,
      ...{ manager: newManager.firstname + " " + newManager.lastname },
    };

    setRestaurants((oldArray) => [...oldArray, restoran]); // privremeno da doda dok se ne povuce iz baze ponovo

    
    console.log("Ulazim u servis");
    userService.addManager(newManager).then((res) => {

      console.log(res.data);
      let rest = newRestaurant;
      rest = { ...rest, ...{ managerUUID: res.data.user.uuid } };

      console.log(rest);
      setShow(false);
      restaurantService.addRestaurant(rest).then((resz) => {
        console.log("Uspjesno dodan restoran za managera");
        console.log(resz.data);
      });
    });
  };

  return (
    <div>
      <Modal show={show} onClose={handleClose}>
        <Modal.Header>
          <ModalTitle>Add a new restaurant</ModalTitle>
        </Modal.Header>
        <ModalBody>
          <Form>
            <Row>
              <Form.Group className="mb-3">
                <Form.Label>Manager firstname</Form.Label>
                <Form.Control
                  type="text"
                  autoFocus
                  margin="normal"
                  id="firstname"
                  label="Manager firstname"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewManager((current) => ({
                      ...current,
                      ...{ firstname: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Manager lastname</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="lastname"
                  label="Manager lastname"
                  type="text"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewManager((current) => ({
                      ...current,
                      ...{ lastname: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
            </Row>
            <Row>
              <Form.Group className="mb-3">
                <Form.Label>Manager username</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="username"
                  label="Manager username"
                  type="text"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewManager((current) => ({
                      ...current,
                      ...{ username: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Manager email</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="email"
                  label="Manager email"
                  type="email"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewManager((current) => ({
                      ...current,
                      ...{ email: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
            </Row>
            <Row>
              <Form.Group className="mb-3">
                <Form.Label>Manager password</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="password"
                  label="Manager password"
                  type="password"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewManager((current) => ({
                      ...current,
                      ...{ password: e.target.value },
                    }));
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3">
                <Form.Label>Restaurant name</Form.Label>
                <FormControl
                  autoFocus
                  margin="normal"
                  id="name"
                  label="Restaurant name"
                  type="text"
                  fullWidth
                  variant="standard"
                  onChange={(e) => {
                    setnewRestaurant((current) => ({
                      ...current,
                      ...{ name: e.target.value },
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
                    value={newRestaurant.address}
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
              title="Enter restaurant address"
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
export default RestaurantModal;
