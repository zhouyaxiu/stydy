// 补种汇总表
import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

//  列表查询
export function reseedQueryList(data) {
  return axiosUtil.axiosPost(url.reseedSummar + '/queryListForOne', data)
}
// 导出Excel
export function reseedOutput(data) {
  return axiosUtil.downloadFile(url.reseedSummar + '/outputForOne', data)
}
