import { OptionItem } from '../ma/System';
export declare interface ViewDetailWrapper {
  viewId: string;
  title: string;
  createdTime: string;
  createdTimeFormat: string;
  html: string;
}
export declare interface infoType {
  mobile: string;
  nickName: string;
}

export declare interface IFundNavHisItem {
  fundCode: string;
  fundName: string;
  nav: number;
  navFormat: string;
  accumulativeNav: number;
  accumulativeNavFormat: string;
  dayRateValue: number;
  dayRateValueFormat: string;
  date: string;
}
export declare interface IFundRankingItem {
  rateValueType: number;
  rateValueTypeFormat: string;
  rateValue: number;
  rateValueFormat: string;
  standardRateValue: number;
  standardRateValueFormat: string;
  BigDecimal: number;
  total: number;
  sorterFormat: string;
  date: string;
  sorterChange: string;
  starCount: number;
}
export declare interface IFundYieldItem {
  dateTime: string;
  hsYield?: number;
  hsYieldFormat?: string;
  isPurchase?: null;
  isRedeem?: null;
  totalYield: number;
  totalYieldForamt: string;
}
export interface IHsYieldItem {
  dateTime: string;
  hsYield: number;
  hsYieldFormat: null;
  isPurchase: null;
  isRedeem: null;
  totalYield: null;
  totalYieldForamt: null;
}
export interface IManagerHis {
  fundCode: string;
  fundName: string;
  officeBeginDate: string;
  officeBeginDateFormat: string;
  officeEndDate: null;
  officeEndDateFormat: null;
  officeType: string;
  officeRepay: number;
  officeRepayFormat: string;
  leaveOfficeCause: null;
}
export interface IManagerItem {
  years: number;
  repay: number;
  days: number;
  repayFormat: string;
  managerName: string;
  managerremark: null;
  resume: string;
  assumedate: string;
  firstDate: string;
  managerId: string;
  companyId: null;
  fundManagerName: string;
  headUrl: string;
  redound: string;
  managerHistotyList: Array<IManagerHis>;
}
interface IFundDetailFund {
  agent: boolean;
  buy: boolean;
  fixedInvest: boolean;
  follow: boolean;
  fundCode: string;
  fundFullName: string;
  fundManagerPoint: string;
  fundManagerPointIcon: string;
  fundManagerPointTitle: string;
  fundName: string;
  fundState: string;
  fundStateFormat: string;
  fundType: string;
  fundTypeFormat: string;
  fundTypeName: string;
  nav: number;
  navDate: string;
  navDateFormat: string;
  navFormat: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueLabel: string;
  riskLevel: string;
  riskLevelFormat: string;
  subscribe: boolean;
  subscribeEnd: boolean;
  subscribeEndDate: string;
  subscribeEndDateFormat: string;
  subscribeStartDate: string;
  subscribeStartDateFormat: string;
  tagNameList: string[];
  unitProfit: number;
  unitProfitFormat: string;
  yearlyRateValue: number;
  yearlyRateValueFormat: string;
}
interface IFundNavList {
  accumulativeNav: number;
  accumulativeNavFormat: string;
  date: string;
  dayRateValue: number;
  dayRateValueFormat: string;
  fundCode: string;
  fundName: string;
  nav: number;
  navFormat: string;
}
interface IFundCompany {
  companyId: string;
  companyLogo: string;
  companyName: string;
}
interface IBuyFee {
  buyFee: number;
  buyFeeFormat: string;
  discountBuyFee: number;
  discountBuyFeeFormat: string;
}
interface IDividendInfo {
  divExDate: string;
  divPayDate: string;
  divPerShare: number;
  divPerShareFormat: string;
  divRegDate: string;
  fundCode: string;
}
// 基金详情结果
export declare interface IFundDetailResult {
  dividendInfo: IDividendInfo;
  fundStateFormat: string;
  fixedInvest: string;
  fund: IFundDetailFund;
  currencyFlag?: boolean;
  follow: boolean;
  companyId: string;
  companyLogo: string;
  companyName: string;
  confirmDate: string;
  confirmDateDesc: string;
  currDate: string;
  currDateDesc: string;
  fundCode: string;
  fundDividendList: null;
  fundFullName: string;
  fundName: string;
  fundNavHisList: Array<IFundNavHisItem>;
  fundRankingList: Array<IFundRankingItem>;
  fundType: string | null;
  fundTypeName: string;
  fundYieldTrendList: {
    fundYieldList: Array<IFundYieldItem>;
    hsYieldList: Array<IHsYieldItem>;
  };
  managerLst: Array<IManagerItem>;
  nav: number;
  navDate: string;
  navDateFormat: string;
  navFormat: string;
  newFundFlag: string;
  profitDate: string;
  profitDateDesc: string;
  rate: number;
  rateFormat: string;
  riskLevel: string;
  riskLevelFormat: string;
  periodList: Array<OptionItem<string>> | [];
  defaultPeriod: string;
  fundNavList: IFundNavList[];
  fundCompany: IFundCompany;
  buyFee: IBuyFee;
}

