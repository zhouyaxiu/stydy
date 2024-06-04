<template>
  <el-dialog
    title="查验信息修改"
    :visible.sync="visible"
    :before-close="close"
    :close-on-click-modal="false"
    custom-class="cydialog"
    width="70%"
    :lock-scroll="true" :append-to-body="true"
  >
    <div class="areaInfoDialog" :style="{ height: height - 50 + 'px' }" style="overflow-y: auto;">
      <el-form label-width="200px" label-position="right" :model="unitManageForm" ref="unitManageForm">
        <el-col :span="8">
          <div style="padding-left: 60px;"><ss-custom-title type="info" text="基本信息"></ss-custom-title></div>
          <div class="scroll">
            <el-form-item label="受种者预防接种档案编号：" prop="stuNo">
              <el-input v-model="unitManageForm.stuNo" readonly disabled />
            </el-form-item>
            <el-form-item label="受种者信息介质识别码：" prop="stuImuno">
              <el-input v-model="unitManageForm.stuImuno" readonly disabled />
            </el-form-item>
            <el-form-item label="所属预防接种单位：" prop="stuStationname">
              <el-input v-model="unitManageForm.stuStationname" readonly disabled />
            </el-form-item>
            <el-form-item label="上次入托/入学扫码日期：" prop="lastStuSmrq">
              <el-input v-model="unitManageForm.lastStuSmrq" readonly disabled />
            </el-form-item>
            <el-form-item label="上次入托/入学扫码学校：" prop="lastStuSchname">
              <el-input v-model="unitManageForm.lastStuSchname" readonly disabled />
            </el-form-item>
            <el-form-item label="年份：" prop="stuclayear">
              <el-input v-model="unitManageForm.stuclayear" readonly disabled />
            </el-form-item>
            <el-form-item label="轮次：" prop="stuSeq">
              <el-input clearable maxlength="20" v-model.trim="unitManageForm.stuSeq" readonly disabled class="w_320"></el-input>
            </el-form-item>
            <el-form-item label="年级：" prop="stuClalevel">
              <el-input v-model="unitManageForm.stuClalevel" readonly disabled />
            </el-form-item>
            <el-form-item label="受种者班级：" prop="stuClassname">
              <el-input v-model="unitManageForm.stuClassname" readonly disabled />
            </el-form-item>
            <el-form-item label="受种者姓名：" prop="stuName">
              <el-input clearable v-model.trim="unitManageForm.stuName" placeholder="请输入" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item label="性别：" prop="stuSex">
              <el-radio-group v-model="unitManageForm.stuSex" style="white-space: nowrap;">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
                <el-radio label="未知">未知</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="接种证：" prop="stuIsjzz">
              <el-radio-group v-model="unitManageForm.stuIsjzz" style="white-space: nowrap;">
                <el-radio label="0" :disabled="unitManageForm.stuIsjzztemp=='0'?true:false">有</el-radio>
                <el-radio label="1" :disabled="unitManageForm.stuIsjzztemp=='0'?true:false">无</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期：" prop="stuBirth">
              <!-- value-format="yyyyMMdd" format="yyyyMMdd"  -->
              <el-date-picker class="w_320" v-model="unitManageForm.stuBirth" :picker-options="startTimeOption" type="date" placeholder="请输入">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="户籍类型：" prop="stuType">
              <el-select clearable v-model="unitManageForm.stuType" placeholder="请选择" class="w_320">
                <el-option v-for="item in typeList" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </el-form-item>
            <el-form-item label="父亲/母亲/监护人姓名：" prop="stuParentname">
              <el-input clearable maxlength="20" v-model.trim="unitManageForm.stuParentname" placeholder="请输入" class="w_320"></el-input>
            </el-form-item>
            <el-form-item label="备注：" prop="remark">
              <el-input clearable maxlength="26" v-model.trim="unitManageForm.remark" placeholder="请输入" class="w_320"></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <div style="padding-left: 60px;"><ss-custom-title type="record" text="接种记录"></ss-custom-title></div>
          <div class="scroll">
            <el-form-item v-for="(jz,index) in jzList" :label="jz.name" :prop="jz.code" :key="index" >
              <el-select clearable v-model="jz.value" filterable allow-create default-first-option @change="change($event,index,jzList)" @blur="blur($event,index,jzList)">
                <el-option v-for="item in recordList" :key="item.code" :label="item.name" :value="item.code" class="w_320" />
              </el-select>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="8">
          <div style="padding-left: 60px;"><ss-custom-title type="instead" text="替代疫苗"></ss-custom-title></div>
          <div class="scroll">
            <el-form-item v-for="(td,index) in tdList" :label="td.name" :prop="td.code" :key="index">
              <el-select clearable v-model="td.value" filterable allow-create default-first-option @change="change($event,index,tdList)" @blur="blur($event,index,tdList)">
                <el-option v-for="item in recordList" :key="item.code" :label="item.name" :value="item.code" class="w_320" />
              </el-select>
            </el-form-item>
          </div>
        </el-col>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="save" type="primary">保 存</el-button>
      <el-button @click="close">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getOneStudent, updateOneStudent } from '@/utils/axios/apis/check'
