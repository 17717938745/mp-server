<template>
  <div
      style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;"
      id="printContainer">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
      <div style="width: 100%; text-align: right;font-weight: 400;">VMF46-02-06</div>
      <h1 style="text-align: center;">
        <span style="font-size: 36px;">
          生产设备维修履历
<!--            <el-icon style="vertical-align: middle" @click="handleCopyLink">
              <document-copy/>
            </el-icon>-->
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
            label-class-name="maintain-print-label description-print-border"
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
import {ref,} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType} from '@/store'
import {ElMessage} from 'element-plus'
import {useRoute, useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewContent from '../../component/ViewContent.vue'
import {handleColumnConfigList} from '@/util/IndustryUtil'
import {DocumentCopy, Edit,} from '@element-plus/icons-vue'
import {dousonFullUrl} from '@/util/EnvUtil'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const idKey = 'maintainId'
const data = ref({})
const columnConfigList = ref<ViewConfig[]>([
  {value: 'date', labelKey: 'date', width: 100,},
  {value: 'equipmentNo', labelKey: 'equipmentNo', width: 156,},
  {value: 'positionFormat', labelKey: 'equipmentPosition', width: 198,},
  {value: 'equipmentName', labelKey: 'equipmentName', width: 187,},
  {value: 'brokenReasonFormat', labelKey: 'brokenReason', width: 212, showOverflow: true,},
  {value: 'brokenContent', labelKey: 'brokenContent', width: 213, showOverflow: true,},
  {value: 'repairContent', labelKey: 'repairContent', width: 213, showOverflow: true,},
  {value: 'replacePair', labelKey: 'replacePair', width: 213, showOverflow: true,},
  {value: 'repairTypeFormat', labelKey: 'repairType', width: 187,},
  {value: 'stopHour', labelKey: 'stopHour', width: 100,},
  {value: 'partyUserFormat', labelKey: 'partyUser', width: 198,},
  {value: 'photoList', labelKey: 'photo', width: 128, type: ValueType.Image,},
  {value: 'fileList', labelKey: 'attachment', width: 128, type: ValueType.Attachment,},
])
const handleGet = () => {
  httpGet(`douson/maintain`, {maintainId: route.query.maintainId}).then(
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
}
handleGet()

/*const handleCopyLink = () => {
  const text = dousonFullUrl(`/industry/public/maintain?maintainId=${route.query.maintainId}`)
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
}*/
</script>

<style lang="scss">
.maintain-print-label {
  width: 138px;
}

@media print {
  title, .el-drawer__close-btn .print-hide {
    display: none;
  }
  body {
    height: auto;
  }
}
</style>
