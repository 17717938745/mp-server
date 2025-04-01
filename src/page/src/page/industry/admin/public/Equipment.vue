<template>
  <div style="display: flex; flex-direction: column; align-items: center; justify-content: flex-start; margin-bottom: 20px; position: absolute; width: 100%; top: 20px;" id="printContainer">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 595px;">
      <div style="width: 100%; text-align: right;font-weight: 400;">VMF46-02-01/A</div>
      <h1 style="text-align: center;">
        <span style="font-size: 36px;">
          生产设备台账
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
            label-class-name="equipment-print-label description-print-border"
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
import {StoreType} from '@/store/Index'
import {ElMessage} from 'element-plus'
import {useRoute, useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {DataResult} from '@/typing/ma/System'
import {ValueType, ViewConfig} from '@/typing/industry/ViewItem'
import ViewContent from '../../component/ViewContent.vue'
import {handleColumnConfigList} from '@/util/IndustryUtil'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const roleCodeList = store.state.roleCodeList
const idKey = 'equipmentId'
const data = ref({})
const columnConfigList = ref<ViewConfig[]>([
  {value: 'equipmentNo', labelKey: 'equipmentNo', width: 100,},
  {value: 'equipmentName', labelKey: 'equipmentName', width: 100,},
  {value: 'specification', labelKey: 'specification', width: 100,},
  {value: 'date', labelKey: 'date', width: 100,},
  {value: 'useUserFormat', labelKey: 'useUser', width: 100,},
  {value: 'remark', labelKey: 'description', width: 100,},
  {value: 'detailDescribe', labelKey: 'equipmentDetailDescribe', width: 100,},
  {value: 'gasolineType', labelKey: 'gasolineType', width: 100,},
  {value: 'chargeUserFormat', labelKey: 'chargeUser', width: 100,},
  {value: 'positionFormat', labelKey: 'equipmentPosition', width: 100,},
  {value: 'photoList', labelKey: 'photo', width: 128, type: ValueType.Image,},
  {value: 'fileList', labelKey: 'supportAttachment', width: 128, type: ValueType.Attachment,},
  {value: 'apiDeviceFormat', originValue: 'apiDevice', labelKey: 'apiDevice', width: 127,},
])
/*const userOptionList = ref(new Array<any>())
Promise.all([
  () => {
  },
  httpGet(`system/user/config/list`, {}),
]).then((l: any) => {
  userOptionList.value = (l[1].list || []).map((t: any) => {
    return {
      value: t.userId,
      label: t.name,
    }
  })
  handleGet()
})*/
const handleGet = () => {
  httpGet(`douson/equipment`, {equipmentId: route.query.equipmentId}).then(
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
</script>

<style lang="scss">
.equipment-print-label {
  width: 139px;
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
