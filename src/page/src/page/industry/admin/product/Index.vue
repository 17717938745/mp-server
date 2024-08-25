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
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
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
      <el-input v-model="query.data.nickname"
                @keyup.enter="handlePage"
                :placeholder="store.state.label.username"
                class="search-item"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            v-if="includes(roleCodeList, 'admin') || includes(roleCodeList, 'engineer')"
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <el-button
            v-if="includes(roleCodeList, 'admin')"
            class="login-btn"
            type="warning"
            @click="handleExport"
        >导出 Export
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>

    <view-list
        idKey="productId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleEditShow="handleEditShow"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
    >
      <template #expand="row">
        <!--        {{ JSON.stringify(row.param.reportId) }}-->
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
        <el-form-item prop="productId" :label="store.state.label.productId">
          <el-input v-model="formData.productId" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="openId" :label="store.state.label.openId">
          <el-input v-model="formData.openId" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="reportDate" :label="store.state.label.date">
          <el-date-picker
              type="date"
              v-model="formData.reportDate"
              format="YYYY-MM-DD"
              @change="formData.reportDate = formatDate(formData.reportDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="orderNo" :label="store.state.label.orderNo">
          <el-input v-model="formData.orderNo"/>
        </el-form-item>
        <el-form-item prop="projectSequence" :label="store.state.label.projectSequence">
          <el-input v-model="formData.projectSequence"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber"/>
        </el-form-item>
        <el-form-item prop="testDevice" :label="store.state.label.testDevice">
          <el-select v-model="formData.testDevice"
                     filterable
                     allow-create
                     clearable
                     placeholder="调试设备 Điểu chỉnh máy số" class="search-item">
            <el-option
                v-for="item in config.testDeviceList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="processProcedure" :label="store.state.label.processProcedure">
          <el-select v-model="formData.processProcedure" clearable placeholder="请选择">
            <el-option
                v-for="item in config.processProcedureList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="programNumber" :label="store.state.label.programNumber">
          <el-input v-model="formData.programNumber"/>
        </el-form-item>
        <el-form-item prop="debugMinute" :label="store.state.label.debugMinute">
          <el-input-number :min="1" v-model="formData.debugMinute"/>
        </el-form-item>
        <el-form-item prop="clampingMinute" :label="store.state.label.clampingMinute">
          <el-input-number :min="0" v-model="formData.clampingMinute"/>
        </el-form-item>
        <el-form-item prop="assistMinute" :label="store.state.label.assistMinute">
          <el-input-number :min="0" v-model="formData.assistMinute"/>
        </el-form-item>
        <el-form-item prop="runningMinute" :label="store.state.label.runningMinute">
          <el-input-number :min="0" v-model="formData.runningMinute"/>
        </el-form-item>
        <el-form-item prop="remark" :label="store.state.label.remark">
          <el-input v-model="formData.remark"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="store.state.label.photo">
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
    <el-dialog title="查看照片" v-model="photoVisible" width="60%">
      <!--<img v-for="temp in photoList" :key="temp.photoUrl :key="temp.photoUrl" :src="fullUrl(temp.photoUrl, '')" style="width: 100%;" alt="picture"/>-->
      <el-image
          v-if="photoList.length > 0"
          v-for="t in photoList"
          :src="fullUrl(t.photoCompressUrl, '')"
          fit="cover"
          :preview-src-list="[fullUrl(t.photoUrl, '')]"
          :initial-index="0"
          class="image-view"
      />
      <el-empty v-else description="Empty"/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="photoVisible = false">Cancel</el-button>
          <el-button type="primary" @click="photoVisible = false">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage, UploadFile, UploadFiles} from 'element-plus'
