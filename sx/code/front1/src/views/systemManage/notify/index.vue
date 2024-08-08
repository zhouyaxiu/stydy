<template>
  <!-- 消息管理 -->
  <div>
    <ss-condition-area-new ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row" style="padding-left:150px">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query" />
      </div>
    </ss-condition-area-new>
    <!-- table -->
    <div class="main-content">
      <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="search" :pagenum="pagenum" :total="total">
        <el-table-column prop="status" align="center" min-width="300" label="是否已读" show-overflow-tooltip>
          <template slot-scope="{ row }"><span @click="openDetailDialog(row.notifyId);" style="cursor:pointer;font-weight: bold;" :style="{color:row.status=='已读'?'#2f6cc9':'red'}">{{ row.status }}</span></template>
        </el-table-column>
        <el-table-column prop="notifyTime" align="center" min-width="200" label="消息日期" />
        <el-table-column prop="notifyTitle" align="center" min-width="200" label="消息标题" />
      </ss-table>
    </div>
    <!-- 详细信息弹框 -->
    <detail-info-dialog
      :visible="dialogVisible"
      @close="close"
      :notifyInfo="notifyInfo"
      v-if="dialogVisible"
    />
  </div>
</template>
<script>
  import { queryNotifyList,getNotifyDetail } from '@/utils/axios/apis/notify/index'
  import commonOptions from '@/mixin/commonOptions'
  import detailInfoDialog from './notifyDialog.vue'
  export default {
    name: 'notify',
    mixins: [commonOptions],
    components: { detailInfoDialog },
    data () {
      return {
        pagenum: 1,
        total: 0,
        dataList: [],
        notifyInfo:{},
        dialogVisible:false
      }
    },
    methods: {
      setConditionFlag (falg) {
        this.$refs.tabPage.setHeight()
      },
      search (pagenum) {
        if (!pagenum) {
          pagenum = 1
        }
        this.pagenum = pagenum
        let params = {
          schId: this.$store.state.schId,
          pageIndex: pagenum
        }
        this.openFullScreen()
        queryNotifyList(params).then((res) => {
          if (res.msg) {
            this.$util.errorMsg(res.msg)
          } else {
            this.dataList = res.data.list
            this.total = res.data.total
          }
          this.closeFullScreen()
        })
        .catch(() => {
          this.closeFullScreen()
        })
      },
      openDetailDialog(notifyId){
        // this.notifyId=notifyId;
        this.$nextTick(()=>{
          // this.dialogVisible=true;
          if (!notifyId || notifyId === '') return false
          this.openFullScreen()
          getNotifyDetail({ notifyId:notifyId,schId:this.$store.state.schId })
            .then((res) => {
              if (res && res.data) {
                this.notifyInfo = res.data
                this.dialogVisible=true;
              } else {
                this.$util.errorMsg('消息被删除或者不存在！')
              }
              this.closeFullScreen()
            })
            .catch(() => {
              this.closeFullScreen()
            })
        })
      },
      close(){
        this.dialogVisible=false;
        this.search()
      },
    },
    mounted(){
      this.search()
    }
  }
</script>
<style>
  .infoTable .el-table__header-wrapper tr th:nth-last-child(2) {
    border-right: 0 !important;
  }
  </style>
  <style scoped>
  .mt_10 {
    margin-top: 10px;
  }
</style>
  