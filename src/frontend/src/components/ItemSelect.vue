<template>
  <v-autocomplete
    v-model="localValue"
    :items="titles"
    :search-input.sync="search"
    item-text="title"
    item-value="id"
    label="Title"
    placeholder="Start typing to Search"
    clearable
    return-object
    allow-overflow
    @click:clear="clearList"
  ></v-autocomplete>
</template>


<script>
import axios from "axios";

export default {
  props: {
    value: {
      type: Object,
      required: true,
    },
    relId: { // who is asking (only show own item titles)
      type: String,
      required: true  
    },
    relType: { // and is it a funder or a publisher or a library
      type: String,
      required: true  
    }
  },

  // https://zaengle.com/blog/using-v-model-on-nested-vue-components
  // Avoid mutating a prop directly since the value will be overwritten whenever 
  // the parent component re-renders. Instead, use a data or computed property 
  // based on the prop's value.   
  computed: {

    localValue: {
      get() {
        return this.value;
      },
      set(localValue) {
        if (localValue) this.$emit("input", localValue)
        else this.$emit("input", {"id":"","title":""}) // reset
      },
    },

  },

  data() {
    return {
      title: "",
      titles: [],
      search: "",
      loadingTitles: false,
      blockSearch: false,
    };
  },

  watch: {
    search(val) {
      if (!val) return;
      if (val.length > 3) this.callApi(val);
      else return;
    },
  },

  methods: {
    callApi(val) {

      let apiCall;  
      // In case of funder or publisher only show own titles
      if (this.relType=="funder") 
        apiCall = `find-funder-item?funders=${this.relId}&`
      else if (this.relType=="publisher") 
        apiCall = `find-publisher-item?publishers=${this.relId}&`
      else 
        apiCall = "find-item?";

      this.blockSearch = true;
      axios
        .get(`/api/${apiCall}title=${val}`)
        .then(resp => this.titles = resp.data)
        .catch(error => console.log(error))
        .finally(() => (this.blockSearch = true));
    },

    clearList() {
        // console.log("Clear list")
        this.titles=[]
    }
  },
};
</script>