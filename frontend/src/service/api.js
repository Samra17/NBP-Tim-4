import axios from "axios";
import TokenService from "./token.service";
import tokenService from "./token.service";

const instance = axios.create({
  headers: {
    "Content-Type": "application/json",
  },
});

instance.interceptors.request.use(
  (config) => {
    const token = TokenService.getLocalAccessToken();
    if (token) {
      config.headers["Authorization"] = "Bearer " + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

instance.interceptors.response.use(
  (res) => {
    return res;
  },
  async (err) => {
    const originalConfig = err.config;

    if (
      originalConfig.url !== "/api/auth/login" &&
      originalConfig.url !== "/api/auth/register"
    ) {
      // Access Token was expired
      if (err.response.status === 403 && !originalConfig._retry) {
        originalConfig._retry = true;

        try {
          var refreshToken = TokenService.getLocalRefreshToken();
          const config = {
            headers: {
              Authorization: "Bearer " + refreshToken,
            },
          };

          const rs = await axios.post("/api/auth/refresh-token", null, config);

          var accessToken = rs.data.accessToken;
          TokenService.setAccessToken(accessToken);

          return instance(originalConfig);
        } catch (_error) {
          //Refresh token is also expired, user must log in again
          tokenService.removeUser();
          window.location.reload();
          return Promise.reject(_error);
        }
      } else {
        return err.response;
      }
    }

    return err.response;
  }
);

export default instance;
