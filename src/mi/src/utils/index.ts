/*
 * @Author: YourName
 * @Date: 2022-11-22 15:40:38
 * @LastEditTime: 2023-09-13 14:28:07
 * @LastEditors: 韦玮莹
 * @Description: 
 * @FilePath: \mp_page\src\utils\index.ts
 * 版权声明
 */

export function formatDateTime(val) {

    var timestamp = new Date(val).getTime();
    if (val === null || val === "") { return ""; }
    const d = new Date(val); // val 为表格内取到的后台时间
    const month = d.getMonth() + 1 < 10 ? `0${d.getMonth() + 1}` : d.getMonth() + 1;
    const day = d.getDate() < 10 ? `0${d.getDate()}` : d.getDate();
    const hours = d.getHours() < 10 ? `0${d.getHours()}` : d.getHours();
    const min = d.getMinutes() < 10 ? `0${d.getMinutes()}` : d.getMinutes();
    const sec = d.getSeconds() < 10 ? `0${d.getSeconds()}` : d.getSeconds();
    const times = `${d.getFullYear()}-${month}-${day} ${hours}:${min}:${sec}`;
    return times;
}


// 防抖
// /* 防止重复点击 */
// let clickTimer = 0
// export function clickThrottle(interval = 2000) {
//     // console.log('000000000000000000000000000000000')
//     let now = +new Date(); // 获取当前时间的时间戳
//     let timer = clickTimer; // 记录触发事件的事件戳
//     console.log(now ,timer,interval)
//     if (now - timer < interval) {
//         // 如果当前时间 - 触发事件时的事件 < interVal，那么不符合条件，直接return false，
//         // 不让当前事件继续执行下去
//         uni.showToast({
//             icon: 'none',
//             mask: true,
//             title: '您操作得太快啦~',
//             duration: 2000
//         })
//         return false;
//     } else {
//         // 反之，记录符合条件触发了事件的时间戳，并 return true，使事件继续往下执行
//         clickTimer = now;
//         return true;
//     }
// }

export const throttle = (func,wait=3000) =>{
  let timer = null;
  let startTime = Date.now();  

  return function(){
      var curTime = Date.now();
      var remaining = wait-(curTime-startTime); 
      var context = this;
      var args = arguments;

      clearTimeout(timer);

      if(remaining<=0){ 
          func.apply(context, args);

          startTime = Date.now();

      }else{
          timer = setTimeout(func, remaining);  // 如果小于wait 保证在差值时间后执行
      }
  }
}


export const getDate = () => {
  const date = new Date();
  let year = date.getFullYear();
  let month: number | string = date.getMonth() + 1;
  let day: number | string = date.getDate();
  month = month > 9 ? month : "0" + month;
  day = day > 9 ? day : "0" + day;
  return `${year}-${month}-${day}`;
};
export const getTime = () => {
  const date = new Date();
  let hour: number | string = date.getHours();
  let minute: number | string = date.getMinutes();
  hour = hour > 9 ? hour : "0" + hour;
  minute = minute > 9 ? minute : "0" + minute;
  return `${hour}:${minute}`;
};