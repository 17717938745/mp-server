<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.templateOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.templateOrderNo"
                class="search-item"/>
      <el-select v-model="query.data.borrowTemplatePerson"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.borrowTemplatePerson"
                 class="search-item">
        <el-option
            v-for="item in userOptionList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.materialDescription"
                @change="handlePage"
                :placeholder="store.state.label.materialDescription"
                class="search-item"/>
      <el-select
          v-model="query.data.alreadyReturn"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.isReturn">
        <el-option
            label="Yes"
            :value="true"
        />
        <el-option
            label="No"
            :value="false"
        />
      </el-select>
      <el-select
          v-model="query.data.meetRequirement"
          @change="handlePage"
          clearable
          :placeholder="store.state.label.meetRequirement">
        <el-option
            label="Yes"
            :value="true"
        />
        <el-option
            label="No"
            :value="false"
        />
      </el-select>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start promise return date"
          end-placeholder="End promise return date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
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
        idKey="templateId"
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
        <el-form-item prop="borrowTemplateDate" :label="store.state.label.borrowTemplateDate">
          <el-date-picker
              type="date"
              v-model="formData.borrowTemplateDate"
              format="YYYY-MM-DD"
              @change="formData.borrowTemplateDate = formatDate(formData.borrowTemplateDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="borrowTemplatePerson" :label="store.state.label.borrowTemplatePerson">
          <el-select v-model="formData.borrowTemplatePerson"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.borrowTemplatePerson"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"/>
        </el-form-item>
        <el-form-item prop="materialDescription" :label="store.state.label.materialDescription">
          <el-input v-model="formData.materialDescription"/>
        </el-form-item>
        <el-form-item prop="templateCount" :label="store.state.label.templateCount">
          <el-input-number v-model="formData.templateCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="promiseReturnDate" :label="store.state.label.promiseReturnDate">
          <el-date-picker
              type="date"
              v-model="formData.promiseReturnDate"
              format="YYYY-MM-DD"
              @change="formData.promiseReturnDate = formatDate(formData.promiseReturnDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="operatorPerson" :label="store.state.label.operatorPerson">
          <el-select v-model="formData.operatorPerson"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.operatorPerson"
          >
            <el-option
                v-for="item in userOptionList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input type="textarea" :rows=4 v-model="formData.description"/>
        </el-form-item>
        <el-form-item prop="returnCount" :label="store.state.label.returnCount">
          <el-input-number v-model="formData.returnCount" style="width: 60px;" :controls="false" :min="0" :disabled="!formSave && user.userId !== 'admin'"/>
        </el-form-item>
        <el-form-item prop="actualReturnDate" :label="store.state.label.actualReturnDate">
          <el-date-picker
              type="date"
              v-model="formData.actualReturnDate"
              format="YYYY-MM-DD"
              @change="formData.actualReturnDate = formatDate(formData.actualReturnDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="borrowPhotoList" :label="`${store.state.label.borrowPhoto}(${(formRuleList['borrowPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.borrowPhotoList" :maxSize="Number(`${(formRuleList['borrowPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <el-form-item prop="returnPhotoList" :label="`${store.state.label.returnPhoto}(${(formRuleList['returnPhotoList'] || []).reduce((p:any, t:any) => `Min: ${t.min}, Max: ${t.max}`, 'Unlimited')})`">
          <image-upload :photoList="formData.returnPhotoList" :maxSize="Number(`${(formRuleList['returnPhotoList'] || []).reduce((p:any, t:any) => t.max, 999)}`)"></image-upload>
        </el-form-item>
        <!--        <el-form-item prop="meetRequirement" :label="store.state.label.meetRequirement">
                  <el-select
                      v-model="formData.meetRequirement"
                      filterable
                      allow-create
                      clearable
                      :placeholder="store.state.label.meetRequirement">
                    <el-option
                        label="Yes"
                        :value="true"
                    />
                    <el-option
                        label="No"
                        :value="false"
                    />
                  </el-select>
                </el-form-item>-->
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
          <h1 style="text-align: center;height: 90px; position: relative;width: 100%;">
            <img :src="fullUrl('/third/img/douson.png', '')" style="position: absolute; height: 50px;left: 10px;top: 20px;">
            <div style="font-size: 36px; color: #222222; ">物品借出单</div>
            <div style="font-size: 36px; color: #222222; ">Đơn cho mượn dụng cụ</div>
            <br>
          </h1>
          <div style="width: 100%; text-align: right; color: #ff0000;">单号 Số phiếu：{{ printData.templateOrderNo }}</div>
          <div style="width: 100%; font-size: 14px; margin-top: 20px;">
            <div class="print-center-content">
              <div class="print-left-section">
                <div style="width: 100%;">
                  <div>借用人签字</div>
                  <div>Người mượn：</div>
                </div>
                <div></div>
              </div>
              <div class="print-right-section">借用方(单位) Đơn vị：{{ printData.borrowTemplatePersonFormat }}</div>
            </div>
            <div class="print-center-content">
              <div class="print-left-section">借用时间 Ngày mượn：{{ printData.borrowTemplateDate }}</div>
              <div class="print-right-section">承诺归还时间 Ngày trả：{{ printData.promiseReturnDate }}</div>
            </div>
            <div class="print-center-content">
              <div class="print-left-section">
                <div class="print-item print-border" style="width: 80px;">
                  序号 STT
                </div>
                <div class="print-item" style="width: 80%;">
                  物料号 Mã vật liệu
                </div>
              </div>
              <div class="print-right-section">
                <div class="print-item print-border" style="width: 80%;">
                  物品名称 Miêu tả vật liệu
                </div>
                <div class="print-item" style="width: 120px;">
                  数量 Số lượng
                </div>
              </div>
            </div>
            <div class="print-center-content">
              <div class="print-left-section">
                <div class="print-item print-border" style="width: 80px;">
                  {{ printData.index }}
                </div>
                <div class="print-item" style="width: 80%;">
                  {{ printData.materialNo }}
                </div>
              </div>
              <div class="print-right-section">
                <div class="print-item print-border" style="width: 80%;">
                  {{ printData.materialDescription }}
                </div>
                <div class="print-item" style="width: 120px;">
                  {{ printData.templateCount }}
                </div>
              </div>
            </div>
            <div class="print-center-content">
              <div class="print-left-section">
                <div class="print-bottom-item">
                  <div>经办人签字</div>
                  <div>Người cho mượn：</div>
                </div>
                <div style="display: flex; justify-content: center; align-items: center;">{{ printData.operatorPersonFormat }}</div>
              </div>
              <div class="print-right-section">
                <div class="print-border" style="width: 80%;">
                  <div>部门负责人:</div>
                  <div>Người phụ trách bộ phận：</div>
                </div>
                <div style="width: 120px;">
                  <div>仓管签字:</div>
                  <div>Kho：</div>
                </div>
              </div>
            </div>
            <div class="print-center-content" style="justify-content: flex-start; flex-wrap: wrap;">
              <div style="width: 100%;">备注 Ghi chú：</div>
              <pre>{{ printData.description }}</pre>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType,} from '@/store'
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
import {fullUrl} from '@/util/EnvUtil'
import ImageUpload from "../../component/ImageUpload.vue";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const showPrint = ref<boolean>(false)
const printData = ref<any>(null)
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'expand', label: '', width: 48, type: ValueType.Expand,},
  {value: 'operator', labelKey: 'viewAndEdit', width: 312, type: ValueType.Operator,},
  {value: 'index', labelKey: 'index', width: 45},
  {value: 'borrowTemplateDate', labelKey: 'borrowTemplateDate', width: 89},
  {value: 'borrowTemplatePersonFormat', labelKey: 'borrowTemplatePerson', width: 132},
  {value: 'materialNo', labelKey: 'materialNo', width: 145},
  {value: 'materialDescription', labelKey: 'materialDescription', width: 189},
  {value: 'templateCount', labelKey: 'templateCount', width: 49},
  {value: 'promiseReturnDate', labelKey: 'promiseReturnDate', width: 89},
  {value: 'operatorPersonFormat', labelKey: 'operatorPerson', width: 132},
  {value: 'borrowPhotoCount', labelKey: 'borrowPhotoCount', width: 49},
  {value: 'returnPhotoCount', labelKey: 'returnPhotoCount', width: 49},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'returnCount', labelKey: 'returnCount', width: 49},
  {value: 'actualReturnDate', labelKey: 'actualReturnDate', width: 89},
  {value: 'meetRequirement', labelKey: 'meetRequirement', width: 59},
  {value: 'templateOrderNo', labelKey: 'templateOrderNo', width: 123},
  {value: 'borrowPhotoList', labelKey: 'borrowPhoto', width: 189, type: ValueType.Image,},
  {value: 'returnPhotoList', labelKey: 'returnPhoto', width: 189, type: ValueType.Image,},
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
  borrowTemplateDate: formatDate(new Date(), 'yyyy-MM-dd'),
  borrowTemplatePerson: '',
  materialNo: '',
  materialDescription: '',
  templateCount: '',
  promiseReturnDate: '',
  operatorPerson: '',
  borrowPhotoCount: 0,
  returnPhotoCount: 0,
  description: '',
  returnCount: 0,
  actualReturnDate: '',
  meetRequirement: '',
  templateOrderNo: '',
  borrowPhotoList: new Array<any>(),
  returnPhotoList: new Array<any>(),
}

