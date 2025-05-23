import { Upload } from "@mui/icons-material";
import Button from "@mui/material/Button";
import Checkbox from "@mui/material/Checkbox";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import FormControlLabel from "@mui/material/FormControlLabel";
import TextField from "@mui/material/TextField";
import * as React from "react";
import { useRef, useState } from "react";
import { Figure, Row } from "react-bootstrap";
import defaultImage from "../../images/default.png";
//import { Button, Form, Row, Col, ButtonGroup, Stack } from "react-bootstrap";
import menuService from "../../service/menu.service";
import { useLocation, useNavigate } from "react-router-dom";
import { useEffect } from "react";

export default function AddMenuItem({
  open,
  setOpen,
  menuItems,
  setMenuItems,
  item,
  menuId,
  showAlert,
  setShowAlert,
  alert,
  setAlert,
}) {
  const [menuItemId, setMenuItemId] = useState();
  const [image, setImage] = useState();
  const location = useLocation();
  const navigate = useNavigate();
  const url = new URLSearchParams(location);
  var mounted = false;
  const [menuItem, setMenuItem] = useState({
    name: item?.name || "",
    description: item?.description || "",
    price: item?.price || 0,
    discountPrice: item?.discountPrice || null,
    prepTime: item?.prepTime || 0,
    image: item?.image || null,
  });

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const menuItemId1 = searchParams.get("menuItem");
    // Use the id value in your component logic
    if (menuItemId1 != null) {
      if (!mounted) {
        mounted = true;

        setMenuItemId(menuItemId1);
        setOpen(true);
        document.body.style.cursor = "wait";

        menuService.getMenuItemById(menuItemId1).then((res) => {
          document.body.style.cursor = "default";
          setMenuItem(res.data);
        });
      } else {
      }
    }
   
  }, [location.search]);

  const [discount, setDiscount] = useState(false);
  const [validation, setValidation] = useState({
    name: true,
    description: true,
    price: true,
    discountPrice: true,
    prepTime: true,
    image: true,
  });
  const [valid, setValid] = useState(true);
  const inputRef = useRef(null);

  const handleClose = () => {
    setMenuItem({
      name: "",
      description: "",
      price: 0,
      discountPrice: 0,
      prepTime: null,
      image: null,
    });
    setValidation({
      name: true,
      description: true,
      price: true,
      discountPrice: true,
      prepTime: true,
      image: true,
    });
    if (menuItemId != null) navigate("/menu/add?id=" + menuId);
    setValid(true);
    setOpen(false);
  };

  const handleChange = (e) => {
    setMenuItem({ ...menuItem, [e.target.name]: e.target.value });
  };

  const checkValidation = (i) => {
    let isValid = true;
    let updatedValidation = { ...validation };

    if (
      i.name == null ||
      i.name.length === 0 ||
      i.name.length < 2 ||
      i.name.length > 30
    ) {
      isValid = false;
      updatedValidation.name = false;
    } else {
      updatedValidation.name = true;
    }

    if (i.description != null && i.description.length > 100) {
      isValid = false;
      updatedValidation.description = false;
    } else {
      updatedValidation.description = true;
    }

    if (i.price == null || i.price < 0) {
      isValid = false;
      updatedValidation.price = false;
    } else {
      updatedValidation.price = true;
    }

    if (i.prepTime != null && i.prepTime < 0) {
      isValid = false;
      updatedValidation.prepTime = false;
      updatedValidation.prepTime_error_m = "Cooking time can not be negative!";
    } else if (i.prepTime == null) {
      isValid = false;
      updatedValidation.prepTime = false;
      updatedValidation.prepTime_error_m = "Cooking time must be defined!";
    } else {
      updatedValidation.prepTime = true;
    }

    if (i.discountPrice != null && i.discountPrice < 0) {
      isValid = false;
      updatedValidation.discountPrice = false;
      updatedValidation.discountPrice_error_m =
        "Discount price cannot be negative";
    } else if (
      discount &&
      i.discountPrice != null &&
      i.price != null &&
      i.discountPrice >= i.price
    ) {
      isValid = false;
      updatedValidation.discountPrice = false;
      updatedValidation.discountPrice_error_m =
        "Discounted price should not be higher than the regular price!";
    } else {
      updatedValidation.discountPrice = true;
    }

    setValidation(updatedValidation);
    return { isValid };
  };

  const handleCreate = () => {
    const { isValid } = checkValidation(menuItem);

    if (isValid) {
      document.body.style.cursor = "wait";
      var req = { ...menuItem };
      if (!discount) req.discountPrice = null;
      if (menuItemId != null && menuItemId != undefined) {
        if(image) {
          handleUpload().then((res1)=>{
            if(res1.status==201) {
              setMenuItem({...menuItem,image: res1.data});
              req.image = res1.data;
              menuService.updateMenuItem(menuItemId, req).then((res) => {
                if (res.status == 200) {
                  document.body.style.cursor = "default";
                  setAlert({
                    ...alert,
                    msg: "Successfully updated menu-item!",
                    type: "success",
                  });
                  setShowAlert(true);
                  navigate("/menu/add?id=" + menuId);
                  setMenuItemId(null);
                  const updatedItems = menuItems.map(item =>
                    item.id === menuItemId ? res.data : item
                  );
                  setMenuItems(updatedItems)
                } else {
                  setAlert({ ...alert, msg: res.data, type: "error" });
                  setShowAlert(true);
                }
              });
            } else {
              setAlert({ ...alert, type: "error", msg: res1.data });
              setShowAlert(true);
            }
          })
        }
        
      } else {
        if(image) {
          handleUpload().then((res1)=>{
            setMenuItem({...menuItem,image: res1.data});
            req.image = res1.data;
            if(res1.status==201) {
              menuService.setMenuItems([req], menuId).then((res) => {
                if (res.status == 200) {
                  setMenuItems(res.data.menuItems);
                  document.body.style.cursor = "default";
                  setAlert({
                    ...alert,
                    msg: "Successfully added menu-item!",
                    type: "success",
                  });
                  setShowAlert(true);
              
                } else {
                  setAlert({ ...alert, msg: res.data, type: "error" });
                  setShowAlert(true);
                }
               });
        } else {
          setAlert({ ...alert, type: "error", msg: res1.data });
          setShowAlert(true);
        }});
      }

      setMenuItem({
        name: "",
        description: "",
        price: 0,
        discountPrice: 0,
        prepTime: null,
        image: null,
      });
      setOpen(false);
      }
    }
  };

  const uploadImage = () => {
    inputRef.current?.click();
  };

  const onFileChange = (e) => {
    let files = e.target.files;
    let fileReader = new FileReader();
    fileReader.readAsDataURL(files[0]);
    setImage(files[0])

    fileReader.onload = (event) => {
      setMenuItem({ ...menuItem, image: event.target.result });
    };
  };

  const handleUpload = () => {

    const formDataImage = new FormData();
    formDataImage.append('file', image);

    return menuService.uploadImage(formDataImage);
};

  return (
    <div>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>
          {" "}
          {menuItemId == null ? "Add a menu item" : "Update a menu item"}
        </DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="normal"
            name="name"
            id="name"
            label="Name"
            value={menuItem.name}
            type="text"
            fullWidth
            variant="standard"
            required
            error={!validation.name}
            helperText={
              !validation.name
                ? "Menu item name must be between 2 and 30 characters!"
                : ""
            }
            onChange={handleChange}
          />
          <TextField
            margin="normal"
            name="description"
            id="description"
            label="Description"
            type="text"
            fullWidth
            value={menuItem.description}
            multiline
            rows={4}
            helperText={
              !validation.description
                ? "Menu item description can contain a maximum of 100 characters!"
                : ""
            }
            error={!validation.description}
            onChange={handleChange}
          />
          <br></br>
          <br></br>
          <TextField
            className="col-5"
            type="number"
            label="Original price"
            variant="standard"
            id="discount"
            name="price"
            value={menuItem?.price}
            required
            error={!validation.price}
            inputProps={{
              min: 0,
              step: "0.5",
            }}
            helperText={
              !validation.price ? "Menu item price must be defined!" : ""
            }
            onChange={(event) => {
              const inputValue = event.target.value;
              const formattedValue = Number(parseFloat(inputValue).toFixed(2));
              setMenuItem({
                ...menuItem,
                price: formattedValue,
              });
            }}
          />
          <br></br>
          <div className="p-0 mt-3" style={{ position: "relative" }}>
            <TextField
              className="col-5"
              type="number"
              label="Cooking time"
              variant="standard"
              id="prepTime"
              name="prepTime"
              required
              error={!validation.prepTime}
              inputProps={{ min: 0 }}
              helperText={
                !validation.prepTime ? validation.prepTime_error_m : ""
              }
              value={menuItem?.prepTime || 0}
              onChange={(event) => {
                const inputValue = event.target.value;
                const formattedValue = Number(
                  parseFloat(inputValue).toFixed(2)
                );
                setMenuItem({
                  ...menuItem,
                  prepTime: formattedValue,
                });
              }}
            />
            <span
              className="col-2 px-2"
              style={{ position: "absolute", bottom: "0" }}
            >
              min
            </span>
          </div>
          <br></br>
          <div>
            <TextField
              style={{ verticalAlign: "bottom" }}
              className="col-5"
              type="number"
              variant="standard"
              id="discount"
              label="Discounted price"
              name="discountPrice"
              disabled={!discount}
              error={!validation.discountPrice}
              helperText={
                discount && !validation.discountPrice
                  ? validation.discountPrice_error_m
                  : ""
              }
              inputProps={{
                min: 0,
                step: "0.5",
              }}
              value={menuItem?.discountPrice}
              onChange={(event) => {
                const inputValue = event.target.value;
                const formattedValue = Number(
                  parseFloat(inputValue).toFixed(2)
                );
                setMenuItem({
                  ...menuItem,
                  discountPrice: formattedValue,
                });
              }}
            />
            <FormControlLabel
              className="col-6"
              style={{ marginLeft: "10px" }}
              control={
                <Checkbox
                  defaultChecked={discount}
                  onChange={(e) => {
                    setDiscount(!discount);
                    if (!e.target.checked) {
                      setMenuItem({ ...menuItem, discountPrice: null });
                    }
                  }}
                />
              }
              label="Discounted price"
            />
          </div>
          <br></br>
          <br></br>
          <input
            ref={inputRef}
            className="d-none"
            type="file"
            onChange={onFileChange}
            accept="image/*"
          />
          <Button
            style={{ width: "200px", background: "#606060", color: "black" }}
            onClick={uploadImage}
          >
            Choose an image
            <Upload style={{ marginLeft: "10px" }} />
          </Button>
          <br></br>

          <Figure className="mt-1 ms-4" style={{ width: "150px" }}>
            <Figure.Image
              style={{ height: "150px" }}
              src={menuItem.image ? `${menuItem.image} ` : defaultImage}
            />
          </Figure>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleCreate}>
            {menuItemId == null ? "Create" : "Update"}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
