package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpImgResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpImgResponse implements Serializable {

    private static final long serialVersionUID = -5102590282487063037L;
    /**
     * 链接
     */
    private String url;
    private String fullUrl;
    /**
     * 压缩文件链接
     */
    private String compressUrl;
    private String fullCompressUrl;
}
