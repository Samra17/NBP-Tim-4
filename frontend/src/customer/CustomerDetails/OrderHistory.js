import React, { useEffect, useState } from 'react'
import ListContainer from '../../shared/util/ListContainer/ListContainer'
import orderService from '../../service/order.service'
import Loader from '../../shared/util/Loader/Loader'
import CustomAlert from '../../shared/util/Alert'
import authService from '../../service/auth.service'
import restaurantService from '../../service/restaurant.service'


function OrderHistory() {
  const [orders, setOrders] = useState()
  const [loading, setLoading] = useState(true)
  const [alert, setAlert] = useState({})
  const [showAlert, setShowAlert] = useState(false)
  var mounted = false;
  const perPage = 5;

  const role = authService.getCurrentUser().role;
  useEffect(() => {
    if (!mounted) {
      mounted = true
      if(role=="CUSTOMER") {
      orderService.getUserOrders(1,perPage).then((res) => {
        setLoading(false)
        if (res.status == 200) {
          setOrders(res.data.orders)
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" })
          setShowAlert(true)
        }
      })
    } else if(role == "RESTAURANT_MANAGER") {
      orderService.getRestaurantPastOrders(1,perPage).then((res) => {
        setLoading(false)
        if (res.status == 200) {
          setOrders(res.data.orders)
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" })
          setShowAlert(true)
        }
      })
    }
    }
  }, [])

  async function handlePagination(title, page, perPage, setTotalPages,setContainerLoad, filterData) {

      if(role=="CUSTOMER") {
        orderService.getUserOrders(page,perPage).then((res) => {
          setContainerLoad(false);
          if (res.status == 200) {
            setOrders(res.data.orders);
            setTotalPages(res.data.totalPages);
          } else {
            setAlert({ ...alert, msg: res.data, type: "error" })
            setShowAlert(true)
          }
        })
      } else if(role == "RESTAURANT_MANAGER") {
        orderService.getRestaurantPastOrders(page,perPage).then((res) => {
          setContainerLoad(false);
          if (res.status == 200) {
            setOrders(res.data.orders)
            setTotalPages(res.data.totalPages);
          } else {
            setAlert({ ...alert, msg: res.data, type: "error" })
            setShowAlert(true)
          }
        })
      }

  }



  return (
    <Loader isOpen={loading}>
      <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
      {orders ? <ListContainer
        title={role == "CUSTOMER" ? "My orders" : "Order history"}
        type="order"
        grid={false}
        items={orders}
        setItems={setOrders}
        perPage={perPage}
        handlePagination={handlePagination}
        pagination='server'
      /> : <></>}
    </Loader>
  )
}

export default OrderHistory