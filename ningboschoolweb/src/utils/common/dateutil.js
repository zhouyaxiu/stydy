import moment from 'moment'

export default {
  // 今天
  currentDate () {
    return moment().format('YYYY-MM-DD')
  },
  // num天前，比如 7天前，传7
  dayAddNum (num) {
    return moment().subtract(num, 'day').format('YYYY-MM-DD')
  },

  // 上个月第一天 如 2021-08-01
  lastMonthStartDay () {
    return moment().subtract(1, 'month').startOf('month').format('YYYY-MM-DD')
  },
  // 上月最后一天 如 2021-08-31
  lastMonthEndDay () {
    return moment().subtract(1, 'month').endOf('month').format('YYYY-MM-DD')
  },
  // 当前年 如2021
  currentYear () {
    return moment().format('YYYY')
  },
  getMonthDayNum (selectDate) {
    let myDate = new Date(selectDate.replace(/-/g, '/'))
    let year = myDate.getFullYear()
    let month = myDate.getMonth() + 1
    myDate = new Date(year, month, 0)
    // let day = myDate.getDate()
    return myDate.getDate()
  },
  // 当前月 如 2021-09
  currentMonth () {
    return moment().startOf('month').format('YYYY-MM')
  },
  // 上个月 如 2021-08
  lastMonth () {
    return moment().subtract(1, 'month').startOf('month').format('YYYY-MM')
  },
  // 上个月日期 如 2021-08-01
  lastMonthDate () {
    return moment().subtract(1, 'month').startOf('month').format('YYYY-MM-DD')
  }
}
