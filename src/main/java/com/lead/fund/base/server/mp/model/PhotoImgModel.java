package com.lead.fund.base.server.mp.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * PhotoImgResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class PhotoImgModel implements Serializable {

    private static final long serialVersionUID = -4380897635428113951L;
    /**
     * 照片压缩链接
     */
    private String photoCompressUrl;
    private String fullPhotoCompressUrl;
    /**
     * 拍照
     */
    private String photoUrl;
    private String fullPhotoUrl;
}
