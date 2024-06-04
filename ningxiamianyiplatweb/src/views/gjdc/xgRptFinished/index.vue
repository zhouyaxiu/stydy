<!-- 国家督察-新冠病毒疫苗免疫程序完成情况统计表 -->
<template>
  <div class="finished">
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="true" @nodeClick="nodeClick"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label required">接种日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label required">制品编码：</span>
          <ss-product-code ref="selectedVaccNo"></ss-product-code>
        </div>
      </div>
      <ss-query-bottom :statisTics="false" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { finishedQuery, finishedOutput } from "@/api/apis/gjdc";

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);
const selectedVaccNo = ref(null);
const columnList = reactive([
  {
    label: '地区',
    prop: 'areaName',
    minWidth: '180',
    formatter: function (row) {
      return row.areaName + " " + row.areaCode.slice(0, 10)
    }
  },
  {
    label: '接种人数',
    prop: 'allCount',
    minWidth: '100',
  },
  {
    label: '已完成免疫程序人数',
    prop: 'hasFinishzCount',
    minWidth: '100',
  },
  {
    label: '完成率（%）',
    prop: 'completionRate',
    minWidth: '120',
  },
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName
});
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
// 查询
const search = () => {
  let startTime = monthValue.value[0];
  let endTime = monthValue.value[1];

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择接种日期范围！', 'error', 5000, true);
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaLevel: areaTreeValue.selectAreaLevel,
    showLevel: areaTreeValue.skipValue == "5" ? 4 : areaTreeValue.skipValue,
    endYzDate: endTime,
    vaccNo: selectedVaccNo.value.vaccNo,
    startYzDate: startTime,
  }
  let vaccName = selectedVaccNo.value.vaccNo ? selectedVaccNo.value.vaccName : "";
  searchData.value = {
    ...params,
    areaName: areaTreeValue.areaName + areaTreeValue.areaCode.slice(0, 10),
    vaccName,
  };
  finishedQuery(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.data;
      secTime.value = res.data.data.sec;
    } else {
      if (res.data.popMsg) {
        secTime.value = "";
        tableData.value = [];
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1);
}
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  finishedOutput(params);
}

onMounted(() => {
})
</script>
<style lang='scss'>
.finished .el-table__inner-wrapper::before {
  bottom: 0 !important;
}
</style>