<template>
  <div
    class="area-group"
    :style="
      'width:' +
      minWidth +
      ';min-width:' +
      width +
      (direction != 'row' ? '' : ';margin-right:50px;')
    "
    :class="direction"
  >
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'commonAreaGroup',
  data () {
    return {}
  },
  props: {
    prop: {
      type: String,
      required: false
    },
    width: {
      type: String,
      required: false,
      default: 'auto'
    },
    columnCount: {
      type: Number,
      required: false,
      default: 1
    },
    direction: {
      type: String,
      required: false,
      default: 'column'
    },
    labelPec: {
      type: Number,
      required: false,
      default: 0.3
    }
  },
  computed: {
    minWidth: function () {
      if (this.direction === 'row') {
        return '100%'
      } else {
        return 'calc(' + 100 / this.columnCount + '% - 54px)'
      }
    }
  },
  methods: {},
  mounted () {
    if (this.direction === 'row') {
      this.$nextTick(() => {
        let width = parseFloat(
          window.getComputedStyle(this.$el).width.replace('px', '')
        )
        let singleWidth = (width - 5) / this.columnCount
        var that = this
        let arr = Array.from(this.$el.querySelectorAll('.common-area-item'))
        arr.forEach((item) => {
          item.style.width = singleWidth + 'px'
        })
        arr = Array.from(this.$el.querySelectorAll('.el-form-item__label'))
        arr.forEach((item) => {
          item.style.width = width * that.labelPec + 'px'
        })
        arr = Array.from(this.$el.querySelectorAll('.el-form-item__content'))
        arr.forEach((item) => {
          item.style.width = singleWidth - width * that.labelPec + 'px'
        })
      })
    }
  }
}
</script>
<style>
.area-group {
  display: inline-block;
}
.column .select-area-item,
.column .common-area-item {
  display: block;
}
.row .select-area-item,
.row .common-area-item {
  display: inline-block;
}
.row .el-form-item__content {
  display: inline-block !important;
  margin-left: 0 !important;
}
</style>
