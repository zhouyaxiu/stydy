<template>
  <div class="header-area" :style="backgroundSize">
    <div class="header-left">
      <div class="header-text">{{ titleStr }}</div>
    </div>
    <div class="header-right">
      <div class="logout-area" @click="showMsg" v-show="msgPermission">
        <i class="el-icon-bell" style="font-size: 20px"></i>
      </div>
      <!-- 不显示退出系统 -->
      <div class="logout-area">
        <el-dropdown trigger="click" style="color: #fff">
          <span class="el-dropdown-link">
            更多<i class="el-icon-caret-bottom el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <!-- <el-dropdown-item class="clearfix">
              <div @click="editPwd()">修改密码</div>
            </el-dropdown-item> -->
            <el-dropdown-item class="clearfix">
              <div @click="logout()">退出系统</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div class="split-line"></div>
      <div class="account-area">
        <div class="account-text" :title="userName || $store.state.userName">
          {{ userName || $store.state.userName }}
        </div>
        <div class="account-text">
          <span>{{ areaname || $store.state.areaname }}</span>
          <span style="margin-left: 10px">{{ areacode || $store.state.areacode }}</span>
        </div>
      </div>
    </div>
    <el-dialog title="修改密码" :visible.sync="visible" width="30%" :before-close="close" :close-on-click-modal="false">
      <div class="pwdEditDialog">
        <el-form label-width="150px" :model="pwd_edit_form" ref="pwd_edit_form" :rules="rules" inline v-if="visible">
          <ss-common-area-item label="旧密码：" prop="old_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.old_pwd" @callback="setPwdOld" style="width: 270px" />
          </ss-common-area-item>
          <ss-common-area-item label="新密码：" prop="new_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.new_pwd" @callback="setPwdNew" style="width: 270px" />
          </ss-common-area-item>
          <ss-common-area-item label="确认新密码：" prop="confirm_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.confirm_pwd" @callback="setPwdNewC" style="width: 270px" />
          </ss-common-area-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
    </el-dialog>
    <!-- 集成平台退出登录 -->
    <!-- <form :action="actionUrl" method="post" name="form" id="fromRef">
      <table>
        <tr>
          <td>loginName:</td>
          <td><input type="text" name="loginName" :value="name"></td>
        </tr>
        <tr>
          <td>password:</td>
          <td><input type="text" name="password" :value="passport"></td>
        </tr>
        <tr>
          <td>systemName:</td>
          <td><input type="text" name="systemName" :value="systemName"></td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td><input type="submit" name="Submit" value="home"></td>
        </tr>
      </table>
    </form> -->
  </div>
