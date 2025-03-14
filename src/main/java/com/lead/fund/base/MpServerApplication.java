package com.lead.fund.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

/**
 * MpServerApplication
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 18:30
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@AutoConfiguration
@EnableCaching
//@org.springframework.cloud.client.discovery.EnableDiscoveryClient
public class MpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MpServerApplication.class, args);
    }
}
