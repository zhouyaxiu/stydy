<!-- 人力资源-人力资源统计 -->
<template>
  <div class="statistic">
    <ss-condition-area>
      <div class="condition-row flex">
        <div class="item">
          <span class="required label">单位选择：</span>
          <ss-area-areaTreeStation ref="areaTreeStation" v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick"
            :showSelect="true" @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
      </div>
      <ss-query-bottom :statisTics="true">
        <ss-custom-button :type="'statics'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"
      remarkText="① 技术职称为空不统计；<br>② 省、市、县、乡、接种单位采用静态计算。"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { statistics, outputStatistics } from "@/api/apis/hr";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const tableData = ref([]);
import { mainStore } from "@/store/index";
const store = mainStore()
const columnList = reactive([
  {
    label: '单位',
    prop: 'unitName',
    minWidth: '200',
    overflowTip: true,
  },
  {
    label: '免疫规划条线各种技术职称人数',
    hasChildrenTitle: true,
    list: [
      {
        label: '正主任医师',
        prop: 'director',
        minWidth: '120'
      },
      {
        label: '副主任医师',
        prop: 'associateDirector',
        minWidth: '140'
      },
      {
        label: '主管医师',
        prop: 'directorDoctor',
        minWidth: '120'
      },
      {
        label: '医师',
        prop: 'doctor',
      },
      {
        label: '医士',
        prop: 'medicalAssistant',
      },
      {
        label: '正主任技师',
        prop: 'seniorTechnologist',
        minWidth: '120'
      },
      {
        label: '副主任技师',
        prop: 'associateSeniorTechnologist',
        minWidth: '120'
      },
      {
        label: '主管技师',
        prop: 'technologistInCharge',
        minWidth: '90'
      },
      {
        label: '技师',
        prop: 'technologist',
      },
      {
        label: '技士',
        prop: 'technician',
      },
      {
        label: '正主任护师',
        prop: 'professorNursing',
        minWidth: '150'
      },
      {
        label: '副主任护师',
        prop: 'associateProfessorNursing',
        minWidth: '150'
      },
      {
        label: '主管护师',
        prop: 'directorNursePractitioner',
        minWidth: '90'
      },
      {
        label: '护师',
        prop: 'nursePractitioner',
      },
      {
        label: '护士',
        prop: 'nurse',
      },
      {
        label: '防疫员',
        prop: 'antiepidemic',
      },
      {
        label: '乡村医生',
        prop: 'villageDoctor',
        minWidth: '120'
      },
      {
        label: '其他',
        prop: 'others',
      }
    ]
  },
  {
    label: '合计',
    prop: 'total',
  }
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1)
});
// 查询
const search = () => {
  const params = {
    areaId: areaTreeValue.areaId,
    areaCode: areaTreeValue.areaCode,
    areaName: areaTreeValue.areaName,
    areaLevel: areaTreeValue.selectAreaLevel,
    skipValue: areaTreeValue.skipValue
  }
  searchData.value = params;
  statistics(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.list;
    } else {
      tableData.value = [];
      return globalProxy.$util.showMsg(res.data.popMsg, 'error', 5000, true);
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1)
};
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.areaName = params.areaName + " " + params.areaCode.slice(0, 10);
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  outputStatistics(params)
}
</script>
<style lang='scss'>
.statistic .el-table__inner-wrapper::before {
  bottom: 1px !important;
}

.statistic .el-table--enable-row-transition .el-table__inner-wrapper::before {
  bottom: 0px !important;
}
</style>