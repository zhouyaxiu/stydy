<!--
 * @Description: 多级表头表格
 * @params: tableWidth: number - 表格宽度 100%
 * @params: tableHeight: number - 表格高度 600px
 * @params: headerCellStyle: object - 表头样式
 * @params: cellStyle: object - 单元格样式
 * @params: canSelect: object - 列是否可选
 * @params: changeNull: boolean - 如果数据为null则展示--否则展示0 默认false
 * @params: rowOperateFun: fun - 操作列按钮点击事件
 * @params: pagenum: number - 分页信息目前用于序号累加展示
 * @params: data: Array<object> - 表格的数据源
 * @params: columnData: Array<object> - 列定义
                        {
                          fixed: boolean - 是否固定列
                          type: string - 列类型[selection是否可选,index序号,operate操作列]
                          title: string - 列名
                          hasChildrenTitle：boolean - 是否有子表头
                          list: Array<object> - 子表头的数据
                          key: string - 对应列内容的字段名(prop)
                          minWidth: string - 最小宽度
                          showOverflowTooltip: boolean - 超出隐藏
                          btns: Array<object> - 操作列按钮
                                {
                                  type: string - 按钮类型[insert新增,update修改,delete修改,query查询,output导出]
                                  text: string - 按钮显示名称
                                }
                        }
                        三级表头示例:
                         {
                          title: "流脑疫苗",
                          hasChildrenTitle: true,
                          list: [
                            {
                              title: "A群流脑疫苗",
                              hasChildrenTitle: true,
                              list: [
                                { title: "1", key: "b040InspectionShow", minWidth: "100" },
                                { title: "2", key: "b041InspectionShow", minWidth: "100" },
                              ],
                            },
                            {
                              title: "A+C群流脑疫苗",
                              hasChildrenTitle: true,
                              list: [
                                { title: "1", key: "b045InspectionShow", minWidth: "100" },
                                { title: "2", key: "b046InspectionShow", minWidth: "100" },
                              ],
                            },
                          ],
                        },
