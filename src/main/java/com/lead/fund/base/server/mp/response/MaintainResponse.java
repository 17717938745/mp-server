package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * MaintainResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MaintainResponse implements Serializable {

    private static final long serialVersionUID = 5658438762619500630L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 维修ID
     */
    private String maintainId;
    /**
     * 日期
     */
    private String date;
    /**
     * 设备编号
     */
    private String equipmentId;
    /**
     * 设备编号
     */
    private String equipmentNo;
    /**
     * 位置
     */
    private String position;
    private String positionFormat;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 故障原因
     */
    private String brokenReason;
    private String brokenReasonFormat;
    private List<String> brokenReasonList = new ArrayList<>();
    /**
     * 故障内容
     */
    private String brokenContent;
    /**
     * 修理内容
     */
    private String repairContent;
    /**
     * 更换配件
     */
    private String replacePair;
    /**
     * 维修类型
     */
    private String repairType;
    private String repairTypeFormat;
    /**
     * 停机时长H
     */
    private BigDecimal stopHour;
    private String stopHourFormat;
    /**
     * 当事人
     */
    private String partyUser;
    private List<String> partyUserList = new ArrayList<>();
    private String partyUserFormat;
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
     * 结案
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
}
