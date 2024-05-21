import { Restaurant } from "@mui/icons-material";
import { Nav } from "react-bootstrap";
import Navbar from "react-bootstrap/Navbar";
import { useLocation, useNavigate } from "react-router-dom";
import authService from "../../service/auth.service";

function Header() {
  const user = authService.getCurrentUser();
  const navigate = useNavigate();
  const location = useLocation();

  const customerOptions = () => {
    const logout = () => {
      document.body.style.cursor = "wait";
      authService.logout().then((res) => {
        document.body.style.cursor = "default";
        if (res.status == 200) {
          navigate("/register");
        }
      });
    };

    const customerInfoPage = () => {
      let path = "/customer/details";
      if (location.pathname != path) navigate(path);
    };

    const restaurants = () => {
      let path = "/";
      if (location.pathname != path) navigate(path);
    };

    return (
      <>
        <Nav className="me-auto"></Nav>
        <Nav>
          <Nav.Link className="text-white" onClick={restaurants}>
            Restaurants
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={customerInfoPage}>
            {user.sub}
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={logout}>
            Logout
          </Nav.Link>
        </Nav>
      </>
    );
  };

  const restaurantOptions = () => {
    const logout = () => {
      document.body.style.cursor = "wait";
      authService.logout().then((res) => {
        document.body.style.cursor = "default";
        if (res.status == 200) {
          navigate("/register");
        }
      });
    };

    const restaurantInfoPage = () => {
      let path = "/restaurant/details";
      if (location.pathname != path) navigate(path);
    };

    const orders = () => {
      let path = "/";
      if (location.pathname != path) navigate(path);
    };

    return (
      <>
        <Nav className="me-auto"></Nav>
        <Nav>
          <Nav.Link className="text-white" onClick={restaurantInfoPage}>
            My restaurant
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={orders}>
            Orders
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={logout}>
            Logout
          </Nav.Link>
        </Nav>
      </>
    );
  };

  const administratorOptions = () => {
    const logout = () => {
      document.body.style.cursor = "wait";
      authService.logout().then((res) => {
        document.body.style.cursor = "default";
        if (res.status == 200) {
          navigate("/register");
        }
      });
    };

    const restaurants = () => {
      let path = "/admin/restaurants";
      if (location.pathname != path) navigate(path);
    };

    const scores = () => {
      let path = "/admin/overview";
      if (location.pathname != path) navigate(path);
    };

    const couriers = () => {
      let path = "/admin/couriers";
      if (location.pathname != path) navigate(path);
    };

    return (
      <>
        <Nav className="me-auto"></Nav>
        <Nav>
          <Nav.Link className="text-white" onClick={restaurants}>
            Restaurants
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={scores}>
            Reports
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={couriers}>
            Couriers
          </Nav.Link>
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={logout}>
            Logout
          </Nav.Link>
        </Nav>
      </>
    );
  };

  const courierOptions = () => {
    const logout = () => {
      document.body.style.cursor = "wait";
      authService.logout().then((res) => {
        document.body.style.cursor = "default";
        if (res.status == 200) {
          navigate("/register");
        }
      });
    };

    const courierInfoPage = () => {
      let path = "/courier/details";
      if (location.pathname != path) navigate(path);
    };

    return (
      <>
        <Nav className="me-auto"></Nav>
        <Nav>
          {/*  <Nav.Link className="text-white" onClick={courierInfoPage}>
            My profile
          </Nav.Link>*/}
          <div className="vr text-white"></div>
          <Nav.Link className="text-white" onClick={logout}>
            Logout
          </Nav.Link>
        </Nav>
      </>
    );
  };

  return (
    <>
      <Navbar
        bg="dark"
        variant="dark"
        className="p-2"
        sticky="top"
        style={{ zIndex: 1000 }}
      >
        <Nav className="container-fluid">
          <Nav.Item>
            <Navbar.Brand
              href="#"
              onClick={() => {
                if (location.pathname != "/") navigate("/");
              }}
            >
              <Restaurant></Restaurant> The Convenient Foodie
            </Navbar.Brand>
          </Nav.Item>

          {user == null ? (
            <></>
          ) : user.Role == "CUSTOMER" ? (
            customerOptions()
          ) : user.Role == "ADMINISTRATOR" ? (
            administratorOptions()
          ) : user.Role == "RESTAURANT_MANAGER" ? (
            restaurantOptions()
          ) : (
            courierOptions()
          )}
        </Nav>
      </Navbar>
    </>
  );
}

export default Header;
