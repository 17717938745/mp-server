package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * DeviceQueryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 19:28
 */
@Data
@Accessors(chain = true)
@ToString
public class DeviceQueryRequest implements Serializable {

    private static final long serialVersionUID = 8626379103554793429L;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
}
