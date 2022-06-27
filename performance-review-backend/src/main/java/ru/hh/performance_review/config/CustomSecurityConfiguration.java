package ru.hh.performance_review.config;

import org.springframework.context.annotation.Bean;
import ru.hh.nab.starter.exceptions.SecurityExceptionMapper;
import ru.hh.performance_review.security.aspect.PerformanceReviewSecurityAspect;
import ru.hh.performance_review.service.security.ContextPerformanceReviewService;
import ru.hh.performance_review.service.security.PerformanceReviewSecurityService;


public class CustomSecurityConfiguration {

    @Bean
    public PerformanceReviewSecurityAspect performanceReviewSecurityAspect(PerformanceReviewSecurityService performanceReviewSecurityService,
                                                                           ContextPerformanceReviewService contextPerformanceReviewService) {
        return new PerformanceReviewSecurityAspect(performanceReviewSecurityService, contextPerformanceReviewService);
    }

    @Bean
    public SecurityExceptionMapper SecurityExceptionMapper() {
        return new SecurityExceptionMapper();
    }


}
