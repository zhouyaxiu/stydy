<template>
  <!-- 接种单位管理 -->
  <div>
    <ss-condition-area ref="conditionPage">
      <div class="condition-row">
        <div class="item">
          <span class="label required">上级行政区划：</span>
          <ss-area-areaTreeStation :showSelect="false" :getToLevelNum='4' v-model="areaTreeValue" :reload="showTree"
            @nodeClick="nodeClick" />
        </div>
        <div class="item">
          <el-checkbox v-model="canInputUnitName" :label="true" class="label">接种单位名称：</el-checkbox>
          <el-input :disabled="!canInputUnitName" clearable v-model.trim="searchParams.unitnameCheck" />
        </div>
        <div class="item">
          <el-checkbox v-model="canInputChildNo" :label="true" class="label">客户端识别码：</el-checkbox>
          <el-input :disabled="!canInputChildNo" clearable v-model.trim="searchParams.childNo" />
        </div>
      </div>
      <div class="condition-row">
        <ss-custom-button :type="'insert'" @click="editClick(false, '')"></ss-custom-button>
        <ss-custom-button :type="'query'" @click="search(1)"></ss-custom-button>
        <ss-custom-button :type="'output'" @click="exportExcelFun"></ss-custom-button>
        <ss-explain-tip-text :secTime="secTime" :showSecTimeFlag="true" />
      </div>
    </ss-condition-area>
    <ss-pagination @pageChange="search" :pageSize="pagesize" :pageIndex="pageNum" :total="total" />
    <ss-table :data="dataList" ref="tabPage" :transfer="true" :headerCellStyle="{ 'text-align': 'center' }"
      :cellStyle="{ 'text-align': 'center' }">
      <el-table-column type="index" min-width="60" label="序号" fixed>
        <template #default="scope">{{ 20 * (pageNum - 1) + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column min-width="120px" label="操作">
        <template #default="{ row }">
          <el-button @click.native="editClick(true, row.organCode)" link type='primary'>修改</el-button>
          <el-button @click.native="deleteClick(row.id)" link type='primary'>删除</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="supName" min-width="140" label="上级地区" show-overflow-tooltip />
      <el-table-column prop="organCode" min-width="140" label="接种单位编码" show-overflow-tooltip />
      <el-table-column prop="organName" min-width="180" label="接种单位名称" show-overflow-tooltip />
      <el-table-column prop="organAddress" min-width="200" label="地址" show-overflow-tooltip />
      <el-table-column prop="linkPhone" min-width="140" label="电话" show-overflow-tooltip>
        <template #default="{ row }">{{ row.linkPhone ? row.linkPhone.substring(0, row.linkPhone.length - 4) + '****' : ''
        }}</template>
      </el-table-column>
      <el-table-column prop="linkMan" min-width="100" label="联系人" />
      <el-table-column prop="clinicDay" min-width="80" label="门诊日" />
      <el-table-column prop="beginDate" min-width="110" label="启用日期" />
      <el-table-column prop="endDate" min-width="110" label="停用日期" />
      <el-table-column prop="newBaseCode" min-width="130" label="客户端识别码" show-overflow-tooltip />
      <el-table-column min-width="120" label="是否允许上传">
        <template #default="{ row }">{{ row.mark === '1' ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column prop="serviceType" min-width="100" label="服务类型" />
      <el-table-column prop="clinicType" min-width="90" label="服务周期" />
      <el-table-column prop="workType" min-width="140" label="工作类型" />
      <el-table-column prop="buildingArea" min-width="110" label="建筑总面积" />
      <el-table-column prop="" min-width="420" label="地图坐标" show-overflow-tooltip>
        <template #default="{ row }">
          {{ row.longitude ? '经度：' + row.longitude : '' }}
          {{ row.dimension ? '；纬度：' + row.dimension : '' }}
        </template>
      </el-table-column>
    </ss-table>
    <!-- 新增与编辑弹窗 -->
    <edit-form :editVisible="editVisible" :isEdit="isEdit" :curId="curId" @closeDialog="closeDialog"></edit-form>
    <!-- 分页导出 -->
    <ss-export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'接种单位管理'"
      :dialogVisible="exportDialogVisible"></ss-export-dialog>
  </div>
