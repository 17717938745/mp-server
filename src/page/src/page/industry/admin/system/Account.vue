<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.nickname" @keyup.enter="handlePage" placeholder="昵称"/>
    </div>
    <el-table
        :header-cell-style="{
        background: ' rgba(250, 251, 252, 1)',
        color: 'rgba(102, 102, 102, 1)',
        height: '54px',
        fontSize: '14px',
        padding: 0,
        border: 'none',
        boxShadow:
          'inset 0px -1px 0px  rgba(238, 238, 238, 1),inset 0px 1px 0px  rgba(238, 238, 238, 1)',
      }"
        :data="tableData"
    >
      <el-table-column
          prop="openId"
          label="OpenId"
          width="320"
          align="left"
      >
      </el-table-column>
      <el-table-column
          prop="state"
          label="状态"
          width="64"
          align="left"
      >
        <template #default="scope">
          {{ scope.row.state === 0 ? '正常' : '冻结' }}
        </template>
      </el-table-column>
      <el-table-column
          prop="nickname"
          label="昵称"
          width="192"
          align="left"
      ></el-table-column>
      <el-table-column
          prop="roleCodeList"
          label="角色"
          width="256"
          align="left"
      >
        <template #default="scope">
          <el-space>
            <el-tag v-for="t in scope.row.roleList" :key="t.roleId" effect="dark">
              {{ t.roleCode }}
            </el-tag>
          </el-space>
        </template>
      </el-table-column>
      <el-table-column prop="url" label="操作">
        <template #default="scope">
          <el-space>
            <el-link
                v-if="includes(roleCodeList, 'admin')"
                :icon="Edit"
                @click="handleState(scope.row, scope.$index)"
                class="mr10"
                type="warning"
            >
              {{ scope.row.state === 0 ? '冻结' : '恢复' }}
            </el-link>
          </el-space>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background
        v-model:currentPage="query.page.page"
        :page-sizes="PAGE_SIZE_LIST"
        :page-size="query.page.limit"
        :total="total"
        @current-change="handlePageChange"
        @size-change="handleLimitChange"
        layout="total, sizes, prev, pager, next, jumper"
    />
  </div>
</template>

<script lang="ts" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {ElMessage} from 'element-plus'
import {Edit} from '@element-plus/icons-vue'
import {httpGet, httpPutJson} from '@/util/HttpUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE, PAGE_SIZE_LIST,} from '@/typing/Common'
import {StoreType} from '@/store/Index'
import {includes} from '@/util/ArrayUtil'

const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const state = reactive({
  query: {
    data: {
      nickname: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  total: 0,
});

const handlePage = () => {
  httpGet(`douson/admin/account/page`, state.query).then(
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
handlePage()
const handleState = (row: any) => {
  const state = row.state === 0 ? 1 : 0
  httpPutJson('douson/admin/account/state', {
    openId: row.openId,
    state: state,
  }).then(() => {
    row.state = state
    ElMessage.success('Update success')
  })
}
const {
  query,
  tableData,
  total,
} = {
  ...toRefs(state),
}
</script>

<style scoped lang="scss">
div {

}
</style>
