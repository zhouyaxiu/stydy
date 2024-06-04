<!-- 国家督察-新冠病毒疫苗到期未种统计表 -->
<template>
  <div class="wz">
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="true" @nodeClick="nodeClick"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label required">应种日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label required">出生日期：</span>
          <ss-date-range-new v-model:defaultValue="birthValue"></ss-date-range-new>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <span class="label required">接种间隔（天）：</span>
          <el-input v-model="intervalDay" clearable maxlength="9"></el-input>
        </div>
        <div class="item">
          <span class="label">疫苗生产企业：</span>
          <ss-vaccine-vendor ref="selectedVaccineVendor"></ss-vaccine-vendor>
        </div>
        <div class="item">
          <span class="label required">制品编码：</span>
          <ss-product-code ref="selectedVaccNo"></ss-product-code>
        </div>
        <div class="item">
          <span class="label required">未种剂次：</span>
          <el-select v-model="wzJc" placeholder="请选择" clearable style="position:relative;">
            <el-option v-for="item in wzJcList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-table :data="tableData" :headerCellStyle="{ 'text-align': 'center' }" :cellStyle="{ 'text-align': 'center' }">
      <el-table-column min-width="60" label="地区">
        <template #default="scope">
          <span>{{ scope.row.areaCode.slice(0, 10) }}&nbsp;&nbsp;{{ scope.row.areaName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="人数" prop="count">
        <template #default="scope">
          <span v-if="scope.row.count == 0">{{ scope.row.count }}</span>
          <span v-else @click="openDetailDialog(scope.row)" style="color: #2c6aff;cursor: pointer;">{{
            scope.row.count
          }}</span>
        </template>
      </el-table-column>
    </ss-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :detailData="detailData"></dialog-info>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { wzQuery, wzOutput } from "@/api/apis/gjdc";
import { getLevel } from '@/utils/common/area.js'
import moment from "moment";
import dialogInfo from "./dialogInfo.vue";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { positive_interger } from "@/utils/common/regex";
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);
const detailData = ref(null);
// const detailOtherData = ref(null);
const dialogVisible = ref(false);
//应种日期
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
//出生日期
const birthValue = ref([moment().add(-1, 'month').format("YYYY-MM-DD"), globalProxy.$dateUtil.currentDate()]);
const intervalDay = ref(''); //间隔天数
const wzJc = ref(""); //未种剂次
const selectedVaccineVendor = ref(null);
const selectedVaccNo = ref(null);

// 未种剂次列表
const tempWzJcList = [
  { id: '', name: '请选择' },
  { id: '2', name: '第2剂次' },
  { id: '3', name: '第3剂次' },
  { id: '4', name: '第4剂次' },
]
const wzJcList = ref(tempWzJcList);
watch(() => selectedVaccNo && selectedVaccNo.value && selectedVaccNo.value.vaccNo, (val) => {
  if (val == 'G3') {
    wzJcList.value = tempWzJcList.filter(item => item.id != '4');
  } else {
    wzJcList.value = tempWzJcList;
  }
  wzJc.value = "";
}, { deep: false, immediate: false, });

const searchData = ref(null);
let areaTreeValue = reactive({
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName
});

// 查询
const search = () => {
  let startTime = monthValue.value[0];
  let endTime = monthValue.value[1];

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择应种日期范围！', 'error', 5000, true);
  }
  let birthstartTime = birthValue.value[0];
  let birthendTime = birthValue.value[1];
  if (!positive_interger.test(intervalDay.value)) {
    return globalProxy.$util.showMsg('接种间隔（天）必须是大于0的正整数', 'error', 5000, true);
  }
  if (!wzJc.value) {
    return globalProxy.$util.showMsg('请选择一个剂次', 'error', 5000, true);
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaLevel: areaTreeValue.selectAreaLevel,
    showLevel: areaTreeValue.skipValue == "5" ? 4 : areaTreeValue.skipValue,
    startYzDate: startTime,
    endYzDate: endTime,
    startBirthDate: birthstartTime,
    endBirthDate: birthendTime,
    day: intervalDay.value,
    fenterNo: selectedVaccineVendor.value.fenterNo,
    vaccNo: selectedVaccNo.value.vaccNo,
    jici: wzJc.value,
  }
  let jiciName = wzJc.value ? wzJcList.value.filter(item => item.id == wzJc.value)[0].name : "";
  let fenterName = selectedVaccineVendor.value.fenterNo ? selectedVaccineVendor.value.fenterName : "";
  let vaccName = selectedVaccNo.value.vaccNo ? selectedVaccNo.value.vaccName : "";
  searchData.value = {
    ...params,
    areaName: areaTreeValue.areaName + areaTreeValue.areaCode.slice(0, 10),
    vaccName,
    fenterName,
    jiciName
  };
  wzQuery(params).then(res => {
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
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
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
  wzOutput(params)
}
// 点击弹框查看明细
const openDetailDialog = (row, prop) => {
  detailData.value = {
    ...searchData.value,
    areaCode: row.areaCode != '合计' ? row.areaCode : areaTreeValue.areaCode,
    areaLevel: row.areaCode != '合计' ? getLevel(row.areaCode) : areaTreeValue.selectAreaLevel,
    showLevel: ""
  }
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
onMounted(() => {
})
</script>
<style lang='scss'>
.wz .el-table__inner-wrapper::before {
  bottom: 1px !important;
}
</style>