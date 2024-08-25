export interface FundDetailModel {
    buyFee?: BuyFee
    defaultPeriod?: string
    fund?: FundWrapper
    fundCode?: string
    fundCompany?: FundCompany
    fundNavList?: FundNavModel[]
    fundRankingList?: FundRankingModel[]
    managerLst?: FundManagerModel[]
    performanceTrend?: PerformanceTrend
    periodList?: StringOptionItem[]
    timeLineList?: TimeLine[]
}
export interface BuyFee {
        buyFee?: number
        buyFeeFormat?: string
        discountBuyFee?: number
        discountBuyFeeFormat?: string
}
export interface FundWrapper {
        buy?: boolean
        fixedInvest?: boolean
        follow?: boolean
        fundCode?: string
        fundFullName?: string
        fundManagerPoint?: string
        fundManagerPointIcon?: string
        fundManagerPointTitle?: string
        fundName?: string
        fundState?: string
        fundStateFormat?: string
        fundType?: string
        fundTypeFormat?: string
        fundTypeName?: string
        nav?: number
        navDate?: string
        navDateFormat?: string
        navFormat?: string
        rateValue?: number
        rateValueFormat?: string
        rateValueLabel?: string
        riskLevel?: string
        riskLevelFormat?: string
        subscribe?: boolean
        subscribeEnd?: boolean
        subscribeEndDate?: string
        subscribeEndDateFormat?: string
        subscribeStartDate?: string
        subscribeStartDateFormat?: string
        tagNameList?: String[]
        unitProfit?: number
        unitProfitFormat?: string
        yearlyRateValue?: number
        yearlyRateValueFormat?: string
}
export interface FundCompany {
        companyId?: string
        companyLogo?: string
        companyName?: string
}
export interface FundNavModel {
        accumulativeNav?: number
        accumulativeNavFormat?: string
        date?: string
        dayRateValue?: number
        dayRateValueFormat?: string
        fundCode?: string
        fundName?: string
        nav?: number
        navFormat?: string
}
export interface FundRankingModel {
        date?: string
        rateValue?: number
        rateValueFormat?: string
        rateValueType?: string
        rateValueTypeFormat?: string
        sorter?: number
        sorterChange?: string
        sorterFormat?: string
        standardRateValue?: number
        standardRateValueFormat?: string
        starCount?: number
        total?: number
}
export interface FundManagerModel {
        assumeDate?: string
        assumeList?: FundManagerAssumeModel[]
        companyId?: string
        days?: number
        firstAssumeDate?: string
        headUrl?: string
        managerId?: string
        managerName?: string
        repay?: number
        repayFormat?: string
        resume?: string
        years?: number
}
export interface FundManagerAssumeModel {
            endDate?: string
            endDateFormat?: string
            fundCode?: string
            fundName?: string
            leaveReason?: string
            officeType?: string
            repay?: number
            repayFormat?: string
            startDate?: string
            startDateFormat?: string
}
export interface PerformanceTrend {
        date?: string
        rateValue?: number
        rateValueFormat?: string
        rateValueLabel?: string
        rateValueList?: FundPerformanceTrendWrapper[]
        standardRateValue?: number
        standardRateValueFormat?: string
        standardRateValueLabel?: string
}
export interface FundPerformanceTrendWrapper {
            buy?: boolean
            date?: string
            rateValue?: number
            sale?: boolean
            standardRateValue?: number
}
export interface StringOptionItem {
        label?: string
        value?: string
}
export interface TimeLine {
        dateFormat?: string
        describe?: string
}
