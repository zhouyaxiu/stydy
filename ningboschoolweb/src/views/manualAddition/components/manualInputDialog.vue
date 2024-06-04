<template>
  <el-dialog title="录入" :visible.sync="visible" width="60%" :before-close="close" :close-on-click-modal="false"
    v-if="visible" class="manualInputFormDialog">
    <div v-if="visible">
      <el-form label-width="150px" :model="manualInputForm" ref="manualInputForm" :rules="rules" inline>
        <ss-common-area-group :columnCount="1" style="width：100%">
          <ss-common-area-item label="学年:" prop="classYear" style="width:50%">
            <el-select v-model="manualInputForm.classYear" placeholder="请选择学年" clearable>
              <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="年级:" prop="grade">
            <el-select clearable v-model="manualInputForm.grade" placeholder="请选择年级">
              <el-option v-for="item in gradeList" :key="item.claLevel" :label="item.claLevelCN" :value="item.claLevel" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="轮次:" prop="lunci">
            <el-select v-model="manualInputForm.lunci" placeholder="请选择轮次" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="班级:" prop="classes">
            <el-input v-model.trim="manualInputForm.classes" placeholder="请输入" maxlength="10"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="学生姓名:" prop="stuName">
            <el-input clearable maxlength="10" placeholder="请输入" v-model.trim="manualInputForm.stuName"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="出生日期:" prop="stuBirth" style="width:60%">
            <el-date-picker class="w_320" v-model="manualInputForm.stuBirth" :picker-options="startTimeOption" type="date"
              placeholder="选择日期">
            </el-date-picker>
          </ss-common-area-item>
          <ss-common-area-item label="性别：" prop="stuSex">
            <el-select v-model="manualInputForm.stuSex">
              <el-option label="男" value="1">男</el-option>
              <el-option label="女" value="0">女</el-option>
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="移动电话:" prop="schPhone">
            <el-input clearable maxlength="26" placeholder="请输入" v-model.trim="manualInputForm.schPhone"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="身份证件号码:" prop="sid">
            <el-input clearable maxlength="18" placeholder="请输入" v-model.trim="manualInputForm.sid"></el-input>
          </ss-common-area-item>
        </ss-common-area-group>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="save" size="small">保 存</el-button>
      <ss-custom-button @click="printBefore" :type="$consts.CUSTOMBUTTONTYPES.jiandang" class="print" />
    </span>
  </el-dialog>
</template>
<script>
import { addClassMess, findClassById, updateClassMess } from '@/utils/axios/apis/classInfo'
import commonOptions from '@/mixin/commonOptions'
export default {
  name: 'classesManageDialog',
  mixins: [commonOptions],
  props: {
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
  },
  watch: {
    visible(v) {
      if (v) {
        if (this.isEdit) {
          findClassById({ classId: this.curId }).then((res) => {
            this.manualInputForm.claNameYear = res.data.claNameYear
            this.manualInputForm.claLevel = this.classList.findIndex(i => i === res.data.claLevel).toString()
            this.manualInputForm.claShortName = res.data.claShortName
            this.manualInputForm.claTotal = res.data.claTotal
            this.manualInputForm.claLinkMan = res.data.claLinkman
            this.manualInputForm.claPhone = res.data.claPhone
            this.manualInputForm.claSeq = res.data.claSeq
            this.classInfo = res.data
          })
        }
      }
    }
  },
  data() {
    let validatePhoneNum = (rule, value, callback) => {
      const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
      const telephoneRegex = /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/
      if (!reg.test(value) && !telephoneRegex.test(value)) {
        callback(new Error('电话号格式不正确'))
      }
      callback()
    }
    let validateSid = (rule, value, callback) => {
      const reg = /^(^\\d{15}$)|\\d{6}(18|19|20)\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[X])$/
      if (!reg.test(value)) {
        callback(new Error('身份证号码格式不正确'))
      }
      callback()
    }
    return {
      startTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
      queryNode: {},
      gradeList: [],
      areaCodes: '',
      areaName: '',
      selectTypeList: [
        {
          name: '幼托机构',
          code: '0'
        },
        {
          name: '小学',
          code: '1'
        }
      ],
      manualInputForm: {
        stuBirth: '',
        stuName: '',
        stuSex: '',
        schPhone: '',
        classes: '',
        lunci: '',
        grade: '',
        sid: '',
        classYear: this.$dateutil.currentYear()
      },
      rules: {
        classYear: [
          { required: true, message: '请选择学年', trigger: 'change' }
        ],
        grade: [
          { required: true, message: '请选择年级', trigger: 'blur' }
        ],
        lunci: [
          { required: true, message: '请选择轮次', trigger: 'change' }
        ],
        classes: [
          { required: true, message: '请输入班级', trigger: 'blur' }
        ],
        stuName: [
          { required: true, message: '请输入学生姓名', trigger: 'blur' }
        ],
        stuBirth: [
          { required: true, message: '请选择出生日期', trigger: 'change' }
        ],
        stuSex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        schPhone: [
          { required: true, message: '请输入移动电话', trigger: 'blur' },
          { validator: validatePhoneNum, trigger: 'blur' }
        ],
        sid: [
          { required: true, message: '请输入身份证号码', trigger: 'blur' },
          { validator: validateSid, trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    // 保存
    save() {
      const data = {
        ...this.manualInputForm
      }
      console.log('save-add', data)
      this.$refs.manualInputForm.validate().then((flag) => {
        if (flag) {
          // 新增
          console.log('save-add', data)
          insertSchool({
            ...data,
            operateType: this.$consts.OPTYPE_INSERT,
            cModId: this.$store.state.moduleId
          }).then((res) => {
            if (res.msg) {
              if (res.msg === '新增成功！') {
                this.$message({
                  message: res.msg,
                  type: 'success'
                })
                this.close()
                this.$emit('fetchData')
              } else {
                this.$message({
                  message: res.msg,
                  type: 'warning'
                })
              }
            } else {
              this.$message({
                message: res.data,
                type: 'success'
              })
              this.close()
              this.$emit('fetchData')
            }
          }).catch(() => {
            // this.close();
            // this.$router.push({ name: 'errorPage' });
          })
        }
      })
    },
    close() {
      this.$refs.manualInputForm.resetFields()
      this.queryNode = {}
      this.$emit('closeDialog')
    },
    printBefore() {

    }
  },
  mounted() {
    this.findClaYear()
  }
}
</script>
<style>
.manualInputFormDialog .el-dialog__body {
  overflow: auto !important;
}

.manualInputFormDialog .el-input {
  width: 280px;
}

.manualInputFormDialog :not(.el-transfer) .el-button--primary,
:not(.el-transfer) .el-button--primary:focus,
:not(.el-transfer) .el-button--primary:hover {
  height: 32px !important;
}

::v-deep .manualInputFormDialog .el-transfer-panel {
  border: 1px solid rgb(212, 208, 208);
}

.manualInputFormDialog .column .common-area-item {
  display: block;
  width: 50% !important;
  float: left !important;
}
</style>
<style scoped>
.print {
  margin-left: 20px;
  float: right;
}
</style>
