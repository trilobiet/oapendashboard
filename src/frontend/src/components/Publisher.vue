<template>

  <div>

      <v-toolbar dense class="fixed-bar"> 
          <v-btn icon @click="drawer=!drawer"><v-icon>mdi-menu</v-icon></v-btn>
          <v-spacer></v-spacer>
          <strong>{{user.name}}</strong> 
          &nbsp;-&nbsp;{{user.role}}
      </v-toolbar>

      <v-navigation-drawer v-model="drawer" absolute temporary>

          <v-list dense>
          
              <v-subheader>MONTHLY DOWNLOADS REPORTS</v-subheader>
          
              <v-list-item v-on:click="setSheet('downloadsPerTitle')">
                <v-list-item-icon><v-icon>mdi-book-open-page-variant-outline</v-icon></v-list-item-icon>
                <v-list-item-content><v-list-item-title>Per title</v-list-item-title></v-list-item-content>
              </v-list-item>
          
              <v-list-item v-on:click="setSheet('downloadsPerCountry')">
                <v-list-item-icon><v-icon>mdi-earth</v-icon></v-list-item-icon>
                <v-list-item-content><v-list-item-title>Per country</v-list-item-title></v-list-item-content>
              </v-list-item>
          
          </v-list>

      </v-navigation-drawer>    

      <downloads-per-title-for-publisher v-if="activeSheet=='downloadsPerTitle'"
        :rel-id="this.user.irusId" />
         
      <downloads-per-country v-if="activeSheet=='downloadsPerCountry'"
        :rel-id="this.user.irusId" :rel-type="'publisher'"/>

  </div>

</template>

<script>
import DownloadsPerCountry from '@/components/DownloadsPerCountry.vue'
import DownloadsPerTitleForPublisher from '@/components/DownloadsPerTitleForPublisher.vue'

export default {
  name: 'Publisher',
  components: {
    DownloadsPerCountry,
    DownloadsPerTitleForPublisher,
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


