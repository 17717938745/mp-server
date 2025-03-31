package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ParamResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParamResponse implements Serializable {

    private static final long serialVersionUID = -5102590282487063037L;
    /**
     * 加工工序
     */
    private List<ParamConfigResponse> processProcedureList;
    /**
     * 调试设备
     */
    private List<ParamConfigResponse> testDeviceList;
    /**
     * 班次
     */
    private List<ParamConfigResponse> scheduleList;
    /**
     * 事故类型
     */
    private List<ParamConfigResponse> accidentTypeList;
    /**
     * 工作时间（分钟）
     */
    private List<ParamConfigResponse> workMinuteList;
    /**
     * 加工工序列表
     */
    private List<ParamConfigResponse> processTypeList;
    /**
     * 停机内容1
     */
    private List<ParamConfigResponse> stopWorkingContent1List;
    /**
     * 停机内容2
     */
    private List<ParamConfigResponse> stopWorkingContent2List;
    /**
     * 停机内容3
     */
    private List<ParamConfigResponse> stopWorkingContent3List;
    /**
     * 客户简称
     */
    private List<ParamConfigResponse> customerShortNameList;
    /**
     * 流程
     */
    private List<ParamConfigResponse> processList;
    /**
     * 检验节点
     */
    private List<ParamConfigResponse> checkPointList;
    /**
     * 质量处理意见
     */
    private List<ParamConfigResponse> qualityDealOpinionList;
    /**
     * 技术处理意见
     */
    private List<ParamConfigResponse> skillDealOpinionList;
    /**
     * 缺陷类型
     */
    private List<ParamConfigResponse> defectTypeList;
    /**
     * 部门
     */
    private List<ParamConfigResponse> departmentList;
    /**
     * 精益类型
     */
    private List<ParamConfigResponse> optimizeTypeList;
    /**
     * 职位
     */
    private List<ParamConfigResponse> professionList;
    /**
     * 中越文名称
     */
    private List<ParamConfigResponse> chineseVietnamNameList;
    /**
     * 校准单位
     */
    private List<ParamConfigResponse> calibrationPeriodList;
    /**
     * 库位
     */
    private List<ParamConfigResponse> storageList;
    /**
     * 状态
     */
    private List<ParamConfigResponse> deviceCheckLedgerStateList;
    /**
     * 人员属性
     */
    private List<ParamConfigResponse> userPropertyList;
    /**
     * 设备名称
     */
    private List<ParamConfigResponse> computerNameList;
    /**
     * 位置
     */
    private List<ParamConfigResponse> companyPositionList;
    /**
     * 状态
     */
    private List<ParamConfigResponse> computerStateList;
    /**
     * 原因分析
     */
    private List<ParamConfigResponse> eventReasonList;
    /**
     * 设备编号
     */
    private List<ParamConfigResponse> equipmentNoList;
    /**
     * 位置
     */
    private List<ParamConfigResponse> equipmentPositionList;
    /**
     * 故障原因
     */
    private List<ParamConfigResponse> brokenReasonList;
    /**
     * 维修类型
     */
    private List<ParamConfigResponse> repairTypeList;
    /**
     * 改善原因
     */
    private List<ParamConfigResponse> improveReasonList;
    /**
     * 原因分析
     */
    private List<ParamConfigResponse> qualityReasonList;
    /**
     * 原因分析
     */
    private List<ParamConfigResponse> crashReasonList;
    /**
     * 原因分析
     */
    private List<ParamConfigResponse> troubleReasonList;
    /**
     * 请假类型
     */
    private List<ParamConfigResponse> vocationTypeList;
    /**
     * 计划外库存类型
     */
    private List<ParamConfigResponse> inventoryOutOfPlanTypeList;
    /**
     * 负责小组
     */
    private List<ParamConfigResponse> responsibleTeamList;
    /**
     * API设备
     */
    private List<ParamConfigResponse> apiDeviceList;
}
