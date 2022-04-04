<template>

  <div>

      <v-toolbar dense class="fixed-bar"> 
          <v-btn icon @click="drawer=!drawer"><v-icon>mdi-menu</v-icon></v-btn>
          <v-spacer></v-spacer>
          <strong>{{user.fullname}}</strong> 
          &nbsp;-&nbsp;{{user.role}}
      </v-toolbar>

      <v-navigation-drawer v-model="drawer" fixed temporary>

          <v-list dense>
          
              <v-subheader>Number of Successful title requests</v-subheader>
          
              <v-list-item v-on:click="setSheet('downloadsPerTitle')">
                <v-list-item-icon><v-icon>mdi-book-open-page-variant-outline</v-icon></v-list-item-icon>
                <v-list-item-content><v-list-item-title>Per Month and Title</v-list-item-title></v-list-item-content>
              </v-list-item>
          
              <v-list-item v-on:click="setSheet('downloadsPerCountry')">
                <v-list-item-icon><v-icon>mdi-earth</v-icon></v-list-item-icon>
                <v-list-item-content><v-list-item-title>Per Month and Country</v-list-item-title></v-list-item-content>
              </v-list-item>
          
          </v-list>

      </v-navigation-drawer>    

      <downloads-per-title-for-funder v-if="activeSheet=='downloadsPerTitle'"
        :rel-ids="this.user.urlIrusIds" />

      <downloads-per-country v-if="activeSheet=='downloadsPerCountry'"
        :rel-ids="this.user.urlIrusIds" :rel-type="'funder'"/>
         
  </div>

</template>

<script>
import DownloadsPerCountry from '@/components/DownloadsPerCountry.vue'
import DownloadsPerTitleForFunder from '@/components/DownloadsPerTitleForFunder.vue'

export default {
  name: 'Funder',
  components: {
    DownloadsPerCountry,
    DownloadsPerTitleForFunder,
  },
  props: {
    user: Object
  },
  data() {
    return {
      drawer: false,
      activeSheet: 'downloadsPerTitle'
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


