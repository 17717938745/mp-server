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
import Index from './assembly/Index.vue'
import Summary from './assembly/Summary.vue'
import Summary2 from './assembly/Summary2.vue'
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()


const componentList = ref([
  {
    label: store.state.label.completeMachineAssemblyRecord,
    component: shallowRef(Index),
  },
  {
    label: `1${store.state.label.completeMachineAssemblyRecord} Summary`,
    component: shallowRef(Summary),
  },
  {
    label: `2${store.state.label.completeMachineAssemblyRecord} Summary`,
    component: shallowRef(Summary2),
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
