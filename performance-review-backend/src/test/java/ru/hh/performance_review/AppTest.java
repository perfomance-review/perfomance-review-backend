package ru.hh.performance_review;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.hh.nab.starter.NabApplication;
import ru.hh.nab.testbase.NabTestBase;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {AppTestConfig.class, DataBaseTestConfig.class})
public class AppTest extends NabTestBase {

    @Override
    protected NabApplication getApplication() {
        return NabApplication.builder().configureJersey().bindToRoot().build();
    }

    @Before
    public void before() {
    }

    @Test
    public void checkEvenNumber_success() {
        for (int i = 1; i < 11; i++) {
            String url = String.format("/checkevennumber/%s", i);
            Response response = createRequest(url)
                    .buildGet()
                    .invoke();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        }
    }

    @Test
    public void checkEvenNumber_negative() {
        Response response = createRequest("/checkevennumber/-1")
                .buildGet()
                .invoke();

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
