package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.common.basic.response.DeviceResponse;
import com.lead.fund.base.server.mp.entity.douson.ForumCommentaryEntity;
import com.lead.fund.base.server.mp.entity.douson.ForumEntity;
import com.lead.fund.base.server.mp.entity.douson.ForumThumbsUpEntity;
import com.lead.fund.base.server.mp.request.ForumCommentaryRequest;
import com.lead.fund.base.server.mp.request.ForumRequest;
import com.lead.fund.base.server.mp.response.ForumCommentaryResponse;
import com.lead.fund.base.server.mp.response.ForumResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MpForumConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface MpForumConverter extends Serializable {

    MpForumConverter MP_FORUM_INSTANCE = Mappers.getMapper(MpForumConverter.class);

    @Mapping(target = "forumId", source = "id")
    @Mapping(target = "thumbsUp", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(d.getThumbsUp()))")
    @Mapping(target = "commentary", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(d.getCommentary()))")
    ForumResponse data(ForumEntity d);

    List<ForumResponse> list(List<ForumEntity> l);

    @Mapping(target = "id", source = "forumId")
    ForumEntity entity(ForumRequest request);

    ForumCommentaryEntity commentary(ForumCommentaryRequest t);

    @Mapping(target = "commentaryId", source = "id")
    @Mapping(target = "createdTimeFormat", expression = "java(com.lead.fund.base.common.util.DateUtil.day(r.getCreatedTime()))")
    ForumCommentaryResponse commentary(ForumCommentaryEntity r);

    List<ForumCommentaryResponse> commentaryList(List<ForumCommentaryEntity> l);
}
