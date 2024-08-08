import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 行政区划管理 - 列表
export function queryAreaList (data) {
  return axiosUtil.axiosPost(url.areaURI + '/query', data)
}
//  导出
export function exportAreaList (data) {
  return axiosUtil.downloadFile(url.areaURI + '/outPut', data)
}
