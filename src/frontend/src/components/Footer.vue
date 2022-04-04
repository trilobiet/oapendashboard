<template>
  <v-footer color="#ccccdd" class="lighten-1 mt-12 py-4">
    <v-row justify="center" no-gutters>

      <v-btn color="#334" text rounded class="my-2" to="/dashboard">home</v-btn>
      <v-btn color="#334" text rounded class="my-2" to="/about">about</v-btn>
      <v-btn color="#334" text rounded class="my-2" href="/logout?logout">logout</v-btn>

      <v-col class="pa-2 py-8 text-center white--text" cols="12">
        &copy;{{ new Date().getFullYear() }} — <strong>OAPEN.org</strong>
      </v-col>

    </v-row>
  </v-footer>
</template>

<script>
export default {
};
</script>