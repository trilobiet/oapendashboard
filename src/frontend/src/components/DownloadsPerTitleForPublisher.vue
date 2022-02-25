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
          <v-autocomplete v-model="currentFunderFilter" return-object :items="this.$store.getters.getFunders" 
              item-text="name" item-value="id" label="Funder" />
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
        <my-data-table :headers="headers" :items="items" :loading="loading" :report-title="reportTitle" />
      </v-col>
    </v-row>
  </v-container>

  <v-container fluid>
    <v-row>
        <v-col>
          <stacked-bar-chart :rows="25" :items="items" categoriesField="title" 
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
    relId: {type: String, default:''}
  },
  
  data() {
    return {
      loading: true,
      headers: [],
      items:[], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentCountry: {name:"",code:""},
      currentItemType: "",
      currentFunderFilter: {name:"",id:""},
      reportTitle: "Number of Successful Title Requests per Month and Title"
    }    
  },
  
  computed: {

    chartTitle() {
      return `Requests per title per month until ${this.currentMonth}` 
    },
  },

  mounted() {
    this.callApi();
  },
  
  watch: {
     currentMonth:'callApi',
     currentCountry: 'callApi',
     currentItemType:'callApi',
     currentFunderFilter:'callApi',
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
        { text: "OAPEN link", value: "id" },
        { text: "Isbn", value: "isbn" },
        { text: "Title", value: "title", cellClass: "td-title" },
        { text: "Doi", value: "doi" },
        { text: "Funder", value: "funders" },
        { text: "Doc. type", value: "type" },
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
 
      let s = 'month='+this.currentMonth+'&publisher-id='+ this.relId;
      if(this.currentCountry.code) s += '&country-code=' + this.currentCountry.code
      if(this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentFunderFilter.id) s += '&funder-id=' + this.currentFunderFilter.id
      // console.log("RequestString: " + s)
      return s;
    },

  }

}; 
</script>