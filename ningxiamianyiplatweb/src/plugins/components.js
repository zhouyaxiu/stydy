import customButton from "@/components/button/CustomButton.vue";
import areaTreeExpand from '@/components/common/AreaTreeExpand.vue'
import areaTreeStation from '@/components/common/AreaTreeStation.vue'
import checkResiType from '@/components/common/CheckResiType.vue';
import conditionArea from "@/components/common/ConditionArea.vue";
import customArea from "@/components/common/CustomArea.vue";
import commonIcon from "@/components/common/ElIcon.vue";
import explainTipText from '@/components/common/ExplainTipText.vue';
import exportDialog from "@/components/common/ExportDialog.vue";
import Icon from "@/components/common/Icon.vue";
import productCode from '@/components/common/ProductCode.vue';
import queryBottom from "@/components/common/QueryBottom.vue";
import queryHeader from "@/components/common/QueryHeader.vue";
import commonSelect from "@/components/common/Select.vue";
import selectVaccineVendor from '@/components/common/SelectVaccineVendor.vue';
import selectAllVaccineVendor from '@/components/common/SelectAllVaccineVendor.vue';

import datePicker from "@/components/datetime/DatePicker.vue";
import daterange from "@/components/datetime/Daterange.vue";
import daterangeNew from "@/components/datetime/DaterangeNew.vue";
import dateTimeRange from "@/components/datetime/DateTimeRange.vue";
import monthrange from "@/components/datetime/Monthrange.vue";
import monthrangeNew from "@/components/datetime/MonthrangeNew.vue";

import radioGroup from "@/components/group/Radio.vue";
import checkboxGroup from "@/components/group/Checkbox.vue";

import commonTable from "@/components/table/CommonTable.vue";
import customTable from "@/components/table/CustomTable.vue";
import multiHeaderTable from "@/components/table/MultiHeaderTable.vue";
import pagination from "@/components/table/Pagination.vue";
import normalTable from "@/components/table/Table.vue";

import treeSelect from "@/components/tree/TreeSelect.vue";
import infoSetting from '@/components/tree/InfoSetting.vue'
import infoSettingShow from '@/components/tree/InfoSettingShow.vue'
import treeTransfer from "@/components/tree/TreeTransfer.vue";
import uploadTxt from "@/components/upload/UploadTxt.vue";
import sourceData from "@/components/common/sourceData.vue";
import customSelect from "@/components/common/customSelect.vue";

export default (app) => {
  app.component("ss-custom-button", customButton);
  app.component("ss-date-picker", datePicker);
  app.component("ss-normal-table", normalTable);
  app.component("ss-table", commonTable);
  app.component("ss-multi-header-table", multiHeaderTable);
  app.component("ss-tree-select", treeSelect);
  app.component("ss-info-setting", infoSetting);
  app.component("ss-info-setting-show", infoSettingShow);
  app.component("ss-common-select", commonSelect);
  app.component("ss-common-icon", commonIcon);
  app.component("ss-icon", Icon);
  app.component("ss-radio-group", radioGroup);
  app.component("ss-checkbox-group", checkboxGroup);
  app.component("ss-query-header", queryHeader);
  app.component("ss-tree-transfer", treeTransfer);
  app.component("ss-upload-txt", uploadTxt);
  app.component("ss-condition-area", conditionArea);
  app.component("ss-custom-area", customArea);
  app.component("ss-date-range", daterange);
  app.component("ss-month-range", monthrange);
  app.component("ss-date-range-new", daterangeNew);
  app.component("ss-month-range-new", monthrangeNew);
  app.component("ss-date-time-range", dateTimeRange);
  app.component("ss-pagination", pagination);
  app.component("ss-area-areaTreeStation", areaTreeStation);
  app.component("ss-area-areaTreeExpand", areaTreeExpand);
  app.component('ss-explain-tip-text', explainTipText);
  app.component("ss-query-bottom", queryBottom);
  app.component("ss-custom-table", customTable);
  app.component("ss-export-dialog", exportDialog);
  app.component("ss-check-resiType", checkResiType);
  app.component("ss-product-code", productCode);
  app.component("ss-vaccine-vendor", selectVaccineVendor);
  app.component("ss-all-vaccine-vendor", selectAllVaccineVendor);
  app.component("ss-source-data", sourceData);
  app.component("ss-custom-select", customSelect);
};
