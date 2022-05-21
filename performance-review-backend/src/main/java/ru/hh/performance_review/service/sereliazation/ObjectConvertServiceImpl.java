package ru.hh.performance_review.service.sereliazation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;

@Service
@RequiredArgsConstructor
public class ObjectConvertServiceImpl implements ObjectConvertService {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new BusinessServiceException(e.getMessage(), e,
                    InternalErrorCode.SERIALIZATION_ERROR, String.format("Error serialize %s", o));
        }

    }
}
