<template>
    <l-map style="height:100%;width:100%;" :zoom="10" :center="[47.334852, -1.509485]">
        <l-tile-layer url="http://{s}.tile.osm.org/{z}/{x}/{y}.png"></l-tile-layer>
        <!--<l-tile-layer url="https://api.maptiler.com/maps/bright/{z}/{x}/{y}.png?key=1FW1bxWAI6uTYSLFIVWk"></l-tile-layer>-->
        <!--<l-tile-layer url="https://stamen-tiles.a.ssl.fastly.net/toner-lite/{z}/{x}/{y}.png"></l-tile-layer>-->
        <!--<l-tile-layer url="https://stamen-tiles.a.ssl.fastly.net/watercolor/{z}/{x}/{y}.png"></l-tile-layer>-->
    </l-map>
</template>


<script>
import { LMap, LTileLayer } from "vue2-leaflet";
import 'leaflet/dist/leaflet.css';

export default {
  components: {
    LMap,
    LTileLayer
  },
  data() {
    return {
      latlngs: [
        [47.334852, -1.509485, 10],
        [47.342596, -1.329731, 4],
        [47.241487, -1.190568, 70],
        [47.234787, -1.358337, 2]
      ]
    };
  }
};
</script>

