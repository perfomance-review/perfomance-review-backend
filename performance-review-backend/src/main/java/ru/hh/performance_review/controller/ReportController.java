package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.PrintWriter;

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
    public void getUserInfoReport(@Context HttpServletResponse response) {

        try {

            ReportRequestContextDto reportRequestContextDto = new ReportRequestContextDto()
                    .setReportType(ReportType.USERS_INFO);

            final ReportResponseContextDto reportResponseContextDto = reportDocumentService.createReportContext(reportRequestContextDto);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + reportRequestContextDto.getReportType().getReportName() + "\"");

            try (
                    PrintWriter printWriter = response.getWriter();
                    CSVPrinter csvPrinter = new CSVPrinter(printWriter, CSVFormat.DEFAULT.withHeader("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            ) {
                csvPrinter.printRecord(reportResponseContextDto);
            } catch (IOException e) {
                log.error("Произошла ошибка при загрузки данных в csv файл");
                e.printStackTrace();
            }
            log.info("Загрузка данных в csv файл успешно завершена");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//            return Response.status(Response.Status.OK.getStatusCode())
//                    .type(MediaType.APPLICATION_OCTET_STREAM)
//                    .header("Content-Disposition", String.format("attachment; filename=\"%s\"", reportRequestContextDto.getReportType().getReportName()))
//                    .entity(reportResponseContextDto.getReportBytes())
//                    .build();
//
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity(objectConvertService.convertToJson(new ErrorDto(InternalErrorCode.INTERNAL_ERROR, e.getMessage())))
//                    .build();
//        }

