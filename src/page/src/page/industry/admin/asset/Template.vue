<template>
  <div>
    <div class="query-container">
      <el-select v-model="query.data.userId"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.chineseName"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.brand"
                @change="handlePage"
                :placeholder="store.state.label.brand"
                class="search-item"/>
      <el-select v-model="query.data.computerName"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.computerName"
                 class="search-item">
        <el-option
            v-for="item in config.computerNameList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.model"
                @change="handlePage"
                :placeholder="store.state.label.computerModel"
                class="search-item"/>
      <el-select
          v-model="query.data.position"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.position">
        <el-option
            v-for="item in config.companyPositionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select
          v-model="query.data.detailed"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.detailed">
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
        idKey="computerId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="includes(roleCodeList, 'admin') ? handleDelete : null"
        :page="query.page"
        :total="total"
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
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="brand" :label="store.state.label.brand">
          <el-input v-model="formData.brand"/>
        </el-form-item>
        <el-form-item prop="computerName" :label="store.state.label.computerName">
          <el-select v-model="formData.computerName"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.computerName"
          >
            <el-option
                v-for="item in config.computerNameList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="model" :label="store.state.label.computerModel">
          <el-input v-model="formData.model"/>
        </el-form-item>
        <el-form-item prop="userId" :label="store.state.label.user">
          <el-select v-model="formData.userId"
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
        </el-form-item>
        <el-form-item prop="position" :label="store.state.label.position">
          <el-select v-model="formData.position"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.position"
          >
            <el-option
                v-for="item in config.companyPositionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="storageDate" :label="store.state.label.storageDate">
          <el-date-picker
              type="date"
              v-model="formData.storageDate"
              format="YYYY-MM-DD"
              @change="formData.storageDate = formatDate(formData.storageDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="computerState" :label="store.state.label.computerState">
          <el-select v-model="formData.computerState"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.computerState"
          >
            <el-option
                v-for="item in config.computerStateList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="detailed" :label="store.state.label.detailed">
          <el-select
              v-model="formData.detailed"
              filterable
              allow-create
              clearable
              :placeholder="store.state.label.detailed">
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
        <el-form-item prop="productPlace" :label="store.state.label.productPlace">
          <el-input v-model="formData.productPlace"/>
        </el-form-item>
        <el-form-item prop="supplier" :label="store.state.label.supplier">
          <el-input v-model="formData.supplier"/>
        </el-form-item>
        <el-form-item prop="remark" :label="store.state.label.remark">
          <el-input v-model="formData.remark"/>
        </el-form-item>
        <el-form-item prop="photoList" :label="store.state.label.photo">
          <image-upload :photoList="formData.photoList" :maxSize="3"></image-upload>
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
        :append-to-body="true"
        :lock-scroll="false"
        modal-class="print-drawer"
        :z-index="99999"
    >
      <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; margin-bottom: 20px;">
        <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 585px;">
          <h1 style="text-align: center;height: 50px; position: relative;width: 100%;">
            <img :src="fullUrl('/third/img/douson.png', '')" style="position: absolute; height: 100%;left: 10px;top: 0px;">
            <span style="font-size: 36px; color: #ff0000;">ASSET CARD 财产卡</span>
            <br>
          </h1>
          <el-descriptions
              :column="1"
              style="width: 100%;"
              border
          >`
            <el-descriptions-item
                :label="store.state.label.materialNo"
                align="center"
                label-class-name="box-print-label"
            >
              <template #label>
                Mã thiết bị 设备物料号
              </template>
              {{ printData['materialNo'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.chineseName"
                align="center"
                label-class-name="box-print-label"
            >
              <template #label>
                Người Sử Dụng 用户
              </template>
              {{ printData['userChineseName'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.position"
                align="center"
                label-class-name="box-print-label"
            >
              <template #label>
                Vị trí 位置
              </template>
              {{ printData['positionFormat'] }}
            </el-descriptions-item>
            <el-descriptions-item
                :label="store.state.label.department"
                align="center"
                label-class-name="box-print-label"
            >
              <template #label>
                Bộ Phận 部门
              </template>
              {{ printData['departmentFormat'] }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Plus, Printer, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'
import ImageUpload from '../../component/ImageUpload.vue'
import {fullUrl} from '@/util/EnvUtil'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const showPrint = ref<boolean>(false)
const printData = ref<any>(null)
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
    width: 312,
    type: ValueType.Operator,
  },
  {value: 'index', labelKey: 'index', width: 51},
  {value: 'photoCount', labelKey: 'photoCount', width: 57},
  {value: 'materialNo', labelKey: 'materialNo', width: 189},
  {value: 'brand', labelKey: 'brand', width: 96},
  {value: 'computerNameFormat', labelKey: 'computerName', width: 189},
  {value: 'model', labelKey: 'computerModel', width: 293, showOverflow: true,},
  // {value: 'username', labelKey: 'username', width: 96},
  {value: 'departmentFormat', labelKey: 'department', width: 168},
  {value: 'userChineseName', labelKey: 'chineseName', width: 190},
  {value: 'professionFormat', labelKey: 'profession', width: 268},
  {value: 'positionFormat', labelKey: 'position', width: 96},
  {value: 'storageDate', labelKey: 'storageDate', width: 102},
  {value: 'computerStateFormat', labelKey: 'computerState', width: 168},
  {value: 'detailedFormat', labelKey: 'detailed', width: 96},
  {value: 'productPlace', labelKey: 'productPlace', width: 96},
  {value: 'supplier', labelKey: 'supplier', width: 96},
  {value: 'remark', labelKey: 'remark', width: 169, showOverflow: true,},
  {value: 'photoList', labelKey: 'photo', type: ValueType.Image, width: 96},
])
if (!includes(roleCodeList, 'admin') && !includes(roleCodeList, 'manager')) {
  columnConfigList.value = columnConfigList.value.map(t => {
    if ('dealOpinion' === t.value) {
      t.children = (t.children || []).filter(t1 => 'fineAmountFormat' !== t1.value)
    }
    return t
  })
}
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
const defaultFormData = {
  creator: user.userId,
  index: '',
  computerId: '',
  materialNo: '',
  brand: '',
  computerName: '',
  model: '',
  userId: '',
  position: '',
  storageDate: '',
  computerState: '',
  detailed: null,
  productPlace: '',
  supplier: '',
  remark: '',
  photoList: new Array<any>(),
}

const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      userId: '',
      startDueDate: '',
      endDueDate: '',
      deviceNumber: '',
      specification: '',
      chineseVietnamName: '',
      deviceCheckLedgerState: '',
      borrower: '',
      storage: '',
      outOfStock: null,
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
    computerNameList: [],
    companyPositionList: [],
    computerStateList: [],
  },
  formVisible: false,
  managerEdit: false,
  formRuleList: {
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    brand: [{required: true, message: 'Please check', trigger: 'blur'}],
    computerName: [{required: true, message: 'Please check', trigger: 'blur'}],
    model: [{required: true, message: 'Please check', trigger: 'blur'}],
    userId: [{required: true, message: 'Please check', trigger: 'blur'}],
    storageDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    computerState: [{required: true, message: 'Please check', trigger: 'blur'}],
    detailed: [{required: true, message: 'Please check', trigger: 'blur'}],
    productPlace: [{required: true, message: 'Please check', trigger: 'blur'}],
    photoList: [{required: false, type: 'array', min: 0, max: 3}],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startDueDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endDueDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startDueDate = ''
    state.query.data.endDueDate = ''
  }
  handlePage()
}

const handleShowPrintDetail = (d: any) => {
  // ElMessage.info('该功能正在开发中')
  printData.value = Object.assign({}, d.param || {})
  showPrint.value = true
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
const handleFormatValue = (key: string, val: any) => {
  // @ts-ignore
  const a = (state.config[key] || []).filter(t => t.value === val)
  return a.length > 0 ? a[0].label : val
}
httpGet('douson/config', {
  categoryIdList: [
    'computerName',
    'companyPosition',
    'computerState',
  ],
}).then(r => {
  state.config = r.data
})
const handlePage = () => {
  httpGet(`douson/admin/computer/page`, state.query).then(
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
  state.formData = Object.assign({}, defaultFormData)
  state.formVisible = true
  state.formSave = true
}
const handleEdit = (row: any, managerEdit: boolean = false) => {
  state.managerEdit = managerEdit
  state.formVisible = true
  state.formSave = false
  state.formData = Object.assign({}, row)
}
const handleEditShow = (row: any) => {
  if (includes(roleCodeList, 'admin') || includes(roleCodeList, 'itManager')) {
    return true
  } else {
    return row.creator === store.state.user.userId
  }
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPostJson('douson/admin/computer', state.formData).then(() => {
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
  return httpPutJson('douson/admin/computer', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/computer', {
      computerId: row.computerId,
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
  managerEdit,
  formRuleList,
  photoVisible,
  photoList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
