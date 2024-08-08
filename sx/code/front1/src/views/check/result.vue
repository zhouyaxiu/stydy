<template>
  <!-- 查验结果查询 -->
  <div class="checkresult">
    <ss-condition-area-new definedTitle="查验结果查询" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form ref="form" :model="form" size="medium" onsubmit="return false;" label-position="right" label-width="200px" :inline="true">
        <div class="condition-row">
          <el-form-item prop="stuclayear" class="item" style="width:30%">
            <div class="label" slot="label"><span class="red">*</span>年份：</div>
            <el-select clearable v-model="form.stuclayear" placeholder="请选择">
              <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="stuName" class="item" label-width="140px" style="width:40%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllName" @change="val => changeSourceId(val, '受种者姓名')">受种者姓名：</el-checkbox>
            </div>
            <el-input clearable v-model="form.stuName" :disabled="!exportAllName"></el-input>
          </el-form-item>
          <el-form-item prop="ifHasjzz" class="item" style="width:20%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllJzz" @change="val => val?form.ifHasjzz=[0,1]:form.ifHasjzz=[0,1]">是否有接种证：</el-checkbox>
            </div>
            <el-checkbox-group v-model="form.ifHasjzz" :disabled="!exportAllJzz" class="flex">
              <el-checkbox :label="0">有</el-checkbox>
              <el-checkbox :label="1">无</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="lunci" class="item" style="width:30%">
            <div class="label" slot="label"><span class="red">*</span>轮次：</div>
            <el-select v-model="form.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item prop="sourceId" class="item" label-width="140px" style="width:40%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAll" @change="val => changeSourceId(val, '数据来源')">数据来源：</el-checkbox>
            </div>
            <el-checkbox v-model="checkAll" :disabled="!exportAll" @change="val => changeSourceId(val, '全部')">全部</el-checkbox>
            <el-checkbox-group v-model="form.sourceId" :disabled="!exportAll" class="flex">
              <el-checkbox v-for="item in sourceIdList" :label="item.id" :key="item.id" @change="val => changeSourceId(val, '数据')">{{item.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item prop="ifIzFull" class="item" style="width:20%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllFull" @change="val => val?form.ifIzFull=[0,1]:form.ifIzFull=[0,1]">是否种全：</el-checkbox>
            </div>
            <el-checkbox-group v-model="form.ifIzFull" :disabled="!exportAllFull" class="flex">
              <el-checkbox :label="0">是</el-checkbox>
              <el-checkbox :label="1">否</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="stuClalevel" class="item" style="width:30%">
            <div class="label" slot="label"><span class="red">*</span>年级：</div>
            <el-select clearable v-model="form.stuClalevel" placeholder="请选择" multiple collapse-tags>
              <el-option v-for="item in gradeList" :key="item.claLevel" :label="item.claLevelCN" :value="item.claLevel"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="stuImuno" class="item" label-width="140px" style="width:50%;padding-left: 24px;">
            <div class="label" slot="label">
              <el-checkbox style="vertical-align: middle;" v-model="exportAllType" @change="val => changeSourceId(val, '户籍类型')">户籍类型：</el-checkbox>
            </div>
            <ss-check-resiType ref="checkResiTypePage" :disabled="!exportAllType" style="vertical-align: middle;" />
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="classId" class="item">
            <div class="label" slot="label"><span class="red">*</span>班级名称：</div>
            <ss-custom-select ref="cSelect" :selectList="classesList" :selectType="3" :dataInfo="{clearable:true,value:'',name:'',optsLabel:'',optsValue:''}" @callback="changeFun"></ss-custom-select>
            <!-- <el-select clearable v-model="form.classId" placeholder="请选择" multiple collapse-tags>
              <el-option v-for="item in classesList" :key="item.classId" :label="item.claFullName.substr(4)" :value="item.classId"/>
            </el-select> -->
            <!-- <el-select clearable v-model="form.classId" placeholder="请选择">
              <el-option v-for="item in classesList" :key="item.classId" :label="item.claName" :value="item.classId"/>
            </el-select> -->
          </el-form-item>
          <div class="item">
            <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
            <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" />
            <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
            <ss-custom-button @click="dialogVisible1=true" :type="$consts.CUSTOMBUTTONTYPES.column" class="ml_10" />
            <ss-custom-button @click="prints" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" />
          </div>
        </div>
      </el-form>
    </ss-condition-area-new>
    <div class="content-area">
      <ss-table :data="dataList" @handleSelectionChange="handleSelectionChange" ref="tabPage" class="infoTable" :pageSize="20" @goPage="search" :pagenum="pagenum" :total="total">
        <el-table-column
          type="selection"
          width="55"
          align="center"
          :selectable="canBeSelect">
        </el-table-column>
        <el-table-column prop="stuSourceid" align="center" min-width="180" label="数据来源">
          <template slot-scope="{ row }">
            <span>{{ row.stuSourceid === '0' ? '信息系统提供' : row.stuSourceid === '1'?'excel导入':'校医录入' }}</span>
          </template>
        </el-table-column>
        <el-table-column type="index" align="center" width="120" label="流水号" />
        <el-table-column prop="stuClassname" align="center" min-width="180" label="受种者班级" />
        <el-table-column prop="stuNo" align="center" min-width="180" label="受种者预防接种档案编号" />
        <el-table-column prop="stuImuno" align="center" min-width="180" label="受种者信息介质识别码" />
        <el-table-column prop="stuType" align="center" min-width="120" label="户籍类型" />
        <el-table-column prop="stuName" align="center" min-width="150" label="受种者姓名" />
        <el-table-column prop="stuSex" align="center" label="性别" />
        <el-table-column prop="stuBirth" align="center" min-width="120" label="出生日期" />
        <el-table-column prop="stuSeq" align="center" min-width="120" label="轮次">
          <template slot-scope="{ row }">
            <span>{{ row.stuSeq === '1' ? '春季' : '秋季' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuIsjzz" align="center" min-width="100" label="是否有接种证">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjzz === '0' ? '有' : '无' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuParentname" align="center" min-width="160" label="父亲/母亲/监护人姓名" />
        <el-table-column prop="stuStationname" align="center" min-width="160" label="所属预防接种单位" />
        <el-table-column align="center" label="乙肝疫苗">
          <el-table-column align="center" label="1" prop="B063InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="2" prop="B064InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="3" prop="B065InspectionShow" min-width="100"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="脊灰疫苗">
          <el-table-column align="center" label="1" prop="B009InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="2" prop="B010InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="3" prop="B011InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="4" prop="B012InspectionShow" min-width="100"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="百白破疫苗">
          <el-table-column align="center" label="1" prop="B015InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="2" prop="B016InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="3" prop="B017InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="4" prop="B018InspectionShow" min-width="100"></el-table-column>
        </el-table-column>
        <el-table-column prop="B037InspectionShow" align="center" label="白破疫苗" min-width="100" />
        <el-table-column align="center" label="麻疹类疫苗">
          <el-table-column align="center" label="1" prop="B059InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="2" prop="B060InspectionShow" min-width="100"></el-table-column>
        </el-table-column>
        <el-table-column align="center" label="流脑疫苗">
          <el-table-column align="center" label="A群流脑疫苗">
            <el-table-column align="center" label="1" prop="B040InspectionShow" min-width="100"></el-table-column>
            <el-table-column align="center" label="2" prop="B041InspectionShow" min-width="100"></el-table-column>
          </el-table-column>
          <el-table-column align="center" label="A+C群流脑疫苗">
            <el-table-column align="center" label="1" prop="B045InspectionShow" min-width="100"></el-table-column>
            <el-table-column align="center" label="2" prop="B046InspectionShow" min-width="100"></el-table-column>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="B069InspectionShow" align="center" label="甲肝疫苗" />
        <el-table-column align="center" label="乙脑疫苗">
          <el-table-column align="center" label="1" prop="B032InspectionShow" min-width="100"></el-table-column>
          <el-table-column align="center" label="2" prop="B033InspectionShow" min-width="100"></el-table-column>
        </el-table-column>
        <el-table-column prop="stuIsjz" align="center" label="是否种全">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjz === '0' ? '是' : '否' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuIsjzFirst" align="center" min-width="100" label="补种是否完成">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjzFirst === '0' && row.stuIsjz === '0' ? '-' : row.stuIsjz==='0'?'是':'否' }}</span>
          </template>
        </el-table-column>
      </ss-table>
    </div>
    <ss-tree-transfer :visible="dialogVisible1" @close="close" v-if="dialogVisible1"></ss-tree-transfer>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'查验结果查询'" :dialogVisible="exportDialogVisible"></export-dialog>
  </div>
</template>
<script>
import { getCheckResultList, printOut, outCheckResultList } from '@/utils/axios/apis/check'
import commonOptions from '@/mixin/commonOptions'
import treeTransfer from './treeTransfer'
import exportDialog from '@/components/common/exportDialog'
export default {
  name: 'vaccinationInspection',
  mixins: [commonOptions],
  components: {
    'ss-tree-transfer': treeTransfer,
    exportDialog
  },
  data () {
    return {
      dataList: [],
      pagenum: 1,
      total: 0,
      exportAreaName: '',
      cModId: this.$store.state.moduleId,
      classesList: [],
      sourceIdList: [{ id: 0, name: '信息系统提供' }, { id: 1, name: 'excel导入' }, { id: 2, name: '校医录入' }],
      checkAll: true,
      exportAll: false,
      exportAllName: false,
      exportAllJzz: false,
      exportAllFull: false,
      exportAllType: false,
      dialogVisible: false,
      showKey: 0,
      form: {
        stuclayear: '',
        stuClalevel: [],
        classId: [],
        // claCode:'',
        claName: '',
        stuName: '', // 受种者姓名
        ifHasjzz: [0, 1], // 是否有接种证
        ifIzFull: [0, 1], // 是否种全
        sourceId: [0, 1, 2], // 数据来源
        lunci:'',
      },
      exportDialogVisible: false,
      dialogVisible1: false
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
        // 学年变化，查找年级
        if (newVal.stuclayear !== oldVal.stuclayear) {
          this.form.stuClalevel = []
          this.$refs.cSelect.selectValue=[]
          // this.form.classId = []
          this.form.claName = ''
          if (newVal.stuclayear && this.form.lunci) {
            await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            // this.form.stuClalevel = this.gradeList[0].claLevel
            // this.form.stuClalevel.push(this.gradeList[0].claLevel)
            // 查找班级
            // await this.findClassNew({ claLevel: newVal.stuClalevel, claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            // this.classChange()
          } else {
            this.gradeList = []
            this.classesList = []
          }
        }
        // 年级发生改变
        if (newVal.stuClalevel.join(",") !== oldVal.stuClalevel.join(",")) {
          this.form.classId = []
          this.$refs.cSelect.selectValue=[]
          this.form.claName = ''
          if (newVal.stuClalevel && newVal.stuClalevel.length>0 && this.form.lunci) {
            // 查找班级
            await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel.join(","), claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            // this.form.classId=this.classesList.length>0?this.classesList.map(item=>item.classId):[]
            // this.classChange()
          } else {
            this.classesList = []
          }
        }
      }
    },
    async "form.lunci"(newVal,oldVal){
      if(newVal==''){
        this.form.stuClalevel = []
        // this.form.classId = []
        this.$refs.cSelect.selectValue=[]
        this.classesList = []
        this.gradeList = []
        return
      }
      if(newVal!=oldVal && newVal!='' && this.form.lunci){
        this.form.stuClalevel = []
        // // this.form.classId = []
        this.$refs.cSelect.selectValue=[]
        // 查找年级
        await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.form.stuclayear,lunci:newVal })
        // this.form.stuClalevel.push(this.gradeList[0].claLevel)
        // 查找班级
        if(this.classesList.length>0){
          await this.findClassNew({ schId: this.$store.state.schId,claLevel: this.form.stuClalevel.join(","), claNameYear: this.form.stuclayear,lunci:newVal })
        }
        // this.form.classId=this.classesList.length>0?this.classesList.map(item=>item.classId):[]
      }
    }
  },
  methods: {
    changeFun (val, item) {
      this.$emit('changeFun', val, item)
    },
    canBeSelect (row) {
      if (row.stuIsjz === '1') {
        return true
      } else {
        return false
      }
    },
    handleSelectionChange (val) {
      this.multipleSelection = val.map(item => item.rowkey)
    },
    classChange () {
      let arr = []
      this.classesList.forEach((item, index) => {
        arr.push(this.classesList[index].classId)
      })
      this.form.classId = arr
    },
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    close () {
      this.dialogVisible1 = false
    },
    // 查询接口
    async search (pagenum) {
      if (!pagenum) {
        pagenum = 1
      }
      this.pagenum = pagenum
      let that = this
      let resiType = that.$refs.checkResiTypePage.resiType
      if(!that.form.stuclayear){
        that.$util.errorMsg('年份至少选择一个')
        return false
      }
      if (that.exportAllName && !that.form.stuName) {
        that.$util.errorMsg('受种者姓名必填')
        return false
      }
      if (that.exportAllJzz && that.form.ifHasjzz.length === 0) {
        that.$util.errorMsg('是否有接种证至少选择一个')
        return false
      }
      if(!that.form.lunci){
        that.$util.errorMsg('轮次必填')
        return false
      }
      if (that.exportAll && that.form.sourceId.length === 0) {
        that.$util.errorMsg('至少选择一个数据来源')
        return false
      }
      if (that.exportAllFull && that.form.ifIzFull.length === 0) {
        that.$util.errorMsg('是否种全至少选择一个')
        return false
      }
      if(that.form.stuClalevel.length==0){
        that.$util.errorMsg('年级至少选择一个')
        return false
      }
      if ((!resiType || resiType.length === 0) && that.exportAllType) {
        that.$util.errorMsg('户籍类型至少选中一个！')
        return false
      }
      if (that.$refs.cSelect.selectValue.length === 0) {
        that.$util.errorMsg('班级名称至少选择一个')
        return false
      }
      let cSelect=that.$refs.cSelect.selectValue
      let params = {
        schId: that.$store.state.schId,
        classCode: cSelect.join(','),
        stuName: that.form.stuName,
        sourceId: that.exportAll ? that.form.sourceId.join(',') : '',
        ifHasjzz: that.exportAllJzz && that.form.ifHasjzz.length < 2 ? that.form.ifHasjzz.join(',') : '',
        ifIzFull: that.exportAllFull ? that.form.ifIzFull.join(',') : '',
        regType: that.exportAllType ? resiType.join(',') : '',
        pageIndex: that.pagenum,
        lunci:that.form.lunci
      }
      that.searchData = params
      that.openFullScreen()
      getCheckResultList(that.searchData).then((res) => {
        this.dataList = res.data.list
        this.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    async reset () {
      this.exportAll = false
      this.exportAllName = false
      this.exportAllJzz = false
      this.exportAllFull = false
      this.exportAllType = false
      this.checkAll = true
      this.$refs.checkResiTypePage.resiType = ['市地内流动', '省际内流动', '国际流动', '县区内流动', '其他', '临时', '常住', '本省内流动']
      this.$refs.checkResiTypePage.checkAll = true
      this.$refs.checkResiTypePage.isIndeterminate = false
      this.$refs.form.resetFields()
      this.dataList = []
      this.total = 0
      this.pagenum = 1
      // 查询学年
      await this.findClaYear()
      if (this.schoolYearList.length > 0) {
        this.form.stuclayear = this.schoolYearList[0].claNameYear
      }
    },
    exportExcelFun () {
      if (!this.searchData) {
        this.$util.errorMsg('查询后才可以导出数据！')
        return false
      }
      if (!this.dataList.length) {
        this.$util.errorMsg('暂无可以导出的数据！')
        return false
      }
      if (this.$refs.cSelect.selectValue.length === 0) {
        this.$util.errorMsg('班级名称至少选择一个')
        return
      }
      // this.exportExcel()
      this.exportDialogVisible = true
    },
    handleExportClose () {
      this.exportDialogVisible = false
    },
    prints () {
      if (!this.multipleSelection || (this.multipleSelection && this.multipleSelection.length==0)) {
        this.$util.errorMsg('请选择要打印的受种者！')
        return false
      }
      let params = {
        stuRowId: this.multipleSelection.join(','),
        schName: this.$store.state.schName
      }
      printOut(params)
    },
    exportExcel (pageNum) {
      let className = []
      this.classesList.map(item => {
        this.$refs.cSelect.selectValue.map(it => {
          if (it === item.classId) {
            className.push(item.claName)
          }
        })
      })
      let sourceIdShow = this.sourceIdList.filter(item => this.form.sourceId.some(im => im === item.id)).map(itm => itm.name)
      let ifHasjzzShow
      this.form.ifHasjzz.forEach(item => {
        if (item === 0) { ifHasjzzShow = '有' }
        if (item === 1) { ifHasjzzShow = '无' }
      })
      if (this.form.ifHasjzz.length === 2) { ifHasjzzShow = '有,无' }
      let ifIzFullShow
      this.form.ifIzFull.forEach(item => {
        if (item === 0) { ifIzFullShow = '是' }
        if (item === 1) { ifIzFullShow = '否' }
      })
      if (this.form.ifIzFull.length === 2) { ifIzFullShow = '是,否' }
      let resiType = this.$refs.checkResiTypePage.resiType
      let cSelect=this.$refs.cSelect.selectValue
      let params = {
        schId: this.$store.state.schId,
        schName: this.$store.state.schName,
        classCode: cSelect.join(','),
        stuName: this.form.stuName,
        sourceId: this.exportAll ? this.form.sourceId.join(',') : '',
        sourceIdShow: this.exportAll ? sourceIdShow.join(',') : '',
        ifHasjzz: this.exportAllJzz ? this.form.ifHasjzz.join(',') : '',
        ifHasjzzShow: this.exportAllJzz ? ifHasjzzShow : '',
        ifIzFull: this.exportAllFull ? this.form.ifIzFull.join(',') : '',
        ifIzFullShow: this.exportAllFull ? ifIzFullShow : '',
        regType: this.exportAllType ? resiType.join(',') : '',
        pageIndex: pageNum,
        classYear: this.form.stuclayear,
        classLevel: this.form.stuClalevel.join(','),
        className: className.join(','),
        lunci:this.form.lunci
      }
      outCheckResultList(params)
    },
    changeSourceId (val, name) {
      if (val && name === '数据来源') this.disabled = false
      if (!val && name === '数据来源') { this.disabled = true; this.form.sourceId = [0, 1, 2]; this.checkAll = true }
      if (!val && name === '数据') this.checkAll = false
      if (!val && name === '受种者姓名') { this.form.stuName = '' }
      if (val && name === '数据' && this.form.sourceId.length === 3) this.checkAll = true
      if (!val && name === '全部') this.form.sourceId = []
      if (val && name === '全部') this.form.sourceId = [0, 1, 2]
      if (!val && name === '户籍类型') { this.$refs.checkResiTypePage.resiType = ['市地内流动', '省际内流动', '国际流动', '县区内流动', '其他', '临时', '常住', '本省内流动']; this.$refs.checkResiTypePage.checkAll = true; this.$refs.checkResiTypePage.isIndeterminate = false }
    }
  },
  async mounted () {
    // 查询学年
    await this.findClaYear()
    if (this.schoolYearList.length > 0) {
      this.form.stuclayear = this.schoolYearList[0].claNameYear
    }
  }
}
</script>
<style>
.checkresult .el-table__header-wrapper tr:last-child th:nth-last-child(2),
.checkresult .el-table__header-wrapper tr:nth-last-child(2) th:nth-last-child(2),
.checkresult .el-table__header-wrapper tr+tr th:nth-last-child(2),
.checkresult .el-table__header-wrapper tr th:not(.is-leaf) {
  border-right: 1px solid #E8EDFF !important;
}

.checkresult .el-table__header-wrapper tr th:last-child,
.checkresult .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: none !important
}
.checkresult .el-table th.is-leaf,.vaccination .el-table thead.is-group th.el-table__cell{
  height: 34px !important;
}
</style>
