import React, { useEffect, useState } from 'react'
import { Spinner, Container, Col, Row } from 'react-bootstrap';
import Loader from '../shared/util/Loader/Loader';
import Map2 from '../shared/MapModal/Map2';
import ListContainer from '../shared/util/ListContainer/ListContainer';
import restaurantService from '../service/restaurant.service';
import CustomAlert from '../shared/util/Alert';
import orderService from '../service/order.service';

function Orders() {
    var mounted = false;
    const [readyfordelivery, setReadyForDelivery] = useState([])
    const [inDelivery, setInDelivery] = useState([])
    const [loading, setLoading] = useState(true);
    const [alert, setAlert] = useState({});
    const [showAlert, setShowAlert] = useState(false);
    const perPage = 5;


    function handleReadyOrdersPagination(title,page,perPage,setTotalPages,setContainerLoading,filter) {
            orderService
          .getReadyForDeliveryOrders(page, perPage)
          .then((res) => {
            setContainerLoading(false);
            if (res.status == 200) {
              setReadyForDelivery(res.data.orders);
              setTotalPages(res.data.totalPages);
            } else {
              setAlert({ msg: res.data, type: "error" });
              setShowAlert(true);
            }
          });
    
      }

    function handleInDeliveryOrdersPagination(title,page,perPage,setTotalPages,setContainerLoading,filter) {
        orderService
      .getDeliveryPersonOrders(page, perPage)
      .then((res) => {
        setContainerLoading(false);
        if (res.status == 200) {
          setInDelivery(res.data.orders);
          setTotalPages(res.data.totalPages);
        } else {
          setAlert({ msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });

  }


    const acceptForDelivery = (oldOrder,newOrder) => {
        setReadyForDelivery(readyfordelivery.filter(o=>o.id!=oldOrder.id))
        setInDelivery([newOrder,...inDelivery])
    }

    const deliver = (oldOrder) => {
        setInDelivery(inDelivery.filter(o => o.id!=oldOrder.id))
    }

    return (
        <>
                <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
                {
                   
                        <Row>
                            <Col>
                                <ListContainer title={"Ready for delivery"} type="order" grid={false} items={readyfordelivery} perPage={perPage} moveOrder={acceptForDelivery} setItems={setReadyForDelivery} setAlert={setAlert} setShowAlert={setShowAlert} alert={alert} pagination='server' handlePagination={handleReadyOrdersPagination}/>
                            </Col>
                            <Col>
                                <ListContainer title={"Assigned orders"} type="order" grid={false} items={inDelivery} perPage={perPage} moveOrder={deliver} setItems={setInDelivery} setAlert={setAlert} setShowAlert={setShowAlert} alert={alert} pagination='server' handlePagination={handleInDeliveryOrdersPagination}/>
                            </Col>
                        </Row>
                  
                }
        </>
    )
}

export default Orders