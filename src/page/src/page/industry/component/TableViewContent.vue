<template>
  <table-column-view-content
      v-for="(viewConfig, i) in handleColumnConfigList(viewConfigList, [ValueType.Image, ValueType.Video, ValueType.Attachment], false, true, props.groupList, props.checkedGroupList)"
      :key="viewConfig.value"
      :idKey="props.idKey"
      :viewConfig="viewConfig"
      :viewConfigList="viewConfigList"
      :handleShowDetail="props.handleShowDetail"
      :handleEdit="props.handleEdit"
      :handleUpdate="props.handleUpdate"
      :handleEditShow="props.handleEditShow"
      :handleDelete="props.handleDelete"
      :scale="scale"
  >
    <template v-if="viewConfig.type === ValueType.Operator" #operator="row">
      <slot name="operator" :param="row.param"/>
    </template>
    <template v-else-if="viewConfig.type === ValueType.Template" #template="{param, viewConfig}">
      <slot name="template" :param="param" :viewConfig="viewConfig"/>
    </template>
  </table-column-view-content>
</template>

<script lang="ts" setup>
import {ref, watch} from 'vue'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import {useRoute, useRouter} from 'vue-router'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Industry'
import TableColumnViewContent from './TableColumnViewContent.vue'
import {handleColumnConfigList} from '@/util/IndustryUtil'

const props = withDefaults(defineProps<PropType>(), {
  scale: 1,
})
const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList

interface PropType {
  idKey: string;
  viewConfigList: ViewConfig[];
  groupList?: any[];
  checkedGroupList?: any[];
  handleShowDetail?: (row: any) => {};
  handleEdit?: (row: any) => {};
  handleUpdate?: (row: any) => {};
  handleEditShow?: (row: any) => {};
  handleDelete?: (row: any) => {};
  scale?: number;
}

const groupList = props.groupList || []
const checkedGroupList = ref<any[]>(props.checkedGroupList || [])
const viewConfigList = ref<any[]>(props.viewConfigList || [])
const scale = ref(props.scale)

const emit = defineEmits([])
defineExpose({})
watch(
    () => props.viewConfigList,
    (l: any) => {
      viewConfigList.value = l
    },
    {
      immediate: true,
    }
)
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
