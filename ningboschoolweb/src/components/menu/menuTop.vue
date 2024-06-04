<template>
  <div class="header-area" :style="backgroundSize">
    <div class="header-contain">
      <img class="header-left" src="../../assets/img/index/title.png">
      <div class="header-center">
        <ss-menu />
      </div>
      <div class="header-right">
        <div class="account-area">
          <div class="account-avatar"></div>
          <div class="account-text" :title="$store.state.schName">{{ $store.state.schName }}</div>
          <!-- <div class="account-mewmsg" @click="showMsg" v-show="!msgPermission"></div> -->
          <!-- <el-dropdown trigger="click" style="color: #fff">
            <img class="account-drop" src="../../assets/img/index/icon-drop.png" alt="">
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item class="clearfix">
                <div @click="editPwd()">修改密码</div>
              </el-dropdown-item> -->
          <!-- <el-dropdown-item class="clearfix">
                <div @click="$router.push('/systemManage/notify')">消息管理</div>
              </el-dropdown-item> -->
          <!-- </el-dropdown-menu>
          </el-dropdown> -->
        </div>
        <div class="logout-area" @click="logout()"></div>
      </div>
    </div>
    <el-dialog title="修改密码" :visible.sync="visible" width="30%" :before-close="close" :close-on-click-modal="false">
      <div class="pwdEditDialog">
        <el-form label-width="150px" :model="pwd_edit_form" ref="pwd_edit_form" :rules="rules" inline v-if="visible">
          <ss-common-area-item label="旧密码：" prop="old_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.old_pwd" @callback="setPwdOld" style="width: 270px"
              placeholder="请输入" />
          </ss-common-area-item>
          <ss-common-area-item label="新密码：" prop="new_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.new_pwd" @callback="setPwdNew" style="width: 270px"
              placeholder="请输入" />
          </ss-common-area-item>
          <ss-common-area-item label="确认新密码：" prop="confirm_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.confirm_pwd" @callback="setPwdNewC" style="width: 270px"
              placeholder="请输入" />
          </ss-common-area-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { logout, editPass, JiChengLogout } from '@/utils/axios/api'
