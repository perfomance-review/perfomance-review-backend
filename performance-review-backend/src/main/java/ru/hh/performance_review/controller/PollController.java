package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hh.performance_review.consts.RequestParams;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.controller.base.HttpRequestHandler;
import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;
import ru.hh.performance_review.dto.response.*;
import ru.hh.performance_review.security.annotation.JwtTokenCookie;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.SecurityContext;
import ru.hh.performance_review.security.context.SecurityRole;
import ru.hh.performance_review.service.*;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;
import ru.hh.performance_review.service.validate.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class PollController {

    private final PollService pollService;
    private final UserService userService;
    private final UserValidateService userValidateService;
    private final PollValidateService pollValidateService;
    private final ObjectConvertService objectConvertService;
    private final StartPollService startPollService;
    private final StarPollValidateService starPollValidateService;

    private final RatingRequestValidateService ratingRequestValidateService;
    private final WinnerCompleteService winnerCompleteService;
    private final GradeService gradeService;
    private final ResultUserValidateService resultUserValidateService;

    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path("polls")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPolls(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken, @QueryParam("status") Set<String> statuses) {
        log.info("Получен запрос /polls");
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        String userId = SecurityContext.getUserId();
        return new HttpRequestHandler<String, PollsByUserIdResponseDto>()
            .validate(v -> pollValidateService.validatePollsByUserId(userId, statuses))
            .process(x -> pollService.getPollsByUserId(userId, statuses))
            .convert(objectConvertService::convertToJson)
            .forArgument(userId, cookie);
    }

    /**
     * endpoint начала опроса. Меняет статус опроса и формирует пары для опроса
     *
     * @param jwtToken          -
     * @param pollId            - идентификатор опроса
     * @param includedIdsString - массив участников опроса
     * @return - ДТО с информацией об опросе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @POST
    @Path(value = "/start/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startPoll(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                              @PathParam("poll_id") String pollId,
                              @RequestBody List<String> includedIdsString) {

        log.info("Получен запрос /start/" + pollId + " с телом: {} ", includedIdsString);
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        String userId = SecurityContext.getUserId();
        return new HttpRequestHandler<String, PollProgressDto>()
                .validate(v -> starPollValidateService.validateDataStartPoll(pollId, userId, includedIdsString))
                .process(x -> startPollService.doStartPoll(pollId, userId, includedIdsString))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения оценки данного пользователя по всем вопросам и компетенциям данного опроса
     *
     * @param jwtToken - jwtToken
     * @param pollId - идентификатор опроса
     * @return - ДТО с вопросами, компетенциями и оценками
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path(value = "/result/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResultForUser(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                                     @PathParam("poll_id") String pollId) {
        String userId = SecurityContext.getUserId();
        log.info("Get result /result/" + pollId + " для пользователя: " + userId);
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        return new HttpRequestHandler<String, GradeUserDto>()
                .validate(v -> resultUserValidateService.validateDataResultUser(pollId, userId))
                .process(x -> gradeService.countGrade(userId, pollId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения рейтинга всех респондентов по всем вопросам для данного опроса
     * Поддерживает пагинацию, page - номер вопроса с 1
     *
     * @param jwtToken - jwtToken (менеджер)
     * @param pollId - идентификатор опроса
     * @param page   - номер вопроса, начиная с 1, необязательный
     * @return - ДТО с вопросами, респондентами и оценками
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER})
    @GET
    @Path(value = "/rating/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRating(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                              @PathParam("poll_id") String pollId,
                              @QueryParam("page") Integer page) {
        String userId = SecurityContext.getUserId();
        log.info("Получен запрос /rating/ " + pollId + " для менеджера: " + userId);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, RatingResponseDto>()
                .validate(v -> resultUserValidateService.validateDataResultUser(pollId, userId))
                .process(x -> gradeService.countRating(pollId, page))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения данных о пользователи по идентификатору пользователя
     *
     * @param jwtToken - jwtToken
     * @return - ДТО с информацией о пользователе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path("getuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken) {
        log.info("Получен запрос /getuser");
        String userId = SecurityContext.getUserId();
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        return new HttpRequestHandler<String, UserResponseDto>()
                .validate(v -> userValidateService.userIdValidate(userId))
                .process(x -> userService.getRespondentByUserId(userId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения данных об опросе по идентификатору опроса
     *
     * @param jwtToken - jwtToken
     * @param pollId   - идентификатор опроса
     * @return - ДТО с информацией об опросе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path("polls/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoolById(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                                @PathParam("poll_id") String pollId) {
        log.info("Получен запрос pools/" + pollId);
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        String userId = SecurityContext.getUserId();
        return new HttpRequestHandler<String, PollByIdResponseDto>()
                .validate(v -> pollValidateService.validatePollById(userId, pollId))
                .process(x -> pollService.getPollById(pollId, userId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint обновления победителя в паре по конкретному вопросу текущего опроса
     *
     * @param jwtToken               - jwtToken
     * @param updateWinnerRequestDto - данные запроса
     * @return - ДТО с информацией об опросе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @POST
    @Path("updatewinner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWinner(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                                 @RequestBody UpdateWinnerRequestDto updateWinnerRequestDto) {
        log.info("Получен запрос /updatewinner с телом: {}", updateWinnerRequestDto);
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        String userId = SecurityContext.getUserId();
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> ratingRequestValidateService.validateUpdateWinnerRequestDto(userId, updateWinnerRequestDto))
                .process(x -> winnerCompleteService.updateWinner(userId, updateWinnerRequestDto))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint который отдаёт вопрос и сформированные пары по опросу
     *
     * @param jwtToken - идентификатор пользователя
     * @param pollId   - pollId запроса
     * @return - ДТО с информацией об опросе
     */
    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER, SecurityRole.RESPONDENT})
    @GET
    @Path("comparepairsofpoll/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComparePairsOfPoll(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                                          @PathParam(RequestParams.POLL_ID) String pollId) {
        log.info("Получен запрос /comparepairsofpoll с poll_id: {}", pollId);
        NewCookie cookie = new NewCookie(CookieConst.ACCESS_TOKEN, jwtToken);
        String userId = SecurityContext.getUserId();
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> pollValidateService.validateComparePairsOfPoll(userId, pollId))
                .process(x -> pollService.getComparePairOfPollDto(userId, pollId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

}

