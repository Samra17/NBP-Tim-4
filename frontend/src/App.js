import "./App.css";

import { Route, Routes } from "react-router-dom";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import Home from "./shared/Home/Home";
import PrivateRoute from "./shared/PrivateRoute/PrivateRoute";
import RegisterPage from "./shared/RegisterPage/RegisterPage";
import NotFound from "./shared/util/NotFound";
import CustomerDetails from "./customer/CustomerDetails/CustomerDetails";
import RestaurantDetails from "./restaurantManager/RestaurantDetails/RestaurantDetails";
import RestaurantOverview from "./customer/Restaurant/RestaurantOverview";

function App() {
  return (
    <>
      <Routes>
        <Route path="/register" element={<RegisterPage></RegisterPage>} />
        <Route path="*" element={<NotFound></NotFound>} />
        <Route path="/" element={<PrivateRoute />}>
          <Route path="/" element={<Home></Home>}></Route>
          <Route
            path="/customer/details"
            element={<CustomerDetails></CustomerDetails>}
          ></Route>
          <Route
            path="/customer/restaurant/favorites"
            element={<CustomerDetails></CustomerDetails>}
          ></Route>
          <Route
            path="/restaurant/menus"
            element={<RestaurantDetails></RestaurantDetails>}
          ></Route>
          <Route
            path="/customer/restaurant"
            element={<RestaurantOverview></RestaurantOverview>}
          ></Route>
        </Route>
      </Routes>
    </>
  );
}

export default App;
