<template>
  <div>
    <el-tree
        style="max-width: 600px"
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        :data="data"
        draggable
        default-expand-all
        node-key="id"
        @node-drag-start="handleDragStart"
        @node-drag-enter="handleDragEnter"
        @node-drag-leave="handleDragLeave"
        @node-drag-over="handleDragOver"
        @node-drag-end="handleDragEnd"
        @node-drop="handleDrop"
        @node-click="handleClick"
    />
    <el-affix position="bottom" :offset="20" style="margin-top: 20px;">
      <el-button type="primary" @click="handleMerge">Save</el-button>
    </el-affix>
  </div>
</template>

<script lang="tsx" setup>
import {ref,} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import type {AllowDropType, NodeDropType, RenderContentContext,} from 'element-plus'
import {ElMessage} from 'element-plus'
import {httpGet, httpPutJson} from '@/util/HttpUtil'
import {useRouter} from 'vue-router'
import type {DragEvents} from 'element-plus/es/components/tree/src/model/useDragNode'

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user

type Node = RenderContentContext['node']

const handleDragStart = (node: Node, ev: DragEvents) => {
  console.log('drag start', node)
}
const handleDragEnter = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
  console.log('tree drag enter:', dropNode.label)
}
const handleDragLeave = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
) => {
  console.log('tree drag leave:', dropNode.label)
}
const handleDragOver = (draggingNode: Node, dropNode: Node, ev: DragEvents) => {
  console.log('tree drag over:', dropNode.label)
}
const handleDragEnd = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
  console.log('tree drag end:', dropNode && dropNode.label, dropType)
}
const handleDrop = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
) => {
  console.log('tree drop:', dropNode.label, dropType)
}
const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
  if (dropNode.data.label === 'Level two 3-1') {
    return type !== 'inner'
  } else {
    return true
  }
}
const allowDrag = (draggingNode: Node) => {
  return !draggingNode.data.label.includes('Level three 3-1-1')
}
const handleClick = (draggingNode) => {
  console.log(JSON.stringify(draggingNode, null, 2))
}
const data = ref([])
const handleList = () => {
  return httpGet(`/system/depart/list`, {}).then(r => {
    data.value = r.list
  })
}
const handleMerge = () => {
  return httpPutJson(`/system/depart/list`, data.value).then(r => {
    ElMessage.success('Save Success')
  })
}
handleList()
</script>

<style scoped lang="scss">
</style>
