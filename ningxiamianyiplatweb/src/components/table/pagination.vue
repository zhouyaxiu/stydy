 <!-- 自定义分页条
  /** 父组件传参
    * v-total {String}  记录总条数
    * v-pageIndex {String}  页码
    * v-pageSize {String}  单页记录数
    * @pageChange {Function}  子组件回调父组件方法
 */ -->
<template>
  <div class="ss-pagination" v-show="total">
    <el-pagination :current-page="pageIndex" layout="total, prev, pager, next, jumper, slot" :page-size="pageSize"
      :total="total" background @current-change="goPage">
      <!-- <slot>
                <span class="slot">目前第[{{ pageIndex }}]页/共[{{ pageTotal }}]页</span>
            </slot> -->
    </el-pagination>
  </div>
</template>
<script setup>
const pageTotal = ref(0)
const pagination_el = ref()
//子组件回调父组件方法
const emits = defineEmits(['pageChange'])
const goPage = (pageIndex) => {
  emits('pageChange', pageIndex)
};
//父组件传参--按钮类型
const props = defineProps({
  pageIndex: {
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
    default: 20
  }
})
// const setPaginationRef = (el) => {
//     pagination_el.value = el
// }
// watch(() => props.total, (val) => {
//     setTotalTip()
// })
const setTotalTip = () => {
  let el = pagination_el.value.$el
  el.getElementsByClassName(
    'el-pagination__total'
  )[0].innerHTML = `共 [${props.total}] 条记录`
  pageTotal.value = parseInt(
    (props.total + props.pageSize - 1) / props.pageSize
  )
}
</script>
<style lang="scss">
.ss-pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;

  .el-pagination .el-pagination__total {
    color: #6C798B !important;
  }

  .el-pagination__editor.el-input .el-input__wrapper {
    width: 58px;
    height: 28px;
    background: #FFFFFF;
    border-radius: 2px;
    border: 1px solid #C6CFDC;
    box-shadow: none;
  }

  .el-pager li,
  .el-pagination.is-background .btn-next,
  .el-pagination.is-background .btn-prev {
    width: 28px;
    height: 28px;
    background-color: #FFFFFF !important;
    border-radius: 2px 2px 2px 2px;
    border: 1px solid #C6CFDC;
    padding: 0;
    color: #47566B;
  }

  .el-pagination.is-background .btn-next.is-active,
  .el-pagination.is-background .btn-prev.is-active,
  .el-pagination.is-background .el-pager li.is-active {
    border-color: #3385FF;
    color: #3385FF;
    font-weight: normal;
  }

  .el-pagination.is-background .btn-prev.is-disabled,
  .el-pagination.is-background .btn-prev:disabled {
    color: #BCC3CC !important;
  }
}
</style>
