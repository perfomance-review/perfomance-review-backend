package ru.hh.performance_review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.hh.performance_review.service.report.ReportGenerateService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@Path("/reports/")
public class ReportController {

    private final ReportGenerateService reportGenerateService;


    @GET
    @Path("users.xlsx")
    @Produces({
            "application/excel",
            "application/vnd.ms-excel",
            "application/x-excel",
            "application/x-msexcel",
    })
    public Response getReport() {

        String userId = UUID.randomUUID().toString();
        byte[] fileXLSXbytes = reportGenerateService.getReport(userId);

        return Response.status(Response.Status.OK.getStatusCode())
                .entity(fileXLSXbytes)
                .build();
    }
}
