<!-- 国家督察-新冠病毒疫苗接种记录质量控制报表  详细 -->
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
      <el-table-column align="center" type="index" min-width="60" label="序号" fixed>
        <template #default="scope">
          {{ (pageIndex - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="fchildno" align="center" min-width="200" label="预防接种档案编号" />
      <el-table-column prop="name" align="center" min-width="150" label="受种者姓名" />
      <el-table-column prop="fidCardTypeText" align="center" min-width="120" label="身份证件类别" />
      <el-table-column prop="fidCardType" align="center" min-width="150" label="身份证件类别代码" />
      <el-table-column prop="cid" align="center" min-width="160" label="身份证件号码" />
      <el-table-column prop="sexCn" align="center" min-width="100" label="性别" />
      <el-table-column prop="sexCodeCn" align="center" min-width="90" label="性别代码" />
      <el-table-column prop="birth" align="center" min-width="140" label="受种者出生时间" />
      <el-table-column prop="nationName" align="center" min-width="100" label="国籍" />
      <el-table-column prop="fnationAlityCode" align="center" min-width="100" label="国籍代码" />
      <el-table-column prop="crowdKindCnName" align="center" min-width="200" label="人群分类" show-overflow-tooltip />
      <el-table-column prop="fcrowdKindCode" align="center" min-width="120" label="人群分类代码" />
      <el-table-column prop="jzrq" align="center" min-width="170" label="疫苗接种时间" />
      <el-table-column prop="jc" align="center" min-width="90" label="疫苗剂次" />
      <el-table-column prop="compShortNameCn" align="center" min-width="120" label="疫苗生产企业" />
      <el-table-column prop="compGjCode" align="center" min-width="150" label="疫苗生产企业代码" />
      <el-table-column prop="ymph" align="center" min-width="100" label="疫苗批号" />
      <el-table-column prop="fmonitorCode" align="center" min-width="200" label="疫苗追溯码" />
      <el-table-column prop="stationNameCn" align="center" min-width="220" label="接种单位" />
      <el-table-column prop="stationCodeCn" align="center" min-width="200" label="接种单位编码" />
      <el-table-column prop="counNameCn" align="center" min-width="100" label="区县名称" />
      <el-table-column prop="counCodeCn" align="center" min-width="100" label="区县编码" />
      <el-table-column prop="cityNameCn" align="center" min-width="100" label="地市名称" />
      <el-table-column prop="cityCodeCn" align="center" min-width="100" label="地市编码" />
    </ss-table>
    <!-- 分页导出 -->
    <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total"
      :typeStr="'新冠病毒疫苗接种记录质量控制报表详细列表'" :dialogVisible="exportDialogVisible"></ss-export-dialog>
  </el-dialog>
</template>
<script setup>
import { ElMessage } from "element-plus";
import { showDetailXgRptInfoFullJzjlfull, outputXgRptInfoFullJzjlfullDetail } from "@/api/apis/gjdc";
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
  showDetailXgRptInfoFullJzjlfull(params).then(res => {
    tableData.value = res.data.data.list
    total.value = res.data.data.total
  })
}

watch(() => props.detailData.areaCode, (val) => {
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
    areaName: props.detailData.areaName,
    pageNum: pageIndex.value,
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  outputXgRptInfoFullJzjlfullDetail(params)
}
</script>
<style>
.xgRptZL-table .el-table--border::after {
  right: 1px;
}
</style>