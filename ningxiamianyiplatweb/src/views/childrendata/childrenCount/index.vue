<!-- 受种者资料查询-受种者数查询 -->
<template>
  <div>
    <ss-condition-area ref="conditionPage">
      <div class="condition-row">
        <div class="item">
          <span class="label required">选择地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" :showSelect="true"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <div class="flex">
            <el-checkbox v-model="birthDisabled" @change="birthDisabledHandler" size="large" class="ml20">
              <span class="mr10 label" style="line-height:32px;height:32px;margin-left:4px;">出生日期：</span>
            </el-checkbox>
          </div>
          <ss-date-range-new :disabledFlag="!birthDisabled" v-model:defaultValue="birthDate"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="required label">受种者标识：</span>
          <el-select v-model="fisadult" clearable style="position: relative;">
            <el-option v-for="item in fisadultOptions" :key="item.code" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <span class="required label">户籍类型：</span>
          <ss-check-resiType ref="checkResiTypePage"></ss-check-resiType>
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
import { ref, reactive } from 'vue';
import { queryChildCount, outputChildCount } from "@/api/apis/childrendata";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const tableData = ref([]);
import { mainStore } from "@/store/index";
const store = mainStore()
const secTime = ref(0);
const columnList = reactive([
  {
    label: '地区名称',
    prop: 'areaName',
    minWidth: '120',
  },
  {
    label: '受种者数量',
    prop: 'childCount',
  }
])
const birthDisabled = ref(false);
const birthDate = ref([])
// const checkbirth = ref(null);
const checkResiTypePage = ref(null);
// const checkBirth = ref(globalProxy.$dateUtil.currentDate())
const fisadult = ref("");//受种者标识
const fisadultOptions = globalProxy.$commonOptions.heraStateList;
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
const searchData = ref(null);
const birthDisabledHandler = (val) => {
  if (val) {
    birthDate.value = [globalProxy.$dateUtil.currentDate(), globalProxy.$dateUtil.currentDate()]
  } else {
    birthDate.value = []
  }
}
// 查询
const search = () => {
  if (checkResiTypePage.value.needResiType.length == 0) {
    return globalProxy.$util.showMsg('户籍类型必选！', 'error', 5000, true);
  }
  // if (birthDisabled.value && checkbirth.value.msg) return globalProxy.$util.showMsg(checkbirth.value.msg, 'error', 5000, true);
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaLevel: areaTreeValue.selectAreaLevel,
    skipValue: areaTreeValue.skipValue,
    startBirth: birthDate.value[0],
    endBirth: birthDate.value[1],
    regtypes: checkResiTypePage.value.needResiType.join(","),
    fisadult: fisadult.value,
  }
  searchData.value = params;
  queryChildCount(params).then(res => {
    if (res.data.popMsg) {
      tableData.value = [];
      secTime.value = "";
      return globalProxy.$util.showMsg(res.data.popMsg, 'error', 5000, true);
    }
    tableData.value = res.data.data.data;
    secTime.value = res.data.data.sec;
  })
}
// 导出excel
const exportExcel = () => {
  let params = JSON.parse(JSON.stringify(searchData.value));
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode || checkResiTypePage.value.needResiType.join(",") != params.regtypes) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let regtypesName = []
  checkResiTypePage.value.resiTypeList.map(item => checkResiTypePage.value.resiType.map(itm => { if (item.id == itm) { regtypesName.push(item.name) } }))
  params.areaName = areaTreeValue.areaName + " " + areaTreeValue.areaCode.slice(0, 10)
  params.skipName = areaTreeValue.skipValue == 1 ? '以地市为单位查看' : areaTreeValue.skipValue == 2 ? '以区县为单位查看' : areaTreeValue.skipValue == 3 ? '以乡镇为单位查看' : areaTreeValue.skipValue == 4 ? '以接种点为单位查看' : ''
  params.regtypesName = regtypesName.join(",")
  console.log(params, 2222)
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  outputChildCount(params)
}
const nodeClick = (node) => {
  areaTreeValue = node
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
}
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
</script>
<style scoped lang='scss'></style>