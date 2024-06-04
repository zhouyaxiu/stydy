<template>
  <el-dialog :title="isEdit ? '修改' : '新增'" :visible.sync="visible" width="50%" :before-close="close"
    :close-on-click-modal="false" class="userManageFormDialog">
    <div style="height:500px">
      <el-form label-width="150px" :model="unitManageForm" ref="unitManageForm" :rules="rules" inline>
        <ss-common-area-group :columnCount="1" style="width：100%">
          <ss-common-area-item label="通知日期:" prop="notifyTime">
            <el-date-picker v-model="unitManageForm.notifyTime" disabled type="date" placeholder="选择日期"></el-date-picker>
          </ss-common-area-item>
          <ss-common-area-item label="通知标题:" prop="notifyTitle">
            <el-input clearable style="width: 250px" maxlength="20" v-model.trim="unitManageForm.notifyTitle"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="通知范围:" prop="areaRange">
            <ss-area-areaTreeStation ref="areaTreePage" @nodeClick="nodeClickSelf" :queryNode="queryNode" v-if="isRefresh" :getToLevelNum="5 - currentLevel" :treeH="'320px'"/>
          </ss-common-area-item>
          <ss-common-area-item label="发布内容:">
            <quill-editor style="width:600px; height:200px" v-model="content" ref="myQuillEditor" :options="editorOption"></quill-editor>
          </ss-common-area-item>
        </ss-common-area-group>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { addNotify, updateNotify, getNotify } from '@/utils/axios/apis/rxrt/showNotifyQuery'
import { getLevel } from '@/utils/common/area.js'

// 工具栏配置
const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'], // 加粗 斜体 下划线 删除线 -----['bold', 'italic', 'underline', 'strike']
  ['blockquote', 'code-block'], // 引用  代码块-----['blockquote', 'code-block']
  [{ header: 1 }, { header: 2 }], // 1、2 级标题-----[{ header: 1 }, { header: 2 }]
  [{ list: 'ordered' }, { list: 'bullet' }], // 有序、无序列表-----[{ list: 'ordered' }, { list: 'bullet' }]
  [{ script: 'sub' }, { script: 'super' }], // 上标/下标-----[{ script: 'sub' }, { script: 'super' }]
  [{ indent: '-1' }, { indent: '+1' }], // 缩进-----[{ indent: '-1' }, { indent: '+1' }]
  [{ direction: 'rtl' }], // 文本方向-----[{'direction': 'rtl'}]
  [{ size: ['small', false, 'large', 'huge'] }], // 字体大小-----[{ size: ['small', false, 'large', 'huge'] }]
  [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题-----[{ header: [1, 2, 3, 4, 5, 6, false] }]
  [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色-----[{ color: [] }, { background: [] }]
  [{ font: [] }], // 字体种类-----[{ font: [] }]
  [{ align: [] }], // 对齐方式-----[{ align: [] }]
  ['clean'], // 清除文本格式-----['clean']
  [] // 链接、图片、视频-----['link', 'image', 'video']
]

export default {
  name: 'publishManageDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    curId: {
      type: [Number, String],
      default: null
    }
  },
  watch: {
    visible (v) {
      if (v) {
        if (this.isEdit) {
          getNotify({ id: this.curId }).then((res) => {
            this.unitManageForm.notifyTime = res.data.notifyTime
            this.unitManageForm.notifyTitle = res.data.notifyTitle
            this.unitManageForm.areaCode = res.data.areaCode
            this.content = res.data.content
            // 初始化区划节点，刷新区划组件
            this.queryNode = {
              areacode: res.data.areaCode,
              areaname: res.data.areaName
            }
            this.isRefresh = false
            this.timer = setInterval(() => {
              this.isRefresh = true
            }, 300)
          })
        }
      }
    }
  },
  data () {
    // let validateTitle = (rule, value, callback) => {
    //   const reg = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]")
    //   if (reg.test(value)) {
    //     callback(new Error('不可输入特殊字符'))
    //   }
    //   callback()
    // }
    return {
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      isRefresh: true,
      queryNode: {},
      unitManageForm: {
        notifyTime: this.$util.today(),
        notifyTitle: ''
      },
      rules: {
        notifyTime: [
          { required: true, message: '请输入通知日期', trigger: 'blur' }
        ],
        notifyTitle: [
          { required: true, message: '请输入通知标题', trigger: 'blur' }
          // { validator: validateTitle, trigger: "blur" },
        ]
      },
      content: '', // 双向数据绑定数据
      editorOption: {
        placeholder: '请输入内容...',
        modules: {
          toolbar: {
            container: toolbarOptions,
            handlers: {
              image: this.handleSuccess
            }
          }
        }
      }
    }
  },
  methods: {
    // 选择地区节点
    nodeClickSelf (data, checked, node) {
      this.queryNode = data
      this.unitManageForm.areaCode = data.areacode
    },
    // 富文本上传图片
    handleImgUpload () {
      const { quill } = this.$refs.myQuillEditor
      // eslint-disable-next-line no-undef
      SelectImages({
        visible: true,
        multi: true,
        showButton: true,
        maxMulti: 18,
        success: (data, filPath) => {
          for (const item of data) {
            const length = quill.getSelection(true).index
            // 获取光标所在位置
            // 插入图片，res为服务器返回的图片链接地址
            quill.insertEmbed(length, 'image', filPath + item)
            // 调整光标到最后
            quill.setSelection(length + 1)
          }
        }
      })
    },
    save () {
      this.unitManageForm.areaLevel = this.$refs.areaTreePage.selectAreaLevel
      this.unitManageForm.areaCode = this.$refs.areaTreePage.selectAreaCode
      const data = {
        content: this.$refs.myQuillEditor.value,
        ...this.unitManageForm
      }
      this.$refs.unitManageForm.validate((valid) => {
        if (valid) {
          if (this.isEdit) {
            // 编辑
            console.log('edit', { ...data, id: this.curId })
            updateNotify({
              ...data,
              id: this.curId,
              operateType: this.$consts.OPTYPE_UPDATE,
              cModId: this.$store.state.moduleId
            })
              .then((res) => {
                if (res.msg) {
                  this.$message({
                    message: res.msg,
                    type: 'warning'
                  })
                } else {
                  this.$message({
                    message: res.data,
                    type: 'success'
                  })
                  this.$emit('fetchData')
                  this.close()
                }
              })
              .catch(() => {
                // this.close();
                // this.$router.push({ name: "errorPage" });
              })
          } else {
            // 新增
            console.log('save-add', data)
            addNotify({
              ...data,
              operateType: this.$consts.OPTYPE_INSERT,
              cModId: this.$store.state.moduleId
            })
              .then((res) => {
                if (res.msg) {
                  this.$message({
                    message: res.msg,
                    type: 'warning'
                  })
                } else {
                  this.$message({
                    message: res.data,
                    type: 'success'
                  })
                  this.$emit('fetchData')
                  this.close()
                }
              })
              .catch(() => {
                // this.close();
                // this.$router.push({ name: "errorPage" });
              })
          }
        }
      })
    },
    close () {
      this.$refs.unitManageForm.resetFields()
      this.$emit('close')
      this.content = ''
      this.queryNode = {}
    }
  }
}
</script>
<style>
.userManageFormDialog .el-dialog__body {
  overflow: auto !important;
}

