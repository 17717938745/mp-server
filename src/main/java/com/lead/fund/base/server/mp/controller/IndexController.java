package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.common.web.util.SpringUtil.getFilenameArray;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ACCOUNT_DEVICE_ID_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ACCOUNT_SIGN_IN_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_COMPRESS_IMG_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_SAVE_FILE_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_SAVE_H5_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_SAVE_VIDEO_ERROR;
import static com.lead.fund.base.server.mp.converter.MpAccountConverter.MP_ACCOUNT_INSTANCE;
import static com.lead.fund.base.server.mp.converter.MpIndexConverter.MP_INDEX_INSTANCE;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.img.ImgUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.server.mp.dao.MpAccountDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpDeviceEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpFileEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpH5Entity;
import com.lead.fund.base.server.mp.entity.dmmp.MpImgEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpVideoEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpDeviceMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpFileMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpH5Mapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpImgMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpVideoMapper;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.request.MpAccountRequest;
import com.lead.fund.base.server.mp.request.MpH5Request;
import com.lead.fund.base.server.mp.request.MpImgLoadRequest;
import com.lead.fund.base.server.mp.request.MpMobileRequest;
import com.lead.fund.base.server.mp.request.MpSignRequest;
import com.lead.fund.base.server.mp.response.MpAccountResponse;
import com.lead.fund.base.server.mp.response.MpH5BaseResponse;
import com.lead.fund.base.server.mp.response.MpImgResponse;
import com.lead.fund.base.server.mp.response.MpMobileResponse;
import com.lead.fund.base.server.mp.response.MpVideoResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * IndexController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 11:39
 */
@SuppressWarnings("SqlResolve")
@RestController
@RequestMapping("/index")
@Slf4j
@Validated
public class IndexController {

    @Resource
    private WxMaService wxService;
    @Resource
    private AccountHelper accountHelper;
    @Resource
    private MpAccountDao mpAccountDao;
    @Resource
    private MpDeviceMapper mpDeviceMapper;
    @Resource
    private MpImgMapper imgMapper;
    @Resource
    private MpFileMapper fileMapper;
    @Resource
    private MpVideoMapper videoMapper;
    @Resource
    private RestTemplate externalRestTemplate;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private MpH5Mapper h5Mapper;

    /**
     * 首页
     *
     * @return {@link ModelAndView}
     */
    @GetMapping("")
    public ModelAndView home() {
        return new ModelAndView("/industry/industry.html");
    }

    /**
     * 查询用户信息
     *
     * @param deviceId 设备id
     * @return {@link DataResult<MpAccountResponse>}
     */
    @GetMapping("/account")
    public DataResult<MpAccountResponse> account(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId) {
        return new DataResult<>(
                MP_ACCOUNT_INSTANCE.response(accountHelper.getAccount(deviceId))
        );
    }

    /**
     * 查询用户信息
     *
     * @param deviceId 设备id
     * @return {@link DataResult<MpAccountResponse>}
     */
    @PutMapping("/account")
    public DataResult<MpAccountResponse> accountUpdate(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, @RequestBody MpAccountRequest request) {
        final MpAccountEntity account = accountHelper.getAccount(deviceId);
        MpAccountEntity entity = MP_ACCOUNT_INSTANCE.entity(request);
        entity.setOpenId(account.getOpenId());
        mpAccountDao.updateById(entity);
        accountHelper.refresh(deviceId);
        return new DataResult<>(
                MP_ACCOUNT_INSTANCE.response(account)
        );
    }

    /**
     * 登录/注册
     *
     * @param deviceId 设备id
     * @param request  {@link MpSignRequest}
     * @return {@link DataResult<MpAccountEntity>}
     */
    @PutMapping("/sign")
    public DataResult<MpAccountEntity> sign(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, @RequestBody MpSignRequest request) {
        if (isNotBlank(request.getCode())) {
            try {
                WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(request.getCode());
                log.info("session key: {}, open id: {}", session.getSessionKey(), session.getOpenid());
                MpAccountEntity miniAccount = MP_ACCOUNT_INSTANCE.dataToEntity(session);
                MpAccountEntity db = mpAccountDao.getById(miniAccount.getOpenId());
                if (null != db) {
                    db.setNickname(defaultIfBlank(db.getNickname(), miniAccount.getNickname()));
                    db.setAvatarUrl(defaultIfBlank(db.getAvatarUrl(), miniAccount.getAvatarUrl()));
                    mpAccountDao.updateById(db);
                } else {
                    mpAccountDao.save(db = miniAccount);
                }
                if (mpDeviceMapper.update(
                        null,
                        new LambdaUpdateWrapper<MpDeviceEntity>()
                                .set(MpDeviceEntity::getAccountId, db.getOpenId())
                                .eq(MpDeviceEntity::getId, deviceId)
                ) > 0) {
                    accountHelper.clear(deviceId);
                    return new DataResult<>(db);
                } else {
                    throw new BusinessException(MP_ACCOUNT_DEVICE_ID_ERROR);
                }
            } catch (WxErrorException e) {
                log.error("sign error", e);
            }
        }
        throw new BusinessException(MP_ACCOUNT_SIGN_IN_ERROR);
    }


