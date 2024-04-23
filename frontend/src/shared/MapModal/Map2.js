import React from "react";
import { useState, useRef, useEffect } from "react";
import maplibregl from "maplibre-gl";
import { Container } from "react-bootstrap";
import * as maptilersdk from "@maptiler/sdk";
import "@maptiler/sdk/dist/maptiler-sdk.css";

import "./Map.css";
export default function Map2({ restaurantLocations }) {
  const mapContainer = useRef(null);
  const map = useRef(null);
  const [API_KEY] = useState("PX9OLKTez43ZizU6Cyjv");

  useEffect(() => {
    if (map.current) return;

    const locations = restaurantLocations
      .filter((rl) => rl.mapCoordinates != null)
      .map((rl) => ({
        type: "Feature",
        properties: { description: "<strong>" + rl.name + "</strong>" },
        geometry: {
          type: "Point",
          coordinates: [
            rl.mapCoordinates.split(",")[1],
            rl.mapCoordinates.split(",")[0],
          ],
        },
      }));

    console.log(locations);

    map.current = new maplibregl.Map({
      container: mapContainer.current,
      style:
        "https://api.maptiler.com/maps/streets/style.json?key=" +
        "PX9OLKTez43ZizU6Cyjv",
      center: [18.407728349309792, 43.86060380521191],
      zoom: 12,
    });

    map.current.on("load", function () {
      map.current.loadImage(
        "https://docs.maptiler.com/sdk-js/assets/custom_marker.png",
        // Add an image to use as a custom marker
        function (error, image) {
          if (error) throw error;
          map.current.addImage("custom-marker", image);

          map.current.addSource("places", {
            type: "geojson",
            data: {
              type: "FeatureCollection",
              features: locations,
            },
          });

          map.current.addLayer({
            id: "places",
            type: "symbol",
            source: "places",
            layout: {
              "icon-image": "custom-marker",
              "icon-overlap": "always",
            },
          });
        }
      );

      map.current.addControl(new maplibregl.NavigationControl(), "top-right");

      var popup = new maptilersdk.Popup({
        closeButton: false,
        closeOnClick: false,
      });

      map.current.on("mouseenter", "places", function (e) {
        // Change the cursor style as a UI indicator.
        map.current.getCanvas().style.cursor = "pointer";

        var coordinates = e.features[0].geometry.coordinates.slice();
        var description = e.features[0].properties.description;

        // Ensure that if the map is zoomed out such that multiple
        // copies of the feature are visible, the popup appears
        // over the copy being pointed to.
        while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
          coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
        }

        // Populate the popup and set its coordinates
        // based on the feature found.

        popup.setLngLat(coordinates).setHTML(description).addTo(map.current);
      });

      map.current.on("mouseleave", "places", function () {
        map.current.getCanvas().style.cursor = "";
        popup.remove();
      });
    });
  });

  return (
    <Container
      style={{
        backgroundColor: "#F5F5F4",
        borderRadius: "5px",
        width: "100%",
        minWidth: "35rem",
        marginTop: "20px",
      }}
      className="container-fluid"
    >
      <div className="map-wrap">
        <div ref={mapContainer} className="map" />
      </div>
    </Container>
  );
}
