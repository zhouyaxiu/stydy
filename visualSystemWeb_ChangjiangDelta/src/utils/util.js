/**
 * 公共方法
 */

// 格式化日期中的 mon、day等 小于9的数值
function formatStr(str) {
    return str > 9 ? (str + "") : ("0" + str);
}
export default {
    // 当前日期+时间
    getCurDateTimeStr() {
        let date = new Date();
        let year = date.getFullYear();
        let mon = formatStr(date.getMonth() + 1);
        let dates = formatStr(date.getDate());
        let hour = formatStr(date.getHours());
        let min = formatStr(date.getMinutes());
        let sec = formatStr(date.getSeconds());
        let day = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")[date.getDay()];
        let obj = { date: `${year}-${mon}-${dates}  ${day}`, time: `${hour}:${min}` };
        return obj;
    },
    // 月份 
    getMonthStr(time) {
        let date = new Date();
        if (time) {
            //date = new Date(time);
            date = time;
        }
        let year = date.getFullYear();
        let mon = formatStr(date.getMonth() + 1);
        let obj = `${year}-${mon}`;
        return obj;
    },
    // 特殊月份  (如：常规免疫合格率，按出生日期来查  进来默认选择的日期是2006-12到2016-12（6到16岁）)
    getSpecMonthStr(yearNum) {
        let date = new Date();
        let year = date.getFullYear() - yearNum;
        let mon = formatStr(date.getMonth() + 1);
        let obj = `${year}-${mon}`;
        return obj;
    },
    // 日期 
    getDateStr(time) {
        let date = new Date(time);
        let year = date.getFullYear();
        let mon = formatStr(date.getMonth() + 1);
        let dates = formatStr(date.getDate());
        let obj = `${year}-${mon}-${dates}`;
        return obj;
    },
    // 图片
    getImageUrl(imgName) {
        return new URL(`../assets/img/${imgName}`, import.meta.url).href
    },
    //验证非0开头的无限位整数和小数。整数支持无限位，小数点前支持无限位，小数点后最多保留两位(更改位数修改{1,2})。
    //此正则无法校验第一位就是 . 的数字
    checkNum(num) {
        let reg = /^(([^0][0-9]+|0)\.([0-9]{1,2})$)|^(([^0][0-9]+|0)$)|^(([1-9]+)\.([0-9]{1,2})$)|^(([1-9]+)$)/;
        if (num.indexOf(".") != 0) {
            if (reg.test(num)) {
                return true;
            } else {
                return false;
            }
        }
    },
	// 格式化数字，3位以逗号分隔
	formatNum(strNum) {
	  if (strNum.length <= 3) {
	    return strNum;
	  }
	  if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
	    return strNum;
	  }
	  let a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
	  let re = new RegExp();
	  re.compile("(\\d)(\\d{3})(,|$)");
	  while (re.test(b)) {
	    b = b.replace(re, "$1,$2$3");
	  }
	  return a + "" + b + "" + c;
	},
	//判断两个日期是否超过1年
	compareDateToOneYear(starDate, endDate) {
		var begin = new Date(starDate).getTime();
		var end = new Date(endDate).getTime();
		var day = (end - begin)/(24*60*60*1000);
		if (day > 365) {
			return true;
		}else{
			return false;
		}
	}
}
