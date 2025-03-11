import axios, { AxiosInstance } from 'axios'
import { getDeviceId, getSalt, setDeviceId, setSalt } from './StorageUtil'
import { getBbcChannelCode, getMerchantId, getServiceId, getUrlPrefix, getUserId } from './EnvUtil'
import Platform, { isMp } from './Platform'
import { getPathname } from './LocationUtil'
import { sm3 } from './SecurityUtil'
import { formatDate } from "./DateUtil";

interface AxiosMap {
  [key: string]: AxiosInstance
}

if (isMp) {
  axios.defaults.adapter = function (config: any) {
    return new Promise((resolve, reject) => {
      const url: string = String(axios.getUri(config.url, config.params, config.paramsSerializer)) || ''
      const prefix: string = url.startsWith('http://') || url.startsWith("https://") ? '' : (config.baseURL || '')
      // @ts-ignore
      wx.showLoading({
        title: '加载中'
      });
      // @ts-ignore
      wx.request({
        method: config.method.toUpperCase(),
        url: prefix + url,
        header: config.headers,
        data: config.data,
        dataType: config.dataType,
        responseType: config.responseType,
        sslVerify: config.sslVerify,
        complete: (response: any) => {
          // @ts-ignore
          wx.hideLoading()
          // @ts-ignore
          // console.log('response',response)
          // 不合理，小程序错误码不统一

          response = {
            data: response.data,
            status: response.statusCode,
            errMsg: response.errMsg,
            header: response.header,
            config: config
          }
          if (response.data && response.data.code !== 0 && response.data.respCode !== '000000') {
            // console.log(2,response,Boolean(response&&response.data&&response.data.respMessage))
            if (Boolean(response && response.data && response.data.respMessage)) {
              // @ts-ignore
              uni.showToast({
                title: response.data.respMessage,
                duration: 2000,
                mask: true,
                icon: 'none'
              });
            } else if (Boolean(response && response.data && response.data.message)) {
              // @ts-ignore
              uni.showToast({
                title: response.data.message,
                duration: 2000,
                mask: true,
                icon: 'none'
              });
            }
            reject(response.data)
            if (response.data && response.data.code === 310001) {
              console.log('redirect to mine...')
              // @ts-ignore
              uni.reLaunch({
                url: `/page/mine/mine`
              })
            }
            console.log('接口报错', response.data)
          }
          axios(resolve, reject, response)
        }
      })
    })
  }
}

