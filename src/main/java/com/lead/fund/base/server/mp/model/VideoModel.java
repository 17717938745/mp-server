package com.lead.fund.base.server.mp.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VideoModel
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoModel implements Serializable {

    private static final long serialVersionUID = -4380897635428113952L;
    /**
     * 压缩链接
     */
    private String videoCompressUrl;
    private String fullVideoCompressUrl;
    /**
     * 链接
     */
    private String videoUrl;
    private String fullVideoUrl;
}
