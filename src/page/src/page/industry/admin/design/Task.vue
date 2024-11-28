<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.customerShortName"
                @change="handlePage"
                :placeholder="store.state.label.customerShortName"
                class="search-item"/>
      <el-input v-model="query.data.customerOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.customerOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.customerProjectSequence"
                @change="handlePage"
                :placeholder="store.state.label.customerProjectSequence"
                class="search-item"/>
      <el-input v-model="query.data.saleOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProjectNo"
                @change="handlePage"
                :placeholder="store.state.label.orderProjectNo"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handlePage"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-input v-model="query.data.designNumber"
                @change="handlePage"
                :placeholder="store.state.label.designNumber"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start promise done date"
          end-placeholder="End promise done date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-select v-model="query.data.surplusCountType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.surplusCount"
                 class="search-item">
        <el-option
            v-for="item in [
                {
                  value: 0,
                  label: '=0',
                },
                {
                  value: 1,
                  label: '>0',
                },
                {
                  value: -1,
                  label: '<0',
                },
                {
                  value: 2,
                  label: '!=0',
                },
            ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.remainCountType"
                 @change="handlePage"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.materialCount + '-' +  store.state.label.productionCount"
                 class="search-item">
        <el-option
            v-for="item in [
                {
                  value: 0,
                  label: '=0',
                },
                {
                  value: 1,
                  label: '>0',
                },
                {
                  value: -1,
                  label: '<0',
                },
                {
                  value: 2,
                  label: '!=0',
                },
            ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.chargeCompany"
                @change="handlePage"
                :placeholder="store.state.label.chargeCompany"
                class="search-item"/>
      <el-select v-model="query.data.nde"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.nde"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.assemble"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.assemble"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.testPress"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.testPress"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-select v-model="query.data.surfaceTreatment"
                 filterable
                 allow-create
                 clearable
                 @change="handlePage"
                 :placeholder="store.state.label.surfaceTreatment"
      >
        <el-option
            v-for="item in checkOrNotList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.checkOrderNo"
                @change="handlePage"
                :placeholder="store.state.label.checkOrderNo"
                class="search-item"/>
      <div class="query-btn">
        <el-button :icon="Search" @click="handlePage" type="primary">Search</el-button>
        <el-button
            :icon="Plus"
            @click="handleSaveModal"
            type="success"
            :disabled="!includes(roleCodeList, 'material')"
        >Add
        </el-button>
      </div>
    </div>
    <div>
      <el-space wrap>
        <el-switch v-model="showMore" active-text="Show more" inactive-text="Hide info" @change="handleToggleMore"/>
      </el-space>
    </div>
    <view-list
        idKey="taskId"
        :columnConfigList="columnConfigList"
        :list="tableData"
        :handleEdit="handleEdit"
        :handleUpdate="handleUpdate"
        :handleEditShow="handleEditShow"
        :handleDelete="includes(roleCodeList, 'admin') ? handleDelete : null"
        :page="query.page"
        :total="total"
        :handleTableRowClassName="handleTableRowClassName"
        :handlePageChange="handlePageChange"
        :handleLimitChange="handleLimitChange"
        :detailLink="false"
    >
    </view-list>
    <el-dialog :title="formSave ? 'Add' : 'Edit'" v-model="formVisible" width="60%" :close-on-click-modal="false">
      <el-form
          :rules="formRuleList"
          :model="formData"
          ref="formRef"
          label-width="auto"
          label-position="top"
      >
        <el-form-item prop="testDevice" :label="store.state.label.testDevice">
          <el-select v-model="formData.testDevice"
                     filterable
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
        </el-form-item>
        <el-form-item prop="planReformCount" :label="store.state.label.planReformCount">
          <el-input-number v-model="formData.planReformCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="supplierRemark" :label="store.state.label.supplierRemark">
          <el-input v-model="formData.supplierRemark" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item prop="productCountHour8" :label="store.state.label.productCountHour8">
          <el-input-number v-model="formData.productCountHour8" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="productCountHour12" :label="store.state.label.productCountHour12">
          <el-input-number v-model="formData.productCountHour12" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="processWorkingHour" :label="store.state.label.processWorkingHour">
          <el-input-number v-model="formData.processWorkingHour" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="onlineDate" :label="store.state.label.onlineDate">
          <el-date-picker
              type="date"
              v-model="formData.onlineDate"
              format="YYYY-MM-DD"
              @change="formData.onlineDate = formatDate(formData.onlineDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="offlineDate" :label="store.state.label.offlineDate">
          <el-date-picker
              type="date"
              v-model="formData.offlineDate"
              format="YYYY-MM-DD"
              @change="formData.offlineDate = formatDate(formData.offlineDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="delay" :label="store.state.label.delay">
          <el-select
              v-model="formData.delay"
              filterable
              allow-create
              clearable
              :placeholder="store.state.label.delay">
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
        <el-form-item prop="processCount" :label="store.state.label.processCount">
          <el-input-number v-model="formData.processCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="processProcedure" :label="store.state.label.processProcedure">
          <el-select v-model="formData.processProcedure"
                     filterable
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
        </el-form-item>
        <el-form-item prop="surplus" :label="store.state.label.surplus">
          <el-input-number v-model="formData.surplus" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="supplierDoneDate" :label="store.state.label.supplierDoneDate">
          <el-date-picker
              type="date"
              v-model="formData.supplierDoneDate"
              format="YYYY-MM-DD"
              @change="formData.supplierDoneDate = formatDate(formData.supplierDoneDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="deliverCount" :label="store.state.label.deliverCount">
          <el-input-number v-model="formData.deliverCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="deliverDate" :label="store.state.label.deliverDate">
          <el-date-picker
              type="date"
              v-model="formData.deliverDate"
              format="YYYY-MM-DD"
              @change="formData.deliverDate = formatDate(formData.deliverDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="receiptCount" :label="store.state.label.receiptCount">
          <el-input-number v-model="formData.receiptCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="receiptDate" :label="store.state.label.receiptDate">
          <el-date-picker
              type="date"
              v-model="formData.receiptDate"
              format="YYYY-MM-DD"
              @change="formData.receiptDate = formatDate(formData.receiptDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="scrapCount" :label="store.state.label.scrapCount">
          <el-input-number v-model="formData.scrapCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="supplierPromiseDoneDate" :label="store.state.label.supplierPromiseDoneDate">
          <el-date-picker
              type="date"
              v-model="formData.supplierPromiseDoneDate"
              format="YYYY-MM-DD"
              @change="formData.supplierPromiseDoneDate = formatDate(formData.supplierPromiseDoneDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item prop="customerShortName" :label="store.state.label.customerShortName">
          <el-input v-model="formData.customerShortName"/>
        </el-form-item>
        <el-form-item prop="customerOrderNo" :label="store.state.label.customerOrderNo">
          <el-input v-model="formData.customerOrderNo"
                    @change="formData.customerOrderNo = (formData.customerOrderNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="customerProjectSequence" :label="store.state.label.customerProjectSequence">
          <el-input v-model="formData.customerProjectSequence"/>
        </el-form-item>
        <el-form-item prop="saleOrderNo" :label="store.state.label.saleOrderNo">
          <el-input v-model="formData.saleOrderNo"
                    @change="formData.saleOrderNo = (formData.saleOrderNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="orderProjectNo" :label="store.state.label.orderProjectNo">
          <el-input v-model="formData.orderProjectNo"/>
        </el-form-item>
        <el-form-item prop="materialNo" :label="store.state.label.materialNo">
          <el-input v-model="formData.materialNo"
                    @change="formData.materialNo = (formData.materialNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="improveMaterialDescribe" :label="store.state.label.improveMaterialDescribe">
          <el-input v-model="formData.improveMaterialDescribe" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item prop="designNumber" :label="store.state.label.designNumber">
          <el-input v-model="formData.designNumber"
                    @change="formData.designNumber = (formData.designNumber || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="orderCount" :label="store.state.label.orderCount">
          <el-input-number v-model="formData.orderCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="productionDate" :label="store.state.label.productionDate">
          <el-date-picker
              type="date"
              v-model="formData.productionDate"
              format="YYYY-MM-DD"
              @change="formData.productionDate = formatDate(formData.productionDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="promiseDoneDate" :label="store.state.label.promiseDoneDate">
          <el-date-picker
              type="date"
              v-model="formData.promiseDoneDate"
              format="YYYY-MM-DD"
              @change="formData.promiseDoneDate = formatDate(formData.promiseDoneDate, 'yyyy-MM-dd')"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="blankMaterialNo" :label="store.state.label.blankMaterialNo">
          <el-input v-model="formData.blankMaterialNo"
                    @change="formData.blankMaterialNo = (formData.blankMaterialNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="blankMaterialDescribe" :label="store.state.label.blankMaterialDescribe">
          <el-input v-model="formData.blankMaterialDescribe"/>
        </el-form-item>
        <el-form-item prop="roughcastDesignNumber" :label="store.state.label.roughcastDesignNumber">
          <el-input v-model="formData.roughcastDesignNumber"
                    @change="formData.roughcastDesignNumber = (formData.roughcastDesignNumber || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="materialCount" :label="store.state.label.materialCount">
          <el-input-number v-model="formData.materialCount" style="width: 60px;" :controls="false" :min="0"/>
        </el-form-item>
        <el-form-item prop="stoveNo" :label="store.state.label.stoveNo">
          <el-input v-model="formData.stoveNo" @change="formData.stoveNo = (formData.stoveNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="hotBatchNo" :label="store.state.label.hotBatchNo">
          <el-input v-model="formData.hotBatchNo"
                    @change="formData.hotBatchNo = (formData.hotBatchNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="serialNo" :label="store.state.label.serialNo">
          <el-input v-model="formData.serialNo" @change="formData.serialNo = (formData.serialNo || '').toUpperCase()"/>
        </el-form-item>
        <el-form-item prop="nde" :label="store.state.label.nde">
          <el-select v-model="formData.nde"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.nde"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="assemble" :label="store.state.label.assemble">
          <el-select v-model="formData.assemble"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.assemble"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="testPress" :label="store.state.label.testPress">
          <el-select v-model="formData.testPress"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.testPress"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="surfaceTreatment" :label="store.state.label.surfaceTreatment">
          <el-select v-model="formData.surfaceTreatment"
                     filterable
                     allow-create
                     clearable
                     :placeholder="store.state.label.surfaceTreatment"
          >
            <el-option
                v-for="item in checkOrNotList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="chargeCompany" :label="store.state.label.chargeCompany">
          <el-input v-model="formData.chargeCompany"/>
        </el-form-item>
        <el-form-item prop="description" :label="store.state.label.description">
          <el-input v-model="formData.description" type="textarea" :rows="4"/>
        </el-form-item>
        <el-form-item prop="productionCount" :label="store.state.label.productionCount">
          <el-input-number v-model="formData.productionCount" style="width: 60px;" :controls="false" :min="0"
                           :disabled="!includes(roleCodeList, 'materialManager') && 'admin' !== user.username"/>
        </el-form-item>
        <el-form-item prop="arrangeProductionDate" :label="store.state.label.arrangeProductionDate">
          <el-date-picker
              type="date"
              v-model="formData.arrangeProductionDate"
              format="YYYY-MM-DD"
              @change="formData.arrangeProductionDate = formatDate(formData.arrangeProductionDate, 'yyyy-MM-dd')"
              :disabled="!includes(roleCodeList, 'materialManager') && 'admin' !== user.username"
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

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType,} from '@/store'
import {ElMessage, ElMessageBox, UploadFile, UploadFiles} from 'element-plus'
import {Plus, Printer, Search, UploadFilled,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpDelete, httpGet, httpPostJson, httpPutJson, httpUpload,} from '@/util/HttpUtil'
import {ListResult, PageResult} from '@/typing/ma/System'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {formatDate} from '@/util/DateUtil'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../component/ViewList.vue'
import {includes} from '@/util/ArrayUtil'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'operator', labelKey: 'viewAndEdit', width: 235, type: ValueType.Operator,},
  {value: 'device', labelKey: 'device', width: 189},
  {value: 'customerShortName', labelKey: 'customerShortName', width: 189},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 189},
  {value: 'orderProjectNo', labelKey: 'orderProjectNo', width: 189},
  {value: 'materialNo', labelKey: 'materialNo', width: 189},
  {value: 'improveMaterialDescribe', labelKey: 'improveMaterialDescribe', width: 189},
  {value: 'designNumber', labelKey: 'designNumber', width: 189},
  {value: 'orderCount', labelKey: 'orderCount', width: 189},
  {value: 'roughcastExpireDate', labelKey: 'roughcastExpireDate', width: 189},
  {value: 'materialCount', labelKey: 'materialCount', width: 189},
  {value: 'promiseDoneDate', labelKey: 'promiseDoneDate', width: 189},
  {value: 'planReformCount', labelKey: 'planReformCount', width: 189},
  {value: 'supplierRemark', labelKey: 'supplierRemark', width: 189},
  {value: 'productCountHour8', labelKey: 'productCountHour8', width: 189},
  {value: 'productCountHour12', labelKey: 'productCountHour12', width: 189},
  {value: 'processWorkingHour', labelKey: 'processWorkingHour', width: 189},
  {value: 'onlineDate', labelKey: 'onlineDate', width: 189},
  {value: 'offlineDate', labelKey: 'offlineDate', width: 189},
  {value: 'delay', labelKey: 'delay', width: 189},
  {value: 'processCount', labelKey: 'processCount', width: 189},
  {value: 'processProcedure', labelKey: 'processProcedure', width: 189},
  {value: 'supplierDoneDate', labelKey: 'supplierDoneDate', width: 189},
  {value: 'deliverCount', labelKey: 'deliverCount', width: 189},
  {value: 'deliverDate', labelKey: 'deliverDate', width: 189},
  {value: 'receiptCount', labelKey: 'receiptCount', width: 189},
  {value: 'receiptDate', labelKey: 'receiptDate', width: 189},
  {value: 'scrapCount', labelKey: 'scrapCount', width: 189},
  {value: 'supplierPromiseDoneDate', labelKey: 'supplierPromiseDoneDate', width: 189},
  {value: 'nde', labelKey: 'nde', width: 189},
  {value: 'assemble', labelKey: 'assemble', width: 189},
  {value: 'testPress', labelKey: 'testPress', width: 189},
  {value: 'surfaceTreatment', labelKey: 'surfaceTreatment', width: 189},
  {value: 'surplus', labelKey: 'surplus', width: 189},
  {
    value: 'materialOrderNoFormat',
    labelKey: 'materialOrderNo',
    width: 146,
    mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],
    type: ValueType.Link,
    openLink: (d: any) => {
      window.open(`/industry/public/material/index?materialOrderNo=${d.materialOrderNo}`);
    },
  },
  {
    value: 'checkOrderNoFormat',
    labelKey: 'checkOrderNo',
    width: 146,
    mergeKey: ['saleOrderNo', 'orderProjectNo', 'productionDate'],
    type: ValueType.Link,
    openLink: (d: any) => {
      window.open(`/industry/public/material/check?checkOrderNo=${d.checkOrderNo}`);
    },
  },
])
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
  device: '',
  testDevice: '',
  customerShortName: '',
  saleOrderNo: '',
  orderProject: '',
  materialNo: '',
  materialDescription: '',
  designNumber: '',
  orderCount: '',
  roughcastExpireDate: '',
  materialCount: '',
  promiseDoneDate: '',
  planReformCount: '',
  supplierRemark: '',
  productCountHour8: '',
  productCountHour12: '',
  processWorkingHour: '',
  onlineDate: '',
  offlineDate: '',
  delay: 0,
  processCount: 0,
  processProcedure: '',
  nde: '',
  assemble: '',
  testPress: '',
  surfaceTreatment: '',
  surplus: 0,
  materialOrderNo: '',
  checkOrderNo: '',
  supplierDoneDate: '',
  deliverCount: 0,
  deliverDate: '',
  receiptCount: 0,
  receiptDate: formatDate(new Date(), 'yyyy-MM-dd'),
  scrapCount: 0,
  supplierPromiseDoneDate: '',
}
const checkOrNotList = ref([
  {
    value: '√',
    label: '√',
  },
])
const state = reactive({
  dateTimeList: [],
  photoVisible: false,
  photoList: new Array<any>(),
  userConfigList: new Array<any>(),
  expandRowKeys: new Array<string>(),
  query: {
    data: {
      customerShortName: '',
      customerOrderNo: '',
      customerProjectSequence: '',
      saleOrderNo: '',
      orderProjectNo: '',
      materialNo: '',
      designNumber: '',
      surplusCountType: null,
      remainCountType: includes(roleCodeList, 'materialManager') ? 2 : null,
      chargeCompany: '',
      nde: '',
      assemble: '',
      testPress: '',
      surfaceTreatment: '',
      checkOrderNo: '',
      startPromiseDoneDate: '',
      endPromiseDoneDate: '',
    },
    page: {
      page: DEFAULT_PAGE,
      limit: 20,
    },
  },
  tableData: new Array<any>(),
  total: 0,
  formData: Object.assign({}, defaultFormData),
  config: {},
  formSave: true,
  formVisible: false,
  formRuleList: {
    customerShortName: [{required: true, message: 'Please check', trigger: 'blur'}],
    customerOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    customerProjectSequence: [{required: true, message: 'Please check', trigger: 'blur'}],
    saleOrderNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderProjectNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    improveMaterialDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    designNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    orderCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    productionDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    promiseDoneDate: [{required: true, message: 'Please check', trigger: 'blur'}],
    blankMaterialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    blankMaterialDescribe: [{required: true, message: 'Please check', trigger: 'blur'}],
    roughcastDesignNumber: [{required: true, message: 'Please check', trigger: 'blur'}],
    materialCount: [{required: true, message: 'Please check', trigger: 'blur'}],
    stoveNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    hotBatchNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    serialNo: [{required: true, message: 'Please check', trigger: 'blur'}],
    surplusCount: [{required: true, message: 'Please check', trigger: 'blur'}],
  },
})
const toggleKeyList = [
  'customerOrderNo',
  'customerProjectSequence',
  'blankMaterialNo',
  'blankMaterialDescribe',
  'roughcastDesignNumber',
  'stoveNo',
  'hotBatchNo',
  'serialNo',
  'materialOrderNo',
  'checkOrderNo',
]
const showMore = ref(true)
const handleToggleMore = (v) => {
  columnConfigList.value = columnConfigList.value.map(t => {
    if (toggleKeyList.indexOf(t.value) >= 0) {
      t.hide = !v
    }
    return t
  })
}
handleToggleMore(showMore.value)
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startPromiseDoneDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd hh:mm:ss'
    );
    state.query.data.endPromiseDoneDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd hh:mm:ss'
    );
  } else {
    state.query.data.startPromiseDoneDate = ''
    state.query.data.endPromiseDoneDate = ''
  }
  handlePage()
}

