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

  getAllCouponsForRestaurant(page,perPage) {
    try {
      return api
        .get(`/api/coupon/res?page=${page}&size=${perPage}` )
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
        .post("/api/coupon/add",coupon)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getByCode(code,id) {
    try {
      return api
        .get("/api/coupon/code?code=" + code + "&restaurantId=" + id)
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
        .delete("/api/coupon/"+id)
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


}

export default new DiscountService();