import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 学校管理新增
export function insertSchool (data) {
  return axiosUtil.axiosPost(url.school + '/addSchool', data)
}
// 学校管理详情
export function schoolDetail (data) {
  return axiosUtil.axiosPost(url.school + '/detail', data)
}
// 学校管理编辑
export function updateSchool (data) {
  return axiosUtil.axiosPost(url.school + '/modifySchool', data)
}
// 学校管理删除
export function deleteSchool (data) {
  return axiosUtil.axiosPost(url.school + '/deleteSchool', data)
}
// 学校管理授权/取消授权
export function authorizeSchool (data) {
  return axiosUtil.axiosPost(url.school + '/authorize', data)
}
// 学校查询
export function searchSchoolList (data) {
  return axiosUtil.axiosPost(url.school + '/schoolList', data)
}
// 学校导出
export function exportSchool (data) {
  return axiosUtil.downloadFile(url.school + '/output', data)
}
// 学校用户查询
export function schUserList (data) {
  return axiosUtil.axiosPost(url.school + '/schUserList', data)
}
// 新增学校用户
export function addSchUser (data) {
  return axiosUtil.axiosPost(url.school + '/addSchUser', data)
}
// 学校用户密码重置
export function resetSchUser (data) {
  return axiosUtil.axiosPost(url.school + '/resetSchUser', data)
}
// 同步学校数据
export function syncSchAndSchUser (data) {
  return axiosUtil.axiosPost(url.school + '/syncSchAndSchUser', data)
}
