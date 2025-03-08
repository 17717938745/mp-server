package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.dmmp.MpRoleEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserPhotoEntity;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.MpUserMergeRequest;
import com.lead.fund.base.server.mp.response.MpRoleResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import java.io.Serializable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MpSystemConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface MpSystemConverter extends Serializable {

    MpSystemConverter MP_SYSTEM_INSTANCE = Mappers.getMapper(MpSystemConverter.class);

    /**
     * entity转response
     *
     * @param d {@link MpUserEntity}
     * @return {@link MpUserResponse}
     */
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "departmentFormat", source = "department")
    @Mapping(target = "professionFormat", source = "profession")
    @Mapping(target = "stateFormat", expression = "java(com.lead.fund.base.common.basic.cons.frame.AdminState.DELETE.getCode().equals(d.getState()) ? \"离职 Leave\" : com.lead.fund.base.common.basic.cons.frame.AdminState.NORMAL.getName())")
    @Mapping(target = "createTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getCreateTime()))")
    @Mapping(target = "modifyTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getModifyTime()))")
    @Mapping(target = "passwordExpire", expression = "java(null != d.getPasswordExpireTime() && cn.hutool.core.date.DateTime.now().compareTo(d.getPasswordExpireTime()) >= 0)")
    @Mapping(target = "planIncreaseSalaryDateCount", expression = "java(null == d.getPlanIncreaseSalaryDate() ? null : (int)cn.hutool.core.date.DateUtil.between(new java.util.Date(), com.lead.fund.base.common.util.DateUtil.parse(d.getPlanIncreaseSalaryDate()), cn.hutool.core.date.DateUnit.DAY, false))")
    MpUserResponse data(MpUserEntity d);

    /**
     * entity转response
     *
     * @param d {@link MpUserEntity}
     * @return {@link MpUserResponse}
     */
    @Mapping(target = "roleId", source = "id")
    MpRoleResponse data(MpRoleEntity d);

    @Mapping(target = "id", source = "userId")
    MpUserEntity entity(MpUserMergeRequest request);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(MpUserPhotoEntity e, String urlPrefix);
}
