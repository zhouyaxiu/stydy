<template>
  <!-- 批量导入按钮功能组件 -->
  <el-dialog title="选择导入内容" :visible.sync="visible" width="40%" :before-close="close" :close-on-click-modal="false">
    <div v-if="visible" class="content">
      <el-radio-group v-model="radioGroup">
        <el-radio label="0">编码导入</el-radio>
        <el-radio label="1">身份证件号码导入</el-radio>
      </el-radio-group>
      <el-upload class="mt_10" ref="upload_file" action :limit="1" accept=".xls,.xlsx" :before-upload="beforeUploadFile"
        :http-request="uploadFile" :headers="{ 'Content-Type': 'multipart/form-data' }" :auto-upload="true"
        :on-change="progress" :on-remove="revomeFile" :show-file-list="true" :file-list="fileList">
        <!-- <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES[type]" /> -->
        <el-button type="primary" style="height: 36px; margin-left: 0; margin-top: 10px;width: 113px;">选择档案</el-button>
      </el-upload>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { read, utils } from 'xlsx'
export default {
  data() {
    return {
      radioGroup: '0',
      file: null,
      fileList: []
    }
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'batchImport'
    }
  },
  methods: {
    // 批量导入前格式校验
    beforeUploadFile(file) {
      const fileArr=file.name.split(".");
      const extension = fileArr[fileArr.length-1] === 'xls'
      const extension2 = fileArr[fileArr.length-1] === 'xlsx'
      if (!extension && !extension2) {
        this.$util.errorMsg('上传文件只能是 xls、xlsx格式!')
        this.$emit('uploadEnd')
      }
    },
    // 读取Excel文件内容
    readFile(file) {
      return new Promise(resolve => {
        let reader = new FileReader()
        reader.readAsBinaryString(file)
        reader.onload = ev => { resolve(ev.target.result) }
      })
    },
    // 批量导入
    async uploadFile(file) {
      const fileArr=file.file.name.split(".");
      let extension = fileArr[fileArr.length-1] === 'xls'
      let extension2 = fileArr[fileArr.length-1] === 'xlsx'
      if (!extension && !extension2) {
        this.$emit('uploadEnd')
        return
      }
      let dataBinary = await this.readFile(file.file)
      let workBook = read(dataBinary, { type: 'binary', cellDates: true })
      let workSheet = workBook.Sheets[workBook.SheetNames[0]]
      const data = utils.sheet_to_json(workSheet)
      console.log(data)
      if (data.length < 1) {
        this.$message.error('至少填写一条数据')
        this.$emit('uploadEnd')
      } else {
        this.fileList.push({ name: file.file.name })
        this.file = file
        this.$emit('uploadEnd')
        this.$util.successMsg('导入成功')
        // this.$emit('upload', this.file)
        // this.revomeFile()
        // }
      }
    },
    // 文件上传中
    progress() {
      this.$emit('progress')
    },
    // 删除上传文件
    revomeFile() {
      this.file = null
      this.fileList = []
    },
    // 点击保存执行导入
    save () {
      if (this.file === null) {
        return this.$util.errorMsg('请选择上传文件!')
      }
      this.$emit('upload', this.file, this.radioGroup)
      this.revomeFile()
    },
    // 关闭
    close () {
      this.radioGroup='0'
      this.revomeFile()
      this.$emit('close')
    }
  }
}
</script>

<style></style>
