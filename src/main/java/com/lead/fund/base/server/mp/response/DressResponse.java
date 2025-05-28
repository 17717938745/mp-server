package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * DressRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@Accessors(chain = true)
public class DressResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096010702L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 检验记录id
     */
    private String dressId;
    /**
     * 工装类型
     */
    private String workDressType;
    private String workDressTypeFormat;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 描述
     */
    private String remark;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 申请数量
     */
    private BigDecimal applyCount;
    /**
     * 申请日期
     */
    private String applyDate;
    /**
     * 库存位置
     */
    private String storePosition;
    private String storePositionFormat;
    /**
     * 验收
     */
    private String checkAcceptUser;
    private String checkAcceptUserFormat;
    /**
     * 备注（注明为哪个订单服务）
     */
    private String descriptionOfOrder;
    /**
     * 入库数量
     */
    private BigDecimal storeCount;
    /**
     * 入库日期备注
     */
    private String storeDateDescription;
    /**
     * 图号（挂PDF）
     */
    private Integer designNumberCount;
    private List<FileModel> designNumberList = new ArrayList<>();
    /**
     * 入库图片
     */
    private Integer storePictureCount;
    private List<PhotoImgModel> storePictureList = new ArrayList<>();
}
