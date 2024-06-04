<!-- 投诉管理-投诉管理 -->
<template>
  <div>
    <ss-condition-area>
      <div class="flex">
        <div class="flex">
          <span class="mr10 label">归属地区：</span>
          <ss-area-areaTreeStation ref="areaTreeStation" v-model="areaTreeValue" :getToLevelNum="5" :showSelect="false"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
        <div class="flex ml20">
          <span class="mr10 label">投诉时间范围：</span>
          <ss-date-range-new v-model:defaultValue="monthValue" rangeName="year"></ss-date-range-new>
        </div>
      </div>
      <div class="mt10">
        <!-- <ss-add-button @click="add"></ss-add-button>
        <ss-query-button @click="search"></ss-query-button> -->
        <ss-custom-button :type="'insert'" @click="add"></ss-custom-button>
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
      </div>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" @openDetailDialog="openDetailDialog" :total="0"
      @rowOperateFun="rowOperateFun"></ss-custom-table>
    <dialog-info :visible="dialogVisible" @close="close" :areaTreeValue="areaTreeData" :id="id"
      :isEdit="isEdit"></dialog-info>
  </div>
</template>

<script setup>
import { ref, } from 'vue';
import { mainStore } from "@/store/index";
import { ElMessageBox } from 'element-plus'
const store = mainStore();
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { queryComlains, deleteComplain } from "@/api/apis/setting";
import dialogInfo from "./dialogInfo.vue";
const monthValue = ref([globalProxy.$dateUtil.currentMonth('YYYY-MM-DD'), globalProxy.$dateUtil.dayAddNum(0)])
// const daterange = ref([globalProxy.$dateUtil.currentMonth('YYYY-MM-DD'), globalProxy.$dateUtil.dayAddNum(0)])
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
// const pageSize = ref(20)
const columnList = reactive([
  {
    label: '序号',
    prop: 'rowNum',
  },
  {
    label: '操作',
    type: 'operation',
    btns: [
      {
        type: 'primary',
        text: '查看',
        event: 'see'
      },
      {
        type: 'danger',
        text: '删除',
        event: 'delete'
      }
    ],
  },
  {
    label: '被投诉单位',
    prop: 'organName',
  },
  {
    label: '投诉日期',
    prop: 'complainTime',
  },
  {
    label: '投诉主题',
    prop: 'theme',
  },
  {
    label: '记录人员',
    prop: 'userName',
  },
])
const dialogVisible = ref(false);
// // 投诉时间范围ref
// const daterangeRef = ref(null);
const id = ref("");
const isEdit = ref(false);
// 查询
// 地区
let areaTreeValue = reactive({
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: sessionStorage.getItem("currentLevel"),
});
const search = (pageNum) => {
  // if (daterangeRef.value.OverYearVerify('投诉时间')) return;
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    areaLevel: areaTreeValue.selectAreaLevel,
    areaCode: areaTreeValue.areaCode,
    complainStartTime: monthValue.value[0],
    complainEndTime: monthValue.value[1],
    pageNum: pageIndex.value,
  }
  queryComlains(params).then(res => {
    if (res.data.code === 200) {
      tableData.value = res.data.data.data.list
      total.value = res.data.data.data.total
    }
  })
}
// 新增
const add = () => {
  id.value = '';
  isEdit.value = false;
  dialogVisible.value = true;
}
// 查看
const seeHandler = (row) => {
  id.value = row.id;
  isEdit.value = true;
  dialogVisible.value = true;
}
// 删除
const deleteHandler = (row) => {
  ElMessageBox.confirm(
    '请问是否确认删除?',
    '提示',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'danger',
    }
  )
    .then(() => {
      const params = {
        id: row.id,
      }
      deleteComplain(params).then(res => {
        if (res.data.code === 200) {
          search()
          return globalProxy.$util.showMsg(res.data.data.data || '删除成功！', 'success');
        }
      })
    })
    .catch(() => {
      return globalProxy.$util.showMsg('取消删除！', 'info');
    })
}

// 操作按钮
const rowOperateFun = (row, type) => {
  if (type == 'see') seeHandler(row)
  if (type == 'delete') deleteHandler(row)
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
const nodeClick = (node) => {
  areaTreeValue = node
}
</script>
<style scoped lang='scss'></style>