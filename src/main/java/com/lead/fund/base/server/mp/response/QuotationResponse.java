package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * QuotationRequest
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
public class QuotationResponse extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 报价ID
     */
    private String quotationId;
    /**
     * 报价明细ID
     */
    private String quotationItemId;
    /**
     * 客户
     */
    @FieldRemark(value = "客户")
    @Size(max = 64, message = "客户长度不合法")
    private String customer;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 64, message = "图号长度不合法")
    private String designNumber;
    private BigDecimal designNumberCount;
    private List<PhotoImgModel> designNumberList = new ArrayList<>();
    /**
     * 名称
     */
    @FieldRemark(value = "名称")
    @Size(max = 128, message = "名称长度不合法")
    private String name;
    /**
     * 材质
     */
    @FieldRemark(value = "材质")
    @Size(max = 32, message = "材质长度不合法")
    private String materialQuality;
    /**
     * 数量
     */
    @FieldRemark(value = "数量")
    private BigDecimal count;
    /**
     * 报价日期
     */
    @FieldRemark(value = "报价日期")
    @Size(max = 32, message = "报价日期长度不合法")
    private String quotationDate;
    /**
     * 加工工序
     */
    @FieldRemark(value = "加工工序")
    @Size(max = 32, message = "加工工序长度不合法")
    private String processProcedure;
    private String processProcedureFormat;
    /**
     * 加工设备
     */
    @FieldRemark(value = "加工设备")
    @Size(max = 32, message = "加工设备长度不合法")
    private String processDevice;
    private String processDeviceFormat;
    /**
     * 加工单价
     */
    @FieldRemark(value = "加工单价")
    private BigDecimal processUnitPrice;
    /**
     * 加工时间
     */
    @FieldRemark(value = "加工时间")
    private BigDecimal processTime;
    /**
     * 汇总价格/元
     */
    @FieldRemark(value = "汇总价格/元")
    private BigDecimal summaryPrice;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private BigDecimal remarks;
    /**
     * 报价人
     */
    @FieldRemark(value = "报价人")
    @Size(max = 64, message = "报价人长度不合法")
    private String bidder;
    private String bidderFormat;
    /**
     * 是否成功接单
     */
    @FieldRemark(value = "是否成功接单")
    private Boolean acceptOrder;
    private String acceptOrderFormat;
}
