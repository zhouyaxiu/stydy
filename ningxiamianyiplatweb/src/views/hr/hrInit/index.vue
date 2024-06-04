<!-- 人力资源-人力资源管理 -->
<template>
  <div>
    <ss-condition-area>
      <div class="condition-row">
        <div class="item">
          <span class="required label">单位选择：</span>
          <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5"
            @nodeClick="nodeClick"></ss-area-areaTreeStation>
        </div>
      </div>
      <ss-query-bottom :secTime="secTime">
        <ss-custom-button :type="'insert'" @click="add"></ss-custom-button>
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
      </ss-query-bottom>
    </ss-condition-area>
    <ss-custom-table :tableData="tableData" :columnList="columnList" :total="total" @rowOperateFun="rowOperateFun"
      @goPage="search"></ss-custom-table>
    <dialog-info :visible="dialogVisible" v-if="dialogVisible" @close="close" :areaTreeValue="areaTreeData"
      :alterData="alterData" :isEdit="isEdit" @fetchData="search"></dialog-info>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessageBox } from 'element-plus'
import { mainStore } from "@/store/index";
const store = mainStore()
import { queryEmployeeInfoList, deleteEmployee } from "@/api/apis/hr";
import dialogInfo from "./dialogInfo.vue";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const tableData = ref([]);
const total = ref(0);
const pageIndex = ref(1);
const secTime = ref(0);
const dialogVisible = ref(false);
const isEdit = ref(false);
const alterData = ref({});
const areaTreeData = ref({});
const columnList = ref([
  {
    label: '序号',
    type: 'index',
  },
  {
    label: '操作',
    type: 'operation',
    minWidth: '150',
    btns: [
      {
        event: 'delete',
        type: 'danger',
        text: '删除'
      },
      {
        event: 'alter',
        text: '修改'
      }
    ]
  },
  {
    label: '所属地区',
    prop: 'areaName',
    minWidth: '120',
    overflowTip: true,
  },
  {
    label: '所属接种单位',
    prop: 'unitName',
    minWidth: '150',
    overflowTip: true,
  },
  {
    label: '人员编码',
    prop: 'personId',
    minWidth: '140'
  },
  {
    label: '姓名',
    prop: 'name',
    minWidth: '100',
    overflowTip: true,
  },
  {
    label: '性别',
    prop: 'gender',
    minWidth: '60',
  },
  {
    label: '年龄',
    prop: 'age',
    minWidth: '60',
  },
  {
    label: '专/兼职',
    prop: 'positionType',
    minWidth: '80',
  },
  {
    label: '从事岗位',
    prop: 'post',
    minWidth: '140',
    overflowTip: true,
  },
  {
    label: '工龄（全部）',
    prop: 'workAge',
    minWidth: '120'
  },
  {
    label: '工龄（免疫规划）',
    prop: 'immunityWorkingAge',
    minWidth: '150',
  },
  {
    label: '技术职称',
    prop: 'skillTitle',
    minWidth: '110'
  },
  {
    label: '学历',
    prop: 'degree',
    minWidth: '140',
    overflowTip: true,
  },
  {
    label: '培训证有无',
    prop: 'certificate',
    minWidth: '120',
  },
  {
    label: '培训合格有效期至',
    prop: 'validDate',
    minWidth: '160',
  },
  {
    label: '维护日期',
    prop: 'maintenanceDate',
    minWidth: '160'
  }
]);
let areaTreeValue = reactive({
  supCode: store.userInfo.areaCode,
  areaId: store.userInfo.areaId,
  id: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  areaName: store.userInfo.areaName,
  selectAreaLevel: sessionStorage.getItem("currentLevel")
});
const nodeClick = (node) => {
  areaTreeValue = node;
  areaTreeData.value.id = node.id;
  areaTreeData.value = node;
};
onMounted(() => {
  const arr = JSON.parse(sessionStorage.getItem("roleModules"));
  const item = arr.filter(item => item.moduleId == 4648)[0];
  const hasPermision = item.delete == '1' || item.update == '1' ? true : false;
  if (!hasPermision) {
    columnList.value = columnList.value.filter(item => item.type != 'operation');
  }
})
// 查询
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  const params = {
    areaCode: areaTreeValue.areaCode,
    areaId: areaTreeValue.areaId,
    areaLevel: areaTreeValue.selectAreaLevel,
    areaName: areaTreeValue.areaName,
    pageNum: pageIndex.value,
    pageSize: globalProxy.$consts.PAGE_SIZE,
    moduleId: sessionStorage.getItem("moduleId"),
  }
  queryEmployeeInfoList(params).then(res => {
    if (res.data.code === 200) {
      res.data.data.list.map(item => {
        item.validDate = item.validDate ? /\d{4}-\d{1,2}-\d{1,2}/g.exec(item.validDate)[0] : '';
        item.maintenanceDate = item.maintenanceDate ? /\d{4}-\d{1,2}-\d{1,2}/g.exec(item.maintenanceDate)[0] : '';
      })
      tableData.value = res.data.data.list;
      total.value = res.data.data.total;
    }
  })
}
// 新增
const add = () => {
  if (areaTreeValue.selectAreaLevel != '4') {
    return globalProxy.$util.showMsg('请选择接种单位！', 'error')
  }
  areaTreeData.value = areaTreeValue;
  isEdit.value = false;
  nextTick(() => {
    dialogVisible.value = true;
  })
}
// 修改
const alterHandler = (data) => {
  alterData.value = data;
  isEdit.value = true;
  nextTick(() => {
    dialogVisible.value = true;
  })
}
// 删除
const deleteHandler = (data) => {
  // proxy.$util.showConfirmDialog("msg", confirm, '确定删除吗？')
  ElMessageBox.confirm('确定删除吗？', '提示',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      draggable: true,
    }
  )
    .then(() => {
      const params = {
        buildBaseCode: data.buildBasecode,
        id: data.id,
      }
      deleteEmployee(params).then(res => {
        if (res.data.code === 200) {
          search();
          return globalProxy.$util.showMsg('删除成功！', 'success');
        }
      })
    })
    .catch(() => {

    })
}
// 关闭弹框
const close = () => {
  dialogVisible.value = false;
}
// 操作按钮
const rowOperateFun = (id, type) => {
  if (type == 'alter') alterHandler(id)
  if (type == 'delete') deleteHandler(id)
}
</script>
<style scoped lang='scss'></style>