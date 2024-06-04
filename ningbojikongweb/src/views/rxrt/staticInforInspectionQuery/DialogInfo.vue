<template>
    <el-dialog
      title="需补种人数"
      :visible.sync="visible"
      width="70%"
      :before-close="close"
      :close-on-click-modal="false"
    >
      <div class="areaInfoDialog" style="overflow-y: auto;overflow-x: hidden;">
        <ss-table :data="dataList" ref="tabPage" class="infoTable" :pageSize="20" @goPage="getDetailInfo" :pagenum="pagenum" :total="total">
          <el-table-column prop="stuNo" align="center" min-width="120" label="受种者编号" />
          <el-table-column prop="stuName" align="center" min-width="120" label="姓名" />
          <el-table-column prop="missVaccines" align="center" min-width="350" label="缺漏疫苗名称+针次" />
        </ss-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
      </span>
    </el-dialog>
  </template>
  <script>
    import { reseedDetail } from '@/utils/axios/apis/vaccinationInspection'
    export default {
      name: 'receptionInfoDialog',
      data () {
        return {
          editorVisible: false,
          height: window.winHeight - 15 / window.scale - 500,
          dataList:[],
          pagenum: 1,
          total: 0,
        }
      },
      props: {
        visible: {
          type: Boolean,
          required: true,
          default: false
        },
        rowkey:{
          type:String,
          default:''
        }
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
        getDetailInfo (pagenum) {
          if (!pagenum) {
            pagenum = 1
          }
          this.pagenum = pagenum
          let checkStartTime = this.$parent.checkDate[0] || ''
          let checkEndTime = this.$parent.checkDate[1] || ''
          this.openFullScreen()
          reseedDetail({ classIds:this.rowkey,pageNum:this.pagenum,schName:this.$parent.schName,checkType:this.$parent.checkType,startDate:checkStartTime,endDate:checkEndTime,})
            .then((res) => {
              if (res && res.data) {
                this.dataList=res.data.list;
                this.total=res.data.total;
              } else {
                this.$util.errorMsg('信息被删除或者不存在！')
              }
              this.closeFullScreen()
            })
            .catch(() => {
              this.closeFullScreen()
            })
        },
        close () {
          this.$emit('close', true)
          // this.$parent.close()
        },
      },
    }
    </script>