<template>
  <div class="login-page" :style="'margin: 0px ' + marginSide + 'px 0 ' + marginSide + 'px'" v-if="loading">
    <div class="login-left" :style="leftStyle"></div>
    <div class="login-right" :style="rightStyle">
      <div class="img"></div>
    </div>
    <div class="login-form" :style="formStyle">
      <el-form ref="form" :model="form" label-position="top" class="login-el-form">
        <div class="form-c">
          <div class="row-1">
            <span>欢迎登录</span>
            <div :style="'margin-bottom:' + 24 * HScale + 'px'"></div>
          </div>
          <el-form-item label="" prop="name" :style="'margin-bottom:' + 40 * scale * HScale + 'px;'">
            <el-input placeholder="请输入用户名" v-model.trim="form.name" />
          </el-form-item>
          <el-form-item label="" prop="pass" :style="'margin-bottom:' + 40 * scale * HScale + 'px;'">
            <ss-pwd-input v-model.trim="form.pass" @callback="setPwd" :vertical="'bottom'" />
          </el-form-item>
          <el-form-item label="" prop="valid" :style="'margin-bottom:' + 28 * scale * HScale + 'px;'">
            <div class="row-3" style="display: flex; justify-content: space-between">
              <el-input @keyup.enter.native="onSubmit()" placeholder="请输入验证码" v-model="form.valid"
                :style="'width:calc(100% - ' + 140 + 'px)'" />
              <div style="margin-top: 5px; margin-right: 6px">
                <img v-if="img_src" :src="img_src" style="height: 40px" alt="点击刷新" title="点击刷新"
                  @click="time = new Date().getTime(); cnt = 60; img_src = $store.state.baseUrl + '/user/getVerifyImg?' + time + '&params=' + $util.sm4(JSON.stringify({ jessionid: sessionid }));" />
              </div>
            </div>
          </el-form-item>
          <div class="row-2" :style="'width:100%;height:' + 77 * HScale + 'px;'"></div>
          <div class="login-btn" @click=" onSubmit()">登 录</div>
        </div>
      </el-form>
    </div>
    <div class="login-footer" :style="footerStyle">
      <div :style="'color: rgba(129, 149, 180, 1); line-height:' + 27 * scale + 'px;font-size:' + 14 * scale + 'px'">
        沈苏科技(苏州)股份有限公司研制，建议使用谷歌浏览器、360浏览器或搜狗浏览器
      </div>
    </div>
    <!-- 修改密码 -->
    <el-dialog title="修改密码" :visible.sync="visible" width="32%" :close-on-click-modal="false" :show-close="false"
      :close-on-press-escape="false">
      <div class="pwdEditDialog">
        <el-form label-width="150px" :model="pwd_edit_form" ref="pwd_edit_form" :rules="rules" inline v-if="visible">
          <ss-common-area-item label="旧密码：" prop="old_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.old_pwd" @callback="setPwdOld" style="width: 320px" />
          </ss-common-area-item>
          <ss-common-area-item label="新密码：" prop="new_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.new_pwd" @callback="setPwdNew" style="width: 320px" />
          </ss-common-area-item>
          <ss-common-area-item label="确认新密码：" prop="confirm_pwd">
            <ss-pwd-input v-model.trim="pwd_edit_form.confirm_pwd" @callback="setPwdNewC" style="width: 320px" />
          </ss-common-area-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="visible = false">取 消</el-button> -->
        <el-button type="primary" @click="pwdUpdate">保 存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { login, getSessionid, JiChengloginIn, editPass } from '../utils/axios/api'
