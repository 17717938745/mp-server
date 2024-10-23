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
 * TemplatePhotoEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_TEMPLATE_PHOTO")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("副本供应商刀具图片")
@Accessors(chain = true)
public class TemplatePhotoEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 5945041243096726053L;
    /**
     * 供应商刀具模板ID
     */
    @FieldRemark(value = "供应商刀具模板ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "供应商刀具模板ID")
    private String templateId;
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
     * 照片Type，0-借出前，1-归还后
     */
    @FieldRemark(value = "照片压缩链接")
    private Integer photoType;
}
