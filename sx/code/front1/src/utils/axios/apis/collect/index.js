import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 校医录入保存
export function doctorEntry (data) {
  return axiosUtil.axiosPost(url.collect + '/doctorEntry', data)
}
// 批量扫码
export function batchScan (data) {
  return axiosUtil.axiosPost(url.collect + '/batchScan', data)
}
// 模板下载
export function dowmLoadInfoCollectMould (data) {
  return axiosUtil.downloadFile(url.collect + '/dowmLoadInfoCollectMould', data)
}
// 受种者信息介质识别码
export function savePlStu (data) {
  return axiosUtil.axiosPost(url.collect + '/savePlStu', data)
}
// 受种者预防接种档案编号
export function savePlStuByNo (data) {
  return axiosUtil.axiosPost(url.collect + '/savePlStuByNo', data)
}
// 转班转学校
export function oKSave (data) {
  return axiosUtil.axiosPost(url.collect + '/oKSave', data)
}
// Excel导入
export function excelImport (data) {
  return axiosUtil.axiosPostFile(url.collect + '/excelImport', data)
}
// 批量导入
export function batchExcelImport (data) {
  return axiosUtil.axiosPostFile(url.collect + '/batchExcelImport', data)
}
// 信息补种
export function revaccination (data) {
  return axiosUtil.axiosPostFile(url.collect + '/revaccination', data)
}
