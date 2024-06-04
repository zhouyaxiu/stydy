<!-- 手动添加 -->
<template>
  <div class="manualInputForm">
    <ss-condition-area-new ref="conditionPage" @setConditionFlag="setConditionFlag">
      <el-form label-width="195px" :model="manualInputForm" ref="manualInputFormRef" :rules="rules" inline
        class="manual-form">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学年：" prop="year">
              <el-select v-model="manualInputForm.year" placeholder="请选择学年" clearable>
                <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年级：" prop="grade">
              <el-select clearable v-model="manualInputForm.grade" placeholder="请选择年级">
                <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="轮次：" prop="round">
              <el-select v-model="manualInputForm.round" placeholder="请选择轮次" clearable>
                <el-option label="春季" value="1" />
                <el-option label="秋季" value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级：" prop="className">
              <el-input v-model.trim="manualInputForm.className" placeholder="请输入班级，比如：1班" maxlength="10"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生姓名：" prop="stuName">
              <el-input clearable maxlength="10" placeholder="请输入" v-model.trim="manualInputForm.stuName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期：" prop="stuBirth">
              <el-date-picker v-model="manualInputForm.stuBirth" :picker-options="startTimeOption" type="date"
                placeholder="选择日期" @change="$forceUpdate()" value-format="yyyy-MM-dd" format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别：" prop="stuSex">
              <el-select v-model="manualInputForm.stuSex">
                <el-option label="男" value="男">男</el-option>
                <el-option label="女" value="女">女</el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="移动电话：" prop="stuPhone">
              <el-input clearable maxlength="26" placeholder="请输入" v-model.trim="manualInputForm.stuPhone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证件号码：" prop="cid">
              <el-input clearable maxlength="18" placeholder="请输入" v-model.trim="manualInputForm.cid"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="父母姓名（父亲或者母亲）：" prop="stuParentname">
              <el-input clearable maxlength="18" placeholder="请输入"
                v-model.trim="manualInputForm.stuParentname"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="button-footer">
        <el-button class="mr_10" type="default" @click="cancel" size="small">取 消</el-button>
        <el-button class="mr_10" style="margin-left: 0" type="primary" @click="save" size="small">保 存</el-button>
        <ss-custom-button @click="printBefore" :type="$consts.CUSTOMBUTTONTYPES.buzheng" class="print" />
      </div>
    </ss-condition-area-new>

    <print-noice :visible="dialogVisible" @close="closeModal" :noiceList="noiceList" title="预防接种证查验疫苗补证联系通知单" />
  </div>
