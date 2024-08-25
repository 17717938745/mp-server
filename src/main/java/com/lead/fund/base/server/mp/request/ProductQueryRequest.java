package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * IndustryProductQueryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 19:28
 */
@Data
@Accessors(chain = true)
@ToString
public class ProductQueryRequest implements Serializable {

    private static final long serialVersionUID = -7096126141894413212L;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 调试设备
     */
    private String testDevice;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 订单ID列表
     */
    private List<String> orderIdList;
    /**
     * 小程序id
     */
    private String openId;
    private List<String> openIdList;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startReportDate;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endReportDate;
    /**
     * 加工工序
     */
    private String processProcedure;
    /**
     * 订单编号
     */
    private String orderNo;
}
