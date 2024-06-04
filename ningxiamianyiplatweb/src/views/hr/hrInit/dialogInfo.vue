<!-- 人力资源-人力资源管理弹框 -->
<template>
  <el-dialog :title="props.isEdit ? '编辑' : '新增'" v-model="visible" width="1000" :before-close="close"
    :close-on-click-modal="false" class="customDialog" :append-to-body="true" style="max-height:700px;overflow-y: auto;">
    <el-form ref="ruleFormRef" :rules="rules" :model="ruleForm" label-width="120px">
      <el-form-item label="地区" prop="areaName" v-if="!props.isEdit" label-width="200">
        <el-input :value="areaTreeValue.supCode.slice(0, -2) + ' ' + areaTreeValue.supAreaName" readonly disabled />
      </el-form-item>
      <el-form-item label="接种单位" prop="unitName" v-if="!props.isEdit" label-width="200">
        <el-input :value="areaTreeValue.areaCode.slice(0, -2) + ' ' + areaTreeValue.areaName" readonly disabled />
      </el-form-item>
      <el-form-item label="姓名" prop="name" label-width="200">
        <el-input v-model="ruleForm.name" clearable />
      </el-form-item>
      <el-form-item label="性别" label-width="200">
        <el-select v-model="ruleForm.gender" clearable>
          <el-option :label="item.name" :value="item.id" v-for="item in globalProxy.$commonOptions.genderSelect"
            :key="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="年龄" prop="age" label-width="200">
        <el-input v-model="ruleForm.age" maxlength="3" min="1" max="150"></el-input>
      </el-form-item>
      <el-form-item label="专/兼职" prop="positionType" label-width="200">
        <el-select v-model="ruleForm.positionType" clearable>
          <el-option :label="item.name" :value="item.id" v-for="item in globalProxy.$commonOptions.positionTypeSelect"
            :key="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="从事岗位" prop="post" label-width="200">
        <el-checkbox-group v-model="ruleForm.post">
          <el-checkbox :label="item.code" v-for="item in Position" :key="item.code">{{ item.name }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="工龄（全部）" prop="workAge" label-width="200">
        <el-input v-model="ruleForm.workAge" maxlength="3" min="1" max="150" />
      </el-form-item>
      <el-form-item label="工龄（免疫规划）" prop="immunityWorkingAge" label-width="200">
        <el-input v-model="ruleForm.immunityWorkingAge" maxlength="3" min="1" max="150" />
      </el-form-item>
      <el-form-item label="技术职称" prop="skillTitle" label-width="200">
        <el-select v-model="ruleForm.skillTitle">
          <el-option :label="item.name" :value="item.code" v-for="item in skillTitle" :key="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="学历" prop="degree" label-width="200">
        <el-select v-model="ruleForm.degree">
          <el-option :label="item.name" :value="item.code" v-for="item in Education" :key="item.code" />
        </el-select>
      </el-form-item>
      <el-form-item label="培训证有无" prop="certificate" label-width="200">
        <el-select v-model="ruleForm.certificate" @change="certiHandler">
          <el-option :label="item.name" :value="item.id" v-for="item in globalProxy.$commonOptions.certiFicate"
            :key="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="培训合格有效期至" prop="validDate" label-width="200">
        <el-date-picker v-model="ruleForm.validDate" :readonly="validDateDisabled" :disabled="validDateDisabled"
          type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width:234px;" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer" style="display: flex;justify-content: end;">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="onSubmit(ruleFormRef)">保 存</el-button>
    </span>
    <el-form-item label-width="200" class="dialog-footer">
      <ss-remark-text :remarkText=remarkText></ss-remark-text>
    </el-form-item>
  </el-dialog>
</template>
<script setup>
import { detail, getDicts, save } from "@/api/apis/hr";
import { getAreaById } from '@/api/apis/setting'
import { positive_interger, special_character } from "@/utils/common/regex";
import ssRemarkText from '@/components/common/RemarkText.vue'
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  areaTreeValue: {
    type: Object,
    default: {}
  },
  alterData: {
    type: Object,
    default: {}
  },
})
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const remarkText = ref('姓名不能包含特殊字符< > & \' \"');
const ruleForm = ref({
  age: '',
  buildBasecode: !props.isEdit ? props.areaTreeValue.supCode.slice(0, -2) : '',
  certificate: '',
  id: '',
  degree: '',
  gender: '',
  immunityWorkingAge: '',
  name: '',
  newBasecode: !props.isEdit ? props.areaTreeValue.areaCode.slice(0, -2) : '',
  positionType: '',
  post: [],
  skillTitle: '',
  validDate: '',
  workAge: '',
  unitId: props.alterData.unitId
})
const Position = ref(null);//从事岗位
const Education = ref(null);//学历
const Occupation = ref(null);//职业
const skillTitle = ref(null);//职业
const ruleFormRef = ref(null);
const areaTreeValue = ref(props.areaTreeValue);
const validDateDisabled = ref(false);//培训合格有效期至默认禁用

