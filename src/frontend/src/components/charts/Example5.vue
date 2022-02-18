<template>
  <v-map :zoom="10" :center="initialLocation">
    <v-icondefault></v-icondefault>
    <v-tilelayer url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"></v-tilelayer>

    <!--<v-circle-marker :key="99999" :lat-lng="[-34.9205,-57.953646,]" :radius="10"></v-circle-marker>-->

    <v-marker-cluster
      :options="clusterOptions"
      @clusterclick="click()"
      @ready="ready"
    >
      <v-marker
        v-for="l in locations"
        :key="l.id"
        :lat-lng="l.latlng"
        :icon="icon"
        :options="{ weight: l.weight }"
      >
        <v-popup :content="l.text"></v-popup>
      </v-marker>
    </v-marker-cluster>
  </v-map>
</template>

<script>
import { latLng, Icon, icon, DivIcon, Point } from "leaflet";
import * as Vue2Leaflet from "vue2-leaflet";
import Vue2LeafletMarkercluster from "vue2-leaflet-markercluster";
import iconUrl from "leaflet/dist/images/marker-icon.png";
import shadowUrl from "leaflet/dist/images/marker-shadow.png";

function rand(n) {
  let max = n + 0.1;
  let min = n - 0.1;
  return Math.random() * (max - min) + min;
}

export default {
  components: {
    "v-map": Vue2Leaflet.LMap,
    "v-tilelayer": Vue2Leaflet.LTileLayer,
    "v-icondefault": Vue2Leaflet.LIconDefault,
    "v-marker": Vue2Leaflet.LMarker,
    "v-popup": Vue2Leaflet.LPopup,
    "v-marker-cluster": Vue2LeafletMarkercluster,
    //'v-circle-marker': Vue2Leaflet.LCircleMarker
  },
  data() {
    let locations = [];
    for (let i = 0; i < 100; i++) {
      locations.push({
        id: i,
        latlng: latLng(rand(51.909558), rand(4.452511)),
        text: "Hola " + i,
        weight: Math.floor(Math.random() * 100),
      });
    }
    console.log("locations len " + locations.length);
    let customicon = icon(
      Object.assign({}, Icon.Default.prototype.options, { iconUrl, shadowUrl })
    );
    let refWeight = locations.reduce((m, l) => {
      console.log("x " + l.weight);
      return Math.max(m, l.weight);
    }, 0);
    console.log("HEAVIEST " + refWeight);
    return {
      locations,
      refWeight,
      icon: customicon,
      clusterOptions: {
        singleMarkerMode: true,
        iconCreateFunction: this.createCircle,
        spiderfyOnMaxZoom: false,
        iconCreateFunction: this.createCircle,
      },
      initialLocation: latLng(51.909558, 4.452511),
    };
  },
  mounted() {
    setTimeout(() => {
      console.log("done");
      this.$nextTick(() => {
        this.clusterOptions = { disableClusteringAtZoom: 17 };
      });
    }, 5000);
  },
  methods: {
    click: (e) => console.log("clusterclick", e),

    ready: (e) => console.log("ready", e),

    createCircle(cluster) {
      const weight = this.getClusterWeight(cluster);
      console.log("WEIGHT " + weight);
      return new DivIcon({
        html: this.getCircleAsHtml(weight),
        className: "marker-cluster marker-cluser-large",
        iconSize: new Point(0, 0),
      });
    },

    getClusterWeight(cluster) {
      return cluster
        .getAllChildMarkers()
        .reduce((acc, c) => acc + c.options.weight, 0);
    },

    getScaling(maxValue) {
      const REF_WIDTH = 250;
      return 5 * REF_WIDTH / maxValue;
    },

    getCircleAsHtml(weight) {
      console.log("refWeight: " + this.refWeight)
      const mWeight = Math.sqrt(weight * this.getScaling(this.refWeight));
      const radius = mWeight / 2;

      //let circle = '<svg viewBox="0 0 60 60" xmlns="http://www.w3.org/2000/svg" style="opacity:.4"><circle cx="20" cy="20" r="20"/></svg>'

      const doos =
        '<div style="width:' + mWeight + "px;" 
        + "height:" + mWeight + "px;" 
        + "margin:-" + radius + "px 0 0 -" + radius + "px;" 
        + "padding:" + radius + "px 0 0 0;" 
        + "border-radius:" + radius + "px;" 
        + 'background:green; color:white; opacity:.5;">' 
        +  mWeight 
        +  "</div>";

      return doos;
    },
  },
};
</script>

<style>
@import "~leaflet/dist/leaflet.css";
@import "~leaflet.markercluster/dist/MarkerCluster.css";
@import "~leaflet.markercluster/dist/MarkerCluster.Default.css";

html,
body {
  height: 100%;
  margin: 0;
}
</style>