const handlePage = () => {
  httpGet(`douson/task/page`, state.query).then(
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

const supplierManagerColumnValueList = ['operator', 'customerShortName', 'saleOrderNo', 'orderProjectNo', 'materialNo', 'improveMaterialDescribe', 'designNumber', 'orderCount', 'roughcastExpireDate', 'materialCount', 'promiseDoneDate', 'planReformCount', 'supplierRemark', 'productCountHour8', 'productCountHour12', 'processWorkingHour', 'onlineDate', 'offlineDate', 'delay', 'processCount', 'processProcedure', 'nde', 'assemble', 'testPress', 'surfaceTreatment', 'surplus', 'materialOrderNoFormat', 'checkOrderNoFormat']
const materialManagerColumnValueList = ['operator', 'customerShortName', 'saleOrderNo', 'orderProjectNo', 'materialNo', 'improveMaterialDescribe', 'designNumber', 'orderCount', 'roughcastExpireDate', 'materialCount', 'promiseDoneDate', 'supplierRemark', 'supplierDoneDate', 'deliverCount', 'deliverDate', 'receiptCount', 'receiptDate', 'scrapCount', 'supplierPromiseDoneDate', 'nde', 'assemble', 'testPress', 'surfaceTreatment', 'surplus', 'materialOrderNoFormat', 'checkOrderNoFormat']
if (!includes(roleCodeList, 'admin') && !includes(roleCodeList, 'taskView')) {
  if (includes(roleCodeList, 'supplierManager') && !includes(roleCodeList, 'materialManager')) {
    columnConfigList.value = supplierManagerColumnValueList.map(k => columnConfigList.value.filter(t => k === t.value)[0])
  } else if (includes(roleCodeList, 'materialManager') && !includes(roleCodeList, 'supplierManager')) {
    columnConfigList.value = materialManagerColumnValueList.map(k => columnConfigList.value.filter(t => k === t.value)[0])
  }
}
if (user.username === 'admin' || includes(roleCodeList, 'materialManager')) {
  columnConfigList.value = columnConfigList.value.map(t => {
    if ('description' === t.value) {
      t.type = ValueType.TextEdit
    } else if ('chargeCompany' === t.value) {
      t.type = ValueType.TextEdit
    } else if ('productionCount' === t.value) {
      t.width = 168
      t.type = ValueType.NumberEdit
    }
    return t
  })
}
handlePage()
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
const handleEditShow = (row: any) => {
  if (includes(roleCodeList, 'admin') || includes(roleCodeList, 'material') || includes(roleCodeList, 'materialManager')) {
    return true
  } else {
    return false
  }
}
const handleMerge = () => {
  formRef.value.validate((valid: any) => {
    if (valid) {
      if (state.formSave) {
        httpPutJson('douson/task/merge', state.formData).then(() => {
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
  return httpPutJson('douson/task/merge', row).then(() => {
    state.formVisible = false
    ElMessage.success('Update success')
    handlePage()
  })
}
const handleDelete = (row: any) => {
  ElMessageBox.confirm('Confirm Delete?', 'Tips', {
    type: 'warning',
  }).then(() => {
    httpDelete('douson/task', {
      materialId: row.materialId,
    })
    .then(() => {
      ElMessage.success('Delete success')
      handlePage()
    })
  })
}
Promise.all([
  httpGet('douson/config', {
    categoryIdList: [
      'device',
      'processProcedure',
    ]
  }),
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  state.config = l[0].data || {}
  // userOptionList.value = (l[1].list || []).map((t: any) => {
  //   return {
  //     value: t.userId,
  //     label: t.name,
  //   }
  // })
  // handlePage()
})
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
  formRuleList,
  photoList,
} = {
  ...toRefs(state),
}

const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.productionCount === row.materialCount) {
    return 'row-done'
  } else if (row.surplusCount < 0) {
    return 'row-error'
  }
  return ''
}
</script>

<style scoped lang="scss">
</style>
