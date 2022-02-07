<template>
<div>
  <v-container>
    <v-row class="text-center">
      <v-col md="2">
          <v-select v-model="currentCountry" return-object :items="this.$store.getters.getCountries" 
              item-text="name" item-value="code" :hint="`${currentCountry.name} ${currentCountry.code}`" 
              label="Select" persistent-hint />
      </v-col>
      <v-col md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="Month" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>
    </v-row>    
  </v-container>
  <v-container>
    <v-row class="text-center">
      <v-col cols="12">
        <my-data-table :headers="headers" :items="items" :overlay="overlay" />
      </v-col>
    </v-row>
  </v-container>
</div>  
</template>

<script>

import MyDataTable from '@/components/MyDataTable.vue';

export default {
  components: { MyDataTable },
  
  props: {
    relId: {type: String, default:''}
  },
  
  data() {
    return {
      overlay: true,
      headers: [],
      items:[], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentCountry: this.$store.getters.getCountries[0],
      currentItemType: this.$store.getters.getItemTypes[0],
      currentPublisherFilter: this.$store.getters.getPublishers[0],
    }    
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

      this.overlay = true; // visual darkening while loading  
      fetch(`/api/eventcount-per-item-publisherfunder?${this.getRequestString()}`)
      .then(r => r.json())
      .then(json => {
         this.items=json;
         this.headers=this.getHeaders(json);
      })
      .catch(error => console.log(error))
      .finally(() => this.overlay = false )
    },
    
    getHeaders(json) {

      let arr = [
        { text: "OAPEN link", value: "id" },
        { text: "Isbn", value: "isbn" },
        { text: "Title", value: "title" },
        { text: "Doi", value: "doi" },
        { text: "Publisher", value: "publisherName" },
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
 
      let s = 'month='+this.currentMonth+'&funder-id='+ this.relId;
      if(this.currentCountry.code) s += '&country-code=' + this.currentCountry.code
      if(this.currentItemType.value) s += '&item-type=' + this.currentItemType.value
      if(this.currentPublisherFilter.id) s += '&publisher-id=' + this.currentPublisherFilter.id
      // console.log("RequestString: " + s)
      return s;
    },

  }

}; 
</script>