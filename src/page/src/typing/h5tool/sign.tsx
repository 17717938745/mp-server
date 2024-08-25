/*
 * @Author: YourName
 * @Date: 2022-02-24 15:20:11
 * @LastEditTime: 2023-06-28 10:41:51
 * @LastEditors: 韦玮莹
 * @Description: 
 * @FilePath: \h5\src\typing\h5tool\sign.tsx
 * 版权声明
 */

export interface registers {
    code: string,
    password?:string
}
export interface bankAccount {
    bankAccount: string
}
export interface open {
    bankAccount: string,
    bankProvinceName: string,
    bankProvinceId: string,
    bankCityId: string,
    bankCityName: string,
    bankMobile: string,

}
export  interface PurchaseForm {
    code: string
    value: number
    tradePassword: string
    bankCardId: string
    treasure: boolean;
    getCode?:boolean
}
export interface TradeRedeem {
    tradeAccount: string,
    tradePassword: string,
    fastRedeem?: boolean,
    share: number,
    redeemToBankCard?: boolean
}
export interface dividend {
    tradeAccount: string,
    dividendMethod: string,
    tradePassword: string,
    fundCode: string
}