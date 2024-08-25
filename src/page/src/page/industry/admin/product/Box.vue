<template>
  <div>
    <!--    <div>
          <el-alert title="该功能正在开发中" type="error"/>
        </div>-->
    <div class="query-container">
      <!--      <el-date-picker
                v-model="dateTimeList"
                @change="handleDateTimeChange"
                type="daterange"
                format="YYYY-MM-DD"
                range-separator="-"
                start-placeholder="Start date"
                end-placeholder="End date"
                style="width: 180px; margin-right: 20px;"
            >
            </el-date-picker>-->
      <el-input v-model="query.data.orderNo"
                @change="handlePage"
                :placeholder="store.state.label.orderNo"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start date"
          end-placeholder="End date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.saleOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProject"
                @change="handlePage"
                :placeholder="store.state.label.orderProject"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-select v-model="query.data.customerShortName"
                 @change="handlePage"
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
                @change="handlePage"
                :placeholder="store.state.label.purchaseOrderNo"
                class="search-item"/>
      <el-select
          v-model="query.data.alreadySend"
          @change="handlePage"
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
      <el-input v-model="query.data.poProject"
                @change="handlePage"
                :placeholder="store.state.label.poProject"
                class="search-item"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            :icon="Plus"
            v-if="includes(roleCodeList, 'admin') || includes(roleCodeList, 'box')"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>

    <view-list
        idKey="boxFlagId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="includes(roleCodeList, 'admin') ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
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
                       @click="formData = Object.assign({}, defaultFormData, {creator: formData.creator, orderNo: formData.orderNo, boxNumber: formData.boxNumber, creatorFormat: formData.creatorFormat, boxFlagId: formData.boxFlagId, })"
                       type="warning">Reset
            </el-button>
          </div>
        </el-form-item>
        <el-form-item prop="boxFlagId" :label="store.state.label.boxFlagId">
          <el-input v-model="formData.boxFlagId" :disabled="true"/>
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
        <el-form-item prop="boxNumber" :label="store.state.label.boxNumber">
          <el-input v-model="formData.boxNumber" :disabled="true">
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
        <el-form-item prop="remark" :label="store.state.label.remark">
          <el-input v-model="formData.remark"
                    :disabled="!includes(roleCodeList, 'admin') && store.state.user.userId !== formData.creator"
          />
        </el-form-item>
        <el-form-item prop="sendCount" :label="store.state.label.sendCount">
          <el-input-number :min="0" v-model="formData.sendCount"
                           :disabled="!includes(roleCodeList, 'boxManager')"
                           @change="handleSendCountChange"
          />
        </el-form-item>
        <el-form-item prop="sendDate" :label="store.state.label.sendDate">
          <el-date-picker
              type="date"
              v-model="formData.sendDate"
              format="YYYY-MM-DD"
              @change="formData.sendDate = formatDate(formData.sendDate, 'yyyy-MM-dd')"
              :disabled="!includes(roleCodeList, 'boxManager')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="photoList" :label="store.state.label.mostFourPhoto">
          <image-upload :photoList="formData.photoList"></image-upload>
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
      <div
          style="display: flex; flex-direction: column; align-items: center; justify-content: center; margin-bottom: 20px;">
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
import {Plus, Printer, Refresh, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {DataResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const showPrint = ref<boolean>(false)
const printData = ref<any>(null)
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
    width: 312,
    type: ValueType.Operator,
  },
  {value: 'photoCount', labelKey: 'photoCount', width: 57},
  {value: 'creatorFormat', labelKey: 'username', width: 165},
  {value: 'date', labelKey: 'date', width: 108},
  {value: 'customerShortNameFormat', labelKey: 'customerShortName', width: 108},
  {value: 'purchaseOrderNo', labelKey: 'purchaseOrderNo', width: 128},
  {value: 'poProject', labelKey: 'poProject', width: 68},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 108},
  {value: 'orderProject', labelKey: 'orderProject', width: 50},
  {value: 'materialNo', labelKey: 'materialNo', width: 163, align: 'left',},
  {value: 'eachBoxCount', labelKey: 'eachBoxCount', width: 50},
  {value: 'boxNumberFormat', labelKey: 'boxNumber', width: 68},
  {value: 'serialNo', labelKey: 'serialNo', width: 260, type: ValueType.Text,},
  {value: 'volumeFormat', labelKey: 'volume', width: 124,},
  {value: 'unitWeight', labelKey: 'unitWeight', width: 50,},
  {value: 'eachBoxWeight', labelKey: 'eachBoxWeight', width: 73,},
  {value: 'orderNo', labelKey: 'orderNo', width: 82,},
  {value: 'remark', labelKey: 'remark', width: 270, type: ValueType.FixedText,},
  {value: 'sendCount', labelKey: 'sendCount', width: 98, type: ValueType.NumberEdit,},
  {value: 'sendDate', labelKey: 'sendDate', width: 108, type: ValueType.DateEdit,},
  {value: 'photoList', labelKey: 'photoList', width: 269, type: ValueType.Image,},
])

if (!includes(roleCodeList, 'boxManager')) {
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
  boxFlagId: '',
  creator: '',
  creatorFormat: store.state.user.username,
  date: formatDate(new Date(), 'yyyy-MM-dd'),
  customerShortName: '',
  purchaseOrderNo: '',
  poProject: '',
  saleOrderNo: '',
  orderProject: '',
  materialNo: '',
  eachBoxCount: 0,
  boxNumber: 0,
  serialNo: '',
  volume: '',
  length: 0,
  width: 0,
  height: 0,
  unitWeight: 0,
  eachBoxWeight: 0,
  orderNo: '',
  remark: '',
  sendCount: 0,
  sendDate: '',
  photoList: new Array<any>(),
}
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      orderNo: '',
      saleOrderNo: '',
      orderProject: '',
      poProject: '',
      customerShortName: '',
      purchaseOrderNo: '',
      materialNo: '',
      alreadySend: null,
      startDate: '',
      endDate: '',
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
    boxNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    serialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    volume: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
  },
})

const handleShowPrintDetail = (d: any) => {
  printData.value = Object.assign({}, d.param || {})
  showPrint.value = true
}
httpGet('douson/config').then(r => {
  state.config = r.data
})
const handlePage = () => {
  httpGet(`douson/admin/box-flag/page`, state.query).then(
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
const handleSaveModal = () => {
  httpGet(`douson/admin/box-flag/last`, {}).then(
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
        httpPostJson('douson/admin/box-flag', state.formData).then(() => {
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
  return httpPutJson('douson/admin/box-flag', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
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
    httpDelete('douson/admin/box-flag', {
      boxFlagId: row.boxFlagId,
    })
        .then(() => {
          ElMessage.success('Delete success')
          handlePage()
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
  handlePage()
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

.box-print-label {
  width: 269px;
}

.box-already-send {
  background-color: #cccccc;
}
</style>
