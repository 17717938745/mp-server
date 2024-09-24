package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpH5Request
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpH5Request implements Serializable {

    private static final long serialVersionUID = -5102590282487063007L;
    /**
     * 页面ID
     */
    private String h5Id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
}
