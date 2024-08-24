package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * BoxFlagRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BoxFlagRequest implements Serializable {

    private static final long serialVersionUID = -8191529848485941186L;
    /**
     * 装箱标识卡ID
     */
    private String boxFlagId;
    /**
     * 创建人用户id
     */
    private String creator;
    /**
     * 日期
     */
    private String date;
    /**
     * 客户简称
     */
    private String customerShortName;
    /**
     * 采购订单编号
     */
    private String purchaseOrderNo;
    /**
     * PO项目
     */
    private String poProject;
    /**
     * 销售订单
     */
    private String saleOrderNo;
    /**
     * 订单项目
     */
    private String orderProject;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 每箱数量
     */
    private Integer eachBoxCount;
    /**
     * 箱号
     */
    private Integer boxNumber;
    /**
     * 序号
     */
    private String serialNo;
    /**
     * 序号列表
     */
    private List<String> serialNoList;
    /**
     * 长
     */
    private BigDecimal length;
    /**
     * 宽
     */
    private BigDecimal width;
    /**
     * 高
     */
    private BigDecimal height;
    /**
     * 单件重量
     */
    private BigDecimal unitWeight;
    /**
     * 每箱重量
     */
    private BigDecimal eachBoxWeight;
    /**
     * 单号
     */
    private String orderNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 发送数量
     */
    private BigDecimal sendCount;
    /**
     * 发送日期
     */
    private String sendDate;
    /**
     * 是否已经发行
     */
    private Boolean alreadySend;
}
