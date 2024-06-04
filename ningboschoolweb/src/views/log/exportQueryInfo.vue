<template>
  <!-- 导出日志明细 -->
  <div>
    <ss-condition-area-new definedTitle="导出日志明细" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="areaInfoDialog" style="margin-top:40px;margin-left:-56px;margin-right:24px;">
        <div :style="{ 'max-height': height + 'px' }" style="overflow:hidden;">
          <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="getDetailInfo" :pagenum="pagenum" :total="total" :maxHeight="height - 150">
            <el-table-column prop="logClaname" align="center" min-width="200" label="受种者班级" />
            <el-table-column prop="logStuimuno" align="center" min-width="200" label="受种者信息介质识别码" />
            <el-table-column prop="logStuname" align="center" min-width="200" label="受种者姓名" />
            <el-table-column prop="logStubirth" align="center" min-width="200" label="出生日期" />
          </ss-table>
        </div>
        <div class="dialog-footer center" style="text-align:center;margin:40px;">
          <el-button @click="close" type="primary">返回</el-button>
        </div>
      </div>
    </ss-condition-area-new>
  </div>
</template>
<script>
  import { queryExportDetail } from '@/utils/axios/apis/log/index'
  export default {
    name: 'receptionInfoDialog',
    data () {
      return {
        dataList: [],
        pagenum: 1,
        total: 0,
        height: window.winHeight - 15 / window.scale-200,
      }
    },
    props: {
      rowkey:{
        type:String,
        default:''
      },
      visible: {
        type: Boolean,
        required: true,
        default: false
      },
    },
    watch: {
      rowkey: {
        immediate: true,
        handler (val) {
          if (val) this.getDetailInfo()
        }
      }
    },
    methods: {
      setConditionFlag () {
        this.$refs.tabPage.setHeight()
      },
      getDetailInfo (pagenum) {
        if (!this.rowkey || this.rowkey === '') return false
        if (!pagenum) {
          pagenum = 1
        }
        this.pagenum = pagenum
        this.openFullScreen()
        queryExportDetail({ rowkey:this.rowkey,pageIndex:this.pagenum })
          .then((res) => {
            if (res && res.data) {
              this.dataList = res.data.list
              this.total = res.data.total
            } else {
              this.$util.errorMsg('信息被删除或者不存在！')
            }
            this.closeFullScreen()
          })
          .catch(() => {
            this.closeFullScreen()
          })
      },
      close(){
        this.$emit('close', true)
        // this.$router.push({ path: '/log/exportQuery' })
      }
    },
    mounted () {
      // this.getDetailInfo()
    }
  }
</script>