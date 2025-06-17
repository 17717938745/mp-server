<template>
  <div>
    <div class="query-container">
      <el-select v-model="query.data.paramCategoryId"
                 filterable
                 allow-create
                 clearable
                 placeholder="请选择参数类型"
                 @change="handleChangeAndList">
        <el-option-group
            v-for="(g, i) in paramCategoryGroupList"
            :key="`group-${i}`"
            :label="store.state.label[g.labelKey]"
        >
          <el-option
              v-for="item in g.paramCategoryIdOptionList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-option-group>
      </el-select>
      <div class="query-btn">
        <el-button
            :icon="Search"
            @click="handleList"
            type="primary"
        >Search
        </el-button>
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
          prop="paramCategoryId"
          label="参数分类"
          width="256"
          align="left"
      >
        <template #default="scope">
          {{ handleFormatValue(scope.row.paramCategoryId) }}（{{ scope.row.paramCategoryId }}）
        </template>
      </el-table-column>
      <el-table-column
          prop="paramCode"
          label="参数编码"
          width="192"
          align="left"
      ></el-table-column>
      <el-table-column
          prop="paramName"
          label="参数名称"
          width="256"
          align="left"
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
            <el-link
                v-if="includes(roleCodeList, 'admin')"
                @click="handleDelete(scope.row)"
                type="danger">
              Delete
            </el-link>
          </el-space>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form :rules="formRuleList" :model="formData" ref="formRef" label-width="128px">
        <el-form-item prop="paramCategoryId" label="参数分类" ref="formAutoFocusRef">
          <el-select v-model="formData.paramCategoryId" :disabled="!formSave" clearable placeholder="参数类型" @change="query.data.paramCategoryId = formData.paramCategoryId">
            <el-option-group
                v-for="(g, i) in paramCategoryGroupList"
                :key="`group-${i}`"
                :label="store.state.label[g.labelKey]"
            >
              <el-option
                  v-for="item in g.paramCategoryIdOptionList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item prop="paramCode" label="参数编码">
          <el-input v-model="formData.paramCode" :disabled="!formSave"/>
        </el-form-item>
        <el-form-item prop="paramName" label="参数名称">
          <el-input v-model="formData.paramName"/>
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
import {Store, useStore} from 'vuex'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Edit, Plus, Search} from '@element-plus/icons-vue'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {includes} from '@/util/ArrayUtil'
import {StoreType} from '@/store/Index'

