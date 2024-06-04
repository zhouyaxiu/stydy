<!-- 质量控制-上传数据质量统计 -->
<template>
  <el-dialog :title="isEdit ? '投诉管理编辑' : '投诉管理新增'" v-model="visible" width="1000" :before-close="close"
    :close-on-click-modal="false" class="customDialog" :append-to-body="true" style="max-height: 700px;overflow-y: auto;">
    <el-form ref="ruleFormRef" :rules="rules" :model="ruleForm" label-width="120px">
      <el-form-item label="被投诉单位：" required label-width="200">
        <ss-area-areaTreeStation v-model="areaTreeValue" :getToLevelNum="5" @nodeClick="nodeClick" clearable
          v-if="!isEdit"></ss-area-areaTreeStation>
        <el-input v-model="ruleForm.organName" maxlength="50" readonly="true" v-else />
      </el-form-item>
      <el-form-item label="投诉主题：" prop="theme" required label-width="200">
        <el-input v-model="ruleForm.theme" maxlength="50" :readonly="isEdit ? true : false" />
      </el-form-item>
      <el-form-item label="投诉内容：" prop="content" label-width="200">
        <el-input v-model="ruleForm.content" maxlength="500" show-word-limit rows="6" type="textarea"
          :readonly="isEdit ? true : false" />
      </el-form-item>
      <el-form-item label="投诉日期：" prop="complainTime" label-width="200">
        <el-date-picker v-model="ruleForm.complainTime" type="date" :disabled-date="disabledDate"
          :readonly="isEdit ? true : false" />
      </el-form-item>
      <el-form-item label="记录人员：" prop="userId" label-width="200" v-if="isEdit">
        <el-input v-model="ruleForm.userName" maxlength="50" :readonly="isEdit ? true : false" />
      </el-form-item>
      <el-form-item label-width="200" class="dialog-footer">
        <el-button @click="close" v-if="isEdit">关 闭</el-button>
        <el-button type="primary" @click="onSubmit(ruleFormRef)" v-if="!isEdit">保 存</el-button>
        <el-button @click="close" v-if="!isEdit">取 消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>
<script setup>
import { mainStore } from "@/store/index";
const store = mainStore();
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
import { queryComlainDetail, saveComplain } from "@/api/apis/setting";
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  id: {
    type: String,
    default: ''
  }
})

let areaTreeValue = reactive({
  areaCode: '',
  selectAreaLevel: sessionStorage.getItem("currentLevel")
});
const nodeClick = (node) => {
  areaTreeValue = node;
};
const ruleForm = ref({
  organName: '',//被投诉单位
  theme: '',//投诉主题
  content: '',//投诉内容
  complainTime: globalProxy.$dateUtil.currentDate(),//投诉日期
  userName: '',//记录人员
})
const disabledDate = (time) => {
  return time.getTime() > Date.now();
}
const ruleFormRef = ref(null);
// 校验规则
const rules = reactive({
  theme: [
    { required: true, message: '投诉主题必填且不能为空！', trigger: 'blur' },
  ],
  content: [
    { required: true, message: '投诉内容必填且不能为空！', trigger: 'blur' },
  ],
});
watch(() => props.visible, (val) => {
  if (val) {
    if (props.isEdit) {
      const params = {
        id: props.id,
      }
      queryComlainDetail(params).then(res => {
        if (res.data.code === 200) {
          ruleForm.value = res.data.data.data
        }
      })
    } else {
      ruleForm.value = {
        organName: '',//被投诉单位
        theme: '',//投诉主题
        content: '',//投诉内容
        complainTime: globalProxy.$dateUtil.currentDate(),//投诉日期
        userName: '',//记录人员
      }
    }
  };
}, { deep: true, immediate: true })

const emits = defineEmits(['close']);
// 取消
const close = () => {
  emits('close', true);
}

// 保存
const onSubmit = (ruleFormRef) => {
  ruleFormRef.validate((valid, fields) => {
    if (valid) {
      if (areaTreeValue.selectAreaLevel != '4') {
        return globalProxy.$util.showMsg('请选择接种单位！', 'error')
      }
      const params = {
        userId: store.userInfo.id,
        areaCode: store.userInfo.areaCode,
        organCode: areaTreeValue.areaCode,
        theme: ruleForm.value.theme,
        content: ruleForm.value.content,
        time: ruleForm.value.complainTime,
      }
      saveComplain(params).then(res => {
        if (res.data.code === 200) {
          close()
          return globalProxy.$util.showMsg(res.data.data.data || '新增成功！', 'success');
        } else {
          close()
          return globalProxy.$util.showMsg(res.data.data.popMsg || '新增失败！', 'error');
        }
      })
    }
  })
}
</script>