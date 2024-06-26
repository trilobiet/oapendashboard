<template>

  <v-form
    ref="userForm"
    v-model="isValidForm"
    lazy-validation
  >
    <v-card>

      <v-card-title class="white--text" :style="'background-color:'+headerColor"> 
        <span class="text-h5">
          <v-icon dark large>{{headerIcon}}</v-icon> {{ title }}
        </span>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text>

        <v-container>
          <v-row>
            <v-col cols="12" sm="6" md="4">
              <v-text-field v-model="item.username" label="User name" :rules="validation.username"
               :disabled="!isNewItem"></v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-select v-model="item.role" :items="roles" label="Role" :rules="validation.role" 
               :disabled="!isNewItem" />
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-text-field v-model="item.fullname" label="Full name" :rules="validation.fullName"></v-text-field>
            </v-col>
          </v-row>  
          <v-row>  
            <v-col cols="12" sm="6" md="4">
              <v-row>

                <v-col v-if="isNewItem"> <!-- -->
                  <v-text-field v-model="item.password" label="Password" :rules="validation.password"
                    append-icon="mdi-auto-fix" @click:append="generatePassword"></v-text-field>
                </v-col>

                <v-col v-if="!isNewItem">
                  <v-text-field v-model="item.password" label="Password" :rules="validation.password" v-if="setPassword"
                    append-icon="mdi-auto-fix" @click:append="generatePassword"></v-text-field>
                  <v-text-field v-else label="Set password" disabled><!-- just a 'label' --></v-text-field>
                </v-col>
                <v-col cols="4" v-if="!isNewItem">
                  <v-switch v-model="setPassword" inset></v-switch>
                </v-col>  

              </v-row> 
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <span class="caption">
                Passwords are one-way encrypted and will only be visible on creation!
              </span>
            </v-col>  
          </v-row>  
          <v-row v-if="item.role == 'library'">  
            <v-col cols="12" sm="6" md="4">
              <v-autocomplete v-model="item.countryCode" :items="countries" validate-on-blur
                item-text="name" item-value="code" label="Country" persistent-hint :rules="validation.country"> 
                <template slot="item" slot-scope="data">
                  {{ data.item.name }}&nbsp;({{ data.item.code }})
                </template>
              </v-autocomplete>
            </v-col>
            <v-col cols="12" sm="6" md="4">
              <v-text-field :value="geoToCoords(item.geoLocation)" label="Location" @change="coordsToGeo" :rules="validation.geo" 
               append-icon="mdi-earth" @click:append="openMap(item.geoLocation)" hint="Click icon to view on map" persistent-hint></v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="4">
               <v-select v-model="item.initialRadius" :items="radii" item-text="text" item-value="value" label="Initial radius of area (km)" />
            </v-col>
          </v-row>
          <v-row v-if="item.role == 'library'">    
            <v-col cols="8">
              <v-textarea :value="ipRangesToText(item.ipRanges)" label="Ip Ranges" @change="textToIpRanges" :rules="validation.ipranges"
               rows="8" outlined filled ></v-textarea>
            </v-col>
            <v-col cols="4" class="caption">
              <h4>Ip Ranges example</h4>
              <p>
              132.100.234.55 - 134.10.234.55<br>
              10.20.33.123 - 10.22.44.125<br>
              129.35.10.10 - 129.35.10.10<br>
              </p>
              <ul>
                <li>Each range on a new line;</li>
                <li>A range always consists of 2 ip addresses, but they can be identical to set a single ip;</li>
                <li>Within a range ip addresses are separated by '&nbsp;-&nbsp;' (space+dash+space);</li>
                <li>Only valid and complete ip4 addresses are allowed.</li>
              </ul>  
            </v-col>  
          </v-row>
          <v-row v-if="item.role == 'publisher'">
            <v-col cols="12">
              <v-autocomplete v-model="item.irusIds" :items="publishers" multiple validate-on-blur chips small-chips deletable-chips outlined filled 
                item-text="name" item-value="id" label="Publisher(s)" :rules="validation.publishers">

                <!-- show publisher along with irusUk id -->   
                <template v-slot:item="data">
                  <v-list-item-content>
                    <v-list-item-title v-html="data.item.name"></v-list-item-title>
                    <v-list-item-subtitle class="caption blue-grey--text text--lighten-3" v-html="'id: '+showSpace(data.item.id)"></v-list-item-subtitle>
                  </v-list-item-content>                  
                </template>  

              </v-autocomplete>
            </v-col>  
          </v-row>  
          <v-row v-if="item.role == 'funder'">
            <v-col cols="12">
              <v-autocomplete v-model="item.irusIds" :items="funders" multiple validate-on-blur chips small-chips deletable-chips outlined filled 
                item-text="name" item-value="id" label="Funder(s)" :rules="validation.funders">

                <!-- show funder along with irusUk id -->   
                <template v-slot:item="data">
                  <v-list-item-content>
                    <v-list-item-title v-html="data.item.name"></v-list-item-title>
                    <v-list-item-subtitle class="caption blue-grey--text text--lighten-3" v-html="'id: '+showSpace(data.item.id)"></v-list-item-subtitle>
                  </v-list-item-content>                  
                </template>  

              </v-autocomplete>  
            </v-col>  
          </v-row>  

        </v-container>

      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions class="blue-grey lighten-5" >
        <v-container>
          <v-row >
            <v-col>
              <span class="text-left red--text caption" v-if="!isValidForm">
                <v-icon color="red" >mdi-alert-circle-outline</v-icon>
                Please fix validation issues before saving
              </span>
            </v-col>
            <v-col class="text-right">
                <v-btn color="blue darken-3" text @click="cancel">Cancel</v-btn>
                <v-btn color="blue darken-3" text @click="save" :disabled="!isValidForm">Save</v-btn>
            </v-col>
          </v-row>   
        </v-container>
      </v-card-actions>

    </v-card>

  </v-form>  

