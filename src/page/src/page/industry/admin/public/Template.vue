<template>
  <div id="printDescription">
    <div
        style="display: flex; flex-direction: column; align-items: center; justify-content: center; margin-bottom: 20px;">
      <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; width: 585px;">
        <h1 style="text-align: center;height: 90px; position: relative;width: 100%;">
          <img :src="fullUrl('/third/img/douson.png', '')"
               style="position: absolute; height: 50px;left: 10px;top: 20px;">
          <div style="font-size: 36px; color: #222222; ">物品借出单</div>
          <div style="font-size: 36px; color: #222222; ">Đơn cho mượn dụng cụ</div>
          <br>
        </h1>
        <div style="width: 100%; text-align: right; color: #ff0000;">单号 Số phiếu：{{ printData.templateOrderNo }}</div>
        <div class="print-container">
          <div class="print-center-content">
            <div class="flex-no-wrap border-bottom print-line">
              <div class="left-content  flex-no-wrap">
                <div class="padding">
                  <div>借用人签字：</div>
                  <div>Người mượn：</div>
                </div>
                <div></div>
              </div>
              <div>
              </div>
              <div class="right-content flex-no-wrap">
                <div class="padding">
                  <div>借用方(单位) Đơn vị：</div>
                </div>
                <div>
                  {{ printData.borrowTemplatePersonFormat }}
                </div>
              </div>
            </div>
            <div class="flex-no-wrap border-bottom print-line">
              <div class="left-content flex-no-wrap">
                <div class="padding">
                  <div>借用时间 Ngày mượn：</div>
                </div>
                <div>
                  {{ printData.borrowTemplateDate }}
                </div>
              </div>
              <div class="right-content flex-no-wrap">
                <div class="padding">
                  <div>承诺归还时间 Ngày trả：</div>
                </div>
                <div>
                  {{ printData.promiseReturnDate }}
                </div>
              </div>
            </div>
            <div class="flex-no-wrap border-bottom print-line">
              <div class="left-content flex-no-wrap">
                <div class="center left border-right padding">
                  <div>序号</div>
                  <div>STT</div>
                </div>
                <div class="right center padding">
                  <div>物料号</div>
                  <div>Mã vật liệu</div>
                </div>
              </div>
              <div class="right-content flex-no-wrap">
                <div class="left center border-right padding">
                  <div>物品名称</div>
                  <div>Miêu tả vật liệu</div>
                </div>
                <div class="right center padding">
                  <div>数量</div>
                  <div>Số lượng</div>
                </div>
              </div>
            </div>
            <div class="flex-no-wrap border-bottom print-line">
              <div class="left-content flex-no-wrap">
                <div class="center left border-right padding large-content">
                  {{ printData.index || '1' }}
                </div>
                <div class="center right padding">
                  {{ printData.materialNo }}
                </div>
              </div>
              <div class="right-content flex-no-wrap">
                <div class="center left border-right padding large-content">
                  {{ printData.materialDescription }}
                </div>
                <div class="center right padding">
                  {{ printData.templateCount }}
                </div>
              </div>
            </div>
            <div class="flex-no-wrap border-bottom print-line">
              <div class="left-content flex-no-wrap">
                <div class="padding">
                  <div>经办人签字：</div>
                  <div>Người cho mượn：</div>
                </div>
                <div class="padding flex-wrap" style="width: 100px; height: 60px;">
                  {{ printData.operatorPersonFormat }}
                </div>
              </div>
              <div class="right-content flex-no-wrap">
                <div class="border-right padding flex-wrap" style="width: 208px; height: 60px;">
                  <div>
                    <div>部门负责人：</div>
                    <div>Người phụ trách bộ phận：</div>
                  </div>
                  <div></div>
                </div>
                <div>
                  <div class="padding">
                    <div>仓管签字:</div>
                    <div>Kho：</div>
                  </div>
                  <div></div>
                </div>
              </div>
            </div>
            <div class="flex-no-wrap print-line">
              <div class="flex-wrap padding">
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
import {ElMessage} from "element-plus";

const router = useRouter()
const route = useRoute()
const store: Store<StoreType> = useStore<StoreType>()
const user = store.state.user
const roleCodeList = store.state.roleCodeList
const printData = ref<any>({})
const templateId = route.query.equipmentId
httpGet(`douson/template`, {templateId: templateId}).then(
    (res: any) => {
      printData.value = res.data || {}
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
$print_border_color: #ddd;
.print-container {
  width: 100%;
  font-size: 12px;
  margin-top: 20px;
  border: 1px solid $print_border_color;
  word-break: break-all;

  .print-line {
    width: 100%;
  }

  .padding {
    padding: 5px;
  }

  .flex-no-wrap {
    display: flex;
    flex-wrap: nowrap;
    justify-content: flex-start;
    align-items: center;
  }

  .border-bottom {
    border-bottom: 1px solid $print_border_color;
  }

  .border-right {
    border-right: 1px solid $print_border_color;
  }

  .flex-wrap {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: center;
  }

  .left-content {
    width: 228px;
    border-right: 1px solid $print_border_color;

    .left {
      width: 60px;
    }

    .right {
      width: 178px;
    }
  }

  .large-content {
    height: 150px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .right-content {
    width: 357px;

    .left {
      width: 287px;
    }

    .right {
      width: 60px;
    }
  }

  .center {
    text-align: center;
  }

}
</style>
