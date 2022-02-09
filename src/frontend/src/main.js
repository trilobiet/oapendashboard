
import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
import axios from 'axios'

import '@/globalComponents'

import {globalfunctions} from './globalFunctions.js'

Vue.prototype.$func = globalfunctions

Vue.config.productionTip = false

//store.dispatch("setGlobals");

// TODO temporary 'login'
// const userId = "b818ba9d-2dd9-4fd7-a364-7f305aef7ee9"; // funder
// const userId = "df73bf94-b818-494c-a8dd-6775b0573bc2"; // publisher
// const userId = "6145e100-82b1-11ec-a8a3-0242ac120002"; // library

// TODO

// Preload some api lookup data
const promises = [

  //axios.get('/api/find-user?id='+userId).then(resp => store.commit("setUser",resp.data)),

  axios.get('/api/lastrequestablemonth')
    .then(resp => store.commit("setLastRequestableMonth",resp.data)),

  axios.get('/api/countries')
    .then(resp => store.commit("setCountries",resp.data)),

  axios.get('/api/months')
    .then(resp => store.commit("setMonths",resp.data)),

  axios.get('/api/itemtypes')
    .then(resp => store.commit("setItemTypes",resp.data)),
] 

// Proceed when all promises have been fulfilled
Promise.all(promises).then( () => {

  new Vue({
    vuetify,
    store,
    router,
    axios,
    globalfunctions,
    render: h => h(App),
  }).$mount('#app')

}); 



