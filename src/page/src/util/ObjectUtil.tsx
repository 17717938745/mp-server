/*
 * @Author: YourName
 * @Date: 2022-02-14 15:42:12
 * @LastEditTime: 2024-06-19 15:35:39
 * @LastEditors: 韦玮莹
 * @Description: 
 * @FilePath: \h5\src\util\ObjectUtil.tsx
 * 版权声明
 */
export function replaceAllByList(data: any, list: any[], keyString: string, valueString: string) {
  Object.keys(data).forEach(temp => {
    delete data[temp]
  })
  list.forEach(temp => {
    data[temp[keyString]] = temp[valueString]
  })
}

// 判断是否是普通对象
export function isPlainObject(val: any): val is Object {
  return toString.call(val) === '[object Object]'
}

export function isNotEmpty(obj: any): boolean {
  return !isEmpty(obj)
}


export function isEmpty(obj: any): boolean {
  return keyArray(obj).length <= 0
}

export function keyArray(obj: any): string[] {
  return Object.keys(obj || {})
}

export function deepSet(obj: any, tar: any) {
  Object.keys(tar).forEach(k => {
    const val = tar[k]
    if (isPlainObject(val)) {
      if (!(k in obj)) {
        obj[k] = {}
      }
      deepSet(obj[k], val)
    } else {
      obj[k] = val
    }
  })
}

/**
 * 返回一个新对象，深层次合并
 * @param objs 参数
 */
export function deepMerge(...objs: any[]): any {
  const result = Object.create(null)
  objs.forEach(obj => {
    if (obj) {
      Object.keys(obj).forEach(key => {
        const val = obj[key]
        if (isPlainObject(val)) {
          // 这里判断 原对象上 相同键是否是一个 对象
          // 如果是将 新的对象合并到原对象上 (递归)
          if (isPlainObject(result[key])) {
            result[key] = deepMerge(result[key], val)
          } else {
            result[key] = deepMerge(val)
          }
        } else {
          result[key] = val
        }
      })
    }
  })
  return result
}

export function parseJsonStr(str: string) {
  let json
  try {
    json = JSON.parse(str)
  } catch (e) {
    json = {}
  }
  return json
}

export function beautifyJsonStr(str: string) {
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch (e) {
    return str
  }
}

// 深度拷贝
export const deepCopy = (obj: any): any => {
  if (obj === null || typeof obj !== 'object') {
    return obj;
  }

  if (obj instanceof Date) {
    return new Date(obj)
  }

  if (obj instanceof Array) {
    return obj.map(item => deepCopy(item))
  }

  if (obj instanceof Object) {
    const copy: any = {};
    // 循环对象上所有的可枚举属性
    for (let key in obj) {
      // hasOwnProperty 只处理对象自身的属性，避免处理原型链上的属性
      if (obj.hasOwnProperty(key)) {
        copy[key] = deepCopy(obj[key])
      }
    }
    return copy;
  }
  throw new Error('Unable to copy object');
}

// 传入对象或者map生成options[label,value]数组
export const generateOptions = (obj: Record<string | number, string> | Map<number, string>) => {
  const options = [];
  if (obj instanceof Map) {
    for (const [key, label] of obj) {
      options.push({ value: key, label });
    }
  } else {
    for (const key in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, key)) {
        options.push({ value: key, label: obj[key] });
      }
    }
  }
  return options;
};
