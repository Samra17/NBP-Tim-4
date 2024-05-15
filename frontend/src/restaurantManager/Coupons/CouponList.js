import React, { useEffect, useState } from "react";
import discountService from "../../service/discount.service";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import CustomAlert from "../../shared/util/Alert";


function CouponList() {
  const [coupons, setCoupons] = useState([]);
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const perPage = 5;

  /*
  useEffect(()=> {
    if(!mounted) {
      mounted = true;
    }

    discountService.getAllCouponsForRestaurant(1, perPage).then((res) => {
      setLoading(false);
      if (res.status == 200)  {
      setCoupons(res.data.coupons);
      }
      else {
        setAlert({ ...alert, msg: [res.data], type: "error" });
        setShowAlert(true);
      }
    })

  },[])
    */
  async function handlePagination(title, page, perPage, setTotalPages,setContainerLoad, filterData) {

      discountService.getAllCouponsForRestaurant(page, perPage).then((res) => {
        setContainerLoad(false);
        if (res.status == 200)  {
        setCoupons(res.data.couponResponse);
        setTotalPages(res.data.totalPages);
        }
        else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
      })

  }

  

  return (
    <div>
      <CustomAlert
          setShow={setShowAlert}
          show={showAlert}
          type={alert.type}
          msg={alert.msg}
        ></CustomAlert>
          <ListContainer
            title={"Coupons"}
            type="coupon"
            grid={false}
            items={coupons}
            setItems={setCoupons}
            perPage={perPage}
            pagination="server"
            handlePagination={handlePagination}
            setAlert={setAlert}
            setShowAlert={setShowAlert}
            alert={alert}
          />
    </div>
  );
}

export default CouponList;
