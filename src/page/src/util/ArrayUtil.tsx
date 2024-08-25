/*
 * @Author: YourName
 * @Date: 2022-02-14 15:42:12
 * @LastEditTime: 2024-06-27 10:21:52
 * @LastEditors: liushengzhuang shengzhuang.liu@leadbank.com.cn
 * @Description:
 * @FilePath: \h5\src\util\ArrayUtil.tsx
 * 版权声明
 */
export function clearAll(list: any[]) {
  list.splice(0, list.length);
}

export function replaceAll(list: any[], newList: any[]) {
  list.splice(0, list.length);
  list.push(...newList);
}

export function notIncludes<T>(list: T[], data: T) {
  return null == list || list.indexOf(data) < 0;
}

export function includes<T>(list: T[], data: T) {
  return !notIncludes(list, data);
}

export function remove<T>(list: T[], data: T) {
  replaceAll(
    list,
    list.filter((item) => item !== data)
  );
}

export function moveUp<T>(list: T[], index: number) {
  const data = list[index];
  list.splice(index, 1);
  list.splice(index - 1, 0, data);
}

export function moveDown<T>(list: T[], index: number) {
  const data = list[index];
  list.splice(index, 1);
  list.splice(index + 1, 0, data);
}

export function insert<T>(list: T[], data: T, index: number = 0): T[] {
  list.splice(index, 0, data);
  return list;
}

export function putIfAbsent<T>(list: T[], data: T) {
  if (list.indexOf(data) < 0) {
    list.push(data);
  }
}

export function toggle<T>(list: T[], data: T) {
  const index = list.indexOf(data);
  if (index >= 0) {
    list.splice(index, 1);
  } else {
    list.push(data);
  }
}

// picker 传入值，需要定位值在数组中的index，下次打开可以显示当前值
export const findPickerIndex = (list: any[], key: string, value: any): number => {
  // console.log(list,key,value)
  let index = 0;
  list.forEach((element, j) => {
    if (element[key] == value) {
      index = j;
      return index;
    }
  });
  return index;
};
export const classify = (arr: any[]) => {
  // 定义一个用来存已经分好类数据的数组
  var arr2: any = [];
  // 遍历数组
  for (var i = 0; i < arr.length; i++) {
    // 判断新定义的数组有没有值  没有值则减第一个类给他赋值上去
    if (arr2.length == 0) {
      if (!arr[i].percent) {
        arr[i].percent = '';
      }
      arr2.push({
        fundCategory: arr[i].fundCategory,
        fundTypeFormat: arr[i].fundTypeFormat,
        fundType: arr[i].fundType,
        value: '0',
        data: [arr[i]],
      });
    } else {
      // 第一个完后 后面的有值了 使用findindex()方法进行比较 存在返回下标index 不存在则返回-1
      var index = arr2.findIndex((item: any) => {
        return item.fundCategory == arr[i].fundCategory;
      });
      // 判断index是否大于0 大于0则在该类的data push进该类别的数据
      if (index >= 0) {
        if (!arr[i].percent) {
          arr[i].percent = '';
        }
        arr2[index].data.push(arr[i]);
      } else {
        // 不存在则新建一个对象  push 进arr2 里面 作为第二个类
        if (!arr[i].percent) {
          arr[i].percent = '';
        }
        arr2.push({
          fundCategory: arr[i].fundCategory,
          fundTypeFormat: arr[i].fundTypeFormat,
          fundType: arr[i].fundType,
          value: '0',
          data: [arr[i]],
        });
      }
    }
  }
  return arr2;
};

// 数组排序
export function sortByKey(array: any[], key: string | number) {
  return array.sort(function (a, b) {
    var x = a[key]; //如果要从大到小,把x,y互换就好
    var y = b[key];
    return x < y ? -1 : x > y ? 1 : 0;
  });
}

