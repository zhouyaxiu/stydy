<template>
  <!-- 信息查验-信息扫码 -->
  <div class="information">
    <ss-condition-area definedTitle="信息扫码" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="3" v-model="unitManageForm.radio1"><span
                class="texts">儿童姓名：</span></el-radio></div>
          <el-input v-model="unitManageForm.stuName" placeholder="请输入儿童姓名" clearable
            :disabled="unitManageForm.radio1 != 3" class="w200"></el-input>
        </div>
        <div class="item">
          <div class="label"><span class="texts">性别：</span></div>
          <el-select v-model="unitManageForm.stuSex" placeholder="请选择性别" clearable :disabled="unitManageForm.radio1 != 3"
            @change="$forceUpdate()">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </div>
        <div class="item">
          <div class="label"><span class="texts">出生日期：</span></div>
          <el-date-picker class="w_320" v-model="unitManageForm.stuBirth" :picker-options="startTimeOption"
            value-format="yyyy-MM-dd" format="yyyyMMdd" type="date" placeholder="请输入"
            :disabled="unitManageForm.radio1 != 3" @change="$forceUpdate()">
          </el-date-picker>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="1" v-model="unitManageForm.radio1"><span
                class="texts">儿童编码：</span></el-radio></div>
          <el-input v-model="unitManageForm.childNo" ref="searchInput" clearable :disabled="unitManageForm.radio1 != 1"
            placeholder="请输入儿童编码"></el-input>
        </div>
      </div>
      <div class="condition-row">
        <div class="item">
          <div class="label"><el-radio :label="2" v-model="unitManageForm.radio1"><span
                class="texts">儿童条码：</span></el-radio></div>
          <el-input v-model="unitManageForm.imonu" ref="searchInput" clearable :disabled="unitManageForm.radio1 != 2"
            placeholder="请输入儿童条码"></el-input>
        </div>
        <div class="item" style="align-items: center;">
          <div class="label">&nbsp;</div>
          <div style="display: flex;">
            <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
            <ss-custom-button @click="resetHandler" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" />
          </div>
        </div>
      </div>
      <!-- <ss-class-select
        ref="selectClass"
        :otherField="{'schoolname':true,'grade':true,'class':true,'classname':true,'classRequired':true,'schoolnameRequired':true}"
        >
      </ss-class-select> -->
    </ss-condition-area>
    <div class="main-content" style="position: relative;">
      <ss-custom-title type="info" text="学生信息" slot="title"></ss-custom-title>
      <div class="mt_10" style="display:flex;position: absolute;right: 25px;top: -14px;">
        <ss-custom-button @click="saveHandler" :type="$consts.CUSTOMBUTTONTYPES.save" class="mr_10" />
        <!-- <ss-custom-button @click="mobanHandler('预防接种证查验疫苗补证联系', 1)" :type="$consts.CUSTOMBUTTONTYPES.buzheng" /> -->
        <ss-custom-button @click="mobanHandler('预防接种证查验疫苗补种联系', 0)" :type="$consts.CUSTOMBUTTONTYPES.buzhong" />
        <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.chayan" @click="chayanHandler()" />
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-descriptions :column="1" border labelClassName="labelwidth">
            <el-descriptions-item label="学生姓名">{{ stuInfo.stuName }}</el-descriptions-item>
            <el-descriptions-item label="学生性别">{{ stuInfo.stuSex }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ stuInfo.stuBirth }}</el-descriptions-item>
            <el-descriptions-item label="父亲/母亲/监护人姓名">{{ stuInfo.stuParentname }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="6">
          <el-descriptions :column="1" border labelClassName="labelwidth">
            <el-descriptions-item label="家庭住址" contentStyle="height:92px" labelStyle="height:92px">{{ stuInfo.fResideAddr
            }}</el-descriptions-item>
            <el-descriptions-item label="所属预防接种单位" contentStyle="height:92px" labelStyle="height:92px">{{
              stuInfo.stuBelongStationcode }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="12">
          <el-descriptions :column="2" border labelClassName="labelwidth">
            <!-- <el-descriptions-item label="家庭住址" span="2">{{ stuInfo.fResideAddr }}</el-descriptions-item> -->
            <el-descriptions-item label="选择地区" labelStyle="color:red;" contentClassName="nopad">
              <ss-area-areaTreeStation class="area" :showSelect="false" ref="areaTreePage"
                :getToLevelNum="5 - currentLevel" @nodeClick="nodeClick" />
            </el-descriptions-item>
            <el-descriptions-item label="学校类型" labelStyle="color:red;" contentClassName="nopad">
              <el-select v-model="unitManageForm.checkType" placeholder="请选择学校类型" clearable class="select">
                <el-option label="幼托机构" value="0" />
                <el-option label="小学" value="1" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="学校名称" labelStyle="color:red;" contentClassName="nopad">
              <el-select filterable v-model="unitManageForm.schoolId" placeholder="请选择学校" @change="schoolChange" clearable
                class="select">
                <el-option v-for="item in schoolList" :key="item.schId" :label="item.schName" :value="item.schId" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="学年" labelStyle="color:red;" contentClassName="nopad">
              <el-select clearable v-model="unitManageForm.stuclayear" placeholder="请选择" class="select">
                <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="年级" labelStyle="color:red;" contentClassName="nopad">
              <el-select clearable v-model="unitManageForm.stuClalevel" placeholder="请选择" class="select"
                @change="$forceUpdate()">
                <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="轮次" labelStyle="color:red;" contentClassName="nopad">
              <el-select v-model="unitManageForm.lunci" placeholder="请选择" clearable class="select">
                <el-option label="春季" value="1" />
                <el-option label="秋季" value="0" />
              </el-select>
            </el-descriptions-item>
            <el-descriptions-item label="班级" labelStyle="color:red;" contentClassName="nopad">
              <el-input placeholder="请输入" clearable v-model.trim="unitManageForm.classId" maxlength="10"></el-input>
            </el-descriptions-item>
            <!-- <el-descriptions-item label="有无接种证" labelStyle="color:red;" contentClassName="nopad">
              <el-select placeholder="请选择" clearable class="select" v-model="stuInfo.stuIsjzz" disabled>
                <el-option label="有" value="0" />
                <el-option label="无" value="1" />
              </el-select>
            </el-descriptions-item> -->
          </el-descriptions>
        </el-col>
      </el-row>
    </div>
    <div style="margin:20px 0 100px;">
      <ss-custom-title type="record" text="接种情况" slot="title"></ss-custom-title>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="main-content">
            <el-descriptions :column="2" border labelClassName="labelwidth">
              <el-descriptions-item :span="2" label="是否种全">{{ stuInfo.stuIsjz == 0 ? '是' : stuInfo.stuIsjz == 1 ? '否-需补种':stuInfo.stuIsjz == -1 ? '否-无需补种':stuInfo.stuIsjz == null ? null : ''}}</el-descriptions-item>
              <el-descriptions-item contentStyle="display:none;" label="卡介苗"></el-descriptions-item>
              <el-descriptions-item label="1" labelClassName="labelwidth2">{{ stuInfo.d001 }}</el-descriptions-item>
              <el-descriptions-item label="乙肝疫苗" :span="2" contentClassName="nopad">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d063 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d064 }}</el-descriptions-item>
                  <el-descriptions-item label="3">{{ stuInfo.d065 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
              <el-descriptions-item label="脊灰疫苗" :span="2" contentClassName="nopad">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d009 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d010 }}</el-descriptions-item>
                  <el-descriptions-item label="3">{{ stuInfo.d011 }}</el-descriptions-item>
                  <el-descriptions-item label="4">{{ stuInfo.d012 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="main-content">
            <el-descriptions :column="2" border contentClassName="nopad" labelClassName="labelwidth">
              <el-descriptions-item label="百白破疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d015 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d016 }}</el-descriptions-item>
                  <el-descriptions-item label="3">{{ stuInfo.d017 }}</el-descriptions-item>
                  <el-descriptions-item label="4">{{ stuInfo.d018 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
              <el-descriptions-item contentStyle="display:none;" label="白破疫苗"
                labelClassName="labelwidth"></el-descriptions-item>
              <el-descriptions-item label="1" labelClassName="labelwidth2" contentStyle="padding:12px 10px!important;">{{
                stuInfo.d037 }}</el-descriptions-item>
              <el-descriptions-item label="麻腮风疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d059 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d060 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
              <el-descriptions-item label="A群流脑疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d040 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d041 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="main-content">
            <el-descriptions :column="2" border labelClassName="labelwidth" contentClassName="nopad">
              <el-descriptions-item label="A+C群流脑疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d045 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d046 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
              <el-descriptions-item label="乙脑疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d032 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d033 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
              <!-- <el-descriptions-item contentStyle="display:none;" label="甲肝疫苗" labelClassName="labelwidth"></el-descriptions-item>
              <el-descriptions-item label="1" labelClassName="labelwidth2" contentStyle="padding:12px 10px!important;">{{ stuInfo.d069 }}</el-descriptions-item> -->
              <el-descriptions-item label="甲肝灭活疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d073 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d074 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
              <el-descriptions-item label="*水痘疫苗" :span="2">
                <el-descriptions :column="1" border labelClassName="labelwidth2">
                  <el-descriptions-item label="1">{{ stuInfo.d050 }}</el-descriptions-item>
                  <el-descriptions-item label="2">{{ stuInfo.d132 }}</el-descriptions-item>
                </el-descriptions>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="main-content">
            <el-descriptions direction="vertical" :column="1" border>
              <el-descriptions-item label="说明">
                <ul class="infos">
                  <li>“X”表示需要补种疫苗；</li>
                  <li>是否种全：是指入托入学报到查验时是否完成全部免疫规划疫苗（不含带*的疫苗）的全程接种；</li>
                  <li>表格中的“数字”表示接种针次；</li>
                  <li>*表示自愿自费接种疫苗；</li>
                  <li>O表示当前疫苗为禁忌、已患或超期不种。</li>
                </ul>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 详细信息弹框 -->
    <info-scan-dialog :visible="dialogVisible" :dataList="dataList" @saveHandler="saveHandler" @close="close"
      v-if="dialogVisible" />
    <formwork-dialog :visible="dialogFVisible" @close="close" :formData="formData" />
    <mselectDialog :visible="dialogTableVisible" :gridData="gridData" :searchData="searchData"></mselectDialog>
    <check-dialog :visible="checkDialogVisible" v-if="checkDialogVisible" @close="closeCheck" :dataList="checkList" />
  </div>
</template>
<script>
import { queryPlStu, queryPlStuNo, savePlStu, savePlStuNo, okSave, queryStuByThreeCondition, saveStuByThreeCondition, queryReplantCard, queryReplantMess, queryReplantProve } from '@/utils/axios/apis/rxrt/showInforInspectionResultQuery'
import commonOptions from '@/mixin/commonOptions'
import InfoScanDialog from './components/InfoScanDialog.vue'
import formworkDialog from './components/formworkDialog.vue'
import mselectDialog from './components/mselectDialog.vue'
import { getLevel } from '@/utils/common/area.js'
import { findSchoolByAreaAndSchType } from '@/utils/axios/api'
import checkDialog from '../checkVaccination/components/checkDialog.vue'
export default {
  components: { InfoScanDialog, formworkDialog, mselectDialog, checkDialog },
  mixins: [commonOptions],
  data() {
    return {
      data: {},
      unitManageForm: {
        stuclayear: this.$dateutil.currentYear(),
        radio1: '',
        stuName: '',
        stuSex: '',
        stuBirth: '',
        childNo: '',
        imonu: '',
        stuClalevel: '',
        lunci: '',
        classId: '',
        checkType: '',
        schoolId: ''
      },
      height: window.winHeight - 15 / window.scale - 300,
      dialogVisible: false,
      dialogFVisible: false,
      dataList: [],
      startTimeOption: {
        disabledDate: time => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          )
        }
      },
      stuInfo: {},
      maininfo: {},
      formData: {},
      dialogTableVisible: false,
      gridData: [],
      searchData: {},
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      checkDialogVisible: false,
      checkList: []
    }
  },
  watch: {
    'unitManageForm.radio1'(newVal, oldVal) {
      if (newVal == 1 && (oldVal == 2 || oldVal == 3)) {
        if (oldVal == 2) this.unitManageForm.imonu = ''
        if (oldVal == 3) this.unitManageForm.stuName = ''; this.unitManageForm.stuSex = ''; this.unitManageForm.stuBirth = ''
      } else if (newVal == 2) {
        if (oldVal == 1) this.unitManageForm.childNo = ''
        if (oldVal == 3) this.unitManageForm.stuName = ''; this.unitManageForm.stuSex = ''; this.unitManageForm.stuBirth = ''
      } else if (newVal == 3 && (oldVal == 1 || oldVal == 2)) {
        if (oldVal == 1) this.unitManageForm.childNo = ''
        if (oldVal == 2) this.unitManageForm.imonu = ''
      } else if (newVal == '' && oldVal == 1) {
        this.unitManageForm.childNo = ''
      } else if (newVal == '' && oldVal == 2) {
        this.unitManageForm.imonu = ''
      } else if (newVal == '' && oldVal == 3) {
        this.unitManageForm.stuName = ''; this.unitManageForm.stuSex = ''; this.unitManageForm.stuBirth = ''
      }
    },
    async 'unitManageForm.checkType'(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.unitManageForm.schoolId = ''
        if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
          this.$util.errorMsg('必须选择地区！')
          return
        }
        await this.findSchool()
        this.unitManageForm.stuClalevel = ""
        await this.findClaLevelSecond(this.unitManageForm.checkType)
      }
    }
  },
  methods: {
    // 打印查验证明
    chayanHandler() {
      if (Object.keys(this.stuInfo).length == 0) {
        this.$util.errorMsg('必须先点击查询！')
        return
      }
      let params = {
        childNo: this.stuInfo.stuNo,
        stationName: this.stuInfo.stuStationname || this.$store.state.areaname,
        operateType: this.$consts.OPTYPE_PRINT,
      }
      // console.log(params)
      queryReplantProve(params).then(res => {
        this.checkList = [res.data];
        this.checkDialogVisible = true;
      })
    },
    closeCheck() {
      this.checkDialogVisible = false;
    },
    // 查询接口
    async search(pagenum) {
      // let flag = this.$refs['selectClass'].validateForm();
      // this.$refs['selectClass'].$children[0].clearValidate()
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      if(this.unitManageForm.radio1 && this.unitManageForm.childNo || this.unitManageForm.radio2 && this.unitManageForm.imonu ||this.unitManageForm.radio3 && this.unitManageForm.stuName && this.unitManageForm.stuSex && this.unitManageForm.stuBirth){
        this.unitManageForm.tchildNo=null
      }
      if (!this.unitManageForm.radio1) {
        this.$util.errorMsg('儿童编码或儿童条码或儿童姓名等至少选中一个！')
        return false
      }
      if (this.unitManageForm.radio1 == 1) {
        if (!this.unitManageForm.childNo) {
          this.$util.errorMsg('儿童编码不能为空！')
          return false
        }
        let params = {
          childNo: this.unitManageForm.childNo,
          operateType: this.$consts.OPTYPE_QUERY,
        }
        that.searchData = params
        that.openFullScreen()
        queryPlStuNo(that.searchData).then((res) => {
          if (res.code == 200) {
            that.stuInfo = res.data
          }
          if (res.code == 521) {
            this.$util.errorMsg(res.data)
          }
          that.secTime = res.sec
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      } else if (this.unitManageForm.radio1 == 2) {
        if (!this.unitManageForm.imonu) {
          this.$util.errorMsg('儿童条码不能为空！')
          return false
        }
        let params = {
          imonu: this.unitManageForm.imonu,
          operateType: this.$consts.OPTYPE_QUERY,
        }
        that.searchData = params
        that.openFullScreen()
        queryPlStu(that.searchData).then((res) => {
          if (res.code == 200) {
            that.stuInfo = res.data
          }
          if (res.code == 201) {
            this.dataList = res.data;
            this.dialogVisible = true;
          }
          if (res.code == 521) {
            this.$util.errorMsg(res.data)
          }
          that.secTime = res.sec
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      } else {
        if (!this.unitManageForm.stuName) {
          this.$util.errorMsg('受种者姓名不能为空！')
          return false
        }
        if (!this.unitManageForm.stuSex) {
          this.$util.errorMsg('受种者性别不能为空！')
          return false
        }
        if (!this.unitManageForm.stuBirth) {
          this.$util.errorMsg('受种者出生日期不能为空！')
          return false
        }
        let params = {
          stuName: this.unitManageForm.stuName,
          stuSex: this.unitManageForm.stuSex,
          stuBirth: this.unitManageForm.stuBirth,
          operateType: this.$consts.OPTYPE_QUERY,
        }
        that.searchData = params
        that.openFullScreen()
        queryStuByThreeCondition(that.searchData).then((res) => {
          if (res.code == 200) {
            that.stuInfo = res.data
          }
          if (res.code == 201) {
            this.dataList = res.data;
            this.dialogVisible = true;
          }
          if (res.code == 521) {
            this.$confirm(res.data, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.$router.push({
                path: '/rxrt/showInforInspectionResultQuery/manualAddition'
              })
            }).catch(() => {
            })
          }
          that.secTime = res.sec
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      }
    },
    async sonSearch(){
      let params = {
        childNo: this.unitManageForm.tchildNo,
        operateType: this.$consts.OPTYPE_QUERY,
      }
      this.openFullScreen()
      queryPlStuNo(params).then((res) => {
        if (res.code == 200) {
          this.stuInfo = res.data
        }
        if (res.code == 521) {
          this.$util.errorMsg(res.data)
        }
        this.secTime = res.sec
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    // 保存
    saveHandler(name) {
      let unitManageForm = this.unitManageForm
      if (Object.keys(this.stuInfo).length == 0) {
        this.$util.errorMsg('必须先点击查询！')
        return
      }
      if (!this.$refs.areaTreePage.selectAreaCode || this.$refs.areaTreePage.selectAreaCode.length === 0) {
        this.$util.errorMsg('必须选择地区！')
        return false
      }
      if (!this.unitManageForm.schoolId) {
        this.$util.errorMsg('请选择学校名称！')
        return false
      }
      if (!this.unitManageForm.stuclayear) {
        this.$util.errorMsg('请选择学年！')
        return false
      }
      if (!this.unitManageForm.stuClalevel) {
        this.$util.errorMsg('请选择年级！')
        return false
      }
      if (!this.unitManageForm.lunci) {
        this.$util.errorMsg('请选择轮次！')
        return false
      }
      if (!this.unitManageForm.classId) {
        this.$util.errorMsg('请输入班级！')
        return false
      }
      if (!/^[0-9A-Za-z\u4e00-\u9fa5]+$/.test(this.unitManageForm.classId)) {
        this.$util.errorMsg('班级只能是中文，英文，数字！')
        return false
      }

      let that = this;
      let params = {
        schId: unitManageForm.schoolId,
        stuclayear: unitManageForm.stuclayear,
        stuLevel: unitManageForm.stuClalevel,
        lunci: unitManageForm.lunci,
        claName: unitManageForm.classId,
        stationCode: unitManageForm.schStationCode,
        operateType: this.$consts.OPTYPE_INSERT,
      }
      // if(this.unitManageForm.radio1 && this.unitManageForm.childNo || this.unitManageForm.radio2 && this.unitManageForm.imonu ||this.unitManageForm.radio3 && this.unitManageForm.stuName && this.unitManageForm.stuSex && this.unitManageForm.stuBirth){
      //   this.unitManageForm.tchildNo=null
      // }
      if (this.unitManageForm.radio1 == 1 || (this.unitManageForm.radio1 == 2 && this.unitManageForm.tchildNo) || (this.unitManageForm.radio1 == 3 && this.unitManageForm.tchildNo)) {
        params.childNo = this.unitManageForm.tchildNo || this.unitManageForm.childNo
        that.searchData = params
        that.openFullScreen()
        savePlStuNo(that.searchData).then((res) => {
          if (res.code == 200) {
            this.$util.successMsg('保存成功！')
          }
          if (res.code == 201) {
            this.dataList = res.data;
            this.dialogVisible = true;
          }
          if (res.code == 202) {
            this.$confirm(res.data, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              params.stateCode = res.code
              okSave(params).then(res => {
                this.$message({
                  message: res.data || '保存成功!',
                  type: 'success'
                })
                this.closeFullScreen()
              }).catch(() => {
                this.closeFullScreen()
              })
            }).catch(() => {
            })
          }
          if (res.code == 204) {
            this.dialogTableVisible = true;
            this.gridData = res.data
            params.stateCode = 204
            params.childNo = this.stuInfo.stuNo
            this.searchData = params
          }
          if (res.code == 521 || res.code == 203) {
            this.$util.errorMsg(res.data)
            this.closeFullScreen()
          }
          that.closeFullScreen()
          // that.unitManageForm.tchildNo = ''
        }).catch(() => {
          that.closeFullScreen()
        })
      } else if (this.unitManageForm.radio1 == 2) {
        params.imonu = this.unitManageForm.imonu
        that.openFullScreen()
        savePlStu(params).then((res) => {
          if (res.code == 200) {
            this.$util.successMsg('保存成功！')
          }
          if (res.code == 201) {
            this.dataList = res.data;
            this.dialogVisible = true;
          }
          if (res.code == 202) {
            this.$confirm(res.data, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              params.stateCode = res.code
              params.childNo = this.stuInfo.stuNo
              okSave(params).then(res => {
                this.$message({
                  message: res.data || '保存成功!',
                  type: 'success'
                })
                this.closeFullScreen()
              }).catch(() => {
                this.closeFullScreen()
              })
            }).catch(() => {
            })
          }
          if (res.code == 204) {
            this.dialogTableVisible = true;
            params.stateCode = 204
            params.childNo = this.stuInfo.stuNo
            this.gridData = res.data
            this.searchData = params
          }
          if (res.code == 521 || res.code == 203) {
            this.$util.errorMsg(res.data)
            this.closeFullScreen()
            return
          }
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      } else if (this.unitManageForm.radio1 == 3) {
        params.stuName = this.unitManageForm.stuName
        params.stuSex = this.unitManageForm.stuSex
        params.stuBirth = this.unitManageForm.stuBirth
        that.openFullScreen()
        saveStuByThreeCondition(params).then((res) => {
          if (res.code == 200) {
            this.$util.successMsg('保存成功！')
          }
          if (res.code == 201) {
            this.dataList = res.data;
            this.dialogVisible = true;
          }
          if (res.code == 202) {
            this.$confirm(res.data, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning',
            }).then(() => {
              params.stateCode = res.code
              params.childNo = this.stuInfo.stuNo
              okSave(params).then(res => {
                this.$message({
                  message: res.data || '保存成功!',
                  type: 'success'
                })
                this.closeFullScreen()
              }).catch(() => {
                this.closeFullScreen()
              })
            }).catch(() => {
            })
          }
          if (res.code == 204) {
            params.stateCode = 204
            params.childNo = this.stuInfo.stuNo
            this.gridData = res.data
            this.searchData = params
            this.dialogTableVisible = true;
          }
          if (res.code == 521 || res.code == 203) {
            this.$util.errorMsg(res.data)
            this.closeFullScreen()
            return
          }
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      }
    },
    // 重置
    resetHandler() {
      this.unitManageForm.radio1 = ''
      this.unitManageForm.stuclayear = this.$dateutil.currentYear();
      this.unitManageForm.checkType = '';
      this.unitManageForm.schoolId = '';
      this.unitManageForm.stuClalevel = '';
      this.unitManageForm.lunci = '';
      this.unitManageForm.classId = '';
      this.unitManageForm.tchildNo='';
      this.stuInfo = {};
    },
    mobanHandler(title, maintype) {
      let unitManageForm = this.unitManageForm
      if (Object.keys(this.stuInfo).length == 0) {
        this.$util.errorMsg('必须先点击查询！')
        return
      }
      if (maintype == 0) {
        if (this.stuInfo.stuIsjz != 1) {
          this.$util.errorMsg('只有需补种的才可打印！')
          return
        }
        let params = {
          childNo: this.stuInfo.stuNo,
          stationName: this.stuInfo.stuStationname || this.$store.state.areaname,
          operateType: this.$consts.OPTYPE_PRINT,
        }
        queryReplantMess(params).then(res => {
          let arr = []
          for (let key in res.data.replantMap) {
            arr.push({ name: key, value: res.data.replantMap[key] })
          }
          // this.formData=res.data
          // this.formData.tableData=arr
          res.data.tableData = arr
          this.formData = {
            title: title + '通知单',
            maintype: 0,
            schId: unitManageForm.schoolId,
            customcontent: arr.length > 0 ? true : false,
            // tableData:arr,
            stuData: [res.data]
          }
          // this.stuData=res.data
          this.dialogFVisible = true;
        })
      }
      if (maintype == 1) {
        if (this.stuInfo.stuIsjzz == 0) {
          this.$util.errorMsg('已有接种证，无需打印！')
          return
        }
        let params = {
          childNo: this.stuInfo.stuNo,
          stationName: this.stuInfo.stuStationname || this.$store.state.areaname,
          operateType: this.$consts.OPTYPE_PRINT,
        }
        queryReplantCard(params).then(res => {
          this.formData = {
            title: title + '通知单',
            maintype: 1,
            schId: unitManageForm.schoolId,
            stuData: [res.data]
          }
          // this.stuData=res.data
          this.dialogFVisible = true;
        })
      }
    },
    nodeClick() {
      this.unitManageForm.schoolId = ''
      this.findSchool()
    },
    async schoolChange(val) {
      this.unitManageForm.schoolname = this.schoolList.filter(item => this.unitManageForm.schoolId==item.schId).map(item => item.schName).join(",")
      this.unitManageForm.schStationCode = this.schoolList.filter(item => this.unitManageForm.schoolId==item.schId).map(item => item.schStationCode).join(",")
      this.unitManageForm.stuClalevel = ""
      if (val) {
        let schType = this.schoolList.filter(item => item.schId == val)[0].schType;
        await this.findClaLevelSecond(schType)
      }
      this.$forceUpdate()
    },
    // 查询学校
    findSchool() {
      findSchoolByAreaAndSchType({
        areaCode: this.$refs.areaTreePage.selectAreaCode,
        areaLevel: this.$refs.areaTreePage.selectAreaLevel,
        schType: this.unitManageForm.checkType,
      }).then((res) => {
        this.schoolList = res.data
      })
    },
    close() {
      this.dialogVisible = false
      this.dataList = []
      this.dialogFVisible = false
      this.formData = {}
      this.dialogTableVisible = false;
      this.gridData = []
      this.searchData = {}
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    }
  },
  async mounted() {
    await this.findSchool()
    await this.findClaYear()
    await this.findClaLevelSecond()
  }
}
</script>
<style scoped>
.information .el-input {
  width: 200px
}

.information .condition-row .el-radio {
  margin-right: 0 !important;
}
</style>
<style>
.mr_10 {
  margin-right: 10px;
}

.information .el-input {
  width: 200px
}

.information .el-radio {
  margin-right: 0 !important;
}

.information .texts {
  font-size: 13px;
  color: #213255;
}

.information .labelwidth {
  width: 160px;
}

.information .labelwidth2 {
  width: 60px;
  text-align: center !important;
}

.information .nopad {
  padding: 0 !important;
  border-top: 0;
  border-bottom: 0;
}

.information .nopad .el-descriptions {
  margin: -1px;
}

.information .nopad tbody:last-of-type th,
.information .nopad tbody:last-of-type td {
  border-bottom-color: transparent;
  line-height: initial;
  border-bottom: 0;
}

.information .nopad tbody:first-of-type th,
.information .nopad tbody:first-of-type td {
  border-top-color: transparent;
  line-height: initial;
  border-top: 0;
}

.information .infos {
  list-style-type: decimal;
  margin: 0;
  padding-left: 16px;
  line-height: 26px;
}

.information .select {
  width: 100%;
  outline: none;
  border: 0;
}

.information .select .el-input.is-disabled .el-input__inner {
  background: #fff;
  color: #333;
  border: 0;
  cursor: default;
}

.information .select input {
  height: 45px !important;
  line-height: 45px !important;
  border-color: #fff;
  outline: none;
  width: 100% !important;
  border: 0;
}

.information .select .el-input,
.information .nopad .el-input {
  width: 100%;
  border: 0;
  border-color: #fff;
  outline: none;
}

.information .select .el-select:hover .el-input__inner,
.information .nopad .el-input__inner {
  border-color: #fff;
}
</style>