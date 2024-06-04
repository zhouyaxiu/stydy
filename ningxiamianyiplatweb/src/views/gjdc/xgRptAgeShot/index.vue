<!-- 国家督察-新冠疫苗分年龄接种情况-->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label">选择地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label">截止日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
  </div>
</template>

<script setup>
import { queryBaseChangedList, outputBaseChangedList } from "@/api/apis/gjdc";

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);

const columnList = reactive([
  {
    label: '行政区划编码',
    prop: 'area_name',
    minWidth: '120',
  },
  {
    label: '行政区划',
    prop: 'area_name',
    minWidth: '100',
  },
  {
    label: '疫苗（大类）',
    prop: 'area_name',
    minWidth: '120',
  },
  {
    label: '疫苗（小类）',
    prop: 'area_name',
    minWidth: '120',
  },
  {
    label: '接种剂次',
    prop: 'area_name',
    minWidth: '120',
  },
  {
    label: '实际接种剂次数',
    hasChildrenTitle: true,
    list: [
      {
        label: '0岁',
        prop: 'lj',
        minWidth: '50'
      },
      {
        label: '1岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '2岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '3岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '4岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '5岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '6岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '7岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '8岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '9岁',
        prop: 'lj1',
        minWidth: '50'
      },
      {
        label: '10岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '11岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '12岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '13岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '14岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '15岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '16岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '17岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '18岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '19岁',
        prop: 'lj1',
        minWidth: '60'
      },
      {
        label: '20-24岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '25-29岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '30-34岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '35-39岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '40-44岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '45-49岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '50-54岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '55-59岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '60-64岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '65-69岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '70-74岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '75-79岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '80-84岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '85-89岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '90-94岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '95-99岁',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '100岁及以上',
        prop: 'lj1',
        minWidth: '120'
      }
    ]
  },
  {
    label: '合计',
    prop: 'area_name',
    minWidth: '80',
  },
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
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
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaId: areaTreeValue.areaId,
    areaLevel: areaTreeValue.selectAreaLevel,
    areaName: areaTreeValue.areaCode,
    endTime: endTime,
    startTime: startTime,
  }
  searchData.value = params;
  queryBaseChangedList(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
      secTime.value = res.data.sec;
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
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value));
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  outputBaseChangedList(params);
}

onMounted(() => {
})
</script>
<style scoped lang='scss'></style>