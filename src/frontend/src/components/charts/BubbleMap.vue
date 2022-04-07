
<!-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  Documentation:
  
  - leaflet:
    https://leafletjs.com/
    https://vue2-leaflet.netlify.app/

  - leaflet.markercluster:
    https://github.com/Leaflet/Leaflet.markercluster
    https://github.com/jperelli/vue2-leaflet-markercluster


    Input array 'points' must have at least these fields:
      
      name
      latitude
      longitude
      size      

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ -->

<template>

    <div class="oa-map-wrapper">

      <v-map ref="mapRef" :options="mapOptions" 
        :zoom="initialZoom" :minZoom="minZoom" :maxZoom="maxZoom" 
        :center="centerLocation"
      >
      
        <v-tile-layer url="https://{s}.tile.osm.org/{z}/{x}/{y}.png" :attribution="attribution" />
        <v-control-scale position="topright" :imperial="false" :metric="true"/>
        <v-control-zoom position="bottomright" />
        <v-control class="oa-map-caption" position="topleft">{{title}}</v-control>

        <v-marker-cluster ref="clusterRef" :options="clusterOptions">
          <v-marker
            v-for="point in points"
            :key="getKey(point)"
            :lat-lng="latLng(point.latitude,point.longitude)"
            :options="{weight: point.size}">
            <v-popup :content="popupText(point)" />
          </v-marker>
        </v-marker-cluster>
      </v-map>

    </div>

</template>

<script>

import { latLng, DivIcon, Point } from "leaflet";
import * as Vue2Leaflet from "vue2-leaflet";
import Vue2LeafletMarkercluster from "vue2-leaflet-markercluster";

export default {

  components: {
    "v-map": Vue2Leaflet.LMap,
    "v-tile-layer": Vue2Leaflet.LTileLayer,
    "v-control": Vue2Leaflet.LControl,
    "v-control-scale": Vue2Leaflet.LControlScale,
    "v-control-zoom": Vue2Leaflet.LControlZoom,
    "v-marker": Vue2Leaflet.LMarker,
    "v-popup": Vue2Leaflet.LPopup,
    "v-marker-cluster": Vue2LeafletMarkercluster,
  }, 

  props: {
    title: {type: String, default: ""},
    points: {type: Array},
    centerLocation: {type: Object, default() { return latLng(41.8, 9.5)} }, // world center
    initialZoom: {type: Number, default: 2},
    minZoom: {type: Number, default: 2},
    maxZoom: {type: Number, default: 8},
    // heaviest non-clustered circle should have an on screen diameter of circleSizePixels
    circleSizePixels: {type: Number, default: 150} // pixels
  },

  data() {
    return {
      mapOptions: {zoomControl: false},
      attribution:
        '&copy; <a target="_blank" href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      clusterOptions: {
        singleMarkerMode: true,
        showCoverageOnHover: false,
        spiderfyOnMaxZoom: false,
        iconCreateFunction: this.createCircle,
      },
      maxWeight: 1,
      scalingFactor: 0.1
    };
  },

  updated: function() {

    this.maxWeight = this.points.reduce((m, p) => Math.max(m, p.size), 0)
    this.scalingFactor = this.circleSizePixels / Math.sqrt(this.maxWeight)
    console.log(`MAXWEIGHT: ${this.maxWeight}, SCALING: ${this.scalingFactor}`)
    // data changed, redraw 
    this.$refs.clusterRef.mapObject.refreshClusters()
    // data changed, re-center
    this.$refs.mapRef.mapObject.panTo(this.centerLocation)
  },

  methods: {

    latLng,  // import eponymous function from Leaflet

    popupText(point) {
      return `<div class="oa-marker-popup">${point.name}<br/><b>${point.size}</b></div>`
    },

    getKey() { // ignore point value
      return Math.floor(Math.random()*10000000)
    },

    createCircle(cluster) {

      const weight = this.getClusterWeight(cluster);
      // console.log("WEIGHT " + weight);
      return new DivIcon({
        html: this.getCircleAsHtml(weight),
        className: "oa-marker-icon",
        iconSize: new Point(0, 0),
      });
    },

    getClusterWeight(cluster) {

      return cluster
        .getAllChildMarkers()
        .reduce((acc, c) => acc + c.options.weight, 0);
    },

    getCircleAsHtml(weight) {
      
      const mWeight = Math.sqrt(weight) * this.scalingFactor // TODO do not calculate every time
      let diameter, clazz

      // set circle properties
      if (mWeight >= this.circleSizePixels*.9) {
        diameter  = mWeight
        clazz = "mb-large"
      }
      else if (mWeight > this.circleSizePixels*.09) {
        diameter  = Math.max(30,mWeight)
        clazz = "mb-middle"
      }
      else if (mWeight > this.circleSizePixels*.009) {
        diameter  = Math.max(30,mWeight)
        clazz = "mb-small"
      }
      else {
        diameter  = 20
        clazz = "mb-tiny"
      }

      const radius = diameter / 2;

      const markerbox =
          `<div class="oa-marker-box ${clazz}" mweight="${mWeight}" style="`
        +   `width:${diameter}px;` 
        +   `height:${diameter}px;` 
        +   `margin:-${radius}px 0 0 -${radius}px;` 
        + `">` 
        +     `<div class="oa-marker-circle" style="`
        +       `border-radius:${radius}px;` 
        +     `"></div>`
        +     `<div class="oa-marker-caption" style="`
        +       `line-height:${diameter}px;`
        +     `">${weight}</div>`
        + `</div>`

      return markerbox;
    },
  },
};
</script>

<style lang="scss">

  @import "~leaflet/dist/leaflet.css";
  @import "~leaflet.markercluster/dist/MarkerCluster.css";
  @import "~leaflet.markercluster/dist/MarkerCluster.Default.css";

  html, body {
    height: 100%;
    margin: 0;
  }

  .oa-map-wrapper {
    position:relative;
    height:100%;
    z-index: 1;
  }

  .oa-map-caption {
     
     padding: 4px 10px; 
     font-size: 14px; 
     font-weight: 900;
     background: #fffc;
  }

  .oa-marker-box {
    position: relative;
  }  

  .oa-marker-circle {

    position: absolute;
    top: 0; left: 0;
    width:100%; height:100%;
    text-align: center;
    color: white;
    font-weight: bold;
    color:white; 
    opacity:.5;
    box-shadow: 0px 0px 30px #fff8;
    
    @at-root .mb-tiny   & { background: rgb(0, 104, 224) }
    @at-root .mb-small  & { background: rgb(0, 0, 255) }
    @at-root .mb-middle & { background: rgb(148, 0, 192) }
    @at-root .mb-large  & { background: rgb(192, 0, 16) }
  }

  .oa-marker-caption {

    position: absolute;
    top: 0; left: 0;
    width: 140%; height: 100%;
    text-align: center;
    margin-left: -20%;
    color:white;
    font-weight: bold;
    text-shadow: 0 0 2px #000;

    @at-root .mb-tiny   & { font-size: 0 }
    @at-root .mb-small  & { font-size: 0 }
    @at-root .mb-middle & { font-size: 1em }
    @at-root .mb-large  & { font-size: 1em }
  } 

  .oa-map-popup {

    text-align: center;
    color: navy;
  }
  
</style>