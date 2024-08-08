<template>
  <!-- 批量导入按钮功能组件 -->
  <el-upload ref="upload_file" action :limit="1" accept=".xls,.xlsx" :before-upload="beforeUploadFile"
    :http-request="uploadFile" :headers="{ 'Content-Type': 'multipart/form-data' }" :auto-upload="true"
    :on-change="progress" :on-remove="revomeFile" :show-file-list="false">
    <ss-custom-button style="height:37px" :type="$consts.CUSTOMBUTTONTYPES[type]" />
  </el-upload>
</template>
<script>
import { read, utils } from 'xlsx'
export default {
  data() {
    return {
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
      const extension = file.name.split('.')[1] === 'xls'
      const extension2 = file.name.split('.')[1] === 'xlsx'
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
      let extension = file.file.name.split('.')[1] === 'xls'
      let extension2 = file.file.name.split('.')[1] === 'xlsx'
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
        this.$emit('upload', this.file)
        this.revomeFile()
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
    }
  }
}
</script>

<style></style>