</template>
<script>
import { queryNoticeMess } from "@/utils/axios/apis/notice/index";
import { manualEntry,ifNotHasCard } from "@/utils/axios/apis/manualEntry/index";
import moment from "moment";
import commonOptions from "@/mixin/commonOptions";
import printNoice from "./components/printNoice.vue";
export default {
  mixins: [commonOptions],
  components: { printNoice },
  name: "manualAddition",
  data() {
    let validatePhoneNum = (rule, value, callback) => {
      console.log(value);
      const reg =
        /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
      const telephoneRegex =
        /^0\d{2,3}-\d{7,8}|\(?0\d{2,3}[)-]?\d{7,8}|\(?0\d{2,3}[)-]*\d{7,8}/;
      if (!reg.test(value) && !telephoneRegex.test(value)) {
        callback(new Error("电话号格式不正确"));
      }
      callback();
    };
    let validateSid = (rule, value, callback) => {
      const reg =
      /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9X]$/;
      if (!reg.test(value)) {
        callback(new Error("身份证件号码格式不正确"));
      }
      callback();
    };
    let validatorClassName = (rule, value, callback) => {
      const reg = /^[0-9A-Za-z\u4e00-\u9fa5]+$/;
      if (!reg.test(value)) {
        callback(new Error("班级只能是中文，英文，数字!"));
      }
      callback();
    };
    return {
      startTimeOption: {
        disabledDate: (time) => {
          return (
            time.getTime() > new Date(this.$dateutil.dayAddNum()).getTime()
          );
        },
      },
      gradeList: this.$util.getGrade(2),
      areaCodes: "",
      areaName: "",
      selectTypeList: [
        {
          name: "幼托机构",
          code: "0",
        },
        {
          name: "小学",
          code: "1",
        },
      ],
      manualInputForm: {
        year: this.$dateutil.currentYear(),
        grade: "",
        schId: this.$store.state.schId,
        schType: "",
        round: "",
        className: "",
        stuName: "",
        stuBirth: "",
        stuSex: "",
        stuPhone: "",
        cid: "",
        stuParentname: "",
      },
      dialogVisible: false,
      mainInfo: {},
      noiceList: [],
      schstationCode: this.$store.state.schStationcode,
      schoolName: this.$store.state.schName,
      schStationName: this.$store.state.schStationname,
      rules: {
        year: [{ required: true, message: "请选择学年", trigger: "change" }],
        grade: [{ required: true, message: "请选择年级", trigger: "change" }],
        round: [{ required: true, message: "请选择轮次", trigger: "change" }],
        className: [
          { required: true, message: "请输入班级", trigger: "blur" },
          { validator: validatorClassName, trigger: "blur" },
        ],
        stuName: [
          { required: true, message: "请输入学生姓名", trigger: "blur" },
        ],
        stuBirth: [
          { required: true, message: "请选择出生日期", trigger: "change" },
        ],
        stuSex: [{ required: true, message: "请选择性别", trigger: "change" }],
        stuPhone: [
          { required: true, message: "请输入移动电话", trigger: "blur" },
          { validator: validatePhoneNum, trigger: "blur" },
        ],
        cid: [
          { required: true, message: "请输入身份证件号码", trigger: "blur" },
          { validator: validateSid, trigger: "blur" },
        ],
        stuParentname: [
          { required: true, message: "请输入父母姓名", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight();
    },
    closeModal() {
      this.dialogVisible = false;
    },
    // 保存
    save() {
      const data = {
        ...this.manualInputForm,
        stuBirth: moment(this.manualInputForm.stuBirth).format("YYYY-MM-DD"),
        schstationCode: this.schstationCode,
      };
      console.log("save-add", data);
      this.$refs.manualInputFormRef.validate().then((flag) => {
        if (flag) {
          this.openFullScreen();
          // 新增
          manualEntry({
            ...data,
            operateType: this.$consts.OPTYPE_INSERT,
            // cModId: this.$store.state.moduleId
          })
            .then((res) => {
              if (res.message == "添加成功！") {
                this.$message({
                  message: res.message,
                  type: "success",
                });
                this.cancel();
              } else {
                this.$message({
                  message: res.message,
                  type: "error",
                });
              }
              this.closeFullScreen();
            })
            .catch(() => {
              this.closeFullScreen();
            });
        }
      });
    },
    cancel() {
      this.$refs.manualInputFormRef.resetFields();
    },
    printBefore() {
      this.$refs.manualInputFormRef.validate().then((flag) => {
        if (flag) {
          ifNotHasCard({cid:this.manualInputForm.cid}).then(res => {
            if(res.data==0){
              this.mainInfo = {
                title: "补证通知单",
                maintype: 1,
                ...this.manualInputForm,
                stuBirth: moment(this.manualInputForm.stuBirth).format(
                  "YYYY-MM-DD"
                ),
                schoolName: this.schoolName,
                gradeName: this.gradeList.filter(
                  (item) => item.code == this.manualInputForm.grade
                )[0].name,
                schStationName: this.schStationName,
                className: this.manualInputForm.className,
              };
              let params = {
                noticeType: 1,
                schId: this.mainInfo.schId,
              };
              queryNoticeMess(params).then((res) => {
                this.mainInfo = {
                  ...this.mainInfo,
                  noticeContent: res.data[0].noticeContent,
                };
                this.noiceList = [
                  {
                    ...this.mainInfo,
                  },
                ];
                this.dialogVisible = true;
              });
            }else if(res.data==-1){
              return this.$util.errorMsg('信息系统中该学生处于踢卡状态，无需打印补证通知单！')
            }else if(res.data==1){
              return this.$util.errorMsg('该受种者有证，无需打印补证通知单！')
            }
          })
        }
      });
    },
  },
  async mounted() {
    await this.findClaYear();
    await this.findClaLevelNew();
  },
};
</script>
<style lang="less">
.manualInputForm .el-input {
  width: 280px;
}

.manualInputForm :not(.el-transfer) .el-button--primary,
:not(.el-transfer) .el-button--primary:focus,
:not(.el-transfer) .el-button--primary:hover {
  height: 34px !important;
}

.manualInputForm :not(.el-transfer) .el-button--default,
:not(.el-transfer) .el-button--default:focus,
:not(.el-transfer) .el-button--default:hover {
  height: 34px !important;
}

::v-deep .manualInputForm .el-transfer-panel {
  border: 1px solid rgb(212, 208, 208);
}

.batch.manual .el-form-item {
  margin-bottom: 10px;
  margin-left: -20px;
}

.manualInputForm .condition-area-new .el-form-item__content {
  display: inline-block;
}

.manualInputForm .manual-form {
  width: 80%;
  margin: 0 auto;
  margin-top: 60px;
}

.button-footer {
  display: flex;
  margin-left: 320px;
  margin-bottom: 40px;

  .mr_10 {
    margin-right: 10px;
  }
}
</style>
