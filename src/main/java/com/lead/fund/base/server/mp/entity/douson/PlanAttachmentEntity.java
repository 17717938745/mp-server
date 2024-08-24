package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * PlanAttachmentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_PLAN_ATTACHMENT")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("精益持续改善附件")
@Accessors(chain = true)
public class PlanAttachmentEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = -2336648923290095598L;
    /**
     * 精益持续改善ID
     */
    @FieldRemark(value = "精益持续改善ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "精益持续改善ID长度不合法")
    private String planId;
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
     * 链接
     */
    private String url;
}
