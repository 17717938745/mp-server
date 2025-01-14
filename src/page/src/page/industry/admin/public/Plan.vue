<template>
  <div style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;" id="printContainer">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
      <h1 style="text-align: center;">
        <span style="font-size: 36px;" @click="handleCopyLink">
          精益持续改善 Cải thiện sai lệch
            <el-icon style="vertical-align: middle">
              <document-copy/>
            </el-icon>
        </span>
      </h1>
      <div class="douson-serial-no">{{ data.serialNo}}</div>
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
            label-class-name="plan-print-label description-print-border"
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
const idKey = 'planId'
const columnConfigList = ref<ViewConfig[]>([
  {value: 'createDate', label: store.state.label.createDate, labelKey: 'createDate', width: 98},
  {value: 'creatorFormat', label: store.state.label.username, labelKey: 'username', width: 110},
  {value: 'departmentFormat', label: store.state.label.department, labelKey: 'department', width: 154},
  {value: 'optimizeTypeFormat', label: store.state.label.optimizeType, labelKey: 'optimizeType', width: 128},
  {value: 'existsProblem', label: store.state.label.existsProblem, labelKey: 'existsProblem', width: 334, type: ValueType.Text, showOverflow: true,},
  {value: 'solveScheme', label: store.state.label.solveScheme, labelKey: 'solveScheme', width: 334, type: ValueType.Text, showOverflow: true,},
  {value: 'responsiblePersonFormat', label: store.state.label.responsiblePerson, labelKey: 'responsiblePerson', width: 128},
  {value: 'planCompleteTime', label: store.state.label.planCompleteTime, labelKey: 'planCompleteTime', width: 98},
  {value: 'awardAmountFormat', label: store.state.label.awardAmount, labelKey: 'awardAmount', width: 87},
  {value: 'valid', label: store.state.label.valid, labelKey: 'valid', width: 68, type: ValueType.Valid,},
  {value: 'beforePhotoList', label: store.state.label.photoList, labelKey: 'beforePlanThreePhoto', width: 269, type: ValueType.Image,},
  {value: 'afterPhotoList', label: store.state.label.photoList, labelKey: 'afterPlanThreePhoto', width: 269, type: ValueType.Image,},
  {value: 'attachmentList', label: store.state.label.photoList, labelKey: 'supportAttachment', width: 269, type: ValueType.Attachment,},
])
const data = ref({})
httpGet(`douson/plan`, {planId: route.query.planId}).then(
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
  const text = dousonFullUrl(`/industry/public/plan?planId=${route.query.planId}`)
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
.plan-print-label {
  width: 137px;
}
</style>