    /**
     * 解密手机号
     *
     * @param deviceId 设备id
     * @param request  {@link MpMobileRequest}
     * @return {@link MpMobileRequest}
     */
    @GetMapping("/mobile")
    public DataResult<MpMobileResponse> mobile(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MpMobileRequest request
    ) {
        MpAccountEntity account = accountHelper.getAccount(deviceId);
        try {
            //        final WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(account.getSessionKey(), request.getEncryptedData(), request.getIv());
            final WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(request.getEncryptedData());
            final String mobile = phoneNoInfo.getPurePhoneNumber();
            if (isNotBlank(mobile) && !mobile.equals(account.getMobile())) {
                account.setMobile(mobile);
                mpAccountDao.update(
                        null,
                        new LambdaUpdateWrapper<MpAccountEntity>()
                                .set(MpAccountEntity::getMobile, mobile)
                                .eq(MpAccountEntity::getOpenId, account.getOpenId())
                );
            }
            return new DataResult<>(MP_ACCOUNT_INSTANCE.toMobile(phoneNoInfo));
        } catch (WxErrorException e) {
            throw new BusinessException(MP_ERROR);
        }
    }

    private MpImgResponse saveImg(String[] filenameArray, byte[] content) {
        try {
            MpImgEntity db = MpImgEntity.builder()
                    .compressData(filenameArray.length > 1 && "gif".equalsIgnoreCase(filenameArray[1]) ? content : compress(content))
                    .data(content)
                    .filename(filenameArray[0])
                    .extension(filenameArray[1])
                    .build();
            imgMapper.insert(
                    db
            );
            return MpImgResponse.builder()
                    .url("/index/img/" + db.getId())
                    .fullUrl(urlHelper.getUrlPrefix() + "/index/img/" + db.getId())
                    .compressUrl("/index/img/" + db.getId() + "/compress")
                    .fullCompressUrl(urlHelper.getUrlPrefix() + "/index/img/" + db.getId() + "/compress")
                    .build();
        } catch (Exception e) {
            throw new BusinessException(MP_SAVE_FILE_ERROR).setOriginException(e);
        }
    }

