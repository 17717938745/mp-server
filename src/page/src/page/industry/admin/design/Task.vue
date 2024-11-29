<template>
  <div>
    <div class="query-container">
      <el-select v-model="query.data.deviceId"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.device"
                 class="search-item">
        <el-option
            v-for="item in config.testDeviceList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.customerShortName"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.customerShortName"
                class="search-item"/>
      <el-input v-model="query.data.saleOrderNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProjectNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.orderProjectNo"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.improveMaterialDescribe"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.improveMaterialDescribe"
                class="search-item"/>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-input v-model="query.data.orderCount"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.orderCount"
                type="number"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          clearable
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start promise done date"
          end-placeholder="End promise done date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.delay"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.delay"
                type="number"
                class="search-item"/>
      <el-input v-model="query.data.scrapCount"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.scrapCount"
                type="number"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeListSupplier"
          @change="handleDateTimeChangeSupplier"
          clearable
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start supplier promise done date"
          end-placeholder="End supplier promise done date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.nde"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.nde"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.assemble"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.assemble"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.testPress"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.testPress"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.surfaceTreatment"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.surfaceTreatment"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.materialOrderNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.materialOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.checkOrderNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.checkOrderNo"
                class="search-item"/>
      <el-select v-model="query.data.processType"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.processType"
      >
        <el-option
            v-for="item in [{value: 0, label: 'Undone',}, {value: 1, label: 'Done',}, {value: null, label: 'All',}, ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
            :disabled="!includes(roleCodeList, 'material')"
        >Add
        </el-button>
      </div>
    </div>
    <div>
      <el-space wrap>
        <el-switch v-model="showMore" active-text="Show more" inactive-text="Hide info" @change="handleToggleMore"/>
      </el-space>
    </div>
    <view-list
        idKey="taskId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDeleteShow="handleDeleteShow"
        :handleDelete="taskShow ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handleTableRowClassName="handleTableRowClassName"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detailLink="false"
    >
      <template #operator="row">
        <el-link
            v-if="row.param.taskId"
            :icon="DocumentCopy"
            @click="handleCopy(row)"
            class="mr10"
            type="info"
        >
          Copy
        </el-link>
        <el-link
            v-if="row.param.taskId"
            :icon="ArrowUp"
            @click="handleUp(row)"
            class="mr10"
            type="info"
            :disabled="!(row.param.up)"
        >
          Up
        </el-link>
        <el-link
            v-if="row.param.taskId"
            :icon="ArrowDown"
            @click="handleDown(row)"
            class="mr10"
            type="info"
            :disabled="!(row.param.down)"
        >
          Down
        </el-link>
      </template>
    </view-list>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="deviceId" :label="store.state.label.device">
          <el-select v-model="formData.deviceId"
                     filterable
                     clearable
                     :placeholder="store.state.label.device"
                     class="search-item">
            <el-option
                v-for="item in config.testDeviceList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="planReformCount" :label="store.state.label.planReformCount" v-if="taskShow">
          <el-input-number v-model="formData.planReformCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="supplierRemark" :label="store.state.label.supplierRemark" v-if="taskShow">
          <el-input v-model="formData.supplierRemark" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item prop="processWorkingHour" :label="store.state.label.processWorkingHour" v-if="taskShow">
          <el-input-number v-model="formData.processWorkingHour" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="onlineDate" :label="store.state.label.onlineDate" v-if="taskShow">
          <el-date-picker
              type="date"
              v-model="formData.onlineDate"
              format="YYYY-MM-DD"
              @change="formData.onlineDate = formatDate(formData.onlineDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="processCount" :label="store.state.label.processCount" v-if="taskShow">
          <el-input-number v-model="formData.processCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="processProcedure" :label="store.state.label.processProcedure" v-if="taskShow">
          <el-select v-model="formData.processProcedureList"
                     filterable
                     clearable
                     multiple
                     :placeholder="store.state.label.processProcedure"
                     class="search-item">
            <el-option
                v-for="item in config.processProcedureList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="supplierDoneDate" :label="store.state.label.supplierDoneDate" v-if="supplierShow">
          <el-date-picker
              type="date"
              v-model="formData.supplierDoneDate"
              format="YYYY-MM-DD"
              @change="formData.supplierDoneDate = formatDate(formData.supplierDoneDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="deliverCount" :label="store.state.label.deliverCount" v-if="supplierShow">
          <el-input-number v-model="formData.deliverCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="deliverDate" :label="store.state.label.deliverDate" v-if="supplierShow">
          <el-date-picker
              type="date"
              v-model="formData.deliverDate"
              format="YYYY-MM-DD"
              @change="formData.deliverDate = formatDate(formData.deliverDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="receiptCount" :label="store.state.label.receiptCount" v-if="supplierShow">
          <el-input-number v-model="formData.receiptCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="receiptDate" :label="store.state.label.receiptDate" v-if="supplierShow">
          <el-date-picker
              type="date"
              v-model="formData.receiptDate"
              format="YYYY-MM-DD"
              @change="formData.receiptDate = formatDate(formData.receiptDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="scrapCount" :label="store.state.label.scrapCount" v-if="supplierShow">
          <el-input-number v-model="formData.scrapCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="supplierPromiseDoneDate" :label="store.state.label.supplierPromiseDoneDate" v-if="supplierShow">
          <el-date-picker
              type="date"
              v-model="formData.supplierPromiseDoneDate"
              format="YYYY-MM-DD"
              @change="formData.supplierPromiseDoneDate = formatDate(formData.supplierPromiseDoneDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
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
import {StoreType,} from '@/store'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Printer, Search, UploadFilled, DocumentCopy, ArrowUp, ArrowDown,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload,} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'operator', labelKey: 'viewAndEdit', width: 378, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 56,},
  {value: 'deviceIdFormat', labelKey: 'device', width: 121},
  {value: 'customerShortName', labelKey: 'customerShortName', width: 167},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 87},
  {value: 'orderProjectNo', labelKey: 'orderProjectNo', width: 56},
  {value: 'materialNo', labelKey: 'materialNo', width: 146},
  {value: 'improveMaterialDescribe', labelKey: 'improveMaterialDescribe', width: 189},
  {value: 'designNumber', labelKey: 'designNumber', width: 154},
  {value: 'orderCount', labelKey: 'orderCount', width: 65},
  {value: 'roughcastExpireDate', labelKey: 'roughcastExpireDate', width: 102},
  {value: 'materialCount', labelKey: 'materialCount', width: 65},
  {value: 'promiseDoneDate', labelKey: 'promiseDoneDate', width: 102},
  {value: 'planReformCount', labelKey: 'planReformCount', width: 65},
  {value: 'supplierRemark', labelKey: 'supplierRemark', width: 189},
  {value: 'productCountHour8', labelKey: 'productCountHour8', width: 68},
  {value: 'productCountHour12', labelKey: 'productCountHour12', width: 68},
  {value: 'processWorkingHour', labelKey: 'processWorkingHour', width: 68},
  {value: 'onlineDate', labelKey: 'onlineDate', width: 102},
  {value: 'offlineDate', labelKey: 'offlineDate', width: 102},
  {value: 'delay', labelKey: 'delay', width: 68},
  {value: 'processCount', labelKey: 'processCount', width: 68},
  {value: 'processProcedureFormat', labelKey: 'processProcedure', width: 131},
  {value: 'supplierDoneDate', labelKey: 'supplierDoneDate', width: 102},
  {value: 'deliverCount', labelKey: 'deliverCount', width: 68},
  {value: 'deliverDate', labelKey: 'deliverDate', width: 102},
  {value: 'receiptCount', labelKey: 'receiptCount', width: 68},
  {value: 'receiptDate', labelKey: 'receiptDate', width: 102},
  {value: 'scrapCount', labelKey: 'scrapCount', width: 68},
  {value: 'supplierPromiseDoneDate', labelKey: 'supplierPromiseDoneDate', width: 102},
  {value: 'nde', labelKey: 'nde', width: 56},
  {value: 'assemble', labelKey: 'assemble', width: 56},
  {value: 'testPress', labelKey: 'testPress', width: 56},
  {value: 'surfaceTreatment', labelKey: 'surfaceTreatment', width: 56},
  {value: 'surplus', labelKey: 'surplus', width: 68},
  {
    value: 'materialOrderNoFormat',
    labelKey: 'materialOrderNo',
    width: 86,
    type: ValueType.Link,
    openLink: (d: any) => {
      window.open(`/industry/public/material/index?materialOrderNo=${d.materialOrderNo}`);
    },
  },
  {
    value: 'checkOrderNoFormat',
    labelKey: 'checkOrderNo',
    width: 103,
    type: ValueType.Link,
    openLink: (d: any) => {
      window.open(`/industry/public/material/check?checkOrderNo=${d.checkOrderNo}`);
    },
  },
])
httpGet(`system/user/config/list`, {}).then(
    (res: ListResult<any>) => {
      state.userConfigList = res.list || []
      userOptionList.value = state.userConfigList.map((t: any) => {
        return {
          value: t.userId,
          label: t.name,
        }
      })
    })
