<template>
  <!-- 需补种名单列表 -->
  <div class="batch">
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag" :titleLong="true">
      <ss-class-select ref="selectClass" :isBindGrade="true" :classMultiple="true" :defaultValue="true" :formValue="resetForm"
        :defaultLunci="true" :classId="true" :classSelect="true" :classIdRequired="false">
      </ss-class-select>
      <div class="button-set">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <!-- <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" /> -->
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- <ss-custom-button v-print="printObj" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" /> -->
        <button ref="printBtn" style="display:none" v-print="printObj" class="ml_10">打印</button>
        <ss-custom-button @click="prints" :type="$consts.CUSTOMBUTTONTYPES.print" class="ml_10" />
      </div>
    </ss-condition-area>
    <div class="main-content">
      <ss-table style="width: 100%" :data="dataList" ref="tabPage" class="vaccination" @goPage="search" :pageSize="20"
        :pagenum="pagenum" :total="total">
        <el-table-column type="index" align="center" width="80" label="序号" />
        <!-- <el-table-column prop="schName" align="center" min-width="100" label="学校" /> -->
        <el-table-column prop="gradeName" align="center" min-width="100" label="年级" />
        <el-table-column prop="clazzName" align="center" min-width="80" label="班级" />
        <el-table-column prop="stuName" align="center" min-width="100" label="儿童姓名" />
        <el-table-column prop="stuBirth" align="center" min-width="120" label="出生日期" />
        <el-table-column prop="needReplantRoster" align="center" min-width="150" label="补种疫苗名称" showOverflowTooltip />
        <el-table-column prop="stuPhone" align="center" min-width="120" label="手机号码" >
          <template slot-scope="{ row }">
            {{ $util.encryptPhone(row.stuPhone,false) }}
          </template>
        </el-table-column>
      </ss-table>
    </div>
    <div v-show="printShow">
      <!-- 打印表格 -->
      <vaccination-table :columnList="columnList" :dataList="dataList" id="printArea">
      </vaccination-table>
    </div>
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'需补种名单列表'"
      :dialogVisible="exportDialogVisible"></export-dialog>

  </div>
</template>
<script>
import { queryReplantRoster, outputReplantRoster } from '@/utils/axios/apis/roster'
import commonOptions from '@/mixin/commonOptions'
import vaccinationTable from './components/vaccinationTable.vue'
import exportDialog from '@/components/common/exportDialog'
import { tableHeader } from './tableHeader'
export default {
  name: 'vaccinationInspection',
  mixins: [commonOptions],
  components: { vaccinationTable, exportDialog },
  data () {
    return {
      columnData: tableHeader,
      dataList: [],
      unitManageForm: {
        stuclayear: '',
        stuClalevel: '',
        classId: '',
        lunci: ''
      },
      resetForm: {},
      secTime: 0,
      tipText: '统计截止到：' + this.$util.dayAdd(this.$util.today(), -1) + '。',
      exportAreaName: '',
      pagesize: 20,
      total: 0,
      pagenum: 1,
      gradeList: this.$util.getGrade(2),
      printObj: {
        id: 'printArea',
        popTitle: '打印', // 打印配置页上方标题
        extraHead: '', // 最上方的头部文字，附加在head标签上的额外标签,使用逗号分隔
        preview: '', // 是否启动预览模式，默认是false（开启预览模式，可以先预览后打印）
        previewTitle: '', // 打印预览的标题（开启预览模式后出现）,
        previewPrintBtnLabel: '', // 打印预览的标题的下方按钮文本，点击可进入打印（开启预览模式后出现）
        zIndex: '', // 预览的窗口的z-index，默认是 20002（此值要高一些，这涉及到预览模式是否显示在最上面）
        previewBeforeOpenCallback () { }, // 预览窗口打开之前的callback（开启预览模式调用）
        previewOpenCallback () { }, // 预览窗口打开之后的callback（开启预览模式调用）
        beforeOpenCallback () {

        }, // 开启打印前的回调事件
        openCallback () {
          this.isPrint = true
        }, // 调用打印之后的回调事件
        closeCallback () { }, // 关闭打印的回调事件（无法确定点击的是确认还是取消）
        url: '',
        standard: '',
        extraCss: '',
        scanStyle: false
      },
      printShow: false,
      columnList: [
        {
          type: 'index',
          width: 60,
          align: 'center'
        }, {
          title: '学校',
          key: 'schName'
        },
        {
          title: '年级',
          key: 'gradeName'
        },
        {
          title: '班级',
          key: 'clazzName'
        },
        {
          title: '儿童姓名',
          key: 'stuName'
        },
        {
          title: '出生日期',
          key: 'stuBirth'
        },
        {
          title: '补充疫苗名称',
          key: 'needReplantRoster'
        },
        {
          title: '手机号码',
          key: 'stuPhone'
        }],
      exportDialogVisible: false
    }
  },
  methods: {

    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },

    // 重置
    async reset () {
      await this.findClaYear()
      this.$refs.selectClass.unitManageForm.stuclayear = this.$dateutil.currentYear()
      this.$refs.selectClass.unitManageForm.lunci = ''
      this.$refs.selectClass.unitManageForm.stuClalevel = ''
      this.dataList = []
    },
    // 查询接口
    search (pagenum) {
      let that = this
      that.unitManageForm = that.$refs.selectClass.unitManageForm
      if (!this.unitManageForm.stuclayear) {
        this.$util.errorMsg('请选择学年！')
        return
      }
      if (!this.unitManageForm.lunci) {
        this.$util.errorMsg('请选择轮次！')
        return
      }
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let params = {
        schId: this.$store.state.schId,
        year: that.unitManageForm.stuclayear,
        grade: that.unitManageForm.stuClalevel,
        operateType: that.$consts.OPTYPE_QUERY,
        rounds: that.unitManageForm.lunci,
        pageNum: that.pagenum,
        pageSize: that.pagesize,
        className: that.unitManageForm.classId
      }
      that.searchData = params
      that.openFullScreen()
      queryReplantRoster(params).then((res) => {
        this.dataList = res.data.list
        that.dataList = that.dataList.map(item => {
          return {
            ...item,
            gradeName: this.gradeList.filter(sub => sub.code == item.grade)[0].name
          }
        })
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
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
      this.exportDialogVisible = true
    },
    exportExcel (pageNum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.operateType = this.$consts.OPTYPE_OUTPUT
      params.pageNum = pageNum
      console.log('导出参数', params)
      outputReplantRoster(params)
    },
    prints () {
      if (this.dataList.length == 0) {
        this.$util.errorMsg('必须先点击查询！')
        return
      }
      this.$refs.printBtn.click()
    }
  }
}
</script>
