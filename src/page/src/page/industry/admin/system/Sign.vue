<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.username" @keyup.enter="handlePage" :placeholder="store.state.label.username"/>1
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
      </div>
    </div>
    <view-list
        idKey="signInHistoryId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
    >
    </view-list>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {Search} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {httpGet} from '@/util/HttpUtil'
import {PageResult} from '@/typing/ma/System'
import {StoreType} from '@/store/Index'
import ViewList from '../../component/ViewList.vue'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import {DEFAULT_LIMIT, DEFAULT_PAGE} from "@/typing/Common";

const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const columnConfigList = ref<ViewConfig[]>([
  {value: 'deviceId', labelKey: 'deviceNumber', width: 218,},
  {value: 'userId', labelKey: 'user', width: 186,},
  {value: 'username', labelKey: 'username', width: 168,},
  {value: 'signInTime', labelKey: 'signInTime', width: 168,},
  {value: 'clientIp', labelKey: 'clientIp', width: 168,},
  {value: 'successFormat', labelKey: 'isSuccess', width: 168,},
  {value: 'errorMessage', labelKey: 'errorMessage', width: 328, showOverflow: true,},
])
const state = reactive({
  query: {
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
    data: {
      department: '',
      profession: '',
      username: '',
      roleId: '',
      name: '',
      publicIp: null,
      userProperty: '',
      schedule: '',
      state: 0,
    },
  },
  total: 0,
  tableData: new Array<any>(),
})
const handlePage = () => {
  httpGet(`system/sign-in-history/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        state.total = res.total
        state.tableData = (res.list || [])
        ElMessage.success("Query success")
      }
  )
}
handlePage()
const handlePageChange = (val: number) => {
  state.query.page.page = val
  handlePage()
}
const handleLimitChange = (val: number) => {
  state.query.page.limit = val
  handlePage()
}
const {
  query,
  total,
  tableData,
} = {
  ...toRefs(state),
}
</script>

<style scoped lang="scss">
div {

}
</style>
