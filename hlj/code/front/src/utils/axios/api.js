import axiosUtil from './axios'
import url from './url'

/**
 * 登录
 */
export function getSessionid() {
    return axiosUtil.axioSessionId(url.getSessionId);
}
export function login(data) {
    return axiosUtil.axiosLogin(url.login,data);
}
export function editPass(data) {
    return axiosUtil.axiosPut(url.editPass,data);
}
export function logout(data) {
    return axiosUtil.axiosDelete(url.logout,data);
}
export function checkPwd(data) {
    return axiosUtil.axiosPut(url.checkPwd,data);
}
export function checkLogout() {
    return axiosUtil.axiosGet(url.checkLogout);
}

//地区相关接口
export function getArea(data) {
    return axiosUtil.axiosGet(url.vaccinationUnit+"/getArea", data);
}
//年龄组
export function getAges() {
    return axiosUtil.axiosGet(url.user+"/getAges");
}
//疫苗种类
export function getVaccineType() {
    return axiosUtil.axiosGet(url.vaccination+"/getVaccineType");
}
/*******************左侧*******************/

//疫苗接种率
export function getVaccinationRates(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getVaccinationRates", data);
}
//扫码接种率
export function getWisdomScanVaccinationRates(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getWisdomScanVaccinationRates", data);
}
//实时库存
export function getInventory(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getInventory", data);
}
//接种单位分类
export function getClassification(data) {
    return axiosUtil.axiosGet(url.vaccinationUnit+"/getClassification", data);
}
/*******************中间******************/
//地图下面的--人数统计
export function getTotal(data) {
    return axiosUtil.axiosGet(url.user+"/getTotal", data);
}
//历年出生儿童建档数
export function getWisdomBirthYear(data) {
    return axiosUtil.axiosGet(url.user+"/getWisdomBirthYear", data);
}
//当年出生儿童建档数
export function getWisdomBirthMonth(data) {
    return axiosUtil.axiosGet(url.user+"/getWisdomBirthMonth", data);
}
/*******************右侧*******************/
//当日接种
export function getSameDayVaccination(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getSameDayVaccination", data);
}
//0-7岁儿童NIP疫苗全程接种率
export function getNIPVaccination(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getNIPVaccination", data);
}
//当年非免疫规划疫苗接种统计
export function getVaccinationCounts(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getVaccinationCounts", data);
}
//疫苗库存
export function getWisdomInventory(data) {
    return axiosUtil.axiosGet(url.vaccination+"/getWisdomInventory", data);
}
//集成平台_登录
export function jcpt_login(data) {
    return axiosUtil.axiosGet(url.systemURI+"/singleLogin", data);
}

