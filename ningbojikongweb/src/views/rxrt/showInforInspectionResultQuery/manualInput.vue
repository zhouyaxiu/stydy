<!-- 录入 -->
<template>
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag"></ss-condition-area>
    <el-form label-width="195px" :model="manualInputForm" ref="manualInputForm" :rules="rules" inline
      class="manualInputForm">
      <ss-common-area-group :columnCount="1" style="width：100%">
        <ss-common-area-item label="学校类型：" prop="">
          <el-select v-model="manualInputForm.schType" @change="changeSchType" clearable>
            <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </ss-common-area-item>
        <ss-common-area-item label="学校名称：" prop="schId">
          <el-select v-model="manualInputForm.schId" placeholder="请选择学校名称" clearable @change="changeSchool">
            <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
          </el-select>
        </ss-common-area-item>
        <ss-common-area-item label="学年：" prop="year">
          <el-select v-model="manualInputForm.year" placeholder="请选择学年" clearable>
            <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
          </el-select>
        </ss-common-area-item>
        <ss-common-area-item label="年级：" prop="grade">
          <el-select clearable v-model="manualInputForm.grade" placeholder="请选择">
            <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </ss-common-area-item>
        <ss-common-area-item label="轮次：" prop="round">
          <el-select v-model="manualInputForm.round" placeholder="请选择" clearable>
            <el-option label="春季" value="1" />
            <el-option label="秋季" value="0" />
          </el-select>
        </ss-common-area-item>
        <ss-common-area-item label="班级：" prop="className">
          <el-input v-model.trim="manualInputForm.className" placeholder="请输入班级，比如：1班" maxlength="20"
            style="width: 250px"></el-input>
        </ss-common-area-item>
        <ss-common-area-item label="学生姓名：" prop="stuName">
          <el-input clearable style="width: 250px" maxlength="10" placeholder="请输入"
            v-model.trim="manualInputForm.stuName"></el-input>
        </ss-common-area-item>
        <ss-common-area-item label="出生日期：" prop="stuBirth">
          <el-date-picker class="w_320" v-model="manualInputForm.stuBirth" :picker-options="startTimeOption" type="date"
            placeholder="选择日期">
          </el-date-picker>
        </ss-common-area-item>
        <ss-common-area-item label="性别：" prop="stuSex">
          <el-select v-model="manualInputForm.stuSex">
            <el-option label="男" value="男">男</el-option>
            <el-option label="女" value="女">女</el-option>
          </el-select>
        </ss-common-area-item>
        <ss-common-area-item label="移动电话：" prop="stuPhone">
          <el-input clearable style="width: 250px" maxlength="26" placeholder="请输入"
            v-model.trim="manualInputForm.stuPhone"></el-input>
        </ss-common-area-item>
        <ss-common-area-item label="身份证件号码：" prop="cid">
          <el-input clearable style="width: 250px" maxlength="18" placeholder="请输入"
            v-model.trim="manualInputForm.cid"></el-input>
        </ss-common-area-item>
        <ss-common-area-item label="父母姓名（父亲或者母亲）：" prop="stuParentname">
          <el-input clearable style="width: 250px" maxlength="18" placeholder="请输入"
            v-model.trim="manualInputForm.stuParentname"></el-input>
        </ss-common-area-item>
        <ss-common-area-item label=" " prop="">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
          <ss-custom-button @click="printBefore" :type="$consts.CUSTOMBUTTONTYPES.buzheng" class="print buzhengbtn" />
        </ss-common-area-item>
      </ss-common-area-group>
    </el-form>
    <print-noice :visible="dialogVisible" @close="closeModal" :noiceList="noiceList" title="预防接种证查验疫苗补证联系通知单" />
  </div>
</template>
<script>
import moment from 'moment'
import { manualEntry,ifNotHasCard } from '@/utils/axios/apis/manualEntry/index'
import commonOptions from '@/mixin/commonOptions'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
import printNoice from './components/printNoice.vue'
import { queryNoticeMess } from '@/utils/axios/apis/notice/index'

