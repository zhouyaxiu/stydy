<template>
  <!-- 特定疫苗接种证查验轮次管理 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row">
        <div class="item">
          <div class="label"><span style="color: red; margin-right: 6px;">*</span>选择地区：</div>
          <ss-area-areaTreeStation :showSelect="false" ref="areaTreePage" class="area" :disabled="msgPubPermission"
            :getToLevelNum="5 - currentLevel" />
        </div>
        <div class="item">
          <div class="label">疫苗：</div>
          <el-select v-model="vaccName" placeholder="请选择疫苗" clearable style="width:200px;" @change="changeData($event,'vaccName')">
            <el-option v-for="item in $store.state.vaccinumList" :key="item.code" :label="item.name" :value="item.code" />
          </el-select>
        </div>
        <div class="item">
          <div class="label">年份：</div>
          <el-select style="width:200px;" slot="fEnterno" v-model="nowYear" placeholder="请选择年份" clearable @change="changeData($event,'nowYear')">
            <el-option style="width:200px;" v-for="item in nowYearList" :key="item" :label="item" :value="item" />
          </el-select>
        </div>
        <div class="item">
          <div class="label">轮次：</div>
          <el-select v-model="roundName" placeholder="请选择轮次" clearable>
            <el-option v-for="item in roundNameList" :key="item" :label="item" :value="item" />
          </el-select>
        </div>
      </div>
      <div class="condition-row mt_10">
        <ss-custom-button @click="search" :type="$consts.CUSTOMBUTTONTYPES.query"/>
        <ss-custom-button @click="add" :type="$consts.CUSTOMBUTTONTYPES.add" class="ml_10" v-if="msgPubPermission" />
      </div>
    </ss-condition-area>
    <div style="color:#999;font-size:14px;">置灰项为已删除项，仅展示。</div>
    <ss-table :data="dataList" ref="tabPage" :changeRowBg="true" class="infoTable" @goPage="search" :pagenum="pagenum" :total="total">
      <el-table-column align="center" min-width="60" label="操作" v-if="msgPubPermission">
        <template slot-scope="{ row }" v-if="row.deleteState=='0'">
          <el-row>
            <el-button @click.native="del(row)" type="text" class="table-btn">删除</el-button>
          </el-row>
        </template>
      </el-table-column>
      <el-table-column prop="roundName" align="center" min-width="100" label="轮次" />
      <el-table-column prop="roundYear" align="center" min-width="50" label="年份" />
      <el-table-column prop="roundVaccName" align="center" min-width="60" label="疫苗" />
      <el-table-column prop="schNames" align="center" min-width="120" label="学校名称" show-overflow-tooltip />
      <el-table-column prop="claLevels" align="center" min-width="140" label="年级" />
      <el-table-column align="center" min-width="130" label="出生日期范围">
        <template slot-scope="{ row }">
          <span v-if="row.startBirth!=''">{{ row.startBirth }} 至 {{ row.endBirth }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" min-width="130" label="截止时间">
        <template slot-scope="{ row }">
          {{ row.startTime }} 至 {{ row.endTime }}
        </template>
      </el-table-column>
    </ss-table>
    <claseqManageDialog :visible="unitManageDialogVisible" @fetchData="search" @closeDialog="close" />
  </div>
</template>
<script>
  import { findRounds,deleteRounds } from '@/utils/axios/apis/claSeq'
  import commonOptions from '@/mixin/commonOptions'
  import { tableHeader } from './tableHeader'
  import claseqManageDialog from './components/claseqManageDialog.vue'
  import { getLevel } from '@/utils/common/area.js'
  export default {
    name: 'vaccinationInspection',
    mixins: [commonOptions],
    components: { claseqManageDialog },
    data () {
      return {
        columnData: tableHeader,
        dataList: [],
        secTime: 0,
        tipText: '统计截止到：' + this.$util.today() + '。',
        total: 0,
        pagenum: 1,
        unitManageDialogVisible:false,
        cModId: this.$store.state.moduleId,
        vaccName:'',//疫苗名称
        nowYear:'',//年份
        roundName:'',//轮次
        msgPubPermission:false,
        currentLevel: Number(getLevel(this.$store.state.userCode)),
      }
    },
    methods: {
      setConditionFlag (falg) {
        this.$refs.tabPage.setHeight()
      },
      // 查询接口
      async search (pagenum) {
        if (!pagenum) {
          pagenum = 1
        }
        this.pagenum = pagenum
        let params = {
          areaCode: this.$refs.areaTreePage.selectAreaCode,
          areaLevel:this.$refs.areaTreePage.selectAreaLevel,
          nowYear:this.nowYear,//查询年份
          vaccName:this.vaccName,//疫苗名称
          roundName:this.roundName.slice(1,-1),//轮次名称
          pageNum:this.pagenum,
          operateType:this.$consts.OPTYPE_QUERY
        }
        this.searchData = params
        this.openFullScreen()
        findRounds(this.searchData).then((res) => {
          for(let i=0;i<res.data.list.length;i++){
            if(res.data.list[i].claLevels!=null){
              let claIds=res.data.list[i].claLevels.split(",").sort((a,b)=>a-b)
              for(let i=0;i<claIds.length;i++){
                if(claIds[i]==0){
                  claIds[i]='幼儿园托班'
                }else if(claIds[i]==1){
                  claIds[i]='幼儿园小班'
                }else if(claIds[i]==2){
                  claIds[i]='幼儿园中班'
                }else if(claIds[i]==3){
                  claIds[i]='幼儿园大班'
                }else if(claIds[i]==4){
                  claIds[i]='一年级'
                }else if(claIds[i]==5){
                  claIds[i]='二年级'
                }else if(claIds[i]==6){
                  claIds[i]='三年级'
                }else if(claIds[i]==7){
                  claIds[i]='四年级'
                }else if(claIds[i]==8){
                  claIds[i]='五年级'
                }else if(claIds[i]==9){
                  claIds[i]='六年级'
                }
              }
              res.data.list[i].claLevels=claIds.join(",")
            }
          }
          this.dataList = res.data.list
          this.total = res.data.total
          this.secTime = res.sec
          this.closeFullScreen()
        }).catch(() => {
          this.closeFullScreen()
        })
      },
      // 添加
      add () {
        this.unitManageDialogVisible = true
      },
      // 删除
      del (row) {
        this.$confirm('是否确定要删除'+row.roundName+'次的查验结果？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          deleteRounds({
            roundId: row.roundId,
            operateType: this.$consts.OPTYPE_DELETE,
            cModId: this.$store.state.moduleId,
          }).then((res) => {
            if (res.msg && res.msg !== '删除成功!') {
              this.$message({
                type: 'error',
                message: res.msg
              })
              return
            }
            this.search()
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
          })
        }).catch(() => {
          this.closeFullScreen()
          this.$message({
            type: 'info',
            message: '已取消'
          })
        }).finally(() => {
          // setTimeout(()=>{
          //   this.search()
          // },500)
        })
      },
      // 关闭弹窗
      close () {
        this.unitManageDialogVisible = false
      },
      changeData(val,name){
        // if(name=='vaccName') this.nowYear=''
        this.roundName=''
        this.getRound()
        // if(this.nowYear || this.vaccName){
        //   this.getRound()
        // }else{
        //   // if(name=='vaccName')  this.nowYearList=[]
        //   // this.roundNameList=[]
        // }
      },
      getRound(){
        let params = {
          areaCode: this.$refs.areaTreePage.selectAreaCode,
          areaLevel:this.$refs.areaTreePage.selectAreaLevel,
          nowYear:this.nowYear,//查询年份
          vaccName:this.vaccName,//疫苗名称
          roundName:this.roundName,//轮次名称
        }
        this.getRoundName(params)
      },
      getYear(){
        let params = {
          areaCode: this.$refs.areaTreePage.selectAreaCode,
          areaLevel:this.$refs.areaTreePage.selectAreaLevel,
          nowYear:this.nowYear,//查询年份
          vaccName:this.vaccName,//疫苗名称
          roundName:this.roundName,//轮次名称
        }
        this.getRoundYear(params)
      }
    },
    mounted(){
      this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
      this.getYear()
      this.getRound()
    }
  }
  </script>