package ru.hh.performance_review.controller.base;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.CannotCreateTransactionException;
import ru.hh.performance_review.dto.response.ResponseMessage;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.ErrorDto;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.function.Consumer;
import java.util.function.Function;

import static ru.hh.performance_review.exception.InternalErrorCode.*;


/**
 * Шаблонный код для обработки запроса в контроллере.
 * Включает в себя фазы валидации, получения данных и обработки исключений.
 *
 * @param <I> тип объекта с входными данными
 * @param <R> тип объекта с возвращаемыми данными
 */
@Slf4j
@NoArgsConstructor
public class HttpRequestHandler<I, R extends ResponseMessage> {

    private Consumer<I> validator;
    private Function<I, R> processor;
    private Function<ResponseMessage, String> converter;

    public HttpRequestHandler<I, R> validate(Consumer<I> validator) {
        this.validator = validator;
        return this;
    }

    public HttpRequestHandler<I, R> process(Function<I, R> processor) {
        this.processor = processor;
        return this;
    }

    public HttpRequestHandler<I, R> convert(Function<ResponseMessage, String> converter) {
        this.converter = converter;
        return this;
    }

    public Response forArgument(I request, NewCookie cookie) {
        log.info("Запрос: {}", request);
        try {
            validator.accept(request);
            R response = processor.apply(request);
            String jsonResponse = converter.apply(response);
            log.info("Ответ: {}", jsonResponse);

            return Response.status(Response.Status.OK.getStatusCode())
                    .cookie(cookie)
                    .entity(jsonResponse)
                    .build();

        } catch (ValidateException e) {
            log.error("", e);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                    .cookie(cookie)
                    .entity(converter.apply(new ErrorDto(e)))
                    .build();
        } catch (BusinessServiceException e) {
            log.error("", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .cookie(cookie)
                    .entity(converter.apply(new ErrorDto(e)))
                    .build();
        } catch (DataAccessResourceFailureException | JDBCConnectionException | CannotCreateTransactionException e) {
            log.error("", e);
            return createErrorResponse(DB_ACCESS_ERROR, cookie);
        } catch (DataAccessException e) {
            log.error("", e);
            return createErrorResponse(DB_SQL_ERROR, cookie);
        } catch (Exception e) {
            log.error("", e);
            return createErrorResponse(INTERNAL_ERROR, cookie);
        }
    }

    private Response createErrorResponse(InternalErrorCode internalErrorCode, NewCookie cookie) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .cookie(cookie)
                .entity(converter.apply(new ErrorDto(internalErrorCode)))
                .build();
    }

}
