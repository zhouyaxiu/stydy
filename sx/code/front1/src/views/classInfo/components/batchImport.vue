<template>
  <!-- 批量导入按钮功能组件 -->
  <el-upload ref="upload_file" action :limit="1" accept=".xls,.xlsx" :before-upload="beforeUploadFile"
    :http-request="uploadFile" :headers="{ 'Content-Type': 'multipart/form-data' }" :auto-upload="true"
    :on-change="progress" :on-remove="revomeFile" :show-file-list="false">
    <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.import"/>
  </el-upload>
</template>
<script>
import { read, utils } from 'xlsx'
export default {
  data () {
    return {
      file: null,
      fileList: []
    }
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    // 批量导入前格式校验
    beforeUploadFile (file) {
      const extension = file.name.split('.')[1] === 'xls'
      const extension2 = file.name.split('.')[1] === 'xlsx'
      if (!extension && !extension2) {
        this.$util.errorMsg('上传文件只能是 xls、xlsx格式!')
        this.$emit('uploadEnd')
      }
    },
    // 读取Excel文件内容
    readFile (file) {
      return new Promise(resolve => {
        let reader = new FileReader()
        reader.readAsBinaryString(file)
        reader.onload = ev => { resolve(ev.target.result) }
      })
    },
    // 检测导入数据
    checkData (data) {
      let errMes = ''
      let yzy = ['幼儿园托班', '幼儿园小班', '幼儿园中班', '幼儿园大班']
      let xx = ['一年级', '二年级', '三年级', '四年级', '五年级', '六年级']
      const reg_tel = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
      const telephoneRegex = /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/
      // let reg_tel = /^(((\d{3,4}-)?[0-9]{7,8})|(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8})$/
      for (let key in data) {
        let item = data[key]
        if (key === '0' || key === JSON.stringify(data.length - 1)) {
          continue
        } else {
          if (item['__EMPTY'] === undefined) {
            errMes += `第${Number(key) + 3}行数据:年份不能为空;<br/>`
          }
          if (item['__EMPTY'] !== undefined && !/^[0-9]{4}[\d]*$/.test(item['__EMPTY'])) {
            errMes += `第${Number(key) + 3}行数据:年份请输入四位数字;<br/>`
          }
          if (item['__EMPTY'] !== undefined && (item['__EMPTY'] > new Date().getFullYear() || item['__EMPTY'] < 1900)) {
            errMes += `第${Number(key) + 3}行数据:年份不能小于1900，并且不能大于当前年份;<br/>`
          }
          if (item['__EMPTY_1'] === undefined) {
            errMes += `第${Number(key) + 3}行数据:轮次不能为空;<br/>`
          }
          if (item['__EMPTY_2'] === undefined) {
            errMes += `第${Number(key) + 3}行数据:年级不能为空;<br/>`
          }
          if (item['__EMPTY_2'] !== undefined && this.$store.state.schType === '0' && yzy.indexOf(item['__EMPTY_2']) === -1) {
            errMes += `第${Number(key) + 3}行数据:幼托机构的年级必须为幼儿园托班、幼儿园小班、幼儿园中班、幼儿园大班;<br/>`
          }
          if (item['__EMPTY_2'] !== undefined && this.$store.state.schType === '1' && xx.indexOf(item['__EMPTY_2']) === -1) {
            errMes += `第${Number(key) + 3}行数据:小学的年级必须为一年级、二年级、三年级、四年级、五年级、六年级;<br/>`
          }
          if (item['__EMPTY_3'] === undefined) {
            errMes += `第${Number(key) + 3}行数据:班级不能为空;<br/>`
          }
          if (item['班级信息'] === undefined || !/^[1-9]{1,}[\d]*$/.test(Number(item['班级信息']))) {
            errMes += `第${Number(key) + 3}行数据:班级总人数请输入1-3位正整数;<br/>`
          }
          if (item['__EMPTY_4'] !== undefined && !reg_tel.test(item['__EMPTY_4']) && !telephoneRegex.test(item['__EMPTY_4'])) {
            errMes += `第${Number(key) + 3}行数据:电话号格式不正确;<br/>`
          }
        }
      }
      return errMes
    },
    // 批量导入
    async uploadFile (file) {
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
      if (data.length < 3) {
        this.$message.error('至少填写一条数据')
        this.$emit('uploadEnd')
      } else {
        let errMes = this.checkData(data)
        if (errMes !== '') {
          this.$message({
            dangerouslyUseHTMLString: true,
            message: errMes,
            type: 'error',
            duration: 6000,
            showClose: true
          })
          this.$emit('uploadEnd')
          return
        }else{
          this.fileList.push({ name: file.file.name })
          this.file = file
          this.$emit('uploadEnd')
          this.$emit('upload', this.file)
          this.revomeFile()
        }
      }
    },
    // 文件上传中
    progress () {
      this.$emit('progress')
    },
    // 删除上传文件
    revomeFile () {
      this.file = null
      this.fileList = []
    }
  }
}
</script>

<style>
</style>
