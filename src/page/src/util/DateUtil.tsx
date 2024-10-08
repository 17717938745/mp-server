export const formatDate = (dateParam: Date | string, fmt: string): string => {
  let date: Date
  if (dateParam instanceof Date) {
    date = dateParam
  } else {
    date = new Date(dateParam)
  }
  const o: Record<string, number> = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'H+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds(),
    'q+': Math.floor((date.getMonth() + 3) / 3),
    'S': date.getMilliseconds(),
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  Object.keys(o).forEach(k => {
    if (new RegExp('(' + k + ')').test(fmt)) {
      const val: string = '' + o[k]
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? val : (('00' + o[k]).substr((val).length)))
    }
  })
  return fmt
}


function GetDateDiff(checked_in) {
  //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式
  if (typeof checked_in !== 'string') return checked_in;
  if (checked_in.indexOf('-') > -1) {
    checked_in = checked_in.replace(/\-/g, "/");
  } else if (checked_in.indexOf('/') > -1) {
    return checked_in
  } else {
    // 20110101
    checked_in = checked_in.slice(0, 4) + '/' + checked_in.slice(4, 6) + '/' + checked_in.slice(-2)
  }

  return checked_in
}

export const getGapDate = (startTime: string): { year: number, day: number } => {
  // if(startTime.indexOf('-')>-1){
  startTime = GetDateDiff(startTime)
  // }
  const new_date = new Date().valueOf(); //新建一个日期对象，默认现在的时间
  const old_date = new Date(startTime + " 00:00:00").valueOf(); //设置过去的一个时间点，"yyyy-MM-dd HH:mm:ss"格式化日期
  const difftime = (Number(new_date) - Number(old_date)) / 1000; //计算时间差,并把毫秒转换成秒
  const days = Math.floor(difftime / 86400); // 天  24*60*60*1000
  // console.log({
  //     year:  Math.floor(days / 365) ,
  //     day: days % 365
  // })
  return {
    year: Math.floor(days / 365),
    day: days % 365
  }

}

// 对比两个日期，结束时间是否大于开始时间
export const compartDate = (beforeDate: string, afterDate: string): boolean => {
  return Boolean(new Date(afterDate).getTime() - new Date(beforeDate).getTime())
}


// 计算时间差（相距天时分秒）
//计算两个时间之间的时间差 多少天时分秒
interface ITime {
  d: string | number;
  h: string | number;
  m: string | number;
  s: string | number;
}

export function intervalTime(startTime, endTime): ITime {
  //   var timestamp = Date.parse(new Date()) / 1000; //计算当前时间戳 (毫秒级)
  endTime = GetDateDiff(endTime)
  //   if(endTime.length<=10) endTime+=" 00:00:00"
  //   console.log('endTime',endTime)
  var dateBegin = new Date(GetDateDiff(startTime)).getTime();
  var dateEnd = new Date(endTime).getTime();
  var date1 = dateBegin;
  var date2 = dateEnd; //结束时间
  //   console.log('date1',date1)
  //   console.log('date2',date2,endTime,GetDateDiff(endTime))
  var date3 = (date2 - date1) * 1000; //时间差的毫秒数
  //计算出相差天数
  var days = Math.floor(date3 / (24 * 3600 * 1000 * 1000));
  //计算出小时数
  var leave1 = date3 % (24 * 3600 * 1000 * 1000); //计算天数后剩余的毫秒数
  var hours = Math.floor(leave1 / (60 * 60 * 1000 * 1000));
  //计算相差分钟数
  var leave2 = leave1 % (60 * 60 * 1000 * 1000); //计算小时数后剩余的毫秒数
  var minutes = Math.floor(leave2 / (60 * 1000 * 1000));
  //计算相差秒数
  var leave3 = leave2 % (60 * 1000 * 1000); //计算分钟数后剩余的毫秒数
  var seconds = Math.round(leave3 / (1000 * 1000));
  console.log(days + "天 " + hours + "小时 " + minutes + " 分钟" + seconds + " 秒");
  //   return   days + "天 " + hours + "小时 " + minutes + " 分钟" + seconds + " 秒"
  //   return days + "天 " + hours + "小时 " + minutes + " 分钟";
  return {
    d: days,
    h: hours,
    m: minutes,
    s: seconds,

  }
}

export const getQuarter = (diff: number = 0): Date => {
  const date = new Date()
  let quarter = Math.ceil((new Date().getMonth() + 1) / 3) + diff;
  console.log('quarter', quarter)
  let year = date.getFullYear()
  quarter += 1;
  if (quarter > 4) {
    year += Math.floor(quarter / 4)
    quarter = (quarter % 4)
  }
  const dateString = String(year).padStart(4, '0') + '-' + String(quarter * 3).padStart(2, '0') + '-01 00:00:00'
  console.log(dateString)
  return new Date(dateString)
}