// 校验规则
const rules = reactive({
  name: [
    { required: true, message: '姓名必填且不能为空！', trigger: 'blur' },
    { pattern: special_character, message: `姓名不能包含特殊字符< > & ' "`, trigger: 'blur' }
  ],
});
const GetDicts = () => {
  getDicts().then(res => {
    if (res.data.code === 200) {
      Occupation.value = res.data.data.occupation;
      Education.value = res.data.data.education;
      Position.value = res.data.data.position;
      skillTitle.value = res.data.data.skillTitle;
    }
  })
}
const GetAreaById = () => {
  const params = {
    areaId: props.areaTreeValue.id
  }
  getAreaById(params).then(res => {
    if (res.data.code === 200) {
      areaTreeValue.value = res.data.data.data;
    }
  })
}
watch(() => props.visible, (val) => {
  if (val) {
    GetDicts()
    if (props.isEdit) {
      const params = {
        buildBaseCode: props.alterData.buildBasecode,
        id: props.alterData.id,
      }
      detail(params).then(res => {
        if (res.data.code === 200) {
          let post = res.data.data.data.post != '' ? res.data.data.data.post.split(",").map(item => item.trim()) : [];
          // let post = res.data.data.data.post != '' ? res.data.data.data.post.split(",").map(item => item.trim()) : [];
          ruleForm.value = {
            // ...res.data.data.data,
            age: res.data.data.data.age,
            buildBasecode: res.data.data.data.buildBasecode,
            certificate: res.data.data.data.certificate,
            id: res.data.data.data.id,
            degree: res.data.data.data.degree,
            gender: res.data.data.data.gender,
            immunityWorkingAge: res.data.data.data.immunityWorkingAge,
            name: res.data.data.data.name,
            newBasecode: res.data.data.data.newBasecode,
            positionType: res.data.data.data.positionType,
            skillTitle: res.data.data.data.skillTitle,
            validDate: res.data.data.data.validDate,
            workAge: res.data.data.data.workAge,
            post,
            unitId: props.alterData.unitId
          }
        }
      })
    } else {
      GetAreaById()
    }
  };
}, { deep: true, immediate: true })

const emits = defineEmits(['fetchData', 'close']);
// 取消
const close = () => {
  emits('fetchData')
  emits('close', true);
}
// 培训证有无监听
const certiHandler = (val) => {
  if (val == 2) {
    ruleForm.value.validDate = '';
    validDateDisabled.value = true;
  } else if (val == 1) {
    validDateDisabled.value = false;
  }
}
// 保存
const onSubmit = (ruleFormRef) => {
  ruleFormRef.validate((valid) => {
    if (valid) {
      let age = ruleForm.value.age;
      let post = ruleForm.value.post.join(",");
      let workAge = ruleForm.value.workAge;
      let immunityWorkingAge = ruleForm.value.immunityWorkingAge;
      if (age != '' && !positive_interger.test(age)) return globalProxy.$util.showMsg('年龄应输入0~150之间的正整数！', 'error', 5000, true);
      if (workAge != '' && !positive_interger.test(workAge)) return globalProxy.$util.showMsg('工龄(全部)应输入0~150之间的正整数！', 'error', 5000, true);
      if (immunityWorkingAge != '' && !positive_interger.test(immunityWorkingAge)) return globalProxy.$util.showMsg('工龄(免疫规划)应输入0~150之间的正整数！', 'error', 5000, true);
      if (Number(workAge) > Number(age)) return globalProxy.$util.showMsg('工龄（全部）应小于年龄！', 'error', 5000, true);
      if (Number(immunityWorkingAge) > Number(age)) return globalProxy.$util.showMsg('工龄（免疫规划）应小于年龄！', 'error', 5000, true);
      if (Number(immunityWorkingAge) > Number(workAge)) return globalProxy.$util.showMsg('工龄（免疫规划）应小于工龄（全部）！', 'error', 5000, true);
      if (ruleForm.value.certificate == '1' && !ruleForm.value.validDate) return globalProxy.$util.showMsg('证书有效期终止时间必填且不能为空！', 'error', 5000, true);
      const params = {
        ...ruleForm.value,
        post,
      }
      save(params).then(res => {
        if (res.data.code === 200) {
          close();
          return globalProxy.$util.showMsg(res.data.data.data || '修改成功！', 'success', 5000, true);
        } else {
          return globalProxy.$util.showMsg(res.data.data.data || '修改失败！', 'error', 5000, true);
        }
      })
    }
  })
}
</script>