</template>
<script>
import { logout, editPass, JiChengLogout } from '@/utils/axios/api'
export default {
  name: 'menuTop',
  components: {},
  data() {
    var validatePass = (rule, value, callback) => {
      const reg =
        /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])[\da-zA-Z!@#$%^&*()]{8,}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度至少8位，须包含字母、数字和特殊字符'))
      } else {
        window.user_pass = value
        callback()
      }
    }
    var validatePass1 = (rule, value, callback) => {
      const reg =
        /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])[\da-zA-Z!@#$%^&*()]{8,}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度至少8位，须包含字母、数字和特殊字符'))
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
      // actionUrl: window.taiAnJCLogout,
      name: sessionStorage.getItem('isJC') ? sessionStorage.getItem('account') : '',
      passport: sessionStorage.getItem('isJC') ? sessionStorage.getItem('jsptPassword') : '',
      systemName: encodeURIComponent('入园入学查验系统', 'utf-8'),
      isJC: sessionStorage.getItem('isJC') || false,
      userName:sessionStorage.getItem('isJC') ?sessionStorage.getItem('userName') : '',
      areacode:sessionStorage.getItem('isJC') ?sessionStorage.getItem('areacode') : '',
      areaname:sessionStorage.getItem('isJC') ?sessionStorage.getItem('areaname') : ''
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
      if (this.$route.path === '/systemManage/infoReception') {
        this.$router.replace('/refresh')
      } else {
        this.$router.push('/systemManage/infoReception')
      }
    },
    logout() {
      this.logoutNomarl()
      // if (sessionStorage.getItem('isJC') === 'true') {
      //   this.logoutJC()
      // } else {
      //   this.logoutNomarl()
      // }
    },
    logoutJC() {
      JiChengLogout({
        loginName: sessionStorage.getItem('account'),
        password: sessionStorage.getItem('jsptPassword'),
        systemName: encodeURIComponent('海南省预防接种信息系统', 'utf-8')
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
              loginName: sessionStorage.getItem('account')
            }
            // params.confirm_pwd = this.$util.sm4(params.confirm_pwd);
            // "userId","newPasswd","loginname"
            editPass(params)
              .then((res) => {
                if (res.msg) {
                  _self.$message({
                    message: res.msg,
                    type: 'error'
                  })
                } else {
                  _self.visible = false
                  _self.$message({
                    message: '修改密码成功!',
                    type: 'success'
                  })
                  _self.$store.commit(this.$types.LOGOUT)
                  _self.$router.push('/')
                  _self.closeFullScreen()
                }
              })
              .catch(() => {
                _self.closeFullScreen()
                _self.$router.push({ name: 'errorPage' })
              })
          }
        }
      })
    },
    gotoJCsystem() {
      if (sessionStorage.getItem('modelObject') == '1') {
        //  1：门诊端
        window.location.href = window.JCMZsystem
      } else {
        // 0：疾控端
        window.location.href = window.JCJkSystem
      }
    }
  },
  mounted() {
    this.backgroundSize = ''
    this.msgPermission =
      parseInt(sessionStorage.getItem('areaLevel')) === 4 &&
      this.$store.getters.checkModules('/systemManage/infoReception')
    this.userName=sessionStorage.getItem('isJC') ?sessionStorage.getItem('userName') : '',
    this.areacode=sessionStorage.getItem('isJC') ?sessionStorage.getItem('areacode') : '',
    this.areaname=sessionStorage.getItem('isJC') ?sessionStorage.getItem('areaname') : ''
  }
}
</script>
<style scoped>
.header-area {
  height: 64px;
  /* background: #0b2da8; */
  color: #fff;
  background: url(../../assets/img/index/image-bg-left.png) left top no-repeat;
  background-color: #17229e;
  /* background-repeat: repeat-x; */
  background-position: left top;
  background-size: contain;
  /* background-size: 100% 125%; */
}

.header-left {
  /* background: url(../../assets/img/index/image-bg-left.png) left top no-repeat; */
  background-size: contain;
  background-position: left top;
  min-width: 940px;
  min-height: 64px;
  position: absolute;
  z-index: 2;
  left: 0px;
}

.header-text {
  font-weight: bolder;
  font-size: 28px;
  line-height: 64px;
  padding-left: 70px;
  letter-spacing: 4px;
}

.header-right {
  font-size: 14px;
  display: flex;
  justify-content: flex-end;
  background: url(../../assets/img/index/image-bg-right.png) right top no-repeat;
  background-size: contain;
  background-position: right top;
}

.header-right .logout-area {
  cursor: pointer;
  line-height: 64px;
  /* cursor: pointer;
  width: 90px;
  height: 28px;
  color: #fff;
  background: url(../../assets/img/index/loginout.png) left top no-repeat;
  background-position: left top;
  background-size: contain;
  margin-left: 18px; */
}

.header-right .split-line {
  border-left: 1px solid #fff;
  height: 25px;
  margin: 20px 20px 0px 20px;
}

.header-right .account-area {
  line-height: 20px;
  margin-top: 11px;
  margin-right: 26px;
}

.header-right .account-text {
  display: flex;
  justify-content: flex-end;
}

.pwdEditDialog .el-input {
  width: 250px;
}

::v-deep .pwdEditDialog .el-transfer-panel {
  border: 1px solid rgb(212, 208, 208);
}

.mr_30 {
  margin-right: 30px;
}

.f16 {
  font-size: 16px;
  font-weight: bold;
}
</style>
