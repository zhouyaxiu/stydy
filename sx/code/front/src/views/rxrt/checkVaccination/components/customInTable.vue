<template>
    <div>
      <el-table class="customize_table_show_table" :style="'width: ' + tableWidth + ';margin-top:10px;'"
        :max-height="tableHeight" :data="data" :header-cell-style="headerCellStyle" :cell-style="cellStyle"
        :row-key="getRowKeys" empty-text="暂无数据" :row-class-name="tableRowClassName" @selection-change="selectionChange"
        :span-method="arraySpanMethod" ref="itsmDataTable">
        <template v-for="(item, index) in columnData">
          <el-table-column :key="index" :selectable="canSelect" type="selection" width="50" v-if="item.type === 'selection'"
            :fixed="item.fixed" :reserve-selection="true"></el-table-column>
          <el-table-column :key="index" type="index" label="序号" v-else-if="item.type === 'index'" :fixed="item.fixed">
            <template slot-scope="scope">
              {{ 20 * (pagenum - 1) + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column v-else-if="item.type === 'operate' && $store.state.modelObject != 0" align="center" :width="item.width" :label="item.title"
            :key="index" :fixed="item.fixed" :min-width="item.minWidth">
            <template slot-scope="{ row }">
              <span  v-for="btn in item.btns" :key="btn.text" @click="btnClick(row, btn.type)" style="cursor:pointer" class="el-button el-button--text">{{ btn.text }}</span>
            </template>
          </el-table-column>
          <el-table-column v-else-if="item.isVaccine" :prop="item.key" :label="item.title"
            :width="item.width" :min-width="item.minWidth">
            <template v-if="item.hasChildrenTitle">
              <el-table-column :prop="itm.key" :label="itm.title"
                :width="itm.width" :min-width="itm.minWidth" v-for="(itm,inx) in item.list" :key="inx">
                <template v-if="item.hasChildrenTitle">
                  <el-table-column :prop="im.key" :label="im.title"
                    :width="im.width" :min-width="im.minWidth" v-for="(im,ix) in itm.list" :key="ix">
                    <template slot-scope="scope">
                      <span :style="{ color: scope.row[im.key] == '2' ? '#3385ff' : scope.row[im.key] == '0' ? 'red' : '' }">
                        {{ scope.row[im.key] == '0' ?'X': scope.row[im.key]== '1' || scope.row[im.key] == '2' ? '√' : scope.row[im.key] == '-1' ? 'O' : '' }}
                      </span>
                    </template>
                </el-table-column>
                </template>
                <template slot-scope="scope">
                  <span :style="{ color: scope.row[itm.key] == '2' ? '#3385ff' : scope.row[itm.key] == '0' ? 'red' : '' }">
                    {{ scope.row[itm.key] == '0' ?'X': scope.row[itm.key]== '1' || scope.row[itm.key] == '2' ? '√' : scope.row[itm.key] == '-1' ? 'O' : '' }}
                  </span>
                </template>
              </el-table-column>
            </template>
            <template slot-scope="scope">
              <span :style="{ color: scope.row[item.key] == '2' ? '#3385ff' : scope.row[item.key] == '0' ? 'red' : '' }">
                {{ scope.row[item.key] == '0' ?'X': scope.row[item.key]== '1' || scope.row[item.key] == '2' ? '√' : scope.row[item.key] == '-1' ? 'O' : '' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column v-else-if="item.type != 'selection' && item.type != 'index' && item.type != 'operate'" :key="index" :sortable="item.sort" :prop="item.key" :label="item.title"
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
                          <span v-if="!ctem.render">
                            {{scope.row[ctem.key] || ''}}
                          </span>
                        </template>
                      </el-table-column>
                    </template>
                    <template slot="header" slot-scope="{}">
                      {{ ktem.title }}
                    </template>
                    <template slot-scope="scope">
                      <!-- ??? 不确定是否要改为&& -->
                      <!-- + 三目判断属性值是否为null，否则显示0 -->
                      <span v-if="!ktem.render">{{scope.row[ktem.key] || ''}}</span>
                      <ex-slot v-if="ktem.render" :render="ktem.render" :row="scope.row" :index="scope.$index" :column="ktem" />
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
                  <span v-if="!jtem.render">{{scope.row[jtem.key] || ''}}</span>
                  <ex-slot v-if="jtem.render" :render="jtem.render" :row="scope.row" :index="scope.$index" :column="jtem" />
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
              <ex-slot v-if="item.render" :render="item.render" :row="scope.row" :index="scope.$index" :column="item" />
              <span v-else>{{scope.row[item.key] || ''}}</span>
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
  // 自定义内容的组件
  var exSlot = {
    functional: true,
    props: {
      row: Object,
      render: Function,
      index: Number,
      column: {
        type: Object,
        default: null
      }
    },
    render: (h, data) => {
      const params = {
        row: data.props.row,
        index: data.props.index
      }
      if (data.props.column) params.column = data.props.column
      return data.props.render(h, params)
    }
  }
  export default {
    name: 'customizeInTable',
    data () {
      return {
        tableHeight: 600,
        tableWidth: '100%'
      }
    },
    components: { exSlot },
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
      },
      // 修改行背景颜色标识列属性
      changeRowAttr: {
        type: String,
        required: false,
        defalut: ''
      },
      // 背景颜色列表
      rowBgList: {
        type: Array,
        required: false,
        defalut: () => {
          return [
            {
              text: '需补种',
              style: { background: '#E8FFE8' },
              stuIsjz: '1',
              className: 'row-green'
            },
            {
              text: '无接种证',
              style: { background: '#E9FFFF' },
              stuIsjz: '2',
              className: 'row-yellow'
            },
          ]
        }
      },
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
        let that = this
        // 是否修改背景颜色
        if (that.changeRowBg && that.changeRowAttr) {
          // 根据标识属性值获取颜色
          let rowBg = that.rowBgList.find(
            (i) => i[that.changeRowAttr] === row[that.changeRowAttr]
          )
          if (rowBg) {
            return rowBg.className
          }
        }
      }
    }
  }
  </script>