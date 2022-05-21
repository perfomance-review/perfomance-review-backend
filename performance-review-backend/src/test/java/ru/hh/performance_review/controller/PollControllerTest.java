package ru.hh.performance_review.controller;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.hh.nab.starter.NabApplication;
import ru.hh.nab.testbase.NabTestBase;
import ru.hh.performance_review.AppTestConfig;

import javax.ws.rs.core.Response;

@ContextConfiguration(classes = {AppTestConfig.class})
public class PollControllerTest extends NabTestBase {

    @Override
    protected NabApplication getApplication() {
        return NabApplication.builder().configureJersey().bindToRoot().build();
    }

    @Test
    public void getPolls_success() {
        String url = "/polls";
        Response response = createRequest(url)
                .buildGet()
                .invoke();

        //assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}