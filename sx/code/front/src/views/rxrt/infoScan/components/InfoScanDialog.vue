<template>
  <div>
    <el-dialog
      title="信息扫码选择"
      :visible.sync="visible"
      width="70%"
      :before-close="close"
      :lock-scroll="true"
      :append-to-body="true"
    >
    <!-- :close-on-click-modal="false" -->
      <div class="areaInfoDialog" style="overflow-y: auto;overflow-x: hidden;max-height:500px">
        <ss-table :data="dataList" ref="tabPage" class="infoTable">
          <el-table-column prop="fchildno" align="center" min-width="180" label="儿童编码" />
          <el-table-column prop="name" align="center" min-width="120" label="姓名" />
          <el-table-column prop="birth" align="center" min-width="160" label="出生日期" />
          <el-table-column prop="fathername" align="center" min-width="160" label="父亲/母亲/监护人姓名">
            <!-- mothername fathername -->
            <template slot-scope="{ row }">
              {{ row.mothername?row.mothername:row.fathername?row.fathername:''}}
            </template>
          </el-table-column>
          <el-table-column prop="sex" align="center" min-width="80" label="性别" />
          <el-table-column prop="regtype" align="center" min-width="100" label="户籍类型" />
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <span @click="selectDialog(row)" style="cursor:pointer;font-weight: bold;color: #2f6cc9;margin-right:10px;">选择</span>
            </template>
          </el-table-column>
        </ss-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name: 'receptionInfoDialog',
    data () {
      return {
        height: window.winHeight - 15 / window.scale - 500,
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
        this.$parent.unitManageForm.tchildNo=row.fchildno
        this.$parent.close()
        this.$parent.sonSearch()
        // this.$parent.stuInfo=row
      },
      close () {
        this.$parent.close()
      },
    },
  }
</script>