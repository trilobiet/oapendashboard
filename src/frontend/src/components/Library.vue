<template>

  <div>
      
      <v-toolbar dense class="fixed-bar">
          <v-btn icon @click="drawer=!drawer"><v-icon>mdi-menu</v-icon></v-btn>
          <v-spacer></v-spacer>
          <strong>{{user.name}}</strong> 
          &nbsp;-&nbsp;{{user.role}}
      </v-toolbar>
     
      <v-navigation-drawer v-model="drawer" fixed temporary class="pt-10 px-4" style="background:#eeeef6">

          <v-list dense>

              <v-subheader>
                Number of Successful Title Requests<br/>
                Per Month and Title:
              </v-subheader>

              <v-divider class="pa-4"/>
          
              <v-list-item v-on:click="setSheet('downloadsPerTitleLibrary')">
                <v-list-item-icon><v-icon>mdi-library</v-icon></v-list-item-icon>
                <v-list-item-content><v-list-item-title>For Library</v-list-item-title></v-list-item-content>
              </v-list-item>
          
              <v-list-item v-on:click="setSheet('downloadsPerTitleRegion')">
                <v-list-item-icon><v-icon>mdi-chart-bubble</v-icon></v-list-item-icon>
                <v-list-item-content><v-list-item-title>For Region</v-list-item-title></v-list-item-content>
              </v-list-item>

          </v-list>

      </v-navigation-drawer>    

      <downloads-per-title-for-library v-if="activeSheet=='downloadsPerTitleLibrary'" 
        :rel-id="this.user.id" />

      <downloads-per-title-for-region v-if="activeSheet=='downloadsPerTitleRegion'" 
        :rel-geo="this.user.geoLocation"/>

  </div>

</template>

<script>
import DownloadsPerTitleForLibrary from '@/components/DownloadsPerTitleForLibrary.vue'
import DownloadsPerTitleForRegion from '@/components/DownloadsPerTitleForRegion.vue'

export default {
  name: 'Libary',
  components: {
    DownloadsPerTitleForLibrary,
    DownloadsPerTitleForRegion
  },
  props: {
    user: Object
  },
  data() {
    return {
      drawer: false,
      activeSheet: 'downloadsPerTitleLibrary'
    }
  },
  methods: {

    setSheet(sheet) {
      this.activeSheet = sheet;
      this.drawer = false;
    }
  }
}
</script>


