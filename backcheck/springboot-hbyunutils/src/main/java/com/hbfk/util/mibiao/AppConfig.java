package com.hbfk.util.mibiao;

import com.eetrust.label.LabelOperator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public LabelOperator labelOperator() {
        return new LabelOperator("192.0.2.200",8090);
    }
}