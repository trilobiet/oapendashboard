
<template>

    <v-sheet class="elevation-5 pa-2" min-height="60vh" height="100%" style="position:relative">
    
        <v-overlay :value="loading" :absolute="true">loading...</v-overlay>
        <bubble-map 
            :points="bubbleMapPoints(points)" 
            :centerLocation="geo"
            :initialZoom="zoom"
            :minZoom="5"
            :maxZoom="14"
            :title="mapTitle"
        />
    
    </v-sheet>        

</template>

<script>
import axios from 'axios';
import BubbleMap from '@/components/charts/BubbleMap.vue';

export default {
  components: { BubbleMap },
  
  props: {
    month: {type: String, default: null},
    geo: {type: Object, default: null },
    radius: {type: Number, default: 50},
    funderFilter: {type: Object, default(){ return {name:"",id:""} } },
    publisherFilter: {type: Object, default(){ return {name:"",id:""} } },
    itemType: {type: String, default: null},
  },

  data() {
    return {
      loading: true,
      points: [],
      precision: 2, // 1 decimal in lat/lon 
    }
  },

  computed: {

    mapTitle() {
       return `Year total requests up to ${this.month}` 
    },

    zoom(){  
        // https://www.dcode.fr/function-equation-finder
        // f(50)=9, f(100)=8, f(200)=7, f(400)=6    
        const z = Math.round(34.8752 - 21.0703 * Math.pow(this.radius,0.0526834))
        console.log("RADIUS: " + this.radius + " ZOOM: " + z)     
        return z;
    }
  },

  mounted() {
     this.callApi();
  },
  
  watch: {
     month:['callApi'],
     itemType:['callApi'],
     funderFilter:['callApi'],
     publisherFilter:['callApi'],
     radius:['callApi']
  },
  
  methods: {

    callApi() {

      this.loading = true; // visual darkening while loading  
      axios.get(`/api/eventcount-per-region?${this.getRequestString()}`)
      .then(resp => this.points=resp.data)
      .catch(error => console.log(error))
      .finally(() => this.loading = false )
    },
    
    getRequestString() {

      const startMonth = this.$func.twelveMonthsBack(this.month)  
      console.log("twelveMonthsBack = " + startMonth)

      let s = 'startmonth='+startMonth+'&endmonth='+this.month
              +'&latitude='+this.geo.lat+'&longitude='+this.geo.lon
              +'&radius='+this.radius+'&precision='+this.precision; 
      if(this.itemType && this.itemType.value) s += '&item-type=' + this.itemType.value
      if(this.funderFilter && this.funderFilter.id) s += '&funder-id=' + this.funderFilter.id
      if(this.publisherFilter && this.publisherFilter.id) s += '&publisher-id=' + this.publisherFilter.id
      // console.log("Events Per Region RequestString: " + s)
      return s;
    },

    bubbleMapPoints(points) {

      const m = points.map(p => {
        return {
          name: p.city,
          latitude: p.latitude,
          longitude: p.longitude,
          size: p.requests
        }
      })  

      // console.log("MAPPED ITEMS:" + JSON.stringify(m))
      return(m)
    },

  }

}; 
</script>