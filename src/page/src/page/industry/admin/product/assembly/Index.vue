<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.serialNumber" @keyup.enter="handlePage" :placeholder="store.state.label.serialNumber" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.purchaseOrderNo" @keyup.enter="handlePage" :placeholder="store.state.label.purchaseOrderNo" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.poProject" @keyup.enter="handlePage" :placeholder="store.state.label.poProject" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.saleOrderNo" @keyup.enter="handlePage" :placeholder="store.state.label.saleOrderNo" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.orderProject" @keyup.enter="handlePage" :placeholder="store.state.label.orderProject" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.materialNo" @keyup.enter="handlePage" :placeholder="store.state.label.materialNo" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.materialDescription" @keyup.enter="handlePage" :placeholder="store.state.label.materialDescription" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.designNumber" @keyup.enter="handlePage" :placeholder="store.state.label.designNumber" clearable @change="handlePage" class="search-item"/>
      <el-date-picker
          v-model="deliveryDateTimeList"
          @change="() => {handleDateTimeChange(deliveryDateTimeList, query.data, 'deliveryDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.deliveryDate}`"
          :end-placeholder="`End ${store.state.label.deliveryDate}`"
      >
      </el-date-picker>
      <el-input v-model="query.data.description" :placeholder="store.state.label.description" @keyup.enter="handlePage" clearable @change="handlePage" class="search-item"/>
      <el-input v-model="query.data.valveBody" :placeholder="store.state.label.valveBody" @keyup.enter="handlePage" clearable @change="handlePage" class="search-item"/>
      <!--      <el-select v-model="query.data.department"
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
            </el-select>-->
            <el-select v-model="query.data.assemblyCompleteType"
                       @change="handlePage"
                       filterable
                       allow-create
                       clearable
                       :placeholder="`${store.state.label.assemblyCompleteType}`"
                       class="search-item">
              <el-option
                  v-for="item in [{value: null, label: `All`,},{value: 0, label: `${store.state.label.alreadyComplete}`,},{value: 1, label: `${store.state.label.notYetComplete}`,}]"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            v-if="editAll"
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <div class="douson-flex-item" style="align-items: center;">
      <el-button :icon="UploadFilled" @click="handleDownloadTemplate" type="primary">Download template</el-button>
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
          <el-button v-if="editAll" :icon="UploadFilled" @click="handlePage" type="danger">Upload</el-button>
        </div>
      </el-upload>
    </div>
    <view-list
        idKey="planId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleEditShow="() => {return editYellow || editBlue}"
        :handleUpdate="handleUpdate"
        :handleDelete="'admin' === user.username || editAll ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detail-link="false"
        :handleTableRowClassName="handleTableRowClassName"
    >
      <template #operator="row">
        <el-link
            :icon="More"
            @click="handleShowPrintDetail(row)"
            class="mr10"
            type="info"
        >
          <el-tag size="small">Detail</el-tag>
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
          <el-input v-model="formData.materialNo" :disabled="!editAll"/>
        </el-form-item>
        <el-form-item prop="materialDescription" :label="store.state.label.materialDescription">
          <el-input v-model="formData.materialDescription" :disabled="!editAll"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber" :disabled="!editAll"/>
        </el-form-item>
        <el-form-item prop="deliveryDate" :label="store.state.label.deliveryDate">
          <el-date-picker
              type="date"
              v-model="formData.deliveryDate"
              format="YYYY-MM-DD"
              :disabled="!editAll"
              @change="formData.deliveryDate = formatDate(formData.deliveryDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="orderCount" :label="store.state.label.orderCount">
          <el-input-number v-model="formData.orderCount" style="width: 60px;" :controls="false" :min="0" :max="999999" :disabled="!(formData.serialIndex === 0 && editAll)"/>
        </el-form-item>
        <el-form-item prop="completedQty" :label="store.state.label.completedQty">
          <el-input-number v-model="formData.completedQty" style="width: 60px;" :controls="false" :min="0" :max="999999" :disabled="!editAll"/>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input v-model="formData.description" :disabled="!editAll"/>
        </el-form-item>
        <el-form-item prop="valveBody" :label="store.state.label.valveBody">
          <el-input v-model="formData.valveBody" :disabled="!(editBlue)"/>
        </el-form-item>
        <el-form-item prop="valveBodyPhotoList" :label="`${store.state.label.valveBodyPhoto}(${(formRuleList['valveBodyPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveBodyPhotoList" :maxSize="Number(`${(formRuleList['valveBodyPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editBlue)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveCover" :label="store.state.label.valveCover">
          <el-input v-model="formData.valveCover" :disabled="!(editBlue)"/>
        </el-form-item>
        <el-form-item prop="valveCoverPhotoList" :label="`${store.state.label.valveCoverPhoto}(${(formRuleList['valveCoverPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveCoverPhotoList" :maxSize="Number(`${(formRuleList['valveCoverPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editBlue)"></image-upload>
        </el-form-item>
        <el-form-item prop="gate" :label="store.state.label.gate">
          <el-input v-model="formData.gate" :disabled="!(editBlue)"/>
        </el-form-item>
        <el-form-item prop="gatePhotoList" :label="`${store.state.label.gatePhoto}(${(formRuleList['gatePhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.gatePhotoList" :maxSize="Number(`${(formRuleList['gatePhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editBlue)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveSeat" :label="store.state.label.valveSeat">
          <el-input v-model="formData.valveSeat" :disabled="!(editBlue)"/>
        </el-form-item>
        <el-form-item prop="valveSeatPhotoList" :label="`${store.state.label.valveSeatPhoto}(${(formRuleList['valveSeatPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveSeatPhotoList" :maxSize="Number(`${(formRuleList['valveSeatPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editBlue)"></image-upload>
        </el-form-item>
        <el-form-item prop="valveStem" :label="store.state.label.valveStem">
          <el-input v-model="formData.valveStem" :disabled="!(editBlue)"/>
        </el-form-item>
        <el-form-item prop="valveStemPhotoList" :label="`${store.state.label.valveStemPhoto}(${(formRuleList['valveStemPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.valveStemPhotoList" :maxSize="Number(`${(formRuleList['valveStemPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editBlue)"></image-upload>
        </el-form-item>
        <el-form-item prop="assemblyPerson" :label="store.state.label.assemblyPerson">
          <el-select v-model="formData.assemblyPerson" clearable filterable :placeholder="`Please upload ${store.state.label.valveBodyPhoto}`" :disabled="true">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="assemblyStartDate" :label="store.state.label.assemblyStartDate">
          <el-date-picker
              type="date"
              v-model="formData.assemblyStartDate"
              format="YYYY-MM-DD HH:mm:ss"
              :disabled="true"
              @change="formData.assemblyStartDate = formatDate(formData.assemblyStartDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="pressureTestPhotoList" :label="`${store.state.label.pressureTestPhoto}(${(formRuleList['pressureTestPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.pressureTestPhotoList" :maxSize="Number(`${(formRuleList['pressureTestPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editYellow)"></image-upload>
        </el-form-item>
        <el-form-item prop="torqueNm" :label="store.state.label.torqueNm">
          <el-input type="number" v-model="formData.torqueNm" style="width: 180px;" :min="100" :max="300" :disabled="!(editYellow)">
            <template #append>
              N.m
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="oilInjectionPhotoList" :label="`${store.state.label.oilInjectionPhoto}(${(formRuleList['oilInjectionPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.oilInjectionPhotoList" :maxSize="Number(`${(formRuleList['oilInjectionPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!(editYellow)"></image-upload>
        </el-form-item>
        <el-form-item prop="tester" :label="store.state.label.tester">
          <el-select v-model="formData.tester" clearable filterable :placeholder="`Please upload ${store.state.label.oilInjectionPhoto}`" :disabled="true">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="assemblyCompleteDate" :label="store.state.label.assemblyCompleteDate">
          <el-date-picker
              type="date"
              v-model="formData.assemblyCompleteDate"
              format="YYYY-MM-DD HH:mm:ss"
              :disabled="true"
              @change="formData.assemblyCompleteDate = formatDate(formData.assemblyCompleteDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-affix position="bottom" :offset="20">
          <span class="dialog-footer">
            <el-button @click="formVisible = false">Cancel</el-button>
            <el-button type="primary" @click="handleMerge">Confirm</el-button>
          </span>
        </el-affix>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {onMounted, reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Search, More, UploadFilled, List,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload, httpDownloadFile,} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../../component/ImageUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const generateFormRef: Ref = ref(null)
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const editAll = includes(roleCodeList, 'assemblyManager')
const editBlue = editAll || includes(roleCodeList, 'assemblyRecord')
const editYellow = editAll || includes(roleCodeList, 'assemblyTesterRecord')
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
  {value: 'orderCount', labelKey: 'orderCount', width: 47},
  {value: 'deliveryDate', labelKey: 'deliveryDate', width: 102},
  {value: 'completedQty', labelKey: 'completedQty', width: 47},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'valveBody', labelKey: 'valveBody', width: 121},
  {
    value: 'valveBodyPhotoCount', labelKey: 'valveBodyPhoto', width: 52,
  },
  {value: 'valveBodyPhotoList', labelKey: 'valveBodyPhoto', width: 131, type: ValueType.Image,},
  {value: 'valveCover', labelKey: 'valveCover', width: 121},
  {
    value: 'valveCoverPhotoCount', labelKey: 'valveCoverPhoto', width: 52,
  },
  {value: 'valveCoverPhotoList', labelKey: 'valveCoverPhoto', width: 151, type: ValueType.Image,},
  {value: 'gate', labelKey: 'gate', width: 121},
  {
    value: 'gatePhotoCount', labelKey: 'gatePhoto', width: 52,
  },
  {value: 'gatePhotoList', labelKey: 'gatePhoto', width: 189, type: ValueType.Image,},
  {value: 'valveSeat', labelKey: 'valveSeat', width: 121},
  {
    value: 'valveSeatPhotoCount', labelKey: 'valveSeatPhoto', width: 52,
  },
  {value: 'valveSeatPhotoList', labelKey: 'valveSeatPhoto', width: 189, type: ValueType.Image,},
  {value: 'valveStem', labelKey: 'valveStem', width: 189},
  {
    value: 'valveStemPhotoCount', labelKey: 'valveStemPhoto', width: 65,
  },
  {value: 'valveStemPhotoList', labelKey: 'valveStemPhoto', width: 189, type: ValueType.Image,},
  {value: 'assemblyPersonFormat', labelKey: 'assemblyPerson', width: 189},
  {value: 'assemblyStartDate', labelKey: 'assemblyStartDate', width: 189},
  {
    value: 'pressureTestPhotoCount', labelKey: 'pressureTestPhoto', width: 65,
  },
  {value: 'pressureTestPhotoList', labelKey: 'pressureTestPhoto', width: 189, type: ValueType.Image,},
  {value: 'torqueNmFormat', originValue: 'torqueNm', labelKey: 'torqueNm', width: 189},
  {
    value: 'oilInjectionPhotoCount', labelKey: 'oilInjection', width: 65,
  },
  {value: 'oilInjectionPhotoList', labelKey: 'oilInjectionPhoto', width: 189, type: ValueType.Image,},
  {value: 'testerFormat', labelKey: 'tester', width: 189},
  {value: 'assemblyCompleteDate', labelKey: 'assemblyCompleteDate', width: 189},
])
const handleDownloadTemplate = () => {
  httpDownloadFile("douson/assembly/template", state.query.data);
}
const defaultFormData = {
  serialNumber: '',
  maxSerialOrderIndex: 0,
  serialIndex: 0,
  createOrderCount: 0,
  purchaseOrderNo: '',
  poProject: '',
  saleOrderNo: '',
  orderProject: '',
  materialNo: '',
  materialDescription: '',
  designNumber: '',
  deliveryDate: '',
  orderCount: 0,
  completedQty: 0,
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
  oilInjectionPhotoCount: 0,
  oilInjectionPhotoList: [],
  tester: '',
  assemblyCompleteDate: '',

  createDate: formatDate(new Date(), 'yyyy-MM-dd'),
  responsiblePersonList: [store.state.user.userId],
  attachmentList: [],
}
const deliveryDateTimeList = ref([])
const state = reactive({
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      serialNumber: '',
      purchaseOrderNo: '',
      poProject: '',
      saleOrderNo: '',
      orderProject: '',
      materialNo: '',
      materialDescription: '',
      designNumber: '',
      startDeliveryDate: '',
      endDeliveryDate: '',
      description: '',
      valveBody: '',
      assemblyCompleteType: includes(roleCodeList, 'assemblyRecord') ? 1 : includes(roleCodeList, 'assemblyTesterRecord') ? 0 : null,

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
    torqueNm: [{required: false, type: 'number', min: 100, max: 300}],
    gatePhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    valveStemPhotoList: [{required: false, type: 'array', min: 0, max: 4}],
    pressureTestPhotoList: [{required: false, type: 'array', min: 0, max: 6}],
    oilInjectionPhotoList: [{required: false, type: 'array', min: 0, max: 2}],
  },
})
const generateListFormRuleList = {
  createOrderCount: [{required: true, type: 'number', min: 1, message: 'Please check', trigger: 'blur'}],
}
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
  handlePage()
}
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/assembly?assemblyId=${d.param.assemblyId}`)
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
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.serialIndex === 0) {
    if( row.orderCount === row.completedQty) {
      return 'row-green'
    }
  } else {
    if(row.completedQty > 0) {
      return 'row-gray'
    }
  }
  return ''
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'equipmentNo',
      'equipmentPosition',
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
  columnConfigList.value = columnConfigList.value.map(t => {
    if (editBlue) {
      if ('valveBodyPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveBodyPhotoList')
          }, 100)
        }
      } else if ('valveCoverPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveCoverPhotoList')
          }, 100)
        }
      } else if ('gatePhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('gatePhotoList')
          }, 100)
        }
      } else if ('valveSeatPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveSeatPhotoList')
          }, 100)
        }
      } else if ('valveStemPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveStemPhotoList')
          }, 100)
        }
      } else if ('description' === t.value) {
        t.type = ValueType.TextEdit
      }
    }
    if (editYellow) {
      if ('pressureTestPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('pressureTestPhotoList')
          }, 100)
        }
      } else if ('oilInjectionPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('oilInjectionPhotoList')
          }, 100)
        }
      } else if ('description' === t.value) {
        t.type = ValueType.TextEdit
      } else if ('torqueNmFormat' === t.value) {
        t.width = 161
        t.type = ValueType.NumberEdit
      }
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
