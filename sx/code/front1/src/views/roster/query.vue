<template>
  <!-- 班级花名册录入-查询 -->
  <div>
    <ss-condition-area ref="conditionPage" @setConditionFlag="setConditionFlag" :titleLong="true">
      <ss-class-select ref="selectClass" :classSelect="true" :classId="true" :classIdRequired="false">
        <div class="item">
          <div class="label">是否种全：</div>
          <!-- <el-select v-model="unitManageForm.isjz" placeholder="请选择是否种全" clearable>
            <el-option label="全部" value="" />
            <el-option label="是" value="0" />
            <el-option label="否" value="1" />
          </el-select> -->
          <el-select v-model="unitManageForm.isjz" placeholder="请选择是否种全" clearable
            :class="unitManageForm.isjz==1? 'isjzSelects' : ''">
            <el-option label="全部" value="" />
            <el-option label="是" value="0" />
            <el-option label="否" value="1" />
          </el-select>
          <el-select v-model="unitManageForm.isjzTemp" placeholder="请选择" clearable
            class="isjzSelect" v-if="unitManageForm.isjz==1">
            <el-option label="需补种" value="1" />
            <el-option label="无需补种" value="-1" />
          </el-select>
        </div>
        <div class="item">
          <div class="label">查验状态：</div>
          <el-select v-model="unitManageForm.status" placeholder="请选择查验状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="已种全" value="0" />
            <!-- <el-option label="未种全" value="1" /> -->
            <el-option label="未种全需补种" value="1" />
            <el-option label="未种全无需补种" value="-1" />
            <el-option label="无接种证" value="2" />
          </el-select>
        </div>
        <div class="item">
          <div class="label">儿童姓名：</div>
          <el-input placeholder="请输入儿童姓名" clearable v-model="unitManageForm.name"></el-input>
        </div>
        <div class="item">
          <div class="label">身份证件号码：</div>
          <el-input placeholder="请输入身份证件号码" clearable v-model="unitManageForm.idcard"></el-input>
        </div>
        <div class="item">
          <div class="label">受种者编码：</div>
          <el-input placeholder="请输入受种者编码" clearable v-model="unitManageForm.code"></el-input>
        </div>
      </ss-class-select>
      <div class="mt_10 ml_200" style="display: flex;">
        <ss-custom-button @click="search(1,'search')" :type="$consts.CUSTOMBUTTONTYPES.query" />
        <ss-custom-button @click="exportExcelFun" :type="$consts.CUSTOMBUTTONTYPES.export" class="ml_10" />
        <!-- <ss-custom-button @click="checkHandler()" :type="$consts.CUSTOMBUTTONTYPES.check" class="ml_10" /> -->
        <!-- <ss-custom-button @click="deleteHandler()" :type="$consts.CUSTOMBUTTONTYPES.delete" class="ml_10" /> -->
        <!-- <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.alter" @click="alterHandler()" class="ml_10" /> -->
        <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.buzhong" @click="mobanHandler('预防接种证查验疫苗补种联系', 0)" class="ml_10" />
        <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.buzheng" @click="mobanHandler('预防接种证查验疫苗补证联系', 1)" class="ml_10" />
        <!-- <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.jiandang" @click="printNoiceHandler()" class="ml_10" /> -->
        <!-- <ss-custom-button :type="$consts.CUSTOMBUTTONTYPES.chayan" class="ml_10" @click="checkDialogVisible = true" /> -->
      </div>
    </ss-condition-area>
    <div class="main-content" style="position:relative;">
      <ss-table :changeRowBg="true" :rowBgList="tipList" :changeRowAttr="'stuIsjz'" style="width: 100%"
        @handleSelectionChange="handleSelectionChange" :data="dataList" ref="tabPage" class="vaccination querytable" :pageSize="20"
        :changeNull="true" @goPage="search" :pagenum="pagenum" :total="total">
        <el-table-column type="selection" width="55" align="center" :reserve-selection="true">
        </el-table-column>
        <!-- <el-table-column align="center" label="操作" width="200">
          <template slot-scope="{ row }">
            <span @click="deleteHandler(row.rowkey)" style="cursor:pointer" class="el-button el-button--text">删除</span>
            <span @click="alterHandler(row.rowkey)" style="cursor:pointer" class="el-button el-button--text">修改</span>
            <span @click="checkHandler(row.rowkey)" style="cursor:pointer" class="el-button el-button--text">查验</span>
          </template>
        </el-table-column> -->
        <el-table-column prop="stuIsjzOrigin" align="center" min-width="130" label="查验状态">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjzOrigin == '0' ? '已种全' : row.stuIsjzOrigin == 1 ? '未种全需补种' : row.stuIsjzOrigin == -1?'未种全无需补种':row.stuIsjzOrigin == 2 || row.stuIsjzOrigin == null?'无接种证':'' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuIsjz" align="center" min-width="100" label="是否种全">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjz == '0' ? '是' : row.stuIsjz == '1'?'否-需补种':row.stuIsjz == '-1'?'否-无需补种':'' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="schname" align="center" min-width="120" label="学校名称" showOverflowTooltip />
        <el-table-column prop="academicYear" align="center" min-width="80" label="学年" />
        <el-table-column prop="rounds" align="center" min-width="80" label="轮次">
          <template slot-scope="{ row }">
            <span>{{ row.rounds == '1' ? '春季' : '秋季' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="grade" align="center" min-width="100" label="所在年级" />
        <el-table-column prop="clazzName" align="center" min-width="100" label="所在班级" />
        <el-table-column prop="stuNo" align="center" min-width="180" label="受种者编码" />
        <el-table-column prop="stuPhone" align="center" min-width="120" label="手机号码" />
        <el-table-column prop="stuName" align="center" min-width="120" label="儿童姓名" />
        <el-table-column prop="stuSex" align="center" min-width="120" label="性别"></el-table-column>
        <el-table-column prop="stuBirth" align="center" min-width="180" label="出生日期" />
        <el-table-column prop="stuIsjzz" align="center" min-width="120" label="是否有接种证">
          <template slot-scope="{ row }">
            <span>{{ row.stuIsjzz == '0' || row.stuIsbjzz == '0'?'是': (row.stuIsjzz == '1' && row.stuIsbjzz == '1') || (row.stuIsjzz == '1' && row.stuIsbjzz == null) || row.stuIsjz == '2'?'否':''}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stuImuno" align="center" min-width="180" label="儿童条码" />
        <el-table-column prop="stuType" align="center" min-width="120" label="户籍类型">
          <template slot-scope="{ row }">
            <span>{{ row.stuType == '1' ? '常住' : row.stuType == '2'?'县区内流动':row.stuType == '3'?'地市内流动':row.stuType == '4'?'省内流动':row.stuType == '5'?'国内流动': row.stuType =='6'?'国外流动':'' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="firstStuSmrq" align="center" min-width="180" label="查验日期" />
        <el-table-column prop="d001" align="center" min-width="70" label="卡介苗">
          <template slot-scope="{ row }">
            <span :style="{ color: row.d001 == '2' ? '#3385ff' : row.d001 == '0' ? 'red' : '' }">{{ row.d001 == '0' ? 'X': row.d001== '1' || row.d001 == '2' ? '√' : row.d001 == '-1'?'O':'' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="乙肝疫苗">
          <el-table-column align="center" label="1" prop="d063" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d063 == '2' ? '#3385ff' : row.d063 == '0' ? 'red' : '' }">{{ row.d063 == '0' ?
                'X' :
                row.d063 == '1' || row.d063 == '2' ? '√' : row.d063 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d064" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d064 == '2' ? '#3385ff' : row.d064 == '0' ? 'red' : '' }">{{ row.d064 == '0' ?
                'X' :
                row.d064 == '1' || row.d064 == '2' ? '√' : row.d064 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="3" prop="d065" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d065 == '2' ? '#3385ff' : row.d065 == '0' ? 'red' : '' }">{{ row.d065 == '0' ?
                'X' :
                row.d065 == '1' || row.d065 == '2' ? '√' : row.d065 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="脊灰疫苗">
          <el-table-column align="center" label="1" prop="d009" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d009 == '2' ? '#3385ff' : row.d009 == '0' ? 'red' : '' }">{{ row.d009 == '0' ?
                'X' :
                row.d009 == '1' || row.d009 == '2' ? '√' : row.d009 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d010" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d010 == '2' ? '#3385ff' : row.d010 == '0' ? 'red' : '' }">{{ row.d010 == '0' ?
                'X' :
                row.d010 == '1' || row.d010 == '2' ? '√' : row.d010 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="3" prop="d011" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d011 == '2' ? '#3385ff' : row.d011 == '0' ? 'red' : '' }">{{ row.d011 == '0' ?
                'X' :
                row.d011 == '1' || row.d011 == '2' ? '√' : row.d011 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="4" prop="d012" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d012 == '2' ? '#3385ff' : row.d012 == '0' ? 'red' : '' }">{{ row.d012 == '0' ?
                'X' :
                row.d012 == '1' || row.d012 == '2' ? '√' : row.d012 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="百白破疫苗">
          <el-table-column align="center" label="1" prop="d015" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d015 == '2' ? '#3385ff' : row.d015 == '0' ? 'red' : '' }">{{ row.d015 == '0' ?
                'X' :
                row.d015 == '1' || row.d015 == '2' ? '√' : row.d015 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d016" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d016 == '2' ? '#3385ff' : row.d016 == '0' ? 'red' : '' }">{{ row.d016 == '0' ?
                'X' :
                row.d016 == '1' || row.d016 == '2' ? '√' : row.d016 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="3" prop="d017" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d017 == '2' ? '#3385ff' : row.d017 == '0' ? 'red' : '' }">{{ row.d017 == '0' ?
                'X' :
                row.d017 == '1' || row.d017 == '2' ? '√' : row.d017 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="4" prop="d018" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d018 == '2' ? '#3385ff' : row.d018 == '0' ? 'red' : '' }">{{ row.d018 == '0' ?
                'X' :
                row.d018 == '1' || row.d018 == '2' ? '√' : row.d018 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="d037" align="center" label="白破疫苗" min-width="100">
          <template slot-scope="{ row }">
            <span :style="{ color: row.d037 == '2' ? '#3385ff' : row.d037 == '0' ? 'red' : '' }">{{ row.d037 == '0' ? 'X'
              : row.d037
                == '1' || row.d037 == '2' ? '√' : row.d037 == '-1'?'O':'' }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="麻腮风疫苗">
          <el-table-column align="center" label="1" prop="d059" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d059 == '2' ? '#3385ff' : row.d059 == '0' ? 'red' : '' }">{{ row.d059 == '0' ?
                'X' :
                row.d059 == '1' || row.d059 == '2' ? '√' : row.d059 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d060" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d060 == '2' ? '#3385ff' : row.d060 == '0' ? 'red' : '' }">{{ row.d060 == '0' ?
                'X' :
                row.d060 == '1' || row.d060 == '2' ? '√' : row.d060 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="流脑疫苗">
          <el-table-column align="center" label="A群流脑疫苗">
            <el-table-column align="center" label="1" prop="d040" min-width="50">
              <template slot-scope="{ row }">
                <span :style="{ color: row.d040 == '2' ? '#3385ff' : row.d040 == '0' ? 'red' : '' }">{{ row.d040 == '0' ?
                  'X' :
                  row.d040 == '1' || row.d040 == '2' ? '√' : row.d040 == '-1'?'O':'' }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="2" prop="d041" min-width="50">
              <template slot-scope="{ row }">
                <span :style="{ color: row.d041 == '2' ? '#3385ff' : row.d041 == '0' ? 'red' : '' }">{{ row.d041 == '0' ?
                  'X' :
                  row.d041 == '1' || row.d041 == '2' ? '√' : row.d041 == '-1'?'O':'' }}</span>
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column align="center" label="A+C群流脑疫苗">
            <el-table-column align="center" label="1" prop="d045" min-width="70">
              <template slot-scope="{ row }">
                <span :style="{ color: row.d045 == '2' ? '#3385ff' : row.d045 == '0' ? 'red' : '' }">{{ row.d045 == '0' ?
                  'X' :
                  row.d045 == '1' || row.d045 == '2' ? '√' : row.d045 == '-1'?'O':'' }}</span>
              </template>
            </el-table-column>
            <el-table-column align="center" label="2" prop="d046" min-width="70">
              <template slot-scope="{ row }">
                <span :style="{ color: row.d046 == '2' ? '#3385ff' : row.d046 == '0' ? 'red' : '' }">{{ row.d046 == '0' ?
                  'X' :
                  row.d046 == '1' || row.d046 == '2' ? '√' : row.d046 == '-1'?'O':'' }}</span>
              </template>
            </el-table-column>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="乙脑疫苗">
          <el-table-column align="center" label="1" prop="d032" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d032 == '2' ? '#3385ff' : row.d032 == '0' ? 'red' : '' }">{{ row.d032 == '0' ?
                'X' :
                row.d032 == '1' || row.d032 == '2' ? '√' : row.d032 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d033" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d033 == '2' ? '#3385ff' : row.d033 == '0' ? 'red' : '' }">{{ row.d033 == '0' ?
                'X' :
                row.d033 == '1' || row.d033 == '2' ? '√' : row.d033 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="d069" align="center" label="甲肝灭活疫苗" min-width="100">
          <!-- <template slot-scope="{ row }">
            <span :style="{color:row.d069 == '2'?'#3385ff':row.d069 == '0'?'red':''}">{{ row.d069 == '0' ? 'X' : row.d069 == '1' || row.d069 == '2'?'√':'' }}</span>
          </template> -->
          <el-table-column align="center" label="1" prop="d073" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d073 == '2' ? '#3385ff' : row.d073 == '0' ? 'red' : '' }">{{ row.d073 == '0' ?
                'X' :
                row.d073 == '1' || row.d073 == '2' ? '√' : row.d073 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d074" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d074 == '2' ? '#3385ff' : row.d074 == '0' ? 'red' : '' }">{{ row.d074 == '0' ?
                'X' :
                row.d074 == '1' || row.d074 == '2' ? '√' : row.d074 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center" label="*水痘疫苗">
          <el-table-column align="center" label="1" prop="d050" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d050 == '2' ? '#3385ff' : row.d050 == '0' ? 'red' : '' }">{{ row.d050 == '0' ?
                'X' :
                row.d050 == '1' || row.d050 == '2' ? '√' : row.d050 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="2" prop="d132" min-width="50">
            <template slot-scope="{ row }">
              <span :style="{ color: row.d132 == '2' ? '#3385ff' : row.d132 == '0' ? 'red' : '' }">{{ row.d132 == '0' ?
                'X' :
                row.d132 == '1' || row.d132 == '2' ? '√' : row.d132 == '-1'?'O':'' }}</span>
            </template>
          </el-table-column>
        </el-table-column>
      </ss-table>
      <div class="tags" style="position: absolute;bottom: 30px;" v-if="dataList.length > 0">
        <!-- <div><span></span>校医录入</div> -->
        <div><span></span>需补种</div>
        <div><span></span>需补证</div>
      </div>
    </div>
    <!-- <customize-table :data="dataList" :columnData="columnData" ref="tabPage" class="vaccination" :pageSize="20" :changeNull="true" @goPage="search" @rowOperateFun="rowOperateFun" :pagenum="pagenum" :total="total"></customize-table> -->
    <export-dialog @callback="exportExcel" @handleClose="handleExportClose" :total="total" :typeStr="'班级花名册查询报表'"
      :dialogVisible="exportDialogVisible"></export-dialog>
    <formwork-dialog :visible="dialogVisible" @close="close" :formData="formData" />
    <!-- <check-dialog :visible="checkDialogVisible" @close="closeCheck" /> -->
    <!-- 详细信息修改弹框 -->
    <alter-dialog :visible="alterdialogVisible" :rowkey="rowkey" @close="closeAlter" />
    <!-- 打印建档通知单 -->
    <print-noice :visible="dialogVisible2" @close="closeModal" :noiceList="noiceList" :isAfter="true" title="预防接种证查验疫苗建档联系通知单" />
  </div>
</template>
<script>
import { queryList, deletes, check, output, rqueryReplantCard, rqueryReplantMess, rqueryNoArchive } from '@/utils/axios/apis/roster/index'
import commonOptions from '@/mixin/commonOptions'
import { queryNoticeMess } from '@/utils/axios/apis/notice/index'
// import { getLevel } from '@/utils/common/area.js'
import exportDialog from '@/components/common/exportDialog'
import formworkDialog from './components/qformworkDialog.vue'
// import checkDialog from './components/checkDialog.vue'
import alterDialog from './components/alertDialog.vue'
// import { tableHeader } from './tableHeader'
import printNoice from '../manualAddition/components/printNoice.vue'
export default {
  name: 'vaccinationInspection',
  components: {
    exportDialog,
    formworkDialog,
    printNoice,
    alterDialog
  },
  mixins: [commonOptions],
  data() {
    return {
      dataList: [],
      secTime: 0,
      tipText: '统计截止到：' + this.$util.today() + '。',
      pagesize: 20,
      total: 0,
      pagenum: 1,
      exportDialogVisible: false,
      alterdialogVisible: false,
      dialogVisible: false,
      /* 以下是弹框详细 */
      detailDialog: false,
      detailParams: {},
      exportAreaName: '',
      cModId: this.$store.state.moduleId,
      selectResiTypeFlag: true,
      unitManageForm: {
        status: '',
        isjz: '',
        isjzTemp:'',
        name:'',
        idcard:'',
        code:''
      },
      maininfo: {},
      checkDialogVisible: false,
      dialogVisible2: false,
      rowkey: '',
      formData: {},
      stuData: [],
      noiceList: [],
      gradeList: this.$util.getGrade(2),
      tipList: [
        {
          text: '需补种',
          style: { background: '#E8FFE8' },
          stuIsjz: '1',
          className: 'row-green'
        },
        {
          text: '未建档',
          style: { background: '#E9FFFF' },
          stuIsjz: '2',
          className: 'row-yellow'
        },
        // {
        //   text: '信息系统提供',
        //   style: { background: '#FFFDD9' },
        //   stuSourceid: '0',
        //   className: 'row-yellow'
        // }
      ],
    }
  },
  methods: {
    printNoiceHandler() {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.noiceList = []
      let params = JSON.parse(JSON.stringify(this.searchData))
      console.log(params)
      params.rowkey = this.multipleSelection.join(',')
      queryNoticeMess({
        noticeType: 2,
        schId: this.$store.state.schId
      }).then(res => {
        if (this.stuIsjzOrigin.includes("0") || this.stuIsjzOrigin.includes("1") || this.stuIsjzOrigin.includes("-1") || this.stuIsjzOrigin.includes("")) {
          this.$util.errorMsg('存在已建档的数据，无需打印！')
          return
        }
        this.noticeContent = res.data[0].noticeContent
        rqueryNoArchive(params).then(res => {
          console.log(res.data)
          this.noiceList = res.data.list
          this.noiceList = this.noiceList.map(item => {
            return {
              stuName: item.stuName,
              schoolName: item.schName,
              gradeName: this.gradeList.filter(sub => sub.code == item.claLevel)[0].name,
              className: item.claName,
              stuParentname: item.parentName,
              schStationName: item.belongSchoolStation,
              noticeContent: this.noticeContent,
              stuBirth: item.stuBirth
            }
          })
          this.dialogVisible2 = true;
        })
      })
    },
    mobanHandler(title, maintype) {
      if (!this.multipleSelection || this.multipleSelection.length === 0) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.formData = {
        title: title + '通知单',
        maintype: maintype
      }
      let params = {
        schId: this.searchData.schId,
        noticeType: maintype,
        operateType: this.$consts.OPTYPE_QUERY,
      }
      queryNoticeMess(params).then(res => {
        this.formData.content = res.data[0].noticeContent
        this.formData = Object.assign({}, this.formData, {
          'content': res.data[0].noticeContent,
        })
      }).catch(() => {
      })
      let paramsNew = {
        ...this.searchData,
        rowkey: this.multipleSelection.join(',')
      }
      if (maintype == 0) {
        this.maininfo.customcontent = true
        this.maininfo.isZiFei = true
        if (this.stuIsjz.includes("2")) {
          this.$util.errorMsg('选中的受种者存在无接种证状态，请先去补证！')
          return
        }
        if (this.stuIsjz.includes("-1")) {
          this.$util.errorMsg('选中的受种者无需补种，请重新选择！')
          return
        }
        if (this.stuIsjz.includes("0")) {
          this.$util.errorMsg('选中的受种者存在已种全状态，请重新选择！')
          return
        }
        rqueryReplantMess({ ...paramsNew }).then((res) => {
          for(let i=0;i<res.data.list.length;i++){
            let arr = []
            for (let key in res.data.list[i].replantMap) {
              arr.push({ name: key, value: res.data.list[i].replantMap[key] })
            }
            res.data.list[i].tableData = arr
          }
          this.formData = Object.assign({}, this.formData, {
            'stuData': [...res.data.list, ...this.formData],
          })
          
          this.$nextTick(() => {
            this.dialogVisible = true;
          })
        }).catch(() => {
        })
      }
      if (maintype == 1) {
        // if (this.stuIsjz.includes("2")) {
        //   this.$util.errorMsg('选中的受种者存在无接种证状态，请先去补证！')
        //   return
        // }
        if (this.stuIsjzz.includes("0") || this.stuIsbjzz.includes("0")) {
          this.$util.errorMsg('已有接种证，无需打印！')
          return
        }
        rqueryReplantCard({ ...paramsNew }).then((res) => {
          this.formData = Object.assign({}, this.formData, {
            'stuData': [...res.data.list, ...this.formData],
          })
          this.$nextTick(() => {
            this.dialogVisible = true;
          })
        }).catch(() => {
        })
      }
      if (maintype == 2) {
        rqueryNoArchive({ ...paramsNew }).then((res) => {
          this.formData = Object.assign({}, this.formData, {
            'stuData': [...res.data.list, ...this.formData]
          })
          this.$nextTick(() => {
            this.dialogVisible = true;
          })
        }).catch(() => {
        })
      }
    },
    closeModal() {
      this.dialogVisible2 = false;
    },
    checkHandler(rowkey) {
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      check({ rowkey: rowkey || this.multipleSelection.join(',') }).then((res) => {
        this.$message({
          type: 'success',
          message: res.popMsg || '查验成功！'
        })
        this.search()
        this.closeFullScreen()
      }).catch(() => {
        this.closeFullScreen()
      })
    },
    setConditionFlag(falg) {
      this.$refs.tabPage.setHeight()
    },
    // 查询接口
    async search(pagenum,names) {
      let unitManageForm = this.$refs.selectClass.unitManageForm
      if(this.unitManageForm.name=='' && this.unitManageForm.idcard=='' && this.unitManageForm.code==''){
        let flag = this.$refs['selectClass'].validateForm();
        if (!flag) {
          return false;
        }
      }
      let that = this
      if (!pagenum) {
        pagenum = 1
      }
      that.pagenum = pagenum
      let params = {
        class: unitManageForm.classId,//班级
        code: this.unitManageForm.code,//儿童编码
        grade: unitManageForm.stuClalevel,//年级
        idcard: this.unitManageForm.idcard,//身份证件号码
        isjz: this.unitManageForm.isjz==1?this.unitManageForm.isjzTemp==''?'1,-1':this.unitManageForm.isjzTemp:this.unitManageForm.isjz,//是否种全
        name: this.unitManageForm.name,//儿童姓名
        pageNum: pagenum,
        pageSize: this.pagesize,
        rounds: unitManageForm.lunci,//轮次
        schId: this.$store.state.schId,
        status: this.unitManageForm.status,//查验状态
        year: unitManageForm.stuclayear,//学校类型
        operateType: this.$consts.OPTYPE_QUERY,
      }
      that.searchData = params
      that.openFullScreen()
      queryList(that.searchData).then((res) => {
        this.dataList = res.data.list
        that.secTime = res.sec
        that.total = res.data.total
        if(names=='search'){
          this.$refs.tabPage.$refs.itsmDataTable.clearSelection();
        }
        that.closeFullScreen()
      }).catch(() => {
        that.closeFullScreen()
      })
    },
    handleExportClose() {
      this.exportDialogVisible = false
    },
    exportExcelFun() {
      if (!this.searchData) {
        this.$util.errorMsg('查询后才可以导出！')
        return
      }
      if (this.dataList.length === 0) {
        this.$util.errorMsg('暂无需要导出的数据！')
        return
      }
      this.exportDialogVisible = true
    },
    exportExcel(pageNum) {
      let params = JSON.parse(JSON.stringify(this.searchData))
      params.areaName = '班级花名册查询报表'
      params.schName = this.$store.state.schName
      params.operateType = this.$consts.OPTYPE_OUTPUT
      console.log('导出参数', params)
      output(params)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val.map(item => item.rowkey)
      this.stuIsjz = val.map(item => item.stuIsjz)
      this.stuIsjzz = val.map(item => item.stuIsjzz)
      this.stuIsbjzz = val.map(item => item.stuIsbjzz)
      this.clazzName=new Set(val.map(item=>item.clazzName))
      this.grade=new Set(val.map(item=>item.grade))
      this.stuIsjzOrigin=val.map(item=>item.stuIsjzOrigin)
    },
    // 修改
    alterHandler(rowkey) {
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      if (rowkey === undefined && (this.grade==undefined || this.grade.size>1)) {
        this.$util.errorMsg('请先选中查询页面的年级，再进行勾选批量修改！')
        return false
      }
      if (rowkey === undefined && (this.clazzName==undefined || this.clazzName.size>1)) {
        this.$util.errorMsg('请先选中查询页面的班级，再进行勾选批量修改！')
        return false
      }
      this.rowkey = rowkey || this.multipleSelection.join(',');
      this.$nextTick(() => {
        this.alterdialogVisible = true
      })
    },
    // 删除
    deleteHandler(rowkey) {
      if (rowkey === undefined && (!this.multipleSelection || this.multipleSelection.length === 0)) {
        this.$util.errorMsg('受种者至少选中一个！')
        return false
      }
      this.$confirm('您确定要删除吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletes({ rowkey: rowkey || this.multipleSelection.join(','), operateType: this.$consts.OPTYPE_DELETE }).then((res) => {
          this.$message({
            type: 'success',
            message: res.popMsg || '删除成功'
          })
          this.search()
          this.closeFullScreen()
        }).catch(() => {
          this.closeFullScreen()
        })
      }).catch(() => {
      })
    },
    close() {
      this.dialogVisible = false;
      this.maininfo = {};
    },
    closeCheck() {
      this.checkDialogVisible = false;
    },
    closeAlter() {
      this.alterdialogVisible = false;
      this.rowkey = "";
    }
  }
}
</script>
<style scoped>
.tags {
  position: absolute;
  bottom: 30px;
  display: flex;
}

.tags div {
  margin-right: 20px;
}

.tags span {
  width: 14px;
  height: 14px;
  background: #e8ffe8;
  display: inline-block;
  margin-right: 4px;
  vertical-align: middle;
}

/* .tags div:nth-child(2) span{background: #e8ffe8;} */
.tags div:last-child span {
  background: #f7ffff;
}
</style>
<style>
.querytable .el-table__header-wrapper tr:last-child th:nth-last-child(2),
.querytable .el-table__header-wrapper tr:nth-last-child(2) th:nth-last-child(2),
.querytable .el-table__header-wrapper tr+tr th:nth-last-child(2),
.querytable .el-table__header-wrapper tr th:not(.is-leaf) {
  border-right: 1px solid #E8EDFF !important;
}
.isjzSelect .el-input {
  width: 134px;
}

.isjzSelect {
  min-width: 134px !important;
  margin-left:6px;
}

.isjzSelects .el-input {
  width: 60px;
}

.isjzSelects {
  min-width: 60px !important;
  width: 60px;
}
</style>