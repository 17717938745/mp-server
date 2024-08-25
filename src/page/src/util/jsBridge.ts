import Bridge from '../util/bridge';
import { isApp } from '@/util/Platform';
import { is_weixin } from '@/util/userAgent';
declare const window: Window & { bridge: any };
if (window) {
  window.bridge = Bridge;
}

export function jsCallNative(name: string, params: any = {}) {
  return new Promise((resolve, reject) => {
    try {
      window.bridge.callhandler(name, params, (responseData: any) => {
        let data = {};
        if (Object.prototype.toString.call(responseData) === '[object Object]') {
          data = responseData;
        } else if (Object.prototype.toString.call(responseData) === '[object String]') {
          data = JSON.parse(responseData);
        }
        // console.log('data-----',data)
        resolve(data);
      });
    } catch (error) {
      reject(error);
    }
  });
}
/** 打开APP 未打开会跳转应用宝下载地址 */
export const openApp = function () {
  var iosApppUrl = 'tianshengwocai://jstoapp.toHome()';
  var androidAppUrl = 'tianshengwocai://jstoapp.toHome/';
  var ua = navigator.userAgent;
  var wx = ua.indexOf('MicroMessenger');
  if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i)) {
    window.location = iosApppUrl;
  } else if (navigator.userAgent.match(/android/i)) {
    window.location = androidAppUrl;
  }
  setTimeout(function () {
    if (navigator.userAgent.match(/(iPhone|iPod|iPad);?/i) || wx != -1) {
      window.location = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.leadbank.lbf';
    } else if (navigator.userAgent.match(/android/i)) {
      window.location = 'http://a.leadfund.com.cn/adnld/leadfund.apk?randNum=' + Math.random() * 10;
    }
  }, 2000);
};
// 封装路由跳转app/h5
export const routerTo = (
  router: any, // 路由
  path: string, // 跳转url
  params?: any, // 参数
  title?: string, // 原生头部标题
  hiddenNavigationBar?: boolean, // 是否隐藏原生导航栏
  isNeedReload?: string // 容器是否需要刷新 "0" 不需要刷新，  "1"需要刷新
): void => {
  let arr = [];
  let str: string = '';
  if (params) {
    for (const key in params) {
      if (Object.prototype.hasOwnProperty.call(params, key)) {
        arr.push(`${key}=${params[key]}`);
      }
    }
  }
  if (arr.length > 0) {
    str += `?${arr.join('&')}`;
  }
  if (isApp) {
    jsCallNative('createContainer', {
      jumpUrl: `${location.origin}${path}${str}`,
      title: title,
      hiddenNavigationBar: title || hiddenNavigationBar ? '0' : '1',
      isNeedReload: isNeedReload ? isNeedReload : '0',
    });
  } else {
    router.push({
      path: path,
      query: params,
    });
  }
};
// 封装路由返回app/h5
export const hybridGoBack = (router: any) => {
  if (isApp) {
    jsCallNative('closeContainer');
  } else {
    router.back();
  }
};
// 跳转基金详情
export const goFundDetail = (router: any, fundCode = '', path = '/html5/product/fundDetails') => {
  if (isApp) {
    jsCallNative('jumpFundDetail', {
      jumpUrl: `${location.origin}${path}`,
      productId: fundCode,
    });
  } else {
    if (path.startsWith('http')) {
      window.location.href = path;
    } else {
      router.push({
        path: path,
        query: { fundCode },
      });
    }
  }
};
// 原生交互-不需要原生头-内容与顶部撑开距离
export const contentPandTop = (num = 46) => {
  let statusBarHeight = 0;
  if (isApp) {
    jsCallNative('getStatusBarHeight').then((res) => {
      statusBarHeight = (res as any).statusBarHeight;
    });
  }
  console.log('原生交互-不需要原生头-内容与顶部撑开距离', statusBarHeight + num);
};
