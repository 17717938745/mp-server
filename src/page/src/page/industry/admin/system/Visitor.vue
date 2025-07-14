<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.visitorId"
                @change="handlePage"
                :placeholder="store.state.label.visitor"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="() => {handleDateTimeChange(dateTimeList, query.data, 'applyDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.applyDate}`"
          :end-placeholder="`End ${store.state.label.applyDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.applicant"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.applicant"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.visitorName"
                @change="handlePage"
                :placeholder="store.state.label.visitorName"
                class="search-item"/>
      <el-input v-model="query.data.companyName"
                @change="handlePage"
                :placeholder="store.state.label.companyName"
                class="search-item"/>
      <el-input v-model="query.data.visitContent"
                @change="handlePage"
                :placeholder="store.state.label.visitContent"
                class="search-item"/>
      <el-date-picker
          v-model="expectedVisitTimeList"
          @change="() => {handleDateTimeChange(expectedVisitTimeList, query.data, 'expectedVisitTime')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.expectedVisitTime}`"
          :end-placeholder="`End ${store.state.label.expectedVisitTime}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-date-picker
          v-model="expectedEndTimeList"
          @change="() => {handleDateTimeChange(expectedEndTimeList, query.data, 'expectedEndTime')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.expectedEndTime}`"
          :end-placeholder="`End ${store.state.label.expectedEndTime}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="formData.contactPerson"
                 filterable
                 @change="handlePage"
                 clearable
                 :placeholder="store.state.label.contactPerson"
      >
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="formData.visitDepartment"
                 filterable
                 @change="handlePage"
                 clearable
                 :placeholder="store.state.label.visitDepartment"
      >
        <el-option
            v-for="item in config.departmentList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-date-picker
          v-model="dateTimeList"
          @change="() => {handleDateTimeChange(dateTimeList, query.data, 'visitorFactoryDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.visitorFactoryDate}`"
          :end-placeholder="`End ${store.state.label.visitorFactoryDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.printNumber"
                @change="handlePage"
                :placeholder="store.state.label.printNumber"
                class="search-item"/>
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
        idKey="visitorId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="visitorManager ? handleDelete : null"
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
        <el-form-item prop="applyDate" :label="store.state.label.applyDate">
          <el-input v-model="formData.applyDate" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="applicant" :label="store.state.label.applicant">
          <el-select v-model="formData.applicant"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.applicant"
                     :disabled="true"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="visitorName" :label="store.state.label.visitorName">
          <el-input v-model="formData.visitorName" :disabled="!handleEditable(formData, 'visitorName')"/>
        </el-form-item>
        <el-form-item prop="phoneNumber" :label="store.state.label.phoneNumber">
          <el-input v-model="formData.phoneNumber" :disabled="!handleEditable(formData, 'phoneNumber')"/>
        </el-form-item>
        <el-form-item prop="companyName" :label="store.state.label.companyName">
          <el-input v-model="formData.companyName" :disabled="!handleEditable(formData, 'companyName')"/>
        </el-form-item>
        <el-form-item prop="visitContent" :label="store.state.label.visitContent">
          <el-select v-model="formData.visitContent"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.visitContent"
                     :disabled="!handleEditable(formData, 'visitContent')"
          >
            <el-option
                v-for="item in config.visitContentList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="expectedVisitTime" :label="store.state.label.expectedVisitTime">
          <el-date-picker
              type="datetime"
              v-model="formData.expectedVisitTime"
              format="YYYY-MM-DD HH:mm"
              @change="formData.applyDate = formatDate(formData.applyDate, 'yyyy-MM-dd HH:mm')"
              :disabled="!handleEditable(formData, 'expectedVisitTime')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="expectedEndTime" :label="store.state.label.expectedEndTime">
          <el-date-picker
              type="datetime"
              v-model="formData.expectedEndTime"
              format="YYYY-MM-DD HH:mm"
              @change="formData.applyDate = formatDate(formData.applyDate, 'yyyy-MM-dd HH:mm')"
              :disabled="!handleEditable(formData, 'expectedEndTime')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="contactPerson" :label="store.state.label.contactPerson">
          <el-select v-model="formData.contactPerson"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.contactPerson"
                     :disabled="!handleEditable(formData, 'contactPerson')"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!formSave" prop="valid" :label="store.state.label.approve">
          <el-checkbox v-model="formData.valid" name="valid" :disabled="!visitorManager">
            Approve
          </el-checkbox>
        </el-form-item>
        <el-form-item prop="idAndPhotosList" :label="`${store.state.label.idAndPhotos}(${(formRuleList['factoryRecordsList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.idAndPhotosList" :maxSize="Number(`${(formRuleList['factoryRecordsList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!handleEditable(formData, 'factoryRecordsList')"></image-upload>
        </el-form-item>
        <el-form-item prop="remarks" :label="store.state.label.remarks">
          <el-input v-model="formData.remarks" :disabled="!handleEditable(formData, 'remarks')"/>
        </el-form-item>
        <el-form-item prop="factoryRecordsList" :label="`${store.state.label.factoryRecords}(${(formRuleList['factoryRecordsList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.factoryRecordsList" :maxSize="Number(`${(formRuleList['factoryRecordsList'] || []).reduce((p:any, t:any) => t.max, 999)}`)" :disabled="!handleEditable(formData, 'factoryRecordsList')"></image-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="formVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleMerge">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
    <el-drawer
        v-model="showPrint"
        size="100%"
        :lock-scroll="false"
        modal-class="print-drawer"
        :z-index="99999"
    >
      <div id="printDescription" class="douson-flex douson-flex-item-center">
        <div style="width: 585px;">
          <div>
            <h1 class="douson-flex-item-center">DOUSON访客单</h1>
          </div>
          <div class="douson-flex" style="justify-content: flex-end;">{{ store.state.label.printNumber }}：{{printData.printNumber}}</div>
          <div class="douson-container" style="font-size: 14px; margin-top: 10px;">
            <div class="douson-flex douson-row">
              <div class="douson-flex-item-column-center douson-column" style="width: 33%">
                <div>{{ store.state.label.applyDate }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 34%;">
                <div>{{ store.state.label.applicant }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ store.state.label.expectedVisitTime }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ store.state.label.expectedEndTime }}</div>
              </div>
            </div>
            <div class="douson-flex douson-row">
              <div class="douson-flex-item-column-center douson-column" style="width: 33%">
                <div>{{ printData.applyDate }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 34%;">
                <div>{{ printData.applicantFormat }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ printData.expectedVisitTime }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ printData.expectedEndTime }}</div>
              </div>
            </div>
            <div class="douson-flex douson-row">
              <div class="douson-flex-item-column-center douson-column" style="width: 33%">
                <div>{{ store.state.label.visitorName }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 34%;">
                <div>{{ store.state.label.companyName }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ store.state.label.contactPerson }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ store.state.label.visitDepartment }}</div>
              </div>
            </div>
            <div class="douson-flex douson-row">
              <div class="douson-flex-item-column-center douson-column" style="width: 33%">
                <div>{{ printData.visitorName }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 34%;">
                <div>{{ printData.companyName }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ printData.contactPersonFormat }}</div>
              </div>
              <div class="douson-flex-item-column-center douson-column" style="width: 33%;">
                <div>{{ printData.visitDepartmentFormat }}</div>
              </div>
            </div>
            <div class="douson-flex douson-row" style="min-height: 130px;">
              <div class="douson-flex-item douson-column" style="width: 30%; justify-content: center;">
                <div class="douson-flex-item-center">
                  {{ store.state.label.visitContent }}
                </div>
              </div>
              <div class="douson-flex-item douson-column" style="width: 70%;">
                <div class="douson-flex-item-center">
                  <div>{{ printData.visitContent }}</div>
                </div>
                <div>
                </div>
              </div>
            </div>
            <div class="douson-flex" style="justify-content: flex-end; min-height: 80px;">{{ store.state.label.approval }}：<span style="width: 230px;"></span></div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Printer, Search,} from '@element-plus/icons-vue'
