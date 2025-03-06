<template>
  <div>
    <div class="query-container">
      <el-date-picker
          v-model="deliveryDateTimeList"
          @change="() => {handleDateTimeChange(deliveryDateTimeList, query.data, 'deliveryDate')}"
          type="week"
          format="YYYY-MM"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.date}`"
          :end-placeholder="`End ${store.state.label.date}`"
      >
      </el-date-picker>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            v-if="editAll"
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <view-list
        idKey="schedulingId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleEditShow="() => {return editAll}"
        :handleUpdate="handleUpdate"
        :handleDelete="'admin' === user.username || editAll ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detail-link="false"
        :handleTableCellClassName="handleTableCellClassName"
    >
      <template #operator="row">
        <el-link
            :icon="CopyDocument"
            @click="() => {
              handleCopyModal(row.param)
            }"
            class="mr10"
            type="info"
        >
          <el-tag size="small" type="info">Copy</el-tag>
        </el-link>
        <el-link
            :icon="More"
            @click="() => {
              query.data.schedulingId = row.param.schedulingId
              handleShowDetail()
            }"
            class="mr10"
            type="info"
        >
          <el-tag size="small">Detail</el-tag>
        </el-link>
      </template>
    </view-list>
    <el-dialog :title="'Scheduling'" v-model="showDetail" width="90%" :close-on-click-modal="false">
      <div class="douson-flex" style="text-align: center; justify-content: center; align-items: center;">
        <h1>{{ tableDetailSummaryData.schedulingTitle }}</h1>
      </div>
      <div class="query-container">
        <el-select v-model="query.data.deviceNumber"
                   @change="handleShowDetail"
                   filterable
                   allow-create
                   clearable
                   :placeholder="store.state.label.testDevice"
                   class="search-item">
          <el-option
              v-for="item in config.testDeviceList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
        <div class="query-btn">
          <el-button :icon="Search" @click="handleShowDetail" type="primary">Search</el-button>
        </div>
      </div>
      <view-list
          idKey="schedulingDetailId"
          :columnConfigList="columnDetailConfigList"
          :list="tableDetailData"
          :handleUpdate="handleUpdateDetail"
          :detail-link="false"
          :handleTableCellClassName="handleTableCellClassName"
      >
        <template #operator="row">
        </template>
      </view-list>
    </el-dialog>
    <el-dialog :title="formState === 0 ? 'Save' : formState === 1 ? 'Update' : 'Copy'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="dateMonth" :label="store.state.label.date">
          <el-date-picker
              type="week"
              v-model="formData.dateMonth"
              format="[Week] ww"
              @change="formData.dateMonth = formatDate(formData.dateMonth, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="scheduleDayTimeLabel" :label="`${store.state.label.scheduleDayTime} label`">
          <el-input v-model="formData.scheduleDayTimeLabel"/>
        </el-form-item>
        <el-form-item prop="scheduleMiddleLabel" :label="`${store.state.label.scheduleMiddle} label`">
          <el-input v-model="formData.scheduleMiddleLabel"/>
        </el-form-item>
        <el-form-item prop="scheduleEveningLabel" :label="`${store.state.label.scheduleEvening} label`">
          <el-input v-model="formData.scheduleEveningLabel"/>
        </el-form-item>
        <el-form-item prop="scheduleDayTime12Label" :label="`${store.state.label.scheduleDayTime12} label`">
          <el-input v-model="formData.scheduleDayTime12Label"/>
        </el-form-item>
        <el-form-item prop="scheduleEvening12Label" :label="`${store.state.label.scheduleEvening12} label`">
          <el-input v-model="formData.scheduleEvening12Label"/>
        </el-form-item>
        <el-form-item prop="scheduleDayTimeTechnologyGroupLabel" :label="`${store.state.label.scheduleDayTimeTechnologyGroup} label`">
          <el-input v-model="formData.scheduleDayTimeTechnologyGroupLabel"/>
        </el-form-item>
        <el-form-item prop="scheduleEveningTechnologyGroupLabel" :label="`${store.state.label.scheduleEveningTechnologyGroup} label`">
          <el-input v-model="formData.scheduleEveningTechnologyGroupLabel"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-affix position="bottom" :offset="20">
          <span class="dialog-footer">
            <el-button @click="formVisible = false">Cancel</el-button>
            <el-button type="primary" @click="handleMerge">Confirm</el-button>
          </span>
        </el-affix>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {onMounted, reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Search, More, UploadFilled, List, CopyDocument,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload, httpDownloadFile,} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'
import {DataResult} from "../../../../typing/ma/System";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const editAll = includes(roleCodeList, 'schedulingManager')
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 203, type: ValueType.Operator,},
  {value: 'dateMonthFormat', labelKey: 'date', width: 201},
  {value: 'scheduleDayTimeLabel', labelKey: 'scheduleMiddle', width: 103},
  {value: 'scheduleMiddleLabel', labelKey: 'scheduleMiddle', width: 103},
  {value: 'scheduleEveningLabel', labelKey: 'scheduleEvening', width: 103},
  {value: 'scheduleDayTime12Label', labelKey: 'scheduleDayTime12', width: 103},
  {value: 'scheduleEvening12Label', labelKey: 'scheduleEvening12', width: 103},
  {value: 'scheduleDayTimeTechnologyGroupLabel', labelKey: 'scheduleDayTimeTechnologyGroup', width: 129},
  {value: 'scheduleEveningTechnologyGroupLabel', labelKey: 'scheduleEveningTechnologyGroup', width: 129},
])
const handleValueHighLight = (row, t) => {
  return includes(highLightUserIdList.value || [], t)
}
const columnDetailConfigList = ref<ViewConfig[]>([
  {value: 'deviceNumberFormat', labelKey: 'deviceNumber', width: 126},
  {value: 'scheduleDayTimeFormat', originValue: 'scheduleDayTimeList', labelKey: 'scheduleDayTime', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
  {value: 'scheduleMiddleFormat', originValue: 'scheduleMiddleList', labelKey: 'scheduleMiddle', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
  {value: 'scheduleEveningFormat', originValue: 'scheduleEveningList', labelKey: 'scheduleEvening', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
  {value: 'scheduleDayTime12Format', originValue: 'scheduleDayTime12List', labelKey: 'scheduleDayTime12', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
  {value: 'scheduleEvening12Format', originValue: 'scheduleEvening12List', labelKey: 'scheduleEvening12', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
  {value: 'scheduleDayTimeTechnologyGroupFormat', originValue: 'scheduleDayTimeTechnologyGroupList', labelKey: 'scheduleDayTimeTechnologyGroup', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
  {value: 'scheduleEveningTechnologyGroupFormat', originValue: 'scheduleEveningTechnologyGroupList', labelKey: 'scheduleEveningTechnologyGroup', width: 129, listShowType: 1, valueHighLight: handleValueHighLight, staticIdList: '-1',},
])
const defaultFormData = {
  dateMonth: '',
  scheduleDayTimeLabel: '',
  scheduleMiddleLabel: '',
  scheduleEveningLabel: '',
  scheduleDayTime12Label: '',
  scheduleEvening12Label: '',
  scheduleDayTimeTechnologyGroupLabel: '',
  scheduleEveningTechnologyGroupLabel: '',
}
const deliveryDateTimeList = ref([])
const tableDetailData = ref([])
const tableDetailSummaryData = ref({})
const highLightUserIdList = ref([])
const state = reactive({
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      schedulingId: '',
      deviceNumber: '',
      scheduleDayTime: '',
      scheduleMiddle: '',
      scheduleEvening: '',
      scheduleDayTime12: '',
      scheduleEvening12: '',
      scheduleDayTimeTechnologyGroup: '',
      scheduleEveningTechnologyGroup: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  formState: 0,
  config: {
    testDeviceList: [],
  },
  userConfigList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    dateMonth: [{required: true, message: 'Please check', trigger: 'blur'}],
  },
})
let startKeyIndex = 1
const columnIndexKey = {

}
columnIndexKey[startKeyIndex++ ] = 'scheduleDayTimeProfessionFormat'
columnIndexKey[startKeyIndex++ ] = 'scheduleMiddleProfessionFormat'
columnIndexKey[startKeyIndex++ ] = 'scheduleEveningProfessionFormat'
columnIndexKey[startKeyIndex++ ] = 'scheduleDayTime12ProfessionFormat'
columnIndexKey[startKeyIndex++ ] = 'scheduleEvening12ProfessionFormat'
columnIndexKey[startKeyIndex++ ] = 'scheduleDayTimeTechnologyGroupProfessionFormat'
columnIndexKey[startKeyIndex++ ] = 'scheduleEveningTechnologyGroupProfessionFormat'
const handleTableCellClassName = ({
                                    row,
                                    column,
                                    rowIndex,
                                    columnIndex,
                                  }: {
  row: any
  rowIndex: number
}) => {
  const k = columnIndexKey[columnIndex]
  if (k && row[k] && row[k].indexOf('组长') >= 0) {
    // return 'cell-red'
  }
  return ''
}
const handleDateTimeChange = (da: any, qd: any, key: string, format: string = 'yyyy-MM-dd hh:mm:ss') => {
  key = key || 'createDate'
  const upfKey = key.charAt(0).toUpperCase() + key.slice(1)
  const sk = `start${upfKey}`
  const ek = `end${upfKey}`
  if (da && da.length > 1) {
    qd[sk] = formatDate(
        da[0],
        format
    )
    qd[ek] = formatDate(
        da[1],
        format
    )
  } else {
    qd[sk] = ''
    qd[ek] = ''
  }
  handlePage()
}
const handlePage = () => {
  httpGet(`/douson/scheduling/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = res.list
        state.total = res.total
        ElMessage.success("Query success")
      }
  )
}
const handlePageChange = (val: number) => {
  state.query.page.page = val
  handlePage()
}
const handleLimitChange = (val: number) => {
  state.query.page.limit = val
  handlePage()
}

Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'device',
    ]
  }),
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  state.config = l[0].data
  userOptionList.value = (l[1].list || []).map((t: any) => {
    return {
      value: t.userId,
      label: t.name,
    }
  })
  columnConfigList.value = columnConfigList.value.map(t => {
    if (editAll) {

    }
    return t
  })
  columnDetailConfigList.value = columnDetailConfigList.value.map(t => {
    if (editAll) {
      if (t.value === 'scheduleDayTimeFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'scheduleMiddleFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'scheduleEveningFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'scheduleDayTime12Format') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'scheduleEvening12Format') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'scheduleDayTimeTechnologyGroupFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'scheduleEveningTechnologyGroupFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      }
    }
    return t
  })
  handlePage()
})
const handleSaveModal = () => {
  state.formData = Object.assign({}, defaultFormData)
  state.formVisible = true
  state.formState = 0
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formState = 1
  state.formData = Object.assign({}, row)
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      handleUpdate(state.formData)
    }
  })
}
const handleUpdate = (row: any) => {
  return httpPutJson('douson/scheduling/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success(`${formState === 0 ? 'Save' : formState === 1 ? 'Update' : 'Copy'} success`)
    handlePage()
  })
}
const handleUpdateDetail = (row: any) => {
  return httpPutJson('douson/scheduling/detail', row).then(() => {
    ElMessage.success('Update success')
    handleShowDetail()
  })
}
const handleCopyModal = (row: any) => {
  state.formVisible = true
  state.formState = 2
  state.formData = Object.assign({}, row, {
    schedulingId: '',
    schedulingCopyId: row.schedulingId,
    dateMonth: '',
  })
}
const handleShowDetail = () => {
  httpGet(`/douson/scheduling/detail`, state.query.data).then(
      (res: DataResult<any>) => {
        showDetail.value = true
        tableDetailData.value = res.data.schedulingList
        highLightUserIdList.value = res.data.highLightUserIdList
        const d = res.data.scheduling
        tableDetailSummaryData.value = d
        columnDetailConfigList.value = columnDetailConfigList.value.map(t => {
          if (t.value === 'scheduleDayTimeFormat') {
            t.labelKey = null
            t.label = d.scheduleDayTimeLabel
          } else if (t.value === 'scheduleMiddleFormat') {
            t.labelKey = null
            t.label = d.scheduleMiddleLabel
          } else if (t.value === 'scheduleEveningFormat') {
            t.labelKey = null
            t.label = d.scheduleEveningLabel
          } else if (t.value === 'scheduleDayTime12Format') {
            t.labelKey = null
            t.label = d.scheduleDayTime12Label
          } else if (t.value === 'scheduleEvening12Format') {
            t.labelKey = null
            t.label = d.scheduleEvening12Label
          } else if (t.value === 'scheduleDayTimeTechnologyGroupFormat') {
            t.labelKey = null
            t.label = d.scheduleDayTimeTechnologyGroupLabel
          } else if (t.value === 'scheduleEveningTechnologyGroupFormat') {
            t.labelKey = null
            t.label = d.scheduleEveningTechnologyGroupLabel
          }
          return t
        })
        ElMessage.success("Query success")
      }
  )
}
const showDetail = ref(false)
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('/douson/scheduling', {
      schedulingId: row.schedulingId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const {
  expandRowKeys,
  query,
  tableData,
  config,
  userConfigList,
  total,
  formData,
  formState,
  formVisible,
  formRuleList,
  photoVisible,
  photoList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
