package ru.hh.performance_review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.dto.PollGetDtoResponse;
import ru.hh.performance_review.service.PollService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@Slf4j
@Path("/polls")
public class PollController {

  private static final Logger logger = LoggerFactory.getLogger(PollController.class);
  private final PollService pollService;
  private final ObjectMapper objectMapper;

  @Inject
  public PollController(final PollService pollService, final ObjectMapper objectMapper) {
    this.pollService = pollService;
    this.objectMapper = objectMapper;
  }

  @GET()
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getPolls() throws JsonProcessingException {
    /*
    TODO:
    1. Если аутентификация уже выполнена - передать в pollService информацию о пользователе: id
     */
    final List<PollGetDtoResponse> polls = pollService.getPolls("user");

    return Response.ok(objectMapper.writeValueAsString(polls)).build();
  }
}

