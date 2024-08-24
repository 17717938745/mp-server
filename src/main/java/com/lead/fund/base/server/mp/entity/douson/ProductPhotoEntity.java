package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * IndustryProductPhotoEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_PRODUCT_PHOTO")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("零件")
@Accessors(chain = true)
public class ProductPhotoEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 5945041243096726178L;
    /**
     * 产品id
     */
    @FieldRemark(value = "产品id", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "产品id长度不合法")
    private String productId;
    /**
     * 照片链接
     */
    @FieldRemark(value = "照片链接", indexType = "UNIQUE", indexGroup = "0")
    private String photoUrl;
    /**
     * 照片压缩链接
     */
    @FieldRemark(value = "照片压缩链接")
    private String photoCompressUrl;
}
