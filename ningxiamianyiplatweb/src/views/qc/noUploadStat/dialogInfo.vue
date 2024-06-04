<!-- 质量控制-上传数据质量统计 -->
<template>
  <!-- style="max-height:90%;overflow-y: hidden;margin-top: 50px;" -->
  <el-dialog title="上传数据质量统计明细表" v-model="visible" width="1200" :before-close="close" :close-on-click-modal="false"
    class="userManageFormDialog" :append-to-body="true">
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"></ss-pagination>
    <div style="position: absolute;top: 92px;right: 40px;" v-if="total">
      <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
    </div>
    <div style="margin: -16px;">
      <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
    </div>
  </el-dialog>
  <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'上传数据质量统计明细表'"
    :dialogVisible="exportDialogVisible"></ss-export-dialog>
</template>
<script setup>
import { detailList, detailOutput } from "@/api/apis/qc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  detailData: {
    type: Object,
    default: {}
  },
})
//表格列
const columnList = reactive([
  {
    label: '序号',
    type: 'index',
  },
  {
    label: '接种单位名称',
    minWidth: '250',
    overflowTip: true,
    formatter: function (row) {
      return row.cityName + "、" + row.countyName + "、" + row.stationName
    }
  },
  {
    label: '接种单位编码',
    prop: 'stationCode',
    minWidth: '120',
    overflowTip: true,
  },
  {
    label: '接种单位地址',
    prop: 'stationAddr',
    minWidth: '150',
    overflowTip: true,
  },
  {
    label: '接种单位电话',
    prop: 'linkPhone',
    minWidth: '140',
    overflowTip: true,
  },
  {
    label: '接种单位联系人',
    prop: 'linkman',
    minWidth: '140',
    overflowTip: true,
  },
])
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
const pageSize = ref(20)
const exportDialogVisible = ref(false)

const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    areaCode: props.detailData.areaCode,
    overDay: props.detailData.overDay,
    pageNum: pageIndex.value
  }
  detailList(params).then(res => {
    tableData.value = res.data.data.list
    total.value = res.data.data.total
  })
}

watch(() => props.visible, (val) => {
  if (val) {
    search()
  };
}, { deep: true, immediate: true })

const emits = defineEmits(['close']);
// 取消
const close = () => {
  emits('close', true);
}

// 导出excel
const exportExcelFun = () => {
  exportDialogVisible.value = true
}
const exportExcel = (pageNum) => {
  let params = {
    areaCode: props.detailData.areaCode,
    overDay: props.detailData.overDay,
    areaName: props.detailData.areaName,
    pageNum: pageNum,
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  detailOutput(params)
}
const handleExportClose = () => {
  exportDialogVisible.value = false;
}
</script>
<style>
.xgRptZL-table .el-table--border::after {
  right: 1px;
}
</style>