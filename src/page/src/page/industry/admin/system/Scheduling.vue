<template>
  <div>
    <div class="query-container">
      <el-date-picker
          v-model="deliveryDateTimeList"
          @change="() => {handleDateTimeChange(deliveryDateTimeList, query.data, 'deliveryDate')}"
          type="week"
          format="YYYY-MM"
          range-separator="-"
          :start-placeholder="`Start ${store.state.label.date}`"
          :end-placeholder="`End ${store.state.label.date}`"
      >
      </el-date-picker>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            v-if="editAll"
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
        <!--<el-button :icon="Plus" @click="handleSaveModal" type="success">Add</el-button>-->
      </div>
    </div>
    <view-list
        idKey="planId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleEditShow="() => {return editYellow || editBlue}"
        :handleUpdate="handleUpdate"
        :handleDelete="'admin' === user.username || editAll ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detail-link="false"
        :handleTableCellClassName="handleTableCellClassName"
    >
      <template #operator="row">
        <el-link
            :icon="More"
            @click="handleShowPrintDetail(row)"
            class="mr10"
            type="info"
        >
          <el-tag size="small">Detail</el-tag>
        </el-link>
        <el-link
            v-if="row.param.serialIndex === 0"
            :icon="List"
            @click="handleGenerateListModal(row)"
            class="mr10"
            type="info"
            :disabled="row.param.maxSerialOrderIndex >= row.param.orderCount"
        >
          <el-tag size="small" :type="row.param.maxSerialOrderIndex >= row.param.orderCount ? 'info' : 'primary'">Generate</el-tag>
        </el-link>
      </template>
    </view-list>

    <el-dialog :title="'Generate list'" v-model="generateListVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="generateListFormRuleList"
          :model="formData"
          ref="generateFormRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="createOrderCount" :label="store.state.label.createOrderCount">
          <el-input-number style="width: 300px;" :min="1" :max="formData.orderCount - formData.maxSerialOrderIndex" v-model="formData.createOrderCount" :placeholder="`1 <= input <= ${formData.orderCount - formData.maxSerialOrderIndex}`"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="generateListVisible = false">Cancel</el-button>
          <el-button type="primary" @click="handleGenerateList">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="schedulingCompleteDate" :label="store.state.label.schedulingCompleteDate">
          <el-date-picker
              type="week"
              v-model="formData.schedulingCompleteDate"
              format="YYYY-MM-DD"
              @change="formData.schedulingCompleteDate = formatDate(formData.schedulingCompleteDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="deviceNumber" :label="store.state.label.deviceNumber">
          <el-input v-model="formData.deviceNumber"/>
        </el-form-item>
        <el-form-item prop="scheduleDayTimeLabel" :label="store.state.label.deviceNumber">
          <el-input v-model="formData.scheduleDayTimeLabel"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-affix position="bottom" :offset="20">
          <span class="dialog-footer">
            <el-button @click="formVisible = false">Cancel</el-button>
            <el-button type="primary" @click="handleMerge">Confirm</el-button>
          </span>
        </el-affix>
      </template>
    </el-dialog>
    <el-dialog :title="formDetailSave ? 'Add' : 'Edit'" v-model="formDetailVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formDetailRuleList"
          :model="formDetailData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="schedulingCompleteDate" :label="store.state.label.schedulingCompleteDate">
          <el-date-picker
              type="week"
              v-model="formDetailData.schedulingCompleteDate"
              format="YYYY-MM-DD"
              :disabled="true"
              @change="formData.schedulingCompleteDate = formatDate(formData.schedulingCompleteDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="deviceNumber" :label="store.state.label.deviceNumber">
          <el-input v-model="formDetailData.deviceNumber" :disabled="true"/>
        </el-form-item>
        <el-form-item prop="scheduleDayTime" :label="store.state.label.scheduleDayTime">
          <el-select v-model="formDetailData.scheduleDayTimeList" clearable filterable multiple placeholder="Please select">
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-affix position="bottom" :offset="20">
          <span class="dialog-footer">
            <el-button @click="formVisible = false">Cancel</el-button>
            <el-button type="primary" @click="handleMerge">Confirm</el-button>
          </span>
        </el-affix>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="tsx" setup>
