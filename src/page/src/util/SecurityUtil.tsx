import {SM3, SM4} from 'gm-crypto'

interface SM4RES {
  encrypt: Function
  decrypt: Function
}

/**
 * @description: 字符串转hex
 * @param {string} str
 * @return {string}
 */
const strToHex = (str: string): string => {
  let val = '';
  for (let i = 0; i < str.length; i++) {
    if (val == '') val = str.charCodeAt(i).toString(16);
    else val += '' + str.charCodeAt(i).toString(16);
  }
  return val;
};

/**
 * @description: sm3加密解密
 * @param {string} data 加密/解密文本
 * @return {SM4RES} {Function}encrypt 加密；  {Function} decrypt 解密
 */
const sm3 = (data: string) => {
  return SM3.digest(data, 'utf8', 'hex')
}

/**
 * @description: sm4加密解密
 * @param {string} data 加密/解密文本
 * @param {string} key 密钥
 * @return {SM4RES} {Function}encrypt 加密；  {Function} decrypt 解密
 */
const sm4 = (data: string, key: string = '0000000000000000'): SM4RES => {
  const _key = strToHex(key)
  const encrypt = () => {
    return SM4.encrypt(data, _key, {
      inputEncoding: 'utf8',
      outputEncoding: 'hex',
    })
  }
  const decrypt = () => {
    return SM4.decrypt(data, _key, {
      inputEncoding: 'hex',
      outputEncoding: 'utf8',
    })
  }
  return {
    encrypt,
    decrypt,
  }
}

const sm4Encrypt = (data: string, key: string = '0000000000000000'): string => {
  return SM4.encrypt(data, strToHex(key), {
    inputEncoding: 'utf8',
    outputEncoding: 'hex',
  })
}

const sm4Decrypt = (data: string, key: string = '0000000000000000'): string => {
  return SM4.decrypt(data, strToHex(key), {
    inputEncoding: 'hex',
    outputEncoding: 'utf8',
  })
}

export {
  sm3,
  sm4,
  sm4Encrypt,
  sm4Decrypt,
}
