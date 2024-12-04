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
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'deviceIdFormat', labelKey: 'device', width: 268},
  {value: 'reportDateCount', label: '工作天数', width: 87},
  {value: 'deviceCompletePercentFormat', label: '平均值项:当班绩效，当台的效率/%', width: 256},
  {value: 'deviceUsePercentFormat', label: '平均值项:当班设备利用率', width: 231},
  {value: 'percentDiffFormat', label: '差异比', width: 110},
])
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
