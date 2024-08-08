import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 接种证查验汇总表
export function queryList (data) {
  return axiosUtil.axiosPost(url.reseedSummar + '/queryList', data)
}
// 接种证查验汇总表导出
export function output (data) {
  return axiosUtil.downloadFile(url.reseedSummar + '/output', data)
}
