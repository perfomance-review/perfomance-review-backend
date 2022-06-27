package ru.hh.performance_review.security.context;


public final class SecurityContext {
    private static final ThreadLocal<CallContext> threadLocalContext = new ThreadLocal<>();

    public static void set(AuthUserInfo authUserInfo) {
        threadLocalContext.set(new CallContext(authUserInfo));
    }

    public static void set(CallContext callContext) {
        threadLocalContext.set(callContext);
    }

    public static CallContext get() {
        return threadLocalContext.get();
    }

    public static AuthUserInfo getUserInfo() {
        return get().getAuthUserInfo();
    }

    public static String getUserId(){
        return getUserInfo().id();
    }

    public static void clear() {
        threadLocalContext.remove();
    }
}

