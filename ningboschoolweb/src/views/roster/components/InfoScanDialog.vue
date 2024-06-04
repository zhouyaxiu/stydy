<template>
  <el-dialog
    title="信息扫码选择"
    :visible.sync="visible"
    width="70%"
    :before-close="close"
    :lock-scroll="true"
    :append-to-body="true"
  >
    <div class="areaInfoDialog" style="overflow-y: auto;overflow-x: hidden;max-height:500px">
      <div style="font-size:16px;color:#999;margin-bottom:10px;">置灰数据处于踢卡状态不可选择</div>
      <ss-table :data="dataList" ref="tabPage" class="infoTable" :changeRowBg="true">
        <el-table-column prop="stuNo" align="center" min-width="180" label="儿童编码" />
        <el-table-column prop="stuName" align="center" min-width="120" label="姓名" />
        <el-table-column prop="stuBirth" align="center" min-width="160" label="出生日期" />
        <el-table-column prop="stuParentname" align="center" min-width="160" label="父亲/母亲/监护人姓名" />
        <el-table-column prop="stuSex" align="center" min-width="80" label="性别" />
        <el-table-column prop="stuType" align="center" min-width="100" label="户籍类型" />
        <el-table-column align="center" label="操作">
          <template slot-scope="{ row }" v-if="row.fState!='9'">
            <span @click="selectDialog(row)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;margin-right:10px;">选择</span>
          </template>
        </el-table-column>
      </ss-table>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
    </span>
  </el-dialog>
</template>
<script>
  import CustomDescriptions from "@/components/common/customDescriptions.vue"
  // import { savePlStuNo,okSave } from '@/utils/axios/apis/rxrt/showInforInspectionResultQuery'
  // import mselectDialog from "./mselectDialog.vue"
  export default {
    name: 'receptionInfoDialog',
    components: { CustomDescriptions },
    data () {
      return {
        height: window.winHeight - 15 / window.scale - 500,
        dialogTableVisible:false,
        gridData:[],
        searchData:{},
        dialogVisible1:false
      }
    },
    props: {
      dataList: {
        type: Array,
        default: function(){return []}
      },
      visible: {
        type: Boolean,
        required: true,
        default: false
      }
    },
    methods: {
      selectDialog (row) {
        this.$parent.close()
        this.$parent.unitManageForm.tchildNo=row.stuNo
        // this.$parent.stuInfo=row
        this.$parent.sonSearch()
      },
      close () {
        this.$parent.close()
      },
    },
  }
</script>