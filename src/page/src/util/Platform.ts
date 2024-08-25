/**枚举EPlatform*/
export enum EPlatform {
  /**App*/
  AppPlus = 'APP-PLUS',
  /**App nvue*/
  AppPlusNvue = 'APP-PLUS-NVUE',
  /**H5*/
  H5 = 'H5',
  /**微信小程序*/
  Mini = 'MP-WEIXIN',
  /**支付宝小程序*/
  MpAlipay = 'MP-ALIPAY',
  /**百度小程序*/
  MpBaidu = 'MP-BAIDU',
  /**字节跳动小程序*/
  MpToutiao = 'MP-TOUTIAO',
  /**QQ小程序*/
  MpQq = 'MP-QQ',
  /**360小程序*/
  Mp360 = 'MP-360',
  /**微信小程序/支付宝小程序/百度小程序/字节跳动小程序/QQ小程序/360小程序*/
  Mp = 'MP',
  /**
   * 微信公众号
   */
  Wechat = 'WECHAT',
  /**快应用通用(包含联盟、华为)*/
  QuickappWebview = 'quickapp-webview',
  /**快应用联盟*/
  QuickappWebviewUnion = 'quickapp-webview-union',
  /**快应用华为*/
  QuickappWebviewHuawei = 'quickapp-webview-huawei',
}

/**使用条件编译获取平台信息*/
export function ifDefPlatform(): EPlatform {
  let platform: EPlatform;
  //#ifdef APP-PLUS
  platform = EPlatform.AppPlus;
  //#endif
  //#ifdef APP-PLUS-NVUE
  platform = EPlatform.AppPlusNvue;
  //#endif
  //#ifdef MP-WEIXIN
  platform = EPlatform.Mini;
  //#endif
  //#ifdef MP-ALIPAY
  platform = EPlatform.MpAlipay;
  //#endif
  //#ifdef MP-BAIDU
  platform = EPlatform.MpBaidu;
  //#endif
  //#ifdef MP-TOUTIAO
  platform = EPlatform.MpToutiao;
  //#endif
  //#ifdef MP-QQ
  platform = EPlatform.MpQq;
  //#endif
  //#ifdef MP-360
  platform = EPlatform.Mp360;
  //#endif
  //#ifdef MP
  platform = EPlatform.Mp;
  //#endif
  //#ifdef quickapp-webview
  platform = EPlatform.QuickappWebview;
  //#endif
  //#ifdef quickapp-webview-union
  platform = EPlatform.QuickappWebviewUnion;
  //#endif
  //#ifdef quickapp-webview-huawei
  platform = EPlatform.QuickappWebviewHuawei;
  //#endif
  //#ifdef H5
  platform = EPlatform.H5;
  //#endif
  if (window) {
    const ua = window.navigator.userAgent.toLowerCase();
    const list = ua.match(/MicroMessenger/i);
    if (list && list.length > 0 && list[0] === 'micromessenger') {
      return EPlatform.Wechat;
    } else if (list && list.length > 0 && list[0] === 'miniprogram') {
      return EPlatform.Mini;
    }
  }
  return platform;
}

/**平台类型*/
export const Platform: EPlatform = ifDefPlatform();
/**默认导出平台类型*/
export default Platform;

/**App*/
export const isAppPlus = Platform == EPlatform.AppPlus;
/**App nvue*/
export const isAppPlusNvue = Platform == EPlatform.AppPlusNvue;
/**H5*/
export const isH5 = Platform == EPlatform.H5;
/**微信小程序*/
export const isMini = Platform == EPlatform.Mini;
export const isWechat = Platform == EPlatform.Wechat;
/**支付宝小程序*/
export const isMpAlipay = Platform == EPlatform.MpAlipay;
/**百度小程序*/
export const isMpBaidu = Platform == EPlatform.MpBaidu;
/**字节跳动小程序*/
export const isMpToutiao = Platform == EPlatform.MpToutiao;
/**QQ小程序*/
export const isMpQq = Platform == EPlatform.MpQq;
/**360小程序*/
export const isMp360 = Platform == EPlatform.Mp360;
/**微信小程序/支付宝小程序/百度小程序/字节跳动小程序/QQ小程序/360小程序*/
export const isMp = Platform == EPlatform.Mp;
/**快应用通用(包含联盟、华为)*/
export const isQuickappWebview = Platform == EPlatform.QuickappWebview;
/**快应用联盟*/
export const isQuickappWebviewUnion = Platform == EPlatform.QuickappWebviewUnion;
/**快应用华为*/
export const isQuickappWebviewHuawei = Platform == EPlatform.QuickappWebviewHuawei;

// @ts-ignore
// window.xwbridge.leadAppType() === 'IOS' ||
export const isApp =
  window && window.navigator && window.navigator.userAgent.indexOf('leadbank|APP') > -1 ? 1 : 0;

// 分销网点
export const netNoData = ['hz001', 'lnt002'];
