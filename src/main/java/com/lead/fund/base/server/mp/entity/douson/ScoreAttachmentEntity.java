package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ScoreAttachmentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_SCORE_ATTACHMENT")
@NoArgsConstructor
@ClassFunction("季度评比报告附件")
@Accessors(chain = true)
public class ScoreAttachmentEntity extends AbstractPrimaryKey {

    @Serial
    private static final long serialVersionUID = 4268114233816227332L;
    /**
     * 季度评比报告ID
     */
    @FieldRemark(value = "季度评比报告ID", unique = true, constraintGroup = "0")
    @Size(max = 64, message = "季度评比报告ID")
    private String scoreId;
    /**
     * 附件分类，photo-阀体照片
     */
    @FieldRemark(value = "附件分类", unique = true, constraintGroup = "0")
    @Size(max = 64, message = "附件分类")
    private String attachmentCategory;
    /**
     * 附件类型，0：照片；1-视频
     */
    @FieldRemark(value = "附件类型", unique = true, constraintGroup = "0")
    @Size(max = 4, message = "附件类型")
    private String attachmentType;
    /**
     * 链接
     */
    @FieldRemark(value = "链接", unique = true, constraintGroup = "0")
    @Size(max = 256, message = "链接")
    private String url;
    /**
     * 文件ID
     */
    @FieldRemark(value = "文件ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "链接")
    private String fileId;
    /**
     * 文件名
     */
    @FieldRemark(value = "文件名")
    private String filename;
    /**
     * 压缩链接
     */
    @FieldRemark(value = "压缩链接")
    private String compressUrl;
}
