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
      <el-select v-model="query.data.dutyPerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.dutyPerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.qualityDealOpinion"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.qualityDealOpinion"
                 class="search-item">
        <el-option
            v-for="item in config.qualityDealOpinionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.skillDealOpinion"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.skillDealOpinion">
        <el-option
            v-for="item in config.skillDealOpinionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.orderNo"
                @change="handlePage"
                :placeholder="store.state.label.orderNo"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-select v-model="query.data.defectType"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.defectType">
        <el-option
            v-for="item in config.defectTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.checkPoint"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.checkPoint">
        <el-option
            v-for="item in config.checkPointList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.disqualificationOrder"
                @change="handlePage"
                :placeholder="store.state.label.disqualificationOrder"
                class="search-item"/>
      <el-input v-model="query.data.disqualificationContent"
                @change="handlePage"
                :placeholder="store.state.label.disqualificationContent"
                class="search-item"/>
      <el-select v-model="query.data.process"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.process"
                 class="search-item">
        <el-option
            v-for="item in config.processList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select
          v-model="query.data.userProperty"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.userProperty">
        <el-option
            v-for="item in config.userPropertyList"
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
        idKey="disqualificationOrderId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleEditShow="handleEditShow"
        :handleUpdate="handleUpdate"
        :handleDelete="includes(roleCodeList, 'qualityManager') ? handleDelete : null"
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
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="operator">
          <div style="display: flex; flex-direction: row-reverse !important; width: 100%;">
            <el-button :icon="Refresh" @click="formData = Object.assign({}, defaultFormData, {disqualificationOrderNo: formData.disqualificationOrderNo})" type="warning">Reset</el-button>
          </div>
        </el-form-item>
        <el-form-item prop="disqualificationOrderNo" :label="store.state.label.disqualificationOrderNo">
          <el-input v-model="formData.disqualificationOrderNo" :disabled="true">
            <template #prepend>NCR-{{ formatDate(new Date(), 'yyyy-MM-dd') }}</template>
          </el-input>
        </el-form-item>
        <el-form-item prop="orderNo" :label="store.state.label.orderNo">
          <el-input v-model="formData.orderNo" @change="formData.orderNo = (formData.orderNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="projectSequence" :label="store.state.label.projectSequence">
          <el-input v-model="formData.projectSequence"/>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber" @change="formData.designNumber = (formData.designNumber || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="process" :label="store.state.label.process">
          <el-select v-model="formData.process"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.process">
            <el-option
                v-for="item in config.processList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="checkPoint" :label="store.state.label.checkPoint">
          <el-select v-model="formData.checkPoint"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.checkPoint">
            <el-option
                v-for="item in config.checkPointList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="stoveNo" :label="store.state.label.stoveNo">
          <el-input v-model="formData.stoveNo"/>
        </el-form-item>
        <el-form-item prop="hotBatchNo" :label="store.state.label.hotBatchNo">
          <el-input v-model="formData.hotBatchNo"/>
        </el-form-item>
        <el-form-item prop="serialNo" :label="store.state.label.serialNo">
          <el-input v-model="formData.serialNo"/>
        </el-form-item>
        <el-form-item prop="disqualificationContent" :label="store.state.label.disqualificationContent">
          <el-input v-model="formData.disqualificationContent"/>
        </el-form-item>
        <el-form-item prop="count" :label="store.state.label.count">
          <el-input-number :min="0" v-model="formData.count"/>
        </el-form-item>
        <el-form-item prop="defectType" :label="store.state.label.defectType">
          <el-select v-model="formData.defectType"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.defectType">
            <el-option
                v-for="item in config.defectTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="dutyPersonList" :label="store.state.label.dutyPerson">
          <el-select v-model="formData.dutyPersonList"
                     filterable
                     multiple
                     clearable
                     :placeholder="store.state.label.dutyPerson">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.photo}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item v-if="includes(roleCodeList, 'qualityManager')" prop="qualityDealOpinion" :label="store.state.label.qualityDealOpinion">
          <el-select v-model="formData.qualityDealOpinion"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.qualityDealOpinion">
            <el-option
                v-for="item in config.qualityDealOpinionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="includes(roleCodeList, 'technologyManager')" prop="skillDealOpinion" :label="store.state.label.skillDealOpinion">
          <el-select v-model="formData.skillDealOpinion"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.skillDealOpinion">
            <el-option
                v-for="item in config.skillDealOpinionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="includes(roleCodeList, 'manager')" prop="fineAmount" :label="store.state.label.fineAmount">
          <el-input-number :min="0" v-model="formData.fineAmount"/>
        </el-form-item>
        <el-form-item v-if="includes(roleCodeList, 'manager')" prop="remark" :label="store.state.label.description">
          <el-input v-model="formData.remark"/>
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
import {Plus, Refresh, Search, Printer,} from '@element-plus/icons-vue'
import {useRouter, useRoute,} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {DataResult, ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'

const router = useRouter()
const route = useRoute()
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
    width: 219,
    type: ValueType.Operator,
  },
  {
    value: 'productInfo', labelKey: 'productInfo', children: [
      {value: 'disqualificationOrderNoFormat', labelKey: 'disqualificationOrderNo', width: 125},
      {value: 'creatorFormat', labelKey: 'inspector', width: 96},
      {value: 'orderNo', labelKey: 'orderNo', width: 80},
      {value: 'projectSequence', labelKey: 'projectSequence', width: 50},
      {value: 'materialNo', labelKey: 'materialNo', width: 163, align: 'left',},
      {value: 'designNumber', labelKey: 'designNumber', width: 153, align: 'left',},
      {value: 'processFormat', labelKey: 'process', width: 106},
      {value: 'checkPointFormat', labelKey: 'checkPoint', width: 106},
      {value: 'stoveNo', labelKey: 'stoveNo', width: 109},
      {value: 'hotBatchNo', labelKey: 'hotBatchNo', width: 109},
    ]
  },
  {
    value: 'disqualificationInfo', labelKey: 'disqualificationInfo', children: [
      {value: 'serialNo', labelKey: 'serialNo', width: 138},
      {value: 'disqualificationContent', labelKey: 'disqualificationContent', width: 276, type: ValueType.Text},
      {value: 'count', labelKey: 'count', width: 67},
      {value: 'defectTypeFormat', labelKey: 'defectType', width: 128},
      {value: 'dutyPersonFormat', labelKey: 'dutyPerson', width: 128},
      {value: 'photoList', label: store.state.label.photoList, labelKey: 'photo', width: 269, type: ValueType.Image,},
    ]
  },
  {
    value: 'dealOpinion', labelKey: 'dealOpinion', children: [
      {
        value: 'qualityDealOpinionFormat', originValue: 'qualityDealOpinion', labelKey: 'qualityDealOpinion', width: 86,
      },
      {
        value: 'skillDealOpinionFormat', originValue: 'skillDealOpinion', labelKey: 'skillDealOpinion', width: 86,
      },
      {
        value: 'fineAmountFormat', originValue: 'fineAmount', labelKey: 'fineAmount', width: 87,
      },
      {
        value: 'remark', labelKey: 'description', width: 256,
      },
    ]
  },
])
const defaultFormData = {
  disqualificationOrderId: '',
  creator: user.userId,
  disqualificationOrder: '',
  orderNo: '',
  projectSequence: '',
  materialNo: '',
  designNumber: '',
  process: '',
  disqualificationContent: '',
  count: 0,
  checkPoint: '',
  dutyPersonList: [],
  qualityDealOpinion: '',
  skillDealOpinion: '',
  fineAmount: null,
  remark: '',
  stoveNo: '',
  hotBatchNo: '',
  serialNo: '',
  defectType: '',
  photoList: new Array<any>(),
}

