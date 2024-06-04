<template>
  <el-dialog :title="isEdit ? '修改' : '新增'" v-model="editVisible" width="55%" :before-close="handleClose">
    <div class="replace-add inoculationUnit_editForm" style="margin: -20px 0;" v-loading='loading'>
      <el-form ref="ruleForm" :rules="formRules" label-width="150px" :model="inoculationUnit">
        <div style="margin-left: -30px" class="w_50">
          <el-form-item label="上级地区编码：" prop="supCode" v-if="isEdit">
            <el-input v-model="inoculationUnit.supCode" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="上级地区名称：" prop="commName" v-if="isEdit">
            <el-input v-model="inoculationUnit.commName" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="上级地区名称：" prop="commName" v-if="!isEdit">
            <ss-area-areaTreeStation :defTreeValue="areaTreeValue.areaId" v-model="areaTreeValue"
              :getToLevelNum="4 - store.currentLevel" @nodeClick="nodeClick" />
          </el-form-item>
          <el-form-item label="接种单位编码：" prop="organCode">
            <el-input v-model="inoculationUnit.organCode" :disabled="isEdit"></el-input>
          </el-form-item>
          <el-form-item label="接种单位名称：" prop="organName">
            <el-input v-model="inoculationUnit.organName"></el-input>
          </el-form-item>
          <el-form-item label="地址：" prop="organAddress">
            <el-input v-model="inoculationUnit.organAddress" maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="电话：" prop="linkPhone">
            <el-input v-model="inoculationUnit.linkPhone" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="联系人：" prop="linkMan">
            <el-input v-model="inoculationUnit.linkMan" maxlength="30"></el-input>
          </el-form-item>
          <el-form-item label="门诊日：" prop="clinicDate">
            <el-input v-model="inoculationUnit.clinicDate" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="启用日期：" prop="beginDate">
            <el-date-picker v-model="inoculationUnit.beginDate" :disabled-date="pickerOptions" :format="'YYYY-MM-DD'"
              :value-format="'YYYY-MM-DD'" @change="changeTime($event, 'endDate')"></el-date-picker>
          </el-form-item>
          <el-form-item label="停用日期：" prop="endDate">
            <el-date-picker v-model="inoculationUnit.endDate" :disabled-date="endPickerOptions"
              @change="changeTime($event, 'beginDate')" :clearable="false" :format="'YYYY-MM-DD'"
              :value-format="'YYYY-MM-DD'"></el-date-picker>
          </el-form-item>
        </div>
        <div class="w_50">
          <el-form-item label="客户端识别码：" prop="newBaseCode">
            <el-input v-model="inoculationUnit.newBaseCode" :disabled="isEdit"></el-input>
          </el-form-item>
          <el-form-item label="上传密码：" prop="passWord" class="edit_formcheck_setting">
            <el-input v-model="inoculationUnit.passWord" type="password" show-password></el-input>
          </el-form-item>
          <el-form-item label="建筑面积：" prop="buildArea">
            <el-input v-model="inoculationUnit.buildArea">
              <template #append>m²</template>
            </el-input>
          </el-form-item>
          <el-form-item label="地图坐标：" prop="longitude">
            <el-input v-model="inoculationUnit.longitude"><template #prepend>经度</template></el-input>
          </el-form-item>
          <el-form-item label="" prop="dimension">
            <el-input v-model="inoculationUnit.dimension"><template #prepend>纬度</template></el-input>
          </el-form-item>
          <el-form-item label="服务周期：" prop="clinicType">
            <el-select v-model="inoculationUnit.clinicType" clearable filterable style="width:100%">
              <el-option v-for="i in globalProxy.$commonOptions.clinicTypeList" :key="i.code" :value="i.code"
                :label="i.name" />
            </el-select>
          </el-form-item>
          <el-form-item label="服务类型：" prop="serviceType">
            <el-select v-model="inoculationUnit.serviceType" clearable filterable style="width:100%">
              <el-option v-for="i in globalProxy.$commonOptions.fwTypeList" :key="i.code" :value="i.code"
                :label="i.name" />
            </el-select>
          </el-form-item>
          <el-form-item label="工作类型：" prop="workType">
            <el-select v-model="inoculationUnit.workType" clearable style="width:100%">
              <el-option v-for="i in globalProxy.$commonOptions.workTypeList" :key="i.code" :value="i.code"
                :label="i.name" />
            </el-select>
          </el-form-item>
          <el-form-item prop="checksetting" class="edit_formcheck_setting">
            <el-checkbox v-model="inoculationUnit.checksetting">是否允许上传</el-checkbox>
          </el-form-item>
        </div>
      </el-form>
    </div>
    <span class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script setup>
