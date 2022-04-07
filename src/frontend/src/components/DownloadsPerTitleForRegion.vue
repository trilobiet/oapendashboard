<template>
<div>
  <v-container fluid>
    <v-row>
      <v-col cols="6" md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="End Month" />
      </v-col>
      <v-col cols="6" md="2">
          <v-autocomplete v-model="currentFunderFilter" return-object :items="this.$store.getters.getFunders" 
              item-text="name" item-value="id" label="Funder" />
      </v-col>
      <v-col cols="6" md="2">
          <v-autocomplete v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col cols="6" md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>
      <v-col cols="6" md="2">
          <v-select v-model="currentRadius" return-object :items="radii" 
              item-text="text" item-value="value" label="Area radius" />
      </v-col>

      <v-spacer/>
      <v-col md="2" class="text-right">

          <vue-json-to-csv :json-data="$func.flattenJsonArray(this.items)" csv-title="monthly_requests_for_region">
            <v-btn color="blue-grey" class="ma-3 white--text">
              Download
              <v-icon right dark>mdi-table-arrow-down</v-icon>
            </v-btn>
          </vue-json-to-csv>

      </v-col>
      
    </v-row>    
  </v-container>

  <v-container fluid>
    <v-row>
      <v-col>
        <my-data-table :headers="headers" :items="items" :loading="loading" 
          :report-title="reportTitle"/>
      </v-col>
    </v-row>
  </v-container>

  <v-container fluid>
    <v-row class="d-flex">
        <v-col cols="12" lg="6" class="flex-grow-1">
          <stacked-bar-chart :rows="maxRows" :items="items" categoriesField="title"
            :title="chartTitle" />
        </v-col>  
        <v-col cols="12" lg="6" class="flex-grow-1">
          <events-per-region :geo="geo" :radius="currentRadius.value"
            :month="currentMonth"
            :funderFilter="currentFunderFilter" :publisherFilter="currentPublisherFilter"
            :itemType="currentItemType" 
          />
        </v-col>  
    </v-row>
  </v-container>  

</div>  
</template>

<script>
import axios from 'axios';
import MyDataTable from '@/components/MyDataTable.vue';
import StackedBarChart from '@/components/charts/StackedBarChart.vue';
import EventsPerRegion from '@/components/EventsPerRegion.vue';

export default {
  components: { MyDataTable, StackedBarChart, EventsPerRegion },
  
  props: {
    geo: {type: Object, default: null },
    radius: {type: Number, default: 0}
  },
  
  data() {
    return {
      maxRows: 25,
      loading: true,
      headers: [],
      items:[], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentItemType: "",
      currentFunderFilter: {name:"",id:""},
      currentPublisherFilter: {name:"",id:""},
      radii: [
        {value:20, text:"20 km"},
        {value:50, text:"50 km"},
        {value:100, text:"100 km"},
        {value:250, text:"250 km"},
        {value:500, text:"500 km"}
      ],      
      currentRadius:{value:this.radius, text:"50 km"}
    }    
  },

  computed: {

    reportTitle() {
      return `Number of successful title requests per month and title for library and region (area radius ${this.currentRadius.value} km)`
    },

    chartTitle() {
      return `Top ${Math.min(this.items.length,this.maxRows)} requests per title per month up to ${this.currentMonth}` 
    },
  },
  
  mounted() {
    this.callApi();
  },
  
  watch: {
     currentMonth:'callApi',
     currentItemType:'callApi',
     currentFunderFilter:'callApi',
     currentPublisherFilter:'callApi',
     currentRadius:['callApi']
  },
  
  methods: {

    callApi() {

      this.loading = true; // visual darkening while loading  
      axios.get(`/api/eventcount-per-item-region?${this.getRequestString()}`)
      .then(resp => {
         this.items=resp.data;
         this.headers=this.getHeaders(resp.data);
      })
      .catch(error => console.log(error))
      .finally(() => this.loading = false )
    },
    
    getHeaders(json) {

      let arr = [
        { text: "OAPEN id", value: "id" },
        { text: "Isbn", value: "isbn" },
        { text: "Title", value: "title", cellClass: "td-title" },
        { text: "Doi", value: "doi" },
        { text: "Publisher", value: "publisherName" },
        { text: "Funding", value: "funders" },
        { text: "Item type", value: "type" },
        { text: "Year total", value: "total", align:"right" }
      ];

      if (json[0]) {
        let months=json[0].monthTotals;
        for (const [key] of Object.entries(months)) {
          arr.push({"text":key,"value":"monthTotals."+key,"align":"right"})
        }
      }  

      return arr;
    },
    
    getRequestString() {
 
      let s = 'month='+this.currentMonth+'&latitude='+this.geo.lat
              +'&longitude='+this.geo.lon+'&radius='+this.currentRadius.value; 
      if(this.currentItemType && this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentFunderFilter && this.currentFunderFilter.id) s += '&funder-id=' + this.currentFunderFilter.id
      if(this.currentPublisherFilter && this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      console.log("RequestString: " + s)
      return s;
    },

  }

}; 
</script>