</template>


<script>

  export default {

    props: {
        isOpen: {type: Boolean, default: null},
        item: {type: Object, default: null},
        title: {type: String, default: ''},
        takenUsernames: {type: Array, default: ()=>[]},
        headerColor: {type: String, default: 'gray'},
        headerIcon: {type: String, default: 'mdi-account-edit'},
    },

    data() {
      return {
        isValidForm: false,
        setPassword: false,
        roles: ["publisher","funder","library"],
        countries: this.$store.getters.getSingleCountries,
        publishers: this.$store.getters.getSinglePublishers,
        funders: this.$store.getters.getSingleFunders,
        editedUser: "",
        radii: [
          {value:20, text:"20 km"},
          {value:50, text:"50 km"},
          {value:100, text:"100 km"},
          {value:250, text:"250 km"},
          {value:500, text:"500 km"}
        ]
      }      
    },

    watch: {
      isOpen: function(newVal,oldVal) {
        //if (newVal==true) {
          this.$refs.userForm.resetValidation()
        //}
        console.log('Prop changed: ', newVal, ' | was: ', oldVal)
        console.log(JSON.stringify(this.item))
        this.editedUser = this.item.username
        this.setPassword = false
        this.item.password = ''
      },

      setPassword: function(newVal, oldVal) {
        if (newVal==false) this.item.password = ''
      }    
    },  

    computed: {

      isNewItem() {
         return this.item.id == null || this.item.id.length == 0
      },

      validation() {

        return {

          username: [
            v => !!v || 'User name is required',
            v => (v && v.length >= 4) || 'User name cannot be shorter than 4 characters',
            v => (v && v.length <= 45) || 'User name cannot be longer than 45 characters',
            v => (v && !/\s/g.test(v)) || 'User name must not contain whitespace',
            v => this.validateUsernameFree(v) || 'User name is taken. Choose another user name.'
          ],
          fullName: [
            v => !!v || 'Full name is required',
            v => (v && v.length >= 4) || 'Full name cannot be shorter than 4 characters',
            v => (v && v.length <= 255) || 'Full name cannot be longer than 255 characters',
          ],
          password: [
            v => !!v || 'Password is required',
            v => (v && v.trim().length >= 6) || 'Password must contain at least 8 characters',
            v => (v && v.length <= 255) || 'Password cannot be longer than 255 characters',
          ],
          role: [
            v => !!v || 'Please choose a role'
          ],
          country: [
            v => !!v || 'Please choose a country'
          ],
          geo: [
            v => this.validateGeo(v) || "Not a valid geo location"
          ],
          ipranges: [
            v => !!v || 'Please provide at least one ip address range',
            v => this.validateIps(v) || "Ip ranges contain errors"
          ],
          publishers: [
            v => (v && v.length > 0)  || 'Please choose at least one publisher'
          ],
          funders: [
            v => (v && v.length > 0)  || 'Please choose at least one funder'
          ],

        }  
      }
    },

    methods: {

      showSpace(val) {
        return val.replace(/\s/gi,"_")
      },

      cancel() {
        this.$emit('cancel')
      },

      save() {

        if(this.$refs.userForm.validate()) {
        // if (this.isValidForm) {
          console.log("VALIDATION PASSED! " + JSON.stringify(this.item))
          this.$emit('save')
        }  
        else {
          console.log("VALIDATION ERRORS!") 
        }  
      },

      geoToCoords(val) {
         return(val.lat + ", " + val.lon)
      },

      coordsToGeo(val) {

        function numerify(val) {
          if( val ) return isNaN( val.trim() ) ? 0: val.trim()
          else return 0;
        }

        const ar = val.split(",")
        const lat = numerify(ar[0])
        const lon = numerify(ar[1])
        const nlat = Math.min( 90, Math.max(-90, lat) )
        const nlon = Math.min( 180, Math.max(-180, lon) )
        const geo = { "lat": nlat, "lon": nlon }

        this.item.geoLocation = geo
      },

      openMap(geo) {

        const url = 'https://www.openstreetmap.org?mlat='
          + geo.lat + '&mlon=' + geo.lon 

         window.open(url, "_blank");     
      },

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
            return { "ipStart": pair[0], "ipEnd": pair[1] }
          })

          // console.log(JSON.stringify(ipRanges))

          this.item.ipRanges = ipRanges
      },

      validateIps(val) {

        if (val.trim().length==0) return true;

        const faultyEntry = val.split("\n").find(nv => {
          const pair = nv.split(" - ")
          return !this.$func.isValidIp4(pair[0]) || !this.$func.isValidIp4(pair[1])
        })

        if (faultyEntry) {
          console.log("Faulty entry: " + faultyEntry)
          return false
        }  
        else {
          console.log("No faulty entries")
          return true
        }  
      },

      validateGeo(val) {

        const regLat = /^(-?[1-8]?\d(?:\.\d{1,18})?|90(?:\.0{1,18})?)$/;
        const regLon = /^(-?(?:1[0-7]|[1-9])?\d(?:\.\d{1,18})?|180(?:\.0{1,18})?)$/;
        const ar = val.split(/,[\s]{0,1}/)

        var validLat = regLat.test(ar[0]);
        var validLon = regLon.test(ar[1]);
        if(validLat && validLon) return true
        else return false;
      },

      validateUsernameFree(val) {

        if (this.isNewItem) {

          if (!val) return false;
          const posInList = this.takenUsernames.indexOf(val.trim())
          if ( posInList == -1 ) return true
          else return false  
        }
        else return true // existing users cannot be renamed
      },

      generatePassword() {

        var length = 8,
            charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
            retVal = "";

        for (var i = 0, n = charset.length; i < length; ++i) {
          retVal += charset.charAt(Math.floor(Math.random() * n));
        }

        this.setPassword = false

        console.log("PW " + retVal)
        this.item.password = retVal
        console.log("PW " + this.item.password)
        this.setPassword = true
        
      }

    },


  }

</script>