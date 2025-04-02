<!-- 我的 -->
<template>
  <div class="mine min-height-100vh relative">
    <div
        class="bg width-100vw h-946 absolute bg-no-repeat"
        :style="{
        backgroundImage: `url(${fullUrl('/third/img/1544612679650402306.png', '')})`,
      }"
    ></div>
    <mp-header></mp-header>
    <div class="header width-100%">
      <div class="content padding-30 border-box absolute width-100%">
        <!-- 个人信息 -->
        <div class="d-flex p-l-10 justify-content-between align-items-center width-100%">
          <div class="d-flex justify-content-start align-items-center">
            <div class="d-flex justify-content-start align-items-center fz-40">
              <button
                  class="avator w-120 h-120 radius-60 p-0"
                  open-type="chooseAvatar"
                  @chooseavatar="handleChooseAvatar"
              >
                <image
                    class="avatar width-100 height-100"
                    :src="userInfo.avatarUrl"
                ></image>
              </button>
              <div class="relative">
                <text
                    class="name m-l-40 fw-600"
                    v-if="!userInfo.mobile"
                    @click="handleClickLogin">注册/登录
                </text>
                <text v-if="!isEdit"
                      class="name m-l-40 fw-600"
                      style="max-width: 80px;display: block"
                >
                  {{ userInfo.mobile ? userInfo.mobile.substr(0, 3) + "****" + userInfo.mobile.substring(7) : userInfo.nickname || userInfo.openId }}
                </text>
                <input
                    :focus="isFocus"
                    v-else
                    maxlength="20"
                    type="nickname"
                    class="name m-l-40 fw-600 w-300"
                    v-model="userInfo.nickname"
                    placeholder="请输入昵称"
                    @blur="handleSubmitNickname"
                />
                <img
                    v-if="!isFocus"
                    :src="`${fullUrl('/third/img/1613019912565972994.png', '')}`"
                    class="w-40 h-40 absolute right--60 top-5"
                    alt=""
                    @click="
                    isEdit = true;
                    isFocus = true;
                  "
                />
              </div>
            </div>
          </div>
          <!-- 二维码 -->
          <button
              v-if="userInfo.accountType === 1"
              open-type="share"
              class="m-0 p-0 bgc-transparent m-r-10"
          >
            <img
                class="w-40 h-40"
                :src="`${fullUrl('/third/img/1544579386716610562.png', '')}`"
                alt=""
            />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {ref} from "vue"
import {fullUrl, getUrlPrefix} from "@util/EnvUtil"
// @ts-ignore
import mpHeader from "@/components/mpHeader.vue"
import {httpGet, httpPutJson, initClient} from "@util/HttpUtil"
import {setDeviceId, setSalt} from "@util/StorageUtil"
import {onShareAppMessage, onShow} from "@dcloudio/uni-app";
import {IUserInfo} from "@/typing/mp";
import {toLogin} from "@/common/login";

const isLogin = ref(false)
const isEdit = ref(false)
const isFocus = ref(false)
const userInfo = ref<IUserInfo>({
  avatarUrl: '',
} as IUserInfo)
// 获取用户信息
const httpGetUserInfo = () => {
  httpGet("index/account")
  .then((res: any) => {
    userInfo.value = res.data
    isLogin.value = true
    uni.showShareMenu()
  })
  .catch((err) => {
    console.log('user not login')
    if (err.code === 90002) {
      // 用户还没有登录
      isLogin.value = false
      handleLogin()
    }
  })
}
onShow(() => {
  handleLogin()
})
const handleClickLogin = () => {
  uni.setStorageSync("redirect", "/page/mine/mine");
  toLogin(false);
};

// 获取shareToken
const token = ref("");

// 用户登陆的方法
const handleLogin = (retry: boolean = true) => {
  uni.login({
    success(res: any) {
      if (res.code) {
        console.log('res.code: ', res.code)
        httpPutJson('index/sign', {
          code: res.code
        }).then((res) => {
          httpGetUserInfo()
        })
        .catch((err) => {
          if (err.code === 310003) {
            console.log('set device id empty')
            setDeviceId('')
            setSalt('')
            if (retry) {
              initClient().then(r => {
                handleLogin(false)
              })
            }
          }
        })
      } else {
        console.log('get code error...')
      }
    },
  })
}
const handleSubmit = (json) => {
  httpPutJson(`index/account`, json)
  .then((res) => {
    uni.showModal({
      content: '更新个人信息成功',
      showCancel: false
    })
  })
};

function handleChooseAvatar(e) {
  const {avatarUrl} = e.detail
  uni.uploadFile({
    url: getUrlPrefix() + 'index/img',
    filePath: avatarUrl,
    name: "file",
    formData: {
      file: avatarUrl,
    },
    header: {
      'Content-Type': 'multipart/form-data',
    },
    success: (res) => {
      const avatarUrl = JSON.parse(res.data).data.fullUrl
      userInfo.value.avatarUrl = avatarUrl
      handleSubmit({avatarUrl})
    },
    fail: (error) => {
    },
  })
}

const handleSubmitNickname = (e) => {
  userInfo.value.nickname = e.detail.value
  if (userInfo.value.nickname) {
    handleSubmit({
      nickname: userInfo.value.nickname,
    })
    isFocus.value = false
    isEdit.value = false
  }
}
onShareAppMessage(() => {
  return {
    title: '你的好友邀请你注册',
    path: '/page/mine/mine',
    imageUrl: `${fullUrl('/third/img/1613019912565972994.png', '')}`,
  };
});

const handleGoPage = (url: string): void => {
  uni.navigateTo({
    url,
  });
};

uni.getUserInfo;
</script>

<style lang="scss" scoped>
@import "../../../src/asset/css/wechat";

.mine {
  background: linear-gradient(184deg, #e5f2ff 0%, rgba(243, 245, 250, 0) 100%);

  // backdrop-filter: blur(30rpx);
  .bg {
    background-size: 100% 560 rpx;
    // filter: blur(30rpx);
  }

  .header {
    .content {
    }
  }

  .avator {
    border: 5 rpx solid #fff;
  }
}
</style>
