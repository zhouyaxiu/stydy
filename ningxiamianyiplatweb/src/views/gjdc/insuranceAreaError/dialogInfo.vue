<!-- 国家督察-新冠病毒疫苗医保错误数据统计（总表） -->
<template>
  <el-dialog title="新冠病毒疫苗医保错误数据统计（总表）详情" v-model="visible" width="1200" :before-close="close"
    :close-on-click-modal="false" class="customDialog" :append-to-body="true">
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"></ss-pagination>
    <div style="position: absolute;top: 92px;right: 40px;" v-if="total">
      <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
    </div>
    <div style="margin: -16px;">
      <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
    </div>
  </el-dialog>
  <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'国家数据不合格未上传统计'"
    :dialogVisible="exportDialogVisible"></ss-export-dialog>
</template>
<script setup>
import { queryUploadNoPackageDetailList, outputUploadNoPackageDetailList } from "@/api/apis/gjdc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  searchData: {
    type: Object,
    default: {}
  },
})
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
const columnList = reactive([
  {
    label: '序号',
    minWidth: '60',
    type: "index"
  },
  {
    label: '地市编码',
    prop: 'cityCode',
    minWidth: '100'
  },
  {
    label: '地市名称',
    prop: 'cityName',
    minWidth: '100'
  },
  {
    label: '县区编码',
    prop: 'countyCode',
    minWidth: '100'
  },
  {
    label: '县区名称',
    prop: 'countyName',
    minWidth: '100'
  },
  {
    label: '单位编码',
    prop: 'fstationCode',
    minWidth: '100'
  },
  {
    label: '单位名称',
    prop: 'fstationName',
    minWidth: '100'
  },
  {
    label: '受种者编码',
    prop: 'fchildno',
    minWidth: "130"
  },
  {
    label: '姓名',
    prop: 'name',
  },
  {
    label: '性别',
    prop: 'sex',
    type: props.searchData.dataType == '01' ? 'judge' : 'null'
  },
  {
    label: '出生日期',
    prop: 'birth',
    type: props.searchData.dataType == '01' ? 'judge' : 'null'
  },
  {
    label: '身份证件号码',
    prop: 'cid',
    type: props.searchData.dataType == '01' ? 'judge' : 'null'
  },
  {
    label: '疫苗剂次',
    prop: 'fbactid',
    type: props.searchData.dataType == '02' ? 'judge' : 'null'
  },
  {
    label: '接种日期',
    prop: 'jzrq',
    type: props.searchData.dataType == '02' ? 'judge' : 'null'
  },
  {
    label: '错误描述（message）',
    prop: 'errorDesc',
    minWidth: "180"
  },
  {
    label: '数据不合格类型',
    prop: 'messageType',
    minWidth: "150"
  },
])
const exportDialogVisible = ref(null);

const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    areaCode: props.searchData.areaCode,
    areaId: props.searchData.areaId,
    areaLevel: props.searchData.areaLevel,
    areaName: props.searchData.areaName,
    endTime: props.searchData.endTime,
    dataType: props.searchData.dataType,//(01:基本信息, 02:接种记录)
    startTime: props.searchData.startTime,
    childCount: props.searchData.childCount,
    messageType: props.searchData.messageType,
    pageNum: pageIndex.value,
    pageSize: globalProxy.$consts.PAGE_SIZE,
  }
  queryUploadNoPackageDetailList(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
      total.value = res.data.data.total;
    }
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
  // 分页导出
  if (!props.searchData || props.searchData.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  if (tableData.value && tableData.value.length == 0) {
    return globalProxy.$util.showMsg('暂无需要导出的数据！', 'error', 5000, true);
  }
  // 分页
  exportDialogVisible.value = true;
}
const exportExcel = () => {
  let params = JSON.parse(JSON.stringify(searchData.value));
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  outputUploadNoPackageDetailList(params);
}

const handleExportClose = () => {
  exportDialogVisible.value = false;
}


</script>