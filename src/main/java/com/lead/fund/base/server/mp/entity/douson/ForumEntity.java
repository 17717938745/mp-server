package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_FORUM")
@NoArgsConstructor
@ClassFunction("论坛")
@Accessors(chain = true)
public class ForumEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -8361023609419505009L;
    /**
     * 用户ID
     */
    @FieldRemark(value = "用户ID")
    @Size(max = 64, message = "用户ID长度不合法")
    private String userId;
    /**
     * H5的ID
     */
    @FieldRemark(value = "H5的ID")
    @Size(max = 64, message = "H5的ID长度不合法")
    private String h5Id;
    /**
     * 点赞
     */
    @FieldRemark(value = "点赞")
    @Size(max = 16, message = "点赞长度不合法")
    private BigDecimal thumbsUp;
    /**
     * 评论
     */
    @FieldRemark(value = "评论")
    @Size(max = 16, message = "评论长度不合法")
    private BigDecimal commentary;
}
