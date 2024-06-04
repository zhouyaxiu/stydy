<template>
  <div>
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item required">
          <ss-date-range title="登录时间" v-model:defaultValue="syncDate"></ss-date-range>
        </div>
        <div class="item">
          <span class="label required">登录是否成功：</span>
          <el-radio-group v-model="isLogin">
            <el-radio label="0">成功</el-radio>
            <el-radio label="1">失败</el-radio>
          </el-radio-group>
        </div>
        <div class="item">
          <el-checkbox v-model="canInputUserName" :label="true" class="label">用户登录名：</el-checkbox>
          <el-input :disabled="!canInputUserName" clearable v-model.trim="username" />
        </div>
        <div class="item">
          <el-checkbox v-model="canInputUserIP" :label="true" class="label">用户登录IP：</el-checkbox>
          <el-input :disabled="!canInputUserIP" clearable v-model.trim="loginIp" />
        </div>
      </div>
      <div class="condition-row">
        <ss-custom-button :type="'query'" @click="search(1)"></ss-custom-button>
      </div>
    </ss-condition-area>
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="PAGE_SIZE" @pageChange="search"></ss-pagination>
    <ss-table :data="tableData" ref="tabPage">
      <el-table-column min-width="50px" type="index" label="序号">
        <template #default="scope">{{ (pageIndex - 1) * PAGE_SIZE + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="userName" label="用户登录名"></el-table-column>
      <el-table-column prop="pwd" label="登录密码" show-overflow-tooltip></el-table-column>
      <el-table-column prop="userIp" label="用户登录IP"></el-table-column>
      <el-table-column prop="flag" label="登录是否成功">
        <template #default="{ row }">{{ row.flag == "true" ? "成功" : "失败" }}</template>
      </el-table-column>
      <el-table-column prop="loginTime" label="用户登录时间">
        <template #default="{ row }">{{ globalProxy.$dateUtil.toDateTimeFormat(row.loginTime) }}</template>
      </el-table-column>
    </ss-table>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { PAGE_SIZE } from '@/utils/common/consts'
import { getUserLoginLog } from "@/api/apis/setting";
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const tableData = ref([]);
const tabPage = ref(null);
const total = ref(0);
const pageIndex = ref(1)
const canInputUserName = ref(false)
const canInputUserIP = ref(false)

const startDate = globalProxy.$dateUtil.dayAddNum(7);
const endDate = globalProxy.$dateUtil.currentDate();
const syncDate = ref([startDate, endDate])
const isLogin = ref('0')
const username = ref('');
const loginIp = ref('')

const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}

const search = pageNum => {

  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  let startTime = syncDate.value[0]
  let endTime = syncDate.value[1]

  if (!startTime || !endTime) {
    ElMessage.error("请选择同步日期")
    return
  }
  const params = {
    startDate: startTime,
    endDate: endTime,
    selectUserName: username.value,
    userIp: loginIp.value,
    flag: isLogin.value,
    pageNum: pageIndex.value,
    pageSize: PAGE_SIZE,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  getUserLoginLog(params).then(res => {
    if (res.status === 200) {
      tableData.value = res.data.data.list
      total.value = res.data.data.total
    }
  })
}


</script>
<style scoped lang='scss'>
.el-input {
  width: 260px;
}
</style>