<template>
  <div id="printDescription" style="width: 100%; display: flex; justify-content: center; align-items: center;">
    <div class="douson-print" style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 987px; border: 2px solid #ee1111; margin-top: 20px; font-size: 12px; word-break: break-all;">
      <div class="line">
        <div style="width: 60px;">
          <img :src="'/third/img/douson.png'" style="width: 100%; height: 100%;" alt="">
        </div>
        <div style="margin-left: 10px; width: 70%; text-align: center; border-right: 1px solid #aaaaaa;">
          <div style="font-size: 12px; color: #00aa00; border-bottom: 1px solid #aaaaaa; padding: 5px 0;">
            DOUSON VIETNAM WELLHEAD EQUIPMENT CO.,LTD
          </div>
          <div style="font-size: 14px; color: #222222; margin-top: 10px">
            <div style="padding: 2px;">THE REQUISITION OF PRODUCTION MATERIAL</div>
            <div style="padding: 2px;">ĐƠN LĨNH LIỆU</div>
          </div>
        </div>
        <div class="flex-center" style="width: 180px; margin-left: 10px;">
          VMF47-08-01 Rev. B
        </div>
      </div>
      <div class="line line-first">
        <div class="col" style="width: 190px;">PO NO ( Đơn đặt hàng khách ) :</div>
        <div class="col" style="width: 170px; justify-content: center;">
          {{ printData.customerOrderNo }}
        </div>
        <div class="col" style="width: 60px; justify-content: center;">
          {{ printData.customerProjectSequence }}
        </div>
        <div class="col" style="width: 110px; height: 25px; justify-content: center;">
          Số phiếu lĩnh liệu
        </div>
        <div class="col" style="width: 160px; justify-content: center;">
          &nbsp;
        </div>
        <div class="col" style="width: 30%; text-align: center; justify-content: center;">
          {{ printData.checkOrderNo }}
        </div>
      </div>
      <div class="line col-group" style="font-size: 12px;">
        <div class="col" style="flex-wrap: wrap; width: 460px;">
          <div class="flex-start" style="width: 100%;">
            <div class="col-up flex-column-start" style="width: 40%; height: 40px; align-items: flex-start;">
              <div>Order No</div>
              <div>Đơn hàng tiêu thụ</div>
            </div>
            <div class="flex-start" style="width: 60%;">
              <div class="col-up" style="width: 66%; justify-content: center;">
                {{ printData.saleOrderNo }}
              </div>
              <div class="col-up" style="width: 34%; justify-content: center;">
                {{ printData.orderProjectNo }}
              </div>
            </div>
          </div>
          <div class="flex-start" style="width: 100%;">
            <div class="col-down flex-column-start" style="width: 40%; align-items: flex-start;">
              <div>Part No</div>
              <div>Mã vật liệu</div>
            </div>
            <div class="col-down flex-column-start" style="width: 60%; justify-content: center;">
              {{ printData.materialNo }}
            </div>
          </div>
        </div>
        <div class="col col-combine flex-column-start" style="width:270px;">
          <div>
            Description
          </div>
          <div>
            Mô tả vật liệu
          </div>
        </div>
        <div class="col col-combine flex-column-start" style="width: 360px;">
          {{ printData.improveMaterialDescribe }}
        </div>
        <div class="col" style="width: 360px; flex-wrap: wrap;">
          <div class="flex-start" style="width: 100%;">
            <div class="col col-up flex-column-start" style="width: 70%;">
              <div>
                Qty.
              </div>
              <div>
                Tổng số lượng lĩnh vật liệu
              </div>
            </div>
            <div class="col col-up flex-start" style="width: 30%; justify-content: center;">
              {{
                (printList || []).reduce((prev, cur, index, arr) => {
                  return (prev || 0) + (cur.materialCount || 0)
                }, 0).toFixed(0)
              }}
            </div>
          </div>
          <div class="flex-start" style="width: 100%;">
            <div class="col col-down flex-column-start" style="width: 70%;">
              <div>
                Drawing NO.
              </div>
              <div>
                Bản vẽ
              </div>
            </div>
            <div class="col col-down flex-column-start" style="width: 30%; justify-content: center;">
              <div>{{ printData.designNumber }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="line col-group" style="font-size: 12px;text-align: center;">
        <div class="col" style="width: 50px;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Seq.
              </div>
              <div>
                STT
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ i + 1 }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 140px;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Component No
              </div>
              <div>
                Mã vật liệu phôi
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.blankMaterialNo }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 190px">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Component description
              </div>
              <div>
                Mô tả vật liệu phôi
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.blankMaterialDescribe }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 110px;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Drawing No
              </div>
              <div>
                Bản vẽ phôi
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.roughcastDesignNumber }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 90px;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Qty
              </div>
              <div>
                Số lượng liệu
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.materialCount }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 90px;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Heat No.
              </div>
              <div>
                Số lò nhiệt
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.stoveNo }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 113px;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Heat Lot No
              </div>
              <div>
                Số lô xử lý nhiệt
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.hotBatchNo }}
            </div>
          </div>
        </div>
        <div class="col" style="width: 28%;">
          <div class="flex-column-start" style="width: 100%;">
            <div class="item-label">
              <div>
                Serial No ( Số Serial )
              </div>
              <div>
                &nbsp;
              </div>
            </div>
            <div v-for="(d, i) in printList" class="item">
              {{ d.serialNo }}
            </div>
          </div>
        </div>
      </div>
      <div class="line" style="font-size: 12px;">
        <div class="flex-start">
          <div class="paper-col">
            <div>
              Preparer
            </div>
            <div>
              Người lĩnh liệu :
            </div>
            <div>
              Date
            </div>
            <div>
              Ngày tháng :
            </div>
          </div>
          <div class="paper-col">
            <div>
              Approver
            </div>
            <div>
              Người phê duyệt :
            </div>
            <div>
              Date
            </div>
            <div>
              Ngày tháng :
            </div>
          </div>
          <div class="paper-col">
            <div>
              Warehouse
            </div>
            <div>
              Người phát liệu :
            </div>
            <div>
              Date
            </div>
            <div>
              Ngày tháng :
            </div>
          </div>
          <div class="paper-col">
            <div>
              Chief Accountant
            </div>
            <div>
              Kế toán trưởng :
            </div>
            <div>
              Date
            </div>
            <div>
              Ngày tháng :
            </div>
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
import {ElMessage} from "element-plus";

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const printList = ref<any>([])
const printData = ref<any>({})
httpGet(`douson/material/index`, {materialOrderNo: route.query.materialOrderNo}).then(
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

  .flex-center {
    display: flex;
    justify-content: center;
    flex-wrap: nowrap;
    align-items: center;
    width: 100%;
  }

  .flex-start {
    display: flex;
    justify-content: flex-start;
    flex-wrap: nowrap;
    align-items: center;
  }

  .line-first {
    .col {
      height: 25px;
    }
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
    padding: 5px;
    display: flex;
    flex-wrap: nowrap;
    justify-items: center;
    align-items: center;
    border-right: 1px solid #666666;

    &:last-child {
      border-right: none;
    }
  }

  .col-up {
    padding: 5px;
    display: flex;
    flex-wrap: nowrap;
    justify-items: center;
    align-items: center;
    border-right: 1px solid #666666;

    &:last-child {
      border: none;
    }

    height: 40px;
  }

  .col-down {
    padding: 5px;
    display: flex;
    flex-wrap: nowrap;
    justify-items: center;
    align-items: center;
    border-right: 1px solid #666666;
    border-top: 1px solid #666666;

    &:last-child {
      border-right: none;
    }

    height: 40px;
  }

  .col-combine {
    height: 100px;
    padding: 5px;
  }

  .col-group > .col {
    padding: 0;
  }

  .item-label {
    padding: 5px;
  }

  .header {
    padding: 5px;
    height: 80px;
    border-top: 1px solid #666666;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .header-label {
    padding: 5px;
  }

  .item {
    padding: 0;
    height: 80px;
    width: 100%;
    border-top: 1px solid #666666;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .paper-col {
    padding: 5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: flex-start;
    width: 250px;
    border-right: 1px solid #666666;

    &:last-child {
      border: none;
    }
  }
}
</style>
