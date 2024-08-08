/**
 * 公共方法
 */
import router from '../../router'
import ElementUI from 'element-ui'
import CryptoJS from 'crypto-js'
import store from '../../store/store'
import * as dateMath from 'date-arithmetic'
import axios from 'axios'
import FileSaver from 'file-saver'

// 格式化时间时候用到
function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}

export default {
  // SM4加密
  sm4(word) {
    let sm4 = require('sm-crypto').sm4
    let keyStr = this.stringToHex(store.state.aesKey) // 16进制串32位
    let encrypted = sm4.encrypt(word, keyStr)
    return encrypted.toString()
  },
  // sm4解密
  decodeAes(word) {
    if (!word) {
      return ''
    }
    let keyStr = this.stringToHex(store.state.aesKey) // 16进制串32位
    let sm4 = require('sm-crypto').sm4
    let decryptedStr = sm4.decrypt(word, keyStr)
    return decryptedStr.toString().replace(/\u0000/g, '')
  },
  // 字符转16进制串32位
  stringToHex(str) {
    var val = ''
    for (var i = 0; i < str.length; i++) {
      if (val === '') {
        val = str.charCodeAt(i).toString(16)
      } else {
        val += str.charCodeAt(i).toString(16)
      }
    }
    return val
  },
  // str=>num
  getNum(num) {
    if (typeof num === 'number') {
      return num
    }
    return parseFloat(num + '')
  },
  encodeAes(word) {
    if (!word) {
      return ''
    }
    let srcs = CryptoJS.enc.Utf8.parse(word)
    let encrypted = CryptoJS.AES.encrypt(srcs, CryptoJS.enc.Utf8.parse(store.state.aesKey2), {
      mode: CryptoJS.mode.ECB,
      padding: CryptoJS.pad.Pkcs7
    })
    return encrypted.ciphertext.toString() // 还原;
  },
  // 弹出普通提示
  infoMsg(msg) {
    ElementUI.Message({
      message: msg,
      type: 'info',
      center: true,
      offset: 100
    })
  },
  // 弹出错误提示
  errorMsg(msg) {
    ElementUI.Message({
      message: msg,
      type: 'error',
      center: true,
      offset: 100,
      duration: 3000,
      showClose: true
    })
  },
  // 弹出成功提示
  successMsg(msg) {
    ElementUI.Message({
      message: msg,
      type: 'success',
      center: true,
      offset: 100,
      duration: 1000
    })
  },
  // 获取当前时间（不带时分秒）
  today() {
    return this.formatDate(new Date(), 'yyyy-MM-dd')
  },
  todayFormat() {
    return this.formatDate(new Date(), 'yyyy年MM月dd日')
  },
  todayFormatHourMin() {
    return this.formatDate(new Date(), 'yyyy年MM月dd日 hh:mm:ss')
  },
  // 获取当前时间（时分）
  todayMinutes() {
    return this.formatDate(new Date(), 'hh:mm')
  },
  todayIsHourMinutes() {
    return this.formatDate(new Date(), 'yyyy-MM-dd hh:mm')
  },
  todayCurrTime() {
    return this.formatDate(new Date(), 'yyyy-MM-dd hh:mm:ss')
  },
  // xx日期的前一天,默认为昨天
  getYesterdayDateStr(n) {
    let day = n ? new Date(n) : new Date()
    day.setDate(day.getDate() - 1)
    return this.formatDate(day, 'yyyy-MM-dd hh:mm:ss')
  },
  // n年之后的今天
  getAfterNYearsDateStr(n) {
    let day = new Date()
    day.setTime(day.getTime())
    let month = day.getMonth() + 1 < 10 ? '0' + (day.getMonth() + 1) : day.getMonth() + 1
    let dayTime = day.getDate() < 10 ? '0' + day.getDate() : day.getDate()
    return day.getFullYear() + n + '-' + month + '-' + dayTime
  },
  // n年之前
  getBeforeNYearsDateStr(n) {
    let day = new Date()
    day.setTime(day.getTime())
    return day.getFullYear() - n - 1 + '-12-31'
  },
  // n年之前的前一天
  getBeforeTodayYearsDateStr(n) {
    let day = new Date()
    day.setTime(day.getTime())
    let month = day.getMonth() + 1 < 10 ? '0' + (day.getMonth() + 1) : day.getMonth() + 1
    let dayTime = day.getDate() < 10 ? '0' + day.getDate() : day.getDate()
    return day.getFullYear() - n + '-' + month + '-' + dayTime
  },

  // n年之前的当月
  getBeforeTodayYearsMonthStr(n) {
    let day = new Date()
    day.setTime(day.getTime())
    let month = day.getMonth() + 1 < 10 ? '0' + (day.getMonth() + 1) : day.getMonth() + 1
    return day.getFullYear() - n + '-' + month
  },
  // 月份增加
  monthAdd(timestr, interval) {
    let date = new Date(timestr.replace(/-/g, '/'))
    return this.formatDate(dateMath.add(date, interval, 'month'))
  },
  // 天增加
  dayAdd(timestr, interval) {
    let date = new Date(timestr.replace(/-/g, '/'))
    return this.formatDate(dateMath.add(date, interval, 'day'))
  },
  dateStrToArr(datestr) {
    if (!datestr) {
      return []
    }
    let arr = datestr.split('-')
    for (let i = 0; i < arr.length; i++) {
      arr[i] = parseInt(arr[i])
    }
    return arr
  },
  getCurrentTime() {
    let date = new Date()
    let seperator1 = '-'
    let seperator2 = ':'
    let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
    let strDate = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
    let currentDate = date.getFullYear() + seperator1 + month + seperator1 + strDate + ' ' +
      date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds()
    return currentDate
  },
  getDatestr() {
    let day = new Date()
    day.setTime(day.getTime())
    let month = day.getMonth() + 1 < 10 ? '0' + (day.getMonth() + 1) : day.getMonth() + 1
    let dayTime = day.getDate() < 10 ? '0' + day.getDate() : day.getDate()
    return day.getFullYear() + '-' + month + '-' + dayTime
  },
  getDatestrM(timestamp) {
    return this.formatDate(new Date(timestamp), 'yyyy-MM-dd hh:mm')
  },
  getDatestrS(timestamp, format) {
    return this.formatDate(new Date(timestamp), format)
  },
  // 1月之前
  getBeforeOneMonthsDateStr() {
    let n = 1
    let day = new Date()
    day.setTime(day.getTime())
    let year = day.getFullYear()
    let month = '01'
    let dayTime = day.getDate() < 10 ? '0' + day.getDate() : day.getDate()
    if (day.getMonth() + 1 - n < 1) {
      month = 12
      year = year - 1
    } else {
      month = day.getMonth() + 1 - n < 10 ? '0' + (day.getMonth() + 1 - n) : day.getMonth() + 1 - n
    }
    return year + '-' + month + '-' + dayTime
  },
  // 获取时间字符串
  getDateArr(date) {
    if (typeof date === 'string') {
      date = this.parseDate(date)
    }
    return [date.getFullYear(), date.getMonth() + 1, date.getDate()]
  },
  // 当年
  getCurrentYear() {
    let day = new Date()
    day.setTime(day.getTime())
    return day.getFullYear()
  },
  // 当月
  getCurrentMonth() {
    return this.today().substr(0, 7)
  },
  // 上个月
  getLastMonth() {
    return this.monthAdd(this.today(), -1).substr(0, 7)
  },
  getNextMonth() {
    return this.monthAdd(this.today(), 1).substr(0, 7)
  },
  // 字符串转时间
  parseDate(datestr) {
    if (!datestr) {
      return new Date()
    }
    return new Date(datestr.replace(/-/g, '/'))
  },
  // 设置固定的最大最小时间
  disabledDate(max, min) {
    return (time) => {
      let timestr = this.formatDate(time, 'yyyy-MM-dd')
      if (max && timestr > max) {
        return true
      }
      if (min && timestr < min) {
        return true
      }
      return false
    }
  },
  // 设置固定的最大最小时间,不包含今天
  disabledDateNoToday(max, min) {
    return (time) => {
      let timestr = this.formatDate(time, 'yyyy-MM-dd')
      if (max && timestr >= max) {
        return true
      }
      if (min && timestr < min) {
        return true
      }
      return false
    }
  },
  // 设置月份控件固定的最大最小时间
  disabledMonthDate(max, min) {
    return (time) => {
      let timestr = this.formatDate(time, 'yyyy-MM')
      if (max && timestr > max) {
        return true
      }
      if (min && timestr < min) {
        return true
      }
      return false
    }
  },
  // 设置年份控件固定的最大最小时间
  disabledYearDate(max, min) {
    return (time) => {
      let timestr = this.formatDate(time, 'yyyy')
      if (max && timestr > max) {
        return true
      }
      if (min && timestr < min) {
        return true
      }
      return false
    }
  },
  // 后退一页
  goBack() {
    router.go(-1)
  },
  // 获取绝对位置x
  getAbsoluteLeft(o) {
    let oLeft = o.offsetLeft
    while (o.offsetParent != null) {
      let oParent = o.offsetParent
      oLeft += oParent.offsetLeft
      o = oParent
    }
    return oLeft
  },
  // 获取绝对位置y
  getAbsoluteTop(o) {
    let oTop = o.offsetTop
    while (o.offsetParent != null) {
      let oParent = o.offsetParent
      oTop += oParent.offsetTop
      o = oParent
    }
    return oTop
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
  // 禁止浏览器返回键
  doStopReturn() {
    if (window.history && window.history.pushState) {
      window.addEventListener('popstate', function () {
        window.history.pushState(null, null, document.URL)
      })
    }
    window.history.pushState(null, null, document.URL)
  },
  generateHourList(start, val) {
    let list = []
    let maxNum = 23
    if (val === 'current') {
      maxNum = new Date().getHours()
    }
    for (let i = start; i <= maxNum; i++) {
      if (i < 10 && i.toString().length === 1) {
        list.push({
          value: '0' + i,
          label: '0' + i
        })
      } else {
        list.push({
          value: '' + i,
          label: '' + i
        })
      }
    }
    return list
  },
  // 系统日志查询中使用,当前时间加一小时
  generateNextHourList(start, val) {
    let list = []
    let maxNum = 23
    if (val === 'current') {
      maxNum = new Date().getHours() + 1
    }
    for (let i = start; i <= maxNum; i++) {
      if (i < 10 && i.toString().length === 1) {
        list.push({
          value: '0' + i,
          label: '0' + i
        })
      } else {
        list.push({
          value: '' + i,
          label: '' + i
        })
      }
    }
    return list
  },
  generateMinList(start, val) {
    let list = []
    let maxNum = 59
    if (val === 'current') {
      maxNum = new Date().getMinutes()
    }
    for (let i = start; i <= maxNum; i++) {
      if (i < 10 && i.toString().length === 1) {
        list.push({
          value: '0' + i,
          label: '0' + i
        })
      } else {
        list.push({
          value: '' + i,
          label: '' + i
        })
      }
    }
    return list
  },
  // 下载模板
  mubanDown(url, fileName = '') {
    axios.get(url, {
      responseType: 'blob'
    })
      .then(res => {
        let blob = new Blob([res.data], {
          type: 'applicationnd.ms-excel'
        })
        FileSaver.saveAs(blob, fileName)
      })
  },
  // 根据学校类型获取年级
  getGrade(type) {
    let arr = []
    if (type == 0) {
      arr.push(
        {
          code: '0',
          name: '幼儿园托班'
        },
        {
          code: '1',
          name: '幼儿园小班'
        },
        {
          code: '2',
          name: '幼儿园中班'
        },
        {
          code: '3',
          name: '幼儿园大班'
        }
      )
    }
    else if (type == 1) {
      arr.push(
        {
          code: '4',
          name: '一年级'
        },
        {
          code: '5',
          name: '二年级'
        },
        {
          code: '6',
          name: '三年级'
        },
        {
          code: '7',
          name: '四年级'
        },
        {
          code: '8',
          name: '五年级'
        },
        {
          code: '9',
          name: '六年级'
        },
      )
    } else {
      arr.push(
        {
          code: '0',
          name: '幼儿园托班'
        },
        {
          code: '1',
          name: '幼儿园小班'
        },
        {
          code: '2',
          name: '幼儿园中班'
        },
        {
          code: '3',
          name: '幼儿园大班'
        },
        {
          code: '4',
          name: '一年级'
        },
        {
          code: '5',
          name: '二年级'
        },
        {
          code: '6',
          name: '三年级'
        },
        {
          code: '7',
          name: '四年级'
        },
        {
          code: '8',
          name: '五年级'
        },
        {
          code: '9',
          name: '六年级'
        },
      )
    }
    return arr;
  }
}
