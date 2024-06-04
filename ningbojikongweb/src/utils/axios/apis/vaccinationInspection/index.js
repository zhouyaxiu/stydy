import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查验补种情况汇总表查询
export function searchInspectionQuery (data) {
  return axiosUtil.axiosPost(url.staticInspection + '/queryList', data)
}
// 查验补种情况汇总表导出
export function exportInspection (data) {
  return axiosUtil.downloadFile(url.staticInspection + '/output', data)
}
//  导出
export function reseedDetail (data) {
  return axiosUtil.axiosPost(url.staticInspection + '/reseedDetail', data)
}