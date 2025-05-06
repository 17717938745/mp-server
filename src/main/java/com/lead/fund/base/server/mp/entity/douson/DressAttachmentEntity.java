package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * DressAttachmentEntity
 * <p>
 * 订单工装管理附件
 *
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_DRESS_ATTACHMENT")
@NoArgsConstructor
@ClassFunction("工装管理附件")
@Accessors(chain = true)
public class DressAttachmentEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010702L;
    /**
     * 工装管理id
     */
    @FieldRemark(value = "工装管理id", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "工装管理id长度不合法")
    private String dressId;

    /**
     * 附件分类，0-照片，1-附件
     */
    @FieldRemark(value = "附件分类", unique = true, constraintGroup = "0")
    @Size(max = 64, message = "附件分类长度不合法")
    private String attachmentCategory;
    /**
     * 附件类型，designNumber：图号（挂PDF）；storePicture-入库图片；
     */
    @FieldRemark(value = "附件类型", unique = true, constraintGroup = "0")
    @Size(max = 32, message = "附件类型长度不合法")
    private String attachmentType;
    /**
     * 链接
     */
    @FieldRemark(value = "链接", unique = true, constraintGroup = "0")
    private String url;
    /**
     * 文件ID
     */
    @FieldRemark(value = "文件ID", indexType = "UNIQUE", indexGroup = "0")
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