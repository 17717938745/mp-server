<template>
  <view style="margin-top: 50px;overflow-y: scroll;min-height: 100vh;height: 90vh;">
    <view class="title">{{ formData.productId ? '修改' : '新增' }}</view>
    <view class="mg10">
      <button v-if="formData.productId" @click="handleAddPrepare">点击新增</button>
    </view>
    <form @submit="handleFormSubmit" @reset="handleFormReset">
      <view class="form-section">
        <view class="form-title">日期 Ngày tháng</view>
        <view>
          <picker mode="date"
                  :focus="'reportDate' === focusKey"
                  :class="('reportDate' === focusKey ? 'form-focus' : '') +  ' form-item'"
                  @blur="focusKey = ''"
                  :value="formData.reportDate"
                  :start="startDate"
                  :end="endDate"
                  @change="handleDateChange">
            <view
                class="form-input">{{ formData.reportDate || '请选择' }}
            </view>
          </picker>
        </view>
      </view>
      <view class="form-section">
        <view class="form-title">订单 đơn đặt hàng</view>
        <input v-model="formData.orderNo"
               :focus="'orderNo' === focusKey"
               :class="('orderNo' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''; formData.orderNo = (formData.orderNo || '').toUpperCase();"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">项次 số mục hàng</view>
        <input v-model="formData.projectSequence"
               :focus="'projectSequence' === focusKey"
               :class="('projectSequence' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">图号 Số bản vẽ</view>
        <input v-model="formData.designNumber"
               :focus="'designNumber' === focusKey"
               :class="('designNumber' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''; formData.designNumber = (formData.designNumber || '').toUpperCase();"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">调试设备 Điểu chỉnh máy số</view>
        <picker @change="handleTestDeviceChange"
                :focus="'testDevice' === focusKey"
                :class="('testDevice' === focusKey ? 'form-focus' : '') +  ' form-item'"
                @blur="focusKey = ''"
                :range="(config.testDeviceList || []).map(t => t.label)"
        >
          <view
              class="form-input">{{ formData.testDevice || '请选择' }}
          </view>
        </picker>
      </view>
      <view class="form-section">
        <view class="form-title">加工工序 Công đoạn gia công</view>
        <picker @change="handleProcessProcedureChange"
                :focus="'processProcedure' === focusKey"
                :class="('processProcedure' === focusKey ? 'form-focus' : '') +  ' form-item'"
                @blur="focusKey = ''"
                :range="(config.processProcedureList || []).map(t => t.label)"
        >
          <view class="form-input">{{ formData.processProcedure || '请选择' }}</view>
        </picker>
      </view>
      <view class="form-section">
        <view class="form-title">程序号số chương trình</view>
        <input v-model="formData.programNumber"
               :focus="'programNumber' === focusKey"
               :class="('programNumber' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">调试时间/分钟 thời gian set hàng/phút</view>
        <input v-model="formData.debugMinute"
               :focus="'debugMinute' === focusKey"
               :class="('debugMinute' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               type="number"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">装夹时间/分钟 Thời gian gá/phút</view>
        <input v-model="formData.clampingMinute"
               :focus="'clampingMinute' === focusKey"
               :class="('clampingMinute' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               type="number"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">辅助时间/分钟 Thời gian hỗ trợ/phút</view>
        <input v-model="formData.assistMinute"
               :focus="'assistMinute' === focusKey"
               :class="('assistMinute' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               type="number" placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">程式运行时间/分钟 Thời gian vận hành chương trình/phút</view>
        <input v-model="formData.runningMinute"
               :focus="'runningMinute' === focusKey"
               :class="('runningMinute' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               type="number" placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">备注 chú giải</view>
        <input v-model="formData.remark"
               :focus="'remark' === focusKey"
               :class="('remark' === focusKey ? 'form-focus' : '') +  ' form-item'"
               @blur="focusKey = ''"
               placeholder="请输入"/>
      </view>
      <view class="form-section">
        <view class="form-title">拍照 Chụp ảnh</view>
        <button :focus="'photoList' === focusKey"
                v-if="formData.photoList.length < 3"
                @click="handleTakePhoto"
                style="margin-left: 5px;"
                type="primary"
        >
          拍照 Chụp ảnh
        </button>
        <view v-for="(t, i) in formData.photoList" :key="t.photoUrl">
          <view>照片{{ i + 1 }}</view>
          <button @click="formData.photoList.splice(i, 1)" style="margin-left: 5px;">删除</button>
          <view>
            <image mode="widthFix" :src="t.fullPhotoUrl"></image>
          </view>
        </view>
        <!--        <view v-if="takePhoto" style="display: flex; flex-direction: column; align-items: center;">
                  <camera class="image" device-position="back" flash="off" style="width: 100%; height: 280px;"></camera>
                </view>-->
      </view>
      <view class="form-btn" style="margin-bottom: 60px;">
        <button form-type="reset">重置</button>
        <button type="primary" form-type="submit">提交</button>
      </view>
    </form>
  </view>
</template>

<script lang='ts' setup>
import {reactive, ref, toRefs} from 'vue'
import {httpPutJson, httpPostJson, httpGet} from '@util/HttpUtil'
import {getUrlPrefix} from '@util/EnvUtil'
import {onLoad, onShow} from '@dcloudio/uni-app'

