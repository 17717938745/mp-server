package com.lead.fund.base.server.mp.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ScoreUploadResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@Accessors(chain = true)
public class ScoreUploadResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 4083223105492336504L;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 错误数量
     */
    private int errorCount;
    /**
     * 未匹配到的用户列表
     */
    private int notMatchUserCount;
    private List<String> notMatchUserList = new ArrayList<>();
    /**
     * 更新的用户列表
     */
    private int updateUserCount;
    private List<String> updateUserList = new ArrayList<>();
    /**
     * 新增的用户列表
     */
    private int insertUserCount;
    private List<String> insertUserList = new ArrayList<>();
    /**
     * 错误信息
     */
    private String errorMessage;
}
