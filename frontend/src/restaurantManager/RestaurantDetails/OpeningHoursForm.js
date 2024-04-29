import { LocalizationProvider, TimePicker } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import React from "react";
import { Col, Container, Form, Row } from "react-bootstrap";

function OpeningHoursForm({
  checked,
  setChecked,
  formattedHours,
  setFormattedHours,
}) {
  return (
    <Container style={{ width: "100%" }}>
      <Form noValidate={true}>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Monday"
              name="mon"
              type={"checkbox"}
              checked={checked.mon}
              onChange={(e) => {
                setChecked({ ...checked, mon: !checked.mon });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                name="mon-open"
                style={{ height: "30px" }}
                disabled={!checked.mon}
                value={formattedHours.mondayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, mondayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.mon}
                value={formattedHours.mondayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, mondayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Tuesday"
              type={"checkbox"}
              checked={checked.tue}
              onChange={(e) => {
                setChecked({ ...checked, tue: !checked.tue });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                style={{ height: "30px" }}
                disabled={!checked.tue}
                value={formattedHours.tuesdayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, tuesdayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.tue}
                value={formattedHours.tuesdayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, tuesdayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Wednesday"
              type={"checkbox"}
              checked={checked.wed}
              onChange={(e) => {
                setChecked({ ...checked, wed: !checked.wed });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                style={{ height: "30px" }}
                disabled={!checked.wed}
                value={formattedHours.wednesdayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, wednesdayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.wed}
                value={formattedHours.wednesdayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, wednesdayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Thursday"
              type={"checkbox"}
              checked={checked.thu}
              onChange={(e) => {
                setChecked({ ...checked, thu: !checked.thu });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                disabled={!checked.thu}
                value={formattedHours.thursdayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, thursdayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.thu}
                value={formattedHours.thursdayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, thursdayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Friday"
              type={"checkbox"}
              checked={checked.fri}
              onChange={(e) => {
                setChecked({ ...checked, fri: !checked.fri });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                style={{ height: "30px" }}
                disabled={!checked.fri}
                value={formattedHours.fridayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, fridayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.fri}
                value={formattedHours.fridayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, fridayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Saturday"
              type={"checkbox"}
              checked={checked.sat}
              onChange={(e) => {
                setChecked({ ...checked, sat: !checked.sat });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                style={{ height: "30px" }}
                disabled={!checked.sat}
                value={formattedHours.saturdayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, saturdayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.sat}
                value={formattedHours.saturdayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, saturdayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
        <Row className="mb-2">
          <Col className="col-4">
            <Form.Check
              style={{ marginTop: "12px" }}
              label="Sunday"
              type={"checkbox"}
              checked={checked.sun}
              onChange={(e) => {
                setChecked({ ...checked, sun: !checked.sun });
              }}
            />
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Open at"
                style={{ height: "30px" }}
                disabled={!checked.sun}
                value={formattedHours.sundayOpen}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, sundayOpen: val });
                }}
              />
            </LocalizationProvider>
          </Col>
          <Col className="col-4">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                label="Close at"
                style={{ height: "30px" }}
                disabled={!checked.sun}
                value={formattedHours.sundayClose}
                onChange={(val) => {
                  setFormattedHours({ ...formattedHours, sundayClose: val });
                }}
              />
            </LocalizationProvider>
          </Col>
        </Row>
      </Form>
    </Container>
  );
}

export default OpeningHoursForm;
