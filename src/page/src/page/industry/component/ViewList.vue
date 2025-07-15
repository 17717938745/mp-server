<template>
  <div v-show="props.showControl">
    <el-space wrap>
      <el-switch v-model="pcModel" active-text="PC model" inactive-text="Mobile model"/>
      <el-input-number v-if="pcModel" v-model="scale" :min="0.2" :max="1" :step="0.05" step-strictly style="margin-left: 20px;" @change="handleScaleChange"/>
    </el-space>
  </div>
  <div v-if="groupList && groupList.length > 0">
    <el-checkbox-group v-model="checkedGroupList" @change="handleCheckBoxChange">
      <el-checkbox v-for="(t, i) in groupList" :key="t" :label="t" :value="t">
        {{ store.state.label[t] || t }}
      </el-checkbox>
    </el-checkbox-group>
  </div>
  <el-descriptions
      v-if="!pcModel"
      v-for="(row, i) in list"
      :key="'des-' + i"
      :title="`Submit info ${i + 1}`"
      direction="vertical"
      :column="column"
      style="margin-bottom: 20px;"
      border
  >`
    <el-descriptions-item
        v-for="(viewConfig, i) in handleColumnConfigList(columnConfigList, [ValueType.Expand], true, false, groupList, checkedGroupList)"
        :key="viewConfig.value"
        :label="viewConfig.labelKey ? store.state.label[viewConfig.labelKey] : viewConfig.label"
        :align="viewConfig.align || 'center'"
    >
      <view-content
          :idKey="props.idKey"
          :viewConfig="viewConfig"
          :row="row"
          :handleShowDetail="props.detailLink ? handleShowDetail : null"
          :handleEdit="props.handleEdit"
          :handleUpdate="props.handleUpdate"
          :handleEditShow="props.handleEditShow"
          :handleDeleteShow="props.handleDeleteShow"
          :handleDelete="props.handleDelete"
      >
        <template #expand="row">
          <slot name="expand" :param="row.param"/>
        </template>
        <template #template="{param, viewConfig}">
          <slot name="template" :param="param" :viewConfig="viewConfig"/>
        </template>
      </view-content>
    </el-descriptions-item>
  </el-descriptions>
  <el-table
      v-if="pcModel"
      ref="tableRef"
      :header-cell-style="{
        background: ' rgba(250, 251, 252, 1)',
        color: 'rgba(102, 102, 102, 1)',
        height: '54px',
        fontSize: `${props.headerFontSize || 14}px`,
        padding: 0,
        boxShadow:
          'inset 0px -1px 0px  rgba(238, 238, 238, 1),inset 0px 1px 0px  rgba(238, 238, 238, 1)',
      }"
      :cell-style="{
        fontSize: `${props.cellFontSize || 14}px`,
      }"
      :border="true"
      :data="list"
      size="small"
      :row-key="props.idKey"
      :expand-row-keys="expandRowKeys"
      :height="showControl ? tableHeight : '100%'"
      :row-class-name="props.handleTableRowClassName || handleTableRowClassName"
      :cell-class-name="props.handleTableCellClassName"
      :span-method="handleGetSpan"
      :style="{
          transform: `scale(${scale})`,
          width: `${100 * (1 / scale)}% !important`,
          maxWidth: `${100 * (1 / scale)}% !important`,
          marginLeft: `${100 * (1 / scale - 1) / -2}%`,
          marginTop: `${tableHeight * (1 - scale)  / -2}px`,
          display: scale === 1 ? '' : 'block',
          fontSize: `${props.cellFontSize || 14}px`,
        }"
      @selection-change="props.handleSelectionChange"
  >
    <table-view-content
        :idKey="props.idKey"
        :viewConfigList="columnConfigList"
        :handleShowDetail="props.detailLink ? handleShowDetail : null"
        :handleEdit="props.handleEdit"
        :handleUpdate="props.handleUpdate"
        :handleEditShow="props.handleEditShow"
        :handleDeleteShow="props.handleDeleteShow"
        :handleDelete="props.handleDelete"
        :groupList="groupList"
        :checkedGroupList="checkedGroupList"
        :scale="scale"
    >
      <template #operator="row">
        <slot name="operator" :param="row.param"/>
      </template>
      <template #template="{param, viewConfig}">
        <slot name="template" :param="param" :viewConfig="viewConfig"/>
      </template>
    </table-view-content>
  </el-table>
  <el-pagination
      v-if="props.total >= 0"
      background
      :page-sizes="PAGE_SIZE_LIST"
      v-model:currentPage="props.page.page"
      :page-size="props.page.limit"
      :total="props.total"
      @current-change="props.handlePageChange"
      @size-change="props.handleLimitChange"
      layout="total, sizes, prev, pager, next, jumper"
      :style="{
        marginTop: pcModel ? `${tableHeight * (1 - scale)  / -2}px` : 0,
      }"
  />
  <el-drawer
      v-if="props.detailLink"
      v-model="showDetail"
      size="100%"
      :append-to-body="true"
      :lock-scroll="false"
      modal-class="view-list-print-drawer"
      :before-close="handleDrawerClose"
      :z-index="99999"
  >
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;" id="printContainer">
      <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
        <h1 v-if="props.detailTitle">{{ props.detailTitle }}</h1>
        <h1 v-else>{{ store.state.label[props.idKey.substring(0, props.idKey.length - 2)] }}</h1>
        <div v-if="props.detailSubTitle" style="display: flex; justify-content: flex-end; width: 100%;">
          <h5>{{ props.detailSubTitle }}</h5>
        </div>

        <!--direction="vertical"-->
        <el-descriptions
            :column="1"
            style="width: 100%;"
            border
        >`
          <el-descriptions-item
              v-for="(viewConfig, i) in handleColumnConfigList(columnConfigList, [ValueType.Expand, ValueType.Operator], true, false, groupList, checkedGroupList)"
              :key="viewConfig.value"
              :label="viewConfig.labelKey ? store.state.label[viewConfig.labelKey] : viewConfig.label"
              align="center"
              label-class-name="print-label"
          >
            <view-content
                :idKey="props.idKey"
                :viewConfig="viewConfig"
                :row="detailInfo"
                :handleEdit="props.handleEdit"
                :handleUpdate="props.handleUpdate"
                :handleEditShow="props.handleEditShow"
                :handleDelete="props.handleDelete"
                :fullMessage="true"
            >
              <template #expand="row">
                <slot name="expand" :param="row.param"/>
              </template>
              <template #template="{param, viewConfig}">
                <slot name="template" :param="param" :viewConfig="viewConfig"/>
              </template>
            </view-content>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
  </el-drawer>
