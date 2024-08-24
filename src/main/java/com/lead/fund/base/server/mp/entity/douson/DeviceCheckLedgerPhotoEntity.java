package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * DeviceCheckLedgerPhotoEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_DEVICE_CHECK_LEDGER_PHOTO")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("零件")
@Accessors(chain = true)
public class DeviceCheckLedgerPhotoEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 1337825593051002231L;
    /**
     * 检查设备台账ID
     */
    @FieldRemark(value = "检查设备台账ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "检查设备台账ID长度不合法")
    private String deviceCheckLedgerId;
    /**
     * 照片链接
     */
    @FieldRemark(value = "照片链接", indexType = "UNIQUE", indexGroup = "0")
    private String photoUrl;
    /**
     * 照片压缩链接
     */
    @FieldRemark(value = "照片压缩链接")
    private String photoCompressUrl;
}
