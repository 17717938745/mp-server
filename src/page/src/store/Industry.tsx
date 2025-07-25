import {StoreOptions} from 'vuex'
import {Tag} from '../typing/ma/Tag'
import {MenuTree} from '../typing/ma/System'
import {getMenuTreeList, getStorage, setMenuTreeList, setStorage} from '../util/StorageUtil'
import {clearAll, replaceAll} from '../util/ArrayUtil'
import {storeOptions as commonStoreOptions, StoreType as CommonStoreType} from './Common'

const ROLE_LIST_KEY: string = 'industry_role_list'
const USER_KEY: string = 'industry_user'
const TEXT_KEY: string = 'industry_text'

export interface Text {
  value: string;
  label: string;
}

export interface TextObject {
  [key: string]: Text
}

export interface MultipleText {
  [key: string]: Text[]
}

export interface Label {
  [key: string]: string
}

const MULTIPLE_TEXT_LIST: MultipleText = {
      'CHS': [
        {
          "value": "successAcceptOrder",
          "label": "是否成功接单"
        },
        {
          "value": "bidder",
          "label": "报价人"
        },
        {
          "value": "summaryPrice",
          "label": "汇总价格/元"
        },
        {
          "value": "processTime",
          "label": "加工时间"
        },
        {
          "value": "processUnitPrice",
          "label": "加工单价"
        },
        {
          "value": "processDevice",
          "label": "加工设备"
        },
        {
          "value": "quotationDate",
          "label": "报价日期"
        },
        {
          "value": "applicant",
          "label": "申请人"
        },
        {
          "value": "materialQuality",
          "label": "材质"
        },
        {
          "value": "customer",
          "label": "客户"
        },
        {
          "value": "visitorName",
          "label": "访客姓名"
        },
        {
          "value": "phoneNumber",
          "label": "手机号"
        },
        {
          "value": "companyName",
          "label": "公司名称"
        },
        {
          "value": "visitContent",
          "label": "来访内容"
        },
        {
          "value": "expectedVisitTime",
          "label": "预计来访时间"
        },
        {
          "value": "expectedEndTime",
          "label": "预计访问结束时间"
        },
        {
          "value": "contactPerson",
          "label": "联系人"
        },
        {
          "value": "visitDepartment",
          "label": "来访部门"
        },
        {
          "value": "approver",
          "label": "批准人"
        },
        {
          "value": "approve",
          "label": "批准人"
        },
        {
          "value": "idAndPhotos",
          "label": "证件及本人照片（5张）"
        },
        {
          "value": "remarks",
          "label": "备注"
        },
        {
          "value": "factoryRecords",
          "label": "出厂记录（5张）"
        },
        {
          "value": "visitorFactoryDate",
          "label": "访客出厂日期"
        },
        {
          "value": "printNumber",
          "label": "打印单号"
        },
        {
          "value": "approval",
          "label": "被访人签字"
        },
        {value: "visitorManage", label: "访客管理"},
        {value: "externalSign", label: "外网登录"},
        {value: "innerNetSection", label: "内网网段"},
        {"value": "scoreReport", "label": "季度评比报告"},
        {"value": "perspectiveAnalysis", "label": "透视分析"},
        {"value": "employeeId", "label": "工号"},
        {"value": "userName", "label": "姓名"},
        {"value": "quarter", "label": "季度"},
        {"value": "qualityScore", "label": "质量（50分）"},
        {"value": "attendanceScore", "label": "全勤/团结（10分）"},
        {"value": "safetyScore", "label": "环境安全和工艺（20分）"},
        {"value": "monthlyPerformance", "label": "月度绩效 (20分)"},
        {"value": "totalWorkDays", "label": "总上班日数"},
        {"value": "evaluationMonths", "label": "评估月数"},
        {"value": "evaluationResult", "label": "评比结果"},
        {"value": "quarterlyBonus", "label": "季度奖金"},
        {"value": "photos", "label": "照片(最多50)"},
        {
          "value": "workDressType",
          "label": "工装类型"
        },
        {
          "value": "remark",
          "label": "描述"
        },
        {
          "value": "designNumberOfPdf",
          "label": "图号（挂PDF）"
        },
        {
          "value": "applyCount",
          "label": "申请数量"
        },
        {
          "value": "applyDate",
          "label": "申请日期"
        },
        {
          "value": "storePosition",
          "label": "库存位置"
        },
        {
          "value": "checkAccept",
          "label": "验收"
        },
        {
          "value": "descriptionOfOrder",
          "label": "备注（注明为哪个订单服务）"
        },
        {
          "value": "storeCount",
          "label": "入库数量"
        },
        {
          "value": "storeDateDescription",
          "label": "入库日期备注"
        },
        {
          "value": "storePicture",
          "label": "入库图片"
        },
        {
          "value": "workDressManage",
          "label": "工装管理"
        },
        {
          "value": "managerUser",
          "label": "管理者"
        },
        {
          "value": "isCirculatedDocument",
          "label": "是否流转单据"
        },
        {
          "value": "vietnamCount",
          "label": "Vietnam"
        },
        {
          "value": "chinaCount",
          "label": "China"
        },
        {
          "value": "nationality",
          "label": "国籍"
        },
        {
          "value": "materialDescription",
          "label": "物料描述"
        },
        {
          "value": "inspectionReport",
          "label": "检查报告"
        },
        {
          "value": "processGroupCheck",
          "label": "工艺组检查"
        },
        {
          "value": "ehsSafetyGroupCheck",
          "label": "EHS安全组检查"
        },
        {
          "value": "qualityDepartmentCheck",
          "label": "质量部检查"
        },
        {
          "value": "apiDevice",
          "label": "API设备"
        },
        {
          "value": "refuelPhoto",
          "label": "设备加油照片"
        },
        {
          "value": "generateOrderInspectionRecord",
          "label": "生成订单检验记录"
        },
        {
          "value": "loadMore",
          "label": "加载更多"
        },
        {
          "value": "orderTotalQuantity",
          "label": "报检单合计数量"
        },
        {
          "value": "totalMaterialQuantity",
          "label": "领料合计数量"
        },
        {
          "value": "identification",
          "label": "标识"
        },
        {
          "value": "hardness",
          "label": "硬度"
        },
        {
          "value": "identificationHardnessRemark",
          "label": "标识/硬度备注"
        },
        {
          "value": "dimension",
          "label": "尺寸"
        },
        {
          "value": "ndeDimensionRemark",
          "label": "NDE/尺寸备注"
        },
        {
          "value": "inspectionCompletedQuantity",
          "label": "检验完成数量"
        },
        {
          "value": "orderQuantity",
          "label": "订单数量"
        },
        {
          "value": "identificationPerson",
          "label": "标识人员"
        },
        {
          "value": "identificationDate",
          "label": "标识日期"
        },
        {
          "value": "inspectionPerson",
          "label": "检验人员"
        },
        {
          "value": "inspectionCompletedDate",
          "label": "检验完成日期"
        },
        {
          "value": "planQuantity",
          "label": "计划数量"
        },
        {
          "value": "remainingQuantity",
          "label": "剩余数量"
        },
        {
          "value": "outOfPlanOrderNo",
          "label": "计划外单号"
        },
        {
          "value": "unitHour",
          "label": "单位：小时"
        },
        {
          "value": "templateCount",
          "label": "数量"
        },
        {
          "value": "promiseReturnDate",
          "label": "承诺归还日期"
        },
        {
          "value": "operatorPerson",
          "label": "经办人"
        },
        {
          "value": "borrowPhotoCount",
          "label": "借出照片数量"
        },
        {
          "value": "returnPhotoCount",
          "label": "还回照片数量"
        },
        {
          "value": "description",
          "label": "备注"
        },
        {
          "value": "returnCount",
          "label": "已还数量"
        },
        {
          "value": "actualReturnDate",
          "label": "实际归还日期"
        },
        {
          "value": "meetRequirement",
          "label": "是否符合要求"
        },
        {
          "value": "borrowTemplateDate",
          "label": "借用日期"
        },
        {
          "value": "borrowTemplatePerson",
          "label": "借用方"
        },
        {
          "value": "borrowPhoto",
          "label": "借出照片"
        },
        {
          "value": "returnPhoto",
          "label": "还回照片"
        },
        {
          "value": "isReturn",
          "label": "是否归还"
        },
        {
          "value": "templateOrderNo",
          "label": "单号"
        },
        {
          "value": "templateManage",
          "label": "物品借出单"
        },
        {
          "value": "material",
          "label": "生产工单"
        },
        {
          "value": "designManage",
          "label": "计划管理"
        },
        {
          "value": "generateTask",
          "label": "是否生成计划"
        },
        {
          "value": "customerOrderNo",
          "label": "客户订单号"
        },
        {
          "value": "customerProjectSequence",
          "label": "客户项次号"
        },
        {
          "value": "saleOrderNo",
          "label": "销售订单号"
        },
        {
          "value": "materialNo",
          "label": "物料号"
        },
        {
          "value": "improveMaterialDescribe",
          "label": "加工物料描述"
        },
        {
          "value": "designNumber",
          "label": "图号"
        },
        {
          "value": "orderCount",
          "label": "订单数量"
        },
        {
          "value": "completeMachineAssemblyRecord",
          "label": "整机装配记录"
        },
        {
          "value": "createOrderCount",
          "label": "创建订单数量"
        },
        {
          "value": "createTime",
          "label": "创建时间"
        },
        {
          "value": "productionDate",
          "label": "投产日期"
        },
        {
          "value": "promiseDoneDate",
          "label": "承诺完成日期"
        },
        {
          "value": "blankMaterialNo",
          "label": "毛坯物料号"
        },
        {
          "value": "blankMaterialDescribe",
          "label": "毛坯物料描述"
        },
        {
          "value": "materialCount",
          "label": "领料数量"
        },
        {
          "value": "stoveNo",
          "label": "炉号"
        },
        {
          "value": "hotBatchNo",
          "label": "热批号"
        },
        {
          "value": "serialNo",
          "label": "序列号"
        },
        {
          "value": "surplusCount",
          "label": "欠交数量"
        },
        {
          "value": "nde",
          "label": "NDE"
        },
        {
          "value": "assemble",
          "label": "装配"
        },
        {
          "value": "testPress",
          "label": "试压"
        },
        {
          "value": "surfaceTreatment",
          "label": "表面处理"
        },
        {
          "value": "deliverCount",
          "label": "发货数量"
        },
        {
          "value": "deliverDate",
          "label": "发货日期"
        },
        {
          "value": "deliverDateRemark",
          "label": "发货日期Remark"
        },
        {
          "value": "receiptCount",
          "label": "收货数量"
        },
        {
          "value": "receiptDate",
          "label": "收货日期"
        },
        {
          "value": "receiptDateRemark",
          "label": "收货日期Remark"
        },
        {
          "value": "scrapCount",
          "label": "报废数量"
        },
        {
          "value": "taskManage",
          "label": "作业计划"
        },
        {
          "value": "supplierPromiseDoneDate",
          "label": "外协承诺完成时间"
        },
        {
          "value": "supplierDoneDate",
          "label": "要求外协完成交期"
        },
        {
          "value": "chargeCompany",
          "label": "负责单位"
        },
        {
          "value": "productionCount",
          "label": "排产数量"
        },
        {
          "value": "arrangeProductionDate",
          "label": "排产日期"
        },
        {
          "value": "materialOrderNo",
          "label": "领料单号"
        },
        {
          "value": "checkOrderNo",
          "label": "报检单号"
        },
        {
          "value": "date",
          "label": "日期"
        },
        {
          "value": "person",
          "label": "人员"
        },
        {
          "value": "reportDate",
          "label": "日期"
        },
        {
          "value": "user",
          "label": "人员"
        },
        {
          "value": "partyUser",
          "label": "当事人"
        },
        {
          "value": "directLeader",
          "label": "上级领导"
        },
        {
          "value": "customerShortName",
          "label": "客户简称"
        },
        {
          "value": "purchaseOrderNo",
          "label": "采购订单编号"
        },
        {
          "value": "poProject",
          "label": "PO项目"
        },
        {
          "value": "saleOrderNo",
          "label": "销售订单"
        },
        {
          "value": "orderProject",
          "label": "订单项目"
        },
        {
          "value": "eachBoxCount",
          "label": "每箱数量"
        },
        {
          "value": "boxNumber",
          "label": "箱号"
        },
        {
          "value": "volume",
          "label": "长*宽*高(MM)"
        },
        {
          "value": "unitWeight",
          "label": "单件重量"
        },
        {
          "value": "eachBoxWeight",
          "label": "每箱重量"
        },
        {
          "value": "orderNo",
          "label": "订单号"
        },
        {
          "value": "photo",
          "label": "拍照"
        },
        {
          "value": "photoUnit",
          "label": "张"
        },
        {
          "value": "disqualificationOrderNo",
          "label": "不合格单号"
        },
        {
          "value": "inspector",
          "label": "检验人员"
        },
        {
          "value": "projectSequence",
          "label": "项次"
        },
        {
          "value": "materialDescribe",
          "label": "物料描述"
        },
        {
          "value": "roughcastDesignNumber",
          "label": "毛坯图号"
        },
        {
          "value": "pressureTest",
          "label": "试压"
        },
        {
          "value": "dealSurface",
          "label": "表面处理"
        },
        {
          "value": "scheduleProduce",
          "label": "是否排产"
        },
        {
          "value": "planReformCount",
          "label": "计划加工数量"
        },
        {
          "value": "supplierRemark",
          "label": "外协工序备注"
        },
        {
          "value": "productCountHour8",
          "label": "8H班产量/件"
        }, {
          "value": "productCountHour12",
          "label": "12小时班产量/件"
        }, {
          "value": "processWorkingHour",
          "label": "工序工时/件"
        },
        {
          "value": "process",
          "label": "流程"
        },
        {
          "value": "disqualificationContent",
          "label": "不合格内容"
        },
        {
          "value": "count",
          "label": "数量"
        },
        {
          "value": "orderUnit",
          "label": "件"
        },
        {
          "value": "checkPoint",
          "label": "检验节点"
        },
        {
          "value": "dutyPerson",
          "label": "责任人员"
        },
        {
          "value": "qualityDealOpinion",
          "label": "质量处理意见"
        },
        {
          "value": "skillDealOpinion",
          "label": "技术处理意见"
        },
        {
          "value": "fineAmount",
          "label": "扣款"
        },
        {
          "value": "defectType",
          "label": "缺陷类型"
        },
        {
          "value": "createDate",
          "label": "创建日期"
        },
        {
          "value": "department",
          "label": "部门"
        },
        {
          "value": "optimizeType",
          "label": "精益类型"
        },
        {
          "value": "existsProblem",
          "label": "存在问题"
        },
        {
          "value": "solveScheme",
          "label": "解决方案"
        },
        {
          "value": "responsiblePerson",
          "label": "负责人"
        },
        {
          "value": "planCompleteTime",
          "label": "计划完成时间"
        },
        {
          "value": "awardAmount",
          "label": "奖励金额"
        },
        {
          "value": "valid",
          "label": "结案"
        },
        {
          "value": "boxFlagCard",
          "label": "装箱标识卡"
        },
        {
          "value": "boxFlagId",
          "label": "装箱标识卡"
        },
        {
          "value": "reportManage",
          "label": "报告管理"
        },
        {
          "value": "reportSummaryAccount",
          "label": "机加工KPI数据汇总表"
        },
        {
          "value": "reportSummaryDevice",
          "label": "机加工设备数据汇总表"
        },
        {
          "value": "accidentReport",
          "label": "事故报告"
        },
        {
          "value": "accidentReportBackup",
          "label": "事故报告（Backup）"
        },
        {
          "value": "accidentEvent",
          "label": "EHS安全"
        },
        {
          "value": "accidentQuality",
          "label": "质量事故报告"
        },
        {
          "value": "accidentCrash",
          "label": "设备撞机"
        },
        {
          "value": "accidentTrouble",
          "label": "量具损坏"
        },
        {
          "value": "userRunReport",
          "label": "人员运行日报"
        },
        {
          "value": "productQuotaManage",
          "label": "产品定额管理"
        },
        {
          "value": "quotationManage",
          "label": "加工报价管理"
        },
        {
          "value": "orderManage",
          "label": "订单管理"
        },
        {
          "value": "orderInspectionRecord",
          "label": "订单检验记录"
        },
        {
          "value": "deviceManage",
          "label": "设备管理"
        },
        {
          "value": "reportSumDeviceCompletePercent",
          "label": "累计项:当班绩效，当台的效率/%"
        },
        {
          "value": "reportAvgDeviceCompletePercent",
          "label": "平均值项:当班绩效，当台的效率/%"
        },
        {
          "value": "reportSumDeviceUsePercent",
          "label": "累计项：当班设备利用率"
        },
        {
          "value": "totalCount",
          "label": "记录数"
        },
        {
          "value": "userCount",
          "label": "人员数"
        },
        {
          "value": "reportDateCount",
          "label": "工作天数"
        },
        {
          "value": "reportDeviceCompletePercent",
          "label": "平均值项:当班绩效，当台的效率/%"
        },
        {
          "value": "reportAvgDeviceCompletePercent",
          "label": "平均值项:当班绩效，当台的效率/%"
        },
        {
          "value": "reportDeviceUsePercent",
          "label": "平均值项:当班设备利用率"
        },
        {
          "value": "reportDiff",
          "label": "差异比"
        },
        {
          "value": "reportSalary",
          "label": "求和项:当天考核金额"
        },
        {
          "value": "device",
          "label": "设备"
        },
        {
          "value": "paramManage",
          "label": "参数管理"
        },
        {
          "value": "productManage",
          "label": "产品管理"
        },
        {
          "value": "dashboard",
          "label": "首页"
        },
        {
          "value": "forum",
          "label": "道森心声社区"
        },
        {
          "value": "dousonVoiceCommunity",
          "label": "道森心声社区"
        },
        {
          "value": "designNumberCount",
          "label": "图号计数"
        },
        {
          "value": "agree",
          "label": "赞同"
        },
        {
          "value": "comment",
          "label": "评论"
        },
        {
          "value": "delete",
          "label": "删除"
        },
        {
          "value": "pleaseInputTitle",
          "label": "请输入标题"
        },
        {
          "value": "readAll",
          "label": "阅读全文"
        },
        {
          "value": "readSummary",
          "label": "收起"
        },
        {
          "value": "admin",
          "label": "管理页面"
        },
        {
          "value": "processProcedure",
          "label": "加工工序"
        },
        {
          "value": "processType",
          "label": "加工类型"
        },
        {
          "value": "schedule",
          "label": "班次"
        },
        {
          "value": "userProperty",
          "label": "人员属性"
        },
        {
          "value": "workMinute",
          "label": "工作时间（分钟）"
        },
        {
          "value": "stopWorkingContent1",
          "label": "停机内容1"
        },
        {
          "value": "stopWorkingContent2",
          "label": "停机内容2"
        },
        {
          "value": "stopWorkingContent3",
          "label": "停机内容3"
        },
        {
          "value": "accidentType",
          "label": "事故报告类型"
        },
        {
          "value": "most",
          "label": "最多"
        },
        {
          "value": "mostFourPhoto",
          "label": "最多四张"
        },
        {
          "value": "mostThreePhoto",
          "label": "最多三张"
        },
        {
          "value": "mostFourFile",
          "label": "最多四个"
        },
        {
          "value": "systemManage",
          "label": "系统管理"
        },
        {
          "value": "userManage",
          "label": "用户管理"
        },
        {
          "value": "workShop",
          "label": "车间"
        },
        {
          "value": "office",
          "label": "科室"
        },
        {
          "value": "total",
          "label": "总计"
        },
        {
          "value": "scheduleNull",
          "label": "-"
        },
        {
          "value": "scheduleDayTime",
          "label": "白班"
        },
        {
          "value": "scheduleDayTimeTechnologyGroup",
          "label": "白班技术组"
        },
        {
          "value": "scheduleDayTime12",
          "label": "白班12H"
        },
        {
          "value": "scheduleEvening",
          "label": "夜班"
        },
        {
          "value": "scheduleEvening12",
          "label": "夜班12H"
        },
        {
          "value": "scheduleEveningTechnologyGroup",
          "label": "夜班技术组"
        },
        {
          "value": "scheduleMiddle",
          "label": "中班"
        },
        {
          "value": "departmentAndSchedule",
          "label": "部门和班次"
        },
        {
          "value": "accountManage",
          "label": "人员管理"
        },
        {
          "value": "disqualificationOrder",
          "label": "不合格单"
        },
        {
          "value": "keepImprove",
          "label": "精益持续改善"
        },
        {
          "value": "disqualificationOrderId",
          "label": "不合格单ID"
        },
        {
          "value": "disqualificationOrderId",
          "label": "不合格单ID"
        },
        {
          "value": "beforePlanThreePhoto",
          "label": "改善前照片3张"
        },
        {
          "value": "afterPlanThreePhoto",
          "label": "改善后照片3张"
        },
        {
          "value": "supportAttachment",
          "label": "支持的附件"
        },
        {
          "value": "attachment",
          "label": "支持的附件"
        },
        {
          "value": "profession",
          "label": "职务"
        },
        {
          "value": "productInfo",
          "label": "产品信息"
        },
        {
          "value": "disqualificationInfo",
          "label": "不合格信息"
        },
        {
          "value": "dealOpinion",
          "label": "处理意见"
        },
        {
          "value": "username",
          "label": "用户"
        },
        {
          "value": "computerName",
          "label": "设备名称"
        },
        {
          "value": "companyPosition",
          "label": "位置"
        },
        {
          "value": "computerState",
          "label": "IT设备状态"
        },
        {
          "value": "computerManage",
          "label": "电脑管理"
        },
        {
          "value": "brand",
          "label": "品牌"
        },
        {
          "value": "computerModel",
          "label": "设备型号及描述"
        },
        {
          "value": "position",
          "label": "位置"
        },
        {
          "value": "storageDate",
          "label": "入库日期"
        },
        {
          "value": "detailed",
          "label": "是否有账"
        },
        {
          "value": "productPlace",
          "label": "设备原产地"
        },
        {
          "value": "supplier",
          "label": "供应商"
        },
        {
          "value": "name",
          "label": "名称"
        },
        {
          "value": "chineseName",
          "label": "中文名称"
        },
        {
          "value": "mobile",
          "label": "手机号"
        },
        {
          "value": "role",
          "label": "角色"
        },
        {
          "value": "operator",
          "label": "操作"
        },
        {
          "value": "viewAndEdit",
          "label": "预览编辑"
        },
        {
          "value": "index",
          "label": "序号"
        },
        {
          "value": "state",
          "label": "状态"
        },
        {
          "value": "portrait",
          "label": "头像"
        },
        {
          "value": "leaveCompany",
          "label": "离职"
        },
        {
          "value": "normal",
          "label": "正常"
        },
        {
          "value": "testDevice",
          "label": "调试设备"
        },
        {
          "value": "deviceNumber",
          "label": "设备编号"
        },
        {
          "value": "deviceCheckLedgerState",
          "label": "检测设备台账状态"
        },
        {
          "value": "assetManage",
          "label": "资产管理"
        },
        {
          "value": "deviceCheckLedger",
          "label": "检测设备台帐"
        },
        {
          "value": "equipmentNo",
          "label": "设备编号"
        },
        {
          "value": "chineseVietnamName",
          "label": "中越文名称"
        },
        {
          "value": "englishName",
          "label": "英文名称"
        },
        {
          "value": "specification",
          "label": "规格"
        },
        {
          "value": "calibrationUnit",
          "label": "校准单位"
        },
        {
          "value": "calibrationDate",
          "label": "校准日期"
        },
        {
          "value": "dueDate",
          "label": "有效期(下次校验日期)"
        },
        {
          "value": "calibrationPeriod",
          "label": "校验周期（天）"
        },
        {
          "value": "calibrationPeriod",
          "label": "校验周期（天）"
        },
        {
          "value": "manufacturers",
          "label": "厂家"
        },
        {
          "value": "acceptanceStandard",
          "label": "验收标准"
        },
        {
          "value": "storage",
          "label": "库位"
        },
        {
          "value": "outOfStock",
          "label": "是否出库"
        },
        {
          "value": "borrower",
          "label": "借用人"
        },
        {
          "value": "borrowDate",
          "label": "借用日期"
        },
        {
          "value": "clampingMinute",
          "label": "装夹时间/分钟"
        },
        {
          "value": "assistMinute",
          "label": "辅助时间/分钟"
        },
        {
          "value": "runningMinute",
          "label": "程式运行时间/分钟"
        },
        {
          "value": "productStandMinute",
          "label": "产品标准工时"
        },
        {
          "value": "shouldCompleteCount",
          "label": "当班定额产量"
        },
        {
          "value": "orderProjectNo",
          "label": "订单项目号"
        },
        {
          "value": "roughcastExpireDate",
          "label": "毛坯到货日期"
        },
        {
          "value": "actualCompleteCount",
          "label": "当班完成数量"
        },
        {
          "value": "completeMinute",
          "label": "完成工时"
        },
        {
          "value": "leaderSubsidyMinute",
          "label": "主管补贴工时"
        },
        {
          "value": "responsibleTeam",
          "label": "负责小组"
        },
        {
          "value": "deviceCompletePercent",
          "label": "当班绩效，当台的效率/%"
        },
        {
          "value": "clientIp",
          "label": "客户端IP"
        },
        {
          "value": "signInTime",
          "label": "登录时间"
        },
        {
          "value": "isSuccess",
          "label": "是否成功"
        },
        {
          "value": "errorMessage",
          "label": "异常信息"
        },
        {
          "value": "deviceUnitPrice",
          "label": "设备工时单价/VND"
        },
        {
          "value": "salary",
          "label": "当天考核金额/VND"
        },
        {
          "value": "deviceRunningTime",
          "label": "设备运行时间"
        },
        {
          "value": "deviceUsePercent",
          "label": "当班设备利用率%"
        },
        {
          "value": "stopWorkingMinute1",
          "label": "停机时间1"
        },
        {
          "value": "stopWorkingMinute2",
          "label": "停机时间2"
        },
        {
          "value": "stopWorkingMinute3",
          "label": "停机时间3"
        },
        {
          "value": "improveSuggestion",
          "label": "明天的改善意见（未完成85%）"
        },
        {
          "value": "programNumber",
          "label": "程序号"
        },
        {
          "value": "debugMinute",
          "label": "调试时间/分钟"
        },
        {
          "value": "countHour8",
          "label": "8小时加工件数/分子435"
        },
        {
          "value": "countHour12",
          "label": "12小时加工件数/分子585"
        },
        {
          "value": "productId",
          "label": "产品ID"
        },
        {
          "value": "openId",
          "label": "Open ID"
        },
        {
          "value": "password",
          "label": "密码"
        },
        {
          "value": "normalInfo",
          "label": "一般信息"
        },
        {
          "value": "groupLeader",
          "label": "班组长"
        },
        {
          "value": "chargePerson",
          "label": "主管"
        },
        {
          "value": "leaderUserId",
          "label": "当班主管"
        },
        {
          "value": "manager",
          "label": "经理"
        },
        {
          "value": "devicePhoto",
          "label": "机（设备）"
        },
        {
          "value": "deviceDescribe",
          "label": "机（设备）描述"
        },
        {
          "value": "toolDescribe",
          "label": "刀具描述"
        },
        {
          "value": "designNumberPhoto",
          "label": "产品图号"
        },
        {
          "value": "designNumberDescribe",
          "label": "产品图号描述"
        },
        {
          "value": "productWeight",
          "label": "产品重量（KG）"
        },
        {
          "value": "accidentDescribe",
          "label": "问题描述"
        },
        {
          "value": "accidentEventDescribe",
          "label": "发生过程描述，人员受伤情况，财产损失情况"
        },
        {
          "value": "accidentQualityDescribe",
          "label": "发生过程描述，财产损失情况"
        },
        {
          "value": "accidentImproveDescribe",
          "label": "发生过程描述，人员受伤情况，财产损失情况"
        },
        {
          "value": "sceneAndAccidentDescribe",
          "label": "场景和问题描述"
        },
        {
          "value": "video",
          "label": "视频"
        },
        {
          "value": "damagePhotoList",
          "label": "人受伤情况"
        },
        {
          "value": "damageDescribe",
          "label": "人受伤情况描述"
        },
        {
          "value": "propertyLossDescribe",
          "label": "财产损失情况描述"
        },
        {
          "value": "propertyLossPhotoList",
          "label": "财产损失情况"
        },
        {
          "value": "humanFactorReason",
          "label": "原因分析"
        },
        {
          "value": "eventReason",
          "label": "EHS原因分析"
        },
        {
          "value": "qualityReason",
          "label": "原因分析"
        },
        {
          "value": "crashReason",
          "label": "撞机原因分析"
        },
        {
          "value": "troubleReason",
          "label": "量具事故原因分析"
        },
        {
          "value": "improveReason",
          "label": "原因分析"
        },
        {
          "value": "humanFactorSolve",
          "label": "解决方法"
        },
        {
          "value": "eventSolve",
          "label": "解决方法"
        },
        {
          "value": "qualitySolve",
          "label": "解决方法"
        },
        {
          "value": "improveSolve",
          "label": "解决方法"
        },
        {
          "value": "improveDescribe",
          "label": "改善后的证据描述"
        },
        {
          "value": "eventOpinion",
          "label": "奖惩意见"
        },
        {
          "value": "qualityOpinion",
          "label": "奖惩意见"
        },
        {
          "value": "improveOpinion",
          "label": "奖惩意见"
        },
        {
          "value": "eventPhoto",
          "label": "问题照片"
        },
        {
          "value": "qualityPhoto",
          "label": "问题照片"
        },
        {
          "value": "improvePhoto",
          "label": "问题照片"
        },
        {
          "value": "eventFile",
          "label": "问题附件"
        },
        {
          "value": "qualityFile",
          "label": "问题附件"
        },
        {
          "value": "improveFile",
          "label": "问题附件"
        },
        {
          "value": "improveEventPhoto",
          "label": "改善照片"
        },
        {
          "value": "improveQualityPhoto",
          "label": "改善照片"
        },
        {
          "value": "improveImprovePhoto",
          "label": "改善照片"
        },
        {
          "value": "improveEventFile",
          "label": "改善后附件"
        },
        {
          "value": "improveQualityFile",
          "label": "改善后附件"
        },
        {
          "value": "improveImproveFile",
          "label": "改善后附件"
        },
        {
          "value": "awardOption",
          "label": "奖惩意见"
        },
        {
          "value": "dutyPerson1",
          "label": "责任人1"
        },
        {
          "value": "fineAmount1",
          "label": "金额1"
        },
        {
          "value": "dutyPerson2",
          "label": "责任人2"
        },
        {
          "value": "fineAmount2",
          "label": "金额2"
        },
        {
          "value": "dutyPerson3",
          "label": "责任人3"
        },
        {
          "value": "fineAmount3",
          "label": "金额3"
        },
        {
          "value": "preventAndTracePlan",
          "label": "预防措施跟踪"
        },
        {
          "value": "improveEvidencePhotoList",
          "label": "改善后的证据"
        },
        {
          "value": "improveEvidenceDescribe",
          "label": "改善后的证据描述"
        },
        {
          "value": "interviewResume",
          "label": "面试履历"
        },
        {
          "value": "signInHistory",
          "label": "登录历史"
        },
        {
          "value": "productMachine",
          "label": "生产设备"
        },
        {
          "value": "machineEquipment",
          "label": "生产设备台帐"
        },
        {
          "value": "machineMaintainRepair",
          "label": "生产设备维修履历"
        },
        {
          "value": "equipmentNo",
          "label": "设备编号"
        },
        {
          "value": "accidentImprove",
          "label": "工艺刀具事故"
        },
        {
          "value": "accidentImproveReason",
          "label": "刀具事故原因分析"
        },
        {
          "value": "equipmentPosition",
          "label": "位置"
        },
        {
          "value": "equipmentId",
          "label": "设备ID"
        },
        {
          "value": "equipmentName",
          "label": "设备名称"
        },
        {
          "value": "specification",
          "label": "规格"
        },
        {
          "value": "useUser",
          "label": "使用人员"
        },
        {
          "value": "equipmentDetailDescribe",
          "label": "设备细节描述"
        },
        {
          "value": "gasolineType",
          "label": "设备加油类型"
        },
        {
          "value": "chargeUser",
          "label": "负责人"
        },
        {
          "value": "chargeUser",
          "label": "负责人"
        },
        {
          "value": "brokenReason",
          "label": "故障原因"
        },
        {
          "value": "brokenContent",
          "label": "故障内容"
        },
        {
          "value": "repairContent",
          "label": "修理内容"
        },
        {
          "value": "replacePair",
          "label": "更换配件"
        },
        {
          "value": "repairType",
          "label": "维修类型"
        },
        {
          "value": "stopHour",
          "label": "停机时长H"
        },
        {
          "value": "sendCount",
          "label": "已发运数量"
        },
        {
          "value": "sendDate",
          "label": "发运日期"
        },
        {
          "value": "photoCount",
          "label": "照片数量"
        }, {
          "value": "inventoryCount",
          "label": "库存数量"
        },
        {
          "value": "inventoryDate",
          "label": "库存日期"
        },
        {
          "value": "type",
          "label": "类型"
        },
        {
          "value": "materialDate",
          "label": "领料日期"
        },
        {
          "value": "photoDescribe",
          "label": "图片描述"
        },
        {
          "value": "attachment",
          "label": "附件"
        },
        {
          "value": "inventoryOutOfPlan",
          "label": "计划外库存"
        },
        {
          "value": "inventoryOutOfPlanType",
          "label": "计划外库存"
        },
        {
          "value": "vocationRecord",
          "label": "请假记录"
        },
        {
          "value": "vocationSummary",
          "label": "请假汇总分析"
        },
        {
          "value": "vocationType",
          "label": "请假类型"
        },
        {
          "value": "vocationDate",
          "label": "请假请日期"
        },
        {
          "value": "vocationUser",
          "label": "请假人"
        },
        {"value": "serialNumber", "label": "整机序列号"},
        {"value": "deliveryDate", "label": "承诺交期"},
        {"value": "completedQty", "label": "完成数量"},
        {"value": "valveBody", "label": "阀体"},
        {"value": "valveBodyPhoto", "label": "阀体照片"},
        {"value": "valveCover", "label": "阀盖/尾盖"},
        {"value": "valveCoverPhoto", "label": "阀盖/尾盖照片"},
        {"value": "gate", "label": "闸板"},
        {"value": "gatePhoto", "label": "闸板照片"},
        {"value": "valveSeat", "label": "阀座/阀瓣"},
        {"value": "valveSeatPhoto", "label": "阀座/阀瓣照片"},
        {"value": "valveStem", "label": "阀杆/尾杆"},
        {"value": "valveStemPhoto", "label": "阀杆/尾杆照片"},
        {"value": "assemblyPerson", "label": "装配人员"},
        {"value": "assemblyStartDate", "label": "开始装配日期"},
        {"value": "pressureTest", "label": "整机和驱动器试压"},
        {"value": "pressureTestPhoto", "label": "整机和驱动器试压照片"},
        {"value": "torqueNm", "label": "闸阀开关扭矩，N.m"},
        {"value": "oilInjection", "label": "注油"},
        {"value": "oilInjectionCompleteCount", "label": "注油完成数量"},
        {"value": "oilInjectionCompleteDate", "label": "注油完成日期"},
        {"value": "oilInjectionPhoto", "label": "注油照片"},
        {"value": "mainDataNotAllowEdit", "label": "主数据不允许编辑该列"},
        {"value": "tester", "label": "试压人员"},
        {"value": "assemblyCompleteDate", "label": "装配完成日期"},
        {"value": "assemblyCompleteCount", "label": "装配完成数量"},
        {"value": "assemblyCompleteType", "label": "装配完成情况"},
        {"value": "assemblySummary1Tips", "label": "此处只统计主数据"},
        {"value": "assemblySummaryTips", "label": "按照采购订单编号、PO项目、销售订单、订单项目的维度统计数据，统计时间为当天早上8点（含8点整）至第二早上8点（不含8点整）"},
        {"value": "oilInjectionCompleteType", "label": "注油完成情况"},
        {"value": "lastIncreaseSalaryDate", "label": "最后一次调薪日期"},
        {"value": "planIncreaseSalaryDate", "label": "计划下次调薪日期"},
        {
          "value": "vocationChargeUser",
          "label": "主管领导"
        },
        {
          "value": "vocationStartDate",
          "label": "请假开始日期"
        },
        {
          "value": "vocationEndDate",
          "label": "请假结束日期"
        },
        {
          "value": "vocationReason",
          "label": "请假理由"
        },
        {
          "value": "vocationDays",
          "label": "请假天数"
        },
        {
          "value": "vocationCompliance",
          "label": "是否符合请假规定"
        },
        {
          "value": "violationReason",
          "label": "不符合理由"
        },
        {
          "value": "violationUserCount",
          "label": "请假人次"
        },
        {
          "value": "violationViolationCount",
          "label": "不合格"
        },
        {
          "value": "violationComplianceRate",
          "label": "合格率"
        },
        {
          "value": "pressureTest",
          "label": "试压"
        },
        {
          "value": "schedulingCnc",
          "label": "CNC排班表"
        },
        {
          "value": "scheduleProduce",
          "label": "是否排产"
        },
        {
          "value": "externalProcedureRemark",
          "label": "外协工序备注"
        },
        {
          "value": "procedureCountHour8",
          "label": "8H班产量/件"
        },
        {
          "value": "procedureCountHour12",
          "label": "12小时班产量/件"
        },
        {
          "value": "processManHour",
          "label": "工序工时/件"
        },
        {
          "value": "processManHour",
          "label": "工序工时/件"
        },
        {
          "value": "onlineDate",
          "label": "上线时间"
        },
        {
          "value": "onlineDateDiff",
          "label": "上线时间偏移"
        },
        {
          "value": "offlineDate",
          "label": "下线时间"
        },
        {
          "value": "delay",
          "label": "是否拖期"
        },
        {
          "value": "timelyDeliver",
          "label": "是否准时交付"
        },
        {
          "value": "processCount",
          "label": "已加工数量"
        },
        {
          "value": "procedureSketch",
          "label": "工序简述"
        },
        {
          "value": "surplus",
          "label": "剩余"
        },
        {
          "value": "alreadyComplete",
          "label": "已完成"
        },
        {
          "value": "all",
          "label": "全部"
        },
        {
          "value": "notYetComplete",
          "label": "未完成"
        },
      ],
      'THA': [
        {
          "value": "successAcceptOrder",
          "label": "Đơn hàng đã nhận thành công hay chưa"
        },
        {
          "value": "bidder",
          "label": "Người báo giá"
        },
        {
          "value": "summaryPrice",
          "label": "Tổng giá/nhân dân tệ"
        },
        {
          "value": "processTime",
          "label": "Thời gian gia công"
        },
        {
          "value": "processUnitPrice",
          "label": "Giá gia công"
        },
        {
          "value": "processDevice",
          "label": "Thiết bị gia công"
        },
        {
          "value": "quotationDate",
          "label": "Ngày báo giá"
        },
        {
          "value": "applicant",
          "label": "Người đăng ký"
        },
        {
          "value": "materialQuality",
          "label": "Vật liệu"
        },
        {
          "value": "customer",
          "label": "Khách hàng"
        },
        {
          "value": "visitorName",
          "label": "Họ và Tên Khách"
        },
        {
          "value": "phoneNumber",
          "label": "SDT"
        },
        {
          "value": "companyName",
          "label": "Đơn vị/ Tổ chức"
        },
        {
          "value": "visitContent",
          "label": "Mục đích ra vào"
        },
        {
          "value": "expectedVisitTime",
          "label": "Dự kiến thời gian đến"
        },
        {
          "value": "expectedEndTime",
          "label": "Dự kiến thời gian kết thúc"
        },
        {
          "value": "contactPerson",
          "label": "Người liên hệ"
        },
        {
          "value": "visitDepartment",
          "label": "Bộ phận tiếp đón"
        },
        {
          "value": "approver",
          "label": "Lãnh đạo phê duyệt"
        },
        {
          "value": "approve",
          "label": "Phê duyệt"
        },
        {
          "value": "idAndPhotos",
          "label": "Hình ảnh vào"
        },
        {
          "value": "remarks",
          "label": "Ghi chú"
        },
        {
          "value": "factoryRecords",
          "label": "Hình ảnh ra"
        },
        {
          "value": "visitorFactoryDate",
          "label": "Thời gian ra ngoài"
        },
        {
          "value": "printNumber",
          "label": "Mã phiếu"
        },
        {
          "value": "approval",
          "label": "Chữ ký của người bảo lãnh"
        },
        {value: "visitorManage", label: "Quản lý khách"},
        {value: "externalSign", label: "Đăng nhập mạng bên ngoài"},
        {value: "innerNetSection", label: "Phân đoạn mạng nội bộ"},
        {"value": "scoreReport", "label": "Báo cáo đánh giá hàng quý"},
        {"value": "perspectiveAnalysis", "label": "Phân tích góc nhìn"},
        {"value": "employeeId", "label": "Mã NV"},
        {"value": "userName", "label": "Họ và Tên"},
        {"value": "quarter", "label": "Qúy"},
        {"value": "qualityScore", "label": "chất lượng (50 điểm)"},
        {"value": "attendanceScore", "label": "Chuyên cần/Tính đoàn kết (10 điểm)"},
        {"value": "safetyScore", "label": "EHS / công nghệ ( 20 điểm )"},
        {"value": "monthlyPerformance", "label": "Hiệu suất tháng（20 điểm )"},
        {"value": "totalWorkDays", "label": "Tổng số ngày đi làm"},
        {"value": "evaluationMonths", "label": "Số tháng Đánh giá"},
        {"value": "evaluationResult", "label": "Xếp loại"},
        {"value": "quarterlyBonus", "label": "Thưởng KPI"},
        {"value": "photos", "label": "ảnh MAX50"},
        {
          "value": "workDressType",
          "label": "Loại đồ gá"
        },
        {
          "value": "remark",
          "label": "Mô tả"
        },
        {
          "value": "designNumberOfPdf",
          "label": "Mã bản vẽ (theo PDF)"
        },
        {
          "value": "applyCount",
          "label": "Số lượng yêu cầu"
        },
        {
          "value": "applyDate",
          "label": "Thời gian yêu cầu"
        },
        {
          "value": "storePosition",
          "label": "Vị trí lưu trữ"
        },
        {
          "value": "checkAccept",
          "label": "Người kiểm duyệt"
        },
        {
          "value": "descriptionOfOrder",
          "label": "Ghi chú (nêu rõ dụng cụ này dành cho đơn hàng nào)"
        },
        {
          "value": "storeCount",
          "label": "Số lượng nhập kho"
        },
        {
          "value": "storeDateDescription",
          "label": "Thời gian nhập kho"
        },
        {
          "value": "storePicture",
          "label": "Hình ảnh nhập kho"
        },
        {
          "value": "workDressManage",
          "label": "Quản lí đồ gá"
        },
        {
          "value": "managerUser",
          "label": "Người quản lý"
        },
        {
          "value": "isCirculatedDocument",
          "label": "Đơn hàng đã được ban hành"
        },
        {
          "value": "vietnamCount",
          "label": "Vietnam"
        },
        {
          "value": "chinaCount",
          "label": "China"
        },
        {
          "value": "nationality",
          "label": "Quốc tịch"
        },
        {
          "value": "materialDescription",
          "label": "miêu tả vật liệu"
        },
        {
          "value": "inspectionReport",
          "label": "Báo cáo kiểm tra",
        },
        {
          "value": "processGroupCheck",
          "label": "Kiểm tra tổ công nghệ"
        },
        {
          "value": "ehsSafetyGroupCheck",
          "label": "Kiểm tra tổ an toàn EHS"
        },
        {
          "value": "qualityDepartmentCheck",
          "label": "Kiểm tra bộ phận chất lượng"
        },
        {
          "value": "generateOrderInspectionRecord",
          "label": "Tạo hồ sơ kiểm tra đơn hàng"
        },
        {
          "value": "apiDevice",
          "label": "Thiết bị dùng Audit"
        },
        {
          "value": "refuelPhoto",
          "label": "Hình ảnh loại dầu thêm vào thiết bị"
        },
        {
          "value": "loadMore",
          "label": "Tải thêm"
        },
        {
          "value": "orderTotalQuantity",
          "label": "Số lượng  dự kiến nhập kho"
        },
        {
          "value": "totalMaterialQuantity",
          "label": "Tổng số lượng vật liệu"
        },
        {
          "value": "identification",
          "label": "Khắc dấu"
        },
        {
          "value": "hardness",
          "label": "Độ cứng"
        },
        {
          "value": "identificationHardnessRemark",
          "label": "Khắc dấu/Độ cứng ghi chú"
        },
        {
          "value": "dimension",
          "label": "Kích thước"
        },
        {
          "value": "ndeDimensionRemark",
          "label": "NDE/Kích thước ghi chú"
        },
        {
          "value": "inspectionCompletedQuantity",
          "label": "Số lượng hoàn thành kiểm tra"
        },
        {
          "value": "orderQuantity",
          "label": "SL đơn hàng"
        },
        {
          "value": "identificationPerson",
          "label": "Nhân viên khắc dấu"
        },
        {
          "value": "identificationDate",
          "label": "Ngày khắc dấu"
        },
        {
          "value": "inspectionPerson",
          "label": "Nhân viên kiểm tra"
        },
        {
          "value": "inspectionCompletedDate",
          "label": "Ngày hoàn thành kiểm tra"
        },
        {
          "value": "planQuantity",
          "label": "Số lượng kế hoạch"
        },
        {
          "value": "remainingQuantity",
          "label": "Số lượng còn dư"
        },
        {
          "value": "outOfPlanOrderNo",
          "label": "Mã đơn ngoài kế hoạch"
        },
        {
          "value": "unitHour",
          "label": "Đơn vị: giờ"
        },
        {
          "value": "templateCount",
          "label": "số lượng"
        },
        {
          "value": "promiseReturnDate",
          "label": "Ngày hứa hoàn trả"
        },
        {
          "value": "operatorPerson",
          "label": "Người cho mượn"
        },
        {
          "value": "borrowPhotoCount",
          "label": "Hình ảnh mượn"
        },
        {
          "value": "returnPhotoCount",
          "label": "Hình ảnh trả"
        },
        {
          "value": "description",
          "label": "Ghi chú"
        },
        {
          "value": "returnCount",
          "label": "Số lượng đã trả"
        },
        {
          "value": "actualReturnDate",
          "label": "ngày trả thực tế"
        },
        {
          "value": "borrowTemplateDate",
          "label": "Ngày tháng"
        },
        {
          "value": "borrowTemplatePerson",
          "label": "Bên Mượn"
        },
        {
          "value": "borrowPhoto",
          "label": "Before borrow photo"
        },
        {
          "value": "returnPhoto",
          "label": "Return photo"
        },
        {
          "value": "isReturn",
          "label": "Is return"
        },
        {
          "value": "templateOrderNo",
          "label": "Số đơn"
        },
        {
          "value": "meetRequirement",
          "label": "có đáp ứng  yêu cầu không?"
        },
        {
          "value": "templateManage",
          "label": "Đơn cho mượn dụng cụ"
        },
        {
          "value": "material",
          "label": "Product order"
        },
        {
          "value": "designManage",
          "label": "Quản lý kế hoạch"
        },
        {
          "value": "generateTask",
          "label": "Already generate job"
        },
        {
          "value": "customerOrderNo",
          "label": "Đơn đặt hàng khách hàng"
        },
        {
          "value": "customerProjectSequence",
          "label": " Số hạng mục khách hàng"
        },
        {
          "value": "saleOrderNo",
          "label": "Đơn đặt hàng"
        },
        {
          "value": "materialNo",
          "label": "Mã vật liệu"
        },
        {
          "value": "improveMaterialDescribe",
          "label": "Mô tả vật liệu"
        },
        {
          "value": "designNumber",
          "label": "Mã bản vẽ"
        },
        {
          "value": "orderCount",
          "label": "S L đơn hàng"
        },
        {
          "value": "completeMachineAssemblyRecord",
          "label": "Ghi chép lắp ráp máy"
        },
        {
          "value": "createOrderCount",
          "label": "Tạo số lượng đơn hàng"
        },
        {
          "value": "createTime",
          "label": "Thời gian tạo"
        },
        {
          "value": "productionDate",
          "label": "Ngày sản xuất"
        },
        {
          "value": "promiseDoneDate",
          "label": "Ngày sản xuất chịu trách nhiệm nhập kho"
        },
        {
          "value": "blankMaterialNo",
          "label": "Mã vật liệu phôi"
        },
        {
          "value": "blankMaterialDescribe",
          "label": "Mô tả vật liệu phôi"
        },
        {
          "value": "materialCount",
          "label": "Số lượng lĩnh liệu"
        },
        {
          "value": "stoveNo",
          "label": "Số lò nhiệt"
        },
        {
          "value": "hotBatchNo",
          "label": "Số lô nhiệt"
        },
        {
          "value": "serialNo",
          "label": "Số Serial"
        },
        {
          "value": "surplusCount",
          "label": "Số lượng còn thiếu"
        },
        {
          "value": "nde",
          "label": "NDE"
        },
        {
          "value": "assemble",
          "label": "Lắp ráp"
        },
        {
          "value": "testPress",
          "label": "Thử áp"
        },
        {
          "value": "surfaceTreatment",
          "label": "Xử lý bề mặt"
        },
        {
          "value": "deliverCount",
          "label": "Số lượng xuất GC"
        },
        {
          "value": "deliverDate",
          "label": "Ngày xuất"
        },
        {
          "value": "deliverDateRemark",
          "label": "Ngày xuất Remark"
        },
        {
          "value": "receiptCount",
          "label": "Số lượng nhập"
        },
        {
          "value": "receiptDate",
          "label": "Ngày nhập"
        },
        {
          "value": "receiptDate",
          "label": "Ngày nhập Remark"
        },
        {
          "value": "scrapCount",
          "label": "Số lượng báo phế"
        },
        {
          "value": "taskManage",
          "label": "kế hoạch làm việc"
        },
        {
          "value": "supplierPromiseDoneDate",
          "label": "Thời gian cam kết giao hàng của nhà GC"
        },
        {
          "value": "supplierDoneDate",
          "label": "Ngày yêu cầu Nhà GC giao hàng"
        },
        {
          "value": "chargeCompany",
          "label": "Đơn vị phụ trách"
        },
        {
          "value": "productionCount",
          "label": "Số lượng sắp xếp sản xuất"
        },
        {
          "value": "productionDate",
          "label": "Ngày sắp xếp sản xuất"
        },
        {
          "value": "materialOrderNo",
          "label": "Số phiếu lĩnh liệu"
        },
        {
          "value": "checkOrderNo",
          "label": "Số phiếu nhập kho"
        },
        {
          "value": "date",
          "label": "Ngày tháng"
        },
        {
          "value": "person",
          "label": "Nhân viên"
        },
        {
          "value": "reportDate",
          "label": "Ngày tháng"
        },
        {
          "value": "user",
          "label": "Nhân viên"
        },
        {
          "value": "partyUser",
          "label": "Người phụ trách"
        },
        {
          "value": "directLeader",
          "label": "Quản lí bên trên"
        },
        {
          "value": "customerShortName",
          "label": "Tên khách hàng"
        },
        {
          "value": "purchaseOrderNo",
          "label": "mã đơn đặt hàng của khách hàng"
        },
        {
          "value": "poProject",
          "label": "hạng mục D D H"
        },
        {
          "value": "saleOrderNo",
          "label": "đơn đặt hàng"
        },
        {
          "value": "orderProject",
          "label": "số mục hàng"
        },
        {
          "value": "eachBoxCount",
          "label": "Số lượng mỗi thùng"
        },
        {
          "value": "boxNumber",
          "label": "Mã thùng"
        },
        {
          "value": "volume",
          "label": "Dài*rộng*cao"
        },
        {
          "value": "unitWeight",
          "label": "Trọng lượng mỗi con KG"
        },
        {
          "value": "eachBoxWeight",
          "label": "Trọng lượng mỗi thùng"
        },
        {
          "value": "orderNo",
          "label": "Số phiếu"
        },
        {
          "value": "photo",
          "label": "Hình ảnh"
        },
        {
          "value": "photoUnit",
          "label": "tấm"
        },
        {
          "value": "disqualificationOrderNo",
          "label": "Số NCR"
        },
        {
          "value": "inspector",
          "label": "Người kiểm tra"
        },
        {
          "value": "projectSequence",
          "label": "Hạng mục"
        },
        {
          "value": "materialDescribe",
          "label": "Mô tả vật liệu"
        },
        {
          "value": "roughcastDesignNumber",
          "label": "Bản vẽ phôi"
        },
        {
          "value": "procedureCountHour12",
          "label": "Sản lượng của 12h/ con"
        },
        {
          "value": "processManHour",
          "label": "Thời gian/ cái"
        },
        {
          "value": "onlineDate",
          "label": "Ngày hàng lên máy SX"
        },
        {
          "value": "onlineDateDiff",
          "label": "Ngày hàng lên máy SX Diff"
        },
        {
          "value": "offlineDate",
          "label": "Ngày SX xong"
        },
        {
          "value": "delay",
          "label": "Có quá hạn hay không"
        },
        {
          "value": "timelyDeliver",
          "label": "Timely deliver"
        },
        {
          "value": "processCount",
          "label": "Số lượng đã SX"
        },
        {
          "value": "procedureSketch",
          "label": "Công đoạn gia công"
        },
        {
          "value": "surplus",
          "label": "Còn lại"
        },
        {
          "value": "alreadyComplete",
          "label": "Đã hoàn thành"
        },
        {
          "value": "all",
          "label": "All"
        },
        {
          "value": "notYetComplete",
          "label": "Chưa hoàn thành"
        },
        {
          "value": "procedureCountHour8",
          "label": "Sản lượng của 8h/ con"
        },
        {
          "value": "materialCount",
          "label": "Số lượng lĩnh liệu"
        },
        {
          "value": "planReformCount",
          "label": "Kế hoạch số lượng gia công"
        },
        {
          "value": "supplierRemark",
          "label": "Ghi chú công đoạn nhà gia công"
        },
        {
          "value": "productCountHour8",
          "label": "Sản lượng của 8h/con"
        }, {
          "value": "productCountHour12",
          "label": "Sản lượng của 12h/con"
        },
        {
          "value": "processWorkingHour",
          "label": "Thời gian/cái"
        },
        {
          "value": "externalProcedureRemark",
          "label": "Ghi chú công đoạn nhà gia công"
        },
        {
          "value": "designNumber",
          "label": "Số bản vẽ"
        },
        {
          "value": "process",
          "label": "Quá trình"
        },
        {
          "value": "disqualificationContent",
          "label": "Nội dung của sự không phù hợp"
        },
        {
          "value": "count",
          "label": "Số lượng"
        },
        {
          "value": "orderUnit",
          "label": "PCS"
        },
        {
          "value": "checkPoint",
          "label": "Điểm phát hiện"
        },
        {
          "value": "dutyPerson",
          "label": "Người chịu trách nhiệm"
        },
        {
          "value": "qualityDealOpinion",
          "label": "Ý kiến xử lí bp chất lượng"
        },
        {
          "value": "skillDealOpinion",
          "label": "Ý kiến xử lí bp thiết kế"
        },
        {
          "value": "fineAmount",
          "label": "Số tiền khấu trừ"
        },
        {
          "value": "defectType",
          "label": "Phân loại sai lỗi"
        },
        {
          "value": "createDate",
          "label": "Ngày lập"
        },
        {
          "value": "department",
          "label": "Bộ phận"
        },
        {
          "value": "optimizeType",
          "label": "Hạng mục cần cải tiến"
        },
        {
          "value": "existsProblem",
          "label": "Vấn đề tồn đọng"
        },
        {
          "value": "solveScheme",
          "label": "Phương án xử lý"
        },
        {
          "value": "responsiblePerson",
          "label": "Người phụ trách"
        },
        {
          "value": "planCompleteTime",
          "label": "Kế hoạch hoàn thành"
        },
        {
          "value": "awardAmount",
          "label": "Tiền thưởng"
        },
        {
          "value": "valid",
          "label": "Kết thúc"
        },
        {
          "value": "boxFlagCard",
          "label": "Biểu đóng thùng"
        },
        {
          "value": "boxFlagId",
          "label": "Biểu đóng thùng"
        },
        {
          "value": "reportManage",
          "label": "quản lý báo cáo"
        },
        {
          "value": "reportSummaryAccount",
          "label": "Bảng tổng kết số liệu KPI các máy gia công"
        },
        {
          "value": "reportSummaryDevice",
          "label": "Bảng tổng kết số liệu  các máy gia công"
        },
        {
          "value": "accidentReport",
          "label": "Báo cáo sự cố"
        },
        {
          "value": "accidentReportBackup",
          "label": "Báo cáo sự cố（Backup）"
        },
        {
          "value": "accidentEvent",
          "label": "An toànEHS"
        },
        {
          "value": "accidentQuality",
          "label": "Báo cáo sự cố chất lượng"
        },
        {
          "value": "accidentCrash",
          "label": "Va đập thiết bị"
        },
        {
          "value": "accidentTrouble",
          "label": "Dụng cụ đo bị hỏng"
        },
        {
          "value": "userRunReport",
          "label": "Báo cáo vận hành theo ngày"
        },
        {
          "value": "productQuotaManage",
          "label": "Quản lý định mức sản phẩm"
        },
        {
          "value": "quotationManage",
          "label": "Quản lý báo giá gia công"
        },
        {
          "value": "orderManage",
          "label": "Đơn hàng"
        },
        {
          "value": "orderInspectionRecord",
          "label": "Hồ sơ kiểm tra đơn hàng"
        },
        {
          "value": "deviceManage",
          "label": "Thiết bị manage"
        },
        {
          "value": "reportSumDeviceCompletePercent",
          "label": "Mục trung bình：Hiệu suất ca, hiệu suất máy/%"
        },
        {
          "value": "reportAvgDeviceCompletePercent",
          "label": "Mục trung bình：Hiệu suất ca, hiệu suất máy /%"
        },
        {
          "value": "reportDeviceUsePercent",
          "label": "Tỷ lệ % hiệu suất máy ca"
        },
        {
          "value": "reportDiff",
          "label": "Tỷ lệ chênh lệch"
        },
        {
          "value": "reportSumDeviceUsePercent",
          "label": "Mục trung bình: Tỷ lệ % hiệu suất máy ca"
        },
        {
          "value": "totalCount",
          "label": "Số lượng ghi chép"
        },
        {
          "value": "userCount",
          "label": "Số người"
        },
        {
          "value": "reportDateCount",
          "label": "Số ngày làm việc"
        },
        {
          "value": "reportSalary",
          "label": "Mục tổng: số tiền đánh giá vào ngày đó"
        },
        {
          "value": "reportDeviceCompletePercent",
          "label": "Mục trung bình：Hiệu suất ca, hiệu suất máy /%"
        },
        {
          "value": "reportAvgDeviceCompletePercent",
          "label": "Mục trung bình：Hiệu suất ca, hiệu suất máy /%"
        },
        {
          "value": "device",
          "label": "Thiết bị"
        },
        {
          "value": "paramManage",
          "label": "Tham số"
        },
        {
          "value": "productManage",
          "label": "Quản lý Sản phẩm"
        },
        {
          "value": "dashboard",
          "label": "Home"
        },
        {
          "value": "forum",
          "label": "Bạn đang nghĩ gì vậy?"
        },
        {
          "value": "dousonVoiceCommunity",
          "label": "Bạn đang nghĩ gì vậy?"
        },
        {
          "value": "designNumberCount",
          "label": "Tính toán số bản vẽ"
        },
        {
          "value": "agree",
          "label": "Like"
        },
        {
          "value": "comment",
          "label": "Bình luận"
        },
        {
          "value": "delete",
          "label": "Xóa"
        },
        {
          "value": "pleaseInputTitle",
          "label": "Nhập tiêu đề"
        },
        {
          "value": "readAll",
          "label": "Nội dung bài đăng"
        },
        {
          "value": "readSummary",
          "label": "Thu gọn"
        },
        {
          "value": "admin",
          "label": "Admin"
        },
        {
          "value": "processProcedure",
          "label": "Công đoạn gia công"
        },
        {
          "value": "processType",
          "label": "Phân loại gia công"
        },
        {
          "value": "schedule",
          "label": "Ca loại"
        },
        {
          "value": "userProperty",
          "label": "Nhân viên thuộc bộ phận"
        },
        {
          "value": "workMinute",
          "label": "Thời gian làm việc"
        },
        {
          "value": "stopWorkingContent1",
          "label": "Nội dung dừng máy 1"
        },
        {
          "value": "stopWorkingContent2",
          "label": "Nội dung dừng máy 2"
        },
        {
          "value": "stopWorkingContent3",
          "label": "Nội dung dừng máy 3"
        },
        {
          "value": "accidentType",
          "label": "Loại hình báo cáo"
        },
        {
          "value": "most",
          "label": "Nhiều nhất"
        },
        {
          "value": "mostFourPhoto",
          "label": "Hình ảnh (Nhiều nhất 4 tấm)"
        },
        {
          "value": "mostThreePhoto",
          "label": "Hình ảnh (Nhiều nhất 3 tấm)"
        },
        {
          "value": "mostFourFile",
          "label": "Hình ảnh (Nhiều nhất 4)"
        },
        {
          "value": "systemManage",
          "label": "Quản lý hệ thống"
        },
        {
          "value": "userManage",
          "label": "Người quản trị"
        },
        {
          "value": "workShop",
          "label": "Xưởng SX"
        },
        {
          "value": "office",
          "label": "Văn phòng"
        },
        {
          "value": "total",
          "label": "TOTAL"
        },
        {
          "value": "scheduleNull",
          "label": "-"
        },
        {
          "value": "scheduleDayTime",
          "label": "Ca ngày"
        },
        {
          "value": "scheduleDayTimeTechnologyGroup",
          "label": "Nhóm kỹ thuật ca ngày"
        },
        {
          "value": "scheduleDayTime12",
          "label": "Ca ngày 12H"
        },
        {
          "value": "scheduleEvening",
          "label": "Ca đêm"
        },
        {
          "value": "scheduleEveningTechnologyGroup",
          "label": "Nhóm kỹ thuật ca đêm"
        },
        {
          "value": "scheduleEvening12",
          "label": "Ca đêm 12H"
        },
        {
          "value": "scheduleMiddle",
          "label": "Ca hai"
        },
        {
          "value": "departmentAndSchedule",
          "label": "Bộ phận và Ca loại"
        },
        {
          "value": "accountManage",
          "label": "Thành viên"
        },
        {
          "value": "disqualificationOrder",
          "label": "Số NCR"
        },
        {
          "value": "keepImprove",
          "label": "Cải thiện sai lệch"
        },
        {
          "value": "disqualificationOrderId",
          "label": "Số NCR ID"
        },
        {
          "value": "beforePlanThreePhoto",
          "label": "3 tấm ảnh trước khi cải tiến"
        },
        {
          "value": "afterPlanThreePhoto",
          "label": "3 tấm Hình ảnh sau khi cải tiến"
        },
        {
          "value": "supportAttachment",
          "label": "Nơi lưu trữ văn kiện"
        },
        {
          "value": "attachment",
          "label": "Nơi lưu trữ văn kiện"
        },
        {
          "value": "profession",
          "label": "chức vụ"
        },
        {
          "value": "productInfo",
          "label": "Thông tin sản phẩm"
        },
        {
          "value": "disqualificationInfo",
          "label": "Thông tin sản phẩm không hợp lệ"
        },
        {
          "value": "dealOpinion",
          "label": "Ý kiến xử lý"
        },
        {
          "value": "username",
          "label": "người sử dụng"
        },
        {
          "value": "computerName",
          "label": "Tên thiết bị"
        },
        {
          "value": "companyPosition",
          "label": "Vị trí"
        },
        {
          "value": "computerManage",
          "label": "Mẫu theo dõi máy tính"
        },
        {
          "value": "brand",
          "label": "Thương hiệu"
        },
        {
          "value": "computerModel",
          "label": "Model thiết bị"
        },
        {
          "value": "position",
          "label": "Vị trí"
        },
        {
          "value": "storageDate",
          "label": "Ngày nhập kho"
        },
        {
          "value": "computerState",
          "label": "IT device status"
        },
        {
          "value": "detailed",
          "label": "Có hoặc không có HĐ"
        },
        {
          "value": "productPlace",
          "label": "Xuất xứ"
        },
        {
          "value": "supplier",
          "label": "Nhà cung cấp"
        },
        {
          "value": "state",
          "label": "tình trạng"
        },
        {
          "value": "name",
          "label": "tên gọi"
        },
        {
          "value": "chineseName",
          "label": "Tên trung quốc "
        },
        {
          "value": "mobile",
          "label": "SDT"
        },
        {
          "value": "role",
          "label": "vai trò"
        },
        {
          "value": "operator",
          "label": "thao tác "
        },
        {
          "value": "viewAndEdit",
          "label": "Tùy chỉnh xem trước"
        },
        {
          "value": "index",
          "label": "STT"
        },
        {
          "value": "portrait",
          "label": "Portrait"
        },
        {
          "value": "leaveCompany",
          "label": "nghỉ việc"
        },
        {
          "value": "normal",
          "label": "bình thường"
        },
        {
          "value": "testDevice",
          "label": "Điểu chỉnh máy số"
        },
        {
          "value": "deviceNumber",
          "label": "Mã máy"
        },
        {
          "value": "deviceCheckLedgerState",
          "label": "Sổ nhật ký thiết bị thí nghiệm tình trạng"
        },
        {
          "value": "assetManage",
          "label": "Quản lí tài sản"
        },
        {
          "value": "deviceCheckLedger",
          "label": "Sổ nhật ký thiết bị thí nghiệm"
        },
        {
          "value": "equipmentNo",
          "label": "Mã thiết bị"
        },
        {
          "value": "chineseVietnamName",
          "label": "Tên trung quốc"
        },
        {
          "value": "englishName",
          "label": "Tên tiếng anh"
        },
        {
          "value": "specification",
          "label": "Quy phạm"
        },
        {
          "value": "calibrationUnit",
          "label": "Đơn vị hiệu chuẩn"
        },
        {
          "value": "calibrationDate",
          "label": "Ngày hiệu chuẩn"
        },
        {
          "value": "dueDate",
          "label": "Ngày hết hạn"
        },
        {
          "value": "calibrationPeriod",
          "label": "Chu kì hiệu chuẩn（ngày）"
        },
        {
          "value": "manufacturers",
          "label": "Nhà sản xuất"
        },
        {
          "value": "acceptanceStandard",
          "label": "Tiêu chuẩn chấp nhận"
        },
        {
          "value": "storage",
          "label": "Khu vực lưu trữ"
        },
        {
          "value": "outOfStock",
          "label": "Đã rời kho"
        },
        {
          "value": "borrower",
          "label": "Người mượn"
        },
        {
          "value": "borrowDate",
          "label": "Ngày mượn"
        },
        {
          "value": "clampingMinute",
          "label": "Thời gian gá/phút"
        },
        {
          "value": "assistMinute",
          "label": "Thời gian hỗ trợ/phút"
        },
        {
          "value": "runningMinute",
          "label": "Thời gian vận hành chương trình/phút"
        },
        {
          "value": "productStandMinute",
          "label": "Tiêu chuẩn công thức sản phẩm"
        },
        {
          "value": "shouldCompleteCount",
          "label": "Định mức sản lượng ca"
        },
        {
          "value": "orderProjectNo",
          "label": "Số hạng mục đơn hàng"
        },
        {
          "value": "roughcastExpireDate",
          "label": "Ngày phôi về"
        },
        {
          "value": "actualCompleteCount",
          "label": "Định mức số lượng hoàn thành ca"
        },
        {
          "value": "completeMinute",
          "label": "Số giờ hoàn thành"
        },
        {
          "value": "leaderSubsidyMinute",
          "label": "Số giờ trợ cấp chủ quản"
        },
        {
          "value": "responsibleTeam",
          "label": "nhóm phụ trách"
        },
        {
          "value": "deviceCompletePercent",
          "label": "Hiệu suất ca /%"
        },
        {
          "value": "clientIp",
          "label": "Client IP"
        },
        {
          "value": "signInTime",
          "label": "Sign in time"
        },
        {
          "value": "isSuccess",
          "label": "Is success"
        },
        {
          "value": "errorMessage",
          "label": "Error message"
        },
        {
          "value": "deviceUnitPrice",
          "label": "Đơn giá số giờ thiết bị"
        },
        {
          "value": "salary",
          "label": "Thành tiền hiệu suất ca"
        },
        {
          "value": "deviceRunningTime",
          "label": "Thời gian bắt đầu vận hành máy lúc làm"
        },
        {
          "value": "deviceUsePercent",
          "label": "Tỷ lệ % hiệu suất máy ca%"
        },
        {
          "value": "stopWorkingMinute1",
          "label": "Time dừng 1"
        },
        {
          "value": "stopWorkingMinute2",
          "label": "Time dừng 2"
        },
        {
          "value": "stopWorkingMinute3",
          "label": "Time dừng 3"
        },
        {
          "value": "improveSuggestion",
          "label": "Đề xuất cải tiến hàng ngày ( Không hoàn thành từ 85% )"
        },
        {
          "value": "programNumber",
          "label": "số chương trình"
        },
        {
          "value": "debugMinute",
          "label": "thời gian set hàng/phút"
        },
        {
          "value": "countHour8",
          "label": "Số lượng sản phẩm gia công trong 8h/ 435"
        },
        {
          "value": "countHour12",
          "label": "Số lượng sản phẩm gia công trong 12h/ 585"
        },
        {
          "value": "productId",
          "label": "Product ID"
        },
        {
          "value": "openId",
          "label": "Open ID"
        },
        {
          "value": "password",
          "label": "Password"
        },
        {
          "value": "normalInfo",
          "label": "Thông tin chung"
        },
        {
          "value": "groupLeader",
          "label": "Tổ trưởng"
        },
        {
          "value": "chargePerson",
          "label": "Chủ quản BP"
        },
        {
          "value": "leaderUserId",
          "label": "chủ quản ca đó"
        },
        {
          "value": "manager",
          "label": "Giám đốc BP"
        },
        {
          "value": "devicePhoto",
          "label": "Máy ( Thiết bị & công cụ )"
        },
        {
          "value": "deviceDescribe",
          "label": "Máy ( Thiết bị & công cụ )"
        },
        {
          "value": "toolDescribe",
          "label": "Tool describe"
        },
        {
          "value": "designNumberPhoto",
          "label": "Số bản vẽ"
        },
        {
          "value": "designNumberDescribe",
          "label": "Số bản vẽ"
        },
        {
          "value": "productWeight",
          "label": "Trọng lượng sản phẩm"
        },
        {
          "value": "accidentDescribe",
          "label": "Tóm tắt sự cố"
        },
        {
          "value": "accidentEventDescribe",
          "label": "Tóm tắt sự cố，Tình trạng người bị thương，Tình trạng tổn thất tài sản"
        },
        {
          "value": "accidentQualityDescribe",
          "label": "Tóm tắt sự cố，Tình trạng tổn thất tài sản"
        },
        {
          "value": "accidentImproveDescribe",
          "label": "Tóm tắt sự cố，Tình trạng người bị thương，Tình trạng tổn thất tài sản"
        },
        {
          "value": "accidentImproveDescribe",
          "label": "Tóm tắt sự cố，Tình trạng người bị thương，Tình trạng tổn thất tài sản"
        },
        {
          "value": "sceneAndAccidentDescribe",
          "label": "Tóm tắt sơ bộ hiện trường và sự cố"
        },
        {
          "value": "video",
          "label": "Video"
        },
        {
          "value": "damagePhotoList",
          "label": "Tình trạng người bị thương"
        },
        {
          "value": "damageDescribe",
          "label": "Tình trạng người bị thương"
        },
        {
          "value": "propertyLossDescribe",
          "label": "Tình trạng tổn thất tài sản"
        },
        {
          "value": "propertyLossPhotoList",
          "label": "Tình trạng tổn thất tài sản"
        },
        {
          "value": "humanFactorReason",
          "label": "Phân tích nguyên nhân"
        },
        {
          "value": "eventReason",
          "label": "EHS Phân tích nguyên nhân"
        },
        {
          "value": "qualityReason",
          "label": "Phân tích nguyên nhân"
        },
        {
          "value": "crashReason",
          "label": "Phân tích nguyên nhân（Va đập thiết bị）"
        },
        {
          "value": "troubleReason",
          "label": "Phân tích nguyên nhân（Dụng cụ đo bị hỏng）"
        },
        {
          "value": "improveReason",
          "label": "Phân tích nguyên nhân"
        },
        {
          "value": "humanFactorSolve",
          "label": "Giải pháp xử lý"
        },
        {
          "value": "eventSolve",
          "label": "Giải pháp xử lý"
        },
        {
          "value": "qualitySolve",
          "label": "Giải pháp xử lý"
        },
        {
          "value": "improveSolve",
          "label": "Giải pháp xử lý"
        },
        {
          "value": "improveDescribe",
          "label": "chi tiết sau khi cải thiện"
        },
        {
          "value": "eventOpinion",
          "label": "Đề xuất Thưởng Phạt"
        },
        {
          "value": "qualityOpinion",
          "label": "Đề xuất Thưởng Phạt"
        },
        {
          "value": "improveOpinion",
          "label": "Đề xuất Thưởng Phạt"
        },
        {
          "value": "eventPhoto",
          "label": "Ảnh vấn đề minh hoạ"
        },
        {
          "value": "qualityPhoto",
          "label": "Ảnh vấn đề minh hoạ"
        },
        {
          "value": "improvePhoto",
          "label": "Ảnh vấn đề minh hoạ"
        },
        {
          "value": "eventFile",
          "label": "Ảnh vấn đề minh hoạ"
        },
        {
          "value": "qualityFile",
          "label": "Ảnh vấn đề minh hoạ"
        },
        {
          "value": "improveFile",
          "label": "Ảnh vấn đề minh hoạ"
        },
        {
          "value": "improveEventPhoto",
          "label": "hỉnh ảnh sau khi cải thiện"
        },
        {
          "value": "improveQualityPhoto",
          "label": "hỉnh ảnh sau khi cải thiện"
        },
        {
          "value": "improveImprovePhoto",
          "label": "hỉnh ảnh sau khi cải thiện"
        },
        {
          "value": "improveEventFile",
          "label": "văn kiện sau khi cải thiện"
        },
        {
          "value": "improveQualityFile",
          "label": "văn kiện sau khi cải thiện"
        },
        {
          "value": "improveImproveFile",
          "label": "văn kiện sau khi cải thiện"
        },
        {
          "value": "awardOption",
          "label": "Đề xuất Thưởng Phạt"
        },
        {
          "value": "dutyPerson1",
          "label": "Người phụ trách 1"
        },
        {
          "value": "fineAmount1",
          "label": "Số tiền 1"
        },
        {
          "value": "dutyPerson2",
          "label": "Người phụ trách 2"
        },
        {
          "value": "fineAmount2",
          "label": "Số tiền 2"
        },
        {
          "value": "dutyPerson3",
          "label": "Người phụ trách 3"
        },
        {
          "value": "fineAmount3",
          "label": "Số tiền 3"
        },
        {
          "value": "preventAndTracePlan",
          "label": "Giám sát quá trình phòng ngừa"
        },
        {
          "value": "improveEvidencePhotoList",
          "label": "Sau cái tiến"
        },
        {
          "value": "improveEvidenceDescribe",
          "label": "Sau cái tiến"
        },
        {
          "value": "interviewResume",
          "label": "Interview resume"
        },
        {
          "value": "signInHistory",
          "label": "Sign In History"
        },
        {
          "value": "productMachine",
          "label": "thiết bị sản xuất"
        },
        {
          "value": "machineEquipment",
          "label": "Danh sách máy móc sản xuất"
        },
        {
          "value": "machineMaintainRepair",
          "label": "Lịch sử bảo trì thiết bị sản xuất"
        },
        {
          "value": "equipmentNo",
          "label": "Mã máy"
        },
        {
          "value": "accidentImprove",
          "label": "Sự cố về công cụ dụng cụ , quy trình công nghệ"
        },
        {
          "value": "accidentImproveReason",
          "label": "Phân tích sự cố dao cụ"
        },
        {
          "value": "equipmentPosition",
          "label": "Vị trí"
        },
        {
          "value": "equipmentId",
          "label": "Equipment ID"
        },
        {
          "value": "equipmentName",
          "label": "Tên máy móc"
        },
        {
          "value": "specification",
          "label": "Quy cách"
        },
        {
          "value": "useUser",
          "label": "Người sử dụng"
        },
        {
          "value": "equipmentDetailDescribe",
          "label": "Mô tả chi tiết thiết bị"
        },
        {
          "value": "gasolineType",
          "label": "Loại dầu thêm vào thiết bị"
        },
        {
          "value": "chargeUser",
          "label": "Người sử dụng"
        },
        {
          "value": "brokenReason",
          "label": "Lý do lỗi"
        },
        {
          "value": "brokenContent",
          "label": "Nội dung lỗi"
        },
        {
          "value": "repairContent",
          "label": "Nội dung sửa chữa"
        },
        {
          "value": "replacePair",
          "label": "Phụ kiện thay thế"
        },
        {
          "value": "repairType",
          "label": "Loại sửa chữa"
        },
        {
          "value": "sendCount",
          "label": "Số lương đã xuất"
        },
        {
          "value": "sendDate",
          "label": "Ngày xuất"
        },
        {
          "value": "photoCount",
          "label": "Số lượng hình ảnh"
        },
        {
          "value": "inventoryCount",
          "label": "số lượng"
        },
        {
          "value": "inventoryDate",
          "label": "Ngày hàng về"
        },
        {
          "value": "type",
          "label": "Phân loại hàng"
        },
        {
          "value": "materialDate",
          "label": "Ngày lĩnh liệu"
        },
        {
          "value": "photoDescribe",
          "label": "Hình ảnh"
        },
        {
          "value": "attachment",
          "label": "Đính kèm"
        },
        {
          "value": "inventoryOutOfPlan",
          "label": "hàng tồn kho ngoài kế hoạch"
        },
        {
          "value": "vocationRecord",
          "label": "Ghi chú xin nghỉ"
        },
        {
          "value": "vocationSummary",
          "label": "Phân tích báo cáo xin nghỉ"
        },
        {
          "value": "vocationType",
          "label": "Phân loại xin nghỉ"
        },
        {
          "value": "vocationDate",
          "label": "Ngày xin nghỉ"
        },
        {
          "value": "vocationUser",
          "label": "Người xin nghỉ"
        }, {
          "value": "serialNumber",
          "label": "Seri"
        },
        {
          "value": "deliveryDate",
          "label": "Ngày sản xuất chịu trách nhiệm nhập kho"
        },
        {
          "value": "completedQty",
          "label": "S L hoàn thành"
        },
        {
          "value": "valveBody",
          "label": "Thân van"
        },
        {
          "value": "valveBodyPhoto",
          "label": "Hình ảnh (Max 4)"
        },
        {
          "value": "valveCover",
          "label": "Nắp van/Nắp đậy"
        },
        {
          "value": "valveCoverPhoto",
          "label": "Hình ảnh (Max 4)"
        },
        {
          "value": "gate",
          "label": "Cửa van"
        },
        {
          "value": "gatePhoto",
          "label": "Hình ảnh (Max 4)"
        },
        {
          "value": "valveSeat",
          "label": "Đế van"
        },
        {
          "value": "valveSeatPhoto",
          "label": "Hình ảnh (Max 4)"
        },
        {
          "value": "valveStem",
          "label": "Cán van"
        },
        {
          "value": "valveStemPhoto",
          "label": "Hình ảnh (Max 4)"
        },
        {
          "value": "assemblyPerson",
          "label": "Nhân viên lắp ráp"
        },
        {
          "value": "assemblyStartDate",
          "label": "Ngày bắt đầu lắp"
        },
        {
          "value": "pressureTest",
          "label": "Kiểm tra áp suất toàn bộ máy và Động cơ"
        },
        {
          "value": "pressureTestPhoto",
          "label": "Kiểm tra áp suất toàn bộ máy và Động cơ(Max 6)"
        },
        {"value": "mainDataNotAllowEdit", "label": "Dữ liệu chính không cho phép thay đổi cột này"},
        {
          "value": "torqueNm",
          "label": "lực vặn đóng mở van khi có áp suất N.m"
        },
        {
          "value": "oilInjection",
          "label": "Bơm dầu"
        },
        {"value": "oilInjectionCompleteCount", "label": "Số lượng bơm dầu hoàn thành"},
        {
          "value": "oilInjectionCompleteDate",
          "label": "Ngày bơm dầu hoàn thành"
        },
        {
          "value": "oilInjectionPhoto",
          "label": "Bơm dầu Hình ảnh (Max 4)"
        },
        {
          "value": "tester",
          "label": "Nhân viên thử áp"
        },
        {
          "value": "assemblyCompleteDate",
          "label": "Ngày lắp ráp hoàn thành"
        },
        {"value": "assemblySummary1Tips", "label": "Ở đây chỉ thống kê dữ liệu chính."},
        {"value": "assemblySummaryTips", "label": "Thống kê dữ liệu theo các chiều số đơn đặt hàng mua (PO), hạng mục PO, đơn đặt hàng bán, hạng mục đơn hàng, thời gian thống kê là từ 8 giờ sáng hôm nay (bao gồm 8 giờ đúng) đến 8 giờ sáng ngày hôm sau (không bao gồm 8 giờ đúng)."},
        {"value": "lastIncreaseSalaryDate", "label": "Ngày điều chỉnh lương gần đây nhất"},
        {"value": "planIncreaseSalaryDate", "label": "Ngày dự kiến điều chỉnh lương lần tiếp theo"},
        {
          "value": "schedulingCnc",
          "label": "Bảng chia ca CNC"
        },
        {"value": "assemblyCompleteCount", "label": "Số lượng lắp ráp hoàn thành"},
        {"value": "assemblyCompleteType", "label": "Tình hình hoàn thành lắp ráp"},
        {"value": "oilInjectionCompleteType", "label": "Tình hình hoàn thành việc bơm dầu"},
        {
          "value": "vocationChargeUser",
          "label": "Chủ quản ca"
        },
        {
          "value": "vocationStartDate",
          "label": "Ngày bắt đầu nghỉ"
        },
        {
          "value": "vocationEndDate",
          "label": "Ngày kết thúc"
        },
        {
          "value": "vocationReason",
          "label": " Lý do"
        },
        {
          "value": "vocationDays",
          "label": " Tổng số  ngày nghỉ"
        },
        {
          "value": "vocationCompliance",
          "label": "Hợp lệ/ Không hợp lệ"
        },
        {
          "value": "violationReason",
          "label": "Lý do nghỉ không hợp lệ"
        },
        {
          "value": "violationUserCount",
          "label": "Số lần xin nghỉ"
        },
        {
          "value": "violationViolationCount",
          "label": "không hợp lệ "
        },
        {
          "value": "violationComplianceRate",
          "label": "Tỷ lệ hợp lệ"
        }
      ],
    },
    CHS: Label = {},
    THA: Label = {}
