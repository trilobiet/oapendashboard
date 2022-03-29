<template>

  <div class="management">

      <!-- TODO toggle from axios on save if an error occurs -->  
      <v-alert v-if="dialogError" type="error" dismissible >
        <span @click="alertErrorDetail">A problem occurred when saving (click for details)</span>
      </v-alert>

      <v-container fluid>

      <v-row>

        <v-col>

          <v-card class="elevation-5">

              <v-card-title>
                Users

                <v-spacer></v-spacer>
                
                <!-- Edit User Dialog ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
                <v-dialog v-model="dialog" max-width="50%">
                
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">New User</v-btn>
                  </template>

                  <user-form :item="editedItem" :title="formTitle" @cancel="cancel" @save="saveUser" :isOpen="dialog"
                    :takenUsernames="userNames" />

                </v-dialog>

              </v-card-title>

              <v-card-text>   
                <v-text-field
                    v-model="tableSearch"
                    append-icon="mdi-magnify"
                    label="Search"
                    single-line 
                    hide-details></v-text-field>
              </v-card-text>      

              <v-card-text> 

                <v-data-table :sort-by="['role','fullname']"
                  :loading="loading" :search="tableSearch" 
                  :headers="headers" :items="items"   
                  :footer-props="{'items-per-page-options': [10, 25, 50, 100, -1]}"
                  calculate-widths>

                  <template v-slot:item.fullname="{ item }">
                    <span style="cursor:pointer" @click="editItem(item)"
                     class="blue--text text--darken-4"
                    >{{item.fullname}}</span>
                  </template> 

                  <template v-slot:item.geoLocation="{ item }">
                    <span v-html="geoLink(item)"></span>
                  </template> 

                  <template v-slot:item.irusIds="{ item }">
                    <v-chip :color="'#aaa'" dark v-if="item.irusIds.length">
                      {{ item.irusIds.length }}
                    </v-chip>
                  </template>

                  <template v-slot:item.role="{ item }">
                    <v-chip :color="colorForRole(item.role)" dark>
                      <v-icon :title="item.role">{{iconForRole(item.role)}}</v-icon>
                    </v-chip>
                  </template>

                  <template v-slot:item.actions="{ item }">
                    <v-hover v-slot="{ hover }" v-if="item.username!='administrator'">
                      <v-icon @click="deleteItem(item)"
                      :class="hover?'red--text text--darken-3':'gray--text'">mdi-close-circle-outline</v-icon>
                    </v-hover>  
                  </template>

                  <template v-slot:no-data>
                    <v-btn color="primary" @click="initialize">Reset</v-btn>
                  </template>                  

                </v-data-table>

              </v-card-text>
          </v-card>    

        </v-col>
      </v-row>

    </v-container>

    <!-- DELETE Dialog ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
    <v-dialog v-model="dialogDelete" max-width="500px">

      <v-card>
        <v-card-title class="text-h5">Are you sure you want to delete this user?</v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="cancelDelete">Cancel</v-btn>
          <v-btn color="blue darken-1" text @click="confirmDelete">OK</v-btn>
          <v-spacer></v-spacer>
        </v-card-actions>
      </v-card>

    </v-dialog>

  </div>

</template>

<script>
import axios from 'axios';
import UserForm from './UserForm.vue';

export default {
  components: { UserForm },
  
  data() {
    return {
      loading: true,
      tableSearch: '',
      dialog: false,
      dialogDelete: false,      
      dialogError: false, 
      dialogErrorDetail: "",     
      headers: [],
      items:[], 
      editedIndex: -1,      
      editedItem: {
        username: '',
        password: '',
        fullname: '',
        role: 'library',
        id: '',
        irusIds: [],
        countryCode: '',
        geoLocation: {'lat':'0.0','lon':'0.0'},
        ipRanges: []
      },
      defaultItem: {
        username: '',
        password: '',
        fullname: '',
        role: 'library',
        irusIds: [],
        countryCode: '',
        geoLocation: {'lat':'0.0','lon':'0.0'},
        ipRanges: []
      },   
    }    
  },
  
  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New User' : 'Edit User'
    },

    userNames() {
      return this.items.map(i => i.username)
    }
  },

  mounted() {
    this.loadUsers();
  },
  
  methods: {

    colorForRole(role) {

      if (role == 'admin') return 'red'
      else if (role == 'library') return 'green'
      else if (role == 'publisher') return 'blue'
      else return 'purple'
    },

    iconForRole(role) {

      if (role == 'admin') return 'mdi-key-chain-variant'
      else if (role == 'library') return 'mdi-library'
      else if (role == 'publisher') return 'mdi-bank'
      else return 'mdi-hand-coin'
    },

    geoLink(item) {

        if( item.geoLocation && item.geoLocation.lat != 0 && item.geoLocation.lon != 0)
          return  '<a href="https://www.openstreetmap.org?mlat='
            + item.geoLocation.lat + '&mlon=' + item.geoLocation.lon 
            + '" target="_blank">'
            + item.geoLocation.lat+ ', ' + item.geoLocation.lon        
            + '</a>' 
        else
          return ''
    },

    loadUsers() {

      this.loading = true; 
      axios.get(`/api/users`)
      .then(resp => {
         this.items=resp.data;
         this.headers=this.getHeaders(resp.data);
      })
      .catch(error => console.log(error))
      .finally(() => this.loading = false )
    },
    
    getHeaders() {

      let arr = [
        { text: "Role", value: "role" },
        { text: "Name", value: "fullname" },
        { text: "Username", value: "username" },
        { text: "IRUS Ids", value: "irusIds" },
        { text: "Country Code", value: "countryCode", cellClass: "td-title" },
        { text: "Location", value: "geoLocation" },
        { text: 'Actions', value: 'actions', sortable: false },
      ];

      return arr;
    },

    editItem (item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem (item) {
      this.editedIndex = this.items.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    confirmDelete () {

      axios.post(`/api/delete-user`, this.editedItem)
        .then( resp => {
          this.items.splice(this.editedIndex, 1)
        })
        .catch( err => {
          // Show error on alert
          this.dialogError = true
        })
        .finally(() => {
          this.setDefault();
        })

      this.dialogDelete = false
    },

    cancel () {
      this.dialog = false
      this.setDefault();      
    },

    cancelDelete () {
      this.dialogDelete = false
      this.setDefault();      
    },

    saveUser () {

      // Content-type: application/json
      axios.post(`/api/save-user`, this.editedItem)
        .then( resp => {
          console.log(resp)
          if (this.editedIndex > -1) {
            Object.assign(this.items[this.editedIndex], this.editedItem)
          }  
          else {
            this.items.push(this.editedItem)
            this.editedItem.id = resp.data.id
          }  
        })
        .catch( err => {
          console.log(err.response)
          // Show error on alert
          // TODO show logout message on session expiration
          this.dialogErrorDetail = err.response
          this.dialogError = true
        })
        .finally(() => {
          this.setDefault();
          console.log("Ready.") 
        })

      this.dialog = false
    },    

    setDefault() {
      this.editedItem = Object.assign({}, this.defaultItem)
      this.editedIndex = -1
    },

    alertErrorDetail() {
      alert(JSON.stringify(this.dialogErrorDetail))
    }

  }

}; 
</script>