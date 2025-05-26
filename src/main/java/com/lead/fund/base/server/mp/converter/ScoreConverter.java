package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.ScoreAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.ScoreEntity;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.ScoreRequest;
import com.lead.fund.base.server.mp.response.ScoreResponse;
import com.lead.fund.base.server.mp.response.ScoreSummaryResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * ScoreConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface ScoreConverter extends Serializable {

    ScoreConverter SCORE_INSTANCE = Mappers.getMapper(ScoreConverter.class);

    @Mapping(target = "id", source = "scoreId")
    ScoreEntity score(ScoreRequest request);

    @Mapping(target = "scoreId", source = "id")
    @Mapping(target = "modifyTime", expression = "java(com.lead.fund.base.common.util.DateUtil.dateTime(t.getLastModifiedTime(), com.lead.fund.base.common.basic.cons.util.date.DateFormat.DOT_MILLIS.getCode(), \"--\"))")
    ScoreResponse score(ScoreEntity t);

    List<ScoreResponse> scoreList(List<ScoreEntity> list);

    @Mapping(target = "scoreId", source = "scoreId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    ScoreAttachmentEntity attachment(String scoreId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel photo(ScoreAttachmentEntity e, String urlPrefix);

    @Mapping(target = "evaluationResult", expression = "java(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getEvaluationResult()))")
    ScoreSummaryResponse scoreSummary(ScoreEntity t);

    List<ScoreSummaryResponse> scoreSummaryList(List<ScoreEntity> list);
}
