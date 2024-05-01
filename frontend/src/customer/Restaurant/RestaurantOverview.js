import React, { useEffect, useState } from "react";
import { Button, Container, Row, Col } from "react-bootstrap";
import { HeartFill, StarFill } from "react-bootstrap-icons";
import Image from "react-bootstrap/Image";
import { useLocation } from "react-router-dom";
import StarRatings from "react-star-ratings";
import defaultImage from "../../images/default.png";
import restaurantService from "../../service/restaurant.service";
import CustomAlert from "../../shared/util/Alert";
import Loader from "../../shared/util/Loader/Loader";
import NotFound from "../../shared/util/NotFound";
import Gallery from "./Gallery";
import MenuOverview from "./MenuOverview";
import AddReviewPopup from "../Review/AddReviewPopup";

function RestaurantOverview() {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const id = searchParams.get("id");
  const [notFound, setNotFound] = useState(false);
  const [loading, setLoading] = useState(true);
  const [restaurant, setRestaurant] = useState();
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const [categories, setCategories] = useState("");
  const [showReviewPopup, setShowReviewPopup] = useState(false);

  var mounted = false;
  useEffect(() => {
    if (!mounted) {
      mounted = true;

      if (id != null) {
        restaurantService.getRestaurant(id).then((res) => {
          setLoading(false);
          if (res.status == 200) {
            setRestaurant(res.data);
            setCategories(res.data.categories.join(", "));
          } else {
            if (res.status == 404) {
              setNotFound(true);
            }
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
            console.log(res);
          }
        });
      } else {
        setLoading(false);
      }
    }
  }, []);

  const favoritesButton = () => {
    if (restaurant.customerFavorite) {
      return (
        <Button
          size="sm"
          onClick={() => removeFromFavorites(restaurant.id)}
          style={{
            width: "fit-content",
            backgroundColor: "#fe724c",
            borderColor: "#fe724c",
            padding: "5px",
            marginRight: "10px",
            float: "right",
          }}
        >
          <HeartFill
            style={{
              color: "white",
              marginRight: "5px",
              marginBottom: "5px",
            }}
          />
          Remove from favorites{" "}
        </Button>
      );
    } else {
      return (
        <Button
          size="sm"
          onClick={() => addToFavorites(restaurant.id)}
          style={{
            width: "fit-content",
            backgroundColor: "#fe724c",
            borderColor: "#fe724c",
            padding: "5px",
            marginRight: "10px",
            float: "right",
          }}
        >
          <HeartFill
            style={{
              color: "white",
              marginRight: "5px",
              marginBottom: "5px",
            }}
          />
          Add to favorites{" "}
        </Button>
      );
    }
  };

  const addReviewButton = () => {
    return (
      <Button
        size="sm"
        onClick={() => addReview()}
        style={{
          width: "fit-content",
          backgroundColor: "#fe724c",
          borderColor: "#fe724c",
          padding: "5px",
          marginRight: "10px",
          float: "left",
        }}
      >
        <StarFill
          style={{ color: "white", marginRight: "5px", marginBottom: "5px" }}
        />
        Add a review
      </Button>
    );
  };

  const addToFavorites = (id) => {
    document.body.style.cursor = "wait";
    restaurantService.addRestaurantToFavorites(id).then((res) => {
      document.body.style.cursor = "default";
      if (res.status == 201) {
        setRestaurant({
          ...restaurant,
          customerFavorite: true,
          customersFavorited: restaurant.customersFavorited + 1,
        });
      } else {
        setAlert({ ...alert, msg: res.data, type: "error" });
        setShowAlert(true);
      }
    });
  };

  const removeFromFavorites = (id) => {
    document.body.style.cursor = "wait";
    restaurantService.removeRestaurantFromFavorites(id).then((res) => {
      document.body.style.cursor = "default";
      if (res.status == 204) {
        setRestaurant({
          ...restaurant,
          customerFavorite: false,
          customersFavorited: restaurant.customersFavorited - 1,
        });
      } else {
        setAlert({ ...alert, msg: res.data, type: "error" });
        setShowAlert(true);
      }
    });
  };

  const addReview = () => {
    setShowReviewPopup(true);
  };

  return (
    <>
      {notFound ? <NotFound header={false} /> : <></>}
      {id ? (
        <Loader isOpen={loading}>
          <CustomAlert
            setShow={setShowAlert}
            show={showAlert}
            type={alert.type}
            msg={alert.msg}
          ></CustomAlert>
          <AddReviewPopup
            setAlert={setAlert}
            setShowAlert={setShowAlert}
            restaurantId={restaurant?.id}
            show={showReviewPopup}
            setShow={setShowReviewPopup}
          />
          {restaurant ? (
            <div>
              <Container
                style={{
                  width: "95%",
                  padding: 0,
                  position: "relative",
                  backgroundColor: "#F5F5F4",
                  minHeight: "500px",
                }}
              >
                <Gallery id={restaurant.id} />
                <div style={{ zIndex: 1 }}>
                  <Image
                    fluid
                    thumbnail
                    src={restaurant.logo ? `${restaurant.logo} ` : defaultImage}
                    style={{
                      objectFit: "cover",
                      height: "250px",
                      width: "250px",
                      zIndex: 1,
                      position: "absolute",
                      top: 220,
                      left: 70,
                    }}
                    className="shadow-lg"
                  />
                  <div style={{ marginLeft: "340px", marginTop: "15px" }}>
                    <Row style={{ gap: 0 }}>
                      <Col className="col-7">
                        <h2>
                          {restaurant.name}{" "}
                          {restaurant.open ? (
                            <></>
                          ) : (
                            <span style={{ color: "red" }}>(CLOSED)</span>
                          )}
                        </h2>
                      </Col>
                      <Col className="col-3" style={{ padding: 0 }}>
                        {favoritesButton()}
                      </Col>
                      <Col className="col-2 " style={{ padding: 0 }}>
                        {addReviewButton()}
                      </Col>
                    </Row>
                    <h6>{restaurant.address}</h6>
                    <h6 style={{ color: "#fe724c", fontWeight: "bold" }}>
                      {categories}
                    </h6>
                    <div style={{}}>
                      <StarRatings
                        rating={restaurant.rating ? restaurant.rating : 0}
                        starRatedColor="#FE724C"
                        numberOfStars={5}
                        name="rating"
                        starDimension="20px"
                        starSpacing="2px"
                        starEmptyColor="grey"
                      />
                      <span
                        style={{ color: "#FE724C", verticalAlign: "middle" }}
                      >
                        {" "}
                        {restaurant.rating ? restaurant.rating : 0}{" "}
                      </span>
                      <span style={{ color: "grey", verticalAlign: "middle" }}>
                        {"(" + restaurant.customersRated + ")"}
                      </span>
                    </div>
                    <div style={{}}>
                      <HeartFill
                        style={{ color: "#fe724c", verticalAlign: "middle" }}
                      ></HeartFill>
                      <span style={{ color: "grey", verticalAlign: "middle" }}>
                        {" "}
                        {restaurant.customersFavorited} customers love this
                      </span>
                    </div>
                  </div>
                </div>
                <div style={{ marginTop: "50px" }}>
                  <MenuOverview
                    restaurant={restaurant}
                    setAlert={setAlert}
                    setShowAlert={setShowAlert}
                  ></MenuOverview>
                </div>
              </Container>
            </div>
          ) : (
            <></>
          )}
        </Loader>
      ) : (
        <NotFound header={false}></NotFound>
      )}
    </>
  );
}

export default RestaurantOverview;
