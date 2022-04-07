<template>
  <v-footer color="#ccccdd" class="lighten-1 mt-12 py-4">
    <v-row justify="center" no-gutters>

      <v-btn color="#334" text rounded class="my-2" to="/dashboard">home</v-btn>
      <v-btn color="#334" text rounded class="my-2" to="/about">about</v-btn>
      <v-btn color="#334" text rounded class="my-2" href="/logout?logout">logout</v-btn>

      <v-col class="pa-2 py-8 text-center credits" cols="12">
        &copy;{{ new Date().getFullYear() }} — 
        <a href="https://oapen.org" target="_blank">OAPEN.org</a>
        <br/>
        <span class="text-caption">
          analytics &amp; application by
          <a href="https://www.trilobiet.eu" target="_blank">trilobiet.eu</a>
        </span>  
      </v-col>

    </v-row>
  </v-footer>
</template>

<script>
export default {
};
</script>

<style scoped>

  .v-footer .credits {
    color: #f3f3f3;
  }  

  .v-footer a {
    color: #f3f3f3;
    text-decoration: none;
  }

  .v-footer a:hover {
    color: #fff;
    text-decoration: none;
    opacity: 1;
  }


</style>