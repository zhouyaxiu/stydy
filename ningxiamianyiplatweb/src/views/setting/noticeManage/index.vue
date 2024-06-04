<template>
  <!-- 公告板管理 -->
  <div>
    <ss-condition-area>
      <!-- 搜索条件 -->
      <div class="condition-row">
        <div class="item required">
          <ss-date-range title="发布时间" v-model:defaultValue="searchParams.complaintDate" />
        </div>
        <div class="item">
          <div class="label required">发布范围：</div>
          <ss-area-areaTreeStation :getToLevelNum="3" v-model="areaTreeValue" @nodeClick="nodeClick" />
        </div>
        <div class="item">
          <div class="label required">排列方式：</div>
          <el-select v-model="searchParams.orderType" placeholder="请选择">
            <el-option v-for="item in arrangeTypeSelect" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </div>

      </div>
      <!-- 按钮 -->
      <div class="condition-row">
        <ss-custom-button :type="'insert'" @click="add"></ss-custom-button>
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
      </div>
    </ss-condition-area>
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"></ss-pagination>
    <!-- table -->
    <ss-table :data="tableData">
      <el-table-column type="index" min-width="80" label="序号" fixed>
        <template #default="scope">{{ 20 * (pageIndex - 1) + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="title" min-width="200" label="标题" show-overflow-tooltip />
      <el-table-column prop="fbDate" min-width="160" label="发布时间" />
      <el-table-column prop="fbUser" min-width="180" label="发布者" />
      <el-table-column prop="fbdw" min-width="200" label="发布者单位" />
      <el-table-column prop="distName" min-width="160" label="发布范围" />
      <el-table-column prop="validDate" min-width="180" label="有效期限">
        <template #default="{ row }"> {{ row.validDate ? row.validDate.slice(0, 10) : '永久有效' }}</template>
      </el-table-column>
      <el-table-column prop="valid" min-width="120" label="是否保留">
        <template #default="{ row }">
          <span v-if="row.valid === '1'">是</span>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column min-width="160px" label="操作">
        <template #default="{ row }">
          <el-button :disabled="!row.canUpdate" @click="edit(row)" link type='primary'>修改</el-button>
          <el-button :disabled="!row.canDelete" @click="del(row)" link type='primary'>删除</el-button>
        </template>
      </el-table-column>
    </ss-table>
    <noticeManageDialog :visible="noticeManageDialogVisible" @close="close" @fetchData="getList" :isEdit="isEdit"
      :curId="curId" />
  </div>
</template>
<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNoticeManageList, delNotice } from '@/api/apis/setting'
import noticeManageDialog from './noticeManageDialog.vue'
import { reactive } from 'vue';
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const noticeManageDialogVisible = ref(false)
const isEdit = ref(false)
const curId = ref(null)
const tableData = ref([])
const pageIndex = ref(1)
const total = ref(0)
const pageSize = ref(20)
const arrangeTypeSelect = [
  { id: '0', name: '按时间排列' },
  { id: '1', name: '按发布范围排列' },
  { id: '2', name: '按标题排列' }
]
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: store.currentLevel
});
const searchParams = reactive({
  orderType: '0',
  complaintDate: [globalProxy.$dateUtil.currentDate(), globalProxy.$dateUtil.currentDate()],
  pageNum: 1
})
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  getList()
}
const getList = () => {
  searchParams.startDate = searchParams.complaintDate[0]
  searchParams.endDate = searchParams.complaintDate[1]
  searchParams.selectAreaId = areaTreeValue.areaId
  searchParams.userId = store.userInfo.id
  searchParams.pageNum = pageIndex.value
  searchParams.operateType = globalProxy.$consts.OPTYPE_QUERY
  searchParams.cModId = store.moduleId
  getNoticeManageList(searchParams)
    .then(res => {
      tableData.value = res.data.data.list
      total.value = res.data.data.total
    })
    .catch(() => {
    })
}
const nodeClick = (node) => {
  areaTreeValue = node
}
const add = () => {
  noticeManageDialogVisible.value = true
}
const edit = (row) => {
  noticeManageDialogVisible.value = true
  curId.value = row.id
  isEdit.value = true
}
const del = (row) => {
  ElMessageBox.confirm('请确认是否删除?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  })
    .then(() => {
      delNotice({ id: row.id, cModId: store.moduleId }).then(
        res => {
          ElMessage.success('删除成功!')
          search()
        }
      )
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}
const close = () => {
  noticeManageDialogVisible.value = false
  isEdit.value = false
  curId.value = null
}
</script>
<style scoped></style>
