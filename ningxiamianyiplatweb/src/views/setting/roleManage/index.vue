<template>
  <!-- 系统角色管理 -->
  <div class="rolesManage">
    <ss-condition-area @setConditionFlag="setConditionFlag">
      <div class="flex flex-top">
        <div>
          <div class="item">
            <div class="label">选择角色：</div>
            <el-select v-model="rolesForm.id" placeholder="请选择" @change="rolesChange" class="w_294">
              <el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id">
              </el-option>
            </el-select>
          </div>
          <el-form class="form" ref="roleform" :rules="ruleCheck" label-width="110" :model="rolesForm" inline>
            <el-form-item label="角色编码：" :required="true" prop="roleCode">
              <el-input @keyup.native="checkblank" v-model.trim="rolesForm.roleCode" maxlength="20" class="w_294" />
            </el-form-item>
            <el-form-item label="角色名称：" :required="true" prop="roleName">
              <el-input @keyup.native="checkblank" v-model.trim="rolesForm.roleName" maxlength="20" class="w_294" />
            </el-form-item>
            <el-form-item label="是否启用：" prop="onOff">
              <el-radio-group v-model="rolesForm.onOff">
                <el-radio label="1">启用</el-radio>
                <el-radio label="0">停用</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="角色描述：">
              <el-input @keyup.native="checkblank" type="textarea" v-model.trim="rolesForm.roleExplain" maxlength="20"
                class="w_294" />
            </el-form-item>
            <el-form-item label=" " style="margin-bottom: 0;">
              <ss-custom-button v-if="!rolesForm.id" :type="'insert'" @click="saveData"></ss-custom-button>
              <ss-custom-button v-if="rolesForm.id" :type="'update'" @click="saveData"></ss-custom-button>
              <ss-custom-button v-if="rolesForm.id" :type="'delete'" @click="deleteData"></ss-custom-button>
              <ss-custom-button v-if="!rolesForm.id" :type="'clear'" @click="resetData"></ss-custom-button>
            </el-form-item>
          </el-form>
        </div>
        <treeTransfer class="transfer" :treeData="treeList" @callabck="getSelected" :defValue="selectTreeList" />
      </div>
    </ss-condition-area>
    <div class="module-area">
      <div class="op-area">
        <div class="left">角色模块权限</div>
        <div class="right">
          <el-button type="primary" @click="selectAllBtn">全选</el-button>
          <el-button @click="cancleBtn" class="ml_10">取消</el-button>
        </div>
      </div>
      <ss-table :data="moduleList" ref="tabPage">
        <el-table-column prop="code" min-width="200" label="模块编码" show-overflow-tooltip />
        <el-table-column prop="name" min-width="200" label="模块名称" />
        <el-table-column width="80" label="添加">
          <template #default="{ row }">
            <el-checkbox v-model="row.insert" :disabled="!row.canInsert"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column width="80" label="修改">
          <template #default="{ row }">
            <el-checkbox v-model="row.update" :disabled="!row.canUpdate"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column width="80" label="删除">
          <template #default="{ row }">
            <el-checkbox v-model="row.delete" :disabled="!row.canDelete"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column width="80" label="导出">
          <template #default="{ row }">
            <el-checkbox v-model="row.output" :disabled="!row.canOutput"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="nouse" width="150" label="操作">
          <template #default="{ row }">
            <el-button @click="rowCheckAll(row)" type="primary" link>全选</el-button>
            <el-button @click="rowCancel(row)" type="primary" link>取消</el-button>
          </template>
        </el-table-column>
      </ss-table>
    </div>
  </div>
