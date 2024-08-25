<template>
  <div class="crumb">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item v-for="(menu, index) in handleBreadcrumbList()">
        <span v-if="index === handleBreadcrumbList().length -1">{{ menu.name }}</span>
        <el-link
            v-else-if="menu.link"
            type="primary"
            class="link"
            @click="handleLinkTo(menu.link)"
        >
          {{ menu.name }}
        </el-link>
        <el-link
            v-else
            type="info"
            class="link">
          {{ menu.name }}
        </el-link>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>
<script lang="tsx">
import {useRouter} from 'vue-router'
import {reactive, toRefs} from 'vue'
import {Store, useStore} from 'vuex'
import {menuTreeToBreadCrumbList} from '../../util/RouterUtil'
import {BreadCrumb} from '../../typing/ma/System'
import {StoreType} from '../../store/Industry'

export default {
  name: 'industry-bread-crumb',
  components: {},
  setup() {
    const router = useRouter()
    const store: Store<StoreType> = useStore()
    const storeState: StoreType = store.state
    const state = reactive({
      pathList: storeState.pathList,
      menuTreeList: storeState.menuTreeList,
    })
    const handleLinkTo = (path: string) => {
          console.log(`do nothing, path: ${path}`)
          // router.push(path)
        },
        handleBreadcrumbList = (): BreadCrumb[] => {
          return menuTreeToBreadCrumbList(storeState.menuTreeList, storeState.pathList)
        }

    return {
      handleLinkTo,
      handleBreadcrumbList,
      ...toRefs(state),
    };
  },
};
</script>
<style scoped lang="scss">
</style>
