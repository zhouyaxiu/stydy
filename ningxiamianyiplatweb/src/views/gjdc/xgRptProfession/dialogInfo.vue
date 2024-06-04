<!-- 国家督察-按职业分类的新冠病毒疫苗接种信息汇总表 -->
<template>
  <el-dialog title="按职业分类的新冠病毒疫苗接种信息汇总表" v-model="visible" width="1200" :before-close="close"
    :close-on-click-modal="false" class="userManageFormDialog" :append-to-body="true">
    <!-- <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"></ss-pagination> -->
    <div style="position: absolute;top: 92px;right: 40px;" v-if="total">
      <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
    </div>
    <div style="margin:-16px">
      <ss-custom-table :tableData="tableData" :columnList="columnList" :total="total"></ss-custom-table>
    </div>
  </el-dialog>
  <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total"
    :typeStr="'按职业分类的新冠病毒疫苗接种信息汇总表'" :dialogVisible="exportDialogVisible"></ss-export-dialog>
</template>
<script setup>
import { proQueryDetail, proOutputDetail } from "@/api/apis/gjdc";
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
  detailOtherData: {
    type: Object,
    default: {}
  },
})
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
const exportDialogVisible = ref(false);
const columnList = reactive([
  {
    label: '序号',
    minWidth: '60',
    type: "index"
  },
  {
    label: '姓名',
    prop: 'name',
    minWidth: '100',
    overflowTip: true
  },
  {
    label: '性别',
    prop: 'sex',
    minWidth: '100'
  },
  {
    label: '出生日期',
    prop: 'birth',
    minWidth: '100'
  },
  {
    label: '工作单位',
    prop: 'fchildUnitName',
    minWidth: '100'
  },
  {
    label: '职业分类',
    prop: 'professionName',
    minWidth: '130',
    overflowTip: true
  },
  {
    label: '联系电话',
    prop: 'mobPhone',
    minWidth: '130',
    overflowTip: true
  },
  {
    label: '第1剂次',
    hasChildrenTitle: true,
    list: [
      {
        label: '接种日期',
        prop: 'jzrq1',
        minWidth: '100'
      },
      {
        label: '接种人员',
        prop: 'jzys1',
        minWidth: '100',
        overflowTip: true
      },
      {
        label: '接种单位',
        prop: 'fstationName1',
        minWidth: '130',
        overflowTip: true
      }
    ]
  },
  {
    label: '第2剂次',
    hasChildrenTitle: true,
    list: [
      {
        label: '接种日期',
        prop: 'jzrq2',
        minWidth: '100'
      },
      {
        label: '接种人员',
        prop: 'jzys2',
        minWidth: '100',
        overflowTip: true
      },
      {
        label: '接种单位',
        prop: 'fstationName2',
        minWidth: '130',
        overflowTip: true
      }
    ]
  },
  {
    label: '第3剂次',
    hasChildrenTitle: true,
    list: [
      {
        label: '接种日期',
        prop: 'jzrq3',
        minWidth: '100'
      },
      {
        label: '接种人员',
        prop: 'jzys3',
        minWidth: '100',
        overflowTip: true
      },
      {
        label: '接种单位',
        prop: 'fstationName3',
        minWidth: '130',
        overflowTip: true
      }
    ]
  },
  {
    label: '第4剂次',
    hasChildrenTitle: true,
    list: [
      {
        label: '接种日期',
        prop: 'jzrq4',
        minWidth: '100'
      },
      {
        label: '接种人员',
        prop: 'jzys4',
        minWidth: '100',
        overflowTip: true
      },
      {
        label: '接种单位',
        prop: 'fstationName4',
        minWidth: '130',
        overflowTip: true
      }
    ]
  },
  {
    label: '第5剂次',
    hasChildrenTitle: true,
    list: [
      {
        label: '接种日期',
        prop: 'jzrq5',
        minWidth: '100'
      },
      {
        label: '接种人员',
        prop: 'jzys5',
        minWidth: '100'
      },
      {
        label: '接种单位',
        prop: 'fstationName5',
        minWidth: '130',
        overflowTip: true
      }
    ]
  },
])
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    ...props.detailData,
    pageIndex: pageIndex.value
  }
  proQueryDetail(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.data.list;
      total.value = res.data.data.data.total;
    } else {
      if (res.data.popMsg) {
        secTime.value = "";
        tableData.value = [];
        return globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
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
  exportDialogVisible.value = true;
}
const exportExcel = (pageNum) => {
  let params = {
    ...props.detailData,
    ...props.detailOtherData,
    pageIndex: pageNum,
    areaName: props.detailOtherData.areaName + " " + props.detailData.areaCode.slice(0, 10),
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  proOutputDetail(params);
}

const handleExportClose = () => {
  exportDialogVisible.value = false;
}
</script>