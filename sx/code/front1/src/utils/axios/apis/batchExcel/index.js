import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 导入接口
export function batchExcelImport(data) {
  return axiosUtil.axiosPostFile(url.batchExcel + '/excelImport', data)
}
// 导入模板下载
export function downLoadImportMould(data) {
  return axiosUtil.downloadFile(url.batchExcel + '/downLoadImportMould', data)
}
//  失败查询接口
export function errorList(data) {
  return axiosUtil.axiosPost(url.batchExcel + '/errorList', data)
}