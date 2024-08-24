package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.model.VideoModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AccidentResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AccidentResponse implements Serializable {

    private static final long serialVersionUID = -757227324009571443L;
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
     * 用户名
     */
    private String username;
    /**
     * 事故类型
     */
    private String accidentType;
    private String accidentTypeFormat;
    /**
     * 责任人
     */
    private String dutyPerson;
    private String dutyPersonFormat;
    /**
     * 班组长
     */
    private String groupLeader;
    private String groupLeaderFormat;
    /**
     * 主管
     */
    private String chargePerson;
    private String chargePersonFormat;
    /**
     * 经理
     */
    private String manager;
    private String managerFormat;
    /**
     * 设备图片列表
     */
    private List<PhotoImgModel> devicePhotoList = new ArrayList<>();
    /**
     * 设备描述
     */
    private String deviceDescribe;
    /**
     * 图号图片列表
     */
    private List<PhotoImgModel> designNumberPhotoList = new ArrayList<>();
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
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * 图片列表
     */
    private List<VideoModel> videoList = new ArrayList<>();
    /**
     * 伤害况图片列表
     */
    private List<PhotoImgModel> damagePhotoList = new ArrayList<>();
    /**
     * 伤害描述
     */
    private String damageDescribe;
    /**
     * 财产损失情况图片列表
     */
    private List<PhotoImgModel> propertyLossPhotoList = new ArrayList<>();
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
    private String fineAmount1Format;
    /**
     * 责任人2
     */
    private String dutyPerson2;
    /**
     * 罚款金额2
     */
    private BigDecimal fineAmount2;
    private String fineAmount2Format;
    /**
     * 责任人3
     */
    private String dutyPerson3;
    /**
     * 罚款金额3
     */
    private BigDecimal fineAmount3;
    private String fineAmount3Format;
    /**
     * 改善后的证据况图片列表
     */
    private List<PhotoImgModel> improveEvidencePhotoList = new ArrayList<>();
    /**
     * 改善后的证据描述
     */
    private String improveEvidenceDescribe;
    /**
     * 是否有效
     */
    private Boolean valid;
}