import { modules } from '@/plugins/menu'
export default {
  name: 'login',
  components: {},
  data() {
    var validatePass = (rule, value, callback) => {
      const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度为8-16位，须包含数字、大小写字母和特殊字符'))
      } else {
        window.user_pass = value
        callback()
      }
    }
    var validatePass1 = (rule, value, callback) => {
      const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度为8-16位，须包含数字、大小写字母和特殊字符'))
      } else {
        if (value === window.user_pass) {
          callback()
        } else {
          callback(new Error('确认密码与新密码输入不一致'))
        }
      }
    }
    return {
      loading: false,
      img_src: '',
      sessionid: '',
      scale: window.scale,
      marginSide: window.scale >= 1 && (!window.firefox || window.winWidth > 1440) ? window.winWidth * 0.05 : 0,
      isAble: false,
      cnt: 60,
      form: {
        name: '',
        pass: '',
        valid: ''
      },
      time: new Date().getTime(),
      leftStyle: '',
      rightStyle: '',
      formStyle: '',
      footerStyle: '',
      HScale: window.winHeight / 1080,
      pwd_edit_form: {
        old_pwd: '',
        new_pwd: '',
        confirm_pwd: ''
      },
      visible: false,
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
      }
    }
  },
  methods: {
    setPwd(val) {
      this.form.pass = val
    },
    setPwdOld(v) {
      this.pwd_edit_form.old_pwd = v
    },
    setPwdNew(v) {
      this.pwd_edit_form.new_pwd = v
    },
    setPwdNewC(v) {
      this.pwd_edit_form.confirm_pwd = v
    },
    pwdEdit(data) {
      let rt = false
      if (data.forcePwd === '1') {
        this.visible = true
        this.pwd_edit_form = {
          old_pwd: '',
          new_pwd: '',
          confirm_pwd: ''
        }
        if (this.$refs.pwd_edit_form && this.$refs.pwd_edit_form.resetFields) {
          this.$refs.pwd_edit_form.resetFields()
        }
        rt = true
      }
      if (data.forceMsg) {
        this.$util.warningMsg(data.forceMsg)
      }
      return rt
    },
    pwdUpdate() {
      let _self = this
      this.$refs.pwd_edit_form.validate((valid) => {
        if (valid) {
          if (this.pwd_edit_form.old_pwd === this.pwd_edit_form.new_pwd) {
            this.$util.errorMsg('新密码与旧密码相同，请修改新密码！')
          } else {
            let params = {
              oldPassword: this.$md5(this.pwd_edit_form.old_pwd),
              newPassword: this.$md5(this.pwd_edit_form.new_pwd),
              loginName: sessionStorage.getItem('account'),
              modelName: '登录',
              isForce: '1',
              operateType: this.$consts.OPTYPE_UPDATE,
            }
            editPass(params)
              .then((res) => {
                if (res.msg == '修改成功！') {
                  _self.visible = false
                  _self.$message({
                    message: '修改密码成功，请重新登录！',
                    type: 'success'
                  })
                  _self.$router.replace('/refresh')
                } else {
                  _self.$message({
                    message: res.msg,
                    type: 'error'
                  })
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
    loginFun(params, isJC, jsptPassword) {
      let that = this
      console.log('登录信息', params, jsptPassword)
      let param = { jessionid: that.sessionid }
      let apiMethond = isJC === true ? JiChengloginIn : login
      apiMethond(params)
        .then((response) => {
          console.log('login-res', response)
          that.$store.commit(that.$types.LOGIN, response)
          if (isJC === true) {
            that.$store.commit(that.$types.ACCOUNT, response.userinfo.loginName)
            sessionStorage.setItem('jsptPassword', jsptPassword)
            that.closeFullScreen()
          } else {
            that.$store.commit(that.$types.ACCOUNT, response.userinfo.loginName || that.form.name)
          }
          that.$store.commit(that.$types.USERNAME, response)
          that.$store.dispatch(that.$types.SET_USERAREA_ACTION, modules)
          that.loading = true
          sessionStorage.setItem('isJC', isJC)
          // if (!that.pwdEdit(response.userinfo)) {
            // 清除权限按钮缓存
            that.$store.commit(that.$types.RESET_PAGEBTN)
            // 清除权限按钮缓存

            document.getElementById('app').scrollTop = 0
            that.$router.push({ path: '/index' })
          // }
        })
        .catch((res) => {
          that.time = new Date().getTime()
          that.form.valid = ''
          that.cnt = 60
          that.img_src =
            that.$store.state.baseUrl +
            '/user/getVerifyImg?' +
            that.time +
            '&params=' +
            that.$util.sm4(JSON.stringify(param))
          if (isJC === true) {
            that.closeFullScreen()
          }
          that.loading = true
        })
    },
    onSubmit() {
      let that = this
      if (that.isAble) return false
      if (event && event.keyCode === 13) return false
      if (!that.form.valid || that.form.valid.length !== 4) {
        that.isAble = false
        that.$util.errorMsg('验证码为4位数！')
        return
      }
      that.isAble = true
      that.$refs['form'].validate((valid) => {
        if (valid) {
          let params = {
            userName: that.form.name,
            password: that.form.pass ? this.$md5(that.form.pass) : '',
            validCode: that.form.valid,
            operateType: that.$consts.OPTYPE_LOGIN,
          }
          that.isAble = false
          console.log('login-params', params)
          this.loginFun(params, false)
        }
      })
    },
    getSyncStyle() {
      let scale = this.scale
      let HScale = window.winHeight / 1080
      this.leftStyle = `left:${(158 - this.marginSide) * scale}px;
                        top:${160 * scale * HScale}px;
                        width:${882 * scale * HScale}px;
                        height:${690 * scale * HScale}px;`
      this.rightStyle = `top:${378 * scale * HScale}px;
                        right:${window.winWidth >= 1920 ? HScale < 1 ? 160 : 30 : this.marginSide ? this.marginSide + this.marginSide * scale : 160}px;`
      this.formStyle = `margin-top:${234 * scale * HScale > 138 ? 234 * scale * HScale : 138}px;
                        width:${[717 * scale - this.marginSide] * scale}px;
                        left:${882 * scale * HScale < 730 ? 800 : 882 * scale * HScale + (158 - this.marginSide) * scale}px;`
      this.footerStyle = `width:100%; bottom:${137 * scale * HScale}px`
    }
  },
  mounted() {
    this.getSyncStyle()
    let that = this
    that.$parent.createScrollbar()
    getSessionid()
      .then((res) => {
        that.img_show = true
        that.cnt = 60
        that.time = new Date().getTime()
        that.sessionid = sessionStorage.getItem('sessionid')
        let params = { jessionid: that.sessionid }
        that.img_src =
          that.$store.state.baseUrl +
          '/user/getVerifyImg?' +
          that.time +
          '&params=' +
          that.$util.sm4(JSON.stringify(params))
        setInterval(function () {
          that.cnt--
          if (that.cnt <= 0) {
            that.time = new Date().getTime()
            that.cnt = 60
            that.img_src =
              that.$store.state.baseUrl +
              '/user/getVerifyImg?' +
              that.time +
              '&params=' +
              that.$util.sm4(JSON.stringify(params))
          }
        }, 1000)
      })
      .catch((res) => {
        if (that.sessionid) {
          let params = { jessionid: that.sessionid }
          that.img_src =
            that.$store.state.baseUrl +
            '/user/getVerifyImg?' +
            that.time +
            '&params=' +
            that.$util.sm4(JSON.stringify(params))
        }
      })
  },
  created() {
    // 集成平台跳转
    if (this.$route.query.params) {
      this.openFullScreen('加载中...', 'rgba(255, 255, 255, 1)')
      let loginParams = JSON.parse(this.$util.decodeAes(this.$route.query.params))
      let params = {
        userName: loginParams.loginName,
        password: loginParams.password
      }
      this.loginFun(params, true, loginParams.jsptPassword)
    } else {
      this.loading = true
    }
  }
}
</script>

<style scoped lang="less">
.login-page {
  height: 100%;
  padding: 0;
  position: relative;
}

.login-left {
  background: url(../assets/img/login/img-01.png) left top no-repeat;
  background-size: contain;
  background-position: left top;
  position: absolute;
  min-width: 730px;
  min-height: 572px;
}

.login-right {
  margin: 0 auto;
  position: absolute;
  width: 170px;
  height: 451px;

  .img {
    background: url(../assets/img/login/img-03.png) right bottom no-repeat;
    background-size: contain;
    background-position: right bottom;
    position: absolute;
    bottom: 76px;
    width: 163px;
    height: 131px;
  }
}

.login-btn {
  background-color: #1e6ce4;
  background-size: contain;
  background-position: center bottom;
  min-width: 402px;
  height: 55px;
  cursor: pointer;
  border-radius: 10px;
  margin-top: -3px;
  color: #fff;
  font-size: 23px;
  line-height: 56px;
}

.login-form {
  position: absolute;
  overflow: hidden;
  text-align: center;
  min-width: 650px;
}

.login-el-form {
  .form-c {
    background: transparent;
    width: calc(100% - 242px);
    padding: 0 110px 0 132px;
  }

  .row-1 {
    text-align: left;
    clear: both;
    height: 65px;

    span {
      font-weight: 700;
      color: #000000;
      letter-spacing: 8px;
      line-height: 65px;
      margin-bottom: 27px;
      display: inline-block;
      float: left;
      font-size: 42px;
      padding-right: 10px;
    }

    div {
      float: left;
      background: url(../assets/img/login/img-04.png) no-repeat;
      background-size: 100% 100%;
      margin-top: 16px;
      width: 70px;
      height: 61px;
    }
  }

  .row-2 {
    background: url(../assets/img/login/img-02.png) no-repeat left;
    background-size: contain;
    margin-top: 0;
  }
}

.login-title {
  font-size: 33px;
  font-weight: bolder;
  letter-spacing: 3px;
  text-align: center;
}

.login-footer {
  position: absolute;
  text-align: center;
  font-size: 12px;
}
</style>
<style>
.login-form .el-input__inner {
  background-color: rgba(255, 255, 255, 1) !important;
  border: 1px solid rgba(204, 221, 246, 1) !important;
  border-radius: 5px !important;
  height: 55px !important;
}

.login-form .el-input__inner::placeholder {
  color: rgba(129, 149, 180, 1);
  letter-spacing: 2px !important;
  font-size: 16px;
}
</style>
