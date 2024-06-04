import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查验信息查询
export function rxrtQuery (data) {
  return axiosUtil.axiosPost(url.rxrt + '/rxrtQuery', data)
}
//  查验接种重新查验学生信息
export function checkStudent (data) {
  return axiosUtil.axiosPost(url.rxrt + '/checkStudent', data)
}
//  导出
export function rxrtOutPut (data) {
  return axiosUtil.downloadFile(url.rxrt + '/rxrtOutPut', data)
}
//  查询所有学年
export function findAllClaYears (data) {
  return axiosUtil.axiosPost(url.commURI + '/findAllClaYears', data)
}
//  查询所有的指定地区下的学校
export function queryAllSchoolByAreaCode (data) {
  return axiosUtil.axiosPost(url.commURI + '/queryAllSchoolByAreaCode', data)
}
//  根据条码查询受中者信息
export function queryPlStu (data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryPlStu', data)
}
//  根据个案编码查询受中者信息
export function queryPlStuNo (data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryPlStuNo', data)
}
//  根据条码保存受种者信息
export function savePlStu (data) {
  return axiosUtil.axiosPost(url.scanCode + '/savePlStu', data)
}
//  根据个案编码保存受中者信息
export function savePlStuNo (data) {
  return axiosUtil.axiosPost(url.scanCode + '/savePlStuNo', data)
}
//  转校或者转班级轮次点击确定后的保存接口
export function okSave (data) {
  return axiosUtil.axiosPost(url.scanCode + '/okSave', data)
}
//  根据姓名性别出生日期操作查询学生信息
export function queryStuByThreeCondition (data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryStuByThreeCondition', data)
}
//  根据姓名性别出生日期保存学生信息
export function saveStuByThreeCondition (data) {
  return axiosUtil.axiosPost(url.scanCode + '/saveStuByThreeCondition', data)
}
//  打印补证通知单(前)
export function queryReplantCard (data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryReplantCard', data)
}
//  打印补种通知单（前）
export function queryReplantMess (data) {
  return axiosUtil.axiosPost(url.scanCode + '/queryReplantMess', data)
}
//  打印建档通知单（前）
export function queryNoArchive (data) {
  return axiosUtil.axiosGet(url.scanCode + '/queryNoArchive', data)
}
// 单个、批量查验
export function check(data) {
  return axiosUtil.axiosGet(url.roster + '/check', data)
}
// 单个、批量删除
export function deletes(data) {
  return axiosUtil.axiosGet(url.roster + '/delete', data)
}
// 单个班级花名册查询
export function query(data) {
  return axiosUtil.axiosGet(url.roster + '/query', data)
}
// 班级花名册查询
export function queryList(data) {
  return axiosUtil.axiosGet(url.roster + '/queryList', data)
}
// 批量编辑保存
export function save4MultipleEdit(data) {
  return axiosUtil.axiosPost(url.roster + '/save4MultipleEdit', data)
}
// 班级花名册导出
export function output(data) {
  return axiosUtil.downloadFile(url.roster + '/output', data)
}
// 需补种名单列表
export function queryReplantRoster(data) {
  return axiosUtil.axiosGet(url.roster + '/queryReplantRoster', data)
}
// 需补种名单导出
export function outputReplantRoster(data) {
  return axiosUtil.downloadFile(url.roster + '/outputReplantRoster', data)
}
//  打印补证通知单(查询)
export function rqueryReplantCard (data) {
  return axiosUtil.axiosGet(url.roster + '/queryReplantCard', data)
}
//  打印补种通知单（查询）
export function rqueryReplantMess (data) {
  return axiosUtil.axiosGet(url.roster + '/queryReplantMess', data)
}
//  打印建档通知单（查询）
export function rqueryNoArchive (data) {
  return axiosUtil.axiosGet(url.roster + '/queryNoArchive', data)
}