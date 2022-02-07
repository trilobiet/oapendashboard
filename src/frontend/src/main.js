import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import store from './store'
//import {globalfunctions} from './globalfunctions.js'

//Vue.prototype.$globalfunctions = globalfunctions

Vue.config.productionTip = false

//store.dispatch("setGlobals");

// TODO temporary 'login'
const userId = "b818ba9d-2dd9-4fd7-a364-7f305aef7ee9"; // funder
// const userId = "df73bf94-b818-494c-a8dd-6775b0573bc2"; // publisher
// const userId = "6145e100-82b1-11ec-a8a3-0242ac120002"; // library

// TODO

// Preload some api lookup data
const promises = [

  fetch('/api/find-user?id='+userId)
  .then(r => r.json())
  .then(json => store.commit("setUser",json)),

  fetch('/api/lastrequestablemonth')
    .then(r => r.json())
    .then(json => store.commit("setLastRequestableMonth",json)),

  fetch('/api/countries')
    .then(r => r.json())
    .then(json => store.commit("setCountries",json)),

  fetch('/api/months')
    .then(r => r.json())
    .then(json => store.commit("setMonths",json)),

  fetch('/api/itemtypes')
    .then(r => r.json())
    .then(json => store.commit("setItemTypes",json)),
]


Promise.all(promises).then( () => {

  new Vue({
    vuetify,
    store,
    router,
    render: h => h(App)
  }).$mount('#app')

}); 