// 数组过滤
export const arrFilter = (obj: any, arr: any[]) => {
  // debugger
  let left = 0;
  let right = arr.length;
  let num = Number(obj);
  if (num < Math.min.apply(null, arr)) {
    return 0;
  }
  if (num > Math.max.apply(null, arr)) {
    return arr.length - 1;
  }
  for (let i = 0; i < arr.length; i++) {
    if (num === arr[i]) {
      right = i;
      break;
    } else if (num < arr[i]) {
      right = i - 1;
      break;
    }
  }
  return right;
};
// 获取有效银行卡（）
export const getBankList = (bankArr: any[], type = '', obj: any = {}) => {
  let arr: any[] = [];
  if (bankArr && bankArr.length > 0) {
    bankArr.forEach((item) => {
      if (type) {
        switch (type) {
          case 'subscribe':
            if (item?.treasure) {
              item.bankFormat = `利活宝&nbsp;(${item.bankFormat})`;
              item.pageRemark = `可用余额${item.share}元`;
              item.remark = `可用余额${item.share}元`;
            } else {
              item.bankFormat = `${item.bankFormat ? item.bankFormat : item.bankAccountFormat}`;
              item.pageRemark = `单笔限${item.perMaxAmountFormat}&nbsp;&nbsp;单日累计${item.dayMaxAmountFormat}`;
              item.remark = `单笔限${item.perMaxAmountFormat}&nbsp;&nbsp;单日累计${item.dayMaxAmountFormat}`;
            }
            break;
          case 'redeem':
            if (item.treasure) {
              // 利活宝提现
              /**
               * /**
               * 利活宝记息日=利活宝到账  treasureInterestDateFormat;
               * 银行卡比利活宝玩多少天？0-不展示文字；>0展示 bankCardLaterThenTreasure = 0;
               * */
              item.bankFormat = `卖出到利活宝`;
              item.pageRemark = `${item.treasureReceiptDateFormat}前到账，可用于购买其他基金，${item.treasureInterestDateFormat}开始计息。`;
              item.remark = `${item.treasureReceiptDateFormat}前到账，可用于购买其他基金，${item.treasureInterestDateFormat}开始计息。`;
              if (item.bankCardLaterThenTreasure && item.bankCardLaterThenTreasure < 0) {
                item.pageRemark += `<span style="color:#DC2828">(到账比银行卡晚${Math.abs(
                  item.bankCardLaterThenTreasure
                )}天)</span>`;
                item.remark += `<span style="color:#DC2828">(到账比银行卡晚${Math.abs(
                  item.bankCardLaterThenTreasure
                )}天)</span>`;
              }
              item.tag = {
                text: '享货基收益',
                color: '#FF5F5F',
              };
            } else {
              item.bankFormat = `卖出到银行卡(${item.bankFormat})`;
              item.pageRemark = `${item.receiptDateFormat} 前到账${item.bankCardLaterThenTreasure && item.bankCardLaterThenTreasure < 0 ? '。' : ''}`;
              item.remark = `${item.receiptDateFormat} 前到账${item.bankCardLaterThenTreasure && item.bankCardLaterThenTreasure < 0 ? '。' : ''}`;
              if (item.bankCardLaterThenTreasure && item.bankCardLaterThenTreasure > 0) {
                item.pageRemark += `，<span style="color:#DC2828">比利活宝晚${item.bankCardLaterThenTreasure}天</span>`;
                item.remark += `，<span style="color:#DC2828">比利活宝晚${item.bankCardLaterThenTreasure}天</span>`;
              }
              // item.tag = {
              //   text: '提现',
              //   color: '#36B7C3',
              // };
            }

            break;
          case 'leadBankRedeem': // 利活宝提现
            if (JSON.stringify(obj) !== '{}' && obj) {
              item.pageRemark = `快速提现余额：${
                item.share > obj.daySumShare ? obj.daySumShareFormat : item.shareFormat + '元'
              }<br>普通提现余额：${item.shareFormat}元`;
              item.remark = `快速提现余额：${
                item.share > obj.daySumShare ? obj.daySumShareFormat : item.shareFormat + '元'
              }<br>普通提现余额：${item.shareFormat}元`;
            }
            break;
          default:
            break;
        }
      }
      if (item.valid) {
        arr.push(item);
      }
    });
  }
  console.log('arr', arr);
  return arr;
};
export const getBankCardFormFormt = (obj: any) => {
  return {
    ...obj,
    bankFormat: `${obj.treasure ? '卖出到利活宝' : '卖出到银行卡'} (${obj.bankFormat})`,
    pageRemark: `${obj.receiptDateFormat} 前到账`,
    remark: `${obj.receiptDateFormat} 前到账`,
  };
};
