import api from "./api";
import authService from "./auth.service";

class UserService {
  getAllUsers() {
    try {
      return api.get("/api/user/all").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  addManager(manager) {
    try {
      return api
        .post("/api/auth/register-manager", manager)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  addCourier(courier) {
    try {
      return api
        .post("/api/auth/register-courier", courier)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getAllManagers() {
    try {
      return api.get("/api/user/managers").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getAllCouriers() {
    try {
      return api.get("/api/user/couriers").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getDistanceToRestaurant(user,restaurant) {
    
    var c1 = restaurant.mapCoordinates
      .split(", ")
      .map((x) => (parseFloat(x) / 180) * Math.PI);
    var c2 = user.mapCoordinates
      .split(", ")
      .map((x) => (parseFloat(x) / 180) * Math.PI);
    var d =
      Math.acos(
        Math.sin(c1[0]) * Math.sin(c2[0]) +
          Math.cos(c1[0]) * Math.cos(c2[0]) * Math.cos(c2[1] - c1[1])
      ) *
      6371 *
      10;
    return Math.round(d) / 10;
    
  }

  getUserAnalysisReport() {
    return api.get("/api/admin/user-analysis-report",{responseType:"blob"})
  }
    
}

export default new UserService();
