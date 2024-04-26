import React, { useEffect, useState } from "react";
import authService from "../../service/auth.service";
import discountService from "../../service/discount.service";
import restaurantService from "../../service/restaurant.service";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import Loader from "../../shared/util/Loader/Loader";

function CouponList() {
  const [loading, setLoading] = useState(true);
  const [coupons, setCoupons] = useState();
  const [restaurantUuid, setRestaurantUuid] = useState();
  const user = authService.getCurrentUser();

  useEffect(() => {
    restaurantService.getManagersRestaurantUUID().then((res) => {
      console.log("ispod gledam");
      setRestaurantUuid(res.data);
      console.log(res.data);
      console.log(user);
      discountService.getAllCouponsForRestaurant(res.data).then((resp) => {
        if (resp.status == 200) {
          setCoupons(resp.data);
          setLoading(false);
          console.log(resp.data);
        }
      });
    });
  }, []);

  return (
    <div>
      <Loader isOpen={loading}>
        {coupons ? (
          <ListContainer
            title={"Active coupons"}
            type="coupon"
            grid={false}
            items={coupons}
            setItems={setCoupons}
            perPage={5}
            restaurantUuid={restaurantUuid}
          />
        ) : (
          <></>
        )}
      </Loader>
    </div>
  );
}

export default CouponList;