</template>
<script setup>
import {
  queryInoculationUnitList,
  delInoculationUnit,
  exportInoculationUnit
} from '@/api/apis/setting'
import editForm from './editForm.vue'
import { PAGE_SIZE } from '@/utils/common/consts'
import { areaTree } from '@/utils/common/area.js'
import { ElMessage, ElMessageBox } from 'element-plus';
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  newBaseCode: store.userInfo.areaCode.substr(0, 10),
  areaName: store.userInfo.areaName,
  selectAreaLevel: store.currentLevel
});
const secTime = ref(0)
const isEdit = ref(false)
const showTree = ref(false)
const canInputUnitName = ref(false)
const canInputChildNo = ref(false)
const editVisible = ref(false)
const searchData = ref(null)
const searchParams = reactive(
  {
    unitnameCheck: '',
    childNo: ''
  }
)
const pagesize = ref(PAGE_SIZE)
const exportDialogVisible = ref(false)
const total = ref(0)
const pageNum = ref(1)
const dataList = ref([])
const curId = ref('')
watch(() => canInputUnitName.value, (val) => {
  if (!val) {
    searchParams.unitnameCheck = ''
  }
}, {
  deep: true,
  immediate: true,
})
watch(() => canInputChildNo.value, (val) => {
  if (!val) {
    searchParams.childNo = ''
  }
}, {
  deep: true,
  immediate: true,
})
// 选择地区
const nodeClick = (node) => {
  areaTreeValue = node
}
// 查询
const search = (pagenum) => {
  if (!pagenum) {
    pagenum = 1
  }
  if (canInputUnitName.value && searchParams.unitnameCheck === '') {
    ElMessage.error("请输入接种单位名称")
    return false
  }
  if (canInputChildNo.value && searchParams.childNo === '') {
    ElMessage.error("请输入客户端识别码")
    return false
  }
  pageNum.value = pagenum
  const params = {
    supCode: areaTreeValue.areaCode,
    supAreaLevel: areaTreeValue.selectAreaLevel,
    supAreaName: areaTreeValue.areaName + ' ' + areaTreeValue.newBaseCode,
    organName: searchParams.unitnameCheck,
    newBasecode: searchParams.childNo,
    pageNum: pagenum,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  searchData.value = params
  queryInoculationUnitList(params)
    .then(res => {
      dataList.value = res.data.data.data.list
      total.value = res.data.data.data.total
      secTime.value = res.data.data.sec
    })
    .catch(() => {
    })
}
// 编辑
const editClick = (flag, id) => {
  isEdit.value = flag
  if (flag) { curId.value = id }
  editVisible.value = true
}
// 弹窗关闭回调
const closeDialog = (isrefrash) => {
  curId.value = null
  if (isrefrash === true) {
    search()
    setTimeout(() => {
      showTree.value = true
    }, 500);
  }
  editVisible.value = false
}
// 删除
const deleteClick = (id) => {
  ElMessageBox.confirm('请确认是否删除?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  })
    .then(() => {
      delInoculationUnit({
        id: id,
        operateType: globalProxy.$consts.OPTYPE_DELETE,
        cModID: store.moduleId
      }).then(res => {
        if (res.data.code === 200) {
          areaTree()
          setTimeout(() => {
            showTree.value = true
          }, 500);
          ElMessage.success(res.data.data.data)
        } else {
          ElMessage.error(res.data.popMsg)
        }
        search()
      })
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}

// 导出excel
const exportExcelFun = () => {
  if (!searchData.value) {
    ElMessage.error("查询后才可以导出！")
    return
  }
  if (dataList.value.length == 0) {
    ElMessage.error("暂无需要导出的数据！")
    return
  }
  exportDialogVisible.value = true;
}
const handleExportClose = () => {
  exportDialogVisible.value = false
}

const exportExcel = () => {
  let params = JSON.parse(JSON.stringify(searchData.value))
  params.selectAreaName = params.selectAreaName + params.newBaseCode
  params.operateType = globalProxy.$consts.OPTYPE_OUTPUT
  exportInoculationUnit(params)
}
</script>
<style scoped></style>
