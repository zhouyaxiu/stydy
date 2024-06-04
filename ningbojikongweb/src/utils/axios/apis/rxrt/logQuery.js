import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 日志查询
export function queryLog (data) {
  return axiosUtil.axiosPost(url.log + '/queryLog', data)
}
