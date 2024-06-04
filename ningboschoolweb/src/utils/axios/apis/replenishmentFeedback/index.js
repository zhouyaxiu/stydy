import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 查询补种情况反馈信息
export function queryReplantMess (data) {
  return axiosUtil.axiosPost(url.inforInspect + '/queryReplantMess', data)
}

// 查询补种情况反馈表导出
export function outPutReplantMess (data) {
  return axiosUtil.downloadFile(url.inforInspect + '/outPutReplantMess', data)
}
