<template>
  <div class="common-area-item">
    <el-form-item
      :label="label"
      :prop="prop"
      :style="'width:' + width"
      :label-width="labelWidth"
      :required="required"
      :rules="rules"
    >
      <slot></slot>
    </el-form-item>
  </div>
</template>

<script>
export default {
  name: 'commonAreaItem',
  data () {
    return {}
  },
  props: {
    label: {
      type: String,
      required: false
    },
    prop: {
      type: String,
      required: false
    },
    labelWidth: {
      type: String,
      required: false
    },
    width: {
      type: String,
      required: false,
      default: '100%'
    },
    required: {
      type: Boolean,
      required: false
    },
    rules: {
      type: [Object, Array],
      default: () => {}
    }
  },
  methods: {}
}
</script>

<style scoped>
.common-area-item :not(".el-input-number"):not(".el-date-editor").el-input {
  width: calc(100% - 32px);
}
.common-area-item .el-select {
  width: 100%;
}
.common-area-item .el-date-editor.el-input {
  width: 100%;
}
</style>
<style>
.common-area-item .el-form-item__label {
  font-size: 13px !important;
}
</style>
