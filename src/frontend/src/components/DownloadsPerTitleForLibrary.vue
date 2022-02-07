<template>
<div>
  <v-container>
    <v-row class="text-center">
      <v-col md="2">
          <v-select value v-model="currentMonth" :items="this.$store.getters.getMonths" 
              label="Month" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentFunderFilter" return-object :items="this.$store.getters.getFunders" 
              item-text="name" item-value="id" label="Funder" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentPublisherFilter" return-object :items="this.$store.getters.getPublishers" 
              item-text="name" item-value="id" label="Publisher" />
      </v-col>
      <v-col md="2">
          <v-select v-model="currentItemType" return-object :items="this.$store.getters.getItemTypes" 
              item-text="text" item-value="value" label="Item type" />
      </v-col>
      <!--
      <v-col md="2">
          <v-text-field v-model="currentItemId" label="Item ids"></v-text-field>
      </v-col> -->
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
      usertype: '',
      headers: [],
      items: [], 
      currentMonth: this.$store.getters.getLastRequestableMonth,
      currentItemType: this.$store.getters.getItemTypes[0],
      currentFunderFilter: this.$store.getters.getFunders[0],
      currentPublisherFilter: this.$store.getters.getPublishers[0],
      currentItemId: ''
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

      this.overlay = true; // visual darkening while loading  
      fetch(`/api/eventcount-per-item-library?${this.getRequestString()}`)
      .then(r => r.json())
      .then(json => {
         this.items=json;
         this.headers=this.getHeaders(json);
      })
      .catch(error => {
        console.log("One hell of an error.")
        console.log(error)
      })
      .finally(() => this.overlay = false )
    },
    
    getHeaders(json) {

      let arr = [
        { text: "OAPEN link", value: "id" },
        { text: "Isbn", value: "isbn" },
        { text: "Title", value: "title" },
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
       console.log("RequestString: " + s)
      return s;
    },

  }

}; 
</script>