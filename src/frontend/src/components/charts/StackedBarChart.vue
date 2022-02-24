<template>

    <v-card class="elevation-5 pa-2" min-height="60vh" :height="height">
      <apexchart type="bar" :options="options" :series="series" height="100%"></apexchart>
    </v-card>    

</template>

<script>

export default {
  components: { },
  
  props: {
    title: {type: String, default: ""},
    rows: {type: Number, default: 10},
    items: {type: Array, default: () => []},
    categoriesField: {type: String, default: ""}
  },

  data() {
    return {
      height: "100%"
    }  
  },

  computed: {

      series() { return this.getMonthsData() },

      options() {

        return {  
          chart: {
            type: 'bar',
            stacked: true,
          },
          dataLabels: {
            enabled: false,
          },  
          stroke: {
            width: 1,
            colors: ['#fff']
          },
          plotOptions: {
            bar: { horizontal: true, barHeight: '95%' },
          },
          title: {
            text: this.title
          },
          xaxis: { lines: {show: true}, 
            categories: this.getCategories()
          },
          yaxis: { lines: {show: true} 
          },
          legend: {
            position: 'top',
            horizontalAlign: 'left',
            offsetX: 40
          },
        }
      }

  },

  updated: function() {
      this.height = Math.min(this.items.length,this.rows) * 25 
      console.log("HEIGHT: " + this.height)
  },

  methods: {

    // labels y-axis
    getCategories() {

      if (!this.items.length) return []
      else {

        let len = Math.min(this.items.length,this.rows)
        let categories = []
        for (let i=0; i<len; i++) categories.push(this.items[i][this.categoriesField])
        // console.log("Categories: " + JSON.stringify(categories))
        return categories
      }
    },

    // bars
    getMonthsData() {

      if (!this.items.length) return []
      else {

        let len = Math.min(this.items.length,this.rows)
        let monthsData = []
        let months = Object.keys(this.items[0].monthTotals) // 2022-01, 2021-12, ...

        for(const month of months) {

          const temp = [];
          for (let i=0; i<len; i++) temp.push(this.items[i].monthTotals[month])
          monthsData.push({ "name": month, "data": temp }) 
        }
        // console.log("Data: " + JSON.stringify(monthsData))
        return monthsData
      }
    },


  },


}; 
</script>
