<template>
  <el-dialog :title="isEdit ? '编辑' : '新增'" :visible.sync="visible" width="40%" :before-close="close"
    :close-on-click-modal="false" class="schoolManageFormDialog" :lock-scroll="true" :append-to-body="true">
    <div>
      <el-form label-width="150px" :model="unitManageForm" ref="unitManageForm" :rules="rules" inline>
        <ss-common-area-group :columnCount="1" style="width：100%">
          <ss-common-area-item label="接种单位编码:" prop="schStationCode" v-if="isEdit">
            <el-input v-model="unitManageForm.schStationCode" disabled style="width: 250px" maxlength="10"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="接种单位名称:" prop="schStationName" v-if="isEdit">
            <el-input v-model="unitManageForm.schStationName" disabled style="width: 250px"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="所属地区:" prop="areaId" v-if="!isEdit">
            <ss-area-areaTreeStation ref="areaTreePage" @nodeClick="nodeClickSelf" :queryNode="queryNode"
              :getToLevelNum="5 - currentLevel" :disabled="msgPubPermission" />
          </ss-common-area-item>
          <ss-common-area-item label="学校编码:" prop="schCode">
            <el-input v-model.trim="unitManageForm.schCode" maxlength="3" :disabled="isEdit ? true : false"
              style="width: 250px"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="学校名称:" prop="schName">
            <el-input clearable style="width: 250px" maxlength="18" v-model.trim="unitManageForm.schName"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="学校类型:" prop="schType">
            <el-select v-model="unitManageForm.schType">
              <el-option v-for="item in selectTypeList" :key="item.code" :label="item.name" :value="item.code" />
            </el-select>
          </ss-common-area-item>
          <ss-common-area-item label="地址:" prop="schAddress">
            <el-input clearable style="width: 250px" maxlength="26" v-model.trim="unitManageForm.schAddress"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="电话:" prop="schPhone">
            <el-input clearable style="width: 250px" maxlength="26" v-model.trim="unitManageForm.schPhone" :disabled="isEdit && !isCreater"></el-input>
          </ss-common-area-item>
          <ss-common-area-item label="联系人:" prop="schLinkMan">
            <el-input clearable style="width: 250px" maxlength="26" v-model.trim="unitManageForm.schLinkMan"></el-input>
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
import { insertSchool, schoolDetail, updateSchool } from '@/utils/axios/apis/informationManage/schoolManage'
import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'schoolManageDialog',
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
          schoolDetail({ schId: this.curId }).then((res) => {
            this.detail = res.data
            this.isCreater = this.$store.state.userCode === res.data.schStationCode
            this.unitManageForm.schStationCode = res.data.schStationCode
            this.unitManageForm.schStationName = res.data.schStationName
            this.unitManageForm.schCode = res.data.schCode
            this.unitManageForm.schName = res.data.schName
            this.unitManageForm.schType = res.data.schType
            this.unitManageForm.schAddress = res.data.schAddress
            this.unitManageForm.schPhone = res.data.schPhone.length > 11 ? this.$util.encryptPhone(res.data.schPhone, !this.isCreater) : res.data.schPhone
            this.unitManageForm.schLinkMan = res.data.schLinkMan
          })
        }
      }
    }
  },
  data () {
    let validatePhoneNum = (rule, value, callback) => {
      const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
      const telephoneRegex = /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/
      if (!reg.test(value) && !telephoneRegex.test(value) && this.isCreater) {
        callback(new Error('电话号格式不正确'))
      }
      callback()
    }
    let validateNum = (rule, value, callback) => {
      const reg = /^\d{3}$/
      if (!reg.test(value)) {
        callback(new Error('请输入3位数字学校编码'))
      }
      callback()
    }
    return {
      queryNode: {},
      areaCodes: '',
      areaName: '',
      currentLevel: Number(getLevel(this.$store.state.userCode)),
      selectTypeList: [
        {
          name: '幼托机构',
          code: '0'
        },
        {
          name: '小学',
          code: '1'
        }
      ],
      unitManageForm: {
        schCode: '',
        schName: '',
        schType: '0',
        schAddress: '',
        schPhone: '',
        schLinkMan: '',
        areaId: {},
        schStationCode: '',
        schStationName: ''
      },
      msgPubPermission: false,
      rules: {
        areaId: [
          { required: true, message: '请选择到接种单位', trigger: 'blur' }
        ],
        schCode: [
          { required: true, message: '请输入3位数字学校编码', trigger: 'blur' },
          { validator: validateNum, trigger: 'blur' }
        ],
        schName: [
          { required: true, message: '请输入学校名称', trigger: 'blur' }
        ],
        schType: [
          { required: true, message: '请选择学校类型', trigger: 'blur' }
        ],
        schAddress: [
          { required: true, message: '请输入学校地址', trigger: 'blur' }
        ],
        schPhone: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
          { validator: validatePhoneNum, trigger: 'blur' }
        ],
        schLinkMan: [
          { required: true, message: '请输入联系人', trigger: 'blur' }
        ]
      },
      // 是否是数据的创建者
      isCreater: false,
      detail: {}
    }
  },
  methods: {
    // 选择地区节点
    nodeClickSelf (data, checked, node) {
      if (this.$refs.areaTreePage.selectAreaLevel !== '4') {
        return this.$util.errorMsg('请选择到接种单位!')
      } else {
        if (data.children.length > 0) {
          this.queryNode = data.children[0]
        } else {
          this.queryNode = data
        }
        console.log('nodeClick-data ', data)
        this.unitManageForm.areaCode = this.queryNode.areacode
      }
    },
    // 保存或编辑
    save () {
      if (!this.isEdit) {
        if (this.$refs.areaTreePage.selectAreaLevel !== '4') {
          return this.$util.errorMsg('请选择到接种单位!')
        }
        this.unitManageForm.areaId = this.$refs.areaTreePage.selectAreaId
        this.unitManageForm.areaCode = this.$refs.areaTreePage.selectAreaCode
        this.unitManageForm.areaName = this.$refs.areaTreePage.selectAreaName
        this.unitManageForm.supCode = this.$refs.areaTreePage.supcode || this.queryNode.supcode
      } else {
        this.unitManageForm.areaCode = this.unitManageForm.areaCode + '00'
      }
      if (!this.isCreater && this.isEdit) {
        this.unitManageForm.schPhone = this.detail.schPhone
      }
      const data = {
        ...this.unitManageForm
      }
      console.log('save-add', data)
      this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          if (this.isEdit) {
            // 编辑
            console.log('edit', { ...data, schId: this.curId })
            updateSchool({
              ...data,
              schId: this.curId,
              operateType: this.$consts.OPTYPE_UPDATE,
              cModId: this.$store.state.moduleId
            }).then((res) => {
              if (res.msg) {
                if (res.msg === '修改成功！') {
                  this.$message({
                    message: res.msg,
                    type: 'success'
                  })
                  this.close()
                  this.$emit('fetchData')
                } else if (res.msg.indexOf('该学校下存在学生') !== -1) {
                  this.$util.errorMsg(res.msg)
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'warning'
                  })
                }
              } else {
                this.$message({
                  message: res.data,
                  type: 'success'
                })
                this.close()
                this.$emit('fetchData')
              }
            }).catch(() => {
              // this.close();
              // this.$router.push({ name: 'errorPage' });
            })
          } else {
            // 新增
            console.log('save-add', data)
            insertSchool({
              ...data,
              operateType: this.$consts.OPTYPE_INSERT,
              cModId: this.$store.state.moduleId
            }).then((res) => {
              if (res.msg) {
                if (res.msg === '新增成功！') {
                  this.$message({
                    message: res.msg,
                    type: 'success'
                  })
                  this.close()
                  this.$emit('fetchData')
                } else if (res.msg.indexOf('学校编码已存在') !== -1) {
                  this.$util.errorMsg(res.msg)
                } else {
                  this.$message({
                    message: res.msg,
                    type: 'warning'
                  })
                }
              } else {
                this.$message({
                  message: res.data,
                  type: 'success'
                })
                this.close()
                this.$emit('fetchData')
              }
            }).catch(() => {
              // this.close();
              // this.$router.push({ name: 'errorPage' });
            })
          }
        }
      })
    },
    close () {
      this.$refs.unitManageForm.resetFields()
      this.queryNode = {}
      this.$emit('closeDialog')
    }
  },
  mounted () {
    this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
    console.log(this.msgPubPermission)
    // 等于4为门诊：true，其他是疾控：false
  }

}
</script>
<style>
.schoolManageFormDialog .el-dialog__body {
  overflow: auto !important;
}

.schoolManageFormDialog .el-input {
  width: 250px;
}

::v-deep .schoolManageFormDialog .el-transfer-panel {
  border: 1px solid rgb(212, 208, 208);
}
</style>
