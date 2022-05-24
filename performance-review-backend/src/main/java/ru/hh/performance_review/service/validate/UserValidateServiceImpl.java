package ru.hh.performance_review.service.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.service.validate.utils.Utils;


@Slf4j
@Service
public class UserValidateServiceImpl implements UserValidateService {

    @Override
    public void userIdValidate(String userId) {
        Utils.validateUuidAsString(userId, "userId");
    }
}
