import axiosUtil from './axios'
import url from './url'

/**
 * 登录
 */
export function getSessionid () {
  return axiosUtil.axioSessionId(url.getSessionId)
}
export function login (data) {
  return axiosUtil.axiosLogin(url.login, data)
}
export function editPass (data) {
  return axiosUtil.axiosPost(url.editPass, data)
}
export function logout (data) {
  return axiosUtil.axiosDelete(url.logout, data)
}
export function checkPwd (data) {
  return axiosUtil.axiosPut(url.checkPwd, data)
}
export function checkLogout () {
  return axiosUtil.axiosGet(url.checkLogout)
}
// 集成平台登录
export function JiChengloginIn (data) {
  return axiosUtil.axiosLogin(url.integratedPlatform + '/JiChengloginIn', data)
}
// 集成平台退出登录
export function JiChengLogout (data) {
  return axiosUtil.axiosJCLogout(url.logoutJC + '/toHome2', data)
}
// 通用--查询地区
export function queryArea (data) {
  return axiosUtil.axiosPost(url.commURI + '/queryArea', data)
}
// 通用--查询地区
export function queryAreaNew (data) {
  return axiosUtil.axiosPostJC(url.commURI + '/queryArea', data)
}
// 通用--查询所有学年
export function findAllClaYears (data) {
  return axiosUtil.axiosPost(url.commURI + '/findAllClaYears', data)
}
// 通用--查询所有学校
export function queryAllSchoolByAreaCode (data) {
  return axiosUtil.axiosPost(url.commURI + '/queryAllSchoolByAreaCode', data)
}
// 通用--根据学校类型查询学校
export function findClassBySchType (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassBySchType', data)
}
// 通用--查询学校下的学年
export function findClaYearBySchId (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClaYearBySchId', data)
}
// 通用--查询学年下的年级
export function findClaLevelBySchId (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClaLevelBySchId', data)
}
// 通用--查询学年下的年级
export function findClaLevelBySchIdAndSeq (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClaLevelBySchIdAndSeq', data)
}
// 通用--查询年纪下的班级
export function findClassBySchId (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassBySchId', data)
}
// 通用--查询年纪下的班级
export function findClassBySchIdAndSeq (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassBySchIdAndSeq', data)
}
// 通用--下拉框数值统一查询
export function getOptions (data) {
  return axiosUtil.axiosGet(url.commURI + '/options', data)
}
// 通用--获取全部疫苗生产企业
export function findYiMiaodEnterp (data) {
  return axiosUtil.axiosGet(url.childrendataTask + '/findYiMiaodEnterp', data)
}
// 通用--查询人群分类
export function queryCrowdKind () {
  return axiosUtil.axiosGet(url.commURI + '/queryCrowdKind')
}
/** ***************************************通用--显示设置*****************************************/
// 通用--获取疫苗剂次
export function findYiMiaoJiCi (data) {
  return axiosUtil.axiosGet(url.findyimiaojici, data)
}
// 通用--获取页面按钮权限
export function getPageAuthorizedBtn (data) {
  return axiosUtil.axiosPost(url.role + '/roleModel', data)
}
// 通用--获取配置列表
export function optionList (data) {
  return axiosUtil.axiosGet(url.optionList, data)
}
// 通用--获取户籍信息2
export function regTypeQuery2 (data) {
  return axiosUtil.axiosGet(url.commURI + '/query', data)
}
// 通用--户籍信息转换
export function regTypeTransform2 (data) {
  return axiosUtil.axiosGet(url.commURI + '/transform', data)
}
// 集成平台用户角色列表
export function rxrtShowSysUserUpdate (data) {
  return axiosUtil.axiosPostJC(url.integratedPlatform + '/rxrtShowSysUserUpdate', data)
}
// 集成平台用户角色保存
export function updateRxrtUser (data) {
  return axiosUtil.axiosPostJC(url.integratedPlatform + '/updateRxrtUser', data)
}
// 根据地区学校类型查询学校信息
export function findSchoolByAreaAndSchType (data) {
  return axiosUtil.axiosPost(url.commURI + '/findSchoolByAreaAndSchType', data)
}
// 通用--通过学校id和年级学年轮次查询班级
export function findClassMess (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassMess', data)
}
// 通用--通过学校id和年级学年年级查询班级
export function findHighClassMess (data) {
  return axiosUtil.axiosPost(url.commURI + '/findHighClassMess', data)
}
//  通用--获取符合条件的所有的轮次的名称
export function findRoundName (data) {
  return axiosUtil.axiosPost(url.round + '/findRoundName', data)
}
//  通用--获取符合条件的所有的轮次的名称
export function findRounds (data) {
  return axiosUtil.axiosPost(url.commURI + '/findRounds', data)
}
//  通用--获取符合要求的所有的年份
export function findRoundYear (data) {
  return axiosUtil.axiosPost(url.round + '/findRoundYear', data)
}
//  通用--查找高年级学年
export function findHighClassYear (data) {
  return axiosUtil.axiosPost(url.commURI + '/findHighClassYear', data)
}
