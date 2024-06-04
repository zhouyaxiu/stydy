<!-- 国家督察-新冠病毒疫苗基本信息质量控制报表 -->
<template>
  <div>
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <span class="label required">选择地区：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" :showSelect="true"
            @selectClick="selectClick"></ss-area-areaTreeStation>
        </div>
        <div class="item">
          <ss-date-range title="接种日期" v-model:defaultValue="jzDate"></ss-date-range>
        </div>
        <div class="item">
          <ss-date-range title="出生日期" v-model:defaultValue="birthDate"></ss-date-range>
        </div>
      </div>
      <ss-query-bottom :secTime="secTime">
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcel"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-table :data="tableData" ref="tabPage">
      <el-table-column prop="areaName" align="center" min-width="250" label="地区">
        <template #default="{ row }">
          {{ row.areaName + ' ' + row.areaCode }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="总体数据情况（人数）">
        <el-table-column prop="childCount" align="center" min-width="90" label="总数" />
        <el-table-column align="center" min-width="100" label="不合格数">
          <template #default="{ row }">
            <div v-if="row.all && row.all > 0">
              <div class="link-text" @click="showDetail(row, 'all')">{{ row.all }}</div>
            </div>
            <div v-else>{{ row.all }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="allHgrate" align="center" min-width="150" label="合格率" />
      </el-table-column>
      <el-table-column align="center" label="单项不合格数（人数）">
        <el-table-column align="center" min-width="100" label="受种者姓名">
          <template #default="{ row }">
            <div v-if="row.name && row.name > 0">
              <div class="link-text" @click="showDetail(row, 'name')">{{ row.name }}</div>
            </div>
            <div v-else>{{ row.name }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="证件类型">
          <template #default="{ row }">
            <div v-if="row.cid && row.cid > 0">
              <div class="link-text" @click="showDetail(row, 'cid')">{{ row.cid }}</div>
            </div>
            <div v-else>{{ row.cid }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="性别">
          <template #default="{ row }">
            <div v-if="row.sex && row.sex > 0">
              <div class="link-text" @click="showDetail(row, 'sex')">{{ row.sex }}</div>
            </div>
            <div v-else>{{ row.sex }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="出生日期">
          <template #default="{ row }">
            <div v-if="row.birth && row.birth > 0">
              <div class="link-text" @click="showDetail(row, 'birth')">{{ row.birth }}</div>
            </div>
            <div v-else>{{ row.birth }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="国籍">
          <template #default="{ row }">
            <div v-if="row.nationality && row.nationality > 0">
              <div class="link-text" @click="showDetail(row, 'nationality')">{{ row.nationality }}</div>
            </div>
            <div v-else>{{ row.nationality }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="人群分类">
          <template #default="{ row }">
            <div v-if="row.crowdkind && row.crowdkind > 0">
              <div class="link-text" @click="showDetail(row, 'crowdkind')">{{ row.crowdkind }}</div>
            </div>
            <div v-else>{{ row.crowdkind }}</div>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="100" label="重复档案">
          <template #default="{ row }">
            <div v-if="row.samecard && row.samecard > 0">
              <div class="link-text" @click="showDetail(row, 'samecard')">{{ row.samecard }}</div>
            </div>
            <div v-else>{{ row.samecard }}</div>
          </template>
        </el-table-column>
      </el-table-column>
    </ss-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :detailData="detailData"></dialog-info>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { searchXgRptInfoFullInfofull, exportXgRptInfoFullInfofull } from "@/api/apis/gjdc";
import dialogInfo from "./dialogInfo.vue";
import { mainStore } from "@/store/index";
const store = mainStore();

//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const jzDate = ref([globalProxy.$dateUtil.dayAddNum(globalProxy.$dateUtil.getMonthDayNum(globalProxy.$dateUtil.lastMonthStartDay())), globalProxy.$dateUtil.currentDate()])
const birthDate = ref([globalProxy.$dateUtil.dayAddNum(7), globalProxy.$dateUtil.currentDate()])

const tableData = ref([]);
const tabPage = ref(null);
const secTime = ref(0);
const dialogVisible = ref(false);
const detailData = ref({});
const exportAreaName = ref('');

const searchData = ref(null);
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
const selectClick = (node) => {
  areaTreeValue.skipValue = node;
}

const search = () => {
  let jzrqStartTime = jzDate.value[0] || ''
  let jzrqEndTime = jzDate.value[1] || ''
  if (!jzrqStartTime || jzrqStartTime == '' || !jzrqEndTime || jzrqEndTime == '') {
    ElMessage.error('接种日期为必填项，请填写完整再查询！')
    return false
  }
  let birthStartTime = birthDate.value[0] || ''
  let birthEndTime = birthDate.value[1] || ''
  if (!birthStartTime || birthStartTime == '' || !birthEndTime || birthEndTime == '') {
    ElMessage.error('出生日期为必填项，请填写完整再查询！')
    return false
  }
  let params = {
    areaCode: areaTreeValue.areaCode,
    areaName: areaTreeValue.areaName,
    skipLevel: areaTreeValue.skipValue,
    birthStart: birthStartTime,
    birthEnd: birthEndTime,
    jzrqStart: jzrqStartTime,
    jzrqEnd: jzrqEndTime,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  searchData.value = params
  exportAreaName.value = areaTreeValue.areaName + ' ' + areaTreeValue.areaCode
  searchXgRptInfoFullInfofull(params)
    .then(res => {
      tableData.value = res.data.data
      secTime.value = res.data.sec
    })
    .catch(() => {
    })
}
const exportExcel = () => {
  if (!searchData.value) {
    ElMessage.error('查询后才可以导出！')
    return
  }
  if (tableData.value.length === 1) {
    ElMessage.error('暂无需要导出的数据！')
    return
  }
  searchData.value.selectAreaName = exportAreaName.value
  let params = {
    ...searchData.value,
    operateType: globalProxy.$consts.OPTYPE_OUTPUT
  }
  exportXgRptInfoFullInfofull(params)
}
const showDetail = (row, type) => {
  let data = row
  let areacode = data.areaCode
  if (!areacode || areacode == '') {
    ElMessage.error('所选中的行的区划代码为空！')
    return false
  }
  let areaname = data.areaName
  if (areaname == '合计') {
    areaname = exportAreaName.value
  }
  detailData.value = {
    ...searchData.value,
    selectAreaCode: areacode,
    queryType: type,
    selectAreaName: areaname
  }
  dialogVisible.value = true;
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
</script>
<style scoped lang='scss'></style>