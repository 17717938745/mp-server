package com.lead.fund.base.server.mp.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MpProperties
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-24 21:20
 */
@Data
@ConfigurationProperties(prefix = "wx.mp")
public class MpProperties {

    private List<Config> configs;

    @Data
    public static class Config {

        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;
    }
}
