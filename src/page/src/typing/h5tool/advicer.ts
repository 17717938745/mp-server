import { S } from 'vite/dist/node/types.d-jgA8ss1A';

interface ITradeRule {
  buyInfo: string;
  confirmInfo: string;
  diffRate: number;
  inConfirmDay: string | number;
  moneyAccountDay: string;
  outConfirmDay: string;
  profitInfo: string;
  profitLossDay: string;
  serviceRate: TServiceRate[];
  minInAmount: number;
}
interface TServiceRate {
  val: string;
}
export interface IProduct {
  state: string;
  tradeRule: ITradeRule;
  company: string;
  companyCode: string;
  companyLogo: string;
  dayRate: number;
  dayRateFormat: string;
  feeType: string;
  head1: string;
  head1Val: any;
  head1Name: string;
  head2Name: string;
  head2: string;
  head2Val: string;
  holdInfo: string;
  infoUrl: string;
  isHome: boolean;
  isShow: boolean;
  maxYearDrawDown: string;
  minAmount: string;
  nav: string;
  navDate: string;
  onlineDate: string;
  operationDesc: string;
  productCode: string;
  productName: string;
  riskCode: string;
  riskName: string;
  setupDate: string;
  shortInfo: string;
  showProfit: string;
  targetProfit: string;
  type: string;
  docPlan: IProductDocItem;
  docProduct: IProductDocItem;
  docQaList: IProductDocItem[];
  tagList: string[];
  operationDays: number;
  sharpeRate: number;
  sharpeRateFormat: string;
  yearlyVolatility: number;
  yearlyVolatilityFormat: string;
  maxDrawdown: number;
  maxDrawdownFormat: string;
}

interface IProductDocItem {
  backgroundUrl: string;
  code: string;
  createdTime: string;
  creator: string;
  date: null;
  deletedFlag: number;
  docId: string;
  docType: string;
  id: string;
  introduction: string;
  isAll: boolean;
  lastModifiedTime: string;
  modifier: string;
  remark: null;
  title: string;
  type: string;
  url: string;
}

/**
 * 投顾全部交易列表项
 */
export interface TradeItem {
  transValueFormat: string;
  transStatusFormat: string;
  transTypeFormat: string;
  orderId: string;
  tradeType: string;
  tradeTypeDisplay: string;
  transType: string;
  productCode: string;
  productName: string;
  transDate: string;
  transTime: string;
  transTimeFull: string;
  transAmt: number;
  requestAmtDisplay: number;
  saleRate: number;
  fee: number;
  transStatus: string;
  transStatusDisplay: string;
  bankName: string;
  bankTail: string;
  canRevoke: string;
}
export interface IAssetListItem {
  appointOrder: boolean;
  dayProfit: number;
  dayProfitFormat: string;
  frozenAmount: number;
  frozenAmountFormat: string;
  fundCode: string;
  fundEnd: boolean;
  fundName: string;
  historyImport: boolean;
  holdAmount: number;
  holdAmountFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
  passageAmount: number;
  passageAmountFormat: string;
  productType: string;
  profitDate: string;
  profitDateFormat: string;
  totalProfit: number;
  totalProfitFormat: string;
}
interface IAssetSummary {
  dayProfit: number;
  dayProfitFormat: string;
  frozenAmount: number;
  frozenAmountFormat: string;
  holdAmount: number;
  holdAmountFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
  largeToBeConfirmAmount: number;
  largeToBeConfirmAmountFormat: string;
  largeToBeConfirmCount: number;
  largeToBeConfirmList: any[];
  passageAmount: number;
  passageAmountFormat: string;
  profitDate: string;
  profitDateFormat: string;
  totalProfit: number;
  totalProfitFormat: string;
}
// 利活宝持仓
export interface ITG_Asset {
  assetList: IAssetListItem[];
  assetSummary: IAssetSummary;
}
/**
 * 持仓详情
 */
export interface AssetItem {
  availableShare: number;
  availableShareDisplay: string;
  company: null;
  dayProfit: number;
  dayProfitDisplay: string;
  days: null;
  holdProfit: number;
  holdProfitDisplay: string;
  marketValue: number;
  marketValueDisplay: string;
  productCode: string;
  productName: string;
  totalProfit: number;
  totalProfitDisplay: string;
  updateDate: string;
}
/**
 * 投顾银行卡数据定义
 */
