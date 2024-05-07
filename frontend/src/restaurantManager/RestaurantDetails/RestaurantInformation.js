import { East, LocationOn, Upload } from "@mui/icons-material";
import dayjs from "dayjs";
import Multiselect from "multiselect-react-dropdown";
import React, { useEffect, useRef, useState } from "react";
import {
  Button,
  ButtonGroup,
  Col,
  Figure,
  Form,
  Row,
  Stack,
} from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import defaultImage from "../../images/default.png";
import restaurantService from "../../service/restaurant.service";
import MapModal from "../../shared/MapModal/MapModal";
import CustomAlert from "../../shared/util/Alert";
import Loader from "../../shared/util/Loader/Loader";
import "./Multiselect.css";
import OpeningHoursForm from "./OpeningHoursForm";

function RestaurantInformation() {
  const [validated, setValidated] = useState();
  const [restaurant, setRestaurant] = useState();
  const [formData, setFormData] = useState();
  const [showMap, setShowMap] = useState(false);
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState();
  const inputRef = useRef(null);
  const [formattedHours, setFormattedHours] = useState({
    mondayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    tuesdayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    wednesdayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    thursdayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    fridayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    saturdayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    sundayOpen: dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
    mondayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    tuesdayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    wednesdayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    thursdayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    fridayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    saturdayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    sundayClose: dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
  });
  const [checked, setChecked] = useState({
    mon: false,
    tue: false,
    wed: false,
    thu: false,
    fri: false,
    sat: false,
    sun: false,
  });
  const navigate = useNavigate();

  var mounted = false;
  useEffect(() => {
    if (!mounted) {
      mounted = true;
      restaurantService.getManagersRestaurant().then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setFormData(res.data);
          setRestaurant(res.data);
      
          if (res.data.openingHours) {
            setFormattedHours({
              mondayOpen: res.data.openingHours.mondayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.mondayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              mondayClose: res.data.openingHours.mondayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.mondayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
              tuesdayOpen: res.data.openingHours.tuesdayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.tuesdayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              tuesdayClose: res.data.openingHours.tuesdayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.tuesdayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
              wednesdayOpen: res.data.openingHours.wednesdayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.wednesdayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              wednesdayClose: res.data.openingHours.wednesdayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.wednesdayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
              thursdayOpen: res.data.openingHours.thursdayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.thursdayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              thursdayClose: res.data.openingHours.thursdayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.thursdayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
              fridayOpen: res.data.openingHours.fridayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.fridayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              fridayClose: res.data.openingHours.fridayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.fridayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
              saturdayOpen: res.data.openingHours.saturdayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.saturdayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              saturdayClose: res.data.openingHours.saturdayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.saturdayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
              sundayOpen: res.data.openingHours.sundayOpen
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.sundayOpen
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
              sundayClose: res.data.openingHours.sundayClose
                ? dayjs(
                    dayjs().format("YYYY-MM-DD") +
                      "T" +
                      res.data.openingHours.sundayClose
                  )
                : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
            });
            setChecked({
              mon: res.data.openingHours.mondayOpen != null ? true : false,
              tue: res.data.openingHours.tuesdayOpen != null ? true : false,
              wed: res.data.openingHours.wednesdayOpen != null ? true : false,
              thu: res.data.openingHours.thursdayOpen != null ? true : false,
              fri: res.data.openingHours.fridayOpen != null ? true : false,
              sat: res.data.openingHours.saturdayOpen != null ? true : false,
              sun: res.data.openingHours.sundayOpen != null ? true : false,
            });
          }
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
        restaurantService.getCategories().then((res) => {
          setLoading(false);
          if (res.status == 200)
            setCategories(res.data.map((c) => ({ key: c.name, cat: c.id })));
          else {
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
          }
        });
      });
    }
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const setAddress = (addr, coordinates) => {
    setFormData({ ...formData, address: addr, mapCoordinates: coordinates });
  };

  const handleSubmit = (e) => {
    const form = e.currentTarget;
    e.preventDefault();
    e.stopPropagation();
    if (form.checkValidity() === false) {
      return;
    }

    var hours = {};

    if (checked.mon) {
      hours.mondayOpen = dayjs(formattedHours.mondayOpen).format("HH:mm:ss");
      hours.mondayClose = dayjs(formattedHours.mondayClose).format("HH:mm:ss");
    } else {
      hours.mondayOpen = null;
      hours.mondayClose = null;
    }

    if (checked.tue) {
      hours.tuesdayOpen = dayjs(formattedHours.tuesdayOpen).format("HH:mm:ss");
      hours.tuesdayClose = dayjs(formattedHours.tuesdayClose).format(
        "HH:mm:ss"
      );
    } else {
      hours.tuesdayOpen = null;
      hours.tuesdayClose = null;
    }

    if (checked.wed) {
      hours.wednesdayOpen = dayjs(formattedHours.wednesdayOpen).format(
        "HH:mm:ss"
      );
      hours.wednesdayClose = dayjs(formattedHours.wednesdayClose).format(
        "HH:mm:ss"
      );
    } else {
      hours.wednesdayOpen = null;
      hours.wednesdayClose = null;
    }

    if (checked.thu) {
      hours.thursdayOpen = dayjs(formattedHours.thursdayOpen).format(
        "HH:mm:ss"
      );
      hours.thursdayClose = dayjs(formattedHours.thursdayClose).format(
        "HH:mm:ss"
      );
    } else {
      hours.thursdayOpen = null;
      hours.thursdayClose = null;
    }

    if (checked.fri) {
      hours.fridayOpen = dayjs(formattedHours.fridayOpen).format("HH:mm:ss");
      hours.fridayClose = dayjs(formattedHours.fridayClose).format("HH:mm:ss");
    } else {
      hours.fridayOpen = null;
      hours.fridayClose = null;
    }

    if (checked.sat) {
      hours.saturdayOpen = dayjs(formattedHours.saturdayOpen).format(
        "HH:mm:ss"
      );
      hours.saturdayClose = dayjs(formattedHours.saturdayClose).format(
        "HH:mm:ss"
      );
    } else {
      hours.saturdayOpen = null;
      hours.saturdayClose = null;
    }
    if (checked.sun) {
      hours.sundayOpen = dayjs(formattedHours.sundayOpen).format("HH:mm:ss");
      hours.sundayClose = dayjs(formattedHours.sundayClose).format("HH:mm:ss");
    } else {
      hours.sundayOpen = null;
      hours.sundayClose = null;
    }

    var body = {
      name: formData.name,
      mapCoordinates: formData.mapCoordinates,
      address: formData.address,
      logo: formData.logo,
    };

    var categories = formData.categories.map((c) => c.id);

    document.body.style.cursor = "wait";

    restaurantService.updateRestaurant(body, restaurant.id).then((r1) => {
      if (r1.status == 200) {
        restaurantService
          .updateRestaurantCategories(categories, restaurant.id)
          .then((r2) => {
            if (r2.status == 200) {
              restaurantService
                .updateRestaurantOpeningHours(hours, restaurant.id)
                .then((r3) => {
                  document.body.style.cursor = "default";
                  if (r3.status == 200) {
                    setAlert({
                      ...alert,
                      type: "success",
                      msg: "Successfully updated restaurant information!",
                    });
                    setShowAlert(true);
                    setRestaurant(r3.data);
                  } else {
                    setAlert({ ...alert, type: "error", msg: r3.data });
                    setShowAlert(true);
                  }
                });
            } else {
              document.body.style.cursor = "default";
              setAlert({ ...alert, type: "error", msg: r2.data });
              setShowAlert(true);
            }
          });
      } else {
        document.body.style.cursor = "default";
        setAlert({ ...alert, type: "error", msg: r1.data });
        setShowAlert(true);
      }
    });
  };

  const revert = () => {
    setFormData(restaurant);
    setFormattedHours({
      mondayOpen: restaurant.openingHours.mondayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.mondayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      mondayClose: restaurant.openingHours.mondayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.mondayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
      tuesdayOpen: restaurant.openingHours.tuesdayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.tuesdayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      tuesdayClose: restaurant.openingHours.tuesdayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.tuesdayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
      wednesdayOpen: restaurant.openingHours.wednesdayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.wednesdayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      wednesdayClose: restaurant.openingHours.wednesdayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.wednesdayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
      thursdayOpen: restaurant.openingHours.thursdayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.thursdayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      thursdayClose: restaurant.openingHours.thursdayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.thursdayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
      fridayOpen: restaurant.openingHours.fridayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.fridayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      fridayClose: restaurant.openingHours.fridayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.fridayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
      saturdayOpen: restaurant.openingHours.saturdayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.saturdayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      saturdayClose: restaurant.openingHours.saturdayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.saturdayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
      sundayOpen: restaurant.openingHours.sundayOpen
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.sundayOpen
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T09:00:00"),
      sundayClose: restaurant.openingHours.sundayClose
        ? dayjs(
            dayjs().format("YYYY-MM-DD") +
              "T" +
              restaurant.openingHours.sundayClose
          )
        : dayjs(dayjs().format("YYYY-MM-DD") + "T22:00:00"),
    });
    setChecked({
      mon: restaurant.openingHours.mondayOpen != null ? true : false,
      tue: restaurant.openingHours.tuesdayOpen != null ? true : false,
      wed: restaurant.openingHours.wednesdayOpen != null ? true : false,
      thu: restaurant.openingHours.thursdayOpen != null ? true : false,
      fri: restaurant.openingHours.fridayOpen != null ? true : false,
      sat: restaurant.openingHours.saturdayOpen != null ? true : false,
      sun: restaurant.openingHours.sundayOpen != null ? true : false,
    });
  };

  const handleCategoryChange = (e) => {
    setFormData({
      ...formData,
      categories: e.map((c) => ({ name: c.key, id: c.cat })),
    });
  };

  const uploadLogo = () => {
    inputRef.current?.click();
  };

  const onFileChange = (e) => {
    let files = e.target.files;
    let fileReader = new FileReader();
    fileReader.readAsDataURL(files[0]);

    fileReader.onload = (event) => {
      setFormData({ ...formData, logo: event.target.result });
    };
  };

  const setOpeningHours = (openingHours) => {
    setFormData({ ...formData, openingHours: openingHours });
  };

  return (
    <Loader isOpen={loading}>
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
      {formData ? (
        <>
          <Row>
            <Col className="col-8">
              <Form
                validated={validated}
                onSubmit={(e) => handleSubmit(e)}
                style={{ padding: "20px" }}
              >
                <Form.Group className="mb-3" controlId="formGroupFirstname">
                  <Form.Label>Restaurant name</Form.Label>
                  <Form.Control
                    required
                    type="text"
                    name="restaurantName"
                    value={formData.name}
                    onChange={handleChange}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formGroupAddress">
                  <Form.Label>Address</Form.Label>
                  <Row>
                    <Col xs={11}>
                      <Form.Control
                        readonly
                        disabled
                        required
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
                <Form.Group>
                  <Row className="mb-3" controlId="formGroupCategories">
                    <Form.Label>Categories</Form.Label>
                    <Multiselect
                      closeIcon="circle"
                      displayValue="key"
                      hidePlaceholder="true"
                      showArrow="true"
                      avoidHighlightFirstOption="true"
                      placeholder=""
                      options={categories}
                      selectedValues={formData.categories.map((c) => ({
                        key: c.name,
                        cat: c.id,
                      }))}
                      onSelect={handleCategoryChange}
                      onRemove={handleCategoryChange}
                      loading={categories ? false : true}
                      style={{
                        chips: {
                          background: "#272D2F",
                          border: { borderRadius: "0px" },
                          color: "white",
                        },
                        searchBox: { "border-radius": "5px", color: "black" },
                        icon: { color: "black" },
                        color: "black !important",
                      }}
                    />
                  </Row>
                </Form.Group>
                {checked && formattedHours ? (
                  <OpeningHoursForm
                    checked={checked}
                    setChecked={setChecked}
                    formattedHours={formattedHours}
                    setFormattedHours={setFormattedHours}
                  />
                ) : (
                  <></>
                )}
              </Form>
            </Col>
            <Col className="col-2">
              <Figure className="mt-5" style={{ width: "200px" }}>
                <Figure.Image
                  style={{ height: "200px" }}
                  src={formData.logo ? `${formData.logo} ` : defaultImage}
                />
                <Figure.Caption>Logo image</Figure.Caption>
              </Figure>
              <Stack gap={2} className="mb-3">
                <input
                  ref={inputRef}
                  className="d-none"
                  type="file"
                  onChange={onFileChange}
                  accept="image/*"
                />
                <Button style={{ width: "200px" }} onClick={uploadLogo}>
                  Choose an image
                  <Upload style={{ marginLeft: "10px" }} />
                </Button>
                <Button
                  style={{ width: "200px" }}
                  onClick={() =>
                    navigate("/restaurant/gallery", { state: restaurant.id })
                  }
                >
                  Image gallery
                  <East style={{ marginLeft: "10px" }} />
                </Button>
              </Stack>
            </Col>
          </Row>
          <Row>
            <Col className="col-8"></Col>
            <Col className="col-4">
              <ButtonGroup style={{ width: "fit-content" }}>
                <Button
                  onClick={revert}
                  type="button"
                  className="mb-3"
                  style={{ width: "fit-content" }}
                >
                  Cancel
                </Button>

                <Button
                  type="submit"
                  className="mb-3"
                  style={{
                    width: "fit-content",
                    backgroundColor: "#fe724c",
                    border: "#fe724c",
                  }}
                  onClick={handleSubmit}
                >
                  Submit changes
                </Button>
              </ButtonGroup>
            </Col>
          </Row>
        </>
      ) : (
        <></>
      )}
    </Loader>
  );
}

export default RestaurantInformation;
