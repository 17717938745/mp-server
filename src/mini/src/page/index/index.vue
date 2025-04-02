<template>
  <view class="content">
    <view v-for="t in list" :key="t.productId" class="product-section" @click="handleToForm(t)">
      <view class="form-section">
        <view class="form-title">日期 Ngày tháng</view>
        <view class="form-item">{{ t.reportDate }}</view>
      </view>
      <view class="form-section">
        <view class="form-title">订单 đơn đặt hàng</view>
        <view class="form-item">{{ t.orderNo }}</view>
      </view>
      <view class="form-section">
        <view class="form-title">项次 số mục hàng</view>
        <view class="form-item">{{ t.projectSequence }}</view>
      </view>
      <view class="form-section">
        <view class="form-title">图号 Số bản vẽ</view>
        <view class="form-item">{{ t.designNumber }}</view>
      </view>
      <view class="form-section">
        <view class="form-title">调试设备 Điểu chỉnh máy số</view>
        <view class="form-item">{{ t.testDevice }}</view>
      </view>
      <view class="form-section">
        <view class="form-title">加工工序 Công đoạn gia công</view>
        <view class="form-item">{{ t.processProcedure }}</view>
      </view>
    </view>
    <view class="content">
      <view>{{ list.length }}/{{ total }}</view>
      <view class="form-section">
        <button @click="handlePage()">查询</button>
        <button v-if="list.length < total" @click="handleNextPage">查看更多</button>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import {reactive, ref, toRefs} from 'vue'
import {httpPutJson, httpPostJson, httpGet} from '@util/HttpUtil'

const request = ref({
  page: {
    page: 1,
    limit: 5,
  },
  data: {}
})
const total = ref(0)
const list = ref([])

const handlePage = (page: number = 1, add: boolean = false) => {
  request.value.page.page = page
  httpGet('douson/product/page', request.value).then(r => {
    total.value = r.total
    if (!add) {
      list.value.splice(0, list.value.length)
    }
    list.value.push(...r.list)
  })
}
handlePage()
const handleNextPage = () => {
  handlePage(request.value.page.page + 1, true)
}
const handleToForm = (t) => {
  uni.reLaunch({
    url: `/page/form/index?productId=${t.productId}`,
  })
}
</script>

<style lang="scss">

.content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.product-section {
  width: 100%;
  background-color: #cccccc;
  margin-bottom: 20px;
}

button {
  width: 80px;
  height: 30px;
  font-size: 12px;
}

.form-section {
  margin: 2px 5px;
}

.form-title {
  font-size: 14px;
  color: #303133;
}

.form-item {
  border: 1px solid #dddddd;
  font-size: 12px;
  color: #aaaaaa;
  background-color: #fff6e8;
}

.form-focus {
  border: 1px solid #ee2222;
}

</style>