import {useRouter, useRoute} from 'vue-router'
import {httpDelete, httpGet, httpPutJson} from '@/util/HttpUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'
import FileUpload from "../../component/FileUpload.vue";
import {fullUrl} from "@/util/EnvUtil";

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const visitorView = includes(roleCodeList, 'visitorView')
const visitorManager = includes(roleCodeList, 'visitorManager')
const visitorSecurity = includes(roleCodeList, 'visitorSecurity')
const securityEdit = visitorManager || visitorSecurity
const formRef: Ref = ref(null)
const printData = ref<any>(null)
const showPrint = ref<boolean>(false)
const userOptionList = ref(new Array<any>())
const handleEditable = (row, key)  => {
  return visitorManager || (!row.valid && (includes(['idAndPhotos', 'remarks', 'factoryRecords'], key) ? visitorSecurity : user.userId === row.applicant))
}
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 167, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 56},
  {value: 'applyDate', originValue: 'applyDate', labelKey: 'applyDate', width: 98},
  {value: 'applicantFormat', originValue: 'applicant', labelKey: 'applicant', width: 82},
  {value: 'visitorName', labelKey: 'visitorName', width: 101},
  {value: 'phoneNumber', originValue: 'phoneNumber', labelKey: 'phoneNumber', width: 102, showOverflow: true,},
  {value: 'companyName', originValue: 'companyName', labelKey: 'companyName', width: 189, showOverflow: true,},
  {value: 'visitContentFormat', originValue: 'visitContent', labelKey: 'visitContent', width: 87},
  {value: 'expectedVisitTime', labelKey: 'expectedVisitTime', width: 87},
  {value: 'expectedEndTime', labelKey: 'expectedEndTime', width: 87},
  {value: 'contactPersonFormat', originValue: 'contactPerson', labelKey: 'contactPerson', width: 87},
  {value: 'visitDepartmentFormat', originValue: 'visitDepartment', labelKey: 'visitDepartment', width: 131},
  {value: 'approverFormat', labelKey: 'approver', width: 98},
  {value: 'valid', labelKey: 'valid', width: 87},
  {value: 'idAndPhotos', labelKey: 'idAndPhotos', width: 103, },
  {value: 'idAndPhotosList', labelKey: 'idAndPhotos', width: 189, type: ValueType.Image,},
  {value: 'remarks', labelKey: 'remarks', width: 189,},
  {value: 'factoryRecords', labelKey: 'factoryRecords', width: 93, },
  {value: 'factoryRecordsList', labelKey: 'factoryRecords', width: 189, type: ValueType.Image,},
  {value: 'visitorFactoryDate', labelKey: 'visitorFactoryDate', width: 121, },
  {value: 'printNumber', labelKey: 'printNumber', width: 132, },
])
const defaultFormData = {
  creator: user.userId,
  applyDate: '',
  applicant: '',
  applicant: '',
  visitorName: '',
  phoneNumber: '',
  companyName: '',
  visitContent: '',
  expectedVisitTime: '',
  expectedEndTime: '',
  contactPerson: '',
  visitDepartment: '',
  valid: null,
  idAndPhotosList: new Array<any>(),
  remarks: null,
  factoryRecordsList: new Array<any>(),
  visitorFactoryDate: '',
  printNumber: '',
}
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
const handleShowPrintDetail = (d: any) => {
  // ElMessage.info('该功能正在开发中')
  printData.value = Object.assign({}, d.param || {})
  showPrint.value = true
}
const expectedVisitTimeList = ref([])
const expectedEndTimeList = ref([])
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      visitorId: route.query.visitorId || '',
      applyDate: '',
      startApplyDate: '',
      endApplyDate: '',
      applicant: '',
      visitorName: '',
      companyName: '',
      expectedVisitTime: '',
      startExpectedVisitTime: '',
      endExpectedVisitTime: '',
      expectedEndTime: '',
      startExpectedEndTime: '',
      endExpectedEndTime: '',
      contactPerson: '',
      visitDepartment: '',
      printNumber: '',
      visitorFactoryDate: '',
      startVisitorFactoryDate: '',
      endVisitorFactoryDate: '',

      remark: '',
      designNumber: '',
      applyCount: null,
      storePosition: '',
      checkAccept: '',
      descriptionOfOrder: '',
      storeCount: null,
      storeDateDescription: '',
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
    visitContentList: [],
    departmentList: [],
  },
  formVisible: false,
  formRuleList: {
    applyDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    applicant: [{required: true, message: 'Please check', trigger: 'blur'}],
    visitorName: [{required: true, message: 'Please check', trigger: 'blur'}],
    phoneNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    companyName: [{required: true, message: 'Please check', trigger: 'blur'}],
    visitContent: [{required: true, message: 'Please check', trigger: 'blur'}],
    expectedVisitTime: [{required: true, message: 'Please check', trigger: 'blur'}],
    expectedEndTime: [{required: true, message: 'Please check', trigger: 'blur'}],
    contactPerson: [{required: true, message: 'Please check', trigger: 'blur'}],
    visitDepartment: [{required: true, message: 'Please check', trigger: 'blur'}],
    idAndPhotosList: [{required: /*visitorSecurity*/false, type: 'array', min: 0, max: 5}],
    remarks: [{required: visitorSecurity, message: 'Please check', trigger: 'blur'}],
    factoryRecordsList: [{required: /*visitorSecurity*/false, type: 'array', min: 0, max: 5}],
  },
})
const handlePageChange = (val: number) => {
  state.query.page.page = val
  handlePage()
}
const handleLimitChange = (val: number) => {
  state.query.page.limit = val
  handlePage()
}
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.applyCount !== row.storeCount) {
    return 'row-yellow'
  }
  return ''
}

