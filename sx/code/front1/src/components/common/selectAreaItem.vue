<template>
    <div class="select-area-item" :style="'width:'+width">
        <div class="select-item-label"
            v-show="!!label"
            :style="'margin-bottom:'+marginBottom">
            <span style="color: red;margin-right: 5px;" v-show="required">*</span><span style="font-size:13px;">{{label}}</span>
        </div>
        <div class="select-item-slot">
            <el-form-item :prop="prop" label-width="0">
                <template slot-scoped="scoped">
                    <slot></slot>
                </template>
            </el-form-item>
        </div>
    </div>
</template>

<script>
export default {
  name: 'selectAreaItem',
  data () {
    return {
    }
  },
  props: {
    label: {
      type: String,
      required: false
    },
    width: {
      type: String,
      required: false,
      default: '300px'
    },
    required: {
      type: Boolean,
      required: false,
      default: false
    },
    prop: {
      type: String,
      required: false
    },
    marginBottom: {
      type: String,
      required: false,
      default: '0'
    }
  }
}
</script>

<style scoped>
    .select-area-item {
        display: inline-block;
        margin: 0 10px;
        position:relative;
        z-index: 0;
    }
    .select-item-label{
        margin: 0 5px;
        color: darkblue;
        width: 100%;
        height: 40px;
        vertical-align: bottom;
    }
    .select-item-slot{
        line-height: 40px;
        min-height: 40px;
    }
    .select-item-slot .el-form-item{
        margin-bottom: 0 !important;
    }
</style>