.userManageFormDialog .el-input {
  width: 250px;
}

::v-deep .userManageFormDialog .el-transfer-panel {
  border: 1px solid rgb(212, 208, 208);
}
</style>
<style>
  .editor {
    line-height: normal !important;
    height: 800px;
  }
  .ql-snow .ql-tooltip[data-mode=link]::before {
    content: "请输入链接地址:";
  }
  .ql-snow .ql-tooltip.ql-editing a.ql-action::after {
      border-right: 0px;
      content: '保存';
      padding-right: 0px;
  }

  .ql-snow .ql-tooltip[data-mode=video]::before {
      content: "请输入视频地址:";
  }

  .ql-snow .ql-picker.ql-size .ql-picker-label::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item::before {
    content: '14px';
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value=small]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value=small]::before {
    content: '10px';
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value=large]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value=large]::before {
    content: '18px';
  }
  .ql-snow .ql-picker.ql-size .ql-picker-label[data-value=huge]::before,
  .ql-snow .ql-picker.ql-size .ql-picker-item[data-value=huge]::before {
    content: '32px';
  }

  .ql-snow .ql-picker.ql-header .ql-picker-label::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item::before {
    content: '文本';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
    content: '标题1';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
    content: '标题2';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
    content: '标题3';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
    content: '标题4';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
    content: '标题5';
  }
  .ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
  .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
    content: '标题6';
  }

  .ql-snow .ql-picker.ql-font .ql-picker-label::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item::before {
    content: '标准字体';
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label[data-value=serif]::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item[data-value=serif]::before {
    content: '衬线字体';
  }
  .ql-snow .ql-picker.ql-font .ql-picker-label[data-value=monospace]::before,
  .ql-snow .ql-picker.ql-font .ql-picker-item[data-value=monospace]::before {
    content: '等宽字体';
  }
  .ql-snow .ql-picker{
    height: 40px;
  }
  </style>
