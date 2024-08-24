package com.lead.fund.base.server.mp.cons;

import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.UTIL_CLASS_FORBID_CONSTRUCT;

import com.lead.fund.base.common.basic.exec.BusinessException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MpConst
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-15 11:45
 */
@SuppressWarnings("unused")
public class MpConst {

    public MpConst() {
        throw new BusinessException(UTIL_CLASS_FORBID_CONSTRUCT);
    }

    public static final List<String> STRING_LIST_NORMAL_USER_ROLE_CODE_LIST = List.of("user");
    public static final List<String> STRING_LIST_ENGINEER_USER_ROLE_CODE_LIST = List.of("engineer");
}
