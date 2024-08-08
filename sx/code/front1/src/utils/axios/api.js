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
// 通用--查询所有地区
export function queryAllArea (data) {
  return axiosUtil.axiosPost(url.commURI + '/queryAllArea', data)
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
export function findClaLevelBySchIdAndLunci (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClaLevelBySchIdAndLunci', data)
}
// 通用--查询年纪下的班级
export function findClassBySchId (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassBySchIdLevelYear', data)
}
// 通用--通过学校id和轮次学年班级查询班级
export function findClassBySchIdLevelYearAndSeq (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassBySchIdLevelYearAndSeq', data)
}
// 通用--通过学校id和年级学年轮次查询班级
export function findClassMess (data) {
  return axiosUtil.axiosPost(url.commURI + '/findClassMess', data)
}
// 通用--下拉框数值统一查询
export function getOptions (data) {
  return axiosUtil.axiosGet(url.commURI + '/options', data)
}
// 通用--获取户籍信息
export function regTypeQuery (data) {
  return axiosUtil.axiosGet(url.commURI + '/query', data)
}
// 通用--户籍信息转换
export function regTypeTransform (data) {
  return axiosUtil.axiosGet(url.commURI + '/transform', data)
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
// 通用--查询所有学年
export function findAllClaYears (data) {
  return axiosUtil.axiosPost(url.commURI + '/findAllClaYears', data)
}
