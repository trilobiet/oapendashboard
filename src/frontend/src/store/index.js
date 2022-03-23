
import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);


export default new Vuex.Store({

    state: {

        user: null,    
        countries: [],
        itemTypes: [],
        funders: [],
        publishers: [],
        months: [],
        lastRequestableMonth: '',
        figuren: []
    },
    mutations: {

        clearUser(state) {
            state.user = null;
        },

        setUser(state,payload) {
            state.user = payload
            this.dispatch("loadPublishers",state.user) // preload publishers for this user
            this.dispatch("loadFunders",state.user) // preload funders for this user
            console.log("SET USER: " + JSON.stringify(state.user)) 
        },

        setFunders(state,payload) {
            const ar = [{"id":"",name:"All funders"}] // empty first
            // load funders for this publisher
            if (state.user.role!='funder') for (const obj of payload) ar.push(obj)
            state.funders = ar
        },

        setPublishers(state,payload) {
            const ar = [{"id":"",name:"All publishers"}] // empty first
            // load publishers for this funder
            if (state.user.role!='publisher') for (const obj of payload) ar.push(obj)
            state.publishers = ar
        },

        setCountries(state,payload) {
            const ar = [{"code":"","name":"All countries"}] // empty first
            for (const key in payload) {
               ar.push({"code":key,"name":payload[key]})     
            }
            state.countries = ar;
        },

        setItemTypes(state,payload) {
            const ar = [{"value":"","text":"All"}] // empty first
            for (const key of payload) {
               ar.push({"value":key,"text":key.replace("_"," ")})     
            }
            state.itemTypes = ar;
        },

        setMonths(state,payload) {
            state.months = payload;
        },

        setLastRequestableMonth(state,payload) {
            state.lastRequestableMonth = payload;
        },
    },
    actions: {

        async loadFunders(context) {
            
            const u = context.state.user; 

            if (u.role == 'publisher') { 
                const r = await axios('/api/funders?publisher-id='+u.irusId)
                this.commit("setFunders", await r.data)     
            }    
            else { // library and admin 
                const r = await axios('/api/funders')
                this.commit("setFunders", await r.data)     
            }
        },

        async loadPublishers(context) {

            const u = context.state.user; 

            if (u.role == 'funder') { 
                const r = await axios.get('/api/publishers?funder-id='+u.irusId)
                this.commit("setPublishers", r.data)
            }         
            else { // library and admin 
                const r = await axios.get('/api/publishers');
                this.commit("setPublishers", r.data)     
            }
        }
    },
    getters: {

        getUser(state) {
            return state.user
        },

        getFunders(state) {
            return state.funders
        },

        getPublishers(state) {
            return state.publishers
        },

        getCountries(state) {
           return state.countries
        },

        getItemTypes(state) {
           return state.itemTypes
        },

        getMonths(state) {
            return state.months
        },

        getLastRequestableMonth(state) {
           return state.lastRequestableMonth;
        },
          
    },

    plugins: [createPersistedState()]

});