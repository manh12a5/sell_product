package com.example.demo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomizationConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        List<String> baseNames = new ArrayList<>();
        baseNames.add("file:messages");
        baseNames.add("classpath:messages/messages");
        messageSource.setBasenames(baseNames.toArray(new String[baseNames.size()]));
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(5*60);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

}
