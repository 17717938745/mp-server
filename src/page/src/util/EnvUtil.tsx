import {getStorage, setStorage} from './StorageUtil'

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

export function clearAllCache() {
  setStorage(KEY_SPRING_PROFILE_ACTIVE, null)
  setStorage(KEY_STRING_URL_PREFIX, null)
  setStorage(KEY_STRING_URL_PREFIX + '.' + 'api', null)
  setStorage(KEY_STRING_URL_PREFIX + '.' + 'fund-base', null)
  setStorage(KEY_STRING_SERVICE_ID, null)
}

export function getDefaultUrlPrefix(newPrefix: string = 'api'): string {
  const urlPrefix = (!newPrefix.startsWith('/') && !newPrefix.startsWith('http:/') && !newPrefix.startsWith('https:/') && !newPrefix.startsWith('ws:/') ? '/' : '') + newPrefix + (!newPrefix.endsWith('/') ? '/' : '')
  switch (getEnv()) {
    case 'douson':
      return 'http://14.241.40.75:8888/'
    case 'pch':
      return 'https://pch.mynatapp.cc/'
    case 'java':
      return '/'
    case 'local':
      return 'http://localhost:8888/'
    default:
      return urlPrefix
  }
}

export function setUrlPrefix(val: string, uriPrefix: string = 'api') {
  setStorage(KEY_STRING_URL_PREFIX + '.' + uriPrefix, val)
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

export function getUrlPrefixList(): OptionItem<string>[] {
  return [
    {
      label: '生产地址',
      value: 'http://14.241.40.75:8888/',
    },
    {
      label: '默认',
      value: 'http://localhost/',
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
  const prefix = `${window.location.origin}/`
  if (prefix.endsWith('/') && uri.startsWith('/')) {
    return prefix.substring(0, prefix.length - 1) + uri
  } else if (!prefix.endsWith('') && !uri.startsWith('/')) {
    return prefix + '/' + uri
  }
  return prefix + uri
}

