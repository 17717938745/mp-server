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
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import {ListResult} from "../../../../../typing/ma/System";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'departmentFormat', labelKey: 'department', width: 168},
  {value: 'sumUserCountFormat', labelKey: 'violationUserCount', width: 189},
  {value: 'sumCountFormat', labelKey: 'vocationDays', width: 189},
  {value: 'sumViolationCountFormat', labelKey: 'violationViolationCount', width: 189},
  {value: 'complianceRateFormat', labelKey: 'violationComplianceRate', width: 189},
])
const state = reactive({
  dateTimeList: [],
  userConfigList: new Array<any>(),
  query: {
    data: {
      department: '',
      vocationType: '',
      user: '',
      date: '',
      startDate: '',
      endDate: '',
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
    state.query.data.startDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd'
    );
    state.query.data.endDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd'
    );
  } else {
    state.query.data.startDate = ''
    state.query.data.endDate = ''
  }
  handleList()
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'vocationType',
      'department',
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
  handleList()
})
const handleList = () => {
  httpGet(`douson/admin/vocation/summary-list`, state.query.data).then(
      (res: ListResult<typeof state.tableData>) => {
        state.tableData = res.list
        ElMessage.success("Query success")
      }
  )
}
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
