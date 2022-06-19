package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hh.performance_review.consts.RequestParams;
import ru.hh.performance_review.controller.base.Cookie;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.controller.base.HttpRequestHandler;
import ru.hh.performance_review.dto.response.*;
import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;
import ru.hh.performance_review.service.*;
import ru.hh.performance_review.service.PollService;
import ru.hh.performance_review.service.StartPollService;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.WinnerCompleteService;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;
import ru.hh.performance_review.service.validate.PollValidateService;
import ru.hh.performance_review.service.validate.RatingRequestValidateService;
import ru.hh.performance_review.service.validate.StarPollValidateService;
import ru.hh.performance_review.service.validate.UserValidateService;
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
    private final QuestionService questionService;

    @GET
    @Path("polls")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPolls(@CookieParam("user-id") String userId, @QueryParam("status") Set<String> statuses) {
        log.info("Получен запрос /polls");
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, PollsByUserIdResponseDto>()
            .validate(v -> pollValidateService.validatePollsByUserId(userId, statuses))
            .process(x -> pollService.getPollsByUserId(userId, statuses))
            .convert(objectConvertService::convertToJson)
            .forArgument(userId, cookie);
    }

    /**
     * endpoint начала опроса. Меняет статус опроса и формирует пары для опроса
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @paramRequestBody - массив участников опроса
     * @return - ДТО с информацией об опросе
     */
    @POST
    @Path(value = "/start/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startPoll(@PathParam("poll_id") String pollId, @CookieParam(CookieConst.USER_ID) String userId, @RequestBody List<String> includedIdsString) {

        log.info("Получен запрос /start/" + pollId + " с телом: {} ", includedIdsString);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, PollProgressDto>()
                .validate(v -> starPollValidateService.validateDataStartPoll(pollId, userId, includedIdsString))
                .process(x -> startPollService.doStartPoll(pollId, userId, includedIdsString))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения оценки данного пользователя по всем вопросам и компетенциям данного опроса
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @return - ДТО с вопросами, компетенциями и оценками
     */
    @GET
    @Path(value = "/result/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResultForUser(@PathParam("poll_id") String pollId, @CookieParam(CookieConst.USER_ID) String userId) {
        log.info("Получен запрос /result/ " + pollId + " для пользователя: " + userId);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
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
     * @param userId - идентификатор пользователя (менеджер)
     * @param pollId - идентификатор опроса
     * @param page - номер вопроса, начиная с 1, необязательный
     * @return - ДТО с вопросами, респондентами и оценками
     */
    @GET
    @Path(value = "/rating/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRating(@PathParam("poll_id") String pollId, @CookieParam(CookieConst.USER_ID) String userId,
                              @QueryParam("page") Integer page) {
        log.info("Получен запрос /rating/ " + pollId + " для менеджера: " + userId);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, RatingResponseDto>()
                .validate(v -> resultUserValidateService.validateDataResultUser(pollId, userId))
                .process(x -> gradeService.countRating(pollId, page))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    @GET
    @Path(value = "/questions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions(@CookieParam(CookieConst.USER_ID) String userId) {
        log.info("Получен запрос /questions/ для пользователя: " + userId);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, QuestionsResponseDto>()
                .validate(v -> userValidateService.userIdValidate(userId))
                .process(x -> questionService.getAllQuestionsForManager(userId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }


    /**
     * endpoint получения данных о пользователи по идентификатору пользователя
     *
     * @param userId - идентификатор пользователя
     * @return - ДТО с информацией о пользователе
     */
    @GET
    @Path("getuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@CookieParam("user-id") String userId) {
        log.info("Получен запрос /getuser");
        NewCookie cookie = new NewCookie(Cookie.USER_ID.getValue(), userId);
        return new HttpRequestHandler<String, UserResponseDto>()
                .validate(v -> userValidateService.userIdValidate(userId))
                .process(x -> userService.getRespondentByUserId(userId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint получения данных об опросе по идентификатору опроса
     *
     * @param userId - идентификатор пользователя
     * @param pollId - идентификатор опроса
     * @return - ДТО с информацией об опросе
     */
    @GET
    @Path("polls/{poll_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoolById(@CookieParam("user-id") String userId, @PathParam("poll_id") String pollId) {
        log.info("Получен запрос pools/" + pollId);
        NewCookie cookie = new NewCookie(Cookie.USER_ID.getValue(), userId);
        return new HttpRequestHandler<String, PollByIdResponseDto>()
            .validate(v -> pollValidateService.validatePollById(userId, pollId))
            .process(x -> pollService.getPollById(pollId, userId))
            .convert(objectConvertService::convertToJson)
            .forArgument(userId, cookie);
    }

    /**
     * endpoint обновления победителя в паре по конкретному вопросу текущего опроса
     *
     * @param userId              - идентификатор пользователя
     * @param updateWinnerRequestDto - данные запроса
     * @return - ДТО с информацией об опросе
     */
    @POST
    @Path("updatewinner")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWinner(@CookieParam(CookieConst.USER_ID) String userId,
                                 @RequestBody UpdateWinnerRequestDto updateWinnerRequestDto) {
        log.info("Получен запрос /updatewinner с телом: {}", updateWinnerRequestDto);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> ratingRequestValidateService.validateUpdateWinnerRequestDto(userId, updateWinnerRequestDto))
                .process(x -> winnerCompleteService.updateWinner(userId, updateWinnerRequestDto))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

    /**
     * endpoint который отдаёт вопрос и сформированные пары по опросу
     *
     * @param userId - идентификатор пользователя
     * @param pollId - pollId запроса
     * @return - ДТО с информацией об опросе
     */
    @GET
    @Path("comparepairsofpoll/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComparePairsOfPoll(@CookieParam(CookieConst.USER_ID) String userId,
                                          @PathParam(RequestParams.POLL_ID) String pollId) {
        log.info("Получен запрос /comparepairsofpoll с poll_id: {}", pollId);
        NewCookie cookie = new NewCookie(CookieConst.USER_ID, userId);
        return new HttpRequestHandler<String, ResponseMessage>()
                .validate(v -> pollValidateService.validateComparePairsOfPoll(userId, pollId))
                .process(x -> pollService.getComparePairOfPollDto(userId, pollId))
                .convert(objectConvertService::convertToJson)
                .forArgument(userId, cookie);
    }

}

