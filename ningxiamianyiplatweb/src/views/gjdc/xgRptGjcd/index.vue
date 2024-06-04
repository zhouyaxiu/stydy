<!-- 国家督察-国家重档数据查询 -->
<template>
  <div>
    <ss-condition-area>
      <div class="flex">
        <span class="label">身份证件号码：</span>
        <el-input v-model="idcards" style="max-width:300px;"></el-input>
        <span class="label">（最多允许输入10个，中间逗号隔开）</span>
      </div>
      <div class="mt10">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
      </div>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
    <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'国家重档数据查询导出'"
      :dialogVisible="exportDialogVisible"></ss-export-dialog>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { querySameRecordList, outputSameRecordList } from "@/api/apis/gjdc";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const tableData = ref([]);
const total = ref(0);
const idcards = ref('');
const exportDialogVisible = ref(false);
const columnList = reactive([
  {
    label: '档案预防接种档案编号',
    prop: 'fchildno',
    minWidth: '180',
  },
  {
    label: '省',
    prop: 'province',
  },
  {
    label: '接种单位',
    prop: 'currentManagementCodeName',
    minWidth: "220",
    overflowTip: true
  },
  {
    label: '姓名',
    prop: 'name',
  },
  {
    label: '性别',
    prop: 'genderCode',
  },
  {
    label: '身份证件号码',
    prop: 'idCard',
    minWidth: "180"
  },
  {
    label: '联系电话',
    prop: 'tel',
    minWidth: "120"
  },
  {
    label: '接种日期',
    prop: 'jzrq',
    minWidth: "160"
  },
  {
    label: '接种剂次',
    prop: 'seq',
    minWidth: "100"
  },
  {
    label: '部位',
    prop: 'bdpart',
  },
  {
    label: '追溯码',
    prop: 'zsm',
    minWidth: 200,
    overflowTip: true
  },
  {
    label: '疫苗种类',
    prop: 'ymName',
    minWidth: "160",
    overflowTip: true
  },
  {
    label: '疫苗编码',
    prop: 'bactid',
    minWidth: "100"
  },
  {
    label: '批号',
    prop: 'ymph',
    minWidth: "120"
  },
  {
    label: '有效期',
    prop: 'yxq',
    minWidth: "120"
  },
  {
    label: '接种单位',
    prop: 'jzdwbmmc',
    minWidth: "220",
    overflowTip: true
  },
  {
    label: '接种单位编码',
    prop: 'jzdwbmbm',
    minWidth: "140"
  },
  {
    label: '接种人员',
    prop: 'jzys',
    minWidth: "120"
  },
  {
    label: '接种途径',
    prop: 'tj',
    minWidth: "120"
  },
  {
    label: '录入单位',
    prop: 'lrdwbmmc',
    minWidth: "220",
    overflowTip: true
  },
  {
    label: '更新单位',
    prop: 'gxdwbmmc',
    minWidth: "220",
    overflowTip: true
  },
  {
    label: '上传时间',
    prop: 'uploadDate',
    minWidth: "170"
  },
  {
    label: '更新时间',
    prop: 'updateDate',
    minWidth: "170"
  },
  {
    label: '疫苗属性',
    prop: 'ymsx',
    minWidth: "120"
  },
  {
    label: '价格',
    prop: 'price',
  },
  {
    label: '疫苗类型',
    prop: 'ymtype',
    minWidth: "100"
  },
  {
    label: '企业',
    prop: 'factory',
    minWidth: "130"
  }
])
const searchData = ref(null);
// 查询
const search = () => {
  let len = new Set(idcards.value.split(",")).size;
  if (!idcards.value) {
    return globalProxy.$util.showMsg("身份证件号码必填！", 'error')
  }
  if (len > 10) {
    return globalProxy.$util.showMsg("最多允许输入10个！", 'warning')
  }
  const params = {
    idcards: idcards.value,
  }
  searchData.value = params;
  querySameRecordList(params).then(res => {
    if (res.data.code == 200) {
      tableData.value = res.data.data.list;
      total.value = res.data.data.total;
    } else {
      if (res.data.popMsg) {
        tableData.value = [];
        total.value = 0;
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
// 导出excel
const exportExcelFun = () => {
  if (!searchData.value) {
    ElMessage.error('查询后才可以导出！')
    return
  }
  if (tableData.value.length === 1) {
    ElMessage.error('暂无需要导出的数据！')
    return
  }
  // 分页
  exportDialogVisible.value = true;
}
const exportExcel = () => {
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  outputSameRecordList(params)
}
const handleExportClose = () => {
  exportDialogVisible.value = false
}
</script>
<style scoped lang='scss'></style>