// 基金基本信息
export declare interface IFundArchiveInfo {
  fundArchiveBasicInfo: IFundArchiveBasicInfo;
  fundDividendList: IFundDividendItem[];
  fundNoticeList: IFundNoticeItam;
  fundPositionModel: IFundPositionModel;
}
interface IStockHeavy {
  stockNameFormat: string;
  rationFormat: string;
}
// 基金基本信息-概况
export declare interface IFundArchiveBasicInfo {
  assetAmountFormat: string;
  assetAmountDateFormat: string;
  collectfeesType: string;
  companyId: string;
  companyName: string;
  companylogo: string;
  defaultdividendType: string;
  foundDate: string;
  fundCode: string;
  fundEntrustPer: string;
  fundManagePerson: string;
  fundManagerName: string;
  fundName: string;
  fundType: string;
  fundTypeName: string;
  investmentProp: string;
  investmentStrat: string;
  reportDate: string;
  share: number;
  shareFormat: string;
}

// 基金基本信息- 公告
export declare interface IFundNoticeItam {
  content: string;
  fundCode: string;
  noticeDate: string;
  title: string;
}
// 基金基本信息 - 持仓
export declare interface IFundPositionModel {
  cashPercent: number;
  cashPercentFormat: string;
  debtPercent: number;
  debtPercentFormat: string;
  industryList: IFundInfoIndustryItem[];
  reportDate: string;
  reportDateFormat: string;
  stockHeavyList: IStockHeavy[];
  stockPercent: number;
  stockPercentFormat: string;
  heavyDebtList: IHeavyDebt[];
}
interface IHeavyDebt {
  industry: string;
  ratioFormat: string;
}
export declare interface IFundInfoIndustryItem {
  industry: string;
  ratio: number;
  ratioFormat: string;
}
// 基金基本信息 - 分红
export declare interface IFundDividendItem {
  fundCode: string;
  fundName: string;
  registerDate: string;
  exdividendDate: string;
  everyDividend: number;
  everyDividendFormat: string;
  dividendDate: string;
}

// 基金规则
export declare interface IFundTrandRule {
  buyDateList: IRuleItem[];
  buyDiscount: string;
  custodyRate: string;
  expAccountDate: string;
  fundCode: null;
  fundName: null;
  fundState: string;
  fundType: string;
  isSalesAgent: string;
  manageRate: string;
  newFundFlag: string;
  operationPeriod: null;
  orderDetail: string;
  promptDesc: string;
  purConfirm: string;
  repurConfirm: string;
  saleServiceRate: string;
  purRateList: IRuleItem[];
  repurRateList: IRuleItem[];
  saleDateList: IRuleItem[];
  subRateList: IRuleItem[];
}
interface IRuleItem {
  amountDesc: string;
  confirmDate: null;
  disCount: null;
  disValue: string;
  fareType: null;
  gainDate: null;
  maxAmt: null;
  minAmt: null;
  rate: null;
  rateType: null;
  rateTypeName: null;
  rateValue: string;
  transDate: null;
  value: null;
}
// 持仓详情返回结果
export interface IPeriodItem {
  value: string;
  label: string;
}
interface IFundInfo {
  buy: boolean;
  dividendFlag: boolean;
  dividendMethod: string;
  dividendMethodDes: string;
  fixedInvest: boolean;
  fundCode: string;
  fundName: string;
  redeemFlag: boolean;
  useableFlag: boolean;
}
interface IFundAsset {
  dayProfit: number;
  dayProfitFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
  holdProfitRate: string;
  holdProfitRateFormat: string;
  marketValue: number;
  marketValueFormat: string;
  nav: number;
  navFormat: string;
  profitDate: string;
  profitDateFormat: string;
  totalProfit: number;
  totalProfitFormat: string;
  tradeAccount: string;
}
export interface IHoleDetailResult {
  fundInfo: IFundInfo;
  fundAsset: IFundAsset;
  defaultPeriod: string;
  periodList: IPeriodItem[];
  confirmDetailInfo: [];
}

