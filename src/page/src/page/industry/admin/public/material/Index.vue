<template>
  <div id="printDescription" class="douson-flex-item-center" style="width: 100%;">
    <div class="douson-container douson-flex-column" style=" width: 987px; border: 2px solid #ee1111; margin-top: 20px; font-size: 12px; word-break: break-all;">
      <div class="douson-row douson-flex-item-center" style="width: 100%;">
        <img :src="fullUrl('/third/img/douson.png', '')"
             style="height: 50px;"
             alt="logo"
        />
        <div class="douson-column douson-flex-item-column-center" style="width: 70%;">
          <div class="douson-row douson-flex-item-center" style="font-size: 12px; color: #00aa00; padding: 5px 0; width: 100%;">
            DOUSON VIETNAM WELLHEAD EQUIPMENT CO.,LTD
          </div>
          <div class="douson-row douson-flex-item-column-center" style="font-size: 14px; ">
            <div style="padding: 2px;">THE REQUISITION OF PRODUCTION MATERIAL</div>
            <div style="padding: 2px;">ĐƠN LĨNH LIỆU</div>
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 180px;">
          VMF47-08-01 Rev. B
        </div>
      </div>
      <div class="douson-row douson-flex-item-center douson-column-height-10" style="width: 100%;">
        <div class="douson-column douson-flex-item" style="width: 20%;">PO NO ( Đơn đặt hàng khách ) :</div>
        <div class="douson-column douson-flex-item-center" style="width: 15%;">
          {{ printData.customerOrderNo }}
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 5%;">
          {{ printData.customerProjectSequence }}
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 15%;">
          Số phiếu lĩnh liệu
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 20%;">
          &nbsp;
        </div>
        <div class="douson-column douson-flex-item-center" style="width: 25%;">
          {{ printData.materialOrderNo }}
        </div>
      </div>
      <div class="douson-row douson-flex-item douson-column-padding-0" style="width: 100%;">
        <div class="douson-column douson-flex-item-column douson-column-padding-0" style="width: 30%;">
          <div class="douson-row douson-column douson-flex-item" style="width: 100%;">
            <div class="douson-column douson-flex-item-column" style="width: 35%;">
              <div>Order No</div>
              <div>Đơn hàng tiêu thụ</div>
            </div>
            <div class="douson-column douson-flex-item-center" style="width: 40%;">
              {{ printData.saleOrderNo }}
            </div>
            <div class="douson-column douson-flex-item-center" style="width: 20%;">
              {{ printData.orderProjectNo }}
            </div>
          </div>
          <div class="douson-row douson-column douson-flex-item" style="width: 100%;">
            <div class="douson-column douson-flex-item-column" style="width: 40%;">
              <div>Part No</div>
              <div>Mã vật liệu</div>
            </div>
            <div class="douson-column douson-flex-item-center" style="width: 60%;">
              {{ printData.materialNo }}
            </div>
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 20%;">
          <div>
            Description
          </div>
          <div>
            Mô tả vật liệu
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 25%;">
          {{ printData.improveMaterialDescribe }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 30%;">
          <div class="douson-row douson-flex-item" style="width: 100%;">
            <div class="douson-column douson-flex-item-column-center" style="width: 65%;">
              <div>
                Qty.
              </div>
              <div>
                Tổng số lượng lĩnh vật liệu
              </div>
            </div>
            <div class="douson-column douson-flex-item-column-center" style="width: 35%; justify-content: center;">
              {{
                (printList || []).reduce((prev, cur, index, arr) => {
                  return (prev || 0) + (cur.materialCount || 0)
                }, 0).toFixed(0)
              }}
            </div>
          </div>
          <div class="douson-row douson-flex-item" style="width: 100%;">
            <div class="douson-column douson-flex-item-column-center" style="width: 65%;">
              <div>
                Drawing NO.
              </div>
              <div>
                Bản vẽ
              </div>
            </div>
            <div class="douson-column douson-flex-item-column-center" style="width: 35%; justify-content: center;">
              <div>{{ printData.designNumber }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="douson-row douson-flex-item" style="width: 100%;">
        <div class="douson-column douson-flex-item-column-center" style="width: 5%;">
          <div>
            Seq.
          </div>
          <div>
            STT
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 15%;">
          <div>
            Component No
          </div>
          <div>
            Mã vật liệu phôi
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 25%;">
          <div>
            Component description
          </div>
          <div>
            Mô tả vật liệu phôi
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 10%;">
          <div>
            Drawing No
          </div>
          <div>
            Bản vẽ phôi
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 8%;">
          <div>
            Qty
          </div>
          <div>
            Số lượng liệu
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 9%;">
          <div>
            Heat Lot No
          </div>
          <div>
            Số lô xử lý nhiệt
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 7%;">
          <div>
            Heat No.
          </div>
          <div>
            Số lò nhiệt
          </div>
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 20%;">
          <div>
            Serial No
          </div>
          <div>
            ( Số Serial )
          </div>
        </div>
      </div>
      <div v-for="(d, i) in printList" :key="`item-${i}`" class="douson-row douson-flex-item" style="width: 100%;">
        <div class="douson-column douson-flex-item-column-center" style="width: 5%;">
          {{ i + 1 }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 15%;">
          {{ d.blankMaterialNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 25%;">
          {{ d.blankMaterialDescribe }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 10%;">
          {{ d.roughcastDesignNumber }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 8%;">
          {{ d.materialCount }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 9%;">
          {{ d.stoveNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 7%;">
          {{ d.hotBatchNo }}
        </div>
        <div class="douson-column douson-flex-item-column-center" style="width: 20%;">
          {{ d.serialNo }}
        </div>
      </div>
      <div class="douson-row douson-flex-item" style="width: 100%;">
        <div class="douson-column douson-flex-item" style="width: 25%;">
          <div class="douson-flex-item-column" style="width: 50%;">
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
          <div class="douson-flex-item-column-center" style="width: 50%;">
            {{ printData.creatorFormat }}
          </div>
        </div>
        <div class="douson-column douson-flex-item" style="width: 25%;">
          <div class="douson-flex-item-column" style="width: 50%;">
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
        </div>
        <div class="douson-column douson-flex-item" style="width: 25%;">
          <div class="douson-flex-item-column" style="width: 50%;">
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
        </div>
        <div class="douson-column douson-flex-item" style="width: 25%;">
          <div class="douson-flex-item-column" style="width: 50%;">
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

</style>
