/*
 * @Author: your name
 * @Date: 2023-12-28 14:20:42
 * @LastEditTime: 2024-01-18 13:35:34
 * @LastEditors: 韦玮莹
 * @Description: In User Settings Edit
 * @FilePath: \dmmp_page\src\api\h5\fundDetail\BaseInfo.ts
 */
export interface IAnnouncement {
  fundCode: string; //基金代码
  annDate: string; //公告日
  annType: string; //公告类型
  title: string; //标题
  url: string; //链接
  annDateFormat: string; //时间格式化
}
export interface IFundBase {
  fundCode: string; //基金代码
  fullName: string; //基金全称
  fundName: string; //基金简称
  fundType: string; //基金类型
  fundTypeFormat: string; //基金类型格式化
  secCatType: string; //基金二级分类
  secCatTypeFormat: string; //基金二级分类格式化
  estabDate: string; //成立日期
  estabDateFormat: string; //成立日期格式化
  astDate: string; //基金规模日期
  astDateFormat: string; //基金规模日期格式化
  fundAst: number; //基金规模（按代码）
  fundAstFormat: string; //基金规模格式化（按代码）
  fundSh: number; //基金份额（按代码）
  fundShFormat: string; //基金份额格式化（按代码）
  mgmtCoCode: string; //基金管理人代码；关联管理人表
  mgmtCoName: string; //管理人简称
  custName: string; //基金托管人
  invObj: string; //投资目标
  invStrgy: string; //投资策略
  invScope: string; //投资范围
  perfBmk: string; //投资策略
}
interface IHodeBase<T> {
  fundCode: string;
  summary: {
    annDate: string; //基金公告日期
    annType: string; //基金公告类型，0 - 季度 1 - 半年 2 - 年度
    annTypeFormat: string; //基金公告类型格式化
  };
  itemList: Array<T>;
}

interface IPreviousSummary {
  //上一次基金债券配置概览
  annDate: string; //基金公告日期
  annType: string; //基金公告类型，0 - 季度 1 - 半年 2 - 年度
  annTypeFormat: string; //基金公告类型格式化
}
interface IInds {
  indsType: string; //行业类型编码（申万三级分类）
  indsName: string; //行业类型名称
  stockIndsAstProp: number; //百分比（按"净资产占比"计算）
  stockIndsAstPropFormat: string; //
  stockIndsProp: number; //百分比（按"股票占比"计算）
  stockIndsPropFormat: string; //行业分布模块
}
export interface IFundCompAstResponse {
  compAstType: string; //资产类型编码
  compAstName: string; //资产类型名称
  compAstProp: number; //百分比
  compAstPropFormat: string; //
}
export interface IHoleResult {
  fundCompAstResponse: IHodeBase<IFundCompAstResponse>; //资产配置比例
  fundIndsResponse: IHodeBase<IInds>;
  fundStockResponse: IHodeBase<{
    //重仓股票
    stockCode: string; //股票编码
    stockName: string; //股票名称
    tenStockAstProp: number; //百分比（按"净资产占比"计算）
    tenStockAstPropFormat: string; //
    tenStockProp: number; //百分比（按"股票占比"计算）
    tenStockPropFormat: string; //比较结果，0 - 上期包含该股票，1 - 新增{ @link IndexCompare }
    compareFormat: string; //
    tenStockAstPropCompareValue: number; //比较结果（按"净资产占比"计算），如果为新增，为null，否则为当期 - 上期
    tenStockAstPropCompareValueFormat: string; //
    tenStockPropCompareValue: number; //比较结果（按"股票占比"计算），如果为新增，为null，否则为当期 - 上期
    tenStockPropCompareValueFormat: string;
  }> &
  IPreviousSummary;
  fundBondResponse: IHodeBase<IFundBondResponse> & IPreviousSummary;
}
export interface IFundBondResponse {
  //重仓债券
  bondCode: string; //债券编码
  bondName: string; //债券名称
  bondExg: string; //债券市场
  tenBondAstProp: number; //百分比（按"净资产占比"计算）
  tenBondAstPropFormat: string; //
  tenBondProp: number; //百分比（按"股票占比"计算）
  tenBondPropFormat: string; //比较结果，0 - 上期包含该债券，1 - 新增{ @link IndexCompare }
  compareFormat: string; //
  tenBondAstPropCompareValue: number; //比较结果（按"净资产占比"计算），如果为新增，为null，否则为当期 - 上期
  tenBondAstPropCompareValueFormat: string; //
  tenBondPropCompareValue: number; //比较结果（按"股票占比"计算），如果为新增，为null，否则为当期 - 上期
  ttenBondPropCompareValueFormat: string; //
}

export enum EAnnType {
  IssuanceOperation = "1", //"发行运作"
  dividendDistribution = "2", // "分红送配"
  periodicalReports = "3", // "定期报告"
  personnelAdjustment = "4", // "人事调整"
  fundSeal = "5", // "基金销售"
  other = "-1", //"其他公告"
}
interface IFundDivResponse {
  fundCode: string; //基金代码
  portReportDate: string; //公告日期
  portReportDateFormat: string; //
  divRegDate: string; //权益登记日
  divRegDateFormat: string; //
  divPayDate: string; //发放日
  divPayDateFormat: string; //
  divExDate: string; //除息日
  divExDateFormat: string; //
  divPerShare: number; //每份分红
  divPerShareFormat: string; //每份分红格式化
}
interface IFundDivSplitResponse {
  fundCode: string; //基金代码
  splitDate: string; //拆分折算日
  splitType: string; //拆分折算类型
  splitRatio: number; // 拆分折算，1: n
  splitRatioFormat: string; //拆分折算格式化
}
export interface IFundDividend {
  fundDivResponseList: Array<IFundDivResponse>;
  fundDivSplitResponseList: Array<IFundDivSplitResponse>;
}
