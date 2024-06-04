<!-- 国家督察-国家数据上传错误统计表 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">上传日期：</span>
          <ss-date-range-new ref="daterangeRef" v-model:defaultValue="monthValue" rangeName="weeks"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="required label">选择地区：</span>
          <ss-area-areaTreeStation ref="areaTreeStation" v-model="areaTreeValue" :getToLevelNum="5" :showSelect="false"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
      </div>
      <ss-query-bottom tipText="国家上传错误数据按归属单位统计。" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" @openDetailDialog="openDetailDialog" :total="total"
      remarkText="1、系统当天的数据，在17点进行汇总，汇总上传时间截止到16点前的更新个案情况。同时更新前一天上传档案对应的上传成功档案数、上传失败档案数、国家无返回档案数。<br>2、系统非当天的数据，在第2天凌晨进行汇总。"></ss-custom-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :searchData="searchData"></dialog-info>
    <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'未上传接种单位统计'"
      :dialogVisible="exportDialogVisible"></ss-export-dialog>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { queryUploadWrongList, outputUploadWrongList } from "@/api/apis/gjdc";
import { mainStore } from "@/store/index";
import dialogInfo from "./dialogInfo.vue";
const store = mainStore();
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
const secTime = ref(0);
const dialogVisible = ref(false);
const columnList = reactive([
  {
    label: '序号',
    prop: 'id',
  },
  {
    label: '错误编码',
    prop: 'status',
  },
  {
    label: '错误描述',
    prop: 'message',
  },
  {
    label: '数量',
    prop: 'totalCount',
    type: 'clickable'
  }
])
const searchData = ref(null);
const exportDialogVisible = ref(null);
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(1), globalProxy.$dateUtil.dayAddNum(1)])
// 上传日期ref
const daterangeRef = ref(null);//提示验证时使用
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
});
// 查询
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  let startTime = monthValue.value[0]
  let endTime = monthValue.value[1]

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择日期范围！', 'error', 5000, true);
  }
  // if (!daterangeRef.value.SevenVerify()) return;
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaId: areaTreeValue.areaId,
    areaLevel: areaTreeValue.selectAreaLevel,
    areaName: areaTreeValue.areaName,
    endTime: endTime,
    startTime: startTime,
    pageNum: pageIndex.value,
    pageSize: globalProxy.$consts.PAGE_SIZE,
  }
  searchData.value = params;

  queryUploadWrongList(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.list
      total.value = res.data.data.total
      secTime.value = res.data.sec
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
  // params.selectAreaName = params.selectAreaName + params.newBaseCode
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  outputUploadWrongList(params)
}
const handleExportClose = () => {
  exportDialogVisible.value = false
}
// 点击弹框查看明细
const openDetailDialog = (params) => {
  let item = tableData.value.filter(item => item.id == params)[0]
  searchData.message = item.message;
  searchData.status = item.status;
  searchData.totalCount = item.totalCount;
  searchData.id = item.id;
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
</script>
<style scoped lang='scss'></style>