</template>

<script lang="ts" setup>
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import {useRoute, useRouter} from 'vue-router'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Industry'
import {ref, Ref, watch} from 'vue'
import {getStorage, setStorage} from '@/util/StorageUtil'
import {TableColumnCtx} from 'element-plus'
import {PAGE_SIZE_LIST,} from '@/typing/Common'
import {handleColumnConfigList} from '@/util/IndustryUtil'
import TableViewContent from './TableViewContent.vue'
import ViewContent from './ViewContent.vue'

const props = withDefaults(defineProps<PropType>(), {
  total: -1,
  headerFontSize: 14,
  cellFontSize: 14,
  detailLink: true,
  showControl: true,
})
const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const scaleKey = `${props.idKey}_scale`
const spanArray: any = {}
const scale: Ref = ref(Number(getStorage(scaleKey) || '1'))
const defaultTableHeight = Math.max(window.innerHeight - 380, 550)
const column: Ref = ref(Math.max(2, 1 + Number((window.innerWidth / 256).toFixed(0))))
const pcModel: Ref = ref(window.innerWidth >= 512)
const tableHeight = ref(defaultTableHeight / scale.value)
const showDetail = ref(false)
const showControl = ref(props.showControl)
const detailInfo = ref({})
const handleScaleChange = () => {
  tableHeight.value = defaultTableHeight / scale.value
  setStorage(scaleKey, scale.value)
}
const handleDrawerClose = () => {
  document.body.style.height = bodyHeight
  showDetail.value = false
}

const handleCheckBoxChange = (e: any) => {
  console.log(`e: ${e}`)
}

interface Page {
  page: number;
  limit: number;
}

interface PropType {
  idKey: string;
  columnConfigList: ViewConfig[];
  list: any[];
  total?: number;
  handleEdit?: (row: any) => {};
  handleUpdate?: (row: any) => {};
  handleEditShow?: (row: any) => {};
  handleDeleteShow?: (row: any) => {};
  handleDelete?: (row: any) => {};
  page?: Page;
  handlePageChange?: (i: number) => {};
  handleLimitChange?: (i: number) => {};
  handleTableRowClassName?: (row: any) => {};
  handleTableCellClassName?: (row: any) => {};
  handleSelectionChange?: (row: any) => {};
  detailLink?: boolean;
  showControl?: boolean;
  headerFontSize?: number;
  cellFontSize?: number;
}

