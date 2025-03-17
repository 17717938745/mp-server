<template>
  <div>
    <div class="query-container">
      <!--<el-input v-model="query.data.deviceId" placeholder="设备ID"/>-->
      <el-input v-model="query.data.deviceName" @keyup.enter="handleList" placeholder="设备名称"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handleList" type="primary">Search</el-button>
        <el-button
            v-if="includes(roleCodeList, 'admin')"
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
      </div>
    </div>
    <el-table
        :header-cell-style="{
        background: ' rgba(250, 251, 252, 1)',
        color: 'rgba(102, 102, 102, 1)',
        height: '54px',
        fontSize: '14px',
        padding: 0,
        border: 'none',
        boxShadow:
          'inset 0px -1px 0px  rgba(238, 238, 238, 1),inset 0px 1px 0px  rgba(238, 238, 238, 1)',
      }"
        :data="tableData"
    >
      <el-table-column
          prop="deviceName"
          label="设备名称"
          width="256"
          align="left"
      ></el-table-column>
      <el-table-column
          prop="deviceTime"
          label="运行时间"
          width="256"
          align="center"
      >
        <template #default="scope">
          {{ `${scope.row.runningHour}:${scope.row.runningMinute}` }}
        </template>
      </el-table-column>
      <el-table-column
          prop="unitPrice"
          label="单价"
          width="256"
          align="center"
      ></el-table-column>
      <el-table-column
          prop="supplierFormat"
          label="是否供应商"
          width="256"
          align="center"
      ></el-table-column>
      <el-table-column
          prop="sorter"
          label="排序"
          width="64"
          align="left"
      ></el-table-column>
      <el-table-column
          prop="operator"
          label="操作"
          width="256"
      >
        <template #default="scope">
          <el-space>
            <el-link
                v-if="includes(roleCodeList, 'admin')"
                :icon="Edit"
                @click="handleEdit(scope.row, scope.$index)"
                class="mr10"
                type="warning"
            >
              Edit
            </el-link>
            <!--            <el-link @click="handleDelete(scope.row)" class="mr10" type="danger">
                          Delete
                        </el-link>-->
          </el-space>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form :rules="formRuleList" :model="formData" ref="formRef" label-width="128px">
        <el-form-item prop="deviceName" label="设备名称">
          <el-input v-model="formData.deviceName"/>
        </el-form-item>
        <el-form-item prop="unitPrice" label="单价">
          <el-input-number v-model="formData.unitPrice" :min="0" :controls="false"/>
        </el-form-item>
        <el-form-item prop="runningTime" label="运行时间">
          <el-input-number v-model="formData.runningHour" style="width: 60px;" :min="0" :controls="false"/>
          :
          <el-input-number v-model="formData.runningMinute" style="width: 60px;" :controls="false" :min="0" :max="59"/>
        </el-form-item>
        <el-form-item prop="supplier" :label="store.state.label.supplier">
          <el-select
              v-model="formData.supplier"
              filterable
              allow-create
              clearable
              :placeholder="store.state.label.supplier">
            <el-option
                label="Yes"
                :value="true"
            />
            <el-option
                label="No"
                :value="false"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="sorter" label="排序">
          <el-input v-model="formData.sorter"/>
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
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Edit, Plus, Search} from '@element-plus/icons-vue'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {includes} from '@/util/ArrayUtil'
import {Store, useStore} from "vuex";
import {StoreType} from '@/store/Index';

const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const router = useRouter()
const route = useRoute()
const formRef: Ref = ref(null)
const state = reactive({
  query: {
    data: {
      deviceId: '',
      deviceName: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  formData: {
    id: '',
    deviceName: '',
    unitPrice: 0,
    runningHour: 0,
    runningMinute: 0,
    supplier: false,
    sorter: 0,
  },
  formSave: false,
  formVisible: false,
  formRuleList: {
    deviceName: [{required: true, message: '请输入', trigger: 'blur'}],
    unitPrice: [{required: true, message: '请输入', trigger: 'blur'}],
    runningHour: [{required: true, message: '请输入', trigger: 'blur'}],
    runningMinute: [{required: true, message: '请输入', trigger: 'blur'}],
    supplier: [{required: true, message: 'Please select', trigger: 'blur'}],
  },
})


const handleList = (deviceId: string = '') => {
  state.query.data.deviceId = deviceId
  httpGet(`douson/admin/device/list`, state.query.data).then(
      (res: ListResult<any>) => {
        const l = res.list || []
        state.tableData = l
        if (deviceId && l.length > 0) {
          state.formVisible = true
          state.formData = Object.assign({}, l[0])
        }
        ElMessage.success("Query success")
      }
  )
}
const deviceId: string = String(route.query.deviceId || '')
handleList(deviceId)
const handleSaveModal = () => {
  state.formVisible = true
  state.formSave = true
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
        httpPostJson('douson/admin/device', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handleList()
        })
      } else {
        httpPutJson('douson/admin/device', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Update success')
          handleList()
        })
      }
    }
  })
}
const handleDelete = (row: typeof state.tableData) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/device', row)
    .then(() => {
      ElMessage.success('Delete success')
      handleList()
    })
  })
}
const {
  query,
  tableData,
  formData,
  formSave,
  formVisible,
  formRuleList,
} = {
  ...toRefs(state),
}
</script>

<style scoped lang="scss">
</style>
