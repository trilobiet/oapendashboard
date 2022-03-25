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
                                  <v-text-field v-model="editedItem.username" label="User name"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.password" label="Password"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.fullname" label="Full name"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-select v-model="editedItem.role" :items="roles" label="Role" />
                                </v-col>
                              </v-row>  
                              <v-row v-if="editedItem.role=='library'">  
                                <v-col cols="12" sm="6" md="4">
                                  <v-autocomplete v-model="editedItem.countryCode" :items="countries" 
                                    item-text="name" item-value="code" label="Country" persistent-hint>
                                    <template slot="item" slot-scope="data">
                                      {{ data.item.name }}&nbsp;({{ data.item.code }})
                                    </template>
                                  </v-autocomplete>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.geoLocation.lat" label="Location lat"></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" md="4">
                                  <v-text-field v-model="editedItem.geoLocation.lon" label="Location lon"></v-text-field>
                                </v-col>
                                <v-col cols="12">
                                  <!--<v-textarea v-model="ipRangesToText" label="Ip Ranges"></v-textarea>-->
                                  <v-textarea :value="ipRangesToText(editedItem.ipRanges)" label="Ip Ranges" @change="textToIpRanges"></v-textarea>
                                </v-col>
                              </v-row>
                              <v-row v-if="editedItem.role=='publisher'">
                                <v-col cols="12" sm="6" md="4">
                                  <v-autocomplete v-model="editedItem.irusIds" :items="publishers" multiple
                                    item-text="name" item-value="id" label="Publisher(s)"/>
                                </v-col>  
                              </v-row>  
                              <v-row v-if="editedItem.role=='funder'">
                                <v-col cols="12" sm="6" md="4">
                                  <v-autocomplete v-model="editedItem.irusIds" :items="funders" multiple
                                    item-text="name" item-value="id" label="Funder(s)"/>
                                </v-col>  
                              </v-row>  
                            </v-container>
                          </v-card-text>

                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="cancel">Cancel</v-btn>
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
                            <v-btn color="blue darken-1" text @click="cancelDelete">Cancel</v-btn>
                            <v-btn color="blue darken-1" text @click="confirmDelete">OK</v-btn>
                            <v-spacer></v-spacer>
                          </v-card-actions>
                        </v-card>

                      </v-dialog>


                      <!-- Error Dialog ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
                      <v-dialog v-model="dialogError" max-width="500px">

                        <v-card>
                          <v-card-title class="text-h6">Oops</v-card-title>
                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="closeError">Close</v-btn>
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
      dialogError: false,      
      headers: [],
      items:[], 
      editedIndex: -1,      
      editedItem: {
        username: '',
        password: '',
        fullname: '',
        role: '',
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
        //id: '',
        irusIds: [],
        countryCode: '',
        geoLocation: {'lat':'0.0','lon':'0.0'},
        ipRanges: []
      },   
      roles: ["publisher","funder","library"],
      countries: this.$store.getters.getSingleCountries,
      publishers: this.$store.getters.getSinglePublishers,
      funders: this.$store.getters.getSingleFunders,
    }    
  },
  
  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New User' : 'Edit User'
    },

    /*
    ipRangesToText: {
        get(){
          //this function will determine what is displayed in the input
          console.log("HALLO: " + JSON.stringify(this.editedItem.ipRanges))
          return this.editedItem.ipRanges.reduce((tot,y) => tot + "" + y.ipStart + " - " + y.ipEnd + "\n", "")
        },
        set(newVal){
          //this function will run whenever the input changes
          const ret = newVal.split("\n") // an Array
          .filter(nv => {
            const pair = nv.split(" - ")
            return this.$func.isValidIp4(pair[0]) && this.$func.isValidIp4(pair[1])
          })
          .map(nv => {
            const pair = nv.split(" - ")
            return {"ipStart":pair[0],"ipEnd":pair[1]}
          })

          console.log("NIEUW: " + JSON.stringify(ret))
        }
    }
    */

  },

  /*created() {
    this.callApi();
  },*/

  mounted() {
    this.callApi();
    console.log(JSON.stringify(this.publishers))
  },
  
 /* watch: {
    dialog (val) {
      val || this.close()
    },
    dialogDelete (val) {
      val || this.closeDelete()
    },
  }, */
  
  methods: {

    ipRangesToText(val) {
      return val.reduce((tot,y) => tot + "" + y.ipStart + " - " + y.ipEnd + "\n", "")
    },

    textToIpRanges(val) {

      const ipRanges = val.split("\n") // an Array
        .filter(nv => {
          const pair = nv.split(" - ")
          return this.$func.isValidIp4(pair[0]) && this.$func.isValidIp4(pair[1])
        })
        .map(nv => {
          const pair = nv.split(" - ")
          return {"ipStart":pair[0],"ipEnd":pair[1]}
        })

        this.editedItem.ipRanges = ipRanges

        console.log("NIEUW!: " + JSON.stringify(ipRanges))
    },

    changeIt() {
      console.log("CHANGED")
    },

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

        if( item.geoLocation.lat != 0 && item.geoLocation.lon != 0)
          return  '<a href="https://www.openstreetmap.org?mlat='
            + item.geoLocation.lat + '&mlon=' + item.geoLocation.lon 
            + '" target="_blank">'
            + item.geoLocation.lat+ ', ' + item.geoLocation.lon        
            + '</a>' 
        else
          return ''
    },
    
    callApi() {

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
        { text: "Username", value: "username" },
        { text: "Role", value: "role" },
        { text: "Name", value: "fullname" },
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
          console.log(resp)
          console.log("DELETED " + this.editedIndex)
          this.items.splice(this.editedIndex, 1)
        })
        .catch( err => {
          console.log(err.response)
          // Show error on alert
          this.dialogError = true
        })
        .finally(() => {
          this.setDefault();
          console.log("Ready.") 
        })

      this.dialogDelete = false
    },

    cancel () {
      this.dialog = false
      /*this.$nextTick(() => {
        this.setDefault();      
      })*/
      this.setDefault();      
    },

    cancelDelete () {
      this.dialogDelete = false
      /*this.$nextTick(() => {
        this.setDefault();      
      })*/
      this.setDefault();      
    },

    closeError () {
      this.dialogError = false
    },

    save () {

      // Content-type: application/json
      axios.post(`/api/save-user`, this.editedItem)
        .then( resp => {
          console.log(resp)
          if (this.editedIndex > -1) {
            Object.assign(this.items[this.editedIndex], this.editedItem)
            console.log("ASSIGN " + this.editedIndex + "  ->  " + JSON.stringify(this.editedItem))
          }  
          else {
            this.items.push(this.editedItem)
            console.log("PUSH " + this.editedIndex + "  ->  " + JSON.stringify(this.editedItem))
            console.log("NEW id: " + resp.data.id)
            this.editedItem.id = resp.data.id
          }  
        })
        .catch( err =>
          console.log(err.response)
          // Show error on alert
        )
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


  }

}; 
</script>