<template>
<div>
  <v-container fluid>
    <v-row>
      <v-col cols="6" md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="End Month" />
      </v-col>
      <v-col cols="6" md="2" v-if="relType=='publisher'">
          <v-select v-model="currentFunderFilter" return-object :items="this.$store.getters.getFunders" 
              item-text="name" item-value="id" label="Funder" />
      </v-col>
      <v-col cols="6" md="2" v-if="relType=='funder'">
          <v-select v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col cols="6" md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>
      <v-col cols="6" md="2">
          <item-select v-model="currentItem" :relType="relType" :relIds="relIds" />
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

  <v-container fluid>
    <v-row>
      <v-col>
        <my-data-table :headers="headers" :items="items" :loading="loading" :report-title="reportTitle" :subTitle="subTitle" />
      </v-col>
    </v-row>
  </v-container>

  <v-container fluid>
    <v-row class="d-flex">
        <v-col cols="12" lg="6" class="flex-grow-1">
          <stacked-bar-chart :rows="25" :items="items" categoriesField="country"
            :title="chartTitle" />
        </v-col>  
        <v-col cols="12" lg="6" class="flex-grow-1">
          <v-sheet class="elevation-5 pa-2" min-height="60vh" height="100%">
            <bubble-map :points="bubbleMapPoints(items)" 
            :title="mapTitle" />
          </v-sheet>
        </v-col>  
    </v-row>
  </v-container>  

</div>  
</template>

<script>
import axios from 'axios';
import MyDataTable from '@/components/MyDataTable.vue';
import StackedBarChart from '@/components/charts/StackedBarChart.vue';
import ItemSelect from './ItemSelect.vue';
import BubbleMap from './charts/BubbleMap.vue';

export default {
  components: { MyDataTable, StackedBarChart, ItemSelect, BubbleMap },
  
  props: {
    relIds: {type: Array, default:()=>[]},
    relType: {type: String, default:''},     
  },
  
  data() {
    return {
      usertype: '',
      loading: true,
      headers: [],
      items:[], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentItemType: "",
      currentItem: {id:"", title:""},
      currentFunderFilter: {name:"",id:""},
      currentPublisherFilter: {name:"",id:""},
      reportTitle: "Number of Successful Title Requests per Month and Country",
      subTitle: ""
    }    
  },
  
  mounted() {
    this.callApi();
  },

  computed: {

    chartTitle() {
      return `Requests per country per month until ${this.currentMonth}` 
    },

    mapTitle() {
      return `Yearly requests until ${this.currentMonth}` 
    }
  },
  
  watch: {
     currentMonth:'callApi',
     currentItemType:'callApi',
     currentItem: 'callApi',
     currentFunderFilter:'callApi',
     currentPublisherFilter:'callApi',
  },
  
  methods: {

    callApi() {

      this.loading = true;  
      axios.get(`/api/eventcount-per-country?${this.getRequestString()}`)
      .then(resp => {
         this.items=resp.data
         this.headers=this.getHeaders(resp.data)
         this.subTitle = this.currentItem.title
      })
      .catch(error => {console.log(error)})
      .finally(() => this.loading = false )
    },
    
    getHeaders(json) {

      let arr = [
        { text: "Country", value: "country" },
        { text: "Code", value: "countryCode" },
        { text: "Year total", value: "total", align: "right" }
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
      s += this.relIds.join(",");

      if(this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentFunderFilter.id) s += '&funder-id=' + this.currentFunderFilter.id
      if(this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      if(this.currentItem.id) s+= '&item-id=' + this.currentItem.id
      // console.log("RequestString: " + s)
      return s
    },

    bubbleMapPoints(items) {

      const m = items.map(it => {
        return {
          name:it.countryCode,
          latitude:it.latitude,
          longitude:it.longitude,
          size:it.total
        }
      })  

      // console.log("MAPPED ITEMS:" + JSON.stringify(m))
      return(m)
    },

}

}; 
</script>

