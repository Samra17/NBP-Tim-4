import React, { useEffect } from "react";
import { useState } from "react";
import Loader from "../../shared/util/Loader/Loader";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import restaurantService from "../../service/restaurant.service";
import { HeartFill } from "react-bootstrap-icons";
import CustomAlert from "../../shared/util/Alert";

function Reviews() {
  const [reviews, setReviews] = useState();
  const [loading, setLoading] = useState(false);
  const [favorites, setFavorites] = useState();
  const [alert,setAlert] = useState({});
  const [showAlert,setShowAlert] = useState(false);

  var mounted = false;
  useEffect(() => {
    if (!mounted) {
      mounted = true;
      setLoading(true);
      restaurantService.getReviews().then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setReviews(res.data);
        } else {
            setAlert({msg:res.data,type:"error"})
            setShowAlert(true)
        }
      });
      restaurantService.getNumberOfFavorites().then((res) => {
        if (res.status == 200) {
          setFavorites(res.data);
        } else {
            setAlert({msg:res.data,type:"error"})
            setShowAlert(true)
        }
      });
    }
  }, []);

  const getAvg = () => {
    var avg = 0.0;
    reviews
      .map((r) => r.rating)
      .forEach((rating) => {
        avg += rating;
      });

    return reviews.length > 0 ? avg / reviews.length : 0.0;
  };

  return (
    <Loader isOpen={loading}>
      <CustomAlert
        msg={alert.msg}
        type={alert.type}
        show={showAlert}
        setShow={setShowAlert}
      ></CustomAlert>
      {reviews ? (
        <>
          <h2 style={{ paddingLeft: "20px",paddingTop:"20px",paddingBottom:"10px" }}>
            Average rating:{" "}
            <span
              style={{
                fontSize: "28px",
                color: "#fe724c",
                fontWeight: "regular",
              }}
            >
              {getAvg()}
            </span>{" "}
            <span
              style={{ color: "grey", fontSize: "24px", fontWeight: "regular" }}
            >
              ({reviews.length})
            </span>
          </h2>
          {favorites ? (
            <div style={{paddingLeft:"20px"}}>
              <HeartFill
                style={{ color: "#fe724c", verticalAlign: "middle",width:"25px",height:"25px" }}
              ></HeartFill>
              <span style={{paddingLeft:"10px",fontSize:"18px"}}>
              {favorites} customers added to Favorites
              </span>
            </div>
          ) : (
            <></>
          )}
          <ListContainer
            title="Reviews"
            setItems={reviews}
            items={reviews}
            perPage={8}
            grid={false}
            type="review"
          ></ListContainer>
        </>
      ) : (
        <></>
      )}
    </Loader>
  );
}

export default Reviews;
