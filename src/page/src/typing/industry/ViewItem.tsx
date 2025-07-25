import {OptionItem} from "@/typing/ma/System";

export interface ViewConfig {
  type?: ValueType,
  mergeKey?: string[],
  optionList?: OptionItem<any>[],
  allowCreate: boolean,
  value: string,
  originValue?: string,
  className?: string,
  label?: string,
  labelKey?: string,
  width?: number,
  pictureHeight?: number,
  align?: string,
  textLabel?: string,
  hide?: boolean,
  highLight?: boolean,
  showOverflow?: boolean,
  children?: ViewConfig[],
  managerEdit?: Function,
  valueHighLight?: Function,
  openLink?: Function,
  editable?: Function,
  listShowType?: number,
  staticIdList?: string[],
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
  PictureText,
  SwitchEdit,
  BoldText,
}
