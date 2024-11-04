<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.templateOrderNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.templateOrderNo"
                class="search-item"/>
      <el-select v-model="query.data.borrowTemplatePerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.borrowTemplatePerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.materialDescription"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.materialDescription"
                class="search-item"/>
      <el-select
          v-model="query.data.alreadyReturn"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.isReturn">
        <el-option
            label="Yes"
            :value="true"
        />
        <el-option
            label="No"
            :value="false"
        />
      </el-select>
      <el-select
          v-model="query.data.meetRequirement"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.meetRequirement">
        <el-option
            label="Yes"
            :value="true"
        />
        <el-option
            label="No"
            :value="false"
        />
      </el-select>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          clearable
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start promise return date"
          end-placeholder="End promise return date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
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
        idKey="templateId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="includes(roleCodeList, 'admin') ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handleTableRowClassName="handleTableRowClassName"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
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
        <el-form-item prop="borrowTemplateDate" :label="store.state.label.borrowTemplateDate">
          <el-date-picker
              type="date"
              v-model="formData.borrowTemplateDate"
              format="YYYY-MM-DD"
              @change="formData.borrowTemplateDate = formatDate(formData.borrowTemplateDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="borrowTemplatePerson" :label="store.state.label.borrowTemplatePerson">
          <el-select v-model="formData.borrowTemplatePerson"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.borrowTemplatePerson"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="materialDescription" :label="store.state.label.materialDescription">
          <el-input v-model="formData.materialDescription"/>
        </el-form-item>
        <el-form-item prop="templateCount" :label="store.state.label.templateCount">
          <el-input-number v-model="formData.templateCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="promiseReturnDate" :label="store.state.label.promiseReturnDate">
          <el-date-picker
              type="date"
              v-model="formData.promiseReturnDate"
              format="YYYY-MM-DD"
              @change="formData.promiseReturnDate = formatDate(formData.promiseReturnDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="operatorPerson" :label="store.state.label.operatorPerson">
          <el-select v-model="formData.operatorPerson"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.operatorPerson"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input type="textarea" :rows=4 v-model="formData.description"/>
        </el-form-item>
        <el-form-item prop="returnCount" :label="store.state.label.returnCount">
          <el-input-number v-model="formData.returnCount" style="width: 60px;" :controls="false" :min="0" :disabled="!(user.username === 'admin' || includes(roleCodeList, 'templateManager'))"/>
        </el-form-item>
        <el-form-item prop="actualReturnDate" :label="store.state.label.actualReturnDate">
          <el-date-picker
              type="date"
              v-model="formData.actualReturnDate"
              format="YYYY-MM-DD"
              @change="formData.actualReturnDate = formatDate(formData.actualReturnDate, 'yyyy-MM-dd')"
              :disabled="!(user.username === 'admin' || includes(roleCodeList, 'templateManager'))"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="borrowPhotoList" :label="`${store.state.label.borrowPhoto}(${(formRuleList['borrowPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.borrowPhotoList" :maxSize="Number(`${(formRuleList['borrowPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="returnPhotoList" :label="`${store.state.label.returnPhoto}(${(formRuleList['returnPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.returnPhotoList" :maxSize="Number(`${(formRuleList['returnPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <!--        <el-form-item prop="meetRequirement" :label="store.state.label.meetRequirement">
                  <el-select
                      v-model="formData.meetRequirement"
                      filterable
                      allow-create
                      clearable
                      :placeholder="store.state.label.meetRequirement">
                    <el-option
                        label="Yes"
                        :value="true"
                    />
                    <el-option
                        label="No"
                        :value="false"
                    />
                  </el-select>
                </el-form-item>-->
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
import {StoreType,} from '@/store'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Printer, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from "../../component/ImageUpload.vue";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const showPrint = ref<boolean>(false)
const printData = ref<any>(null)
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 312, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 45},
  {value: 'borrowTemplateDate', labelKey: 'borrowTemplateDate', width: 102},
  {value: 'borrowTemplatePersonFormat', labelKey: 'borrowTemplatePerson', width: 132},
  {value: 'materialNo', labelKey: 'materialNo', width: 145},
  {value: 'materialDescription', labelKey: 'materialDescription', width: 189},
  {value: 'templateCount', labelKey: 'templateCount', width: 49},
  {value: 'promiseReturnDate', labelKey: 'promiseReturnDate', width: 102},
  {value: 'operatorPersonFormat', labelKey: 'operatorPerson', width: 132},
  {value: 'borrowPhotoCount', labelKey: 'borrowPhotoCount', width: 49},
  {value: 'returnPhotoCount', labelKey: 'returnPhotoCount', width: 49},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'returnCount', labelKey: 'returnCount', width: 49},
  {value: 'actualReturnDate', labelKey: 'actualReturnDate', width: 102},
  {value: 'meetRequirementFormat', labelKey: 'meetRequirement', width: 59},
  {value: 'templateOrderNo', labelKey: 'templateOrderNo', width: 123},
  {value: 'borrowPhotoList', labelKey: 'borrowPhoto', width: 189, type: ValueType.Image,},
  {value: 'returnPhotoList', labelKey: 'returnPhoto', width: 189, type: ValueType.Image,},
])
if (user.username === 'admin' || includes(roleCodeList, 'templateManager')) {
  columnConfigList.value = columnConfigList.value.map(t => {
    if ('description' === t.value) {
      t.type = ValueType.TextEdit
    } else if ('returnCount' === t.value) {
      t.width = 161
      t.type = ValueType.NumberEdit
    }
    return t
  })
}
httpGet(`system/user/config/list`, {}).then(
    (res: ListResult<any>) => {
      state.userConfigList = res.list || []
      userOptionList.value = state.userConfigList.map((t: any) => {
        return {
          value: t.userId,
          label: t.name,
        }
      })
    })
