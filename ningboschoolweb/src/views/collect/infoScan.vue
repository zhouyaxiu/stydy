<template>
  <!-- 信息扫码 -->
  <div>
    <ss-condition-area-new definedTitle="信息扫码" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form ref="form" :model="form" size="medium" onsubmit="return false;" label-position="right" label-width="200px" :inline="true">
        <div class="condition-row">
          <el-form-item prop="stuclayear" :rules="{required: true, message: '请选择年份', trigger: 'blur'}" class="item">
            <div class="label" slot="label" style="min-width:160px;">年份：</div>
            <!-- <el-select clearable v-model="form.stuclayear" placeholder="请选择">
              <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear"/>
            </el-select> -->
            <el-input v-model="form.stuclayear" disabled></el-input>
          </el-form-item>
          <el-form-item prop="lunci" class="item" :rules="{required: true, message: '请选择轮次', trigger: 'blur'}">
            <div class="label" slot="label">轮次：</div>
            <!-- <el-select v-model="form.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select> -->
            <el-input v-model="form.lunci==1?'春季':'秋季'" disabled></el-input>
          </el-form-item>
          <el-form-item prop="stuClalevel" :rules="{required: true, message: '请选择年级', trigger: 'blur'}" class="item">
            <div class="label" slot="label" style="min-width:170px;">年级：</div>
            <el-select clearable v-model="form.stuClalevel" placeholder="请选择">
              <el-option v-for="item in gradeList" :key="item.claLevel" :label="item.claLevelCN" :value="item.claLevel"/>
            </el-select>
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="classId" :rules="{required: true, message: '请选择班级名称', trigger: 'blur'}" class="item">
            <div class="label" slot="label">班级名称：</div>
            <el-select clearable v-model="form.classId" placeholder="请选择">
              <el-option v-for="item in classesList" :key="item.classId" :label="item.claName" :value="item.classId"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="imonu" class="item">
            <div class="label" slot="label" style="min-width:160px;">受种者信息介质识别码：</div>
            <el-input v-model="form.imonu" ref="searchInput" @change="imonuHanler($event)" clearable></el-input>
          </el-form-item>
          <el-form-item prop="fchildNo" class="item">
            <div class="label" slot="label" style="min-width:170px;">受种者预防接种档案编号：</div>
            <el-input v-model="form.fchildNo" @change="fchildNoHanler($event)" clearable></el-input>
          </el-form-item>
        </div>
      </el-form>
    </ss-condition-area-new>
    <div class="">
      <el-row :gutter="10" :style="{ height: height + 'px' }">
        <el-col :span="8">
          <div class="main-content">
            <ss-custom-title type="info" text="基本信息"></ss-custom-title>
            <custom-descriptions :items="baseList" :style="{ height: height - 40 + 'px' }" style="overflow:scroll"></custom-descriptions>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="main-content">
            <ss-custom-title type="record" text="接种记录"></ss-custom-title>
            <custom-descriptions :items="jzList" :style="{ height: height - 40 + 'px' }" style="overflow:scroll"></custom-descriptions>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="main-content">
            <ss-custom-title type="instead" text="替代疫苗"></ss-custom-title>
            <custom-descriptions :items="tdList" :style="{ height: height - 40 + 'px' }" style="overflow:scroll"></custom-descriptions>
          </div>
        </el-col>
      </el-row>
    </div>
    <!-- 详细信息弹框 -->
    <info-scan-dialog
      :visible="dialogVisible"
      :dataList="dataList"
      :type="type"
      @close="close1"
      @saveHandler="saveHandler"
    />
    <detail-dialog-info
      :visible="dialogVisible1"
      :stuId="stuInfo"
      @close="close"
    />
  </div>
