package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.model.VideoModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AccidentRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AccidentRequest implements Serializable {

    private static final long serialVersionUID = -8361023609419505399L;
    /**
     * 事故ID
     */
    private String accidentId;
    /**
     * 活动时间
     */
    private String reportDate;
    /**
     * 操作人
     */
    private String userId;
    /**
     * 事故类型
     */
    private String accidentType;
    /**
     * 责任人
     */
    private String dutyPerson;
    /**
     * 组长
     */
    private String groupLeader;
    /**
     * 主管
     */
    private String chargePerson;
    /**
     * 经理
     */
    private String manager;
    /**
     * 设备图片列表
     */
    private List<PhotoImgModel> devicePhotoList;
    /**
     * 设备描述
     */
    private String deviceDescribe;
    /**
     * 图号图片列表
     */
    private List<PhotoImgModel> designNumberPhotoList;
    /**
     * 图号描述
     */
    private String designNumberDescribe;
    /**
     * 产品重量（KG）
     */
    private BigDecimal productWeight;
    /**
     * 问题描述
     */
    private String accidentDescribe;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
    /**
     * 图片列表
     */
    private List<VideoModel> videoList;
    /**
     * 伤害况图片列表
     */
    private List<PhotoImgModel> damagePhotoList;
    /**
     * 伤害描述
     */
    private String damageDescribe;
    /**
     * 财产损失情况图片列表
     */
    private List<PhotoImgModel> propertyLossPhotoList;
    /**
     * 财产损失描述
     */
    private String propertyLossDescribe;
    /**
     * 人的因素原因
     */
    private String humanFactorReason;
    /**
     * 设备原因
     */
    private String deviceReason;
    /**
     * 材料原因
     */
    private String materialReason;
    /**
     * 工作方法原因
     */
    private String workMethodReason;
    /**
     * 环境原因
     */
    private String environmentReason;
    /**
     * 人的因素解决方法
     */
    private String humanFactorSolve;
    /**
     * 设备解决方法
     */
    private String deviceSolve;
    /**
     * 材料解决方法
     */
    private String materialSolve;
    /**
     * 工作方法解决方法
     */
    private String workMethodSolve;
    /**
     * 环境解决方法
     */
    private String environmentSolve;
    /**
     * 责任人1
     */
    private String dutyPerson1;
    /**
     * 罚款金额1
     */
    private BigDecimal fineAmount1;
    /**
     * 责任人2
     */
    private String dutyPerson2;
    /**
     * 罚款金额2
     */
    private BigDecimal fineAmount2;
    /**
     * 责任人3
     */
    private String dutyPerson3;
    /**
     * 罚款金额3
     */
    private BigDecimal fineAmount3;
    /**
     * 改善后的证据况图片列表
     */
    private List<PhotoImgModel> improveEvidencePhotoList;
    /**
     * 改善后的证据描述
     */
    private String improveEvidenceDescribe;
    /**
     * 是否有效
     */
    private Boolean valid;
}
