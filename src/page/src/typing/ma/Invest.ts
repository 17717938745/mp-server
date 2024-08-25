/*
 * @Author: niuqiang
 * @Date: 2022-02-26 14:44:45
 * @LastEditors: niuqiang
 * @LastEditTime: 2022-03-03 14:16:49
 * @FilePath: /h5/src/typing/ma/Invest.ts
 * @Description:
 *
 * Copyright (c) 2022 by leadbank, All Rights Reserved.
 */


export interface InvestStore {
    code: string
}



export interface InvestBasic {
    company: string //
    code: string
    name: string
    riskName: string
    riskCode: string
    setupDate: string
    onlineDate: string
    status: string
    isHome: string
    state: string
    shortInfo: string //简介
}


export interface InvestRisk {
    code: string
    name: string
    nickName: string
    info: string
    holdInfo: string
}

