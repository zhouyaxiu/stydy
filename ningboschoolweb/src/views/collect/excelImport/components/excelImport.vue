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
        const fileArr=file.name.split(".");
        const extension = fileArr[fileArr.length-1] === 'xls'
        const extension2 = fileArr[fileArr.length-1] === 'xlsx'
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
        for (let key in data) {
          let item = data[key]
          if (key === '0' || key === JSON.stringify(data.length - 1)) {
            continue
          } else {
            if (item['海南入托入学管理系统受种者信息'] === undefined) {
              errMes += `第${Number(key) + 3}行数据:受种者姓名不能为空;<br/>`
            }
            if (item['__EMPTY'] === undefined) {
              errMes += `第${Number(key) + 3}行数据:出生日期不能为空;<br/>`
            }
            if (item['__EMPTY_3'] === undefined) {
              errMes += `第${Number(key) + 3}行数据:父亲/母亲/监护人姓名不能为空;<br/>`
            }
            if (item['__EMPTY_2'] === undefined) {
              errMes += `第${Number(key) + 3}行数据:性别不能为空;<br/>`
            }
          }
        }
        return errMes
      },
      // 批量导入
      async uploadFile (file) {
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
          }
          this.fileList.push({ name: file.file.name })
          this.file = file
          this.$emit('uploadEnd')
          this.$emit('upload', this.file)
          this.revomeFile()
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