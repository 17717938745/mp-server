package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * InventoryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Accessors(chain = true)
public class InventoryRequest extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010702L;
    /**
     * 库存id
     */
    private String inventoryId;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDescription;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 库存数量
     */
    private BigDecimal inventoryCount;
    /**
     * 库存日期
     */
    private String inventoryDate;
    /**
     * 类型
     */
    private String type;
    /**
     * 备注
     */
    private String description;
    /**
     * 领料数量
     */
    private BigDecimal materialCount;
    /**
     * 领料日期
     */
    private String materialDate;
    private String startMaterialDate;
    private String endMaterialDate;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * 附件列表
     */
    private List<FileModel> attachmentList = new ArrayList<>();
    /**
     * 库存数量类型，0=库存数量=领料数量
     */
    private Integer inventoryCountType;
}
