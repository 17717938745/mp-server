/*
 * @Author: YourName
 * @Date: 2022-02-22 09:49:37
 * @LastEditTime: 2024-08-06 16:20:26
 * @LastEditors: liushengzhuang shengzhuang.liu@leadbank.com.cn
 * @Description:
 * @FilePath: \h5\src\typing\h5tool\Transaction.tsx
 * 版权声明
 */
export interface BankCardListItem {
  bankAccountFormat: string;
  bankCardId: string;
  bankName: string;
  dayMaxAmount: number;
  dayMaxAmountFormat: string;
  cardTailNumber: string;
  icon: string;
  lastUsed: boolean;
  perMaxAmount: number;
  perMaxAmountFormat: string;
  share: null;
  tradeAccount: string;
  treasure: boolean;
  valid: boolean;
}
export interface DiscountListItem {
  amount: number;
  amountFormat?: any;
  discount: number;
  useRateValue: boolean;
  value: number;
  valueFormat?: string | null;
}
export interface Item {
  label: string;
  url: string;
}
export interface RiskResult {
  riskTest: boolean;
  riskWarning: boolean;
  accountRiskFormat: string;
  fundRiskFormat: string;
}
export interface IFundInfo {
  businessType?: string;
  fundCode: string;
  fundName?: string;
  fundNameFormat?: string;
  fundType?: string;
  fundTypeName?: string;
  minHoldAmount?: number;
  nav?: number;
  riskLevel?: string;
  riskLevelName?: string;
  riskLevelNameFormat?: string;
  protocolList?: Array<Item>;
  treasure?: boolean;
  profilePath?: any;
  fundState?: any;
}
export interface ReeemFundInfo {
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeName: string;
  minHoldAmount: number;
  nav: number;
  navDate: string;
  navFormat: string | null;
  riskLevel: string;
  riskLevelName: string;
  riskLevelNameFormat: string;
  treasure: boolean;
  prodType: string;
  prodTypeFormat: string | null;
}
export interface ITradeLimit {
  addMultiple: boolean;
  businessType: string;
  businessTypeFormat: string;
  fundCode: string;
  maxSumValue: number;
  maxSumValueFormat: string;
  maxValue: number;
  maxValueFormat: string;
  minAddValue: number;
  minAddValueFormat: string;
  minHoldShare: number;
  minHoldShareFormat: string;
  minValue: number;
  minValueFormat: string;
  usableBuyAmount: number | null;
  usableBuyAmountFormat: number | null;
}
export interface RedeemTradeLimit {
  addAmount: number;
  addMultiple: boolean;
  maxAmount: number;
  maxAmountFormat: string;
  maxValue: number;
  maxValueFormat?: string;
  minValue: number;
  minValueFormat?: string;
  minAmount: number;
  minAmountFormat: string;
  minHoldShare: number;
  minHoldShareFormat: string;
  maxSumValue: number;
  maxSumValueFormat: string;
  passageValue: number;
  passageValueFormat?: string;
  usableBuyAmount: number | null;
  usableBuyAmountFormat: string | null;
  usableRedeemShare: number | null;
  usableRedeemShareFormat?: string | null;
}
export interface PeTradeLimit {
  addMultiple: boolean;
  businessType: string;
  businessTypeFormat: string;
  fundCode: string;
  maxSumValue: number;
  maxSumValueFormat: string;
  maxValue: number;
  maxValueFormat: string;
  minAddValue: number;
  minAddValueFormat: string;
  minHoldShare: number;
  minHoldShareFormat: string;
  minValue: number;
  minValueFormat: string;
  usableBuyAmount: number;
  usableBuyAmountFormat: string;
}
export interface PeSign {
  firstBuy: boolean;
  flowCode: string;
  message: string;
  signState: number;
  signModel: number;
  signStateFormat: string;
}
export declare interface purchaseFormResult {
  treasureFundCode: string;
  bankCardList: Array<BankCardListItem>;
  confirmDateFormat: string;
  discountList: Array<DiscountListItem>;
  financialPeriodDescribe: null;
  forbidLargeTrade?: boolean;
  forbidLargeTradeEndTime?: string;
  forbidLargeTradeStartTime?: string;
  fundInfo: IFundInfo;
  icbcProtocolList: Array<Item>;
  largeTradeLimit: {
    addMultiple: boolean;
    businessType: string;
    businessTypeFormat: string;
    fundCode: string;
    maxSumValue: number;
    maxSumValueFormat: string;
    maxValue: number;
    maxValueFormat: string;
    minAddValue: number;
    minAddValueFormat: string;
    minHoldShare: number;
    minHoldShareFormat: string;
    minValue: number;
    minValueFormat: string;
    usableBuyAmount: number | null;
    usableBuyAmountFormat: number | null;
  };
  profitDateFormat: string;
  redeemToPurchaseProtocol: Array<Item>;
  tradeLimit: ITradeLimit;
  risk: RiskResult;
  sign: any;
  tradeDay: boolean;
}
interface ModifyFixInvestInfo {
  amount: number;
  amountForamt: string;
  periodChildCode: string;
  periodCode: string;
  cardInfo: BankCardListItem;
}
export declare interface FixedFormResult {
  bankCardList: Array<BankCardListItem>;
  fundInfo: IFundInfo;
  protocolList: Array<Item>;
  period: {
    defaultPeriodCode: string;
    defaultPeriodChildCode: string;
  };
  risk: RiskResult;
  tradeLimit: ITradeLimit;
  modifyFixInvestInfo: ModifyFixInvestInfo;
}
export interface ShareListItem {
  appointDate: string;
  arriveDate: any;
  tradeDate: string;
  tradeDateFormat: string;
  bankCardLaterThenTreasure: number;
  receiptDateFormat: string;
  bankAccount: string;
  bankAccountFormat: string;
  tradeAccount: string;
  bankName: string;
  bankFormat: string;
  bankNo: string;
  icon: string;
  share: number;
  needChange: boolean;
  shareFormat: string;
  valid?: boolean;
  treasure?: boolean;
  treasureReceiptDateFormat: string;
}
export declare interface RedeemShare {
  appointRedeem: boolean;
  appointShareList: ShareListItem[];
  appointTotalShare: number;
  normalRedeem: boolean;
  normalShareList: ShareListItem[];
  normalTotalShare: number;
}
export declare interface IFundRedeem {
  appointReceiptDateFormat: string;
  appointRedeemDate: string;
  appointRedeemDateFormat: string;
  appointRedeemStatus: boolean;
  lhbAppointRedeemDateFormat?: string;
  receiptDateFormat: string;
  redeemToTreasureProtocol: Array<Item>;
  treasureReceiptDateFormat: string;
}
export declare interface RedeemFormResult {
  shareList?: Array<ShareListItem>;
  fundRedeem: IFundRedeem;
  fundInfo: IFundInfo;
  tradeLimit: ITradeLimit;
  treasureFastRedeem: null;
  treasureRedeem: null;
  redeemShare: RedeemShare;
}
export declare interface SectionListItem {
  id: string;
  link: string;
  label: string;
  tips: string;
  content: string;
  param: any;
  type: number;
}
export declare interface SectionBlockListItem {
  id: string;
  link: string;
  linkType: number;
  title: string;
  sectionList: SectionListItem[];
}

