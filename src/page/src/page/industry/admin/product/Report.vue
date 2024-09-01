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

      <el-select v-model="query.data.userId"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.user"
      >
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.orderNo" @keyup.enter="handleList" :placeholder="store.state.label.orderNo" class="search-item"/>
      <el-input v-model="query.data.projectSequence" @keyup.enter="handleList" :placeholder="store.state.label.projectSequence" class="search-item"/>
      <el-input v-model="query.data.designNumber" @keyup.enter="handleList" :placeholder="store.state.label.designNumber" class="search-item"/>
      <el-select v-model="query.data.testDevice"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.testDevice"
                 class="search-item">
        <el-option
            v-for="item in config.testDeviceList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.processType"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.processType"
                 class="search-item">
        <el-option
            v-for="item in config.processTypeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.processProcedure"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.processProcedure"
                 class="search-item">
        <el-option
            v-for="item in config.processProcedureList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <div class="query-btn">
        <el-button v-if="route.query.productId" :icon="Tools" @click="handleStart" type="warning">Start</el-button>
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
      </div>
    </div>
    <div>
      <el-space wrap>
        <el-switch v-model="showDetail" active-text="Show order、product info" inactive-text="Hide order、product info" @change="handleToggleDetail"/>
        <el-switch v-model="showRemark" active-text="Show remark info" inactive-text="Hide remark info" @change="handleToggleRemark"/>
      </el-space>
    </div>
    <div>
      <span style="font-size: 26px; font-weight: bold;">{{ store.state.label.salary }}:</span>
      <span style="font-weight: bold;">{{ summaryData.totalSalaryFormat }}</span>
    </div>
    <view-list
        idKey="reportId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleUpdate="handleUpdate"
        :handleEdit="handleEdit"
        :handleDelete="handleDelete"
    >
      <template #expand="row">
        <!--        {{ JSON.stringify(row.param.reportId) }}-->
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
        <el-form-item prop="showFormDetail" label="Operator">
          <el-switch v-model="showFormDetail" active-text="Show detail" inactive-text="Hide detail" @change="handleToggleDetail"/>
        </el-form-item>
        <el-form-item prop="reportDate" :label="store.state.label.date">
          <el-date-picker
              type="date"
              v-model="formData.reportDate"
              format="YYYY-MM-DD"
              @change="formData.reportDate = formatDate(formData.reportDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="userId" :label="store.state.label.username">
          <el-input v-model="formData.username" :disabled="true"/>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="testDevice" :label="store.state.label.deviceNumber">
          <el-select v-model="formData.testDevice" clearable placeholder="请选择" :disabled="true">
            <el-option
                v-for="item in config.testDeviceList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="orderNo" :label="store.state.label.orderNo">
          <el-input v-model="formData.orderNo"/>
        </el-form-item>
        <el-form-item prop="projectSequence" :label="store.state.label.projectSequence">
          <el-input v-model="formData.projectSequence"/>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber" :disabled="true"/>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="orderCount" :label="store.state.label.orderCount">
          <el-input
              type="number"
              v-model="formData.orderCount"
              :disabled="true"
          />
        </el-form-item>
        <el-form-item prop="processType" :label="store.state.label.processType">
          <el-select v-model="formData.processType" clearable placeholder="请选择">
            <el-option
                v-for="item in config.processTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="processProcedure" :label="store.state.label.processProcedure">
          <el-select v-model="formData.processProcedure" clearable placeholder="请选择" :disabled="true">
            <el-option
                v-for="item in config.processProcedureList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="clampingMinute" :label="store.state.label.clampingMinute">
          <el-input type="number" v-model="formData.clampingMinute" :disabled="true"/>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="assistMinute" :label="store.state.label.assistMinute">
          <el-input type="number" v-model="formData.assistMinute" :disabled="true"/>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="runningMinute" :label="store.state.label.runningMinute">
          <el-input type="number" v-model="formData.runningMinute" :disabled="true"/>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="productStandMinute" :label="store.state.label.productStandMinute">
          <el-input type="number" v-model="formData.productStandMinute" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="schedule" :label="store.state.label.schedule">
          <el-select v-model="formData.schedule" clearable placeholder="请选择">
            <el-option
                v-for="item in config.scheduleList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="workMinute" :label="store.state.label.workMinute">
          <el-select v-model="formData.workMinute"
                     filterable
                     :allow-create="includes(roleCodeList, 'admin')"
                     clearable
                     placeholder="请选择"
                     @change="formData.shouldCompleteCount = Math.round(10 * formData.workMinute / Math.max((formData.clampingMinute + formData.assistMinute + formData.runningMinute), 1)) / 10">
            <el-option
                v-for="item in config.workMinuteList"
                :key="item.value"
                :label="`${item.label}（${item.value}）`"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="shouldCompleteCount" :label="store.state.label.shouldCompleteCount">
          <el-input type="number" v-model="formData.shouldCompleteCount" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="actualCompleteCount" :label="store.state.label.actualCompleteCount">
          <el-input-number v-model="formData.actualCompleteCount" style="width: 60px;" :controls="false" :min="0" @change="handleInitSerialNoList"/>
        </el-form-item>
        <el-form-item prop="serialNoList" :label="store.state.label.serialNoList">
          <el-button type="primary" :icon="CirclePlus" @click="formData.serialNoList.push('')">
            Add
          </el-button>
          <el-input type="number" v-for="(t, i) in formData.serialNoList"
                    :key="`serialNo-${i}`"
                    v-model="formData.serialNoList[i]"
                    @blur="handleAutoInsertSerialNo(t, i, formData.serialNoList)"
                    style="width: 100%; margin-top: 10px;">
            <template #prepend>
              {{ `${formData.orderNo}-${formData.projectSequence}-` }}
            </template>
            <template v-if="i > 0" #append>
              <el-button :icon="Remove" @click="formData.serialNoList.splice(i, 1)"/>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item v-if="showFormDetail" prop="completeMinute" :label="store.state.label.completeMinute">
          <el-input type="number" v-model="formData.completeMinute" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="leaderSubsidyMinute" :label="store.state.label.leaderSubsidyMinute">
          <el-input-number v-model="formData.leaderSubsidyMinute" style="width: 60px;" :controls="false" :min="0" :disabled="!includes(roleCodeList, 'admin')"/>
        </el-form-item>
        <el-form-item prop="deviceRunningTime" :label="store.state.label.deviceRunningTime">
          <el-input-number v-model="formData.deviceRunningStartHour" style="width: 100px;" :disabled="formSave" :controls="false"/>
          :
          <el-input-number v-model="formData.deviceRunningStartMinute" style="width: 60px;" :disabled="formSave" :controls="false"/>
          <el-icon style="margin: 0 20px;">
            <Minus/>
          </el-icon>
          <el-input-number v-model="formData.deviceRunningEndHour" style="width: 100px;" :min="formData.deviceRunningStartMinute >= 59 ? formData.deviceRunningStartHour + 1 : formData.deviceRunningStartHour" :controls="false"/>
          :
          <el-input-number v-model="formData.deviceRunningEndMinute" style="width: 60px;" :controls="false" :min="formData.deviceRunningEndHour > formData.deviceRunningStartHour ?  0 : formData.deviceRunningStartMinute" :max="59"/>
        </el-form-item>
        <el-form-item prop="remark" :label="store.state.label.remark">
          <el-input v-model="formData.remark"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="store.state.label.photo">
          <image-upload :photoList="formData.photoList"></image-upload>
        </el-form-item>
        <el-form-item prop="stopWorkingContent1" :label="store.state.label.stopWorkingContent1">
          <el-select v-model="formData.stopWorkingContent1"
                     filterable
                     allow-create
                     clearable
                     placeholder="请选择">
            <el-option
                v-for="item in config.stopWorkingContent1List"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="stopWorkingMinute1" :label="store.state.label.stopWorkingMinute1">
          <el-input type="number" v-model="formData.stopWorkingMinute1"/>
        </el-form-item>
        <el-form-item prop="stopWorkingContent2" :label="store.state.label.stopWorkingContent2">
          <el-select v-model="formData.stopWorkingContent2"
                     filterable
                     allow-create
                     clearable
                     placeholder="请选择">
            <el-option
                v-for="item in config.stopWorkingContent2List"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="stopWorkingMinute2" :label="store.state.label.stopWorkingMinute2">
          <el-input type="number" v-model="formData.stopWorkingMinute2"/>
        </el-form-item>
        <el-form-item prop="stopWorkingContent3" :label="store.state.label.stopWorkingContent3">
          <el-select v-model="formData.stopWorkingContent3"
                     filterable
                     allow-create
                     clearable
                     placeholder="请选择">
            <el-option
                v-for="item in config.stopWorkingContent3List"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="stopWorkingMinute3" :label="store.state.label.stopWorkingMinute3">
          <el-input type="number" v-model="formData.stopWorkingMinute3"/>
        </el-form-item>
        <el-form-item prop="improveSuggestion" :label="store.state.label.improveSuggestion">
          <el-input v-model="formData.improveSuggestion"/>
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
    <el-dialog title="查看照片" v-model="photoVisible" width="60%">
      <!--<img v-for="temp in photoList" :key="temp.photoUrl :key="temp.photoUrl" :src="fullUrl(temp.photoUrl, '')" style="width: 100%;" alt="picture"/>-->
      <el-image
          v-if="photoList.length > 0"
          v-for="t in photoList"
          :src="fullUrl(t.photoCompressUrl, '')"
          fit="cover"
          :preview-src-list="[fullUrl(t.photoUrl, '')]"
          :initial-index="0"
          class="image-view"
      />
      <el-empty v-else description="Empty"/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="photoVisible = false">Cancel</el-button>
          <el-button type="primary" @click="photoVisible = false">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/industry'
