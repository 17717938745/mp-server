import { getStorage, setStorage } from './StorageUtil'

declare type OptionItem<T> = {
  label: string;
  value: T;
  active?: boolean | undefined;
}
// @ts-ignore
const CONST_ENV: string = String(import.meta.env.VITE_ENV),
  KEY_SPRING_PROFILE_ACTIVE: string = 'spring.profiles.active',
  KEY_SPRING_MERCHANT_ID: string = 'merchant.id',
  KEY_SPRING_BBC_CHANNEL_CODE: string = 'bbc.channel.code',
  KEY_STRING_URL_PREFIX: string = 'url.prefix',
  KEY_STRING_SERVICE_ID: string = 'service.id',
  KEY_STRING_USER_ID: string = 'user.id'
  ;

export function getEnv(): string {
  return CONST_ENV
}

function getDefaultProfile(): string {
  return ''
}

export function getProfile(): string {
  const profile = getStorage(KEY_SPRING_PROFILE_ACTIVE)
  // console.log('profile',profile)
  if (undefined === profile || null == profile || "" == profile) {
    return getDefaultProfile()
  }
  return profile
}

export function getUserId(): string {
  return getStorage(KEY_STRING_USER_ID) || ''
}

export function getMerchantId(): string {
  return getStorage(KEY_SPRING_MERCHANT_ID) || ''
}

export function getBbcChannelCode(): string {
  return getStorage(KEY_SPRING_BBC_CHANNEL_CODE) || ''
}

export function setProfile(val: string) {
  setStorage(KEY_SPRING_PROFILE_ACTIVE, val)
}

export function clearAllCache() {
  setStorage(KEY_SPRING_PROFILE_ACTIVE, null)
  setStorage(KEY_STRING_URL_PREFIX, null)
  setStorage(KEY_STRING_URL_PREFIX + '.' + 'api', null)
  setStorage(KEY_STRING_URL_PREFIX + '.' + 'fund-base', null)
  setStorage(KEY_STRING_SERVICE_ID, null)
}

export function getDefaultUrlPrefix(newPrefix: string = 'api'): string {
  const urlPrefix = '/' + newPrefix + '/'
  switch (getEnv()) {
    case 'douson':
      return 'https://douson.natapp4.cc/'
    case 'pch':
      return 'https://pch.mynatapp.cc/'
    case 'java':
      return '/'
    default:
      return urlPrefix
  }
}

export function setUrlPrefix(val: string, uriPrefix: string = 'api') {
  setStorage(KEY_STRING_URL_PREFIX + '.' + uriPrefix, val)
}

export function setUesrId(val: string) {
  setStorage(KEY_STRING_USER_ID, val)
}

export function setMerchantId(val: string) {
  setStorage(KEY_SPRING_MERCHANT_ID, val)
}

export function setBbcChannelCode(val: string) {
  setStorage(KEY_SPRING_BBC_CHANNEL_CODE, val)
}

export function getUrlPrefix(uriPrefix: string = 'api'): string {
  const urlPrefix = getStorage(KEY_STRING_URL_PREFIX + '.' + uriPrefix)
  if (undefined === urlPrefix || null == urlPrefix || urlPrefix.trim().length <= 0) {
    return getDefaultUrlPrefix(uriPrefix)
  }
  return urlPrefix || ''
}

export function setServiceId(val: string) {
  setStorage(KEY_STRING_SERVICE_ID, val)
}

export function getServiceId(): string {
  return getStorage(KEY_STRING_SERVICE_ID) || ''
}

export function getServiceIdList(): OptionItem<string>[] {
  return [
    {
      label: 'html5服务',
      value: 'provider',
    }, {
      label: 'h5工具',
      value: 'h5tool',
    }, {
      label: '系统模块',
      value: 'system',
    }, {
      label: '权限模块',
      value: 'authority',
    }, {
      label: '审核模块',
      value: 'audit',
    }, {
      label: '账户模块',
      value: 'account',
    }, {
      label: '微信',
      value: 'wechat',
    }, {
      label: '小程序',
      value: 'mini',
    }, {
      label: '新后台',
      value: 'admin-server',
    },
  ]
}

export function getMerchantIdList(): OptionItem<string>[] {
  return [
    {
      value: '',
      label: '自营',
    },
    {
      value: 'hz001',
      label: '杭州市民卡',
    },
    {
      value: 'lnt002',
      label: '岭南通',
    },
  ]
}

export function getProfileList(): OptionItem<string>[] {
  return [
    {
      label: '丽莎',
      value: 'lx',
    },
    {
      label: '陈平',
      value: 'chp',
    },
    {
      label: '丛子林',
      value: 'leon',
    },
    {
      label: '潘朝辉',
      value: 'mac',
    },
    {
      label: '潘朝辉-公网',
      value: 'pch',
    },
    {
      label: '黄帅',
      value: 'hhh',
    },
    {
      label: '开发环境',
      value: 'dev',
    },
    {
      label: '测试环境',
      value: 'sit',
    },
    {
      label: '预发环境',
      value: 'pre',
    },
    {
      label: '预发环境或者回归测试',
      value: 'uat',
    },
    {
      label: '生产环境',
      value: 'prod',
    },
    {
      label: '生产环境',
      value: 'prd',
    },
    {
      label: '默认',
      value: '',
    },
  ]
}


export const URL_PREFIX = '/api/'
export const URL_PREFIX_SIT = 'https://testm.leadfund.com.cn/api/'
export const URL_PREFIX_TEST2 = 'http://test2www.inleadbank.com.cn/api/'
export const URL_PREFIX_UAT = 'https://prepfund.leadbank.com.cn/api/'
export const URL_PREFIX_PRD = 'https://www.leadfund.com.cn/api/'

export function getUrlPrefixList(): OptionItem<string>[] {
  return [
    {
      label: '生产地址',
      value: URL_PREFIX_PRD,
    },
    {
      label: '预发地址',
      value: URL_PREFIX_UAT,
    },
    {
      label: '测试2套地址',
      value: URL_PREFIX_TEST2,
    },
    {
      label: '测试地址',
      value: URL_PREFIX_SIT,
    },
    {
      label: '默认',
      value: URL_PREFIX,
    },
    {
      label: '潘朝辉公网',
      value: 'https://pch.mynatapp.cc/api/',
    },
  ]
}

export function getFullUrl(uri: string): string {
  return '/api/' + uri
}

export function fullUrl(uri: string, urlPrefix: string = 'api'): string {
  let prefix = getUrlPrefix()
  if (prefix.endsWith('/')) {
    prefix += urlPrefix
  }
  if (prefix.endsWith('/') && uri.startsWith('/')) {
    return prefix.substring(0, prefix.length - 1) + uri
  } else if (!prefix.endsWith('') && !urlPrefix.startsWith('/')) {
    return prefix + '/' + uri
  }
  return prefix + uri
}

export function dousonFullUrl(uri: string): string {
  const prefix = 'https://douson.natapp4.cc/'
  if (prefix.endsWith('/') && uri.startsWith('/')) {
    return prefix.substring(0, prefix.length - 1) + uri
  } else if (!prefix.endsWith('') && !uri.startsWith('/')) {
    return prefix + '/' + uri
  }
  return prefix + uri
}

