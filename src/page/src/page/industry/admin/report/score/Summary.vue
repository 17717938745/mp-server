<template>
  <div>
    <div class="query-container">
        <el-date-picker
            ref="yearPicker"
            v-model="selectedYear"
            type="year"
            placeholder="Select Year"
            @change="handleYearChange"
            :clearable="false"
        />
        <el-select ref="quarterSelect" v-model="selectedQuarter" placeholder="Select Quarter" @change="handleQuarterChange">
          <el-option
              v-for="(quarter, index) in quarters"
              :key="index"
              :label="quarter.label"
              :value="quarter.value"
          />
        </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
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
import {StoreType} from '@/store/Index'
import {ElMessage} from 'element-plus'
import {Plus, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {ListResult, PageResult} from '../../../../../typing/ma/System'
import {formatDate, getMonthStart, getMonthEnd, getQuarterStartMonthString} from '@/util/DateUtil'
import {includes} from "@/util/ArrayUtil";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'evaluationResult', label: '评比 XẾP LOẠI', width: 213},
  {value: 'count', label: '数量 SỐ LƯỢNG', width: 167},
  {value: 'percentFormat', label: '占总数的百分比 CHIẾM % TỔNG SỐ', width: 345},
])
const state = reactive({
  dateTimeList: [getMonthStart(), getMonthEnd()],
  userConfigList: new Array<any>(),
  query: {
    data: {
      orderNo: '',
      saleOrderNo: '',
      orderProject: '',
      poProject: '',
      customerShortName: '',
      purchaseOrderNo: '',
      materialNo: '',
      alreadySend: false,
      startDate: '',
      endDate: '',
    },
  },
  tableData: new Array<any>(),
  config: {
    assemblyTypeList: [],
    departmentList: [],
  },
})
state.dateTimeList = []
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startAssemblyCompleteDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd'
    );
    state.query.data.endAssemblyCompleteDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd'
    );
  } else {
    state.query.data.startAssemblyCompleteDate = ''
    state.query.data.endAssemblyCompleteDate = ''
  }
  handleList()
}
const handleList = () => {
  httpGet(`douson/score/summary-list`, state.query.data).then(
      (res: ListResult<typeof state.tableData>) => {
        state.tableData = res.list
        ElMessage.success("Query success")
      }
  )
}
const yearPicker = ref(null)
const quarterSelect = ref(null)
const selectedQuarter = ref(getQuarterStartMonthString())
const selectedYear = ref(formatDate(new Date(), 'yyyy'))
const quarters = [
  {label: "Q1 (Jan - Mar)", value: "Q1"},
  {label: "Q2 (Apr - Jun)", value: "Q2"},
  {label: "Q3 (Jul - Sep)", value: "Q3"},
  {label: "Q4 (Oct - Dec)", value: "Q4"},
]
const handleQuarterChange = (value) => {
  handleList()
}
const handleYearChange = (value) => {
  handleList()
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
