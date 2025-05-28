package com.lead.fund.base.server.mp.cons;

import com.lead.fund.base.common.basic.api.frame.TypeApi;
import lombok.Getter;

/**
 * ExceptionType
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-15 11:46
 */
@Getter
public enum MpExceptionType implements TypeApi<Integer> {

    /**
     * 微信小程序异常
     */
    MP_ERROR(310000, "微信小程序异常"),
    MP_ACCOUNT_NOT_SIGN_IN(310001, "小程序用户未登录"),
    MP_ACCOUNT_SIGN_IN_ERROR(310002, "小程序用户登录失败"),
    MP_ACCOUNT_DEVICE_ID_ERROR(310003, "设备信息错误"),
    MP_COMPRESS_IMG_ERROR(310004, "压缩图片失败"),
    MP_SAVE_FILE_ERROR(310005, "保存文件失败"),
    MP_PRODUCT_EXISTS(310006, "已经提交，请修改！"),
    MP_ACCOUNT_FROZEN(310007, "账户已被冻结，请联系管理员"),
    MP_USER_NOT_SIGN_IN(310008, "用户未登录"),
    MP_USER_FROZEN(310009, "用户已被冻结，请联系管理员"),
    MP_RECORD_DEVICE_TIME_ERROR(310010, "记录设备时间异常"),
    MP_SAVE_VIDEO_ERROR(310011, "保存视频失败"),
    MP_OPERATOR_OTHER_NOT_ALLOW(310012, "非管理员无法修改他人数据"),
    MP_ORDER_REPEAT(310013, "序列号重复"),
    MP_PASSWORD_ERROR(310014, "密码不正确"),
    MP_SAVE_H5_ERROR(310015, "H5保存失败"),
    MP_UPLOAD_EXCEL_ERROR(310016, "上传Excel失败"),
    MP_DATA_QUERY_ERROR(310017, "数据查询异常"),
    MP_USER_EMPLOYEE_ID_REPEAT(310018, "用户工号重复"),
    ;

    private final Integer code;
    private final String name;

    MpExceptionType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
