import icon from '@/components/common/icon.vue'
import selectAreaItem from '@/components/common/selectAreaItem.vue'
import shadowCard from '@/components/common/shadowCard.vue'
import conditionArea from '@/components/common/conditionArea.vue'
import conditionAreaNew from '@/components/common/conditionAreaNew.vue'
import explainText from '@/components/common/explainText.vue'
import explainTipText from '@/components/common/explainTipText.vue'
import checkResiType from '@/components/common/checkResiType.vue'
import pagination from '@/components/table/pagination.vue'
import customizeTable from '@/components/table/customizeTable'
import customButton from '@/components/button/customButton.vue'
import table from '@/components/table/table.vue'
import dateRange from '@/components/date/dateRange.vue'
import areaTreeStation from '@/components/common/areaTreeStation.vue'
import commonAreaGroup from '@/components/common/commonAreaGroup.vue'
import commonAreaItem from '@/components/common/commonAreaItem.vue'
import pwdInput from '@/components/common/pwdInput.vue'
import customSelect from '@/components/common/customSelect.vue'
import customTitle from '@/components/common/customTitle.vue'
import selectClass from '@/components/common/selectClass.vue'
import vueEasyPrint from "vue-easy-print"

export default {
  install: (Vue) => {
    Vue.component('ss-icon', icon)
    Vue.component('ss-shadow-card', shadowCard)
    Vue.component('ss-select-area-item', selectAreaItem)
    Vue.component('ss-condition-area', conditionArea)
    Vue.component('ss-condition-area-new', conditionAreaNew)
    Vue.component('ss-explain-text', explainText)
    Vue.component('ss-explain-tip-text', explainTipText)
    Vue.component('ss-check-resiType', checkResiType)
    Vue.component('ss-table', table)
    Vue.component('ss-pagination', pagination)
    Vue.component('customize-table', customizeTable)
    Vue.component('ss-custom-button', customButton)
    Vue.component('ss-date-range', dateRange)
    Vue.component('ss-area-areaTreeStation', areaTreeStation)
    Vue.component('ss-common-area-group', commonAreaGroup)
    Vue.component('ss-common-area-item', commonAreaItem)
    Vue.component('ss-pwd-input', pwdInput)
    Vue.component('ss-custom-select', customSelect)
    Vue.component('ss-custom-title', customTitle)
    Vue.component('ss-class-select', selectClass)
    Vue.component('vue-easy-print', vueEasyPrint)
  }
}
