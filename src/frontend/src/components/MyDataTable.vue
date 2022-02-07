<template>

    <v-card>
        <v-card-title>
        <v-text-field
            v-model="tableSearch"
            append-icon="mdi-magnify"
            label="Search"
            single-line 
            hide-details></v-text-field>
        </v-card-title> 
        <v-data-table :search="tableSearch" :headers="headers" :items="items"/>
        <v-overlay opacity=".4" :value="overlay" absolute>
            <v-progress-circular :size="50" color="#ccc" indeterminate></v-progress-circular>
            <br/>Loading... please wait
        </v-overlay>
    </v-card>    

</template>


<script>

export default {

  props: {
    headers: {type: Array},
    items: {type: Array},     
    overlay: {type: Boolean, default: true},
  },

  data() {
    return {
      tableSearch: '',
    }
  }  
}
</script>
