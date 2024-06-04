<template>
  <!-- 信息补种 -->
  <div>
    <ss-condition-area-new ref="conditionPage">
      <el-form ref="form" :model="form" size="medium" onsubmit="return false;" label-position="right" label-width="200px" :inline="true">
        <div class="condition-row">
          <el-form-item prop="stuclayear" :rules="{required: true, message: '请选择年份', trigger: 'blur'}" class="item" style="width: 22%;">
              <div class="label" slot="label" style="min-width:160px;">年份：</div>
              <!-- <el-select clearable v-model="form.stuclayear" placeholder="请选择">
                <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear"/>
              </el-select> -->
              <el-input v-model="form.stuclayear" disabled></el-input>
            </el-form-item>
          <el-form-item :rules="{required: true, message: '请选择轮次', trigger: 'blur'}" class="item" style="width: 22%;">
            <div class="label" slot="label" style="min-width:160px;">轮次：</div>
            <!-- <el-select v-model="form.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select> -->
            <el-input v-model="form.lunci==1?'春季':'秋季'" disabled></el-input>
          </el-form-item>
          <el-form-item class="item" style="width: 28%;">
            <div class="label" slot="label" style="min-width:160px;">受种者信息介质识别码：</div>
            <el-input ref="searchInput" clearable v-model.trim="form.number" @keyup.enter.native="onSubmit" style="width: 200px" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item class="item" style="width: 25%;">
            <ss-custom-button @click="onSubmit" :type="$consts.CUSTOMBUTTONTYPES.query" class="ml_60 fl" />
            <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_20 fl" />
          </el-form-item>
        </div>
      </el-form>
    </ss-condition-area-new>
    <div class="main-content">
      <el-row type="flex">
        <el-col :span="8" class="col" :style="{ height: height + 'px' }">
          <ss-custom-title type="info" text="基本信息"></ss-custom-title>
          <el-descriptions :column="1" border :labelStyle="labelstyle" class="scroll" :style="{ height: height - 80 + 'px' }">
            <el-descriptions-item label="受种者预防接种档案编号">{{data.stuNo||''}}</el-descriptions-item>
            <el-descriptions-item label="受种者信息介质识别码">{{data.stuImuno||''}}</el-descriptions-item>
            <el-descriptions-item label="受种者姓名">{{data.stuName||''}}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{data.stuBirth||''}}</el-descriptions-item>
            <el-descriptions-item label="性别">{{data.stuSex||''}}</el-descriptions-item>
            <el-descriptions-item label="户籍类型">{{data.stuType||''}}</el-descriptions-item>
            <el-descriptions-item label="有无接种证">{{data.stuIsjzz===undefined?'':data.stuIsjzz==='0'?'有':'无'}}</el-descriptions-item>
            <el-descriptions-item label="父亲/母亲/监护人姓名">{{data.stuParentname||''}}</el-descriptions-item>
            <el-descriptions-item label="轮次">{{data.stuSeq===undefined?'':data.stuSeq==='1'?'春季':'秋季'}}</el-descriptions-item>
          </el-descriptions>
        </el-col>
        <el-col :span="8" class="col" :style="{ height: height + 'px' }">
            <ss-custom-title type="record" text="接种记录"></ss-custom-title>
            <el-descriptions :column="1" border :labelStyle="labelstyle" class="scroll" :style="{ height: height - 80 + 'px' }">
              <el-descriptions-item v-for="(jz,index) in jzList" :key="index" :label="jz.name">{{data[jz.code]}}</el-descriptions-item>
            </el-descriptions>
          </el-col>
        <el-col :span="8" class="col" :style="{ height: height + 'px' }">
          <ss-custom-title type="instead" text="替代疫苗"></ss-custom-title>
          <el-descriptions :column="1" border :labelStyle="labelstyle" class="scroll" :style="{ height: height - 80 + 'px' }">
              <el-descriptions-item v-for="(td,index) in tdList" :key="index" :label="td.name">{{data[td.code]}}</el-descriptions-item>
            </el-descriptions>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { revaccination } from '@/utils/axios/apis/collect'
import commonOptions from '@/mixin/commonOptions'
export default {
  mixins: [commonOptions],
  data () {
    return {
      height: window.winHeight - 15 / window.scale - 184,
      form:{
        stuclayear:this.$dateutil.currentYear(),
        lunci:sessionStorage.getItem("defaultLunci"),
        number: '',
      },
      labelstyle: {width:'270px',color:'#333333',background:'rgba(235, 239, 255, 0.2)'},
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
      data: {},
      schoolYearList: [],
    }
  },
  // computed: {
  //   unitManageFormCopy () {
  //     return JSON.parse(JSON.stringify(this.form))
  //   }
  // },
  // watch: {
  //   unitManageFormCopy: {
  //     deep: true,
  //     async handler (newVal, oldVal) {
  //       // 学年变化，查找年级
  //       if (newVal.stuclayear !== oldVal.stuclayear) {
  //         this.form.stuClalevel = ''
  //         this.form.classId = ''
  //         this.form.claName = ''
  //         if (newVal.stuclayear) {
  //           await this.findClaLevel({ claNameYear: newVal.stuclayear })
  //           // if (this.defaultValue) {
  //           this.form.stuClalevel = this.gradeList[0].claLevel
  //           // }
  //           // 查找班级
  //           let res=await this.findClassNew({ claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
  //           this.classList=res
  //         } else {
  //           this.gradeList = []
  //           this.classesList = []
  //         }
  //       }
  //       // 年级发生改变
  //       if (newVal.stuClalevel !== oldVal.stuClalevel) {
  //         this.form.classId = ''
  //         this.form.claName = ''
  //         if (newVal.stuClalevel) {
  //           // 查找班级
  //           await this.findClassNew({ claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
  //           this.form.classId = this.classesList && this.classesList.length > 0 ? this.classesList[0].classId : ''
  //         } else {
  //           this.classesList = []
  //         }
  //       }
  //     }
  //   },
  // },
  methods: {
    onSubmit () {
      if (!this.form.stuclayear) {
        this.$util.errorMsg('请选择年份！')
        return
      }
      if (!this.form.lunci) {
        this.$util.errorMsg('请选择轮次！')
        return
      }
      if (!this.form.number) {
        this.$util.errorMsg('请输入受种者信息介质识别码！')
        return
      }
      revaccination({ imuno: this.form.number, fchildNo: '',stuclayear:this.form.stuclayear,lunci:this.form.lunci }).then(res => {
        if (res.message) {
          if (res.message === '存在多个学生，无法补种！') {
            this.$util.errorMsg(res.message)
          } else {
            this.$confirm('该受种者本系统中不存在，是否新采集数据？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.$router.push({ path: '/collect/infoScan' })
            })
          }
        } else {
          this.data = res.data
        }
      })
    },
    async reset () {
      this.form.number = ''
      this.form.lunci = ''
      this.data = {}
      this.$refs.searchInput.focus()
      await this.findClaYear()
      if (this.schoolYearList.length > 0) {
        this.form.stuclayear = this.schoolYearList[0].claNameYear
      }
    }
  },
  async mounted () {
    this.$refs.searchInput.focus()
    // await this.findClaYear()
    // if (this.schoolYearList.length > 0) {
    //   this.form.stuclayear = this.schoolYearList[0].claNameYear
    // }
  }
}
</script>
<style scoped>
.col{
  background: #ffffff;
  padding: 16px 20px;
  border-radius: 10px;
}
.col:not(:last-child){
  margin-right: 10px;
}
.scroll{
  overflow: scroll;
}
.main-content{
  background-color: rgba(0, 0, 0, 0);
  padding: 0;
}

</style>
