package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * OrderRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-11 10:58
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest implements Serializable {

    private static final long serialVersionUID = -1302050664424581999L;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 订单
     */
    private String orderNo;
    /**
     * 项次
     */
    private String projectSequence;
    /**
     * 调试设备
     */
    private String testDevice;
    /**
     * 加工工序
     */
    private String processProcedure;
    /**
     * 产品数量
     */
    private BigDecimal orderCount;
    /**
     * 排序
     */
    private Integer sorter;
}