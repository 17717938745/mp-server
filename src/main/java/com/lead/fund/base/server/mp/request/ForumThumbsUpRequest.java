package com.lead.fund.base.server.mp.request;

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
public class ForumThumbsUpRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726010L;
    /**
     * 论坛ID
     */
    private String forumId;
    /**
     * 点赞
     */
    private Boolean thumbsUp;

}
