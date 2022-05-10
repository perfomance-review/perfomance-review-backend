package ru.hh.performance_review;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.hh.nab.starter.NabApplication;
import ru.hh.nab.testbase.NabTestBase;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(classes = {AppTestConfig.class})
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
        String url = "/checkevennumber/10";
        Response response = createRequest(url)
                .buildGet()
                .invoke();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}
