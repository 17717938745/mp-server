<template>
  <div>
    <div class="query-container">
      <el-date-picker
          ref="yearPicker"
          v-model="selectedYear"
          type="year"
          placeholder="Select Year"
          @change="handleYearChange"
          :clearable="false"
      />
      <el-select ref="quarterSelect" v-model="selectedQuarter" placeholder="Select Quarter" @change="handleQuarterChange">
        <el-option
            v-for="(quarter, index) in quarters"
            :key="index"
            :label="quarter.label"
            :value="quarter.value"
        />
      </el-select>
      <el-select v-model="query.data.userId"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.chineseName"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.evaluationResult" @change="handleList" :placeholder="store.state.label.evaluationResult"/>
      <el-input v-model="query.data.description" @change="handleList" :placeholder="store.state.label.description"/>
      <el-input v-model="query.data.leaderUserId" @change="handleList" :placeholder="store.state.label.leaderUserId"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
    <div class="douson-flex-item" style="align-items: center;  gap: 16px;">
      <el-button v-if="roleManager" :icon="UploadFilled" @click="handleDownloadTemplate" type="primary">Download template</el-button>
      <el-upload
          v-if="roleManager"
          action="#"
          :disabled="!selectedYear || !selectedQuarter"
          :show-file-list="false"
          :on-change="handleFileChange"
          :before-upload="handleBeforeUpload"
          :http-request="handleRequest"
          accept=".xlsx,.xls"
          multiple
          :drag="true"
      >
        <div>
          <el-button v-if="roleManager" :icon="UploadFilled" @click="handleList" type="danger">Upload</el-button>
        </div>
      </el-upload>
      <el-button v-if="roleManager" round :icon="EditPen" @click="editRuleVisible = true">
        Edit rule
      </el-button>
      <el-link v-if="tableData.length > 0"
               type="danger"
               class="link"
               @click="handleShowRule">
        Show rule
      </el-link>
    </div>
    <el-dialog :title="rule.title" v-model="ruleVisible" width="80%" :close-on-click-modal="false">
      <div v-if="rule" v-html="rule.content" class="douson-h5"></div>
    </el-dialog>
    <div>
      <el-alert v-if="afterUpload"
                :title="`上传总记录数：${uploadData.totalCount}, 失败数：${uploadData.errorCount}；未匹配：${uploadData.notMatchUserCount}：${uploadData.notMatchUserList.join('、')}, 更新：${uploadData.updateUserCount}, 新增：${uploadData.insertUserCount}`"
                :type="uploadData.errorCount > 0 ? 'error' : 'success'"/>
    </div>
    <view-list
        idKey="scoreId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="(row) => roleManager&& row.scoreId"
        :handleDelete=" handleDelete"
        :handleDeleteShow="(row) => roleManager&& row.scoreId"
        :page="query.page"
        :handleTableRowClassName="handleTableRowClassName"
    >
      <template #operator="row">
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
        <el-form-item prop="deviceNumber" :label="store.state.label.deviceNumber">
          <el-input v-model="formData.deviceNumber"/>
        </el-form-item>
        <el-form-item prop="qualityScore" :label="store.state.label.qualityScore">
          <el-input-number v-model="formData.qualityScore" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="attendanceScore" :label="store.state.label.attendanceScore">
          <el-input-number v-model="formData.attendanceScore" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="safetyScore" :label="store.state.label.safetyScore">
          <el-input-number v-model="formData.safetyScore" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="monthlyPerformance" :label="store.state.label.monthlyPerformance">
          <el-input-number v-model="formData.monthlyPerformance" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="totalWorkDays" :label="store.state.label.totalWorkDays">
          <el-input-number v-model="formData.totalWorkDays" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="evaluationMonths" :label="store.state.label.evaluationMonths">
          <el-input-number v-model="formData.evaluationMonths" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="evaluationResult" :label="store.state.label.evaluationResult">
          <el-input v-model="formData.evaluationResult"/>
        </el-form-item>
        <el-form-item prop="quarterlyBonus" :label="store.state.label.quarterlyBonus">
          <el-input-number v-model="formData.quarterlyBonus" style="width: 160px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input type="textarea" :rows=4 v-model="formData.description"/>
        </el-form-item>
        <el-form-item prop="leaderUserId" :label="store.state.label.leaderUserId">
          <el-input v-model="formData.leaderUserId"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.photo}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-affix position="bottom" :offset="20">
        <span class="dialog-footer">
          <el-button @click="formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleMerge">Confirm</el-button>
        </span>
        </el-affix>
      </template>
    </el-dialog>
    <el-drawer
        v-model="editRuleVisible"
        size="90%"
        :append-to-body="true"
        :lock-scroll="false"
        modal-class="print-drawer"
        :z-index="1"
    >
      <tinymce :forum-id="'score'" :category="'score'" :after-success="() => {editRuleVisible = false; }"></tinymce>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editRuleVisible = false">Cancel</el-button>
        </span>
      </template>
    </el-drawer>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {EditPen, Printer, Refresh, Search, UploadFilled,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpDownloadFile, httpGet, httpPostJson, httpPutJson, httpUpload} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate, getQuarterStartMonthString} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../../component/ImageUpload.vue'
