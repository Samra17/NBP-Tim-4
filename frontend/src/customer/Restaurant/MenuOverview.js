import React, { useEffect, useState } from "react";
import menuService from "../../service/menu.service";
import tokenService from "../../service/token.service";
import { Tabs, Tab } from "react-bootstrap";
import "./MenuOverview.css";
import { Trash, X, HeartFill } from "react-bootstrap-icons";
import MenuItem from "./MenuItem";
import Loader from "../../shared/util/Loader/Loader";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import { Spinner, Container } from "react-bootstrap";
import { Row, Col, Button } from "react-bootstrap";
import { MDBInput, MDBSpinner } from "mdb-react-ui-kit";
import discountService from "../../service/discount.service";
import userService from "../../service/user.service";
import orderService from "../../service/order.service";
import Alert from "../../shared/util/Alert";
import CustomAlert from "../../shared/util/Alert";
import shoppingCart from "../../images/Shopping cart.svg"

function MenuOverview({ restaurant, setAlert, setShowAlert }) {
  const [menus, setMenus] = useState([]);
  const [loading, setLoading] = useState(true);
  const [orderList, setOrderList] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const [deliveryPrice, setDeliveryPrice] = useState(0);

  const [couponCode, setCouponCode] = useState("");
  const [totalDiscount, setTotalDiscount] = useState(0);
  const [usedCode, setUsedCode] = useState(null);
  const [couponId, setCouponId] = useState(null);
  const [couponValid, setCouponValid] = useState(false);
  const [checkingCoupon, setCheckingCoupon] = useState(false);
  const [checkedCoupon, setCheckedCoupon] = useState(false);
  const [estDeliveryTime, setEstDeliveryTime] = useState(0);
  const [orderCreated, setOrderCreated] = useState(false);

  const [freeDeliveryUsed, setFreeDeliveryUsed] = useState(false);
  const [hasFreeDelivery, setHasFreeDelivery] = useState(false);
  const [freeDeliveryType, setFreeDeliveryType] = useState("");
  const [placingOrder, setPlacingOrder] = useState(false);

  const marginBetweenOrderItems = "10px";

  useEffect(() => {
    //var c1 = restaurant.mapCoordinates.split(", ").map(x => parseFloat(x) / 180 * Math.PI);
    //var c2 = tokenService.getUser().user.mapCoordinates.split(", ").map(x => parseFloat(x) / 180 * Math.PI);
    //var d = Math.acos(Math.sin(c1[0])*Math.sin(c2[0])+Math.cos(c1[0] )*Math.cos(c2[0])*Math.cos(c2[1]-c1[1])) * 6371 * 10;
    setDeliveryPrice(userService.getDistanceToRestaurant(restaurant));
    
  }, []);

  useEffect(() => {
    var price = 0;
    var time = 0;
    orderList.forEach((o) => {
      if (parseInt(o.menuItem.prep_time) > time) time = o.menuItem.prep_time;
      if (o.menuItem.discount_price != null) {
        price += o.menuItem.discount_price * o.count;
      } else {
        price += o.menuItem.price * o.count;
      }
    });
    time += Math.round(deliveryPrice * 2.5);

    setEstDeliveryTime(time);
    setTotalPrice(price);
  }, [orderList]);

  const checkCoupon = () => {
    setCheckingCoupon(true);

    discountService.getByCode(couponCode, restaurant.id).then((res)=> {
      setCheckingCoupon(false);
      setCheckedCoupon(true);
      if(res.status == 200) {
        setCouponValid(true);
        setTotalDiscount(res.data.discountPercent);
      } else if(res.status == 404) {
        setCouponValid(false);
      }
      else {
        setShowAlert(true);
        setAlert({ msg: res.data, type: "error" });
      } 
    })
  };



  const removeItemFromOrder = (item) => {
    var newOrderList = [];
    orderList.forEach((i) => {
      if (i.menuItem.id != item.menuItem.id) newOrderList.push(i);
    });
    setOrderList(newOrderList);
  };

  const orderListToUI = () => {
    return orderList.map((item) => (
      <>
        <Row style={{ marginBottom: "5px" }}>
          <Col xs={7}>
            {item.menuItem.name} x{item.count}
          </Col>
          <Col xs={3}>
            {(item.menuItem.discount_price != null
              ? item.menuItem.discount_price
              : item.menuItem.price) * item.count}{" "}
            KM
          </Col>
          <Col xs={1}>
            <Button
              onClick={() => removeItemFromOrder(item)}
              style={{
                backgroundColor: "#fe724c",
                borderColor: "#fe724c",
                width: "fit-content",
                height: "25px",
                width: "25px",
                padding: "0px",
              }}
            >
              <Trash></Trash>
            </Button>
          </Col>
        </Row>
      </>
    ));
  };

  const placeOrder = () => {
    setPlacingOrder(true);
    var user = tokenService.getUser();
    var orderRequest = {};

    orderRequest.totalPrice =
      (totalPrice + deliveryPrice * !hasFreeDelivery) *
      (1 - totalDiscount / 100);

    orderRequest.restaurantId = restaurant.id;
    orderRequest.estimatedDeliveryTime = estDeliveryTime;
    orderRequest.couponId = couponId;

    orderRequest.deliveryFee = deliveryPrice * !hasFreeDelivery;
    orderRequest.menuItemIds = [];
    for (var i = 0; i < orderList.length; i++) {
      console.log(orderList)
      orderRequest.menuItemIds.push({id: orderList[i].menuItem.id, quantity: orderList[i].count})
    }
    orderRequest.restaurantName = restaurant.name;
    orderRequest.customerPhoneNumber = user.user.phoneNumber;
    orderRequest.customerAddress = user.user.address;
    orderRequest.restaurantAddress = restaurant.address;
    orderRequest.customerId =user.user.id;
    orderService.createOrder(orderRequest).then((response) => {
      if (response.status < 300) {
        setOrderCreated(true);
        setShowAlert(true);
        setAlert({ msg: "Order successfully created!", type: "success" });

        if (couponId != null) {
          discountService.applyCoupon(couponId);
        }
        
      } else {
        setShowAlert(true);
        setAlert({ msg: "Error creating order!", type: "error" });
      }

      setPlacingOrder(false);
    });
  };

  useEffect(() => {
    
      menuService.getActiveRestaurantMenus(restaurant.id).then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setMenus(res.data);
          setLoading(false);
        }
      });
    
    }, []);

  return (
    <>
      <Loader isOpen={loading}>
        <div style={{ overflowY: "auto" }}>
          <hr className="tab-separator" />
          <Tabs defaultActiveKey={0} id="my-tabs" style={{ padding: 10 }}>
            {menus.map((menus, index) => (
              <Tab
                eventKey={index}
                title={<span style={{ color: "black" }}>{menus.name}</span>}
                key={index}
              >
                <div>
                  {menus.menuItems ? (
                    <>
                      <Container
                        style={{
                          backgroundColor: "#D9D9D9",
                          width: "100%",
                          margin: "auto",
                          marginTop: "20px",
                          marginBottom: "20px",
                          maxWidth: "60%",
                          float: "left",
                        }}
                      >
                        {menus.menuItems ? (
                          <ListContainer
                            items={menus.menuItems}
                            title={""}
                            showFilters={false}
                            perPage={5}
                            type={"menu"}
                            grid={false}
                            orderList={orderList}
                            setOrderList={setOrderList}
                          ></ListContainer>
                        ) : (
                          <div
                            style={{
                              display: "flex",
                              justifyContent: "center",
                            }}
                          >
                            <Spinner
                              animation="border"
                              style={{ color: "white", marginTop: "20%" }}
                            />
                          </div>
                        )}
                      </Container>
                      <Container
                        style={{
                          backgroundColor: "#D9D9D9",
                          width: "100%",
                          margin: "auto",
                          marginTop: "20px",
                          marginBottom: "20px",
                          maxWidth: "38%",
                          float: "right",
                        }}
                      >
                        Order
                        <Container
                          style={{
                            backgroundColor: "#FFFFFF",
                            width: "100%",
                            margin: "auto",
                            marginTop: "5px",
                            marginBottom: "5px",
                          }}
                        >
                          {restaurant.open ? (
                            tokenService.getUser().user.mapCoordinates !=
                            null ? (
                              orderList.length > 0 ? (
                                <>
                                  {orderListToUI()}

                                  <hr></hr>

                                  <Row
                                    style={{
                                      fontSize: "16px",
                                      marginTop: "20px",
                                    }}
                                  >
                                    <Col xs={8}>
                                      <MDBInput
                                        id="form1"
                                        type="text"
                                        placeholder="Got a coupon?"
                                        value={couponCode}
                                        onChange={(e) =>
                                          setCouponCode(e.target.value)
                                        }
                                      />
                                    </Col>
                                    <Col xs={3}>
                                      {!checkingCoupon ? (
                                        <Button
                                          style={{
                                            fontSize: "16px",
                                            width: "100%",
                                          }}
                                          onClick={checkCoupon}
                                        >
                                          {" "}
                                          Check{" "}
                                        </Button>
                                      ) : (
                                        <MDBSpinner></MDBSpinner>
                                      )}
                                    </Col>
                                  </Row>
                                  {checkedCoupon ? (
                                    couponValid ? (
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "green",
                                        }}
                                      >
                                        Coupon valid!{" "}
                                      </div>
                                    ) : (
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "red",
                                        }}
                                      >
                                        Coupon invalid!{" "}
                                      </div>
                                    )
                                  ) : (
                                    <></>
                                  )}
                                  {!freeDeliveryUsed ? (
                                    <Row>
                                      <Col xs={8}>
                                        <div
                                          style={{
                                            fontSize: "16px",
                                            marginTop: "20px",
                                          }}
                                        >
                                          Delivery fee:{" "}
                                          {deliveryPrice.toFixed(2)} KM
                                        </div>
                                      </Col>
                                      
                                    </Row>
                                  ) : hasFreeDelivery ? (
                                    <>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: "10px",
                                        }}
                                      >
                                        Delivery fee:{" "}
                                        <del>{deliveryPrice.toFixed(2)}</del>{" "}
                                        0.00 KM
                                      </div>
                                      <div
                                        style={{
                                          fontSize: "10px",
                                          color: "green",
                                        }}
                                      >
                                        Free delivery used!{" "}
                                      </div>
                                    </>
                                  ) : (
                                    <>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: "10px",
                                        }}
                                      >
                                        Delivery fee: {deliveryPrice.toFixed(2)}{" "}
                                        KM
                                      </div>
                                    </>
                                  )}

                                  {couponValid ? (
                                    <>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: marginBetweenOrderItems,
                                        }}
                                      >
                                        Total discount: {totalDiscount}%
                                      </div>
                                      <div
                                        style={{
                                          fontSize: "16px",
                                          marginTop: marginBetweenOrderItems,
                                        }}
                                      >
                                        Total price:{" "}
                                        <del>
                                          {(
                                            totalPrice +
                                            deliveryPrice * !hasFreeDelivery
                                          ).toFixed(2)}
                                        </del>{" "}
                                        {(
                                          (totalPrice +
                                            deliveryPrice * !hasFreeDelivery) *
                                          (1 - totalDiscount / 100)
                                        ).toFixed(2)}{" "}
                                        KM{" "}
                                      </div>
                                    </>
                                  ) : (
                                    <div
                                      style={{
                                        fontSize: "16px",
                                        marginTop: marginBetweenOrderItems,
                                      }}
                                    >
                                      Total price:{" "}
                                      {(
                                        totalPrice +
                                        deliveryPrice * !hasFreeDelivery
                                      ).toFixed(2)}{" "}
                                      KM
                                    </div>
                                  )}
                                  <div
                                    style={{
                                      fontSize: "16px",
                                      marginTop: marginBetweenOrderItems,
                                    }}
                                  >
                                    Delivery time: {estDeliveryTime.toFixed(0)}{" "}
                                    min
                                  </div>
                                  <hr></hr>
                                  {orderCreated ? (
                                    <></>
                                  ) : placingOrder ? (
                                    <Row>
                                      <Col xs={5}></Col>
                                      <Col xs={1}>
                                        <MDBSpinner></MDBSpinner>
                                      </Col>
                                      <Col xs={5}></Col>
                                    </Row>
                                  ) : (
                                    <Row>
                                      <Col xs={2}></Col>
                                      <Col xs={7}>
                                        <Button onClick={placeOrder}>
                                          Place order
                                        </Button>
                                      </Col>
                                      <Col xs={2}></Col>
                                    </Row>
                                  )}
                                </>
                              ) : (
                                <div style={{textAlign:"center",color:"grey"}}>
                                  <img src={shoppingCart} style={{width:"300px",marginLeft:"40px"}}/>
                                  <br/>
                                  <span>Your cart is empty</span>
                                </div>
                              )
                            ) : (
                              <>
                                Please add address and phone number to account
                                before ordering
                              </>
                            )
                          ) : (
                            <> Restaurant is currently closed </>
                          )}
                        </Container>
                      </Container>
                    </>
                  ) : (
                    <></>
                  )}
                </div>
              </Tab>
            ))}
          </Tabs>
        </div>
      </Loader>
    </>
  );
}

export default MenuOverview;
