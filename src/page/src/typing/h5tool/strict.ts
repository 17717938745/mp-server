export interface BannerItem {
    imgUrl: string
    link: string
}
export interface FundCategoryItem {
    categoryCode: string
    categoryName: string
    linkUrl: string;
}
export interface iconLabelListItem {
    icon: string
    label: string
    link: string
}
export interface topFundItem {
    categoryCode: string
    categoryName: string
    endTime: string
    fundCode: string
    fundName: string
    fundType: string
    fundTypeFormat: string
    homePage: boolean
    id: string
    issued: boolean
    rateValue: number
    rateValueFormat: string
    rateValueType: string
    rateValueTypeFormat: string
    recommendReason: string
    riskLevel: string
    riskLevelFormat: string
    slogan: string
    sorter: number
    startTime: string
    tagNameList: string[]
}



export interface HomePage {
    bannerList: BannerItem[]
    defaultFundCategory: string
    followedFundCodeList: any
    fundCategoryList: FundCategoryItem[]
    header: BannerItem
    topFundList: topFundItem[]
    iconLabelList: iconLabelListItem[]
    fundTypeList: { label: string; value: string }[]
}
export interface FundInformation {
    fundCode: string
    fundName: string
    fundNameFormat: string
    describe: string
    rateValue: number
    rateValueFormat: string
    rateValueType: string
    valueDate: string
    valueDateFormat: string
    tagNameList: string[]
}
export interface TopMoneyFundInfo extends FundInformation {
    riskLevel: string
    riskLevelFormat: string
}
export interface hotMoneyFundInfoListItem {
    fundCode: string
    fundName: string
    fundNameFormat: string
    rateValue: string
    rateValueFormat: string
    rateValueType: string
    minHoldDays: string
}
export interface MoneyPage {
    treasureFundInfo: FundInformation
    topMoneyFundInfo: TopMoneyFundInfo
    hotMoneyFundInfoList: hotMoneyFundInfoListItem[]
}