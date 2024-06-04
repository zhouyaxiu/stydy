<template>
  <!-- 查询提示信息
    * v-secTime {Number}  查询花费时间
    * v-tipText {String}  首部固定显示的提示
-->
  <div class="explain-tip-text">
    <div style="color: #717e89">
      {{ tipText }}
    </div>
    <div style="color: #409eff; margin-left: 10px" v-if="secTime > 0">
      <ss-icon iconType="tshi" style="margin-right: 5px" />
      友情提示：本次查询所花费时间大约是 {{ secTime }} 秒。
    </div>
  </div>
</template>

<script>
export default {
  name: 'explainTipText',
  data () {
    return {
      currentDate: this.$util.today(),
      yesterdayDate: this.$util.dayAdd(this.$util.today(), -1)
    }
  },
  props: {
    secTime: {
      type: Number,
      required: false,
      default: 0
    },
    tipText: {
      type: String,
      required: false,
      default: ''
    }
  }
}
</script>

<style scoped>
.explain-tip-text {
  width: 100%;
  display: flex;
  line-height: 32px;
  padding-left: 20px;
  font-size: 12px;
  background: #fff;
  color: #000;
}
</style>
