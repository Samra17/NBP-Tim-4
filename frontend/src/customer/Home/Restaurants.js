import React, { useEffect, useState } from "react";
import { Container, Spinner } from "react-bootstrap";
import restaurantService from "../../service/restaurant.service";
import Map2 from "../../shared/MapModal/Map2";
import CustomAlert from "../../shared/util/Alert";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import Loader from "../../shared/util/Loader/Loader";

function Restaurants() {
  var mounted = false;
  const [favorites, setFavorites] = useState();
  const [searchResults, setSearchResults] = useState();
  const [categories, setCategories] = useState();
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const perPage = 2;

  async function handlePagination(page, perPage) {
    const restaurantsRes = await restaurantService.getAllRestaurants(
      page,
      perPage
    );
    if (restaurantsRes.status === 200) {
      setSearchResults(restaurantsRes.data.restaurants);
    }
  }

  useEffect(() => {
    if (!mounted) {
      mounted = true;
      setLoading(true);
      restaurantService.getAllRestaurants().then((res) => {
        if (res.status == 200) {
          setSearchResults(res.data.restaurants);
          restaurantService.getUserFavorites().then((res) => {
            if (res.status == 200) setFavorites(res.data);
            else {
              console.log(res);
              setLoading(false);
              setAlert({ ...alert, msg: [res.data], type: "error" });
              setShowAlert(true);
            }
            restaurantService.getCategories().then((res) => {
              setLoading(false);
              if (res.status == 200) setCategories(res.data);
              else {
                setAlert({ ...alert, msg: [res.data], type: "error" });
                setShowAlert(true);
              }
            });
          });
        } else {
          console.log(res);
          setLoading(false);
          setAlert({ ...alert, msg: [res.data], type: "error" });
          setShowAlert(true);
          setSearchResults([]);
        }
      });
    }
  }, []);

  return (
    <>
      <Loader isOpen={loading}>
        <CustomAlert
          setShow={setShowAlert}
          show={showAlert}
          type={alert.type}
          msg={alert.msg}
        ></CustomAlert>
        {searchResults ? (
          <>
            <Container
              style={{
                backgroundColor: "#D9D9D9",
                width: "95%",
                margin: "auto",
                marginTop: "20px",
                marginBottom: "20px",
                maxWidth: "95%",
              }}
            >
              {favorites && favorites.length > 0 ? (
                <ListContainer
                  items={favorites}
                  title={"Favorite restaurants"}
                  showFilters={false}
                ></ListContainer>
              ) : (
                <></>
              )}
              {searchResults ? (
                <ListContainer
                  items={searchResults}
                  title={"All restaurants"}
                  showFilters={true}
                  perPage={perPage}
                  categories={categories}
                  setItems={setSearchResults}
                  pagination={true}
                  // handlePagination={handlePagination}
                ></ListContainer>
              ) : (
                <div style={{ display: "flex", justifyContent: "center" }}>
                  <Spinner
                    animation="border"
                    style={{ color: "white", marginTop: "20%" }}
                  />
                </div>
              )}
              <Map2 restaurantLocations={searchResults}></Map2>
            </Container>
          </>
        ) : (
          <></>
        )}
      </Loader>
    </>
  );
}

export default Restaurants;
