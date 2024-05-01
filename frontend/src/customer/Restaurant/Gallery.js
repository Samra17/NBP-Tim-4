import React, { useEffect,useState } from 'react';
import { Carousel } from 'react-bootstrap';
import default_img from "../../images/output-onlinejpgtools.jpg"
import restaurantService from '../../service/restaurant.service';

function Gallery({ id }) {
    const [images, setImages] = useState();

    var mounted = false
    useEffect(() => {
        if (!mounted) {
            mounted = true;
            restaurantService.getRestaurantImages(id).then((res) => {
                console.log(res)
                if (res.status == 200) {

                    setImages(res.data.map(i => i.imageData))
                }
            })
        }
    }, [])

    return (
        <Carousel fade>
            
                {images && images.length > 0 ?
                    (images.map((image) =>
                        <Carousel.Item interval={10000}>
                            <img
                                style={{ height: 300 }}
                                className="d-block w-100"
                                src={image}
                                alt="First slide"
                            />
                        </Carousel.Item>
                    )) :
                    <Carousel.Item interval={10000}>
                        <img
                            style={{ height: 300 }}
                            className="d-block w-100"
                            src={default_img}
                            alt="First slide"
                        />

                    </Carousel.Item>}
        </Carousel>
    )
}

export default Gallery