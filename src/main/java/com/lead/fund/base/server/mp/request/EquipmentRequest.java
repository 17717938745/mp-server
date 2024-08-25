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
 * EquipmentRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EquipmentRequest implements Serializable {

    private static final long serialVersionUID = 224990548044070026L;
    /**
     * 设备ID
     */
    private String equipmentId;
    /**
     * 设备编号
     */
    private String equipmentNo;
    /**
     * 查询用户ID
     */
    private String queryUserId;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 规格
     */
    private String specification;
    /**
     * 日期
     */
    private String date;
    /**
     * 使用人员
     */
    private String useUser;
    private List<String> useUserList = new ArrayList<>();
    /**
     * 备注
     */
    private String remark;
    /**
     * 设备细节描述
     */
    private String detailDescribe;
    /**
     * 设备细节描述
     */
    private String gasolineType;
    /**
     * 负责人
     */
    private String chargeUser;
    private List<String> chargeUserList;
    /**
     * 位置
     */
    private String position;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * 文件列表
     */
    private List<FileModel> fileList = new ArrayList<>();
}
