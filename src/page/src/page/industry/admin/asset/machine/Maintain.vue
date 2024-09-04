<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.maintainId" @keyup.enter="handlePage"
                :placeholder="store.state.label.accidentmaintain" clearable @change="handlePage" class="search-item"/>
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
      <el-select v-model="query.data.partyUser"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.partyUser"
      >
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.brokenReason"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.brokenReason"
                 class="search-item">
        <el-option
            v-for="item in config.brokenReasonList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>
      </div>
    </div>
    <div>
      <span style="font-size: 26px; font-weight: bold;">{{ store.state.label.stopHour }}:</span>
      <span style="font-weight: bold;">{{ summaryData.stopHour }}</span>
    </div>
    <view-list
        idKey="maintainId"
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
        <el-form-item prop="date" :label="store.state.label.date">
          <el-date-picker
              type="date"
              v-model="formData.date"
              format="YYYY-MM-DD"
              @change="formData.date = formatDate(formData.date, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="equipmentId" :label="store.state.label.equipmentNo">
          <el-select v-model="formData.equipmentId" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in config.equipmentNoList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="brokenReasonList" :label="store.state.label.brokenReason">
          <el-select v-model="formData.brokenReasonList" clearable filterable multiple placeholder="Please select">
            <el-option
                v-for="item in config.brokenReasonList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="brokenContent" :label="store.state.label.brokenContent">
          <el-input type="textarea" :rows=4 v-model="formData.brokenContent"/>
        </el-form-item>
        <el-form-item prop="repairContent" :label="store.state.label.repairContent">
          <el-input type="textarea" :rows=4 v-model="formData.repairContent"/>
        </el-form-item>
        <el-form-item prop="replacePair" :label="store.state.label.replacePair">
          <el-input type="textarea" :rows=4 v-model="formData.replacePair"/>
        </el-form-item>
        <el-form-item prop="repairType" :label="store.state.label.repairType">
          <el-select v-model="formData.repairType" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in config.repairTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="stopHour" :label="store.state.label.stopHour">
          <el-input-number
              v-model="formData.stopHour"
              :precision="0"
              :controls="false"
              :min="0"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="partyUser" :label="store.state.label.partyUser">
          <el-select v-model="formData.partyUserList" clearable filterable multiple placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="photoList"
                      :label="`${store.state.label.photo}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList"
                        :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="fileList"
                      :label="`${store.state.label.attachment}(${(formRuleList['fileList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.fileList"
                       :maxSize="Number(`${(formRuleList['fileList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
        </el-form-item>
        <el-form-item prop="valid" :label="store.state.label.valid">
          <el-checkbox v-model="formData.valid" name="valid" :disabled="!includes(roleCodeList, 'admin')">
            Done
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
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Printer, Search,} from '@element-plus/icons-vue'
import {useRouter, useRoute,} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {includes} from '@/util/ArrayUtil'
import {ListResult, PageResult, PageDataResult} from '@/typing/ma/System'
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
  {value: 'date', labelKey: 'date', width: 100,},
  {value: 'equipmentNo', labelKey: 'equipmentNo', width: 156,},
  {value: 'positionFormat', labelKey: 'equipmentPosition', width: 198,},
  {value: 'equipmentName', labelKey: 'equipmentName', width: 187,},
  {value: 'brokenReasonFormat', labelKey: 'brokenReason', width: 212, showOverflow: true,},
  {value: 'brokenContent', labelKey: 'brokenContent', width: 213, showOverflow: true,},
  {value: 'repairContent', labelKey: 'repairContent', width: 213, showOverflow: true,},
  {value: 'replacePair', labelKey: 'replacePair', width: 213, showOverflow: true,},
  {value: 'repairTypeFormat', labelKey: 'repairType', width: 187,},
  {value: 'stopHour', labelKey: 'stopHour', width: 100,},
  {value: 'partyUserFormat', labelKey: 'partyUser', width: 198,},
  {value: 'photoList', labelKey: 'photo', width: 128, type: ValueType.Image,},
  {value: 'fileList', labelKey: 'attachment', width: 128, type: ValueType.Attachment,},
])
/*
let str = ''
let str1 = ''
columnConfigList.value.filter((t:any) => t.value !== 'expand' && t.value !== 'operator' && t.value !== 'index').forEach((t: any) => {
  str += '    /!**\n' +
      '     * ' + store.state.label[t.labelKey] + '\n' +
      '     *!/\n' +
      '    @FieldRemark(value = "' + store.state.label[t.labelKey] + '")\n' +
      '    private String ' + t.value + ';\n'
  str1 += '    /!**\n' +
      '     * ' + store.state.label[t.labelKey] + '\n' +
      '     *!/\n' +
      '    private String ' + t.value + ';\n'
})
console.log(str)
console.log('\n\n')
console.log(str1)
*/
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/maintain?maintainId=${d.param.maintainId}`)
}

const defaultFormData = {
  date: formatDate(new Date(), 'yyyy-MM-dd'),
  equipmentId: '',
  brokenReason: '',
  brokenReasonList: [],
  brokenContent: '',
  repairContent: '',
  replacePair: '',
  repairType: '',
  stopHour: 0,
  partyUser: user.userId,
  partyUserList: [user.userId],
  photoList: [],
  fileList: [],
  valid: false,
}
const state = reactive({
  dateTimeList: [],
  query: {
    data: {
      maintainId: route.query.maintainId,
      date: '',
      startDate: '',
      endDate: '',
      equipmentId: '',
      brokenReason: '',
      brokenContent: '',
      repairContent: '',
      replacePair: '',
      repairType: '',
      partyUser: '',
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
    repairTypeList: [],
    maintainReasonList: [],
    brokenReasonList: [],
    equipmentNoList: [],
  },
  userConfigList: new Array<any>(),
  userOptionList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    date: [{required: true, message: 'Please check', trigger: 'blur'}],
    equipmentId: [{required: true, message: 'Please check', trigger: 'blur'}],
    brokenReason: [{required: true, message: 'Please check', trigger: 'blur'}],
    brokenContent: [{required: true, message: 'Please check', trigger: 'blur'}],
    partyUser: [{required: true, message: 'Please check', trigger: 'blur'}],
    brokenReasonList: [{required: false, type: 'array', message: 'Please check', min: 1, max: 99999}],
    photoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 10}],
    fileList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6}],
  },
})
const userOptionList = ref(new Array<any>())
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'repairType',
      'maintainReason',
      'brokenReason',
      'equipmentNo',
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
  handlePage()
})
const summaryData = ref({})
const handlePage = () => {
  handleDateTimeChange(false)
  httpGet(`douson/admin/maintain/page`, state.query).then(
      (res: PageDataResult<any, any>) => {
        const l = res.list || []
        state.tableData = l
        state.total = res.total
        summaryData.value = res.data || {}
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
const handleMerge = () => {
  formRef.value.validate((valid: any, fields: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/maintain', state.formData).then(() => {
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
  return httpPutJson('douson/admin/maintain', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/maintain', {maintainId: row.maintainId})
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
  userConfigList,
  total,
  dateTimeList,
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
