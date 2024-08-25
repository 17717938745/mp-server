export interface ProductItem {
  dayRateValue: number;
  dayRateValueFormat: string;
  dayRose: null;
  dayRoseFormat: null;
  follow: false;
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeName: null;
  increasePeriod: null;
  increasePeriodFormat: null;
  increaseRate: null;
  increaseRateFormat: null;
  nav: number;
  navDay: null;
  navDayFormat: string;
  navFormat: string;
  productType: null;
  purchaseFlag: null;
  recentlyFiveYearRateValue: null;
  recentlyFiveYearRateValueFormat: string;
  recentlyFourYearRateValue: null;
  recentlyFourYearRateValueFormat: string;
  recentlyMonthRateValue: number;
  recentlyMonthRateValueFormat: string;
  recentlySixMonthRateValue: number;
  recentlySixMonthRateValueFormat: string;
  recentlySixYearRateValue: null;
  recentlySixYearRateValueFormat: string;
  recentlyThreeMonthRateValue: number;
  recentlyThreeMonthRateValueFormat: string;
  recentlyThreeYearRateValue: number;
  recentlyThreeYearRateValueFormat: string;
  recentlyTwoYearRateValue: number;
  recentlyTwoYearRateValueFormat: string;
  recentlyWeekRateValue: number;
  recentlyWeekRateValueFormat: string;
  recentlyYearRateValue: number;
  recentlyYearRateValueFormat: string;
  setUpRateValue: number;
  setUpRateValueFormat: string;
  tagList: null;
  thisYearRateValue: number;
  thisYearRateValueFormat: string;
}
export interface TopFundListItem {
  categoryCode: string;
  categoryName: string;
  endTime: string;
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeFormat: string;
  homePage: boolean;
  id: string;
  issued: boolean;
  rateValue: number;
  rateValueFormat: string;
  rateValueType: string;
  rateValueTypeFormat: string;
  recommendReason: string;
  riskLevel: string;
  riskLevelFormat: string;
  slogan: string;
  sorter: number;
  startTime: string;
  tagList: string[];
  tagNameList: string[];
}
export interface FundCategoryListItem {
  categoryCode: string;
  categoryName: string;
}
export interface IManager {
  fundCode: string; //基金代码
  mgrId: string; //基金经理ID
  current: boolean; //是否现任
  mgrTnr: string;
  mgrTnrStart: string; //开始日期
  mgrTnrEnd: string; //结束日期
  mgrFundRet: number; //管理基金回报率(任期)
  mgrFundRetFormat: string;
  mgrFundYrRet: number; //管理基金回报率(年化)
  catMgrFundRet: number; //同类管理基金回报率(任期)
  catMgrFundYrRet: number; //同类管理基金回报率(年化)
  mgmtCoCode: string; //管理人代码
  mgrName: string; //基金经理名称
  mgrProf: string; //基金经理简介
  tnrExp: number; //从业时间
  mgrMgmtYears: number; //管理总年限
  mgrMgmtAst: number; //管理总规模
  allocPref: string; //基金资产偏好 （配置占比最高的资产）
  indsPref: string; //基金行业偏好 （配置占比最高的行业）
  mktPref: string; //基金市值偏好 （配置占比最高的风格类型）
  mktValPref: string; //基金估值偏好 （配置占比最高的估值类型）
}
interface INavMode {
  code: number;
  message: string;
  list: Array<{
    fundCode: string; //基金代码
    navDate: string; //净值日期
    adjNav: number; //复权净值
    adjNavFormat: string; //
    nav: number; //单位净值
    navFormat: string; //
    accNav: number; //累计净值
    accNavFormat: string; //
    annualYldSeven: number; //七日年化
    annualYldSevenFormat: string; //
    divPerTt: number; //万份收益
    divPerTtFormat: string; //
    navFlag: string; //净值标志
  }>;
}
interface IindicsEntityDetails {
  fundCode: string; //基金代码
  navDate: string; //净值日期
  dRet: number; //日涨幅
  wkRet: number; //近一周涨跌幅
  mthRet: number; //近一月涨跌幅
  mthRetYear: number; //涨跌幅年化
  threeMthRet: number; //近三月涨跌幅
  sixMthRet: number; //近六月涨跌幅
  oneYrRet: number; //近一年涨跌幅
  twoYrRet: number; //近二年涨跌幅
  threeYrRet: number; //近三年涨跌幅
  fiveYrRet: number; //近五年涨跌幅
  incptRet: number; //成立以来涨跌幅
  ytdRet: number; //今年以来涨跌幅
  twoAvgAnnualRet: number; //近两年收益率（年化）
  threeAvgAnnualRet: number; //近三年收益率（年化）
  fiveAvgAnnualRet: number; //近五年收益率（年化）
  incptAvgAnnualRet: number; //成立以来收益率（年化）
  wkFundRank: string; //近一周同类排名
  wkFundRankRet: number; //近一周同类排名 - 转换结果
  mthFundRank: string; //近一月同类排名
  mthFundRankRet: string; //近一月同类排名 - 转换结果
  threeMthFundRank: string; //近三月同类排名
  threeMthFundRankRet: number; //近三月同类排名 - 转换结果
  sixMthFundRank: string; //近六月同类排名
  sixMthFundRankRet: number; //近六月同类排名 - 转换结果
  oneYrFundRank: string; //近一年同类排名
  oneYrFundRankRet: number; //近一年同类排名 - 转换结果
  twoYrFundRank: string; //近二年同类排名
  twoYrFundRankRet: number; //近二年同类排名 - 转换结果
  threeYrFundRank: string; //近三年同类排名
  threeYrFundRankRet: number; //近三年同类排名 - 转换结果
  fiveYrFundRank: string; //近五年同类排名
  fiveYrFundRankRet: number; //近五年同类排名 - 转换结果
  incptFundRank: string; //成立以来同类排名
  incptFundRankRet: number; //成立以来同类排名 - 转换结果
  ytdFundRank: string; //今年以来同类排名
  ytdFundRankRet: number; //今年以来同类排名 - 转换结果
  mthMdd: number; //近一月最大回撤
  threeMthMdd: number; //近三月最大回撤
  sixMthMdd: number; //近六月最大回撤
  oneYrMdd: number; //近一年最大回撤
  oneYrMddFormat: string; //近一年最大回撤格式化
  twoYrMdd: number; //近二年最大回撤
  threeYrMdd: number; //近三年最大回撤
  fiveYrMdd: number; //近五年最大回撤
  incptMdd: number; //成立以来最大回撤
  ytdMdd: number; //今年以来最大回撤
  oneYrVol: number; //近一年波动率
  twoYrVol: number; //近二年波动率
  threeYrVol: number; //近三年波动率
  wkRetFormat: string; //周涨幅
  mthRetFormat: string; //月涨幅
  mthRetYearFormat: string; //货币为年化，* 12
  threeMthRetFormat: string; //近3月涨幅，货币为年化，* 4
  sixMthRetFormat: string; //近6月涨幅，货币为年化，* 2
  oneYrRetFormat: string; //近1年涨幅
  twoYrRetFormat: string; //近2年涨幅，货币为年化，/2
  threeYrRetFormat: string; //近3年涨幅
  fiveYrRetFormat: string; //近5年涨幅
  incptRetFormat: string; //成立以来涨幅
  ytdRetFormat: string; //今年以来涨幅 / 收益率
}
interface ITrand {
  annualYldSeven: number; //七日年化(只对货币基金)
  annualYldSevenFormat: string; //change:string;//涨跌幅(只对非货)
  csi300Change: string; //沪深300涨跌幅
  date: string; //日期
}
interface IStageIncrease {
  wkRet: number; //近一周涨跌幅
  mthRet: number; //近一月涨跌幅
  threeMthRet: number; //近三月涨跌幅
  sixMthRet: number; //近六月涨跌幅
  oneYrRet: number; //近一年涨跌幅
  twoYrRet: number; //近二年涨跌幅
  threeYrRet: number; //近三年涨跌幅
  fiveYrRet: number; //近五年涨跌幅
  incptRet: number; //成立以来涨跌幅
  ytdRet: number; //今年以来涨跌幅
  twoAvgAnnualRet: number; //近两年收益率（年化）
  threeAvgAnnualRet: number; //近三年收益率（年化）
  fiveAvgAnnualRet: number; //近五年收益率（年化）
  incptAvgAnnualRet: number; //成立以来收益率（年化）
  wkFundRank: string; //近一周同类排名
  mthFundRank: string; //近一月同类排名
  threeMthFundRank: string; //近三月同类排名
  sixMthFundRank: string; //近六月同类排名
  oneYrFundRank: string; //近一年同类排名
  twoYrFundRank: string; //近二年同类排名
  threeYrFundRank: string; //近三年同类排名
  fiveYrFundRank: string; //近五年同类排名
  incptFundRank: string; //成立以来同类排名
  ytdFundRank: string; //今年以来同类排名
  ytdFundRankRet: number; //今年以来同类排名 - 转换结果
  mthMdd: number; //近一月最大回撤
  threeMthMdd: number; //近三月最大回撤
  sixMthMdd: number; //近六月最大回撤
  oneYrMdd: number; //近一年最大回撤
  twoYrMdd: number; //近二年最大回撤
  threeYrMdd: number; //近三年最大回撤
  fiveYrMdd: number; //近五年最大回撤
  incptMdd: number; //成立以来最大回撤
  ytdMdd: number; //今年以来最大回撤
  oneYrVol: number; //近一年波动率
  twoYrVol: number; //近二年波动率
  threeYrVol: number; //近三年波动率
  mthSr: number; //近一月夏普比率
  threeMthSr: number; //近三月夏普比率
  sixMthSr: number; //近六月夏普比率
  oneYrSr: number; //近一年夏普比率
  twoYrSr: number; //近二年夏普比率
  threeYrSr: number; //近三年夏普比率
  fiveYrSr: number; //近五年夏普比率
  incptSr: number; //成立以来夏普比率
  ytdSr: number; //今年以来夏普比率
  mthCatRet: number; //近一月同类平均涨跌幅
  threeMthCatRet: number; //近三月同类平均涨跌幅
  sixMthCatRet: number; //近六月同类平均涨跌幅
  oneYrCatRet: number; //近一年同类平均涨跌幅
  twoYrCatRet: number; //近二年同类平均涨跌幅
  threeYrCatRet: number; //近三年同类平均涨跌幅
  fiveYrCatRet: number; //近五年同类平均涨跌幅
  incptCatRet: number; //成立以来同类平均涨跌幅
  ytdCatRet: number; //今年以来同类平均涨跌幅
}
export interface IFund {
  fundCode: string; //基金代码
  fundType: string; //基金类型
  riskLevel: string; //风险等级; R1 - R5
  fullName: string; //基金全称
  navDate: string; //净值日期
  nav: number; //净值
  fundName: string; //基金简称
  prodType: string; //产品类型; 0 - 公募基金 1 - 私募基金 2 - 券商资管
  annualYldSeven: number; //七日年化
  divPerTt: number; //万份收益
  fundTypeName: string; //基金类型名称
  ipoStartDate: string; //募集发行日期
  ipoEndDate: string; //募集结束日期
  clsdpStartDate: string; //基金封闭期开始日
  clsdpEndDate: string; //基金封闭期结束日
  mgmtCoCode: string; //基金管理人代码；关联管理人表
  mgmtCoName: string; //基金管理人名称；关联管理人表
  estabDate: string; //成立日期
  fundAst: number; //基金规模（按代码）
  fundAstFormat: string; //基金规模（按代码）
  mgr: IManager[]; //现任和历史基金经理名称
  navModelList: INavMode[]; //基金历史七日年化和万份收益
  indicsEntityDetails: object[]; //年化收益率集合
  response: {
    trands: ITrand; //收益走势
    stageIncrease: IStageIncrease; //阶段涨幅
  };
  subscribe: string; //1 - 募集未开始 2 - 正在募集 3 - 募集结束 - 1 - 数据异常
  close: string; //0 - 不在封闭期 1 - 在封闭期
}
interface IFundIpo {
  fundCode: string; //基金代码
  fundName: string; //基金名称
  fundType: string; //基金类型
  fundTypeName: string; //基金类型名称
  riskLevel: string; //风险等级
  riskLevelFormat: string; //风险等级格式化
  ipoStartDate: string; //募集开始日期
  ipoStartDateFormat: string; //募集开始日期格式化
  ipoEndDate: string; //募集结束日期
  ipoEndDateFormat: string; //募集结束日期格式化
}
export interface IFee {
  netNo: string; //网点号
  netName: string; //网点名称
  oriFee: number; //原始价格
  oriFeeFormat: string; //原始价格格式化
  trustFee: number; //真实费率，即原始费率 * 折扣
  trustFeeFormat: string; //真实费率格式化
}
export interface ISubscriptionFund {
  fundIpo: IFundIpo; //ipo信息
  fundFee: IFee; //价格
  tradeRule: ITradeRule[];
}
interface IBase {
  fundCode: string; //基金代码
  mainFundCode: string; //主基金代码
  fundName: string; //基金简称
  fundType: string; //基金类型
  fundTypeName: string; //基金类型名称
  riskLevel: string; //风险等级; R1 - R5
  fundAst: number; //基金规模（按代码）
  astDate: string; //基金规模日期
  befPurcFeeRate: string; //前端申购费率
  estabDate: string; //成立日期
  nav: number; //单位净值
  navDate: string; //净值日期
  agent: boolean; //是否代销
  fundSts: string; //基金状态
  dret: number; //日涨幅
  oneYrRet: number; //近一年涨跌幅 近一年收益
  oneYrFundRank: string; //近一年同类排名
  mgmtCoCode: string; //管理人代码
  mgmtCoName: string; //管理人简称
  mgrName: string[]; //现任基金经理名称
  minValue: number; //起购金额
  dayMaxValue: number; //当日累计最大限额
  riskLevelFormat: string; //风险等级; R1 - R5
  fundAstFormat: string; //基金规模（按代码）
  estabDateFormat: string; //成立日期
  navFormat: string; //单位净值
  navDateFormat: string; //单位净值格式化
  dretFormat: string; //日涨幅
  oneYrRetFormat: string; //近一年涨跌幅 近一年收益
  annualYldSeven: number;
  annualYldSevenFormat: string;
  divPerTt: number;
  divPerTtFormat: string;
  secCatName: string;
  secCatType: string;
}
export interface IPperformance {
  annualYldSeven: number; //七日年化(只对货币基金)
  annualYldSevenFormat: string; //七日年化格式化(只对货币基金)
  ret: number; //涨跌幅(只对非货)
  retFormat: string; //涨跌幅(只对非货)
  benchMark: number; //比较基准
  benchMarkFormat: string; //比较基准
  date: string; //日期
  dateFormat: string; //日期格式化
  divPerTt: number; //万份收益 只对货币基金
  divPerTtFormat: string; //万份收益格式化 只对货币基金
}
export interface INavChartData {
  fundCode: string; //基金代码
  navDate: string; //净值日期
  nav: number; //单位净值
  navFormat: string; //单位净值格式化
  accNav: number; //累计净值
  accNavFormat: string; //累计净值格式化
  annualYldSeven: number; //七日年化
  annualYldSevenFormat: string; //七日年化格式化
  divPerTt: number; //万份收益
  divPerTtFormat: string; //万份收益格式化
  ret: number; //日涨跌幅
  retFormat: string; //日涨跌幅格式化
}
export interface INavHisList {
  fundCode: string; //基金代码
  navDate: string; //净值日期
  navDateFormat: string;
  nav: number; //单位净值
  navFormat: string;
  accNav: number; //累计净值
  accNavFormat: string;
  ret: number; //涨跌幅
  retFormat: string;
  annualYldSeven: number; //七日年化
  annualYldSevenFormat: string;
  divPerTt: number; //万份收益
  divPerTtFormat: string;
}
export interface INavChart {
  navChartDataList: Array<INavChartData>; //净值列表
  histList: string[]; //分红日期
  mergeList: string[]; //份额合并 日期
  splitList: string[]; //份额拆分 日期
  obversionList: string[]; //份额折算 日期
  closeToOpenList: string[]; //封转开份额换算 日期
}
export interface IStagelncrease {
  stage: string; //时间区间
  ret: number; //涨跌幅
  retFormat: string; //涨跌幅格式化
  catRet: number; //同类平均
  catRetFormat: string; //同类平均格式化
  fundRank: string; //同类排名
}
export interface INavHistory {
  code: number; //返回代码，0 - 正常，其他 - 错误
  message: string; //返回信息
  total: number; //总数
  list: INavHisList[]; // 列表
}
interface ICompAst {
  fundCode: string; //基金代码
  summary: {
    annDate: string; //基金公告日期
    annType: string; //基金公告类型，0 - 季度 1 - 半年 2 - 年度
    annTypeFormat: string; //基金公告类型格式化
  }; //概览

