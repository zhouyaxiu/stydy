<template>
  <el-dialog :title="isEdit ? '编辑' : '新增'" :visible.sync="visible" width="60%" :before-close="close"
    :close-on-click-modal="false" v-if="visible">
    <div v-if="visible">
      <el-form label-width="150px" :model="unitManageForm" ref="unitManageForm" :rules="rules" inline>
        <ss-common-area-group :columnCount="1" style="width：100%">
          <ss-common-area-item label="轮次：" prop="claSeq">
            <div class="label" slot="label">轮次：</div>
            <el-select v-model="unitManageForm.claSeq" placeholder="请选择" clearable v-if="!isEdit">
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
            <el-input clearable class="w_240" maxlength="4" v-model.trim="unitManageForm.claSeq==1?'春季':'秋季'" placeholder="请输入" :disabled="isEdit?true:false" v-else></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="班级名称:" prop="claNameYear" style="float:left">
            <el-input clearable class="w_240" maxlength="4" v-model.trim="unitManageForm.claNameYear" placeholder="请输入" :disabled="isEdit?true:false"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="年份" label-width="50px"  style="float:left;"></ss-common-area-item>
          <ss-common-area-item prop="claLevel" style="float:left">
            <el-select class="w_240 ml_10" clearable v-model="unitManageForm.claLevel"
              placeholder="请选择"
              :disabled="isEdit?true:false">
              <el-option v-for="item in gradeList" class="w_240" :key="item.code" :label="item.name"
                :value="item.code" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item prop="claShortName" style="float:left">
            <el-input clearable class="w_240 ml_10"
              v-model.trim="unitManageForm.claShortName" placeholder="请输入" maxlength="10" :disabled="isEdit?true:false"></el-input>
          </ss-common-area-item>
          <div class="remark">备注：班级名称不可以使用单引号</div>
          <ss-common-area-item label="班级总人数:" prop="claTotal">
            <el-input clearable class="w_240" maxlength="3" v-model.trim="unitManageForm.claTotal"
              placeholder="请输入" :disabled="!isEdit || stuclayear==unitManageForm.claNameYear && unitManageForm.claSeq==1?false:true"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="联系人:" prop="claLinkMan">
            <el-input clearable class="w_240" maxlength="20" v-model.trim="unitManageForm.claLinkMan"
              placeholder="请输入"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="电话:" prop="claPhone">
            <el-input clearable class="w_240" maxlength="26" v-model.trim="unitManageForm.claPhone"
              placeholder="请输入"></el-input>
          </ss-common-area-item>
        </ss-common-area-group>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
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
    visible (v) {
      if (v) {
        if (this.isEdit) {
          findClassById({ classId: this.curId }).then((res) => {
            this.unitManageForm.claNameYear = res.data.claNameYear
            this.unitManageForm.claLevel = this.classList.findIndex(i => i === res.data.claLevel).toString()
            this.unitManageForm.claShortName = res.data.claShortName
            this.unitManageForm.claTotal = res.data.claTotal
            this.unitManageForm.claLinkMan = res.data.claLinkman
            this.unitManageForm.claPhone = res.data.claPhone
            this.unitManageForm.claSeq = res.data.claSeq
            this.classInfo = res.data
          })
        }
      }
    }
  },
  data () {
    let validatePhoneNum = (rule, value, callback) => {
      const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
      const telephoneRegex = /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/
      if (value && !reg.test(value) && !telephoneRegex.test(value)) {
        callback(new Error('电话号格式不正确'))
      }
      callback()
    }
    let validateNum = (rule, value, callback) => {
      const reg = /^[1-9]{1,}[\d]*$/
      if (value && !reg.test(value)) {
        callback(new Error('请输入1-3位正整数'))
      }
      if (this.isEdit && this.classInfo && this.classInfo.stuNum && (value === '' || value < this.classInfo.stuNum)) {
        callback(new Error('不可小于当前班级实际人数' + this.classInfo.stuNum + '人'))
      }
      callback()
    }
    let validateYear = (rule, value, callback) => {
      const reg = /^[0-9]{4}[\d]*$/
      if (!value) {
        callback(new Error('请输入年份'))
      }
      if (!reg.test(value)) {
        callback(new Error('请输入四位数字'))
      }
      if (value < 1900) {
        callback(new Error('年份不能小于1900！'))
      }
      if (value > new Date().getFullYear()) {
        callback(new Error('年份不能大于当前年份！'))
      }
      callback()
    }
    let validateclaName = (rule, value, callback) => {
      if (value.indexOf('\'') !== -1 || value.indexOf('’') !== -1 || value.indexOf('‘') !== -1) {
        callback(new Error('不可使用单引号'))
      }
      callback()
    }
    return {
      gradeList: [],
      classInfo: '',
      unitManageForm: {
        claNameYear: '',
        claLevel: '',
        claShortName: '',
        claTotal: '',
        claLinkMan: '',
        claPhone: '',
        claSeq: '1'
      },
      classList: [
        '幼儿园托班',
        '幼儿园小班',
        '幼儿园中班',
        '幼儿园大班',
        '一年级',
        '二年级',
        '三年级',
        '四年级',
        '五年级',
        '六年级'
      ],
      rules: {
        claSeq: [
          { required: true, message: '请输入轮次', trigger: 'blur' },
        ],
        claNameYear: [
          { required: true, message: '请输入年份', trigger: 'blur' },
          { validator: validateYear, trigger: 'blur' }
        ],
        claLevel: [
          { required: true, message: '请选择年级', trigger: 'blur' }
        ],
        claShortName: [
          { required: true, message: '请输入班级', trigger: 'blur' },
          { validator: validateclaName, trigger: 'blur' }
        ],
        claTotal: [
          { required: true, message: '请输入1-3位正整数', trigger: 'blur' },
          { validator: validateNum, trigger: 'blur' }
        ],
        claLinkMan: [
          { required: false, message: '请输入联系人', trigger: 'blur' }
        ],
        claPhone: [
          { required: false, message: '请输入电话号码', trigger: 'blur' },
          { validator: validatePhoneNum, trigger: 'blur' }
        ]
      },
      stuclayear:''
    }
  },
  methods: {
    // 保存或编辑
    save () {
      const data = {
        ...this.unitManageForm
      }
      console.log('save-add', data)
      this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          if (this.isEdit) {
            // 编辑
            console.log('edit', { ...data, rowKey: this.curId })
            updateClassMess({
              ...data,
              classId: this.curId,
              operateType: this.$consts.OPTYPE_UPDATE,
              cModId: this.$store.state.moduleId
            }).then((res) => {
              if (res.msg) {
                if (res.msg === '修改成功！') {
                  this.$message({
                    message: res.msg,
                    type: 'success'
                  })
                  this.close()
                  this.$emit('fetchData')
                } else if (res.msg.indexOf('该学校下存在学生') !== -1) {
                  this.$util.errorMsg(res.msg)
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'error'
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
            })
          } else {
            // 新增
            console.log('save-add', { ...data })
            addClassMess({
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
                } else if (res.msg.indexOf('学校编码已存在') !== -1) {
                  this.$util.errorMsg(res.msg)
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
            })
          }
        }
      })
    },
    close () {
      this.unitManageForm.claLevel = ''
      this.unitManageForm.claNameYear = ''
      this.$refs.unitManageForm.resetFields()
      this.$emit('closeDialog')
    }
  },
  async created () {
    if (this.$store.state.schType === '1') {
      this.gradeList = this.primaryGradeList
    } else {
      this.gradeList = this.kindergartenGradeList
    }
    await this.findClaYear()
    if (this.schoolYearList.length > 0) {
      this.stuclayear = this.schoolYearList[0].claNameYear
    }
  }
}
</script>
<style scoped>
  .ml_10 {
    margin-left: 10px;
  }
  .w_240 {
    width: 240px;
  }
  .remark{
    display: block;
    color: #6878B2;
    margin-left: 150px;
    font-size: 14px;
    float: left;
    margin-top: -5px;
    margin-bottom: 15px;
  }
</style>
