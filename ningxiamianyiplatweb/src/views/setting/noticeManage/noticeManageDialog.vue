<template>
  <el-dialog :title="isEdit ? '修改' : '新增'" v-model="visible" width="38%" :before-close="close"
    :close-on-click-modal="false" class="noticeManageFormDialog">
    <div>
      <el-form label-width="180px" :model="unitManageForm" ref="unitFormRef" :rules="rules">
        <el-form-item label="标题:" prop="title">
          <el-input v-model.trim="unitManageForm.title"></el-input>
        </el-form-item>
        <el-form-item label="发布范围:" prop="areaRange">
          <ss-area-areaTreeStation v-if="!isEdit && visible" :defTreeValue="areaCheckValue" :getToLevelNum="3"
            :multiple='true' @checkClick="nodeChecked" />
          <ss-area-areaTreeStation v-else :getToLevelNum="3" :defTreeValue="areaTreeValue.areaId" v-model="areaTreeValue"
            @nodeClick="nodeClick" />
        </el-form-item>
        <el-form-item label="有效期限:" prop="validDate">
          <el-date-picker style="width: 100%;" :clearable="false" :disabled="unitManageForm.validDate === null"
            v-model="unitManageForm.validDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="validEver">永久有效</el-checkbox>
          <el-checkbox v-model="unitManageForm.valid">保留该信息</el-checkbox>
        </el-form-item>
        <el-form-item label="详细内容:" prop="detail">
          <el-input :rows="5" type="textarea" clearable maxlength="1000" show-word-limit
            v-model.trim="unitManageForm.detail"></el-input>
        </el-form-item>
      </el-form>
    </div>
    <span class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </span>
  </el-dialog>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { getNoticeDetial, updateNotice, addNotice } from '@/api/apis/setting'
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  curId: {
    type: [Number, String],
    default: null
  }
})
const validateAreaName = (rule, value, callback) => {
  callback()
}
const emits = defineEmits(['fetchData', 'close'])
const unitFormRef = ref(null)
const tempDate = ref('')
const distCode = ref([])
const validEver = ref(false)
const areaCheckValue = ref('')
let areaTreeValue = reactive({
  areaId: ''
});
let unitManageForm = ref({
  valid: true,
  validDate: []
})
const rules = {
  title: { required: true, message: '请输入', trigger: 'blur' },
  detail: { required: true, message: '请输入', trigger: 'blur' },
  areaRange: [
    {
      required: true,
      // message: '发布范围为必输项，请填写完整再保存！'
      validator: validateAreaName
    }
  ]
}
watch(() => props.visible, (val) => {
  if (val) {
    if (props.isEdit) {
      getNoticeDetial({ id: props.curId }).then(res => {
        unitManageForm.value = res.data.data
        validEver.value = unitManageForm.value.validDate === null
        unitManageForm.value.valid = unitManageForm.value.valid === '1'
        areaTreeValue.areaId = unitManageForm.value.distName
        distCode.value = unitManageForm.value.distId
        tempDate.value = unitManageForm.value.validDate
      })
    } else {
      unitManageForm.value.valid = true
      unitManageForm.value.validDate = globalProxy.$dateUtil.currentDate()
    }
  }
}, {
  deep: true,
  immediate: true,
})
watch(() => validEver.value, (val) => {
  if (val) {
    unitManageForm.value.validDate = null
  } else {
    if (!props.isEdit) {
      unitManageForm.value.validDate = globalProxy.$dateUtil.currentDate()
    } else {
      if (tempDate.value === null) {
        unitManageForm.value.validDate = globalProxy.$dateUtil.currentDate()
      } else {
        unitManageForm.value.validDate = tempDate.value
      }
    }
  }
}, {
  deep: true,
  immediate: true,
})
// 新增时发布范围数据
const nodeChecked = (data) => {
  unitManageForm.value.areaRange = data
  distCode.value = data.join()
}
// 编辑时发布范围数据
const nodeClick = (data) => {
  areaTreeValue = data
  distCode.value = data.id
}
// 发布对象下拉框
const save = () => {
  if (!distCode.value.length) {
    ElMessage.error('请选择发布范围')
    return false
  }
  const data = {
    ...unitManageForm.value,
    cModId: store.moduleId,
    distId: distCode.value,
    valid: unitManageForm.value.valid === true ? '1' : '0',
    fbUser: store.userInfo.id,
    fbDistId: store.userInfo.areaId,
    fbdw: store.userInfo.areaName + ' ' + store.userInfo.areaCode,
  }
  unitFormRef.value.validate(valid => {
    if (valid) {
      if (props.isEdit) {
        // 编辑
        updateNotice({ ...data, id: props.curId })
          .then(res => {
            ElMessage.success(res.data.msg)
            emits('fetchData')
            close()
          })
          .catch(() => {
            close()
          })
      } else {
        // 新增
        addNotice(data)
          .then(res => {
            ElMessage.success(res.data.msg)
            emits('fetchData')
            close()
          })
          .catch(() => {
            close()
          })
      }
    }
  })
}
const close = () => {
  unitFormRef.value.resetFields()
  emits('close')
}
</script>
<style lang="scss">
.noticeManageFormDialog {

  .ss-tree,
  .el-select {
    width: 100%;
  }

  .dialog-footer {
    display: block;
    text-align: right;
  }

  .el-form {
    margin-right: 110px;
  }
}
</style>
