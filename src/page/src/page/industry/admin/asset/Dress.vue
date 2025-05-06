<template>
  <div>
    <div class="query-container">
      <el-select v-model="query.data.workDressType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.workDressType"
                 class="search-item">
        <el-option
            v-for="item in config.workDressTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.remark"
                @change="handlePage"
                :placeholder="store.state.label.remark"
                class="search-item"/>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="() => {handleDateTimeChange(dateTimeList, query.data, 'applyDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.applyDate}`"
          :end-placeholder="`End ${store.state.label.applyDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.descriptionOfOrder"
                @change="handlePage"
                :placeholder="store.state.label.descriptionOfOrder"
                class="search-item"/>
      <el-input v-model="query.data.storeDateDescription"
                @change="handlePage"
                :placeholder="store.state.label.storeDateDescription"
                class="search-item"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            v-if="includes(roleCodeList, 'dressManager')"
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
        <el-form-item prop="workDressType" :label="store.state.label.workDressType">
          <el-select v-model="formData.workDressType"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.workDressType"
                     :disabled="!dressManager"
          >
            <el-option
                v-for="item in config.workDressTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo" :disabled="!dressManager"/>
        </el-form-item>
        <el-form-item prop="remark" :label="store.state.label.remark">
          <el-input v-model="formData.remark"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber"/>
        </el-form-item>
        <el-form-item prop="designNumberList" :label="`${store.state.label.designNumberOfPdf}(${(formRuleList['designNumberList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.designNumberList" :maxSize="Number(`${(formRuleList['designNumberList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
        </el-form-item>
        <el-form-item prop="applyCount" :label="store.state.label.applyCount">
          <el-input-number v-model="formData.applyCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="applyDate" :label="store.state.label.applyDate">
          <el-date-picker
              type="date"
              v-model="formData.applyDate"
              format="YYYY-MM-DD"
              @change="formData.applyDate = formatDate(formData.applyDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="storePosition" :label="store.state.label.storePosition">
          <el-select v-model="formData.storePosition"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.storePosition"
          >
            <el-option
                v-for="item in config.storePositionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="checkAcceptUser" :label="store.state.label.checkAccept">
          <el-select v-model="formData.checkAcceptUser"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.checkAccept"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="descriptionOfOrder" :label="store.state.label.descriptionOfOrder">
          <el-input v-model="formData.descriptionOfOrder"/>
        </el-form-item>
        <el-form-item prop="storeCount" :label="store.state.label.storeCount">
          <el-input-number v-model="formData.storeCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="storePictureList" :label="`${store.state.label.storePicture}(${(formRuleList['storePictureList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.storePictureList" :maxSize="Number(`${(formRuleList['storePictureList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
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
import {StoreType} from '@/store/Index'
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
import FileUpload from "../../component/FileUpload.vue";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const dressManager = includes(roleCodeList, 'dressManager')
const storeDressManager = includes(roleCodeList, 'storeDressManager')
const formRef: Ref = ref(null)
const printData = ref<any>(null)
const showPrint = ref<boolean>(false)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 312, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 189},
  {value: 'workDressType', labelKey: 'workDressType', width: 189},
  {value: 'materialNo', labelKey: 'materialNo', width: 189},
  {value: 'remark', labelKey: 'remark', width: 189},
  {value: 'designNumber', labelKey: 'designNumber', width: 189},
  {value: 'designNumberCount', labelKey: 'designNumberOfPdf', width: 189},
  {value: 'applyCount', labelKey: 'applyCount', width: 189},
  {value: 'applyDate', labelKey: 'applyDate', width: 189},
  {value: 'storePosition', labelKey: 'storePosition', width: 189},
  {value: 'checkAcceptUserFormat', originValue: 'checkAcceptUser', labelKey: 'checkAccept', width: 189},
  {value: 'descriptionOfOrder', labelKey: 'descriptionOfOrder', width: 189},
  {value: 'storeCount', labelKey: 'storeCount', width: 189},
  {value: 'storeDateDescription', labelKey: 'storeDateDescription', width: 189},
  {value: 'storePictureList', labelKey: 'storePicture', width: 189},
])
const defaultFormData = {
  creator: user.userId,
  workDressType: '',
  materialNo: '',
  remark: '',
  designNumber: '',
  applyCount: null,
  applyDate: '',
  storePosition: '',
  checkAcceptUser: '',
  descriptionOfOrder: '',
  storeCount: null,
  storeDateDescription: '',
  designNumberList: new Array<any>(),
  storePictureList: new Array<any>(),
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
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      workDressType: '',
      materialNo: '',
      remark: '',
      designNumber: '',
      applyCount: null,
      applyDate: '',
      storePosition: '',
      checkAccept: '',
      descriptionOfOrder: '',
      storeCount: null,
      storeDateDescription: '',
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
    workDressTypeList: [],
    storePositionList: [],
  },
  formVisible: false,
  formRuleList: {
    workDressType: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    deviceNumber: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    remark: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    designNumber: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    designNumberList: [{required: dressManager, type: 'array', min: 1, max: 6}],
    applyCount: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    applyDate: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    storePosition: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    checkAcceptUser: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    descriptionOfOrder: [{required: dressManager, message: 'Please check', trigger: 'blur'}],
    storeCount: [{required: storeDressManager, message: 'Please check', trigger: 'blur'}],
    storeDateDescription: [{required: storeDressManager, message: 'Please check', trigger: 'blur'}],
    storeCount: [{required: storeDressManager, message: 'Please check', trigger: 'blur'}],
    storePictureList: [{required: false, type: 'array', min: 0, max: 6}],
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
  if (row.deviceCheckLedgerState === '999') {
    return 'device-scrap'
  } else if ('1' === row.deviceCheckLedgerState && row.dueDateWarning) {
    return 'device-expire'
  }
  return ''
}

const handlePage = () => {
  httpGet(`douson/dress/page`, state.query).then(
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
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  return dressManager || storeDressManager
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'workDressType',
      'storePosition',
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
    if (t.value === 'storePosition' && dressManager) {
      t.type = ValueType.SelectEdit
      t.optionList = state.config.storePositionList
    } else if (t.value === 'descriptionOfOrder' && (dressManager || dressManager)) {
      t.type = ValueType.TextEdit
    } else if (t.value === 'storeCount' && (dressManager)) {
      t.type = ValueType.NumberEdit
    } else if (t.value === 'storeDateDescription' && (dressManager)) {
      t.type = ValueType.TextEdit
    }
    return t
  })
  handlePage()
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
        httpPostJson('douson/admin/dress', state.formData).then(() => {
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
  return httpPutJson('douson/admin/dress', row).then(() => {
    state.formVisible = false
    ElMessage.success('Edit success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/dress', {
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
