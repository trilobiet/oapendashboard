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
              item-text="text" item-value="value" label="Distance" />
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
        <my-data-table :headers="headers" :items="items" :loading="loading" :report-title="getReportTitle"/>
      </v-col>
    </v-row>
  </v-container>

  <v-container fluid>
    <v-row class="d-flex">
        <v-col lg="6" class="flex-grow-1">
          <stacked-bar-chart :rows="25" :items="items" categoriesField="title"
            title="Requests per Title" />
        </v-col>  
        <v-col lg="6" class="flex-grow-1">
          <events-per-region :relGeo="relGeo" :radius="currentRadius.value"
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
    relId: {type: String, default:''},
    relGeo: {type: Object, default:null }
  },
  
  data() {
    return {
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
      currentRadius:{value:50, text:"50 km"},      
    }    
  },

  computed: {

      getReportTitle() {
        return `Number of Successful Title Requests per Month and Title for Region (distance ${this.currentRadius.value} km)`
      }  
  },
  
  mounted() {
    this.callApi();
  },
  
  watch: {
     currentMonth:'callApi',
     currentItemType:'callApi',
     currentFunderFilter:'callApi',
     currentPublisherFilter:'callApi',
     currentRadius:['callApi','setTitle']
  },
  
  methods: {

    setTitle() {
       this.reportTitle = 'burp' 
    },
    
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
        { text: "OAPEN link", value: "id" },
        { text: "Isbn", value: "isbn" },
        { text: "Title", value: "title", cellClass: "td-title" },
        { text: "Doi", value: "doi" },
        { text: "Publisher", value: "publisherName" },
        { text: "Funding", value: "funders" },
        { text: "Doc. type", value: "type" },
        { text: "Total", value: "total", align:"right" }
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
 
      let s = 'month='+this.currentMonth+'&latitude='+this.relGeo.lat
              +'&longitude='+this.relGeo.lon+'&radius='+this.currentRadius.value; 
      if(this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentFunderFilter.id) s += '&funder-id=' + this.currentFunderFilter.id
      if(this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      console.log("RequestString: " + s)
      return s;
    },

  }

}; 
</script>