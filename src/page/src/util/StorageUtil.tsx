import { MenuTree, SignParam } from '../typing/ma/System';
import { AuditStore } from '../typing/ma/Audit';
import { InvestStore } from '../typing/ma/Invest';
import { isApp, isMp } from './Platform';
import { getPathname } from './LocationUtil';
import { DatabaseQueryData } from '@/typing/ma/Log';

const KEY_STORAGE_PREFIX: string = 'com.pch.',
  KEY_DEVICE_ID: string = 'pch_scope_device_id',
  KEY_SALT: string = 'pch_scope_salt',
  KEY_ROLE_NAME: string = 'pch_scope_role_name',
  KEY_SIGN_DATA: string = 'pch_scope_sign_data',
  KEY_MENU_TREE_LIST: string = 'pch_scope_menu_tree_list',
  KEY_AUDIT: string = 'pch_scope_audit',
  KEY_TOKEN: string = 'userid',
  KEY_INVEST: string = 'scope_invest',
  KEY_DATABASE_QUERY_DATA: string = 'database_query_data',
  KEY_COMBINATION_LIST: string = 'combination_list',
  KEY_PORTFOLIONAME: string = 'portfolioName',
  KEY_PORTFOLIODESCRIBE: string = 'portfolioDescribe',
  KEY_PORTFOLIlIST: string = 'protocolList',
  KEY_PE_TAB_DATA: string = 'peTabData',
  KEY_PE_RISK_DATA: string = 'peRiskData',
  KEY_PE_TAB_ACTIVE_INDEX: string = 'peTabActiveIndex',
  KEY_ADVICE_RISK_TIP: string = 'adviceRiskTip',
  KEY_PE_SORT_TYPE: string = 'peSortType',
  removeStorage = (key: string): void => {
    const redisKey: string = (KEY_STORAGE_PREFIX + key).toUpperCase();
    if (isMp) {
      // @ts-ignore
      wx.removeStorageSync(redisKey);
    } else {
      localStorage.removeItem(redisKey);
    }
  },
  setStorage = (
    key: string,
    value: string | null,
    keyPrefix: string = KEY_STORAGE_PREFIX
  ): void => {
    if (null === value || undefined === value) {
      removeStorage(key);
    } else {
      const redisKey: string = (keyPrefix + key).toUpperCase();
      if (isMp) {
        // @ts-ignore
        wx.setStorageSync(redisKey, value);
      } else {
        localStorage.setItem(redisKey, value);
      }
    }
  },
  getStorage = (key: string, keyPrefix: string = KEY_STORAGE_PREFIX): string | null => {
    const redisKey: string = (keyPrefix + key).toUpperCase();
    if (isMp) {
      // @ts-ignore
      return wx.getStorageSync(redisKey);
    } else {
      return localStorage.getItem(redisKey);
    }
  },
  setSession = (
    key: string,
    value: string | null,
    keyPrefix: string = KEY_STORAGE_PREFIX
  ): void => {
    // sessionStorage.setItem(key, value)\
    if (isMp) {
      // @ts-ignore
      wx.setStorageSync(key, value);
    } else {
      // @ts-ignore
      sessionStorage.setItem(key, value);
    }
  },
  getSession = (key: string, keyPrefix: string = KEY_STORAGE_PREFIX): string | null => {
    if (isMp) {
      // @ts-ignore
      return wx.getStorageSync(key);
    } else {
      return sessionStorage.getItem(key);
    }
  },
  getDeviceId = () => {
    // 手机中不可能打开后管
    if (getCookie('leadAppType')) {
      const token = getCookie('token_lead_h5') || getCookie('token') || '';
      // console.log(`token--js--: ${deviceId}, isApp: ${isApp}`);
      if (token) {
        return token;
      }
    }
    const moduleName = (getPathname() || '').split('/').filter((t: string) => t.length > 0)[0];
    if ('website' === moduleName) {
      const token = getStorage('TOKEN', '');
      if (token) {
        return token;
      }
    }
    const deviceId = getStorage(KEY_DEVICE_ID) || '';
    try {
      return JSON.parse(deviceId)['value'];
    } catch (e) {
      return deviceId;
    }
  },
  setDeviceId = function (deviceId: string): string {
    const moduleName = (getPathname() || '').split('/').filter((t: string) => t.length > 0)[0];
    if ('website' === moduleName) {
      setStorage('TOKEN', deviceId);
    }
    setStorage(KEY_DEVICE_ID, JSON.stringify({ value: deviceId, expire: null }));
    return deviceId;
  },
  removeDeviceId = function (): void {
    removeStorage(KEY_DEVICE_ID);
  },
  setSalt = function (passCode: string): void {
    setStorage(KEY_SALT, JSON.stringify({ value: passCode, expire: null }));
  },
  getSalt = function (): string {
    const v: string = getStorage(KEY_SALT) || '';
    try {
      return JSON.parse(v)['value'];
    } catch (e) {
      return v;
    }
  },
  setUsername = function (username: string): void {
    setStorage(KEY_ROLE_NAME, username);
  },
  getUsername = function (): string {
    return getStorage(KEY_ROLE_NAME) || '';
  },
  setSignData = function (data: SignParam): void {
    setStorage(KEY_SIGN_DATA, JSON.stringify(data));
  },
  getSignData = function (): SignParam {
    try {
      return JSON.parse(getStorage(KEY_SIGN_DATA) || '{}');
    } catch (e) {
      return {};
    }
  },
  setMenuTreeList = function (menuTreeList: MenuTree[]): void {
    setStorage(KEY_MENU_TREE_LIST, JSON.stringify(menuTreeList));
  },
  getMenuTreeList = function (): MenuTree[] {
    try {
      return JSON.parse(getStorage(KEY_MENU_TREE_LIST) || '[]');
    } catch (e) {
      return [];
    }
  },
  setCombinationList = function (combinationList: any) {
    console.log('执行了吗');
    setStorage(KEY_COMBINATION_LIST, JSON.stringify(combinationList));
  },
  getCombinationList = function () {
    try {
      return JSON.parse(getStorage(KEY_COMBINATION_LIST) || '[]');
    } catch (e) {
      return [];
    }
  },
  setAudit = function (audit: AuditStore): void {
    setStorage(KEY_AUDIT, JSON.stringify(audit));
  },
  getAudit = function (): AuditStore {
    try {
      return JSON.parse(getStorage(KEY_AUDIT) || '');
    } catch (e) {
      return {
        configCode: '',
        serialNo: '',
        auditFlag: -1,
      };
    }
  },
  setUserid = function (userid: string): void {
    setStorage(KEY_TOKEN, userid);
  },
  getUserid = function (): string {
    return getStorage(KEY_TOKEN) || '';
  },
  removeUserid = function (): void {
    removeStorage(KEY_TOKEN);
  },
  setFromPath = function (path: string): void {
    setStorage('fromPath', path);
  },
  getFromPath = function () {
    return getStorage('fromPath');
  },
  setFromPathParams = function (str: any): void {
    setStorage('fromPathParams', JSON.stringify(str));
  },
  getFromPathParams = function () {
    return getStorage('fromPathParams');
  },
  setEyesStatus = function (show: string): void {
    setStorage('eyesShow', show);
  },
  getEyesStatus = function () {
    return getStorage('eyesShow');
  },
  removeEyesStatus = function (): void {
    removeStorage('eyesShow');
  },
  setInvest = function (invest: InvestStore): void {
    setStorage(KEY_INVEST, JSON.stringify(invest));
  },
  setDatabaseQueryData = function (data: DatabaseQueryData): void {
    setStorage(KEY_DATABASE_QUERY_DATA, JSON.stringify(data));
  },
  getDatabaseQueryData = function (): DatabaseQueryData | null {
    try {
      return JSON.parse(getStorage(KEY_DATABASE_QUERY_DATA) || '');
    } catch (e) {
      return null;
    }
  },
  getInvest = function (): InvestStore {
    try {
      return JSON.parse(getStorage(KEY_INVEST) || '');
    } catch (e) {
      return {
        code: '',
      };
    }
  },
  getCookie = (name: string): string | null => {
    let cookieValue: string | null = null;
    if (document && document.cookie && document.cookie !== '') {
      let cookies = document.cookie.split(';');
      for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.substring(0, name.length + 1) === name + '=') {
          cookieValue = cookie.substring(name.length + 1);
          break;
        }
      }
    }
    return cookieValue;
  },
  setPortfolioName = function (str: string): void {
    setStorage('KEY_PORTFOLIONAME', str);
  },
  getPortfolioName = function () {
    return getStorage('KEY_PORTFOLIONAME') || '';
  },
  setPortfolioDescribe = function (str: string): void {
    setStorage('KEY_PORTFOLIODESCRIBE', str);
  },
  getPortfolioDescribe = function () {
    return getStorage('KEY_PORTFOLIODESCRIBE') || '';
  },
  setProtocolList = function (str: string): void {
    setStorage(KEY_PORTFOLIlIST, str);
  },
  getProtocolList = function () {
    return getStorage(KEY_PORTFOLIlIST) || '';
  },
  removeProtocolList = function (): void {
    removeStorage(KEY_PORTFOLIlIST);
  },
  setPeTabData = function (str: string): void {
    setStorage(KEY_PE_TAB_DATA, JSON.stringify(str));
  },
  getPeTabData = function () {
    let obj = JSON.parse(getStorage(KEY_PE_TAB_DATA) || '{}');
    return obj || '';
  },
  setPeRiskData = function (str: string): void {
    setStorage(KEY_PE_RISK_DATA, str);
  },
  getPeRiskData = function () {
    return getStorage(KEY_PE_RISK_DATA) || '';
  },
  removePeRiskData = function (): void {
    removeStorage(KEY_PE_RISK_DATA);
  },
  setPeTabActiveIndex = function (str: string): void {
    setStorage(KEY_PE_TAB_ACTIVE_INDEX, str);
  },
  getPeTabActiveIndex = function () {
    return getStorage(KEY_PE_TAB_ACTIVE_INDEX) || '';
  },
  removePeTabActiveIndex = function (): void {
    removeStorage(KEY_PE_TAB_ACTIVE_INDEX);
  },
  setAdviceRiskTip = function (show: string): void {
    setStorage(KEY_ADVICE_RISK_TIP, show);
  },
  getAdviceRiskTip = function () {
    return getStorage(KEY_ADVICE_RISK_TIP) || '1';
  },
  setPeSortType = function (str: string): void {
    setStorage(KEY_PE_SORT_TYPE, str);
  },
  getPeSortType = function () {
    return getStorage(KEY_PE_SORT_TYPE) || '';
  };
export {
  setSession,
  getSession,
  setStorage,
  getStorage,
  getDeviceId,
  setDeviceId,
  removeDeviceId,
  setUsername,
  getUsername,
  setSalt,
  getSalt,
  setMenuTreeList,
  getMenuTreeList,
  setSignData,
  getSignData,
  setAudit,
  getAudit,
  setUserid,
  getUserid,
  removeUserid,
  setFromPath,
  getFromPath,
  setInvest,
  getInvest,
  setDatabaseQueryData,
  getDatabaseQueryData,
  setEyesStatus,
  getEyesStatus,
  removeEyesStatus,
  getCookie,
  setFromPathParams,
  getFromPathParams,
  setCombinationList,
  getCombinationList,
  setPortfolioName,
  getPortfolioName,
  setPortfolioDescribe,
  getPortfolioDescribe,
  setProtocolList,
  getProtocolList,
  removeProtocolList,
  setPeTabData,
  getPeTabData,
  setPeRiskData,
  getPeRiskData,
  removePeRiskData,
  setPeTabActiveIndex,
  getPeTabActiveIndex,
  removePeTabActiveIndex,
  setAdviceRiskTip,
  getAdviceRiskTip,
  getPeSortType,
  setPeSortType,
};
