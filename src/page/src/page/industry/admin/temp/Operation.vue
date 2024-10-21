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
  {value: 'device', labelKey: 'device', width: 100,},
  {value: 'customerShortName', labelKey: 'customerShortName', width: 100,},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 100,},
  {value: 'orderProject', labelKey: 'orderProject', width: 100,},
  {value: 'materialNo', labelKey: 'materialNo', width: 100,},
  {value: 'materialDescribe', labelKey: 'materialDescribe', width: 100,},
  {value: 'roughcastDesignNumber', labelKey: 'roughcastDesignNumber', width: 100,},
  {value: 'orderCount', labelKey: 'orderCount', width: 100,},
  {value: 'roughcastExpireDate', labelKey: 'roughcastExpireDate', width: 100,},
  {value: 'materialCount', labelKey: 'materialCount', width: 100,},
  {value: 'promiseCompleteDate', labelKey: 'promiseCompleteDate', width: 100,},
  {value: 'planReformCount', labelKey: 'planReformCount', width: 100,},
  {value: 'externalProcedureRemark', labelKey: 'externalProcedureRemark', width: 100,},
  {value: 'procedureCountHour8', labelKey: 'procedureCountHour8', width: 100,},
  {value: 'procedureCountHour12', labelKey: 'procedureCountHour12', width: 100,},
  {value: 'processManHour', labelKey: 'processManHour', width: 100,},
  {value: 'onlineDate', labelKey: 'onlineDate', width: 100,},
  {value: 'offlineDate', labelKey: 'offlineDate', width: 100,},
  {value: 'delay', labelKey: 'delay', width: 100,},
  {value: 'processCount', labelKey: 'processCount', width: 100,},
  {value: 'procedureSketch', labelKey: 'procedureSketch', width: 100,},
  {value: 'assemble', labelKey: 'assemble', width: 100,},
  {value: 'pressureTest', labelKey: 'pressureTest', width: 100,},
  {value: 'dealSurface', labelKey: 'dealSurface', width: 100,},
  {value: 'surplus', labelKey: 'surplus', width: 100,},
  {value: 'materialOrderNo', labelKey: 'materialOrderNo', width: 100,},

])
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
