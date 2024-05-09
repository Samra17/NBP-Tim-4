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

  getAllCouponsForRestaurant(id) {
    try {
      return api
        .get("/api/coupon/res/" +id)
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
    return api.post("api/coupon/apply/"+id);
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