export interface IMarketIndexResponse{
    code: string;
    dateFormat: string;
    dateTime: string;
    index: number;
    indexFormat: string;
    label: string;
    profit: number;
    profitFormat: string;
    profitRate: number;
    profitRateFormat: string;
    timeFormat:string;
  }
  export interface ILeadAdvantage{
      count: string;
      describe:string;
      icon:string;
      unit:string;
  }