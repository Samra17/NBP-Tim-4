import api from "./api";
import TokenService from "./token.service";

class RestaurantService {
  getAllRestaurants(page = 1, perPage = 4) {
    try {
      return api
        .get(`/api/restaurant/all?page=${page}&perPage=${perPage}`)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  addRestaurant(restaurant) {
    try {
      return api.post("/restaurant/add", restaurant).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteRestaurant(id) {
    try {
      return api.delete("/restaurant/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getAllFullRestaurants() {
    try {
      return api.get("/restaurant/all/full").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getUserFavorites() {
    try {
      return api
        .get(`/api/restaurant/favorites?page=${1}&perPage=${10}`)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  searchRestaurants(filters) {
    let query = "";
    if (filters.name) {
      if (query.length == 0) {
        query += "?name=" + filters.name;
      } else {
        query += "&name=" + filters.name;
      }
    }

    if (filters.offeringDiscount) {
      if (query.length == 0) {
        query += "?isOfferingDiscount=" + filters.offeringDiscount;
      } else {
        query += "&isOfferingDiscount=" + filters.offeringDiscount;
      }
    }

    if (filters.categoryIds) {
      filters.categoryIds.forEach((cid) => {
        if (query.length == 0) {
          query += "?categoryIds=" + cid;
        } else {
          query += "&categoryIds=" + cid;
        }
      });
    }

    if (query.length == 0) {
      query += "?sortBy=" + filters.sortBy + "&ascending=" + filters.ascending;
    } else {
      query += "&sortBy=" + filters.sortBy + "&ascending=" + filters.ascending;
    }

    try {
      return api.get("/api/restaurant/search" + query).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getCategories() {
    try {
      return api.get("/api/category/all").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getRestaurant(id) {
    try {
      return api.get("/restaurant/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getRestaurantImages(id) {
    try {
      return api.get("/restaurant/image/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  addRestaurantToFavorites(id) {
    try {
      return api
        .post("/restaurant/" + id + "/add-to-favorites")
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  removeRestaurantFromFavorites(id) {
    try {
      return api
        .put("/restaurant/" + id + "/remove-from-favorites")
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getManagersRestaurant() {
    try {
      return api.get("/restaurant/manager").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getManagersRestaurantUUID() {
    try {
      return api.get("/restaurant/uuid/manager").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateRestaurant(req, id) {
    try {
      return api.put("/restaurant/update/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateRestaurantCategories(req, id) {
    try {
      return api
        .put("/restaurant/" + id + "/add-categories", req)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  updateRestaurantOpeningHours(req, id) {
    try {
      return api
        .put("/restaurant/" + id + "/set-opening-hours", req)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getCurrentRestaurantUUID() {
    let restaurantUUID = TokenService.getRestaurantUUID();
    return restaurantUUID;
  }

  addImageToRestaurantGallery(req, id) {
    try {
      return api.post("/restaurant/image/add/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteImageFromRestaurantGallery(id) {
    try {
      return api.delete("/restaurant/image/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getReviews() {
    try {
      return api
        .get("/review/restaurant/" + this.getCurrentRestaurantUUID())
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getNumberOfFavorites() {
    try {
      return api
        .get("/restaurant/favorites/" + this.getCurrentRestaurantUUID())
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  addReview(req) {
    try {
      return api.post("/review/add", req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }
}

export default new RestaurantService();
