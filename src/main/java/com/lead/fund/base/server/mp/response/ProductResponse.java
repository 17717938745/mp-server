package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

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
public class ProductResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726177L;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 修改人
     */
    private String modifier;
    private String modifierFormat;
    /**
     * 操作人
     */
    private String usernameFormat;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 报告时间
     */
    private String reportDate;
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
     * 调试设备
     */
    private String testDevice;
    private String testDeviceFormat;
    /**
     * 设备单价
     */
    private BigDecimal deviceUnitPrice;
    /**
     * 开始设备运行时间（小时）
     */
    private Integer deviceRunningStartHour;
    /**
     * 开始设备运行时间（分钟）
     */
    private Integer deviceRunningStartMinute;
    /**
     * 加工工序
     */
    private String processProcedure;
    private String processProcedureFormat;
    /**
     * 产品数量
     */
    private BigDecimal orderCount;
    /**
     * 程序号
     */
    private String programNumber;
    /**
     * 调试时间/分钟
     */
    private Integer debugMinute;
    /**
     * 装夹时间/分钟
     */
    private Integer clampingMinute;
    /**
     * 辅助时间/分钟
     */
    private Integer assistMinute;
    /**
     * 程式运行时间/分钟
     */
    private Integer runningMinute;
    /**
     * 产品标准工时/分钟
     */
    private BigDecimal productStandMinute;
    /**
     * 8小时加工件数
     */
    private BigDecimal countHour8;
    /**
     * 12小时加工件数
     */
    private BigDecimal countHour12;
    /**
     * 备注
     */
    private String remark;
    /**
     * 小程序id
     */
    private String openId;
    /**
     * 用户名
     */
    private String openIdFormat;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
}