import { ElMessage } from 'element-plus'
import { editInoculationUnit, addInoculationUnit, getInoculationUnitDetail } from '@/api/apis/setting'
import { areaTree } from '@/utils/common/area.js'
import { mainStore } from "@/store/index";
import { nextTick } from 'vue';
import md5 from 'js-md5'
const store = mainStore()
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;
const props = defineProps({
  // 是否编辑
  isEdit: {
    type: Boolean,
    default: false
  },
  editVisible: {
    type: Boolean,
    default: false
  },
  // 编辑状态下的id
  curId: {
    type: String,
    default: ''
  }
})
// 接种单位编码校验
const validatororganCode = (rule, value, callback) => {
  const reg = /^[0-9]{2}$/
  if (!props.isEdit && !(value > 0 && value < 99 && reg.test(value))) {
    callback(new Error('范围从01-98，且必须为两位数，同一乡镇下不能重复'))
  } else {
    callback()
  }
}
// 接种单位名称校验
const validatororganName = (rule, value, callback) => {
  const reg = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/
  if (!reg.test(value)) {
    callback(new Error('不可输入特殊字符，同一乡镇下不能相同'))
  } else {
    callback()
  }
}
// 建筑面积校验
const validatorJzdwArea = (rule, value, callback) => {
  const reg = /^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/
  if (!reg.test(value)) {
    callback(new Error('请输入正整数或保留两位小数的正数'))
  } else if (!(value <= 9999999999.99)) {
    callback(new Error('请输入不大于9999999999.99的正数'))
  } else {
    callback()
  }
}
// 经度校验
const validatorLongitude = (rule, value, callback) => {
  const reg = /^[\-\+]?(0?\d{1,2}(\.\d{1,6})*|1[0-7]?\d{1}(\.\d{1,6})*|180(\.0{1,6})*)$/
  if (value && !reg.test(value)) {
    callback(new Error('-180.0～+180.0，最多保留6位小数，或输入整数'))
  } else {
    callback()
  }
}
// 纬度校验
const validatorLatitude = (rule, value, callback) => {
  const reg = /^[\-\+]?([0-8]?\d{1}(\.\d{1,6})*|90(\.0{1,6})*)$/
  if (value && !reg.test(value)) {
    callback(new Error('-90.0～+90.0，最多保留6位小数位，或输入整数'))
  } else {
    callback()
  }
}
// 电话号码校验
const validatePhoneNum = (rule, value, callback) => {
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  const telephoneRegex = /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/
  if (value && !reg.test(value) && !telephoneRegex.test(value)) {
    callback(new Error('电话号格式不正确'))
  }
  callback()
}
// 表单校验定义
const formRules = {
  commName: [
    {
      required: true,
      message: '上级地区项只能选择乡镇（街道）',
      trigger: 'change'
    }
  ],
  organCode: [
    {
      required: true,
      trigger: 'blur',
      validator: validatororganCode
    }
  ],
  organName: [
    {
      required: true,
      trigger: 'blur',
      validator: validatororganName
    }
  ],
  beginDate: [
    {
      required: true,
      message: '启用日期项为必选项，请填写完整再保存！',
      trigger: 'blur'
    }
  ],
  endDate: [
    {
      required: true,
      message: '停用日期项为必选项，请填写完整再保存！',
      trigger: 'blur'
    }
  ],
  buildArea: [
    {
      required: true,
      trigger: 'blur',
      validator: validatorJzdwArea
    }
  ],
  longitude: [
    {
      required: false,
      trigger: 'blur',
      validator: validatorLongitude
    }
  ],
  dimension: [
    {
      required: false,
      trigger: 'blur',
      validator: validatorLatitude
    }
  ],
  linkPhone: [
    {
      required: false,
      trigger: 'blur',
      validator: validatePhoneNum
    }
  ],
  newBaseCode: [
    {
      required: true,
      pattern: /^\d{10}$/,
      message: '客户端识别码必须为10位数字，请填写完整再保存！',
      trigger: 'blur'
    }
  ],
  passWord: [
    {
      required: true,
      message: '上传密码项为必选项，请填写完整再保存！',
      trigger: 'blur'
    }
  ],
  serviceType: [
    {
      required: true,
      message: '服务类型项为必选项，请填写完整再保存！',
      trigger: 'change'
    }
  ],
  workType: [
    {
      required: true,
      message: '工作类型项为必选项，请填写完整再保存！',
      trigger: 'change'
    }
  ]
}
const emits = defineEmits(['closeDialog'])
const ruleForm = ref(null)
const loading = ref(false)
let inoculationUnit = ref(
  {
    passWord: '',
    supId: store.userInfo.areaId,
    supCode: store.userInfo.areaCode,
    supAreaLevel: store.currentLevel,
    organCode: '',
    organName: '',
    organAddress: '',
    linkPhone: '',
    linkMan: '',
    clinicDate: '',
    beginDate: globalProxy.$dateUtil.currentDate(),
    endDate: globalProxy.$dateUtil.getAfterNYearsDateStr(10),
    newBaseCode: '',
    buildArea: '',
    clinicType: '',
    serviceType: '',
    workType: '',
    longitude: '',
    dimension: '',
    businessType: '',
    checksetting: true
  }
)
// 停用日期限制，默认系统当前日期+10年，启用日期与停用日期间隔<=10年并且启用日期在停用日期之前
const endPickerOptions = (time) => {
  let time1 = (Number(inoculationUnit.value.beginDate.substr(0, 4)) + 10).toString() + inoculationUnit.value.beginDate.substr(4, 10)
  return time.getTime() > new Date(time1).getTime()
}
// 启用日期限制，默认系统当前日期
const pickerOptions = (time) => {
  let time1 = (Number(inoculationUnit.value.endDate.substr(0, 4)) - 10).toString() + inoculationUnit.value.endDate.substr(4, 10)
  return (time.getTime() < new Date(time1).getTime() || time.getTime() > new Date(inoculationUnit.value.endDate).getTime()
  )
}
let areaTreeValue = reactive({
  areaId: store.userInfo.areaId,
  areaCode: store.userInfo.areaCode,
  selectAreaLevel: store.currentLevel
});
let formDetail = reactive({})

