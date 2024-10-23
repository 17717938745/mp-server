<template>
  <div>
    <div class="query-container">
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start due date"
          end-placeholder="End due date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.deviceNumber"
                @change="handlePage"
                :placeholder="store.state.label.deviceNumber"
                class="search-item"/>
      <el-input v-model="query.data.specification"
                @change="handlePage"
                :placeholder="store.state.label.specification"
                class="search-item"/>
      <el-select v-model="query.data.chineseVietnamName"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.chineseVietnamName"
                 class="search-item">
        <el-option
            v-for="item in config.chineseVietnamNameList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select
          v-model="query.data.deviceCheckLedgerState"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.deviceCheckLedgerState">
        <el-option
            v-for="item in config.deviceCheckLedgerStateList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.borrower"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.borrower"
                 @change="handlePage"
      >
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select
          v-model="query.data.storage"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.storage">
        <el-option
            v-for="item in config.storageList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select
          v-model="query.data.outOfStock"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.outOfStock">
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
            v-if="includes(roleCodeList, 'gauger')"
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <view-list
        idKey="deviceCheckLedgerId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="user.userId === 'admin' ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handleTableRowClassName="handleTableRowClassName"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
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
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="deviceNumber" :label="store.state.label.deviceNumber">
          <el-input v-model="formData.deviceNumber"/>
        </el-form-item>
        <el-form-item prop="chineseVietnamName" :label="store.state.label.chineseVietnamName">
          <el-select v-model="formData.chineseVietnamName"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.chineseVietnamName"
          >
            <el-option
                v-for="item in config.chineseVietnamNameList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="englishName" :label="store.state.label.englishName">
          <el-input v-model="formData.englishName"/>
        </el-form-item>
        <el-form-item prop="specification" :label="store.state.label.specification">
          <el-input v-model="formData.specification"/>
        </el-form-item>
        <el-form-item prop="calibrationUnit" :label="store.state.label.calibrationUnit">
          <el-input v-model="formData.calibrationUnit"/>
        </el-form-item>
        <el-form-item prop="calibrationDate" :label="store.state.label.calibrationDate">
          <el-date-picker
              type="date"
              v-model="formData.calibrationDate"
              format="YYYY-MM-DD"
              @change="handleCalcValidDate"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="dueDate" :label="store.state.label.dueDate">
          <el-date-picker
              :disabled="true"
              type="date"
              v-model="formData.dueDate"
              format="YYYY-MM-DD"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="calibrationPeriod" :label="store.state.label.calibrationPeriod">
          <el-select v-model="formData.calibrationPeriod"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.calibrationPeriod"
                     @change="handleCalcValidDate"
          >
            <el-option
                v-for="item in config.calibrationPeriodList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="deviceCheckLedgerState" :label="store.state.label.deviceCheckLedgerState">
          <el-select v-model="formData.deviceCheckLedgerState"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.deviceCheckLedgerState"
          >
            <el-option
                v-for="item in config.deviceCheckLedgerStateList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="manufacturers" :label="store.state.label.manufacturers">
          <el-input v-model="formData.manufacturers"/>
        </el-form-item>
        <el-form-item prop="acceptanceStandard" :label="store.state.label.acceptanceStandard">
          <el-input v-model="formData.acceptanceStandard"/>
        </el-form-item>
        <el-form-item prop="storage" :label="store.state.label.storage">
          <el-select v-model="formData.storage"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.storage"
          >
            <el-option
                v-for="item in config.storageList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="outOfStock" :label="store.state.label.outOfStock">
          <el-select
              v-model="formData.outOfStock"
              filterable
              allow-create
              clearable
              :placeholder="store.state.label.outOfStock">
            <el-option
                label="Yes"
                :value="true"
            />
            <el-option
                label="No"
                :value="false"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="borrowerList" :label="store.state.label.borrower">
          <el-select v-model="formData.borrowerList"
                     filterable
                     allow-create
                     clearable
                     multiple
                     :placeholder="store.state.label.borrower"
                     @change="handleBorrowerChange"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="borrowDate" :label="store.state.label.borrowDate">
          <el-date-picker
              type="date"
              v-model="formData.borrowDate"
              format="YYYY-MM-DD"
              @change="formData.borrowDate = formatDate(formData.borrowDate, 'yyyy-MM-dd')"
              :disabled="(formData.borrower || '').length <= 0"
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
        size="100%"
        :append-to-body="true"
        :lock-scroll="false"
        modal-class="print-drawer"
        :z-index="99999"
    >
      <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; margin-bottom: 20px;">
        <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 585px;">
          <h1 style="text-align: center;">
            <span style="font-size: 36px;">PACKING STAMP</span>
            <br>
            <span style="font-size: 24px;">TEM ĐÓNG THÙNG</span>
          </h1>
          <div style="display: flex; justify-content: flex-end; width: 100%;">
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
            >
              <template #label>
                Amount Per Box
                <br>
                Số lượng mỗi thùng
              </template>
              {{ printData['eachBoxCount'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.boxNumber"
                align="center"
                label-class-name="box-print-label"
            >
              <template #label>
                Package NO.
                <br>
                Mã thùng
              </template>
              {{ printData['boxNumberFormat'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.serialNo"
                align="center"
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
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
                label-class-name="box-print-label"
            >
              <template #label>
                Gross Weight
                <br>
                Trọng lượng mỗi thùng
              </template>
              {{ printData['eachBoxWeight'] }}
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
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const printData = ref<any>(null)
const showPrint = ref<boolean>(false)
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
  {value: 'index', labelKey: 'index', width: 78},
  {value: 'photoCount', labelKey: 'photoCount', width: 57},
  {value: 'materialNo', labelKey: 'materialNo', width: 96},
  {value: 'deviceNumber', labelKey: 'deviceNumber', width: 158},
  {value: 'chineseVietnamNameFormat', labelKey: 'chineseVietnamName', width: 169},
  {value: 'englishName', labelKey: 'englishName', width: 138},
  {value: 'specification', labelKey: 'specification', width: 96},
  {value: 'calibrationUnit', labelKey: 'calibrationUnit', width: 85},
  {value: 'calibrationDate', labelKey: 'calibrationDate', width: 102},
  {value: 'dueDate', labelKey: 'dueDate', width: 102},
  {value: 'calibrationPeriod', labelKey: 'calibrationPeriod', width: 96},
  {value: 'deviceCheckLedgerStateFormat', labelKey: 'state', width: 169},
  {value: 'manufacturers', labelKey: 'manufacturers', width: 96},
  {value: 'acceptanceStandard', labelKey: 'acceptanceStandard', width: 168},
  {value: 'storageFormat', labelKey: 'storage', width: 138},
  {value: 'outOfStockFormat', labelKey: 'outOfStock', width: 96},
  {value: 'borrowerFormat', originValue: 'borrowerList', labelKey: 'borrower', width: 168,},
  {value: 'borrowDate', labelKey: 'borrowDate', width: 102},
  {value: 'photoList', labelKey: 'photo', width: 96, type: ValueType.Image},
])
const defaultFormData = {
  creator: user.userId,
  index: '',
  materialNo: '',
  deviceNumber: '',
  chineseVietnamName: '',
  englishName: '',
  specification: '',
  calibrationUnit: '',
  calibrationDate: '',
  dueDate: '',
  calibrationPeriod: null,
  manufacturers: '',
  acceptanceStandard: '',
  storage: '',
  outOfStock: '',
  borrower: '',
  borrowerList: [],
  borrowDate: '',
  photo: '',
  state: '',
  deviceCheckLedgerState: '',
  photoList: new Array<any>(),
}

const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      startDueDate: '',
      endDueDate: '',
      deviceNumber: '',
      specification: '',
      chineseVietnamName: '',
      deviceCheckLedgerState: '',
      borrower: '',
      storage: '',
      outOfStock: null,
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
    chineseVietnamNameList: [],
    calibrationPeriodList: [],
    deviceCheckLedgerStateList: [],
    storageList: [],
  },
  formVisible: false,
  managerEdit: false,
  formRuleList: {
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    deviceNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    chineseVietnamName: [{required: true, message: 'Please check', trigger: 'blur'}],
    englishName: [{required: true, message: 'Please check', trigger: 'blur'}],
    specification: [{required: true, message: 'Please check', trigger: 'blur'}],
    calibrationUnit: [{required: true, message: 'Please check', trigger: 'blur'}],
    calibrationDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    calibrationPeriod: [{required: true, message: 'Please check', trigger: 'blur'}],
    state: [{required: true, message: 'Please check', trigger: 'blur'}],
    manufacturers: [{required: true, message: 'Please check', trigger: 'blur'}],
    acceptanceStandard: [{required: true, message: 'Please check', trigger: 'blur'}],
    storage: [{required: true, message: 'Please check', trigger: 'blur'}],
    outOfStock: [{required: true, message: 'Please check', trigger: 'blur'}],
    borrowerList: [{required: false, type: 'array', min: 0, max: 99999}],
    photoList: [{required: false, type: 'array', min: 0, max: 3}],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startDueDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endDueDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startDueDate = ''
    state.query.data.endDueDate = ''
  }
  handlePage()
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
  if (row.deviceCheckLedgerState === '999') {
    return 'device-scrap'
  } else if ('1' === row.deviceCheckLedgerState && row.dueDateWarning) {
    return 'device-expire'
  }
  return ''
}
const handleBorrowerChange = () => {
  if (formData.value.borrower) {
    formData.value.borrowDate = formatDate(new Date(), 'yyyy-MM-dd')
  } else {
    formData.value.borrowDate = ''
  }
}
const handleCalcValidDate = () => {
  if (formData.value.calibrationDate) {
    if ('[object Date]' === Object.prototype.toString.call(formData.value.calibrationDate)) {
      formData.value.calibrationDate = formatDate(formData.value.calibrationDate, 'yyyy-MM-dd')
    }
    const d = new Date(formData.value.calibrationDate)
    if (formData.value.calibrationPeriod) {
      const d1 = new Date(d.getTime() + 1000 * 3600 * 24 * Number(formData.value.calibrationPeriod))
      formData.value.dueDate = formatDate(d1, 'yyyy-MM-dd')
    }
  }
}
const handlePage = () => {
  httpGet(`douson/admin/device-check-ledger/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = res.list
        state.total = res.total
        ElMessage.success("Query success")
      }
  )
}
const handleSaveModal = () => {
  state.formData = Object.assign({}, defaultFormData)
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any, managerEdit: boolean = false) => {
  state.managerEdit = managerEdit
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  if (includes(roleCodeList, 'gauger')) {
    return true
  } else {
    return false
  }
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'chineseVietnamName',
      'calibrationPeriod',
      'deviceCheckLedgerState',
      'storage',
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
  if (includes(roleCodeList, 'gauger')) {
    columnConfigList.value = columnConfigList.value.map((t: any) => {
      if (t.value === 'borrowerFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      }
      return t
    })
  }
  handlePage()
})
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/device-check-ledger', state.formData).then(() => {
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
  return httpPutJson('douson/admin/device-check-ledger', row).then(() => {
    state.formVisible = false
    ElMessage.success('Edit success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/device-check-ledger', {
      disqualificationOrderId: row.disqualificationOrderId,
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
  managerEdit,
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
