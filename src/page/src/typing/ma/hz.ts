/*
 * @Author: wwy
 * @Date: 2023-06-11 16:24:26
 * @LastEditTime: 2023-12-08 15:55:54
 * @LastEditors: 韦玮莹
 * @Description: 
 * @FilePath: \dmmp_page\src\typing\ma\hz.ts
 * 版权声明
 */
export interface ICusItem {
  accountId: string;
  createTime: string;
  createTimeFormat: string;
  mobile: string;
  name: string;
  openState: 1;
  openStateFormat: string;
  openTime: string;
  recommendCode: string;
  sumBuyAmount: number;
  sumBuyAmountFormat: string;
  sumHoldAmount: number;
  sumHoldAmountFormat: string;
  riskLevelFormat: string;
  affirmState: number;
  affirmStepOfAccount: number
}

export interface IAppointment {
  accountId: string;
  appointState: string;
  appointStateFormat: string;
  appointTime: string;
  appointTimeFormat: string;
  endAppointTradeTime: string;
  endAppointTradeTimeFormat: string;
  endValidTime: string;
  endValidTimeFormat: string;
  fundCode: string;
  fundName: string;
  mobile: string;
  name: string;
  operatorUserId: string;
  operatorUserIdFormat: string;
  orderState: string;
  orderStateFormat: string;
  planNo: string;
  startAppointTradeTime: string;
  startAppointTradeTimeFormat: string;
  productTypeFormat: string;
  trustrequestNo: string
}
export interface ITradeItem {
  fundCode: string;
  fundName: string;
  mobile: string;
  name: string;
  orderTime: string;
  applyDate: string;
  confirmDate: string;
  confirmFlag: string;
  confirmFlagFormat: string;
  businessType: string;
  businessTypeFormat: string;
  confirmAmount: number;
  confirmAmountFormat: string;
  confirmShare: number,
  confirmShareFormat: number
}