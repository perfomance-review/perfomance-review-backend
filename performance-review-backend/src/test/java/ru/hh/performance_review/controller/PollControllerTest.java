package ru.hh.performance_review.controller;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.hh.nab.starter.NabApplication;
import ru.hh.nab.testbase.NabTestBase;
import ru.hh.performance_review.AppTestConfig;
import ru.hh.performance_review.controller.base.CookieConst;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {AppTestConfig.class})
public class PollControllerTest extends NabTestBase {

    @Override
    protected NabApplication getApplication() {
        return NabApplication.builder().configureJersey().bindToRoot().build();
    }

    @Test
    public void getPolls_success() {
        String userId = UUID.randomUUID().toString();
        String url = "/polls";
        Response response = createRequest(url)
                .cookie(CookieConst.USER_ID, userId)
                .buildGet()
                .invoke();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}