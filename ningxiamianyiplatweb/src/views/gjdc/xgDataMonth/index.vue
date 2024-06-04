<template>
  <!-- 国家督察-按月提供新冠疫苗数据 -->
  <div>
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label">选择地区：</div>
          <ss-area-areaTreeStation :getToLevelNum="2 - store.currentLevel" v-model="areaTreeValue"
            @nodeClick="nodeClick" />
        </div>
        <div class="item">
          <div class="label">导出月份：</div>
          <ss-date-picker :value="selectDate" :disabled-date="pickerOptions" :valueFormat="'YYYY-MM'" :dateType="'month'"
            @input="dateChange" />
        </div>
      </div>
      <ss-query-bottom :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-pagination :total="total" :pageIndex="pageNum" :pageSize="pageSize" @pageChange="goPage"></ss-pagination>
    <ss-table :data="tableData" ref="tabPage">
      <el-table-column align="center" type="index" width="60" label="序号" fixed>
        <template #default="scope">
          {{ (pageNum - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="fchildno" label="预防接种档案编号" min-width="180" />
      <el-table-column prop="name" min-width="160" label="受种者姓名" />
      <el-table-column prop="fidCardTypeText" label="身份证件类别" min-width="180" />
      <el-table-column prop="fidCardType" label="身份证件类别代码" width="150" />
      <el-table-column prop="cid" label="身份证件号码" min-width="160" />
      <el-table-column prop="sexCn" label="性别" min-width="70" />
      <el-table-column prop="sexCodeCn" label="性别代码" min-width="90" />
      <el-table-column prop="birth" label="受种者出生时间" min-width="140" />
      <el-table-column prop="nationName" label="国籍" min-width="100" />
      <el-table-column prop="fnationAlityCode" label="国籍代码" min-width="90" />
      <el-table-column prop="crowdKindCnName" label="人群分类" min-width="280" show-overflow-tooltip />
      <el-table-column prop="fcrowdKindCode" label="人群分类代码" min-width="120" />
      <el-table-column prop="jzrq" label="疫苗接种时间" min-width="170" />
      <el-table-column prop="jc" label="疫苗剂次" min-width="90" />
      <el-table-column prop="compShortNameCn" label="疫苗生产企业" min-width="120" />
      <el-table-column prop="compGjCode" label="疫苗生产企业代码" min-width="160" />
      <el-table-column prop="ymph" label="疫苗批号" min-width="170" />
      <el-table-column prop="fmonitorCode" label="疫苗追溯码" min-width="200" />
      <el-table-column prop="stationNameCn" label="接种单位" min-width="200" show-overflow-tooltip />
      <el-table-column prop="stationCodeCn" label="接种单位编码" min-width="130" />
      <el-table-column prop="counNameCn" label="区县名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="counCodeCn" label="区县编码" min-width="120" />
      <el-table-column prop="cityNameCn" label="地市名称" min-width="120" show-overflow-tooltip />
      <el-table-column prop="cityCodeCn" label="地市编码" min-width="120" />
    </ss-table>
  </div>
</template>
<script setup>
import { ElMessage, ElMessageBox } from "element-plus";
import { getVacInfoMonthlyData, exportVacInfoMonthlyData } from '@/api/apis/gjdc'
import { mainStore } from "@/store/index";
const store = mainStore();

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const tableData = ref([]);
const tabPage = ref(null);
const secTime = ref(0);
const total = ref(0);
const searchParams = ref(null);
const searchData = ref(null);
const pageNum = ref(1);
const pageSize = ref(20)
const selectDate = ref(globalProxy.$dateUtil.lastMonth())
// 地区
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});

const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
};
const pickerOptions = (time) => {
  return (
    time.getTime() < new Date(globalProxy.$consts.MINSELECTTIME).getTime() ||
    time.getTime() > new Date(globalProxy.$dateUtil.lastMonth()).getTime()
  )
}
const dateChange = (value) => {
  selectDate.value = value
}
const search = () => {
  searchParams.value = {
    pageNum: !pageNum.value ? 1 : pageNum.value,
    date: selectDate.value,
    areaCode: areaTreeValue.areaCode,
    areaName: areaTreeValue.areaName,
    pageSize: 20,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  searchData.value = JSON.parse(JSON.stringify(searchParams.value))
  initQuery()
}
const goPage = (page) => {
  pageNum.value = page
  searchParams.value.pageNum = !page ? 1 : page
  getList(2)
}
const getList = (flag) => {
  searchParams.value.flag = flag
  getVacInfoMonthlyData({ ...searchParams.value })
    .then(res => {
      tableData.value = res.data.data.list
      total.value = res.data.data.total
      secTime.value = res.data.sec
    })
    .catch(() => {
    })
}
const initQuery = () => {
  getVacInfoMonthlyData({ ...searchParams.value, flag: '' })
    .then(res => {
      if (res.data.msg === '-1') {
        ElMessage.info('查询将花费数分钟,已提交后台统计！')
        getList('rpt')
      } else if (res.data.msg === '0') {
        ElMessage.warning('正在进行后台统计, 请稍后查询 ')
      } else {
        const time = res.data.msg.split(',')[0]
        ElMessageBox.confirm(
          `${time}进行过统计, 是否直接查看？\n(点击取消将重新查询)`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true
          }
        )
          .then(() => {
            getList('query')
          })
          .catch(() => {
            ElMessage.info('查询将花费数分钟,已提交后台统计！')
            getList('rpt')
          })
      }
    })
    .catch(() => {
    })
}
const exportExcel = () => {
  if (!searchData.value) {
    ElMessage.error('查询后才可以导出！')
    return
  }
  if (tableData.value.length === 0) {
    ElMessage.error('暂无需要导出的数据！')
    return
  }
  let params = {
    ...searchData.value,
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  exportVacInfoMonthlyData(params)
}
</script>
<style scoped></style>