const getDate = (type: string) => {
  const date: any = new Date()
  let year: number = date.getFullYear()
  let month: number = date.getMonth() + 1
  let day = date.getDate()
  if (type === 'start') {
    year = year - 60;
  } else if (type === 'end') {
    year = year + 2
  }
  return `${year}-${month > 9 ? month : '0' + month}-${day > 9 ? day : '0' + day}`
}
const defaultFormData = {
  productId: '',
  reportDate: getDate('currentDate'),
  orderNo: '',
  projectSequence: '',
  designNumber: '',
  testDevice: '',
  processProcedure: '',
  debugMinute: null,
  clampingMinute: null,
  assistMinute: null,
  runningMinute: null,
  programNumber: '',
  remark: '',
  photoList: [],
}
const startDate = getDate('start')
const endDate = getDate('end')
const config = ref({})
const focusKey = ref('')
// const takePhoto = ref(false)
const formData = ref(Object.assign({}, defaultFormData))
onLoad((option: any) => {
  if (option.productId) {
    console.log(`option.productId: ${option.productId}`,)
    formData.value.productId = option.productId
    httpGet('douson/product', {
      productId: option.productId
    }).then(r => {
      formData.value = r.data
    })
  }
})

const handleDateChange = (e) => {
  formData.value.reportDate = e.detail.value
}
const handleFormReset = () => {
  for (let key in defaultFormData) {
    formData.value[key] = defaultFormData[key]
  }
}
const handleFormSubmit = () => {
  for (let key in defaultFormData) {
    if (['productId', 'remark'].indexOf(key) < 0 && !formData.value[key]) {
      console.log('valid failed, key: ' + key)
      focusKey.value = key
      return
    }
  }
  if (formData.value['photoList'].length <= 0) {
    uni.showModal({
      content: '请提供至少一张照片',
      showCancel: false
    })
    focusKey.value = 'photoList'
    return
  }
  // focusKey.value = 'orderNo'
  if (formData.value.productId) {
    httpPutJson('douson/product', formData.value).then((res) => {
      uni.showModal({
        content: '修改成功',
        showCancel: false
      })
    })
  } else {
    httpPostJson('douson/product', formData.value).then((res) => {
      uni.showModal({
        content: '保存成功',
        showCancel: false
      })
    })
  }
}
const handleAddPrepare = () => {
  for (let key in formData.value) {
    formData.value[key] = defaultFormData[key]
  }
}
httpGet('douson/config').then(r => {
  config.value = r.data
})
const handleTestDeviceChange = (e) => {
  formData.value.testDevice = config.value.testDeviceList[e.detail.value].value
}
const handleProcessProcedureChange = (e) => {
  formData.value.processProcedure = config.value.processProcedureList[e.detail.value].value
}
const rpxToPx = (rpx) => {
  return uni.getSystemInfoSync().screenWidth * Number.parseInt(rpx) / 750
}
const handleTakePhoto = () => {
  if (formData.value.photoList.length > 3) {
    uni.showModal({
      content: '最多只能上传三张照片',
      showCancel: false
    })
  } else {
    // const ctx = uni.createCameraContext()
    // ctx.takePhoto({
    uni.chooseImage({
      sourceType: ['camera', 'album'],
      sizeType: ['compressed', 'original'],
      crop: {"quality": 80, "width": 100, "height": 100, "resize": false},
      count: 3 - formData.value.photoList.length,
      quality: 'high',
      success: (res) => {
        for (let i = 0; i < res.tempFilePaths.length; i++) {
          const tempImagePath = res.tempFilePaths[i]
          uni.uploadFile({
            url: getUrlPrefix() + 'index/img',
            filePath: tempImagePath,
            name: "file",
            formData: {
              file: tempImagePath,
            },
            header: {
              'Content-Type': 'multipart/form-data',
            },
            success: (res) => {
              const d = JSON.parse((res.data || '{}')).data || {}
              if (d.compressUrl && d.url && d.fullCompressUrl && d.fullUrl) {
                formData.value.photoList.push({
                  photoCompressUrl: d.compressUrl,
                  photoUrl: d.url,
                  fullPhotoCompressUrl: d.fullCompressUrl,
                  fullPhotoUrl: d.fullUrl,
                })
              } else {
                uni.showModal({
                  content: '上传失败',
                  showCancel: false
                })
              }
              // takePhoto.value = false
            },
            fail: (error) => {
              // takePhoto.value = false
            },
          })
        }
      }
    })
  }
}
</script>
<style lang='scss' scoped>

.form-section {
  padding: 5px 0;
}

.title {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  font-weight: 800;
}

.form-title {
  font-size: 16px;
  color: #303133;
}

.form-item {
  border: 1px solid #dddddd;
  margin: 5px 5px 10px 0px;
  padding: 5px 5px 5px 10px;
  font-size: 14px;
  background-color: #fff6e8;
  font-weight: 600;
  color: #dd1111;
}

.form-focus {
  border: 1px solid #ee2222;
}

button {
  width: 80px;
  height: 30px;
  font-size: 12px;
}

.mg10 {
  margin: 10px 0;
}

.image {
  width: 384px;
  height: 512px;
}

.form-btn {
  display: flex;
}
</style>
