<template>
  <el-table-column
      :type="viewConfig.type === ValueType.Expand ? 'expand' : viewConfig.type === ValueType.Selection ? 'selection' : ''"
      :key="viewConfig.value"
      :prop="viewConfig.value"
      :label="viewConfig.labelKey ? store.state.label[viewConfig.labelKey] : viewConfig.label"
      :width="viewConfig.width <= 0 ? '' : (viewConfig.width || 128)"
      :align="viewConfig.align || 'center'"
      :show-overflow-tooltip="viewConfig.showOverflow"
  >
    <template v-if="viewConfig.type !== ValueType.Selection && (viewConfig.children || []).length <= 0" #default="{row}">
      <view-content
          :idKey="props.idKey"
          :viewConfig="viewConfig"
          :row="row"
          :handleShowDetail="props.handleShowDetail"
          :handleEdit="props.handleEdit"
          :handleUpdate="props.handleUpdate"
          :handleEditShow="props.handleEditShow"
          :handleDeleteShow="props.handleDeleteShow"
          :handleDelete="props.handleDelete"
          :scale="scale"
      >
        <template v-if="viewConfig.type === ValueType.Expand" #expand="row">
          <slot name="expand" :param="row.param"/>
          <el-space wrap>
            <el-card
                v-if="handleMediaCount(row.param) > 1"
                v-for="(c, i) in handleMediaColumnConfigList()" :key="c.value">
              <template #header>{{ store.state.label[c.labelKey] || c.label }}</template>
              <view-content
                  :idKey="props.idKey"
                  :viewConfig="c"
                  :row="row.param"
                  :scale="scale"
              >
              </view-content>
            </el-card>
            <view-content
                v-else
                v-for="(c, i) in handleMediaColumnConfigList()" :key="c.value"
                :idKey="props.idKey"
                :viewConfig="c"
                :row="row.param"
                :scale="scale"
            >
            </view-content>
          </el-space>
        </template>
        <template v-else-if="viewConfig.type === ValueType.Operator" #operator="row">
          <slot name="operator" :param="row.param"/>
        </template>
        <template v-else-if="viewConfig.type === ValueType.Template" #template="row">
          <slot name="template" :param="row.param" :viewConfig="row.viewConfig"/>
        </template>
      </view-content>
    </template>
    <table-column-view-content
        v-if="viewConfig.type !== ValueType.Selection && (viewConfig.children|| []).length > 0"
        v-for="(cv, i ) in handleColumnConfigList(viewConfig.children, [ValueType.Image, ValueType.Video], false, true)"
        :key="cv.value"
        :idKey="props.idKey"
        :viewConfig="cv"
        :viewConfigList="viewConfigList"
        :handleShowDetail="props.handleShowDetail"
        :handleEdit="props.handleEdit"
        :handleUpdate="props.handleUpdate"
        :handleEditShow="props.handleEditShow"
        :handleDeleteShow="props.handleDeleteShow"
        :handleDelete="props.handleDelete"
        :scale="scale"
    >
    </table-column-view-content>
  </el-table-column>
</template>

<script lang="ts" setup>
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import {useRoute, useRouter} from 'vue-router'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Industry'
import ViewContent from './ViewContent.vue'
import {handleColumnConfigList} from '@/util/IndustryUtil'
import {includes} from '@/util/ArrayUtil'
import {ref, watch} from 'vue'
import TableViewContent from './TableViewContent.vue'
import TableColumnViewContent from './TableColumnViewContent.vue'

const handleIsTemplate = (row: any, cv: any) => {
  console.log(cv.type === ValueType.Template, JSON.stringify(cv))
  return cv.type === ValueType.Template
}

interface PropType {
  idKey: string;
  viewConfig: ViewConfig;
  viewConfigList: ViewConfig[];
  handleShowDetail?: (row: any) => {};
  handleEdit?: (row: any) => {};
  handleUpdate?: (row: any) => {};
  handleEditShow?: (row: any) => {};
  handleDeleteShow?: (row: any) => {};
  handleDelete?: (row: any) => {};
  scale?: number;
  detailLink?: boolean;
  groupList?: any[];
  checkedGroupList?: any[];
}

const props = withDefaults(defineProps<PropType>(), {
  scale: 1,
})
const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const viewConfig = props.viewConfig
const viewConfigList = props.viewConfigList
const scale = ref(props.scale)

const emit = defineEmits([])
defineExpose({})
const handleMediaColumnConfigList = () => {
  const mediaTypeList = [ValueType.Image, ValueType.Video, ValueType.Attachment]
  return handleColumnConfigList(viewConfigList, [], true, true)
  .filter(t => includes(mediaTypeList, t.type))
}
const handleMediaCount = (row: any) => {
  return handleMediaColumnConfigList()
      // .filter(t => {
      //   const d = row[t.value]
      //   return d && d.length > 0
      // })
      .length
}
watch(
    () => props.scale,
    (t: number) => {
      scale.value = t
    },
    {
      immediate: true,
    }
)
</script>
<style lang="scss" scoped></style>
