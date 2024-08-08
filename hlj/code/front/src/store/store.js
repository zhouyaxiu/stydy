/**
 * 模块
 */
import { defineStore } from "pinia";
export const mainStore = defineStore("main", {
	state: () => {
		return {
			token: null, // 登录token
			userName: '', // 用户昵称
			userCode: '', // 用户编码
			account: '', // 用户名
			errorMsg: '', // 错误提示语
			title: null, // 当前页面标题
			pagesize: 20, // 分页每页数量
			modules: [], // 目录
			routers: [], // 权限
			loadingAxiosFlag: false,
			loginType: null,
			currAreaData: {},//当前单击的地图
			loginAreaData: {},//登录进来时的地图
			textColor:'#B0DFFF',
			textStyleColor:"#7cc5f7",
			demoDataFlag:process.env.VITE_DEMODATAFLAG
		}
	},
	actions: {
		LOGIN(data){
			if(data) {
				sessionStorage.token = data.token
				this.token = data.token
			}
		},
		LOGOUT(){
			sessionStorage.removeItem('token')
			sessionStorage.removeItem('userName')
			this.token = null
			this.account = null
		},
		SET_ERROR(data){
			this.errorMsg = data
		},
		MAP_DATA(data){
			sessionStorage.currAreaData = JSON.stringify(data)
			this.currAreaData = data
		},
		SET_LOADING_AXIOS(data) {
	    	this.loadingAxiosFlag = data;
		},
		SET_LOGINTYPE(data){
			sessionStorage.loginType = data
			this.loginType = data
		},
		SET_LOGINAREADATA(data){
			sessionStorage.loginAreaData = JSON.stringify(data)
			this.loginAreaData = data
		},
	},
})