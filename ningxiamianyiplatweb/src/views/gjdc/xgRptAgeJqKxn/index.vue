<!-- 国家督察-新冠疫苗加强第2针分年龄汇总表（康希诺）-->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label">选择地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="true" @nodeClick="nodeClick"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label">截止日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label">生产企业：</span>
          <ss-all-vaccine-vendor ref="selectedVaccineVendor" :type="3"></ss-all-vaccine-vendor>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" :spanMethod="objectSpanMethod"></ss-custom-table>
  </div>
</template>

<script setup>
import { kxnList, outputKxn } from "@/api/apis/gjdc";

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);
const selectedVaccineVendor = ref();

const columnList = reactive([
  {
    label: '接种人群',
    prop: 'pageAge',
    minWidth: '90',
  },
  {
    label: '地区',
    prop: 'areaName',
    minWidth: '200',
  },
  {
    label: '疫苗生产企业',
    prop: 'factoryName',
    minWidth: '120',
  },
  {
    label: '基础免疫第一剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：人）',
        prop: 'jc1dr',
        minWidth: '120'
      },
      {
        label: '累计接种人数（单位：人）',
        prop: 'jc1lj',
        minWidth: '120'
      }]
  },
  {
    label: '加强接种第二剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：人）',
        prop: 'jq2dr',
        minWidth: '120'
      },
      {
        label: '累计接种人数（单位：人）',
        prop: 'jq2lj',
        minWidth: '120'
      }]
  },
  {
    label: '序贯接种第三剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：人）',
        prop: 'xug3dr',
        minWidth: '120'
      },
      {
        label: '累计接种人数（单位：人）',
        prop: 'xug3lj',
        minWidth: '120'
      }]
  },
  {
    label: '加强接种第三剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：人）',
        prop: 'jq3dr',
        minWidth: '120'
      },
      {
        label: '累计接种人数（单位：人）',
        prop: 'jq3lj',
        minWidth: '120'
      }]
  },
  {
    label: '序贯接种第四剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：人）',
        prop: 'xug4dr',
        minWidth: '120'
      },
      {
        label: '累计接种人数（单位：人）',
        prop: 'xug4lj',
        minWidth: '120'
      }]
  },
  {
    label: '未感染第五剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：人）',
        prop: 'wgr5dr',
        minWidth: '120'
      },
      {
        label: '累计接种人数（单位：人）',
        prop: 'wgr5lj',
        minWidth: '120'
      }]
  },
  {
    label: '已感染人群',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日接种人数（单位：剂次）',
        prop: 'ygrdr',
        minWidth: '140'
      },
      {
        label: '累计接种人数（单位：剂次）',
        prop: 'ygrlj',
        minWidth: '140'
      }]
  },
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(8), globalProxy.$dateUtil.dayAddNum(1)]);
// 查询
const search = () => {
  let startTime = monthValue.value[0];
  let endTime = monthValue.value[1];

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择填报日期范围！', 'error', 5000, true);
  }

  if (!selectedVaccineVendor.value.fenterNo) {
    return globalProxy.$util.showMsg('请选择生产企业！', 'error', 5000, true);
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaName: areaTreeValue.areaName,
    skipLevel: areaTreeValue.skipValue,//跳级参数(1:以地市为单位查看、2:以县区为单位查看、3:以乡镇为单位查看、4:以接种单位查看)
    startDate: startTime,
    endDate: endTime,
    factoryId: selectedVaccineVendor.value.fenterNo.join(','),
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  searchData.value = params;
  kxnList(params).then(res => {
    tableData.value = res.data.data;
    secTime.value = res.data.sec;
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
  let params = JSON.parse(JSON.stringify(searchData.value));
  // params.selectAreaName = params.selectAreaName + params.newBaseCode
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  outputKxn(params);
}
const objectSpanMethod = ({ row, column, rowIndex, columnIndex }) => {
  if (columnIndex == 0 || columnIndex == 2) {
    //数据处理
    const cellValue = row[column.property];
    if (cellValue) {
      // 上一条数据
      const prevRow = tableData.value[rowIndex - 1]
      // 下一条数据
      let nextRow = tableData.value[rowIndex + 1]
      // 当上一条数据等于下一条数据

      if (prevRow && prevRow[column.property] === cellValue) {
        return { rowspan: 0, colspan: 0 }
      } else {
        let rowspan = 1
        while (nextRow && nextRow[column.property] === cellValue) {
          rowspan++
          nextRow = tableData.value[rowspan + rowIndex]
        }
        if (rowspan > 1) {
          return { rowspan, colspan: 1 }
        }
      }
    }
  }
}
onMounted(() => {
})
</script>
<style scoped lang='scss'></style>