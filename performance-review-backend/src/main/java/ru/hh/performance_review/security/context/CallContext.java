package ru.hh.performance_review.security.context;

import lombok.Builder;

import java.util.Set;

@Builder
public class CallContext {

    /**
     * Текущий пользователь
     */
    private final AuthUserInfo authUserInfo;

    public String getAccessToken() {
        return authUserInfo.accessToken();
    }

    /**
     * Роли пользователя
     */
    public Set<String> getRoles() {
        return authUserInfo.roles();
    }

    public AuthUserInfo getAuthUserInfo() {
        return authUserInfo;
    }

    public CallContext(AuthUserInfo authUserInfo) {
        this.authUserInfo = authUserInfo;
    }
}
