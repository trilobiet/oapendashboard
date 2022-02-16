<template>

    <v-card class="elevation-5">
        <v-card-title>
          {{reportTitle}}
        </v-card-title>
        <v-card-subtitle v-if="subTitle" class="align-start">
          <b>{{subTitle}}</b>
        </v-card-subtitle>  
        <v-card-text>   
          <v-text-field
              v-model="tableSearch"
              append-icon="mdi-magnify"
              label="Search"
              single-line 
              hide-details></v-text-field>
        </v-card-text>      
        <v-card-text> 
          <v-data-table 
            :loading="loading" :loading-text="loadingText" :search="tableSearch" 
            :headers="headers" :items="items"   
            :footer-props="{'items-per-page-options': [10, 25, 50, 100, -1]}"
            calculate-widths />
        </v-card-text>
    </v-card>    

</template>


<script>

export default {

  props: {
    headers: {type: Array},
    items: {type: Array},     
    loading: {type: Boolean, default: false},
    reportTitle: {type: String},
    subTitle:  {type: String, default: ""}
  },

  data() {
    return {
      tableSearch: '',
      loadingText: 'Loading... \n(Preparing new reports may take up to a few minutes)'
    }
  }  
}
</script>
