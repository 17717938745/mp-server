<template>
  <div id="printDescription" class="douson-flex douson-flex-item-center">
    <div style="width: 585px;">
      <div class="douson-flex-item-center" style="justify-content: space-between; position: relative; font-size: 36px; color: #222222; margin-top: 20px;">
        <img :src="fullUrl('/third/img/douson.png', '')"
             style="height: 50px;"
             alt="logo"
        />
        <div class="douson-flex-item-column-center">
          <div>物品借出单</div>
          <div>Đơn cho mượn dụng cụ</div>
        </div>
      </div>
      <div class="douson-container" style="font-size: 12px; margin-top: 10px;">
        <div class="douson-flex douson-row">
          <div class="douson-flex-item-column douson-column" style="width: 50%;">
            <div>借用人签字：</div>
            <div>Người mượn：</div>
          </div>
          <div class="douson-flex-item douson-column" style="width: 50%;">
            <div class="douson-flex-item-center">
              借用方(单位) Đơn vị：{{ printData.borrowTemplatePersonFormat }}
            </div>
          </div>
        </div>
        <div class="douson-flex douson-row">
          <div class="douson-flex-item douson-column" style="width: 50%;">
            <div>借用时间 Ngày mượn：</div>
            <div>
              {{ printData.borrowTemplateDate }}
            </div>
          </div>
          <div class="douson-flex-item douson-column" style="width: 50%;">
            <div>
              承诺归还时间 Ngày trả：
            </div>
            <div>
              {{ printData.promiseReturnDate }}
            </div>
          </div>
        </div>
        <div class="douson-flex douson-row">
          <div class="douson-flex-item-column-center douson-column" style="width: 10%;">
            <div>序号</div>
            <div>STT</div>
          </div>
          <div class="douson-flex-item-column-center douson-column" style="width: 20%">
            <div>物料号</div>
            <div>Mã vật liệu</div>
          </div>
          <div class="douson-flex-item-column-center douson-column" style="width: 40%;">
            <div>物品名称</div>
            <div>Miêu tả vật liệu</div>
          </div>
          <div class="douson-flex-item-column-center douson-column" style="width: 10%;">
            <div>数量</div>
            <div>Số lượng</div>
          </div>
          <div class="douson-flex-item-column-center douson-column" style="width: 20%;">
            <div>单号</div>
            <div>Số phiếu</div>
          </div>
        </div>
        <div v-for="printData in printList" :key="printData.templateId" class="douson-flex douson-row">
          <div class="douson-flex-item-center douson-column" style="width: 10%;">
            {{ printData.index || '1' }}
          </div>
          <div class="douson-flex-item-center douson-column" style="width: 20%;">
            {{ printData.materialNo }}
          </div>
          <div class="douson-flex-item-center douson-column" style="width: 40%;">
            {{ printData.materialDescription }}
          </div>
          <div class="douson-flex-item-center douson-column" style="width: 10%;">
            {{ printData.templateCount }}
          </div>
          <div class="douson-flex-item-center douson-column" style="width: 20%;">
            {{ printData.templateOrderNo }}
          </div>
        </div>
        <div class="douson-flex douson-row">
          <div class="douson-flex-item douson-column" style="width: 34%;">
            <div>
              <div>经办人签字：</div>
              <div>Người cho mượn：</div>
            </div>
            <div class="douson-flex-item-center">
              {{ printData.operatorPersonFormat }}
            </div>
          </div>
          <div class="douson-flex-item douson-column" style="width: 33%;">
            <div>
              <div>部门负责人：</div>
              <div>Người phụ trách bộ phận：</div>
            </div>
            <div>
            </div>
          </div>
          <div class="douson-flex-item douson-column" style="width: 33%;">
            <div>
              <div>仓管签字:</div>
              <div>Kho：</div>
            </div>
            <div>
            </div>
          </div>
        </div>
        <div class="douson-flex-column douson-row">
          <div style="width: 100%;">备注 Ghi chú：</div>
          <pre>{{ printData.description }}</pre>
          <div>
            1.借用说明：采购专员参与协调外协所借用工具、量具、刀具、刀片等物品型号，并通知流程中相关参与人员以作备案。原则上仓管员不直接与外协人员打交道。
          </div>
          <div> 1. Mượn đồ: Đối với dụng cụ, dao cụ, chip... Tất cả đều do người phụ trách nhà cung cấp mượn và
            nhận trả, đồng thời thông báo cho kho, nhà cung cấp không được tự ý liên hệ trực tiếp với kho.
          </div>
          <div>
            2.归还说明：外协归还的物品需由采购专员、仓管员共同验收，并拍照入系统。如有损坏、遗失则按规定价格赔偿，采购专员执行跟踪落实。
          </div>
          <div>2. Trả đồ: Khi nhà cung cấp trả đồ phải thông báo cho người phụ trách nhà cung cấp, nhân viên kho
            nhận đồ từ người phụ trách đồng thời tiến hành kiểm tra dụng cụ, chụp ảnh nhập vào hệ thống. Nếu như
            dụng cụ hỏng hóc và có tổn hại, nhà cung cấp dựa vào giá tiền quy định của công ty để bồi thường,
            người phụ trách nhà cung cấp tiến hành theo dõi và thực hiện.
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
import {fullUrl} from '@/util/EnvUtil'
import {httpGet} from '@/util/HttpUtil'
import {ElMessage} from 'element-plus'

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const printList = ref<any>([])
const printData = ref<any>({})
const templateIdList = route.query.templateIdList
httpGet(`douson/admin/template/page`, {
  page: {
    page: 1,
    limit: 9999,
  },
  data: {
    templateIdList: templateIdList,
  }
}).then(
    (res: any) => {

      printList.value = res.list || []
      printData.value = printList.value[0]
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
