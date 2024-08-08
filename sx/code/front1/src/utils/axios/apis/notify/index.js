import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查询该学校下所有的通知信息
export function queryNotifyList (data) {
  return axiosUtil.axiosPost(url.notify + '/queryNotifyList', data)
}
// 查询通知详细信息
export function getNotifyDetail (data) {
    return axiosUtil.axiosPost(url.notify + '/getNotifyDetail', data)
  }