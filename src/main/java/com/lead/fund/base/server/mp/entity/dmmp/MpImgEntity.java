package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * MpImgEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("MP_IMG")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ClassFunction("小程序图片")
public class MpImgEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = -4052806293520831102L;
    /**
     * 压缩内容
     */
    @TableField(jdbcType = JdbcType.BLOB)
    private byte[] compressData;
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
        return "MpImgEntity{" +
                "compressData=" + "[" + compressData.length + "]" +
                ", data=" + "[" + data.length + "]" +
                ", filename='" + filename + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
