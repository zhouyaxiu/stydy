// import solarLunar from 'solarLunar'

// 格式化时间时候用到
function padLeftZero (str) {
  return ('00' + str).substr(str.length)
}

export default {
  //公历转农历
  solarToLunar(timestr){
    let arr = this.dateStrToArr(timestr);
    return this.lunarToStr(solarLunar.solar2lunar(arr[0],arr[1],arr[2]));
  },
  //农历转公历
  lunarToSolar(timestr){
    let arr = this.dateStrToArr(timestr);
    return this.solarToStr(solarLunar.lunar2solar(arr[0],arr[1],arr[2]))
  },
	 sort(arr){
    for(var j=0;j<arr.length-1;j++){
    //两两比较，如果前一个比后一个大，则交换位置。
       for(var i=0;i<arr.length-1-j;i++){
            if(arr[i]>arr[i+1]){
                var temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1] = temp;
            }
        } 
    }
		return  arr;
},
	
  // 获取当前时间（不带时分秒）
  today () {
    return this.dateFormat(new Date(), 'yyyy-MM-dd')
  },
  // 格式化时间
  dateFormat (date, fmt) {
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
  //获取时间字符串
  getDateStr(year,month,date){
    return this.dateFormat(new Date(year,month,date))
  },
  //日期增加天数
  dayAdd(timestr,day){
    if(!timestr || !day) {
      return timestr;
    }
    let date = new Date(timestr.replace(/-/g,"\/"));
    let timestamp = date.getTime()+day*24*60*60*1000;
    return this.dateFormat(new Date(timestamp))
  },
  //月份增加
  monthAdd(timestr,interval){
    let arr = this.dateStrToArr(timestr);
    arr[1] = arr[1] - 1 + interval;
    let newDate = new Date(arr[0], arr[1], arr[2]);
    if(newDate.getDate() === arr[2]){
      return this.dateFormat(newDate)
    }
    return null;
  },
	
		 getDiffDate(datestr1,datestr2){
      let date1 = new Date(datestr1.replace(/-/g,"\/"));
      let date2 = new Date(datestr2.replace(/-/g,"\/"));
      return (date2.getTime() - date1.getTime()) / (24*60*60*1000);
    },
	 //月份增加 (每个月的1号)
	monthAdd_oneday(timestr,interval,day){
	  let arr = this.dateStrToArr(timestr);
	  arr[1] = arr[1] - 1 + interval;
	  let newDate = new Date(arr[0], arr[1], day);
	  if(newDate.getDate() === arr[2]){
	    return this.dateFormat(newDate)
	  }
	  return null;
	},
	
  //年份增加
  yearAdd(timestr,interval){
    let arr = this.dateStrToArr(timestr);
    arr[0] = arr[0] + interval;
    arr[1] -= 1;
    let newDate = new Date(arr[0], arr[1], arr[2]);
    if(newDate.getDate() === arr[2]){
      return this.dateFormat(newDate)
    }
    return null;
  },
  //下个同样日期的月份
  nextDayOfMonth(timestr){
    let arr = this.dateStrToArr(timestr);
    let newDate = new Date(arr[0], arr[1], arr[2]);
    if(newDate.getDate() === arr[2]){
      return this.dateFormat(newDate)
    }
    return null;
  },
  //下个同样日期的年份
  nextDayOfYear(timestr){
    let arr = this.dateStrToArr(timestr);
    arr[0] += 1;
    arr[1] -= 1;
    let newDate = new Date(arr[0], arr[1], arr[2]);
    if(newDate.getDate() === arr[2]){
      return this.dateFormat(newDate)
    }
    return null;
  },
  //获取日期是周几
  getDayOfWeek(timestr){
    return new Date(timestr.replace(/-/g,"\/")).getDay();
  },
  //当前月份最大天数
  getMaxDateOfMonth(timestr){
    let arr = this.dateStrToArr(timestr);
    let newDate = new Date(arr[0], arr[1] + 1, 1);
    let timestamp = newDate.getTime() - 24*60*60*1000;
    return new Date(timestamp).getDate();
  },
  //获取当前年份
  getFullYear(timestr){
    return new Date(timestr.replace(/-/g,"\/")).getFullYear();
  },
  //获取当前月份
  getMonth(timestr){
    return new Date(timestr.replace(/-/g,"\/")).getMonth() ;
  },
  //获取当前天数
  getDate(timestr){
    return new Date(timestr.replace(/-/g,"\/")).getDate();
  },
  dateStrToArr (datestr) {
    if(!datestr){
      return [];
    }
    let arr = datestr.split("-");
    for (let i=0;i<arr.length;i++){
      arr[i] = parseInt(arr[i]);
    }
    return arr;
  },
  arrToDateStr (arr) {
    if(!arr || arr.length != 3){
      return;
    }
    let str = arr[0] + '-';
    if(arr[1] < 10){
      str += '0' + arr[1] + '-'
    }else {
      str += arr[1] + '-'
    }
    if(arr[2] < 10){
      str += '0' + arr[2]
    }else {
      str += arr[2]
    }
    return str;
  },
  solarToStr(sl){
    return this.arrToDateStr([
      sl.cYear,
      sl.cMonth,
      sl.cDay
    ])
  },
  lunarToStr(sl){
    return this.arrToDateStr([
      sl.lYear,
      sl.lMonth,
      sl.lDay
    ])
  },
}