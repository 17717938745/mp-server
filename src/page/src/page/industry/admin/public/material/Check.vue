<template>
  <div id="printDescription" class="douson-flex-item-center" style="width: 100%;">
    <div class="douson-container douson-flex-column" style=" width: 987px; border: 2px solid #ee1111; margin-top: 20px; font-size: 12px; word-break: break-all;">
      <div class="douson-row douson-flex-item-center" style="width: 100%;">
        <img :src="fullUrl('/third/img/douson.png', '')"
             style="height: 50px;"
             alt="logo"
        />
        <div class="douson-column douson-flex-item-column-center" style="font-weight: 600; width: 70%;">
          <div class="douson-row douson-flex-item-center" style="font-size: 14px; padding: 5px 0; width: 100%;">
            Production warehousing Inspection Sheet
          </div>
          <div class="douson-row douson-flex-item-column-center" style="font-size: 14px; color: #222222; margin-top: 10px">
            Biểu báo cáo sản xuất nhập kho
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 180px;">
          VMF47-13-05 Rev.B
        </div>
      </div>
      <div class="douson-row douson-flex-item-center douson-column-height-10" style="width: 100%;">
        <div class="douson-column douson-flex-item" style="color: #1f63e1; width: 18%;">
          Customer （Tên khách hàng:）
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 15%;">
          {{ printData.customerShortName }}
        </div>
        <div class="douson-column douson-flex-item" style="color: #1f63e1; width: 18%;">
          PO NO ( Đơn đặt hàng khách )
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 13%;">
          {{ printData.customerOrderNo }}
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 5%;">
          {{ printData.customerProjectSequence }}
        </div>
        <div class="douson-column douson-flex-item" style="color: #1f63e1; width: 16%;">
          Số phiếu nhập kho
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 15%;">
          {{ printData.checkOrderNo }}
        </div>
      </div>
      <div class="douson-row douson-flex-item" style="width: 100%; text-align: center;">
        <div class="douson-column douson-flex-item-column-center" style="width: 4%;">
          <div>
            Seq.
          </div>
          <div>
            STT
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 8%;">
          <div>
            Order No
          </div>
          <div>
            Đơn hàng tiêu thụ
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 4%;">
          <div>
            Item
          </div>
          <div>
            Mục
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 15%;">
          <div>
            Part No
          </div>
          <div>
            Mã vật liệu
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 15%;">
          <div>
            Description
          </div>
          <div>
            Mô tả vật liệu
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 13%;">
          <div>
            Drawing NO.
          </div>
          <div>
            Bản vẽ
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 11%;">
          <div>
            Heat No.
          </div>
          <div>
            Số lò nhiệt
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 11%;">
          <div>
            Heat Lot No
          </div>
          <div>
            Số lô xử lý nhiệt
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 5%;">
          <div>
            Qty
          </div>
          <div>
            Số lượng liệu
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 12%;">
          <div>
            Serial No
          </div>
          <div>
            ( Số Serial )
          </div>
        </div>
      </div>
      <div v-for="(d, i) in printList" :key="`item-${i}`" class="douson-row douson-flex-item" style="width: 100%; text-align: center;">
        <div class="douson-column douson-flex-item-column-center" style="width: 4%;">
          {{ i + 1 }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 8%;">
          {{ d.saleOrderNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 4%;">
          {{ d.orderProjectNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 15%;">
          {{ d.materialNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 15%;">
          {{ d.improveMaterialDescribe }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 13%;">
          {{ d.designNumber }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 11%;">
          {{ d.stoveNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 11%;">
          {{ d.hotBatchNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 5%;">
          {{ d.materialCount }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 12%;">
          {{ d.serialNo }}
        </div>
      </div>
      <div class="douson-row douson-flex-item" style="width: 100%;">
        <div class="douson-column douson-flex-item" style="width: 44%; padding: 0;">
          <div class="douson-column douson-flex-item-center" style="width: 10%;"></div>
          <div class="douson-column douson-flex-item-column-center" style="width: 90%; align-items: flex-start;">
            <div>
              Item (confirmed by the applicant)
            </div>
            <div>
              hạng mục (do người kiểm tra tick chọn)
            </div>
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 8%;">
          <div>
            Qty.
          </div>
          <div>
            Số lượng
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 12%;">
          <div>
            Pass
          </div>
          <div>
            Số lượng phù hợp
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 7%;">
          <div>
            PassScrap
          </div>
          <div>
            Báo phế
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 7%;">
          <div>
            Scrap
          </div>
          <div>
            Báo phế
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 7%;">
          <div>
            Reject
          </div>
          <div>
            Trả hàng
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 7%;">
          <div>
            NCR NO.
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 8%;">
          <div>
            signature
          </div>
          <div>
            Ký Tên
          </div>
        </div>
      </div>
      <div v-for="(d, i) in bottomColList" :key="`bottom-${i}`" class="douson-row douson-flex-item" style="width: 100%; text-align: center;">
        <div class="douson-column douson-flex-item-column-center" style="width: 44%; padding: 0;">
          <div class="douson-flex-item" style="width: 100%; padding: 0; border: none;">
            <div class="douson-column douson-flex-item-center" style="width: 10%;">
              {{ d.valueFunction(printData) }}
            </div>
            <div class="douson-column douson-flex-item-column-center" style="width: 90%; align-items: flex-start;">
              {{ d.label }}
            </div>
          </div>
        </div>
        <div class="douson-column" style="width: 8%;">
        </div>
        <div class="douson-column" style="width: 12%;">
        </div>
        <div class="douson-column" style="width: 7%;">
        </div>
        <div class="douson-column" style="width: 7%;">
        </div>
        <div class="douson-column" style="width: 7%;">
        </div>
        <div class="douson-column" style="width: 7%;">
        </div>
        <div class="douson-column" style="width: 8%;">
        </div>
      </div>
      <div class="douson-row douson-flex-item douson-column-height-10" style="width: 100%;">
        <div class="douson-column douson-flex-item" style="width: 40%;">
          <div style="width: 60%;">
            applicant┇người nộp đơn: Ngân Trang
          </div>
          <div style="width: 40%;">{{ printData.creatorFormat }}</div>
        </div>
        <div class="douson-column douson-flex-item" style="width: 30%;">
          <div>
            Warehouse┇kho:
          </div>
        </div>
        <div class="douson-column douson-flex-item" style="width: 30%;">
          <div>
            Accountant┇Kế toán:
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="tsx" setup>
import {ref} from 'vue'
import {Store, useStore} from 'vuex'
import {StoreType,} from '@/store'
import {useRoute, useRouter} from 'vue-router'
import {httpGet} from '@/util/HttpUtil'
import {ElMessage} from 'element-plus'
import {fullUrl} from '@/util/EnvUtil'

const bottomColList = [
  {
    valueFunction: (printData: any) => {
      return '√'
    },
    label: 'Hardness┇độ cứng',
  },
  {
    valueFunction: (printData: any) => {
      return '√'
    },
    label: 'Product marking┇Khắc dâu',
  },
  {
    valueFunction: (printData: any) => {
      return '√'
    },
    label: 'Size/appearance┇Chất lượng/Ngoại quan',
  },
  {
    valueFunction: (printData: any) => {
      return printData.nde || ' '
    },
    label: 'NDE',
  },
  {
    valueFunction: (printData: any) => {
      return '√'
    },
    label: 'Surface treatment┇Xử lý bề mặt',
  },
  {
    valueFunction: (printData: any) => {
      return printData.assemble || ' '
    },
    label: 'Assembly ┇đóng gói',
  },
  {
    valueFunction: (printData: any) => {
      return printData.testPress || ' '
    },
    label: 'Pressure test ┇thử áp suất',
  },
  {
    valueFunction: (printData: any) => {
      return printData.surfaceTreatment || ' '
    },
    label: 'Paint ┇sơn',
  },
  {
    valueFunction: (printData: any) => {
      return '√'
    },
    label: 'MTR',
  },
]
const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const printList = ref<any>([])
const printData = ref<any>({})
httpGet(`douson/material/check`, {checkOrderNo: route.query.checkOrderNo}).then(
    (res: any) => {
      printList.value = res.list || []
      printData.value = printList.value.length > 0 ? printList.value[0] : {}
      ElMessage.success({
        message: 'Query success',
        duration: 1000,
      })
      setTimeout(() => {
        const heightPx = (document.getElementById('printDescription')?.offsetHeight || 1024) + 450 + 'px'
        const printContainer = document.getElementById('printContainer')
        if (printContainer) {
          printContainer.style.height = heightPx
        }
      }, 1000)
    }
)

</script>

<style scoped lang="scss">
</style>