export interface BankItem {
  bankAccountFormat: any;
  bankCardId: string;
  bankName: string;
  dayMaxAmount: number;
  dayMaxAmountFormat: string;
  icon: string;
  perMaxAmount: number;
  perMaxAmountFormat: string;
  share: any;
  treasure: boolean;
  valid: boolean;
}
export interface Revoke {
  orderId: string;
  scheduleList: null;
  transTime: null;
  txnCode: string;
  txnMessage: string;
  txnMsg: string;
  txnStatus: string;
}
export interface ScheduleListItem {
  active: number;
  time: string;
  title: string;
}
interface IDetailInfo {
  title: string;
  rows: Array<{ title: string; value: string; type: boolean }>;
}
export interface TradDetailsData {
  detailInfo: IDetailInfo[];
  transValueFormat: string;
  bankName: string;
  bankTail: string;
  canRevoke: string;
  confirmDate: string;
  fee: number;
  feeFormat: string;
  orderId: string;
  productCode: string;
  productName: string;
  repealTime: string;
  retMessage: string;
  revokeMessage: null;
  saleRate: null;
  saleRateFormat: null;
  scheduleList: ScheduleListItem[];
  transAmt: number;
  transAmtFormat: string;
  transDate: string;
  transStatus: string;
  transStatusFormat: string;
  transTime: string;
  transTimeFull: string;
  transType: string;
  transTypeFormat: string;
  workDay: string;
  emptyInfo: boolean;
}
export interface FundListItem {
  addFlag: any;
  afterPer: number;
  afterPerFormat: string;
  beforePer: any;
  beforePerFormat: any;
  changeFlag: number;
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeName: string;
  marketValue: any;
  marketValueFormat: any;
  nav: number;
  navDate: any;
  navFormat: string;
  rose: number;
  roseFormat: string;
}
export interface AssetDetail {
  fundList: FundListItem[];
  redeem: boolean;
  purchase: boolean;
  skip: boolean;
  summary: {
    dayProfit: number;
    dayProfitFormat: string;
    holdProfit: number;
    holdProfitFormat: string;
    holdProfitRate: null;
    holdProfitRateFormat: string;
    marketValue: number;
    marketValueFormat: string;
    onloadAmt: number;
    onloadAmtFormat: string;
    totalProfit: number;
    totalProfitFormat: string;
    updateDate: string;
    product: any;
  };
}

interface IbannerItem {
  goUrl: string;
  picUrl: string;
}
export interface IDocItem {
  backgroundUrl: string;
  code: string;
  createdTime: string;
  creator: string;
  date: string;
  deletedFlag: number;
  docId: string;
  docType: string;
  id: string;
  introduction: string;
  isAll: boolean;
  lastModifiedTime: string;
  modifier: string;
  remark: string;
  title: string;
  type: string;
  url: string;
  typeFormat: string;
  fontColor: string;
}
interface IheadItem {
  iconUrl: string;
  value: string;
}
export interface IRiskItem {
  code: string;
  holdInfo: string;
  info: string;
  name: string;
  nickName: string;
  products: IRiskProduct[];
}
interface IRiskProduct {
  code: string;
  companyCode: string;
  companyLogo: null;
  companyName: string;
  companyNickName: string;
  head1: string;
  head1Val: string;
  head1Name: string;
  head2: string;
  head2Val: string;
  head2Name: string;
  name: string;
  requiredInfo: string;
  riskCode: string;
  shortInfo: string;
  showHome: boolean;
  state: string;
  tags: string;
}
interface IQstnItemListItem {
  id: number;
  inputFlag: any;
  itemName: string;
  orderBy: number;
  orderByCode: string;
  selected: boolean;
}
export interface IQstnItem {
  orderBy: number;
  qstnCategateId: string;
  qstnCode: null;
  qstnVersion: string;
  question: string;
  itemList: IQstnItemListItem[];
  checked?: string;
}
interface ldQstn {
  qstnList: IQstnItem[];
  riskLevel: any;
  reviewFlag: boolean | string | any;
  answersList: any[];
}
export interface IAnswersListItem {
  answers: string;
  orderby: number;
  type: string;
}
export interface IAdvicerPageData {
  banners: IbannerItem[];
  docList: IDocItem[];
  heads: IheadItem[];
  riskMoList: IRiskItem[];
  serviceList: IDocItem[];
  ldQstn: ldQstn;
  login: boolean;
}
interface Item {
  id: number;
  inputFlag: null | string | number;
  itemName: string;
  orderBy: number;
  orderByCode: string;
  selected: boolean;
}

export interface ArrItem {
  orderBy: number;
  qstnCategateId: string;
  qstnCode: string | null;
  qstnVersion: string | null;
  question: string;
  questionInfo: string | null;
  itemList: Item[];
  checked?: string;
}
export interface IRisk {
  collect: boolean;
  expireDate: string;
  investorType: string;
  lowRiskLevel: boolean;
  riskLevel: string;
  riskLevelName: string;
  riskReviewFlag: string;
}
