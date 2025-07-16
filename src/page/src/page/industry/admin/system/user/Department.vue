<template>
  <div>
    <div class="query-container">
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
    <view-list
        idKey="department"
        :columnConfigList="columnConfigList"
        :handleUpdate="handleUpdate"
        :list="tableData"
    >
    </view-list>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {ElMessage} from 'element-plus'
import {Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpGet, httpPutJson} from '@/util/HttpUtil'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {ListResult} from '../../../../../typing/ma/System'
import {getMonthEnd, getMonthStart} from '@/util/DateUtil'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const config = ref<any>({
  departmentList: [],
})
const columnConfigList = ref<ViewConfig[]>([
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'departmentFormat', labelKey: 'department', width: 168},
  {value: 'workShop', labelKey: 'workShop', width: 123},
  {value: 'office', labelKey: 'office', width: 128},
  {value: 'vietnamCount', labelKey: 'vietnamCount', width: 110},
  {value: 'chinaCount', labelKey: 'chinaCount', width: 110},
  {value: 'total', labelKey: 'total', width: 128},
  {value: 'managerFormat', originValue: 'manager', labelKey: 'manager', width: 238,},
  // {value: 'scheduleNull', labelKey: 'scheduleNull', width: 128},
  // {value: 'scheduleDayTime', labelKey: 'scheduleDayTime', width: 128},
  // {value: 'scheduleDayTime12', labelKey: 'scheduleDayTime12', width: 128},
  // {value: 'scheduleMiddle', labelKey: 'scheduleMiddle', width: 128},
  // {value: 'scheduleEvening', labelKey: 'scheduleEvening', width: 128},
  // {value: 'scheduleEvening12', labelKey: 'scheduleEvening12', width: 128},
])
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'department',
    ]
  }),
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  config.value = l[0].data || {}
  const userMap = {}
  userOptionList.value = (l[1].list || []).map((t: any) => {
    userMap[t.userId] = t.name
    return {
      value: t.userId,
      label: t.name,
    }
  })
  if ('admin' === user.username) {
    columnConfigList.value = columnConfigList.value.map(t => {
      if ('managerFormat' === t.value) {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
        t.editable = (row) => row.department !== '-1'
        t.width = 238
      }
      return t
    })
  }
  handleList()
})
const handleUpdate = (row: any) => {
  console.log(JSON.stringify(row))
  return httpPutJson('douson/admin/param', {
    paramCategoryId: 'department',
    paramCode: row.department,
    expandFirst: row.manager,
  }).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handleList()
  })
}
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
const {
  query,
  tableData,
  userConfigList,
  dateTimeList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
