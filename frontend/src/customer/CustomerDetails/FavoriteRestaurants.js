import React from 'react'
import { useState, useEffect } from 'react';
import restaurantService from '../../service/restaurant.service';
import Loader from '../../shared/util/Loader/Loader';
import ListContainer from '../../shared/util/ListContainer/ListContainer'
import CustomAlert from '../../shared/util/Alert';

function FavoriteRestaurants() {
  var mounted = false;
  const perPage = 5;
  const [favorites, setFavorites] = useState();
  const [loading, setLoading] = useState(false)
  const [alert, setAlert] = useState({})
  const [showAlert, setShowAlert] = useState(false)

  useEffect(() => {
    if (!mounted) {

      mounted = true;
      setLoading(true)
      restaurantService.getUserFavorites(1, perPage).then((res) => {
        setLoading(false);
        if (res.status == 200)  {
        setFavorites(res.data.restaurants);
        }
        else {
          setAlert({ ...alert, msg: [res.data], type: "error" });
          setShowAlert(true);
        }
      })

    }



  }, [])

  async function handlePagination(title, page, perPage, setTotalPages,setContainerLoad, filterData) {

    
      restaurantService.getUserFavorites(page, perPage).then((res) => {
        setContainerLoad(false);
        if (res.status == 200)  {
        setFavorites(res.data.restaurants);
        setTotalPages(res.data.totalPages);
        }
        else {
          setAlert({ ...alert, msg: [res.data], type: "error" });
          setShowAlert(true);
        }
      })

  }



  return (
    <>
      <Loader isOpen={loading} >
        <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
        {favorites ?
          <ListContainer items={favorites} title={"Favorite restaurants"} showFilters={false} grid={false} perPage={perPage} handlePagination={handlePagination} pagination='server'></ListContainer>
          : <></>}
      </Loader>
    </>
  )
}

export default FavoriteRestaurants