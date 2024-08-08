<template>
  <el-dialog :title="isEdit ? '编辑' : '新增'" :visible.sync="visible" width="900px" :before-close="close"
    :close-on-click-modal="false" v-if="visible">
    <div v-if="visible">
      <el-form label-width="150px" :model="unitManageForm" ref="unitManageForm" :rules="rules" inline>
        <ss-common-area-group :columnCount="1" style="width：100%">
          <ss-common-area-item label="选择地区:" required>
            <ss-area-areaTreeStation ref="areaTreePage" @nodeClick="nodeClickSelf" :queryNode="queryNode" :getToLevelNum="5 - currentLevel" :disabled="msgPubPermission || isEdit" />
          </ss-common-area-item>
          <ss-common-area-item label="学校类型:" prop="schType">
            <el-select v-model="unitManageForm.schType" placeholder="请选择学校类型" clearable @change="schTypeChange" :disabled="isEdit">
              <el-option label="幼托机构" value="0" />
              <el-option label="小学" value="1" />
              <el-option label="初中" value="2" />
              <el-option label="高中" value="3" />
              <el-option label="大学" value="4" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="学校名称:" prop="claSchid" v-if="!isEdit">
            <el-select v-model="unitManageForm.claSchid" clearable placeholder="请选择学校">
              <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="学校名称:" prop="schName" v-if="isEdit">
            <el-select v-model="unitManageForm.schName" clearable :disabled="isEdit" placeholder="请选择学校名称">
              <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="班级:" prop="claNameYear" style="float:left">
            <el-input clearable maxlength="4" v-model.trim="unitManageForm.claNameYear" placeholder="请输入学年" style="width:200px;" :disabled="isEdit"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="" prop="claLevel" style="float:left" labelWidth="0">
            <el-select clearable v-model="unitManageForm.claLevel" placeholder="请选择年级" style="width:200px;" :disabled="isEdit">
              <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code"/>
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="" prop="claShortName" style="float:left" labelWidth="0">
            <el-input clearable v-model.trim="unitManageForm.claShortName" placeholder="请输入班级名称" style="width:200px;" :disabled="isEdit"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="班级应查验人数:" prop="claTotal">
            <el-input clearable class="w_240" maxlength="3" v-model.trim="unitManageForm.claTotal"
              placeholder="请输入班级应查验人数" style="width:200px;"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="联系人:" prop="claLinkman">
            <el-input clearable class="w_240" maxlength="20" v-model.trim="unitManageForm.claLinkman"
              placeholder="请输入联系人" style="width:200px;"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="电话:" prop="claPhone">
            <el-input clearable class="w_240" maxlength="26" v-model.trim="unitManageForm.claPhone"
              placeholder="请输入电话" style="width:200px;"></el-input>
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
import { addClassMess, findClassById, updateClassMess } from '@/utils/axios/apis/rxrt/classInfo'
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
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
          findClassById({ classId: this.curId }).then(async (res) => {
            await this.findClaLevelNew(res.data.schType)
            this.unitManageForm.schType = res.data.schType
            this.unitManageForm.claSchid = res.data.claSchid
            this.unitManageForm.schName = res.data.schName
            this.unitManageForm.claNameYear = res.data.claNameYear
            this.unitManageForm.claLevel = res.data.claLevel
            this.unitManageForm.claShortName = res.data.claShortName
            this.unitManageForm.claTotal = res.data.claTotal
            this.unitManageForm.claLinkman = res.data.claLinkman
            this.unitManageForm.claPhone = res.data.claPhone
            this.classInfo = res.data
          })
        }
      }
    }
  },
  data () {
    let validatePhoneNum = (rule, value, callback) => {
      const reg = /^1[3-9]\d{9}$/
      const telephoneRegex = /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/
      if (value && !reg.test(value) && !telephoneRegex.test(value)) {
        callback(new Error('电话号格式不正确'))
      }
      callback()
    }
    let validateNum = (rule, value, callback) => {
      const reg = /^[1-9]{1,}[\d]*$/
      if (!value) {
        callback(new Error('请输入班级应查验人数'))
      }
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
        schType: '',
        claSchid: '',
        claNameYear: '',
        claLevel: '',
        claShortName: '',
        claTotal: '',
        claLinkman: '',
        claPhone: '',
        schName: ''
      },
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      msgPubPermission: false,
      selectTypeList: this.$store.state.selectTypeList,
      rules: {
        schType: [
          { required: true, message: '请选择学校类型', trigger: 'change' }
        ],
        claSchid: [
          { required: true, message: '请选择学校', trigger: 'change' }
        ],
        schName: [
          { required: true, message: '请选择学校', trigger: 'blur' }
        ],
        claNameYear: [
          { required: true, message: '请输入学年', trigger: 'blur' },
          { validator: validateYear, trigger: 'blur' }
        ],
        claLevel: [
          { required: true, message: '请选择年级', trigger: 'change' }
          // { validator: validateYear, trigger: 'blur' }
        ],
        claShortName: [
          { required: true, message: '请输入班级名称', trigger: 'blur' }
        ],
        claLinkman: [
          { required: false, message: '请选择年级', trigger: 'blur' },
          { validator: validateclaName, trigger: 'blur' }
        ],
        claTotal: [
          { required: true, message: '请输入班级应查验人数', trigger: 'blur' },
          { validator: validateNum, message: '请输入1-3位正整数', trigger: 'blur' }
        ],
        claPhone: [
          { required: false, message: '请输入电话号码', trigger: 'blur' },
          { validator: validatePhoneNum, trigger: 'blur' }
        ]
      },
      stuclayear: '',
      queryNode: {}
    }
  },
  methods: {
    // 选择地区节点
    async nodeClickSelf (data, checked, node) {
      this.unitManageForm.claSchid = ''
      this.schoolList = []
      if (this.$refs.areaTreePage.selectAreaLevel !== '4') {
        return this.$util.errorMsg('请选择到接种单位!')
      } else {
        await this.findSchool()
      }
    },
    // 保存或编辑
    save () {
      if (this.$refs.areaTreePage.selectAreaLevel !== '4') {
        return this.$util.errorMsg('请选择到接种单位!')
      }
      this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          if (this.isEdit) {
            // 编辑
            let params = {
              claTotal: this.unitManageForm.claTotal,
              claLinkman: this.unitManageForm.claLinkman,
              claPhone: this.unitManageForm.claPhone
            }
            updateClassMess({
              ...params,
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
            let params = {
              claTotal: this.unitManageForm.claTotal,
              claLinkman: this.unitManageForm.claLinkman,
              claPhone: this.unitManageForm.claPhone,
              claSchid: this.unitManageForm.claSchid,
              claNameYear: this.unitManageForm.claNameYear,
              claShortName: this.unitManageForm.claShortName,
              claLevel: this.unitManageForm.claLevel
            }
            addClassMess({
              ...params,
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
              }
            }).catch(() => {
            })
          }
        }
      })
    },
    close () {
      // this.unitManageForm.claLevel = ''
      // this.unitManageForm.claNameYear = ''
      this.unitManageForm.claSchid = ''
      this.$refs.unitManageForm.resetFields()
      this.$emit('closeDialog')
    },
    async schTypeChange (val) {
      this.unitManageForm.claLevel = ''
      this.unitManageForm.claSchid = ''
      await this.findClaLevelNew(val)
      if (this.$refs.areaTreePage.selectAreaLevel == '4') {
        await this.findSchool()
      }
    },
    // 查询学校
    findSchool () {
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        // this.$util.errorMsg('必须选择地区！')
        return false
      }
      if (!this.unitManageForm.schType) {
        // this.$util.errorMsg('必须选择学校类型！')
        return false
      }
      let params = {
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.schType
      }
      this.findSchoolNew(params)
      // findSchoolByAreaAndSchType({
      //   areaCode: this.$refs.areaTreePage.selectAreaCode,
      //   areaLevel: this.$refs.areaTreePage.selectAreaLevel,
      //   schType: this.schType
      // }).then((res) => {
      //   this.schoolList = res.data
      // })
    }
  },
  async created () {
    // if (this.$store.state.schType === '1') {
    //   this.gradeList = this.primaryGradeList
    // } else {
    //   this.gradeList = this.kindergartenGradeList
    // }
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4

    // await this.findClaYear()
    // if (this.schoolYearList.length > 0) {
    //   this.stuclayear = this.schoolYearList[0].claNameYear
    // }
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
