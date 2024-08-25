export interface PortfolioItem {
  dayRateValue: number;
  dayRateValueFormat: string;
  nav: number;
  navDate: string;
  navDateFormat: string;
  navFormat: string;
  portfolioCode: string;
  portfolioDescribe: string;
  portfolioName: string;
  percent?: number | string;
}
export interface labelItem {
  label: string;
  url: string;
}
export interface protocolTtem extends labelItem {
  name: string;
}
interface IProductShare {
  marketValue: number;
  marketValueFormat: string;
  maxRedeemPercent: number;
  maxRedeemPercentFormat: string;
  minRedeemAmount: number;
  minRedeemAmountFormat: string;
  minRedeemPercent: number;
  minRedeemPercentFormat: string;
  netNo: string;
  tradeAccount: string;
  usableRate: number;
  usableRateFormat: string;
}
export interface BankCardItem {
  bankAccountFormat: string;
  bankCardId: string;
  bankName: string;
  dayMaxAmount: number;
  dayMaxAmountFormat: string;
  icon: string;
  lastUsed: boolean;
  perMaxAmount: number;
  perMaxAmountFormat: string;
  share: any;
  tradeAccount: string;
  treasure: boolean;
  valid: boolean;
  productShare: IProductShare;
  showAllFlag: boolean;
  tradeDateFormat: string;
}
export interface Risk {
  accountRiskFormat: string;
  accountRiskLevel: string;
  fundRisk: string;
  fundRiskFormat: string;
  riskTest: boolean;
  riskWarning: boolean;
}
export interface TradeLimit {
  addAmount: number;
  maxAmount: number;
  maxAmountFormat: string;
  minAmount: number;
  minAmountFormat: string;
}
export interface Form {
  bankCardList: BankCardItem[];
  confirmDateFormat: string;
  portfolioCode: string;
  portfolioDescribe: string;
  portfolioName: string;
  protocol: labelItem[];
  protocolList: protocolTtem[];
  risk: Risk;
  tradeLimit: TradeLimit;
  redeemForPurchaseProtocol: labelItem;
  icbcProtocolList: labelItem[];
  portfolioInfo: any;
}
export interface PortfolioPageItem {
  portfolioCode: string;
  portfolioName: string;
  worthValue: number;
  worthValueFormat: string;
  dayProfit: number;
  dayProfitFormat: string;
  dayProfitDateFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
}
export interface HoldData {
  dayProfit: number;
  dayProfitDate: string;
  dayProfitDateFormat: string;
  dayProfitFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
  totalProfit: number;
  totalProfitFormat: string;
  totalWorthValue: number;
  totalWorthValueFormat: string;
  undoneAmount: number;
  undoneAmountFormat: string;
}
export interface HoldListItem {
  dayProfit: number;
  dayProfitDate: string;
  dayProfitDateFormat: string;
  dayProfitFormat: string;
  holdProfit: number;
  holdProfitFormat: string;
  portfolioCode: string;
  portfolioName: string;
  worthValue: number;
  worthValueFormat: string;
  appointOrder: boolean;
  fundCode: string;
}
export interface RedeemBankItem {
  bankAccountFormat: string;
  bankCardId: string;
  bankName: string;
  icon: string;
  tradeAccount: string;
  portfolioShare: {
    marketValue: number;
    marketValueFormat: string;
    minRedeemAmount: number;
    minRedeemAmountFormat: string;
    minRedeemPercent: number;
    minRedeemPercentFormat: string;
    netNo: string;
    tradeAccount: string;
  };
}
export interface RedeemForm {
  bankCardList: RedeemBankItem[];
  confirmTip: string;
  portfolioCode: string;
  portfolioDescribe: string;
  portfolioName: string;
  risk: Risk;
}

// -------------------------------------------------------------------------------

interface IPortfolio {
  dayRateValue: number;
  dayRateValueFormat: string;
  nav: number;
  navDate: string;
  navDateFormat: string;
  navFormat: string;
  portfolioCode: string;
  portfolioDescribe: string;
  portfolioFundByCategoryList: [];
  portfolioFundByTypeList: [];
  portfolioFundList: IPortfolioFund[];
  portfolioName: [];
  portfolioRate: [];
  rateTypeList: [];
  rateValueType: string;
  totalRateValue: number;
  totalRateValueFormat: string;
}
interface IPortfolioFund {
  fundCategory: string;
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeFormat: string;
  newcomer: false;
  percent: number;
  percentFormat: string;
}