-->
<template>
  <div class="searchTerm">
    <el-table class="customize_table_show_table" :style="'width: ' + tableWidth + ';margin-top:10px;'"
      :max-height="tableHeight" :data="data" :header-cell-style="headerCellStyle" :cell-style="cellStyle"
      :row-key="getRowKeys" empty-text="暂无数据" :row-class-name="tableRowClassName" @selection-change="selectionChange" :span-method="arraySpanMethod">
      <template v-for="(item, index) in columnData">
        <el-table-column :key="index" :selectable="canSelect" type="selection" width="50"
          v-if="item.type === 'selection'" :fixed="item.fixed"></el-table-column>
        <el-table-column :key="index" type="index" label="序号" v-else-if="item.type === 'index'" :fixed="item.fixed">
          <template slot-scope="scope">
            {{ 20 * (pagenum - 1) + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column v-else-if="item.type === 'operate'" align="center" :width="item.width" :label="item.title"
          :key="index" :fixed="item.fixed" :min-width="item.minWidth">
          <template slot-scope="{ row }">
            <el-button v-for="btn in item.btns" :key="btn.text" @click.native="btnClick(row, btn.type)" type="text"
              class="table-btn" v-btnPermi="btn.type">
              {{ btn.text }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column v-else :key="index" :sortable="item.sort" :prop="item.attrName" :label="item.label"
          :width="item.width" :min-width="item.minWidth" :fixed="item.fixed"
          :show-overflow-tooltip="item.showOverflowTooltip">
          <!-- 多级表头 -->
          <template v-if="item.hasChildrenTitle">
            <el-table-column v-for="(jtem, j) in item.list" :key="j" :sortable="jtem.sort" :prop="jtem.attrName"
              :label="jtem.label" :width="jtem.width" :min-width="jtem.minWidth" :fixed="jtem.fixed">
              <!-- third start added by xing.yy 2022.05.13-->
              <template v-if="item.hasChildrenTitle">
                <el-table-column v-for="(ktem, k) in jtem.list" :key="k" :sortable="ktem.sort" :prop="ktem.key"
                  :label="ktem.title" :width="ktem.width" :min-width="ktem.minWidth" :fixed="ktem.fixed">
                  <!-- forth start-->
                  <template v-if="ktem.hasChildrenTitle">
                    <el-table-column v-for="(ctem, c) in ktem.list" :key="c" :sortable="ctem.sort" :prop="ctem.key"
                      :label="ctem.title" :width="ctem.width" :min-width="ctem.minWidth" :fixed="ctem.fixed">
                      <template slot-scope="scope">
                        <!-- + 三目判断属性值是否为null，否则显示0 -->
                        <span v-if="!jtem.slot">{{
                        scope.row[ctem.key] !== "" ||
                        scope.row[ctem.key] !== null
                        ? scope.row[ctem.key]
                        ? scope.row[ctem.key]
                        : changeNull
                        ? 0
                        : 0
                        : scope.row[ctem.sparekey]
                        ? scope.row[ctem.sparekey]
                        : changeNull
                        ? 0
                        : 0
                        }}</span>
                      </template>
                    </el-table-column>
                  </template>
                  <template slot="header" slot-scope="{}">
                    {{ ktem.title }}
                  </template>
                  <template slot-scope="scope">
                    <!-- ??? 不确定是否要改为&& -->
                    <!-- + 三目判断属性值是否为null，否则显示0 -->
                    <span v-if="!ktem.slot">{{
                    scope.row[ktem.key] !== "" || scope.row[ktem.key] !== null
                    ? scope.row[ktem.key]
                    ? scope.row[ktem.key]
                    : changeNull
                    ? 0
                    : 0
                    : scope.row[ktem.sparekey]
                    ? scope.row[ktem.sparekey]
                    : changeNull
                    ? 0
                    : 0
                    }}</span>
                    <slot :name="ktem.slot" :$index="scope.$index" :row="scope.row"></slot>
                  </template>
                  <!-- forth end-->
                </el-table-column>
              </template>
              <!-- third end-->
              <template slot="header" slot-scope="{}">
                {{ jtem.title }}
              </template>
              <template slot-scope="scope">
                <!-- ??? 不确定是否要改为&& -->
                <!-- + 三目判断属性值是否为null，否则显示0 -->
                <span v-if="!jtem.slot">{{
                scope.row[jtem.key] !== "" || scope.row[jtem.key] !== null
                ? scope.row[jtem.key]
                ? scope.row[jtem.key]
                : changeNull
                ? 0
                : 0
                : scope.row[jtem.sparekey]
                ? scope.row[jtem.sparekey]
                : changeNull
                ? 0
                : 0
                }}</span>
                <slot :name="jtem.slot" :$index="scope.$index" :row="scope.row"></slot>
              </template>
            </el-table-column>
          </template>
          <!-- 表头 -->
          <template slot="header" slot-scope="{}">
            {{ item.title }}
          </template>
          <!-- 内容 slot -->
          <template slot-scope="scope">
            <!-- 2021.10.29 修改 || ==> && -->
            <!-- + 三目判断属性值是否为null，否则显示0 -->
            <span v-if="!item.slot">{{
            scope.row[item.key] !== "" && scope.row[item.key] !== null
            ? scope.row[item.key]
            ? scope.row[item.key]
            : changeNull
            ? 0
            : 0
            : scope.row[item.sparekey]
            ? scope.row[item.sparekey]
            : changeNull
            ? 0
            : 0
            }}</span>
            <slot :name="item.slot" :$index="scope.$index" :row="scope.row"></slot>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <div class="pagination" v-show="total">
      <ss-pagination @goPage="goPage" :pagenum="pagenum" :total="total" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'customizeTable',
  data () {
    return {
      tableHeight: 600,
      tableWidth: '100%'
    }
  },
  props: {
    arraySpanMethod: {
      type: Function,
      default: function () {}
    },
    changeRowBg: {
      type: Boolean,
      required: false,
      defalut: false
    },
    changeNull: {
      type: Boolean,
      required: false,
      defalut: false
    },
    transfer: {
      type: Boolean,
      required: false,
      defalut: false
    },
    selection: {
      type: Boolean,
      default: false
    },
    showIndex: {
      type: Boolean,
      default: false
    },
    columnData: {
      type: Array,
      default: () => {
        return []
      }
    },
    data: {
      type: Array,
      default: () => {
        return []
      }
    },
    canSelect: {
      type: Function,
      default () {
        return {}
      }
    },
    cellStyle: {
      type: Object,
      default: () => {
        return { 'text-align': 'center' }
      }
    },
    headerCellStyle: {
      type: Object|Function,
      default: () => {
        return { 'text-align': 'center' } || function(){}
      }
    },
    // 默认进来获取自定义高度和宽度
    isSetWidth: {
      type: Boolean,
      default: true
    },
    // page相关
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
  destroyed () {
    window.removeEventListener('resize', this.onScreenResize, true)
  },
  updated () {
    this.setHeight()
    this.setScrollBar()
  },
  mounted () {
    this.setWidth()
    this.setScrollBar()
  },
  methods: {
    btnClick (row, btnType) {
      this.$emit('rowOperateFun', row, btnType)
    },
    setHeight () {
      let that = this
      let top = this.$util.getAbsoluteTop(that.$el)
      if (!top) {
        that.height = null
        return
      }
      let remarkHeight = 0
      if (that.$parent.$refs.remarkPage) {
        // 表格下方备注区域的高度
        remarkHeight = that.$parent.$refs.remarkPage.clientHeight
      }
      that.tableHeight =
        window.winHeight - top - 15 / window.scale - remarkHeight - 36 // 调整翻页条位置后，需减掉翻页条高度36px
      // console.log(`setHeight---top:${top};height:${that.height}`)
    },
    setWidth () {
      let that = this
      that.tableWidth = '100%'
      if (that.$parent.$refs.conditionPage) {
        // 表格上方查询条件的宽度
        that.tableWidth =
          that.$parent.$refs.conditionPage.$el.clientWidth + 'px'
      }
    },
    setWidth2 () {
      let that = this
      that.tableWidth = '100%'
      if (that.$parent.$refs.conditionPage) {
        // 表格上方查询条件的宽度
        that.tableWidth =
          that.$parent.$refs.conditionPage.$el.clientWidth - 30 + 'px'
      }
    },
    onScreenResize () {
      return (() => {
        let height
        if (document.compatMode === 'BackCompat') {
          height = document.body.scrollHeight - 270
        } else {
          height = document.documentElement.scrollHeight - 270
        }
        height -= 36 /// /调整翻页条位置后，需减掉翻页条高度36px
        this.$nextTick(() => {
          this.tableHeight = height < 250 ? 250 : height
          this.tableWidth =
            document.getElementsByClassName('searchTerm')[0].offsetWidth
        })
      })()
    },
    goPage (pagenum) {
      this.$emit('goPage', pagenum)
    },
    selectionChange (val) {
      this.$emit('selectionChange', val)
    },
    // 换页时不清空之前页选中项
    getRowKeys (row) {
      return row.id || row.rowkey
    },
    tableRowClassName ({ row }) {
      if (row.rowColor === '#CCFFFF') {
        return 'row-blue'
      } else if (row.rowColor === '#E3FDD6') {
        return 'row-green'
      } else {
        return 'row-white'
      }
    }
  }
}
</script>

<style scoped>
.pagination {
  width: 100%;
  text-align: right;
  margin-right: 20px;
}

.pagination .el-pagination button,
.pagination .el-dialog,
.el-pager li {
  background-color: transparent !important;
}

.pagination .el-pagination {
  color: #232d39 !important;
}

.pagination .el-pagination__total {
  color: #5d5f65 !important;
}

.pagination .el-pager li.active {
  color: #097af9 !important;
}

.pagination .el-pagination button:disabled {
  color: #a5a6b6 !important;
}

/*在册受种者：颜色标注白色；失访受种者：颜色标注绿色；死亡受种者：颜色标注黄色。*/
.row-white {
  background-color: #fff !important;
}

.row-blue {
  background-color: #ccffff !important;
}

.row-green {
  background-color: #e3fdd6 !important;
}

.row-yellow {
  background-color: #ffff99 !important;
}

/*紫色*/
.row-purple {
  background-color: #cfb0dc !important;
}
</style>
