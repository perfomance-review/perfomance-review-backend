package ru.hh.performance_review;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hh.nab.testbase.NabTestConfig;
import ru.hh.performance_review.config.CommonConfig;

@Configuration
@Import({CommonConfig.class, NabTestConfig.class})
public class AppTestConfig {
}
