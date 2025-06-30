<template>
  <!-- 如果有children或者children展示的个数大于1，显示children菜单 -->
  <el-sub-menu
      v-if="
      sidebarTree.showFlag &&
      sidebarTree.children &&
      sidebarTree.children.length > 0/* &&
      childrenShow(sidebarTree.children) > 1*/
    "
      :index="'/' + sidebarTree.pathList.join('/')"
      :key="sidebarTree.id"
  >
    <!--suppress HtmlUnknownAttribute -->
    <template #title>
      <el-icon>
        <Folder/>
      </el-icon>
      <span style="display: inline-block; padding-right: 40px" class="m-l-5">
        {{ sidebarTree.nameKey ? store.state.label[sidebarTree.nameKey] : sidebarTree.name }}
      </span>
    </template>
    <template v-for="child in sidebarTree.children">
      <industry-sidebar-item :sidebar-tree="child"/>
    </template>
  </el-sub-menu>
  <!-- 如果有children或者children展示的个数==1，显示第一个showFlag children菜单 -->
  <el-menu-item
      v-else-if="
      sidebarTree.showFlag &&
      sidebarTree.children &&
      sidebarTree.children.length > 0 &&
      childrenShow(sidebarTree.children) === 1
    "
      :index="'/' + `${sidebarTree.children[0].pathList.join('/')}`"
      :key="sidebarTree.id"
      @click="handleClickMenu"
  >
    <template #title>
      <span class="m-l-10">
      {{ sidebarTree.nameKey ? store.state.label[sidebarTree.nameKey] : sidebarTree.name }}
    </span>
    </template>
  </el-menu-item>
  <el-menu-item
      v-else-if="sidebarTree.showFlag"
      :index="'/' + sidebarTree.pathList.join('/')"
      :key="sidebarTree.id"
      @click="handleClickMenu"
  >
    <template #title><span class="m-l-10">
        {{ sidebarTree.nameKey ? store.state.label[sidebarTree.nameKey] : sidebarTree.name }}
      </span>
    </template>
  </el-menu-item>
</template>

<script lang="tsx" setup>
import {Folder} from '@element-plus/icons-vue'
import {SidebarTree} from '@/typing/ma/System'
import {Store, useStore} from 'vuex'
import {useRoute} from 'vue-router'
import {StoreType} from '@/store/Industry'

const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()

interface Prop {
  sidebarTree: SidebarTree
}

const props = withDefaults(defineProps<Prop>(), {})
const sidebarTree = props.sidebarTree
// 判断children是不是有show的
const childrenShow = (list: any[]) => {
  const showList = list.filter((i) => i.showFlag)
  return !showList ? 0 : showList.length
}
const handleClickMenu = (e) => {
}
</script>

<style lang="scss" scoped>
:deep(.el-sub-menu .el-icon) {
  margin-right: 0;
}
</style>
