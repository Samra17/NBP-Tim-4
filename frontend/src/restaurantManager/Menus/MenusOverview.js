import { useEffect, useState } from "react";
import { Spinner } from "react-bootstrap";
import menuService from "../../service/menu.service";
import CustomAlert from "../../shared/util/Alert";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import Loader from "../../shared/util/Loader/Loader";

function MenusOverview() {
  var mounted = false;
  const [loading, setLoading] = useState(true);
  const [menus, setMenus] = useState([]);
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  useEffect(() => {
    if (!mounted) {
      mounted = true;
      menuService.getAllRestaurantMenus().then((res) => {
        if (res.status == 200) {
          setMenus(res.data);
          setLoading(false);
          console.log(res.data);
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });
    }
  }, []);
  return (
    <Loader isOpen={loading}>
      <CustomAlert
        setShow={setShowAlert}
        show={showAlert}
        type={alert.type}
        msg={alert.msg}
      ></CustomAlert>
      {menus ? (
        <ListContainer
          items={menus}
          setItems={setMenus}
          title={"Menus"}
          showFilters={false}
          perPage={5}
          type={"menus"}
          grid={false}
          alert={alert}
          setAlert={setAlert}
          showAlert={showAlert}
          setShowAlert={setShowAlert}
          setLoadingPage={setLoading}
        ></ListContainer>
      ) : (
        <div style={{ display: "flex", justifyContent: "center" }}>
          <Spinner
            animation="border"
            style={{ color: "white", marginTop: "20%" }}
          />
        </div>
      )}
    </Loader>
  );
}

export default MenusOverview;
