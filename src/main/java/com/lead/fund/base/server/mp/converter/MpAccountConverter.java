package com.lead.fund.base.server.mp.converter;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.request.MpAccountRequest;
import com.lead.fund.base.server.mp.response.MpAccountResponse;
import com.lead.fund.base.server.mp.response.MpMobileResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AccountConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface MpAccountConverter extends Serializable {

    MpAccountConverter MP_ACCOUNT_INSTANCE = Mappers.getMapper(MpAccountConverter.class);

    @Mapping(target = "openId", source = "data.openid")
    @Mapping(target = "sessionKey", source = "data.sessionKey")
    @Mapping(target = "unionId", source = "data.unionid")
    @Mapping(target = "signTime", expression = "java(new java.util.Date())")
    MpAccountEntity dataToEntity(WxMaJscode2SessionResult data);

    @Mapping(target = "mobile", source = "phoneNumber")
    @Mapping(target = "pureMobile", source = "purePhoneNumber")
    @Mapping(target = "countryCode", source = "countryCode")
    MpMobileResponse toMobile(WxMaPhoneNumberInfo model);

    MpAccountResponse response(MpAccountEntity account);

    List<MpAccountResponse> list(List<MpAccountEntity> list);

    MpAccountEntity entity(MpAccountRequest request);

}
