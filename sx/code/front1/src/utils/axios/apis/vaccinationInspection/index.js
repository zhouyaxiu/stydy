import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查验补种情况汇总表查询
export function searchInspectionQuery (data) {
  return axiosUtil.axiosPost(url.staticInfor + '/reseedList', data)
}
// 查验补种情况汇总表导出
export function exportInspection (data) {
  return axiosUtil.downloadFile(url.staticInfor + '/output', data)
}
// 查验补种需补种人数明细
export function reseedDetail (data) {
  return axiosUtil.axiosPost(url.staticInfor + '/reseedDetail', data)
}