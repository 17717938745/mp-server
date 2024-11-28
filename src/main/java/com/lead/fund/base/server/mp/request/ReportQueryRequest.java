package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * IndustryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ReportQueryRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726179L;
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
     * 日报id
     */
    private String reportId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 活动时间 @DateTimeFormat(pattern = "yyyy-MM-dd")
     */
    private String reportDate;
    /**
     * 操作人
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 订单
     */
    private String orderNo;
    /**
     * 项次
     */
    private String projectSequence;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 订单ID
     */
    private List<String> orderIdList;
    /**
     * 产品id
     */
    private List<String> productIdList;
    /**
     * 调试设备
     */
    private String testDevice;
    /**
     * 加工类型
     */
    private String processType;
    /**
     * 加工工序
     */
    private String processProcedure;
    /**
     * 是否生效
     */
    private Boolean valid;
    private Integer surplusCountType;
}
