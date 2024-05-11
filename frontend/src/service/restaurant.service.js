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
      return api.get("/api/restaurant/all/full").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getUserFavorites(page, perPage) {
    try {
      return api
        .get(`/api/restaurant/favorites?page=${page}&perPage=${perPage}`)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  searchRestaurants(filters, page, perPage) {
    let query = `?page=${page}&perPage=${perPage}`;
    if (filters.name) {
      query += "&name=" + filters.name;
      
    }

    if (filters.offeringDiscount) {
     query += "&isOfferingDiscount=" + filters.offeringDiscount;
      
    }

    if (filters.categoryIds) {
      filters.categoryIds.forEach((cid) => {
          query += "&categoryIds=" + cid;
      });
    }

    query += "&sortBy=" + filters.sortBy + "&ascending=" + filters.ascending;
    

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
      return api.get("/api/restaurant/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getRestaurantImages(id) {
    try {
      return api.get("/api/restaurant/image/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  addRestaurantToFavorites(id) {
    try {
      return api
        .post("/api/restaurant/" + id + "/add-to-favorites")
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
        .delete("/api/restaurant/" + id + "/remove-from-favorites")
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getManagersRestaurant() {
    try {
      return api.get("/api/restaurant/manager").then((response) => {
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
      return api.put("/api/restaurant/update/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateRestaurantCategories(req, id) {
    try {
      return api
        .put("/api/restaurant/" + id + "/add-categories", req)
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
        .put("/api/restaurant/" + id + "/set-opening-hours", req)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }



  addImageToRestaurantGallery(req, id) {
    try {
      return api.post("/api/restaurant/image/add/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteImageFromRestaurantGallery(id) {
    try {
      return api.delete("/api/restaurant/image/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getReviews() {
    try {
      return api
        .get("/api/review/restaurant/" + this.getCurrentRestaurantUUID())
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
        .get("/api/restaurant/favorites/" + this.getCurrentRestaurantUUID())
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  addReview(req) {
    try {
      return api.post("/api/review/add", req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }


  uploadLogo(image) {
    try {
      return api.post("/api/restaurant/logo/add", image, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }
}

export default new RestaurantService();
