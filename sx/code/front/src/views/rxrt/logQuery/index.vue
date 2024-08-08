<template>
  <!-- 日志查看 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label"><span class="red">*</span>选择地区：</div>
          <ss-area-areaTreeStation :disabled="$store.state.modelObject==1 ?true:false" :showSelect="false" ref="areaTreePage" :getToLevelNum="5 - currentLevel" />
        </div>
        <div class="item mLeft">
          <div class="label"><span class="red">*</span>日志时间：</div>
          <ss-date-range v-model="fillinDate" :startTimeOption="startTimeOption" :endTimeOption="endTimeOption" />
        </div>
        <div class="item mLeft">
          <div class="label">对象选择：</div>
          <el-select v-model="selection" placeholder="请选择" clearable>
            <el-option label="全部" value="" />
            <el-option label="疾控" value="0" v-if="!$store.state.modelObject==1" />
            <el-option label="接种门诊" value="1" />
            <el-option label="学校" value="2" />
          </el-select>
        </div>
      </div>
      <div class="condition-row">
        <div class="item ">
          <div class="label"><span class="red">*</span>操作类型：</div>
          <el-checkbox-group v-model="type" class="flex">
            <el-checkbox :label="0">登录</el-checkbox>
            <el-checkbox :label="1">添加</el-checkbox>
            <el-checkbox :label="2">修改</el-checkbox>
            <el-checkbox :label="4">查询</el-checkbox>
            <!-- <el-checkbox :label="5">打印</el-checkbox> -->
            <el-checkbox :label="6">导出</el-checkbox>
            <el-checkbox :label="3">删除</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
      <div class="condition-row mt_10">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" class="ml_10" />
        <!-- <ss-explain-tip-text :secTime="secTime" /> -->
      </div>
    </ss-condition-area>
    <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="$store.state.pagesize" @goPage="search" :pagenum="pagenum" :total="total">
      <el-table-column type="index" align="center" width="80" label="序号" />
      <el-table-column prop="operaType" align="center" min-width="80" label="操作类型" />
      <el-table-column prop="modelName" align="center" min-width="200" label="模块名称" />
      <el-table-column prop="loginName" align="center" min-width="80" label="登录名" />
      <el-table-column prop="accountName" align="center" min-width="80" label="账户" />
      <el-table-column prop="logTime" align="center" min-width="150" label="记录时间" />
    </ss-table>
  </div>
</template>
<script>
import { queryLog } from '@/utils/axios/apis/rxrt/logQuery'
import commonOptions from '@/mixin/commonOptions'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'notify',
  mixins: [commonOptions],
  data () {
    return {
      fillinDate: [this.$dateutil.dayAddNum(30), this.$dateutil.dayAddNum()],
      secTime: 0,
      pagenum: 1,
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
      total: 0,
      dataList: [],
      type:[],//操作类型
      selection:'',//对象选择
      currentLevel: Number(getLevel(this.$store.state.userCode)),
    }
  },
  methods: {
    setConditionFlag (falg) {
      this.$refs.tabPage.setHeight()
    },
    search (pagenum) {
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let fillinStartDate = that.fillinDate[0] || ''
      let fillinEndDate = that.fillinDate[1] || ''
      if (!that.$refs.areaTreePage.selectAreaCode || that.$refs.areaTreePage.selectAreaCode.length === 0) {
        that.$util.errorMsg('必须选择地区！')
        return
      }
      if (!fillinStartDate || fillinStartDate === '' || !fillinEndDate || fillinEndDate === '') {
        that.$util.errorMsg('日志时间为必填项，请填写完整再查询！')
        return false
      }
      if(this.type.length==0){
        that.$util.errorMsg('操作类型至少选择一个！')
        return false
      }
      let params = {
        opertimeStrat: fillinStartDate,
        opertimeEnd: fillinEndDate,
        operaType: that.type.join(","),//0 ：登录  1 新增 2 修改 3 删除 4 查询 5 打印6 导出
        operaObject: that.selection,//操作对象  0 疾控端  1门诊  2 学校
        pageIndex: pagenum,
        operateType:this.$consts.OPTYPE_QUERY, //  操作类型  0-6
        // areaCodeSelect:JSON.parse(sessionStorage.getItem('response')).userinfo.areaCode,
        // areaLevelSelect:JSON.parse(sessionStorage.getItem('response')).arealevel
      }
      that.searchData = JSON.parse(JSON.stringify(params))
      that.openFullScreen()
      queryLog(params).then((res) => {
        that.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
  }
}
</script>
<style>
.infoTable .el-table__header-wrapper tr th:nth-last-child(2) {
  border-right: 0 !important;
}

.table-btn {
  display: inline !important;
}
</style>
<style scoped>
.ml_10 {
  margin-left: 10px;
}
.mt_10 {
  margin-top: 10px;
}
</style>
