<template>
  <div id="printDescription" style="width: 100%; display: flex; justify-content: center; align-items: center;">
    <div class="douson-print" style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 987px; border: 2px solid #ee1111; margin: 20px 0 50px; font-size: 12px; word-break: break-all;">
      <div class="line">
        <div style="width: 60px; margin-left: 10px;">
          <img :src="'/third/img/douson.png'" style="width: 100%; height: 100%;" alt="">
        </div>
        <div class="col flex-column-start" style="width: 100%; padding: 10px 5px; font-weight: 600; align-items: center; justify-content: center;">
          <div style="font-size: 14px;">
            Production warehousing Inspection Sheet
          </div>
          <div style="font-size: 14px; color: #222222; margin-top: 10px">
            <div style="padding: 2px;">
              Biểu báo cáo sản xuất nhập kho
            </div>
          </div>
        </div>
        <div class="flex-center" style="width: 160px;">
          VMF47-13-05 Rev.B
        </div>
      </div>
      <div class="line flex-start">
        <div class="col title" style="width: 200px;">
          Customer （Tên khách hàng:）
        </div>
        <div class="col" style="justify-content: center; width: 180px;">
          {{ printData.customerShortName }}
        </div>
        <div class="col title" style="width: 200px;">
          PO NO ( Đơn đặt hàng khách )
        </div>
        <div class="col" style="justify-content: center; width: 120px;">
          {{ printData.customerOrderNo }}
        </div>
        <div class="col" style="justify-content: center; width: 40px;">
          {{ printData.customerProjectSequence }}
        </div>
        <div class="col title" style="width: 130px;">
          Số phiếu nhập kho
        </div>
        <div class="col" style="justify-content: center; width: 16%;">
          {{ printData.checkOrderNo }}
        </div>
      </div>
      <div class="line col-group" style="font-size: 12px; justify-content: center; align-items: center; text-align: center;">
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Seq.
            </div>
            <div>
              STT
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 35px;">
            {{ i + 1 }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Order No
            </div>
            <div>
              Đơn hàng tiêu thụ
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 90px;">
            {{ d.saleOrderNo }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Item
            </div>
            <div>
              Mục
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 50px">
            {{ d.orderProjectNo }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Part No
            </div>
            <div>
              Mã vật liệu
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 100px;">
            {{ d.materialNo }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Description
            </div>
            <div>
              Mô tả vật liệu
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 170px;">
            {{ d.improveMaterialDescribe }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Drawing NO.
            </div>
            <div>
              Bản vẽ
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 90px;">
            {{ d.designNumber }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Heat No.
            </div>
            <div>
              Số lò nhiệt
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 80px;">
            {{ d.stoveNo }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Heat Lot No
            </div>
            <div>
              Số lô xử lý nhiệt
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 90px;">
            {{ d.hotBatchNo }}
          </div>
        </div>
        <div class="col flex-column-start">
          <div class="item-label">
            <div>
              Qty
            </div>
            <div>
              Số lượng liệu
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item" style="width: 75px;">
            {{ d.materialCount }}
          </div>
        </div>
        <div class="col flex-column-start" style="width: 30%">
          <div class="item-label">
            <div>
              Serial No
            </div>
            <div>
              ( Số Serial )
            </div>
          </div>
          <div v-for="(d, i) in printList" class="item">
            {{ d.serialNo }}
          </div>
        </div>
      </div>
      <div class="line" style="font-size: 12px;">
        <div class="flex-start col-group" style="width: 100%">
          <div class="col flex-column-start" style="width: 30%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                Item (confirmed by the applicant)
              </div>
              <div>
                hạng mục (do người kiểm tra tick chọn)
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                √
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Hardness┇độ cứng
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                √
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Product marking┇Khắc dâu
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                √
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Size/appearance┇Chất lượng/Ngoại quan
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                {{ printData.nde || '&nbsp;' }}
              </div>
              <div class="col bottom-item" style="width: 80%;">
                NDE
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                √
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Surface treatment┇Xử lý bề mặt
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                {{ printData.assemble || '&nbsp;' }}
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Assembly ┇đóng gói
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                {{ printData.testPress || '&nbsp;' }}
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Pressure test ┇thử áp suất
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                {{ printData.surfaceTreatment || '&nbsp;' }}
              </div>
              <div class="col bottom-item" style="width: 80%;">
                Paint ┇sơn
              </div>
            </div>
            <div class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="col bottom-item flex-center" style="width: 20%;">
                √
              </div>
              <div class="col bottom-item" style="width: 80%;">
                MTR
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 10%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                Qty.
              </div>
              <div>
                Số lượng
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 12%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                Pass
              </div>
              <div>
                Số lượng phù hợp
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 12%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                PassScrap
              </div>
              <div>
                Báo phế
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 12%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                Scrap
              </div>
              <div>
                Báo phế
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 12%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                Reject
              </div>
              <div>
                Trả hàng
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 12%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                NCR NO.
              </div>
              <div>
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
          <div class="col flex-column-start" style="width: 13%;">
            <div class="flex-column-start" style="height: 40px;">
              <div>
                signature
              </div>
              <div>
                Ký Tên
              </div>
            </div>
            <div v-for="(d, i) in bottomColList" :key="i" class="flex-start" style="border-top: 1px solid #666666; width: 100%;">
              <div class="bottom-item" style="width: 100%;">
                &nbsp;
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="line" style="font-size: 12px;">
        <div class="col" style="width: 34%;">
          <div>
            applicant┇người nộp đơn: Ngân Trang
          </div>
        </div>
        <div class="col" style="width: 33%;">
          <div>
            Warehouse┇kho:
          </div>
        </div>
        <div class="col" style="width: 33%;">
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
import {ElMessage} from "element-plus"

const bottomColList = [0, 1, 2, 3, 4, 5, 6, 7, 8]
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

</script>

<style scoped lang="scss">
.douson-print {
  .flex-column-start {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: flex-start;
  }

  .title {
    color: #1f63e1;
  }

  .flex-center {
    display: flex;
    justify-content: center;
    flex-wrap: nowrap;
    align-items: center;
  }

  .flex-start {
    display: flex;
    justify-content: flex-start;
    flex-wrap: nowrap;
    align-items: center;
  }

  .line {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    border-bottom: 1px solid #666666;

    &:last-child {
      border-bottom: none;
    }
  }

  .col {
    padding: 10px 5px;
    display: flex;
    flex-wrap: nowrap;
    justify-items: center;
    align-items: center;
    border-right: 1px solid #666666;

    &:last-child {
      border: none;
    }
  }

  .col {
    padding: 10px 5px;
    display: flex;
    flex-wrap: nowrap;
    justify-items: center;
    align-items: center;
    border-right: 1px solid #666666;

    &:last-child {
      border: none;
    }
  }

  .col-group {
    .col {
      padding: 0;
    }
  }

  .item-label {
    padding: 5px;
    width: 100%;
  }

  .item {
    font-size: 10px;
    padding: 5px;
    height: 50px;
    border-top: 1px solid #666666;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .bottom-item {
    font-size: 10px;
    height: 25px;
    margin-left: 5px;
  }

}
</style>
