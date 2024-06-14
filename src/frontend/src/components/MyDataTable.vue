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
            :headers="headers" :items="items" :noDataText="nodata"
            :footer-props="{'items-per-page-options': [10, 25, 50, 100, -1]}"
            calculate-widths>

            <!-- if there is an oapen id, make it a link to the library -->
            <template v-slot:item.id="{ item }">
                <a :href="`https://library.oapen.org/handle/${item.id}`" target="oapen_library" style="display:inline-block">
                  {{ item.id }}
                  <v-icon x-small>mdi-open-in-new</v-icon>
                </a>
            </template>

            <template v-if="nodata" v-slot:no-data>
              {{nodata}}
              
              <div class="my-2 blue--text" style="cursor:pointer" @click="reloadData()" v-if="isReloadable">
                <v-icon color="blue">mdi-database-refresh</v-icon> Click to retry 
              </div>
              <div class="my-2 grey--text" v-else>
                <v-icon color="grey">mdi-database-refresh</v-icon> Click to retry (please wait...)
              </div>
            </template>

          </v-data-table>  
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
    subTitle:  {type: String, default: ""},
    //nodata: {type: String, default: "Currently no data available"},
    nodata: {type: String},
    isReloadable: {type: Boolean, default: false}
  },

  data() {
    return {
      tableSearch: '',
      loadingText: 'Loading... (preparing new reports may take up to a few minutes)'
    }
  },
  
  methods: {

    reloadData() {
      /* a custom event that is attached on the parent my-data-table tag 
      via attribute @request-reload-data='[someFunction()]' */
      this.$emit('request-reload-data')
    }
  }

}
</script>

<style lang="scss" scoped>

  a {
    text-decoration: none;
    color: navy;
  }

  // except on very small devices, where tables become a list,
  // we want to hide the link icon
  @media (min-width:600px) {

    a .v-icon {
      opacity: 0;  
      transition: opacity 500ms;
    }

    .v-data-table tr:hover .v-icon {
      opacity: 1;  
    }  
  }  

</style>
