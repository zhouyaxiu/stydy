<!-- 国家督察-新冠病毒疫苗分地区接种信息汇总表 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="label required">地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" :showSelect="true" @nodeClick="nodeClick"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <span class="label required">接种日期：</span>
          <ss-date-range-new v-model:defaultValue="monthValue"></ss-date-range-new>
        </div>
        <div class="item">
          <span class="label">疫苗生产企业：</span>
          <ss-vaccine-vendor ref="selectedVaccineVendor"></ss-vaccine-vendor>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <age-group :ageData="ageData" @update:modelValue="value => ageData = value"></age-group>
          <!-- <span class="label" style="display:flex;align-items:center;">
            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
              @change="handleCheckAllChange"></el-checkbox>&nbsp;&nbsp;年龄组：
          </span>
          <el-checkbox-group v-model="chkAge" @change="handleChkAgeChange" :disabled="!checkAll">
            <el-checkbox v-for="item in agesList" :label="item.id" :key="city">{{ item.name }}</el-checkbox>
          </el-checkbox-group> -->
        </div>
      </div>
      <ss-query-bottom :statisTics="false" :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList"></ss-custom-table>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { areaQuery, areaOutput } from "@/api/apis/gjdc";
import ageGroup from '@/components/common/ageGroup.vue';
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { mainStore } from "@/store/index";
const store = mainStore();
const tableData = ref([]);
const secTime = ref(0);
const ageData = ref([])

const selectedVaccineVendor = ref(null);

const columnList = reactive([
  {
    label: '地区',
    prop: 'areaName',
    minWidth: '250',
    overflowTip: true,
    formatter: function (row) {
      return row.areaName == "合计" ? row.areaCode : row.areaName + " " + row.areaCode.slice(0, 10)
    }
  },
  {
    label: '累计接种人数（人）',
    hasChildrenTitle: true,
    list: [
      {
        label: '合计',
        prop: 'lj',
        minWidth: '100'
      },
      {
        label: '第1剂',
        prop: 'lj1',
        minWidth: '100'
      },
      {
        label: '第2剂',
        prop: 'lj2',
        minWidth: '100'
      },
      {
        label: '第3剂',
        prop: 'lj3',
        minWidth: '100'
      },
      {
        label: '第4剂',
        prop: 'lj4',
        minWidth: '100'
      },
      {
        label: '第5剂',
        prop: 'lj5',
        minWidth: '100'
      },
      {
        label: '第6剂',
        prop: 'lj6',
        minWidth: '100'
      }
    ]
  },
  {
    label: '当日接种人数（人）',
    hasChildrenTitle: true,
    list: [
      {
        label: '合计',
        prop: 'dr',
        minWidth: '100'
      },
      {
        label: '第1剂',
        prop: 'dr1',
        minWidth: '100'
      },
      {
        label: '第2剂',
        prop: 'dr2',
        minWidth: '100'
      },
      {
        label: '第3剂',
        prop: 'dr3',
        minWidth: '100'
      },
      {
        label: '第4剂',
        prop: 'dr4',
        minWidth: '100'
      },
      {
        label: '第5剂',
        prop: 'lj5',
        minWidth: '100'
      },
      {
        label: '第6剂',
        prop: 'lj6',
        minWidth: '100'
      }
    ]
  },
  {
    label: '开展新冠疫苗接种单位数',
    hasChildrenTitle: true,
    list: [
      {
        label: '当日',
        prop: 'drStationcode',
        minWidth: '100'
      },
      {
        label: '累计',
        prop: 'ljStationcode',
        minWidth: '100'
      },
    ]
  },
]);
const searchData = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
  skipValue: String(Number(sessionStorage.getItem("currentLevel")) + 1),
  areaName: store.userInfo.areaName
});
const monthValue = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()]);
// 查询
const search = () => {
  let startTime = monthValue.value[0];
  let endTime = monthValue.value[1];

  if (!startTime || !endTime) {
    return globalProxy.$util.showMsg('请选择填报日期范围！', 'error', 5000, true);
  }
  let ageGroup = ageData.value.map(item => item.id).join(",")
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaLevel: areaTreeValue.selectAreaLevel,
    showLevel: areaTreeValue.skipValue == "5" ? 4 : areaTreeValue.skipValue,
    startDate: startTime,
    endDate: endTime,
    ageGroup: ageGroup,
    fenterNo: selectedVaccineVendor.value.fenterNo
  }
  let ageGroupName = ageData.value.map(item => item.name).join(",");
  searchData.value = {
    ...params,
    fenterName: selectedVaccineVendor.value.fenterName,
    areaName: areaTreeValue.areaName,
    ageGroupName: ageGroupName
  }
  areaQuery(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.data;
      secTime.value = res.data.data.sec;
    } else {
      if (res.data.popMsg) {
        secTime.value = "";
        tableData.value = [];
        globalProxy.$util.showMsg(res.data.popMsg, 'error');
      }
    }
  })
}
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeValue.skipValue = String(Number(areaTreeValue.selectAreaLevel) + 1);
}
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}
// 导出excel
const exportExcel = () => {
  if (!searchData.value || searchData.value.areaCode != areaTreeValue.areaCode) {
    return globalProxy.$util.showMsg('查询后才可以导出！', 'error', 5000, true);
  }
  let params = JSON.parse(JSON.stringify(searchData.value));
  params.areaName = params.areaName + " " + params.areaCode.slice(0, 10);
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT;
  areaOutput(params);
}

</script>
<style scoped lang='scss'></style>