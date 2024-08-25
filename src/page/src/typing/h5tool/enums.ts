/*
 * @Author: your name
 * @Date: 2023-11-20 15:31:28
 * @LastEditTime: 2024-06-17 17:16:26
 * @LastEditors: 韦玮莹
 * @Description: In User Settings Edit
 * @FilePath: \dmmp_page\src\typing\h5tool\enums.ts
 */
// 基金类型枚举
export enum FuncdType {
  All = "-1", //全部
  Stocks = "11", //股票
  Bonds = "13", //债券
  Blending = "12", //混合
  Currency = "14", //货币
  Exponential = "A1", //指数(etf)
  QDII = "15", //QDII
  Other = "99", //其他
}
// 0 顺序,1 倒序
export enum SortEnum {
  Asc = 0,
  Desc,
}

export enum FundSortField {
  FUND_CODE = "0", //基金代码
  ANNUAL_YLD_SEVEN = "1", //货币基金七日年化
  WK_RET = "2", //非货近1周涨跌幅
  MTH_RET = "3", //货币基金近1月年化，非货近1月涨跌幅
  THREE_MTH_RET = "4", //货币基金近3月年化
  SIX_MTH_RET = "5", //货币基金近6月年化, 非货近6月涨跌幅
  ONE_YR_RET = "6", //货币基金近1年年化, 非货近1年涨跌幅
  TWO_YR_RET = "7", //货币基金近2年年化
  THREE_YR_RET = "8", //非货近3年涨跌幅
  YTD_RET = "9", //货币基金今年以来年化, 非货今年以来涨跌幅
  INCPT_RET = "10", //成立至今涨跌幅
  FIVE_YR_RET = "11", //货币基金近五年涨跌幅
  DIV_PER_TT = "12", //货币基金万份收益
  D_RET = "13", //货币基金近一日涨跌幅
  NAV = "14", //最新净值
}

export enum Subscribe {
  notSubribe = 0,
  subscribing,
}

// 分红方式
export enum EDividendMethod {
  Cash = "1",
  Reinvest = "0",
}
export enum ESubscribe {
  notSubscribe = "1",
  subscribing = "2",
  endSubscribe = "3",
  error = "-1",
}
export enum EClose {
  unClose = "0",
  onClose = "1",
}

// 业务代码
export enum EnumBusinessCode {
  apply = "022", //申购
  subscribe = "020", //认购
  applyRedeem = "025", //预约赎回
  lhb = "050",
}

// 网点
export enum ENetNo {
  hz = "8010", //杭州市民卡
  lnt = "8511", //岭南通
  zy = "9998", //"自营"
}
export const ENetNoDescriptions: { [key in ENetNo]: string } = {
  [ENetNo.hz]: "杭州市民卡",
  [ENetNo.lnt]: "岭南通",
  [ENetNo.zy]: "自营",
};
// 定投状态 0生效、1暂停、2终止
export enum EFixedState {
  normal = "0",
  stop = "1",
  end = "2",
}

// 定投操作状态  01-暂停合约，02-恢复合约，03-终止合约
export enum EFixedBusinCode {
  stopFixed = "01",
  restartFixed = "02",
  endFixed = "03",
}

// 定投记录状态
export enum ERecoedState {
  TRADE_FAIL = "0", //交易失败
  TRADE_SUCCESS = "1", // "交易成功")
  PART_CONFIRM = "2", //部分确认"
  REVOKE = "4", // "撤销订单"
  SUBSCRIBE_ONCE_CONFIRM = "5", // "认购申请确认"
  CANCELLATION = "6", // "超时关闭")
  PAY_FAIL = "7", // "支付失败")
  PAY_CONFIRMING = "8", // "支付确认中
  SHARE_CONFIRMING = "9", // "份额确认中
  MATCH = "10", // "已打款待确认")
  DEFAULT = "-1", //未知
}
export enum EAssetType {
  LDB = "0", //利得宝
  LMF = "1", //基金
  ZYF = "2", //活期
  BAFI = "3", //券商资管
  LHB = "4", //利活宝
  GMF = "6", //利智投
  SYPZ = "7", //收益凭证
  BAFC = "8", //银行理财
  CHMT = "9", //020账户
  LMG = "10", //基金组合
  PMF = "11", //高端理财
  SEPE = "12", //阳光私募
  FDIE = "13", //高端固收
  PEEY = "14", //私募股权
  BKF = "15", //银行存款
  QSZG = "16", //券商资管
  DEFAULT = "-1", //未知

  // LEAD_TREASURE(code: "0"name: "利得宝"，assetType: "10"，productType: "LDB", orderType: "LDB")6 usages
  // FUND(code: "1", name: "基金"，assetType: "11", productType: "LMF", orderType: "LMF"),
  //   LIVE_STORAGE(code: "2", name: "活期"，assetType: "15", productType: "ZYF", orderType: "ZYF")
  // TYPE_17(code: "3", name: "券商资管"，orderType: "BAFI"), assetType: "17", productType: "BAFI", 7 usages
  // TREASURE(code: "4"name: "利活宝”，assetType: "21",productType:"LHB",orderType: "MFF")1 usage
  // INVEST(code: "6", name: "利智投”assetType: "11",productType:"GMF",orderType:"GMF")
  // PROFIT_PROVE(code: "7"name: "收益凭证"，assetType: "25", productType: "SYPZ", orderType: "SYPZ"),
  //   BANK(code: "8", name: "银行理财", assetType: "28", productType: "BAFC", orderType: "BAFC"),
  //   ACCOUNT_020(code: "9"name: "020账户"assetType: "020账户"productType: "CHMT", orderType: "CHMT")3 usages
  // PORTFOLI0(code: "10"，name: "基金组合"，assetType: "11", productType: "LMG", orderType: "LMG"),
  //   PRIVATE_FUND(code: "11", name: "高端理财", assetType: "19", productType: "PMF", orderType: "PMF"),
  //   SUN_PRIVATE(code: "12"， name: "阳光私募”,assetType:"19"，productType:"SEPE",orderType: "SEPE" )
  //   HIGH_MONEY(code: "13"，name: "高端固收”assetType: "19", productType: "FDIE", orderType: "FDIE"),
  //   PRIVATE_STOCK(code: "14", name: "私募股权", assetType: "19", productType: "PEEY", orderType: "PEEY")
  //   BANK_MONEY(code: "15", name: "银行存款”, assetType: "19",productType: "BKF", orderType: "BKF"),
  //   ASSET_MANAGER(code: "16", name: "券商资管", assetType: "19", productType: "QSZG", orderType: "QSzG")
  //   DEFAULT(code: "", name: "未知", assetType: " - 1", productType: ""orderType: "");
}
