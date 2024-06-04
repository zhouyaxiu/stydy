<!-- 国家督察-新冠病毒疫苗医保错误数据统计（总表） -->
<template>
  <div>
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <ss-date-range title="接种日期" v-model:defaultValue="jzDate"></ss-date-range>
        </div>
        <div class="item">
          <ss-date-range title="出生日期" v-model:defaultValue="birthDate"></ss-date-range>
        </div>
      </div>
      <ss-query-bottom :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { searchXgRptInfoFullJzjlfull, exportXgRptInfoFullJzjlfull } from "@/api/apis/gjdc";
import { mainStore } from "@/store/index";
const store = mainStore();

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const jzDate = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
const birthDate = ref([globalProxy.$dateUtil.getAfterNYearsDateStr(-1), globalProxy.$dateUtil.currentDate()]);

const tableData = ref([]);
const tabPage = ref(null);
let secTime = ref(0);
const exportAreaName = ref('');
const columnList = reactive([
  {
    label: '问题类型',
    prop: '问题类型',
    minWidth: '180',
  },
  {
    label: '不合格条数',
    prop: 'area_code',
  },
])
const searchData = ref(null);

const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}

const search = () => {
  let jzrqStartTime = jzDate.value[0] || ''
  let jzrqEndTime = jzDate.value[1] || ''
  if (!jzrqStartTime || jzrqStartTime == '' || !jzrqEndTime || jzrqEndTime == '') {
    ElMessage.error('接种日期为必填项，请填写完整再查询！')
    return false
  }
  let birthStartTime = birthDate.value[0] || ''
  let birthEndTime = birthDate.value[1] || ''
  if (!birthStartTime || birthStartTime == '' || !birthEndTime || birthEndTime == '') {
    ElMessage.error('出生日期为必填项，请填写完整再查询！')
    return false
  }
  let params = {
    selectAreaId: areaTreeValue.areaId,
    selectAreaCode: areaTreeValue.areaCode,
    selectAreaName: areaTreeValue.areaName,
    selectAreaLevel: areaTreeValue.selectAreaLevel,
    selectType: areaTreeValue.skipValue,
    birthStartTime: birthStartTime,
    birthEndTime: birthEndTime,
    jzrqStartTime: jzrqStartTime,
    jzrqEndTime: jzrqEndTime,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  searchData.value = params
  exportAreaName.value = areaTreeValue.areaName + ' ' + areaTreeValue.areaCode
  searchXgRptInfoFullJzjlfull(params)
    .then(res => {
      dataList = res.data
      secTime = res.sec
    })
    .catch(() => {
    })
}
const exportExcel = () => {
  if (!searchDataa.value) {
    ElMessage.error('查询后才可以导出！')
    return
  }
  if (dataList.length === 1) {
    ElMessage.error('暂无需要导出的数据！')
    return
  }
  searchData.value.selectAreaName = exportAreaName.value
  let params = {
    ...searchData.value,
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  exportXgRptInfoFullJzjlfull(params)
}
</script>
<style scoped lang='scss'></style>