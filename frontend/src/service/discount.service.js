import api from "./api";

class DiscountService {
  getAllCoupons() {
    try {
      return api
        .get("/coupon/all")
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getAllCouponsForRestaurant(restaurant_uuid) {
    try {
      return api
        .get("/coupon/res/" +restaurant_uuid)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  addCoupon(coupon) {
    try {
      return api
        .post("/coupon/add",coupon)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  deleteCoupon(id) {
    try {
      return api
        .delete("/coupon/"+id)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  applyCoupon(id) {
    return api.post("/coupon/apply/"+id);
  }

  getUserScore() {
    return api.get("/score/get/uuid");
  }

  getRequiredScore() {
    return api.get("/requiredscore/get");
  }

  updateRequiredScore(reqScore) {
    try {
      return api
        .post("/requiredscore/add",reqScore)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  incrementUserOrders(orders) {
    return api.post("/score/update/incrementorders/" + orders).then(response => {
      console.log(response);
    });
  }

  incrementUserMoney(money) {
    return api.post("/score/update/incrementmoney/" + money).then(response => {
      console.log(response);
    });
  }



}

export default new DiscountService();