watch(() => props.editVisible, (val) => {
  if (val) {
    if (props.isEdit) {
      nextTick(() => {
        getFormDetail()
      })
    } else {
      nextTick(() => {
        inoculationUnit.value.checksetting = true
      })
    }
  }
}, {
  deep: true,
  immediate: true,
})
// 选择上级单位
const nodeClick = (node) => {
  if (node.selectAreaLevel !== '3') {
    areaTreeValue.areaId = node.id
    nextTick(() => {
      areaTreeValue.areaId = store.userInfo.areaId
    });
    ElMessage.error("只能选择乡镇")
  } else {
    areaTreeValue = node
    inoculationUnit.value.commName = node.areaName
    inoculationUnit.value.supId = node.id
    inoculationUnit.value.supCode = node.areaCode
    inoculationUnit.value.supAreaLevel = node.selectAreaLevel
  }
}
// 启用日期和停用日期输入
const changeTime = (time, key) => {
  let date1 = new Date(inoculationUnit.value.beginDate)
  let date2 = new Date(inoculationUnit.value.endDate)
  if (
    date2.getTime() - date1.getTime() >
    10 * 365 * 24 * 60 * 60 * 1000 + 2 * 24 * 3600 * 1000
  ) {
    ElMessage.error("启用日期与停用日期不能大于10年")
    inoculationUnit.value.endDate = ''
  }
  if (date1.getTime() > date2.getTime()) {
    ElMessage.error("启用日期不能大于停用日期")
    inoculationUnit.value.endDate = ''
  }
}
// 编辑获取详情
const getFormDetail = () => {
  loading.value = true
  getInoculationUnitDetail({ organCode: props.curId })
    .then(res => {
      loading.value = false
      formDetail = JSON.parse(JSON.stringify(res.data.data.data)) || {}
      inoculationUnit.value = {
        supId: formDetail.supId,
        supCode: formDetail.supCode,
        commName: formDetail.supName,
        organCode: formDetail.organCode || '',
        organName: formDetail.organName || '',
        organAddress: formDetail.organAddress || '',
        linkPhone: formDetail.linkPhone || '',
        linkMan: formDetail.linkMan || '',
        clinicDate: formDetail.clinicDay || '',
        beginDate: formDetail.beginDate || '',
        endDate: formDetail.endDate || '',
        buildArea: formDetail.buildingArea || '',
        newBaseCode: formDetail.newBaseCode || '',
        checksetting: isTobe(formDetail, 'mark'),
        dimension: formDetail.dimension || '',
        longitude: formDetail.longitude || '',
        passWord: formDetail.password || '',
        clinicType: formDetail.clinicType ? globalProxy.$commonOptions.clinicTypeList.filter(item => item.name == formDetail.clinicType)[0].code : '',
        serviceType: formDetail.serviceType ? globalProxy.$commonOptions.fwTypeList.filter(item => item.name == formDetail.serviceType)[0].code : '',
        workType: formDetail.workType ? globalProxy.$commonOptions.workTypeList.filter(item => item.name == formDetail.workType)[0].code : '',
      }
    })
    .catch(() => {
      loading.value = false
    })
}
const isTobe = (obj, key) => {
  if (obj[key] === null) {
    return ''
  } else {
    return obj[key] === '1'
  }
}
// 关闭弹窗
const handleClose = (flag) => {
  ruleForm.value.resetFields()
  formDetail = {}
  emits('closeDialog', flag)
}
// 新增或编辑确认操作
const confirm = () => {
  if (!props.isEdit && inoculationUnit.value.supAreaLevel !== '3') {
    ElMessage.error("上级地区项只能选择乡镇（街道）")
  }
  inoculationUnit.value.cModID = store.moduleId
  ruleForm.value.validate(valid => {
    if (valid) {
      // 编辑
      if (props.isEdit) {
        const params = JSON.parse(JSON.stringify(inoculationUnit.value))
        delete params.checksetting
        delete params.commName
        params.mark = inoculationUnit.value.checksetting ? '1' : '0'
        params.id = formDetail.id
        params.passWord = formDetail.password === inoculationUnit.value.passWord ? formDetail.password : md5(inoculationUnit.value.passWord)
        params.operateType = globalProxy.$consts.OPTYPE_UPDATE
        loading.value = true
        editInoculationUnit(params)
          .then(res => {
            loading.value = false
            if (res.data.code === 200) {
              ElMessage({
                message: res.data.data.data,
                type: 'success'
              })
              areaTree()
              handleClose(true)
            } else {
              ElMessage.error(res.data.popMsg)
            }
          })
          .catch(() => {
            loading.value = false
          })
      } else {
        // 新增
        const params = JSON.parse(JSON.stringify(inoculationUnit.value))
        delete params.checksetting
        params.mark = inoculationUnit.value.checksetting ? '1' : '0'
        params.passWord = md5(inoculationUnit.value.passWord)
        params.operateType = globalProxy.$consts.OPTYPE_INSERT
        loading.value = true
        addInoculationUnit(params)
          .then(res => {
            loading.value = false
            if (res.data.code === 200) {
              areaTree()
              ElMessage.success(res.data.data.data)
              handleClose(true)
            } else {
              ElMessage.error(res.data.popMsg)
            }
          })
          .catch(() => {
            loading.value = false
          })
      }
    }
  })
}
</script>

<style scoped>
.w_50 {
  width: 45%;
}

.dialog-footer {
  margin-right: 46px;
  display: block;
  text-align: right;
}

::v-deep .mapInput .el-input__inner {
  width: 40px;
}

.inoculationUnit_editForm .el-form {
  display: flex;
  justify-content: space-around;
}

::v-deep .inoculationUnit_editForm .el-form .el-form-item__content .china-tree .areaInput {
  width: 100%;
}

::v-deep .inoculationUnit_editForm .el-form .el-input {
  width: 100%;
}

::v-deep .inoculationUnit_editForm .el-form .el-form-item .el-date-editor {
  width: 100%;
}
</style>
