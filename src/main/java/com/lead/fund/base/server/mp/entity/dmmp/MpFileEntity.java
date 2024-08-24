package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MpFileEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("MP_FILE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ClassFunction("文件附件")
public class MpFileEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 4697542411978194066L;
    /**
     * 内容
     */
    private byte[] data;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件后缀
     */
    private String extension;

    @Override
    public String toString() {
        return "MpFileEntity{" +
                "data=" + "[" + data.length + "]" +
                ", filename='" + filename + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
