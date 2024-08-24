package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DisqualificationOrderRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DisqualificationOrderRequest implements Serializable {

    private static final long serialVersionUID = 5945041111096726179L;
    /**
     * 开始时间@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateTime;
    /**
     * 不合格单号ID
     */
    private String disqualificationOrderId;
    /**
     * 创建人用户id
     */
    private String creator;
    /**
     * 不合格单号
     */
    private String disqualificationOrderNo;
    /**
     * 不合格单号
     */
    private String disqualificationOrder;
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
    /**
     * 责任人员
     */
    private String dutyPerson;
    /**
     * 质量处理意见
     */
    private String qualityDealOpinion;
    /**
     * 技术处理意见
     */
    private String skillDealOpinion;
    /**
     * 扣款
     */
    private BigDecimal fineAmount;
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
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
}
