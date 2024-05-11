import { Upload } from "@mui/icons-material";
import React, { useEffect, useRef, useState } from "react";
import { Button, Card, Col, Row } from "react-bootstrap";
import { useLocation } from "react-router-dom";
import restaurantService from "../../service/restaurant.service";
import CustomAlert from "../../shared/util/Alert";
import Loader from "../../shared/util/Loader/Loader";

function RestaurantGallery() {
  const [loading, setLoading] = useState();
  const [images, setImages] = useState([]);
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const { state } = useLocation();
  const inputRef = useRef(null);

  var mounted = false;
  useEffect(() => {
    if (!mounted) {
      setLoading(true);
      mounted = true;
      console.log(state);
      restaurantService.getRestaurantImages(state).then((res) => {
        setLoading(false);
        if (res.status == 200) {
          console.log(res.data);
          setImages(res.data);
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });
    }
  }, []);

  const onFileChange = (e) => {
    let files = e.target.files;
    let fileReader = new FileReader();
    fileReader.readAsDataURL(files[0]);

    const formDataImage = new FormData();
    formDataImage.append('file', files[0]);

    fileReader.onload = (event) => {
      setLoading(true);
      restaurantService
        .addImageToRestaurantGallery(formDataImage)
        .then((res) => {
          setLoading(false);
          if (res.status == 201) {
            setImages([
              { imageData: event.target.result, id: res.data },
              ...images,
            ]);
          } else {
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
          }
        });
    };
  };

  const uploadImage = () => {
    inputRef.current?.click();
  };

  const deleteImage = (e) => {
    setLoading(true);
    restaurantService
      .deleteImageFromRestaurantGallery(e.target.id)
      .then((res) => {
        setLoading(false);
        if (res.status == 204) {
          setImages(images.filter((i) => i.id != e.target.id));
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });
  };

  return (
    <Loader isOpen={loading}>
      <CustomAlert
        setShow={setShowAlert}
        show={showAlert}
        type={alert.type}
        msg={alert.msg}
      ></CustomAlert>
      <Row>
        <Col className="col-9 mx-2 mt-2">
          <h2>Image gallery</h2>
        </Col>
        <Col className="col-2 mt-3">
          <input
            ref={inputRef}
            className="d-none"
            type="file"
            onChange={onFileChange}
            accept="image/*"
          />
          <Button
            style={{ width: "100px", float: "right" }}
            onClick={uploadImage}
          >
            Add
            <Upload
              style={{ marginLeft: "5px", marginBottom: "3px", padding: "2px" }}
            />
          </Button>
        </Col>
      </Row>
      <hr></hr>
      <Row xs={1} md={1} className="g-1">
        {images.map((image) => (
          <Col className="p-2">
            <Card
              style={{ width: "100%", height: "200px", overflow: "hidden" }}
            >
              <Card.Img
                variant="top"
                src={image.imageData}
                style={{ objectFit: "cover", height: "200px", width: "100%" }}
              />
              <Card.ImgOverlay>
                <Button
                  id={image.id}
                  style={{
                    position: "absolute",
                    bottom: 5,
                    width: "100px",
                    background: "#fe724c",
                    border: "#fe724c",
                  }}
                  onClick={deleteImage}
                >
                  Delete
                </Button>
              </Card.ImgOverlay>
            </Card>
          </Col>
        ))}
      </Row>
    </Loader>
  );
}

export default RestaurantGallery;
