import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查验信息查询
export function rxrtQuery(data) {
  return axiosUtil.axiosPost(url.rxrt + '/rxrtQuery', data)
}
//  查验接种重新查验学生信息
export function checkStudent(data) {
  return axiosUtil.axiosPost(url.rxrt + '/checkStudent', data)
}
//  导出
export function rxrtOutPut(data) {
  return axiosUtil.downloadFile(url.rxrt + '/rxrtOutPut', data)
}
//  查询所有学年
export function findAllClaYears(data) {
  return axiosUtil.axiosPost(url.commURI + '/findAllClaYears', data)
}
//  查询所有的指定地区下的学校
export function queryAllSchoolByAreaCode(data) {
  return axiosUtil.axiosPost(url.commURI + '/queryAllSchoolByAreaCode', data)
}
//  根据条码查询受中者信息
export function queryPlStu(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryPlStu', data)
}
//  根据个案编码查询受中者信息
export function queryPlStuNo(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryPlStuNo', data)
}
//  根据条码保存受种者信息
export function savePlStu(data) {
  return axiosUtil.axiosPost(url.scanCode + '/savePlStu', data)
}
//  根据个案编码保存受中者信息
export function savePlStuNo(data) {
  return axiosUtil.axiosPost(url.scanCode + '/savePlStuNo', data)
}
//  转校或者转班级轮次点击确定后的保存接口
export function okSave(data) {
  return axiosUtil.axiosPost(url.scanCode + '/okSave', data)
}
//  根据姓名性别出生日期操作查询学生信息
export function queryStuByThreeCondition(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryStuByThreeCondition', data)
}
//  根据姓名性别出生日期保存学生信息
export function saveStuByThreeCondition(data) {
  return axiosUtil.axiosPost(url.scanCode + '/saveStuByThreeCondition', data)
}
//  打印补证通知单(前)
export function queryReplantCard(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryReplantCard', data)
}
//  打印补种通知单（前）
export function queryReplantMess(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryReplantMess', data)
}
//  打印查验证明
export function queryReplantProve(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryReplantProve', data)
}
//  查询
export function queryByConditions(data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryByConditions', data)
}
//  转校或专班
export function saveAndChange(data) {
  return axiosUtil.axiosPost(url.scanCode + '/saveAndChange', data)
}
//  保存
export function saveByFchildno(data) {
  return axiosUtil.axiosPost(url.scanCode + '/saveByFchildno', data)
}