<template>
  <div>
    <div class="query-container">
      <el-date-picker
          ref="yearPicker"
          v-model="selectedYear"
          type="year"
          placeholder="Select Year"
          @change="handleYearChange"
      />
      <el-select ref="quarterSelect" v-model="selectedQuarter" placeholder="Select Quarter" @change="handleQuarterChange">
        <el-option
            v-for="(quarter, index) in quarters"
            :key="index"
            :label="quarter.label"
            :value="quarter.value"
        />
      </el-select>
      <el-input v-model="query.data.saleOrderNo"
                @change="handleList"
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProject"
                @change="handleList"
                :placeholder="store.state.label.orderProject"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handleList"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-select v-model="query.data.customerShortName"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.customerShortName"
                 class="search-item">
        <el-option
            v-for="item in config.customerShortNameList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.purchaseOrderNo"
                @change="handleList"
                :placeholder="store.state.label.purchaseOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.poProject"
                @change="handleList"
                :placeholder="store.state.label.poProject"
                class="search-item"/>
      <el-select
          v-model="query.data.alreadySend"
          @change="handleList"
          clearable
          placeholder="Already send">
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
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
        <el-button
            :icon="Plus"
            v-if="includes(roleCodeList, 'admin') || includes(roleCodeList, 'score')"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <div>
      <span style="font-size: 26px; font-weight: bold;">{{ store.state.label.eachBoxCount }}（sum）:</span>
      <span style="font-weight: bold;">{{ summaryData.sumEachBoxCount }}</span>
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
          <el-button v-if="true" :icon="UploadFilled" @click="handleList" type="danger">Upload</el-button>
        </div>
      </el-upload>
    </div>
    <view-list
        idKey="scoreFlagId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="includes(roleCodeList, 'admin') ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handleListChange="handleListChange"
        :handleLimitChange="handleLimitChange"
        :handleTableRowClassName="handleTableRowClassName"
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
        <el-form-item prop="operator">
          <div style="display: flex; flex-direction: row-reverse !important; width: 100%;">
            <el-button :icon="Refresh"
                       @click="formData = Object.assign({}, defaultFormData, {creator: formData.creator, orderNo: formData.orderNo, scoreNumber: formData.scoreNumber, creatorFormat: formData.creatorFormat, scoreFlagId: formData.scoreFlagId, })"
                       type="warning">Reset
            </el-button>
          </div>
        </el-form-item>
        <el-form-item prop="scoreFlagId" :label="store.state.label.scoreFlagId">
          <el-input v-model="formData.scoreFlagId" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="creatorFormat" :label="store.state.label.username">
          <el-input v-model="formData.creatorFormat" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="date" :label="store.state.label.date">
          <el-date-picker
              type="date"
              v-model="formData.date"
              format="YYYY-MM-DD"
              @change="formData.date = formatDate(formData.date, 'yyyy-MM-dd')"
              :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="customerShortName" :label="store.state.label.customerShortName">
          <el-select v-model="formData.customerShortName"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.customerShortName"
                     :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          >
            <el-option
                v-for="item in config.customerShortNameList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="purchaseOrderNo" :label="store.state.label.purchaseOrderNo">
          <el-input v-model="formData.purchaseOrderNo"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="poProject" :label="store.state.label.poProject">
          <el-input v-model="formData.poProject"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="saleOrderNo" :label="store.state.label.saleOrderNo">
          <el-input v-model="formData.saleOrderNo"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="orderProject" :label="store.state.label.orderProject">
          <el-input v-model="formData.orderProject"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="eachBoxCount" :label="store.state.label.eachBoxCount">
          <el-input-number :min="0" v-model="formData.eachBoxCount"
                           :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="scoreNumber" :label="store.state.label.scoreNumber">
          <el-input v-model="formData.scoreNumber" :disabled="true">
            <template #prepend>T</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="serialNo" :label="store.state.label.serialNo">
          <el-input type="textarea" :rows=4 v-model="formData.serialNo"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"/>
        </el-form-item>
        <el-form-item prop="volume" :label="store.state.label.volume">
          <el-input-number :min="0" v-model="formData.length"
                           @change="formData.volume = formData.length * formData.width * formData.height"
                           :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
          *
          <el-input-number :min="0" v-model="formData.width"
                           :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
          *
          <el-input-number :min="0" v-model="formData.height"
                           :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="unitWeight" :label="store.state.label.unitWeight">
          <el-input-number :min="0" v-model="formData.unitWeight"
                           :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="eachBoxWeight" :label="store.state.label.eachBoxWeight">
          <el-input-number :min="0" v-model="formData.eachBoxWeight"
                           :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="orderNo" :label="store.state.label.orderNo">
          <el-input v-model="formData.orderNo" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="remark" :label="store.state.label.description">
          <el-input v-model="formData.remark"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="sendCount" :label="store.state.label.sendCount">
          <el-input-number :min="0" v-model="formData.sendCount"
                           :disabled="!includes(roleCodeList, 'scoreManager')"
                           @change="handleSendCountChange"
          />
        </el-form-item>
        <el-form-item prop="sendDate" :label="store.state.label.sendDate">
          <el-date-picker
              type="date"
              v-model="formData.sendDate"
              format="YYYY-MM-DD"
              @change="formData.sendDate = formatDate(formData.sendDate, 'yyyy-MM-dd')"
              :disabled="!includes(roleCodeList, 'scoreManager')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.photo}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleMerge">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
    <el-drawer
        v-model="showPrint"
        :with-header="true"
        size="100%"
        :append-to-body="true"
        :lock-scroll="false"
        modal-class="print-drawer"
        :z-index="99999"
    >
      <div
          style="display: flex; flex-direction: column; align-items: center; justify-content: center; margin-bottom: 10px;">
        <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 585px;">
          <h1 style="text-align: center; height: 50px;">
            <span style="font-size: 32px;">PACKING STAMP</span>
            <br>
            <span style="font-size: 22px;">TEM ĐÓNG THÙNG</span>
          </h1>
          <div style="display: flex; justify-content: flex-end; width: 100%; height: 50px;">
            <h5>{{ store.state.label.orderNo }}：{{ printData['orderNo'] }}</h5>
          </div>
          <el-descriptions
              :column="1"
              style="width: 100%;"
              border
          >`
            <el-descriptions-item
                :label="store.state.label.customerShortName"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Customer
                <br>
                Tên khách hàng
              </template>
              {{ printData['customerShortNameFormat'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.purchaseOrderNo"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                PO NO.
                <br>
                mã đơn đặt hàng của khách hàng
              </template>
              {{ `${printData['purchaseOrderNo']}-${printData['poProject']}` }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.saleOrderNo"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Sales Order
                <br>
                đơn đặt hàng
              </template>
              {{ `${printData['saleOrderNo']}-${printData['orderProject']}` }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.materialNo"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Part NO
                <br>
                mã vật liệu
              </template>
              {{ `${printData['materialNo']}` }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.eachBoxCount"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Amount Per Box
                <br>
                Số lượng mỗi thùng
              </template>
              {{ printData['eachBoxCount'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.scoreNumber"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Package NO.
                <br>
                Mã thùng
              </template>
              {{ printData['scoreNumberFormat'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.serialNo"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Serial NO.
                <br>
                Số seri
              </template>
              {{ printData['serialNo'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.username"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Operator
                <br>
                Nhân viên
              </template>
              {{ printData['creatorFormat'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.date"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Date
                <br>
                Ngày tháng
              </template>
              {{ printData['date'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.date"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Unit Weight
                <br>
                Trọng lượng mỗi con KG
              </template>
              {{ printData['unitWeight'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.date"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Gross Weight
                <br>
                Trọng lượng mỗi thùng
              </template>
              {{ printData['eachBoxWeight'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="'Country of Origin  Xuất xứ'"
                align="center"
                label-class-name="score-print-label"
            >
              <template #label>
                Country of Origin Xuất xứ
              </template>
              MADE IN VIETNAM
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Printer, Refresh, Search, UploadFilled,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpDownloadFile, httpGet, httpPostJson, httpPutJson, httpUpload} from '@/util/HttpUtil'
import {DataResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate, getQuarterStartMonthString} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../../component/ImageUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const showPrint = ref<boolean>(false)
const printData = ref<any>(null)
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 312, type: ValueType.Operator,},
  {value: 'employeeId', labelKey: 'employeeId', width: 189},
  {value: 'userName', labelKey: 'userName', width: 189},
  {value: 'quarter', labelKey: 'quarter', width: 189},
  {value: 'deviceNumber', labelKey: 'deviceNumber', width: 189},
  {value: 'qualityScore', labelKey: 'qualityScore', width: 189},
  {value: 'attendanceScore', labelKey: 'attendanceScore', width: 189},
  {value: 'safetyScore', labelKey: 'safetyScore', width: 189},
  {value: 'monthlyPerformance', labelKey: 'monthlyPerformance', width: 189},
  {value: 'totalWorkDays', labelKey: 'totalWorkDays', width: 189},
  {value: 'total', labelKey: 'total', width: 189},
  {value: 'evaluationMonths', labelKey: 'evaluationMonths', width: 189},
  {value: 'evaluationResult', labelKey: 'evaluationResult', width: 189},
  {value: 'quarterlyBonus', labelKey: 'quarterlyBonus', width: 189},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'leaderUserId', labelKey: 'leaderUserId', width: 189},
  {value: 'photos', labelKey: 'photos', width: 189},
])
const yearPicker = ref(null)
const quarterSelect = ref(null)
const selectedQuarter = ref(getQuarterStartMonthString())
const selectedYear = ref(formatDate(new Date(), 'yyyy'))
const quarters = [
  {label: "Q1 (Jan - Mar)", value: "01"},
  {label: "Q2 (Apr - Jun)", value: "04"},
  {label: "Q3 (Jul - Sep)", value: "07"},
  {label: "Q4 (Oct - Dec)", value: "10"},
]
const handleQuarterChange = (value) => {
}
const handleYearChange = (value) => {
}
const handleDownloadTemplate = () => {
  httpDownloadFile("douson/score/template", state.query.data);
}
const getYearQuarter = () => {
  console.log(`selectedYear.value: ${selectedYear.value}`)
  if (!selectedYear.value) {
    ElMessage.warning('Please select a year')
    yearPicker.value?.focus()
    return ''
  }
  if (!selectedQuarter.value) {
    ElMessage.warning('Please select a quarter')
    quarterSelect.value?.focus()
    return ''
  }
  return selectedYear.value + selectedQuarter.value
}
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
      return httpUpload(`douson/score/upload`, formData)
    }))
    .then((l: any[]) => {
      afterUpload.value = true
      uploadData.value = (l[0] || {}).data || {}
      handleList()
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
if (!includes(roleCodeList, 'scoreManager')) {
  columnConfigList.value = columnConfigList.value.map((t: any) => {
    if (t.value === 'sendCount') {
      t.type = ValueType.Text
    } else if (t.value === 'sendDate') {
      t.type = ValueType.Text
      t.width = 108
    }
    return t
  })
}

const defaultFormData = {
  employeeId: '',
  userName: '',
  quarter: '',
  deviceNumber: '',
  monthlyPerformance: '',
  totalWorkDays: '',
  total: '',
  evaluationMonths: '',
  evaluationResult: '',
  quarterlyBonus: '',
  description: '',
  leaderUserId: '',
  photoList: new Array<any>(),
}
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      employeeId: '',
      userName: '',
      quarter: '',
      deviceNumber: '',
      monthlyPerformance: '',
      totalWorkDays: '',
      total: '',
      evaluationMonths: '',
      evaluationResult: '',
      quarterlyBonus: '',
      description: '',
      leaderUserId: '',
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
  },
  formVisible: false,
  formRuleList: {
    creator: [{required: true, message: 'Please check', trigger: 'blur'}],
    date: [{required: true, message: 'Please check', trigger: 'blur'}],
    customerShortName: [{required: true, message: 'Please check', trigger: 'blur'}],
    purchaseOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    poProject: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    saleOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderProject: [{required: true, message: 'Please check', trigger: 'blur'}],
    eachBoxCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    scoreNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    serialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    volume: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', min: 0, max: 4}],
  },
})

const handleShowPrintDetail = (d: any) => {
  printData.value = Object.assign({}, d.param || {})
  showPrint.value = true
}
httpGet('douson/config').then(r => {
  state.config = r.data
})
const summaryData = ref({})
const handleList = () => {
  const quarter = getYearQuarter()
  if (quarter) {
    state.query.data.quarter = quarter
    httpGet(`douson/score/list`, state.query.data).then(
        (res: DataResult<any>) => {
          state.tableData = res.list
          summaryData.value = res.data || {}
          state.total = res.total
          ElMessage.success("Query success")
        }
    )
  }
}
const handleListChange = (val: number) => {
  state.query.page.page = val
  handleList()
}
const handleLimitChange = (val: number) => {
  state.query.page.limit = val
  handleList()
}
handleList()
const handleSaveModal = () => {
  httpGet(`douson/score/last`, {}).then(
      (res: DataResult<any>) => {
        state.formData = Object.assign({}, res.data)
        state.formVisible = true
        state.formSave = true
      }
  )
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  if (includes(roleCodeList, 'admin') || includes(roleCodeList, 'inspector')) {
    return true
  } else {
    return row.creator === store.state.user.userId
  }
}

const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (!formData.value.length || !formData.value.width || !formData.value.height) {
        ElMessage.error(store.state.label.volume)
        return
      }
      if (state.formSave) {
        httpPostJson('douson/score', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handleList()
        })
      } else {
        handleUpdate(state.formData)
      }
    }
  })
}
const handleUpdate = (row: any) => {
  return httpPutJson('douson/score', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handleList()
  })
}
const handleSendCountChange = () => {
  if (formData.value.sendCount <= 0) {
    formData.value.sendDate = ''
  } else if (!formData.value.sendDate) {
    formData.value.sendDate = formatDate(new Date(), 'yyyy-MM-dd')
  }
}

const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.sendCount > 0) {
    return 'row-done'
  }
  return ''
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/score', {
      scoreFlagId: row.scoreFlagId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handleList()
    })
  })
}
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startDate = formatDate(
        state.dateTimeList[0],
        "yyyy-MM-dd hh:mm:ss"
    );
    state.query.data.endDate = formatDate(
        state.dateTimeList[1],
        "yyyy-MM-dd hh:mm:ss"
    );
  } else {
    state.query.data.startDate = ''
    state.query.data.endDate = ''
  }
  handleList()
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
.print-label {
  width: 150px;
}

.score-print-label {
  width: 269px;
}

.score-already-send {
  background-color: #cccccc;
}
</style>
