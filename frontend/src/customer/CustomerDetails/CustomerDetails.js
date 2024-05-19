import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import authService from "../../service/auth.service";
import NotFound from "../../shared/util/NotFound";
import MainContainer from "../../shared/util/RightSideContainer/MainContainer";
import Sidebar from "../../shared/util/Sidebar/Sidebar";
import FavoriteRestaurants from "./FavoriteRestaurants";
import OrderHistory from "./OrderHistory";
import UserInformation from "./UserInformation";

function CustomerDetails() {
  const options = new Map([
    ["My information", "/customer/details"],
    ["Favorite restaurants", "/customer/restaurant/favorites"],
    ["Order history", "/customer/order/history"],
  ]);
  const icons = new Map([
    ["My information", "user"],
    ["Favorite restaurants", "heart"],
    ["Order history", "receipt"],
  ]);
  const user = authService.getCurrentUser();
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();

  return (
    <div>
      {user.Role != "CUSTOMER" ? (
        <NotFound />
      ) : (
        <Sidebar
          optionsMap={options}
          iconsMap={icons}
          collapsed={collapsed}
          setCollapsed={setCollapsed}
        />
      )}
      <MainContainer collapsed={collapsed}>
        {location.pathname == "/customer/details" ? (
          <UserInformation />
        ) : location.pathname == "/customer/restaurant/favorites" ? (
          <FavoriteRestaurants />
        ) : (
          <OrderHistory />
        )}
      </MainContainer>
    </div>
  );
}

export default CustomerDetails;
