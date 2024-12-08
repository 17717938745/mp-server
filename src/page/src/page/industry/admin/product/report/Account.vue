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
      <el-select v-model="query.data.userId"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.user"
      >
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
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
        idKey="accountId"
        :columnConfigList="columnConfigList"
        :list="tableData"
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
  {value: 'index', labelKey: 'index', width: 51, mergeKey: ['userId']},
  {value: 'userIdFormat', labelKey: 'user', width: 157, mergeKey: ['userId']},
  {value: 'reportDateCount', labelKey: 'reportDateCount', width: 61, mergeKey: ['userId']},
  {value: 'deviceIdFormat', labelKey: 'deviceNumber', width: 150,},
  {value: 'deviceSumDeviceCompletePercentFormat', labelKey: 'reportSumDeviceCompletePercent', width: 115},
  {value: 'deviceTotalCount', labelKey: 'totalCount', width: 61},
  {value: 'deviceDeviceCompletePercentFormat', labelKey: 'reportAvgDeviceCompletePercent', width: 115},
  {value: 'deviceSalaryFormat', labelKey: 'reportSalary', width: 115},
  {value: 'sumDeviceCompletePercentFormat', labelKey: 'reportSumDeviceCompletePercent', width: 115, mergeKey: ['userId']},
  {value: 'totalCount', labelKey: 'totalCount', width: 61, mergeKey: ['userId']},
  {value: 'deviceCompletePercentFormat', labelKey: 'reportDeviceCompletePercent', width: 115, mergeKey: ['userId']},
  {value: 'salaryFormat', labelKey: 'reportSalary', width: 115, mergeKey: ['userId']},
])
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'device',
      'processProcedure',
    ]
  }),
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  state.config = l[0].data || {}
  userOptionList.value = (l[1].list || []).map((t: any) => {
    return {
      value: t.userId,
      label: t.name,
    }
  })
})
const toggleKeyList = ['deviceIdFormat', 'deviceSumDeviceCompletePercentFormat', 'deviceTotalCount', 'deviceDeviceCompletePercentFormat', 'deviceSalaryFormat', ]
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
      // startReportDate: '',
      // endReportDate: '',
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
const handleList = () => {
  httpGet(`douson/report/summary/account`, state.query.data).then(
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
