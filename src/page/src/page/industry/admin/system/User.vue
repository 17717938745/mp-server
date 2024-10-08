<template>
  <div>
    <el-tabs v-model="activeIndex" @tab-click="handleClick">
      <el-tab-pane
          v-for="(d, index) in componentList"
          :label="d.label"
          :key="index"
          :name="index"
      >
      </el-tab-pane>
    </el-tabs>
    <component :is="componentList[activeIndex].component"></component>
  </div>
</template>

<script lang="tsx" setup>
import {ref, shallowRef} from 'vue'
import {Store, useStore} from 'vuex'
import {useRoute} from 'vue-router'
import {TabsPaneContext} from 'element-plus'
import Index from './user/Index.vue'
import Department from './user/Department.vue'
import Profession from './user/Profession.vue'
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()


const componentList = ref([
  {
    label: store.state.label.userManage,
    component: shallowRef(Index),
  },
  {
    label: store.state.label.departmentAndSchedule,
    component: shallowRef(Department),
  },
  {
    label: store.state.label.profession,
    component: shallowRef(Profession),
  },
]);
let tabIndex
try {
  tabIndex = Number(route.query.tabIndex) || 0
} catch (e) {
  tabIndex = 0
}
const activeIndex = ref(Math.min(tabIndex, componentList.value.length - 1))
const handleClick = (tab: TabsPaneContext, event: Event) => {
  const message = `tab.paneName: ${tab.paneName}`
}
</script>

<style scoped lang="scss"></style>
