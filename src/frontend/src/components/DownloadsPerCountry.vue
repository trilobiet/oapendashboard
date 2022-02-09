<template>
<div>
  <v-container>
    <v-row class="text-center">
      <v-col md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="End Month" />
      </v-col>
      <v-col md="2" v-if="relType=='publisher'">
          <v-select v-model="currentFunderFilter" return-object :items="this.$store.getters.getFunders" 
              item-text="name" item-value="id" label="Funder" />
      </v-col>
      <v-col md="2" v-if="relType=='funder'">
          <v-select v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>

      <v-spacer/>
      <v-col md="2" class="text-right">

          <vue-json-to-csv :json-data="$func.flattenJsonArray(this.items)" csv-title="monthly_requests_per_country">
            <v-btn color="blue-grey" class="ma-3 white--text">
              Download
              <v-icon right dark>mdi-table-arrow-down</v-icon>
            </v-btn>
          </vue-json-to-csv>

      </v-col>

    </v-row>    
  </v-container>
  <v-container>
    <v-row class="text-center">
      <v-col cols="12">
        <my-data-table :headers="headers" :items="items" :loading="loading" :report-title="reportTitle" />
      </v-col>
    </v-row>
  </v-container>
</div>  
</template>

<script>
import axios from 'axios';
import MyDataTable from '@/components/MyDataTable.vue';

export default {
  components: { MyDataTable },
  
  props: {
    relId: {type: String, default:''},
    relType: {type: String, default:''},     
  },
  
  data() {
    return {
      usertype: '',
      loading: true,
      headers: [],
      items:[], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentItemType: this.$store.getters.getItemTypes[0],
      currentFunderFilter: this.$store.getters.getFunders[0],
      currentPublisherFilter: this.$store.getters.getPublishers[0],
      reportTitle: "Number of Successful Title Requests by Month and Country"
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
  },
  
  methods: {

    callApi() {
        
      this.loading = true;  
      axios.get(`/api/eventcount-per-country?${this.getRequestString()}`)
      .then(resp => {
         this.items=resp.data;
         this.headers=this.getHeaders(resp.data);
      })
      .catch(error => {console.log(error)})
      .finally(() => this.loading = false )
    },
    
    getHeaders(json) {

      let arr = [
        { text: "Country", value: "country" },
        { text: "Code", value: "countryCode" },
        { text: "Total", value: "total", align: "right" }
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
 
      let s = 'month='+this.currentMonth+'&'; 
      if (this.relType=='funder') s += 'funder-id='; else s += 'publisher-id=';
      s += this.relId;

      if(this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentFunderFilter.id) s += '&funder-id=' + this.currentFunderFilter.id
      if(this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      // console.log("RequestString: " + s)
      return s
    }
  }

}; 
</script>