    /**
     * 上传图片
     *
     * @param file 单个图片 {@link MultipartFile}
     * @return {@link DataResult<MpImgResponse>}
     */
    @PostMapping("img")
    public DataResult<MpImgResponse> upload(
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            return new DataResult<>(
                    saveImg(getFilenameArray(file), file.getBytes())
            );
        } catch (Exception e) {
            throw new BusinessException(MP_SAVE_FILE_ERROR).setOriginException(e);
        }
    }

    /**
     * 上传文件
     *
     * @param file 单个图片 {@link MultipartFile}
     * @return {@link DataResult<FileModel>}
     */
    @PostMapping("file")
    public DataResult<FileModel> uploadFile(
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        final String[] filenameArray = getFilenameArray(file);
        final byte[] content;
        try {
            content = file.getBytes();
            String filename = file.getOriginalFilename();
            String extension = filenameArray[1];
            MpFileEntity db = MpFileEntity.builder()
                    .data(content)
                    .filename(filename)
                    .extension(extension)
                    .build();
            fileMapper.insert(
                    db
            );
            return new DataResult<>(
                    FileModel.builder()
                            .fileId(db.getId())
                            .filename(db.getFilename())
                            .url("/index/file/" + db.getId())
                            .fullUrl(urlHelper.getUrlPrefix() + "/index/file/" + db.getId())
                            .build()
            );
        } catch (Exception e) {
            throw new BusinessException(MP_SAVE_FILE_ERROR).setOriginException(e);
        }
    }

    /**
     * 上传视频
     *
     * @param file 单个文件 {@link MultipartFile}
     * @return {@link DataResult<MpVideoResponse>}
     */
    @PostMapping("video")
    public DataResult<MpVideoResponse> uploadVideo(
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        final String[] filenameArray = getFilenameArray(file);
        final byte[] content;
        try {
            content = file.getBytes();
            String filename = filenameArray[0];
            String extension = defaultIfBlank(filenameArray[1], "mp4");
            MpVideoEntity db = MpVideoEntity.builder()
                    .compressData(content)
                    .data(content)
                    .filename(filename)
                    .extension(extension)
                    .build();
            videoMapper.insert(
                    db
            );
            return new DataResult<>(
                    MpVideoResponse.builder()
                            .url("/index/video/" + db.getId())
                            .fullUrl(urlHelper.getUrlPrefix() + "/index/video/" + db.getId())
                            .extension(extension)
                            .compressUrl("/index/video/" + db.getId() + "/compress")
                            .fullCompressUrl(urlHelper.getUrlPrefix() + "/index/video/" + db.getId() + "/compress")
                            .build()
            );
        } catch (Exception e) {
            throw new BusinessException(MP_SAVE_VIDEO_ERROR).setOriginException(e);
        }
    }


    /**
     * 上传图片
     *
     * @param request 图片链接 {@link MpImgLoadRequest}
     * @return {@link DataResult<MpImgResponse>}
     */
    @PostMapping("img/load")
    public DataResult<MpImgResponse> load(
            @RequestBody MpImgLoadRequest request
    ) {
        try {
            final String[] arr = request.getImgUrl().split("/", -1);
            return new DataResult<>(
                    saveImg(getFilenameArray(arr[arr.length - 1]), externalRestTemplate.exchange(request.getImgUrl(), HttpMethod.GET, null, byte[].class).getBody())
            );
        } catch (Exception e) {
            throw new BusinessException(MP_SAVE_FILE_ERROR).setOriginException(e);
        }
    }

    @GetMapping("img/{id}")
    public ResponseEntity<byte[]> img(@PathVariable(name = "id") String id) {
        final MpImgEntity img = imgMapper.selectById(id);
        byte[] bytes = img.getData();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("image", img.getExtension()));
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("img/{id}/compress")
    public ResponseEntity<byte[]> compress(@PathVariable(name = "id") String id) {
        final MpImgEntity img = imgMapper.selectById(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("image", img.getExtension()));
        return new ResponseEntity<>(img.getCompressData(), headers, HttpStatus.OK);
    }

    @GetMapping("file/{id}")
    public ResponseEntity<byte[]> file(@PathVariable(name = "id") String id, HttpServletResponse response) {
        final MpFileEntity file = fileMapper.selectById(id);
        byte[] bytes = file.getData();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(file.getFilename(), StandardCharsets.UTF_8));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("video/{id}")
    public ResponseEntity<byte[]> video(@PathVariable(name = "id") String id) {
        final MpVideoEntity img = videoMapper.selectById(id);
        byte[] bytes = img.getData();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping("video/{id}/compress")
    public ResponseEntity<byte[]> videoCompress(@PathVariable(name = "id") String id) {
        final MpVideoEntity img = videoMapper.selectById(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(img.getCompressData(), headers, HttpStatus.OK);
    }

    public byte[] compress(byte[] content) {
        float quality = (float) (0.5 - content.length / 1024.0 / 256.0 / 10.0);
        quality = Math.max(quality, 0.1f);
        log.info("quality: {}", quality);
        try (final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content)) {
            final BufferedImage image = ImageIO.read(byteArrayInputStream);
            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (final ImageOutputStream outputStream = new MemoryCacheImageOutputStream(baos)) {
                    if (null == image) {
                        throw new BusinessException(MP_COMPRESS_IMG_ERROR);
                    }
                    ImgUtil.scale(image, outputStream, quality);
                    byte[] bytes = baos.toByteArray();
                    log.info("bytes.length: {}", bytes.length);
                    return bytes;
                }
            }
        } catch (IOException e) {
            log.warn("compress error", e);
            return content;
        }
    }

    /**
     * 上传H5
     *
     * @param request {@link MpH5Request}
     * @return {@link DataResult<MpH5BaseResponse>}
     */
    @PutMapping("h5")
    public DataResult<MpH5BaseResponse> mergeH5(
            @RequestBody MpH5Request request
    ) {
        final MpH5Entity entity = MP_INDEX_INSTANCE.data(request);
        if (isNotBlank(entity.getId())) {
            h5Mapper.updateById(entity);
        } else {
            h5Mapper.insert(entity);
        }
        try {
            return new DataResult<>(
                    MP_INDEX_INSTANCE.data(entity)
            );
        } catch (Exception e) {
            throw new BusinessException(MP_SAVE_H5_ERROR).setOriginException(e);
        }
    }
}
