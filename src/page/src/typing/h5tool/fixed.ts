interface IMyFixedSummary {
  count: number;//定投数量	
  amount: number;//定投金额	
  amountFormat: string;
  failCount: number;
}
export interface IMyFixedItem {
  protocolNo: string;//定投协议编号	
  tradeAccount: string;//交易账号	
  bankFormat: string;//银行账号，例：工商银行（6189）	
  fundCode: string;//基金代码	
  fundName: string;//基金名称	
  amount: number;//定投金额	
  amountFormat: string;//
  sumAmount: number;//已成功定投累计金额	
  sumAmountFormat: number;//
  count: number;//定投期数	
  state: string;//定投状态，生效、暂停、终止	
  stateFormat: string;//
  nextTradeDate: string;//下期定投时间
  nextTradeDateFormat: string;
  periodType: string;//定投周期	
  periodTypeFormat: string;//
  periodValue: string;//定投值	
  periodValueFormat: string;//
  endDate: string;//中止时间
  endDateFormat: string;//
  moidfyState: boolean;//是否可以修改状态
}
// 我的定投页面数据
export interface IMyFixedData {
  summary: IMyFixedSummary,
  list: IMyFixedItem[]
}

export interface IFixedInvest {
  protocolNo: string;//定投协议编号	
  tradeAccount: string;//交易账号	
  bankFormat: string;//银行账号，例：工商银行（6189）	
  fundCode: string;//基金代码	
  fundName: string;//基金名称	
  amount: number;//定投金额	
  amountFormat: string;//
  sumAmount: number;//已成功定投累计金额	
  sumAmountFormat: number;//
  count: number;//定投期数	
  state: string;//定投状态，生效、暂停、终止	
  stateFormat: string;//
  nextTradeDate: string;//下期定投时间
  nextTradeDateFormat: string;//
  periodType: string;//定投周期	
  periodTypeFormat: string;//
  periodValue: string;//定投值	
  periodValueFormat: string;//
  endDate: string;//中止时间
  endDateFormat: string;//
  moidfyState: boolean;//是否可以修改状态
}
export interface IfiexedTradeItem {
  treasure: boolean;
  protocolNo: string;//定投协议编号	
  tradeAccount: string;//交易账号	
  bankFormat: string;//银行账号，例：工商银行（6189）	
  fundCode: string;//基金代码	
  fundName: string;//基金名称	
  amount: number;//定投金额	
  amountFormat: string;//
  sumAmount: number;//已成功定投累计金额
  sumAmountFormat: number;//
  count: number;//定投期数
  state: string;//定投状态，生效、暂停、终止	
  stateFormat: string;//
  nextTradeDate: string;//下期定投时间
  nextTradeDateFormat: string;//
  periodType: string;//定投周期	
  periodTypeFormat: string;//
  periodValue: string;//定投值
  periodValueFormat: string;//
  endDate: string;//中止时间
  endDateFormat: string;//
  moidfyState: boolean;//是否可以修改状态
  applyDate: string;//申请时间
  applyDateFormat: string;//
  failReason: string;//失败原因
  orderNo: string;
  businessType: string;
}

// 定投详情数据
export interface IFixedDetail {
  fixedInvest: IFixedInvest;
  tradeList: IfiexedTradeItem[]
}
// 定投结果
export interface IFixedResult {
  protocolNo: string;//定投协议编号	
  tradeAccount: string;//交易账号	
  bankFormat: string;//银行账号，例：工商银行（6189）	
  fundCode: string;//基金代码	
  fundName: string;//基金名称	
  purchaseSoon: boolean; //立即下单	
  amount: number; //定投金额	
  amountFormat: string;//state:string;//定投状态，生效、暂停、终止	
  state: string;
  stateFormat: string;//nextTradeDate:string;//下期定投时间	
  nextTradeDateFormat: string;//periodType:string;//定投周期	
  periodTypeFormat: string;//periodValue:string;//定投值	
  periodValueFormat: string;//fixedContent:string;//定投提示
}