<!--
 * @Description: 分页
-->
<template>
  <div class="pagination" v-show="total">
    <el-pagination
      :current-page="pagenum"
      layout="total, prev, pager, next, sizes, jumper, slot"
      :page-sizes="[pageSize]"
      :page-size="pageSize"
      :total="total"
      background
      @current-change="goPage"
      ref="pagination_el"
    >
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
      )[0].innerHTML = `共${val}条`
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
    )[0].childNodes[0].nodeValue = '跳至'
    el.getElementsByClassName(
      'el-pagination__total'
    )[0].innerHTML = `共${this.total}条`
    el.getElementsByClassName(
      'el-pagination__sizes'
    )[0].innerHTML = `${this.pageSize}条/页`
  }
}
</script>

<style scoped>
.pagination {
  width: 100%;
  margin-top: 26px;
  text-align: right;
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
  color: #333333 !important;
  font-weight: normal !important;
  margin-right: 40px;
}

.pagination .el-pagination__total, .pagination .el-pagination__jump {
  color: #333333 !important;
}
.pagination .el-pagination button:disabled {
  color: #e4eefe !important;
}
.pagination .slot {
  color: #8990A5;
  font-weight: 400;
  margin-top: -2px;
  padding-left: 10px;
}
.el-pagination button, .el-pagination span:not([class*=suffix]){
  height: 30px;
  line-height: 30px;
  font-size: 14px;
}
.pagination .btn-prev i,
.pagination .btn-next i {
  width: 30px;
  height: 28px;
  line-height: 28px;
  color: #333333;
  border-radius: 4px;
  border: 1px solid #D8D9DB;
}
.pagination .btn-prev:disabled i,
.pagination .btn-next:disabled i {
  color: #AAAAAA;
  border-radius: 4px;
  border: 1px solid #D8D9DB;
}
.el-pagination.is-background .el-pager li{
  margin: 0 10px;
}
.pagination .el-pager li{
  height: 30px;
  border: 1px solid #ffffff;
}
.pagination .el-pager li.active{
  color: #2E5BFF !important;
  border: 1px solid #2E5BFF;
  border-radius: 4px;
}
.el-pagination__jump{
  margin-left: 0;
}
.el-pagination__sizes{
  width: 82px;
  text-align: center;
  height: 30px;
  line-height: 30px;
  border-radius: 4px;
  border: 1px solid #D8D9DB;
  margin-left: 20px;
  color: #333333;
}
.el-pagination__editor.el-input{
  width: 60px;
  margin: 0 8px;
}
.el-input__inner{
  border-color: #D8D9DB;
}
</style>
