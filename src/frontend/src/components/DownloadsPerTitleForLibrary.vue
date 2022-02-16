<template>
<div>
  <v-container>
    <v-row>
      <v-col md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="End Month" />
      </v-col>
      <v-col md="2">
          <v-autocomplete v-model="currentFunderFilter" return-object :items="this.$store.getters.getFunders" 
              item-text="name" item-value="id" label="Funder" />
      </v-col>
      <v-col md="2">
          <v-autocomplete v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>

      <v-spacer/>
      <v-col md="2" class="text-right">

          <vue-json-to-csv :json-data="$func.flattenJsonArray(this.items)" csv-title="monthly_requests_for_library">
            <v-btn color="blue-grey" class="ma-3 white--text">
              Download
              <v-icon right dark>mdi-table-arrow-down</v-icon>
            </v-btn>
          </vue-json-to-csv>

      </v-col>
      
    </v-row>    
  </v-container>

  <v-container>
    <v-row>
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
    relId: {type: String, default:''}
  },
  
  data() {
    return {
      loading: true,
      usertype: '',
      headers: [],
      items: [], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentItemType: "",
      currentFunderFilter: {name:"",id:""},
      currentPublisherFilter: {name:"",id:""},
      currentItemId: "",
      reportTitle: 'Number of Successful Title Requests per Month and Title for Library',
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
     currentItemId:'callApi'
  },
  
  methods: {
    
    callApi() {

      this.loading = true; // visual darkening while loading  
      axios.get(`/api/eventcount-per-item-library?${this.getRequestString()}`)
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
 
      let s = 'month='+this.currentMonth+'&library-id='+this.relId; 
      if(this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentFunderFilter.id) s += '&funder-id=' + this.currentFunderFilter.id
      if(this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      if(this.currentItemId) s += '&item-id=' + this.currentItemId
      // console.log("RequestString: " + s)
      return s;
    },

  }

}; 

</script>