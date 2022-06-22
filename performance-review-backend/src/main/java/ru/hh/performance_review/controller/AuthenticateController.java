package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.dto.request.UserAuthenticateDto;
import ru.hh.performance_review.security.context.AuthUserInfo;
import ru.hh.performance_review.security.context.SecurityContext;
import ru.hh.performance_review.security.exception.BadCredentialsException;
import ru.hh.performance_review.service.security.PerformanceReviewSecurityService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class AuthenticateController {

    private final PerformanceReviewSecurityService performanceReviewSecurityService;

    /**
     * endpoint авторизации
     *
     * @return - userInfo
     */

    @POST
    @Path("auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userAuthenticate(@RequestBody UserAuthenticateDto userAuthenticateDto) {

        log.info("Получен запрос /auth/login : {}", userAuthenticateDto);

        try {
            AuthUserInfo authUserInfo = performanceReviewSecurityService.userAuthenticate(userAuthenticateDto);
            NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN,
                    authUserInfo.accessToken());

            Map<Object, Object> response = new HashMap<>();
            response.put("userEmail", authUserInfo.userEmail());
            response.put(CookieConst.ACCESS_TOKEN, authUserInfo.accessToken());
            SecurityContext.set(authUserInfo);

            return Response.status(Response.Status.OK.getStatusCode())
                    .cookie(cookie)
                    .entity(response)
                    .build();

        } catch (BadCredentialsException e) {
            log.error("", e);
            return Response.status(Response.Status.UNAUTHORIZED.getStatusCode())
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}
