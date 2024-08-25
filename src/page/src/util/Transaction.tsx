/*
 * @Author: liushengzhuang shengzhuang.liu@leadbank.com.cn
 * @Date: 2023-05-16 13:40:18
 * @LastEditors: liushengzhuang shengzhuang.liu@leadbank.com.cn
 * @LastEditTime: 2024-01-10 16:03:19
 * @FilePath: \dmmp_page\src\util\Transaction.tsx
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { todoList } from '@/api/h5/sign/sign';
import { setTransfer } from '@/api/h5/index';
import { Dialog, Toast } from 'vant';
import { isApp } from './Platform';
import { jsCallNative } from './jsBridge';
import { getMerchantId } from '@/util/EnvUtil';
import { netNoData } from '@/util/Platform';
/**
 *
 * @param type 类型: 'mutual':公募基金，pe:私募基金
 */
export const getToDoList = (type: string) => {
  return new Promise<any>((resolve, reject) => {
    todoList().then((res) => {
      let arr: any[] = [];
      let po: string[] = ['0', '1', '2', '3', '4', '5', '6', '7', '12', '14'];
      let pe: string[] = ['0', '1', '2', '3', '4', '5', '6', '7', '11', '12', '14'];
      switch (type) {
        case 'mutual':
          arr = res?.list?.filter((item: any) => po.includes(item.value));
          break;
        case 'pe':
          arr = res?.list?.filter((item: any) => pe.includes(item.value));
          break;
        default:
          break;
      }
      resolve(arr);
    });
  });
};
/**
 * 分销用户弹窗
 */
export const distribution = (item: any) => {
  Dialog.confirm({
    title: '温馨提示',
    confirmButtonText: '立即同步',
    cancelButtonText: '稍后处理',
    messageAlign: 'left',
    allowHtml: true,
    message:
      '<p>是否将您在<span style="color:#DC2828">(' +
      item.keyword +
      ')</span>购买的基金资产同步到利得平台，同步成功后，即可在利得查询到您在<span style="color:#DC2828">(' +
      item.keyword +
      ')</span>的资产！<span style="font-weight: 500">在<span style="color:#DC2828">(' +
      item.keyword +
      ')</span>将不能再查询资产，</span>同步完成大约需要10分钟，同步成功后需重新登录。</p>',
  })
    .then(() => {
      setTransfer().then((res) => {});
    })
    .catch(() => {});
};
/**
 *
 * @param router 路由跳转
 */
export const dialogTips = (arr: any[], router: any) => {
  let title: string = arr[0].popUpTitle;
  let message: string = arr[0].popUpLabel;
  let cancelButtonText: string = arr[0].cancelLabel;
  let confirmButtonText: string = arr[0].confirmLabel;
  let path: string = '';
  if (arr.length >= 2) {
    message = '在交易前，请先完善账户相关信息';
    path = '/html5/management/TodoList';
    cancelButtonText = '取消';
    confirmButtonText = '确定';
  } else {
    switch (arr[0].value) {
      // 未上传身份证
      case '0':
        path = '/html5/openaccount/openaccount';
        break;
      // 补充个人信息
      case '1':
        path = '/html5/management/userInfo';
        break;
      // 未作风险测评
      case '2':
        path = '/html5/management/Risk';
        break;
      // 税收居民身份声明
      case '3':
        path = '/html5/management/taxResidentStatusStatement';
        break;
      // 身份证过期
      case '4':
        path = '/html5/management/reocr';
        break;
      // 添加银行卡
      case '5':
        path = '/html5/openaccount/openaccount';
        break;
      // 分销用户迁移
      case '6':
        distribution(arr[0]);
        break;
      // 设置交易密码
      case '7':
        path = '/html5/openaccount/openaccount';
        break;
      // 合同签署失败
      case '9':
        path = '/html5/pe/todo/pendingContract';
        break;
      // 私募待回访
      case '10':
        path = '/html5/pe/todo/pendingVisit';
        break;
      case '11':
        if (netNoData.includes(getMerchantId())) {
          path = '/html5/pe/qic/Incl';
        } else {
          Toast('请前往利得app认证');
        }
        break;
      // 分销银行卡
      case '12':
        path = '/html5/management/bank';
        break;
      // 持仓风险等级调整
      case '13':
        path = '/html5/management/RiskWarning';
        break;
      // 风险评测过期
      case '14':
        path = '/html5/management/Risk';
        break;
      default:
        break;
    }
  }
  if (path) {
    Dialog.confirm({
      title: title,
      message: `<div style="text-align: center;"><p style="display: inline-block;text-align: left;">${message}</p></div>`,
      cancelButtonText: cancelButtonText,
      confirmButtonText: confirmButtonText,
      messageAlign: 'left',
      allowHtml: true,
    }).then(() => {
      if (isApp) {
        if (arr.length > 1) {
          jsCallNative('PEJumpScheduleList', { scheduleList: '1' });
        } else {
          jsCallNative('PEJumpScheduleList', {
            scheduleList: '0',
            scheduleType: arr[0].value,
            bankName: '',
          });
        }
      } else {
        router.push(path);
      }
    });
  }
};
