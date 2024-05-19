import api from "./api";
import restaurantService from "./restaurant.service";

class MenuService {
  getActiveRestaurantMenus(id) {
    try {
      return api
        .get("/api/menu/restaurant-menus/active/" + id)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }
  getAllRestaurantMenus() {
    try {
      return api.get("/api/menu/restaurant-menus").then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteMenu(id) {
    try {
      return api.delete("/api/menu/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  addMenu(menu) {
    try {
      return api.post("/api/menu/add", menu).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getMenuById(id) {
    try {
      return api.get("/api/menu/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  deleteMenuItem(id) {
    try {
      return api.delete("/api/menu-item/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateMenu(req, id) {
    try {
      return api.put("/api/menu/update/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  setMenuItems(req, id) {
    try {
      return api
        .put("/api/menu/" + id + "/set-menu-items", req)
        .then((response) => {
          return response;
        });
    } catch (e) {
      console.log(e);
    }
  }

  getMenuItemById(id) {
    try {
      return api.get("/api/menu-item/" + id).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateMenuItem(id, req) {
    try {
      return api.put("/api/menu-item/update/" + id, req).then((response) => {
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  uploadImage(image) {
    try {
      return api.post("/api/menu-item/image/add", image, {
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

export default new MenuService();
