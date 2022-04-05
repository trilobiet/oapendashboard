
// use prefix this.$func. to call from vue files

export const globalfunctions = {

  flattenJSON: (obj={}, res={}, extraKey='') => {

    for(const key in obj) {

      if(typeof obj[key] !== 'object') {
          res[extraKey + key] = obj[key]
      }
      else {
          globalfunctions.flattenJSON(obj[key], res, `${extraKey}${key}.`)
      }
    }
    return res; 
  },

  flattenJsonArray: data => {
    return data.map(r => globalfunctions.flattenJSON(r))
  },

  twelveMonthsBack: yearMonth => {

    // example: 2022-02 -> 2021-03

    let d = new Date(yearMonth)
    d.setMonth(d.getMonth()+1) // 2022-02 -> 2022-03
    const year = d.getFullYear() -1 // 2022-03 -> 2021-03
    let month = '' + (d.getMonth()+1); // zero based and turned into a String
    if (month.length < 2) month = '0' + month;
    return year + '-' + month
  }, 

  isValidIp4: str => {

    const regexExp = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/gi;
    return regexExp.test(str);
  },



}