const defaultFormData = {
  creator: user.userId,
  deviceId: '',
  customerShortName: '',
  saleOrderNo: '',
  orderProject: '',
  materialNo: '',
  materialDescription: '',
  designNumber: '',
  orderCount: '',
  roughcastExpireDate: '',
  materialCount: '',
  promiseDoneDate: '',
  planReformCount: '',
  supplierRemark: '',
  productCountHour8: '',
  productCountHour12: '',
  processWorkingHour: '',
  onlineDate: '',
  offlineDate: '',
  delay: 0,
  processCount: 0,
  processProcedureList: [],
  nde: '',
  assemble: '',
  testPress: '',
  surfaceTreatment: '',
  surplus: 0,
  materialOrderNo: '',
  checkOrderNo: '',
  supplierDoneDate: '',
  deliverCount: 0,
  deliverDate: '',
  receiptCount: 0,
  receiptDate: formatDate(new Date(), 'yyyy-MM-dd'),
  scrapCount: 0,
  supplierPromiseDoneDate: '',
}
const checkOrNotList = ref([
  {
    value: '√',
    label: '√',
  },
])
const state = reactive({
  dateTimeList: [],
  dateTimeListSupplier: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      deviceId: '',
      customerShortName: '',
      materialOrderNo: '',
      customerOrderNo: '',
      customerProjectSequence: '',
      saleOrderNo: '',
      orderProjectNo: '',
      materialNo: '',
      designNumber: '',
      surplusCountType: null,
      chargeCompany: '',
      nde: '',
      assemble: '',
      testPress: '',
      surfaceTreatment: '',
      checkOrderNo: '',
      startPromiseDoneDate: '',
      endPromiseDoneDate: '',
      startSupplierDoneDate: '',
      endSupplierDoneDate: '',
      startSupplierPromiseDoneDate: '',
      endSupplierPromiseDoneDate: '',
      improveMaterialDescribe: '',
      orderCount: null,
      delay: null,
      scrapCount: null,
      processType: 0,
    },
    page: {
      page: DEFAULT_PAGE,
      limit: 120,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  config: {},
  formSave: true,
  formVisible: false,
  formRuleList: {
    customerShortName: [{required: true, message: 'Please check', trigger: 'blur'}],
    customerOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    customerProjectSequence: [{required: true, message: 'Please check', trigger: 'blur'}],
    saleOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderProjectNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    improveMaterialDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    designNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    productionDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    promiseDoneDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    blankMaterialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    blankMaterialDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    roughcastDesignNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    stoveNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    hotBatchNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    serialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    surplusCount: [{required: true, message: 'Please check', trigger: 'blur'}],
  },
})
const toggleKeyList = [
  'customerShortName',
  'saleOrderNo',
  'orderProjectNo',
  'materialNo',
  'improveMaterialDescribe',
  'designNumber',
  'orderCount',
]
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
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startPromiseDoneDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endPromiseDoneDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startPromiseDoneDate = ''
    state.query.data.endPromiseDoneDate = ''
  }
  handlePage()
}
const handleDateTimeChangeSupplier = () => {
  if (state.dateTimeListSupplier && state.dateTimeListSupplier.length > 1) {
    state.query.data.startSupplierPromiseDoneDate = formatDate(
        state.dateTimeListSupplier[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endSupplierPromiseDoneDate = formatDate(
        state.dateTimeListSupplier[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startSupplierPromiseDoneDate = ''
    state.query.data.endSupplierPromiseDoneDate = ''
  }
  handlePage()
}

const handlePage = () => {
  httpGet(`douson/task/page`, state.query).then(
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

const taskShow = 'admin' === user.username || (includes(roleCodeList, 'materialManager') && !includes(roleCodeList, 'supplierManager'))
const supplierShow = 'admin' === user.username || (includes(roleCodeList, 'supplierManager') && !includes(roleCodeList, 'materialManager'))
const supplierManagerColumnValueList = ['operator', 'customerShortName', 'saleOrderNo', 'orderProjectNo', 'materialNo', 'improveMaterialDescribe', 'designNumber', 'orderCount', 'roughcastExpireDate', 'materialCount', 'promiseDoneDate', 'planReformCount', 'supplierRemark', 'productCountHour8', 'productCountHour12', 'processWorkingHour', 'onlineDate', 'offlineDate', 'delay', 'processCount', 'processProcedureFormat', 'nde', 'assemble', 'testPress', 'surfaceTreatment', 'surplus', 'materialOrderNoFormat', 'checkOrderNoFormat']
const materialManagerColumnValueList = ['operator', 'customerShortName', 'saleOrderNo', 'orderProjectNo', 'materialNo', 'improveMaterialDescribe', 'designNumber', 'orderCount', 'roughcastExpireDate', 'materialCount', 'promiseDoneDate', 'supplierRemark', 'supplierDoneDate', 'deliverCount', 'deliverDate', 'receiptCount', 'receiptDate', 'scrapCount', 'supplierPromiseDoneDate', 'nde', 'assemble', 'testPress', 'surfaceTreatment', 'surplus', 'materialOrderNoFormat', 'checkOrderNoFormat']
if (!includes(roleCodeList, 'admin') && !includes(roleCodeList, 'taskView') && 'admin' !== user.username) {
  if (includes(roleCodeList, 'supplierManager') && !includes(roleCodeList, 'materialManager')) {
    columnConfigList.value = supplierManagerColumnValueList.map(k => columnConfigList.value.filter(t => k === t.value)[0])
  } else if (includes(roleCodeList, 'materialManager') && !includes(roleCodeList, 'supplierManager')) {
    columnConfigList.value = materialManagerColumnValueList.map(k => columnConfigList.value.filter(t => k === t.value)[0])
  }
}
if (user.username === 'admin' || includes(roleCodeList, 'materialManager')) {
  columnConfigList.value = columnConfigList.value.map(t => {
    if ('description' === t.value) {
      t.type = ValueType.TextEdit
    } else if ('chargeCompany' === t.value) {
      t.type = ValueType.TextEdit
    } else if ('productionCount' === t.value) {
      t.width = 168
      t.type = ValueType.NumberEdit
    }
    return t
  })
}
handlePage()
const handleSaveModal = () => {
  state.formData = Object.assign({}, defaultFormData)
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  return row.taskId && ('admin' === user.username || includes(roleCodeList, 'materialManager') || includes(roleCodeList, 'supplierManager'))
}
const handleDeleteShow = (row: any) => {
  return row.taskId
}
const handleCopy = (d) => {
  const request = Object.assign({}, d.param, {
    taskId: '', id: '', sorter: (d.param.sorter || 0) + 1,
    supplierRemark: null,
    productCountHour8: null,
    productCountHour12: null,
    processWorkingHour: null,
    onlineDate: null,
    offlineDate: null,
    delay: null,
    processCount: null,
    processProcedureList: [],
    supplierDoneDate: null,
    deliverCount: null,
    deliverDate: null,
    receiptCount: null,
    receiptDate: null,
    scrapCount: null,
    supplierPromiseDoneDate: null,
    surplus: null,
  })
  httpPutJson('douson/task/merge', request).then(() => {
    ElMessage.success('Copy success')
    handlePage()
  })
}
const handleUp = (d) => {
  const request = Object.assign({}, d.param, {sorter: (d.param.sorter || 0) - 1})
  httpPutJson('douson/task/merge', request).then(() => {
    ElMessage.success('Up success')
    handlePage()
  })
}
const handleDown = (d) => {
  const request = Object.assign({}, d.param, {sorter: (d.param.sorter || 0) + 2})
  httpPutJson('douson/task/merge', request).then(() => {
    ElMessage.success('Down success')
    handlePage()
  })
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPutJson('douson/task/merge', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handlePage()
        })
      } else {
        handleUpdate(state.formData)
      }
    }
  })
}
const handleUpdate = (row: any) => {
  return httpPutJson('douson/task/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/task', {
      taskId: row.taskId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
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
  // userOptionList.value = (l[1].list || []).map((t: any) => {
  //   return {
  //     value: t.userId,
  //     label: t.name,
  //   }
  // })
  // handlePage()
})
const {
  expandRowKeys,
  query,
  tableData,
  config,
  userConfigList,
  total,
  dateTimeList,
  dateTimeListSupplier,
  formData,
  formSave,
  formVisible,
  formRuleList,
  photoList,
} = {
  ...toRefs(state),
}

const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.processCount > 0 && row.processCount === row.materialCount) {
    return 'row-done'
  }
  return ''
}
</script>

<style scoped lang="scss">
</style>
