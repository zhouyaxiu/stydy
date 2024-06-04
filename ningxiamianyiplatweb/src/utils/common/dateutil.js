import moment from "moment";
// 格式化时间时候用到
function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}
export default {
  // 当前日期开始时刻
  currentTime() {
    return moment().startOf("day");
  },
  // n天前的时间
  nDaysAgoTime(num) {
    return moment().startOf("day").subtract(num, "day");
  },
  // 今天
  currentDate() {
    return moment().format("YYYY-MM-DD");
  },
  // 获取当前时间（时分）
  todayMinutes() {
    return this.formatDate(new Date(), 'hh:mm')
  },
  // num天前，比如 7天前，传7
  dayAddNum(num) {
    return moment().subtract(num, "day").format("YYYY-MM-DD");
  },
  // 上个月第一天 如 2021-08-01
  lastMonthStartDay() {
    return moment().subtract(1, "month").startOf("month").format("YYYY-MM-DD");
  },
  // 上月最后一天 如 2021-08-31
  lastMonthEndDay() {
    return moment().subtract(1, "month").endOf("month").format("YYYY-MM-DD");
  },
  // 当前年 如2021
  currentYear() {
    return moment().format("YYYY");
  },
  getMonthDayNum(selectDate) {
    let myDate = new Date(selectDate.replace(/-/g, "/"));
    let year = myDate.getFullYear();
    let month = myDate.getMonth() + 1;
    myDate = new Date(year, month, 0);
    // let day = myDate.getDate()
    return myDate.getDate();
  },
  // 当前月 如 2021-09
  currentMonth(format = 'YYYY-MM') {
    return moment().startOf("month").format(format);
  },
  // 上个月 如 2021-08
  lastMonth() {
    return moment().subtract(1, "month").startOf("month").format("YYYY-MM");
  },
  // 上个月日期 如 2021-08-01
  lastMonthDate() {
    return moment().subtract(1, "month").startOf("month").format("YYYY-MM-DD");
  },
  // 将日期格式转换
  toDateTimeFormat(date, format = "YYYY-MM-DD HH:mm:ss") {
    if (!date) {
      return "";
    }
    let d = moment(date);
    if (d.unix() <= 0) {
      return "";
    }
    return moment(date).format(format);
  },
  // 格式化时间
  formatDate(date, fmt) {
    if (!date) {
      return ''
    }
    if (!fmt) {
      fmt = 'yyyy-MM-dd'
    }
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    let o = {
      'M+': date.getMonth() + 1,
      'd+': date.getDate(),
      'h+': date.getHours(),
      'm+': date.getMinutes(),
      's+': date.getSeconds()
    }
    for (let k in o) {
      if (new RegExp(`(${k})`).test(fmt)) {
        let str = o[k] + ''
        fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : padLeftZero(str))
      }
    }
    return fmt
  },
  // n年前
  beforeYear(num = 1) {
    return moment().add(num, 'year').startOf("year").format('YYYY-MM')
  },
  // n年之后的今天
  getAfterNYearsDateStr(n) {
    let day = new Date()
    day.setTime(day.getTime())
    let month = day.getMonth() + 1 < 10 ? '0' + (day.getMonth() + 1) : day.getMonth() + 1
    let dayTime = day.getDate() < 10 ? '0' + day.getDate() : day.getDate()
    return day.getFullYear() + n + '-' + month + '-' + dayTime
  },
};
