<!-- 质量控制-上传数据质量统计 -->
<template>
  <el-dialog title="儿童资料列表" v-model="visible" width="1200" :before-close="close" :close-on-click-modal="false"
    class="userManageFormDialog" :append-to-body="true">
    <ss-custom-table :tableData="tableData" :columnList="columnList" :total="total" :changeRowBg="true"></ss-custom-table>
    <div>在册儿童：颜色标注蓝色；单纯迁出儿童：颜色标注绿色。</div>
  </el-dialog>
</template>
<script setup>
import { getColumnList, ColumnDetailList } from "@/api/apis/qc";
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
const pageSize = ref(20);

const columnList = ref([]);

const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum;
  }
  const params = {
    pageNum: pageIndex.value,
    ...props.detailData,
  }
  columnList.value = [];
  getColumnList(params).then(res => {
    let flist = res.data.data.flist.map(item => {
      if (item.fieldCNName.length > 3) item.minWidth = 140; item.overflowTip = true;
      if (item.fieldCNName.length > 6) item.minWidth = 180; item.overflowTip = true;
      return { label: item.fieldCNName, prop: item.fieldName, minWidth: item.minWidth || 100, overflowTip: item.overflowTip }
    });
    let alist = res.data.data.alist.map(item => {
      if (item.fieldCNName.length > 3) item.minWidth = 140; item.overflowTip = true;
      if (item.fieldCNName.length > 6) item.minWidth = 180; item.overflowTip = true;
      return { label: item.fieldCNName, prop: item.fieldName, minWidth: item.minWidth || 100, overflowTip: item.overflowTip }
    });
    columnList.value.push(...flist, ...alist)
  })
  ColumnDetailList(params).then(res => {
    tableData.value = [];
    let arr = res.data.data.list;
    total.value = res.data.data.total;
    tableData.value = arr;
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
</script>