import api from "./api";
import restaurantService from "./restaurant.service";

class MenuService {
  getActiveRestaurantMenus(restaurantUUID) {
    try {
      return api
        .get("/menu/restaurant-menus/active/" + restaurantUUID)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }
  getAllRestaurantMenus() {
    try {
      return api.get("/api/menu/restaurant-menus/id/" + 1).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteMenu(id) {
    try {
      return api.delete("/menu/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  addMenu(menu) {
    try {
      return api.post("/menu/add", menu).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getMenuById(id) {
    try {
      return api.get("/menu/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteMenuItem(id) {
    try {
      return api.delete("/menu-item/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateMenu(req, id) {
    try {
      return api.put("/menu/update/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  setMenuItems(req, id) {
    try {
      return api
        .put("/menu/" + id + "/set-menu-items", req)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getMenuItemById(id) {
    try {
      return api.get("/menu-item/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateMenuItem(id, req) {
    try {
      return api.put("/menu-item/update/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }
}

export default new MenuService();
