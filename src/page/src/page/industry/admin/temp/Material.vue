<template>
  <div style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;" id="printContainer">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
      <h1 style="text-align: center;">
        <span style="font-size: 36px;" @click="handleCopyLink">
          Accident Report
            <el-icon style="vertical-align: middle">
              <document-copy/>
            </el-icon>
        </span>
      </h1>
      <el-descriptions
          :column="1"
          style="width: 100%;"
          border
          id="printDescription"
      >`
        <el-descriptions-item
            v-for="(viewConfig, i) in handleColumnConfigList(columnConfigList, [], true, false, [], [])"
            :key="viewConfig.value"
            :label="viewConfig.labelKey ? store.state.label[viewConfig.labelKey] : viewConfig.label"
            align="center"
            label-class-name="accident-print-label description-print-border"
            class-name="description-print-border"
        >
          <view-content
              :idKey="idKey"
              :viewConfig="viewConfig"
              :row="data"
          >
            <template #expand="row">
              <slot name="expand" :param="row.param"/>
            </template>
          </view-content>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script lang="tsx" setup>
import {ref} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage} from 'element-plus'
import {useRoute, useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewContent from '../../component/ViewContent.vue'
import {handleColumnConfigList} from '@/util/IndustryUtil'
import {DocumentCopy,} from '@element-plus/icons-vue'
import {dousonFullUrl} from '@/util/EnvUtil'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const idKey = 'materialId'
const columnConfigList = ref<ViewConfig[]>([
  {value: 'roughcastExpireDate', labelKey: 'roughcastExpireDate', width: 100,},
  {value: 'customerShortName', labelKey: 'customerShortName', width: 100,},
  {value: 'customerOrderNo', labelKey: 'customerOrderNo', width: 100,},
  {value: 'customerProjectSequence', labelKey: 'customerProjectSequence', width: 100,},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 100,},
  {value: 'orderProjectNo', labelKey: 'orderProjectNo', width: 100,},
  {value: 'materialNo', labelKey: 'materialNo', width: 100,},
  {value: 'materialDescribe', labelKey: 'materialDescribe', width: 100,},
  {value: 'roughcastDesignNumber', labelKey: 'roughcastDesignNumber', width: 100,},
  {value: 'stoveNo', labelKey: 'stoveNo', width: 100,},
  {value: 'hotBatchNo', labelKey: 'hotBatchNo', width: 100,},
  {value: 'serialNo', labelKey: 'serialNo', width: 100,},
  {value: 'surplusCount', labelKey: 'surplusCount', width: 100,},
  {value: 'assemble', labelKey: 'assemble', width: 100,},
  {value: 'pressureTest', labelKey: 'pressureTest', width: 100,},
  {value: 'dealSurface', labelKey: 'dealSurface', width: 100,},
  {value: 'scheduleProduce', labelKey: 'scheduleProduce', width: 100,},
  {value: 'materialOrderNo', labelKey: 'materialOrderNo', width: 100,},
])
/*
let str = ''
let str1 = ''
columnConfigList.value.filter((t:any) => t.value !== 'expand' && t.value !== 'operator' && t.value !== 'index').forEach((t: any) => {
  str += '    /!**\n' +
      '     * ' + store.state.label[t.labelKey] + '\n' +
      '     *!/\n' +
      '    @FieldRemark(value = "' + store.state.label[t.labelKey] + '")\n' +
      '    private String ' + t.value + ';\n'
  str1 += '    /!**\n' +
      '     * ' + store.state.label[t.labelKey] + '\n' +
      '     *!/\n' +
      '    private String ' + t.value + ';\n'
})
console.log(str)
console.log('\n\n')
console.log(str1)
*/
const data = ref({})
httpGet(`douson/accident`, {accidentId: route.query.accidentId}).then(
    (res: DataResult<any>) => {
      data.value = res.data || {}
      ElMessage.success("Query success")
      setTimeout(() => {
        const heightPx = (document.getElementById('printDescription')?.offsetHeight || 1024) + 450 + 'px'
        const printContainer = document.getElementById('printContainer')
        if (printContainer) {
          printContainer.style.height = heightPx
        }
      }, 1000)
    }
)

const handleCopyLink = () => {
  const text = dousonFullUrl(`/industry/public/accident?accidentId=${route.query.accidentId}`)
  // 添加一个input元素放置需要的文本内容
  const input = document.createElement('input')
  input.value = text
  document.body.appendChild(input)
  // 选中并复制文本到剪切板
  input.select()
  document.execCommand('copy')
  // 移除input元素
  document.body.removeChild(input)
  ElMessage.success('Copy success')
}
</script>

<style lang="scss">
.accident-print-label {
  width: 139px;
}
</style>
