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
const idKey = 'accidentId'
const columnConfigList = ref<ViewConfig[]>([
  {value: 'reportDate', labelKey: 'date', width: 100,},
  {value: 'username', labelKey: 'username', width: 116,},
  {value: 'accidentTypeFormat', labelKey: 'accidentType', width: 88,},
  {value: 'dutyPersonFormat', labelKey: 'dutyPerson', width: 76,},
  {value: 'groupLeaderFormat', labelKey: 'groupLeader', width: 76,},
  {value: 'chargePersonFormat', labelKey: 'chargePerson', width: 76,},
  {value: 'managerFormat', labelKey: 'manager', width: 76,},
  {value: 'devicePhotoList', labelKey: 'devicePhoto', width: 128, type: ValueType.Image,},
  {value: 'deviceDescribe', labelKey: 'deviceDescribe', width: 112,},
  {value: 'designNumberPhotoList', labelKey: 'designNumberPhoto', width: 128, type: ValueType.Image,},
  {value: 'designNumberDescribe', labelKey: 'designNumberDescribe', width: 126,},
  {value: 'productWeight', labelKey: 'productWeight', width: 64,},
  {value: 'accidentDescribe', labelKey: 'accidentDescribe', width: 276, type: ValueType.Text,},
  {value: 'photoList', labelKey: 'photo', width: 192, type: ValueType.Image,},
  {value: 'videoList', labelKey: 'video', width: 192, type: ValueType.Video,},
  {value: 'damagePhotoList', labelKey: 'damagePhotoList', width: 128, type: ValueType.Image,},
  {value: 'damageDescribe', labelKey: 'damageDescribe', width: 200, type: ValueType.Text,},
  {value: 'propertyLossDescribe', labelKey: 'propertyLossDescribe', width: 200, type: ValueType.Text,},
  {value: 'propertyLossPhotoList', labelKey: 'propertyLossPhotoList', width: 192, type: ValueType.Image,},
  {value: 'humanFactorReason', labelKey: 'humanFactorReason', width: 276, type: ValueType.Text,},
  {value: 'humanFactorSolve', labelKey: 'humanFactorSolve', width: 276, type: ValueType.Text,},
  // {value: 'dutyPerson1', labelKey: 'dutyPerson1', width: 86,},
  // {value: 'fineAmount1Format', labelKey: 'fineAmount1Format', width: 96,},
  // {value: 'dutyPerson2', labelKey: 'dutyPerson2', width: 86,},
  // {value: 'fineAmount2Format', labelKey: 'fineAmount2Format', width: 96,},
  // {value: 'dutyPerson3', labelKey: 'dutyPerson3', width: 86,},
  // {value: 'fineAmount3Format', labelKey: 'fineAmount3Format', width: 96,},
  {value: 'improveEvidencePhotoList', labelKey: 'improveEvidencePhotoList', width: 128, type: ValueType.Image,},
  {value: 'improveEvidenceDescribe', labelKey: 'improveEvidenceDescribe', width: 210, type: ValueType.FixedText,},
  {value: 'valid', labelKey: 'valid', width: 100, type: ValueType.Valid},
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
