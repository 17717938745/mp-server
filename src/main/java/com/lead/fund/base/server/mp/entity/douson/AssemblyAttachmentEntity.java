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
 * AssemblyAttachmentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_ASSEMBLY_ATTACHMENT")
@NoArgsConstructor
@ClassFunction("整机附件")
@Accessors(chain = true)
public class AssemblyAttachmentEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = -2958798731405959801L;
    /**
     * 整机ID
     */
    @FieldRemark(value = "整机ID", unique = true, constraintGroup = "0")
    @Size(max = 64, message = "整机ID")
    private String assemblyId;
    /**
     * 附件分类，valveBody-阀体照片，valveCover-阀盖/尾盖照片，gate-闸板照片，valveSeat-阀座/阀瓣照片，valveStem-阀杆/尾杆照片，pressureTest-整机和驱动器试压照片，oilInjection-注油照片
     */
    @FieldRemark(value = "附件分类", unique = true, constraintGroup = "0")
    @Size(max = 64, message = "附件分类")
    private String attachmentCategory;
    /**
     * 附件类型，0：照片；1-视频
     */
    @FieldRemark(value = "附件类型", unique = true, constraintGroup = "0")
    @Size(max = 32, message = "附件类型")
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
