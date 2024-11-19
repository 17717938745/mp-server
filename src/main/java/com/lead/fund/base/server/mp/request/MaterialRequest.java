package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.database.entity.AbstractAdmin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * MaterialRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Accessors(chain = true)
public class MaterialRequest extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001063L;
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
    private Integer remainCountType;
    /**
     * 排产日期
     */
    private String arrangeProductionDate;
    /**
     * 领料单号
     */
    private String materialOrderNo;
    /**
     * 报检单号
     */
    private String checkOrderNo;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startPromiseDoneDate;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endPromiseDoneDate;
    private Integer surplusCountType;
    /**
     * 是否精确匹配
     */
    private Boolean accurateMatch;
    /**
     * 0-不排，1-正序，2-倒叙
     */
    private int orderByPromiseDoneDate;
}
