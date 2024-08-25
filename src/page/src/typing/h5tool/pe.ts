export interface IFundInfo {
  businessType: string;
  endTime: string;
  endTimeFormat: string;
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeFormat: string;
  closedPeriod: string;
  investStrategy: string;
  lockPeriodFormat: string;
  label2: any[];
  label2Format: string;
  label2Text: string;
  minBuy: number | string;
  minBuyFormat: string;
  minAddAmount: string | number;
  minAddAmountFormat: string;
  nav: string;
  navFormat: string;
  open: false;
  performanceExtractStandard: string;
  performanceExtractStandardFormat: string;
  productType: string;
  productTypeFormat: string;
  rateDate: string;
  rateLabel: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueType: number;
  recentlyYearRateValue: number;
  recentlyYearRateValueFormat: string;
  rewardType: boolean;
  riskLevel: string;
  riskLevelName: string;
  soldOut: false;
  startTime: string;
  startTimeFormat: string;
  subscribe: boolean;
  subscribeEndTime: string;
  subscribeEndTimeFormat: string;
  immediatelyOpen: boolean;
  redeem: boolean;
  buy: boolean;
  add: boolean;
  productSecondType: string;
  buyLabel: string;
  subscribeOpen: boolean;
  openModel: string;
  performanceExtractStandardText: string;
  label4Type: string;
  label4Text: string;
  label: string;
  labe14: string;
  label5: string;
  customize: boolean;
  label4: string;
  label4Value: number;
  label4ValueFormat: string;
  showPopover: boolean;
  lockPeriod: number;
  label3Type: string;
}

export interface ISectionBlock {
  id: string;
  title: string;
  sectionList: ISection[];
}
interface ISection {
  content: string;
  id: string;
  label: string;
  type: number;
}
export interface ISummary {
  applyDate: string;
  applyDateFormat: string;
  businessType: string;
  cancel: boolean;
  confirmState: string;
  confirmStateFormat: string;
  describe: string;
  unit: string;
  valueFormat: string;
}

export interface FundItem {
  businessType?: string;
  endTime: string;
  endTimeFormat: string;
  fundCode: string;
  fundName: string;
  fundSaleType: string;
  fundType: string;
  fundTypeFormat: string;
  lockPeriod?: null;
  lockPeriodFormat?: string;
  minAddAmount: number;
  minAddAmountFormat: string;
  minBuy: number;
  minBuyFormat: string;
  open: boolean;
  remark: string | null;
  productType: string;
  productTypeFormat: string;
  rateDate: string;
  rateLabel: string;
  rateValue: string;
  rateValueFormat: string;
  rateValueType: number;
  riskLevel: string;
  riskLevelName: string;
  soldOut: boolean;
  startTime: string;
  startTimeFormat: string;
  countDownDate?: string;
  subscribe: boolean;
  subscribeEndTimeFormat: string;
  immediatelyOpen: boolean;
  label: string;
  label2: any;
  label4: string;
  label4Type: string;
  label5: string;
  customize: boolean;
}
export interface ITopFund {
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

export interface ITopFundCategory {
  categoryCode: string;
  categoryName: string;
  linkUrl: string;
}

export interface IPrivateFund {
  subscribeEndTimeFormat: string;
  closedPeriod: string | null;
  subscribe: boolean;
  businessType: string;
  customize: boolean;
  endTime: string;
  endTimeFormat: string;
  fundCode: string;
  fundName: string;
  fundType: string;
  fundTypeFormat: string;
  lockPeriod: string;
  lockPeriodFormat: string;
  minBuy: number;
  minBuyFormat: string;
  open: boolean;
  productType: string;
  productTypeFormat: string;
  rateDate: string;
  rateLabel: string;
  rateValue: number;
  rateValueFormat: string;
  riskLevel: string;
  riskLevelName: string;
  soldOut: boolean;
  startTime: string;
  startTimeFormat: string;
  label: string;
  label2: any;
  label4: string;
  label4Type: string;
  label5: string;
}
export interface IRateValue {
  dateFormat: string;
  rateValue: number;
  rateValueStandard: number;
}
export interface IProfitTrendData {
  standardRateValueLabel: string;
  standardRateValue: number;
  date: string;
  rateValueLabel: string;
  dateFormat: string;
  rateValue: number;
  rateValueFormat: string;
  rateValueList: IRateValue[];
  rateValueStandard: number;
  rateValueStandardFormat: string;
  standardRateValueFormat: string;
}

interface INavItem {
  dateFormat: string;
  nav: number;
  totalNav: number;
}

export interface INavTrand {
  standardRateValueFormat: string;
  rateValueLabel: string;
  rateValue: number;
  rateValueFormat: string;
  standardRateValueLabel: string;
  standardRateValue: number;
  date: string;
  dateFormat: string;
  nav: number;
  navFormat: string;
  navList: INavItem[];
  totalNav: number;
  totalNavFormat: string;
}
// 合格投资者表单返回状态
export interface SignatureForm {
  name: string;
  idNoFormat: string;
  electronicSignature: boolean;
  flag: boolean;
  content: {
    html: string;
    title: string;
    viewId: string;
  };
}
// 投资者认定表单
export interface AffirmForm {
  step: number;
  stepList: { value: string; label: string }[];
  affirmState?: number;
  affirmStepOfAccount: number;
  affirmNotice: boolean;
  affirmNoticeContent: {
    viewId: string;
    title: string;
    html: string;
  };
  experienceLinkList: any[];
  experienceTemplateLink: {
    label: string;
    value: string;
  };
  experienceQualified: boolean;
  experienceHtml: string;
  assetLinkList: any[];
  assetHtml: string;
  assetTemplateLink: {
    label: string;
    value: string;
  };
  affirmFailReason: null | any;
  affirmPromise: boolean;
  affirmPromiseContent: {
    viewId: string;
    title: string;
    html: string;
  };
  affirmResult?: {
    affirmDate: string;
    affirmResult: string;
    expireDate: string;
    name: string;
    needAffirm: boolean;
  };
}

export interface IRequestDataChildren {
  key: string;
  label: string;
  limit: number;
  queryKey: string;
  remark: string;
  value: string;
  valueType: number;
  valueTypeFormat: string;
  children: null;
}
export interface IRequestDataList {
  children: IRequestDataChildren[];
  key: string;
  label: string;
  limit: number;
  queryKey: string;
  remark: string;
  value: string;
  valueType: number;
  valueTypeFormat: string;
}