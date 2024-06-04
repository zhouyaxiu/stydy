<!-- 国家督察-新型冠状病毒疫苗及接种信息采集表 -->
<template>
  <div class="jzinfo">
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="false"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label required">接种日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label">身份证件类别：</span>
          <el-select v-model="fidCardType" clearable style="position: relative;" placeholder="请选择">
            <el-option v-for="item in fidCardTypeList" :key="item.code" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </div>
        <div class="item">
          <span class="label">性别：</span>
          <el-select v-model="sex" clearable style="position: relative;" placeholder="请选择">
            <el-option v-for="item in sexList" :key="item.code" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <span class="label">受种者出生时间范围：</span>
          <ss-date-range-new v-model:defaultValue="birthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label">接种剂次：</span>
          <el-select v-model="fbactid" clearable style="position: relative;">
            <el-option v-for="item in fbactidList" :key="item.code" :label="item.name" :value="item.code"></el-option>
          </el-select>
        </div>
        <div class="item">
          <span class="label">疫苗生产企业：</span>
          <ss-vaccine-vendor ref="selectedVaccineVendor"></ss-vaccine-vendor>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <span class="label">疫苗批号：</span>
          <el-input v-model="ymph" clearable></el-input>
        </div>
        <div class="item">
          <span class="label">追溯码：</span>
          <el-input v-model="fmonitorCode" clearable></el-input>
        </div>
      </div>
      <ss-query-bottom :statisTics="true" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="20" @pageChange="search"></ss-pagination>
    <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { jzQuery, jzOutput } from "@/api/apis/gjdc";

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);
const pageIndex = ref(1);
const total = ref(0);
const fidCardTypeList = globalProxy.$commonOptions.fidCardTypeList;
const sexList = globalProxy.$commonOptions.sexList;

const fidCardType = ref(""); //身份证件类别
//接种日期
const monthValue = ref([globalProxy.$dateUtil.lastMonthStartDay(), globalProxy.$dateUtil.lastMonthEndDay()]);
const sex = ref(""); //性别
const birthValue = ref([]);//受种者出生时间范围
const fbactid = ref(""); //接种剂次
const ymph = ref(""); //疫苗批号
const fmonitorCode = ref(""); //追溯码
const selectedVaccineVendor = ref(null);
//疫苗接种批次
const fbactidList = ref([
  { name: '1', code: 'B655' },
  { name: '2', code: 'B656' },
  { name: '3', code: 'B676' },
  { name: '4', code: 'B677' }
]);
const columnList = reactive([
  {
    label: '序号',
    type: 'index',
    minWidth: '80',
  },
  {
    label: '预防接种档案编号',
    prop: 'fchildno',
    minWidth: '180',
  },
  {
    label: '受种者姓名',
    prop: 'name',
    minWidth: '120',
    overflowTip: true
  },
  {
    label: '居委会',
    prop: 'jwh',
    minWidth: '150',
    overflowTip: true
  },
  {
    label: '身份证件类别',
    prop: 'fidCardTypeText',
    minWidth: '120',
  },
  {
    label: '身份证件类别代码',
    prop: 'fidCardType',
    minWidth: '150',
  },
  {
    label: '身份证件号码',
    prop: 'cid',
    minWidth: '150',
    overflowTip: true
  },
  {
    label: '性别',
    prop: 'sexCn',
    minWidth: '100',
  },
  {
    label: '性别代码',
    prop: 'sexCodeCn',
    minWidth: '90',
  },
  {
    label: '受种者出生时间',
    prop: 'birth',
    minWidth: '140',
  },
  {
    label: '国籍',
    prop: 'nationName',
    minWidth: '150',
    overflowTip: true
  },
  {
    label: '国籍代码',
    prop: 'fnationAlityCode',
    minWidth: '100',
  },
  {
    label: '人群分类',
    prop: 'crowdKindCnName',
    minWidth: '140',
    overflowTip: true
  },
  {
    label: '人群分类代码',
    prop: 'fcrowdKindCode',
    minWidth: '120',
  },
  {
    label: '疫苗接种时间',
    prop: 'jzrq',
    minWidth: '150',
    overflowTip: true
  },
  {
    label: '疫苗剂次',
    prop: 'jc',
    minWidth: '100',
  },
  {
    label: '疫苗生产企业',
    prop: 'compShortNameCn',
    minWidth: '120',
  },
  {
    label: '疫苗生产企业代码',
    prop: 'compGjCode',
    minWidth: '160',
  },
  {
    label: '疫苗批号',
    prop: 'ymph',
    minWidth: '140',
    overflowTip: true
  },
  {
    label: '疫苗追溯码',
    prop: 'fmonitorCode',
    minWidth: '160',
    overflowTip: true
  },
  {
    label: '接种单位',
    prop: 'stationNameCn',
    minWidth: '150',
    overflowTip: true
  },
  {
    label: '接种单位编码',
    prop: 'stationCodeCn',
    minWidth: '120',
  },
  {
    label: '区县名称',
    prop: 'counNameCn',
    minWidth: '100',
  },
  {
    label: '区县编码',
    prop: 'counCodeCn',
    minWidth: '100',
  },
  {
    label: '地市名称',
    prop: 'cityNameCn',
    minWidth: '100',
  },
  {
    label: '地市编码',
    prop: 'cityCodeCn',
    minWidth: '100',
  }
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName
});

// 查询
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  let startTime = monthValue.value[0];
  let endTime = monthValue.value[1];
  let birthstartTime = birthValue.value[0];
  let birthendTime = birthValue.value[1];

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择填报日期范围！', 'error', 5000, true);
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaLevel: areaTreeValue.selectAreaLevel,
    startJzDate: startTime,
    endJzDate: endTime,
    IDType: fidCardType.value,
    sex: sex.value,
    startBirthDate: birthstartTime,
    endBirthDate: birthendTime,
    jzJc: fbactid.value,
    fenterNo: selectedVaccineVendor.value.fenterNo,
    vaccBatch: ymph.value,
    fMonitorcode: fmonitorCode.value,
    pageIndex: pageIndex.value
  }
  let IDTypeName = fidCardType.value ? fidCardTypeList.filter(item => item.code == fidCardType.value)[0].name : "";
  let sexShow = sex.value ? sexList.filter(item => item.code == sex.value)[0].name : "";
  let jzJcShow = fbactid.value ? fbactidList.value.filter(item => item.code == fbactid.value)[0].name : "";
  let fenterName = selectedVaccineVendor.value.fenterNo ? selectedVaccineVendor.value.fenterName : "";
  searchData.value = {
    ...params,
    IDTypeName,
    sexShow,
    jzJcShow,
    fenterName,
  };
  jzQuery(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.data.list;
      total.value = res.data.data.data.total;
      secTime.value = res.data.sec;
    } else {
      if (res.data.popMsg) {
        secTime.value = "";
        tableData.value = [];
        total.value = 0;
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node;
}

// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value));
  params.areaName = areaTreeValue.areaName + " " + areaTreeValue.areaCode.slice(0, 10);
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  jzOutput(params)
}

onMounted(() => {
})
</script>
<style lang='scss'>
.jzinfo .el-table__inner-wrapper::before {
  bottom: 1px !important;
}
</style>