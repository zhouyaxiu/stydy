const BASE_URL = window.BASE_URL;

export default {
	checkLogout: BASE_URL + '/common/checkLogout',
	//获取sessionid
	getSessionId: BASE_URL + '/user/getSessionId',
	//登录
	login: BASE_URL + '/user/login',
	//修改密码
	editPass: BASE_URL + '/user/editPass',
	//登出
	logout: BASE_URL + '/user/logout',
	//校验输入的密码是否正确
	checkPwd: BASE_URL + '/user/checkPwd',

	commURI: BASE_URL + '/common',
	//接种单位相关接口
	vaccinationUnit: BASE_URL + '/vaccinationUnit',
	//人员构成相关的接口
	unitPersonnel: BASE_URL + '/UnitPersonnel',
	//接种疫苗相关接口
	vaccination: BASE_URL + '/Vaccination',
	//冷链设备相关接口
	device: BASE_URL + '/Device',
	//登录相关的接口
	systemURI: BASE_URL + '/system',
	user: BASE_URL + '/user',
}