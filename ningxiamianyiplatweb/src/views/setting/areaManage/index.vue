<!-- 地区管理 -->
<template>
  <div>
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <span class="label required">上级地区：</span>
          <ss-area-areaTreeStation :getToLevelNum="3" v-model="areaTreeValue" @nodeClick="nodeClick" />
        </div>
        <div class="item">
          <el-checkbox v-model="selectAreaCode" :label="true" class="label">地区编码：</el-checkbox>
          <el-input :disabled="!selectAreaCode" clearable v-model.trim="areaCode" />
        </div>
        <div class="item">
          <el-checkbox v-model="selectAreaName" :label="true" class="label">地区名称：</el-checkbox>
          <el-input :disabled="!selectAreaName" clearable v-model.trim="areaName" />
        </div>
      </div>
      <div class="condition-row">
        <ss-custom-button :type="'insert'" @click="add"></ss-custom-button>
        <ss-custom-button :type="'query'" @click="search"></ss-custom-button>
        <ss-explain-tip-text :tipText="tipText" :secTime="secTime" :showSecTimeFlag="true" />
      </div>
    </ss-condition-area>
    <ss-pagination :total="total" :pageIndex="pageIndex" :pageSize="pageSize" @pageChange="search"></ss-pagination>
    <ss-table :data="tableData" :headerCellStyle="{ 'text-align': 'center' }" :cellStyle="{ 'text-align': 'center' }"
      ref="tabPage">
      <el-table-column min-width="30px" type="index" label="序号">
        <template #default="scope">{{ (pageIndex - 1) * pageSize + scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="地区编码" prop="areaCode"></el-table-column>
      <el-table-column label="地区名称" prop="areaName"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" link @click="edit(scope.row)">修改</el-button>
          <el-button type="primary" link @click="deleteClick(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </ss-table>
    <!-- 新增 or 修改信息弹框 -->
    <AreaInfoDialog :visible="dialogVisible" :id="editId" :isEdit="isEdit" @close="close" @fetchData="search"
      v-if="dialogVisible" />
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus'

import { areaTree } from '@/utils/common/area.js'
import AreaInfoDialog from './areaInfoDialog.vue';
import { queryAreaList, deleteArea } from '@/api/apis/setting';
import { mainStore } from "@/store/index";

const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const dialogVisible = ref(false); //弹框显示
const isEdit = ref(false); //是否编辑
// 搜索数据
const selectAreaCode = ref(false);
const selectAreaName = ref(false);
const areaCode = ref('');
const areaName = ref('');
const secTime = ref(0);
const tipText = ref('');
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId
});
// 编辑数据
const editId = ref(null);

