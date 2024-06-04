<template>
  <el-dialog :title="isEdit ? '修改' : '新增'" v-model="visible" width="40%" :before-close="close"
    :close-on-click-modal="false">
    <div class="areaInfoDialog">
      <el-form label-width="160px" :model="areaData" ref="areaForm" :rules="!isEdit ? creatRules : editRules" inline>
        <el-form-item label="上级地区编码：" v-if="!isEdit">
          <ss-area-areaTreeStation style="width: 380px;" v-model="areaTreeValue" :getToLevelNum="3"
            @nodeClick="nodeClick" />
        </el-form-item>
        <el-form-item label="上级地区编码：" v-if="isEdit">
          <el-input style="width: 380px;" v-model.trim="areaData.supCode" placeholder="" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="上级地区名称：" v-if="isEdit">
          <el-input style="width: 380px;" v-model.trim="areaData.supAreaName" placeholder="" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="地区编码：" prop="areaCode">
          <el-input maxlength="20" style="width: 380px;" v-model.trim="areaData.areaCode" placeholder=""
            :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="地区名称：" prop="areaName">
          <el-input maxlength="30" style="width: 380px;" v-model.trim="areaData.areaName" placeholder="" />
        </el-form-item>
      </el-form>
    </div>
    <div class="dialog-footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="saveData">保存</el-button>
    </div>
  </el-dialog>
</template>
<script setup>
import { ElMessage } from 'element-plus'
import { getAreaById, insertArea, updateArea } from '@/api/apis/setting'
import { areaTree } from '@/utils/common/area.js'
import { mainStore } from "@/store/index";
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const props = defineProps({
  id: {
    type: String,
    default: ''
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  visible: {
    type: Boolean,
    required: true,
    default: false
  }
})
const validatororganCode = (rule, value, callback) => {
  const reg = /^[0-9]{2}$/
  if (!(value > 0 && value < 99 && reg.test(value))) {
    callback(new Error('接种单位编码范围从01-98,且必须为两位数'))
  } else {
    callback()
  }
}
const creatRules = {
  areaCode: {
    required: true,
    trigger: 'blur',
    validator: validatororganCode
  },
  areaName: {
    required: true,
    message: '地区名称为必输项，请填写完整再保存。',
    trigger: 'blur'
  }
}
const editRules = {
  areaName: {
    required: true,
    message: '地区名称为必输项，请填写完整再保存。',
    trigger: 'blur'
  }
}
const emits = defineEmits(['fetchData', 'close'])
const areaForm = ref(null);
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: store.currentLevel
});
let areaData = ref({
  areaCode: '',
  areaName: ''
});
const nodeClick = (node) => {
  areaTreeValue = node
}
const getArea = () => {
  if (!props.id || props.id === '') return false
  let res = {
    "sec": 0.03,
    "data": {
      "areaCode": "643600000000",
      "areaId": "2058",
      "supAreaName": "",
      "supAreaId": "1",
      "areaName": "滨州市",
      "supCode": "640000000000"
    }
  }
  areaData.value = res.data
  // getAreaById({ areaId: props.id })
  //   .then(res => {
  //     if (res && res.data.data) {
  //       areaData.value = res.data.data.data
  //     }
  //   })
  //   .catch(() => {
  //   })
}

const saveData = () => {
  areaForm.value.validate(flag => {
    if (flag) {
      if (props.isEdit) {
        const data = {
          supAreaId: areaData.value.supAreaId,
          areaName: areaData.value.areaName,
          id: areaData.value.areaId,
          operateType: globalProxy.$consts.OPTYPE_UPDATE,
          cModId: store.moduleId
        }
        updateArea(data).then(res => {
          ElMessage.success('修改成功')
          areaTree()
          emits('fetchData')
          close()
        })
      } else {
        const data = {
          supAreaLevel: areaTreeValue.selectAreaLevel,
          supAreaId: areaTreeValue.areaId,
          supAreaCode: areaTreeValue.areaCode,
          areaCode: areaData.value.areaCode,
          areaName: areaData.value.areaName,
          operateType: globalProxy.$consts.OPTYPE_INSERT,
          cModId: store.moduleId
        }
        insertArea(data).then(res => {
          if (res.data.code == 200) {
            ElMessage.success('新增成功')
            areaTree()
            emits('fetchData')
            close()
          } else {
            ElMessage.error(res.data.popMsg)
          }

        })
      }
    }
  })
}
const close = () => {
  emits('close')
}
watch(() => props.id, () => {
  getArea()
}, {
  deep: true,
  immediate: true,
})
</script>
<style scoped>
.dialog-footer {
  display: block;
  text-align: right;
}
</style>
