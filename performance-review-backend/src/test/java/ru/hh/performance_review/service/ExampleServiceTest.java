package ru.hh.performance_review.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.hh.performance_review.AppTestConfig;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class ExampleServiceTest {

    @Inject
    private ExampleService exampleService;


    @Test
    public void isEvenNumberTest() {
        boolean result = exampleService.isEvenNumber(10);
        assertTrue(result);

        result = exampleService.isEvenNumber(9);
        assertFalse(result);
    }

}