</template>
<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { ptn } from '@/utils/common/validate';
import treeTransfer from "./treeTransfer.vue";
import { getRoleSelect, getRoleDetial, addSystemRole, updateRole, delRole, getAllModuleList } from '@/api/apis/setting'
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const roleCodeCheck = (rule, value, callback) => {
  if (!value) {
    callback('角色编码项为必输项，请填写完整再保存。');
  } else if (value.length > 20) {
    callback('角色编码最多20位字符，请重新填写。');
  }
  callback();
}
const roleNameCheck = (rule, value, callback) => {
  if (!value) {
    callback('角色名称为必输项，请填写完整再保存。');
  } else if (value.length > 20) {
    callback('角色名称最多20位字符，请重新填写。');
  }
  callback();
}
let allModule = []
const tabPage = ref(null);
const specFlag = ref(0)
const roleList = ref([])
const treeList = ref([])
const selectTreeList = ref([])
const moduleList = ref([])
const moduleSelectedList = ref([])
const roleform = ref(null)
let rolesForm = ref({
  id: null,
  roleCode: '',
  roleName: '',
  roleExplain: '',
  onOff: '1',
  moduleids: '',
})
const ruleCheck = reactive({
  roleCode: [{
    required: true,
    trigger: 'blur',
    validator: roleCodeCheck
  }],
  roleName: [
    {
      required: true,
      trigger: 'blur',
      validator: roleNameCheck
    }
  ],
})
const checkblank = (e) => {
  e.target.value = e.target.value.replace(/\s+/g, "");
}
const setConditionFlag = (falg) => {
  tabPage.value.setheight()
}
const saveData = () => {
  rolesForm.value.moduleList = moduleList.value;
  if (specFlag.value === '1') {
    roleform.value.validate().then((flag) => {
      if (flag) {
        if (!rolesForm.value.moduleids || rolesForm.value.moduleids == '') {
          ElMessage.error("请为角色分配模块权限！");
          return false;
        }
        rolesForm.value.cModId = store.moduleId
        rolesForm.value.operateType = globalProxy.$consts.OPTYPE_UPDATE
        updateRole(rolesForm.value).then(() => {
          ElMessage.success("修改成功！");
          getRoleList();
        })
      }
    })
  } else {
    roleform.value.validate().then((flag) => {
      if (flag) {
        if (!rolesForm.value.moduleids || rolesForm.value.moduleids == '') {
          ElMessage.error("请为角色分配模块权限！");
          return false;
        }
        rolesForm.value.cModId = store.moduleId
        rolesForm.value.operateType = globalProxy.$consts.OPTYPE_INSERT
        addSystemRole(rolesForm.value).then(() => {
          ElMessage.success("新增成功！");
          getRoleList();
          resetData();
        })
      }
    })
  }
}
const deleteData = () => {
  if (!rolesForm.value.id || rolesForm.value.id == '') {
    ElMessage.error("请选择要删除的角色！");
    return false;
  }
  ElMessageBox.confirm('请问是否确认删除?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    let params = {
      id: rolesForm.value.id,
      cModId: store.moduleId,
      operateType: globalProxy.$consts.OPTYPE_DELETE
    }
    delRole(params).then(res => {
      ElMessage.success('删除成功！');
      getRoleList();
      resetData();
    })
  }).catch((res) => {
    ElMessage.info('已取消删除')
  })
}
const selectAllBtn = () => {
  moduleList.value.forEach(item => {
    if (item.canInsert) {
      item.insert = true;
    }
    if (item.canUpdate) {
      item.update = true;
    }
    if (item.canDelete) {
      item.delete = true;
    }
    if (item.canOutput) {
      item.output = true;
    }
  })
}
const cancleBtn = () => {
  moduleList.value.forEach(item => {
    if (item.canInsert) {
      item.insert = false;
    }
    if (item.canUpdate) {
      item.update = false;
    }
    if (item.canDelete) {
      item.delete = false;
    }
    if (item.canOutput) {
      item.output = false;
    }
  })
}
const getRoleList = () => {
  getRoleSelect().then(res => {
    roleList.value = res.data.data || [];
  })
}
const setChange = (modules) => {
  let arr = [];
  modules.forEach(item1 => {
    if (item1.children && item1.children.length > 0) {
      item1.children.forEach(item2 => {
        if (!item2.leaf && item2.children && item2.children.length > 0) {
          item2.children.forEach(item3 => {
            let isNew = true;
            let item = item3;
            let module = {
              id: item.id,
              name: item.name,
              code: item.code,
              canQuery: item.canQuery,
              canPrint: item.canPrint,
              canInsert: item.canInsert,
              canUpdate: item.canUpdate,
              canDelete: item.canDelete,
              canOutput: item.canOutput,
            };
            for (let n = 0; n < moduleSelectedList.value.length; n++) {
              if (moduleSelectedList.value[n].id == item3.id) {
                module.query = moduleSelectedList.value[n].query;
                module.print = moduleSelectedList.value[n].print;
                module.insert = moduleSelectedList.value[n].insert;
                module.update = moduleSelectedList.value[n].update;
                module.delete = moduleSelectedList.value[n].delete;
                module.output = moduleSelectedList.value[n].output;
                arr.push(module);
                isNew = false;
                break;
              }
            }
            if (isNew) {
              module.query = false;
              module.print = false;
              module.insert = false;
              module.update = false;
              module.delete = false;
              module.output = false;
              if (module.canQuery) module.query = true;
              if (module.canPrint) module.print = true;
              if (module.canInsert) module.insert = true;
              if (module.canUpdate) module.update = true;
              if (module.canDelete) module.delete = true;
              if (module.canOutput) module.output = true;
              arr.push(module);
            }
          })
        } else {
          let module = {};
          if (specFlag.value != '1' && moduleSelectedList.value.length == 0 && (!module.id || module.id == '')) {
            module = item2
            module.query = false;
            module.print = false;
            module.insert = false;
            module.update = false;
            module.delete = false;
            module.output = false;
            if (module.canQuery) module.query = true;
            if (module.canPrint) module.print = true;
            if (module.canInsert) module.insert = true;
            if (module.canUpdate) module.update = true;
            if (module.canDelete) module.delete = true;
            if (module.canOutput) module.output = true;
            arr.push(module);
          }
          let isNew = false;
          for (let n = 0; n < moduleSelectedList.value.length; n++) {
            isNew = true;
            let item = item2;
            module = {
              id: item.id,
              name: item.name,
              code: item.code,
              canQuery: item.canQuery,
              canPrint: item.canPrint,
              canInsert: item.canInsert,
              canUpdate: item.canUpdate,
              canDelete: item.canDelete,
              canOutput: item.canOutput,
            };
            if (moduleSelectedList.value[n].id == item2.id) {
              module.query = moduleSelectedList.value[n].query;
              module.print = moduleSelectedList.value[n].print;
              module.insert = moduleSelectedList.value[n].insert;
              module.update = moduleSelectedList.value[n].update;
              module.delete = moduleSelectedList.value[n].delete;
              module.output = moduleSelectedList.value[n].output;
              arr.push(module);
              isNew = false;
              break;
            }
          }
          if (isNew) {
            module.query = false;
            module.print = false;
            module.insert = false;
            module.update = false;
            module.delete = false;
            module.output = false;
            if (module.canQuery) module.query = true;
            if (module.canPrint) module.print = true;
            if (module.canInsert) module.insert = true;
            if (module.canUpdate) module.update = true;
            if (module.canDelete) module.delete = true;
            if (module.canOutput) module.output = true;
            arr.push(module);
          }
        }
      })
    }
  })
  moduleList.value = arr
}
const resetData = () => {
  rolesForm.value = {
    id: null,
    roleCode: '',
    roleName: '',
    roleExplain: '',
    onOff: '1',
    moduleids: '',
  }
  specFlag.value = '0';
  treeList.value = []
  nextTick(() => {
    treeList.value = allModule
    selectTreeList.value = []

  })
  moduleList.value = [];
  moduleSelectedList.value = [];
}
const rolesChange = (val) => {
  if (!val) {
    resetData();
  } else {
    specFlag.value = "1";
    getRoleDetial({
      id: val
    }).then(res => {
      // selSysModule, noSelSysModule 这两个暂时无用，后期可考虑去掉
      if (res) {
        rolesForm.value = res.data.data.roleInfo;
        moduleSelectedList.value = res.data.data.selModuleList;
        let selectIds = [];
        moduleSelectedList.value.forEach(item => {
          item.pid = item.supId;
          selectIds.push(item.id);
        });
        getSelected('', selectIds)
        rolesForm.value.moduleids = selectIds.join(",");
        selectTreeList.value = selectIds;
      }
    }).catch(() => {
    })
  }
}
const rowCheckAll = (row) => {
  if (row.canInsert) {
    row.insert = true
  }
  if (row.canUpdate) {
    row.update = true
  }
  if (row.canDelete) {
    row.delete = true
  }
  if (row.canOutput) {
    row.output = true
  }
}
const rowCancel = (row) => {
  if (row.canInsert) {
    row.insert = false
  }
  if (row.canUpdate) {
    row.update = false
  }
  if (row.canDelete) {
    row.delete = false
  }
  if (row.canOutput) {
    row.output = false
  }
}
const getSelected = (node, nodesKey) => {
  let data = getToData(allModule, nodesKey)
  setChange(data)
  rolesForm.value.moduleids = nodesKey.join(",");
};
const getToData = (moduleTree, selectModuleIds) => {
  if (!moduleTree || !selectModuleIds) {
    return []
  }
  let userData = selectModuleIds
  return JSON.parse(JSON.stringify(moduleTree)).filter(item1 => {
    if (item1.children && item1.children.length > 0) {
      item1.children = item1.children.filter(item2 => {
        if (item2.children && item2.children.length > 0) {
          item2.children = item2.children.filter(item3 => {
            return userData.some(selectid => {
              return selectid == item3.id
            })
          })
          return item2.children.length > 0
        }
        return userData.some(selectid => {
          return selectid == item2.id
        })
      })
      return item1.children.length > 0
    }
    return userData.some(selectid => {
      return selectid == item1.id
    })
  })
}
const queryAllRoleData = () => {
  let res = [
    {
      "id": "38",
      "roleCode": "030801",
      "roleName": "030801",
      "roleExplain": "030801",
      "onOff": "1",
      "updateDate": "2023-03-08 14:04:19",
      "updateUser": "超级管理员",
      "tableStatus": null
    },
    {
      "id": "9",
      "roleCode": "0922002",
      "roleName": "0922002",
      "roleExplain": "22222",
      "onOff": "1",
      "updateDate": "2021-09-22 09:57:01",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "18",
      "roleCode": "1103",
      "roleName": "角色名称",
      "roleExplain": "描述信息",
      "onOff": "0",
      "updateDate": "2021-11-08 16:27:13",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "23",
      "roleCode": "1108",
      "roleName": "1108",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-08 16:22:32",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "26",
      "roleCode": "1108-2",
      "roleName": "1108-2",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-08 16:24:45",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "24",
      "roleCode": "110801",
      "roleName": "110801",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-08 16:16:43",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "29",
      "roleCode": "1111-01",
      "roleName": "1111-01",
      "roleExplain": "1",
      "onOff": "1",
      "updateDate": "2021-11-11 10:36:58",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "39",
      "roleCode": "11122",
      "roleName": "11122",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2023-03-08 15:03:07",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "30",
      "roleCode": "1116-1",
      "roleName": "1116-1",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-16 09:34:51",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "35",
      "roleCode": "1119-1",
      "roleName": "1119-1",
      "roleExplain": null,
      "onOff": "0",
      "updateDate": "2021-11-19 16:02:35",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "32",
      "roleCode": "12",
      "roleName": "433",
      "roleExplain": "223",
      "onOff": "1",
      "updateDate": "2021-11-16 17:04:37",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "33",
      "roleCode": "123555",
      "roleName": "2224",
      "roleExplain": "333333",
      "onOff": "1",
      "updateDate": "2021-11-17 11:12:21",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "34",
      "roleCode": "2134-00001",
      "roleName": "111",
      "roleExplain": "44",
      "onOff": "1",
      "updateDate": "2021-11-17 11:12:47",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "31",
      "roleCode": "234",
      "roleName": "11",
      "roleExplain": "1234",
      "onOff": "1",
      "updateDate": "2021-11-16 17:02:59",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "28",
      "roleCode": "3435435",
      "roleName": "3435435",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-08 16:52:47",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "22",
      "roleCode": "55555555555",
      "roleName": "55555555555",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-05 15:58:11",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "12",
      "roleCode": "666",
      "roleName": "666",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-10-18 14:51:34",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "13",
      "roleCode": "6667",
      "roleName": "6667",
      "roleExplain": "6667",
      "onOff": "0",
      "updateDate": "2021-11-29 13:46:06",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "1",
      "roleCode": "ADMINISTRATS",
      "roleName": "超级管理员组",
      "roleExplain": "系统内置，拥有所有模块权限，不可更改删除。",
      "onOff": "1",
      "updateDate": "2023-10-16 16:04:35",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "4",
      "roleCode": "CITY_USER_GROUP",
      "roleName": "市级用户组",
      "roleExplain": "市级用户组",
      "onOff": "1",
      "updateDate": "2021-12-14 09:52:51",
      "updateUser": "银川市",
      "tableStatus": null
    },
    {
      "id": "16",
      "roleCode": "TEST",
      "roleName": "1",
      "roleExplain": null,
      "onOff": "1",
      "updateDate": "2021-11-02 08:36:31",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "2",
      "roleCode": "USER_GROUP",
      "roleName": "普通用户组",
      "roleExplain": "",
      "onOff": "1",
      "updateDate": "2023-05-11 16:23:48",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "3",
      "roleCode": "Y_USER_GROUP",
      "roleName": "县级用户组",
      "roleExplain": "",
      "onOff": "1",
      "updateDate": "2020-12-21 15:47:08",
      "updateUser": "超级管理员",
      "tableStatus": null
    },
    {
      "id": "37",
      "roleCode": "hanlina",
      "roleName": "韩丽娜测试",
      "roleExplain": "前端测试使用",
      "onOff": "0",
      "updateDate": "2023-08-14 10:11:05",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "15",
      "roleCode": "ningxia2021",
      "roleName": "2021交付功能",
      "roleExplain": "2021交付功能",
      "onOff": "1",
      "updateDate": "2021-10-27 10:04:23",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "5",
      "roleCode": "test",
      "roleName": "测试角色",
      "roleExplain": "新建测试",
      "onOff": "0",
      "updateDate": "2021-11-05 15:20:14",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "36",
      "roleCode": "test-tian1",
      "roleName": "测试tian1",
      "roleExplain": "测试tian1测试tian1测试tian",
      "onOff": "1",
      "updateDate": "2021-11-29 14:50:32",
      "updateUser": "超级管理员1",
      "tableStatus": null
    },
    {
      "id": "14",
      "roleCode": "wss_test1022",
      "roleName": "wss_test1022",
      "roleExplain": "wss_test1022",
      "onOff": "0",
      "updateDate": "2021-10-22 14:53:27",
      "updateUser": "超级管理员1",
      "tableStatus": null
    }
  ]
  res.forEach(item1 => {
    item1.pid = item1.supId
    item1.children && item1.children.forEach(item2 => {
      item2.pid = item2.supId
      item2.children && item2.children.forEach(item3 => {
        item3.pid = item3.supId
      })
    })
  })
  allModule = res
  treeList.value = res
  selectTreeList.value = []
  // 所有模块
  // getAllModuleList().then((res) => {
  //   res.data.data.forEach(item1 => {
  //     item1.pid = item1.supId
  //     item1.children && item1.children.forEach(item2 => {
  //       item2.pid = item2.supId
  //       item2.children && item2.children.forEach(item3 => {
  //         item3.pid = item3.supId
  //       })
  //     })
  //   })
  //   allModule = res.data.data
  //   treeList.value = res.data.data
  //   selectTreeList.value = []
  // })
}
onMounted(() => {
  getRoleList();
  queryAllRoleData()
})

