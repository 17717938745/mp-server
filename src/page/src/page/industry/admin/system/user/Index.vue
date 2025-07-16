<template>
  <div>
    <div class="query-container">
      <el-select v-model="query.data.department"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.department"
                 @change="handleList">
        <el-option
            v-for="item in config.departmentList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.profession"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.profession"
                 @change="handleList">
        <el-option
            v-for="item in config.professionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.nationality"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.nationality"
                 @change="handleList">
        <el-option
            v-for="item in config.nationalityList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.employeeId" @keyup.enter="handleList" :placeholder="store.state.label.employeeId"/>
      <el-input v-model="query.data.username" @keyup.enter="handleList" :placeholder="store.state.label.username"/>
      <el-input v-model="query.data.name" @keyup.enter="handleList" :placeholder="store.state.label.chineseName"/>
      <el-select
          v-model="query.data.roleId"
          @change="handleList"
          clearable
          :placeholder="store.state.label.role">
        <el-option-group
            v-for="(g, i) in roleGroupList"
            :key="`group-${i}`"
            :label="g.label"
        >
          <el-option
              v-for="item in g.list || []"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
          />
        </el-option-group>
      </el-select>
      <el-select
          v-if="includes(roleCodeList, 'admin')"
          v-model="query.data.state"
          @change="handleList"
          clearable
          :placeholder="store.state.label.state">
        <el-option
            :label="store.state.label.normal"
            :value="0"
        />
        <el-option
            :label="store.state.label.leaveCompany"
            :value="1"
        />
      </el-select>
      <el-select
          v-model="query.data.userProperty"
          @change="handleList"
          clearable
          :placeholder="store.state.label.userProperty">
        <el-option
            v-for="item in config.userPropertyList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.leaderUserId"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.leaderUserId"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-date-picker
          v-model="createTimeDateTimeList"
          @change="() => {handleDateTimeChange(createTimeDateTimeList, query.data, 'createTime')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.createTime}`"
          :end-placeholder="`End ${store.state.label.createTime}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-date-picker
          v-model="planIncreaseSalaryDateDateTimeList"
          @change="() => {handleDateTimeChange(planIncreaseSalaryDateDateTimeList, query.data, 'planIncreaseSalaryDate')}"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.planIncreaseSalaryDate}`"
          :end-placeholder="`End ${store.state.label.planIncreaseSalaryDate}`"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select
          v-model="query.data.externalSign"
          @change="handleList"
          clearable
          :placeholder="store.state.label.externalSign">
        <el-option
            label="Yes"
            :value="true"
        />
        <el-option
            label="No"
            :value="false"
        />
      </el-select>
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
        <el-button v-if="includes(roleCodeList, 'admin')"
                   :icon="Plus"
                   @click="handleSaveModal"
                   type="success"
        >Add
        </el-button>
      </div>
    </div>
    <view-list
        idKey="userId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleTableRowClassName="handleTableRowClassName"
        :handleEdit="includes(roleCodeList, 'admin') ? handleEdit : null"
        :handle-update="includes(roleCodeList, 'admin') ? handleUpdate : null"
    >
    </view-list>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%">
      <el-form :rules="formRuleList" :model="formData" ref="formRef" label-width="128px">
        <el-form-item prop="userId" label="User id">
          <el-input v-model="formData.userId" :disabled="!formSave" placeholder="User ID, if empty, auto create"/>
        </el-form-item>
        <el-form-item prop="employeeId" :label="store.state.label.employeeId">
          <el-input v-model="formData.employeeId"/>
        </el-form-item>
        <el-form-item prop="username" :label="store.state.label.username">
          <el-input v-model="formData.username"/>
        </el-form-item>
        <el-form-item prop="leaderUserId" :label="store.state.label.leaderUserId">
          <el-select v-model="formData.leaderUserId" clearable filterable placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="department" :label="store.state.label.department">
          <el-select v-model="formData.department"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.department"
                     :disabled="!includes(roleCodeList, 'admin')">
            <el-option
                v-for="item in config.departmentList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="name" :label="store.state.label.chineseName">
          <el-input v-model="formData.name"/>
        </el-form-item>
        <el-form-item prop="userProperty" :label="store.state.label.userProperty">
          <el-select
              v-model="formData.userProperty"
              clearable
              :placeholder="store.state.label.userProperty">
            <el-option
                v-for="item in config.userPropertyList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="profession" :label="store.state.label.profession">
          <el-select v-model="formData.profession"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.profession"
                     :disabled="!includes(roleCodeList, 'admin')">
            <el-option
                v-for="item in config.professionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="nationality" :label="store.state.label.nationality">
          <el-select v-model="formData.nationality"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.nationality"
                     :disabled="!includes(roleCodeList, 'admin')">
            <el-option
                v-for="item in config.nationalityList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="mobile" :label="store.state.label.mobile">
          <el-input v-model="formData.mobile"/>
        </el-form-item>
        <el-form-item prop="roleIdList" :label="store.state.label.role">
          <el-select
              v-model="formData.roleIdList"
              :disabled="user.username !== 'admin'"
              filterable
              multiple
              clearable
              :placeholder="store.state.label.role">
            <el-option-group
                v-for="(g, i) in roleGroupList"
                :key="`group-${i}`"
                :label="g.label"
            >
              <el-option
                  v-for="item in g.list || []"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item prop="interviewResume" :label="store.state.label.interviewResume" v-if="includes(roleCodeList, 'userManager')">
          <el-input type="textarea" :rows=4 v-model="formData.interviewResume"/>
        </el-form-item>
        <el-form-item prop="password" :label="store.state.label.password">
          <el-input v-model="formData.password"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="`${store.state.label.portrait}(${(formRuleList['photoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.photoList" :maxSize="Number(`${(formRuleList['photoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="state" :label="store.state.label.state">
          <el-select v-model="formData.state"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.state"
                     :disabled="!includes(roleCodeList, 'admin')">
            <el-option
                :value="0"
                label="Normal"
            />
            <el-option
                :value="1"
                label="Leave"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="userEdit" prop="lastIncreaseSalaryDate" :label="store.state.label.lastIncreaseSalaryDate">
          <el-date-picker
              type="date"
              v-model="formData.lastIncreaseSalaryDate"
              format="YYYY-MM-DD"
              @change="formData.lastIncreaseSalaryDate = formatDate(formData.lastIncreaseSalaryDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item v-if="userEdit" prop="planIncreaseSalaryDate" :label="store.state.label.planIncreaseSalaryDate">
          <el-date-picker
              type="date"
              v-model="formData.date"
              format="YYYY-MM-DD"
              @change="formData.planIncreaseSalaryDate = formatDate(formData.planIncreaseSalaryDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
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

<script lang="ts" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {Plus, Search} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {includes} from '@/util/ArrayUtil'
import {StoreType} from '@/store/Index'
import ViewList from '../../../component/ViewList.vue'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ImageUpload from '../../../component/ImageUpload.vue'
import {formatDate} from '@/util/DateUtil'

const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const createTimeDateTimeList = ref([])
const planIncreaseSalaryDateDateTimeList = ref([])
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
  handleList()
}
const columnConfigList = ref<ViewConfig[]>([
  {
    value: 'expand',
    label: '',
    width: 48,
    type: ValueType.Expand,
  },
  {value: 'index', labelKey: 'index', width: 46,},
  {
    value: 'operator',
    labelKey: 'viewAndEdit',
    width: 87,
    type: ValueType.Operator,
  },
  {value: 'createTime', labelKey: 'createTime', width: 91,},
  {value: 'employeeId', labelKey: 'employeeId', width: 87,},
  {value: 'username', labelKey: 'username', width: 138,},
  {value: 'departmentFormat', labelKey: 'department', width: 121,},
  {value: 'name', labelKey: 'chineseName', width: 112,},
  {value: 'userPropertyFormat', labelKey: 'userProperty', width: 158,},
  {value: 'professionFormat', labelKey: 'profession', width: 216,},
  {value: 'nationalityFormat', originValue: 'nationality', labelKey: 'nationality', width: 101,},
  {value: 'leaderUserIdFormat', originValue: 'leaderUserId', labelKey: 'leaderUserId', width: 139,},
  {value: 'mobile', labelKey: 'mobile', width: 116,},
  {value: 'roleNameFormat', labelKey: 'role', width: 256, showOverflow: true,},
  {value: 'interviewResume', labelKey: 'interviewResume', width: 368, type: ValueType.Text, showOverflow: true,},
  {value: 'photoList', labelKey: 'photo', width: 269, type: ValueType.Image,},
  {value: 'stateFormat', labelKey: 'state', width: 72,},
  {value: 'externalSignFormat', originValue: 'externalSign', labelKey: 'externalSign', width: 72,type: 'admin' === user.username ? ValueType.SwitchEdit : ValueType.Text,},
])
const defaultFormData = {
  userId: '',
  lastIncreaseSalaryDate: '',
  planIncreaseSalaryDate: '',
  photoList: [],
  roleIdList: ['user'],
  username: '',
  employeeId: '',
  profession: '',
  leaderUserId: '',
  mobile: '',
  department: '',
  name: '',
  interviewResume: '',
  password: '123456',
  state: 0,
  userProperty: '',
  schedule: '',
}
const state = reactive({
  query: {
    data: {
      startPlanIncreaseSalaryDate: '',
      endPlanIncreaseSalaryDate: '',
      startCreateTime: '',
      endCreateTime: '',
      department: '',
      profession: '',
      username: '',
      employeeId: '',
      roleId: '',
      name: '',
      userProperty: '',
      schedule: '',
      nationality: '',
      leaderUserId: '',
      state: 0,
      externalSign: null,
    },
  },
  config: {
    processProcedureList: [],
    testDeviceList: [],
    customerShortNameList: [],
    departmentList: [],
    professionList: [],
    optimizeTypeList: [],
    userPropertyList: [],
    scheduleList: [],
    nationalityList: [],
  },
  formData: Object.assign({}, defaultFormData),
  formRuleList: {
    roleIdList: [{required: true, message: 'Please check', trigger: 'blur'}],
    employeeId: [{
      required: true, validator: (rule, value, callback) => {
        const regex = /^[0-9-]+$/;
        if (!regex.test(value)) {
          callback(new Error('Employee ID can only contain numbers and "-"'))
        } else {
          callback()
        }
      }, message: 'Please check', trigger: 'blur'
    }],
    username: [{required: true, message: 'Please check', trigger: 'blur'}],
    mail: [{required: true, message: 'Please check', trigger: 'blur'}],
    mobile: [{required: true, message: 'Please check', trigger: 'blur'}],
    nickname: [{required: true, message: 'Please check', trigger: 'blur'}],
    name: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', min: 0, max: 5}],
  },
  tableData: new Array<any>(),
  formSave: false,
  formVisible: false,
})
const handleList = () => {
  httpGet(`system/user/list`, state.query.data).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = (res.list || []).map((t: any) => {
          t.roleNameList = (t.roleList || []).map((t1: any) => t1.roleName)
          t.roleNameFormat = (t.roleList || []).map((t1: any) => t1.roleName).join(', ')
          return t
        })
        ElMessage.success("Query success")
      }
  )
}
const roleGroupList = ref(new Array<any>())
httpGet(`system/role/group/list`, {}).then(
    (res: ListResult<any>) => {
      roleGroupList.value = res.list
    }
)
const handleSaveModal = () => {
  state.formVisible = true
  state.formSave = true
  state.formData = Object.assign({}, defaultFormData)
}
const handleEdit = (row: any, index: number = 0) => {
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.planIncreaseSalaryDateCount != null && row.planIncreaseSalaryDateCount < 30) {
    return 'row-yellow'
  }
  return ''
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('system/user', state.formData).then(() => {
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
  return httpPutJson('system/user', row).then(() => {
    state.formVisible = false
    ElMessage.success('Edit success')
    handleList()
  })
}
const userOptionList = ref(new Array<any>())
const userEdit = user.username === 'admin' || includes(roleCodeList, 'userManager')
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'processProcedure',
      'testDevice',
      'customerShortName',
      'department',
      'profession',
      'optimizeType',
      'userProperty',
      'schedule',
      'nationality',
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
  if (includes(roleCodeList, 'admin')) {
    columnConfigList.value = columnConfigList.value.map((t: any) => {
      if (t.value === 'schedule') {
        t.type = ValueType.SelectEdit
        t.optionList = state.config.scheduleList
      } else if (t.value === 'leaderUserIdFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = userOptionList.value
      } else if (t.value === 'nationalityFormat') {
        t.type = ValueType.SelectEdit
        t.optionList = state.config.nationalityList
      }
      return t;
    })
  }
  if (!includes(roleCodeList, 'userManager')) {
    columnConfigList.value = columnConfigList.value.filter((t: any) => t.value !== 'interviewResume')
  }
  if (userEdit) {
    columnConfigList.value.push(...[
      {value: 'lastIncreaseSalaryDate', labelKey: 'lastIncreaseSalaryDate', width: 101, type: ValueType.DateEdit,},
      {value: 'planIncreaseSalaryDate', labelKey: 'planIncreaseSalaryDate', width: 101, type: ValueType.DateEdit,},
    ])
  }
  handleList()
})

const {
  query,
  tableData,
  formData,
  formSave,
  config,
  formVisible,
  formRuleList,
} = {
  ...toRefs(state),
}
</script>

<style scoped lang="scss">
div {

}
</style>
