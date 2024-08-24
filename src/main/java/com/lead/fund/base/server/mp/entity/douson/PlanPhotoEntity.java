package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * PlanPhotoEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_PLAN_PHOTO")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("精益持续改善")
@Accessors(chain = true)
public class PlanPhotoEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = -2336648923290095598L;
    /**
     * 精益持续改善ID
     */
    @FieldRemark(value = "精益持续改善ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "精益持续改善ID长度不合法")
    private String planId;
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
    /**
     * 照片Type，0-改善前，1-改善后
     */
    @FieldRemark(value = "照片压缩链接")
    private Integer photoType;

}
