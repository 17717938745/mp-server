package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * OrderQueryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 19:28
 */
@Data
@Accessors(chain = true)
@ToString
public class OrderQueryRequest implements Serializable {

    private static final long serialVersionUID = -3608048532112976347L;
    /**
     * 订单ID
     */
    private String orderId;
    private List<String> orderIdList;
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
     * 图号
     */
    private String designNumber;
}
