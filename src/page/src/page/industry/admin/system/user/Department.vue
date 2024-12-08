<template>
  <div>
    <div class="query-container">
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
    <view-list
        idKey="departmentId"
        :columnConfigList="columnConfigList"
        :list="tableData"
    >
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
  {value: 'departmentFormat', labelKey: 'department', width: 168},
  {value: 'workShop', labelKey: 'workShop', width: 123},
  {value: 'office', labelKey: 'office', width: 128},
  {value: 'total', labelKey: 'total', width: 128},
  {value: 'scheduleNull', labelKey: 'scheduleNull', width: 128},
  {value: 'scheduleDayTime', labelKey: 'scheduleDayTime', width: 128},
  {value: 'scheduleDayTime12', labelKey: 'scheduleDayTime12', width: 128},
  {value: 'scheduleMiddle', labelKey: 'scheduleMiddle', width: 128},
  {value: 'scheduleEvening', labelKey: 'scheduleEvening', width: 128},
  {value: 'scheduleEvening12', labelKey: 'scheduleEvening12', width: 128},
])
const state = reactive({
  dateTimeList: [getMonthStart(), getMonthEnd()],
  userConfigList: new Array<any>(),
  query: {
    data: {},
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
})

const handleList = () => {
  httpGet(`system/user/department-summary`, state.query.data).then(
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
