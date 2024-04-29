import React from "react";
import Restaurants from "../../customer/Home/Restaurants";
import authService from "../../service/auth.service";

function Home() {
  const user = authService.getCurrentUser();

  return (
    <>
      {user.role == "CUSTOMER" ? (
        <Restaurants></Restaurants>
      ) : user.role == "ADMINISTRATOR" ? (
        <>ADMINISTRATOR</>
      ) : user.role == "COURIER" ? (
        <>COURIER</>
      ) : user.role == "RESTAURANT_MANAGER" ? (
        <>RESTAURANT_MANAGER</>
      ) : (
        <></>
      )}
    </>
  );
}

export default Home;
