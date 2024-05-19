import { jwtDecode } from "jwt-decode";
class TokenService {
  getLocalRefreshToken() {
    return localStorage.getItem("refresh-token")
  }

  getLocalAccessToken() {
    return localStorage.getItem("access-token")
  }


  getUser() {
    var token = this.getLocalAccessToken()
    try {
      return jwtDecode(token)
    } catch{
      return null
    }
    
  }

  setAccessToken(token) {
    localStorage.setItem("access-token", token);
  }

  setRefreshToken(token) {
    localStorage.setItem("refresh-token", token);
  }

  removeUser() {
    localStorage.removeItem("access-token");
    localStorage.removeItem("refresh-token");

  }
}

export default new TokenService();
