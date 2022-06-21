package ru.hh.performance_review.dto.response;

import lombok.Data;

import javax.ws.rs.core.NewCookie;

@Data
public class HttpResponseWrapper implements ResponseMessage {

    private ResponseMessage responseMessage;
    private NewCookie cookie;


}