// 用户信息
export declare interface IUserInfo {
  accountId: string;
  idNoFormat: string;
  idNo: string;
  mobileFormat: string;
  nameFormat: string;
  nameNoEnx: string;
  step: {
    index: number;
  };
}

export interface IBankCardForm {
  bankNo: string;
  bankImg: string;
  bankName: string;
  code: string;
  bankProvinceName?: string;
  bankProvinceId?: string;
  bankCityName?: string;
  bankCityId?: string;
  bankAccount?: string;
  bankMobile: string;
}
// 通用银行卡列表options
export declare interface IBankCardOptionItem {
  bankName: string;
  bankNo: string;
  channelCode: string;
  dayAmount: null;
  icon: string;
  img: string;
  productType: string;
  purchaseLimit: string;
  shortName: string;
  transferAmount: number;
  opened: boolean;
  perMaxAmountFormat: string;
  dayMaxAmountFormat: string;
}

// 我的地址项
export interface IAddressItem {
  addressId: string;
  area: null | string;
  city: string;
  contactPerson: string;
  defaultAddress: string;
  detailAddress: string;
  phone: string;
  province: string;
}

interface IRateValue {
  buy: boolean;
  date: string;
  rateValue: number;
  sale: boolean;
  standardRateValue: number;
}
// 七日年化/万份收益的返回数据
export declare interface IYieldResult {
  dateFormat: string;
  fundYieldForamt: string;
  fundYieldList: IFundYieldItem[];
  hsYieldFormat: string;
  hsYieldList: IFundYieldItem[];
  date: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueLabel: string;
  rateValueList: IRateValue[];
  standardRateValue: number;
  standardRateValueFormat: number;
  standardRateValueLabel: string;
}
export interface ITypologicalAssetSummary {
  assetType?: string;
  assetTypeFormat: string;
  dayProfit: number;
  dayProfitFormat: string;
  frozenAmount: number;
  frozenAmountFormat: string;
  holdAmount: number;
  holdAmountFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
  productType: string;
  totalProfit: number;
  totalProfitFormat: string;
  path?: string;
  slogan?: string;
  rightTitle?: string;
  netNo?: string;
}
export interface IIcon {
  imgUrl: string;
  label: string;
  linkUrl: string;
}
export interface ISummary {
  dayProfit: number;
  dayProfitFormat: string;
  profitDateFormat: string;
  largeToBeConfirmCount: number;
  holdAmount: number;
  holdAmountFormat: string;
  totalProfit: number;
  totalProfitFormat: string;
  typologicalAssetSummaryList: ITypologicalAssetSummary[];
  iconList: IIcon[];
  largeToBeConfirmList: any[];
}
export interface TodoItem {
  contentType: number;
  contentTypeFormat: string;
  count: number;
  keyword: any;
  label: string;
  message: string;
  value: string;
}
export interface IAcountDetail {
  addressInfo: IAddressInfo;
  annualIncomeValue?: number;
  annualIncomeCode?: string;
  annualIncome?: number;
  address?: string;
  age?: string;
  benefitIdNoFormat?: string;
  benefitAccountName?: string;
  benefitCode?: string;
  genderFormat?: string;
  name?: string;
  idNoFormat?: string;
  idTypeFormat?: string;
  income?: string;
  occupationCode?: string;
  occupationName?: string;
  expireDayFormat?: string;
  fullAddress: string;
  expireFlag: boolean;
  genderFlag: boolean;
}
export interface IAddressInfo {
  addressId: string;
  area: string;
  areaCode: string;
  city: string;
  cityCode: string;
  contactPerson: string;
  defaultAddress: boolean;
  detailAddress: string;
  mobile: string;
  province: string;
  provinceCode: string;
}
