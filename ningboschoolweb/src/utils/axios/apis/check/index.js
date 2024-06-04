import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'


// 查验结果查询
export function getCheckResultList (data) {
  return axiosUtil.axiosPost(url.checkResult + '/getCheckResultList', data)
}
// 导出列，非导出列，全部导出列查询接口
export function fingExportColumnBySchId (data) {
  return axiosUtil.axiosPost(url.commURI + '/fingExportColumnBySchId', data)
}

// 导出列保存
export function saveExportColumnBySchId (data) {
  return axiosUtil.axiosPost(url.commURI + '/saveExportColumnBySchId', data)
}
// 打印补种通知单
export function printOut (data) {
  return axiosUtil.downloadFile(url.checkResult + '/printOut', data)
}
// 导出接口
export function outCheckResultList (data) {
  return axiosUtil.downloadFile(url.checkResult + '/outCheckResultList', data)
}
// 受种者查验列表查询
export function inspectionList (data) {
  return axiosUtil.axiosPost(url.studentInspection + '/inspectionList', data)
}
// 受种者查验列表学生详情查询
export function getOneStudent (data) {
  return axiosUtil.axiosPost(url.studentInspection + '/getOneStudent', data)
}
// 受种者查验列表学生修改
export function updateOneStudent (data) {
  return axiosUtil.axiosPost(url.studentInspection + '/updateOneStudent', data)
}
// 受种者查验列表学生删除
export function deleteStudent (data) {
  return axiosUtil.axiosPost(url.studentInspection + '/deleteStudent', data)
}
// 受种者查验导出
export function output (data) {
  return axiosUtil.downloadFile(url.studentInspection + '/output', data)
}