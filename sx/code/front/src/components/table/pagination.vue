<!--
 * @Description: 分页
-->
<template>
  <div class="pagination" v-show="total">
    <el-pagination
      :current-page="pagenum"
      layout="total, prev, pager, next, jumper, slot"
      :page-size="pageSize"
      :total="total"
      background
      @current-change="goPage"
      ref="pagination_el"
    >
      <slot>
        <span class="slot">目前第[{{ pagenum }}]页/共[{{ pageTotal }}]页</span>
      </slot>
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: 'pagination',
  data () {
    return {
      pageTotal: 0
    }
  },
  props: {
    pagenum: {
      type: Number,
      required: false,
      default: 1
    },
    total: {
      type: Number,
      required: false,
      default: 0
    },
    pageSize: {
      type: Number,
      required: false,
      default: function () {
        return this.$store.state.pagesize
      }
    }
  },
  watch: {
    total (val) {
      let el = this.$refs.pagination_el.$el
      el.getElementsByClassName(
        'el-pagination__total'
      )[0].innerHTML = `共 [${val}] 条记录`
      this.pageTotal = parseInt(
        (this.total + this.pageSize - 1) / this.pageSize
      )
    }
  },
  methods: {
    goPage (pagenum) {
      this.$emit('goPage', pagenum)
    }
  },
  mounted () {
    // 自定义分页条
    let el = this.$refs.pagination_el.$el
    el.getElementsByClassName(
      'el-pagination__jump'
    )[0].childNodes[0].nodeValue = '跳转'
    el.getElementsByClassName(
      'el-pagination__total'
    )[0].innerHTML = `共 [${this.total}] 条记录`
    el.getElementsByClassName('btn-prev')[0].childNodes[0].setAttribute(
      'class',
      'el-cion el-icon-caret-left'
    )
    el.getElementsByClassName('btn-next')[0].childNodes[0].setAttribute(
      'class',
      'el-cion el-icon-caret-right'
    )
  }
}
</script>

<style scoped>
.pagination {
  width: 100%;
  /* text-align: center; */
  margin-top: 10px;
  text-align: right;
  margin-right: 20px;
}
</style>
<style>
.pagination .el-pagination button,
.pagination .el-dialog,
.el-pager li {
  background-color: transparent !important;
  color: #8990A5 !important;
}

.pagination .el-pagination {
  color: #8990a5 !important;
}

.pagination .el-pagination__total {
  color: #8990a5 !important;
}

.pagination .el-pager li.active {
  color: #478bff !important;
}

.pagination .el-pagination button:disabled {
  color: e4eefe !important;
}
.pagination .slot {
  color: #8990A5;
  font-weight: 400;
  margin-top: -2px;
  padding-left: 10px;
}
.pagination .btn-prev i,
.pagination .btn-next i {
  background: #e4eefe;
  padding: 3px;
  color: #478bff;
  border-radius: 4px;
}
.pagination .btn-prev:disabled i,
.pagination .btn-next:disabled i {
  background: #eaeaea;
  padding: 3px;
  color: #c5c5c5;
  border-radius: 4px;
}
.pagination .el-pagination__jump{
  color: #8990a5 !important;
}
</style>
