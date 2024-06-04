<template>
  <!-- 删除某接种单位数据 -->
  <div>
    <ss-condition-area :isShowBtn="false"></ss-condition-area>
    <ss-custom-area class="flex">
      <div class="tree-area">
        <ss-area-areaTreeExpand @nodeClick="nodeClick" class="tree" ref="treeRef" />
      </div>
      <div class="form-detial">
        <el-form ref="delForm" :form="delUnitForm" label-width="200px">
          <el-form-item label="上级行政区划:">
            <el-input v-model="delUnitForm.areaName" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="接种单位编码:">
            <el-input v-model="delUnitForm.organCode" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="接种单位名称:">
            <el-input v-model="delUnitForm.organName" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="地址:">
            <el-input v-model="delUnitForm.organAddress" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="电话:">
            <el-input v-model="delUnitForm.linkPhone" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="联系人:">
            <el-input v-model="delUnitForm.linkMan" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="门诊日:">
            <el-input v-model="delUnitForm.clinicDay" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="启用日期:">
            <el-input v-model="delUnitForm.beginDate" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="停用日期:">
            <el-input v-model="delUnitForm.endDate" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="客户端识别码:">
            <el-input v-model="delUnitForm.newBaseCode" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="上传密码:">
            <el-input type="password" v-model="delUnitForm.password" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="delUnitForm.checksetting" disabled label="是否允许上传" name="type"></el-checkbox>
          </el-form-item>
          <el-form-item>
            <ss-custom-button :type="'delete'" @click="del" :disabled="!isCanDel"></ss-custom-button>
          </el-form-item>
        </el-form>
      </div>
    </ss-custom-area>
  </div>
</template>
<script setup>
import { ElMessage, ElMessageBox } from 'element-plus'
import { getInoculationUnitDetail, deleteStationAllMess } from '@/api/apis/setting'
import { areaTree } from '@/utils/common/area.js'
import { mainStore } from "@/store/index";
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const store = mainStore()
let delUnitForm = ref({})
const node = ref(null)
const areaId = ref(null)
const delForm = ref(null)
const treeRef = ref(null)
const isCanDel = ref('')
const del = () => {
  ElMessageBox.confirm('请确认是否删除?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    center: true
  })
    .then(() => {
      deleteStationAllMess({
        id: areaId.value,
        operateType: globalProxy.$consts.OPTYPE_DELETE,
        cModID: store.moduleId
      }).then(res => {
        if (res.data.code === 200) {
          areaTree()
          ElMessage.success(res.data.data.data)
          delUnitForm.value = {}
        } else {
          ElMessage.error(res.data.popMsg)
        }
      })
    })
    .catch(() => {
      ElMessage.info('已取消删除')
    })
}
const nodeClick = (data) => {
  if (data.selectAreaLevel != '4') {
    return
  }
  node.value = data
  areaId.value = data.areaId
  isCanDel.value = data.leaf
  if (data.leaf) {
    getInoculationUnitDetail({ organCode: data.areaCode }).then(res => {
      if (res.data.code === 200) {
        delUnitForm.value = res.data.data.data
        delUnitForm.value.checksetting = delUnitForm.value.mark === '1'
        delUnitForm.value.areaName = delUnitForm.value.supName
      } else {
        delUnitForm.value = {}
        ElMessage.error(res.data.popMsg)
      }
    }).catch(() => {
      delUnitForm.value = {}
    })
  } else {
    delUnitForm.value = {}
  }
}
</script>
<style scoped lang="scss">
.tree-area {
  width: 592px;
  height: calc(100% - 20px);
  padding: 16px;
  margin-left: 50px;
  overflow: hidden;
  background: #ECF6FF;
  box-shadow: 0px 4px 8px 1px #E1E6EC;
  border-radius: 14px;

  .tree {
    height: 100%;
    padding: 40px;
    border-radius: 10px;
    background: #ffffff;
    overflow: auto;
  }
}

.form-detial {
  width: 700px;
  padding-top: 40px;
  background: #FFFFFF;
  box-shadow: 0px 4px 8px 1px #E1E6EC;
  border-radius: 0px 14px 14px 0px;
  border: 1px solid #E1F0FF;

  .el-input {
    width: 360px;
  }
}
</style>
