<template>

  <div>

    <funder v-if="this.user.role=='funder'" 
      :user="this.$store.getters.getUser" />
    <publisher v-else-if="this.user.role=='publisher'" 
      :user="this.$store.getters.getUser" />
    <library v-else-if="this.user.role=='library'" 
      :user="this.$store.getters.getUser" />
    <div v-else>No valid user</div>

  </div>  
         
</template>

<script>
import Funder from '@/components/Funder.vue'
import Publisher from '@/components/Publisher.vue'
import Library from '@/components/Library.vue'

export default {
  name: 'Dashboard',
  components: {
    Funder, Publisher, Library
  },
  data() {
    return {
      user: this.$store.getters.getUser,
      drawer: false
    }
  }
}
</script>

<style>
.fixed-bar {
  position: sticky;
  position: -webkit-sticky; /* for Safari */
  top: 4em;
  z-index: 2;
}
</style>

