import React from "react";
import Restaurants from "../../customer/Home/Restaurants";
import authService from "../../service/auth.service";
import RestaurantOrders from "../../restaurantManager/Home/RestaurantOrders";
import Orders from "../../courier/Orders";

function Home() {
  const user = authService.getCurrentUser();

  return (
    <>
      {user.role == "CUSTOMER" ? (
        <Restaurants></Restaurants>
      ) : user.role == "ADMINISTRATOR" ? (
        <>ADMINISTRATOR</>
      ) : user.role == "COURIER" ? (
       <Orders></Orders> 
      ) : user.role == "RESTAURANT_MANAGER" ? (
        <RestaurantOrders></RestaurantOrders>
      ) : (
        <></>
      )}
    </>
  );
}

export default Home;