const defaultFormData = {
  creator: user.userId,
  borrowTemplateDate: formatDate(new Date(), 'yyyy-MM-dd'),
  borrowTemplatePerson: '',
  materialNo: '',
  materialDescription: '',
  templateCount: '',
  promiseReturnDate: '',
  operatorPerson: '',
  borrowPhotoCount: 0,
  returnPhotoCount: 0,
  description: '',
  returnCount: 0,
  actualReturnDate: '',
  meetRequirement: '',
  templateOrderNo: '',
  borrowPhotoList: new Array<any>(),
  returnPhotoList: new Array<any>(),
}

const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      templateOrderNo: '',
      borrowTemplatePerson: '',
      materialNo: '',
      materialDescription: '',
      alreadyReturn: null,
      meetRequirement: null,
      startPromiseReturnDate: '',
      endPromiseReturnDate: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  config: {},
  formSave: true,
  formVisible: false,
  managerEdit: false,
  formRuleList: {
    borrowTemplateDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    promiseReturnDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    operatorPerson: [{required: true, message: 'Please check', trigger: 'blur'}],
    borrowTemplatePerson: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    templateCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialDescription: [{required: true, message: 'Please check', trigger: 'blur'}],
    borrowPhotoList: [{required: false, type: 'array', min: 0, max: 6}],
    returnPhotoList: [{required: false, type: 'array', min: 0, max: 6}],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startPromiseReturnDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endPromiseReturnDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startPromiseReturnDate = ''
    state.query.data.endPromiseReturnDate = ''
  }
  handlePage()
}
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/template?templateId=${d.param.templateId}`)
}
const handlePage = () => {
  httpGet(`douson/admin/template/page`, state.query).then(
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
const handleEdit = (row: any, managerEdit: boolean = false) => {
  state.managerEdit = managerEdit
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  if (user.username === 'admin' || includes(roleCodeList, 'templateManager') || includes(roleCodeList, 'templateView')) {
    return true
  } else {
    return false
  }
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/template', state.formData).then(() => {
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
  return httpPutJson('douson/admin/template', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/template', {
      templateId: row.templateId,
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
  managerEdit,
  formRuleList,
  photoVisible,
  photoList,
} = {
  ...toRefs(state),
}
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.returnCount >= row.templateCount) {
    return 'row-done'
  } else if (!row.meetRequirement) {
    return 'row-error'
  }
  return ''
}

</script>

<style scoped lang="scss">
</style>
