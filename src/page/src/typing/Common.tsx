export const PAGE_SIZE_LIST = [5, 10, 15, 20, 50, 100, 500, 1000]
export const DEFAULT_PAGE = 1
export const DEFAULT_LIMIT = 15
export const MAX_LIMIT = PAGE_SIZE_LIST.length > 0 ? PAGE_SIZE_LIST[PAGE_SIZE_LIST.length - 1] : 999999

interface Env {
  ip: string
  port: number
  apiPrefix: string
}

interface ModuleServer {
  ip: string
  label: string
}

export const MODULE_SERVER_LIST: ModuleServer[] = [
  {
    ip: '10.1.97.102',
    label: '开发'
  },
  {
    ip: '10.1.1.197',
    label: '测试1套'
  },
  {
    ip: '10.1.41.41',
    label: '测试2套'
  },
  {
    ip: '10.1.97.80',
    label: '预发'
  },
  {
    ip: '10.32.30.208',
    label: '生产1套'
  },
  {
    ip: '10.32.30.209',
    label: '生产2套'
  },
]


interface AssetType {
  dictionaryValue: string
  assetType?: string
  productType?: string
  label?: string
}

export const TREASURE: AssetType = {
  dictionaryValue: '4',
  label: '利活宝',
}

export const INVEST: AssetType = {
  dictionaryValue: '6',
  label: '利智投',
}

export const PUBLIC_FUND: AssetType = {
  dictionaryValue: '1',
  label: '公募基金',
}

export const PORTFOLIO_FUND: AssetType = {
  dictionaryValue: '19',
  label: '基金组合',
}

export const PRIVATE_FUND: AssetType = {
  dictionaryValue: '11',
  label: '私募基金',
}
