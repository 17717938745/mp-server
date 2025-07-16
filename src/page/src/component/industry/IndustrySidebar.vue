<template>
  <div
      style="background-color: #324157; height: 100vh; display: flex; flex-direction: column; padding-bottom: 120px;"
  >
    <el-menu
        ref="sidebarRef"
        class="sidebar"
        :default-active="handleRouter"
        :default-openeds="defaultOpens"
        :collapse="collapse"
        background-color="#324157"
        text-color="#bfcbd9"
        active-text-color="#20a0ff"
        router
    >
      <a href="https://vn.douson.cn/" style="color: #ffffff; display: flex; align-items: center; text-decoration: none;" target="_blank">
        <img :src="fullUrl('/third/img/industry/logo.ico', '')" alt="" style="height: 30px; margin-right: 10px;"/>
        https://vn.douson.cn/
      </a>
      <template v-for="sidebarTree in sidebarTreeList">
        <Industry-sidebar-item class="leadSidebarItem" :sidebar-tree="sidebarTree"/>
      </template>
    </el-menu>
    <div style="background-color: #324157; z-index: 99999;position: fixed; bottom: 5px;left: 5px;">
      <img :src="fullUrl('/third/img/collapse.svg', '')" alt="" @click="collapseChange" style="width: 16px; height: 16px;margin-bottom: 5px;"/>
    </div>
  </div>
</template>

<script lang="tsx">
import {fullUrl} from '@/util/EnvUtil'
import {computed, reactive, ref, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {useRoute} from 'vue-router'
import IndustrySidebarItem from './IndustrySidebarItem.vue'
import {StoreType} from '@/store/Industry'
import {includes} from '@/util/ArrayUtil'

const sidebarRef = ref(null)
export default {
  name: "industry-sidebar",
  props: ["sidebarTreeList"],
  components: {
    IndustrySidebarItem,
  },
  setup(props: any) {
    const route = useRoute();
    const store: Store<StoreType> = useStore();
    const storeState: StoreType = store.state;
    const sidebarTreeList = props.sidebarTreeList
    const collapse = computed(() => storeState.collapse)
    const collapseChange = () => {
      store.commit("setCollapse", !collapse.value)
    }
    const handleGetParentPath = (parentList: [] = [], list: [] = sidebarParentTreeList, path): any[] => {
      if (list && list.length > 0) {
        for (let i = 0; i < list.length; i++) {
          const d = list[i]
          const temp = `/${d.pathList.join('/')}`
          if (temp === path) {
            parentList.push(d)
            return parentList
          }
          if (d.children && d.children.length > 0) {
            const newList =[]
            newList.push(...parentList)
            newList.push(d)
            const beforeLength = newList.length
            const tempList = handleGetParentPath(newList, d.children, path)
            if (beforeLength < tempList.length) {
              return tempList
            }
          }
        }
      }
      return parentList
    }
    const handleRouter = computed(() => {
          const path = route.path
          const parentList = handleGetParentPath([], sidebarParentTreeList, path)
          const parentPathList = parentList.map(parent => {
             return `/${parent.pathList.join('/')}`
          })
          parentPathList.forEach(parentPath => {
            if (!includes(state.defaultOpens, parentPath)) {
              state.defaultOpens.push(parentPath)
            }
          })
          return route.path
        }),
        handleGetWidth = () => {
          return sidebarRef && sidebarRef.value ? sidebarRef.value['offsetWidth'] : 0
        },
        handleGetDom = () => {
          return sidebarRef.value
        }

    const sidebarParentTreeList = sidebarTreeList.filter(
        (t: any) => t.children && t.children.length > 0
    )
    const state = reactive({
      defaultOpens:
          sidebarParentTreeList.length > 0
              ? ["/" + sidebarParentTreeList[0].pathList.join("/")]
              : [],
    });

    return {
      ...toRefs(state),
      storeState,
      collapse,
      sidebarTreeList,
      handleRouter,
      handleGetWidth,
      handleGetDom,
      sidebarRef,
      collapseChange,
      fullUrl
    };
  },
};
</script>

<style scoped>
.sidebar {
  height: 100vh;
  overflow-y: scroll;
}

.border-top-fff01 {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.el-sub-menu .el-menu-item) {
  min-width: 208px;
  margin-bottom: 4px;
  font-size: 14px;
}

.el-menu-item:hover {
  background-color: unset;
}

</style>