// 交易订单详情
export declare interface IOrderDetail {
  channelInfo: IChannelInfo;
  fundInfo: IFundInfo;
  assetType: string;
  hugeRedeem: null;
  orderNo: string;
  summary: ISummary;
  timeLine: ITimeLine;
  title: string;
  tradeState: ITradeState;
  sectionBlockList: SectionBlockListItem[];
}
interface IChannelInfo {
  title: string;
  tradeChannelFormat: string;
}
export declare interface IFundInfo {
  fundCode: string;
  fundNameFormat?: string;
  title?: string;
}

interface ISummary {
  applyDate?: string;
  applyDateFormat?: string;
  appointDate?: string;
  appointDateFormat?: string;
  businessType: string;
  cancel: boolean;
  confirmState?: string;
  confirmStateFormat?: string;
  unit?: string;
  describe: string;
  valueFormat: string;
}

export interface ITimeLine {
  timeLineIndex: number;
  timeLineList: ITimeLineItem[];
}
export interface ITimeLineItem {
  date?: string;
  dateFormat: string;
  describe: string;
}
interface ITradeState {
  title: string;
  tradeState: string;
  tradeStateFormat: string;
}
export declare interface discountListTtem {
  amount: number;
  discount: number;
  useRateValue: boolean;
  value: number;
}
export declare interface FirstBuyPage {
  lhbSwitch: string;
  activeImageUrl: string;
  lhbIntroduce: string;
  lhbIntroduceUrl: string;
  dayGain: string;
  dayGainDate: string;
  fundName: string;
  fundCode: string;
  yearlyRateFormat: string;
  fundRateFormat: string;
}
export declare interface Risk {
  accountRiskFormat: string;
  accountRiskLevel: string;
  fundRisk: string;
  fundRiskFormat: string;
  highAge: boolean;
  highRisk: boolean;
  riskTest: boolean;
  riskWarning: boolean;
}
export declare interface RedeemFundInfo {
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeName: string;
  minHoldAmount: number | null;
  nav: number;
  riskLevel: string;
  riskLevelName: string;
  riskLevelNameFormat: string;
  treasure: boolean;
}
// 预约赎回字段
export declare interface BookingSalesDetail {
  fundInfo: IFundInfo;
  orderNo: string;
  sectionBlockList: SectionBlockListItem[];
  summary: ISummary;
  timeLine: ITimeLine;
  title: string;
  tradeState?: ITradeState;
}
// 购买表单类型校验
// export declare interface BuyFormDataResult {
//   bankCardList:
// }
