<template>
  <!-- 下载导入模板组件 -->
  <el-dialog title="选择下载内容" :visible.sync="visible" width="40%" :before-close="close" :close-on-click-modal="false" class="down">
    <div class="content">
      <el-checkbox-group v-model="checkboxGroup">
        <el-checkbox label="身份证件号码导入模板" border></el-checkbox>
        <el-checkbox label="接种证号导入模板" border></el-checkbox>
        <el-checkbox label="姓名、性别、出生日期、父亲或母亲姓名导入模板" border></el-checkbox>
      </el-checkbox-group>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { dowmLoadInfoCollectMould } from '@/utils/axios/apis/collect'
const options = {
  '身份证件号码导入模板': '1',
  '接种证号导入模板': '2',
  '姓名、性别、出生日期、父亲或母亲姓名导入模板': '3'
}
export default {
  data () {
    return {
      checkboxGroup: ['身份证件号码导入模板']
    }
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    // 点击保存执行导入
    save () {
      if (!this.checkboxGroup.length) {
        this.$util.errorMsg('请选择下载模板！')
        return
      }
      this.downFile(0)
    },
    // 下载模板
    downFile (index) {
      setTimeout(async () => {
        if (index < this.checkboxGroup.length) {
          this.$util.infoMsg('正在下载' + this.checkboxGroup[index])
          await dowmLoadInfoCollectMould({ collectType: options[this.checkboxGroup[index]] })
          this.downFile(++index)
        } else {
          this.close()
        }
      }, 100)
    },
    // 关闭
    close () {
      this.checkboxGroup = ['身份证件号码导入模板']
      this.$emit('close')
    }
  }
}
</script>
<style scoped>
.content{
  width: 65%;
  margin: 0 auto;
  margin-top: 20px;
}
</style>
<style>
.down .el-checkbox.is-bordered{
  display: block;
  height: 48px;
  line-height: 30px;
  padding-left: 70px;
}
.down .el-checkbox__label{
  padding-left: 34px;
}
.down .el-checkbox, .down .el-checkbox.is-bordered+.el-checkbox.is-bordered{
  margin: 0 !important;
  margin-bottom: 20px !important;
}

</style>
