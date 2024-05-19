import api from "./api";
import TokenService from "./token.service";

class AuthService {
  login(username, password) {
    return api
      .post("/api/auth/login", {
        username,
        password,
      })
      .then((response) => {
        if (response.status === 200) {
          TokenService.setAccessToken(response.data.accessToken);
          TokenService.setRefreshToken(response.data.refreshToken);
        }

        return response;
      });
  }

  register(req) {
    try {
      return api.post("/api/auth/register", req).then((response) => {
        if (response.status == 201) {
          TokenService.setAccessToken(response.data.accessToken);
          TokenService.setRefreshToken(response.data.refreshToken);
        }

        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  updateUserInformation(req) {
    try {
      return api.post("/api/auth/user/update", req).then((response) => {
       
        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  logout() {
    try {
      return api.post("/api/auth/logout").then((response) => {
        if (response.status == 200) TokenService.removeUser();

        return response;
      });
    } catch (e) {
      console.log(e);
    }
  }

  getCurrentUser() {
    let user = TokenService.getUser();
    return user;
  }

   async getLoggedInUser() {
    return api.get('/api/user/current')

  }
 
}

export default new AuthService();
