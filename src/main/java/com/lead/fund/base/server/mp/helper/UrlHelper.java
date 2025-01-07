package com.lead.fund.base.server.mp.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * UrlHelper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-17 09:03
 */
@Component
public class UrlHelper {

    private String urlPrefix;

    @Value("${com.lead.fund.base.url-prefix:\"\"}")
    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public String fullUrl(String url) {
        url = null == url ? "" : url;
        return url.startsWith("http:") || url.startsWith("https:") ? url : (urlPrefix + url);
    }
}
