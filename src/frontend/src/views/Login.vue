<template>

      <v-card width="400" class="mx-auto mt-15">

        <v-card-title class="justify-center my-card-title">
          OAPEN Dashboard Login
        </v-card-title>

        <v-divider/>

        <v-card-subtitle>
          Donec in leo in lectus hendrerit sagittis. Mauris felis sapien, 
          dictum eget nisl eget, vestibulum sagittis sapien. Curabitur maximus euismod ante a tincidunt. 
        </v-card-subtitle>
        
        <v-form @submit.prevent="login">

          <v-card-text>
              <v-text-field
                v-model="input.userid"
                label="User id"
                required
              ></v-text-field>
              <v-text-field
                v-model="input.password"
                label="Password"
                :append-icon="showPass ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="showPass = !showPass"
                required
                :type="showPass ? 'text' : 'password'"
              ></v-text-field>
          </v-card-text>

          <v-divider/>

          <v-card-actions color="#26c6da" class="justify-center" style="background: #dde">
            <v-btn color="navy" type="submit" dark>Sign In</v-btn>
          </v-card-actions>

        </v-form>

      </v-card>

</template>

<script>

import axios from 'axios';

export default {
    name: 'Login',
    components: {
    },
    data() {
        return {
            input: { userid: "6145e100-82b1-11ec-a8a3-0242ac120002", password: "sssss" },
            showPass: false
        }
    },
    mounted() {
        // logout
        this.$store.commit("clearUser");
    },
    methods: {
        login() {
            console.log("LOGGING IN");
            if(this.input.userid != "" && this.input.password != "") {
                axios.get('/api/find-user?id='+this.input.userid)
                .then(resp => this.$store.commit("setUser",resp.data))
                .then(() => this.$router.replace({ name: "Dashboard" }))
            } else {
                console.log("A username and password must be present");
            }
        }
    }  
}
</script>

<style lang="scss" scoped>

  .my-card-title {
     background:#99a; 
     color: white;
  }

</style>