export const getMonthStart = () => {
  const now = new Date()
  return new Date(now.getFullYear(), now.getMonth(), 1)
}
export const getMonthEnd = () => {
  const now = new Date()
  const y = now.getFullYear()
  const m = now.getMonth()
  return new Date(new Date(m < 11 ? y : (y + 1),  m < 11 ? (m + 1) : m, 1).getTime() - 24 * 3600 * 1000)
}

// 格式化日期：yyyy-MM-dd
function parseDate(date) {
  const y = date.getFullYear()
  let m = date.getMonth() + 1
  var w = date.getDate()

  if (m < 10) {
    m = '0' + m
  }
  if (w < 10) {
    w = '0' + w
  }
  return (y + '-' + m + '-' + w)
}

//获得某月的天数
function getMonthDays(myMonth) {
  var monthStartDate = new Date(nowYear, myMonth, 1);
  var monthEndDate = new Date(nowYear, myMonth + 1, 1);
  var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
  return days;
}

//获得本季度的开始月份
export function getQuarterStartMonth() {
  var quarterStartMonth = 0;
  let month = nowMonth; //默认当前月份
  if (month < 3) {
    quarterStartMonth = 1;
  }
  if (2 < month && month < 6) {
    quarterStartMonth = 2;
  }
  if (5 < month && month < 9) {
    quarterStartMonth = 3;
  }
  if (month > 8) {
    quarterStartMonth = 4;
  }
  return quarterStartMonth;
}

//获得某季度的开始日期　　
export function getQuarterStartDate(paraSeason = getQuarterStartMonth()) {
  switch (paraSeason) {
    case 1:
      return new Date(nowYear + "-01-01" + ' 00:00:00');
    case 2:
      return new Date(nowYear + "-04-01" + ' 00:00:00');
    case 3:
      return new Date(nowYear + "-07-01" + ' 00:00:00');
    case 4:
      return new Date(nowYear + "-10-01" + ' 00:00:00');
  }
}

//获得某季度的结束日期　　
export function getQuarterEndDate(paraSeason = getQuarterStartMonth()) {
  switch (paraSeason) {
    case 1:
      return new Date(nowYear + "-03-31" + ' 23:59:59');
    case 2:
      return new Date(nowYear + "-06-30" + ' 23:59:59');
    case 3:
      return new Date(nowYear + "-09-30" + ' 23:59:59');
    case 4:
      return new Date(nowYear + "-12-31" + ' 23:59:59');
  }
}

//获得本周的开始日期
function getWeekStartDate() {
  var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
  return parseDate(weekStartDate);
}

//获得本周的结束日期
function getWeekEndDate() {
  var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
  return parseDate(weekEndDate);
}

//获得本月的开始日期
function getMonthStartDate() {
  var monthStartDate = new Date(nowYear, nowMonth, 1);
  return parseDate(monthStartDate);
}

//获得本月的结束日期
function getMonthEndDate() {
  var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
  return parseDate(monthEndDate);
}

//获得上月开始时间
function getLastMonthStartDate() {
  var lastMonthStartDate = new Date(nowYear, lastMonth, 1);
  return parseDate(lastMonthStartDate);
}

//获得上月结束时间
function getLastMonthEndDate() {
  var lastMonthEndDate = new Date(nowYear, lastMonth, getMonthDays(lastMonth));
  return parseDate(lastMonthEndDate);
}


/**
 * 日期格式化函数：
 * 例如：20220901 -> 2022-9-1
 *      202209 -> 2022-9
 *
 */

export function formatUserDate(value: string) {
  if (!value) return;
  let len = value.length;

  if (len < 4) {
    return;
  }

  if (len > 4 && len < 6) {
    return value.substr(0, 4);
  }

  let _arr = [],
      pattern;

  if (len >= 6 && len < 8) {
    pattern = /(\d{4})(\d{2})/;
    value = value.substr(0, 6).replace(pattern, "$1-$2");

    console.log(value);
    _arr = Array.from(value).filter((item, index) => {
      if (index === 5 && item === '0') {
        return false;
      }

      return true;
    });
  }

  if (len >= 8) {
    pattern = /(\d{4})(\d{2})(\d{2})/;
    value = value.substr(0, 8).replace(pattern, "$1-$2-$3");

    _arr = Array.from(value).filter((item, index) => {
      return true;
    });
  }

  return _arr.toString().replace(/,/g, '');
}

// 获取本月最后一天
export const getLastDate = (month = new Date().getMonth() + 1, year = new Date().getFullYear()) => {
  return new Date(new Date(year, month).setDate(0)).getDate();
}

export function getLastMonth() {
  const date = new Date();
  let nowMonth = date.getMonth() // 0-11
  let lastMonth = '0';
  if (nowMonth == 0) {
    //1月的上一月是前一年的12月
    lastMonth = '12';
  }

  if (nowMonth < 10) {
    //10月之前都需要补0
    lastMonth = "0" + nowMonth;
  }
  return lastMonth;
}
