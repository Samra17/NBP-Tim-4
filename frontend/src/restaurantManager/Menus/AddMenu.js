import { Add } from "@mui/icons-material";
import { left } from "@popperjs/core";
import { React, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import menuService from "../../service/menu.service";
import restaurantService from "../../service/restaurant.service";
import CustomAlert from "../../shared/util/Alert";
import AddMenuItem from "./AddMenuItem";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import { useEffect } from "react";
import Loader from "../../shared/util/Loader/Loader";

function AddMenu() {
  var mounted = false;
  const [formData, setFormData] = useState({});
  const [active, setActive] = useState(false);
  const [menuItems, setMenuItems] = useState([]);
  const [open, setOpen] = useState(false);
  const [validated, setValiated] = useState(false);
  const [showAlert, setShowAlert] = useState(false);
  const [alert, setAlert] = useState({});
  const navigate = useNavigate();
  const id = useParams();
  const [loading, setLoading] = useState(true);
  const [idMenu, setIdMenu] = useState();
  const [menuItemId, setMenuItemId] = useState();
  const location = useLocation();
  const url = new URLSearchParams(location);
  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get("id");

    // Use the id value in your component logic
    if (id != null) {
      if (!mounted) {
        mounted = true;
        setIdMenu(id);

        menuService.getMenuById(id).then((res) => {
          if (res.status == 200) {
            setFormData(res.data);
            setLoading(false);
            setActive(res.data.active);
            setMenuItems(res.data.menuItems);
          } else {
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
          }
        });
      }
    } else setLoading(false);
  }, [location.search]);

  const handleSubmit = (e) => {
    const form = e.currentTarget;
    e.preventDefault();
    e.stopPropagation();
    const restaurantUUID = restaurantService.getCurrentRestaurantUUID();
    document.body.style.cursor = "wait";
    if (idMenu) {
      menuService
        .updateMenu({ ...formData, restaurant_uuid: restaurantUUID }, idMenu)
        .then((res) => {
          if (res.status == 201) {
            document.body.style.cursor = "default";
            setAlert({
              ...alert,
              msg: "Successfully updated menu!",
              type: "success",
            });
            setShowAlert(true);
          } else {
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
          }
        });
    } else {
      menuService
        .addMenu({ ...formData, restaurant_uuid: restaurantUUID })
        .then((res) => {
          if (res.status == 201) {
            document.body.style.cursor = "default";
            setAlert({
              ...alert,
              msg: "Successfully added menu!",
              type: "success",
            });
            setShowAlert(true);
            const timeout = setTimeout(() => {
              navigate("/restaurant/menus");
            }, 3000);
          } else {
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
          }
        });
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };
  return (
    <Loader isOpen={loading}>
      <div>
        <CustomAlert
          setShow={setShowAlert}
          show={showAlert}
          type={alert.type}
          msg={alert.msg}
        ></CustomAlert>

        <AddMenuItem
          open={open || menuItemId != undefined}
          setOpen={setOpen}
          menuItems={menuItems}
          setMenuItems={setMenuItems}
          menuId={idMenu}
          showAlert={showAlert}
          setShowAlert={setShowAlert}
          alert={alert}
          setAlert={setAlert}
        ></AddMenuItem>

        <Row>
          <Col>
            <Form
              validated={validated}
              // onSubmit={(e) => handleSubmit(e)}
              style={{ padding: "20px" }}
            >
              <Form.Group className="mb-1" controlId="formGroupActive">
                <Form.Check
                  data-toggle="toggle"
                  style={{
                    marginTop: "40px",
                    float: "right",
                    marginRight: "20%",
                  }}
                  label="Active"
                  name="active"
                  type={"checkbox"}
                  checked={active}
                  onChange={(e) => {
                    setActive(!active);
                    setFormData({ ...formData, active: !active });
                  }}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formGroupFirstname">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  required
                  type="text"
                  name="name"
                  value={formData?.name}
                  onChange={handleChange}
                  style={{ width: "60%" }}
                />
              </Form.Group>

              {idMenu ? (
                <div>
                  <h2
                    style={{
                      textAlign: "start",
                      float: left,
                      marginTop: "10px",
                      marginLeft: "16px",
                    }}
                  >
                    Menu items
                  </h2>
                  <div
                    style={{
                      float: "right",
                      marginTop: "10px",
                      marginRight: "16px",
                    }}
                  >
                    <Button
                      style={{
                        clear: left,
                        textAlign: "center",
                        width: "fit-content",
                        height: "40px",
                      }}
                      class="rounded"
                      onClick={() => setOpen(true)}
                    >
                      Add a menu item <Add></Add>
                    </Button>
                  </div>

                  <ListContainer
                    items={menuItems}
                    setItems={setMenuItems}
                    showFilters={false}
                    perPage={5}
                    type={"menuItems"}
                    grid={false}
                    setLoadingPage={setLoading}
                    setAlert={setAlert}
                    showAlert={showAlert}
                    alert={alert}
                    setShowAlert={setShowAlert}
                  ></ListContainer>
                </div>
              ) : (
                <div></div>
              )}

              <Button
                type="submit"
                className="mb-3"
                style={{
                  width: "fit-content",
                  backgroundColor: "#fe724c",
                  border: "#fe724c",
                }}
                onClick={handleSubmit}
              >
                Submit
              </Button>
            </Form>
          </Col>
        </Row>
      </div>
    </Loader>
  );
}

export default AddMenu;
