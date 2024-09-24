package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ForumRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726010L;
    /**
     * 论坛ID
     */
    private String forumId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * H5的ID
     */
    private String h5Id;
    /**
     * 点赞
     */
    private BigDecimal thumbsUp;
    /**
     * 评论
     */
    private BigDecimal commentary;

}
