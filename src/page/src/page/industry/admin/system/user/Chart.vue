<template>
  <div>
    <div ref="mermaidContainer" class="mermaid-container"></div>
  </div>
</template>

<script lang="tsx" setup>
import {onMounted, ref} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store/Index'
import {useRouter} from 'vue-router'
import mermaid from 'mermaid'
import {httpGet} from "@/util/HttpUtil";

const router = useRouter()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const mermaidContainer = ref(null)
mermaid.initialize({
  // 设置为false，我们将手动渲染
  startOnLoad: false,
  // 可选：default, forest, dark, neutral
  theme: 'default',
  flowchart: {
    curve: 'basis',
    target: '_blank',
  },
  // 允许使用HTML标签
  securityLevel: 'loose'
});

// 图表定义
const chartDefinition = ref(`
graph TD
    A[Vue 应用] --> B{Mermaid 图表}
    B --> C[流程图]
    B --> D[序列图]
    B --> E[甘特图]
    style A fill:#4CAF50,color:white
`)

// 渲染图表函数
const renderChart = async () => {
  try {
    // 使用render方法获取SVG代码
    const {svg} = await mermaid.render('mermaid-svg', chartDefinition.value)
    if (mermaidContainer.value) {
      mermaidContainer.value.innerHTML = svg
    }
  } catch (error) {
    console.error('Mermaid 渲染错误:', error)
  }
}
onMounted(() => {

})
const loopDepart = (list: any[], strList: string[] = [], parentId: string = '', linkList: string[] = []) => {
  list.forEach(t => {
    strList.push(parentId ? `${parentId} --> ${t.id}[${t.label}\n（${t.totalUserCount}人）]` : `${t.id}[${t.label}\n（${t.totalUserCount}人）]`)
    // linkList.push(`click ${t.id} href "/industry/admin/system/user?tabIndex=0&organizationalStructure=${t.id}" _blank`)
    if (t.children && t.children.length > 0) {
      loopDepart(t.children, strList, t.id, linkList)
    }
  })
  return strList
}
const data = ref([])
const handleList = () => {
  return httpGet(`/system/depart/list`, {}).then(r => {
    data.value = r.list
    const chartList = []
    const linkList = []
    loopDepart(data.value, chartList, '', linkList)
    chartDefinition.value = `
flowchart TD
    ${chartList.join('\n    ')}

    ${linkList.join('\n    ')}

`
    console.log(chartDefinition.value)
    //
    // style 0 fill:#4CAF50,color:white
    renderChart()
  })
}
handleList()
</script>

<style scoped lang="scss">
</style>
