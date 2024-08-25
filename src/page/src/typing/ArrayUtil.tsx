/*
 * @Author: YourName
 * @Date: 2022-02-14 15:42:12
 * @LastEditTime: 2022-03-21 16:53:36
 * @LastEditors: YourName
 * @Description: 
 * @FilePath: \h5\src\util\ArrayUtil.tsx
 * 版权声明
 */
export function clearAll(list: any[]) {
  list.splice(0, list.length)
}

export function replaceAll(list: any[], newList: any[]) {
  list.splice(0, list.length)
  list.push(...newList)
}

export function notIncludes<T>(list: T[], data: T) {
  return null == list || list.indexOf(data) < 0
}

export function includes<T>(list: T[], data: T) {
  return !notIncludes(list, data)
}

export function remove<T>(list: T[], data: T) {
  replaceAll(list, list.filter(item => item !== data))
}

export function moveUp<T>(list: T[], index: number) {
  const data = list[index]
  list.splice(index, 1)
  list.splice(index - 1, 0, data)
}

export function moveDown<T>(list: T[], index: number) {
  const data = list[index]
  list.splice(index, 1)
  list.splice(index + 1, 0, data)
}

export function insert<T>(list: T[], data: T, index: number = 0) {
  list.splice(index, 0, data)
}

export function putIfAbsent<T>(list: T[], data: T) {
  if (list.indexOf(data) <= 0) {
    list.push(data)
  }
}

export function toggle<T>(list: T[], data: T) {
  const index = list.indexOf(data)
  if (index >= 0) {
    list.splice(index, 1)
  } else {
    list.push(data)
  }
}

// picker 传入值，需要定位值在数组中的index，下次打开可以显示当前值
export const findPickerIndex = (list: any[], key: string, value: any): number => {
  // console.log(list,key,value)
  let index = 0;
  list.forEach((element, j) => {
    if (element[key] == value) {
      index = j;
      return index
    }
  });
  return index
}