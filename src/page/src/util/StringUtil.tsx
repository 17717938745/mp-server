export const capitalize = (component: string): string => {
  return toUpperCase(component.charAt(0)) + component.substr(1);
};
export const unCapitalize = (component: string): string => {
  return toLowerCase(component.charAt(0)) + component.substr(1);
};
export const toUpperCase = (component: string): string => {
  return component.toUpperCase();
};
export const toLowerCase = (component: string): string => {
  return component.toLowerCase();
};
export const leftPad = (val: string, len: number, str: string): string => {
  while (val.length < len) {
    val = str + val;
  }
  return val;
};
export const rightPad = (val: string, len: number, str: string): string => {
  while (val.length < len) {
    val = val + str;
  }
  return val;
};
export const processname = (name: string): string => {
  let str = new Array(name.length).join('*') + name.substr(-1);
  return str;
};
export const hiddenCardId = (str: string, frontlen: number, endlen: number): string => {
  let len = str.length - frontlen - endlen;
  let start = '';
  for (let i = 0; i < len; i++) {
    start += '*';
  }
  return str.substring(0, frontlen) + start + str.substring(str.length - endlen);
};
export const digitUppercase = (n: any): string => {
  var fraction = ['角', '分'];
  var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
  var unit = [
    ['元', '万', '亿'],
    ['', '拾', '佰', '仟'],
  ];
  var head = n < 0 ? '欠' : '';
  n = Math.abs(n);
  var s = '';
  for (var i = 0; i < fraction.length; i++) {
    s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
  }
  s = s || '整';
  n = Math.floor(n);
  for (var i = 0; i < unit[0].length && n > 0; i++) {
    var p = '';
    for (var j = 0; j < unit[1].length && n > 0; j++) {
      p = digit[n % 10] + unit[1][j] + p;
      n = Math.floor(n / 10);
    }
    s = p.replace(/(零.)*零$/, '').replace(/^$/, '零') + unit[0][i] + s;
  }
  return (
    head +
    s
      .replace(/(零.)*零元/, '元')
      .replace(/(零.)+/g, '零')
      .replace(/^整$/, '零元整')
  );
};

// 判断输入框中是否输入了内容
export const isEmptyString = (str: string | undefined): boolean => {
  // console.log(typeof str)
  if (!str) {
    return true;
  }
  return str.trim() === '';
};

// 判断是否是正确格式的手机号 不校验什么开头只校验11位
export const isMobileNumber = (phone: string): boolean => {
  const regs = /^[0-9]{11}$/;
  return regs.test(phone);
};
// 判断是否是正确身份证格式
export const isCardNo = (card) => {
  // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
  var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if (reg.test(card) === false) {
    return false;
  }
  return true;
};

// 判断正负数返回颜色值
export const numberPositive = (num: number, str?: string): any => {
  if (num > 0) {
    return {
      color: '#DC2828',
    };
  } else if (num < 0) {
    return {
      color: '#12B469',
    };
  } else {
    if (!!str && str === '11') {
      return {
        color: '#ffffff',
      };
    }
    return {
      color: '#19191E',
    };
  }
};
// 修剪字符串中的空格
export const trim = (str: string): string => {
  str = str.trim();
  return str.replace(/\s+/g, '');
};

// 前端千分位
export function formatMoney(num) {
  return (num + '').replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,');
}

// 将字符串两个两个分开成数组
export function splitString(str) {
  let bbb = [...str];
  let a, b;
  let aIndex = 0;
  let bIndex = 1;
  let arr = [];

  bbb.forEach((str, index) => {
    if (index % 2 === 0) {
      a = str;
      aIndex += 1;
    } else {
      b = str;
      bIndex += 1;
    }
    if (a && b && bIndex - aIndex === 1) {
      arr.push(a + b);
    }
  });
  return arr;
}

export function getQuery(url: string) {
  if (url.indexOf('?') > -1 && url.indexOf('&') > -1) {
    const params = url.split('?')[1].split('&');
    const obj = {};
    params.map((item) => (obj[item.split('=')[0]] = item.split('=')[1]));
    return obj;
  }
  return {};
}
// 获取url参数
export const geturlparam = (url) => {
  if (url.indexOf('?') > -1) {
    let p = url.split('?')[1];
    let keyValue = p.split('&');
    let obj = {};
    for (let i = 0; i < keyValue.length; i++) {
      let item = keyValue[i].split('=');
      let key = item[0];
      let value = item[1];
      obj[key] = value;
    }
    return obj;
  }
  return {};
};

