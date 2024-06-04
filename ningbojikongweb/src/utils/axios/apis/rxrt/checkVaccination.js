import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 单个、批量查验
export function check(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/check', data)
}
// 单个、批量删除
export function deletes(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/delete', data)
}
// 单个信息查询
export function query(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/query', data)
}
// 查验接种信息查询
export function queryList(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryList', data)
}
// 批量编辑保存
export function save4MultipleEdit(data) {
  return axiosUtil.axiosPost(url.checkInfo + '/save4MultipleEdit', data)
}
// 查验接种信息导出
export function output(data) {
  return axiosUtil.downloadFile(url.checkInfo + '/output', data)
}
// 需补种名单列表
export function queryReplantRoster(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryReplantRoster', data)
}
// 需补种名单导出
export function outputReplantRoster(data) {
  return axiosUtil.downloadFile(url.checkInfo + '/outputReplantRoster', data)
}
// 补种疫苗统计表
export function queryReplantVacStat(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryReplantVacStat', data)
}
// 打印建档通知单
export function queryNoArchive(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryNoArchive', data)
}
// 打印查验证明
export function queryCheckCert(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryCheckCert', data)
}
// 打印补种通知单
export function queryReplantMess(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryReplantMess', data)
}
// 打印补证通知单
export function queryReplantCard(data) {
  return axiosUtil.axiosGet(url.checkInfo + '/queryReplantCard', data)
}