const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const state = reactive({
  query: {
    data: {
      paramCategoryId: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  formData: {
    paramCategoryId: '',
    paramCode: '',
    paramName: '',
    paramValue: null,
    sorter: 0,
  },
  formSave: false,
  formVisible: false,
  formRuleList: {
    paramCategoryId: [{required: true, message: '请选择参数类型', trigger: 'blur'}],
    paramCode: [{required: true, message: '请输入参数编码', trigger: 'blur'}],
    paramName: [{required: true, message: '请输入参数名称', trigger: 'blur'}],
    sorter: [{required: true, message: '请输入排序', trigger: 'blur'}],
  },
})

const paramCategoryGroupList = [
  {
    labelKey: 'vocationRecord',
    paramCategoryIdOptionList: [
      /*{
        value: 'accidentType',
        label: store.state.label.accidentType,
      },*/
      {
        value: 'vocationType',
        label: store.state.label.vocationType,
      },
    ],
  },
  {
    labelKey: 'deviceCheckLedger',
    paramCategoryIdOptionList: [
      {
        value: 'chineseVietnamName',
        label: store.state.label.chineseVietnamName,
      },
      {
        value: 'calibrationPeriod',
        label: store.state.label.calibrationPeriod,
      },
      {
        value: 'storage',
        label: store.state.label.storage,
      },
      {
        value: 'deviceCheckLedgerState',
        label: store.state.label.deviceCheckLedgerState,
      },
    ],
  },
  {
    labelKey: 'userManage',
    paramCategoryIdOptionList: [
      {
        value: 'userProperty',
        label: store.state.label.userProperty,
      },
      {
        value: 'schedule',
        label: store.state.label.schedule,
      },
      {
        value: 'department',
        label: store.state.label.department,
      },
      {
        value: 'profession',
        label: store.state.label.profession,
      },
      {
        value: 'nationality',
        label: store.state.label.nationality,
      },
      {
        value: 'innerNetSection',
        label: store.state.label.innerNetSection,
      },
    ],
  },
  {
    labelKey: 'productQuotaManage',
    paramCategoryIdOptionList: [
      {
        value: 'processProcedure',
        label: store.state.label.processProcedure,
      },
    ],
  },
  {
    labelKey: 'userRunReport',
    paramCategoryIdOptionList: [
      {
        value: 'processType',
        label: store.state.label.processType,
      },
      {
        value: 'workMinute',
        label: store.state.label.workMinute,
      },
      {
        value: 'stopWorkingContent1',
        label: store.state.label.stopWorkingContent1,
      },
      {
        value: 'stopWorkingContent2',
        label: store.state.label.stopWorkingContent2,
      },
      {
        value: 'stopWorkingContent3',
        label: store.state.label.stopWorkingContent3,
      },
    ],
  },
  {
    labelKey: 'boxFlagCard',
    paramCategoryIdOptionList: [
      {
        value: 'customerShortName',
        label: store.state.label.customerShortName,
      },
    ],
  },
  {
    labelKey: 'disqualificationOrder',
    paramCategoryIdOptionList: [
      {
        value: 'process',
        label: store.state.label.process,
      },
      {
        value: 'checkPoint',
        label: store.state.label.checkPoint,
      },
      {
        value: 'qualityDealOpinion',
        label: store.state.label.qualityDealOpinion,
      },
      {
        value: 'skillDealOpinion',
        label: store.state.label.skillDealOpinion,
      },
      {
        value: 'defectType',
        label: store.state.label.defectType,
      },
    ],
  },
  {
    labelKey: 'accidentQuality',
    paramCategoryIdOptionList: [
      {
        value: 'qualityReason',
        label: store.state.label.qualityReason,
      },
      {
        value: 'crashReason',
        label: store.state.label.crashReason,
      },
      {
        value: 'troubleReason',
        label: store.state.label.troubleReason,
      },
    ],
  },
  {
    labelKey: 'keepImprove',
    paramCategoryIdOptionList: [
      {
        value: 'optimizeType',
        label: store.state.label.optimizeType,
      },
      {
        value: 'responsibleTeam',
        label: store.state.label.responsibleTeam,
      },
    ],
  },
  {
    labelKey: 'computerManage',
    paramCategoryIdOptionList: [
      {
        value: 'computerName',
        label: store.state.label.computerName,
      },
      {
        value: 'companyPosition',
        label: store.state.label.companyPosition,
      },
      {
        value: 'computerState',
        label: store.state.label.computerState,
      },
    ],
  },
  {
    labelKey: 'accidentEvent',
    paramCategoryIdOptionList: [
      {
        value: 'eventReason',
        label: store.state.label.eventReason,
      },
      {
        value: 'improveReason',
        label: store.state.label.accidentImproveReason,
      },
    ],
  },
  {
    labelKey: 'productMachine',
    paramCategoryIdOptionList: [
      {
        value: 'equipmentPosition',
        label: store.state.label.equipmentPosition,
      },
      {
        value: 'brokenReason',
        label: store.state.label.brokenReason,
      },
      {
        value: 'repairType',
        label: store.state.label.repairType,
      },
    ],
  },
  {
    labelKey: 'inventoryOutOfPlanType',
    paramCategoryIdOptionList: [
      {
        value: 'inventoryOutOfPlanType',
        label: store.state.label.type,
      },
    ],
  },
  {
    labelKey: 'equipment',
    paramCategoryIdOptionList: [
      {
        value: 'apiDevice',
        label: store.state.label.apiDevice,
      },
    ],
  },
  {
    labelKey: 'workDressManage',
    paramCategoryIdOptionList: [
      {
        value: 'workDressType',
        label: store.state.label.workDressType,
      },
      {
        value: 'storePosition',
        label: store.state.label.storePosition,
      },
    ],
  },
]

const handleFormatValue = (val: any) => {
  for (let i = 0; i < paramCategoryGroupList.length; i++) {
    const l = paramCategoryGroupList[i].paramCategoryIdOptionList || []
    for (let j = 0; j < l.length; j++) {
      const t = l[j]
      if (t.value === val) {
        return t.label
      }
    }
  }
  return val
}
const handleList = () => {
  httpGet(`douson/admin/param/list`, state.query.data).then(
      (res: ListResult<typeof state.tableData>) => {
        state.tableData = res.list
        ElMessage.success("Query success")
      }
  )
}
const handleChangeAndList = () => {
  formData.value.paramCategoryId = state.query.data.paramCategoryId
  handleList()
}
// handleList()
const handleSaveModal = () => {
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
        httpPostJson('douson/admin/param', state.formData).then(() => {
          state.formVisible = false
          ElMessage.success('Add success')
          handleList()
        })
      } else {
        httpPutJson('douson/admin/param', state.formData).then(() => {
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
    httpDelete('douson/admin/param', row)
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
