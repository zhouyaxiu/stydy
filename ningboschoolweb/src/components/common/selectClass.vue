<template>
  <!-- 年份、轮次、年级、班级联动选择组件 -->
  <!-- schoolnameRequired学校名称必填   schoolname学校名称显示 -->
  <!-- classRequired班级必填   class班级显示 -->
  <!-- grade班级显示 -->
  <div class="condition-row newselect ml_200">
    <div class="item">
      <div class="label"><span style="color: red; margin-right: 6px;">*</span>学年：</div>
      <el-select clearable v-model="unitManageForm.stuclayear" placeholder="请选择">
        <el-option v-for="item in schoolYearList" :key="item" :label="item" :value="item" />
      </el-select>
    </div>
    <div class="item" v-if="stuClalevel">
      <div class="label">
        <span style="color: red; margin-right: 6px;" v-if="isRequired">*</span>
        年级：
      </div>
      <el-select clearable v-model="unitManageForm.stuClalevel" placeholder="请选择" @change="stuClalevelChange">
        <el-option v-for="item in gradeList" :key="item.code" :label="item.name" :value="item.code" />
      </el-select>
    </div>
    <div class="item" style="align-items: center;">
      <div class="label"><span style="color: red; margin-right: 6px;">*</span>轮次：</div>
      <el-select v-model="unitManageForm.lunci" placeholder="请选择" clearable>
        <el-option label="春季" value="1" />
        <el-option label="秋季" value="0" />
      </el-select>
    </div>
    <div class="item" v-if="classId">
      <div class="label"><span style="color: red; margin-right: 6px;" v-if="classIdRequired">*</span>班级：</div>
      <el-input placeholder="请输入" clearable v-model.trim="unitManageForm.classId" maxlength="10"
        v-if="!classSelect"></el-input>
      <el-select v-model="unitManageForm.classId" placeholder="请选择" clearable v-else>
        <el-option v-for="item in classesList" :key="item" :label="item" :value="item" />
      </el-select>
    </div>
    <slot></slot>
  </div>
</template>

<script>
import commonOptions from '@/mixin/commonOptions'
// import { getLevel } from '@/utils/common/area.js'
export default {
  name: 'selectClass',
  mixins: [commonOptions],
  data() {
    return {
      classesList: [],
      resetForm: {},
      // currentLevel: Number(getLevel(this.$store.state.userCode)),
    }
  },
  props: {
    lableWidth: {
      type: String,
      required: false,
      default: '200px'
    },
    classSelect: {
      type: Boolean,
      required: false,
      default: false
    },
    classIdRequired: {
      type: Boolean,
      required: false,
      default: true
    },
    isBindGrade: {
      type: Boolean,
      required: false,
      default: false
    },
    classId: {
      type: Boolean,
      required: false,
      default: false
    },
    stuClalevel: {
      type: Boolean,
      required: false,
      default: true
    },
    unitManageForm: {
      type: Object,
      default: function () {
        return {
          stuclayear: this.$dateutil.currentYear(),
          stuClalevel: '',
          classId: '',//班级
          lunci: '',
          stuClalevelName: ''
        }
      }
    },
    isRequired: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  computed: {
    unitManageFormCopy() {
      return JSON.parse(JSON.stringify(this.unitManageForm))
    }
  },
  watch: {
    unitManageFormCopy: {
      deep: true,
      async handler(newVal, oldVal) {
        if(newVal.stuClalevel=='' && this.isBindGrade){
          return
        }
        if (newVal.stuclayear != '' && newVal.lunci != '' || (this.stuClalevel && newVal.stuClalevel != '')) {
          if (newVal.stuclayear != oldVal.stuclayear || newVal.lunci != oldVal.lunci || (this.stuClalevel && newVal.stuClalevel != oldVal.stuClalevel)) {
            this.unitManageForm.classId = ''
          }
          let params = {
            schId: this.$store.state.schId,
            classLevel: newVal.stuClalevel,
            classYear: newVal.stuclayear,
            lunci: newVal.lunci
          }
          await this.findClassNew(params)
        } else {
          this.unitManageForm.classId = ''
          await this.findClassNew()
        }
        if (newVal.stuClalevel !== oldVal.stuClalevel) {
          this.unitManageForm.classId = ''
          let params = {
            schId: this.$store.state.schId,
            classLevel: newVal.stuClalevel,
            classYear: newVal.stuclayear,
            lunci: newVal.lunci
          }
          await this.findClassNew(params)
        }
      }
    }
  },
  methods: {
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    stuClalevelChange() {
      if (this.unitManageForm.stuClalevel == '') {
        this.unitManageForm.classId = ''
        return
      }
      this.unitManageForm.stuClalevelName = this.gradeList.filter(item => this.unitManageForm.stuClalevel == item.code)[0].name
      this.$forceUpdate()
    },
    validateForm(callback) {
      if (!this.unitManageForm.stuclayear) {
        this.$util.errorMsg('请选择学年!')
        return false
      }
      if (this.isRequired) {
        if (this.stuClalevel && !this.unitManageForm.stuClalevel) {
          this.$util.errorMsg('请选择年级!')
          return false
        }
      }
      if (!this.unitManageForm.lunci) {
        this.$util.errorMsg('请选择轮次!')
        return false
      }
      if (this.classId && !this.unitManageForm.classId && !this.classSelect && this.classIdRequired) {
        this.$util.errorMsg('请输入班级!')
        return false
      }
      if (this.classId && !this.unitManageForm.classId && this.classSelect && this.classIdRequired) {
        this.$util.errorMsg('请选择班级!')
        return false
      }
      return true
    }
  },
  async mounted() {
    // 查询学年
    await this.findClaYear()
    await this.findClaLevelNew()
  }
}
</script>

<style>
.newselect {
  flex-wrap: wrap;
}

.newselect .el-input {
  width: 200px;
}
</style>
