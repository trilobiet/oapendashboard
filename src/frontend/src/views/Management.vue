<template>

  <div class="management">

    <!-- TODO toggle from axios on save if an error occurs -->  
    <v-alert v-if="true" type="error" dismissible>Invalid data or email or password. Whatever.</v-alert>


      <v-container fluid>

      <v-row>
        <v-col>

          <v-card class="elevation-5">

              <v-card-title>
                Users
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

                <v-data-table 
                  :loading="loading" :search="tableSearch" 
                  :headers="headers" :items="items"   
                  :footer-props="{'items-per-page-options': [10, 25, 50, 100, -1]}"
                  calculate-widths>

                  <template v-slot:item.geoLocation="{ item }">
                    <span v-html="geoLink(item)"></span>
                  </template> 

                  <!--  FORM ====================================================== -->
                  <template v-slot:top>

                    <v-toolbar flat>

                      <v-toolbar-title>Users</v-toolbar-title>

                      <v-divider class="mx-4" inset vertical></v-divider>
                      <v-spacer></v-spacer>
                      
                      <!-- New User Dialog ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
                      <v-dialog v-model="dialog" max-width="50%">
                      
                        <template v-slot:activator="{ on, attrs }">
                          <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on">New User</v-btn>
                        </template>
                      
                        <v-card>
                      
                          <v-card-title>
                            <span class="text-h5">{{ formTitle }}</span>
                          </v-card-title>

                          <v-card-text>
                            <v-container>
                              <v-row>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.name" label="User name"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.role" label="Role"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.id" label="Id"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.countryCode" label="Country code"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.geoLocation.lat" label="Location lat"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.geoLocation.lon" label="Location lon"></v-text-field>
                                </v-col>
                              </v-row>
                            </v-container>
                          </v-card-text>

                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="close">Cancel</v-btn>
                            <v-btn color="blue darken-1" text @click="save">Save</v-btn>
                          </v-card-actions>

                        </v-card>

                      </v-dialog>

                      <!-- DELETE Dialog ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
                      <v-dialog v-model="dialogDelete" max-width="500px">

                        <v-card>
                          <v-card-title class="text-h5">Are you sure you want to delete this user?</v-card-title>
                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
                            <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
                            <v-spacer></v-spacer>
                          </v-card-actions>
                        </v-card>

                      </v-dialog>

                    </v-toolbar>
                  </template>
                  <!--  /FORM ====================================================== -->

                  <template v-slot:item.actions="{ item }">
                    <v-icon small class="mr-2" @click="editItem(item)">mdi-pencil</v-icon>
                    <v-icon small @click="deleteItem(item)">mdi-delete</v-icon>
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

  </div>

</template>

<script>
import axios from 'axios';

export default {
  components: {  },
  
  data() {
    return {
      loading: true,
      tableSearch: '',
      dialog: false,
      dialogDelete: false,      
      headers: [],
      items:[], 
      editedIndex: -1,      
      editedItem: {
        name: '',
        role: '',
        id: '',
        irusId: '',
        countryCode: '',
        geoLocation: {'lat':'0.0','lon':'0.0'},
      },
      defaultItem: {
        name: '',
        role: '',
        id: '',
        irusId: '',
        countryCode: '',
        geoLocation: {'lat':'0.0','lon':'0.0'},
      },      
    }    
  },
  
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New User' : 'Edit User'
    },
  },

  /*created() {
    this.callApi();
  },*/

  mounted() {
    this.callApi();
  },
  
  watch: {
    dialog (val) {
      val || this.close()
    },
    dialogDelete (val) {
      val || this.closeDelete()
    },
  },
  
  methods: {

    geoLink(item) {

        if( item.geoLocation.lat != 0 && item.geoLocation.lon != 0)
          return  '<a href="https://www.openstreetmap.org/search?query='
            + item.geoLocation.lat + ',' + item.geoLocation.lon 
            + '" target="_blank">'
            + item.geoLocation.lat+ ', ' + item.geoLocation.lon        
            + '</a>' 
        else
          return ''
    },
    
    callApi() {

      this.loading = true; // visual darkening while loading  
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
        { text: "Name", value: "name" },
        { text: "Role", value: "role" },
        { text: "Id", value: "id" },
        { text: "IRUS Id", value: "irusId" },
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

    deleteItemConfirm () {
      // TODO Call api to remove by id (this.items[this.editedIndex].id)
      console.log("Deleted item: " + JSON.stringify(this.items[this.editedIndex].id))
      this.items.splice(this.editedIndex, 1)
      this.closeDelete()
    },

    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete () {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save () {

      // Content-type: application/json
      axios.post(`/api/save-user`, this.editedItem)
        .then( resp => {
          console.log(resp)
          if (this.editedIndex > -1) 
            Object.assign(this.items[this.editedIndex], this.editedItem)
          else 
            this.items.push(this.editedItem)
          }
        )
        .catch( err =>
          console.log(err.response)
          // Show error on alert
        )
        .finally(() => console.log("Ready.") )

      this.close()
    },    
    
  }

}; 
</script>