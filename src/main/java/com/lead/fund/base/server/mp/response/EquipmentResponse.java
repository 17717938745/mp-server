package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
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
public class EquipmentResponse implements Serializable {

    private static final long serialVersionUID = 5658438762619500630L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 设备ID
     */
    private String equipmentId;
    /**
     * 设备编号
     */
    private String equipmentNo;
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
    private List<String> useUserList;
    private String useUserFormat;
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
    private String chargeUserFormat;
    /**
     * 位置
     */
    private String position;
    private String positionFormat;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * 文件列表
     */
    private List<FileModel> fileList = new ArrayList<>();
    /**
     * API设备
     */
    private String apiDevice;
    private String apiDeviceFormat;
}
