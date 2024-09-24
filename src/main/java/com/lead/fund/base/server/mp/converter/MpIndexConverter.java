package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.dmmp.MpH5Entity;
import com.lead.fund.base.server.mp.entity.dmmp.MpRoleEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserPhotoEntity;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.MpH5Request;
import com.lead.fund.base.server.mp.request.MpUserMergeRequest;
import com.lead.fund.base.server.mp.response.MpH5BaseResponse;
import com.lead.fund.base.server.mp.response.MpH5Response;
import com.lead.fund.base.server.mp.response.MpRoleResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import java.io.Serializable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MpIndexConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface MpIndexConverter extends Serializable {

    MpIndexConverter MP_INDEX_INSTANCE = Mappers.getMapper(MpIndexConverter.class);

    /**
     * 对象转换
     *
     * @param d {@link MpH5Request}
     * @return {@link MpH5Entity}
     */
    @Mapping(target = "id", source = "h5Id")
    MpH5Entity data(MpH5Request d);

    /**
     * 对象转换
     *
     * @param d {@link MpH5Entity}
     * @return {@link MpH5BaseResponse}
     */
    @Mapping(target = "h5Id", source = "id")
    MpH5BaseResponse data(MpH5Entity d);
}
