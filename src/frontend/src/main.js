
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

// Session time out for api calls: redirect to login page 
/*
axios.interceptors.response.use(function (response) {
  // Any status code that lie within the range of 2xx cause this function to trigger
  // Do something with response data
  return response;
  }, function (error) {
  // Any status codes that falls outside the range of 2xx cause this function to trigger
  // Do something with response error
  if(error.response.status === 403) {
      // redirect to login page
      console.log("Session expired...")
      window.location.href = "/login";
  }
  return Promise.reject(error);
});*/


import VueApexCharts from 'vue-apexcharts'
Vue.use(VueApexCharts)
Vue.component('apexchart', VueApexCharts)

// TODO temporary 'login'
// const userId = "b818ba9d-2dd9-4fd7-a364-7f305aef7ee9"; // funder
// const userId = "df73bf94-b818-494c-a8dd-6775b0573bc2"; // publisher
// const userId = "6145e100-82b1-11ec-a8a3-0242ac120002"; // library

// Preload some api lookup data
const promises = [

  // Once we get here we already have a browser session (Spring Boot),
  // so we only need to ask the api who is the logged in user. 
  axios.get('/api/user')
    .then(resp => store.commit("setUser",resp.data)),

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



