<!-- 新冠病毒疫苗基本信息质量控制报表 详细列表 -->
<template>
  <el-dialog title="详细列表" v-model="visible" width="70%" :before-close="close" :close-on-click-modal="false"
    class="userManageFormDialog" :append-to-body="true">
    <div>
      <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"
        style="width: calc(100% - 130px);float: left;"></ss-pagination>
      <ss-custom-button :type="'output'" @click="exportExcelFun"
        style="display: inline-block;float: right;margin-right: 16px;"></ss-custom-button>
    </div>
    <ss-table :data="tableData" :maxHeight="400" class="xgRptZL-table">
      <el-table-column align="center" type="index" min-width="60" label="序号">
        <template #default="scope">
          {{ (pageIndex - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="fchildno" align="center" min-width="200" label="预防接种档案编号" />
      <el-table-column prop="name" align="center" min-width="150" label="姓名" />
      <el-table-column prop="sex" align="center" min-width="55" label="性别" />
      <el-table-column prop="birth" align="center" min-width="120" label="出生日期" />
      <el-table-column prop="stationName" align="center" min-width="200" label="所属接种点名称" show-overflow-tooltip />
      <el-table-column prop="newBaseCode" align="center" min-width="120" label="客户端识别码" />
      <el-table-column prop="bookingDate" align="center" width="170" label="登记日期" />
      <el-table-column prop="nationality" align="center" min-width="100" label="国籍" />
      <el-table-column prop="crowdkind" align="center" min-width="200" label="人群分类" show-overflow-tooltip />
      <el-table-column prop="cidtype" align="center" min-width="100" label="证件类型" />
      <el-table-column prop="cid" align="center" width="150" label="身份证件号" />
    </ss-table>
    <!-- 分页导出 -->
    <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total"
      :typeStr="'新冠病毒疫苗基本信息质量控制报表详细列表'" :dialogVisible="exportDialogVisible"></ss-export-dialog>
  </el-dialog>
</template>
<script setup>
import { ElMessage } from "element-plus";
import { showDetailXgRptInfoFullInfofull, outputXgRptInfoFullInfofullDetail } from "@/api/apis/gjdc";
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
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
const pageSize = ref(20)
const searchData = ref({})
const exportDialogVisible = ref(false)

const search = (pageNum) => {
  if (!props.detailData.areaCode || props.detailData.areaCode == '') return false
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    ...props.detailData,
    pageNum: pageIndex.value,
    operateType: globalProxy.$consts.OPTYPE_QUERY
  }
  searchData.value = params
  showDetailXgRptInfoFullInfofull(params).then(res => {
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
  if (!searchData.value) {
    ElMessage.error('查询后才可以导出！')
    return
  }
  if (tableData.value.length == 0) {
    ElMessage.error('暂无需要导出的数据！')
    return
  }
  exportDialogVisible.value = true;
}
const handleExportClose = () => {
  exportDialogVisible.value = false
}

const exportExcel = () => {
  let params = {
    ...props.detailData,
    areaCode: props.detailData.areaCode,
    overDay: props.detailData.overDay,
    areaName: props.detailData.areaName,
    pageNum: pageIndex.value,
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  outputXgRptInfoFullInfofullDetail(params)
}
</script>
<style>
.xgRptZL-table .el-table--border::after {
  right: 1px;
}
</style>