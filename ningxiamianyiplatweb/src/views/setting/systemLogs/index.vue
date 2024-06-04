<template>
  <!-- 系统日志查看 -->
  <div>
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <!-- 搜索条件 -->
      <div class="condition-row">
        <div class="item required">
          <ss-date-time-range title="日志时间" v-model:defaultValue="surveyDate" :format="'YYYY-MM-DD HH'"
            :valueFormat="'YYYY-MM-DD HH'" />
        </div>
        <div class="item">
          <span class="label required">操作类型：</span>
          <el-checkbox-group v-model="operateTypes">
            <el-checkbox v-for="item in checkSelect" :key="item.key" :label="item.id">{{ item.value }}</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <el-checkbox v-model="canInputAccount" :label="true" class="label">登录账号：</el-checkbox>
          <el-select v-model="searchParams.userId" placeholder="请选择" :disabled="!canInputAccount" filterable>
            <el-option v-for="item in sysLoginUserSelect" :key="item.id" :label="item.loginName" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="item">
          <el-checkbox v-model="canChangeSystem" :label="true" class="label">系统模块：</el-checkbox>
          <el-select v-model="searchParams.moduleId" placeholder="请选择" :disabled="!canChangeSystem" filterable>
            <el-option v-for="item in sysModuleSelect" :key="item.id" :label="item.moduleName" :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <!-- 按钮 -->
      <div class="condition-row">
        <ss-custom-button :type="'query'" @click="search(1, true)"></ss-custom-button>
      </div>
    </ss-condition-area>
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search" />
    <ss-table :data="tableData" ref="tabPage">
      <el-table-column type="index" min-width="120" label="序号">
        <template #default="scope">{{ 20 * (searchParams.pageNum - 1) + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="operType" min-width="200" label="操作类型">
        <template #default="scope">{{ operTypeStatus[Number(scope.row.operType)] }}</template>
      </el-table-column>
      <el-table-column prop="operUser" min-width="160" label="用户名" />
      <el-table-column prop="operModule" min-width="180" label="模块名称" />
      <el-table-column prop="operDate" min-width="160" label="记录时间" />
    </ss-table>
  </div>
</template>
<script setup>
import { queryAllUser, queryAllModule } from '@/api/apis/common'
import { queryLogList } from '@/api/apis/setting'
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const operTypeStatus = ref(['登录', '增加', '修改', '删除', '查询', '', '导出']);
const canInputAccount = ref(false);
const canChangeSystem = ref(false);
const surveyDate = ref([]);
const searchParams = reactive({
  moduleId: '',
  userId: '',
  type: [],
  startDate: '',
  endDate: '',
  pageNum: 0
});
const searchData = ref(null);
const tabPage = ref(null);
const total = ref(0)
const pageIndex = ref(1)
const pageSize = ref(20)
const tableData = ref([]);
const operateTypes = ref([]);
const sysModuleSelect = ref([]);
const sysLoginUserSelect = ref([]);
const checkSelect = ref([
  {
    id: '0',
    value: '登录'
  },
  {
    id: '1',
    value: '增加'
  },
  {
    id: '2',
    value: '修改'
  },
  {
    id: '3',
    value: '删除'
  },
  {
    id: '4',
    value: '查询'
  },
  {
    id: '6',
    value: '导出'
  }]);
watch(() => canInputAccount, () => {
  searchParams.userId = ''
}, {
  deep: true,
  immediate: true,
})
watch(() => canChangeSystem, () => {
  searchParams.moduleId = ''
}, {
  deep: true,
  immediate: true,
})
const getSysModuleSelect = () => {
  queryAllModule().then(res => {
    sysModuleSelect.value = res.data.data
  })
}

const getSysLoginUserSelect = () => {
  queryAllUser().then(res => {
    sysLoginUserSelect.value = res.data.data
  })
}

const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}

const search = (pageNum, flag) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  let startTime = surveyDate.value[0]
  let endTime = surveyDate.value[1]
  if (!startTime || startTime == '' || !endTime || endTime == '' || startTime.length < 10 || endTime.length < 10) {
    ElMessage.error('操作时间为必填项，请填写完整再查询！')
    return false
  } else {
    if (startTime.length == 10 || startTime.length == 15) {
      startTime = startTime.substring(0, 10) + ' 00'
    }
    if (endTime.length == 10 || endTime.length == 15) {
      endTime = endTime.substring(0, 10) + ' 00'
    }
    if (startTime > endTime) {
      ElMessage.error('起始日期范围不能大于结束日期范围，请修改正确再查询！')
      return false
    }
  }
  if (!flag) {
    canInputAccount.value = false
    canChangeSystem.value = false
  } else {
    if (canInputAccount.value && searchParams.loginAccount === '') {
      ElMessage.error('请输入登录账号！')
      searchParams.pageNum = pageNum
      return false
    }
    if (canChangeSystem.value && searchParams.moduleId === '') {
      ElMessage.error('请选择系统模块！')
      searchParams.pageNum = pageNum
      return false
    }
  }
  if (!operateTypes.value.length) {
    ElMessage.error('操作类型至少选择一个！')
    return false
  }
  searchParams.pageNum = pageIndex.value
  searchParams.startDate = startTime
  searchParams.endDate = endTime
  searchParams.operateType = globalProxy.$consts.OPTYPE_QUERY
  searchParams.cModId = store.moduleId
  searchParams.type = operateTypes.value.join()
  searchData.value = JSON.parse(JSON.stringify(searchParams))
  getList()
}
const getList = () => {
  queryLogList(searchParams)
    .then(res => {
      tableData.value = res.data.data.list
      total.value = res.data.data.total
    })
    .catch(() => {
    })
}
onMounted(() => {
  let currentDate = globalProxy.$dateUtil.currentDate()
  let currentHour = Number(globalProxy.$dateUtil.todayMinutes().split(':')[0]) + 1
  surveyDate.value = [currentDate + ' 00', currentDate + ' ' + currentHour]
  getSysModuleSelect()
  getSysLoginUserSelect()
})
</script>
<style scoped></style>
