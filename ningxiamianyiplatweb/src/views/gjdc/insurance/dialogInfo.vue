<!-- 国家督察-新冠病毒疫苗医保正确数据统计表 -->
<template>
  <el-dialog title="新冠病毒疫苗医保正确数据统计表详情" v-model="visible" width="1200" :before-close="close" :close-on-click-modal="false"
    class="customDialog" :append-to-body="true">
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"></ss-pagination>
    <div style="position: absolute;top: 92px;right: 40px;" v-if="total">
      <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
    </div>
    <div style="margin: -16px;">
      <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
    </div>
  </el-dialog>
  <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'新冠病毒疫苗医保正确数据统计表'"
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
    label: '预防接种档案编号',
    prop: 'fchildno',
    minWidth: '100'
  },
  {
    label: '受种者姓名',
    prop: 'name',
    minWidth: '100'
  },
  {
    label: '身份证件类别',
    prop: 'fidCardTypeText',
    minWidth: '100'
  },
  {
    label: '身份证件类别代码',
    prop: 'fidCardType',
    minWidth: '100'
  },
  {
    label: '身份证件号码',
    prop: 'cid',
    minWidth: '100'
  },
  {
    label: '性别',
    prop: 'sexCn',
    minWidth: '100'
  },
  {
    label: '性别代码',
    prop: 'sexCodeCn',
    minWidth: "130"
  },
  {
    label: '受种者出生时间',
    prop: 'birth',
  },
  {
    label: '国籍',
    prop: 'nationName',
  },
  {
    label: '国籍代码',
    prop: 'fnationNalityCode',
  },
  {
    label: '人群分类',
    prop: 'crowdKindCnName',
  },
  {
    label: '人群分类代码',
    prop: 'fcrowdKindCode',
  },
  {
    label: '疫苗接种时间',
    prop: 'jzrq',
  },
  {
    label: '疫苗剂次',
    prop: 'jc',
    minWidth: "180"
  },
  {
    label: '疫苗生产企业',
    prop: 'compShortNameCn',
    minWidth: "150"
  },
  {
    label: '疫苗生产企业代码',
    prop: 'compGjCode',
    minWidth: "150"
  },
  {
    label: '疫苗批号',
    prop: 'ymph',
    minWidth: "150"
  },
  {
    label: '疫苗追溯码',
    prop: 'fmonitorCode',
    minWidth: "150"
  },
  {
    label: '接种单位',
    prop: 'stationNameCn',
    minWidth: "150"
  },
  {
    label: '接种单位编码',
    prop: 'stationCodeCn',
    minWidth: "150"
  },
  {
    label: '区县名称',
    prop: 'counNameCn',
    minWidth: "150"
  },
  {
    label: '区县编码',
    prop: 'counCodeCn',
    minWidth: "150"
  },
  {
    label: '地市名称',
    prop: 'cityNameCn',
    minWidth: "150"
  },
  {
    label: '地市编码',
    prop: 'cityCodeCn',
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
  // params.selectAreaName = params.selectAreaName + params.newBaseCode
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  outputUploadNoPackageDetailList(params);
}

const handleExportClose = () => {
  exportDialogVisible.value = false;
}


</script>