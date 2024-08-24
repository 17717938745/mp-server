package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
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
public class ProductRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726177L;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reportDate;
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
    /**
     * 加工工序
     */
    private String processProcedure;
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
     * 备注
     */
    private String remark;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
}