// 校验邮箱
export function validateEmail(mail) {
  var re = /^.+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
  return re.test(mail);
}

/*
 * 替换URL的参数值
 * url 目标url
 * arg 需要替换的参数名称
 * arg_val 替换后的参数的值
 * return url 参数替换后的url
 */
export function changeURLArg(url, arg, arg_val) {
  var pattern = arg + '=([^&]*)';
  var replaceText = arg + '=' + arg_val;
  if (url.match(pattern)) {
    var retuenUrl = url;
    var temp = '/(&' + arg + '=)([^&]*)/gi';
    if (eval(temp).test(retuenUrl)) {
      retuenUrl = retuenUrl.replace(eval(temp), '&' + replaceText);
    }
    var temps = '/([?])(' + arg + '=)([^&]*)/gi';
    if (eval(temps).test(retuenUrl)) {
      retuenUrl = retuenUrl.replace(eval(temps), '?' + replaceText);
    }
    return retuenUrl;
  } else {
    // if (url.match('[\?]')) {
    if (url.indexOf('?') > 0) {
      return url + '&' + replaceText;
    } else {
      return url + '?' + replaceText;
    }
  }
  return url + '\n' + arg + '\n' + arg_val;
}

// 判断是不是数字
export function isNumber(obj: any) {
  return typeof obj === 'number' && !isNaN(obj);
}

/**
 * 大数字转换，将大额数字转换为万、千万、亿等
 * @param value 数字值
 */
export const simplifyNum = (number) => {
  if (!number && number != 0) return number;
  var str_num;
  number = Number(number);
  if (number >= 1e3 && number < 1e4) {
    str_num = number / 1e3;
    return '千';
  } else if (number >= 1e4 && number < 1e5) {
    str_num = number / 1e4;
    return '万';
  } else if (number >= 1e5 && number < 1e6) {
    str_num = number / 1e5;
    return '十万';
  } else if (number >= 1e6 && number < 1e7) {
    str_num = number / 1e6;
    return '百万';
  } else if (number >= 1e7 && number < 1e8) {
    str_num = number / 1e7;
    return '千万';
  } else if (number >= 1e8 && number < 1e10) {
    str_num = number / 1e8;
    return '亿';
  } else if (number >= 1e10 && number < 1e11) {
    str_num = number / 1e10;
    return '百亿';
  } else if (number >= 1e11 && number < 1e12) {
    str_num = number / 1e11;
    return '千亿';
  } else if (number >= 1e12) {
    str_num = number / 1e12;
    return '万亿';
  } else {
    //一千以下
    return '';
  }
};
/**
 * 大数字转换，将大额数字转换为万、千万、亿等
 * @param value 数字值
 */
export const simplifyNumFn = (number: number) => {
  if (!number && number != 0) return number;
  var str_num;
  if (number >= 1e4) {
    str_num = number / 1e4;
    return `${str_num}万元`;
  } else {
    //一千以下
    return `${number}元`;
  }
};
/**
 * 金额单位换算
 */
export const untilConverter = (num: number | string) => {
  if (!num || isNaN(num)) {
    return;
  }
  num = Number(num);
  if (Math.abs(num) > 100000000) {
    return (num / 100000000).toFixed(0) + '亿';
  } else if (Math.abs(num) > 10000) {
    return (num / 10000).toFixed(0) + '万';
  } else {
    return num.toFixed(0);
  }
};
/**
 * 倒计时
 */
