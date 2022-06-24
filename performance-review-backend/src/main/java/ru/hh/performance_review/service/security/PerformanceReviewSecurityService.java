package ru.hh.performance_review.service.security;

import ru.hh.performance_review.dto.request.UserAuthenticateDto;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.AuthUserInfo;
import ru.hh.performance_review.security.context.CallContext;
import ru.hh.performance_review.security.context.ContextPerformanceReviewDto;

public interface PerformanceReviewSecurityService {

    AuthUserInfo userAuthenticate(UserAuthenticateDto userAuthenticateDto);

    CallContext getCallContextFromContextPerformanceReviewDto(ContextPerformanceReviewDto contextPerformanceReviewDto);

    void checkPerformanceReviewSecuredAuthority(CallContext callContext, PerformanceReviewSecured performanceReviewSecured);
}
