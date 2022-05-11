package ru.hh.performance_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.dao.QuestionDao;
import ru.hh.performance_review.model.Question;
import ru.hh.performance_review.service.ExampleService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class ExampleController {

    private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);
    private final ExampleService exampleService;
    private final ObjectMapper objectMapper;
    private final QuestionDao questionDao;

    @GET
    @Path("/checkevennumber/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkEvenNumber(@PathParam("number") String number) {
        try {
            boolean res = exampleService.isEvenNumber(Integer.valueOf(number));
            String val = res
                    ? String.format("Переданное число '%s' является четным", number)
                    : String.format("Переданное число '%s' является нечетным", number);
            String response = objectMapper.writeValueAsString(val);
            logger.info(response);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("", e);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), e.getLocalizedMessage()).build();
        }
    }

    @GET
    @Path("generateTestQuestions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response generateTestQuestions() {
        try {
            UUID id = UUID.randomUUID();
            Question question = questionDao.getByID(Question.class, id);
            String responseMsg = question == null ? String.format("Не найден question с id:%s", id) : question.toString();
            return Response.ok(responseMsg).build();
        } catch (Exception e) {
            log.error("", e);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), e.getLocalizedMessage()).build();
        }
    }
}