  itemList: Array<{
    compAstType: string; //资产类型编码
    compAstName: string; //资产类型名称
    compAstProp: number; //百分比
    compAstPropFormat: string; //  >; //列表
  }>;
}
export interface IAnalysisIndics {
  type: string; //种类，哪个的率
  rate: number; //率
  rateFormat: string; //率的格式化
  cat: number; //同类平均的值
  catFormat: string; //同类平均的值格式化
  rank: string; //同类排名x位
  rankNumber: number; //同类排名x / y转化成的小数
  better: number; //优于 1 - (位次 / 同类总基金数)
  betterFormat: string; //优于格式化，不保留小数
}
export interface IFundMgmt {
  mgmtCoCode: string; //管理人代码
  mgmtCoFullName: string; //管理人全称
  mgmtCoName: string; //管理人简称
  coEstabDate: string; //公司成立日期
  numCoMgmt: number; //管理基金只数
  numCoMgmtFormat: string; //管理基金只数格式化
  coMgmtAst: number; //公司规模
  coMgmtAstFormat: string; //公司规模格式化
  coAstRank: number; //公司规模排名（名次）
  coAstRankFormat: string; //公司规模排名（名次）格式化
  numCoMgr: number; //基金经理人数
  numCoMgrFormat: string; //基金经理人数格式化
  coProf: string; //公司简介
}
interface IArchive {
  fundMgmtBase: IFundMgmt[]; //基金管理人
  mainFundCode: string; //关联基金代码
  mainFundName: string; //关联基金名称
}
interface IMgr {
  fundCode: string; //基金代码
  mgrId: string; //基金经理ID
  current: boolean; //是否现任
  mgrTnrStart: string; //开始日期
  mgrTnrEnd: string; //结束日期
  mgrFundRet: number; //管理基金回报率(任期)
  mgrFundYrRet: number; //管理基金回报率(年化)
  catMgrFundRet: number; //同类管理基金回报率(任期)
  catMgrFundYrRet: number; //同类管理基金回报率(年化)
  mgmtCoCode: string; //管理人代码
  mgrName: string; //基金经理名称
  mgrProf: string; //基金经理简介
  tnrExp: number; //从业时间
  mgrMgmtYears: number; //管理总年限
  mgrMgmtAst: number; //管理总规模
  allocPref: string; //基金资产偏好 （配置占比最高的资产）
  indsPref: string; //基金行业偏好 （配置占比最高的行业）
  mktPref: string; //基金市值偏好 （配置占比最高的风格类型）
  mktValPref: string; //基金估值偏好 （配置占比最高的估值类型）
}
export interface ITradeRule {
  confDays: IConfDays; //确认时间
  dayMaxPurc: number; //当日累计购买最大金额
  dayMaxPurcFormat: string; //当日累计购买最大金额格式化
  investmentAmountFormat: string;
}
interface IMinTrustFee {
  netNo: string; //网点号
  netName: string; //网点名称
  useValue: boolean; //0 使用折扣率 1 使用固定值
  oriFee: number; //原始价格
  oriFeeFormat: string; //原始价格格式化
  trustFee: number; //真实费率，即原始费率 * 折扣
  trustFeeFormat: string; //真实费率格式化
  minValue: number; //范围小值
  maxValue: number; //范围大值
  timeUnit: string; //持有基金单位
}
interface IUnder {
  fundSts: string; //基金状态
  minTrustFee: IMinTrustFee; //购买费率
  grey: boolean; //是否置灰 0：否 1：是
}
export interface IApplicationFund {
  base: IBase; //基础信息
  performance: IPperformance[]; //业绩走势
  navChart: INavChart; //单位净值
  stageIncrease: IStageIncrease[]; //阶段业绩
  navHistory: INavHistory; //历史净值
  analysisIndics: IAnalysisIndics[]; //分析指标
  compAst: ICompAst; //资产配置
  archive: IArchive; //基金档案
  mgr: IMgr[]; //基金经理
  tradeRule: ITradeRule; //交易规则
  under: IUnder; //底部交易模块
  mgrNameList: string[]
}
export interface IFundDetail {
  businessCode: string; // 业务代码 020认购 022申购
  businessName: string; // 020认购 022申购
  subscriptionFund?: ISubscriptionFund; //认购
  applicationFund?: IApplicationFund; //申购
  grey: boolean;
}
interface IOpFee {
  floatFee: string; //浮动管理费
  floatFeeType: string; //浮动管理费类型 0-数字 1-文本 如果是1，则代表浮动管理费 如果是0，则代表固定管理费
  floatFeeTypeFormat: string;
  mgmtFeeRate: string; // 管理费用
  custFeeRate: number; //托管费率
  distFeeRate: string; //销售服务费
  mgmtFeeRateFormat: string; //管理费用格式化
  custFeeRateFormat: string; //托管费率格式化
  distFeeRateFormat: string; //销售服务费格式化
  feeType: string; //收费类型 A前端 B后端
  feeTypeFormat: string; //收费类型（前端、后端）
  objid: string;
  customRate: number;
  customRateFormat: string;
}
interface IFeeText {
  text: string; //网点号
  oriFeeFormat: string; //原始价格格式化
  trustFeeFormat: string; //真实费率格式化
}
export interface IConfDays {
  businessCode: string; //业务代码
  businessName: string; //业务代码名称
  buyDay: string; //购买日
  confDay: string; //确认日
  incomeDay: string; //收益日
}
export interface IFundTrandRule {
  opFee: IOpFee; //运作费率
  feeText: IFeeText[]; //费率文本
  confDays: IConfDays; //确认天数
}
