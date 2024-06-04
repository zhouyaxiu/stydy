<template>
    <el-dialog
      title="受种者明细"
      :visible.sync="visible"
      width="60%"
      :before-close="close"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div class="areaInfoDialog" :style="{ height: height - 50 + 'px' }" style="overflow-y: auto;overflow-x: hidden;">
        <ss-table :data="gridData" ref="tabPage" class="infoTable">
            <el-table-column prop="stuNo" align="center" min-width="180" label="受种者预防接种档案编号" />
            <el-table-column prop="stuImuno" align="center" min-width="180" label="受种者预防接种条码" />
            <el-table-column prop="stuName" align="center" min-width="120" label="姓名" />
            <el-table-column prop="stuBirth" align="center" min-width="160" label="出生日期" />
            <el-table-column prop="schname" align="center" min-width="160" label="学校" />
            <el-table-column prop="stuclayear" align="center" min-width="160" label="学年" />
            <el-table-column prop="stuClalevel" align="center" min-width="160" label="年级" />
            <el-table-column prop="stuSeq" align="center" min-width="160" label="轮次" />
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
        gridData: {
          type: Array,
          default: function(){return []}
        },
        visible: {
          type: Boolean,
          required: true,
          default: false
        },
      },
      methods: {
        close () {
          this.$emit('close', true)
        }
      },
    }
    </script>