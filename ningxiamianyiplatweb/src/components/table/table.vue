<!-- 自定义标准表格
  /** 父组件传参
    * v-listRequstFun {String}  查询表格数据方法 
    * v-maxHeight {Number}  表格高度
    * v-columnList {Array}  表格列表
        [{
            type:'',  selection(可选列)/operation(操作列，可配置btns)  默认为普通列
            prop: '',
            label: '',
            fixed:true/false,//固定列
            width: '',
            overflowTip:true/false，//超过列宽鼠标浮动提示
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
     注）尾部具名插槽 ==> slotSufixCols
 */ -->
<template>
    <div>
        <el-table ref="normalTable" border :data="tableData" :max-height="tableHeight" :style="tableStyle"
            empty-text="暂无数据" :row-class-name="tableRowClassName" @selection-change="selectionChange"
            highlight-current-row @row-click="rowClick">
            <template v-for="(item, idx) in columnList">
                <el-table-column type="selection" width="55" v-if="item.type == 'selection'" align="center" />
                <el-table-column :key="idx" type="index" label="序号" v-if="item.type === 'index'" width="100"
                    :fixed="item.fixed" align="center" />
                <el-table-column v-else-if="!item.type" :prop="item.prop" :label="item.label"
                    :show-overflow-tooltip="item.overflowTip ? item.overflowTip : false" align="center"
                    :fixed="item.fixed ? item.fixed : false" :min-width="item.width || 200"></el-table-column>
                <el-table-column align="center" v-else-if="item.type == 'operation'" label="操作" min-width="120">
                    <template #default="scope">
                        <el-button v-for="btn in item.btns" @click="btnHandle(scope.row, btn)" size="small"
                            :type="btn.type ? btn.type : 'primary'" :disabled="getBtnEditable(scope.row, btn)">
                            {{ btn.text }}
                        </el-button>
                    </template>
                </el-table-column>
                <!-- 尾部具名插槽，插入自定义列 -->
                <slot name="slotSufixCols"></slot>
            </template>
        </el-table>
        <ss-pagination :total="total" @pageChange="pageChangeFun" :pageIndex="pageNum" :pageSize="pageSize" />
    </div>
</template>
<script setup>
import ssPagination from '@/components/table/Pagination.vue'
//自定义全局公共方法引用
const { appContext } = getCurrentInstance();
const globalProxy = appContext.config.globalProperties;

const normalTable = ref()
const tableHeight = ref(300)
const tableStyle = ref({ width: '100%' })
const total = ref(30)
const pageNum = ref(1)
let catchQueryParams = {}
const tableData = ref([])
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
//复选框事件
const selectionChange = (val) => {
    callback(eventTypes.value.selectionChange, val)
}
//默认选中部分行
const toggleSelection = (rows) => {
    if (rows) {
        rows.forEach((row) => {
            normalTable.value.toggleRowSelection(row, undefined)
        })
    } else {
        normalTable.value.clearSelection()
    }
}
//行按钮是否启用判断
const getBtnEditable = (row, btn) => {
    return row.unEditable ? row.unEditable : false
}
//行按钮点击事件
const btnHandle = (row, btn) => {
    total.value = total.value + 10
    let callback = (type) => {
        console.log(type)
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
    pageSize: {
        type: Number,
        default: 20
    }
})
//子组件方法，父组件调用
const getData = async (queryParams) => {
    if (queryParams) {
        catchQueryParams = queryParams
    } else {
        queryParams = catchQueryParams
    }
    //调用接口,获取当前页数据
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
                name: 'Tom',
                state: 'Tom2',
                zip: 'Tom0',
                address: 'No. 189, Grove St, Los Angeles',
            }, {
                date: '2016-05-02',
                name: 'Tom',
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
