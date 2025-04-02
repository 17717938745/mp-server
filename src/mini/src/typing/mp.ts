interface IMpResult {

}

export interface IOption {
  label: string;
  value: string;
}
export interface IArticleInfo {
  createUser: string
  imgUrl: string;
  millis: number;
  millisFormat: string;
  reportDate: string;
  reportDateFormat: string;
  researchType: string;
  subtitle: string;
  tagList: [];
  title: string
  viewCount: number;
  viewId: string
  viewType: number;
  viewTypeFormat: string;
}
export interface ProductItem {
  createUser: string
  id: string
  imgUrl: string
  reportDate: string
  reportDateFormat: string
  subtitle: string
  tags: string[] | null
  title: string
}

export interface IReportDateItem {
  label: string;
  value: [number, number]
}
export interface IResearchTypeItem extends IOption {
  children: IOption[];
}

export interface ITypeResult {
  defaultResearchType: string;
  defaultChildResearchType: string;
  reportDateList: IReportDateItem[];
  researchTypeList: IResearchTypeItem[];
}


interface IUserInfo {
  accountTypeFormat: string;
  avatarUrl: string;
  company: string;
  gender: string;
  mobile: string;
  name: string;
  nickname: string;
  openId: string;
  recommendUser: string;
  accountType?: number
}


export interface IMsgData {
  pageCount: number;
  reqId: string;
  respCode: string;
  respMessage: string;
  st: string;
  sysTime: number;
  totalCount: number;
  version: number | string;
  keyNoticeIds: string[];
  keyNotices: IKeyNotices[];
}
export interface IKeyNotices {
  appointId: number;
  appointSponsor: number;
  appointSponsorDept: number;
  appointTime: number;
  appointType: number;
  custNameAbbr: number;
  firstContact: number;
  firstContactDept: number;
  isSetTop: string | null;
  messageId: number;
  noticeContent: number;
  noticeFromId: number;
  noticePersons: string[];
  noticeToId: string;
  readCase: string;
  sendStatusInit: string;
  sendStatusRe: string;
  sendTimeInit: string;
  sendTimeRe: string;
}
export interface IinteractMsgs {
  appointId: number
  appointSponsorDept: string
  appointTime: string
  appointType: string
  commentFrom: string
  commentFromCd: string
  commentFromDept: string
  commentFromId: string
  commentId: string
  commentPerson: string
  commentPersonCd: string
  commentPersonDept: string
  commentPersonId: string
  custId: number
  custName: string
  custShortName: string
  firstContact: string
  firstContactDept: null
  isSetTop: string
  messageId: string
  messageType: string
  readCase: string
  sendTime: string
}

export interface ICustItem {
  advisorDeptNames: string;
  advisorNames: string;
  coopStatus: string;
  custId: number;
  custName: string;
  custShortName: string;
  custType: string;
  deptFullName: string;
  deptId: string;
  deptName: string;
  firstDeptId: string;
  memberId: string;
  notes: string;
  orgId: string;
  subDeptName: string;
}

export interface IOrgCust {
  abbrcompanyname: string
  companyname: string
  creditcode: string
  custId: number
  enterpriseId: number
  enterpriseType: string
  isCust: string
}
export interface ICommonResult{
  pageCount: number;
  reqId: string;
  respCode: string;
  respMessage: string;
  st: string
  sysTime: number
  totalCount: number
  version: string
}
