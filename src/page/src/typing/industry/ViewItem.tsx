import {OptionItem} from "@/typing/ma/System";

export interface ViewConfig {
  type?: ValueType,
  mergeKey?: string[],
  optionList?: OptionItem<any>[],
  value: string,
  originValue?: string,
  label?: string,
  labelKey?: string,
  width?: number,
  align?: string,
  hide?: boolean,
  highLight?: boolean,
  showOverflow?: boolean,
  children?: ViewConfig[],
  managerEdit?: Function,
  openLink?: Function,
}

export enum ViewType {
  TableColumn,
  Description,
}

export enum ValueType {
  Expand,
  Text,
  Image,
  Video,
  TextList,
  SerialNoList,
  DeviceRunningTime,
  Valid,
  HighLight,
  Number,
  NumberPositive,
  OrderLink,
  DeviceLink,
  Operator,
  FixedText,
  Attachment,
  ManagerEdit,
  TagList,
  Template,
  SelectEdit,
  NumberEdit,
  TextEdit,
  DateEdit,
  ValidEdit,
  Link,
  Selection,
  TextArea,
}
