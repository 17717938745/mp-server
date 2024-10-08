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
      <el-select
          v-model="query.data.schedule"
          @change="handleList"
          clearable
          :placeholder="store.state.label.schedule">
        <el-option
            v-for="item in config.scheduleList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
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
        :handleEdit="includes(roleCodeList, 'admin') ? handleEdit : null"
        :handle-update="includes(roleCodeList, 'admin') ? handleUpdate : null"
    >
    </view-list>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%">
      <el-form :rules="formRuleList" :model="formData" ref="formRef" label-width="128px">
        <el-form-item prop="userId" label="User id">
          <el-input v-model="formData.userId" :disabled="!formSave" placeholder="User ID, if empty, auto create"/>
        </el-form-item>
        <el-form-item prop="username" :label="store.state.label.username">
          <el-input v-model="formData.username"/>
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
        <el-form-item prop="schedule" :label="store.state.label.schedule">
          <el-select
              v-model="formData.schedule"
              clearable
              :placeholder="store.state.label.schedule">
            <el-option
                v-for="item in config.scheduleList"
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
        <el-form-item prop="mobile" :label="store.state.label.mobile">
          <el-input v-model="formData.mobile"/>
        </el-form-item>
        <el-form-item prop="roleIdList" :label="store.state.label.role">
          <el-select
              v-model="formData.roleIdList"
              :disabled="user.username !== 'admin'"
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
import {StoreType} from '@/store'
import ViewList from '../../../component/ViewList.vue'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ImageUpload from '../../../component/ImageUpload.vue'

const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const columnConfigList = ref<ViewConfig[]>([
  {
    value: 'expand',
    label: '',
    width: 48,
    type: ValueType.Expand,
  },
  {value: 'index', labelKey: 'index', width: 80,},
  {
    value: 'operator',
    labelKey: 'viewAndEdit',
    width: 180,
    type: ValueType.Operator,
  },
  {value: 'username', labelKey: 'username', width: 152,},
  {value: 'departmentFormat', labelKey: 'department', width: 186,},
  {value: 'name', labelKey: 'chineseName', width: 168,},
  {value: 'userPropertyFormat', labelKey: 'userProperty', width: 168,},
  {value: 'schedule', labelKey: 'schedule', width: 168, type: ValueType.SelectEdit, optionList: [],},
  {value: 'professionFormat', labelKey: 'profession', width: 432,},
  {value: 'mobile', labelKey: 'mobile', width: 128,},
  {value: 'roleNameList', labelKey: 'role', width: 256, type: ValueType.TagList},
  {value: 'interviewResume', labelKey: 'interviewResume', width: 368, type: ValueType.Text, showOverflow: true,},
  {value: 'photoList', labelKey: 'photo', width: 269, type: ValueType.Image,},
  {value: 'stateFormat', labelKey: 'state', width: 143,},
])
if (!includes(roleCodeList, 'userManager')) {
  columnConfigList.value = columnConfigList.value.filter((t: any) => t.value !== 'interviewResume')
}
const defaultFormData = {
  userId: '',
  photoList: [],
  roleIdList: ['user'],
  username: '',
  profession: '',
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
      department: '',
      profession: '',
      username: '',
      roleId: '',
      name: '',
      userProperty: '',
      schedule: '',
      state: 0,
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
  },
  formData: Object.assign({}, defaultFormData),
  formRuleList: {
    roleIdList: [{required: true, message: 'Please check', trigger: 'blur'}],
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
const editSchedule = ref(false)
httpGet('douson/config').then(r => {
  state.config = r.data
  columnConfigList.value = columnConfigList.value.map((t: any) => {
    if (t.value === 'schedule') {
      t.type = ValueType.SelectEdit
      t.optionList = state.config.scheduleList
    }
    return t
  })
  handleList()
})
const handleList = () => {
  httpGet(`system/user/list`, state.query.data).then(
      (res: PageResult<typeof state.tableData>) => {
        state.tableData = (res.list || []).map((t: any) => {
          t.roleNameList = (t.roleList || []).map((t1: any) => t1.roleName)
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
const handleEditScheduleShow = (row: any) => {
  editSchedule.value = true
  state.formData = Object.assign({}, row)
}
const handleEditSchedule = (row: any) => {
  httpPutJson('system/user', row).then(() => {
    editSchedule.value = false
    ElMessage.success('Edit success')
    handleList()
  })
}
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
