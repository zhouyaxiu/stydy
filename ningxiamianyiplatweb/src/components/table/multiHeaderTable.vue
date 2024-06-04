 <!-- 自定义多级表头表格
  /** 父组件传参
    * v-listRequstFun {String}  查询表格数据方法 
    * v-maxHeight {Number}  表格高度
    * v-columnList {Array}  表格列表
        [{
            type:'',  operation(操作列，可配置btns)  默认为普通列
            prop: '',
            label: '',
            width: '',
            overflowTip:true/false，//超过列宽鼠标浮动提示
            hasSub:true/false,
            subCols:[{
                prop:'',
                label:'',
                width:'',
                hasSub:true/false,
                overflowTip:true/false
            }]
            btns:[{
                type:'', primary/success/info/warning/danger
                text:''
            }]
        }]
    * @tableEvents {Function}  子组件回调父组件方法
            （params:{  参数
                 eventType: type,//事件类型
                 data: data  //数据
            }）
    注）首、尾具名插槽 ==> slotPreCols、slotSufixCols
 */ -->
<template>
    <el-table ref="multiHeaderTable" border :data="tableData" :max-height="tableHeight" :style="tableStyle"
        highlight-current-row empty-text="暂无数据" :row-class-name="tableRowClassName" @row-click="rowClick">
        <!-- 首部具名插槽，可自定义插入索引列、复选框列等 -->
        <slot name="slotPreCols"></slot>
        <template v-for="(item1, idx1) in columnList">
            <!-- 一级表头 -->
            <el-table-column v-if="item1.hasSub" :label="item1.label"
                :show-overflow-tooltip="item1.overflowTip ? item1.overflowTip : false" align="center"
                :min-width="item1.width || 200">
                <!-- 二级表头 -->
                <template v-for="(item2, idx2) in item1.subCols">
                    <el-table-column v-if="item2.hasSub" :label="item2.label"
                        :show-overflow-tooltip="item2.overflowTip ? item2.overflowTip : false" align="center"
                        :min-width="item2.width || 200">
                        <!-- 三级表头 -->
                        <template v-for="(item3, idx3) in item2.subCols">
                            <el-table-column v-if="item3.hasSub" :label="item3.label"
                                :show-overflow-tooltip="item3.overflowTip ? item3.overflowTip : false" align="center"
                                :min-width="item3.width || 200"></el-table-column>
                            <el-table-column v-else :label="item3.label" :prop="item3.prop"
                                :show-overflow-tooltip="item3.overflowTip ? item3.overflowTip : false" align="center"
                                :min-width="item3.width || 200" />
                        </template>
                    </el-table-column>
                    <el-table-column v-else :label="item2.label" :prop="item2.prop"
                        :show-overflow-tooltip="item2.overflowTip ? item2.overflowTip : false" align="center"
                        :min-width="item2.width || 200" />
                </template>
            </el-table-column>
            <template v-else>
                <!-- 操作列判断 -->
                <el-table-column align="center" v-if="item1.type == 'operation'" label="操作"
                    :min-width="item1.width ? item1.width : 160">
                    <template #default="scope">
                        <el-button v-for="btn in item1.btns" @click="btnHandle(scope.row, btn)" size="small"
                            :type="btn.type ? btn.type : 'primary'" :disabled="getBtnEditable(scope.row, btn)">
                            {{ btn.text }}
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column v-else :label="item1.label" :prop="item1.prop"
                    :show-overflow-tooltip="item1.overflowTip ? item1.overflowTip : false" align="center"
                    :min-width="item1.width || 200" />
            </template>
        </template>
        <!-- 尾部具名插槽，插入自定义列 -->
        <slot name="slotSufixCols"></slot>
    </el-table>
    <ss-pagination :total="total" @pageChange="pageChangeFun" :pageIndex="pageNum" />
</template>
<script setup>
import ssPagination from '@/components/table/Pagination.vue'
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const multiHeaderTable = ref()
const tableHeight = ref(300)
const tableStyle = ref({ width: '100%' })
const total = ref(30)
const pageNum = ref(1)
const tableData = ref([])
let catchQueryParams = {}
//事件类型
const eventTypes = ref({
    pageChange: 'page-change',
    selectionChange: 'selection-change',
    rowClick: 'row-click',
})
//子组件回调父组件方法
const emits = defineEmits(['tableEvents'])
const callback = (type, data) => {
    let params = {
        eventType: type,
        data: data
    }
    emits('tableEvents', params)
};
//页面数据跳转
const pageChangeFun = (pageIndex) => {
    pageNum.value = pageIndex
    getData()
}
//行按钮是否启用判断
const getBtnEditable = (row, btn) => {
    return row.unEditable ? row.unEditable : false
}
//行按钮点击事件
const btnHandle = (row, btn) => {
    total.value = total.value + 10
    let callback = (type) => {
        globalProxy.$util.showMsg(type, type == 'cancle' ? 'info' : 'success')
    }
    globalProxy.$util.showConfirmDialog(`点击了【${btn.text}】按钮`, callback)
}
//行点击事件
const rowClick = (row, column, event) => {
    callback(eventTypes.value.rowClick, row)
}
//根据标识区分行样式
const tableRowClassName = ({ row, rowIndex }) => {
    // console.log(row)
}
//父组件传参--按钮类型
const props = defineProps({
    maxHeight: {
        type: Number,
        default: 0
    },
    columnList: {
        type: Array,
        default: () => {
            return []
        }
    },
    listRequstFun: {
        type: Function,
        default: () => { }
    },
})
//子组件方法，父组件调用
const getData = async (queryParams) => {
    if (queryParams) {
        catchQueryParams = queryParams
    } else {
        queryParams = catchQueryParams
    }
    try {
        // const res = await props.listRequstFun({
        //     pageSize: props.pageSize,
        //     pageIndex: pageNum.value,
        //     ...queryParams
        // })
        // console.log('输出数据', res);
        globalProxy.$util.showLoadingMask()
        setTimeout(() => {
            globalProxy.$util.hideLoadingMask()
        }, 400)
        tableData.value = [
            {
                date: '2016-05-03',
                name: 'Tom2',
                state: 'Tom2',
                zip: 'Tom0',
                address: 'No. 189, Grove St, Los Angeles',
            }, {
                date: '2016-05-02',
                name: 'Tom20',
                state: 'Tom20',
                zip: 'Tom4',
                address: 'No. 189, Grove St, Los Angeles',
                unEditable: true
            }
        ]
        toggleSelection([tableData.value[0]])
    } catch (error) {
        globalProxy.$util.hideLoadingMask()
    }
}
const setHeight = () => {
    if (props.maxHeight && props.maxHeight > 0) {
        tableHeight.value = props.maxHeight
    }
}
const setWidth = () => {
    tableStyle.value.width = '100%'
    // if (that.$parent.$refs.conditionPage) {
    //     // 表格上方查询条件的宽度
    //     that.width = that.$parent.$refs.conditionPage.$el.clientWidth + 'px'
    // }
}
defineExpose({ setHeight, setWidth, getData })
</script>
<style scoped lang="scss"></style>
