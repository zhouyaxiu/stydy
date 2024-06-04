<template>
  <el-dialog
    title="查验接种修改"
    :visible.sync="visible"
    :before-close="close"
    :close-on-click-modal="false"
    custom-class="cydialog"
    max-width="600px"
    :lock-scroll="true" :append-to-body="true"
  >
    <div class="areaInfoDialog" style="overflow-y: auto;">
      <el-form label-width="240px" label-position="right" :model="unitManageForm"  :rules="rules" ref="unitManageForm">
        <div class="scroll">
          <el-form-item label="学年：" prop="stuclayear">
            <el-select clearable v-model="unitManageForm.stuclayear" placeholder="请选择">
              <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item"/>
            </el-select>
          </el-form-item>
          <el-form-item label="年级：" prop="stuClalevel">
            <el-select clearable v-model="unitManageForm.stuClalevel" placeholder="请选择" @change="$forceUpdate()">
              <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code"/>
            </el-select>
          </el-form-item>
          <el-form-item label="班级：" prop="classId">
            <el-input v-model.trim="unitManageForm.classId"  maxlength="10" style="width:220px;" />
            <!-- <el-select v-model="unitManageForm.classId" placeholder="请选择" clearable>
              <el-option v-for="item in classesList" :key="item" :label="item" :value="item"/>
            </el-select> -->
          </el-form-item>
          <el-form-item label="轮次：" prop="lunci">
            <el-select v-model="unitManageForm.lunci" placeholder="请选择" clearable>
              <el-option label="春季" value="1" />
              <el-option label="秋季" value="0" />
            </el-select>
          </el-form-item>
        </div>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button @click="save" type="primary">保 存</el-button>
    </span>
  </el-dialog>
</template>
<script>
  import { query,save4MultipleEdit } from '@/utils/axios/apis/roster/index'
  import commonOptions from '@/mixin/commonOptions'
  export default {
    name: 'receptionInfoDialog',
    mixins: [commonOptions],
    data () {
      return {
        editorVisible: false,
        height: window.winHeight - 15 / window.scale - 650,
        unitManageForm: {},
        rules:{
          stuclayear: [
            { required: true, message: '请选择学年', trigger: 'change' },
          ],
          stuClalevel: [
            { required: true, message: '请选择年级', trigger: 'change' },
          ],
          classId: [
            { required: true, message: '请输入班级', trigger: 'blur' },
          ],
          lunci: [
            { required: true, message: '请选择轮次', trigger: 'change' },
          ],
        },
      }
    },
    props: {
      rowkey: {
        type: String | Number,
        default: '' | 0
      },
      visible: {
        type: Boolean,
        required: true,
        default: false
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
      getDetailInfo () {
        if (!this.rowkey || this.rowkey === '') return false
        // this.unitManageForm = {
        //   stuclayear: this.$parent.unitManageForm.stuclayear,
        //   stuClalevel: this.$parent.unitManageForm.stuClalevel,
        //   classId: this.$parent.unitManageForm.classId,
        //   lunci: this.$parent.unitManageForm.lunci,
        // }
        this.openFullScreen()
        query({ rowkey:this.rowkey,operateType:this.$consts.OPTYPE_QUERY})
          .then(async (res) => {
            if (res && res.data) {
              // this.dataList=res.data.list;
              // this.total=res.data.total;
              this.unitManageForm = {
                stuclayear: res.data.data.academicYear,
                stuClalevel: res.data.data.grade,
                classId: res.data.data.clazzName,
                lunci: res.data.data.rounds,
              }
              // let params={
              //   schId:this.$store.state.schId,
              //   classLevel:res.data.data.grade,
              //   classYear:res.data.data.academicYear,
              //   lunci:res.data.data.rounds
              // }
              // await this.findClassNew(params)
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
        this.$parent.closeAlter()
        // this.$emit('closeAlter', true)
      },
      save () {
        if(!/^[0-9A-Za-z\u4e00-\u9fa5]+$/.test(this.unitManageForm.classId)){
          this.$util.errorMsg('班级只能是中文，英文，数字!')
          return false
        }
        let params = {
          class: this.unitManageForm.classId,
          grade: this.unitManageForm.stuClalevel,
          rounds: this.unitManageForm.lunci,
          rowkey: this.rowkey,
          year: this.unitManageForm.stuclayear,
          operateType: this.$consts.OPTYPE_UPDATE
        }
        this.$refs.unitManageForm.validate().then((flag) => {
          if (flag) {
            this.openFullScreen('正在保存...')
            save4MultipleEdit(params).then((res) => {
              this.$util.successMsg('修改成功'+res.data.data+'条数据！')
              this.$parent.search()
              this.$parent.closeAlter()
              this.closeFullScreen()
            }).catch(() => {
              this.closeFullScreen()
            })
          }
        })
      }
    },
    async mounted () {
      // 查询学年
      await this.findClaYear()
      await this.findClaLevelNew(this.$parent.unitManageForm.checkType)
    }
  }
</script>
<style>
  /* .cydialog{min-width: 1300px;top:50%;height:45%;}
  .cydialog .el-dialog__body{max-height: 40%;margin-top: 30px;} */
  .cydialog .el-input{min-width: 200px;}
  .cydialog .el-date-editor{width:200px;}
  /* .cydialog .el-dialog__footer{text-align: center;margin-bottom: 40px;} */
</style>