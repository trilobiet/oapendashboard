<template>

  <v-map :zoom="initialZoom" :center="centerLocation" :minZoom="minZoom" :maxZoom="maxZoom">
   
    <v-tilelayer url="https://{s}.tile.osm.org/{z}/{x}/{y}.png"></v-tilelayer>

    <v-marker-cluster :options="clusterOptions" @clusterclick="clusterclick" @ready="ready">
      <v-marker
         v-for="item in items"
        :key="item.countryCode"
        :lat-lng="latLng(item.latitude,item.longitude)"
        :options="{weight: item.total}">
        <v-popup :content="popupText(item)"></v-popup>
      </v-marker>
    </v-marker-cluster>
  </v-map>

</template>

<script>

import { latLng, /*Icon, icon,*/ DivIcon, Point } from "leaflet";
import * as Vue2Leaflet from "vue2-leaflet";
import Vue2LeafletMarkercluster from "vue2-leaflet-markercluster";

export default {

  components: {
    "v-map": Vue2Leaflet.LMap,
    "v-tilelayer": Vue2Leaflet.LTileLayer,
    "v-marker": Vue2Leaflet.LMarker,
    "v-popup": Vue2Leaflet.LPopup,
    "v-marker-cluster": Vue2LeafletMarkercluster,
  }, 

  props: {
    items: {type: Array},
    initialZoom: {type: Number, default: 2},
    minZoom: {type: Number, default: 2},
    maxZoom: {type: Number, default: 8},
  },

  data() {
    return {
      clusterOptions: {
        singleMarkerMode: true,
        showCoverageOnHover: false,
        spiderfyOnMaxZoom: false,
        iconCreateFunction: this.createCircle,
      },
      centerLocation: latLng(41.8, 9.5),  // todo avg of all coordinates
    };
  },

  methods: {

    latLng,  // import eponymous function from Leaflet

    clusterclick: (e) => console.log("clusterclick", e),

    ready: (e) => console.log("ready", e),

    popupText(item) {
      return `<div style="text-align:center">${item.country}<br/><b>${item.total}</b></div>`
    },

    getMaxWeight() {
      
      const w = this.items.reduce((m, i) => Math.max(m, i.total), 0)
      // console.log("MAX WEIGHT: " + w)
      return w
    },

    createCircle(cluster) {

      const weight = this.getClusterWeight(cluster);
      // console.log("WEIGHT " + weight);
      return new DivIcon({
        html: this.getCircleAsHtml(weight),
        className: "marker-icon",
        iconSize: new Point(0, 0),
      });
    },

    getClusterWeight(cluster) {

      return cluster
        .getAllChildMarkers()
        .reduce((acc, c) => acc + c.options.weight, 0);
    },

    getScaling() {

      // heaviest non-clustered circle should have an on screen diameter of LARGEST_SIZE_IN_PX
      const LARGEST_WEIGHT = this.getMaxWeight()
      const LARGEST_SIZE_IN_PX = 150
      return LARGEST_SIZE_IN_PX / Math.sqrt(LARGEST_WEIGHT)
    },

    getCircleAsHtml(weight) {
      
      const mWeight = Math.sqrt(weight) * this.getScaling()
      let size, clazz

      if (weight > 100000) {
        size  = mWeight
        clazz = "mb-large"
      }
      else if (weight > 1000) {
        size  = Math.max(30,mWeight)
        clazz = "mb-middle"
      }
      else {
        size  = 20
        clazz = "mb-small"
      }

      const radius = size / 2;

      const markerbox =
          `<div class="marker-box ${clazz}" style="`
        +   `width:${size}px;` 
        +   `height:${size}px;` 
        +   `margin:-${radius}px 0 0 -${radius}px;` 
        +   `padding:${radius}px 0 0 0;`
        + `">` 
        +     `<div class="marker-circle" style="`
        +       `border-radius:${radius}px;` 
        +     `"></div>`
        +     `<div class="marker-caption" style="`
        +       `line-height:${size}px;`
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

  .marker-box {
    position: relative;
  }  

  .marker-circle {

    position: absolute;
    top: 0; left: 0;
    width:100%; height:100%;
    text-align: center;
    color: white;
    font-weight: bold;
    color:white; 
    opacity:.5;

    @at-root .mb-small  & { background: rgb(16, 0, 192) }
    @at-root .mb-middle & { background: rgb(132, 0, 192) }
    @at-root .mb-large  & { background: rgb(192, 0, 16) }
  }

  .marker-caption {

    position: absolute;
    top: 0; left: 0;
    width: 100%; height: 100%;
    text-align: center;
    color:white;
    font-weight: bold;
    text-shadow: 0 0 2px #000;

    @at-root .mb-small  & { font-size: 0 }
    @at-root .mb-middle & { font-size: 1em }
    @at-root .mb-large  & { font-size: 1em }
  } 

  
</style>