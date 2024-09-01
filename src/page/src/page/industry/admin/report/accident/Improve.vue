<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.improveId" @keyup.enter="handlePage" :placeholder="store.state.label.accidentImprove" clearable @change="handlePage" class="search-item"/>
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
      <el-select v-model="query.data.userId"
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
      <el-select v-model="query.data.reason"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.improveReason"
                 class="search-item">
        <el-option
            v-for="item in config.improveReasonList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <!--      <el-select v-model="query.data.valid"
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
            </el-select>-->
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>
      </div>
    </div>
    <view-list
        idKey="improveId"
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
        <el-form-item prop="reportDate" :label="store.state.label.reportDate">
          <el-date-picker
              type="date"
              v-model="formData.reportDate"
              format="YYYY-MM-DD"
              @change="formData.reportDate = formatDate(formData.reportDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="userId" :label="store.state.label.user">
          <el-select v-model="formData.userId" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="directLeader" :label="store.state.label.directLeader">
          <el-select v-model="formData.directLeader" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="accidentDescribe" :label="store.state.label.accidentImproveDescribe">
          <el-input type="textarea" :rows=4 v-model="formData.accidentDescribe"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.improvePhoto}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="fileList" :label="`${store.state.label.improveFile}(${(formRuleList['fileList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.fileList" :maxSize="Number(`${(formRuleList['fileList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
        </el-form-item>
        <el-form-item prop="reason" :label="store.state.label.improveReason">
          <el-select v-model="formData.reasonList"
                     filterable
                     multiple
                     allow-create
                     clearable
                     :placeholder="store.state.label.improveReason"
                     class="search-item">
            <el-option
                v-for="item in config.improveReasonList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="solve" :label="store.state.label.improveSolve">
          <el-input type="textarea" :rows=4 v-model="formData.solve"/>
        </el-form-item>
        <el-form-item prop="improvePhotoList" :label="`${store.state.label.improveImprovePhoto}(${(formRuleList['improvePhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.improvePhotoList" :maxSize="Number(`${(formRuleList['improvePhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="improveFileList" :label="`${store.state.label.improveImproveFile}(${(formRuleList['improveFileList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.improveFileList" :maxSize="Number(`${(formRuleList['improveFileList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
        </el-form-item>
        <el-form-item prop="improveDescribe" :label="store.state.label.improveDescribe">
          <el-input type="textarea" :rows=4 v-model="formData.improveDescribe"/>
        </el-form-item>
        <el-form-item prop="opinion" :label="store.state.label.improveOpinion">
          <el-input type="textarea" :rows=4 v-model="formData.opinion"/>
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
  {value: 'reportDate', labelKey: 'date', width: 100,},
  {value: 'userIdFormat', labelKey: 'partyUser', width: 116,},
  {value: 'directLeaderFormat', labelKey: 'directLeader', width: 88,},
  {value: 'accidentDescribe', labelKey: 'accidentImproveDescribe', width: 276, showOverflow: true,},
  {value: 'reasonFormat', labelKey: 'improveReason', width: 276, showOverflow: true,},
  {value: 'solve', labelKey: 'improveSolve', width: 276, showOverflow: true,},
  {value: 'improveDescribe', labelKey: 'improveDescribe', width: 276, showOverflow: true,},
  {value: 'opinion', labelKey: 'improveOpinion', width: 128,},
  {value: 'valid', labelKey: 'valid', width: 100, type: ValueType.Valid},
  {value: 'photoList', labelKey: 'improvePhoto', width: 128, type: ValueType.Image,},
  {value: 'fileList', labelKey: 'improveFile', width: 128, type: ValueType.Attachment,},
  {value: 'improvePhotoList', labelKey: 'improveImprovePhoto', width: 128, type: ValueType.Image,},
  {value: 'improveFileList', labelKey: 'improveImproveFile', width: 128, type: ValueType.Attachment,},
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
  window.open(`/industry/public/improve?improveId=${d.param.improveId}`)
}

const defaultFormData = {
  reportDate: formatDate(new Date(), 'yyyy-MM-dd'),
  userId: user.userId,
  directLeader: '',
  accidentDescribe: '',
  reason: '',
  reasonList: [],
  solve: '',
  improveDescribe: '',
  improveOpinion: '',
  valid: false,
  photoList: [],
  fileList: [],
  improvePhotoList: [],
  improveFileList: [],
}
const state = reactive({
  dateTimeList: [],
  expandRowKeys: [],
  query: {
    data: {
      improveId: route.query.improveId,
      startReportDate: '',
      endReportDate: '',
      userId: '',
      reason: '',
      reasonList: [],
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
    improveReasonList: [],
  },
  userConfigList: new Array<any>(),
  userOptionList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    reportDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    userId: [{required: true, message: 'Please check', trigger: 'blur'}],
    directLeader: [{required: true, message: 'Please check', trigger: 'blur'}],
    accidentDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    // solve: [{required: true, message: 'Please check', trigger: 'blur'}],
    // improveDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    // opinion: [{required: true, message: 'Please check', trigger: 'blur'}],
    reasonList: [{required: false, type: 'array', message: 'Please check', min: 1, max: 999}],
    photoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6}],
    fileList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6}],
    improvePhotoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6}],
    improveFileList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6}],
  },
})
httpGet('douson/config', {
  categoryIdList: [
    'improveReason',
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
  httpGet(`douson/admin/improve/page`, state.query).then(
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
        httpPostJson('douson/admin/improve', state.formData).then(() => {
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
  return httpPutJson('douson/admin/improve', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/improve', {improveId: row.improveId})
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
  formVisible,
  formRuleList,
} = {
  ...toRefs(state),
}

</script>

<style lang="scss">
</style>
