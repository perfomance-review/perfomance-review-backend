package ru.hh.performance_review.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.dto.request.UserAuthenticateDto;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.AuthUserInfo;
import ru.hh.performance_review.security.context.CallContext;
import ru.hh.performance_review.security.context.ContextPerformanceReviewDto;
import ru.hh.performance_review.security.crypted.CryptedService;
import ru.hh.performance_review.security.exception.AccessDeniedException;
import ru.hh.performance_review.security.exception.BadCredentialsException;
import ru.hh.performance_review.security.jwt.JwtTokenProvider;
import ru.hh.performance_review.service.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceReviewSecurityServiceImpl implements PerformanceReviewSecurityService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CryptedService cryptedService;


    @Override
    public AuthUserInfo userAuthenticate(UserAuthenticateDto userAuthenticateDto) {
        String email = userAuthenticateDto.getEmail();
        String password = userAuthenticateDto.getPassword();

        if (StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new BadCredentialsException(String.format(InternalErrorCode.UNKNOWN_USER.getErrorDescription(), userAuthenticateDto.toString()));
        }
        AuthUserInfo authUserInfo = userService.findAuthUserInfoByUserEmail(
                email);

        if (authUserInfo == null) {
            throw new BadCredentialsException(String.format(InternalErrorCode.UNKNOWN_USER.getErrorDescription(), userAuthenticateDto.toString()));
        }

        if (!comparePasswords(authUserInfo.password(), password)) {
            throw new BadCredentialsException(String.format(InternalErrorCode.INVALID_UNAUTHORIZED_PASSWORD.getErrorDescription(), userAuthenticateDto.toString()));
        }

        String accessToken = jwtTokenProvider.createToken(authUserInfo);
        authUserInfo.accessToken(accessToken);
        return authUserInfo;
    }

    @Override
    public CallContext getCallContextFromContextPerformanceReviewDto(ContextPerformanceReviewDto contextPerformanceReviewDto) {
        String accessToken = contextPerformanceReviewDto.getJwtToken();

        AuthUserInfo userInfo = jwtTokenProvider.getUserInfoFromAccessToken(accessToken);

        return CallContext
                .builder()
                .authUserInfo(userInfo)
                .build();
    }


    @Override
    public void checkPerformanceReviewSecuredAuthority(CallContext callContext, PerformanceReviewSecured performanceReviewSecured) {
        if (!(performanceReviewSecured.roles().length == 0) && !hasAuthority(callContext, performanceReviewSecured.roles())) {
            throw new AccessDeniedException(String.format("Access denied. Недостаточно привилегий. Отсутствует роль, текущие роли:%s", String.join(", ", performanceReviewSecured.roles())));
        }
    }

    private boolean hasAuthority(CallContext callContext, String[] roles) {
        Set<String> availableRoles = new HashSet<>(Arrays.asList(roles));
        return callContext.getRoles().stream().anyMatch(availableRoles::contains);
    }

    public boolean comparePasswords(String userPassword, String userAuthenticatePassword) {
        String decryptedPassword = cryptedService.decryptedPasswordProcess(userPassword);
        return decryptedPassword.equals(userAuthenticatePassword);
    }

}
