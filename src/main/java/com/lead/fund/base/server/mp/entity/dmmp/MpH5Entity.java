package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

/**
 * MpH5Entity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("MP_H5")
@NoArgsConstructor
@Accessors(chain = true)
@ClassFunction("H5页面")
public class MpH5Entity extends AbstractAdmin {

    private static final long serialVersionUID = -4052806293520831005L;
    /**
     * 标题
     */
    private String title;
    /**
     * 页面内容
     */
    @TableField(jdbcType = JdbcType.CLOB)
    private String content;

    @Override
    public String toString() {
        return "MpH5Entity{" +
                "title=" + "\"" + title + "\" " +
                ", content=" + "[" + content.length() + "]" +
                "}";
    }
}