import {onMounted, reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Search, More, UploadFilled, List,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload, httpDownloadFile,} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const generateFormRef: Ref = ref(null)
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const editAll = includes(roleCodeList, 'admin')
const editBlue = editAll || includes(roleCodeList, 'schedulingRecord')
const editYellow = editAll || includes(roleCodeList, 'schedulingTesterRecord')
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 312, type: ValueType.Operator,},
  {value: 'deviceNumber', labelKey: 'deviceNumber', width: 189},
  {value: 'scheduleDayTime', labelKey: 'scheduleDayTime', width: 189},
  {value: 'scheduleMiddle', labelKey: 'scheduleMiddle', width: 189},
  {value: 'scheduleEvening', labelKey: 'scheduleEvening', width: 189},
  {value: 'scheduleDayTime12', labelKey: 'scheduleDayTime12', width: 189},
  {value: 'scheduleEvening12', labelKey: 'scheduleEvening12', width: 189},
  {value: 'scheduleDayTimeTechnologyGroup', labelKey: 'scheduleDayTimeTechnologyGroup', width: 189},
  {value: 'scheduleEveningTechnologyGroup', labelKey: 'scheduleEveningTechnologyGroup', width: 189},
])
const handleDownloadTemplate = () => {
  httpDownloadFile("douson/scheduling/template", state.query.data);
}
const defaultFormData = {
  deviceNumber: '',
  scheduleDayTimeList: [],
  attachmentList: [],
}
const deliveryDateTimeList = ref([])
const formDetailSave = ref(false)
const formDetailVisible = ref(false)
const formDetailRuleList = ref({})
const formDetailData = ref({
  deviceNumber: '',
  scheduleDayTimeList: [],
})
const state = reactive({
  photoVisible: false,
  photoList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      deviceNumber: '',
      scheduleDayTime: '',
      scheduleMiddle: '',
      scheduleEvening: '',
      scheduleDayTime12: '',
      scheduleEvening12: '',
      scheduleDayTimeTechnologyGroup: '',
      scheduleEveningTechnologyGroup: '',
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
    departmentList: [],
    optimizeTypeList: [],
    responsibleTeamList: [],
  },
  userConfigList: new Array<any>(),
  formVisible: false,
  formRuleList: {
  },
})
const generateListFormRuleList = {
  createOrderCount: [{required: true, type: 'number', min: 1, message: 'Please check', trigger: 'blur'}],
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
  window.open(`/industry/public/scheduling?schedulingId=${d.param.schedulingId}`)
}
const generateListVisible = ref(false)
const handleGenerateListModal = (d: any) => {
  state.formData = Object.assign({}, d.param)
  generateListVisible.value = true
}
const handleGenerateList = () => {
  generateFormRef.value.validate((valid: any) => {
    if (valid) {
      httpPostJson('douson/scheduling/generate-list', state.formData).then(() => {
        generateListVisible.value = false
        ElMessage.success('Generate list success')
        handlePage()
      })
    }
  })
}

const handleJumpOrder = (t: any) => {
  router.push(
      {
        path: '/industry/admin/product/order',
        query: {
          orderId: t.orderId,
        },
      })
}
const handleJumpDevice = (t: any) => {
  router.push(
      {
        path: '/industry/admin/product/device',
        query: {
          deviceId: t.testDevice,
        },
      })
}
const handlePage = () => {
  httpGet(`/douson/scheduling/page`, state.query).then(
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
const handleTableCellClassName = ({
                                    row,
                                    column,
                                    rowIndex,
                                    columnIndex,
                                  }: {
  row: any
  rowIndex: number
}) => {
  if (row.serialIndex === 0 && row.orderCount === row.completedQty) {
    return 'row-green'
  }
  return ''
}
let serialNoIndex = -1

Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'equipmentNo',
      'equipmentPosition',
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
    if (editBlue) {
      if ('valveBodyPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveBodyPhotoList')
          }, 100)
        }
      } else if ('valveCoverPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveCoverPhotoList')
          }, 100)
        }
      } else if ('gatePhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('gatePhotoList')
          }, 100)
        }
      } else if ('valveSeatPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveSeatPhotoList')
          }, 100)
        }
      } else if ('valveStemPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('valveStemPhotoList')
          }, 100)
        }
      }
    }
    if (editYellow) {
      if ('pressureTestPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('pressureTestPhotoList')
          }, 100)
        }
      } else if ('oilInjectionPhotoCount' === t.value) {
        t.type = ValueType.Link
        t.openLink = (d: any) => {
          handleEdit(d)
          setTimeout(() => {
            // formRef.value.$el
            formRef.value.scrollToField('oilInjectionPhotoList')
          }, 100)
        }
      }
    }

    return t
  })
  handlePage()
})
const uploadData = ref({})
const afterUpload = ref(false)
const fileMap: any = {}
const handleFileChange = (file: UploadFile, fileList: UploadFiles) => {
  console.log('file change, length: ' + fileList.length)
}
const handleBeforeUpload = (file: UploadFile) => {
  console.log('before upload file: ' + file.uid)
  fileMap[file.uid] = file
  return file
}
const handleRequest = (d: any) => {
  const keys = Object.keys(fileMap)
  const limit = 1
  if (keys.length > limit) {
    ElMessage.error(`Too many upload`)
    keys.forEach((k: any) => {
      delete fileMap[k]
    })
  } else if (keys.length > 0) {
    Promise.all(keys.map((k: any) => {
      const t = fileMap[k]
      const formData = new FormData()
      formData.set("filename", t.name)
      formData.set("file", t)
      return httpUpload(`douson/scheduling/upload`, formData)
    }))
    .then((l: any[]) => {
      afterUpload.value = true
      uploadData.value = (l[0] || {}).data || {}
      handlePage()
      keys.forEach((k: any) => {
        delete fileMap[k]
      })
      return Promise.resolve()
    })
    .catch((err) => {
      ElMessage.error(`Upload failed`)
      keys.forEach((k: any) => {
        delete fileMap[k]
      })
      return Promise.reject()
    })
  }
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
        httpPostJson('douson/admin/plan', state.formData).then(() => {
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
  return httpPutJson('douson/scheduling/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('/douson/scheduling', {
      schedulingId: row.schedulingId,
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
