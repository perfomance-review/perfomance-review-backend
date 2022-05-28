package ru.hh.performance_review.service;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.AppTestConfig;
import ru.hh.performance_review.dao.base.CommonDao;
import ru.hh.performance_review.helper.AbstractResourceLoadingTest;
import ru.hh.performance_review.model.User;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class CommonDaoTest extends AbstractResourceLoadingTest {

    @Inject
    private CommonDao commonDao;


    @SneakyThrows
    @Transactional
    @Rollback
    @Test
    public void testSaveAfterGetUser() {
        User expected = load("/json/user_common.json", User.class);

        commonDao.save(expected);

        User userResult = commonDao.getByID(User.class, expected.getUserId());

        Assert.assertNotNull(userResult);
        Assert.assertEquals(expected.getUserId(), userResult.getUserId());
    }
}