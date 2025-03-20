<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.checkOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.checkOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.customerShortName"
                @change="handlePage"
                :placeholder="store.state.label.customerShortName"
                class="search-item"/>
      <el-input v-model="query.data.saleOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProjectNo"
                @change="handlePage"
                :placeholder="store.state.label.orderProjectNo"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.improveMaterialDescribe"
                @change="handlePage"
                :placeholder="store.state.label.improveMaterialDescribe"
                class="search-item"/>
      <el-date-picker
          v-model="promiseDoneDateList"
          @change="() => {handleDateTimeChange(promiseDoneDateList, query.data, 'promiseDoneDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.promiseDoneDate}`"
          :end-placeholder="`End ${store.state.label.promiseDoneDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.identificationPerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.identificationPerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-date-picker
          v-model="inspectionCompletedDateList"
          @change="() => {handleDateTimeChange(inspectionCompletedDateList, query.data, 'inspectionCompletedDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.inspectionCompletedDate}`"
          :end-placeholder="`End ${store.state.label.inspectionCompletedDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.inspectionPerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.inspectionPerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-date-picker
          v-model="identificationDateList"
          @change="() => {handleDateTimeChange(identificationDateList, query.data, 'inspectionCompletedDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.inspectionCompletedDate}`"
          :end-placeholder="`End ${store.state.label.inspectionCompletedDate}`"
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
      </div>
    </div>
    <view-list
        idKey="examineId"
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
        <el-form-item prop="identificationList" :label="`${store.state.label.identification}(${(formRuleList['identificationList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :disabled="!(roleIdentificationRecord || roleExamineManager)" :photoList="formData.identificationList" :maxSize="Number(`${(formRuleList['identificationList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="hardnessList" :label="`${store.state.label.hardness}(${(formRuleList['hardnessList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <file-upload :disabled="!(roleHardnessRecord || roleExamineManager)" :fileList="formData.hardnessList" :maxSize="Number(`${(formRuleList['hardnessList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></file-upload>
        </el-form-item>
        <el-form-item prop="identificationHardnessRemark" :label="store.state.label.identificationHardnessRemark">
          <el-input type="textarea" :rows=4 v-model="formData.identificationHardnessRemark" :disabled="!(roleIdentificationRecord || roleHardnessRecord || roleExamineManager)"/>
        </el-form-item>
        <el-form-item prop="ndeList" :label="`${store.state.label.nde}(${(formRuleList['ndeList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :disabled="!(roleNdeRecord || roleExamineManager)" :photoList="formData.ndeList" :maxSize="Number(`${(formRuleList['identificationList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="dimensionList" :label="`${store.state.label.dimension}(${(formRuleList['dimensionList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :disabled="!(roleDimensionRecord || roleExamineManager)" :photoList="formData.dimensionList" :maxSize="Number(`${(formRuleList['identificationList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="ndeDimensionRemark" :label="store.state.label.ndeDimensionRemark">
          <el-input type="textarea" :rows=4 v-model="formData.ndeDimensionRemark" :disabled="!(roleNdeRecord || roleDimensionRecord || roleExamineManager)"/>
        </el-form-item>
        <el-form-item prop="stopHour" :label="store.state.label.inspectionCompletedQuantity">
          <el-input-number
              v-model="formData.inspectionCompletedQuantity"
              :precision="0"
              :controls="false"
              :min="0"
              :disabled="!(roleDimensionRecord || roleExamineManager)"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input type="textarea" :rows=4 v-model="formData.description" :disabled="!(roleIdentificationRecord || roleHardnessRecord || roleNdeRecord || roleDimensionRecord || roleExamineManager)"/>
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
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPutJson,} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_PAGE,} from '@/typing/Common'
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
const defaultFormData = {
  creator: user.userId,
  checkOrderNo: '',
  orderTotalQuantity: 0,
  identification: '',
  hardness: '',
  identificationHardnessRemark: '',
  nde: '',
  dimension: '',
  ndeDimensionRemark: '',
  inspectionCompletedQuantity: 0,
  customerShortName: '',
  saleOrderNo: '',
  orderProjectNo: '',
  materialNo: '',
  improveMaterialDescribe: '',
  designNumber: '',
  orderQuantity: '',
  promiseDoneDate: '',
  description: '',
  identificationPerson: '',
  identificationDate: '',
  inspectionPerson: '',
  inspectionCompletedDate: '',
  identificationList: [],
  hardnessList: [],
  ndeList: [],
}
const defaultColumnConfigList = [
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 215, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'checkOrderNo', labelKey: 'checkOrderNo', width: 112},
  {value: 'orderTotalQuantity', labelKey: 'orderTotalQuantity', width: 76},
  {value: 'identificationCount', labelKey: 'identification', width: 65},
  {value: 'identificationList', labelKey: 'identification', type: ValueType.Image,},
  {value: 'hardnessCount', labelKey: 'hardness', width: 65},
  {value: 'hardnessList', labelKey: 'hardness', type: ValueType.Attachment,},
  {value: 'identificationHardnessRemark', labelKey: 'identificationHardnessRemark', width: 189},
  {value: 'ndeCount', labelKey: 'nde', width: 65},
  {value: 'ndeList', labelKey: 'nde', type: ValueType.Image,},
  {value: 'dimensionCount', labelKey: 'dimension', width: 65},
  {value: 'dimensionList', labelKey: 'dimension', type: ValueType.Image,},
  {value: 'ndeDimensionRemark', labelKey: 'ndeDimensionRemark', width: 189},
  {value: 'inspectionCompletedQuantity', labelKey: 'inspectionCompletedQuantity', width: 65},
  {value: 'customerShortName', labelKey: 'customerShortName', width: 111},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 78},
  {value: 'orderProjectNo', labelKey: 'orderProjectNo', width: 54},
  {value: 'materialNo', labelKey: 'materialNo', width: 100},
  {value: 'improveMaterialDescribe', labelKey: 'improveMaterialDescribe', width: 189},
  {value: 'designNumber', labelKey: 'designNumber', width: 100},
  {value: 'orderQuantity', labelKey: 'orderQuantity', width: 46},
  {value: 'promiseDoneDate', labelKey: 'promiseDoneDate', width: 102},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'identificationPersonFormat', labelKey: 'identificationPerson', width: 189},
  {value: 'identificationDate', labelKey: 'identificationDate', width: 102},
  {value: 'inspectionPersonFormat', labelKey: 'inspectionPerson', width: 189},
  {value: 'inspectionCompletedDate', labelKey: 'inspectionCompletedDate', width: 102},
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

const promiseDoneDateList = ref([])
const identificationDateList = ref([])
const inspectionCompletedDateList = ref([])
const state = reactive({
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      checkOrderNo: '',
      identification: '',
      hardness: '',
      identificationHardnessRemark: '',
      nde: '',
      dimension: '',
      ndeDimensionRemark: '',
      customerShortName: '',
      saleOrderNo: '',
      orderProjectNo: '',
      materialNo: '',
      improveMaterialDescribe: '',
      designNumber: '',
      orderQuantity: '',
      promiseDoneDate: '',
      startPromiseDoneDate: '',
      endPromiseDoneDate: '',
      description: '',
      identificationPerson: '',
      identificationDate: '',
      inspectionPerson: '',
      inspectionCompletedDate: '',
      startInspectionCompletedDate: '',
      endInspectionCompletedDate: '',
      startInspectionCompletedDate: '',
      endInspectionCompletedDate: '',
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
    examineOutOfPlanTypeList: []
  },
  formSave: true,
  formVisible: false,
  formRuleList: {
    identificationList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 10,}],
    hardnessList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 999,}],
    ndeList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6,}],
    dimensionList: [{required: false, type: 'array', message: 'Please check', min: 0, max: 6,}],
  },
})
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
const handlePage = () => {
  httpGet(`douson/examine/page`, state.query).then(
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
        httpPutJson('douson/examine/merge', state.formData).then(() => {
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
  return httpPutJson('douson/examine/merge', row).then(() => {
    ElMessage.success('Update success')
    state.formVisible = false
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/examine', {
      examineId: row.examineId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const roleIdentificationRecord = includes(roleCodeList, 'identificationRecord')
const roleHardnessRecord = includes(roleCodeList, 'hardnessRecord')
const roleNdeRecord = includes(roleCodeList, 'ndeRecord')
const roleDimensionRecord = includes(roleCodeList, 'dimensionRecord')
const roleExamineManager = includes(roleCodeList, 'examineManager')
const roleExamineView = includes(roleCodeList, 'examineView')
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'examineOutOfPlanType',
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
  columnConfigList.value = defaultColumnConfigList.map(t => {
    if ('identificationCount' === t.value) {
      if (roleIdentificationRecord || roleExamineManager) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('identificationList')
          }, 100)
        }
      }
    } else if ('hardnessCount' === t.value) {
      if (roleHardnessRecord || roleExamineManager) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('hardnessList')
          }, 100)
        }
      }
    } else if ('identificationHardnessRemark' === t.value) {
      if (roleIdentificationRecord || roleHardnessRecord || roleExamineManager) {
        t.type = ValueType.TextEdit
      }
    } else if ('ndeCount' === t.value) {
      if (roleNdeRecord || roleExamineManager) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('ndeList')
          }, 100)
        }
      }
    } else if ('dimensionCount' === t.value) {
      if (roleDimensionRecord || roleExamineManager) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('dimensionList')
          }, 100)
        }
      }
    } else if ('ndeDimensionRemark' === t.value) {
      if (roleNdeRecord || roleDimensionRecord || roleExamineManager) {
        t.type = ValueType.TextEdit
      }
    } else if ('inspectionCompletedQuantity' === t.value) {
      if (roleDimensionRecord || roleExamineManager) {
        t.width = 98
        t.type = ValueType.NumberEdit
      }
    } else if ('description' === t.value) {
      if (roleIdentificationRecord || roleHardnessRecord || roleNdeRecord || roleDimensionRecord || roleExamineManager) {
        t.type = ValueType.TextEdit
      }
    }
    return t
  })
  handlePage()
})
const {
  expandRowKeys,
  query,
  tableData,
  config,
  userConfigList,
  total,
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
  if (row.inspectionCompletedQuantity > 0 && row.inspectionCompletedQuantity === row.orderTotalQuantity) {
    return 'row-green'
  }
  return ''
}
</script>

<style scoped lang="scss">
</style>
