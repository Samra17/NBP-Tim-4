import React from 'react'
import { useState,useRef,useEffect } from 'react';
import maplibregl from 'maplibre-gl';
import { GeocodingControl } from "@maptiler/geocoding-control/maplibregl";
import * as maptilersdk from '@maptiler/sdk';
import "@maptiler/sdk/dist/maptiler-sdk.css";

import './Map.css'
export default function Map({setAddress}) {
    const mapContainer = useRef(null);
    const map = useRef(null);
    const [API_KEY] = useState('PX9OLKTez43ZizU6Cyjv');
  
    useEffect(() => {
        if (map.current) return;
        map.current = new maplibregl.Map({
        container: mapContainer.current, 
        style: "https://api.maptiler.com/maps/streets/style.json?key=" + 'PX9OLKTez43ZizU6Cyjv',
        center: [ 18.4131,43.8563],
        zoom: 11,
        });

       
        const gc = new GeocodingControl({
            apiKey: API_KEY,
            class: 'geocoder',
            showResultsWhileTyping: true,
            country:"BA",
            noResultsMessage:"Search is restricted to Bosnia and Herzegovina."
          });
          gc.addEventListener("pick", (e) => {
            if(e.detail!=null) {
            setAddress(e.detail.place_name,e.detail.center[1] + ", " + e.detail.center[0])
            
            } else {
                setAddress("","")
                
            }
          });
          map.current.addControl(gc, 'top-left');
          map.current.addControl(new maplibregl.NavigationControl(), 'top-right');

    
  
    });
  
    return (
      <div className="map-wrap">
        <div ref={mapContainer} className="map" />
      </div>
    );
}
