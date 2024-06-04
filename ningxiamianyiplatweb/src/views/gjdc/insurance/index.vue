<!-- 国家督察-新冠病毒疫苗医保正确数据统计表 -->
<template>
  <div>
    <!-- @setConditionFlag="setConditionFlag" -->
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">选择地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" :showSelect="true"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
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
    <ss-custom-table :tableData="tableData" :columnList="columnList"
      @openDetailDialog="openDetailDialog"></ss-custom-table>
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
// 接种日期
const jzDate = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
// 出生日期
const birthDate = ref([globalProxy.$dateUtil.getAfterNYearsDateStr(-1), globalProxy.$dateUtil.currentDate()])

const tableData = ref([]);
const tabPage = ref(null);
let secTime = ref(0);
const exportAreaName = ref('');
const columnList = reactive([
  {
    label: '地区',
    prop: 'area_name',
    minWidth: '180',
  },
  {
    label: '地区编码',
    prop: 'area_code',
  },
  {
    label: '合格接种剂次数',
    prop: 'jcCount',
    minWidth: "120",
    type: 'clickable'
  },
  {
    label: '合格接种人数',
    prop: 'personCount',
  }
])
const dialogVisible = ref(false);
const searchData = ref(null);
// 地区
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});

const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
};
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}

const search = () => {
  let jzrqStartTime = jzDate.value[0] || ''
  let jzrqEndTime = jzDate.value[1] || ''
  if (!jzrqStartTime || jzrqStartTime == '' || !jzrqEndTime || jzrqEndTime == '') {
    return ElMessage.error('接种日期为必填项，请填写完整再查询！');
  }
  let birthStartTime = birthDate.value[0] || ''
  let birthEndTime = birthDate.value[1] || ''
  if (!birthStartTime || birthStartTime == '' || !birthEndTime || birthEndTime == '') {
    return ElMessage.error('出生日期为必填项，请填写完整再查询！');
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
// 点击弹框查看明细
const openDetailDialog = (row, prop) => {
  detailData.value = {
    ...searchData.value,
    professionCode: row.professionName == '合计' ? "-1" : prop.slice(-1),
    fbactid: prop.slice(-1),
  }
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
</script>
<style scoped lang='scss'></style>