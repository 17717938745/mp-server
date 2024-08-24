package com.lead.fund.base.server.mp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * FileModel
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
public class FileModel implements Serializable {

    private static final long serialVersionUID = -5439616642178609575L;
    /**
     * 文件ID
     */
    private String fileId;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 链接
     */
    private String url;
    private String fullUrl;
}
