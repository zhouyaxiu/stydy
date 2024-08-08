<template>
  <!-- 受种者查验 -->
  <div>
    <ss-condition-area-new definedTitle="受种者查验" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form ref="form" :model="form" size="medium" onsubmit="return false;" label-position="right" label-width="200px" :inline="true">
        <div class="condition-row">
          <el-form-item prop="stuclayear" class="item">
            <div class="label" slot="label"><span class="red">*</span>年份：</div>
            <el-select clearable v-model="form.stuclayear" placeholder="请选择">
              <el-option v-for="item in schoolYearList" :key="item.claNameYear" :label="item.claNameYear" :value="item.claNameYear"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="stuName" class="item" style="width:25%;margin-right: 4%;">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllName" @change="val => changeSourceId(val, '受种者姓名')">受种者姓名：</el-checkbox>
            </div>
            <el-input clearable v-model="form.stuName" :disabled="!exportAllName"></el-input>
          </el-form-item>
          <el-form-item prop="sourceId" class="item">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllData" @change="val => changeSourceId(val, '数据来源')">数据来源：</el-checkbox>
            </div>
            <el-checkbox v-model="checkAll" :disabled="!exportAllData" @change="val => changeSourceId(val, '全部')">全部</el-checkbox>
            <el-checkbox-group v-model="form.sourceId" :disabled="!exportAllData" class="flex">
              <el-checkbox v-for="item in sourceIdList" :label="item.id" :key="item.id" @change="val => changeSourceId(val, '数据')">{{item.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="lunci" class="item">
            <div class="label" slot="label"><span class="red">*</span>轮次：</div>
            <el-select v-model="form.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item prop="stuImuno" class="item" style="width:63.45%;">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllType" @change="val => changeSourceId(val, '户籍类型')">户籍类型：</el-checkbox>
            </div>
            <ss-check-resiType ref="checkResiTypePage" :disabled="!exportAllType" />
          </el-form-item>
        </div>
        <div class="condition-row">
          <el-form-item prop="stuClalevel" class="item">
            <div class="label" slot="label"><span class="red">*</span>年级：</div>
            <el-select clearable v-model="form.stuClalevel" placeholder="请选择" multiple collapse-tags>
              <el-option v-for="item in gradeList" :key="item.claLevel" :label="item.claLevelCN" :value="item.claLevel"/>
            </el-select>
          </el-form-item>
          <el-form-item prop="stuClacode" class="item" style="width:25%;margin-right: 4%;">
            <div class="label" slot="label" style="padding-right:11px;"><span class="red">*</span>班级名称：</div>
            <ss-custom-select ref="cSelect" :selectList="classesList" :selectType="3" :dataInfo="{clearable:true,value:'',name:'',optsLabel:'',optsValue:''}" @callback="changeFun"></ss-custom-select>
            <!-- <el-select clearable v-model="form.classId" placeholder="请选择" multiple collapse-tags>
              <el-option v-for="item in classesList" :key="item.classId" :label="item.claFullName.substr(4)" :value="item.classId"/>
            </el-select> -->
          </el-form-item>
          <div class="item">
            <div slot="label" style="min-width:80px;"></div>
            <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
            <ss-custom-button @click="resetHandler" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" />
            <ss-custom-button @click="deleteHandler" :type="$consts.CUSTOMBUTTONTYPES.delete" class="ml_10" />
            <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
          </div>
        </div>
      </el-form>
    </ss-condition-area-new>
    <div class="content-area" style="position:relative;">
      <ss-table :data="dataList" @handleSelectionChange="handleSelectionChange" ref="tabPage" :changeRowBg="true" :rowBgList="tipList" :changeRowAttr="'stuSourceid'" class="infoTable" :pageSize="20" @goPage="search" :pagenum="pagenum" :total="total">
        <el-table-column
        type="selection"
        width="55"
        align="center">
      </el-table-column>
        <el-table-column prop="stuNo" align="center" min-width="180" label="受种者预防接种档案编号" />
        <el-table-column prop="stuImuno" align="center" min-width="180" label="受种者信息介质识别码" />
        <el-table-column prop="stuName" align="center" min-width="120" label="受种者姓名">
          <template slot-scope="{ row }"><span @click="openDetailDialogInfo(row.rowkey)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;margin-right:10px;">{{row.stuName}}</span></template>
        </el-table-column>
        <el-table-column prop="stuSex" align="center" label="性别" />
        <el-table-column prop="stuBirth" align="center" label="出生日期" min-width="100" />
        <el-table-column prop="stuType" align="center" label="户籍类型" min-width="130" />
        <el-table-column prop="stuSeq" align="center" label="轮次">
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
        <el-table-column prop="remark" align="center" label="备注" />
        <el-table-column align="center" label="操作" v-if="isshowbtn">
          <template slot-scope="{ row }">
            <span v-if="row.shoolstatus!=='1'" @click="openDetailDialog(row.rowkey)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;margin-right:10px;">修改</span>
            <span @click="deleteHandler(row.rowkey)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;">删除</span></template>
        </el-table-column>
      </ss-table>
      <div class="tags" style="position: absolute;bottom: 30px;" v-if="dataList.length>0">
        <div><span></span>校医录入</div>
        <div><span></span>excel导入</div>
        <div><span></span>信息系统提供</div>
      </div>
    </div>
    <!-- 详细信息弹框 -->
    <detail-dialog-info
      :visible="dialogVisible1"
      :stuId="rowkey"
      @close="close"
    />
    <!-- 详细信息修改弹框 -->
    <detail-alter-dialog
      :visible="dialogVisible"
      :stuId="rowkey"
      @close="close"
    />
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'受种者查验'" :dialogVisible="exportDialogVisible"></export-dialog>
  </div>
</template>
<script>
import { inspectionList, deleteStudent, output } from '@/utils/axios/apis/check'
import commonOptions from '@/mixin/commonOptions'
import detailAlterDialog from './alterDialog.vue'
import detailDialogInfo from './DialogInfo.vue'
import exportDialog from '@/components/common/exportDialog'
export default {
  name: 'vaccinationInspection',
  mixins: [commonOptions],
  components: { detailAlterDialog, detailDialogInfo, exportDialog },
  data () {
    return {
      type: '6',
      dataList: [],
      classesList: [],
      exportAreaName: '',
      cModId: this.$store.state.moduleId,
      pagenum: 1,
      total: 0,
      sourceIdList: [{ id: 0, name: '信息系统提供' }, { id: 1, name: 'excel导入' }, { id: 2, name: '校医录入' }],
      checkAll: true,
      exportAll: false,
      exportAllName: false,
      exportAllData: true,
      exportAllType: false,
      dialogVisible: false,
      dialogVisible1: false,
      form: {
        stuclayear: '',
        stuClalevel: [],
        classId: [],
        stuImuno: '', // 受种者介质码
        stuName: '', // 受种者姓名
        sourceId: [0, 1, 2], // 数据来源
        lunci: sessionStorage.getItem("defaultLunci"),
        defautltlunci:sessionStorage.getItem("defaultLunci")
      },
      rowkey: '',
      tipList: [
        {
          text: '校医录入',
          style: { background: '#E8FFE8' },
          stuSourceid: '2',
          className: 'row-white'
        },
        {
          text: 'excel导入',
          style: { background: '#E9FFFF' },
          stuSourceid: '1',
          className: 'row-green'
        },
        {
          text: '信息系统提供',
          style: { background: '#FFFDD9' },
          stuSourceid: '0',
          className: 'row-yellow'
        }
      ],
      exportDialogVisible: false,
      isshowbtn:false
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
            // this.form.stuClalevel.push(this.gradeList[0].claLevel)
            // 查找班级
            // await this.findClassNew({ schId: this.$store.state.schId,claLevel: newVal.stuClalevel.join(","), claNameYear: newVal.stuclayear,lunci:this.form.lunci })
            // if(this.classesList.length>0){
            //   this.form.classId=this.classesList.map(item=>item.classId)
            // }
            // this.classChange()
          } else {
            this.gradeList = []
            this.classesList = []
          }
        }
        // 年级发生改变
        if (newVal.stuClalevel.join(",") !== oldVal.stuClalevel.join(",")) {
          // this.form.classId = []
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
        // this.form.classId = []
        this.$refs.cSelect.selectValue=[]
        // 查找年级
        await this.findClaLevelNew({ schId: this.$store.state.schId,claNameYear: this.form.stuclayear,lunci:newVal })
        // this.form.stuClalevel.push(this.gradeList[0].claLevel)
        // 查找班级
        // if(this.gradeList.length>0){
        //   await this.findClassNew({ schId: this.$store.state.schId,claLevel: this.form.stuClalevel.join(","), claNameYear: this.form.stuclayear,lunci:newVal })
        // }
        // this.form.classId=this.classesList.length>0?this.classesList.map(item=>item.classId):[]
      }
    }
  },
  methods: {
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
    // 查询接口
    search (pagenum) {
      if (!pagenum) {
        pagenum = 1
      }
      this.pagenum = pagenum
      let that = this
      let resiType = that.$refs.checkResiTypePage.resiType
      if(!that.form.stuclayear){
        that.$util.errorMsg('年份必填')
        return false
      }
      if (that.exportAllName && !that.form.stuName) {
        that.$util.errorMsg('受种者姓名必填')
        return false
      }
      if (that.exportAllData && that.form.sourceId.length === 0) {
        that.$util.errorMsg('至少选择一个数据来源')
        return false
      }
      if(!that.form.lunci){
        that.$util.errorMsg('轮次必填')
        return false
      }
      if ((!resiType || resiType.length === 0) && that.exportAllType) {
        that.$util.errorMsg('户籍类型至少选中一个！')
        return false
      }
      if (that.form.stuClalevel.length === 0) {
        that.$util.errorMsg('年级至少选择一个')
        return false
      }
      if (that.$refs.cSelect.selectValue.length === 0) {
        that.$util.errorMsg('班级名称至少选择一个')
        return false
      }
      let params1 = {
        schId: that.$store.state.schId,
        classCodes: that.$refs.cSelect.selectValue.join(','),
        stuName: that.form.stuName,
        sourcesIds: that.exportAllData ? that.form.sourceId.join(',') : '',
        stuTypes: that.exportAllType ? resiType.join(',') : '',
        pageNum: that.pagenum,
        lunci: that.form.lunci,
      }
      that.searchData = params1
      that.openFullScreen()
      inspectionList(that.searchData).then((res) => {
        this.dataList = res.data.list
        this.total = res.data.total
        that.isshowbtn=this.dataList.length!=0 && this.$dateutil.currentYear()==this.form.stuclayear && this.form.lunci==this.form.defautltlunci
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    // 重置
    async resetHandler () {
      this.checkAll = true
      this.exportAll = false
      this.exportAllName = false
      this.exportAllType = false
      this.exportAllData = true
      this.sourceId = [0, 1, 2]
      this.$refs.checkResiTypePage.resiType = ['市地内流动', '省际内流动', '国际流动', '县区内流动', '其他', '临时', '常住', '本省内流动']
      this.$refs.checkResiTypePage.checkAll = true
      this.$refs.checkResiTypePage.isIndeterminate = false
      this.dataList = []
      this.total = 0
      this.pagenum = 1
      if (this.$refs.form && this.$refs.form.resetFields) {
        this.$refs.form.resetFields()
      }
      // 查询学年
      await this.findClaYear()
      if (this.schoolYearList.length > 0) {
        this.form.stuclayear = this.schoolYearList[0].claNameYear
      }
    },
    // 删除
    deleteHandler (rowkey) {
      if(this.$dateutil.currentYear()!=this.form.stuclayear || this.form.lunci!=this.form.defautltlunci){
        this.$util.errorMsg('只有当前年份和当前轮次的数据可以进行删除操作！')
        return false
      }
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.$confirm('您确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteStudent({ stuId: rowkey || this.multipleSelection.join(','), fullcode: sessionStorage.id }).then((res) => {
          this.$message({
            type: 'success',
            message: res.message
          })
          this.search()
          this.closeFullScreen()
        }).catch(() => {
          this.closeFullScreen()
        })
      }).catch(() => {
      })
    },
    handleExportClose () {
      this.exportDialogVisible = false
    },
    // 导出
    exportExcelFun () {
      if (!this.searchData) {
        this.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (this.dataList.length === 0) {
        this.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      if (this.$refs.cSelect.selectValue.length === 0) {
        this.$util.errorMsg('班级名称至少选择一个')
        return
      }
      this.exportDialogVisible = true
    },
    exportExcel (pageNum) {
      let resiType = this.$refs.checkResiTypePage.resiType
      let className = []
      this.classesList.map(item => {
        this.$refs.cSelect.selectValue.map(it => {
          if (it === item.classId) {
            className.push(item.claName)
          }
        })
      })
      let params = {
        schId: this.$store.state.schId,
        classCodes: this.$refs.cSelect.selectValue.join(','),
        stuName: this.form.stuName,
        sourcesIds: this.exportAllData ? this.form.sourceId.join(',') : '',
        stuTypes: this.exportAllType ? resiType.join(',') : '',
        pageNum: pageNum,
        claNameYear: this.form.stuclayear,
        claLevel: this.form.stuClalevel.join(','),
        claShortName: className.join(','),
        lunci: this.form.lunci,
      }
      output(params)
    },
    changeSourceId (val, name) {
      if (val && name === '数据来源') { this.disabled = false }
      if (!val && name === '数据来源') { this.disabled = true; this.form.sourceId = [0, 1, 2]; this.checkAll = true }
      if (!val && name === '受种者姓名') { this.form.stuName = '' }
      if (!val && name === '数据') this.checkAll = false
      if (val && name === '数据' && this.form.sourceId.length === 3) this.checkAll = true
      if (!val && name === '全部') this.form.sourceId = []
      if (val && name === '全部') this.form.sourceId = [0, 1, 2]
      if (!val && name === '户籍类型') { this.$refs.checkResiTypePage.resiType = ['市地内流动', '省际内流动', '国际流动', '县区内流动', '其他', '临时', '常住', '本省内流动']; this.$refs.checkResiTypePage.checkAll = true; this.$refs.checkResiTypePage.isIndeterminate = false }
    },
    openDetailDialog (rowkey) {
      this.rowkey = rowkey
      this.$nextTick(() => {
        this.dialogVisible = true
      })
    },
    openDetailDialogInfo (rowkey) {
      this.rowkey = rowkey
      this.$nextTick(() => {
        this.dialogVisible1 = true
      })
    },
    close () {
      this.dialogVisible = false
      this.dialogVisible1 = false
      this.rowkey = ''
      this.search(this.pagenum)
    },
    changeFun (val, item) {
      this.$emit('changeFun', val, item)
    },
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
<style scoped>
  .tags{position: absolute;bottom: 30px;display: flex;}
  .tags div{margin-right: 20px;}
  .tags span{width:14px;height:14px;background: #E8FFE8;display: inline-block;margin-right:4px;vertical-align: middle;}
  .tags div:nth-child(2) span{background: #E9FFFF;}
  .tags div:last-child span{background: #FFFDD9;}
</style>
