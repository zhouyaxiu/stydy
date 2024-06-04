<!-- 国家督察-国家数据上传监测统计表 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">上传日期：</span>
          <ss-date-range-new ref="daterangeRef" v-model:defaultValue="monthValue" rangeName="month"></ss-date-range-new>
        </div>
      </div>
      <ss-query-bottom :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"
      remarkText="1、系统当天的数据，在17点进行汇总，汇总上传时间截止到16点前的更新个案情况。同时更新前一天上传档案对应的上传成功档案数、上传失败档案数、国家无返回档案数。<br>2、系统非当天的数据，在第2天凌晨进行汇总。"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { queryUploadCheckList, outputUploadCheckList } from "@/api/apis/gjdc";
const tableData = ref([]);
const secTime = ref(0)
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const columnList = reactive([
  {
    label: '上传日期',
    prop: 'uploadDate',
  },
  {
    label: '更新档案数',
    prop: 'updateCount',
  },
  {
    label: '打包上传数',
    prop: 'packageCount',
  },
  {
    label: '数据不合格未上传数',
    prop: 'nopackagecount',
  },
  {
    label: '上传成功数',
    prop: 'successcount',
  },
  {
    label: '上传失败数',
    prop: 'failCount',
  },
  {
    label: '国家无返回数',
    prop: 'nobackcount',
  }
])
const searchData = ref(null);
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(1), globalProxy.$dateUtil.dayAddNum(1)]);
// 上传日期ref
const daterangeRef = ref(null);
// 查询
const search = () => {
  // if (!daterangeRef.value.SevenVerify(30)) return;
  const params = {
    endTime: monthValue.value[1],
    startTime: monthValue.value[0]
  }
  searchData.value = params;
  queryUploadCheckList(params).then(res => {
    if (res.data.popMsg) {
      tableData.value = [];
      secTime.value = "";
      globalProxy.$util.showMsg(res.data.popMsg, 'error');
    }
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
      secTime.value = res.data.sec;
    }
  })
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || (searchData.value.startTime != monthValue.value[0] || searchData.value.endTime != monthValue.value[1]) || tableData.value.length == 0) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  // params.selectAreaName = params.selectAreaName + params.newBaseCode
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  outputUploadCheckList(params)
}
</script>
<style scoped lang='scss'></style>