import Tinymce from "../../../component/Tinymce.vue";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const roleManager = includes(roleCodeList, 'scoreManager')
const editRuleVisible = ref(false)
const ruleVisible = ref(false)
const rule = ref({})
const handleShowRule = () => {
  httpGet(`forum`, {
    forumId: 'score'
  }).then(
      (res: DataResult<any>) => {
        rule.value = res.data
        ruleVisible.value = true
        ElMessage.success("Query success")
      }
  )
}
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 312, type: ValueType.Operator,},
  {value: 'employeeId', labelKey: 'employeeId', width: 189},
  {value: 'userName', labelKey: 'userName', width: 189},
  {value: 'quarter', labelKey: 'quarter', width: 189},
  {value: 'deviceNumber', labelKey: 'deviceNumber', width: 189},
  {value: 'qualityScoreFormat', labelKey: 'qualityScore', width: 189},
  {value: 'attendanceScoreFormat', labelKey: 'attendanceScore', width: 189},
  {value: 'safetyScoreFormat', labelKey: 'safetyScore', width: 189},
  {value: 'monthlyPerformanceFormat', labelKey: 'monthlyPerformance', width: 189},
  {value: 'totalWorkDays', labelKey: 'totalWorkDays', width: 189},
  {value: 'totalFormat', labelKey: 'total', width: 189},
  {value: 'evaluationMonths', labelKey: 'evaluationMonths', width: 189},
  {value: 'evaluationResult', labelKey: 'evaluationResult', width: 189},
  {value: 'quarterlyBonusFormat', labelKey: 'quarterlyBonus', width: 189},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'leaderUserId', labelKey: 'leaderUserId', width: 189},
  {value: 'photoCount', labelKey: 'photos', width: 189},
  {value: 'photoList', labelKey: 'photos', width: 128, type: ValueType.Image,},
])
const yearPicker = ref(null)
const quarterSelect = ref(null)
const selectedQuarter = ref(getQuarterStartMonthString())
const selectedYear = ref(formatDate(new Date(), 'yyyy'))
const quarters = [
  {label: "Q1 (Jan - Mar)", value: "Q1"},
  {label: "Q2 (Apr - Jun)", value: "Q2"},
  {label: "Q3 (Jul - Sep)", value: "Q3"},
  {label: "Q4 (Oct - Dec)", value: "Q4"},
]
const handleQuarterChange = (value) => {
  handleList()
}
const handleYearChange = (value) => {
  handleList()
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
  return formatDate(selectedYear.value, 'yyyy') + selectedQuarter.value
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
    const quarter = getYearQuarter()
    if (quarter) {
      Promise.all(keys.map((k: any) => {
        const t = fileMap[k]
        const formData = new FormData()
        formData.set("filename", t.name)
        formData.set("file", t)
        formData.set("quarter", quarter)
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
}
const handleEditable = (row) => {
  return row.scoreId
}

const defaultFormData = {
  employeeId: '',
  userId: '',
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
      userId: '',
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
    deviceNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    qualityScore: [{required: true, message: 'Please check', trigger: 'blur'}],
    attendanceScore: [{required: true, message: 'Please check', trigger: 'blur'}],
    safetyScore: [{required: true, message: 'Please check', trigger: 'blur'}],
    monthlyPerformance: [{required: true, message: 'Please check', trigger: 'blur'}],
    totalWorkDays: [{required: true, message: 'Please check', trigger: 'blur'}],
    evaluationMonths: [{required: true, message: 'Please check', trigger: 'blur'}],
    evaluationResult: [{required: true, message: 'Please check', trigger: 'blur'}],
    quarterlyBonus: [{required: true, message: 'Please check', trigger: 'blur'}],
    description: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', min: 0, max: 50}],
  },
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
const userOptionList = ref([])
Promise.all([
  () => {
    return {
      data: {}
    }
  },
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  state.config = l[0].data
  userOptionList.value = (l[1].list || []).map((t: any) => {
    return {
      value: t.userId,
      label: t.name,
    }
  })
  if (roleManager) {
    columnConfigList.value = columnConfigList.value.map((t: any) => {
      if (t.value === 'description') {
        t.type = ValueType.Link
        t.editable = handleEditable
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('description')
          }, 100)
        }
      } else if (t.value === 'photoCount') {
        t.type = ValueType.Link
        t.editable = handleEditable
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('photoList')
          }, 100)
        }
      }
      return t
    })
  }
  handleList()
})
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}

const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPutJson('douson/score/merge', state.formData).then(() => {
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
  return httpPutJson('douson/score/merge', row).then(() => {
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
      scoreId: row.scoreId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handleList()
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
