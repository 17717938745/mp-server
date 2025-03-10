<template>
  <div>
    <div class="query-container">
      <el-date-picker
          v-model="dateTimeList"
          @change="() => {handleDateTimeChange(dateTimeList, query.data, 'oilInjectionCompleteDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.oilInjectionCompleteDate}`"
          :end-placeholder="`End ${store.state.label.oilInjectionCompleteDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
    <el-alert :title="`${store.state.label.assemblySummaryTips}`" type="info" :closable="false"/>
    <view-list
        idKey="assemblyId"
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
import {formatDate, getMonthStart, getMonthEnd, getYesterday} from '@/util/DateUtil'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'assemblyCompleteDateFormat', labelKey: 'assemblyCompleteDate', width: 92},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 78},
  {value: 'orderProject', labelKey: 'orderProject', width: 56},
  {value: 'materialNo', labelKey: 'materialNo', width: 109},
  {value: 'orderCount', labelKey: 'orderCount', width: 67},
  {value: 'materialDescription', labelKey: 'materialDescription', width: 258},
  {value: 'designNumber', labelKey: 'designNumber', width: 102},
  {value: 'deliveryDateFormat', labelKey: 'deliveryDate', width: 109},
  {value: 'assemblyCompleteCount', labelKey: 'assemblyCompleteCount', width: 69},
  {value: 'oilInjectionCompleteCount', labelKey: 'oilInjectionCompleteCount', width: 87},
])
const state = reactive({
  dateTimeList: [getYesterday(), getYesterday()],
  userConfigList: new Array<any>(),
  query: {
    data: {
      department: '',
      assemblyType: '',
      user: '',
      date: '',
      startOilInjectionCompleteDate: formatDate(getYesterday(), 'yyyy-MM-dd'),
      endOilInjectionCompleteDate: formatDate(getYesterday(), 'yyyy-MM-dd'),
      startAssemblyCompleteDate: formatDate(getYesterday(), 'yyyy-MM-dd'),
      endAssemblyCompleteDate: formatDate(getYesterday(), 'yyyy-MM-dd'),
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  config: {
    assemblyTypeList: [],
    departmentList: [],
  },
})

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
  handleList()
}
const handleList = () => {
  httpGet(`douson/assembly/summary-list2`, state.query.data).then(
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