</script>
<style scoped lang="scss">
.ml_10 {
  margin-left: 10px;
}

.w_294 {
  width: 294px;
}

.form {
  width: 500px;
  margin-right: 20px;
}

.item {
  width: 400px;
  height: 50px;
  box-shadow: 0px 0px 8px 1px #CCD9E6;
  border-radius: 6px;
  margin-left: 20px;
  margin-bottom: 16px;

  .label {
    width: 80px;
    height: 50px;
    margin-right: 10px;
    background: linear-gradient(91deg, #3385FF 0%, #4EDBD6 100%);
    box-shadow: 2px 0px 4px 1px #CCD9E6;
    border-radius: 6px;
    font-weight: 500;
    color: #ffffff;
    text-align: center;
    line-height: 50px;
  }
}

.rolesManage .op-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 22px 16px 0 16px;
  font-size: 14px;
  font-weight: bold;
  color: #03134E;

  .left::before {
    content: '';
    display: inline-block;
    width: 4px;
    height: 16px;
    margin-right: 6px;
    background: #3385FF;
    border-radius: 1px;
    vertical-align: bottom;
  }

  .right {
    .el-button {
      width: 74px;
      height: 28px;
      border-radius: 4px;
      border-color: #3385FF;
      color: #3385FF;
    }

    .el-button--primary {
      color: #ffffff;
      background: #3385FF;
    }
  }
}

.module-area {
  width: 100%;
  background: #fff;
}

.rolesManage .transfer {
  width: calc(100% - 520px);
}
</style>
<style>
.rolesManage .item .el-input {
  width: 294px !important;
}

.rolesManage .select-item-label {
  font-size: 12px !important;
  color: #213255 !important;
  width: 80px !important;
  line-height: 42px;
  text-align: right;
  margin-right: 10px;
}

.rolesManage .select-area-item {
  display: flex;
  width: 100% !important;
  margin-bottom: 20px !important;
  margin-left: -15px;
}

/*角色管理里权限左右移 未勾选数据时按钮禁用不可单击*/
.rolesManage .transfer .is-disabled {
  cursor: not-allowed !important;
  background-color: #C8C9CC !important;
  border: none !important;
}
</style>