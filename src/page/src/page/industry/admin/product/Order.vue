<template>
  <div>
    <div class="query-container">
      <!--<el-input v-model="query.data.orderId" placeholder="订单编号"/>-->
      <el-input v-model="query.data.orderNo" @keyup.enter="handlePage" :placeholder="store.state.label.orderNo"/>
      <el-input v-model="query.data.projectSequence" @keyup.enter="handlePage" :placeholder="store.state.label.projectSequence"/>
      <el-select v-model="query.data.testDevice"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.testDevice"
                 class="search-item">
        <el-option
            v-for="item in config.testDeviceList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <!--        <el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>

    <view-list
        idKey="orderId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="includes(roleCodeList, 'admin') ? handleEdit : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
    >
    </view-list>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form :rules="formRuleList" :model="formData" ref="formRef" label-width="128px">
        <el-form-item prop="orderNo" :label="store.state.label.orderNo">
          <el-input v-model="formData.orderNo" @change="formData.orderNo = (formData.orderNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="projectSequence" :label="store.state.label.projectSequence">
          <el-input v-model="formData.projectSequence"/>
        </el-form-item>
        <el-form-item prop="testDevice" :label="store.state.label.testDevice">
          <el-select v-model="formData.testDevice"
                     filterable
                     clearable
                     :placeholder="store.state.label.testDevice"
                     class="search-item">
            <el-option
                v-for="item in config.testDeviceList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="processProcedure" :label="store.state.label.processProcedure">
          <el-select v-model="formData.processProcedure"
                     filterable
                     clearable
                     :placeholder="store.state.label.processProcedure">
            <el-option
                v-for="item in config.processProcedureList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="orderCount" :label="store.state.label.orderCount">
          <el-input-number v-model="formData.orderCount" :min="0" :controls="false"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleMerge">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Edit, Search} from '@element-plus/icons-vue'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {includes} from '@/util/ArrayUtil'
import {StoreType} from '@/store/Index'
import {DEFAULT_LIMIT, DEFAULT_PAGE, PAGE_SIZE_LIST,} from '@/typing/Common'
import ViewList from '../../component/ViewList.vue'
import {ValueType, ViewConfig} from "@/typing/industry/ViewItem";

const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const router = useRouter()
const route = useRoute()
const formRef: Ref = ref(null)
const orderId: string = String(route.query.orderId || '')
const state = reactive({
  query: {
    data: {
      orderId: '',
      testDevice: '',
      orderNo: '',
      designNumber: '',
      projectSequence: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  config: {
    processProcedureList: [],
    testDeviceList: [],
  },
  total: 0,
  tableData: new Array<any>(),
  formData: {
    orderId: '',
    orderNo: '',
    projectSequence: '',
    testDevice: '',
    processProcedure: '',
    orderCount: 0,
  },
  formSave: false,
  formVisible: false,
  formRuleList: {
    orderCount: [{required: true, message: '请输入', trigger: 'blur'}],
  },
})


const handleList = () => {
  httpGet(`douson/admin/order/list`, state.query.data).then(
      (res: ListResult<typeof state.tableData>) => {
        state.tableData = res.list
        ElMessage.success("Query success")
      }
  )
}

const handlePage = () => {
  return orderPage('')
}
const orderPage = (orderId: string = '') => {
  state.query.data.orderId = orderId
  httpGet(`douson/admin/order/page`, state.query).then(
      (res: PageResult<any>) => {
        state.total = res.total
        const l = res.list || []
        state.tableData = l
        if (orderId && l.length > 0) {
          state.formVisible = true
          state.formData = Object.assign({}, l[0])
        }
        ElMessage.success("Query success")
      }
  )
}
orderPage(orderId)
const handleSaveModal = () => {
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any, index: number = 0) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/order', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handlePage()
        })
      } else {
        httpPutJson('douson/admin/order', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Update success')
          handlePage()
        })
      }
    }
  })
}
const handlePageChange = (val: number) => {
  state.query.page.page = val
  handlePage()
}
const handleLimitChange = (val: number) => {
  state.query.page.limit = val
  handlePage()
}
httpGet('douson/config').then(r => {
  state.config = r.data
})
const handleDelete = (row: typeof state.tableData) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/order', row)
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const columnConfigList = ref<ViewConfig[]>([
  {value: 'orderNo', labelKey: 'orderNo', width: 128, align: 'left',},
  {value: 'projectSequence', labelKey: 'projectSequence', width: 128, align: 'left',},
  {value: 'testDeviceFormat', labelKey: 'testDevice', width: 160, align: 'left',},
  {value: 'processProcedureFormat', labelKey: 'processProcedure', width: 128, align: 'left',},
  {value: 'orderCount', labelKey: 'orderCount', width: 128, align: 'center',},
  {value: 'designNumber', labelKey: 'designNumber', width: 160, align: 'left',},
  {
    value: 'operator',
    labelKey: 'viewAndEdit',
    width: 289,
    type: ValueType.Operator,
  },
])
const {
  query,
  config,
  total,
  tableData,
  formData,
  formSave,
  formVisible,
  formRuleList,
} = {
  ...toRefs(state),
}
</script>

<style scoped lang="scss">
</style>
