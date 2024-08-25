import { IBanner } from './home';
export interface IYearBill {
  isHldp: string;
  badFndFndAbbr: string;
  badFndFndCd: string;
  badFndFndMngr: string;
  badFndFndName: string;
  badFndFndPayf: number;
  badFndFndPayfFormat: string;
  badFndFndRor: number;
  badFndFndRorFormat: string;
  bestFndFndAbbr: string;
  bestFndFndCd: string;
  bestFndFndMngr: string;
  bestFndFndName: string;
  bestFndFndPayf: number;
  bestFndFndPayfFormat: null;
  bestFndFndRor: number;
  bestFndFndRorFormat: string;
  bizYest: string;
  endDate: string;
  fstPurcDate: string;
  fstPurcFndAbbr: string;
  fstPurcFndCd: string;
  fstPurcFndPlAmt: number;
  fstPurcFndPlAmtFormat: string;
  lowDate: string;
  lowPayf: number;
  lowPayfFormat: null;
  lowRor: number;
  lowRorFormat: null;
  lucProAmt: number;
  lucProAmtFormat: null;
  lucTrdy: string;
  memName: string;
  memNo: string;
  memPayf: number;
  memPayfFormat: string;
  memRor: number;
  memRorFormat: null;
  oactDate: string;
  oactDateGBK: null;
  oactTime: string;
  oactYears: number;
  outDate: string;
  peakDate: string;
  peakPayf: number;
  peakPayfFormat: null;
  peakRor: number;
  peakRorFormat: null;
  purcFndVol: number;
  rankVicRate: number;
  rankVicRateDesc: string;
  rankVicRateFormat: string;
  startDate: string;
  terrDefcAmt: number;
  terrDefcAmtFormat: string;
  terrTrdy: string;
}

export interface IYear7 {
  badFndFndAbbr: string;
  badFndFndCd: string;
  badFndFndMngr: string;
  badFndFndName: string;
  badFndFndPayf: number;
  badFndFndPayfFormat: string;
  badFndFndRor: number;
  badFndFndRorFormat: string;
  bestFndFndAbbr: string;
  bestFndFndCd: string;
  bestFndFndMngr: string;
  bestFndFndName: string;
  bestFndFndPayf: number;
  bestFndFndPayfFormat: string;
  bestFndFndRor: number;
  bestFndFndRorFormat: string;
  bizYest: string;
  endDate: string;
  fstPurcDate: string;
  fstPurcFndAbbr: string;
  fstPurcFndCd: string;
  fstPurcFndPlAmt: number;
  fstPurcFndPlAmtFormat: string;
  lowDate: string;
  lowPayf: number;
  lowPayfFormat: string;
  lowRor: number;
  lowRorFormat: string;
  lucProAmt: number;
  lucProAmtFormat: string;
  lucTrdy: string;
  memName: string;
  memNo: string;
  memPayf: number;
  memPayfFormat: string;
  memRor: number;
  memRorFormat: string;
  oactDate: string;
  oactDateGBK: string;
  oactTime: string;
  oactYears: number;
  outDate: string;
  peakDate: string;
  peakPayf: number;
  peakPayfFormat: string;
  peakRor: number;
  peakRorFormat: string;
  purcFndVol: number;
  rankVicRate: number;
  rankVicRateDesc: string;
  rankVicRateFormat: string;
  startDate: string;
  terrDefcAmt: number;
  terrDefcAmtFormat: string;
  terrTrdy: string;
}
interface IMonthListItem {
  label: string;
  value: number;
  valueFormat?: string;
  date: string;
}
interface IAssetProfitIntegration {
  bestProduct: IBestProduct;
  list: IMonthListItem[];
  remark: string;
}
interface IBestProduct {
  label: string;
  productName: string;
  profit: number;
  profitFormat: string;
}
interface IAssetVariation {
  inflow: number;
  inflowCount: number;
  inflowFormat: string;
  list: IMonthListItem[];
  maxInflow: number;
  maxInflowDate: string;
  maxInflowFormat: string;
  monthEndWorthValue: number;
  monthEndWorthValueFormat: string;
  monthStartWorthValue: number;
  monthStartWorthValueFormat: string;
  netInflow: number;
  netInflowFormat: string;
  outflow: number;
  outflowCount: number;
  outflowFormat: string;
  cashBonusFormat: string;
}
interface IDateInfo {
  monthFormat: string;
  serverDate: string;
  yearFormat: string;
  month: string;
}
interface IIndustryAnalysis {
  highStockIndex: string;
  list: IMonthListItem[];
  remark: string;
}
interface IMarketAnalysis {
  analysisList: Array<{
    label: string;
    content: string;
  }>
}
interface IPperformanceTrend {
  date: string;
  hsRateValue: number;
  hsRateValueFormat: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueList: number[];
  szRateValue: number;
  szRateValueFormat: string;
}
interface IProductProfitIntegration {
  bestProduct: string;
  list: IMonthListItem[];
  remark: string;
}
interface IProfit {
  defeatPercent: number;
  defeatPercentFormat: string;
  rankPercent: number;
  rankPercentFormat: string;
  totalProfit: number;
  totalProfitFormat: string;
  totalProfitRate: number;
  totalProfitRateFormat: string;
}
export interface ICalendarItem {
  date: string;
  profit: number;
  profitFormat: string;
}
interface IProfitCalendar {
  list: ICalendarItem[];
  remark: string;
  content: string;
}
interface ISuggestFollowItem {
  describe: string;
  fundCode: string;
  fundName: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueLabel: string;
  riskLevel: string;
  riskLevelFormat: string;
  title: string;

}
interface ISuggestFollow {
  categoryList: IMonthListItem[];
  // list: ISuggestFollowItem[];
  // categoryCode: string;
  // categoryName: string;
  categoryMap: ICategoryMap
}
interface ICategoryMap {
  number: ISuggestFollowItem[]
}
interface IDefective {
  percent: number;
  percentFormat: string;
  productName: string;
  profitRate: number;
  profitRateFormat: string;
  rank: number;
}
interface ITradeAnalysis {
  buyCount: number;
  defectiveList: IDefective[];
  gainList: IDefective[];
  saleCount: number;
}
export interface IMounthData {
  assetProfitIntegration: IAssetProfitIntegration;
  assetVariation: IAssetVariation;
  dateInfo: IDateInfo;
  industryAnalysis: IIndustryAnalysis;
  marketAnalysis: IMarketAnalysis;
  performanceTrend: IPperformanceTrend;
  productProfitIntegration: IProductProfitIntegration;
  profit: IProfit;
  profitCalendar: IProfitCalendar;
  suggestFollow: ISuggestFollow;
  tradeAnalysis: ITradeAnalysis;
  bannerList: IBanner[]
}

interface IBanner {
  id: string;
  imgUrl: string;
  label: string;
  linkUrl: string;
  sceneCode: string;
}