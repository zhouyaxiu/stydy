<!-- 国家督察-各地不同年龄段新冠病毒疫苗接种人数汇总表 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label">地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label">接种日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label">疫苗生产企业：</span>
          <ss-vaccine-vendor ref="selectedVaccineVendor" :multiple="true" :collapseTags="true"></ss-vaccine-vendor>
        </div>
        <div class="item">
          <span class="label">制品编码：</span>
          <ss-product-code v-model="productno"></ss-product-code>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <span class="label">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
              @change="handleCheckAllChange">年龄组：</el-checkbox>
          </span>
          <el-checkbox-group v-model="chkAge" @change="handleChkAgeChange" :disabled="!checkAll">
            <el-checkbox v-for="item in agesList" :label="item.id" :key="item.id">{{ item.name }}</el-checkbox>
          </el-checkbox-group>
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
const chkAge = ref([]);//年龄组
const checkAll = ref(false);//是否选择年龄组
const isIndeterminate = ref(false)
const productno = ref('-1');
const selectedVaccineVendor = ref(null);
// 年龄组
const agesList = globalProxy.$commonOptions.agesList;

const columnList = reactive([
  {
    label: '地区',
    prop: 'area_name',
    minWidth: '80',
  },
  {
    label: '累计接种人数（人）',
    hasChildrenTitle: true,
    list: [
      {
        label: '合计',
        prop: 'lj',
        minWidth: '100'
      },
      {
        label: '第1剂',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '第2剂',
        prop: 'lj2',
        minWidth: '100'
      },
      {
        label: '第3剂',
        prop: 'lj3',
        minWidth: '100'
      },
      {
        label: '第4剂',
        prop: 'lj4',
        minWidth: '100'
      },
      {
        label: '第5剂',
        prop: 'lj5',
        minWidth: '100'
      }
    ]
  },
  {
    label: '当日接种人数（人）',
    hasChildrenTitle: true,
    list: [
      {
        label: '合计',
        prop: 'dr',
        minWidth: '100'
      },
      {
        label: '第1剂',
        prop: 'dr1',
        minWidth: '100'
      },
      {
        label: '第2剂',
        prop: 'dr2',
        minWidth: '100'
      },
      {
        label: '第3剂',
        prop: 'dr3',
        minWidth: '100'
      },
      {
        label: '第4剂',
        prop: 'dr4',
        minWidth: '100'
      },
      {
        label: '第5剂',
        prop: 'dr5',
        minWidth: '100'
      }
    ]
  }
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
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
    startTime: startTime,
    endTime: endTime,
    fenterNo: selectedVaccineVendor.value.fenterNo.join(','),
    productno: productno.value,
    chkAge: chkAge.value,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  searchData.value = params;
  queryBaseChangedList(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data;
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
const handleCheckAllChange = (val) => {
  if (!val) {
    isIndeterminate.value = false
    return chkAge.value = [];
  }
  chkAge.value = agesList.map(item => item.id);
  isIndeterminate.value = false
}
const handleChkAgeChange = (val) => {
  if (!val) {
    checkAll.value = false;
    isIndeterminate.value = true;
  }
  if (chkAge.value.length == agesList.length) {
    checkAll.value = true;
    isIndeterminate.value = false;
  } else {
    isIndeterminate.value = true;
  }
}

onMounted(() => {
})
</script>
<style scoped lang='scss'></style>