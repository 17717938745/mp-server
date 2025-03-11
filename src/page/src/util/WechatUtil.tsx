// @ts-ignore
import {httpGet} from './HttpUtil';
import {DataResult} from '../typing/ma/System';
import {changeURLArg} from './StringUtil';
import {Base64} from 'js-base64'

export const initShare = (token: string = '') => {
  let url = location.href.split('#')[0];
  if (url.includes('html5/bill/monthIndex')) {
    url = url.split('?')[0];
  }
  // alert(url)
  const params: any[] = [];
  httpGet(`wechat/jsapi`, {
    url: Base64.encode(url),
  }).then((res: DataResult<any>) => {
    const data = res.data.signatureData || {};
    const obj = res.data || {};
    wx.config({
      // 开启调试模式，调用的所有api的返回值会在客户端alert出来，在pc端时会打印出来，不需要的话可以将true改成false。
      debug: false,
      // 必填，公众号的唯一标识
      appId: data.appId,
      // 必填，生成签名的随机串
      nonceStr: data.nonceStr,
      // 必填，生成签名的时间戳
      timestamp: data.timestamp,
      // 必填，签名
      signature: data.signature,
      // 必填，需要使用的JS接口列表
      jsApiList: [
        'onMenuShareAppMessage',
        'onMenuShareTimeline',
        'updateAppMessageShareData',
        'updateTimelineShareData',
        'getLocation',
      ],
      openTagList: ['wx-open-launch-app'],
    });
    wx.ready(() => {
      const newUrl =
        url +
        (params.length > 0
          ? (url.indexOf('?') >= 0 ? '&' : '?') +
          params.map((param) => param['key'] + '=' + encodeURIComponent(param['value'])).join('&')
          : '');
      wx.updateAppMessageShareData({
        title: obj.title,
        desc: obj.description,
        link: changeURLArg(data.url, 'token', token),
        imgUrl: obj.iconUrl,
        success: function () {
          console.log('分享成功给好友');
        },
        cancel: function () {
          console.log('取消分享给好友');
        },
      });
      wx.updateTimelineShareData({
        title: obj.title,
        // 分享时的链接
        /*"https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + url + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect"*/
        link: changeURLArg(data.url, 'token', token),
        // 分享时的图标
        imgUrl: obj.iconUrl,
        success: function () {
          console.log('分享到朋友圈、QQ空间成功');
        },
        cancel: function () {
          console.log(`取消分享到朋友圈、QQ空间成功`);
        },
      });
      wx.onMenuShareAppMessage({
        title: obj.title,
        desc: obj.description,
        link: changeURLArg(data.url, 'token', token),
        imgUrl: obj.iconUrl,
        success: function () {
          console.log('111分享成功给好友');
        },
        cancel: function () {
          console.log('111取消分享给好友');
        },
      });
      wx.checkJsApi({
        jsApiList: ['getLocation', 'wx-open-launch-app', 'wx-open-launch-weapp'], // 需要检测的 JS 接口列表，所有 JS 接口列表见附录2,
        success: function (res: any) {
          console.log('wxJsApi success:', res);
        },
        fail: (err: any) => {
          console.log('wxJsApi fail:', err);
        },
      });
      console.log('分享配置成功（调试打印）');
    });
    wx.error((res: any) => {
      console.log(`wx.error: ${JSON.stringify(res, null, 2)}`);
      console.log(`分享配置失败（调试打印）： ${JSON.stringify(res, null, 2)}`);
    });
  });
};
