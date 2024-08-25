const ua = window.navigator.userAgent.toLowerCase();
// 是否公众号
export const is_weixin = ua.includes('micromessenger');
// 是否小程序环境
export const is_mini = ua.includes('miniprogram');