<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.customer"
                @change="handlePage"
                :placeholder="store.state.label.customer"
                class="search-item"/>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-input v-model="query.data.name"
                @change="handlePage"
                :placeholder="store.state.label.name"
                class="search-item"/>
      <el-input v-model="query.data.materialQuality"
                @change="handlePage"
                :placeholder="store.state.label.materialQuality"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="() => {handleDateTimeChange(dateTimeList, query.data, 'quotationDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.applyDate}`"
          :end-placeholder="`End ${store.state.label.applyDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.remarks"
                @change="handlePage"
                :placeholder="store.state.label.remarks"
                class="search-item"/>
      <el-select v-model="query.data.bidder"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.bidder"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select
          v-model="query.data.acceptOrder"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.successAcceptOrder">
        <el-option
            label="Yes"
            :value="true"
        />
        <el-option
            label="No"
            :value="false"
        />
      </el-select>
      <div class="query-btn">
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
        idKey="id"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="quotationManager ? handleDelete : null"
        :handleDeleteShow="handleDeleteShow"
        :page="query.page"
        :total="total"
        :handleTableRowClassName="handleTableRowClassName"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
    >
      <template #operator="row">
        <el-link
            v-if="!row.param.quotationItemId"
            :icon="Plus"
            @click="handleAddItem(row.param)"
            class="mr10"
            type="info"
        >
          <el-tag size="small">Add</el-tag>
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
        <el-form-item prop="customer" :label="store.state.label.customer">
          <el-input v-model="formData.customer" :disabled="!quotationManager"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber" :disabled="!quotationManager"/>
        </el-form-item>
        <el-form-item prop="designNumberList" :label="`${store.state.label.designNumber}(${(formRuleList['designNumberList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.designNumberList" :maxSize="Number(`${(formRuleList['designNumberList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!quotationManager"></file-upload>
        </el-form-item>
        <el-form-item prop="name" :label="store.state.label.name">
          <el-input v-model="formData.name" :disabled="!quotationManager"/>
        </el-form-item>
        <el-form-item prop="materialQuality" :label="store.state.label.materialQuality">
          <el-input v-model="formData.materialQuality" :disabled="!quotationManager"/>
        </el-form-item>
        <el-form-item prop="count" :label="store.state.label.count">
          <el-input v-model="formData.count" :disabled="!quotationManager"/>
        </el-form-item>
        <el-form-item prop="quotationDate" :label="store.state.label.quotationDate">
          <el-date-picker
              type="datetime"
              v-model="formData.quotationDate"
              format="YYYY-MM-DD"
              :disabled="true"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="!formSave" prop="processProcedure" :label="store.state.label.processProcedure">
          <el-select v-model="formData.processProcedure"
                     filterable
                     clearable
                     allow-create
                     :placeholder="store.state.label.processProcedure"
                     :disabled="!quotationManager"
          >
            <el-option
                v-for="item in config.quotationProcessProcedureList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!formSave" prop="processDevice" :label="store.state.label.processDevice">
          <el-select v-model="formData.processDevice"
                     filterable
                     clearable
                     :placeholder="store.state.label.processDevice"
                     :disabled="!quotationManager"
          >
            <el-option
                v-for="item in config.quotationProcessDeviceList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="processTime" :label="store.state.label.processTime">
          <el-input-number
              v-model="formData.processTime"
              :precision="2"
              :controls="false"
              :min="0"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item v-if="!formSave" prop="remarks" :label="store.state.label.remarks">
          <el-input v-model="formData.remarks" :disabled="!quotationManager"/>
        </el-form-item>
        <el-form-item prop="acceptOrder" :label="store.state.label.acceptOrder">
          <el-checkbox v-model="formData.acceptOrder" name="valid" :disabled="!quotationManager">
            {{ store.state.label.successAcceptOrder }}
          </el-checkbox>
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
import {StoreType} from '@/store/Index'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Printer, Search,} from '@element-plus/icons-vue'
import {useRouter, useRoute} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'
import FileUpload from '../../component/FileUpload.vue'
import {fullUrl} from "@/util/EnvUtil";

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const quotationView = includes(roleCodeList, 'quotationView')
const quotationManager = includes(roleCodeList, 'quotationManager')
const quotationSecurity = includes(roleCodeList, 'quotationSecurity')
const securityEdit = quotationManager || quotationSecurity
const formRef: Ref = ref(null)
const printData = ref<any>(null)
const showPrint = ref<boolean>(false)
const userOptionList = ref(new Array<any>())
const handleEditable = (row, key) => {
  return quotationManager || (!row.valid && (includes(['idAndPhotos', 'remarks', 'factoryRecords'], key) ? quotationSecurity : user.userId === row.applicant))
}
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 145, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 56, mergeKey: ['quotationId'],},
  {value: 'customer', originValue: 'customer', labelKey: 'customer', width: 98, mergeKey: ['quotationId'],},
  {value: 'designNumber', labelKey: 'designNumber', width: 118, mergeKey: ['quotationId'],},
  {value: 'name', labelKey: 'name', width: 181, mergeKey: ['quotationId'],},
  {value: 'materialQuality', labelKey: 'materialQuality', width: 68, mergeKey: ['quotationId'],},
  {value: 'count', labelKey: 'count', width: 68, mergeKey: ['quotationId'],},
  {value: 'quotationDateFormat', originValue: 'quotationDate', labelKey: 'quotationDate', width: 95, mergeKey: ['quotationId'],},
  {value: 'processProcedureFormat', originValue: 'processProcedure', labelKey: 'processProcedure', width: 127,},
  {value: 'processDeviceFormat', originValue: 'processDevice', labelKey: 'processDevice', width: 126,},
  {value: 'processUnitPrice', labelKey: 'processUnitPrice', width: 75,},
  {value: 'processTime', labelKey: 'processTime', width: 127,},
  {value: 'summaryPrice', labelKey: 'summaryPrice', width: 118,},
  {value: 'remarks', labelKey: 'remarks', width: 189,},
  {value: 'bidderFormat', originValue: 'bidder', labelKey: 'bidder', width: 136, mergeKey: ['quotationId'],},
  {value: 'acceptOrderFormat', originValue: 'acceptOrder', labelKey: 'successAcceptOrder', width: 110, mergeKey: ['quotationId'],},
  {value: 'designNumberList', labelKey: 'designNumber', width: 189, type: ValueType.Attachment,},
])
const defaultFormData = {
  creator: user.userId,
  customer: '',
  designNumber: '',
  designNumberList: new Array<any>(),
  name: '',
  materialQuality: '',
  count: null,
  quotationDate: formatDate(new Date(), 'yyyy-MM-dd'),
  startQuotationDate: '',
  endQuotationDate: '',
  processProcedure: '',
  processDevice: '',
  processUnitPrice: null,
  processTime: 0,
  summaryPrice: null,
  remarks: '',
  bidder: '',
  acceptOrder: '',
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
  // ElMessage.info('该功能正在开发中')
  printData.value = Object.assign({}, d.param || {})
  showPrint.value = true
}
const expectedVisitTimeList = ref([])
const expectedEndTimeList = ref([])
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      quotationId: route.query.quotationId || '',
      customer: '',
      designNumber: '',
      name: '',
      materialQuality: '',
      count: null,
      quotationDate: '',
      processProcedure: '',
      processDevice: '',
      processUnitPrice: null,
      processTime: '',
      summaryPrice: null,
      remarks: '',
      bidder: '',
      acceptOrder: '',
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
    quotationProcessProcedureList: [],
    quotationProcessDeviceList: [],
  },
  formVisible: false,
  formRuleList: {
    customer: [{required: true, message: 'Please check', trigger: 'blur'}],
    designNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    designNumberList: [{required: true, type: 'array', min: 1, max: 5}],
    name: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialQuality: [{required: true, message: 'Please check', trigger: 'blur'}],
  },
})
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
  if (row.applyCount !== row.storeCount) {
    return 'row-yellow'
  }
  return ''
}

