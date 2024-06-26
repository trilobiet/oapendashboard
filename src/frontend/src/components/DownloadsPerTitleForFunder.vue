<template>
<div>
  
  <v-container fluid>
    <v-row>
      <v-col cols="6" md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="End Month" />
      </v-col>
      <v-col cols="6" md="2">
          <v-autocomplete v-model="currentCountry" return-object :items="this.$store.getters.getCountries"  
              item-text="name" item-value="code" :hint="`${currentCountry.name} ${currentCountry.code}`" 
              label="Select" persistent-hint />
      </v-col>
      <v-col cols="6" md="2">
          <v-autocomplete v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col cols="6" md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>

      <v-spacer/>
      <v-col md="2" class="text-right">

          <vue-json-to-csv :json-data="$func.flattenJsonArray(this.items)" csv-title="monthly_requests_per_title">
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
        <my-data-table :headers="headers" :items="items" :loading="loading" :report-title="reportTitle"/>
      </v-col>
    </v-row>
  </v-container>

  <v-container fluid>
    <v-row>
        <v-col>
          <stacked-bar-chart :rows="maxRows" :items="items" categoriesField="title" 
            :title="chartTitle" />
        </v-col>  
    </v-row>
  </v-container>  

</div>  
</template>

<script>
import axios from 'axios';
import MyDataTable from '@/components/MyDataTable.vue';
import StackedBarChart from '@/components/charts/StackedBarChart.vue';

export default {
  components: { MyDataTable, StackedBarChart },
  
  props: {
    relIds: {type: Array, default:()=>[]}
  },
  
  data() {
    return {
      maxRows: 25,
      loading: true,
      headers: [],
      items:[], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentCountry: {name:"",code:""},
      currentItemType: "",
      currentPublisherFilter: {name:"",id:""},
      reportTitle: "Number of successful title requests per month and title"
    }    
  },

  computed: {

    chartTitle() {
      return `Top ${Math.min(this.items.length,this.maxRows)} requests per month and title up to ${this.currentMonth}` 
    },
  },
  
  mounted() {
    this.callApi();
  },
  
  watch: {
     currentMonth:'callApi',
     currentCountry: 'callApi',
     currentItemType:'callApi',
     currentPublisherFilter:'callApi',
  },
  
  methods: {
    
    callApi() {

      this.loading = true; // visual darkening while loading  
      axios.get(`/api/eventcount-per-item-publisherfunder?${this.getRequestString()}`)
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
 
      let s = 'month='+this.currentMonth+'&funder-id='+ this.relIds.join(",");
      if(this.currentCountry && this.currentCountry.code) s += '&country-code=' + this.currentCountry.code
      if(this.currentItemType && this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentPublisherFilter && this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      // console.log("RequestString: " + s)
      return s;
    },

  }

}; 
</script>