package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.consts.ReportType;
import ru.hh.performance_review.controller.base.CookieConst;
import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;
import ru.hh.performance_review.exception.ErrorDto;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.exception.ValidateException;
import ru.hh.performance_review.security.annotation.JwtTokenCookie;
import ru.hh.performance_review.security.annotation.PerformanceReviewSecured;
import ru.hh.performance_review.security.context.SecurityContext;
import ru.hh.performance_review.security.context.SecurityRole;
import ru.hh.performance_review.service.report.ReportDocumentService;
import ru.hh.performance_review.service.sereliazation.ObjectConvertService;
import ru.hh.performance_review.service.validate.UserValidateService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/reports")
public class ReportController {

    private final ReportDocumentService reportDocumentService;
    private final UserValidateService userValidateService;
    private final ObjectConvertService objectConvertService;
//    private static final String USER_ID = "00000000-0000-0000-0000-00000000000a";

    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER})
    @GET
    @Path("/created_polls.xlsx")
    @Produces({
            "application/excel",
            "application/vnd.ms-excel",
            "application/x-excel",
            "application/x-msexcel",
    })
    public Response getCreatedPollReport(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken) {

        String manager_id = SecurityContext.getUserId();
        try {
            ReportRequestContextDto reportRequestContextDto = new ReportRequestContextDto()
                    .setReportType(ReportType.CREATED_POLLS)
                    .setUserId(manager_id);
            ReportResponseContextDto reportResponseContextDto = reportDocumentService.createReportContext(reportRequestContextDto);

            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(reportResponseContextDto.getReportBytes())
                    .build();

        } catch (ValidateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(objectConvertService.convertToJson(new ErrorDto(e)))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(objectConvertService.convertToJson(new ErrorDto(InternalErrorCode.INTERNAL_ERROR, e.getMessage())))
                    .build();
        }
    }

    @PerformanceReviewSecured(roles = {SecurityRole.ADMINISTRATOR, SecurityRole.MANAGER})
    @GET
    @Path("/{poll_id}/poll_result.xlsx")
    @Produces({
            "application/excel",
            "application/vnd.ms-excel",
            "application/x-excel",
            "application/x-msexcel",
    })
    public Response getPollResultReport(@JwtTokenCookie @CookieParam(CookieConst.ACCESS_TOKEN) String jwtToken,
                                        @PathParam("poll_id") String pollId) {

        String id = SecurityContext.getUserId();
        try {
            ReportRequestContextDto reportRequestContextDto = new ReportRequestContextDto()
                    .setReportType(ReportType.POLL_RESULTS)
                    .setPollId("12dd94c8-a5a1-480f-952a-4e54b8dc7272")
                    .setUserId(id);
            ReportResponseContextDto reportResponseContextDto = reportDocumentService.createReportContext(reportRequestContextDto);

            return Response.status(Response.Status.OK.getStatusCode())
                    .entity(reportResponseContextDto.getReportBytes())
                    .build();

        } catch (ValidateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(objectConvertService.convertToJson(new ErrorDto(e)))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(objectConvertService.convertToJson(new ErrorDto(InternalErrorCode.INTERNAL_ERROR, e.getMessage())))
                    .build();
        }
    }

    @GET
    @Path("/users_info")
    @Produces({
            MediaType.APPLICATION_OCTET_STREAM,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    })
    public Response getUserInfoReport() {

        try {

            ReportRequestContextDto reportRequestContextDto = new ReportRequestContextDto()
                    .setReportType(ReportType.USERS_INFO);

            ReportResponseContextDto reportResponseContextDto = reportDocumentService.createReportContext(reportRequestContextDto);

            return Response.status(Response.Status.OK.getStatusCode())
                    .type(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", String.format("attachment; filename=\"%s\"", reportRequestContextDto.getReportType().getReportName()))
                    .entity(reportResponseContextDto.getReportBytes())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(objectConvertService.convertToJson(new ErrorDto(InternalErrorCode.INTERNAL_ERROR, e.getMessage())))
                    .build();
        }
    }
}
