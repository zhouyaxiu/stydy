import url from "@/api/urls";
import axiosUtil from "@/api/axios";

// 档案变化情况统计
export const queryBaseChangedList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryBaseChangedList`, data);
};
// 建档及时性统计
export const queryBaseTimelyList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryBaseTimelyList`, data);
};
// 获取阀值
export const getThreshold = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/getThreshold`, data);
};
// 设置阀值
export const setThreshold = (data) => {
  return axiosUtil.axiosPost(`${url.analysisRpt}/setThreshold`, data);
};
// 国家数据上传监测统计表
export const queryUploadCheckList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryUploadCheckList`, data);
};
// 国家数据上传错误统计表
export const queryUploadWrongList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryUploadWrongList`, data);
};
// 国家数据上传错误统计详情
export const queryUploadWrongDetailList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryUploadWrongDetailList`, data);
};
// 国家数据上传错误统计详情导出
export const outputUploadWrongDetailList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/outputUploadWrongDetailList`, data);
};
// 国家数据不合格未上传统计表
export const queryUploadNoPackageList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryUploadNoPackageList`, data);
};
// 国家数据不合格未上传统计明细
export const queryUploadNoPackageDetailList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/queryUploadNoPackageDetailList`, data);
};
// 国家重档数据查询
export const querySameRecordList = (data) => {
  return axiosUtil.axiosGet(`${url.analysisRpt}/querySameRecordList`, data);
};
// 国家重档数据导出
export const outputSameRecordList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputSameRecordList`, data);
};

