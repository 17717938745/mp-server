export function toLogin(isBackCurrent = true, token: string = '') {
  // 登录信息过期，请重新登录
  let redirect = ''
  if (isBackCurrent) {
    uni.setStorageSync('redirect', false)
  }
  uni.navigateTo({
    url: `/page/login?token=` + token
  })
}
