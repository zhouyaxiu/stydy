import { regTypeQuery2, regTypeTransform2, queryAllSchoolByAreaCode, findHighClassMess, findClaLevelBySchId, findClassBySchId, findSchoolByAreaAndSchType, findClassMess, findRoundName, findRoundYear, findHighClassYear } from '@/utils/axios/api'
export default {
  data () {
    return {
      // 户籍类型
      resiTypeList: [],
      resiTypeListExceptAll: [],
      // 转化后的户籍
      resiTypeChange: [],
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
      ],
      // 初中-年级  高中-年级
      juniorGradeList: [
        { code: '4', name: '一年级' },
        { code: '5', name: '二年级' },
        { code: '6', name: '三年级' }
      ],
      // 大学-年级
      universityGradeList: [
        { code: '4', name: '一年级' },
        { code: '5', name: '二年级' },
        { code: '6', name: '三年级' },
        { code: '7', name: '四年级' }
      ],
      // 疫苗
      roundNameList: [],
      // 年份
      nowYearList: [],
      selectTypeList2: [
        {
          name: '幼托机构',
          code: '0'
        },
        {
          name: '小学',
          code: '1'
        },
        {
          name: '初中',
          code: '2'
        },
        {
          name: '高中',
          code: '3'
        },
        {
          name: '大学',
          code: '4'
        }
      ],
      schoolYearList: []
    }
  },
  methods: {
    // 户籍类型
    getResiTypeList () {
      let that = this
      regTypeQuery2({}).then((res) => {
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
    async changeRegType (ids) {
      await regTypeTransform2({ regTypes: ids.replace('445,', '').replace('447,', '').replace('447', '') }).then((res) => {
        this.resiTypeChange = res.data
      })
    },
    // 查询学校
    async findSchool (params) {
      await queryAllSchoolByAreaCode(params).then((res) => {
        this.schoolList = res
      })
    },
    // 查询学校
    async findSchoolNew (params) {
      await findSchoolByAreaAndSchType(params).then((res) => {
        this.schoolList = res.data
      })
    },
    // 查询学年
    async findClaYear (params) {
      let currentYear = Number(this.$dateutil.currentYear())
      let arr = []
      for (let i = currentYear - 8; i <= currentYear + 1; i++) {
        arr.push(i)
      }
      this.schoolYearList = arr
    },
    // 查询年级
    async findClaLevel (params) {
      await findClaLevelBySchId(params).then((res) => {
        this.gradeList = res
      })
    },
    // 查询年级
    async findClaLevelNew (params) {
      if (params === '0') {
        this.gradeList = this.kindergartenGradeList
      } else if (params === '1') {
        this.gradeList = this.primaryGradeList
      } else if (params === '2' || params === '3') {
        this.gradeList = this.juniorGradeList
      } else if (params === '4') {
        this.gradeList = this.universityGradeList
      } else {
        this.gradeList = [...this.kindergartenGradeList, ...this.primaryGradeList]
      }
    },
    // 查询班级
    findClass (params) {
      findClassBySchId(params).then((res) => {
        this.classesList = res
      })
    },
    // 查询班级
    async findClassNew (params) {
      if (params != undefined) {
        await findClassMess(params).then((res) => {
          this.classesList = res.data
        })
      } else {
        this.classesList = []
      }
    },
    // 根据班级获取年级
    getClaLevelName (code) {
      let claList = [...this.kindergartenGradeList, ...this.primaryGradeList]
      let claIndex = claList.findIndex(item => item.code === code)
      if (claIndex > -1) {
        let claName = claList[claIndex].name
        return claName
      }
      return ''
    },
    // 查询班级
    async queryHighClassMess (schId, classLevel, classYear) {
      if (schId !== undefined && classLevel != null && classYear != null) {
        let params = {
          schId,
          classLevel,
          classYear
        }
        await findHighClassMess(params).then((res) => {
          this.classesList = res.data
        })
      } else {
        this.classesList = []
      }
    },
    // 获取疫苗
    getRoundName (params) {
      findRoundName(params).then((res) => {
        this.roundNameList = res.data
      }).catch(() => {
      })
    },
    // 获取年份
    getRoundYear (params) {
      findRoundYear(params).then((res) => {
        this.nowYearList = res.data
      }).catch(() => {
      })
    },
    // 根据班级code获取班级名称
    getClaLevelNameNew (val) {
      let claName = ''
      switch (val) {
        case '0':
          claName = '幼儿园托班'
          break
        case '1':
          claName = '幼儿园小班'
          break
        case '2':
          claName = '幼儿园中班'
          break
        case '3':
          claName = '幼儿园大班'
          break
        case '4':
          claName = '一年级'
          break
        case '5':
          claName = '二年级'
          break
        case '6':
          claName = '三年级'
          break
        case '7':
          claName = '四年级'
          break
        case '8':
          claName = '五年级'
          break
        case '9':
          claName = '六年级'
          break
      }
      return claName
    },
    // 根据学校/年级/学年获取班级
    getClasses (params) {
      findHighClassMess(params).then((res) => {
        this.classesList = res.data
      }).catch(() => {
      })
    },
    // 查找高年级学年
    getClassesYear (params) {
      findHighClassYear(params).then((res) => {
        this.schoolYearList = res.data
      }).catch(() => {
      })
    },
    // 查询年级
    async findClaLevelSecond (params) {
      if (params === '0') {
        this.gradeList = this.kindergartenGradeList
      } else if (params === '1') {
        this.gradeList = this.primaryGradeList
      } else if (params === '2' || params === '3') {
        this.gradeList = this.juniorGradeList
      } else if (params === '4') {
        this.gradeList = this.universityGradeList
      } else {
        this.gradeList = [...this.kindergartenGradeList,...this.primaryGradeList]
      }
    }
  },
  mounted () {
    // let that = this
    // 这里最好不要写调接口的方法，此js多地方在调用
  }
}
