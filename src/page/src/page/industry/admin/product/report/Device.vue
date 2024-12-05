<template>
  <div>
    <div class="query-container">
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start due date"
          end-placeholder="End due date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
    <div>
      <el-space wrap>
        <el-switch v-model="showMore" active-text="Show more" inactive-text="Hide info" @change="handleToggleMore"/>
      </el-space>
    </div>
    <view-list
        idKey="computerId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleTableRowClassName="handleTableRowClassName"
    >
      <template #operator="row">
      </template>
    </view-list>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage} from 'element-plus'
import {Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {ListResult} from '../../../../../typing/ma/System'
import {formatDate, getMonthStart, getMonthEnd} from '@/util/DateUtil'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'index', labelKey: 'index', width: 51, mergeKey: ['deviceId']},
  {value: 'deviceIdFormat', labelKey: 'device', width: 121, mergeKey: ['deviceId']},
  {value: 'userIdFormat', labelKey: 'user', width: 168,},
  {value: 'deviceReportDateCount', labelKey: 'reportDateCount', width: 87,},
  {value: 'deviceDeviceCompletePercentFormat', labelKey: 'reportDeviceCompletePercent', width: 139,},
  {value: 'deviceDeviceUsePercentFormat', labelKey: 'reportDeviceUsePercent', width: 139,},
  {value: 'devicePercentDiffFormat', labelKey: 'reportDiff', width: 80,},
  {value: 'sumDeviceCompletePercentFormat', labelKey: 'reportSumDeviceCompletePercent', width: 139, mergeKey: ['deviceId']},
  {value: 'sumDeviceUsePercentFormat', labelKey: 'reportSumDeviceUsePercent', width: 139, mergeKey: ['deviceId']},
  {value: 'totalCount', labelKey: 'totalCount', width: 87, mergeKey: ['deviceId']},
  {value: 'userIdCount', labelKey: 'userCount', width: 87, mergeKey: ['deviceId']},
  {value: 'reportDateCount', labelKey: 'reportDateCount', width: 87, mergeKey: ['deviceId']},
  {value: 'deviceCompletePercentFormat', labelKey: 'reportDeviceCompletePercent', width: 139, mergeKey: ['deviceId']},
  {value: 'deviceUsePercentFormat', labelKey: 'reportDeviceUsePercent', width: 139, mergeKey: ['deviceId']},
  {value: 'percentDiffFormat', labelKey: 'reportDiff', width: 80, mergeKey: ['deviceId']},
])
const toggleKeyList = ['userIdFormat', 'deviceReportDateCount', 'deviceDeviceCompletePercentFormat', 'deviceDeviceUsePercentFormat', 'devicePercentDiffFormat',]
const showMore = ref(true)
const handleToggleMore = (v) => {
  columnConfigList.value = columnConfigList.value.map(t => {
    if (toggleKeyList.indexOf(t.value) >= 0) {
      t.hide = !v
    }
    return t
  })
}
handleToggleMore(showMore.value)
const state = reactive({
  dateTimeList: [getMonthStart(), getMonthEnd()],
  userConfigList: new Array<any>(),
  query: {
    data: {
      department: '',
      vocationType: '',
      user: '',
      date: '',
      startReportDate: formatDate(getMonthStart(), 'yyyy-MM-dd'),
      endReportDate: formatDate(getMonthEnd(), 'yyyy-MM-dd'),
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  config: {
    vocationTypeList: [],
    departmentList: [],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startReportDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd'
    );
    state.query.data.endReportDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd'
    );
  } else {
    state.query.data.startReportDate = ''
    state.query.data.endReportDate = ''
  }
  handleList()
}
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (Math.abs(row.percentDiff) >= 0.2) {
    return 'row-error'
  }
  return ''
}
const handleList = () => {
  httpGet(`douson/report/summary/device`, state.query.data).then(
      (res: ListResult<typeof state.tableData>) => {
        state.tableData = res.list
        ElMessage.success("Query success")
      }
  )
}
handleList()
const {
  query,
  tableData,
  config,
  userConfigList,
  dateTimeList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
