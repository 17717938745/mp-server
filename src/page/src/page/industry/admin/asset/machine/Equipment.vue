<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.equipmentId" @keyup.enter="handlePage" :placeholder="store.state.label.equipmentId" clearable @change="handlePage" class="search-item"/>
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
      <el-select v-model="query.data.equipmentId"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.equipmentNo"
      >
        <el-option
            v-for="item in config.equipmentNoList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.equipmentName"
                @change="handlePage"
                :placeholder="store.state.label.equipmentName"
                class="search-item"/>
      <el-input v-model="query.data.specification"
                @change="handlePage"
                :placeholder="store.state.label.specification"
                class="search-item"/>
      <el-select v-model="query.data.position"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.equipmentPosition"
      >
        <el-option
            v-for="item in config.equipmentPositionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button :icon="Plus" @click="handleSaveModal" type="success" v-if="includes(roleCodeList, 'equipmentManager')">Add</el-button>
      </div>
    </div>
    <view-list
        idKey="equipmentId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleEditShow="handleEditShow"
        :handleUpdate="handleUpdate"
        :handleDelete="'admin' === user.username ? handleDelete : null"
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
        <el-form-item prop="equipmentNo" :label="store.state.label.equipmentId">
          <el-input v-model="formData.equipmentNo" :placeholder="store.state.label.equipmentNo"/>
        </el-form-item>
        <el-form-item prop="equipmentName" :label="store.state.label.equipmentName">
          <el-input v-model="formData.equipmentName" :placeholder="store.state.label.equipmentName"/>
        </el-form-item>
        <el-form-item prop="specification" :label="store.state.label.specification">
          <el-input v-model="formData.specification" :placeholder="store.state.label.specification"/>
        </el-form-item>
        <el-form-item prop="date" :label="store.state.label.date">
          <el-date-picker
              type="date"
              v-model="formData.date"
              format="YYYY-MM-DD"
              @change="formData.date = formatDate(formData.date, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="useUserList" :label="store.state.label.useUser">
          <el-select v-model="formData.useUserList" clearable filterable multiple placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="remark" :label="store.state.label.description">
          <el-input type="textarea" :rows=4 v-model="formData.remark"/>
        </el-form-item>
        <el-form-item prop="detailDescribe" :label="store.state.label.equipmentDetailDescribe">
          <el-input type="textarea" :rows=4 v-model="formData.detailDescribe"/>
        </el-form-item>
        <el-form-item prop="gasolineType" :label="store.state.label.gasolineType">
          <el-input type="textarea" :rows=4 v-model="formData.gasolineType"/>
        </el-form-item>
        <el-form-item prop="chargeUser" :label="store.state.label.chargeUser">
          <el-select v-model="formData.chargeUserList" clearable filterable multiple placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="position" :label="store.state.label.equipmentPosition">
          <el-select v-model="formData.position" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in config.equipmentPositionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.photo}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="fileList" :label="`${store.state.label.attachment}(${(formRuleList['fileList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.fileList" :maxSize="Number(`${(formRuleList['fileList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
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
import {useRouter, useRoute,} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {includes} from '@/util/ArrayUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import ImageUpload from '../../../component/ImageUpload.vue'
import FileUpload from '../../../component/FileUpload.vue'

const router = useRouter()
const route = useRoute()
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
    width: 248,
    type: ValueType.Operator,
  },
  {value: 'index', labelKey: 'index', width: 80,},
  {value: 'equipmentNo', labelKey: 'equipmentNo', width: 146,},
  {value: 'equipmentName', labelKey: 'equipmentName', width: 232,},
  {value: 'specification', labelKey: 'specification', width: 265,},
  {value: 'date', labelKey: 'date', width: 100,},
  {value: 'useUserFormat', labelKey: 'useUser', width: 289,},
  {value: 'remark', labelKey: 'description', width: 232, showOverflow: true,},
  {value: 'detailDescribe', labelKey: 'equipmentDetailDescribe', width: 232, showOverflow: true,},
  {value: 'gasolineType', labelKey: 'gasolineType', width: 187, showOverflow: true,},
  {value: 'chargeUserFormat', labelKey: 'chargeUser', width: 135, showOverflow: true,},
  {value: 'positionFormat', labelKey: 'equipmentPosition', width: 189,},
  {value: 'photoList', labelKey: 'photo', width: 128, type: ValueType.Image,},
  {value: 'fileList', labelKey: 'supportAttachment', width: 128, type: ValueType.Attachment,},
  {value: 'apiDeviceFormat', originValue: 'apiDevice', labelKey: 'apiDevice', width: 127,},
])
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
  window.open(`/industry/public/equipment?equipmentId=${d.param.equipmentId}`)
}

const defaultFormData = {
  equipmentId: '',
  equipmentNo: '',
  equipmentName: '',
  specification: '',
  date: formatDate(new Date(), 'yyyy-MM-dd'),
  useUserList: [user.userId],
  remark: '',
  detailDescribe: '',
  gasolineType: '',
  chargeUser: '',
  chargeUserList: [],
  position: '',
  photoList: [],
  fileList: [],
}
const state = reactive({
  dateTimeList: [],
  expandRowKeys: [],
  query: {
    data: {
      equipmentId: route.query.equipmentId,
      equipmentNo: '',
      equipmentName: '',
      specification: '',
      startDate: '',
      endDate: '',
      useUser: '',
      remark: '',
      detailDescribe: '',
      chargeUser: '',
      position: '',
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
    equipmentNoList: [],
    equipmentPositionList: [],
    apiDeviceList: [],
  },
  userOptionList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    equipmentNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    equipmentName: [{required: true, message: 'Please check', trigger: 'blur'}],
    specification: [{required: true, message: 'Please check', trigger: 'blur'}],
    date: [{required: true, message: 'Please check', trigger: 'blur'}],
    useUserList: [{required: true, type: 'array', message: 'Please check', min: 1, max: 999,}],
    position: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 10,}],
    fileList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 10,}],
  },
})
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'equipmentNo',
      'equipmentPosition',
      'apiDevice',
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
  if (includes(roleCodeList, 'equipmentManager')) {
    columnConfigList.value = columnConfigList.value.map(t => {
      if (t.value === 'apiDeviceFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = state.config.apiDeviceList
      }
      return t
    })
  }
  handlePage()
})
const handlePage = () => {
  handleDateTimeChange(false)
  httpGet(`douson/admin/equipment/page`, state.query).then(
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
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  if (includes(roleCodeList, 'equipmentManager')) {
    return true
  }
  return false
}
const handleMerge = () => {
  formRef.value.validate((valid: any, fields: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/equipment', state.formData).then(() => {
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
  return httpPutJson('douson/admin/equipment', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/equipment', {equipmentId: row.equipmentId})
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const handleDateTimeChange = (query: boolean = true) => {
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
  if (query) {
    handlePage()
  }
}
const {
  query,
  tableData,
  config,
  total,
  dateTimeList,
  expandRowKeys,
  formData,
  formSave,
  formVisible,
  formRuleList,
} = {
  ...toRefs(state),
}

</script>

<style lang="scss">
</style>
