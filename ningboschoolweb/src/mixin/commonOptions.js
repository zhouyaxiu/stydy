import { getOptions, regTypeQuery, regTypeTransform, findYiMiaodEnterp, queryCrowdKind, findYiMiaoJiCi, queryAllSchoolByAreaCode, findClaYearBySchId, findClaLevelBySchId, findClassBySchId, findClassBySchIdLevelYearAndSeq, findClaLevelBySchIdAndLunci,findClassMess } from '@/utils/axios/api'
export default {
  data() {
    return {
      // 户籍类型
      resiTypeList: [],
      resiTypeListExceptAll: [],
      // 转化后的户籍
      resiTypeChange: [],
      // 受种者标识：全部-1  儿童0  成人 1
      heraStateList: [{ code: '-1', name: '全部' }, { code: '0', name: '儿童' }, { code: '1', name: '成人' }],
      // 迁入地：-1全部   0空  1非空
      qrdList: [{ code: '-1', name: '全部' }, { code: '0', name: '空' }, { code: '1', name: '非空' }],
      // 累计表 - 统计方式： 0截至每月月底数据累计  1截至当前时间前一天数据累计
      tjTypeList: [{ code: '0', name: '截止至每月月底的按月统计' }, { code: '1', name: '截止至当前时间前一天' }],
      // 统计表 - 统计方式： 0截至每月月底数据  1截至当前时间前一天数据
      statisticsTypeList: [{ code: '0', name: '截止至每月月底的按月统计' }, { code: '1', name: '截止至当前时间前一天' }],
      // 统计方式  1任意时段 2月统计表 3双月统计表  4年统计表
      staticDateTypeList: [{ code: '1', name: '任意时段' }, { code: '2', name: '月统计表' }, { code: '3', name: '双月统计表' }, { code: '4', name: '年统计表' }],
      // 业务类型list
      ywTypeList: [],
      // 管理类型list
      glTypeList: [],
      // 疫苗生产企业list
      ymFactoryList: [],
      // 选中的疫苗生产企业
      ymFactorySelected: [],
      // 全部疫苗生产企业list
      ymAllFactoryList: [],
      // 选中的疫苗生产企业
      ymAllFactorySelected: [],
      // 数据来源
      sourceList: [{ code: '-1', name: '全部' }, { code: 'A', name: '手机接种' }, { code: 'C', name: '客户端接种' }],
      // 疫苗制品
      productnoList: [{ code: '-1', name: '全部' }, { code: 'G1', name: 'Vero细胞' }, { code: 'G2', name: 'CHO细胞' }, { code: 'G3', name: '腺病毒载体' }],
      // 疫苗制品
      productnoListNoAll: [{ code: 'G1', name: '新冠疫苗(Vero)' }, { code: 'G2', name: '新冠疫苗(CHO)' }, { code: 'G3', name: '新冠疫苗(腺病毒载体)' }],
      // 疫苗剂次list
      ymjcList: [],
      // 接种剂次list
      tjjcList: [{ code: '-1', name: '全部' }, { code: 'B655', name: '第1剂次' }, { code: 'B656', name: '第2剂次' }, { code: 'B676', name: '第3剂次' }, { code: 'B677', name: '第4剂次' }],
      fbactidList: [{ code: 'B655', name: '1' }, { code: 'B656', name: '2' }, { code: 'B676', name: '3' }, { code: 'B677', name: '4' }],
      // 身份证件类别list
      cardTypeList: [],
      // 性别list
      sexList: [],
      // 门诊类型
      clinicTypeList: [{ code: 'DAY', name: '日' }, { code: 'WEEK', name: '周' }, { code: 'PERIOD', name: '旬' }, { code: 'MOMTH', name: '月' }],
      // 服务类型
      fwTypeList: [{ code: '01', name: '常规' }, { code: '02', name: '产科' }, { code: '03', name: '成人' }, { code: '04', name: '犬伤' }, { code: '05', name: '破伤风接种' }, { code: '06', name: '新冠接种' }],
      //  工作类型
      workTypeList: [{ code: '01', name: '社区卫生服务中心' }, { code: '02', name: '乡镇卫生院' }, { code: '03', name: '社区卫生服务站' }, { code: '04', name: '村卫生室' }, { code: '05', name: '其他' }],
      // 技术职称
      technicalTitleSelect: [
        { id: '1', name: '正主任医师' },
        { id: '2', name: '主管医师' },
        { id: '3', name: '医师' },
        { id: '4', name: '医士' },
        { id: '5', name: '主管护师' },
        { id: '6', name: '护师' },
        { id: '7', name: '护士' },
        { id: '8', name: '防疫员' },
        { id: '9', name: '乡村医生' },
        { id: '10', name: '副主任医师' },
        { id: '11', name: '正主任技师' },
        { id: '12', name: '副主任技师' },
        { id: '13', name: '主管技师' },
        { id: '14', name: '技师' },
        { id: '15', name: '技士' },
        { id: '16', name: '正主任护师' },
        { id: '17', name: '副主任护师' },
        { id: '18', name: '其他' }
      ],
      // 性别
      genderSelect: [
        { id: '1', name: '男' },
        { id: '2', name: '女' }
      ],
      // 专/兼职
      positionTypeSelect: [
        { id: '1', name: '专职' },
        { id: '2', name: '兼职' }
      ],
      // 学历
      degreeSelect: [
        { id: '1', name: '博士' },
        { id: '2', name: '硕士' },
        { id: '3', name: '本科' },
        { id: '4', name: '大专' },
        { id: '5', name: '高中' },
        { id: '6', name: '中专' },
        { id: '7', name: '初中' },
        { id: '8', name: '小学' }
      ],
      // 分析儿童疫苗选择
      ymSelect: { 'B001': '卡介苗1', 'B009': 'OPV1', 'B010': 'OPV2', 'B011': 'OPV3', 'B012': 'OPV4', 'B015': 'DPT1', 'B016': 'DPT2', 'B017': 'DPT3', 'B018': 'DPT4', 'B059': 'MV1', 'B089': 'MMR1', 'B063': 'HBV1', 'B064': 'HBV2', 'B065': 'HBV3', 'B032': '乙脑1', 'B033': '乙脑2', 'B040': '流脑A1', 'B041': '流脑A2', 'B045': '流脑A+C1', 'B046': '流脑A+C2', 'B073': '甲肝灭活1', 'B074': '甲肝灭活2', 'B069': '甲肝减毒1', 'B037': '白破1' },
      //  分析儿童-报表类型
      childReportList: [
        { id: 0, name: '接种率报表' },
        { id: 1, name: '合格率报表' },
        { id: 2, name: '及时率报表' },
        { id: 3, name: '不合格率报表' }
      ],
      // 接种单位管理-管理类型
      manageTypeList: [{ code: '01', name: '疾病预防控制局' }, { code: '02', name: '医政医管局' }, { code: '03', name: '基层卫生健康司' }, { code: '04', name: '妇幼健康司 ' }, { code: '05', name: '国家中医药局医政司' }, { code: '06', name: '其他' }],
      // 接种单位管理-业务类型
      businessTypeList: [{ code: '01', name: '产科预防接种' }, { code: '02', name: '免疫规划疫苗预防接种' }, { code: '03', name: '青少年和成人预防接种' }, { code: '04', name: '狂犬病疫苗接种 ' }, { code: '05', name: '外伤后破伤风疫苗接种' }],
      // 接种单位管理-门诊类别
      organTypeList: [{ txt: '预防接种门诊', value: '1' }, { txt: '成人门诊', value: '2' }, { txt: '犬伤门诊', value: '3' }, { txt: '产科门诊', value: '4' }],
      // 年龄组
      agesList: [
        { id: '0', name: '小于3岁' },
        { id: '3', name: '3~11岁' },
        { id: '12', name: '12~17岁' },
        // { id: '15', name: '15~17岁' },
        { id: '18', name: '18~49岁' },
        { id: '50', name: '50~54岁' },
        { id: '55', name: '55~59岁' },
        { id: '60', name: '60~64岁' },
        { id: '65', name: '65~69岁' },
        { id: '70', name: '70~79岁' },
        { id: '80', name: '≥ 80岁' }
      ],
      // 人群分类
      crowdKindList: [],
      // 双月统计 1-6
      towMonthList: [
        { code: '1', name: '1' },
        { code: '2', name: '2' },
        { code: '3', name: '3' },
        { code: '4', name: '4' },
        { code: '5', name: '5' },
        { code: '6', name: '6' }
      ],
      // 受种者状态
      vaccinatedStatus: [{ val: '01', text: '在册' }, { val: '05', text: '失访' }, { val: '03', text: '死亡' }],
      // 受种者状态-不包含死亡
      vaccinatedStatusWithoutDead: [{ val: '01', text: '在册' }, { val: '05', text: '失访' }],
      // 学校类型下拉选项
      schoolTypeOPTS: [{ label: '全部', value: 'all' }, { label: '小学', value: '1' }, { label: '幼托机构', value: '0' }],
      // 学校
      schoolList: [],
      // 学年
      schoolYearList: [],
      // 年级
      gradeList: [],
      // 班级
      classesList: [],
      // 幼托机构-年级
      kindergartenGradeList: [
        { code: '0', name: '幼儿园托班' },
        { code: '1', name: '幼儿园小班' },
        { code: '2', name: '幼儿园中班' },
        { code: '3', name: '幼儿园大班' }
      ],
      // 小学-年级
      primaryGradeList: [
        { code: '4', name: '一年级' },
        { code: '5', name: '二年级' },
        { code: '6', name: '三年级' },
        { code: '7', name: '四年级' },
        { code: '8', name: '五年级' },
        { code: '9', name: '六年级' }
      ]
    }
  },
  methods: {
    // 性别
    getSexList() {
      let that = this
      getOptions({
        type: this.$consts.SEX
      }).then((res) => {
        that.sexList = res
      })
    },
    // 身份证件类别
    getCardTypeList() {
      let that = this
      getOptions({
        type: this.$consts.FIDCARDTYPE
      }).then((res) => {
        that.cardTypeList = res
      })
    },
    // 户籍类型
    getResiTypeList() {
      let that = this
      regTypeQuery({}).then((res) => {
        that.resiTypeList = res.data
        that.resiTypeListExceptAll = []
        that.resiTypeList.forEach(item => {
          that.resiTypeListExceptAll.push(item)
        })
        if (that.resiType) {
          that.resiType = []
          that.resiTypeList.forEach(item => {
            that.resiType.push(item.id)
          })
        }
      })
    },
    // 转化户籍
    async changeRegType(ids) {
      await regTypeTransform({ regTypes: ids.replace('445,', '').replace('447,', '').replace('447', '') }).then((res) => {
        this.resiTypeChange = res.data
      })
    },
    changeResiType(value) {
      alert(value)
    },
    // 业务类型
    getYwTypeList() {
      let that = this
      that.ywTypeList.push({ code: '01', name: '产科预防接种' })
      that.ywTypeList.push({ code: '02', name: '免疫规划疫苗预防接种' })
      that.ywTypeList.push({ code: '03', name: '青少年和成人预防接种' })
      that.ywTypeList.push({ code: '04', name: '狂犬病疫苗接种' })
      that.ywTypeList.push({ code: '05', name: '外伤后破伤风疫苗接种' })
      if (that.ywType) {
        that.ywType = []
        that.ywTypeList.forEach(item => {
          that.ywType.push(item.code)
        })
      }
    },
    // 管理类型
    getGlTypeList() {
      let that = this
      that.glTypeList.push({ code: '01', name: '疾病预防控制局' })
      that.glTypeList.push({ code: '02', name: '医政医管局' })
      that.glTypeList.push({ code: '03', name: '基层卫生健康司' })
      that.glTypeList.push({ code: '04', name: '妇幼健康司' })
      that.glTypeList.push({ code: '05', name: '国家中医药局医政司' })
      that.glTypeList.push({ code: '06', name: '其他' })
      if (that.glType) {
        that.glType = []
        that.glTypeList.forEach(item => {
          that.glType.push(item.code)
        })
      }
    },
    // 疫苗生产企业
    getYmFactoryList() {
      let that = this
      getOptions({
        type: this.$consts.MANUFACTORY
      }).then((res) => {
        res.forEach(i => {
          if (i.code) that.ymFactorySelected.push(i.code)
        })
        that.ymFactoryList = res
      })
    },

    // 疫苗全部生产企业
    getYmAllFactoryList() {
      let that = this
      findYiMiaodEnterp({}).then((res) => {
        res.dataYiMaioQiYe.forEach(i => {
          if (i.enterpno) that.ymAllFactorySelected.push(i.enterpno)
        })
        that.ymAllFactoryList = res.dataYiMaioQiYe
      })
    },
    // 疫苗剂次
    getYmjcList() {
      let that = this
      that.ymjcList = []
      findYiMiaoJiCi().then((res) => {
        console.log(res)
        that.ymjcList = res.dataYiMaioJiCi.map(item => {
          return {
            code: item.fBactID,
            name: item.fBactShortName + item.fBactSeqName
          }
        })
      })
      // that.ymjcList.push({ code: 'HG_B063', name: '乙肝1' })
      // that.ymjcList.push({ code: 'HG_B064', name: '乙肝2' })
      // that.ymjcList.push({ code: 'HG_B065', name: '乙肝3' })
      // that.ymjcList.push({ code: 'HG_B001', name: '卡介苗1' })
      // that.ymjcList.push({ code: 'HG_OPV_1', name: '脊灰1' })
      // that.ymjcList.push({ code: 'HG_OPV_2', name: '脊灰2' })
      // that.ymjcList.push({ code: 'HG_OPV_3', name: '脊灰3' })
      // that.ymjcList.push({ code: 'HG_OPV_4', name: '脊灰4' })
      // that.ymjcList.push({ code: 'HG_DPT_1', name: '百白破1' })
      // that.ymjcList.push({ code: 'HG_DPT_2', name: '百白破2' })
      // that.ymjcList.push({ code: 'HG_DPT_3', name: '百白破3' })
      // that.ymjcList.push({ code: 'HG_DPT_4', name: '百白破4' })
      // that.ymjcList.push({ code: 'HG_B037', name: '白破1' })
      // that.ymjcList.push({ code: 'HG_MV_1', name: '麻疹类1' })
      // that.ymjcList.push({ code: 'HG_MV_2', name: '麻疹类2' })
      // that.ymjcList.push({ code: 'HG_B040', name: '流脑A1' })
      // that.ymjcList.push({ code: 'HG_B041', name: '流脑A2' })
      // that.ymjcList.push({ code: 'HG_B045', name: '流脑A+C1' })
      // that.ymjcList.push({ code: 'HG_B046', name: '流脑A+C2' })
      // that.ymjcList.push({ code: 'HG_B032', name: '乙脑1' })
      // that.ymjcList.push({ code: 'HG_B033', name: '乙脑2' })
      // that.ymjcList.push({ code: 'HG_B069', name: '甲肝1' })
    },
    // 全部年龄组
    getAllAges() {
      let arr = []
      this.agesList.forEach(item => {
        arr.push(item.id)
      })
      return arr
    },
    // 查询人群分类
    queryCrowdKind() {
      let that = this
      queryCrowdKind().then((res) => {
        res.forEach(item => {
          item.checkedState = false
        })
        that.crowdKindList = res
      })
    },
    // 查询学校
    async findSchool(params) {
      await queryAllSchoolByAreaCode(params).then((res) => {
        this.schoolList = res
      })
    },
    // 查询学年
    async findClaYear(params) {
      // await findClaYearBySchId(params).then((res) => {
      //   this.schoolYearList = res
      // })
      let currentYear = Number(this.$dateutil.currentYear());
      let arr = [];
      for (let i = currentYear - 8; i <= currentYear + 1; i++) {
        arr.push(i)
      }
      this.schoolYearList = arr
    },
    // 查询年级
    async findClaLevel(params) {
      await findClaLevelBySchId(params).then((res) => {
        this.gradeList = res
      })
    },
    // 查询年级
    async findClaLevelNew(params) {
      // await findClaLevelBySchIdAndLunci(params).then((res) => {
      //   this.gradeList = res
      // })
      if (this.$store.state.schType === '1') {
        this.gradeList = this.primaryGradeList
      } else {
        this.gradeList = this.kindergartenGradeList
      }
    },
    // 查询班级
    async findClass(params) {
      await findClassBySchId(params).then((res) => {
        this.classesList = res
      })
    },
    // 查询班级
    async findClassNew(params) {
      // await findClassBySchIdLevelYearAndSeq(params).then((res) => {
      //   this.classesList = res
      // })
      if(params!=undefined){
        await findClassMess(params).then((res) => {
          this.classesList = res.data
        })
      }else{
        this.classesList = []
      }
    },
    // 根据班级获取年级
    getClaLevelName(code){
      let claList=[...this.kindergartenGradeList,...this.primaryGradeList]
      let claIndex=claList.findIndex(item=>item.code==code)
      if(claIndex>-1){
        let claName= claList[claIndex].name
        return claName
      }
      return ''
    }
  },
  mounted() {
    // let that = this
    // 这里最好不要写调接口的方法，此js多地方在调用
  }
}