;
(MULTIPLE_TEXT_LIST['CHS'] || []).forEach((t: Text) => {
  CHS[t.value] = t.label
});
(MULTIPLE_TEXT_LIST['THA'] || []).forEach((t: Text) => {
  THA[t.value] = t.label
});
MULTIPLE_TEXT_LIST['CHS|THA'] = (MULTIPLE_TEXT_LIST['CHS'] || []).map((t: Text) => {
  return {
    value: t.value,
    label: `${t.label} ${THA[t.value]}`,
  }
})


export const printMultipleText = (str: string, split: stirng = '', reverse: boolean = false): string => {
  let entityString = ''
  let defaultFormData = ''
  let columnConfigString = '{value: \'expand\', label: \'\', width: 48, type: ValueType.Expand,},\n' +
      '{value: \'operator\', labelKey: \'viewAndEdit\', width: 312, type: ValueType.Operator,},\n'
  let formString = ''
  let formRuleString = ''
  const data = {
    'CHS': [],
    'THA': [],
  }
  let array1 = []
  let array2 = []
  if (split) {
    const arr = str.split('\t')
    for (let i = 0; i < arr.length; i++) {
      const a = arr[i] || ''
      const index = a.indexOf(split)
      if (index > -1) {
        array1[i] = a.substring(0, index).replace("\"", "")
        array2[i] = a.substring(index + 1).replace("\"", "")
      } else {
        array1[i] = a
        array2[i] = ''
      }
    }
  } else {
    const arr = str.split('\n')
    array1 = arr[0].split('\t')
    array2 = arr[1].split('\t')
  }
  for (let i = 0; i < array1.length; i++) {
    const str1 = (array1[i] || '').trim().replace(/^"|"$/g, '').trim()
    const str2 = (array2[i] || '').trim().replace(/^"|"$/g, '').trim()
    const chs = reverse ? str2 : str1
    const tha = reverse ? str1 : str2
    const arr = MULTIPLE_TEXT_LIST['CHS'].filter(t => chs === t.label)
    const arr1 = MULTIPLE_TEXT_LIST['THA'].filter(t => tha === t.label)
    // console.log(`${chs} <> ${tha}, ${JSON.stringify(arr)} <> ${JSON.stringify(arr1)}`)
    const key = arr.length > 0 && arr1.length > 0 && arr1.filter(k => k.value === arr[0].value).length > 0 ? arr[0].value : ''
    if (key) {
      columnConfigString += ('{value: \'' + key + '\', labelKey: \'' + key + '\', width: 189},\n')
      formString += ('        <el-form-item prop="' + key + '" :label="store.state.label.' + key + '">\n' +
          '          <el-input v-model="formData.' + key + '"/>\n' +
          '        </el-form-item>\n')
      defaultFormData += `${key}: '',\n`
      entityString += ('     /**\n' +
          '     * ' + chs + '\n' +
          '     */\n' +
          '    @FieldRemark(value = "' + chs + '")\n' +
          '    @Size(max = 256, message = "' + chs + '长度不合法")\n' +
          '    private String ' + key + ';\n'
      )
      formRuleString += (key + ': [{required: true, message: \'Please check\', trigger: \'blur\'}],\n')
    } else {
      if (arr.length > 0 || arr1.length > 0) {
        console.log(`Found CHS OR THA key, but not found match, CHS: ${JSON.stringify(arr)}, THA: ${JSON.stringify(arr1)}, chs: ${chs}, tha: ${tha}`)
      }
      data['CHS'].push({
        value: `${i}`,
        label: chs,
      })
      data['THA'].push({
        value: `${i}`,
        label: tha,
      })
    }
  }
  console.log(JSON.stringify(data, null, 2))
  console.log(columnConfigString)
  console.log(defaultFormData)
  console.log(entityString)
  console.log(formString)
  console.log(formRuleString)
}

