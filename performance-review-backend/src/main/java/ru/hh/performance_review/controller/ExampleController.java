package ru.hh.performance_review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hh.performance_review.service.ExampleService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/")
public class ExampleController {

    private static final Logger logger = LoggerFactory.getLogger(ExampleController.class);
    private final ExampleService exampleService;
    private final ObjectMapper objectMapper;

    @GET
    @Path("/checkevennumber/{number}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response checkEvenNumber(@PathParam("number") Integer number) {
        try {
            boolean res = exampleService.isEvenNumber(number);
            String val = res
                    ? String.format("Переданное число '%d' является четным", number)
                    : String.format("Переданное число '%d' является нечетным", number);
            String response = objectMapper.writeValueAsString(val);
            logger.info(response);
            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("", e);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), e.getLocalizedMessage()).build();
        }
    }
}

