<template>
  <el-dialog
    :title="isEdit ? '修改' : '新增'"
    :visible.sync="visible"
    :before-close="close"
    :close-on-click-modal="false"
    custom-class="userdialog"
    max-width="600px"
    :append-to-body="true"
    :lock-scroll="true"
  >
    <div class="areaInfoDialog">
      <el-form label-width="240px" label-position="right" :validate-on-rule-change="false" :model="unitManageForm" :rules="isEdit?unitManageForm.isAlterPwd=='1'?editHasAuthRules:editNoAuthRules:addRules" ref="unitManageForm">
        <el-form-item label="所属地区：" required>
          <el-input :disabled="true" readonly v-if="isEdit" :value="unitManageForm.areaName +' '+ unitManageForm.areaCode"></el-input>
          <ss-area-areaTreeStation :showSelect="false" style="width: 250px" ref="areaTreePage" :getToLevelNum="5 - currentLevel" :disabled="msgPubPermission" v-else />
        </el-form-item>
        <el-form-item label="用户显示名：" prop="userName">
          <el-input clearable style="width: 250px" maxlength="15" v-model.trim="unitManageForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="登录账号：" prop="loginName">
          <el-input v-model.trim="unitManageForm.loginName" maxlength="15" style="width:250px;" clearable />
        </el-form-item>
        <el-form-item label="用户权限：" required prop="userType">
          <el-radio-group v-model="unitManageForm.userType" style="white-space: nowrap;">
              <el-radio :label="0">管理员</el-radio>
              <el-radio :label="1">普通用户</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item label="是否修改密码：" v-if="isEdit" prop="isAlterPwd">
          <el-radio-group v-model="unitManageForm.isAlterPwd" style="white-space: nowrap;">
              <el-radio :label="0">否</el-radio>
              <el-radio :label="1">是</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item label="密码：" prop="userPassword" v-if="!isEdit">
          <el-input clearable style="width: 250px" min="8" maxlength="16" v-model.trim="unitManageForm.userPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码：" prop="newuserPassword" v-if="unitManageForm.isAlterPwd=='1'">
          <el-input clearable style="width: 250px" min="8" maxlength="16" v-model.trim="unitManageForm.newuserPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认新密码：" prop="newConfirmuserPassword" v-if="unitManageForm.isAlterPwd=='1'">
          <el-input clearable style="width: 250px" min="8" maxlength="16" v-model.trim="unitManageForm.newConfirmuserPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="备注：">
          <div style="margin-bottom: -15px;">① 新密码长度为8-16位；</div>
          <div style="margin-bottom: -15px;">② 新密码必须同时包含数字、大写字母、小写字母、英文特殊字符；</div>
          <div>③ 字母：a-z A-Z，数字：0-9，特殊字符! @ # $</div>
        </el-form-item>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button @click="saveHandler" type="primary">保 存</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { addUser, updateUser } from '@/utils/axios/apis/rxrt/userManage'
