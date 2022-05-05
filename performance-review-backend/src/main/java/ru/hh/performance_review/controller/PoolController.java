package ru.hh.performance_review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.dto.PoolGetDtoResponse;
import ru.hh.performance_review.service.PoolService;

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
@Path("/pools")
public class PoolController {

  private static final Logger logger = LoggerFactory.getLogger(PoolController.class);
  private final PoolService poolService;
  private final ObjectMapper objectMapper;

  @Inject
  public PoolController(final PoolService poolService, final ObjectMapper objectMapper) {
    this.poolService = poolService;
    this.objectMapper = objectMapper;
  }

  @GET()
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getPools() throws JsonProcessingException {
    /*
    TODO:
    1. Если аутентификация уже выполнена - передать в poolService информацию о пользователе: id
     */
    final List<PoolGetDtoResponse> pools = poolService.getPools("user");

    return Response.ok(objectMapper.writeValueAsString(pools)).build();
  }
}

