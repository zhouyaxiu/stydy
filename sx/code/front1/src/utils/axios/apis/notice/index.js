// 通知单
import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查询通知单内容
export function queryNoticeMess(data) {
  return axiosUtil.axiosPost(url.notice + '/queryNoticeMess', data)
}
//  更新通知单内容
export function upNoticeMess(data) {
  return axiosUtil.axiosPost(url.notice + '/upNoticeMess', data)
}