const tableData = ref([])
const tabPage = ref(null);
const total = ref(0);
const pageIndex = ref(1)
const pageSize = ref(20)
watch(() => selectAreaCode.value, (val) => {
  if (!val) {
    areaCode.value = ''
  }
}, {
  deep: true,
  immediate: true,
})
watch(() => selectAreaName.value, (val) => {
  if (!val) {
    areaName.value = ''
  }
}, {
  deep: true,
  immediate: true,
})
const nodeClick = (node) => {
  areaTreeValue = node
}
const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}
const search = (pageNum) => {
  if (typeof pageNum == 'number') {
    pageIndex.value = pageNum
  }
  if (selectAreaCode.value && areaCode.value === '') {
    ElMessage.error("请输入地区编码")
    return false
  }
  if (selectAreaName.value && areaName.value === '') {
    ElMessage.error("请输入地区名称")
    return false
  }
  const params = {
    supAreaId: areaTreeValue.areaId,
    areaCode: areaCode.value,
    areaName: areaName.value,
    pageNum: pageIndex.value,
    operateType: globalProxy.$consts.OPTYPE_QUERY,
    cModId: store.moduleId
  }
  let res = {
    "success": true,
    "popMsg": null,
    "data": {
      "sec": 0.046,
      "data": {
        "total": 21,
        "list": [
          {
            "id": "2",
            "areaCode": "640100000000",
            "areaName": "银川市",
            "updateDate": "2022-06-06 14:38:40",
            "updateUser": "超级管理员1",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "184",
            "areaCode": "640200000000",
            "areaName": "石嘴山市",
            "updateDate": "2016-03-24 12:20:27",
            "updateUser": "INIT",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "285",
            "areaCode": "640300000000",
            "areaName": "吴忠市",
            "updateDate": "2016-03-24 12:20:28",
            "updateUser": "INIT",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "425",
            "areaCode": "640400000000",
            "areaName": "固原市",
            "updateDate": "2016-03-24 12:20:28",
            "updateUser": "INIT",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "596",
            "areaCode": "640500000000",
            "areaName": "中卫市",
            "updateDate": "2016-03-24 12:20:29",
            "updateUser": "INIT",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2001",
            "areaCode": "642100000000",
            "areaName": "济南市",
            "updateDate": "2016-06-02 10:20:18",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2008",
            "areaCode": "642200000000",
            "areaName": "青岛市",
            "updateDate": "2016-06-02 11:10:44",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2014",
            "areaCode": "642300000000",
            "areaName": "淄博市",
            "updateDate": "2016-06-02 11:10:56",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2020",
            "areaCode": "642400000000",
            "areaName": "枣庄市",
            "updateDate": "2016-06-02 15:22:58",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2026",
            "areaCode": "642500000000",
            "areaName": "东营市",
            "updateDate": "2016-06-02 15:23:09",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2031",
            "areaCode": "642800000000",
            "areaName": "济宁市",
            "updateDate": "2016-06-02 10:25:46",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2037",
            "areaCode": "642900000000",
            "areaName": "泰安市",
            "updateDate": "2016-06-02 15:50:04",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2041",
            "areaCode": "643100000000",
            "areaName": "日照市",
            "updateDate": "2016-06-02 14:10:49",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2046",
            "areaCode": "643200000000",
            "areaName": "莱芜市",
            "updateDate": "2016-06-02 11:11:09",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2049",
            "areaCode": "643300000000",
            "areaName": "临沂市",
            "updateDate": "2016-06-02 14:13:37",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2052",
            "areaCode": "643400000000",
            "areaName": "德州市",
            "updateDate": "2016-06-02 14:15:34",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "2058",
            "areaCode": "643600000000",
            "areaName": "滨州市",
            "updateDate": "2016-06-02 15:59:51",
            "updateUser": "超级管理员",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "100000109",
            "areaCode": "643800000000",
            "areaName": "自测市A",
            "updateDate": "2024-01-12 10:31:53",
            "updateUser": "超级管理员1",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "100000086",
            "areaCode": "645500000000",
            "areaName": "测试删除某接种单位数据",
            "updateDate": "2023-07-17 15:01:07",
            "updateUser": "超级管理员1",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          },
          {
            "id": "100000105",
            "areaCode": "648800000000",
            "areaName": "原地区",
            "updateDate": "2023-08-11 13:10:10",
            "updateUser": "超级管理员1",
            "tableStatus": "0",
            "supId": "1",
            "supArea": null
          }
        ],
        "pageNum": 1,
        "pageSize": 20,
        "size": 20,
        "startRow": 1,
        "endRow": 20,
        "pages": 2,
        "prePage": 0,
        "nextPage": 2,
        "isFirstPage": true,
        "isLastPage": false,
        "hasPreviousPage": false,
        "hasNextPage": true,
        "navigatePages": 8,
        "navigatepageNums": [
          1,
          2
        ],
        "navigateFirstPage": 1,
        "navigateLastPage": 2
      }
    },
    "otherdata": null,
    "otherflag": null,
    "code": 200,
    "errorCode": null
  }
  tableData.value = res.data.data.list
  secTime.value = res.data.sec
  total.value = res.data.data.total

  // queryAreaList(params).then(res => {
  //   if (res.data.code === 200) {
  //     tableData.value = res.data.data.data.list
  //     secTime.value = res.data.data.sec
  //     total.value = res.data.data.data.total
  //   }

  // }).catch(err => {

  // })

}
const add = () => {
  dialogVisible.value = true
  isEdit.value = false
}

const edit = (row) => {
  editId.value = row.id;
  dialogVisible.value = true
  isEdit.value = true
}

const deleteClick = (row) => {
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
      deleteArea({
        id: row.id,
        operateType: globalProxy.$consts.OPTYPE_DELETE,
        cModId: store.moduleId
      }).then(res => {
        if (res.data.code == 200) {
          ElMessage.success('用户删除成功')
          search()
          areaTree()
        } else {
          ElMessage.error(res.data.popMsg)
        }
      })

    })
    .catch(() => {
      ElMessage.info('删除取消')
    })
}

const close = () => {
  editId.value = null;
  isEdit.value = false;
  dialogVisible.value = false
}

</script>
<style lang="scss"></style>