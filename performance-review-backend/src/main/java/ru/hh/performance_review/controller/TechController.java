package ru.hh.performance_review.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.controller.base.HttpRequestHandler;
import ru.hh.performance_review.dto.response.ResponseMessage;
import ru.hh.performance_review.security.annotation.JwtTokenCookie;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.SecurityContext;
import ru.hh.performance_review.security.context.SecurityRole;
import ru.hh.performance_review.security.crypted.CryptedService;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class TechController {

    private final UserService userService;
    private final ObjectConvertService objectConvertService;
    private final CryptedService cryptedService;

    /**
     * endpoint получения данных о всех пользователях
     *
     * @return - ДТО с информацией о пользователе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path("getallusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken) {
        String userId = SecurityContext.getUserId();
        log.info("Получен запрос /getallusers");
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> Function.identity())
                .process(x -> userService.getAllUsers())
                .convert(objectConvertService::convertToJson)
                .forArgument(jwtToken);
    }

    /**
     * Технический эндпоинт, который позволяет получить из БД раскодированные пароли
     * @return
     */
    @GET
    @Path("decodePasswords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response decodePasswords() {
        log.info("Получен запрос /decodePasswords");
        List<UserRawDto> userRawDtos = userService.getAllUsers().getUsersInfo()
                .stream()
                .map(us -> {
                    String decodePassword = cryptedService.decryptedPasswordProcess(us.getPassword());
                    String encodePassword = cryptedService.encryptedPasswordProcess(decodePassword);
                    return UserRawDto.builder()
                            .id(us.getUserId().toString())
                            .email(us.getEmail())
                            .password(us.getPassword())
                            .encodePassword(encodePassword)
                            .decodePassword(decodePassword)
                            .build();
                })
                .collect(Collectors.toList());
        String response = objectConvertService.convertToJson(new UsersRawDtos(userRawDtos));
        return Response.status(Response.Status.OK.getStatusCode())
                .entity(response)
                .build();
    }

    @Data
    @AllArgsConstructor
    public static class UsersRawDtos implements ResponseMessage {
        List<UserRawDto> userRawDtos;
    }

    @Builder
    public static class UserRawDto {
        private final String id;
        private final String email;
        private final String password;
        private final String decodePassword;
        private final String encodePassword;
    }

}
