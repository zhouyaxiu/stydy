import axiosUtil from '@/utils/axios/axios'
import url from '@/utils/axios/url'

// 学生查询
export function searchStudentList (data) {
  return axiosUtil.axiosPost(url.student + '/studentList', data)
}
// 学生导出
export function exportStudent (data) {
  return axiosUtil.downloadFile(url.student + '/output', data)
}