import type {TableColumnCtx} from 'element-plus'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {CircleClose, CirclePlus, Minus, Remove, Search, Tools, UploadFilled,} from '@element-plus/icons-vue'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload} from '@/util/HttpUtil'
import {includes} from '@/util/ArrayUtil'
import {PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {useRoute, useRouter} from 'vue-router'
import {fullUrl} from '@/util/EnvUtil'
import {getStorage, setStorage} from '@/util/StorageUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import ImageUpload from '../../component/ImageUpload.vue'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const user = store.state.user
const formRef: Ref = ref(null)
const showDetail: Ref = ref(false)
const showRemark: Ref = ref(false)
const showFormDetail: Ref = ref(false)
const startDate = new Date(new Date().getTime() - 3600 * 1000 * 24 * 3)
const endDate = new Date()
const state = reactive({
  dateTimeList: [startDate, endDate],
  photoVisible: false,
  photoList: new Array<any>(),
  query: {
    data: {
      startReportDate: formatDate(startDate, 'yyyy-MM-dd'),
      endReportDate: formatDate(endDate, 'yyyy-MM-dd'),
      reportId: '',
      userId: '',
      username: '',
      orderNo: '',
      projectSequence: '',
      designNumber: '',
      productId: '',
      testDevice: '',
      processType: '',
      processProcedure: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  summaryData: {},
  tableData: new Array<any>(),
  config: {
    processProcedureList: [],
    testDeviceList: [],
    processTypeList: [],
    scheduleList: [],
    workMinuteList: [],
    stopWorkingContent1List: [],
    stopWorkingContent2List: [],
    stopWorkingContent3List: [],
  },
  formData: {
    reportId: '',
    reportDate: formatDate(new Date(), 'yyyy-MM-dd'),
    userId: user.userId,
    username: user.username,
    orderCount: 0,
    processType: null,
    schedule: null,
    workMinute: null,
    shouldCompleteCount: null,
    actualCompleteCount: 0,
    serialNoList: new Array<string>(),
    completeMinute: 0,
    leaderSubsidyMinute: 0,
    deviceRunningStartHour: 0,
    deviceRunningStartMinute: 0,
    deviceRunningEndHour: 0,
    productStandMinute: 0,
    deviceRunningEndMinute: 0,
    clampingMinute: 0,
    assistMinute: 0,
    runningMinute: 0,
    remark: '',
    orderNo: '',
    testDevice: '',
    projectSequence: '',
    designNumber: '',
    photoList: new Array<any>(),
    stopWorkingContent1: '',
    stopWorkingMinute1: 0,
    stopWorkingContent2: '',
    stopWorkingMinute2: 0,
    stopWorkingContent3: '',
    stopWorkingMinute3: 0,
    improveSuggestion: '',
    valid: false,
  },
  formSave: false,
  formVisible: false,
  activeReport: null,
  formRuleList: {
    reportDate: [{required: true, message: '请输入日期', trigger: 'blur'}],
    userId: [{required: true, message: '必输', trigger: 'blur'}],
    testDevice: [{required: true, message: '必输', trigger: 'blur'}],
    orderNo: [{required: true, message: '必输', trigger: 'blur'}],
    processType: [{required: true, message: '必输', trigger: 'blur'}],
    projectSequence: [{required: true, message: '必输', trigger: 'blur'}],
    designNumber: [{required: true, message: '必输', trigger: 'blur'}],
    processProcedure: [{required: true, message: '必输', trigger: 'blur'}],
    clampingMinute: [{required: true, message: '必输', trigger: 'blur'}],
    assistMinute: [{required: true, message: '必输', trigger: 'blur'}],
    runningMinute: [{required: true, message: '必输', trigger: 'blur'}],
    productStandMinute: [{required: true, message: '必输', trigger: 'blur'}],
    schedule: [{required: true, message: '必输', trigger: 'blur'}],
    workMinute: [{required: true, message: '必输', trigger: 'blur'}],
    serialNoList: [{required: true, type: 'array', message: '必输', min: 1}],
    photoList: [{required: true, type: 'array', message: '必输', min: 1, max: 4}],
  },
})

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

const handleStart = () => {
  if (route.query.productId) {
    httpGet('douson/admin/report', {
      productId: route.query.productId,
      reportDate: formatDate(new Date(), 'yyyy-MM-dd')
    }).then(r => {
      state.formData = Object.assign({}, state.formData, r.data || {})
      state.formVisible = true
      state.formSave = !r.data.reportId
    })
  }
}

const userOptionList = ref(new Array<any>())
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'processProcedure',
      'testDevice',
      'processType',
      'schedule',
      'workMinute',
      'stopWorkingContent1',
      'stopWorkingContent2',
      'stopWorkingContent3',
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

const handleToggleDetail = () => {
  console.log('handleToggleDetail')
  const keyArr = [
    'clampingMinute',
    'assistMinute',
    'runningMinute',
    'scheduleFormat',
    'serialNoList',
  ]
  columnConfigList.value = columnConfigList.value.map(t => {
    if (keyArr.indexOf(t.value) >= 0) {
      t.hide = !showDetail.value
    }
    return t
  })
}
const handleToggleRemark = () => {
  console.log('handleToggleRemark')
  const keyArr = [
    'stopWorkingContent1Format',
    'stopWorkingMinute1',
    'stopWorkingContent2Format',
    'stopWorkingMinute2',
    'stopWorkingContent3Format',
    'stopWorkingMinute3',
  ]
  columnConfigList.value = columnConfigList.value.map(t => {
    if (keyArr.indexOf(t.value) >= 0) {
      t.hide = !showRemark.value
    }
    return t
  })
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
    width: 239,
    type: ValueType.Operator,
  },
  {
    mergeKey: ['reportDate', 'userId'],
    value: 'reportDate',
    labelKey: 'date',
    showOverflow: true,
    width: 100,
  },
  {
    mergeKey: ['reportDate', 'userId'],
    value: 'userFormat',
    labelKey: 'userFormat',
    width: 100,
    align: 'center',
  },
  {
    mergeKey: ['reportDate', 'userId', 'testDevice'],
    value: 'testDeviceFormat',
    labelKey: 'deviceNumber',
    width: 134,
    showOverflow: true,
    align: 'center',
  },
  {
    value: 'orderNo',
    labelKey: 'orderNo',
    showOverflow: true,
    width: 96,
    align: 'center',
    type: ValueType.OrderLink
  },
  {
    value: 'projectSequence',
    labelKey: 'projectSequence',
    showOverflow: true,
    width: 80,
    align: 'center',
  },
  {
    value: 'designNumber',
    labelKey: 'designNumber',
    width: 160,
    showOverflow: true,
  },
  {
    value: 'orderCount',
    labelKey: 'orderCount',
    width: 64,
    align: 'center',
    type: ValueType.NumberPositive,
  },
  {
    value: 'processTypeFormat',
    labelKey: 'processType',
    width: 96,
    align: 'center',
  },
  {
    value: 'processProcedureFormat',
    labelKey: 'processProcedure',
    showOverflow: true,
  },
  {
    value: 'clampingMinute',
    labelKey: 'clampingMinute',
    width: 64,
    align: 'center',
  },
  {
    value: 'assistMinute',
    labelKey: 'assistMinute',
    width: 64,
    align: 'center',
  },
  {
    value: 'runningMinute',
    labelKey: 'runningMinute',
    width: 64,
    align: 'center',
  },
  {
    value: 'productStandMinute',
    labelKey: 'productStandMinute',
    width: 64,
    align: 'center',
  },
  {
    value: 'scheduleFormat',
    labelKey: 'schedule',
  },
  {
    mergeKey: ['reportDate', 'userId', 'testDevice'],
    value: 'workMinute',
    labelKey: 'workMinute',
    width: 64,
    align: 'center',
  },
  {
    value: 'shouldCompleteCount',
    labelKey: 'shouldCompleteCount',
    width: 64,
    align: 'center',
  },
  {
    value: 'actualCompleteCount',
    labelKey: 'actualCompleteCount',
    width: 64,
    align: 'center',
    type: ValueType.HighLight,
  },
  {
    value: 'serialNoList',
    labelKey: 'serialNo',
    width: 152,
    align: 'center',
    type: ValueType.SerialNoList,
  },
  {
    value: 'completeMinute',
    labelKey: 'completeMinute',
    width: 64,
    align: 'center',
    type: ValueType.HighLight,
  },
  {
    value: 'leaderSubsidyMinute',
    labelKey: 'leaderSubsidyMinute',
    width: 64,
    align: 'center',
  },
  {
    value: 'remark',
    labelKey: 'remark',
    width: 192,
    align: 'center',
  },
  {
    value: 'deviceCompletePercentFormat',
    labelKey: 'deviceCompletePercent',
    width: 82,
    align: 'center',
  },
  {
    value: 'surplusCount',
    labelKey: 'surplusCount',
    width: 64,
    align: 'center',
    type: ValueType.Number,
  },
  {
    mergeKey: ['reportDate', 'userId', 'testDevice'],
    value: 'deviceUnitPriceFormat',
    labelKey: 'deviceUnitPrice',
    width: 82,
    align: 'center',
  },
  {
    value: 'salaryFormat',
    labelKey: 'salary',
    width: 96,
    align: 'center',
  },
  {
    mergeKey: ['reportDate', 'userId', 'testDevice'],
    value: 'deviceRunningTime',
    labelKey: 'deviceRunningTime',
    width: 178,
    align: 'center',
    type: ValueType.DeviceRunningTime,
  },
  {
    mergeKey: ['reportDate', 'userId', 'testDevice'],
    value: 'deviceUsePercentFormat',
    labelKey: 'deviceUsePercent',
    width: 82,
    align: 'center',
  },
  {
    value: 'stopWorkingContent1Format',
    labelKey: 'stopWorkingContent1',
    label: '停机内容1 Nội dung dừng máy 1',
    width: 192,
    align: 'center',
  },
  {
    value: 'stopWorkingMinute1',
    labelKey: 'stopWorkingMinute1',
    width: 64,
    align: 'center',
  },
  {
    value: 'stopWorkingContent2Format',
    labelKey: 'stopWorkingContent2',
    width: 192,
    align: 'center',
  },
  {
    value: 'stopWorkingMinute2',
    labelKey: 'stopWorkingMinute2',
    width: 64,
    align: 'center',
  },
  {
    value: 'stopWorkingContent3Format',
    labelKey: 'stopWorkingContent3',
    width: 192,
    align: 'center',
  },
  {
    value: 'stopWorkingMinute3',
    labelKey: 'stopWorkingMinute3',
    width: 64,
    align: 'center',
  },
  {
    value: 'improveSuggestion',
    labelKey: 'improveSuggestion',
    width: 192,
    align: 'center',
  },
  {
    value: 'valid',
    labelKey: 'valid',
    align: 'center',
    width: 86,
    type: user.username === 'admin' ? ValueType.ValidEdit : ValueType.Valid,
  },
  {
    value: 'photoList',
    labelKey: 'photo',
    align: 'center',
    width: 128,
    type: ValueType.Image,
  },
])
handleToggleDetail()
handleToggleRemark()
const handleList = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startReportDate = formatDate(
        state.dateTimeList[0],
        "yyyy-MM-dd hh:mm:ss"
    )
    state.query.data.endReportDate = formatDate(
        state.dateTimeList[1],
        "yyyy-MM-dd hh:mm:ss"
    )
  } else {
    state.query.data.startReportDate = ''
    state.query.data.endReportDate = ''
  }
  httpGet(`douson/admin/report/list`, state.query.data).then(
      (res: any) => {
        ElMessage.success("Query success")
        const l: any[] = res.list
        state.summaryData = res.data || {}
        state.tableData = l
      }
  )
}
handleList()
const handleSaveModal = () => {
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleInitSerialNoList = () => {
  const len = Math.ceil(state.formData.actualCompleteCount) || 0
  const diff = len - state.formData.serialNoList.length
  if (diff > 0) {
    for (let i = 0; i < diff; i++) {
      state.formData.serialNoList.push('')
    }
  } else if (diff < 0) {
    for (let i = state.formData.serialNoList.length - 1; i >= len; i--) {
      state.formData.serialNoList.splice(i, 1)
    }
  }
  state.formData.completeMinute = state.formData.actualCompleteCount * state.formData.productStandMinute
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    const startMinute = state.formData.deviceRunningStartHour * 60 + state.formData.deviceRunningStartMinute
    const endMinute = (state.formData.deviceRunningEndHour * 60 + state.formData.deviceRunningEndMinute)
    if (startMinute >= endMinute) {
      ElMessage.error('设备运行时间填写错误')
      return
    }
    const alreadyUseList = (state.tableData.filter(t => t.reportDate === state.formData.reportDate && t.userId === state.formData.userId && t.testDevice === state.formData.testDevice && t.reportId !== state.formData.reportId)
    .map(t => t.currentDeviceUseMinute) || [])
    const deviceUseMinute = (alreadyUseList.length <= 0 ? 0 : (alreadyUseList.reduce((t, t1) => t + t1) || 0)) + (endMinute - startMinute)
    const percent = deviceUseMinute * 100 / Math.max(state.formData.workMinute || 1, 1)
    if (percent < 85 && !state.formData.improveSuggestion) {
      ElMessage.error(`当班设备利用率% Tỷ lệ % hiệu suất máy ca: ${percent}%, device use: ${deviceUseMinute}m`)
      return
    }
    const len = Math.ceil(state.formData.actualCompleteCount) || 0
    const sa = state.formData.serialNoList
    if (len != sa.length) {
      ElMessage.error('完成产品序列号与实际完成数量不匹配')
      return
    }
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/report', state.formData).then(() => {
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
  return httpPutJson('douson/admin/report', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handleList()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/report', {
      reportId: row.reportId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handleList()
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
  handleList()
}
const {
  query,
  photoVisible,
  photoList,
  summaryData,
  tableData,
  config,
  dateTimeList,
  formData,
  formSave,
  formVisible,
  activeReport,
  formRuleList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
