<template>

  <v-app id="main" :style="{background: $vuetify.theme.themes[theme].background}">

    <v-app-bar 
      app
      color="#2c84bf" 
      dark 
    >
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="./assets/oapenlogo-white.png"
          transition="scale-transition"
          width="70"
        />
      </div>

      <v-spacer></v-spacer>

      <v-menu>

        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item to="/">
            <v-list-item-title>Home</v-list-item-title>
          </v-list-item>
          <v-list-item to="/about">
            <v-list-item-title>About</v-list-item-title>
          </v-list-item>
          <v-list-item v-if="isLoggedIn" to="/dashboard">
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item>

          <v-divider></v-divider>

          <v-list-item to="/login">
            <v-list-item-title v-if="isLoggedIn">Log out</v-list-item-title>
            <v-list-item-title v-else>Log in</v-list-item-title>
          </v-list-item>

        </v-list>

      </v-menu>  
      
    </v-app-bar>

    <v-main>

      <router-view/>

    </v-main>

    <site-footer/>

  </v-app>

</template>


<script>

import SiteFooter from '@/components/Footer.vue';


export default {
  components: { SiteFooter },
  name: 'App',
  data: () => ({ drawer: null }),
  computed:{
    theme(){
      return (this.$vuetify.theme.dark) ? 'dark' : 'light'
    },
    isLoggedIn(){
      return this.$store.getters.getUser 
    },
    relType(){
      return this.$store.getters.getRelType
    }
  }
};
</script>


<style lang="scss">

  .v-data-table {

    th {
      background:#f4f2f6;
      white-space: nowrap;
      max-width: 25em;
      //overflow: hidden;
      border-bottom: none !important;
      span {
        text-overflow: ellipsis;
      }  
    }
    td {
      white-space: nowrap;
      max-width: 15em;
      overflow: hidden;
      text-overflow: ellipsis;
      &.td-title {
         max-width: 25em; 
      }
    }
  }

</style>
