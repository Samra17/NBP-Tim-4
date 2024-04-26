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

  const role = authService.getCurrentUser().role;
  useEffect(() => {
    if (!mounted) {
      mounted = true
      if(role=="CUSTOMER") {
      orderService.getUserOrders().then((res) => {
        setLoading(false)
        if (res.status == 200) {
          setOrders(res.data)
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" })
          setShowAlert(true)
        }
      })
    } else if(role == "RESTAURANT_MANAGER") {
      orderService.getRestaurantPastOrders(restaurantService.getCurrentRestaurantUUID()).then((res) => {
        setLoading(false)
        if (res.status == 200) {
          setOrders(res.data)
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" })
          setShowAlert(true)
        }
      })
    }
    }
  }, [])



  return (
    <Loader isOpen={loading}>
      <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
      {orders ? <ListContainer
        title={role == "CUSTOMER" ? "My orders" : "Order history"}
        type="order"
        grid={false}
        items={orders}
        setItems={setOrders}
        perPage={5}
      /> : <></>}
    </Loader>
  )
}

export default OrderHistory