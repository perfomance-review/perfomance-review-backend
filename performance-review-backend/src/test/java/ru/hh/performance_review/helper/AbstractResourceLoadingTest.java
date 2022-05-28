package ru.hh.performance_review.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractResourceLoadingTest {

    @Inject
    public ObjectMapper objectMapper;

    protected <T> T load(String resourceName, TypeReference<T> typeReference) throws java.io.IOException {
        return objectMapper.readValue(this.getClass().getResourceAsStream(resourceName), typeReference);
    }

    protected <T> T load(String resourceName, Class<T> implementationClass) throws java.io.IOException {
        return objectMapper.readValue(this.getClass().getResourceAsStream(resourceName), implementationClass);
    }

    protected <T> List<T> loadList(String resourceName, Class<? extends T> implementationClass) throws java.io.IOException {
        return objectMapper.readValue(this.getClass().getResourceAsStream(resourceName),
                objectMapper.getTypeFactory().constructCollectionType(List.class, implementationClass));
    }

    protected <K, V> Map<K, V> loadMap(String resourceName, Class<K> implementationKey, Class<V> implementationClass) throws java.io.IOException {
        return objectMapper.readValue(this.getClass().getResourceAsStream(resourceName),
                objectMapper.getTypeFactory().constructMapType(HashMap.class, implementationKey, implementationClass));
    }


}
