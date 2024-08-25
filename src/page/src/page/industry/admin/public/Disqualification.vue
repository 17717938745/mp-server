<template>
  <div style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;" id="printContainer">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
      <h1 style="text-align: center;">
        <span style="font-size: 34px;" @click="handleCopyLink">
          Non-Conformance Report（NCR)
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
            label-class-name="disqualification-print-label description-print-border"
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
import {includes} from "@/util/ArrayUtil";
import {DocumentCopy,} from '@element-plus/icons-vue'
import {dousonFullUrl} from '@/util/EnvUtil'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const idKey = 'disqualificationId'
const columnConfigList = ref<ViewConfig[]>([
  {value: 'disqualificationOrderNoFormat', labelKey: 'disqualificationOrderNo', width: 125},
  {value: 'creatorFormat', labelKey: 'inspector', width: 96},
  {value: 'orderNo', labelKey: 'orderNo', width: 80},
  {value: 'projectSequence', labelKey: 'projectSequence', width: 50},
  {value: 'materialNo', labelKey: 'materialNo', width: 163, align: 'left',},
  {value: 'designNumber', labelKey: 'designNumber', width: 153, align: 'left',},
  {value: 'processFormat', labelKey: 'process', width: 106},
  {value: 'checkPointFormat', labelKey: 'checkPoint', width: 106},
  {value: 'stoveNo', labelKey: 'stoveNo', width: 109},
  {value: 'hotBatchNo', labelKey: 'hotBatchNo', width: 109},
  {value: 'serialNo', labelKey: 'serialNo', width: 138},
  {value: 'disqualificationContent', labelKey: 'disqualificationContent', width: 276, type: ValueType.Text},
  {value: 'count', labelKey: 'count', width: 67},
  {value: 'defectTypeFormat', labelKey: 'defectType', width: 128},
  {value: 'dutyPersonFormat', labelKey: 'dutyPerson', width: 128},
  {value: 'photoList', label: store.state.label.photoList, labelKey: 'photo', width: 269, type: ValueType.Image,},
  {
    value: 'qualityDealOpinionFormat', labelKey: 'qualityDealOpinion', width: 86,
  },
  {
    value: 'skillDealOpinionFormat', labelKey: 'skillDealOpinion', width: 86,
  },
  {value: 'fineAmountFormat', labelKey: 'fineAmount', width: 87,},
  {
    value: 'remark', labelKey: 'remark', width: 256,
  },
])
const data = ref({})
httpGet(`douson/disqualification-order`, {disqualificationOrderId: route.query.disqualificationOrderId}).then(
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
  const text = dousonFullUrl(`/industry/public/disqualification?disqualificationOrderId=${route.query.disqualificationOrderId}`)
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
.disqualification-print-label {
  width: 139px;
}
</style>
