package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * EventRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ImproveRequest implements Serializable {

    private static final long serialVersionUID = 224990548044070026L;
    /**
     * 事故ID
     */
    private String improveId;
    /**
     * 活动时间
     */
    private String reportDate;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startReportDate;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endReportDate;
    /**
     * 当事人
     */
    private String userId;
    private List<String> userIdList;
    /**
     * 查询用户ID
     */
    private String queryUserId;
    /**
     * 上级领导
     */
    private String directLeader;
    /**
     * 问题描述
     */
    private String accidentDescribe;
    /**
     * 原因
     */
    private List<String> reasonList = new ArrayList<>();
    private String reason;
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
    /**
     * 维修ID
     */
    private String equipmentId;
    /**
     * 刀具描述
     */
    private String toolDescribe;
}
