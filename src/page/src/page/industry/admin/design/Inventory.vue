<template>
  <div>
    <div class="query-container">
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
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-input v-model="query.data.type"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.type"
                class="search-item"/>
      <el-input v-model="query.data.materialDate"
                @change="handlePage"
                clearable
                :placeholder="store.state.label.materialDate"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          clearable
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start material date"
          end-placeholder="End material date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.inventoryCountType"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.inventoryCount"
      >
        <el-option
            v-for="item in [{value: 0, label: `${store.state.label.inventoryCount}!=${store.state.label.materialCount}`,}, ]"
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
      </div>
    </div>
    <view-list
        idKey="inventoryId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleDelete="handleDelete"
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
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="materialDescription" :label="store.state.label.materialDescription">
          <el-input v-model="formData.materialDescription"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber"/>
        </el-form-item>
        <el-form-item prop="inventoryCount" :label="store.state.label.inventoryCount">
          <el-input-number v-model="formData.inventoryCount" style="width: 60px;" :controls="false" :min="0" :max="999999"/>
        </el-form-item>
        <el-form-item prop="inventoryDate" :label="store.state.label.inventoryDate">
          <el-date-picker
              type="date"
              v-model="formData.inventoryDate"
              format="YYYY-MM-DD"
              @change="formData.inventoryDate = formatDate(formData.inventoryDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="type" :label="store.state.label.type">
          <el-select v-model="formData.type"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.type"
          >
            <el-option
                v-for="item in config.inventoryOutOfPlanTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input v-model="formData.description"/>
        </el-form-item>
        <el-form-item prop="materialCount" :label="store.state.label.materialCount">
          <el-input-number v-model="formData.materialCount" style="width: 60px;" :controls="false" :min="0" :max="999999" :disabled="!formSave && !editMore"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.photoDescribe}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :disabled="!formSave && !editMore" :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="attachmentList" :label="`${store.state.label.attachment}(${(formRuleList['fileList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :disabled="!formSave && !editMore" :fileList="formData.attachmentList" :maxSize="Number(`${(formRuleList['fileList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
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
import {StoreType,} from '@/store/Index'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Printer, Search, UploadFilled, DocumentCopy, ArrowUp, ArrowDown,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload,} from '@/util/HttpUtil'
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
const defaultColumnConfigList = [
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 158, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 50},
  {value: 'materialNo', labelKey: 'materialNo', width: 163},
  {value: 'materialDescription', labelKey: 'materialDescription', width: 210},
  {value: 'designNumber', labelKey: 'designNumber', width: 147},
  {value: 'inventoryCount', labelKey: 'inventoryCount', width: 56},
  {value: 'inventoryDate', labelKey: 'inventoryDate', width: 102},
  {value: 'typeFormat', labelKey: 'type', width: 89},
  {value: 'description', labelKey: 'description', width: 189,},
  {value: 'materialCount', labelKey: 'materialCount', width: 76,},
  {value: 'materialDate', labelKey: 'materialDate', width: 102},
  {value: 'photoCount', labelKey: 'photoCount', width: 56},
  {value: 'photoList', labelKey: 'photoDescribe', width: 189, type: ValueType.Image,},
  {value: 'attachmentList', labelKey: 'attachment', width: 189, type: ValueType.Attachment,},
]
const columnConfigList = ref<ViewConfig[]>(defaultColumnConfigList.map(t => t))
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
  materialNo: '',
  materialDescription: '',
  designNumber: '',
  inventoryCount: null,
  inventoryDate: '',
  type: '',
  description: '',
  materialCount: null,
  materialDate: '',
  photoList: [],
  attachmentList: [],
}
const state = reactive({
  dateTimeList: [],
  dateTimeListSupplier: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      materialNo: '',
      materialDescription: '',
      designNumber: '',
      inventoryCount: '',
      inventoryDate: '',
      type: '',
      description: '',
      materialCount: '',
      materialDate: '',
      startMaterialDate: '',
      endMaterialDate: '',
      inventoryCountType: 0,
    },
    page: {
      page: DEFAULT_PAGE,
      limit: 120,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  config: {
    inventoryOutOfPlanTypeList: []
  },
  formSave: true,
  formVisible: false,
  formRuleList: {
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialDescription: [{required: true, message: 'Please check', trigger: 'blur'}],
    designNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    inventoryCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    inventoryDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    type: [{required: true, message: 'Please check', trigger: 'blur'}],
    description: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 3,}],
    attachmentList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 3,}],
  },
})
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startMaterialDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd'
    );
    state.query.data.endMaterialDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd'
    );
  } else {
    state.query.data.startMaterialDate = ''
    state.query.data.endMaterialDate = ''
  }
  handlePage()
}
const handlePage = () => {
  httpGet(`douson/inventory/page`, state.query).then(
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
        httpPutJson('douson/inventory/merge', state.formData).then(() => {
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
  return httpPutJson('douson/inventory/merge', row).then(() => {
    ElMessage.success('Update success')
    state.formVisible = false
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/inventory', {
      inventoryId: row.inventoryId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const editMore = includes(roleCodeList, 'inventoryManager')
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'inventoryOutOfPlanType',
    ]
  }),
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  state.config = l[0].data || {}
  userOptionList.value = (l[1].list || []).map((t: any) => {
    return {
      value: t.userId,
      label: t.name,
    }
  })

  if (editMore) {
    columnConfigList.value = defaultColumnConfigList.map(t => {
      if ('materialCount' === t.value) {
        t.type = ValueType.NumberEdit
        t.width = 95
      } else if ('description' === t.value) {
        t.type = ValueType.TextEdit
      }
      return t
    })
  }
  handlePage()
})
const {
  expandRowKeys,
  query,
  tableData,
  config,
  userConfigList,
  total,
  dateTimeList,
  dateTimeListSupplier,
  formData,
  formSave,
  formVisible,
  formRuleList,
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
  if (row.materialCount && row.materialCount === row.inventoryCount) {
    return 'row-done'
  }
  return ''
}
</script>

<style scoped lang="scss">
</style>
