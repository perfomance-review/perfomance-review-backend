package ru.hh.performance_review.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hh.nab.hibernate.MappingConfig;
import ru.hh.nab.starter.NabCommonConfig;
import ru.hh.performance_review.controller.ExampleController;

@Configuration
@Import({NabCommonConfig.class, ApplicationServiceConfig.class, LogbackConfigurator.class, ExampleController.class })
public class CommonConfig {

    @Bean
    public MappingConfig mappingConfig() {
        MappingConfig mappingConfig = new MappingConfig();
        mappingConfig.addPackagesToScan("ru.hh.performance_review.model");
        return mappingConfig;
    }
}