// printMultipleText('工号\t姓名\t季度\t设备编号\t质量 50分\t全勤/团结  10分\t环境安全和工艺 20分\t月度绩效 (20分)\t总上班日数\t总计\t评估月数\t评比结果\t季度奖金\t备注\t当班主管\t照片(最多50)\n' +
//     'Mã NV\tHọ và Tên\tQúy\tMã máy\tchất lượng (50 điểm)\tChuyên cần/Tính đoàn kết (10 điểm)\tEHS / công nghệ ( 20 điểm )\tHiệu suất tháng（20 điểm )\tTổng số ngày đi làm\tTOTAL\tSố tháng Đánh giá\tXếp loại \tThưởng KPI\tGhi chú\tchủ quản ca đó\tảnh MAX50')
// printMultipleText('序号|STT\t物料号|Mã vật liệu\t物料描述|miêu tả vật liệu\t图号|Số bản vẽ\t库存数量|số lượng\t库存日期|Ngày hàng về\t类型|Phân loại hàng\t备注|Ghi chú\t领料数量|Số lượng lĩnh liệu\t领料日期|Ngày lĩnh liệu\t照片数量|Số lượng hình ảnh\t图片描述|Hình ảnh\t附件|Đính kèm', '|')
// printMultipleText(
//     '"Thời gian yêu cầu\n' +
//     '申请日期\tNgười đăng ký\n' +
//     '申请人"\t"Người đăng ký\n' +
//     '访客姓名"\t"SDT\n' +
//     '手机号"\t"Đơn vị/ Tổ chức\n' +
//     '公司名称"\t"Mục đích ra vào\n' +
//     '来访内容"\t"Dự kiến thời gian đến\n' +
//     '预计来访时间"\t"Dự kiến thời gian kết thúc\n' +
//     '预计访问结束时间"\t"Người liên hệ\n' +
//     '联系人"\t"Bộ phận tiếp đón\n' +
//     '来访部门"\t"Lãnh đạo phê duyệt\n' +
//     '批准人"\t"Hình ảnh vào\n' +
//     '证件及本人照片（5张）"\t"Ghi chú\n' +
//     '备注 "\t"Hình ảnh ra\n' +
//     '出厂记录（5张）"\t"Thời gian ra ngoài\n' +
//     '访客出厂日期"\t"Mã phiếu\n' +
//     '打印单号"',
//     '\n',
//     true
// )
const initText = (textKey: string = 'CHS|THA'): TextObject => {
  const r: TextObject = {};
  (MULTIPLE_TEXT_LIST[textKey] || []).forEach((t: Text) => {
    r[t.value] = t
  })
  return r
}
const initLabel = (textKey: string = 'CHS|THA'): Label => {
  const r: Label = {};
  (MULTIPLE_TEXT_LIST[textKey] || []).forEach((t: Text) => {
    r[t.value] = t.label
  })
  return r
}

