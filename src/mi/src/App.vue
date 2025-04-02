<script setup lang="ts">
import {onLaunch, onShow, onHide} from '@dcloudio/uni-app'
import {initClient} from '@util/HttpUtil'

onLaunch(() => {
  initClient().then((data) => {
  })
})
onShow(() => {
  if (uni.canIUse("getUpdateManager")) {
    const updateManager = uni.getUpdateManager();
    updateManager.onCheckForUpdate(function (res) {
      if (res.hasUpdate) {
        updateManager.onUpdateReady(function (res) {
          uni.showModal({
            title: '更新提示',
            content: '新版本已经准备好，是否重启应用？',
            success(res) {
              if (res.confirm) {
                updateManager.applyUpdate()
              }
            },
          });
        });
      }
    });
  } else {
    uni.showModal({
      title: '提示',
      content:
          '当前微信版本过低，无法更新小程序版本，请把小程序删除后重新打开升级到最新版本。',
    })
  }
})
onHide(() => {
  console.log("App Hide")
});
</script>
<style lang="scss">
@import "./asset/css/commonHtml5";
@import "./asset/css/wechat";

::-webkit-scrollbar {
  display: none;
}

uni-page-head {
  height: 60 rpx !important;
}

//字体
uni-page-head .uni-page-head__title {
  font-size: 36 rpx !important;
  line-height: 60 rpx !important;
  font-weight: normal;
}

//顶部导航条高度
uni-page-head .uni-page-head {
  height: 60 rpx !important;
}
</style>
