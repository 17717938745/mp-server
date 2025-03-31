package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ExamineResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@Accessors(chain = true)
public class ExamineResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096010702L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 检验记录id
     */
    private String examineId;
    /**
     * 报检单号
     */
    private String checkOrderNo;
    /**
     * 报检单合计数量
     */
    private BigDecimal orderTotalQuantity;

    /**
     * 标识/硬度备注
     */
    private String identificationHardnessRemark;
    /**
     * NDE/尺寸备注
     */
    private String ndeDimensionRemark;
    /**
     * 检验完成数量
     */
    private BigDecimal inspectionCompletedQuantity;
    /**
     * 客户简称
     */
    private String customerShortName;
    /**
     * 销售订单
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
    private BigDecimal orderQuantity;
    /**
     * 承诺完成日期
     */
    private String promiseDoneDate;
    /**
     * 备注
     */
    private String description;
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
     * 标识人员
     */
    private String identificationPerson;
    private String identificationPersonFormat;
    /**
     * 标识日期
     */
    private String identificationDate;
    /**
     * 检验人员
     */
    private String inspectionPerson;
    private String inspectionPersonFormat;
    /**
     * 检验完成日期
     */
    private String inspectionCompletedDate;
    /**
     * 标识
     */
    private Integer identificationCount;
    private List<PhotoImgModel> identificationList = new ArrayList<>();
    /**
     * 硬度
     */
    private Integer hardnessCount;
    private List<FileModel> hardnessList = new ArrayList<>();
    /**
     * NDE
     */
    private Integer ndeCount;
    private List<PhotoImgModel> ndeList = new ArrayList<>();
    /**
     * 尺寸
     */
    private Integer dimensionCount;
    private List<PhotoImgModel> dimensionList = new ArrayList<>();
}