export interface StoreType extends CommonStoreType {
  collapse: boolean
  tagList: Tag[]
  pathList: string[]
  roleList: string[]
  roleCodeList: string[]
  menuTreeList: MenuTree[],
  textKey: string,
  text: TextObject,
  label: Label,
  user: any,
  disqualificationOrderId: string,
}

const textKeyConst = getStorage(TEXT_KEY) || 'CHS|THA'

// noinspection JSUnusedGlobalSymbols
export const storeOptions: StoreOptions<StoreType> = {
  // @ts-ignore
  state: Object.assign(
      {
        collapse: window.innerWidth < 1792,
        pathList: location.pathname.split('/').filter(temp => temp),
        roleList: JSON.parse(getStorage(ROLE_LIST_KEY) || '[]'),
        roleCodeList: JSON.parse(getStorage(ROLE_LIST_KEY) || '[]'),
        tagList: new Array<Tag>(),
        menuTreeList: getMenuTreeList(),
        textKey: textKeyConst,
        text: initText(textKeyConst),
        label: initLabel(textKeyConst),
        user: JSON.parse(getStorage(USER_KEY) || '{}'),
      },
      commonStoreOptions.state || {},
  ),
  mutations: Object.assign(
      {
        deleteTag(state: StoreType, {index}: { index: number }) {
          state.tagList.splice(index, 1)
        },
        setPathList(state: StoreType, pathList: string[]) {
          replaceAll(state.pathList, pathList)
        },
        addTag(state: StoreType, data: Tag) {
          state.tagList.push(data)
        },
        clearTagList(state: StoreType) {
          clearAll(state.tagList)
        },
        closeOtherTagList(state: StoreType, data: Tag[]) {
          replaceAll(state.tagList, data)
        },
        setCollapse(state: StoreType, data: boolean) {
          state.collapse = data
        },
        setMenuTreeList(state: StoreType, menuTreeList: MenuTree[]) {
          replaceAll(state.menuTreeList, menuTreeList)
          setMenuTreeList(menuTreeList)
        },
        clearMenuTreeList(state: StoreType) {
          clearAll(state.menuTreeList)
          setMenuTreeList([])
        },
        setRoleList(state: StoreType, roleList: string[]) {
          replaceAll(state.roleList, roleList)
          setStorage(ROLE_LIST_KEY, JSON.stringify(roleList))
        },
        setRoleCodeList(state: StoreType, roleCodeList: string[]) {
          replaceAll(state.roleCodeList, roleCodeList)
          setStorage(ROLE_LIST_KEY, JSON.stringify(roleCodeList))
        },
        setUser(state: StoreType, user: any) {
          state.user = user
          setStorage(USER_KEY, JSON.stringify(user))
        },
        setTextKey(state: StoreType, textKey: string) {
          state.textKey = textKey
          setStorage(TEXT_KEY, textKey)
          state.text = initText(textKey)
          state.label = initLabel(textKey)
        },
        setDisqualificationOrderId(state: StoreType, disqualificationOrderId: string) {
          state.disqualificationOrderId = disqualificationOrderId
        },
      },
      commonStoreOptions.mutations || {},
  ),
  actions: Object.assign(
      {},
      commonStoreOptions.actions || {},
  ),
  modules: Object.assign(
      {},
      commonStoreOptions.modules || {},
  ),
}


