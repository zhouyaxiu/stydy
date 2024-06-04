import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查询班级
export function queryClassMess (data) {
  return axiosUtil.axiosPost(url.classManager + '/queryClassMess', data)
}
// 导出查询班级信息
export function outClassMess (data) {
  return axiosUtil.downloadFile(url.classManager + '/outClassMess', data)
}
// 批量导入班级
export function inClassMesses (data) {
  return axiosUtil.axiosPostFile(url.classManager + '/inClassMesses', data)
}
// 新增班级
export function addClassMess (data) {
  return axiosUtil.axiosPost(url.classManager + '/addClassMess', data)
}
// 查询班级信息
export function findClassById (data) {
  return axiosUtil.axiosPost(url.classManager + '/findClassById', data)
}
// 更新班级信息
export function updateClassMess (data) {
  return axiosUtil.axiosPost(url.classManager + '/updateClassMess', data)
}
// 删除班级信息
export function deleteClassMess (data) {
  return axiosUtil.axiosPost(url.classManager + '/deleteClassMess', data)
}

