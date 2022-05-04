package ru.hh.performance_review.service;

import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl implements ExampleService {

    @Override
    public boolean isEvenNumber(Integer number) {
        if (number == null || number <= 0) {
            throw new IllegalArgumentException(String.format("Некорректное число:%s", number));
        }
        return number % 2 == 0;
    }
}
