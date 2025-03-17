<template>
  <div id="printContainer" class="douson-flex" style="margin-top: 20px;">
    <div class="douson-flex-item-column-center" style="width: 100%;">
      <h1 style="text-align: center;">
        <span style="font-size: 36px;">
          整机装配记录 Ghi chép lắp ráp máy
        </span>
      </h1>
      <div class="douson-serial-no">{{ data.serialNo }}</div>
      <el-descriptions
          :column="1"
          border
          id="printDescription"
      >`
        <el-descriptions-item
            v-for="(viewConfig, i) in handleColumnConfigList(columnConfigList, [], true, false, [], [])"
            :key="viewConfig.value"
            :label="viewConfig.labelKey ? store.state.label[viewConfig.labelKey] : viewConfig.label"
            align="center"
            label-class-name="assembly-print-label description-print-border fixed-width"
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
import {StoreType} from '@/store/Index'
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
const idKey = 'assemblyId'
const columnConfigList = ref<ViewConfig[]>([
  {value: 'serialNumber', labelKey: 'serialNumber', width: 154},
  {value: 'purchaseOrderNo', labelKey: 'purchaseOrderNo', width: 98},
  {value: 'poProject', labelKey: 'poProject', width: 56},
  {value: 'saleOrderNo', labelKey: 'saleOrderNo', width: 102},
  {value: 'orderProject', labelKey: 'orderProject', width: 56},
  {value: 'materialNo', labelKey: 'materialNo', width: 98},
  {value: 'materialDescription', labelKey: 'materialDescription', width: 189},
  {value: 'designNumber', labelKey: 'designNumber', width: 102},
  {value: 'deliveryDate', labelKey: 'deliveryDate', width: 102},
  {value: 'orderCount', labelKey: 'orderCount', width: 47},
  {value: 'completedQty', labelKey: 'completedQty', width: 47},
  {value: 'description', labelKey: 'description', width: 189},
  {value: 'valveBody', labelKey: 'valveBody', width: 121},
  {value: 'valveBodyPhotoList', labelKey: 'valveBodyPhoto', width: 131, type: ValueType.PictureText, textLabel: 'valveBody',},
  {value: 'valveCover', labelKey: 'valveCover', width: 121},
  {value: 'valveCoverPhotoList', labelKey: 'valveCoverPhoto', width: 151, type: ValueType.PictureText, textLabel: 'valveCover',},
  {value: 'gate', labelKey: 'gate', width: 121},
  {value: 'gatePhotoList', labelKey: 'gatePhoto', width: 189, type: ValueType.PictureText, textLabel: 'gate',},
  {value: 'valveSeat', labelKey: 'valveSeat', width: 121},
  {value: 'valveSeatPhotoList', labelKey: 'valveSeatPhoto', width: 189, type: ValueType.PictureText, textLabel: 'valveSeat',},
  {value: 'valveStem', labelKey: 'valveStem', width: 189},
  {value: 'valveStemPhotoList', labelKey: 'valveStemPhoto', width: 189, type: ValueType.PictureText, textLabel: 'valveStem',},
  {value: 'assemblyPersonFormat', labelKey: 'assemblyPerson', width: 189},
  {value: 'assemblyCompleteDate', labelKey: 'assemblyCompleteDate', width: 189},
  {value: 'pressureTestPhotoList', labelKey: 'pressureTestPhoto', width: 189, type: ValueType.PictureText,},
  {value: 'torqueNm', labelKey: 'torqueNm', width: 189},
  {value: 'oilInjectionPhotoList', labelKey: 'oilInjection', width: 189, type: ValueType.PictureText, textLabel: 'oilInjection',},
  {value: 'testerFormat', labelKey: 'tester', width: 189},
  {value: 'oilInjectionCompleteDate', labelKey: 'oilInjectionCompleteDate', width: 189},

])
const data = ref({})
httpGet(`douson/assembly`, {assemblyId: route.query.assemblyId}).then(
    (res: DataResult<any>) => {
      data.value = res.data || {}
      ElMessage.success("Query success")
    }
)

</script>

<style lang="scss">
.assembly-print-label {
}

.fixed-width {
  width: 90px;
}
</style>
