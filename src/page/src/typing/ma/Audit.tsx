export interface Audit {
    configCode: string
    configCodeName?: string
    serialNo: string
    operatorUsername?: string
    assessorUsername?: string
    refusedReason?: string
    auditFlag: number
    auditFlagName?: string
}

export interface AuditStore {
    configCode: string
    serialNo: string
    auditFlag: number
}