import {Search, UploadFilled, CircleClose, Plus,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDownloadFile, httpGet, httpPostJson, httpPutJson, httpUpload} from '@/util/HttpUtil'
import {includes} from '@/util/ArrayUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {fullUrl} from '@/util/EnvUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import ImageUpload from '../../component/ImageUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
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
    align: 'center',
    width: 239,
    type: ValueType.Operator,
  },
  {value: 'usernameFormat', labelKey: 'username', width: 128},
  {value: 'reportDate', labelKey: 'date', width: 108},
  {value: 'orderNo', labelKey: 'orderNo', width: 100, type: ValueType.OrderLink,},
  {value: 'projectSequence', labelKey: 'projectSequence', width: 64},
  {value: 'designNumber', labelKey: 'designNumber', width: 160, align: 'left',},
  {value: 'testDeviceFormat', labelKey: 'testDevice', width: 128, type: ValueType.DeviceLink, align: 'left',},
  {value: 'processProcedureFormat', labelKey: 'processProcedure', width: 128, align: 'left',},
  {value: 'programNumber', labelKey: 'programNumber', width: 64},
  {value: 'debugMinute', labelKey: 'debugMinute', width: 64},
  {value: 'clampingMinute', labelKey: 'clampingMinute', width: 64},
  {value: 'assistMinute', labelKey: 'assistMinute', width: 64},
  {value: 'runningMinute', labelKey: 'runningMinute', width: 80},
  {value: 'productStandMinute', labelKey: 'productStandMinute', width: 64, type: ValueType.HighLight,},
  {value: 'countHour8', labelKey: 'countHour8', width: 96},
  {value: 'countHour12', labelKey: 'countHour12', width: 96},
  {value: 'remark', labelKey: 'remark', width: 269, align: 'left',},
  {value: 'photoList', labelKey: 'photo', width: 269, type: ValueType.Image,},
])
const defaultFormData = {
  productId: '',
  openId: '',
  reportDate: formatDate(new Date(), 'yyyy-MM-dd'),
  orderNo: '',
  projectSequence: '',
  designNumber: '',
  testDevice: '',
  processProcedure: '',
  programNumber: '',
  debugMinute: 0,
  clampingMinute: 0,
  assistMinute: 0,
  runningMinute: 0,
  remark: '',
  photoList: new Array<any>(),
}
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      startReportDate: '',
      endReportDate: '',
      designNumber: '',
      testDevice: '',
      openId: '',
      nickname: '',
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
  },
  formVisible: false,
  formRuleList: {
    reportDate: [{required: true, message: '请输入', trigger: 'blur'}],
    orderNo: [{required: true, message: '请输入', trigger: 'blur'}],
    projectSequence: [{required: true, message: '请输入', trigger: 'blur'}],
    designNumber: [{required: true, message: '请输入', trigger: 'blur'}],
    testDevice: [{required: true, message: '请输入', trigger: 'blur'}],
    processProcedure: [{required: true, message: '请输入', trigger: 'blur'}],
    programNumber: [{required: true, message: '请输入', trigger: 'blur'}],
    debugMinute: [{required: true, message: '请输入', trigger: 'blur'}],
    clampingMinute: [{required: true, message: '请输入', trigger: 'blur'}],
    assistMinute: [{required: true, message: '请输入', trigger: 'blur'}],
    runningMinute: [{required: true, message: '请输入', trigger: 'blur'}],
    remark: [{required: true, message: '请输入', trigger: 'blur'}],
    photoList: [{required: true, type: 'array', message: '必输', min: 1, max: 4}],
  },
})

const handleFormatValue = (key: string, val: any) => {
  // @ts-ignore
  const a = (state.config[key] || []).filter(t => t.value === val)
  return a.length > 0 ? a[0].label : val
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
httpGet('douson/config').then(r => {
  state.config = r.data
})
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startReportDate = formatDate(
        state.dateTimeList[0],
        "yyyy-MM-dd hh:mm:ss"
    );
    state.query.data.endReportDate = formatDate(
        state.dateTimeList[1],
        "yyyy-MM-dd hh:mm:ss"
    );
  } else {
    state.query.data.startReportDate = ''
    state.query.data.endReportDate = ''
  }
  handlePage()
}
const handlePage = () => {
  httpGet(`douson/admin/product/page`, state.query).then(
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
  if(includes(roleCodeList, 'admin')) {
    return true
  } else{
    return row.creator === store.state.user.userId
  }
}

const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/product', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handlePage()
        })
      } else {
        httpPutJson('douson/admin/product', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Update success')
          handlePage()
        })
      }
    }
  })
}

const handleExport = () => {
  httpDownloadFile("douson/admin/product/export", state.query.data);
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

<style scoped lang="scss">
</style>
