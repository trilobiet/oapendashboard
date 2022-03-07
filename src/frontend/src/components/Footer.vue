<template>
  <v-footer color="#ccccdd" class="lighten-1 mt-12 py-4">
    <v-row justify="center" no-gutters>
      <v-btn
        v-for="link in links"
        :key="link"
        color="#334"
        text
        rounded
        class="my-2"
      >
        {{ link }}
      </v-btn>
      <v-col class="pa-2 py-8 text-center white--text" cols="12">
        &copy;{{ new Date().getFullYear() }} — <strong>OAPEN.org</strong>
      </v-col>
    </v-row>
  </v-footer>
</template>


<script>
export default {
  data: () => ({
    links: ["Home", "About", "Dashboard", "Logout"],
  }),
};
</script>