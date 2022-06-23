package ru.hh.performance_review.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.security.context.AuthUserInfo;
import ru.hh.performance_review.security.exception.InvalidSecuredConfigurationException;
import ru.hh.performance_review.security.exception.UnauthorizedException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Predicate;

/**
 * Класс Util, который предоставляет методы для создания, получения, проверки и т. д. токена JWT.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.token.secret:performance_review}")
    private String secret;
    private static final String ROLES = AuthUserInfo.Fields.roles;
    private static final String FIRST_NAME = AuthUserInfo.Fields.firstName;
    private static final String LAST_NAME = AuthUserInfo.Fields.lastName;
    private static final String MIDDLE_NAME = AuthUserInfo.Fields.middleName;


    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(AuthUserInfo authUserInfo) {

        Claims claims = Jwts.claims()
                .setId(authUserInfo.id())
                .setSubject(authUserInfo.userEmail());

        put(claims, CollectionUtils::isNotEmpty, authUserInfo.roles(), ROLES);
        put(claims, StringUtils::isNoneBlank, authUserInfo.firstName(), FIRST_NAME);
        put(claims, StringUtils::isNoneBlank, authUserInfo.lastName(), LAST_NAME);
        put(claims, StringUtils::isNoneBlank, authUserInfo.middleName(), MIDDLE_NAME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private <T> void put(Claims claims, Predicate<T> tPredicate, T t, String key) {
        if(tPredicate.test(t)){
            claims.put(key, t);
        }
    }


    public AuthUserInfo getUserInfoFromAccessToken(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            throw new UnauthorizedException("Не передан пользовательский токен в cookie запроса", InternalErrorCode.UNAUTHORIZED_TOKEN);
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(accessToken)
                    .getBody();

            return AuthUserInfo.builder()
                    .id(claims.getId())
                    .userEmail(claims.getSubject())
                    .firstName((String) claims.get(FIRST_NAME))
                    .lastName((String) claims.get(LAST_NAME))
                    .middleName((String) claims.get(MIDDLE_NAME))
                    .roles(getRoles(claims.get(ROLES)))
                    .build();
        } catch (Exception e) {
            String errMsg = String.format("%s: %s", InternalErrorCode.UNAUTHORIZED_TOKEN.getErrorDescription(), e.getLocalizedMessage());
            log.error(errMsg, e);
            throw new UnauthorizedException(errMsg, e);
        }
    }

    private Set<String> getRoles(Object o) {
        if(o instanceof List){
            List<String> strings = (List<String>) o;
            return new HashSet<>(strings);
        }
        throw new InvalidSecuredConfigurationException("Ошибка получения ролей из токена");
    }
}