export function showTime(endDate) {
  let nowTime = new Date(); //获取当前时间
  let setTime = new Date(endDate); //设置结束时间

  let remianAllSeconds = Math.floor((setTime.getTime() - nowTime.getTime()) / 1000); //剩余总秒数
  if (remianAllSeconds < 0) {
    return false;
  }
  let remainDays = Math.floor(remianAllSeconds / (60 * 60 * 24)); //剩余天数
  let remainHours = Math.floor((remianAllSeconds / (60 * 60)) % 24); //剩余小时数
  let remainMinutes = Math.floor((remianAllSeconds / 60) % 60); //剩余分钟数
  let remainSeconds = Math.floor(remianAllSeconds % 60); //剩余秒数
  let day = remainDays ? remainDays + '天' : '';
  let hours = remainHours ? remainHours + '小时' : '';
  let minutes = remainMinutes ? remainMinutes + '分钟' : '';
  let str = `${day}${hours}${day ? '' : minutes}`;
  return str;
}
// 判断是否为整数
export function isInteger(num: number) {
  return Math.floor(num) === num;
}
// 允许页面滚动
export function AddScroll() {
  document.body.style.overflow = 'visible';
}
// 禁止页面滚动
export function RemoveScroll() {
  document.body.style.overflow = 'hidden';
}
/** 修正安卓收起键盘不聚焦 */
export function fixAndroidKeyboard() {
  if (/(iPhone|iPod|iPad|Macintosh)/i.test(navigator.userAgent)) return;

  const originWindowH = window.innerHeight;
  window.addEventListener('resize', () => {
    if (originWindowH !== window.innerHeight || document.activeElement?.nodeName === 'BODY') return;
    console.info('键盘收起，主动失焦');
    document.activeElement?.blur();
  });
}
/**
 * 截取两位小数
 */
export function toDecimal2(n: number) {
  let num = n.toString();
  return Number(num.match(/^\d+(?:\.\d{0,2})?/));
}
/**
 * 判断一个数字小数点后是否大于0
 */
export function isDecimalGreaterThanZero(number: number) {
  return /\.\d+/.test(number.toString());
}
/**
 * 保留几位小数公共方法number:数字decimalPlaces：要保留的位数
 */
export function truncateDecimals(number: number, decimalPlaces: number) {
  if (Number.isInteger(number) || /^\d+(\.\d{1})?$/.test(number.toString())) {
    return number.toFixed(2);
  }
  let regex = new RegExp(`^-?\\d+(?:\\.\\d{0,${decimalPlaces}})?`);
  let truncatedNumber = number.toString().match(regex)[0];
  return truncatedNumber;
}
/**
 * 小数点后两位之后为0时不显示
 */
export function formatNumber(num: number) {
  if (typeof num !== 'number' || isNaN(num)) {
    return '0.00'; // 如果传入的不是数字或者是 NaN，则返回默认值
  }

  // 判断是否为整数
  if (Number.isInteger(num) || /^\d+(\.\d{1})?$/.test(num.toString())) {
    return num.toFixed(2); // 如果是整数，保留两位小数
  } else {
    // 如果是小数，判断是否超过6位小数，最多只能保留6位小数且最后一位小数不能是0
    const decimalPlaces = num.toString().split('.')[1].length;
    if (decimalPlaces <= 6) {
      return num.toFixed(decimalPlaces).replace(/(?:\.0*|(\.\d+?)0+)$/, '$1'); // 删除多余的0
    } else {
      return num.toFixed(6).replace(/(?:\.0*|(\.\d+?)0+)$/, '$1'); // 超过6位小数，保留6位小数并删除多余的0
    }
  }
}
// 判断是否包含数字
export function containsNumber(str: string) {
  return /\d/.test(str);
}

// 校验是否合法网址
export function isValidURL(url: string) {
  const pattern = new RegExp(
    '^(https?:\\/\\/)?' + // protocol
      '((([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,})|' + // domain name
      'localhost|' + // OR localhost
      '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR IPv4 address
      '(\\:\\d+)?' + // optional port
      '(\\/[-a-zA-Z0-9%_.~+]*)*' + // path
      '(\\?[;&a-zA-Z0-9%_.~+=-]*)?' + // query string
      '(\\#[-a-zA-Z0-9_]*)?$' // fragment locator
  );

  return pattern.test(url);
}
export function px2rem(px: any) {
  if (/%/gi.test(px)) {
    // 有百分号%，特殊处理，表述pc是一个有百分号的数，比如：90%
    return px;
  } else {
    return parseFloat(px) / 16 + 'rem'; // 这里的37.5，和rootValue值对应
  }
}
