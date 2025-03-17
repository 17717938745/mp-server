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
      >
      </el-date-picker>
      <el-select v-model="query.data.department"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.department"
                 class="search-item">
        <el-option
            v-for="item in config.departmentList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.optimizeType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.optimizeType"
                 class="search-item">
        <el-option
            v-for="item in config.optimizeTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.existsProblem"
                @change="handlePage"
                :placeholder="store.state.label.existsProblem"
                class="search-item"/>
      <el-input v-model="query.data.username"
                @change="handlePage"
                :placeholder="store.state.label.username"
                class="search-item"/>
      <el-select v-model="query.data.responsiblePerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.responsiblePerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
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
      <el-input v-model="query.data.serialNo" @keyup.enter="handlePage" :placeholder="store.state.label.templateOrderNo" clearable @change="handlePage" class="search-item"/>
      <el-select v-model="query.data.responsibleTeam"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.responsibleTeam"
                 class="search-item">
        <el-option
            v-for="item in config.responsibleTeamList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <view-list
        idKey="planId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleDelete="'admin' === user.username ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detail-link="false"
        :handleTableCellClassName="handleTableCellClassName"
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
        <el-form-item prop="createDate" :label="store.state.label.createDate">
          <el-date-picker
              type="date"
              v-model="formData.createDate"
              format="YYYY-MM-DD"
              @change="formData.createDate = formatDate(formData.createDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="creator" :label="store.state.label.username">
          <el-input v-model="formData.creator" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="department" :label="store.state.label.department">
          <el-select v-model="formData.department"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.department"
                     class="search-item">
            <el-option
                v-for="item in config.departmentList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="responsibleTeam" :label="store.state.label.responsibleTeam">
          <el-select v-model="formData.responsibleTeam"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.responsibleTeam"
                     class="search-item">
            <el-option
                v-for="item in config.responsibleTeamList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="optimizeType" :label="store.state.label.optimizeType">
          <el-select v-model="formData.optimizeType"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.optimizeType"
                     class="search-item">
            <el-option
                v-for="item in config.optimizeTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="existsProblem" :label="store.state.label.existsProblem">
          <el-input type="textarea" :rows=8 v-model="formData.existsProblem"/>
        </el-form-item>
        <el-form-item prop="solveScheme" :label="store.state.label.solveScheme">
          <el-input type="textarea" :rows=8 v-model="formData.solveScheme"/>
        </el-form-item>
        <el-form-item prop="responsiblePersonList" :label="store.state.label.responsiblePerson">
          <el-select v-model="formData.responsiblePersonList"
                     filterable
                     allow-create
                     multiple
                     clearable
                     :placeholder="store.state.label.responsiblePerson"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="planCompleteTime" :label="store.state.label.planCompleteTime">
          <el-date-picker
              type="date"
              v-model="formData.planCompleteTime"
              format="YYYY-MM-DD"
              @change="formData.planCompleteTime = formatDate(formData.planCompleteTime, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="beforePhotoList" :label="`${store.state.label.beforePlanThreePhoto}(${(formRuleList['beforePhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.beforePhotoList" :maxSize="Number(`${(formRuleList['beforePhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="afterPhotoList" :label="`${store.state.label.afterPlanThreePhoto}(${(formRuleList['afterPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.afterPhotoList" :maxSize="Number(`${(formRuleList['afterPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="attachmentList" :label="`${store.state.label.supportAttachment}(${(formRuleList['attachmentList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :fileList="formData.attachmentList" :maxSize="Number(`${(formRuleList['attachmentList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
        </el-form-item>
        <el-form-item prop="awardAmount" :label="store.state.label.awardAmount">
          <el-input-number :min="0" v-model="formData.awardAmount"/>
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
import {StoreType} from '@/store/Index'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Search, Printer,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'
import FileUpload from '../../component/FileUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
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
  {value: 'createDate', label: store.state.label.createDate, labelKey: 'createDate', width: 102},
  {value: 'creatorFormat', label: store.state.label.username, labelKey: 'username', width: 110},
  {value: 'departmentFormat', label: store.state.label.department, labelKey: 'department', width: 154},
  {value: 'responsibleTeamFormat', label: store.state.label.responsibleTeam, labelKey: 'responsibleTeam', width: 154},
  {value: 'optimizeTypeFormat', label: store.state.label.optimizeType, labelKey: 'optimizeType', width: 128},
  {value: 'existsProblem', label: store.state.label.existsProblem, labelKey: 'existsProblem', width: 334, type: ValueType.Text, showOverflow: true,},
  {value: 'solveScheme', label: store.state.label.solveScheme, labelKey: 'solveScheme', width: 334, type: ValueType.Text, showOverflow: true,},
  {value: 'responsiblePersonFormat', originValue: 'responsiblePersonList', label: store.state.label.responsiblePerson, labelKey: 'responsiblePerson', width: 128},
  {value: 'planCompleteTime', label: store.state.label.planCompleteTime, labelKey: 'planCompleteTime', width: 102},
  {value: 'awardAmountFormat', label: store.state.label.awardAmount, labelKey: 'awardAmount', width: 87},
  {value: 'valid', label: store.state.label.valid, labelKey: 'valid', width: 68, type: ValueType.Valid,},
  {value: 'serialNo', labelKey: 'templateOrderNo', width: 100,},
  {value: 'beforePhotoList', label: store.state.label.photoList, labelKey: 'beforePlanThreePhoto', width: 269, type: ValueType.Image,},
  {value: 'afterPhotoList', label: store.state.label.photoList, labelKey: 'afterPlanThreePhoto', width: 269, type: ValueType.Image,},
  {value: 'attachmentList', label: store.state.label.photoList, labelKey: 'supportAttachment', width: 269, type: ValueType.Attachment,},
])
const defaultFormData = {
  planId: '',
  creator: store.state.user.userId,
  createDate: formatDate(new Date(), 'yyyy-MM-dd'),
  department: '',
  optimizeType: '',
  existsProblem: '',
  solveScheme: '',
  responsibleTeam: '',
  responsiblePersonList: [store.state.user.userId],
  planCompleteTime: '',
  beforePhotoList: [],
  afterPhotoList: [],
  attachmentList: [],
  awardAmount: 0,
  valid: '',
  photoList: new Array<any>(),
}
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      startCreateDate: '',
      endCreateDate: '',
      department: '',
      responsibleTeam: '',
      optimizeType: '',
      existsProblem: '',
      username: '',
      serialNo: '',
      responsiblePerson: '',
      valid: false,
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
    departmentList: [],
    optimizeTypeList: [],
    responsibleTeamList: [],
  },
  userConfigList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    creator: [{required: true, message: 'Please check', trigger: 'blur'}],
    createDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    department: [{required: true, message: 'Please check', trigger: 'blur'}],
    optimizeType: [{required: true, message: 'Please check', trigger: 'blur'}],
    existsProblem: [{required: true, message: 'Please check', trigger: 'blur'}],
    solveScheme: [{required: true, message: 'Please check', trigger: 'blur'}],
    planCompleteTime: [{required: true, message: 'Please check', trigger: 'blur'}],
    beforePhotoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 5}],
    responsiblePersonList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 5}],
    afterPhotoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 5}],
    attachmentList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 4}],
  },
})
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startCreateDate = formatDate(
        state.dateTimeList[0],
        "yyyy-MM-dd hh:mm:ss"
    );
    state.query.data.endCreateDate = formatDate(
        state.dateTimeList[1],
        "yyyy-MM-dd hh:mm:ss"
    );
  } else {
    state.query.data.startCreateDate = ''
    state.query.data.endCreateDate = ''
  }
  handlePage()
}
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/plan?planId=${d.param.planId}`)
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
    'customerShortName',
    'department',
    'optimizeType',
    'responsibleTeam',
  ]
}).then(r => {
  state.config = r.data
})
const handlePage = () => {
  httpGet(`douson/admin/plan/page`, state.query).then(
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
const handleTableCellClassName = ({
                                    row,
                                    column,
                                    rowIndex,
                                    columnIndex,
                                  }: {
  row: any
  rowIndex: number
}) => {
  if (serialNoIndex >= 0 && serialNoIndex === columnIndex) {
    return 'row-red'
  }
  return ''
}
let serialNoIndex = -1
httpGet(`system/user/config/list`, {}).then(
    (res: ListResult<any>) => {
      state.userConfigList = res.list || []
      userOptionList.value = state.userConfigList.map((t: any) => {
        return {
          value: t.userId,
          label: t.name,
        }
      })
      columnConfigList.value = columnConfigList.value.map((t: any, i) => {
        if ('responsiblePersonFormat' === t.value) {
          t.type = ValueType.SelectEdit
          t.width = 234;
          t.optionList = userOptionList.value
        }
        if (t.value === 'serialNo') {
          serialNoIndex = i
        }
        return t
      })
      handlePage()
    })
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
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/plan', state.formData).then(() => {
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
  return httpPutJson('douson/admin/plan', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/plan', {
      planId: row.planId,
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
  userConfigList,
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