const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      templateOrderNo: '',
      borrowTemplatePerson: '',
      materialNo: '',
      materialDescription: '',
      alreadyReturn: null,
      meetRequirement: null,
      startPromiseReturnDate: '',
      endPromiseReturnDate: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: DEFAULT_LIMIT,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  config: {},
  formSave: true,
  formVisible: false,
  managerEdit: false,
  formRuleList: {
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    borrowPhotoList: [{required: false, type: 'array', min: 0, max: 6}],
    returnPhotoList: [{required: false, type: 'array', min: 0, max: 6}],
  },
})

const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startPromiseReturnDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endPromiseReturnDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startPromiseReturnDate = ''
    state.query.data.endPromiseReturnDate = ''
  }
  handlePage()
}

const handleShowPrintDetail = (d: any) => {
  // ElMessage.info('该功能正在开发中')
  printData.value = Object.assign({}, d.param || {})
  showPrint.value = true
}
const handlePage = () => {
  httpGet(`douson/admin/template/page`, state.query).then(
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
        httpPostJson('douson/admin/template', state.formData).then(() => {
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
  return httpPutJson('douson/admin/template', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/admin/template', {
      templateId: row.templateId,
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
.print-center-content {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid #ddd;
  padding: 5px;
}

.print-left-section {
  width: 40%;
  display: flex;
  height: 100%;
  border-right: 1px solid #ddd;
}


.print-border {
  border-right: 1px solid #ddd;
}



.print-right-section {
  width: 60%;
  display: flex;
  align-items: center;
}

.print-item {
  height: 100%;
  text-align: center;
}
</style>
