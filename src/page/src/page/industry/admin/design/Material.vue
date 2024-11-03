<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.customerShortName"
                @change="handlePage"
                :placeholder="store.state.label.customerShortName"
                class="search-item"/>
      <el-input v-model="query.data.customerOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.customerOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.customerProjectSequence"
                @change="handlePage"
                :placeholder="store.state.label.customerProjectSequence"
                class="search-item"/>
      <el-input v-model="query.data.saleOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProjectNo"
                @change="handlePage"
                :placeholder="store.state.label.orderProjectNo"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start promise done date"
          end-placeholder="End promise done date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.surplusCountType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.surplusCount"
                 class="search-item">
        <el-option
            v-for="item in [
                {
                  value: 0,
                  label: '=0',
                },
                {
                  value: 1,
                  label: '>0',
                },
                {
                  value: -1,
                  label: '<0',
                },
                {
                  value: 2,
                  label: '!=0',
                },
            ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.remainCountType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.materialCount + '-' +  store.state.label.productionCount"
                 class="search-item">
        <el-option
            v-for="item in [
                {
                  value: 0,
                  label: '=0',
                },
                {
                  value: 1,
                  label: '>0',
                },
                {
                  value: -1,
                  label: '<0',
                },
                {
                  value: 2,
                  label: '!=0',
                },
            ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.chargeCompany"
                @change="handlePage"
                :placeholder="store.state.label.chargeCompany"
                class="search-item"/>
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
      <el-input v-model="query.data.checkOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.checkOrderNo"
                class="search-item"/>
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
            :disabled="!includes(roleCodeList, 'material')"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <div>
      <el-alert v-show="afterUpload" :title="`上传总记录数：${uploadData.uploadDetailCount}, 订单数：${uploadData.uploadCount}；最终总记录：${uploadData.afterDetailCount}, 订单数：${uploadData.afterCount}`" type="success"/>
    </div>
    <div>
      <el-space wrap>
        <el-switch v-model="showMore" active-text="Show more" inactive-text="Hide info" @change="handleToggleMore"/>
      </el-space>
    </div>
    <view-list
        idKey="materialId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="includes(roleCodeList, 'admin') ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handleTableRowClassName="handleTableRowClassName"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detailLink="false"
    >
    </view-list>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="customerShortName" :label="store.state.label.customerShortName">
          <el-input v-model="formData.customerShortName"/>
        </el-form-item>
        <el-form-item prop="customerOrderNo" :label="store.state.label.customerOrderNo">
          <el-input v-model="formData.customerOrderNo" @change="formData.customerOrderNo = (formData.customerOrderNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="customerProjectSequence" :label="store.state.label.customerProjectSequence">
          <el-input v-model="formData.customerProjectSequence"/>
        </el-form-item>
        <el-form-item prop="saleOrderNo" :label="store.state.label.saleOrderNo">
          <el-input v-model="formData.saleOrderNo" @change="formData.saleOrderNo = (formData.saleOrderNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="orderProjectNo" :label="store.state.label.orderProjectNo">
          <el-input v-model="formData.orderProjectNo"/>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo" @change="formData.materialNo = (formData.materialNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="improveMaterialDescribe" :label="store.state.label.improveMaterialDescribe">
          <el-input v-model="formData.improveMaterialDescribe" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber" @change="formData.designNumber = (formData.designNumber || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="orderCount" :label="store.state.label.orderCount">
          <el-input-number v-model="formData.orderCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="productionDate" :label="store.state.label.productionDate">
          <el-date-picker
              type="date"
              v-model="formData.productionDate"
              format="YYYY-MM-DD"
              @change="formData.productionDate = formatDate(formData.productionDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="promiseDoneDate" :label="store.state.label.promiseDoneDate">
          <el-date-picker
              type="date"
              v-model="formData.promiseDoneDate"
              format="YYYY-MM-DD"
              @change="formData.promiseDoneDate = formatDate(formData.promiseDoneDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="blankMaterialNo" :label="store.state.label.blankMaterialNo">
          <el-input v-model="formData.blankMaterialNo" @change="formData.blankMaterialNo = (formData.blankMaterialNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="blankMaterialDescribe" :label="store.state.label.blankMaterialDescribe">
          <el-input v-model="formData.blankMaterialDescribe"/>
        </el-form-item>
        <el-form-item prop="roughcastDesignNumber" :label="store.state.label.roughcastDesignNumber">
          <el-input v-model="formData.roughcastDesignNumber" @change="formData.roughcastDesignNumber = (formData.roughcastDesignNumber || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="materialCount" :label="store.state.label.materialCount">
          <el-input-number v-model="formData.materialCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="stoveNo" :label="store.state.label.stoveNo">
          <el-input v-model="formData.stoveNo" @change="formData.stoveNo = (formData.stoveNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="hotBatchNo" :label="store.state.label.hotBatchNo">
          <el-input v-model="formData.hotBatchNo" @change="formData.hotBatchNo = (formData.hotBatchNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="serialNo" :label="store.state.label.serialNo">
          <el-input v-model="formData.serialNo" @change="formData.serialNo = (formData.serialNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="nde" :label="store.state.label.nde">
          <el-select v-model="formData.nde"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.nde"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="assemble" :label="store.state.label.assemble">
          <el-select v-model="formData.assemble"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.assemble"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="testPress" :label="store.state.label.testPress">
          <el-select v-model="formData.testPress"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.testPress"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="surfaceTreatment" :label="store.state.label.surfaceTreatment">
          <el-select v-model="formData.surfaceTreatment"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.surfaceTreatment"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="chargeCompany" :label="store.state.label.chargeCompany">
          <el-input v-model="formData.chargeCompany"/>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input v-model="formData.description" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item prop="productionCount" :label="store.state.label.productionCount">
          <el-input-number v-model="formData.productionCount" style="width: 60px;" :controls="false" :min="0" :disabled="!includes(roleCodeList, 'materialManager') && 'admin' !== user.username"/>
        </el-form-item>
        <el-form-item prop="arrangeProductionDate" :label="store.state.label.arrangeProductionDate">
          <el-date-picker
              type="date"
              v-model="formData.arrangeProductionDate"
              format="YYYY-MM-DD"
              @change="formData.arrangeProductionDate = formatDate(formData.arrangeProductionDate, 'yyyy-MM-dd')"
              :disabled="!includes(roleCodeList, 'materialManager') && 'admin' !== user.username"
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
import {Plus, Printer, Search, UploadFilled,} from '@element-plus/icons-vue'
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
const uploadData = ref({})
const afterUpload = ref(false)
const columnConfigList = ref<ViewConfig[]>([
  {value: 'operator', labelKey: 'viewAndEdit', width: 165, type: ValueType.Operator,},
  {value: 'customerShortName', labelKey: 'customerShortName', width: 189, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'customerOrderNo', labelKey: 'customerOrderNo', width: 101, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'customerProjectSequence', labelKey: 'customerProjectSequence', width: 72, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 89, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'orderProjectNo', labelKey: 'orderProjectNo', width: 72, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'materialNo', labelKey: 'materialNo', width: 138, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'improveMaterialDescribe', labelKey: 'improveMaterialDescribe', width: 189, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'designNumber', labelKey: 'designNumber', width: 121, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'orderCount', labelKey: 'orderCount', width: 73, highLight: true, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'productionDate', labelKey: 'productionDate', width: 102, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'promiseDoneDate', labelKey: 'promiseDoneDate', width: 102, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'blankMaterialNo', labelKey: 'blankMaterialNo', width: 168, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'blankMaterialDescribe', labelKey: 'blankMaterialDescribe', width: 189, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'roughcastDesignNumber', labelKey: 'roughcastDesignNumber', width: 168, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'materialCount', labelKey: 'materialCount', highLight: true, width: 82},
  {value: 'stoveNo', labelKey: 'stoveNo', width: 87},
  {value: 'hotBatchNo', labelKey: 'hotBatchNo', width: 87},
  {value: 'serialNo', labelKey: 'serialNo', width: 189},
  {value: 'surplusCount', labelKey: 'surplusCount', highLight: true, width: 72, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'nde', labelKey: 'nde', width: 68, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'assemble', labelKey: 'assemble', width: 68, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'testPress', labelKey: 'testPress', width: 68, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'surfaceTreatment', labelKey: 'surfaceTreatment', width: 68, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'chargeCompany', labelKey: 'chargeCompany', width: 137},
  {value: 'description', labelKey: 'description', width: 189, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],},
  {value: 'productionCount', labelKey: 'productionCount', highLight: true, width: 72},
  {value: 'arrangeProductionDate', labelKey: 'productionDate', width: 102},
  {
    value: 'materialOrderNoFormat', labelKey: 'materialOrderNo', width: 146, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'], type: ValueType.Link, openLink: (d: any) => {
      window.open(`/industry/public/material/index?materialOrderNo=${d.materialOrderNo}`);
    },
  },
  {
    value: 'checkOrderNoFormat', labelKey: 'checkOrderNo', width: 146, mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'], type: ValueType.Link, openLink: (d: any) => {
      window.open(`/industry/public/material/check?checkOrderNo=${d.checkOrderNo}`);
    },
  },
])

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
    ElMessageBox.confirm('上传会先删除当日数据，确定上传？', 'Tips', {
      type: 'warning',
    }).then(() => {
      Promise.all(keys.map((k: any) => {
        const t = fileMap[k]
        const formData = new FormData()
        formData.set("filename", t.name)
        formData.set("file", t)
        return httpUpload(`douson/material/upload`, formData)
      }))
      .then((l: any[]) => {
        afterUpload.value = true
        uploadData.value = (l[0] || {}).data || {}
        handlePage()
        return Promise.resolve()
      })
      .catch((err) => {
        ElMessage.error(`Upload failed`)
        return Promise.reject()
      })
    })
    .finally(() => {
      keys.forEach((k: any) => {
        delete fileMap[k]
      })
    })
  }
}
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
  customerShortName: '',
  customerOrderNo: '',
  customerProjectSequence: '',
  saleOrderNo: '',
  orderProjectNo: '',
  materialNo: '',
  improveMaterialDescribe: '',
  designNumber: '',
  orderCount: 0,
  productionDate: formatDate(new Date(), 'yyyy-MM-dd'),
  promiseDoneDate: '',
  blankMaterialNo: '',
  blankMaterialDescribe: '',
  roughcastDesignNumber: '',
  materialCount: 0,
  stoveNo: '',
  hotBatchNo: '',
  serialNo: '',
  surplusCount: 0,
  nde: '',
  assemble: '',
  testPress: '',
  surfaceTreatment: '',
  chargeCompany: '',
  description: '',
  arrangeProductionDate: '',
  materialOrderNo: '',
  productionCount: 0,
  checkOrderNo: '',
}
const checkOrNotList = ref([
  {
    value: '√',
    label: '√',
  },
])
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      customerShortName: '',
      customerOrderNo: '',
      customerProjectSequence: '',
      saleOrderNo: '',
      orderProjectNo: '',
      materialNo: '',
      designNumber: '',
      surplusCountType: null,
      remainCountType: includes(roleCodeList, 'materialManager') ? 2 : null,
      chargeCompany: '',
      nde: '',
      assemble: '',
      testPress: '',
      surfaceTreatment: '',
      checkOrderNo: '',
      startPromiseDoneDate: '',
      endPromiseDoneDate: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
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
  'customerOrderNo',
  'customerProjectSequence',
  'blankMaterialNo',
  'blankMaterialDescribe',
  'roughcastDesignNumber',
  'stoveNo',
  'hotBatchNo',
  'serialNo',
  'materialOrderNo',
  'checkOrderNo',
]
const showMore = ref(!includes(roleCodeList, 'materialManager'))
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

const handlePage = () => {
  httpGet(`douson/material/page`, state.query).then(
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

if (user.username === 'admin' || includes(roleCodeList, 'materialManager')) {
  columnConfigList.value = columnConfigList.value.map(t => {
    if ('description' === t.value) {
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
  if (includes(roleCodeList, 'admin') || includes(roleCodeList, 'material') || includes(roleCodeList, 'materialManager')) {
    return true
  } else {
    return false
  }
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPutJson('douson/material/merge', state.formData).then(() => {
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
  return httpPutJson('douson/material/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/material', {
      materialId: row.materialId,
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
  if (row.materialCount > row.orderCount) {
    return 'row-error'
  } else if (row.productionCount === row.materialCount) {
    return 'row-done'
  }
  return ''
}
</script>

<style scoped lang="scss">
</style>
