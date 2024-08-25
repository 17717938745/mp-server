export declare interface TreeItem {
  id: string
  parentId?: string
}

export declare interface Tree<T> {
  children?: T[]
}

export declare interface Menu extends TreeItem {
  name?: string
  nameKey?: string
  path: string
  component?: string | undefined
  sorter?: number
  white?: boolean
  icon?: string
  showFlag: boolean
}

export declare interface MenuTree extends Menu, Tree<MenuTree> {
  pathList?: string[]
}

export declare interface SignParam {
  username?: string
  password?: string
  autoLogin?: boolean
}

export declare interface BreadCrumb {
  path: string,
  link?: string,
  name: string,
}


class SidebarTree implements Tree<SidebarTree> {

  id: string
  name: string
  nameKey?: string
  pathList: string[] = []
  children: SidebarTree[] = []
  showFlag: boolean = true
  hasComponent: boolean = true
  icon: string = ''

  constructor(id: string, name: string) {
    this.id = id;
    this.name = name;
  }
}

export {SidebarTree}

export declare interface Result {
  code: number
  message: string
}

export declare interface DataResult<T> extends Result {
  data: T
}

export declare interface ListResult<T> extends Result {
  list: T[]
}

export declare interface PageResult<T> extends Result {
  list: T[]
  total: number
}

export declare interface PageDataResult<T, D> extends Result {
  list: T[]
  data: D
  total: number
}

export declare type OptionItem<T> = {
  label: string;
  value: T;
  active?: boolean | undefined;
}

export declare interface Option extends OptionItem<string> {
}

export declare interface TreeOptionItem {
  id: string
  label: string
  disabled: boolean
  children: TreeOptionItem[]
}

export declare interface Role {
  code: string
  name: string
}

export declare interface InterfaceOfModule {
  module: string
  remark: string
  interfaceList: Interface[]
}

export declare interface Interface {
  sorter: number
  module: string
  moduleName: string
  uri: string
  name: string
  method: string
  authorityName: string
  authority: number[]
}

export declare interface RelateInterfaceDataResult extends Result {
  data: {
    checkedOptionList: number[]
    optionList: Interface[]
  }
}

export declare interface RelateDataResult extends Result {
  data: {
    checkedOptionList: string[]
    optionList: Option[]
  }
}

export declare interface RelateTreeDataResult extends Result {
  data: {
    checkedOptionList: string[]
    optionTree: TreeOptionItem[]
  }
}

export declare interface IProcessItem {
  title: string
  desc: string
}

export declare type IProcessList = Array<IProcessItem>

export declare interface IPaging {
  page: number;
  limit: number;

  [key: string]: any
}
