import LocationOn from "@mui/icons-material/LocationOn";
import React, { useState } from "react";
import { Col, Row } from "react-bootstrap";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import { Link, useNavigate } from "react-router-dom";
import auth from "../../../service/auth.service";
import MapModal from "../../MapModal/MapModal";
import CustomAlert from "../../util/Alert";
import styles from "./signup.css";

function SignUp({ setPage }) {
  const [formData, setFormData] = useState({});
  const [showMap, setShowMap] = useState(false);
  const [validated, setValidated] = useState(false);
  const navigate = useNavigate();
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);

  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  const setAddress = (addr, coordinates) => {
    setFormData({ ...formData, address: addr, mapCoordinates: coordinates });
  };

  const handleSubmit = (event) => {
    const form = event.currentTarget;
    event.preventDefault();
    event.stopPropagation();
    if (form.checkValidity() === false) {
      return;
    }

    setValidated(true);

    if (formData.address != null && formData.address.length == 0) {
      var req = {
        firstname: formData.firstname,
        lastname: formData.lastname,
        email: formData.email,
        username: formData.username,
        password: formData.password,
      };
      if (formData.phoneNumber != null) {
        req.phoneNumber = formData.phoneNumber;
      }
    } else {
      var req = formData;
    }
    try {
      document.body.style.cursor = "wait";
      auth.register(req).then((res) => {
        if (res.status == 201) {
          setAlert({
            ...alert,
            msg: ["You have successfully created a new account!"],
            type: "success",
          });
          setShowAlert(true);
          setTimeout(() => {
            document.body.style.cursor = "default";
            navigate("/");
          }, 5000);
        } else {
          document.body.style.cursor = "default";
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <>
      <MapModal
        show={showMap}
        setShow={setShowMap}
        setAddress={setAddress}
      ></MapModal>
      <CustomAlert
        setShow={setShowAlert}
        show={showAlert}
        type={alert.type}
        msg={alert.msg}
      ></CustomAlert>
      <Container className={styles.container}>
        <Form validated={validated} onSubmit={(e) => handleSubmit(e)}>
          <h1>Sign Up</h1>
          <Form.Group className="mb-3" controlId="formGroupFirstname">
            <Form.Label>Firstname</Form.Label>
            <Form.Control
              required
              type="text"
              name="firstname"
              value={formData.firstname}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formGroupLastname">
            <Form.Label>Lastname</Form.Label>
            <Form.Control
              required
              type="text"
              name="lastname"
              value={formData.lastname}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formGroupUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              required
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formGroupEmail">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              required
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formGroupPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              required
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formGroupAddress">
            <Form.Label>Address (optional)</Form.Label>
            <Row>
              <Col xs={11}>
                <Form.Control
                  readonly
                  disabled
                  type="text"
                  name="address"
                  value={formData.address}
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
          <Form.Group controlId="formGroupPhoneNumber" className="mb-3">
            <Form.Label>Phone number (optional)</Form.Label>
            <Form.Control
              onKeyPress={(event) => {
                if (!/[0-9]/.test(event.key)) {
                  event.preventDefault();
                }
              }}
              size="sm"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
            />
          </Form.Group>

          <Button className={styles.btn} type="submit">
            Sign Up
          </Button>
          <hr></hr>
          <div style={{ textAlign: "center" }}>
            <Link
              className="lnk"
              onClick={() => {
                setPage("login");
              }}
            >
              Already have an account?
            </Link>
          </div>
        </Form>
      </Container>
    </>
  );
}

export default SignUp;
