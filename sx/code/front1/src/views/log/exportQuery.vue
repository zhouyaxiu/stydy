<template>
  <!-- 导出日志查询 -->
  <div>
    <ss-condition-area-new v-if="!dialogVisible" definedTitle="导出日志查询" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form ref="form" size="medium" onsubmit="return false;" label-position="right" label-width="230px" :inline="true">
        <div class="condition-row">
          <el-form-item prop="exportDate" class="item" style="width:45%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAllDate" @change="val => changeLogType(val, '导出日期')">导出日期:</el-checkbox>
            </div>
            <ss-date-range :disabledFlag="!exportAllDate" clearable startPlaceholder="开始日期" endPlaceholder="结束日期" v-model="exportDate" :startTimeOption="startTimeOption" :endTimeOption="endTimeOption" />
          </el-form-item>
          <el-form-item prop="logType" class="item" style="width:55%">
            <div class="label" slot="label">
              <el-checkbox v-model="exportAll" @change="val => changeLogType(val, '导出类型')">导出类型:</el-checkbox>
            </div>
            <el-checkbox v-model="checkAll" :disabled="!exportAll" @change="val => changeLogType(val, '全部')">全部</el-checkbox>
            <el-checkbox-group v-model="logType" :disabled="!exportAll" style="display:flex;">
              <el-checkbox v-for="item in logTypeList" :label="item.id" :key="item.id" @change="val => changeLogType(val, item.id)">{{item.name}}</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </div>
        <div class="condition-row mt_10" style="margin-top:5px;margin-left:120px;">
          <div class="item">
            <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
            <ss-custom-button @click="reset" :type="$consts.CUSTOMBUTTONTYPES.reset" class="ml_10" />
          </div>
        </div>
      </el-form>
    </ss-condition-area-new>
    <div class="main-content" v-if="!dialogVisible">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="search" :pagenum="pagenum" :total="total">
        <el-table-column prop="logDate" align="center" width="150" label="导出日期" />
        <el-table-column prop="logType" align="center" width="150" label="导出类型" />
        <el-table-column prop="logQuerybean" align="center" label="导出条件" />
        <el-table-column prop="status" align="center" width="150" label="操作">
          <template slot-scope="{ row }"><span @click="openDetailDialog(row.rowkey)" style="cursor:pointer;color:#2E5BFF;">查看</span></template>
        </el-table-column>
      </ss-table>
    </div>
    <!-- 详细信息弹框 -->
    <detail-info-dialog
      :visible="dialogVisible"
      @close="close"
      :rowkey="rowkey"
      v-if="dialogVisible"
    />
  </div>
</template>
<script>
  import { queryExportLog } from '@/utils/axios/apis/log/index'
  import commonOptions from '@/mixin/commonOptions'
  import detailInfoDialog from './exportQueryInfo.vue'
  export default {
    name: 'vaccinationInspection',
    mixins: [commonOptions],
    components: { detailInfoDialog },
    data () {
      return {
        dataList: [],
        logType:[0,1],
        // this.$dateutil.dayAddNum(), this.$dateutil.dayAddNum()
        exportDate:[],//变更日期
        pagenum: 1,
        total: 0,
        logTypeList:[{id:0,name:'受种者查验'},{id:1,name:'查验结果查询'}],
        checkAll:true,
        exportAll:false,
        exportAllDate:false,
        startTimeOption: {
          disabledDate: time => {
            return (
              time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
            )
          }
        },
        endTimeOption: {
          disabledDate: time => {
            return (
              time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
            )
          }
        },
        dialogVisible:false,
        rowkey:'',
      }
    },
    methods: {
      setConditionFlag (falg) {
        this.$refs.tabPage.setHeight()
      },
      changeLogType(val,names){
        if(!val && names=='导出日期'){this.exportDate=[];}
        if(val && names=='导出类型'){this.logType=[0,1];this.disabled=false;this.checkAll=true;}
        if(!val && names=='导出类型'){this.disabled=true;this.logType=[0,1];}
        if(!val && names!='导出类型' && names!='全部' && names!='导出日期'){this.checkAll=false;}
        if(val && names!='导出类型' && names!='全部' && this.logType.length==2){this.checkAll=true;}
        if(!val && names=='全部') this.logType=[];
        if(val && names=='全部') this.logType=[0,1];
      },
      // 查询接口
      search (pagenum) {
        let that = this
        if (!pagenum) {
          pagenum = 1
        }
        this.pagenum = pagenum
        let updateDate = this.exportDate[0] || ''
        let endDate = this.exportDate[1] || ''
        if(that.exportAllDate && !updateDate && !endDate){
          that.$util.errorMsg('导出日期必填')
          return false
        }
        if ((!updateDate && endDate) || (updateDate && !endDate)) {
          this.$util.errorMsg('导出日期请填写完整再查询！')
          return false
        }
        if(that.exportAll && this.logType.length==0){
          that.$util.errorMsg('至少选择一个导出类型')
          return false
        }
        let params = {
          userId:sessionStorage.id,
          logType:this.exportAll?this.logType.join(","):'',
          updateDate:updateDate,
          endDate:endDate,
          pageIndex:this.pagenum,
        }
        that.searchData = params
        that.openFullScreen()
        queryExportLog(that.searchData).then((res) => {
          this.dataList = res.data.list
          this.total = res.data.total
          that.closeFullScreen()
        }).catch(() => {
          that.closeFullScreen()
        })
      },
      reset(){
        this.exportAllDate=false;
        this.exportAll=false;
        this.checkAll=true;
        this.logType=[0,1];
        this.exportDate=[];
        this.dataList=[];
        this.total=0;
        this.pagenum=1;
        this.$refs.form.resetFields();
      },
      openDetailDialog(rowkey){
        this.rowkey=rowkey;
        this.dialogVisible=true;
        // this.$router.push({ path: '/log/exportQueryInfo',query:{rowkey:rowkey} })
      },
      close(){
        this.dialogVisible=false;
      },
    },
  }
</script>