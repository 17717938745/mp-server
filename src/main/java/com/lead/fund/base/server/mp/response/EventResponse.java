package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.model.VideoModel;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * EventResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EventResponse implements Serializable {

    private static final long serialVersionUID = 5658438762619500630L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 序号
     */
    private String serialNo;
    /**
     * 事故ID
     */
    private String eventId;
    /**
     * 活动时间
     */
    private String reportDate;
    /**
     * 当事人
     */
    private String userId;
    private String userIdFormat;
    /**
     * 上级领导
     */
    private String directLeader;
    private String directLeaderFormat;
    /**
     * 问题描述
     */
    private String accidentDescribe;
    /**
     * 原因
     */
    private String reason;
    private List<String> reasonList;
    private String reasonFormat;
    /**
     * 解决方法
     */
    private String solve;
    /**
     * 改善后的证据描述
     */
    private String improveDescribe;
    /**
     * 奖惩意见
     */
    private String opinion;
    /**
     * 是否有效
     */
    private Boolean valid;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * 文件列表
     */
    private List<FileModel> fileList = new ArrayList<>();
    /**
     * 改善后的图片列表
     */
    private List<PhotoImgModel> improvePhotoList = new ArrayList<>();
    /**
     * 改善后的文件列表
     */
    private List<FileModel> improveFileList = new ArrayList<>();
}
