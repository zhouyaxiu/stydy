<template>
    <el-dialog
      title="信息扫码选择"
      :visible.sync="visible"
      width="70%"
      :before-close="close"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div class="areaInfoDialog" style="overflow-y: auto;overflow-x: hidden;">
        <ss-table :data="dataList" ref="tabPage" class="infoTable">
          <el-table-column prop="stuNo" align="center" min-width="180" label="受种者预防接种档案编号" />
          <el-table-column prop="stuName" align="center" min-width="120" label="姓名" />
          <el-table-column prop="stuBirth" align="center" min-width="100" label="出生日期" />
          <el-table-column prop="stuParentname" align="center" min-width="160" label="父亲/母亲/监护人姓名" />
          <el-table-column prop="stuSex" align="center" min-width="80" label="性别" />
          <el-table-column prop="stuType" align="center" min-width="100" label="户籍类型" />
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <span @click="selectDialog(row)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;margin-right:10px;">选择</span>
              <span @click="openDetailDialog(row)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;">查看</span></template>
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
    export default {
      name: 'receptionInfoDialog',
      components: { CustomDescriptions },
      data () {
        return {
          editorVisible: false,
          height: window.winHeight - 15 / window.scale - 500,
        }
      },
      props: {
        dataList: {
          type: Array,
          default: []
        },
        visible: {
          type: Boolean,
          required: true,
          default: false
        },
        type: {
          type: String,
          default: 'imuno'
        },
      },
      methods: {
        selectDialog (row) {
          if(this.type=='imuno'){
            this.$parent.saveHandler(row.stuImuno,this.type)
            this.$parent.close1()
          }else{
            this.$parent.saveHandler(row.stuNo,this.type)
            this.$parent.close1()
          }
        },
        openDetailDialog(row){
          this.$parent.getDetail(row)
        },
        close () {
          // this.$emit('close1', true)
          this.$parent.close1()
        },
      },
    }
    </script>