/**
 * 公共方法
 */
import router from '@/router'
import ElementUI from 'element-plus'
import CryptoJS from 'crypto-js'
// import store from '@/store/store.js'
import * as dateMath from 'date-arithmetic'
// 格式化时间时候用到
function padLeftZero(str) {
	return('00' + str).substr(str.length)
}

export default {
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
			offset: 100
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
	// 获取绝对位置x
	getAbsoluteLeft(o) {
		let oLeft = o.offsetLeft
		while(o.offsetParent != null) {
			let oParent = o.offsetParent
			oLeft += oParent.offsetLeft
			o = oParent
		}
		return oLeft
	},
	// 获取绝对位置y
	getAbsoluteTop(o) {
		let oTop = o.offsetTop
		while(o.offsetParent != null) {
			let oParent = o.offsetParent
			oTop += oParent.offsetTop
			o = oParent
		}
		return oTop
	},
	// 禁止浏览器返回键
	doStopReturn() {
		if(window.history && window.history.pushState) {
			window.addEventListener('popstate', function() {
				window.history.pushState(null, null, document.URL)
			})
		}
		window.history.pushState(null, null, document.URL)
	},
	// 格式化时间
	formatDate(date, fmt) {
		if(!date) {
			return ''
		}
		if(!fmt) {
			fmt = 'yyyy-MM-dd'
		}
		if(/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
		}
		let o = {
			'M+': date.getMonth() + 1,
			'd+': date.getDate(),
			'h+': date.getHours(),
			'm+': date.getMinutes(),
			's+': date.getSeconds()
		}
		for(let k in o) {
			if(new RegExp(`(${k})`).test(fmt)) {
				let str = o[k] + ''
				fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : padLeftZero(str))
			}
		}
		return fmt
	},
	//获取当前时间
	getCurrDateTime(fmt) {
		return this.formatDate(new Date(), fmt);
	},
	// 获取当前时间（不带时分秒）
	today() {
		return this.formatDate(new Date(), 'yyyy-MM-dd')
	},
	//当月
	getCurrentMonth() {
		return this.today().substr(0, 7);
	},
	//上个月
	getLastMonth() {
		return this.monthAdd(this.today(), -1).substr(0, 7);
	},
	//月份增加
	monthAdd(timestr, interval) {
		let date = new Date(timestr.replace(/-/g, '/'));
		return this.formatDate(dateMath.add(date, interval, "month"))
	},
	// 设置固定的最大最小时间
	disabledDate(max, min) {
		return(time) => {
			let timestr = this.formatDate(time, 'yyyy-MM-dd')
			if(max && timestr > max) {
				return true
			}
			if(min && timestr < min) {
				return true
			}
			return false
		}
	},
	//数字千位分隔
	numFormat(cellValue) {
		if(cellValue) {
			cellValue += "";
			if(!cellValue.includes(".")) cellValue += ".";
			return cellValue.replace(/(\d)(?=(\d{3})+\.)/g, ($0, $1) => {return $1 + ",";}).replace(/\.$/, "");
		}
		return '0';
	},
	// 根据地区编码得到级别
	getLevelByCode(codeStr){
		if(!codeStr || codeStr==''){
			return '0';
		}
		let level = '';
      	if (codeStr.substr(2, 12) === '0000000000') { // 省
	        level = '1'
      	} else if (codeStr.substr(4, 12) === '00000000') { // 市
	        level = '2'
      	} else if (codeStr.substr(6, 12) === '000000') { // 县
	        level = '3'
      	}
      	return level
	},
	checkIsWideFlag(){
		if(window.oldWidth==1440){
			return false;
		}
		//如果宽/高大于1.6则为16:9宽屏比例，否则为4:3方屏比例
		if(window.bl>1.55){
			return true;
		}
		return false;
	}
}