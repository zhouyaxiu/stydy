<!-- 质量控制-上传数据质量统计 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">选择地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" :showSelect="true"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="ml20 label required">超过</span>
          <el-input v-model="overDay" maxlength="3" min="0" clearable class="mr10" style="max-width:100px" />
          <span class="label">天未上传</span>
        </div>
      </div>
      <ss-query-bottom tipText="可以点击未上传接种单位数字查看明细" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" @openDetailDialog="openDetailDialog"
      :total="0"></ss-custom-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :detailData="detailData"></dialog-info>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { noUploadList, output } from "@/api/apis/qc";
import dialogInfo from "./dialogInfo.vue";
import { mainStore } from "@/store/index";
import { positive_interger } from "@/utils/common/regex";
const store = mainStore();

const tableData = ref([]);
const secTime = ref(0);
const dialogVisible = ref(false);
const detailData = reactive({});

const columnList = reactive([
  {
    label: '地区',
    prop: 'areaName',
    minWidth: '80',
  },
  {
    label: '接种单位数',
    prop: 'unitCount',
    minWidth: '100',
  },
  {
    label: '上传接种单位数',
    prop: 'uploadCount',
    minWidth: '100',
  },
  {
    label: '未上传接种单位数',
    prop: 'noUploadCount',
    minWidth: '100',
    type: "clickable",
  },
  {
    label: '所占百分比(%)',
    prop: 'per',
    minWidth: '100',
  }
]);

const searchData = ref(null);
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
// 地区
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
const overDay = ref("1");//天数
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
};
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
// 查询
const search = () => {
  if (!overDay.value) {
    return globalProxy.$util.showMsg('天数必填且不能为空！', 'error')
  }
  if (!positive_interger.test(overDay.value)) {
    return globalProxy.$util.showMsg('天数必须是大于0的整数！', 'error')
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    skipLevel: areaTreeValue.skipValue == "5" ? 4 : areaTreeValue.skipValue,
    overDay: overDay.value
  }
  searchData.value = params;
  noUploadList(params).then(res => {
    tableData.value = res.data.data;
    secTime.value = res.data.sec;
  })
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.areaName = areaTreeValue.areaName;
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  output(params)
}
// 点击弹框查看明细
const openDetailDialog = (row) => {
  detailData.areaCode = row.areaName == '合计' ? areaTreeValue.areaCode : row.areaCode;
  detailData.overDay = overDay.value;
  detailData.areaName = row.areaName == '合计' ? areaTreeValue.areaName : row.areaName;
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
</script>
<style scoped lang='scss'></style>