const
  CONST_HTTP_HEADER_DEVICE_ID: string = 'Device-Id',
  CONST_HTTP_HEADER_LEAD_PROTECTOR: string = 'Lead-Protector',
  HEADER_SPLIT = '; ',
  HEADER_JOIN = '=',
  INTERCEPTOR_REQUEST_LIST: Function[] = [
    (c: any) => {
      let path = String(c.url) || ''
      if (path.startsWith('http://') || path.startsWith('https://')) {
        path = path.substring(path.indexOf("://") + 3, path.length)
        let index
        index = path.indexOf('/');
        if (index >= 0) {
          path = path.substring(index + 1)
        }
        index = path.indexOf('/');
        if (index >= 0) {
          path = path.substring(index + 1)
        }
      }
      if (!path.startsWith('/')) {
        path = '/' + path
      }
      const h: any = {
        'sign': sm3(`${getSalt()}|${getDeviceId()}|${path}`),
      }
      c.headers[CONST_HTTP_HEADER_DEVICE_ID] = getDeviceId()
      c.headers[CONST_HTTP_HEADER_LEAD_PROTECTOR] = Object.keys(h).map(k => k + HEADER_JOIN + h[k]).join(HEADER_SPLIT)
      return c
    }
  ],
  INTERCEPTOR_RESPONSE_LIST: Function[] = [
    (result: any) => {
      if (result) {
        if (result.status === 401) {
          console.log(`redirect to sign page`)
        } else if (result.status !== 200) {
          console.log('请求错误，状态码：' + result.status)
        } else {
          const data: any = result.data || {};
          if (data.hasOwnProperty('code')) {
            if (data.code !== 0) {
              const errorMsg: string = `请求接口错误，请求链接：${result.request ? result.request.responseURL : ''}，错误代码：${data.code}`
              console.log('errorMsg', errorMsg)
              if (isMp && data.code === 90001) {
                // 小程序未登录
                // handleLogin()
              } else if (isMp && data.code === 90000) {
                // 没有用户信息
                // @ts-ignore
                uni.navigateTo({
                  url: '/page/login'
                })
              }
              return Promise.reject(data)
            } else {
              return data
            }
          } else {
            return result;
          }
        }
      }
    },
    (error: any) => {
      console.log('请求错误：' + error)
      return Promise.reject(error)
    },
  ],
  UTIL_SCOPE_REQUEST_SET_HEADER = (data: any, header: any) => {
    header[CONST_HTTP_HEADER_LEAD_PROTECTOR] = getCommonHeaderInfo() + HEADER_SPLIT + header[CONST_HTTP_HEADER_LEAD_PROTECTOR]
  },
  UTIL_SCOPE_REQUEST_TRANSFORMER_FORM_DATA = (data: any, header: any) => {
    UTIL_SCOPE_REQUEST_SET_HEADER(data, header)
    return data
  },
  UTIL_SCOPE_REQUEST_TRANSFORMER = (data: any, header: any) => {
    UTIL_SCOPE_REQUEST_SET_HEADER(data, header)
    const dataType: string = Object.prototype.toString.call(data)
    if (dataType === '[object Array]' || dataType === '[object Object]') {
      return JSON.stringify(data)
    }
    return data
  },
  UTIL_SCOPE_REQUEST_FORM_DATA_TRANSFORMER = (data: any, header: any) => {
    UTIL_SCOPE_REQUEST_SET_HEADER(data, header)
    let formData: string = ''
    for (const key in data) {
      formData += key + '=' + encodeURIComponent(data[key]) + '&'
    }
    formData = formData.substring(0, formData.lastIndexOf('&'))
    return formData
  },
  UTIL_SCOPE_RESPONSE_TRANSFORMER = (data: any, headers: any): any => {
    if (isResponseTypeJson(headers)) {
      try {
        return JSON.parse(data)
      } catch (e) {
      }
    }
    return data
  },
  UTIL_SCOPE_REQUEST_TYPE_HTTP_UPLOAD: string = 'httpUpload',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_DOWNLOAD: string = 'httpDownload',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_GET: string = 'httpGet',
  // @ts-ignore
  UTIL_SCOPE_REQUEST_TYPE_HTTP_GET_JSON: string = 'httpGetJson',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_POST: string = 'httpPost',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_POST_JSON: string = 'httpPostJson',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_PUT: string = 'httpPut',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_PUT_JSON: string = 'httpPutJson',
  UTIL_SCOPE_REQUEST_TYPE_HTTP_DELETE: string = 'httpDelete',
  UTIL_SCOPE_REQUEST: AxiosMap = {},
  isResponseTypeJson = (headers: any) => {
    const contentType = ((headers || {})['content-type'] || '').toLowerCase()
    return 'application/json'.indexOf(contentType) >= 0
  },
  getCommonHeaderInfo = () => {
    const h: any = {
      'salt': getSalt(),
      'service.id': getServiceId(),
      'uri': getPathname(),
      'device.type': Platform.toString(),
      'device.info': Platform.toString(),
      'clientTime': formatDate(new Date(), 'yyyyMMddHHmmss'),
      'userId': getUserId(),
      'merchantId': getMerchantId(),
      'bbcChannelCode': getBbcChannelCode(),
    }
    try { // @ts-ignore
      h.version = String(GLOBAL_LEAD_GIT_INFO.CURRENT_BRANCH).replace(/[^\w]/g, '')
    } catch (e) {
    }
    return Object.keys(h).map(k => k + HEADER_JOIN + h[k]).join(HEADER_SPLIT)
  },
  // newPrefix不传调的/api
  httpGet = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    let get: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_GET + '_' + uriPrefix
    if (!(get = UTIL_SCOPE_REQUEST[key])) {
      get = axios.create({
        method: 'get',
        timeout: 60 * 1000,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      UTIL_SCOPE_REQUEST[key] = get
      get.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      get.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
    }
    const newParam: any = jsonToParams(param)
    return get.get(uri, Object.assign(config, { params: newParam }), config)
  },
  httpFundBaseGet = async function (uri: string, param: any = {}, config: any = {}) {
    return httpGet(uri, param, config, 'fund-base')
  },
  httpPost = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    let post: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_POST + '_' + uriPrefix
    if (!(post = UTIL_SCOPE_REQUEST[key])) {
      post = axios.create({
        method: 'post',
        timeout: 60 * 1000,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_FORM_DATA_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      UTIL_SCOPE_REQUEST[key] = post
      post.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      post.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
    }
    return post.post(uri, param, config)
  },
  // @ts-ignore
  httpFundBasePost = async function (uri: string, param: any = {}, config: any = {}) {
    return httpPost(uri, param, config, 'fund-base')
  },
  httpPostJson = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    // 定义变量
    let post: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_POST_JSON + '_' + uriPrefix
    // h确保只加载一次
    if (!(post = UTIL_SCOPE_REQUEST[key])) {
      // 创建单例对象
      post = axios.create({
        method: 'post',
        timeout: 60 * 1000,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
        },
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      post.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      // 定义过滤器
      post.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
      // 提升变量作用域
      UTIL_SCOPE_REQUEST[key] = post
    }
    return post.post(uri, param, config)
  },
  // @ts-ignore
  httpFundBasePostJson = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpPostJson(uri, param, config, 'fund-base')
  },
  httpPut = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    let post: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_PUT + '_' + uriPrefix
    if (!(post = UTIL_SCOPE_REQUEST[key])) {
      post = axios.create({
        method: 'put',
        timeout: 60 * 1000,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_FORM_DATA_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      UTIL_SCOPE_REQUEST[key] = post
      post.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      post.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
    }
    return post.put(uri, param, config)
  },
  // @ts-ignore
  httpFundBasePut = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpPut(uri, param, config, 'fund-base')
  },
  httpPutJson = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    let post: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_PUT_JSON + '_' + uriPrefix
    if (!(post = UTIL_SCOPE_REQUEST[key])) {
      post = axios.create({
        method: 'put',
        timeout: 60 * 1000,
        headers: {
          'Content-Type': 'application/json;charset=UTF-8',
        },
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      post.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      post.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
      UTIL_SCOPE_REQUEST[key] = post
    }
    return post.put(uri, param, config)
  },
  // @ts-ignore
  httpFundBasePutJson = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpPutJson(uri, param, config, 'fund-base')
  },
  httpDelete = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    let del: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_DELETE + '_' + uriPrefix
    if (!(del = UTIL_SCOPE_REQUEST[key])) {
      del = axios.create({
        method: 'delete',
        timeout: 60 * 1000,
        headers: {},
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      UTIL_SCOPE_REQUEST[key] = del
      del.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      del.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
    }
    return del.delete(uri, Object.assign(config, { params: param }), config)
  },
  // @ts-ignore
  httpFundBaseDelete = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpDelete(uri, param, config, 'fund-base')
  },
  httpUpload = async function (uri: string, param: FormData, config: any = {}, uriPrefix = 'api') {
    let upload: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_UPLOAD + '_' + uriPrefix
    if (!(upload = UTIL_SCOPE_REQUEST[key])) {
      upload = axios.create({
        timeout: 60 * 1000,
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [UTIL_SCOPE_REQUEST_TRANSFORMER_FORM_DATA],
      })
      UTIL_SCOPE_REQUEST[key] = upload
      upload.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
      upload.interceptors.response.use(
        INTERCEPTOR_RESPONSE_LIST[0],
        INTERCEPTOR_RESPONSE_LIST[1],
      )
    }
    return upload.post(uri, param, config)
  },
  // @ts-ignore
  httpFundBaseUpload = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpUpload(uri, param, config, 'fund-base')
  },
  httpDownload = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    let download: any
    let key: string = UTIL_SCOPE_REQUEST_TYPE_HTTP_DOWNLOAD + '_' + uriPrefix
    if (!(download = UTIL_SCOPE_REQUEST[key])) {
      download = axios.create({
        method: 'get',
        timeout: 60 * 1000,
        responseType: 'blob',
        headers: {},
        baseURL: getUrlPrefix(uriPrefix),
        transformRequest: [
          UTIL_SCOPE_REQUEST_TRANSFORMER,
        ],
        transformResponse: [
          UTIL_SCOPE_RESPONSE_TRANSFORMER,
        ],
      })
      UTIL_SCOPE_REQUEST[key] = download
      download.interceptors.request.use(
        INTERCEPTOR_REQUEST_LIST[0],
      )
    }
    const newParam: any = jsonToParams(param)
    return download.get(uri, Object.assign(config, { params: newParam }), config)
  },
  // @ts-ignore
  httpFundBaseDownload = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpDownload(uri, param, config, 'fund-base')
  },
  httpDownloadFile = function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    httpDownload(uri, param, config, uriPrefix).then((r) => {
      const filename = getFileName(r.headers["content-disposition"]);
      const blob = new Blob([r.data]);
      const url = window.URL.createObjectURL(blob);
      const dom = document.createElement("a");
      dom.style.display = "none";
      dom.href = url;
      dom.setAttribute("download", filename);
      document.body.appendChild(dom);
      dom.click();
      document.body.removeChild(dom);
      window.URL.revokeObjectURL(url);
    })
  },
  httpFundBaseDownloadFile = async function (uri: string, param: any = {}, config: any = {}, uriPrefix = 'api') {
    return httpDownloadFile(uri, param, config, 'fund-base')
  },
  jsonToParams = (obj: any, parentKey: string = '', newParam: any = {}): {} => {
    if (Object.prototype.toString.call(obj) === '[object Object]') {
      for (let k in obj) {
        jsonToParams(obj[k], (parentKey.length > 0 ? parentKey + '.' : parentKey) + k, newParam)
      }
    } else {
      newParam[parentKey] = obj
    }
    return newParam
  },
  getFileName = (str: string): string => {
    str = str || "";
    const tarStr = `filename="`;
    const i = str.indexOf(tarStr);
    if (i < 0) {
      return "file";
    }
    const startNum = str.indexOf(tarStr) + tarStr.length;
    return str.substring(startNum, str.length - 1);
  },
  clearHttp = () => {
    Object.keys(UTIL_SCOPE_REQUEST).forEach(k => {
      delete UTIL_SCOPE_REQUEST[k]
    })
  },
  registerInterceptor = (fn: Function, fn1: Function) => {
    INTERCEPTOR_RESPONSE_LIST[0] = fn
    INTERCEPTOR_RESPONSE_LIST[1] = fn1
  },
  initClient = async () => {
    const deviceId = getDeviceId()
    const salt = getSalt()
    console.log(`HttpUtil.initClient:\nPlatform: ${Platform}\n${CONST_HTTP_HEADER_DEVICE_ID}: ${getDeviceId()}\n${CONST_HTTP_HEADER_LEAD_PROTECTOR}: ${getCommonHeaderInfo()}`)
    if (!deviceId || deviceId === 'undefined' || deviceId.length < 16 || !salt || salt.length < 8) {
      return await httpGet('system/device')
        .then(res => {
          const data = res.data
          console.log(`request device: ${JSON.stringify(res)}`)
          setDeviceId(data.id)
          setSalt(data.salt)
          return res
        })
    }
    return {
      code: 1,
      message: 'from storage',
      data: {
        deviceId: deviceId
      }
    }
  }

export {
  httpGet,
  httpFundBaseGet,
  httpPost,
  httpFundBasePost,
  httpPostJson,
  httpFundBasePostJson,
  httpPut,
  httpFundBasePut,
  httpPutJson,
  httpFundBasePutJson,
  httpDelete,
  httpFundBaseDelete,
  httpUpload,
  httpFundBaseUpload,
  httpDownload,
  httpFundBaseDownload,
  httpDownloadFile,
  httpFundBaseDownloadFile,
  clearHttp,
  registerInterceptor,
  initClient,
}
