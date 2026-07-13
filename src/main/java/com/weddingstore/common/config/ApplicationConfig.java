package com.weddingstore.common.config;

import com.weddingstore.common.config.properties.JwtProperties;
import com.weddingstore.common.config.properties.OpenAiProperties;
import com.weddingstore.common.config.properties.RazorpayProperties;
import com.weddingstore.common.config.properties.S3Properties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    JwtProperties.class,
    S3Properties.class,
    RazorpayProperties.class,
    OpenAiProperties.class
})
public class ApplicationConfig {
}