</template>
<script>
import { savePlStu, savePlStuByNo, oKSave } from '@/utils/axios/apis/collect'
import CustomDescriptions from '@/components/common/customDescriptions.vue'
import commonOptions from '@/mixin/commonOptions'
import detailDialogInfo from './excelImport/components/DialogInfo.vue'
import InfoScanDialog from './InfoScanDialog.vue'
export default {
  components: { CustomDescriptions, detailDialogInfo, InfoScanDialog },
  mixins: [commonOptions],
  data () {
    return {
      number: '',
      baseList: [
        { name: '受种者预防接种档案编号', code: 'stuNo', value: '' },
        { name: '受种者信息介质识别码', code: 'stuImuno', value: '' },
        { name: '受种者姓名', code: 'stuName', value: '' },
        { name: '出生日期', code: 'stuBirth', value: '' },
        { name: '所属预防接种单位', code: 'stuStationname', value: '' },
        { name: '上次入托/入学扫码日期', code: 'lastStuSmrq', value: '' },
        { name: '上次入托/入学扫码学校', code: 'lastStuSchname', value: '' },
        { name: '性别', code: 'stuSex', value: '' },
        { name: '户籍类型', code: 'stuType', value: '' },
        { name: '有无接种证', code: 'stuIsjzz', value: '' },
        { name: '父亲/母亲/监护人姓名', code: 'stuParentname', value: '' },
        { name: '数据来源', code: 'stuSourceid', value: '' },
        { name: '轮次', code: 'stuSeq', value: '' }
      ],
      jzList: [
        { name: '乙肝1', code: 'B063', value: '' },
        { name: '乙肝2', code: 'B064', value: '' },
        { name: '乙肝3', code: 'B065', value: '' },
        { name: '甲肝减毒1', code: 'B069', value: '' },
        { name: '脊灰减毒1', code: 'B009', value: '' },
        { name: '脊灰减毒2', code: 'B010', value: '' },
        { name: '脊灰减毒3', code: 'B011', value: '' },
        { name: '脊灰减毒4', code: 'B012', value: '' },
        { name: '百白破1', code: 'B015', value: '' },
        { name: '百白破2', code: 'B016', value: '' },
        { name: '百白破3', code: 'B017', value: '' },
        { name: '百白破4', code: 'B018', value: '' },
        { name: '白破1', code: 'B037', value: '' },
        { name: '麻疹1', code: 'B059', value: '' },
        { name: '麻疹2', code: 'B060', value: '' },
        { name: 'A群流脑1', code: 'B040', value: '' },
        { name: 'A群流脑2', code: 'B041', value: '' },
        { name: 'A+C群流脑1', code: 'B045', value: '' },
        { name: 'A+C群流脑2', code: 'B046', value: '' },
        { name: '乙脑减毒1', code: 'B032', value: '' },
        { name: '乙脑减毒2', code: 'B033', value: '' }
      ],
      tdList: [
        { name: '脊灰灭活1', code: 'B169', value: '' },
        { name: '脊灰灭活2', code: 'B170', value: '' },
        { name: '脊灰灭活3', code: 'B171', value: '' },
        { name: '脊灰灭活4', code: 'B172', value: '' },
        { name: '五联苗1', code: 'B163', value: '' },
        { name: '五联苗2', code: 'B164', value: '' },
        { name: '五联苗3', code: 'B165', value: '' },
        { name: '五联苗4', code: 'B166', value: '' },
        { name: '四联苗1', code: 'B157', value: '' },
        { name: '四联苗2', code: 'B158', value: '' },
        { name: '四联苗3', code: 'B159', value: '' },
        { name: '四联苗4', code: 'B160', value: '' },
        { name: '乙脑灭活1', code: 'B027', value: '' },
        { name: '乙脑灭活2', code: 'B028', value: '' },
        { name: '乙脑灭活3', code: 'B029', value: '' },
        { name: '乙脑灭活4', code: 'B030', value: '' },
        { name: 'AC流脑Hib联合1', code: 'B427', value: '' },
        { name: 'AC流脑Hib联合2', code: 'B428', value: '' },
        { name: 'AC流脑Hib联合3', code: 'B429', value: '' },
        { name: 'A+C结合流脑1', code: 'B138', value: '' },
        { name: 'A+C结合流脑2', code: 'B139', value: '' },
        { name: 'A+C结合流脑3', code: 'B140', value: '' },
        { name: 'A+C结合流脑4', code: 'B141', value: '' },
        { name: 'ACYW流脑1', code: 'B147', value: '' },
        { name: 'ACYW流脑2', code: 'B148', value: '' },
        { name: '甲肝灭活1', code: 'B073', value: '' },
        { name: '甲肝灭活2', code: 'B074', value: '' },
        { name: '甲乙肝1', code: 'B133', value: '' },
        { name: '甲乙肝2', code: 'B327', value: '' },
        { name: '麻风1', code: 'B093', value: '' },
        { name: '麻风2', code: 'B094', value: '' },
        { name: '麻腮风1', code: 'B089', value: '' },
        { name: '麻腮风2', code: 'B090', value: '' },
        { name: '麻腮1', code: 'B096', value: '' },
        { name: '麻腮2', code: 'B097', value: '' },
        { name: '青少年百白破1', code: 'B155', value: '' }
      ],
      rules: {
        number: [
          { required: true, message: '请输入受种者信息介质识别码', trigger: 'blur' }
        ]
      },
      data: {},
      form: {
        stuclayear: this.$dateutil.currentYear(),
        stuClalevel: '',
        classId: '',
        // claCode:'',
        imonu: '',
        fchildNo: '',
        lunci: sessionStorage.getItem('lunci') || '1',
        schStaionCode: ''
      },
      schoolYearList: [],
      height: window.winHeight - 15 / window.scale - 300,
      dialogVisible: false,
      dialogVisible1: false,
      dialogVisible2: false,
      stuInfo: {},
      dataList: [],
      type: 'imuno'
    }
  },
  computed: {
    unitManageFormCopy () {
      return JSON.parse(JSON.stringify(this.form))
    }
  },
  watch: {
    unitManageFormCopy: {
      deep: true,
      async handler (newVal, oldVal) {
        console.log(newVal,'newVal')
        // 学年变化，查找年级
        if (newVal.stuclayear !== oldVal.stuclayear) {
          this.form.stuClalevel = ''
          this.form.classId = ''
          this.form.claName = ''
          if (newVal.stuclayear && this.form.lunci) {
            await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            // if (this.defaultValue) {
            this.form.stuClalevel =  this.gradeList.length>0?this.gradeList[0].claLevel:''
            // }
            // 查找班级
            // let res=await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            // this.classList=res
          } else {
            this.gradeList = []
            this.classesList = []
          }
        }
        // 年级发生改变
        if (newVal.stuClalevel !== oldVal.stuClalevel) {
          this.form.classId = ''
          this.form.claName = ''
          if (newVal.stuClalevel && this.form.lunci) {
            // 查找班级
            await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            this.form.classId =  this.classesList && this.classesList.length > 0 ? this.classesList[0].classId : ''
          } else {
            this.classesList = []
          }
        }
      }
    },
    async "form.lunci"(newVal,oldVal){
      if(newVal==''){
        this.form.stuClalevel = ''
        this.form.classId = ''
        this.classesList = []
        this.gradeList = []
        return
      }
      if(newVal!=oldVal && newVal!=''){
        this.form.stuClalevel = ''
        this.form.classId = ''
        sessionStorage.setItem("lunci",newVal)
        // 查找年级
        await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.form.stuclayear,lunci:newVal })
        this.form.stuClalevel = this.form.lunci && this.gradeList.length>0?this.gradeList[0].claLevel:''
        // 查找班级
        if(this.gradeList.length>0 && this.form.lunci){
          await this.findClassNew({ schId: this.$store.state.schId,claLevel: this.form.stuClalevel, claNameYear: this.form.stuclayear,lunci:newVal })
          this.form.classId =  this.classesList && this.classesList.length > 0 ? this.classesList[0].classId : ''
        }
      }
    }
  },
  methods: {
    close () {
      this.dialogVisible1 = false
      this.stuInfo = {}
    },
    close1 () {
      this.dialogVisible = false
      this.dataList = []
    },
    saveHandler (field, name) {
      let params = {
        // fchildNo:fchildno,
        schId: this.$store.state.schId,
        claCode: this.form.classId,
        lunci: this.form.lunci,
        schStaionCode: this.$store.state.schStationcode,
        schName: this.$store.state.schName,
        stuclayear: this.form.stuclayear,
      }
      if (name === 'fchildno') {
        params.fchildNo = field
        this.openFullScreen()
        savePlStuByNo(params).then(res => {
          this.dialogVisible1 = false
          if (res.code == 500) {
            this.$util.errorMsg(res.msg)
            this.closeFullScreen()
            return
          }
          if (res.code == 202) {
            this.$util.errorMsg(res.msg)
            this.closeFullScreen()
            return
          }
          if (res.code == 201) {
            this.resetData()
            if (res.msg === '该受种者已存在!') {
              this.dialogVisible = false
              this.$util.errorMsg(res.msg)
              this.getData(res.data)
              this.closeFullScreen()
              return false
            }
            this.$confirm(res.msg, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              oKSave(params).then(res => {
                this.$message({
                  message: res.msg || '保存成功!',
                  type: 'success'
                })
                this.resetData()
                this.getData(res.stu)
                this.closeFullScreen()
              }).catch(() => {
                this.closeFullScreen()
              })
            }).catch(() => {
            })
            this.getData(res.data)
          }
          if (res.code == 200) {
            this.resetData()
            this.getData(res.data)
            this.$message({
              message: res.msg || '保存成功!',
              type: 'success'
            })
            this.closeFullScreen()
          }
          this.closeFullScreen()
        }).catch(() => {
          this.closeFullScreen()
        })
      } else {
        params.imonu = field
        this.openFullScreen()
        oKSave(params).then(res => {
          this.$message({
            message: res.msg || '保存成功!',
            type: 'success'
          })
          this.resetData()
          this.getData(res.stu)
          this.closeFullScreen()
        }).catch(() => {
          this.resetData()
          this.closeFullScreen()
        })
      }
      this.dialogVisible1 = false
    },
    getDetail (data) {
      this.stuInfo = data
      this.dialogVisible1 = true
    },
    fchildNoHanler () {
      if (!this.form.stuclayear) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('年份必填')
        return false
      }
      if (!this.form.lunci) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('轮次必填')
        return false
      }
      if (!this.form.stuClalevel) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('年级必填')
        return false
      }
      if (!this.form.classId) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('班级必填')
        return false
      }
      if (!this.form.fchildNo) return
      let params = {
        fchildNo: this.form.fchildNo,
        schId: this.$store.state.schId,
        claCode: this.form.classId,
        lunci: this.form.lunci,
        schStaionCode: this.$store.state.schStationcode,
        schName: this.$store.state.schName,
        stuclayear: this.form.stuclayear,
      }
      this.openFullScreen()
      savePlStuByNo(params).then(res => {
        this.form.fchildNo = ''
        if (res.code == 500) {
          this.$util.errorMsg(res.msg)
          this.closeFullScreen()
          return
        }
        if (res.code == 202) {
          this.$util.errorMsg(res.msg)
          this.closeFullScreen()
          return
        }
        if (res.code == 201) {
          this.resetData()
          if (res.msg === '该受种者已存在!') {
            this.dialogVisible = false
            this.$util.errorMsg(res.msg)
            this.getData(res.data)
            this.closeFullScreen()
            return false
          }
          this.$confirm(res.msg, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            oKSave(params).then(res => {
              this.$message({
                message: res.msg || '保存成功!',
                type: 'success'
              })
              this.resetData()
              this.getData(res.stu)
              this.closeFullScreen()
            }).catch(() => {
              this.closeFullScreen()
            })
          }).catch(() => {
          })
          this.getData(res.data)
        }
        if (res.code == 200) {
          this.resetData()
          this.getData(res.data)
          this.$message({
            message: res.msg || '保存成功!',
            type: 'success'
          })
          this.closeFullScreen()
        }
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    imonuHanler () {
      if (!this.form.stuclayear) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('年份必填')
        return false
      }
      if (!this.form.lunci) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('轮次必填')
        return false
      }
      if (!this.form.stuClalevel) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('年级必填')
        return false
      }
      if (!this.form.classId) {
        this.form.imonu=''
        this.form.fchildNo=''
        this.$util.errorMsg('班级必填')
        return false
      }
      if (!this.form.imonu) return
      let params = {
        imonu: this.form.imonu,
        schId: this.$store.state.schId,
        claCode: this.form.classId,
        lunci: this.form.lunci,
        schStaionCode: this.$store.state.schStationcode,
        schName: this.$store.state.schName,
        stuclayear: this.form.stuclayear,
      }
      this.openFullScreen()
      savePlStu(params).then(res => {
        this.form.imonu = ''
        if (res.code == 500) {
          this.$util.errorMsg(res.msg)
          this.closeFullScreen()
          return
        }
        if (res.code == 501) {
          if (res.data.length > 1) {
            this.dataList = res.data
            this.dialogVisible = true
            this.type = 'fchildno'
          } else {
            this.$util.errorMsg(res.msg)
          }
          this.closeFullScreen()
          return
        }
        if (res.code == 202) {
          this.$util.errorMsg(res.msg)
          this.closeFullScreen()
          return
        }
        if (res.code == 201) {
          this.resetData()

          if (res.msg === '该受种者已存在!') {
            this.dialogVisible = false
            this.$util.errorMsg(res.msg)
            this.getData(res.data)
            this.closeFullScreen()
            return false
          }
          this.$confirm(res.msg, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            oKSave(params).then(res => {
              this.$message({
                message: res.msg || '保存成功!',
                type: 'success'
              })
              this.resetData()
              this.getData(res.stu)
              this.closeFullScreen()
            }).catch(() => {
              this.resetData()
              this.closeFullScreen()
            })
          }).catch(() => {
          })
          this.getData(res.mainStu)
        }
        if (res.code == 200) {
          this.resetData()
          this.getData(res.data)
          this.$message({
            message: res.msg || '保存成功!',
            type: 'success'
          })
          this.closeFullScreen()
        }
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    resetData () {
      setTimeout(() => {
        this.jzList.map(item => item.value = '')
        this.baseList.map(item => item.value = '')
        this.tdList.map(item => item.value = '')
      }, 5000)
    },
    getData (data) {
      for (let key in data) {
        this.jzList.map(item => {
          if (item.code === key) {
            item.value = data[key]
          }
        })
        this.baseList.map(item => {
          if (item.code === key) {
            item.value = data[key]
            if (item.code === 'stuSourceid') {
              let value = item.value === '0' ? '信息系统提供' : (item.value === '1' ? 'excel导入' : '校医录入')
              item.value = value
            }
            if (item.code === 'stuIsjzz') {
              let value = item.value === '0' ? '有' : '无'
              item.value = value
            }
            if(item.code=='stuSeq'){
              let value=item.value==1?'春季':'秋季'
              item.value=value;
            }
          }
        })
        this.tdList.map(item => {
          if (item.code === key) {
            item.value = data[key]
          }
        })
      }
    },
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    }
  },
  async mounted () {
    // 查询学年
    this.$refs.searchInput.focus()
    // await this.findClaYear()
    // if (this.schoolYearList.length > 0) {
    //   this.form.stuclayear = this.schoolYearList[0].claNameYear
    // }
    await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.form.stuclayear,lunci:this.form.lunci })
    this.form.stuClalevel =  this.gradeList.length>0?this.gradeList[0].claLevel:''
  }
}
</script>