// 档案变化情况统计导出
export const outputBaseChangedList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputBaseChangedList`, data);
};
// 建档及时性统计导出
export const outputBaseTimelyList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputBaseTimelyList`, data);
};
// 国家数据上传监测统计表导出
export const outputUploadCheckList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputUploadCheckList`, data);
};
// 国家数据上传错误统计表导出
export const outputUploadWrongList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputUploadWrongList`, data);
};
// 国家数据不合格未上传统计表导出
export const outputUploadNoPackageList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputUploadNoPackageList`, data);
};
// 国家数据不合格未上传统计明细导出
export const outputUploadNoPackageDetailList = (data) => {
  return axiosUtil.downloadFile(`${url.analysisRpt}/outputUploadNoPackageDetailList`, data);
};


// 新冠病毒疫苗基本信息质量控制报表
export const searchXgRptInfoFullInfofull = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptInfo}/infofullList`, data);
};
export const showDetailXgRptInfoFullInfofull = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptInfo}/infoDetailList`, data);
};
export const exportXgRptInfoFullInfofull = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptInfo}/output`, data);
};
export const outputXgRptInfoFullInfofullDetail = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptInfo}/detailOutput`, data);
};

// 新冠病毒疫苗接种记录质量控制报表
export function searchXgRptInfoFullJzjlfull(data) {
  return axiosUtil.axiosPost(url.xgRptInfo + '/jzjlfullList', data)
}
export function showDetailXgRptInfoFullJzjlfull(data) {
  return axiosUtil.axiosPost(url.xgRptInfo + '/showJzjlDetail', data)
}
export function exportXgRptInfoFullJzjlfull(data) {
  return axiosUtil.downloadFile(url.xgRptInfo + '/jzjlOutput', data)
}
export function outputXgRptInfoFullJzjlfullDetail(data) {
  return axiosUtil.downloadFile(url.xgRptInfo + '/jzjlDetailOutput', data)
}
// 按月提供新冠疫苗数据 - 查询
export function getVacInfoMonthlyData(data) {
  return axiosUtil.axiosPost(url.xgRptMonth + '/jzjlList', data)
}
// 按月提供新冠疫苗数据 - 导出
export function exportVacInfoMonthlyData(data) {
  return axiosUtil.downloadFile(url.xgRptMonth + '/output', data)
}
// 新冠病毒疫苗分年龄、分企业接种信息汇总表查询接口
export function query(data) {
  return axiosUtil.axiosPost(url.xgAgeAndFenter + '/query', data)
}
// 新冠病毒疫苗分年龄、分企业接种信息汇总表 - 导出
export function output(data) {
  return axiosUtil.downloadFile(url.xgAgeAndFenter + '/output', data)
}
// 新冠病毒疫苗分年龄、分企业接种信息汇总表查询详情
export const queryXgDeatil = (data) => {
  return axiosUtil.axiosPost(`${url.xgAgeAndFenter}/queryXgDeatil`, data);
};
// 新冠病毒疫苗分年龄、分企业接种信息汇总表查询详情 - 导出
export const outputDetail = (data) => {
  return axiosUtil.downloadFile(`${url.xgAgeAndFenter}/outputDetail`, data);
};
// 按人群分类的新冠病毒疫苗接种信息汇总表查询
export const crowQuery = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptCrowdkind}/query`, data);
};
// 按人群分类的新冠病毒疫苗接种信息汇总表导出
export const crowOutput = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptCrowdkind}/output`, data);
};
// 按人群分类的新冠病毒疫苗接种信息汇总表详情
export const queryDetail = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptCrowdkind}/queryDetail`, data);
};
// 按人群分类的新冠病毒疫苗接种信息汇总表详情导出
export const crowOutputDetail = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptCrowdkind}/outputDetail`, data);
};

// 新冠疫苗加强第2针分年龄汇总表（vero）查询
export const veroList = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptAge}/veroList`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（vero）导出
export const outputVeros = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptAge}/outputVeros`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（智飞）查询
export const zfList = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptAge}/zfList`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（智飞）导出
export const outputZf = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptAge}/outputZf`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（丽珠、三叶）查询
export const lsList = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptAge}/lsList`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（丽珠、三叶）导出
export const outputLs = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptAge}/outputLs`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（康希诺）查询
export const kxnList = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptAge}/kxnList`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（康希诺）导出
export const outputKxn = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptAge}/outputKxn`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（威斯克、神州、万泰、石药）查询
export const wswList = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptAge}/wswList`, data);
};
// 新冠疫苗加强第2针分年龄汇总表（威斯克、神州、万泰、石药）导出
export const outputWsw = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptAge}/outputWsw`, data);
};
// 按职业分类的新冠病毒疫苗接种信息汇总表查询
export const proQuery = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptProfession}/query`, data);
};
// 按职业分类的新冠病毒疫苗接种信息汇总表导出
export const proOutput = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptProfession}/output`, data);
};
// 按职业分类的新冠病毒疫苗接种信息汇总表细节查询
export const proQueryDetail = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptProfession}/queryDetail`, data);
};
// 按职业分类的新冠病毒疫苗接种信息汇总表细节导出
export const proOutputDetail = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptProfession}/outputDetail`, data);
};
// 新冠病毒疫苗分地区接种信息汇总表查询
export const areaQuery = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptArea}/query`, data);
};
// 新冠病毒疫苗分地区接种信息汇总表导出
export const areaOutput = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptArea}/output`, data);
};
// 新型冠状病毒疫苗及接种信息采集表查询
export const jzQuery = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptJzjl}/query`, data);
};
// 新型冠状病毒疫苗及接种信息采集表导出
export const jzOutput = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptJzjl}/output`, data);
};
// 新冠病毒疫苗到期未种统计表查询
export const wzQuery = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptNotVacc}/query`, data);
};
// 新冠病毒疫苗到期未种统计表导出
export const wzOutput = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptNotVacc}/output`, data);
};
// 新冠病毒疫苗到期未种统计表详情
export const finishQueryDetail = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptNotVacc}/queryDetail`, data);
};
// 新冠病毒疫苗到期未种统计表详情导出
export const finishOutputDetail = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptNotVacc}/outputDetail`, data);
};
// 新冠病毒疫苗免疫程序完成情况统计表查询
export const finishedQuery = (data) => {
  return axiosUtil.axiosPost(`${url.xgRptFinishVacc}/query`, data);
};
// 新冠病毒疫苗免疫程序完成情况统计表导出
export const finishedOutput = (data) => {
  return axiosUtil.downloadFile(`${url.xgRptFinishVacc}/output`, data);
};
