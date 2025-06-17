<template>
  <div>
    <div class="query-container">
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
      <el-select v-model="query.data.vocationType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.vocationType"
                 class="search-item">
        <el-option
            v-for="item in config.vocationTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.user"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.vocationUser"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-date-picker
          type="date"
          v-model="query.data.date"
          format="YYYY-MM-DD"
          :placeholder="store.state.label.vocationDate"
          @change="query.data.date = formatDate(query.data.date, 'yyyy-MM-dd'); handlePage();"
      >
      </el-date-picker>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start due date"
          end-placeholder="End due date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.compliance"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.vocationCompliance"
      >
        <el-option
            v-for="item in [{value: true, label: 'YES'}, {value: false, label: 'NO'}]"
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
    <div>
      <span style="font-size: 26px; font-weight: bold;">{{ store.state.label.vocationDays }}:</span>
      <span style="font-weight: bold;">{{ summaryData.vocationDaysFormat }}</span>
    </div>
    <view-list
        idKey="vocationId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleDelete="handleDelete"
        :handleDeleteShow="() => (includes(roleCodeList, 'admin') || includes(roleCodeList, 'vocationManager'))"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
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
        <el-form-item prop="vocationType" :label="store.state.label.vocationType">
          <el-select v-model="formData.vocationType"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.vocationType"
          >
            <el-option
                v-for="item in config.vocationTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="date" :label="store.state.label.vocationDate">
          <el-date-picker
              type="date"
              v-model="formData.date"
              format="YYYY-MM-DD"
              @change="formData.date = formatDate(formData.date, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="user" :label="store.state.label.vocationUser">
          <el-select v-model="formData.user"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.vocationUser"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="chargeUser" :label="store.state.label.vocationChargeUser">
          <el-select v-model="formData.chargeUser"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.vocationChargeUser"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="dateTimeList" :label="store.state.label.vocationDate">
          <el-date-picker
              v-model="formData.dateTimeList"
              type="daterange"
              format="YYYY-MM-DD"
              range-separator="-"
              start-placeholder="Start date"
              end-placeholder="End date"
              style="width: 180px; margin-right: 20px;"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="reason" :label="store.state.label.vocationReason">
          <el-input type="textarea" :rows=4 v-model="formData.reason"/>
        </el-form-item>
        <el-form-item prop="count" :label="store.state.label.vocationDays">
          <el-input-number v-model="formData.count" :min="0.1" :controls="false" :precision="1"/>
        </el-form-item>
        <el-form-item prop="compliance" :label="store.state.label.vocationCompliance">
          <el-select v-model="formData.compliance"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.vocationCompliance"
          >
            <el-option
                v-for="item in [{value: true, label: 'YES'}, {value: false, label: 'NO'}]"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="violationReason" :label="store.state.label.violationReason">
          <el-input type="textarea" :rows=4 v-model="formData.violationReason"/>
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
import {Plus, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'

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
    width: 137,
    type: ValueType.Operator,
  },
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'vocationTypeFormat', labelKey: 'vocationType', width: 178},
  {value: 'date', labelKey: 'vocationDate', width: 102},
  {value: 'userFormat', originValue: 'user', labelKey: 'vocationUser', width: 189},
  {value: 'chargeUserFormat', labelKey: 'vocationChargeUser', width: 189},
  {value: 'departmentFormat', labelKey: 'department', width: 168},
  {value: 'professionFormat', labelKey: 'profession', width: 232},
  {value: 'startDate', labelKey: 'vocationStartDate', width: 102},
  {value: 'endDate', labelKey: 'vocationEndDate', width: 102},
  {value: 'reason', labelKey: 'vocationReason', width: 254, showOverflow: true},
  {value: 'count', labelKey: 'vocationDays', width: 96},
  {value: 'complianceFormat', labelKey: 'vocationCompliance', width: 76},
  {value: 'violationReason', labelKey: 'violationReason', width: 254, showOverflow: true},
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
const defaultFormData = {
  vocationType: '',
  date: formatDate(new Date(), 'yyyy-MM-dd'),
  user: '',
  chargeUser: '',
  startDate: '',
  endDate: '',
  reason: '',
  dateTimeList: [],
  count: 0,
  compliance: true,
  violationReason: '',
}

const state = reactive({
  dateTimeList: [],
  userConfigList: new Array<any>(),
  query: {
    data: {
      department: '',
      vocationType: '',
      user: '',
      date: '',
      startDate: '',
      endDate: '',
      compliance: null,
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  summaryData: {},
  total: 0,
  formData: Object.assign({}, defaultFormData),
  formSave: false,
  config: {
    vocationTypeList: [],
    departmentList: [],
  },
  formVisible: false,
  managerEdit: false,
  formRuleList: {
    vocationType: [{required: true, message: 'Please check', trigger: 'blur'}],
    date: [{required: true, message: 'Please check', trigger: 'blur'}],
    user: [{required: true, message: 'Please check', trigger: 'blur'}],
    chargeUser: [{required: true, message: 'Please check', trigger: 'blur'}],
    dateTimeList: [{required: true, type: 'array', message: 'Please check', min: 2, max: 2,}],
    reason: [{required: true, message: 'Please check', trigger: 'blur'}],
    count: [{required: true, message: 'Please check', trigger: 'blur'}],
    compliance: [{required: true, message: 'Please check', trigger: 'blur'}],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd'
    );
    state.query.data.endDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd'
    );
  } else {
    state.query.data.startDate = ''
    state.query.data.endDate = ''
  }
  handlePage()
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'vocationType',
      'department',
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
const handlePage = () => {
  httpGet(`douson/admin/vocation/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = res.list
        state.total = res.total
        state.summaryData = res.data || {}
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
const handleEdit = (row: any, managerEdit: boolean = false) => {
  state.managerEdit = managerEdit
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
  state.formData.dateTimeList = [
    state.formData.startDate,
    state.formData.endDate
  ]
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (!formData.value.compliance && !formData.value.violationReason) {
        ElMessage.error(`${store.state.label.violationReason} is required`)
        return
      }
      if (state.formData.dateTimeList && state.formData.dateTimeList.length > 1) {
        state.formData.startDate = formatDate(
            state.formData.dateTimeList[0],
            'yyyy-MM-dd'
        );
        state.formData.endDate = formatDate(
            state.formData.dateTimeList[1],
            'yyyy-MM-dd'
        );
      } else {
        state.formData.startDate = ''
        state.formData.endDate = ''
      }
      if (state.formSave) {
        httpPostJson('douson/admin/vocation', state.formData).then(() => {
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
  return httpPutJson('douson/admin/vocation', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/vocation', {
      vocationId: row.vocationId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
const {
  query,
  tableData,
  summaryData,
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
};

</script>

<style scoped lang="scss">
</style>
