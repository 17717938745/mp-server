export interface IHotNews {
  id: string;
  imgUrl: string;
  label: string;
  linkUrl: string;
  sceneCode: string;
}
export interface IMarketIndex {

  code: string;
  dateFormat: string;
  dateTime: string;
  index: number;
  indexFormat: string;
  label: string;
  profit: number;
  profitFormat: number;
  profitRate: number;
  profitRateFormat: string;
  timeFormat: string;
}
export interface IMoney {
  fundCode: string;
  fundName: string;
  label: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueType: string;
  rateValueTypeFormat: string;
  recommendRemark: string;
  tradeLimitRemark: string;
}
export interface IAssetSummary {
  dayProfit: number;
  dayProfitFormat: string;
  marketValue: number;
  marketValueFormat: string;
}

export interface IIcon {
  id: string;
  imgUrl: string;
  label: string;
  linkUrl: string;
  sceneCode: string;
}
export interface IHotNews {
  categoryCode: string;
  categoryLabel: string;
  createdTime: string;
  createdTimeFormat: string;
  imgUrl: string;
  label: string;
  linkUrl: string;
  newsId: string;
  sceneCode: string;
}
export interface IBanner {
  id: string;
  imgUrl: string;
  label: string;
  linkUrl: string;
  sceneCode: string;
}
export interface INewsCategory {
  newsCategoryCode: string;
  newsCategoryLabel: string;
}
export interface IHomePageData {
  tradeTime: boolean;
  assetSummary: null | number;
  bannerList: IBanner[];
  defaultNewsCategory: null | string;
  defaultTopFundCategory: string;
  followedFundCodeList: [];
  iconList: [];
  marketIndexList: IMarketIndex[];
  hotNewsList: IHotNews[];
  moneyFundList: IMoney[];
  newsCategoryList: INewsCategory[];
  newsList: IHotNews[];
  topFundCategoryList: [];
  topFundList: [];
  privateFundList: [];
}