<!-- 国家督察-国家数据不合格未上传统计表 -->
<template>
  <div class="nopack">
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="required label">上传日期：</span>
          <ss-date-range-new ref="daterangeRef" v-model:defaultValue="monthValue" rangeName="weeks"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="required label">选择地区：</span>
          <ss-area-areaTreeStation ref="areaTreeStation" v-model="areaTreeValue" :getToLevelNum="5" :showSelect="false"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label">数据类型：</span>
          <el-radio-group v-model="dataType" class="ml-4">
            <el-radio label="01" size="large">基本信息</el-radio>
            <el-radio label="02" size="large">接种记录</el-radio>
          </el-radio-group>
        </div>
      </div>
      <ss-query-bottom tipText="基本信息错误按归属单位统计；接种记录错误按接种单位统计；已撤销接种单位按归属单位统计；卡介苗、乙肝1按归属单位统计。" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-table :data="tableData" :headerCellStyle="{ 'text-align': 'center' }" :cellStyle="{ 'text-align': 'center' }">
      <el-table-column label="不合格类型" prop="messageType"></el-table-column>
      <el-table-column min-width="60" label="数量">
        <template #default="scope">
          <span v-if="scope.row.totalCount == 0">{{ scope.row.totalCount }}</span>
          <span v-else @click="openDetailDialog(scope.row.messageType, scope.row.totalCount)" class="pointer">{{
            scope.row.totalCount }}</span>
        </template>
      </el-table-column>
    </ss-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :searchData="searchData"></dialog-info>
    <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'国家数据不合格未上传统计'"
      :dialogVisible="exportDialogVisible"></ss-export-dialog>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { queryUploadNoPackageList, outputUploadNoPackageList } from "@/api/apis/gjdc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import dialogInfo from "./dialogInfo.vue";
import { mainStore } from "@/store/index";
const store = mainStore()
const tableData = ref([]);
const dataType = ref('01');
const secTime = ref(0);
const total = ref(0);
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(1), globalProxy.$dateUtil.dayAddNum(1)])
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  areaName: store.userInfo.areaName,
});
const exportDialogVisible = ref(false);
// 上传日期ref
const daterangeRef = ref(null);
const dialogVisible = ref(false);
const searchData = ref(null);
// 查询
const search = () => {
  let startTime = monthValue.value[0]
  let endTime = monthValue.value[1]

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择上传日期范围！', 'error', 5000, true);
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaId: areaTreeValue.areaId,
    areaLevel: areaTreeValue.selectAreaLevel,
    areaName: areaTreeValue.areaName,
    endTime: endTime,
    dataType: dataType.value,//(01:基本信息, 02:接种记录)
    startTime: startTime,
  }
  searchData.value = params;
  queryUploadNoPackageList(params).then(res => {
    if (res.data.popMsg) {
      tableData.value = [];
      secTime.value = "";
      total.value = 0;
      return globalProxy.$util.showMsg(res.data.popMsg, 'error');
    }
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
      total.value = res.data.data.total;
      secTime.value = res.data.sec;
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node
}
// 导出excel
const exportExcelFun = () => {
  // 分页导出
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  if (tableData.value && tableData.value.length == 0) {
    return globalProxy.$util.showMsg('暂无需要导出的数据！', 'error', 5000, true);
  }
  // 分页
  exportDialogVisible.value = true;
}
const exportExcel = () => {
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.areaName = params.areaName + " " + params.areaCode.slice(0, 10);
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  outputUploadNoPackageList(params)
}
const handleExportClose = () => {
  exportDialogVisible.value = false
}
// 点击弹框查看明细
const openDetailDialog = (messageType, childCount) => {
  searchData.messageType = messageType;
  searchData.childCount = childCount;
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
</script>
<style lang='scss'>
.nopack .el-table__inner-wrapper::before {
  bottom: 1px !important;
}
</style>