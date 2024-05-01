import React from "react";
import { Button, ButtonGroup } from "react-bootstrap";
import { Check2Circle, XCircleFill } from "react-bootstrap-icons";
import authService from "../../service/auth.service";
import orderService from "../../service/order.service";

function Actions({
  order,
  setNoActions,
  moveOrder,
  setOrder,
  alert,
  setAlert,
  setShowAlert,
}) {
  const user = authService.getCurrentUser();

  const handleError = (err) => {
    setAlert({ ...alert, msg: err, type: "error" });
    setShowAlert(true);
  };

  const acceptOrder = () => {
    document.body.style.cursor = "wait";
    orderService.acceptOrderForRestaurant(order.id).then((res) => {
      document.body.style.cursor = "default";
      if (res.status == 200) {
        moveOrder(order, res.data, "Accept");
      } else {
        handleError(res.data);
      }
    });
  };

  const rejectOrder = () => {
    document.body.style.cursor = "wait";
    orderService.rejectOrder(order.id).then((res) => {
      document.body.style.cursor = "default";

      if (res.status == 200) {
        moveOrder(order, res.data, "Reject");
      } else {
        handleError(res.data);
      }
    });
  };

  const cancelOrder = () => {
    document.body.style.cursor = "wait";
    orderService.cancelOrder(order.id).then((res) => {
      document.body.style.cursor = "default";

      if (res.status == 200) {
        setOrder(res.data);
      } else {
        handleError(res.data);
      }
    });
  };

  const readyForDelivery = () => {
    document.body.style.cursor = "wait";
    orderService.orderReady(order.id).then((res) => {
      document.body.style.cursor = "default";

      if (res.status == 200) {
        moveOrder(order, res.data);
      } else {
        handleError(res.data);
      }
    });
  };

  const acceptForDelivery = () => {
    document.body.style.cursor = "wait";
    orderService.acceptOrderForCourier(order.id).then((res) => {
      document.body.style.cursor = "default";

      if (res.status == 200) {
        moveOrder(order, res.data);
      } else {
        handleError(res.data);
      }
    });
  };

  const deliver = () => {
    document.body.style.cursor = "wait";
    orderService.orderDelivered(order.id).then((res) => {
      document.body.style.cursor = "default";

      if (res.status == 200) {
        moveOrder(res.data);
      } else {
        handleError(res.data);
      }
    });
  };

  const restaurantManagerActions = () => {
    switch (order.orderStatus) {
      case "NEW":
        setNoActions(false);
        return (
          <ButtonGroup>
            <Button
              style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 5,
                borderBottomRightRadius: 0,
                width: "100px",
              }}
              variant="secondary"
              onClick={acceptOrder}
            >
              Accept <Check2Circle />
            </Button>
            <Button
              style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 0,
                borderBottomRightRadius: 5,
                backgroundColor: "#FE724C",
                border: "#FE724C",
                width: "100px",
              }}
              variant="secondary"
              onClick={rejectOrder}
            >
              Reject <XCircleFill />
            </Button>
          </ButtonGroup>
        );
        break;
      case "ACCEPTED":
        setNoActions(false);
        return (
          <ButtonGroup>
            <Button
              style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 5,
                borderBottomRightRadius: 5,
                backgroundColor: "#FE724C",
                border: "#FE724C",
                width: "100px",
              }}
              variant="secondary"
              onClick={readyForDelivery}
            >
              Ready <Check2Circle />
            </Button>
          </ButtonGroup>
        );
        break;
      default:
        setNoActions(true);
    }
  };

  const courierActions = () => {
    switch (order.orderStatus) {
      case "READY_FOR_DELIVERY":
        setNoActions(false);
        return (
          <ButtonGroup>
            <Button
              style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 5,
                borderBottomRightRadius: 5,
                backgroundColor: "#FE724C",
                border: "#FE724C",
                width: "100px",
              }}
              variant="secondary"
              onClick={acceptForDelivery}
            >
              Accept <Check2Circle />
            </Button>
          </ButtonGroup>
        );
        break;
      case "IN_DELIVERY":
        setNoActions(false);
        return (
          <ButtonGroup>
            <Button
              style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 5,
                borderBottomRightRadius: 5,
                backgroundColor: "#FE724C",
                border: "#FE724C",
                width: "120px",
              }}
              variant="secondary"
              onClick={deliver}
            >
              Delivered <Check2Circle />
            </Button>
          </ButtonGroup>
        );
        break;
      default:
        setNoActions(true);
    }
  };

  const customerActions = () => {
    switch (order.orderStatus) {
      case "NEW":
        setNoActions(false);
        return (
          <ButtonGroup>
            <Button
              style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 5,
                borderBottomRightRadius: 5,
                backgroundColor: "#FE724C",
                border: "#FE724C",
                width: "100px",
              }}
              variant="secondary"
              onClick={cancelOrder}
            >
              Cancel <XCircleFill />
            </Button>
          </ButtonGroup>
        );
        break;
      default:
        setNoActions(true);
    }
  };

  return (
    <div>
      {user.role == "RESTAURANT_MANAGER"
        ? restaurantManagerActions()
        : user.role == "COURIER"
        ? courierActions()
        : user.role == "CUSTOMER"
        ? customerActions()
        : setNoActions(true)}
    </div>
  );
}

export default Actions;