export default {
  name: 'receptionInfoDialog',
  data () {
    return {
      editorVisible: false,
      isBreak: false,
      height: window.winHeight - 15 / window.scale - 250,
      startTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
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
      ClaLevelList: [
        { code: '0', name: '幼儿园托班' },
        { code: '1', name: '幼儿园小班' },
        { code: '2', name: '幼儿园中班' },
        { code: '3', name: '幼儿园大班' },
        { code: '4', name: '一年级' },
        { code: '5', name: '二年级' },
        { code: '6', name: '三年级' },
        { code: '7', name: '四年级' },
        { code: '8', name: '五年级' },
        { code: '9', name: '六年级' }
      ],
      classesList: [],
      unitManageForm: {
        stuNo: '',
        stuImuno: '',
        stuStationname: '',
        lastStuSchname: '',
        lastStuSmrq: '',
        stuclayear: '',
        stuClalevel: '',
        stuClassname: '',
        stuName: '',
        stuSex: '男',
        stuIsjzz: '0',
        stuIsjzztemp: '0',
        stuBirth: '',
        stuType: '',
        stuParentname: '',
        stuPhone: '',
        stuOtherphone: '',
        remark: '',
        stuSeq:''
      },
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
    }
  },
  props: {
    stuId: {
      type: String,
      default: ''
    },
    visible: {
      type: Boolean,
      required: true,
      default: false
    }
    // releaseUser: {
    //   type: String,
    //   default: ''
    // }
  },
  watch: {
    stuId: {
      immediate: true,
      handler (val) {
        if (val) this.getDetailInfo()
      }
    }
  },
  methods: {
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
    getDetailInfo () {
      if (!this.stuId || this.stuId === '') return false
      this.openFullScreen()
      getOneStudent({ stuId: this.stuId })
        .then((res) => {
          if (res && res.data) {
            res.data.stuClalevel = this.ClaLevelList.filter(item => item.code === res.data.stuClalevel)[0].name
            this.unitManageForm = {
              stuNo: res.data.stuNo,
              stuImuno: res.data.stuImuno,
              stuStationname: res.data.stuStationname,
              lastStuSmrq: res.data.lastStuSmrq,
              lastStuSchname: res.data.lastStuSchname,
              stuclayear: res.data.stuclayear,
              stuClalevel: res.data.stuClalevel,
              stuClassname: res.data.stuClassname,
              stuName: res.data.stuName,
              stuSex: res.data.stuSex,
              stuIsjzz: res.data.stuIsjzz,
              stuIsjzztemp: res.data.stuIsjzz,
              stuBirth: res.data.stuBirth,
              stuType: res.data.stuType,
              stuParentname: res.data.stuParentname,
              stuPhone: res.data.stuPhone,
              stuOtherphone: res.data.stuOtherphone,
              rowkey: res.data.rowkey,
              stuSeq:res.data.stuSeq=='1'?'春季':'秋季',
              remark: res.data.remark
            }
            this.stuBirth1 = res.data.stuBirth
            for (let key in res.data) {
              this.jzList.map(item => {
                if (item.code === key) {
                  item.value = res.data[key]
                }
              })

              this.tdList.map(item => {
                if (item.code === key) {
                  item.value = res.data[key]
                }
              })
            }
          } else {
            this.$util.errorMsg('信息被删除或者不存在！')
          }
          this.closeFullScreen()
        })
        .catch(() => {
          this.closeFullScreen()
        })
    },
    close () {
      this.$emit('close', true)
    },
    formatDate (time) {
      var date = new Date(time)
      var y = date.getFullYear()
      var m = date.getMonth() + 1
      m = m < 10 ? '0' + m : m
      var d = date.getDate()
      d = d < 10 ? '0' + d : d
      return y + '-' + m + '-' + d
    },
    save () {
      this.isBreak = false
      this.unitManageForm.stuBirth = this.unitManageForm.stuBirth ? this.formatDate(this.unitManageForm.stuBirth) : ''
      console.log(this.unitManageForm.stuBirth)
      if (!this.unitManageForm.stuName) {
        this.$util.errorMsg('受种者姓名必填')
        return false
      }
      if (!this.unitManageForm.stuSex) {
        this.$util.errorMsg('性别必填')
        return false
      }
      if (!this.unitManageForm.stuBirth || this.unitManageForm.stuBirth === '1970-01-01') {
        this.$util.errorMsg('出生日期必填')
        this.unitManageForm.stuBirth = ''
        return false
      }
      if (!this.unitManageForm.stuType) {
        this.$util.errorMsg('户籍类型必填')
        return false
      }
      if (!this.unitManageForm.stuParentname) {
        this.$util.errorMsg('父亲/母亲/监护人姓名必填')
        return false
      }
      let data = {
        ...this.unitManageForm,
        jzList: this.jzList,
        tdList: this.tdList
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
        this.$util.errorMsg('存在接种日期小于出生日期')
        return
      }
      delete data.jzList
      delete data.tdList
      data = {
        ...data,
        ...newObjJz,
        ...newObjTd,
        fullcode: sessionStorage.id
      }
      this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          this.openFullScreen('正在保存...')
          updateOneStudent(data).then((res) => {
            if (res.message === '修改成功！') {
              this.$util.successMsg(res.message)
              this.$emit('close', true)
            } else {
              this.$util.warningMsg(res.message)
              this.$emit('close', true)
            }
            this.closeFullScreen()
          }).catch(() => {
            this.closeFullScreen()
          })
        }
      })
    }
  }
}
</script>
<style>
  .cydialog{min-width: 1300px;top:50%;height:90%;}
  .cydialog .el-dialog__body{max-height: 85%;}
  .cydialog .el-input{min-width: 200px;}
  .cydialog .el-date-editor{width:200px;}
</style>