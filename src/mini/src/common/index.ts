

// 获取跳转登录前的页面和参数,存到内存中
export const getCurrentPageUrlWithArgs = function () {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const url = currentPage.route
  const options = currentPage.options
  let urlWithArgs = `/${url}?`
  for (let key in options) {
    const value = options[key]
    urlWithArgs += `${key}=${value}&`
  }
  urlWithArgs = urlWithArgs.substring(0, urlWithArgs.length - 1);


  return urlWithArgs
}



// 节流用户输入
// fn是事件触发执行的函数
// wait是指等待时长，在wait时间内多次触发函数只会被执行一次
// immediate是否立即执行，true表示wait秒内触发只会执行第一次触发；false表示wait秒内触发只会执行最后一次触发
export function debounce(fn, wait = 1000, immediate = false) {
  var timeout;
  return function () {
    let ctx = this;
    let args = arguments;
    if (timeout) clearTimeout(timeout);
    if (immediate) {
      let callNow = !timeout;
      // wait秒后清除定时器
      timeout = setTimeout(() => { timeout = null; }, wait);
      // 先执行函数
      if (callNow) fn.apply(ctx, args)
    } else {
      // wait秒后执行fn
      timeout = setTimeout(() => { fn.apply(ctx, args) }, wait);
    }
  }
}

// 
export const getFilePathName = (path) => {
  let pos1 = path.lastIndexOf('/');
  let pos2 = path.lastIndexOf('\\');
  let position = Math.max(pos1, pos2);
  if (position < 0)
    return path;
  else
    return path.substring(position + 1);
}


// 下载文件
export const downloadFile = (title, filePath, showMenu = false) => {
  let file = decodeURIComponent(filePath)
  file = getFilePathName(file)
  let filenameArr = file.split('.')
  let filename = title + '.' + filenameArr[1]
  uni.showLoading({
    title: '下载中',
    mask: true
  });
  uni.downloadFile({
    url: filePath,
    filePath: uni.env.USER_DATA_PATH + '/' + filename,
    success: (res) => {
      if (res.statusCode === 200) {
        let filePath = res.filePath;
        uni.openDocument({
          showMenu,
          filePath: filePath,
          success: (res) => { console.log('成功打开文档') }
        })
      }
    },
    complete: () => {
      uni.hideLoading()
    }
  });
}
function doHandleMonth(month) {

  var m = month;

  if (month.toString().length == 1) {

    m = "0" + month;

  }

  return m;

}
export function formatDate(inputDate) {
  // 将字符串日期转换为 Date 对象
  var date = new Date(inputDate);

  // 获取年、月、日
  var year = date.getFullYear();
  var month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份从0开始，需要加1，并且保证两位数
  var day = date.getDate().toString().padStart(2, '0'); // 保证两位数

  // 拼接成 YYYY-MM-DD 格式
  var formattedDate = `${year}-${month}-${day}`;

  return formattedDate;
}

export function getTimeRange(type) {
  var currentDate = new Date();
  var startDate, endDate;

  switch (type) {
    case "week":
      startDate = new Date(currentDate);
      startDate.setDate(currentDate.getDate() - 6);
      endDate = new Date();
      break;
    case "month":
      startDate = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
      endDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
      break;
    case "threeMonths":
      startDate = new Date(currentDate);
      startDate.setMonth(currentDate.getMonth() - 2);
      startDate.setDate(1);
      endDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
      break;
    case "year":
      startDate = new Date(currentDate);
      startDate.setFullYear(currentDate.getFullYear() - 1);
      startDate.setDate(1);
      endDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
      break;
    default:
      throw new Error("Invalid time range type");
  }

  return { startDate: formatDate(startDate), endDate: formatDate(endDate) };
}

// 示例用法
// var weekRange = getTimeRange("week");
// var monthRange = getTimeRange("month");
// var threeMonthsRange = getTimeRange("threeMonths");
// var yearRange = getTimeRange("year");

// console.log("近一周的时间范围:", weekRange);
// console.log("一个月的时间范围:", monthRange);
// console.log("三个月的时间范围:", threeMonthsRange);
// console.log("一年的时间范围:", yearRange);

export function getDay(day) {
  var today = new Date();
  var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;

  today.setTime(targetday_milliseconds); //注意，这行是关键代码

  var tYear = today.getFullYear();

  var tMonth = today.getMonth();

  var tDate = today.getDate();

  tMonth = doHandleMonth(tMonth + 1);

  tDate = doHandleMonth(tDate);

  return tYear + "-" + tMonth + "-" + tDate;
}



//type为字符串类型，有两种选择，"s"代表开始,"e"代表结束，
//  dates为数字类型，不传或0代表今年，-1代表去年，1代表明年
export function getYear(type, dates) {
  var dd = new Date();
  var n = dates || 0;
  var year = dd.getFullYear() + Number(n);
  var day = ''
  if (type == "s") {
    day = year + "-01-01";
  };
  if (type == "e") {
    day = year + "-12-31";
  };
  if (!type) {
    day = year + "-01-01/" + year + "-12-31";
  };
  return day;
};