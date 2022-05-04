<template>

  <div class="about">

    <v-container fluid>

      <v-card class="elevation-5 pa-6" min-height="70vh">

          <v-row>

            <v-col cols="12" lg="8" class="pa-12">

              <h1>How to use this dashboard</h1>

              <p>
                The OAPEN dashboard offers two views:
              </p>
              
              <ol>
                <li>Title based</li>
                <li>Location based</li>
              </ol>  

              <p>
                The view shows the data of twelve months. By selecting the 'end month' it is possible to investigate another time period. Furthermore, the data can be filtered in several ways.
              </p>

              <p>
                Select the view with the menu in the top left corner. 
                <v-img src="../assets/page-about/view_selection.png" transition="scale-transition" contain width="282" class="content-image"/>
              </p>
              <p>  
                The selected data can be exported to a CSV (Comma Separated Values) text file, allowing further analysis.
              </p>

              <h3>1.1 Title based view</h3>

              <p>
                The title based view displays the number of downloads for each title. It shows the filter options, below that the list of titles and below that, a bar chart of the 25 most downloaded titles.
              </p>

              <h4>1.1.1 Filter options for publishers</h4>

              <p>
                A publisher can filter the data on:
              </p>

              <ul>
                <li>Time period: end month</li>
                <li>Country</li>
                <li>Funder</li>
                <li>Publication type: book or book segment</li>
              </ul>

              <p><v-img src="../assets/page-about/filter_publisher.png" transition="scale-transition" contain width="910" class="content-image"/></p>    


              <h4>1.1.2 Filter options for funders</h4>

              <p>
                A funder can filter the data on:
              </p>

              <ul>
                <li>Time period: end month</li>
                <li>Country</li>
                <li>Publisher</li>
                <li>Publication type: book or book segment</li>
              </ul>

              <p><v-img src="../assets/page-about/filter_funder.png" transition="scale-transition" contain width="910" class="content-image"/></p>    


              <h4>1.1.3 Filter options for libraries</h4>

              <p>
                A library can filter the data on:
              </p>

              <ul>
                <li>Time period: end month</li>
                <li>Funder</li>
                <li>Publisher</li>
                <li>Publication type: book or book segment</li>
              </ul>

              <p><v-img src="../assets/page-about/filter_library.png" transition="scale-transition" contain width="910" class="content-image"/></p>    


              <h4>1.1.4 Bar chart</h4>

              <p>
                The bar chart displays the 25 most downloaded titles, based on the selection. Use the menu at the top right corner to export the bar chart to an image file or to export the data to a CSV (Comma Separated Values) text file.                  
                <v-img src="../assets/page-about/bar_chart.png" transition="scale-transition" contain width="910" class="content-image"/>
              </p>  


              <h3>Location based view</h3>

              <p>
                The location based view displays the downloads based on regions. For publishers and funders, this view shows the filter options, below that the list of countries and downloads and below that, a map and a bar chart of the 25 most downloaded titles. 
              </p>
              <p>  
                For libraries, the view shows the filter options, below that the downloads originating from an area around the library building, and below that, a map and a bar chart of the 25 most downloaded titles. The filter options allow the selection of a larger or smaller area.
                <v-img src="../assets/page-about/filter_library_radius.png" transition="scale-transition" contain width="910" class="content-image"/>
              </p>  


              <h2>2 Downloading data</h2>  

              <p>
                The data can be exported to a CSV (Comma Separated Values) text file in UTF-8 format. The data to be downloaded depends on the selection you have made using the filter options.
              </p>
              <p>  
                To export the data, click on the download button.
                <v-img src="../assets/page-about/download_button.png" transition="scale-transition" contain width="120" class="content-image"/>
              </p>  
              <p>
                Please keep in mind that programs such as MS Excel might need a specific configuration to import the data. Consult your IT department if necessary.
              </p>  

            </v-col>  

            <v-col cols="12" lg="4" class="pa-12 oapen-about-sidebar">

              <h1>About the OAPEN dashboard</h1>
  
              <p>
                The goal of the OAPEN Dashboard is to provide COUNTER conformant usage statistics to publishers, funders and libraries. Each of these organisation types has different needs, and the dashboard reflects that. Once the dashboard has been set up for your organisation, it allows the members of your organisation to continuously check and download your usage data. The data is automatically updated monthly. The dashboard has been secured and each organisation receives only one user name and password.
              </p>

              <p>
                All data is COUNTER conformant and is based on Release 5 of the COUNTER Code of Practice. This provides you with comparable, authoritative, standards-based data. The metric used is Total_Item_Requests.;  it counts the total number of views of a full content item, e.g. downloading a PDF.
              </p>

              <p>
                The usage data is provided by IRUS-UK. As part of the COUNTER processing, IRUS filters out usage by known robots using both the COUNTER robots list and an additional IRUS robots list. Requests to download an item where the mouse is clicked twice within thirty seconds are designated as "double clicks" and only counted as one request.
              </p>

              <p>
                The dashboard contains usage data obtained from April 2020 onwards.
              </p>  

            </v-col>  

          </v-row>  

      </v-card>  

    </v-container>

  </div>

</template>


<style lang="scss" scoped>
  
  h1,h2,h3,h4,h5,h6 {
    margin: 3em 0 .5rem;
  }

  h1:first-child,
  h2:first-child,
  h3:first-child {
    margin-top: 0;
  }

  h1 {
    border-bottom: solid 1px #ccc;
  }

  ul, ol {
    margin: 1em 0;
  }

  .content-image {
    margin: 2rem 0;
  }

  .oapen-about-sidebar {
    background: #f3f3f3; 

    p {
      text-align:justify;
    }
  }

</style>