package ru.hh.performance_review.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.springframework.context.annotation.*;
import ru.hh.performance_review.security.jwt.JwtTokenProvider;
import ru.hh.performance_review.service.ObjectMapperContextResolverImpl;

import javax.ws.rs.ext.ContextResolver;

@EnableAspectJAutoProxy
@Configuration
@ComponentScan({
        "ru.hh.performance_review.mapper",
        "ru.hh.performance_review.dao",
        "ru.hh.performance_review.service",
        "ru.hh.performance_review.controller"
})
public class ApplicationServiceConfig {

    @Bean(name = "objectMapper")
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

    @Primary
    @Bean
    public ContextResolver<ObjectMapper> ObjectMapperContextResolverImpl(ObjectMapper objectMapper){
        return new ObjectMapperContextResolverImpl<>(objectMapper);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public JwtParser jwtParser() {
        return new DefaultJwtParser();
    }
}
