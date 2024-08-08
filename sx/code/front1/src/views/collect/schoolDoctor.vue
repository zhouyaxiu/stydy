<template>
  <!-- 校医录入 -->
  <div class="schoolDoctor">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag"></ss-condition-area>
    <div class="main-content">
      <el-row :style="{ height: height + 'px' }">
        <el-form label-width="200px" label-position="right" :model="unitManageForm" ref="unitManageForm" :rules="rules">
          <el-col :span="8">
            <ss-custom-title type="info" text="基本信息" class="ml_60"></ss-custom-title>
            <div class="scroll" :style="{ height: height - 80 + 'px' }">
              <el-form-item label="年份：" prop="stuclayear">
                <!-- <el-select clearable v-model="unitManageForm.stuclayear" placeholder="请选择">
                  <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear" class="w_320" />
                </el-select> -->
                <el-input v-model="unitManageForm.stuclayear" disabled></el-input>
              </el-form-item>
              <el-form-item label="轮次：" prop="stuSeq">
                <!-- <el-select v-model="unitManageForm.stuSeq" placeholder="请选择" clearable>
                  <el-option label="春季" value="1" />
                  <el-option label="秋季" value="0" />
                </el-select> -->
                <el-input v-model="unitManageForm.stuSeq==1?'春季':'秋季'" disabled></el-input>
              </el-form-item>
              <el-form-item label="年级：" prop="stuClalevel">
                <el-select clearable v-model="unitManageForm.stuClalevel" placeholder="请选择">
                  <el-option v-for="item in gradeList" :key="item.claLevel" :label="item.claLevelCN" :value="item.claLevel" class="w_320" />
                </el-select>
              </el-form-item>
              <el-form-item label="班级名称：" prop="stuClacode">
                <el-select clearable v-model="unitManageForm.stuClacode" placeholder="请选择">
                  <el-option v-for="item in classesList" :key="item.classId" :label="item.claName" :value="item.classId" class="w_320" />
                </el-select>
              </el-form-item>
              <el-form-item label="受种者姓名：" prop="stuName">
                <el-input clearable v-model.trim="unitManageForm.stuName" placeholder="请输入" maxlength="10"></el-input>
              </el-form-item>
              <el-form-item label="出生日期：" prop="stuBirth">
                <el-date-picker class="w_320" v-model="unitManageForm.stuBirth" :picker-options="startTimeOption" value-format="yyyy-MM-dd" format="yyyyMMdd" type="date" placeholder="请输入">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="户籍类型：" prop="stuType">
                <el-select clearable v-model="unitManageForm.stuType" placeholder="请选择" class="w_320">
                  <el-option v-for="item in typeList" :key="item.code" :label="item.name" :value="item.code" />
                </el-select>
              </el-form-item>
              <el-form-item label="父亲/母亲/监护人姓名：" prop="stuParentname">
                <el-input clearable maxlength="20" v-model.trim="unitManageForm.stuParentname" placeholder="请输入"></el-input>
              </el-form-item>
              <el-form-item label="移动电话：" prop="stuPhone">
                <el-input clearable maxlength="26" v-model.trim="unitManageForm.stuPhone" placeholder="请输入"></el-input>
              </el-form-item>
              <el-form-item label="其它电话：" prop="stuOtherphone">
                <el-input clearable maxlength="26" v-model.trim="unitManageForm.stuOtherphone" placeholder="请输入"></el-input>
              </el-form-item>
              <el-form-item label="性别：" prop="stuSex">
                <el-radio-group v-model="unitManageForm.stuSex">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                  <el-radio label="未知">未知</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="有无接种证：" prop="stuIsjzz">
                <el-radio-group v-model="unitManageForm.stuIsjzz">
                  <el-radio label="0">有</el-radio>
                  <el-radio label="1">无</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="8">
            <ss-custom-title type="record" text="接种记录" class="ml_60"></ss-custom-title>
            <div class="scroll" :style="{ height: height - 80 + 'px' }">
              <el-form-item v-for="(jz, index) in unitManageForm.jzList" :label="jz.name" :prop="jz.code" :key="jz.code">
                <el-select clearable v-model="jz.value" filterable allow-create default-first-option @change="change($event,index,unitManageForm.jzList)" @blur="blur($event,index,unitManageForm.jzList)">
                  <el-option v-for="item in recordList" :key="item.code" :label="item.name" :value="item.code" class="w_320" />
                </el-select>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="8">
            <ss-custom-title type="instead" text="替代疫苗" class="ml_60"></ss-custom-title>
            <div class="scroll" :style="{ height: height - 80 + 'px' }">
              <el-form-item v-for="(td, index) in unitManageForm.tdList" :label="td.name" :prop="td.code" :key="td.code">
                <el-select clearable v-model="td.value" filterable allow-create default-first-option @change="change($event,index,unitManageForm.tdList)" @blur="blur($event,index,unitManageForm.tdList)">
                  <el-option v-for="item in recordList" :key="item.code" :label="item.name" :value="item.code" class="w_320" />
                </el-select>
              </el-form-item>
            </div>
          </el-col>
        </el-form>
      </el-row>
      <div class="submit">
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import { doctorEntry } from '@/utils/axios/apis/collect'
import commonOptions from '@/mixin/commonOptions'
export default {
  mixins: [commonOptions],
  data () {
    let validatePhoneNum = (rule, value, callback) => {
      const reg = /^(((\d{3,4}-)?[0-9]{7,8})|(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8})$/
      if (value && !reg.test(value)) {
        callback(new Error('其它电话格式不正确'))
      }
      callback()
    }
    let validateMobilePhoneNum = (rule, value, callback) => {
      const reg = /^0?(13[0-9]|15[012356789]|17[013678]|18[0-9]|14[57])[0-9]{8}$/
      if (value && !reg.test(value)) {
        callback(new Error('移动电话格式不正确'))
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
      height: window.winHeight - 15 / window.scale - 200,
      isBreak: false,
      typeList: [
        { name: '市地内流动', code: '市地内流动' },
        { name: '省际内流动', code: '省际内流动' },
        { name: '县区内流动', code: '县区内流动' },
        { name: '本省内流动', code: '本省内流动' },
        { name: '常住', code: '常住' },
        { name: '临时', code: '临时' },
        { name: '国际流动', code: '国际流动' },
        { name: '其他', code: '其他' }
      ],
      recordList: [
        { name: '不详', code: '1981' },
        { name: '禁忌', code: '1983' },
        { name: '已患', code: '1986' },
        { name: '超期不种', code: '1984' },
        { name: '已种替代疫苗', code: '1980' },
        { name: '父亲/母亲/监护人要求不种', code: '1982' },
        { name: '已种但时间未知', code: '1985' }
      ],
      unitManageFormReset: {
        stuclayear: this.$dateutil.currentYear(),
        stuSeq:sessionStorage.getItem("defaultLunci"),
        stuClalevel: '',
        stuClacode: '',
        stuName: '',
        stuSex: '男',
        stuIsjzz: '0',
        stuBirth: '',
        stuType: '',
        stuParentname: '',
        stuPhone: '',
        stuOtherphone: '',
        jzList: [
          { name: '乙肝1：', code: 'B063', value: '' },
          { name: '乙肝2：', code: 'B064', value: '' },
          { name: '乙肝3：', code: 'B065', value: '' },
          { name: '甲肝减毒1：', code: 'B069', value: '' },
          { name: '脊灰减毒1：', code: 'B009', value: '' },
          { name: '脊灰减毒2：', code: 'B010', value: '' },
          { name: '脊灰减毒3：', code: 'B011', value: '' },
          { name: '脊灰减毒4：', code: 'B012', value: '' },
          { name: '百白破1：', code: 'B015', value: '' },
          { name: '百白破2：', code: 'B016', value: '' },
          { name: '百白破3：', code: 'B017', value: '' },
          { name: '百白破4：', code: 'B018', value: '' },
          { name: '白破1：', code: 'B037', value: '' },
          { name: '麻疹1：', code: 'B059', value: '' },
          { name: '麻疹2：', code: 'B060', value: '' },
          { name: 'A群流脑1：', code: 'B040', value: '' },
          { name: 'A群流脑2：', code: 'B041', value: '' },
          { name: 'A+C群流脑1：', code: 'B045', value: '' },
          { name: 'A+C群流脑2：', code: 'B046', value: '' },
          { name: '乙脑减毒1：', code: 'B032', value: '' },
          { name: '乙脑减毒2：', code: 'B033', value: '' }
        ],
        tdList: [
          { name: '脊灰灭活1：', code: 'B169', value: '' },
          { name: '脊灰灭活2：', code: 'B170', value: '' },
          { name: '脊灰灭活3：', code: 'B171', value: '' },
          { name: '脊灰灭活4：', code: 'B172', value: '' },
          { name: '五联苗1：', code: 'B163', value: '' },
          { name: '五联苗2：', code: 'B164', value: '' },
          { name: '五联苗3：', code: 'B165', value: '' },
          { name: '五联苗4：', code: 'B166', value: '' },
          { name: '四联苗1：', code: 'B157', value: '' },
          { name: '四联苗2：', code: 'B158', value: '' },
          { name: '四联苗3：', code: 'B159', value: '' },
          { name: '四联苗4：', code: 'B160', value: '' },
          { name: '乙脑灭活1：', code: 'B027', value: '' },
          { name: '乙脑灭活2：', code: 'B028', value: '' },
          { name: '乙脑灭活3：', code: 'B029', value: '' },
          { name: '乙脑灭活4：', code: 'B030', value: '' },
          { name: 'AC流脑Hib联合1：', code: 'B427', value: '' },
          { name: 'AC流脑Hib联合2：', code: 'B428', value: '' },
          { name: 'AC流脑Hib联合3：', code: 'B429', value: '' },
          { name: 'A+C结合流脑1：', code: 'B138', value: '' },
          { name: 'A+C结合流脑2：', code: 'B139', value: '' },
          { name: 'A+C结合流脑3：', code: 'B140', value: '' },
          { name: 'A+C结合流脑4：', code: 'B141', value: '' },
          { name: 'ACYW流脑1：', code: 'B147', value: '' },
          { name: 'ACYW流脑2：', code: 'B148', value: '' },
          { name: '甲肝灭活1：', code: 'B073', value: '' },
          { name: '甲肝灭活2：', code: 'B074', value: '' },
          { name: '甲乙肝1：', code: 'B133', value: '' },
          { name: '甲乙肝2：', code: 'B327', value: '' },
          { name: '麻风1：', code: 'B093', value: '' },
          { name: '麻风2：', code: 'B094', value: '' },
          { name: '麻腮风1：', code: 'B089', value: '' },
          { name: '麻腮风2：', code: 'B090', value: '' },
          { name: '麻腮1：', code: 'B096', value: '' },
          { name: '麻腮2：', code: 'B097', value: '' },
          { name: '青少年百白破1：', code: 'B155', value: '' }
        ]
      },
      unitManageForm: {
        stuclayear: this.$dateutil.currentYear(),
        stuSeq:sessionStorage.getItem("defaultLunci"),
        stuClalevel: '',
        stuClacode: '',
        stuName: '',
        stuSex: '男',
        stuIsjzz: '0',
        stuBirth: '',
        stuType: '',
        stuParentname: '',
        stuPhone: '',
        stuOtherphone: '',
        jzList: [
          { name: '乙肝1：', code: 'B063', value: '' },
          { name: '乙肝2：', code: 'B064', value: '' },
          { name: '乙肝3：', code: 'B065', value: '' },
          { name: '甲肝减毒1：', code: 'B069', value: '' },
          { name: '脊灰减毒1：', code: 'B009', value: '' },
          { name: '脊灰减毒2：', code: 'B010', value: '' },
          { name: '脊灰减毒3：', code: 'B011', value: '' },
          { name: '脊灰减毒4：', code: 'B012', value: '' },
          { name: '百白破1：', code: 'B015', value: '' },
          { name: '百白破2：', code: 'B016', value: '' },
          { name: '百白破3：', code: 'B017', value: '' },
          { name: '百白破4：', code: 'B018', value: '' },
          { name: '白破1：', code: 'B037', value: '' },
          { name: '麻疹1：', code: 'B059', value: '' },
          { name: '麻疹2：', code: 'B060', value: '' },
          { name: 'A群流脑1：', code: 'B040', value: '' },
          { name: 'A群流脑2：', code: 'B041', value: '' },
          { name: 'A+C群流脑1：', code: 'B045', value: '' },
          { name: 'A+C群流脑2：', code: 'B046', value: '' },
          { name: '乙脑减毒1：', code: 'B032', value: '' },
          { name: '乙脑减毒2：', code: 'B033', value: '' }
        ],
        tdList: [
          { name: '脊灰灭活1：', code: 'B169', value: '' },
          { name: '脊灰灭活2：', code: 'B170', value: '' },
          { name: '脊灰灭活3：', code: 'B171', value: '' },
          { name: '脊灰灭活4：', code: 'B172', value: '' },
          { name: '五联苗1：', code: 'B163', value: '' },
          { name: '五联苗2：', code: 'B164', value: '' },
          { name: '五联苗3：', code: 'B165', value: '' },
          { name: '五联苗4：', code: 'B166', value: '' },
          { name: '四联苗1：', code: 'B157', value: '' },
          { name: '四联苗2：', code: 'B158', value: '' },
          { name: '四联苗3：', code: 'B159', value: '' },
          { name: '四联苗4：', code: 'B160', value: '' },
          { name: '乙脑灭活1：', code: 'B027', value: '' },
          { name: '乙脑灭活2：', code: 'B028', value: '' },
          { name: '乙脑灭活3：', code: 'B029', value: '' },
          { name: '乙脑灭活4：', code: 'B030', value: '' },
          { name: 'AC流脑Hib联合1：', code: 'B427', value: '' },
          { name: 'AC流脑Hib联合2：', code: 'B428', value: '' },
          { name: 'AC流脑Hib联合3：', code: 'B429', value: '' },
          { name: 'A+C结合流脑1：', code: 'B138', value: '' },
          { name: 'A+C结合流脑2：', code: 'B139', value: '' },
          { name: 'A+C结合流脑3：', code: 'B140', value: '' },
          { name: 'A+C结合流脑4：', code: 'B141', value: '' },
          { name: 'ACYW流脑1：', code: 'B147', value: '' },
          { name: 'ACYW流脑2：', code: 'B148', value: '' },
          { name: '甲肝灭活1：', code: 'B073', value: '' },
          { name: '甲肝灭活2：', code: 'B074', value: '' },
          { name: '甲乙肝1：', code: 'B133', value: '' },
          { name: '甲乙肝2：', code: 'B327', value: '' },
          { name: '麻风1：', code: 'B093', value: '' },
          { name: '麻风2：', code: 'B094', value: '' },
          { name: '麻腮风1：', code: 'B089', value: '' },
          { name: '麻腮风2：', code: 'B090', value: '' },
          { name: '麻腮1：', code: 'B096', value: '' },
          { name: '麻腮2：', code: 'B097', value: '' },
          { name: '青少年百白破1：', code: 'B155', value: '' }
        ]
      },
      rules: {
        stuclayear: [
          { required: true, message: '请选择年份', trigger: 'blur' }
        ],
        stuSeq: [
          { required: true, message: '请选择轮次', trigger: 'blur' }
        ],
        stuClalevel: [
          { required: true, message: '请选择年级', trigger: 'blur' }
        ],
        stuClacode: [
          { required: true, message: '请选择班级', trigger: 'blur' }
        ],
        stuName: [
          { required: true, message: '请输入受种者姓名', trigger: 'blur' }
        ],
        stuSex: [
          { required: true, message: '请选择性别', trigger: 'blur' }
        ],
        stuBirth: [
          { required: true, message: '请选择出生日期', trigger: 'blur' }
        ],
        stuType: [
          { required: true, message: '请选择户籍类型', trigger: 'blur' }
        ],
        stuParentname: [
          { required: true, message: '请输入父亲/母亲/监护人姓名', trigger: 'blur' }
        ],
        stuIsjzz: [
          { required: true, message: '请选择', trigger: 'blur' }
        ],
        stuPhone: [
          { validator: validateMobilePhoneNum, trigger: 'blur' }
        ],
        stuOtherphone: [
          { validator: validatePhoneNum, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    unitManageFormCopy () {
      return JSON.parse(JSON.stringify(this.unitManageForm))
    }
  },
  watch: {
    unitManageFormCopy: {
      deep: true,
      async handler (newVal, oldVal) {
        // 学年变化，查找年级
        if (newVal.stuclayear !== oldVal.stuclayear) {
          this.unitManageForm.stuClalevel = ''
          this.unitManageForm.stuClacode = ''
          if (newVal.stuclayear) {
            await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: newVal.stuclayear,lunci:this.unitManageForm.stuSeq })
            // 查找班级
            // await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.unitManageForm.stuSeq })
          } else {
            this.gradeList = []
            this.classesList = []
          }
        }
        // 年级发生改变
        if (newVal.stuClalevel !== oldVal.stuClalevel) {
          this.unitManageForm.stuClacode = ''
          if (newVal.stuClalevel) {
          // 查找班级
            await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.unitManageForm.stuSeq })
          } else {
            this.classesList = []
          }
        }
      }
    },
    async "unitManageForm.stuSeq"(newVal,oldVal){
      if(newVal==''){
        this.unitManageForm.stuClalevel = ''
        this.unitManageForm.stuClacode = ''
        this.classesList = []
        this.gradeList = []
        return
      }
      if(newVal!=oldVal && newVal!=''){
        this.unitManageForm.stuClalevel = ''
        this.unitManageForm.stuClacode = ''
        // 查找年级
        await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.unitManageForm.stuclayear,lunci:newVal })
        // // 查找班级
        // if(this.gradeList.length>0){
        //   await this.findClassNew({ schId: this.$store.state.schId,claLevel: this.unitManageForm.stuClalevel, claNameYear: this.unitManageForm.stuclayear,lunci:newVal })
        // }
      }
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    dateRegex (value) {
      let arr = ['1981', '1983', '1986', '1984', '1980', '1982', '1985']
      if (arr.includes(value)) {
        return true
      }
      const reg = /^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)$/
      if (value && !reg.test(value)) {
        return false
      }
      return true
    },
    compareDate (value) {
      if (!value) return true
      let arr = ['1981', '1983', '1986', '1984', '1980', '1982', '1985']
      if (arr.includes(value)) {
        return true
      }
      let date = value
      date = date.slice(0, 4) + '-' + date.slice(4)
      date = date.slice(0, 7) + '-' + date.slice(7)
      if (new Date(date) < new Date(this.unitManageForm.stuBirth)) {
        return false
      } else {
        return true
      }
    },
    change (e, index, arr) {
      if (this.dateRegex(e)) {
        arr[index].value = e
      } else {
        arr[index].value = ''
      }
    },
    blur (e, index, arr) {
      if (this.dateRegex(e.target.value)) {
        if (!this.compareDate(e.target.value)) {
          arr[index].value = ''
          this.$util.errorMsg('不可小于出生日期')
        } else {
          arr[index].value = e.target.value
        }
      } else {
        arr[index].value = ''
        this.$util.errorMsg('日期输入格式例如：20080808')
      }
    },
    onSubmit () {
      this.isBreak = false
      let data = {
        stuStationcode: this.$store.state.schStationcode,
        schname: this.$store.state.schName,
        ...this.unitManageForm
      }
      // 组合数据
      let newObjJz = {}
      let newObjTd = {}
      data.jzList.map((v, i) => {
        if (!this.compareDate(v.value)) {
          this.isBreak = true
        }
        newObjJz[v.code] = v.value
      })
      data.tdList.map((v, i) => {
        if (!this.compareDate(v.value)) {
          this.isBreak = true
        }
        newObjTd[v.code] = v.value
      })
      if (this.isBreak) {
        this.$util.errorMsg('存在接种日期大于出生日期')
        return
      }
      delete data.jzList
      delete data.tdList
      data = {
        ...data,
        ...newObjJz,
        ...newObjTd
      }
      console.log(666, data)
      this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          this.openFullScreen('正在保存...')
          doctorEntry(data).then((res) => {
            if (res.message === '保存成功！') {
              this.$util.successMsg(res.message)
              this.unitManageForm = JSON.parse(JSON.stringify(this.unitManageFormReset))
            } else {
              this.$util.errorMsg(res.message)
            }
            this.closeFullScreen()
          }).catch(() => {
            this.closeFullScreen()
          })
        }
      })
    }
  },
  async created () {
    // 查询学年
    // await this.findClaYear()
    // if (this.schoolYearList.length > 0) {
    //   this.unitManageForm.stuclayear = this.schoolYearList[0].claNameYear
    // }
    await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.unitManageForm.stuclayear,lunci:this.unitManageForm.stuSeq })
  }
}
</script>
<style scoped>
.schoolDoctor .main-content{
  margin-top: -40px;
}
.scroll{
  overflow: scroll
}
.w_320{
  width: 320px;
}
.submit{
  text-align: center;
}
</style>
<style>
  .schoolDoctor .el-form-item__content{
    width: 320px;
  }
  .schoolDoctor .el-select{
    width: 320px !important;
  }
  .schoolDoctor .el-form-item{
    margin-bottom: 10px;
  }
  .schoolDoctor .el-form-item__error{
    padding-top: 0;
    margin-top: -2px;
  }
</style>
