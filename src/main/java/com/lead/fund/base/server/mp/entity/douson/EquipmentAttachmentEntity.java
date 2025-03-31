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
 * EquipmentAttachmentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_EQUIPMENT_ATTACHMENT")
@NoArgsConstructor
@ClassFunction("事故记录")
@Accessors(chain = true)
public class EquipmentAttachmentEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = -3743197093654400698L;
    /**
     * 设备编号
     */
    @FieldRemark(value = "设备编号", unique = true, constraintGroup = "0")
    @Size(max = 64, message = "设备编号")
    private String equipmentId;
    /**
     * 附件分类，0-问题照片，1-问题附件，refuel-设备加油照片
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