const handlePage = () => {
  httpGet(`douson/visitor/page`, state.query).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = res.list
        state.total = res.total
        ElMessage.success("Query success")
      }
  )
}
const handleSaveModal = () => {
  state.formData = Object.assign({}, defaultFormData, {
    applyDate: formatDate(new Date(), 'yyyy-MM-dd'),
    applicant: user.userId,
  })
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  return visitorManager || (!row.valid && (visitorSecurity || row.applicant === user.userId))
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'visitContent',
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
  columnConfigList.value = columnConfigList.value.map(t => {
    if (t.value === 'remarks' && securityEdit) {
      t.type = ValueType.TextEdit
    } else if ('idAndPhotos' === t.value && securityEdit) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('idAndPhotosList')
          }, 100)
        }
    } else if ('factoryRecords' === t.value && securityEdit) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('factoryRecordsList')
          }, 100)
        }
    }
    return t
  })
  handlePage()
  if (includes(roleCodeList, 'gauger')) {
    columnConfigList.value = columnConfigList.value.map((t: any) => {
      if (t.value === 'borrowerFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      }
      return t
    })
  }
  handlePage()
})
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPutJson('douson/visitor/merge', state.formData).then(() => {
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
  return httpPutJson('douson/visitor/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Edit success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/visitor', {
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
.device-expire {
  background-color: #eeee00 !important;
}

.device-scrap {
  background-color: #aaaaaa !important;
}
</style>
