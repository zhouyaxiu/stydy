<!-- 国家督察-按人群分类的新冠病毒疫苗接种信息汇总表 -->
<template>
  <div class="crowed">
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">填报单位：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="false"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label required">填报日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label required">数据来源：</span>
          <ss-source-data :sourceData="sourceData" @update:modelValue="value => sourceData = value"></ss-source-data>
          <!-- <el-select v-model="sourceId" clearable style="position: relative;">
            <el-option v-for="item in sourceList" :key="item.code" :label="item.name" :value="item.code"></el-option>
          </el-select> -->
        </div>
        <div class="item">
          <span class="label">疫苗生产企业：</span>
          <ss-vaccine-vendor ref="selectedVaccineVendor"></ss-vaccine-vendor>
        </div>
      </div>
      <ss-query-bottom :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"
      @openDetailDialog="openDetailDialog"></ss-custom-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :detailData="detailData"
      :detailOtherData="detailOtherData"></dialog-info>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { crowQuery, crowOutput } from "@/api/apis/gjdc";
import dialogInfo from "./dialogInfo.vue";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);
const sourceData = ref({ code: "", name: "全部" });

const columnList = reactive([
  {
    label: '接种人群',
    prop: 'pageName',
    minWidth: '250',
    overflowTip: true
  },
  {
    label: '摸底总人数',
    prop: 'md',
    minWidth: '110',
  },
  {
    label: '合计',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj',
        minWidth: '120'
      },
      {
        label: '当日接种人数',
        prop: 'dr',
        minWidth: '120'
      }
    ]
  },
  {
    label: '第一剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj1',
        minWidth: '120',
        type: "clickable"
      },
      {
        label: '当日接种人数',
        prop: 'dr1',
        minWidth: '120',
        type: "clickable"
      }
    ]
  },
  {
    label: '第二剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj2',
        minWidth: '120',
        type: "clickable"
      },
      {
        label: '当日接种人数',
        prop: 'dr2',
        minWidth: '120',
        type: "clickable"
      }
    ]
  },
  {
    label: '第三剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj3',
        minWidth: '120',
        type: "clickable"
      },
      {
        label: '当日接种人数',
        prop: 'dr3',
        minWidth: '120',
        type: "clickable"
      }
    ]
  },
  {
    label: '第四剂',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj4',
        minWidth: '120',
        type: "clickable"
      },
      {
        label: '当日接种人数',
        prop: 'dr4',
        minWidth: '120',
        type: "clickable"
      }
    ]
  },
  {
    label: '第五剂（未感染）',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj5',
        minWidth: '120',
        type: "clickable",
      },
      {
        label: '当日接种人数',
        prop: 'dr5',
        minWidth: '120',
        type: "clickable"
      }
    ]
  },
  {
    label: '第六剂（未感染）',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'lj5',
        minWidth: '120',
        type: "clickable",
      },
      {
        label: '当日接种人数',
        prop: 'dr5',
        minWidth: '120',
        type: "clickable"
      }
    ]
  },
  {
    label: '已感染人群',
    hasChildrenTitle: true,
    list: [
      {
        label: '累计接种人数',
        prop: 'ygrlj',
        minWidth: '120'
      },
      {
        label: '当日接种人数',
        prop: 'ygrdr',
        minWidth: '120'
      }
    ]
  },
]);
const searchData = ref(null);
const detailData = ref(null);
const detailOtherData = ref(null);

const dialogVisible = ref(false);

let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName
});

const selectedVaccineVendor = ref(null);
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
// 查询
const search = () => {
  let startTime = monthValue.value[0];
  let endTime = monthValue.value[1];

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择填报日期范围！', 'error', 5000, true);
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaLevel: areaTreeValue.selectAreaLevel,
    startDate: startTime,
    endDate: endTime,
    fenterNo: selectedVaccineVendor.value.fenterNo,
    sourceFrom: sourceData.value.code
  }
  searchData.value = params;
  // let sourceName = sourceList.filter(item => item.code == sourceId.value)[0].name;
  detailOtherData.value = {
    sourceFromName: sourceData.value.name,
    fenterName: selectedVaccineVendor.value.fenterName,
    areaName: areaTreeValue.areaName
  }
  crowQuery(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.data;
      secTime.value = res.data.data.sec;
    } else {
      if (res.data.popMsg) {
        tableData.value = [];
        secTime.value = "";
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
}

// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  params = {
    ...params,
    ...detailOtherData.value,
    areaName: areaTreeValue.areaName + " " + areaTreeValue.areaCode.slice(0, 10)
  }
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  crowOutput(params)
}
// 点击弹框查看明细
const openDetailDialog = (row, prop) => {
  detailData.value = {
    ...searchData.value,
    crowdKindCode: row.pageName == '合计' ? "-1" : row.crowdKindCode,
    fbactid: prop.slice(-1),
  }
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}

onMounted(() => {
})
</script>
<style lang='scss'>
.crowed .el-table__inner-wrapper::before {
  bottom: 1px !important;
}

.crowed .el-table--enable-row-transition .el-table__inner-wrapper::before {
  bottom: 0px !important;
}
</style>