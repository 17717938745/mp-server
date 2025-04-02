<template>
  <view class="">
    <view class="p-t-50 p-l-50 p-r-50 bgc-F3F5FA min-height-100vh border-box">
      <view class="bgc-fff p-b-100 radius-4">
        <img
            src=""
            class="h-570 width-100%"
            alt=""
        />
        <view
            v-if="!token"
            class="d-flex flex-column m-t-50 text-center fc-232425 fz-32 lh-54"
        >
          <text> 您需注册并审核通过后</text>
          <text>才能正常使用功能</text>
        </view>
        <view v-else class="d-flex flex-column m-t-50 text-center fc-232425 fz-32 lh-54">
          <text>快速注册</text>
          <text> 注册即可正常使用功能</text>
        </view>

        <button
            class="w-530 h-90 m-t-80 fc-fff fz-30 bgc-1C2D59 radius-8 lh-90"
            v-if="!checked"
            @click="handleShowToast"
        >
          手机号快捷登录
        </button>
        <button
            open-type="getPhoneNumber"
            class="w-530 h-90 m-t-80 fc-fff fz-30 bgc-1C2D59 radius-8 lh-90"
            v-else
            @getphonenumber="handleGetPhoneNumber"
        >
          手机号快捷登录
        </button>
      </view>
      <view
          class="bgc-F3F5FA fixed width-100 border-box left-0 p-x-40 bottom-60 m-t-40 bgc-F3F5FA border-box d-flex justify-content-start align-items-start width-100% fz-24 fc-232425"
          @click.stop="checked = !checked"
      >
        <img
            class="flex-shink-0 radius-25 w-30 h-30 radius-25 m-r-10"
            :src="`${fullUrl('/third/img/1542028712812240897.png', '')}`"
            v-show="!checked"
            alt=""
        />
        <img
            class="flex-shink-0 radius-25 w-30 h-30 radius-25 m-r-10"
            :src="`${fullUrl('/third/img/1542028713168756738.png', '')}`"
            v-show="checked"
            alt=""
        />
        <view class="word-breakAll">
          <text class="word-keepAll">我已阅读并同意</text>
          <text
              v-for="(item, index) of contractList"
              :key="index"
              :href="item.url"
              class="word-breakAll fc-40527E"
              @click.stop="handleGoContract(item.title, item.url)"
          >{{ item.name }}
          </text
          >
        </view>
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import {httpGet, httpPutJson} from "@util/HttpUtil"
import {getUrlPrefix, fullUrl} from "@util/EnvUtil"
import {ref} from "vue";
import {downloadFile} from "@/common/index";
import {onLoad, onShow} from "@dcloudio/uni-app";

interface IOption {
  token: string;
}

onShow(() => {
  /*uni.login({
    success(res: any) {
      if (res.code) {
        httpPutJson('index/sign', {code: res.code}).then((result) => {
          console.log('login success')
        })
      } else {
        console.log('login fail')
      }
    },
  })*/
})
const mobile = ref("");
const checked = ref(false);
const contractList = ref([
  {
    title: "用户协议",
    name: "《用户使用协议》",
    url:
        "",
  },
]);
const token = ref("");
onLoad((option: IOption | any) => {
  // 这个是分享人的token ，如果存在，用户不需要审核，直接登录；
  token.value = option.token;
});
// 点击协议
const handleGoContract = (title: string, url: string) => {
  downloadFile(title, url);
};
const pages = getCurrentPages();
// 点击授权手机号
const handleGetPhoneNumber = (e) => {
  if (e.detail.errMsg == "getPhoneNumber:ok") {
    httpGet(`index/mobile`, {
      encryptedData: e.detail.encryptedData,
      iv: e.detail.iv,
    }).then((res) => {
      mobile.value = res.data.pureMobile;
      uni.setStorageSync("mobile", res.data.pureMobile);
      uni.navigateBack({
        delta: 1,
      })
    });
  } else {
    uni.showModal({
      content: '个人开发者或者未认证的小程序，无法获取用户手机号',
      showCancel: false
    })
  }
};

const handleShowToast = () => {
  uni.showToast({
    title: "请勾选协议",
    icon: "none",
  });
};
</script>

<style>
.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}
</style>
