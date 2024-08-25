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
      <el-select v-model="query.data.accidentType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.accidentType"
                 class="search-item">
        <el-option
            v-for="item in config.accidentTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.dutyPerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.dutyPerson"
      >
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.username" @keyup.enter="handlePage" :placeholder="store.state.label.username" class="search-item"/>
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
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>
      </div>
    </div>
    <view-list
        idKey="accidentId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleDelete="handleDelete"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detailLink="false"
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
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%"
               :close-on-click-modal="false"
    >
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-collapse v-model="activeNames" @change="handleChange">
          <el-collapse-item name="0">
            <template #title>
              <el-tag size="large" type="warning">{{ store.state.label[labelMap['0']] }}</el-tag>
            </template>
            <el-form-item prop="reportDate" :label="store.state.label.reportDate">
              <el-date-picker
                  type="date"
                  v-model="formData.reportDate"
                  format="YYYY-MM-DD"
                  @change="formData.reportDate = formatDate(formData.reportDate, 'yyyy-MM-dd')"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item prop="userId" :label="store.state.label.userId">
              <el-input :value="formData.username" :disabled="true"/>
            </el-form-item>
            <el-form-item prop="accidentType" :label="store.state.label.accidentType">
              <el-select v-model="formData.accidentType" clearable placeholder="请选择">
                <el-option
                    v-for="item in config.accidentTypeList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item prop="dutyPerson" :label="store.state.label.dutyPerson">
              <el-select v-model="formData.dutyPerson"
                         filterable
                         allow-create
                         clearable
                         :placeholder="store.state.label.dutyPerson"
              >
                <el-option
                    v-for="item in userOptionList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item prop="groupLeader" :label="store.state.label.groupLeader">
              <el-select v-model="formData.groupLeader"
                         filterable
                         allow-create
                         clearable
                         :placeholder="store.state.label.groupLeader"
              >
                <el-option
                    v-for="item in userOptionList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item prop="chargePerson" :label="store.state.label.chargePerson">
              <el-select v-model="formData.chargePerson"
                         filterable
                         allow-create
                         clearable
                         :placeholder="store.state.label.chargePerson"
              >
                <el-option
                    v-for="item in userOptionList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item prop="manager" :label="store.state.label.manager">
              <el-select v-model="formData.manager"
                         filterable
                         allow-create
                         clearable
                         :placeholder="store.state.label.manager"
              >
                <el-option
                    v-for="item in userOptionList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item prop="devicePhotoList" :label="store.state.label.devicePhotoList">
              <image-upload :photoList="formData.devicePhotoList"></image-upload>
            </el-form-item>
            <el-form-item prop="deviceDescribe" :label="store.state.label.deviceDescribe">
              <el-input v-model="formData.deviceDescribe"/>
            </el-form-item>
            <el-form-item prop="designNumberPhotoList" :label="store.state.label.designNumberPhotoList">
              <image-upload :photoList="formData.designNumberPhotoList"></image-upload>
            </el-form-item>
            <el-form-item prop="designNumberDescribe" :label="store.state.label.designNumberDescribe">
              <el-input v-model="formData.designNumberDescribe"/>
            </el-form-item>
            <el-form-item prop="productWeight" :label="store.state.label.productWeight">
              <el-input-number v-model="formData.productWeight" :controls="false"/>
            </el-form-item>
          </el-collapse-item>
          <el-collapse-item name="1">
            <template #title>
              <el-tag size="large" type="warning">{{ store.state.label[labelMap['1']] }}</el-tag>
            </template>
            <el-form-item prop="accidentDescribe" :label="store.state.label.accidentDescribe">
              <el-input type="textarea" :rows=4 v-model="formData.accidentDescribe"/>
            </el-form-item>
            <el-form-item prop="photoList" :label="store.state.label.photoList">
              <image-upload :photoList="formData.photoList"></image-upload>
            </el-form-item>
            <el-form-item prop="videoList" :label="store.state.label.video">
              <video-upload :videoList="formData.videoList"></video-upload>
            </el-form-item>
            <el-form-item prop="damagePhotoList" :label="store.state.label.damagePhotoList">
              <image-upload :photoList="formData.damagePhotoList"></image-upload>
            </el-form-item>
            <el-form-item prop="damageDescribe" :label="store.state.label.damageDescribe">
              <el-input type="textarea" :rows=4 v-model="formData.damageDescribe"/>
            </el-form-item>
            <el-form-item prop="propertyLossPhotoList" :label="store.state.label.propertyLossPhotoList">
              <image-upload :photoList="formData.propertyLossPhotoList"></image-upload>
            </el-form-item>
            <el-form-item prop="propertyLossDescribe" :label="store.state.label.propertyLossDescribe">
              <el-input type="textarea" :rows=4 v-model="formData.propertyLossDescribe"/>
            </el-form-item>
          </el-collapse-item>
          <el-form-item prop="humanFactorReason" :label="store.state.label.humanFactorReason">
            <el-input type="textarea" :rows=4 v-model="formData.humanFactorReason"/>
          </el-form-item>
          <el-form-item prop="humanFactorSolve" :label="store.state.label.humanFactorSolve">
            <el-input type="textarea" :rows=4 v-model="formData.humanFactorSolve"/>
          </el-form-item>
          <el-collapse-item name="4" v-if="includes(roleCodeList, 'admin')">
            <template #title>
              <el-tag size="large" type="warning">{{ store.state.label[labelMap['2']] }}</el-tag>
            </template>
            <el-form-item prop="dutyPerson1" :label="store.state.label.dutyPerson1">
              <el-input v-model="formData.dutyPerson1"/>
            </el-form-item>
            <el-form-item prop="fineAmount1" :label="store.state.label.fineAmount1">
              <el-input-number v-model="formData.fineAmount1" :controls="false"/>
            </el-form-item>
            <el-form-item prop="dutyPerson2" :label="store.state.label.dutyPerson2">
              <el-input v-model="formData.dutyPerson2"/>
            </el-form-item>
            <el-form-item prop="fineAmount2" :label="store.state.label.fineAmount2">
              <el-input-number v-model="formData.fineAmount2" :controls="false"/>
            </el-form-item>
            <el-form-item prop="dutyPerson3" :label="store.state.label.dutyPerson3">
              <el-input v-model="formData.dutyPerson3"/>
            </el-form-item>
            <el-form-item prop="fineAmount3" :label="store.state.label.fineAmount3">
              <el-input-number v-model="formData.fineAmount3" :controls="false"/>
            </el-form-item>
          </el-collapse-item>
          <el-collapse-item name="5">
            <template #title>
              <el-tag size="large" type="warning">{{ store.state.label[labelMap['3']] }}</el-tag>
            </template>
            <el-form-item prop="improveEvidencePhotoList" :label="store.state.label.improveEvidencePhotoList">
              <image-upload :photoList="formData.improveEvidencePhotoList"></image-upload>
            </el-form-item>
            <el-form-item prop="improveEvidenceDescribe" :label="store.state.label.improveEvidenceDescribe">
              <el-input type="textarea" :rows=4 v-model="formData.improveEvidenceDescribe"/>
            </el-form-item>
            <el-form-item prop="valid" :label="store.state.label.valid">
              <el-checkbox v-model="formData.valid" name="valid" :disabled="!includes(roleCodeList, 'admin')">
                Done
              </el-checkbox>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
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
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Printer, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {includes} from '@/util/ArrayUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import ImageUpload from '../../../component/ImageUpload.vue'
import VideoUpload from '../../../component/VideoUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const user = store.state.user
const formRef: Ref = ref(null)
const activeNames = ref(['0'])
const handleChange = (val: string[]) => {
  console.log(`collapse change: ${val}`)
}
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
    align: 'center',
    width: 208,
    type: ValueType.Operator,
  },
  {
    value: '0', labelKey: 'normalInfo', align: 'center', children: [
      {value: 'reportDate', labelKey: 'date', width: 100,},
      {value: 'username', labelKey: 'username', width: 116,},
      {value: 'accidentTypeFormat', labelKey: 'accidentType', width: 88,},
      {value: 'dutyPerson', labelKey: 'dutyPerson', width: 76, type: ValueType.SelectEdit, optionList: userOptionList.value,},
      {value: 'groupLeader', labelKey: 'groupLeader', width: 76, type: ValueType.SelectEdit, optionList: userOptionList.value,},
      {value: 'chargePerson', labelKey: 'chargePerson', width: 76, type: ValueType.SelectEdit, optionList: userOptionList.value,},
      {value: 'manager', labelKey: 'manager', width: 76, type: ValueType.SelectEdit, optionList: userOptionList.value,},
      {value: 'devicePhotoList', labelKey: 'devicePhoto', width: 128, type: ValueType.Image,},
      {value: 'deviceDescribe', labelKey: 'deviceDescribe', width: 112,},
      {value: 'designNumberPhotoList', labelKey: 'designNumberPhoto', width: 128, type: ValueType.Image,},
      {value: 'designNumberDescribe', labelKey: 'designNumberDescribe', width: 126, showOverflow: true,},
      {value: 'productWeight', labelKey: 'productWeight', width: 64,},
      {value: 'accidentDescribe', labelKey: 'accidentDescribe', width: 276, type: ValueType.Text, showOverflow: true,},
    ]
  },
  {
    value: '1', labelKey: 'sceneAndAccidentDescribe', children: [
      {value: 'photoList', labelKey: 'photo', width: 192, type: ValueType.Image,},
      {value: 'videoList', labelKey: 'video', width: 192, type: ValueType.Video,},
      {value: 'damagePhotoList', labelKey: 'damagePhotoList', width: 128, type: ValueType.Image,},
      {value: 'damageDescribe', labelKey: 'damageDescribe', width: 200, type: ValueType.Text, showOverflow: true,},
      {value: 'propertyLossDescribe', labelKey: 'propertyLossDescribe', width: 200, type: ValueType.Text, showOverflow: true,},
      {value: 'propertyLossPhotoList', labelKey: 'propertyLossPhotoList', width: 192, type: ValueType.Image,},
    ]
  },
  {value: 'humanFactorReason', labelKey: 'humanFactorReason', width: 276, type: ValueType.Text, showOverflow: true,},
  {value: 'humanFactorSolve', labelKey: 'humanFactorSolve', width: 276, type: ValueType.Text, showOverflow: true,},
  {
    value: '2', labelKey: 'awardOption', children: [
      {value: 'dutyPerson1', labelKey: 'dutyPerson1', width: 86,},
      {value: 'fineAmount1Format', labelKey: 'fineAmount1Format', width: 96,},
      {value: 'dutyPerson2', labelKey: 'dutyPerson2', width: 86,},
      {value: 'fineAmount2Format', labelKey: 'fineAmount2Format', width: 96,},
      {value: 'dutyPerson3', labelKey: 'dutyPerson3', width: 86,},
      {value: 'fineAmount3Format', labelKey: 'fineAmount3Format', width: 96,},
    ]
  },
  {
    value: '3', labelKey: 'preventAndTracePlan', children: [
      {value: 'improveEvidencePhotoList', labelKey: 'improveEvidencePhotoList', width: 128, type: ValueType.Image,},
      {value: 'improveEvidenceDescribe', labelKey: 'improveEvidenceDescribe', width: 210, /*type: ValueType.FixedText, */showOverflow: true,},
      {value: 'valid', labelKey: 'valid', width: 100, type: ValueType.Valid},
    ]
  },
])
const labelMap: any = {}
columnConfigList.value.filter(t => t.children && t.children.length > 0).forEach(t => {
  labelMap[t.value] = t.labelKey
})
if (!includes(roleCodeList, 'admin')) {
  // columnConfigList.value = columnConfigList.value.filter(t => t.value !== '2')
}
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.valid) {
    return 'row-done'
  }
  return ''
}
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/accident?accidentId=${d.param.accidentId}`)
}

const defaultFormData = {
  reportDate: formatDate(new Date(), 'yyyy-MM-dd'),
  userId: user.userId,
  username: user.username,
  accidentType: '',
  dutyPerson: '',
  groupLeader: '',
  chargePerson: '',
  manager: '',
  devicePhotoList: [],
  deviceDescribe: '',
  designNumberPhotoList: [],
  designNumberDescribe: '',
  productWeight: 0,
  accidentDescribe: '',
  photoList: [],
  videoList: [],
  damagePhotoList: [],
  damageDescribe: '',
  propertyLossPhotoList: [],
  propertyLossDescribe: '',
  humanFactorReason: '',
  deviceReason: '',
  materialReason: '',
  workMethodReason: '',
  environmentReason: '',
  humanFactorSolve: '',
  deviceSolve: '',
  materialSolve: '',
  workMethodSolve: '',
  environmentSolve: '',
  dutyPerson1: '',
  fineAmount1: null,
  dutyPerson2: '',
  fineAmount2: null,
  dutyPerson3: '',
  fineAmount3: null,
  improveEvidencePhotoList: [],
  improveEvidenceDescribe: '',
  valid: false,
}
const state = reactive({
  dateTimeList: [],
  expandRowKeys: [],
  query: {
    data: {
      startReportDate: '',
      endReportDate: '',
      username: '',
      accidentType: '',
      dutyPerson: '',
      valid: null,
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
    accidentTypeList: [],
  },
  userConfigList: new Array<any>(),
  userOptionList: new Array<any>(),
  formVisible: false,
  photoVisible: false,
  photoList: new Array<any>(),
  videoVisible: false,
  videoList: new Array<any>(),
  formRuleList: {
    reportDate: [{required: true, message: '请输入', trigger: 'blur'}],
    userId: [{required: true, message: '请输入', trigger: 'blur'}],
    accidentType: [{required: true, message: '请输入', trigger: 'blur'}],
    dutyPerson: [{required: true, message: '请输入', trigger: 'blur'}],
    groupLeader: [{required: true, message: '请输入', trigger: 'blur'}],
    chargePerson: [{required: true, message: '请输入', trigger: 'blur'}],
    manager: [{required: true, message: '请输入', trigger: 'blur'}],
    devicePhotoList: [{required: false, type: 'array', message: '必输', min: 0, max: 4}],
    accidentDescribe: [{required: true, message: '请输入', trigger: 'blur', max: 512}],
    photoList: [{required: false, type: 'array', message: '必输', min: 0, max: 4}],
    videoList: [{required: false, type: 'array', message: '必输', min: 0, max: 4}],
    damagePhotoList: [{required: false, type: 'array', message: '必输', min: 0, max: 4}],
    propertyLossPhotoList: [{required: false, type: 'array', message: '必输', min: 0, max: 4}],
  },
  relateFormVisible: false,
  relateFormRuleList: {},
  checkAll: false,
  isIndeterminate: true,
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
httpGet('douson/config', {
  categoryIdList: [
    'processProcedure',
    'testDevice',
    'accidentType',
  ]
}).then(r => {
  state.config = r.data
})
httpGet(`system/user/config/list`, {}).then(
    (res: ListResult<any>) => {
      state.userConfigList = res.list || []
      userOptionList.value = state.userConfigList.map((t: any) => {
        return {
          value: t.userId,
          label: t.name,
        }
      })
      columnConfigList.value = columnConfigList.value.map(t => {
        if ('0' === t.value) {
          (t.children || []).forEach(t1 => {
            if ('dutyPerson' === t1.value || 'groupLeader' === t1.value || 'chargePerson' === t1.value || 'manager' === t1.value) {
              t1.optionList = userOptionList.value
            }
          })
        }
        return t
      })
      handlePage()
    })
const handlePage = () => {
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
  httpGet(`douson/admin/accident/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        const l = res.list || []
        state.tableData = l
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
const handleSaveModal = () => {
  state.formData = Object.assign({}, defaultFormData)
  state.formData.userId = user.userId
  state.formData.username = user.username
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleMerge = () => {
  formRef.value.validate((valid: any, fields: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/accident', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handlePage()
        })
      } else {
        handleUpdate(state.formData)
      }
    } else {
      ElMessage.error('Please check input')
    }
  })
}
const handleUpdate = (row: any) => {
  return httpPutJson('douson/admin/accident', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/accident', {accidentId: row.accidentId})
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
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
const {
  query,
  tableData,
  config,
  userConfigList,
  total,
  dateTimeList,
  expandRowKeys,
  formData,
  formSave,
  photoVisible,
  photoList,
  videoVisible,
  videoList,
  formVisible,
  formRuleList,
  relateFormVisible,
  relateFormRuleList,
  checkAll,
  isIndeterminate,
} = {
  ...toRefs(state),
};

</script>

<style lang="scss">
</style>
