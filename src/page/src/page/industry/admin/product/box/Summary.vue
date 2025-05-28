<template>
  <div>
    <div class="query-container">
      <el-input v-model="query.data.orderNo"
                @change="handleList"
                :placeholder="store.state.label.orderNo"
                class="search-item"/>
      <el-date-picker
          v-model="dateTimeList"
          @change="handleDateTimeChange"
          type="daterange"
          format="YYYY-MM-DD"
          range-separator="-"
          start-placeholder="Start date"
          end-placeholder="End date"
          style="width: 180px; margin-right: 20px;"
      >
      </el-date-picker>
      <el-input v-model="query.data.saleOrderNo"
                @change="handleList"
                :placeholder="store.state.label.saleOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.orderProject"
                @change="handleList"
                :placeholder="store.state.label.orderProject"
                class="search-item"/>
      <el-input v-model="query.data.materialNo"
                @change="handleList"
                :placeholder="store.state.label.materialNo"
                class="search-item"/>
      <el-select v-model="query.data.customerShortName"
                 @change="handleList"
                 filterable
                 allow-create
                 clearable
                 :placeholder="store.state.label.customerShortName"
                 class="search-item">
        <el-option
            v-for="item in config.customerShortNameList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="query.data.purchaseOrderNo"
                @change="handleList"
                :placeholder="store.state.label.purchaseOrderNo"
                class="search-item"/>
      <el-input v-model="query.data.poProject"
                @change="handleList"
                :placeholder="store.state.label.poProject"
                class="search-item"/>
      <el-select
          v-model="query.data.alreadySend"
          @change="handleList"
          clearable
          placeholder="Already send">
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
        <el-button
            :icon="Plus"
            v-if="includes(roleCodeList, 'admin') || includes(roleCodeList, 'box')"
            @click="handleSaveModal"
            type="success"
        >Add
        </el-button>
      </div>
    </div>
    <view-list
        idKey="assemblyId"
        :columnConfigList="columnConfigList"
        :list="tableData"
    >
      <template #operator="row">
      </template>
    </view-list>
  </div>
</template>

<script lang="tsx" setup>
import {reactive, Ref, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {ElMessage} from 'element-plus'
import {Plus, Search,} from '@element-plus/icons-vue'
import {useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DEFAULT_LIMIT, DEFAULT_PAGE,} from '@/typing/Common'
import {ViewConfig} from '@/typing/industry/ViewItem'
import ViewList from '../../../component/ViewList.vue'
import {ListResult, PageResult} from '../../../../../typing/ma/System'
import {formatDate, getMonthStart, getMonthEnd} from '@/util/DateUtil'
import {includes} from "@/util/ArrayUtil";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const formRef: Ref = ref(null)
const userOptionList = ref(new Array<any>())
const columnConfigList = ref<ViewConfig[]>([
  {value: 'customerShortNameFormat', label: '客户简称 Tên khách hàng', width: 213},
  {value: 'sumEachBoxCount', label: 'Sum of 每箱数量 Số lượng mỗi thùng', width: 167},
  {value: 'sumBoxNumber', label: 'Count of 箱号 Mã thùng', width: 108},
  {value: 'sumEachBoxWeight', label: 'Sum of 每箱重量 Trọng lượng mỗi thùng', width: 213},
])
const state = reactive({
  dateTimeList: [getMonthStart(), getMonthEnd()],
  userConfigList: new Array<any>(),
  query: {
    data: {
      orderNo: '',
      saleOrderNo: '',
      orderProject: '',
      poProject: '',
      customerShortName: '',
      purchaseOrderNo: '',
      materialNo: '',
      alreadySend: false,
      startDate: '',
      endDate: '',
    },
  },
  tableData: new Array<any>(),
  config: {
    assemblyTypeList: [],
    departmentList: [],
  },
})
state.dateTimeList = []
const handleDateTimeChange = () => {
  if (state.dateTimeList && state.dateTimeList.length > 1) {
    state.query.data.startAssemblyCompleteDate = formatDate(
        state.dateTimeList[0],
        'yyyy-MM-dd'
    );
    state.query.data.endAssemblyCompleteDate = formatDate(
        state.dateTimeList[1],
        'yyyy-MM-dd'
    );
  } else {
    state.query.data.startAssemblyCompleteDate = ''
    state.query.data.endAssemblyCompleteDate = ''
  }
  handleList()
}
const handleList = () => {
  httpGet(`douson/box/summary-list`, state.query.data).then(
      (res: ListResult<typeof state.tableData>) => {
        state.tableData = res.list
        ElMessage.success("Query success")
      }
  )
}
handleList()
const {
  query,
  tableData,
  config,
  userConfigList,
  dateTimeList,
} = {
  ...toRefs(state),
};

</script>

<style scoped lang="scss">
</style>
