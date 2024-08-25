export interface IAssetSummary {
  holdAmount: number; //持仓（账户余额）
  holdAmountFormat: string;
  dayProfit: number; //日收益
  dayProfitFormat: string;
  totalProfit: number; //总收益
  totalProfitFormat: string;
  annualYldSeven: number; //七日年化
  annualYldSevenFormat: string;
  usableAmount: number; //可用余额
  usableAmountFormat: string;
  availableWithdrawAmount: number; //可取余额
  availableWithdrawAmountFormat: string;
  confirmAmount: number; //已确认份额
  confirmAmountFormat: string;
  frozenAmountFormat: string;
  frozenAmount: number;
  confirmShare: string;
  currentIncomeFormat: string;
  incomeDateFormat: string;
  /**
   * 当前收益
   */
  currentIncome: number;

  /**
   * 收益日期
   */
  incomeDate: string;
}
interface IHold {
  icon: string; //银行icon
  tradeAccount: string; //交易账号
  bankAccount: string; //银行账号
  bankName: string; //
  bankNameFormat: string; //
  holdAmount: number; //持仓（账户余额）
  holdAmountFormat: string; //
  usableAmount: number; //可用余额
  usableAmountFormat: string; //
  availableWithdrawAmount: number; //可取余额
  a: string;
  availableWithdrawAmountFormat: string;
}
export interface ILhbMain {
  assetSummary: IAssetSummary; //资产汇总
  fundCode: string; //利活宝基金列表
  accountId: string; //会员号
  holdList: IHold[]; //持仓列表
  redeem: boolean;
  fixed: boolean;
}
