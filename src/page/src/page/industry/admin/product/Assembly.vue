<template>
  <div>
    <div class="query-container">
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start date"
          end-placeholder="End date"
      >
      </el-date-picker>
      <el-select v-model="query.data.department"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.department"
                 class="search-item">
        <el-option
            v-for="item in config.departmentList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.optimizeType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.optimizeType"
                 class="search-item">
        <el-option
            v-for="item in config.optimizeTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.existsProblem"
                @change="handlePage"
                :placeholder="store.state.label.existsProblem"
                class="search-item"/>
      <el-input v-model="query.data.username"
                @change="handlePage"
                :placeholder="store.state.label.username"
                class="search-item"/>
      <el-select v-model="query.data.responsiblePerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.responsiblePerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.valid"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.valid"
                 class="search-item">
        <el-option
            label="Done"
            :value="true"
        />
        <el-option
            label="Pending"
            :value="false"
        />
      </el-select>
      <el-input v-model="query.data.serialNo" @keyup.enter="handlePage" :placeholder="store.state.label.templateOrderNo" clearable @change="handlePage" class="search-item"/>
      <el-select v-model="query.data.responsibleTeam"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.responsibleTeam"
                 class="search-item">
        <el-option
            v-for="item in config.responsibleTeamList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <div class="query-btn">
        <el-upload
            action="#"
            :show-file-list="false"
            :on-change="handleFileChange"
            :before-upload="handleBeforeUpload"
            :http-request="handleRequest"
            accept=".xlsx,.xls"
            multiple
            :drag="true"
        >
          <div>
            <el-button :icon="UploadFilled" @click="handlePage" type="danger">Upload</el-button>
          </div>
        </el-upload>
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <view-list
        idKey="planId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleDelete="'admin' === user.username ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detail-link="false"
        :handleTableCellClassName="handleTableCellClassName"
    >
      <template #operator="row">
        <el-link
            :icon="Printer"
            @click="handleShowPrintDetail(row)"
            class="mr10"
            type="info"
        >
          <el-tag size="small">Print</el-tag>
        </el-link>
        <el-link
            v-if="row.param.serialIndex === 0"
            :icon="List"
            @click="handleGenerateListModal(row)"
            class="mr10"
            type="info"
            :disabled="row.param.maxSerialOrderIndex >= row.param.orderCount"
        >
          <el-tag size="small" :type="row.param.maxSerialOrderIndex >= row.param.orderCount ? 'info' : 'primary'">Generate</el-tag>
        </el-link>
      </template>
    </view-list>

    <el-dialog :title="'Generate list'" v-model="generateListVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="generateListFormRuleList"
          :model="formData"
          ref="generateFormRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="createOrderCount" :label="store.state.label.createOrderCount">
          <el-input-number style="width: 300px;" :min="1" :max="formData.orderCount - formData.maxSerialOrderIndex" v-model="formData.createOrderCount" :placeholder="`1 <= input <= ${formData.orderCount - formData.maxSerialOrderIndex}`"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="generateListVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleGenerateList">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="purchaseOrderNo" :label="store.state.label.purchaseOrderNo">
          <el-input v-model="formData.purchaseOrderNo" :disabled="!formSave"/>
        </el-form-item>
        <el-form-item prop="poProject" :label="store.state.label.poProject">
          <el-input v-model="formData.poProject" :disabled="!formSave"/>
        </el-form-item>
        <el-form-item prop="saleOrderNo" :label="store.state.label.saleOrderNo">
          <el-input v-model="formData.saleOrderNo" :disabled="!formSave"/>
        </el-form-item>
        <el-form-item prop="orderProject" :label="store.state.label.orderProject">
          <el-input v-model="formData.orderProject" :disabled="!formSave"/>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="materialDescription" :label="store.state.label.materialDescription">
          <el-input v-model="formData.materialDescription"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber"/>
        </el-form-item>
        <el-form-item prop="deliveryDate" :label="store.state.label.deliveryDate">
          <el-date-picker
              type="date"
              v-model="formData.deliveryDate"
              format="YYYY-MM-DD"
              @change="formData.deliveryDate = formatDate(formData.deliveryDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="orderCount" :label="store.state.label.orderCount">
          <el-input-number v-model="formData.orderCount" style="width: 60px;" :controls="false" :min="0" :max="999999"/>
        </el-form-item>
        <el-form-item prop="completedQty" :label="store.state.label.completedQty">
          <el-input-number v-model="formData.completedQty" style="width: 60px;" :controls="false" :min="0" :max="999999"/>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input v-model="formData.description"/>
        </el-form-item>
        <el-form-item prop="valveBody" :label="store.state.label.valveBody">
          <el-input v-model="formData.valveBody"/>
        </el-form-item>
        <el-form-item prop="valveBodyPhotoList" :label="`${store.state.label.valveBodyPhoto}(${(formRuleList['valveBodyPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveBodyPhotoList" :maxSize="Number(`${(formRuleList['valveBodyPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveCover" :label="store.state.label.valveCover">
          <el-input v-model="formData.valveCover"/>
        </el-form-item>
        <el-form-item prop="valveCoverPhotoList" :label="`${store.state.label.valveCoverPhoto}(${(formRuleList['valveCoverPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveCoverPhotoList" :maxSize="Number(`${(formRuleList['valveCoverPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="oilInjectionPhotoList" :label="`${store.state.label.oilInjectionPhoto}(${(formRuleList['oilInjectionPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.oilInjectionPhotoList" :maxSize="Number(`${(formRuleList['oilInjectionPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="gate" :label="store.state.label.gate">
          <el-input v-model="formData.gate"/>
        </el-form-item>
        <el-form-item prop="gatePhotoList" :label="`${store.state.label.gatePhoto}(${(formRuleList['gatePhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.gatePhotoList" :maxSize="Number(`${(formRuleList['gatePhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveSeat" :label="store.state.label.valveSeat">
          <el-input v-model="formData.valveSeat"/>
        </el-form-item>
        <el-form-item prop="valveSeatPhotoList" :label="`${store.state.label.photo}(${(formRuleList['valveSeatPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveSeatPhotoList" :maxSize="Number(`${(formRuleList['valveSeatPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveStem" :label="store.state.label.valveStem">
          <el-input v-model="formData.valveStem"/>
        </el-form-item>
        <el-form-item prop="valveStemPhotoList" :label="`${store.state.label.photo}(${(formRuleList['valveStemPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveStemPhotoList" :maxSize="Number(`${(formRuleList['valveStemPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="oilInjection" :label="store.state.label.oilInjection">
          <el-input v-model="formData.oilInjection"/>
        </el-form-item>
        <el-form-item prop="oilInjectionPhotoList" :label="`${store.state.label.oilInjectionPhoto}(${(formRuleList['oilInjectionPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.oilInjectionPhotoList" :maxSize="Number(`${(formRuleList['oilInjectionPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveStemPhoto" :label="store.state.label.valveStemPhoto">
          <el-input v-model="formData.valveStemPhoto"/>
        </el-form-item>
        <el-form-item prop="assemblyPerson" :label="store.state.label.assemblyPerson">
          <el-input v-model="formData.assemblyPerson"/>
        </el-form-item>
        <el-form-item prop="assemblyStartDate" :label="store.state.label.assemblyStartDate">
          <el-input v-model="formData.assemblyStartDate"/>
        </el-form-item>
        <el-form-item prop="pressureTest" :label="store.state.label.pressureTest">
          <el-input v-model="formData.pressureTest"/>
        </el-form-item>
        <el-form-item prop="pressureTestPhotoList" :label="`${store.state.label.photo}(${(formRuleList['pressureTestPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.pressureTestPhotoList" :maxSize="Number(`${(formRuleList['pressureTestPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="pressureTestPhoto" :label="store.state.label.pressureTestPhoto">
          <el-input v-model="formData.pressureTestPhoto"/>
        </el-form-item>
        <el-form-item prop="torqueNm" :label="store.state.label.torqueNm">
          <el-input v-model="formData.torqueNm"/>
        </el-form-item>
        <el-form-item prop="oilInjection" :label="store.state.label.oilInjection">
          <el-input v-model="formData.oilInjection"/>
        </el-form-item>
        <el-form-item prop="oilInjectionPhoto" :label="store.state.label.oilInjectionPhoto">
          <el-input v-model="formData.oilInjectionPhoto"/>
        </el-form-item>
        <el-form-item prop="tester" :label="store.state.label.tester">
          <el-input v-model="formData.tester"/>
        </el-form-item>
        <el-form-item prop="assemblyCompleteDate" :label="store.state.label.assemblyCompleteDate">
          <el-input v-model="formData.assemblyCompleteDate"/>
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
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Search, Printer, UploadFilled, List,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'
import FileUpload from '../../component/FileUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const generateFormRef: Ref = ref(null)
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {
    value: 'expand',
    label: '',
    width: 48,
    type: ValueType.Expand,
  },
  {
    value: 'operator',
    labelKey: 'viewAndEdit',
    width: 223,
    type: ValueType.Operator,
  },
  {value: 'serialNumber', labelKey: 'serialNumber', width: 154},
  {value: 'purchaseOrderNo', labelKey: 'purchaseOrderNo', width: 98},
  {value: 'poProject', labelKey: 'poProject', width: 56},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 102},
  {value: 'orderProject', labelKey: 'orderProject', width: 56},
  {value: 'materialNo', labelKey: 'materialNo', width: 98},
  {value: 'materialDescription', labelKey: 'materialDescription', width: 189},
  {value: 'designNumber', labelKey: 'designNumber', width: 102},
  {value: 'deliveryDate', labelKey: 'deliveryDate', width: 102},
  {value: 'orderCount', labelKey: 'orderCount', width: 47},
  {value: 'completedQty', labelKey: 'completedQty', width: 47},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'valveBody', labelKey: 'valveBody', width: 121},
  {value: 'valveBodyPhotoCount', labelKey: 'valveBodyPhoto', width: 52},
  {value: 'valveBodyPhotoList', labelKey: 'valveBodyPhoto', width: 131, type: ValueType.Image,},
  {value: 'valveCover', labelKey: 'valveCover', width: 121},
  {value: 'valveCoverPhotoCount', labelKey: 'valveCoverPhoto', width: 52},
  {value: 'valveCoverPhotoList', labelKey: 'valveCoverPhoto', width: 151, type: ValueType.Image,},
  {value: 'gate', labelKey: 'gate', width: 121},
  {value: 'gatePhotoCount', labelKey: 'gatePhoto', width: 52},
  {value: 'gatePhotoList', labelKey: 'gatePhoto', width: 189, type: ValueType.Image,},
  {value: 'valveSeat', labelKey: 'valveSeat', width: 121},
  {value: 'valveSeatPhotoCount', labelKey: 'valveSeatPhoto', width: 52,},
  {value: 'valveSeatPhotoList', labelKey: 'valveSeatPhoto', width: 189, type: ValueType.Image,},
  {value: 'valveStem', labelKey: 'valveStem', width: 189},
  {value: 'valveStemPhotoCount', labelKey: 'valveStemPhoto', width: 189},
  {value: 'valveStemPhotoList', labelKey: 'valveStemPhoto', width: 189, type: ValueType.Image,},
  {value: 'assemblyPerson', labelKey: 'assemblyPerson', width: 189},
  {value: 'assemblyStartDate', labelKey: 'assemblyStartDate', width: 189},
  {value: 'pressureTest', labelKey: 'pressureTest', width: 189},
  {value: 'pressureTestPhotoCount', labelKey: 'pressureTestPhoto', width: 189},
  {value: 'pressureTestPhotoList', labelKey: 'pressureTestPhoto', width: 189, type: ValueType.Image,},
  {value: 'torqueNm', labelKey: 'torqueNm', width: 189},
  {value: 'oilInjection', labelKey: 'oilInjection', width: 189},
  {value: 'oilInjectionPhotoCount', labelKey: 'oilInjectionPhoto', width: 189,},
  {value: 'oilInjectionPhotoList', labelKey: 'oilInjectionPhoto', width: 189, type: ValueType.Image,},
  {value: 'tester', labelKey: 'tester', width: 189},
  {value: 'assemblyCompleteDate', labelKey: 'assemblyCompleteDate', width: 189},
])
const defaultFormData = {
  serialNumber: '',
  maxSerialOrderIndex: 0,
  createOrderCount: 0,
  purchaseOrderNo: '',
  poProject: '',
  saleOrderNo: '',
  orderProject: '',
  materialNo: '',
  materialDescription: '',
  designNumber: '',
  deliveryDate: '',
  orderCount: '',
  completedQty: '',
  description: '',
  valveBody: '',
  valveBodyPhoto: '',
  valveBodyPhotoCount: 0,
  valveBodyPhotoList: [],
  valveCover: '',
  valveCoverPhotoCount: 0,
  valveCoverPhotoList: [],
  gate: '',
  gatePhotoCount: 0,
  gatePhotoList: [],
  valveSeat: '',
  valveSeatPhotoCount: 0,
  valveSeatPhotoList: [],
  valveStem: '',
  valveStemPhotoCount: 0,
  valveStemPhotoList: [],
  assemblyPerson: '',
  assemblyStartDate: '',
  pressureTest: '',
  pressureTestPhoto: '',
  pressureTestPhotoCount: 0,
  pressureTestPhotoList: [],
  torqueNm: '',
  oilInjection: '',
  oilInjectionPhotoCount: 0,
  oilInjectionPhotoList: [],
  tester: '',
  assemblyCompleteDate: '',

  createDate: formatDate(new Date(), 'yyyy-MM-dd'),
  responsiblePersonList: [store.state.user.userId],
  attachmentList: [],
}
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      startCreateDate: '',
      endCreateDate: '',
      department: '',
      responsibleTeam: '',
      optimizeType: '',
      existsProblem: '',
      username: '',
      serialNo: '',
      responsiblePerson: '',
      valid: false,
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  formSave: false,
  config: {
    processProcedureList: [],
    testDeviceList: [],
    customerShortNameList: [],
    departmentList: [],
    optimizeTypeList: [],
    responsibleTeamList: [],
  },
  userConfigList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    purchaseOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    poProject: [{required: true, message: 'Please check', trigger: 'blur'}],
    saleOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderProject: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    valveBodyPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    valveCoverPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    valveSeatPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    valveStemPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    pressureTestPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    oilInjectionPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
  },
})
const generateListFormRuleList = {
  createOrderCount: [{required: true, type: 'number', min: 1, message: 'Please check', trigger: 'blur'}],
}
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startCreateDate = formatDate(
        state.dateTimeList[0],
        "yyyy-MM-dd hh:mm:ss"
    );
    state.query.data.endCreateDate = formatDate(
        state.dateTimeList[1],
        "yyyy-MM-dd hh:mm:ss"
    );
  } else {
    state.query.data.startCreateDate = ''
    state.query.data.endCreateDate = ''
  }
  handlePage()
}
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/plan?planId=${d.param.planId}`)
}
const generateListVisible = ref(false)
const handleGenerateListModal = (d: any) => {
  state.formData = Object.assign({}, d.param)
  generateListVisible.value = true
}
const handleGenerateList = () => {
  generateFormRef.value.validate((valid: any) => {
    if (valid) {
      httpPostJson('douson/assembly/generate-list', state.formData).then(() => {
        generateListVisible.value = false
        ElMessage.success('Generate list success')
        handlePage()
      })
    }
  })
}

const handleJumpOrder = (t: any) => {
  router.push(
      {
        path: '/industry/admin/product/order',
        query: {
          orderId: t.orderId,
        },
      })
}
const handleJumpDevice = (t: any) => {
  router.push(
      {
        path: '/industry/admin/product/device',
        query: {
          deviceId: t.testDevice,
        },
      })
}
httpGet('douson/config', {
  categoryIdList: [
    'processProcedure',
    'testDevice',
    'customerShortName',
    'department',
    'optimizeType',
    'responsibleTeam',
  ]
}).then(r => {
  state.config = r.data
})
const handlePage = () => {
  httpGet(`douson/assembly/page`, state.query).then(
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
const handleTableCellClassName = ({
                                    row,
                                    column,
                                    rowIndex,
                                    columnIndex,
                                  }: {
  row: any
  rowIndex: number
}) => {
  if (serialNoIndex >= 0 && serialNoIndex === columnIndex) {
    return 'row-red'
  }
  return ''
}
let serialNoIndex = -1
httpGet(`system/user/config/list`, {}).then(
    (res: ListResult<any>) => {
      state.userConfigList = res.list || []
      userOptionList.value = state.userConfigList.map((t: any) => {
        return {
          value: t.userId,
          label: t.name,
        }
      })
      columnConfigList.value = columnConfigList.value.map((t: any, i) => {
        if ('responsiblePersonFormat' === t.value) {
          t.type = ValueType.SelectEdit
          t.width = 234;
          t.optionList = userOptionList.value
        }
        if (t.value === 'serialNo') {
          serialNoIndex = i
        }
        return t
      })
      handlePage()
    })
const uploadData = ref({})
const afterUpload = ref(false)
const fileMap: any = {}
const handleFileChange = (file: UploadFile, fileList: UploadFiles) => {
  console.log('file change, length: ' + fileList.length)
}
const handleBeforeUpload = (file: UploadFile) => {
  console.log('before upload file: ' + file.uid)
  fileMap[file.uid] = file
  return file
}
const handleRequest = (d: any) => {
  const keys = Object.keys(fileMap)
  const limit = 1
  if (keys.length > limit) {
    ElMessage.error(`Too many upload`)
    keys.forEach((k: any) => {
      delete fileMap[k]
    })
  } else if (keys.length > 0) {
    Promise.all(keys.map((k: any) => {
      const t = fileMap[k]
      const formData = new FormData()
      formData.set("filename", t.name)
      formData.set("file", t)
      return httpUpload(`douson/assembly/upload`, formData)
    }))
    .then((l: any[]) => {
      afterUpload.value = true
      uploadData.value = (l[0] || {}).data || {}
      handlePage()
      keys.forEach((k: any) => {
        delete fileMap[k]
      })
      return Promise.resolve()
    })
    .catch((err) => {
      ElMessage.error(`Upload failed`)
      keys.forEach((k: any) => {
        delete fileMap[k]
      })
      return Promise.reject()
    })
  }
}
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
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/plan', state.formData).then(() => {
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
  return httpPutJson('douson/assembly/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('/douson/assembly', {
      assemblyId: row.assemblyId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const {
  expandRowKeys,
  query,
  tableData,
  config,
  userConfigList,
  total,
  dateTimeList,
  formData,
  formSave,
  formVisible,
  formRuleList,
  photoVisible,
  photoList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
