/*
 * @Author: YourName
 * @Date: 2022-02-28 16:41:00
 * @LastEditTime: 2022-03-16 17:58:41
 * @LastEditors: YourName
 * @Description: 
 * @FilePath: \h5\src\util\debounce.tsx
 * 版权声明
 */
// 防抖
export default  function debounce(func, wait,immediate=true) {
    let timeout;
    return function () {
      const context = this;
      const args = [...arguments];
      if (timeout) clearTimeout(timeout);
      wait&&!timeout &&immediate&&func.apply(context, args);
      timeout = setTimeout(() => {
        func.apply(context, args)
      }, wait);
    }
  }