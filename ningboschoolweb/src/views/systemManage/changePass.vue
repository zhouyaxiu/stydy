<template>
  <!-- 修改密码 -->
  <div>
    <ss-condition-area-new definedTitle="修改密码" ref="conditionPage" @setConditionFlag="setConditionFlag">
      <div class="condition-row changepass">
        <el-form :model="pwd_edit_form" ref="pwd_edit_form" :rules="rules" label-position="right" label-width="200px">
          <div class="condition-row">
            <el-form-item prop="old_pwd">
              <div class="label" slot="label">旧密码：</div>
              <el-input v-model.trim="pwd_edit_form.old_pwd" :type="open ? 'password' : 'text'" placeholder="请输入">
                <i slot="suffix" class="el-input__icon ss-icon-view-middle" v-show="open && pwd_edit_form.old_pwd"
                  style="cursor: pointer" @click="open = !open"></i>
                <i slot="suffix" class="el-input__icon ss-icon-unview-middle" v-show="!open && pwd_edit_form.old_pwd"
                  style="cursor: pointer" @click="open = !open"></i>
              </el-input>
            </el-form-item>
            <!-- <el-form-item prop="new_pwd">
              <div class="label" slot="label">新密码：</div>
              <el-input
                v-model.trim="pwd_edit_form.new_pwd"
                :type="open1 ? 'password' : 'text'"
                placeholder="请输入"
              >
              <i
                slot="suffix"
                class="el-input__icon ss-icon-view-middle"
                v-show="open1 && pwd_edit_form.new_pwd"
                style="cursor: pointer"
                @click="open1 = !open1"
              ></i>
              <i
                slot="suffix"
                class="el-input__icon ss-icon-unview-middle"
                v-show="!open1 && pwd_edit_form.new_pwd"
                style="cursor: pointer"
                @click="open1 = !open1"
              ></i>
            </el-input>
            </el-form-item>
            <el-form-item prop="confirm_pwd">
              <div class="label" slot="label">确认新密码：</div>
              <el-input
                v-model.trim="pwd_edit_form.confirm_pwd"
                :type="open2 ? 'password' : 'text'"
                placeholder="请输入"
              >
              <i
                slot="suffix"
                class="el-input__icon ss-icon-view-middle"
                v-show="open2 && pwd_edit_form.confirm_pwd"
                style="cursor: pointer"
                @click="open2 = !open2"
              ></i>
              <i
                slot="suffix"
                class="el-input__icon ss-icon-unview-middle"
                v-show="!open2 && pwd_edit_form.confirm_pwd"
                style="cursor: pointer"
                @click="open2 = !open2"
              ></i>
            </el-input>
            </el-form-item> -->
          </div>
          <div class="condition-row">
            <!-- <el-form-item prop="old_pwd">
              <div class="label" slot="label">旧密码：</div>
              <el-input
                v-model.trim="pwd_edit_form.old_pwd"
                :type="open ? 'password' : 'text'"
                placeholder="请输入"
              >
              <i
                slot="suffix"
                class="el-input__icon ss-icon-view-middle"
                v-show="open && pwd_edit_form.old_pwd"
                style="cursor: pointer"
                @click="open = !open"
              ></i>
              <i
                slot="suffix"
                class="el-input__icon ss-icon-unview-middle"
                v-show="!open && pwd_edit_form.old_pwd"
                style="cursor: pointer"
                @click="open = !open"
              ></i>
            </el-input>
            </el-form-item> -->
            <el-form-item prop="new_pwd">
              <div class="label" slot="label">新密码：</div>
              <el-input v-model.trim="pwd_edit_form.new_pwd" :type="open1 ? 'password' : 'text'" placeholder="请输入">
                <i slot="suffix" class="el-input__icon ss-icon-view-middle" v-show="open1 && pwd_edit_form.new_pwd"
                  style="cursor: pointer" @click="open1 = !open1"></i>
                <i slot="suffix" class="el-input__icon ss-icon-unview-middle" v-show="!open1 && pwd_edit_form.new_pwd"
                  style="cursor: pointer" @click="open1 = !open1"></i>
              </el-input>
            </el-form-item>
            <!-- <el-form-item prop="confirm_pwd">
              <div class="label" slot="label">确认新密码：</div>
              <el-input
                v-model.trim="pwd_edit_form.confirm_pwd"
                :type="open2 ? 'password' : 'text'"
                placeholder="请输入"
              >
              <i
                slot="suffix"
                class="el-input__icon ss-icon-view-middle"
                v-show="open2 && pwd_edit_form.confirm_pwd"
                style="cursor: pointer"
                @click="open2 = !open2"
              ></i>
              <i
                slot="suffix"
                class="el-input__icon ss-icon-unview-middle"
                v-show="!open2 && pwd_edit_form.confirm_pwd"
                style="cursor: pointer"
                @click="open2 = !open2"
              ></i>
            </el-input>
            </el-form-item> -->
          </div>
          <div class="condition-row">
            <!-- <el-form-item prop="old_pwd">
              <div class="label" slot="label">旧密码：</div>
              <el-input
                v-model.trim="pwd_edit_form.old_pwd"
                :type="open ? 'password' : 'text'"
                placeholder="请输入"
              >
              <i
                slot="suffix"
                class="el-input__icon ss-icon-view-middle"
                v-show="open && pwd_edit_form.old_pwd"
                style="cursor: pointer"
                @click="open = !open"
              ></i>
              <i
                slot="suffix"
                class="el-input__icon ss-icon-unview-middle"
                v-show="!open && pwd_edit_form.old_pwd"
                style="cursor: pointer"
                @click="open = !open"
              ></i>
            </el-input>
            </el-form-item>
            <el-form-item prop="new_pwd">
              <div class="label" slot="label">新密码：</div>
              <el-input
                v-model.trim="pwd_edit_form.new_pwd"
                :type="open1 ? 'password' : 'text'"
                placeholder="请输入"
              >
              <i
                slot="suffix"
                class="el-input__icon ss-icon-view-middle"
                v-show="open1 && pwd_edit_form.new_pwd"
                style="cursor: pointer"
                @click="open1 = !open1"
              ></i>
              <i
                slot="suffix"
                class="el-input__icon ss-icon-unview-middle"
                v-show="!open1 && pwd_edit_form.new_pwd"
                style="cursor: pointer"
                @click="open1 = !open1"
              ></i>
            </el-input>
            </el-form-item> -->
            <el-form-item prop="confirm_pwd">
              <div class="label" slot="label">确认新密码：</div>
              <el-input v-model.trim="pwd_edit_form.confirm_pwd" :type="open2 ? 'password' : 'text'" placeholder="请输入">
                <i slot="suffix" class="el-input__icon ss-icon-view-middle" v-show="open2 && pwd_edit_form.confirm_pwd"
                  style="cursor: pointer" @click="open2 = !open2"></i>
                <i slot="suffix" class="el-input__icon ss-icon-unview-middle" v-show="!open2 && pwd_edit_form.confirm_pwd"
                  style="cursor: pointer" @click="open2 = !open2"></i>
              </el-input>
            </el-form-item>
          </div>
          <div class="condition-row mt_10" style="margin-top:5px;margin-left:130px;">
            <div class="item">
              <el-button @click="save" :type="$consts.CUSTOMBUTTONTYPES.query">保存</el-button>
              <el-button @click="resets" :type="$consts.CUSTOMBUTTONTYPES.reset">取消</el-button>
              <div class="note">备注：① 新密码长度为8-16位；② 新密码必须同时包含数字、大写字母、小写字母、英文特殊字符；③ 字母：a-z A-Z，数字：0-9，特殊字符! @ # $</div>
            </div>
          </div>
        </el-form>
      </div>
    </ss-condition-area-new>
  </div>
