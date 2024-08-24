package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * BoxFlagSerialNoEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_BOX_FLAG_SERIAL_NO")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("序列号")
@Accessors(chain = true)
public class BoxFlagSerialNoEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 2680139971295427139L;
    /**
     * 装箱标识卡ID
     */
    @FieldRemark(value = "装箱标识卡ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "装箱标识卡ID长度不合法")
    private String boxFlagId;
    /**
     * 序列号
     */
    @FieldRemark(value = "序列号", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "长度不对")
    private String serialNo;
}