const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      disqualificationOrderId: String(route.query.disqualificationOrderId || ''),
      orderNo: '',
      materialNo: '',
      disqualificationContent: '',
      designNumber: '',
      disqualificationOrderNo: '',
      disqualificationOrder: '',
      dutyPerson: '',
      checkPoint: '',
      defectType: '',
      qualityDealOpinion: '',
      startCreateTime: '',
      endCreateTime: '',
      skillDealOpinion: '',
      userProperty: '',
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
    processList: [],
    checkPointList: [],
    qualityDealOpinionList: [],
    skillDealOpinionList: [],
    defectTypeList: [],
    userPropertyList: [],
  },
  userConfigList: new Array<any>(),
  formVisible: false,
  formRuleList: {
    disqualificationOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    projectSequence: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    designNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    process: [{required: true, message: 'Please check', trigger: 'blur'}],
    disqualificationContent: [{required: true, message: 'Please check', trigger: 'blur'}],
    count: [{required: true, message: 'Please check', trigger: 'blur'}],
    checkPoint: [{required: true, message: 'Please check', trigger: 'blur'}],
    dutyPersonList: [{required: true, type: 'array', min: 1, max: 99999}],
    serialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    defectType: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', min: 1, max: 4}],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startCreateTime = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endCreateTime = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startCreateTime = ''
    state.query.data.endCreateTime = ''
  }
  handlePage()
}
const handleShowPrintDetail = (d: any) => {
  window.open(`/industry/public/disqualification?disqualificationOrderId=${d.param.disqualificationOrderId}`)
}
const handleAutoInsertSerialNo = (t: any, i: number, arr: any[]) => {
  if (i === 0) {
    let n
    try {
      n = Number(t)
    } catch (e) {
      n = 1
    }
    for (let j = 1; j < arr.length; j++) {
      arr[j] = n + j
    }
  }
}
httpGet('douson/config',{
  categoryIdList: [
    'processProcedure',
    'testDevice',
    'customerShortName',
    'process',
    'checkPoint',
    'qualityDealOpinion',
    'skillDealOpinion',
    'defectType',
    'userProperty'
  ]
}).then(r => {
  state.config = r.data
  if (includes(roleCodeList, 'qualityManager') || includes(roleCodeList, 'technologyManager') || includes(roleCodeList, 'manager')) {
    columnConfigList.value = columnConfigList.value.map(t => {
      if ('dealOpinion' === t.value) {
        t.children = (t.children || []).map(t1 => {
          if ((includes(roleCodeList, 'qualityManager')) && 'qualityDealOpinionFormat' === t1.value) {
            t1.type = ValueType.SelectEdit
            t1.optionList = state.config.qualityDealOpinionList
          }
          if ((includes(roleCodeList, 'technologyManager')) && 'skillDealOpinionFormat' === t1.value) {
            t1.type = ValueType.SelectEdit
            t1.optionList = state.config.skillDealOpinionList
          }
          if ((includes(roleCodeList, 'manager')) && 'fineAmountFormat' === t1.value) {
            t1.type = ValueType.NumberEdit
          }
          if ((includes(roleCodeList, 'manager')) && 'remark' === t1.value) {
            t1.type = ValueType.TextEdit
          }
          return t1
        })
      }
      return t
    })
  }
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
    })
const handlePage = () => {
  httpGet(`douson/admin/disqualification-order/page`, state.query).then(
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
  httpGet(`douson/admin/disqualification-order/form`, {}).then(
      (res: DataResult<any>) => {
        const d = res.data || {}
        state.formData = Object.assign({}, defaultFormData, d)
        state.formVisible = true
        state.formSave = true
      }
  )
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  if (includes(roleCodeList, 'admin')) {
    return true
  } else {
    return row.creator === store.state.user.userId
  }
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/disqualification-order', state.formData).then(() => {
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
  return httpPutJson('douson/admin/disqualification-order', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/disqualification-order', {
      disqualificationOrderId: row.disqualificationOrderId,
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
