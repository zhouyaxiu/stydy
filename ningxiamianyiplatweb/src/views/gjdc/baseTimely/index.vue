<!-- 国家督察-建档及时性统计 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="required label">地区选择：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="true" @nodeClick="nodeClick"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="required label">日期范围：</span>
          <ss-month-range-new v-model:defaultValue="monthValue"
            :uptoDate="globalProxy.$consts.ENDDATEMONTH"></ss-month-range-new>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" @openDetailDialog="openDetailDialog"
      remarkText="①省、市、县、乡、接种单位采用静态计算。"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { queryBaseTimelyList, outputBaseTimelyList } from "@/api/apis/gjdc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore()
const tableData = ref([]);
const secTime = ref(0)
const columnList = reactive([
  {
    label: '地区',
    prop: 'areaName',
    overflowTip: true,
  },
  {
    label: '档案（基本信息）',
    hasChildrenTitle: true,
    list: [
      {
        label: '建档数量',
        prop: 'addinfoCount',
        minWidth: '60'
      },
      {
        label: '及时建档数量',
        prop: 'infoaddtimelyCount',
        minWidth: '60'
      },
      {
        label: '建档及时性%',
        prop: 'infoaddtimelyCountRate',
        minWidth: '60'
      },
      {
        label: '上传及时性%',
        prop: 'infosendtimelyCountRate',
        minWidth: '60'
      }
    ]
  },
  {
    label: '接种记录',
    hasChildrenTitle: true,
    list: [
      {
        label: '录入数量',
        prop: 'addjzjlCount',
        minWidth: '60'
      },
      {
        label: '及时录入数量',
        prop: 'jzjladdtimelyCount',
        minWidth: '60'
      },
      {
        label: '录入及时性%',
        prop: 'jzjladdtimelyCountRate',
        minWidth: '60'
      },
      {
        label: '上传及时性%',
        prop: 'jzjlsendtimelyCountRate',
        minWidth: '60'
      }
    ]
  }
])
const searchData = ref(null);
const monthValue = ref([globalProxy.$dateUtil.currentMonth(), globalProxy.$dateUtil.currentMonth()]);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName,
});
// 查询
const search = () => {
  let startTime = monthValue.value[0]
  let endTime = monthValue.value[1]

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择日期范围！', 'error', 5000, true);
  }
  let { areaCode, areaId, selectAreaLevel: areaLevel, areaName, skipValue } = areaTreeValue;

  const params = { areaCode, areaId, areaLevel, areaName, endTime: endTime, skipValue, startTime: startTime }

  searchData.value = params;
  queryBaseTimelyList(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
      secTime.value = res.data.sec;
    } else {
      if (res.data.popMsg) {
        tableData.value = []
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
}
const selectClick = (node) => {
  areaTreeValue.skipValue = node
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.areaName = params.areaName + " " + params.areaCode.slice(0, 10);
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  outputBaseTimelyList(params)
}
</script>
<style scoped lang='scss'></style>