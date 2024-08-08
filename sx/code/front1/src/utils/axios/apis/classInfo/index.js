import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查询班级
export function queryClassMess (data) {
  return axiosUtil.axiosPost(url.nurClass + '/classList', data)
}
// 导出查询班级信息
export function outClassMess (data) {
  return axiosUtil.downloadFile(url.nurClass + '/output', data)
}
// 批量导入班级
export function inClassMesses (data) {
  return axiosUtil.axiosPostFile(url.nurClass + '/batchImportClass', data)
}
// 新增班级
export function addClassMess (data) {
  return axiosUtil.axiosPost(url.nurClass + '/insertClass', data)
}
// 查询班级信息
export function findClassById (data) {
  return axiosUtil.axiosPost(url.nurClass + '/classDetail', data)
}
// 更新班级信息
export function updateClassMess (data) {
  return axiosUtil.axiosPost(url.nurClass + '/updateClass', data)
}
// 删除班级信息
export function deleteClassMess (data) {
  return axiosUtil.axiosPost(url.nurClass + '/deleteClass', data)
}
// 下载模板
export function dowmLoadClassMould (data) {
  return axiosUtil.downloadFile(url.nurClass + '/dowmLoadClassMould', data)
}
