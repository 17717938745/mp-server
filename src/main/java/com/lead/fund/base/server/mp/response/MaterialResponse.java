package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * MaterialResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Accessors(chain = true)
public class MaterialResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001064L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 创建人
     */
    private String creatorFormat;
    /**
     * 生产工单ID
     */
    private String materialId;
    /**
     * 生产工单明细ID
     */
    private String materialDetailId;
    /**
     * 客户简称
     */
    private String customerShortName;
    /**
     * 客户订单号
     */
    private String customerOrderNo;
    /**
     * 客户项次号
     */
    private String customerProjectSequence;
    /**
     * 销售订单号
     */
    private String saleOrderNo;
    /**
     * 订单项目号
     */
    private String orderProjectNo;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 加工物料描述
     */
    private String improveMaterialDescribe;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 订单数量
     */
    private BigDecimal orderCount;
    /**
     * 投产日期
     */
    private String productionDate;
    /**
     * 承诺交期
     */
    private String promiseDoneDate;
    /**
     * 毛坯物料号
     */
    private String blankMaterialNo;
    /**
     * 毛坯物料描述
     */
    private String blankMaterialDescribe;
    /**
     * 毛坯图号
     */
    private String roughcastDesignNumber;
    /**
     * 领料数量
     */
    private BigDecimal materialCount;
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
     * 欠交数量
     */
    private BigDecimal surplusCount;
    /**
     * NDE
     */
    private String nde;
    /**
     * 装配
     */
    private String assemble;
    /**
     * 试压
     */
    private String testPress;
    /**
     * 表面处理
     */
    private String surfaceTreatment;
    /**
     * 负责单位
     */
    private String chargeCompany;
    /**
     * 备注
     */
    private String description;
    /**
     * 排产数量
     */
    private BigDecimal productionCount;
    /**
     * 领料数量-排产数量
     */
    private BigDecimal remainCount;
    /**
     * 排产日期
     */
    private String arrangeProductionDate;
    /**
     * 领料单号
     */
    private String materialOrderNo;
    private BigDecimal materialPrintCount;
    private String materialOrderNoFormat;
    /**
     * 报检单号
     */
    private String checkOrderNo;
    private BigDecimal checkPrintCount;
    private String checkOrderNoFormat;
    /**
     * 是否生成计划
     */
    private Boolean generateTask;
    private String generateTaskFormat;
    /**
     * 是否生成订单检验记录
     */
    private Boolean generateExamine;
    private String generateExamineFormat;
    /**
     * 是否流转文件
     */
    private Boolean circulatedDocument;
    private String circulatedDocumentFormat;
}
