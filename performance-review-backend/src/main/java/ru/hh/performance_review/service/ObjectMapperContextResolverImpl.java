package ru.hh.performance_review.service;

import javax.ws.rs.ext.ContextResolver;

public class ObjectMapperContextResolverImpl<ObjectMapper> implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperContextResolverImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return objectMapper;
    }
}
