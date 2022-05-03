package ru.hh.performance_review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hh.nab.hibernate.MappingConfig;
import ru.hh.nab.starter.NabCommonConfig;

@Configuration
@Import({NabCommonConfig.class, ApplicationServiceConfig.class, LogbackConfigurator.class})
public class CommonConfig {

    @Bean
    public MappingConfig mappingConfig() {
        MappingConfig mappingConfig = new MappingConfig();
        mappingConfig.addPackagesToScan("ru.hh.performance_review.model");
        return mappingConfig;
    }
}