</template>
<script>
import { editPass } from '@/utils/axios/api'
export default {
  name: 'vaccinationInspection',
  data() {
    var validatePass = (rule, value, callback) => {
      const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度为8-16位，须包含数字、大写字母、小写字母数字和特殊字符'))
      } else {
        window.user_pass = value
        callback()
      }
    }
    var validatePass1 = (rule, value, callback) => {
      const reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$_\\-])[a-zA-Z0-9!@#$\\-]{8,16}$/
      if (!reg.test(value)) {
        callback(new Error('密码长度为8-16位，须包含数字、大写字母、小写字母和特殊字符'))
      } else {
        if (value === window.user_pass) {
          callback()
        } else {
          callback(new Error('确认密码与新密码输入不一致'))
        }
      }
    }
    return {
      pwd_edit_form: {
        old_pwd: '',
        new_pwd: '',
        confirm_pwd: ''
      },
      open: true,
      open1: true,
      open2: true,
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
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询接口
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
              isForce: '0',
              operateType: this.$consts.OPTYPE_UPDATE,
            }
            editPass(params)
              .then((res) => {
                if (res.msg != '修改成功！') {
                  _self.$message({
                    message: res.msg,
                    type: 'error'
                  })
                } else {
                  _self.$message({
                    message: res.msg || '修改密码成功!',
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
    resets() {
      this.$refs.pwd_edit_form.resetFields()
    },
  },
}
</script>
<style scoped>
.condition-area-new .condition-row .el-button {
  min-width: 82px;
  min-height: 34px;
  background: #2E5BFF;
  color: #fff;
  padding: 0;
  border-radius: 5px;
}

.condition-area-new .condition-row .el-button.el-button--reset {
  background: #fff;
  border: 1px solid #2E5BFF;
  color: #2E5BFF;
}

.condition-area-new .condition-row .note {
  white-space: nowrap;
  padding-left: 20px;
  color: #6878B2;
}

.ss-icon-view-middle::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_view.png);
  vertical-align: middle;
}

.ss-icon-unview-middle::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_un_view.png);
  vertical-align: middle;
}

.ss-icon-view-bottom::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_view.png);
  vertical-align: bottom;
}

.ss-icon-unview-bottom::after {
  position: relative;
  width: 26px;
  height: 26px;
  background-image: url(../../assets/img/icon/icon_un_view.png);
  vertical-align: bottom;
}
</style>
<style>
.changepass {
  margin: 80px 200px 100px !important;
}

.changepass .el-form-item__content {
  margin-left: 0 !important;
}

.changepass .condition-row .el-form-item {
  margin-bottom: 20px !important;
}

.changepass .el-input:not(.el-date-editor) input {
  width: 300px !important;
}
</style>