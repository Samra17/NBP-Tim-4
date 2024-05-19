import React from "react";
import Restaurants from "../../customer/Home/Restaurants";
import authService from "../../service/auth.service";
import RestaurantOrders from "../../restaurantManager/Home/RestaurantOrders";
import Orders from "../../courier/Orders";
import AdminRestaurants from "../../admin/Home/AdminRestaurants";

function Home() {
  const user = authService.getCurrentUser();
  console.log(user)

  return (
    <>
      {user.Role == "CUSTOMER" ? (
        <Restaurants></Restaurants>
      ) : user.Role == "ADMINISTRATOR" ? (
        <AdminRestaurants></AdminRestaurants>
      ) : user.Role == "COURIER" ? (
       <Orders></Orders> 
      ) : user.Role == "RESTAURANT_MANAGER" ? (
        <RestaurantOrders></RestaurantOrders>
      ) : (
        <></>
      )}
    </>
  );
}

export default Home;