export default {
  name: 'manualInputDialog',
  mixins: [commonOptions],
  components: { printNoice },
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
      const reg = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9X]$/
      // const reg = /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i
      if (!reg.test(value)) {
        callback(new Error('身份证件号码格式不正确'))
      }
      callback()
    }
    let validatorClassName = (rule, value, callback) => {
      const reg = /^[0-9A-Za-z\u4e00-\u9fa5]+$/;
      if (!reg.test(value)) {
        callback(new Error("班级只能是中文，英文，数字!"));
      }
      callback();
    };
    return {
      startTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
      schoolList: [],
      gradeList: this.$util.getGrade(2),
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
      dialogVisible: false,
      mainInfo: {},
      noiceList: [],
      manualInputForm: {
        year: this.$dateutil.currentYear(),
        grade: '',
        schId: '',
        schType: '',
        round: '',
        className: '',
        stuName: '',
        stuBirth: '',
        stuSex: '',
        stuPhone: '',
        cid: '',
        stuParentname: '',
      },
      schstationCode: '',
      schoolName: '',
      schStationName: '',
      rules: {
        year: [
          { required: true, message: '请选择学年', trigger: 'change' }
        ],
        grade: [
          { required: true, message: '请选择年级', trigger: 'change' }
        ],
        schId: [
          { required: true, message: '请选择学校名称', trigger: 'change' }
        ],
        // schType: [
        //   { required: true, message: '请选择学校类型', trigger: 'change' }
        // ],
        round: [
          { required: true, message: '请选择轮次', trigger: 'change' }
        ],
        className: [
          { required: true, message: '请输入班级', trigger: 'blur' },
          { validator: validatorClassName, trigger: "blur" },
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
        stuPhone: [
          { required: true, message: '请输入移动电话', trigger: 'blur' },
          { validator: validatePhoneNum, trigger: 'blur' }
        ],
        cid: [
          { required: true, message: '请输入身份证件号码', trigger: 'blur' },
          { validator: validateSid, trigger: 'blur' }
        ],
        stuParentname: [
          { required: true, message: '请输入父母姓名', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.manualInputForm.setHeight()
    },
    // 保存
    save() {
      const data = {
        ...this.manualInputForm,
        stuBirth: moment(this.manualInputForm.stuBirth).format('YYYY-MM-DD'),
        schstationCode: this.schstationCode
      }
      console.log('save-add', data)
      this.$refs.manualInputForm.validate().then((flag) => {
        if (flag) {
          this.openFullScreen()
          // 新增
          manualEntry({
            ...data,
            operateType: this.$consts.OPTYPE_INSERT,
            // cModId: this.$store.state.moduleId
          }).then((res) => {
            if (res.message == '添加成功！') {
              this.$message({
                message: res.message,
                type: 'success'
              })
              this.close()
            }else {
              this.$message({
                message: res.message,
                type: 'error'
              })
            }
            this.closeFullScreen()
          }).catch(() => {
            this.closeFullScreen()
          })
        }
      })
    },
    close() {
      this.$refs.manualInputForm.resetFields()
      this.manualInputForm.schType = ''
    },
    closeModal() {
      this.dialogVisible = false;
    },
    printBefore() {
      this.$refs.manualInputForm.validate().then((flag) => {
        if (flag) {
          ifNotHasCard({cid:this.manualInputForm.cid}).then(res => {
            if(res.data==0){
              this.mainInfo = {
                title: '补证通知单',
                maintype: 1,
                ...this.manualInputForm,
                stuBirth: moment(this.manualInputForm.stuBirth).format('YYYY-MM-DD'),
                schoolName: this.schoolName,
                gradeName: this.gradeList.filter(item => item.code == this.manualInputForm.grade)[0].name,
                schStationName: this.schStationName,
                className: this.manualInputForm.className
              }
              let params = {
                noticeType: 1,
                schId: this.mainInfo.schId
              }
              queryNoticeMess(params).then(res => {
                this.mainInfo = {
                  ...this.mainInfo,
                  noticeContent: res.data[0].noticeContent
                }
                this.noiceList = [{
                  ...this.mainInfo,
                }]
                this.dialogVisible = true;
              })
            }else if(res.data==-1){
              return this.$util.errorMsg('信息系统中该学生处于踢卡状态，无需打印补证通知单！')
            }else if(res.data==1){
              return this.$util.errorMsg('该受种者有证，无需打印补证通知单！')
            }
          })
        }
      })
    },
    changeSchool(val) {
      if (val) {
        console.log(this.schoolList.filter(item => item.schId == val))
        this.schstationCode = this.schoolList.filter(item => item.schId == val)[0].schStationCode
        this.schoolName = this.schoolList.filter(item => item.schId == val)[0].schName
        this.schStationName = this.schoolList.filter(item => item.schId == val)[0].schStationName
        let schType = this.schoolList.filter(item => item.schId == val)[0].schType;
        this.gradeList = this.$util.getGrade(schType)
        this.manualInputForm.grade = ''
      } else {
        this.gradeList = this.$util.getGrade(2)
        this.manualInputForm.grade = ''
      }
    },
    changeSchType(val) {
      if (val) {
        this.gradeList = this.$util.getGrade(val)
        this.manualInputForm.schId = ''
        this.findSchool();
        this.manualInputForm.grade = ''
      }
    },
    // 查询学校
    findSchool() {
      findSchoolByAreaAndSchType({
        areaCode: sessionStorage.getItem('areacode'),
        areaLevel: sessionStorage.getItem('areaLevel'),
        schType: this.manualInputForm.schType
      }).then((res) => {
        this.schoolList = res.data
      })
    },
  },
  mounted() {
    this.findClaYear()
    if (sessionStorage.getItem('areacode')) {
      this.findSchool();
    }
  }
}
</script>
<style>
.manualInputForm .el-input {
  width: 250px;
}

.buzhengbtn .buzheng-btn{
  min-width: 185px;
  min-height: 40px;
}
</style>
<style scoped>
.print {
  margin-left: 20px;
  float: right;
}

.manualInputForm .column .select-area-item,
.column .common-area-item {
  width: 50%;
  float: left;
}

.dialog-footer {
  margin-top: 50px;
}
</style>
