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
import Index from './box/Index.vue'
import Summary from './box/Summary.vue'
import Summary1 from './box/Summary1.vue'
import {StoreType} from '@/store/Industry'
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()


const componentList = ref([
  {
    label: store.state.label.boxFlagCard,
    component: shallowRef(Index),
  },
  {
    label: `${store.state.label.boxFlagCard} Summary`,
    component: shallowRef(Summary),
  },
  {
    label: `${store.state.label.boxFlagCard} Summary  of todo`,
    component: shallowRef(Summary1),
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
