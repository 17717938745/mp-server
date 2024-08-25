/*
 * @Author: your name
 * @Date: 2023-12-08 15:57:07
 * @LastEditTime: 2024-06-19 16:28:05
 * @LastEditors: 韦玮莹
 * @Description: In User Settings Edit
 * @FilePath: \dmmp_page\src\typing\ma\PageConfig.ts
 */
/*
 * @Author: your name
 * @Date: 2023-12-08 15:57:07
 * @LastEditTime: 2023-12-08 15:57:20
 * @LastEditors: 韦玮莹
 * @Description: In User Settings Edit
 * @FilePath: \dmmp_page\src\typing\ma\PageConfig.ts
 */

export interface IEditHighEndData {
  fundCode: string;
  label: string;
  label2Data: any[];
  label3: string;
  label3Custom?: boolean;
  label3Type: string;
  label4: string;
  label4Custom?: boolean;
  label4Type: string;
  label5: string;
  netNo: string;
  label5Pre?: string;
}
export interface IEditHighEndParams {
  fundCode: string;
  netNo: string;
  label: string; //位置一内容
  label2Data: Array<{
    type: string;
    content: string;
  }>;
  label3: string; //位置三内容
  label3Type: string; // 位置三样式（0-阶段涨幅，1-最大回撤，2-业绩报酬计提基准）
  label4: string; //位置4内容
  label4Type: string; // 位置四样式（0-投资策略，1-产品标语，2-开放申购周期，3-开放赎回周期，4-最大回撤，5-锁定期）
  label5Pre: string;
  label5: string;
}
export interface IRateValueMap {
  code: string;
  dateType: string;
  label: string;
  rateValue: number;
  rateValueFormat: string;
  sorter: number;
  standardRateValue: number;
  standardRateValueFormat: string;
}
export interface IEditHighEndConfigData {
  label3Default: string;
  label3TypeDefault: string;
  fundCode: string;
  fundName: string;
  fundTypeFormat: string;
  lockTimeFormat: string;
  performanceExtractStandardFormat: string;
  purchasePeriodFormat: string;
  rateValue: number;
  rateValueFormat: string;
  redeemPeriodFormat: string;
  retrace: number;
  retraceFormat: string;
  subscribeEndTimeFormat: string;
  productTypeFormat: string;
  riskLevelName: string;
  closedPeriod: string;
  minAddAmount: string;
  minBuy: number;
  rateValueList: Array<IRateValueMap>;
  lockPeriod: string;
  minValue: number;
  minValueFormat: string;
  minAddValue: number;
  minAddValueFormat: string;
  modList: Array<IRateValueMap>;
  lockPeriodFormat: string;
  label4TypeDefault: string;
}
export interface IHighEndEnum {
  productTypeList: Array<{ value: string; label: string }>;
  netNoList: Array<{ value: string; label: string }>;
}
export enum EMode {
  // '1'-组合模式 2-产品模式 3-Banner模式
  Combination = 1,
  Product = 2,
  Banner = 3,
}

export enum EDeployScope {
  // 1总资产, 2持仓类型, 3持仓基金
  TotalAssets = 1,
  HoldType = 2,
  HoldFund = 3,
}
// 发布范围
export const EDeployScopeExplMap = new Map<EDeployScope, string>([
  [EDeployScope.TotalAssets, "总资产"],
  [EDeployScope.HoldType, "持仓类型"],
  [EDeployScope.HoldFund, "持仓基金"],
]);

// 持仓类型
export enum EAssetType {
  // 4:利活宝、1:基金、11:高端理财、6:投顾
  lhb = 4,
  fund = 1,
  highEnd = 11,
  investor = 6,
}
export const EAssetTypeExpl = new Map<EAssetType, string>([
  [EAssetType.lhb, "利活宝"],
  [EAssetType.fund, "基金"],
  [EAssetType.highEnd, "高端理财"],
  [EAssetType.investor, "投顾"],
]);

export enum EConfigType {
  // 配置模式 1: 图文 2: banner
  Text = 1,
  Banner = 2,
}

export const EConfigTypeExpl = new Map<EConfigType, string>([
  [EConfigType.Text, "图文"],
  [EConfigType.Banner, "banner"],
])

export enum EJumpLinkType {
  fund = '1',
  self = '2'
}
export enum EOpenState {
  up = 0,
  down = 1,
}
// 私募产品类型
export enum EProductType {
  sm = "01", // 私募
  zg = "02", //资管
}
const EProductTypeExpl = {
  [EProductType.sm]: "私募",
  [EProductType.zg]: "资管",
}