const handlePage = () => {
  httpGet(`douson/quotation/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = res.list
        state.total = res.total
        ElMessage.success("Query success")
      }
  )
}
const handleSaveModal = () => {
  state.formData = Object.assign({}, defaultFormData, {processTime: 1,})
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  return quotationManager || (!row.valid && (quotationSecurity || row.applicant === user.userId))
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'quotationProcessProcedure',
      'quotationProcessDevice',
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
    if (quotationManager) {
      if (t.value === 'customer') {
        t.type = ValueType.TextEdit
      } else if ('designNumber' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('designNumberList')
          }, 100)
        }
      } else if ('name' === t.value) {
        t.type = ValueType.TextEdit
      } else if ('materialQuality' === t.value) {
        t.type = ValueType.TextEdit
      } else if ('count' === t.value) {
        t.type = ValueType.TextEdit
      } else if ('processTime' === t.value) {
        t.type = ValueType.NumberEdit
      } else if ('remarks' === t.value) {
        t.type = ValueType.TextEdit
      } else if ('processProcedureFormat' === t.value) {
        t.type = ValueType.SelectEdit
        t.allowCreate = true
        t.optionList = state.config.quotationProcessProcedureList
        t.editable = (row) => {
          return row.quotationItemId
        }
      } else if ('processDeviceFormat' === t.value) {
        t.type = ValueType.SelectEdit
        t.optionList = state.config.quotationProcessDeviceList
        t.editable = (row) => {
          return row.quotationItemId
        }
      } else if ('acceptOrderFormat' === t.value) {
        t.type = ValueType.SwitchEdit
      }
    }
    return t
  })
  handlePage()
})
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPutJson('douson/quotation/merge', state.formData).then(() => {
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
  return httpPutJson('douson/quotation/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Edit success')
    handlePage()
  })
}
const handleAddItem = (row: any) => {
  httpPostJson('douson/quotation/item', row).then(() => {
    state.formVisible = false
    ElMessage.success('Add success')
    handlePage()
  })
}
const handleDeleteShow = (row: any) => {
  return true

}
const handleDelete = (row: any) => {
  ElMessageBox.confirm(row.quotationItemId ? 'Confirm Delete?' : 'Will delete all, Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/quotation', {
      quotationId: row.quotationId,
      quotationItemId: row.quotationItemId,
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

<style lang="scss">
.device-expire {
  background-color: #eeee00 !important;
}

.device-scrap {
  background-color: #aaaaaa !important;
}
</style>
