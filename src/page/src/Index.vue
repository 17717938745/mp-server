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
                  // 未登录
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
$print_border_color: #333333;
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

    .douson-serial-no {
      display: flex;
      justify-content: flex-end;
      width: 100%;
      font-size: 16px;
      color: #ff0000;
      font-weight: 800;
      margin-bottom: 5px;
    }

    .douson-container {
      border: 2px solid $print_border_color;
    }

    .douson-flex {
      display: flex;
    }

    .douson-flex-column {
      display: flex;
      flex-direction: column;
    }

    .douson-column {
      border-right: 2px solid $print_border_color;
      padding: 5px;

      &:last-child {
        border: none;
      }
    }

    .douson-column-height-10 > .douson-column {
      padding-top: 10px;
      padding-bottom: 10px;
    }

    .douson-column-height-0 > .douson-column {
      padding-top: 0;
      padding-bottom: 0;
    }

    .douson-column-width-0 > .douson-column {
      padding-left: 0;
      padding-right: 0;
    }

    .douson-column-padding-0 > .douson-column {
      padding: 0;
    }

    .douson-table > .douson-column {
      border-bottom: 2px solid $print_border_color;

      &:last-child {
        border: none;
      }
    }

    .douson-row {
      border-bottom: 2px solid $print_border_color;

      &:last-child {
        border: none;
      }
    }

    .douson-flex-item {
      display: flex;
    }

    .douson-flex-item-column {
      display: flex;
      flex-direction: column;
    }

    .douson-flex-item-center {
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .douson-flex-item-column-center {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>
