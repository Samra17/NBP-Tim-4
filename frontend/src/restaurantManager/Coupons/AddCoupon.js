import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import InputAdornment from "@mui/material/InputAdornment";
import TextField from "@mui/material/TextField";
import * as React from "react";
import { useState } from "react";
import authService from "../../service/auth.service";
import discountService from "../../service/discount.service";


export default function AddCoupon({
  open,
  setOpen,
  coupons,
  setCoupons,
  setShowAlert,
  setAlert,
  alert
}) {
  const user = authService.getCurrentUser();
  const [newCoupon, setnewCoupon] = useState({
    code: "",
    discountPercent: 0,
    quantity: 0
  });
  const [validation, setValidation] = useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const handleCreate = () => {
    setOpen(false);
    discountService.addCoupon(newCoupon).then((res) => {
      if (res.status == 201) {
        setCoupons((oldArray) => [...oldArray, newCoupon]);
      } else {
        setAlert({ ...alert, msg: res.data, type: "error" });
        setShowAlert(true);
      }
    });
  };

  return (
    <div>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add coupon</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="normal"
            id="code"
            label="Coupon code"
            type="text"
            fullWidth
            variant="standard"
            helperText="The code needs to be between 3 and 100 characters long!"
            error={!validation}
            onChange={(e) => {
              if (e.target.value.length > 2 && e.target.value.length <= 100 ) {
                setValidation(true);
                setnewCoupon({ ...newCoupon, ...{ code: e.target.value } });
              } else setValidation(false);
            }}
          />

          <TextField
            label="Discount"
            variant="standard"
            id="discount"
            InputProps={{
              endAdornment: <InputAdornment position="end">%</InputAdornment>,
            }}
            onChange={(e) => {
              setnewCoupon({
                ...newCoupon,
                ...{ discountPercent: e.target.value },
              });
            }}
          />
          <br></br>
          <TextField
            autoFocus
            endAdornment={<InputAdornment position="end">%</InputAdornment>}
            margin="dense"
            id="quantity"
            label="Quantity"
            type="number"
            variant="standard"
            onChange={(e) => {
              setnewCoupon({ ...newCoupon, ...{ quantity: e.target.value } });
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleCreate}>Create</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
