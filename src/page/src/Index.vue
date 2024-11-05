<template>
  <router-view v-if="initialized"/>
  <div v-else>
    页面正在加载中...
    <button @click="initialized = true">继续跳转</button>
  </div>
</template>

<script lang="tsx" setup>
import {initClient, registerInterceptor} from './util/HttpUtil';
import {ref} from 'vue';
import {DataResult} from '@/typing/ma/System';
import {getFullSignUri, getDistributionSignUrl} from '@/util/RouterUtil';
import {useRouter} from 'vue-router';
import {getCookie} from './util/StorageUtil';
import {jsCallNative} from './util/jsBridge';
import {toast} from './component/lead/toast';
import {Toast} from 'vant';
import {getMerchantId} from '@/util/EnvUtil';
import {netNoData} from '@/util/Platform';

try {
  // @ts-ignore
  console.log(
      `--------========<<<<<<<<>>>>>>>>========--------\n${JSON.stringify(
          GLOBAL_LEAD_GIT_INFO,
          null,
          2
      )}--------========<<<<<<<<>>>>>>>>========--------`
  );
} catch (e) {
}

const router = useRouter();
const initialized = ref(false /*location && location.pathname && location.pathname.length <= 1*/);
if (!initialized.value) {
  initClient().then((result: DataResult<any>) => {
    initialized.value = true;
  });
}
registerInterceptor(
    (result: any) => {
      // Toast("哈哈哈哈哈");
      if (result) {
        if (result.status === 401) {
          router.replace(getFullSignUri());
        } else if (result.status !== 200) {
          console.log(`请求错误，状态码：${result.status}`);
          return Promise.reject({
            code: -1,
            message: `请求错误，状态码：${result.status}`,
          });
        } else {
          const data: any = result.data || {};
          const errorMsg: string = data.message || `网络超时，请稍后再试`;
          if (data.hasOwnProperty('code')) {
            const code = data.code;
            if (data.code !== 0) {
              if (data.code === 18) {
                router.replace('/html5/SystemMaintenance');
                return;
              }
              if (location.pathname.startsWith('/html5/')) {
                Toast.clear();
                if (data.code !== 73015 && data.code !== 70000) {
                  // 除了“客户未登录”不弹，其他错误都弹出
                  Toast(errorMsg);
                }
                console.log(`code: ${code}, message: ${errorMsg}`);
              } else {
                toast(errorMsg, 4000);
              }
              switch (data.code) {
                  // html5未登录，自动跳转
                case 70000:
                case 73015:
                  // 分销客户迁移完成
                case 72024:
                  // console.log('这里')
                  setTimeout(() => {
                    if (getCookie('leadAppType')) {
                      jsCallNative('jumpLogin', {});
                    } else if (netNoData.includes(getMerchantId())) {
                      router.replace(getDistributionSignUrl());
                    } else {
                      router.replace(getFullSignUri());
                    }
                  }, 1500);
                  break;
                  // ma未登录
                case 74007:
                case 140000:
                  router.replace(getFullSignUri());
                  break;
                default:
                  const requestUrl = result && result.request ? result.request.responseURL : '';
                  console.log(`requestUrl: ${requestUrl}, code: ${code}, message: ${errorMsg}`);
              }
              return Promise.reject(data);
            } else {
              const data: any = result.data || {};
              return data;
            }
          } else {
            return Promise.reject({
              code: -1,
              message: 'not know error',
              data: data,
            });
          }
        }
      }
    },
    (error: any) => {
      toast(`系统错误或网络异常： ${error}`);
      return Promise.reject(error);
    }
);
</script>

<style lang="scss">
$print_border_color: #ddd;
html,
body {
  margin: 0;
  padding: 0;
  height: 100vh;
  overflow: auto;

  #app {
    margin: 0;
    padding: 0;
    height: 100vh;
    overflow: auto;

    .douson-row {
      border-bottom: 1px solid $print_border_color;

      &:last-child {
        border: none;
      }
    }

    .douson-column {
      border-right: 1px solid $print_border_color;
      padding: 5px;

      &:last-child {
        border: none;
      }
    }

    .douson-container {
      border: 1px solid $print_border_color;
    }

    .douson-flex {
      display: flex;
    }

    .douson-flex-column {
      display: flex;
      flex-direction: column;
    }

    .douson-flex-item-center {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .douson-flex-item-center-column {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }

    .douson-flex-center-column {
      display: flex;
      flex-direction: column;
    }

    .douson-flex-item {
      display: flex;
      flex-direction: row;
    }

    .douson-flex-item-column {
      display: flex;
      flex-direction: column;
    }
  }
}
</style>