import menu from '@/components/menu/menu'
export default {
  name: 'menuTop',
  components: { 'ss-menu': menu },
  data() {
    var validatePass = (rule, value, callback) => {
      const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度为8-16位，须包含字母、数字和特殊字符'))
      } else {
        window.user_pass = value
        callback()
      }
    }
    var validatePass1 = (rule, value, callback) => {
      const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度为8-16位，须包含字母、数字和特殊字符'))
      } else {
        if (value === window.user_pass) {
          callback()
        } else {
          callback(new Error('确认密码与新密码输入不一致'))
        }
      }
    }
    return {
      visible: false,
      pwd_edit_form: {
        old_pwd: '',
        new_pwd: '',
        confirm_pwd: ''
      },
      rules: {
        old_pwd: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
        new_pwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        confirm_pwd: [
          { required: true, message: '请输入确认新密码', trigger: 'blur' },
          { validator: validatePass1, trigger: 'blur' }
        ]
      },
      backgroundSize: '',
      msgPermission: false /* 登录用户的所属地区为接种单位-->即为门诊用户方可查看 */,
      titleStr: window.systemTitle,
      actionUrl: window.taiAnJCLogout,
      name: sessionStorage.getItem('isJC') ? sessionStorage.getItem('account') : '',
      passport: sessionStorage.getItem('isJC') ? sessionStorage.getItem('jsptPassword') : '',
      systemName: encodeURIComponent('海南入托入学查验管理系统', 'utf-8'),
      isJC: sessionStorage.getItem('isJC') || false
    }
  },
  methods: {
    setPwdOld(v) {
      this.pwd_edit_form.old_pwd = v
    },
    setPwdNew(v) {
      this.pwd_edit_form.new_pwd = v
    },
    setPwdNewC(v) {
      this.pwd_edit_form.confirm_pwd = v
    },
    showMsg() {
      if (this.$route.path === '/systemManage/notify') {
        this.$router.replace('/refresh')
      } else {
        this.$router.push('/systemManage/notify')
      }
    },
    logout() {
      if (sessionStorage.getItem('isJC') === 'true') {
        this.logoutJC()
      } else {
        this.logoutNomarl()
      }
    },
    logoutJC() {
      JiChengLogout({
        loginName: sessionStorage.getItem('account'),
        password: sessionStorage.getItem('jsptPassword'),
        systemName: encodeURIComponent('海南入托入学查验管理系统', 'utf-8')
      }).then((res) => {
        sessionStorage.removeItem('isJC')
        sessionStorage.removeItem('jsptPassword')
        if (res.code === 1) {
          window.location.replace(res.url)
        } else {
          this.logoutNomarl()
        }
      })
    },
    logoutNomarl() {
      logout()
      this.$store.commit(this.$types.LOGOUT)
      this.$router.push('/')
    },
    editPwd() {
      // this.pwd_edit_form = {};
      this.visible = true
      window.user_pass = ''
      this.pwd_edit_form = {
        old_pwd: '',
        new_pwd: '',
        confirm_pwd: ''
      }
      if (this.$refs.pwd_edit_form && this.$refs.pwd_edit_form.resetFields) {
        this.$refs.pwd_edit_form.resetFields()
      }
    },
    close() {
      this.visible = false
    },
    save() {
      let _self = this
      this.$refs.pwd_edit_form.validate((valid) => {
        if (valid) {
          if (this.pwd_edit_form.old_pwd === this.pwd_edit_form.new_pwd) {
            this.$util.errorMsg('新密码与旧密码相同，请修改新密码！')
          } else {
            let params = {
              oldPassword: this.$md5(this.pwd_edit_form.old_pwd),
              newPassword: this.$md5(this.pwd_edit_form.new_pwd),
              schId: this.$store.state.schId,
              loginName: sessionStorage.getItem('account'),
              isForce: '0'
            }
            editPass(params)
              .then((res) => {
                if (res.msg !== '修改成功！') {
                  _self.$message({
                    message: res.msg,
                    type: 'error'
                  })
                } else {
                  _self.visible = false
                  _self.$message({
                    message: res.msg || '修改密码成功!',
                    type: 'success'
                  })
                  _self.logout()
                }
              })
              .catch(() => {
                _self.closeFullScreen()
                _self.$router.push({ name: 'errorPage' })
              })
          }
        }
      })
    }
  },
  mounted() {
    this.backgroundSize = ''
    this.msgPermission =
      parseInt(sessionStorage.getItem('areaLevel')) === 4 &&
      this.$store.getters.checkModules('/systemManage/infoReception')
  }
}
</script>
<style scoped>
.header-area {
  height: 260px;
  color: #fff;
  background: url(../../assets/img/index/image-bg-left.png) left top no-repeat;
  background-color: #17229e;
  background-position: left top;
  background-size: cover;
}

.header-contain {
  padding-top: 38px;
  margin: 0 80px 0 80px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  height: 24px;
}

.header-right {
  font-size: 14px;
  display: flex;
  justify-content: flex-end;
}

.header-right .logout-area {
  cursor: pointer;
  width: 90px;
  height: 28px;
  color: #fff;
  background: url(../../assets/img/index/loginout.png) left top no-repeat;
  background-position: left top;
  background-size: contain;
  margin-left: 18px;
}

.header-right .split-line {
  border-left: 1px solid #fff;
  height: 25px;
  margin: 20px 20px 0px 20px;
}

.header-right .account-area {
  height: 28px;
  width: auto;
  background-color: rgba(12, 48, 177, .3);
  border-radius: 18px;
  display: flex;
  align-items: center;
  position: relative;
}

.header-right .account-avatar {
  height: 18px;
  width: 18px;
  margin-left: 4px;
  background: url(../../assets/img/index/user.png) no-repeat;
  background-position: left top;
  background-size: cover;
}

.header-right img.account-drop {
  height: 15px;
  width: 15px;
  margin-right: 6px;
  margin-top: 5px;
  cursor: pointer;
}

.header-right .account-text {
  color: #E9EDFF;
  font-size: 13px;
  letter-spacing: 1px;
  margin: 0 4px;
}

.account-mewmsg {
  height: 18px;
  width: 44px;
  background: url(../../assets/img/index/icon-msg.png) no-repeat;
  background-position: 2px top;
  background-size: cover;
  position: absolute;
  right: -20px;
  top: -12px;
  z-index: 1;
}

.pwdEditDialog .el-input {
  width: 250px;
}

::v-deep .pwdEditDialog .el-transfer-panel {
  border: 1px solid rgb(212, 208, 208);
}
</style>
