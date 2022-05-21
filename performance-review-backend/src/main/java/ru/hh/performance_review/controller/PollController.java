package ru.hh.performance_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.hh.performance_review.dto.GetPollResponseDto;
import ru.hh.performance_review.model.PollStatus;
import ru.hh.performance_review.service.PollService;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.StartPollService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class PollController {

    private final PollService pollService;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final StartPollService startPollService;
    private final static String defaultUserId = "00000000-0000-0000-0000-000000000001";


    @GET
    @Path("polls")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getPolls(@CookieParam("user-id") String userId) {
        log.info("Получен запрос /polls userRole");
    /*
    TODO:
    1. Если аутентификация уже выполнена - передать в pollService информацию о пользователе: id
     */
        try {
            userId = Optional.ofNullable(userId).orElse(userService.getAnyRespondentId());
            log.info("userId:{}", userId);
            final List<GetPollResponseDto> polls = pollService.getPolls(userId);
            String response = objectMapper.writeValueAsString(polls);
            log.info("Ответ на запрос:{}", response);
            return Response.ok(response).build();
        } catch (Exception e) {
            String errorMsg = String.format("Ошибка обработки запроса /polls %s", e.getLocalizedMessage());
            log.error(errorMsg);
            log.error("", e);
            return Response.serverError().build();
        }
    }

    @POST
    @Path(value = "/start/{poll_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startPoll(@PathParam("poll_id") String pollId, @CookieParam("user-id") String userId, @RequestBody List<String> includedIdsString) {
        try {
            log.info("userId:{}", userId);
            userId = Optional.ofNullable(userId).orElse(defaultUserId);
            startPollService.changeStatusPoll(UUID.fromString(pollId), UUID.fromString(userId), PollStatus.PROGRESS);
            if (!CollectionUtils.isEmpty(includedIdsString)) {
                List<UUID> includedIds = includedIdsString.stream()
                        .map(UUID::fromString)
                        .collect(Collectors.toList());
                startPollService.saveExcluded(UUID.fromString(pollId), UUID.fromString(userId), includedIds);
            }
            return Response.ok().build();
        } catch (Exception e) {
            String errorMsg = String.format("Ошибка обработки запроса /start/{poll_id} %s", e.getLocalizedMessage());
            log.error(errorMsg);
            log.error("", e);
            return Response.serverError().build();
        }
    }



}

