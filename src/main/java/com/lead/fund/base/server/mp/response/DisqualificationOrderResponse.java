package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.util.ArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DisqualificationOrderResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DisqualificationOrderResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041122096726179L;
    /**
     * 不合格单号ID
     */
    private String disqualificationOrderId;
    /**
     * 创建人用户id
     */
    private String creator;
    /**
     * 创建人用户名
     */
    private String creatorFormat;
    /**
     * 不合格单号
     */
    private String disqualificationOrderNo;
    private String disqualificationOrderNoFormat;
    /**
     * 单号
     */
    private String orderNo;
    /**
     * 项次
     */
    private String projectSequence;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 流程
     */
    private String process;
    private String processFormat;
    /**
     * 不合格内容
     */
    private String disqualificationContent;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 检验节点
     */
    private String checkPoint;
    private String checkPointFormat;
    /**
     * 责任人员
     */
    private String dutyPerson;
    private String dutyPersonFormat;
    /**
     * 质量处理意见
     */
    private String qualityDealOpinion;
    private String qualityDealOpinionFormat;
    /**
     * 技术处理意见
     */
    private String skillDealOpinion;
    private String skillDealOpinionFormat;
    /**
     * 扣款
     */
    private BigDecimal fineAmount;
    private String fineAmountFormat;
    /**
     * 备注
     */
    private String remark;
    /**
     * 炉号
     */
    private String stoveNo;
    /**
     * 热批号
     */
    private String hotBatchNo;
    /**
     * 序列号
     */
    private String serialNo;
    /**
     * 缺陷类型
     */
    private String defectType;
    private String defectTypeFormat;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
}
