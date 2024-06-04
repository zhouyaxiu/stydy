<template>
    <el-dialog title="受种者已经存在的学校和门诊信息" :visible.sync="visible" :before-close="close" :lock-scroll="true" :append-to-body="true">
    <!-- 学生的编码　条码　　姓名　学校　学年　年级　轮次 -->
    <ss-table :data="gridData" ref="tabPage" class="infoTable">
        <el-table-column prop="stuNo" align="center" min-width="180" label="儿童编码" />
        <el-table-column prop="stuImuno" align="center" min-width="180" label="接种条码" />
        <el-table-column prop="stuName" align="center" min-width="120" label="姓名" />
        <el-table-column prop="stuStationname" align="center" min-width="160" label="所属门诊" />
        <el-table-column prop="schName" align="center" min-width="160" label="学校" />
        <el-table-column prop="academicYear" align="center" min-width="160" label="学年" />
        <el-table-column prop="grade" align="center" min-width="160" label="年级" />
        <el-table-column prop="stuSeq" align="center" min-width="160" label="轮次">
          <template slot-scope="{ row }">
            <span>{{ row.rounds === '1' ? '春季' : '秋季' }}</span>
          </template>
        </el-table-column>
    </ss-table>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveHandler1">保存</el-button>
        <el-button @click="close">取消</el-button>
    </span>
    </el-dialog>
  </template>
  <script>
    import { okSave } from '@/utils/axios/apis/rxrt/showInforInspectionResultQuery'
    export default {
      name: 'receptionInfoDialog',
      data () {
        return {
          height: window.winHeight - 15 / window.scale - 500,
        //   dialogTableVisible:false,
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
        searchData: {
          type: Object,
          default: function(){return {}}
        },
      },
      methods: {
        close () {
          // this.$parent.$parent.close()
          this.$parent.close()
        },
        saveHandler1(){
          okSave(this.searchData).then(res => {
            this.$message({
              message: res.data || '保存成功!',
              type: 'success'
            })
            this.closeFullScreen()
          }).catch(() => {
            this.closeFullScreen()
          })
          this.close()
        }
      },
    }
  </script>