import { getLevel } from '@/utils/common/area.js'
import commonOptions from '@/mixin/commonOptions'
  export default {
    name: 'receptionInfoDialog',
    mixins: [commonOptions],
    data () {
      var validateName = (rule, value, callback) => {
        const reg = /^[\u4e00-\u9fa5_a-zA-Z]{1,15}$/
        if (!reg.test(value)) {
          callback(new Error('只允许输入中英文和下划线'))
        }
        callback()
      }
      var validatePass = (rule, value, callback) => {
        const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
        if (!reg.test(value)) {
          callback(new Error('密码长度为8-16位，须包含数字、大写字母、小写字母数字和特殊字符'))
        }
        callback()
      }
      var validatePass1 = (rule, value, callback) => {
        const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
        if (!reg.test(value)) {
          callback(new Error('密码长度为8-16位，须包含数字、大写字母、小写字母和特殊字符'))
        } else {
          if (value !== this.unitManageForm.newuserPassword) {
            callback(new Error('确认密码与新密码输入不一致'))
          }
        }
        callback()
      }
      return {
        currentLevel: Number(getLevel(this.$store.state.userCode)),
        isRefresh: true,
        msgPubPermission: false,
        unitManageForm: {
          userName:'',
          loginName:'',
          userPassword:'SXcyxt@123456',
          newuserPassword:'',
          newConfirmuserPassword:'',
          isAlterPwd:0,
          userType:1,//0:管理员 1：普通用户
        },
        rules:{},
        editNoAuthRules: {
          userName: [
            { required: true, message: '请输入用户显示名', trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
          loginName: [
            { required: true, message: '请输入登录账号', trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
          userType: [
            { required: true, message: '请选择用户权限', trigger: 'change' },
          ],
          isAlterPwd: [
            { required: true, message: '请选择是否修改密码', trigger: 'change' },
          ],
        },
        editHasAuthRules: {
          userName: [
            { required: true, message: '请输入用户显示名', trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
          loginName: [
            { required: true, message: '请输入登录账号', trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
          userType: [
            { required: true, message: '请选择用户权限', trigger: 'change' },
          ],
          isAlterPwd: [
            { required: true, message: '请选择是否修改密码', trigger: 'change' },
          ],
          newuserPassword: [
            { required: true, message: '请输入新密码', trigger: 'blur' },
            { validator: validatePass, trigger: 'blur' }
          ],
          newConfirmuserPassword: [
            { required: true, message: '请输入确认新密码', trigger: 'blur' },
            { validator: validatePass1, trigger: 'blur' }
          ]
        },
        addRules: {
          userName: [
            { required: true, message: '请输入用户显示名', trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
          loginName: [
            { required: true, message: '请输入登录账号', trigger: 'blur' },
            { validator: validateName, trigger: 'blur' }
          ],
          userType: [
            { required: true, message: '请选择用户权限', trigger: 'change' },
          ],
          userPassword: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { validator: validatePass, trigger: 'blur' }
          ],
        },
      }
    },
    props: {
      visible: {
        type: Boolean,
        default: false
      },
      isEdit: {
        type: Boolean,
        default: false
      },
      row: {
        type: Object,
        default: {}
      }
    },
    watch: {
      visible (v) {
        if (v) {
          if (this.isEdit) {
            this.unitManageForm.userName = this.row.userName
            this.unitManageForm.loginName = this.row.loginName
            this.unitManageForm.userPassword = this.row.userPassword
            this.unitManageForm.newuserPassword = ''
            this.unitManageForm.areaCode = this.row.areaCode
            this.unitManageForm.areaName = this.row.areaName
            this.unitManageForm.updateUser = this.row.updateUser
            this.unitManageForm.areaId = this.row.areaId
            this.unitManageForm.id = this.row.id
            this.unitManageForm.userType = this.row.userType
          }else{
            this.unitManageForm={
              userName:'',
              loginName:'',
              userPassword:'SXcyxt@123456',
              newuserPassword:'',
              newConfirmuserPassword:'',
              isAlterPwd:0,
              userType:1
            }
          }
        }
      },
    },
    methods: {
      saveHandler(){
        this.$refs.unitManageForm.validate().then((flag) => {
        if (flag) {
          if (this.isEdit) {
            updateUser({
              id: this.unitManageForm.id,
              loginName:this.unitManageForm.loginName,
              username:this.unitManageForm.userName,
              userPassword:this.unitManageForm.isAlterPwd==1?this.$md5(this.unitManageForm.newuserPassword):this.unitManageForm.userPassword,
              updateUser:this.unitManageForm.updateUser,
              userType:this.unitManageForm.userType,
              operateType: this.$consts.OPTYPE_UPDATE,
              cModId: this.$store.state.moduleId
            })
              .then((res) => {
                if (res.code!=200) {
                  this.$message({
                    message: res.msg,
                    type: 'error'
                  })
                } else {
                  this.$message({
                    message: res.msg,
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
            let params={
              loginName:this.unitManageForm.loginName,
              username:this.unitManageForm.userName,
              userPassword:this.$md5(this.unitManageForm.userPassword),
              updateUser:sessionStorage.getItem("userName") || this.unitManageForm.updateUser,
              areaId:this.$refs.areaTreePage.selectAreaId,
              userType:this.unitManageForm.userType,
              operateType: this.$consts.OPTYPE_INSERT,
              cModId: this.$store.state.moduleId
            }
            addUser(params)
              .then((res) => {
                if (res.code!=200) {
                  this.$message({
                    message: res.msg,
                    type: 'error'
                  })
                } else {
                  this.$message({
                    message: res.msg,
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
        this.$emit('close')
        this.$refs.unitManageForm.clearValidate()
      }
    },
    async mounted () {
      this.msgPubPermission = parseInt(sessionStorage.getItem('areaLevel')) == 4
    }
  }
</script>
<style>
  .userdialog .el-input{width: 250px;}
  .userdialog .tree-area{max-height:300px!important;}
</style>