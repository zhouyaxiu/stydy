package com.shensu.security.interceptor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zjj
 * @version 1.0.0
 * @ClassName SecureIgnoredProperties.java
 * @Description TODO
 * @createTime 2023年4月4日 15:02
 */
@Data
@Component
@ConfigurationProperties(prefix = "secure.ignored")
public class SecureIgnoredProperties {

    /*
     * 不需要拦截的路径
     */
    private List<String> urls;

}
