package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AccidentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_ACCIDENT_ATTACHMENT")
@NoArgsConstructor
@ClassFunction("事故记录")
@Accessors(chain = true)
public class AccidentAttachmentEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = -8361023609419505388L;
    /**
     * 事故id
     */
    @FieldRemark(value = "事故id")
    @Size(max = 64, message = "事故id")
    private String accidentId;
    /**
     * 附件分类
     */
    @FieldRemark(value = "附件分类")
    @Size(max = 64, message = "附件分类")
    private String attachmentCategory;
    /**
     * 附件类型，0：照片；1-视频
     */
    @FieldRemark(value = "附件类型")
    @Size(max = 32, message = "附件类型")
    private String attachmentType;
    /**
     * 链接
     */
    @FieldRemark(value = "链接")
    private String url;
    /**
     * 压缩链接
     */
    @FieldRemark(value = "压缩链接")
    private String compressUrl;
}