interface SpanMethodProps {
  row: any
  column: TableColumnCtx<any>
  rowIndex: number
  columnIndex: number
}

const expandRowKeys = ref([])
const columnConfigList = ref(props.columnConfigList)
const groupList = ref<string[]>(columnConfigList.value.filter(t => t.children && t.children.length > 0).map((t: ViewConfig) => t.labelKey || t.label || ''))
const checkedGroupList = ref<string[]>(groupList.value.map((t: string) => t))
const list = ref(props.list)
defineExpose({})
const bodyHeight = document.body.style.height
const handleShowDetail = (row: any) => {
  showDetail.value = true
  detailInfo.value = row
  const el = document.getElementsByClassName('view-list-print-drawer')
  const e: any = el[el.length - 1]
  setTimeout(() => {
    handleChangeStyle(e, e, 'el-descriptions')
  }, 500)
}

const handleChangeStyle = (e: any, d: any = e, c: string, loop: boolean = true): any => {
  const children = d.children || []
  let r = null
  for (let i = 0; i < children.length || 0; i++) {
    const t = children[i]
    const str = t.getAttribute('class') || ''
    if (str.indexOf(c) >= 0) {
      r = t
      console.log(`content height: ${r.offsetHeight}, r: ${r}`)
      e.style.height = /*document.body.style.height = */(r.offsetHeight + 600) + 'px'
    } else {
      r = handleChangeStyle(e, t, c, false)
    }
    if (null !== r) {
      break
    }
  }
  if (!r && loop) {
    setTimeout(() => {
      handleChangeStyle(e, d, c)
    }, 100)
  }
  return r
}
const handleTableColumnConfigList = () => {
  return handleColumnConfigList(columnConfigList.value, [ValueType.Image, ValueType.Video, ValueType.Attachment], false, true, groupList.value, checkedGroupList.value)
}

const handleSpanArray = () => {
  for (let k in spanArray) {
    delete spanArray[k]
  }
  const arr = handleTableColumnConfigList()
  const l = list.value
  const length = l.length
  for (let i = 0; i < length; i++) {
    const t = l[i]
    for (let j = 0; j < arr.length; j++) {
      const c: ViewConfig = arr[j]
      if (c.mergeKey) {
        const columnIndex = j
        const columnKeyArr: string[] = c.mergeKey
        if (!spanArray[`${i}-${columnIndex}`]) {
          let index = i
          while (++index < length) {
            const temp = l[index]
            let match = true
            for (let k = 0; k < columnKeyArr.length; k++) {
              const columnKey = columnKeyArr[k]
              if (t[columnKey] !== temp[columnKey]) {
                match = false
                break
              }
            }
            if (match) {
              spanArray[`${index}-${columnIndex}`] = {
                rowspan: 0,
                colspan: 0,
              }
            } else {
              break
            }
          }
          spanArray[`${i}-${columnIndex}`] = {
            rowspan: index - i,
            colspan: 1,
          }
        }
      }
    }
  }
}
const handleGetSpan = ({
                         row,
                         column,
                         rowIndex,
                         columnIndex,
                       }: SpanMethodProps) => {
  return spanArray[`${rowIndex}-${columnIndex}`] || {
    rowspan: 1,
    colspan: 1,
  }
}
const handleTableRowClassName = ({
                                   row,
                                   rowIndex,
                                 }: {
  row: any
  rowIndex: number
}) => {
  if (row.valid) {
    return 'row-done'
  }
  return ''
}
watch(
    () => props.list,
    (l) => {
      list.value = l
      handleSpanArray()
    },
    {
      immediate: true,
    }
)
watch(
    () => props.columnConfigList,
    (l) => {
      columnConfigList.value = l
      handleSpanArray()
    },
    {
      immediate: true,
    }
)
watch(
    () => props.showControl,
    (t) => {
      showControl.value = t
    },
    {
      immediate: true,
    }
)
</script>
<style lang="scss">
.view-list-print-drawer {
  position: absolute;
}

.print-label {
  width: 239px;
}
</style>
