package ru.hh.performance_review.service.report.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.consts.ReportType;
import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseRawDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.report.ReportBuilder;
import ru.hh.performance_review.service.report.utils.FontUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportDocumentUsersInfoBuilder implements ReportBuilder {

    private final UserService userService;
    private final static String[] COLUMNS = {"№", "ФИО", "Почта", "Должность"};

    @Override
    public ReportType getReportType() {
        return ReportType.USERS_INFO;
    }

    @Override
    public ReportResponseContextDto createReportResponseContextDto(ReportRequestContextDto reportRequestContextDto) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Users");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);


        CellStyle headerStyle = FontUtils.buildHeaderTableStyleCenterAlign((XSSFWorkbook) workbook);

        Row header = sheet.createRow(0);

        for (int i = 0; i < COLUMNS.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(COLUMNS[i]);
            headerCell.setCellStyle(headerStyle);
        }

        UsersInfoResponseDto usersInfoResponseDto = userService.getAllUsers();
        List<UsersInfoResponseRawDto> usersInfos = usersInfoResponseDto.getUsersInfo();


        CellStyle bodyStyle = FontUtils.buildBodyStyle((XSSFWorkbook) workbook, 11);
        for (int i = 0, c = 1; i < usersInfos.size(); i++, c++) {
            Row row = sheet.createRow(c);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(String.valueOf(c));
            cell0.setAsActiveCell();
            cell0.setCellStyle(bodyStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(getFullName(usersInfos.get(i)));
            cell1.setCellStyle(bodyStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(usersInfos.get(i).getEmail());
            cell2.setCellStyle(bodyStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(usersInfos.get(i).getRole().name());
            cell3.setCellStyle(bodyStyle);
        }

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            workbook.write(os);
            return ReportResponseContextDto.builder()
                    .reportName(getReportType().getReportName())
                    .reportBytes(os.toByteArray())
                    .build();
        } catch (IOException e) {
            log.error("", e);
            throw new BusinessServiceException(
                    String.format(InternalErrorCode.REPORT_GENERATE_ERROR.getErrorDescription(), "XLSX"),
                    InternalErrorCode.REPORT_GENERATE_ERROR);
        }

    }

    private String getFullName(UsersInfoResponseRawDto usersInfo) {
        String fN = StringUtils.trimToEmpty(usersInfo.getFirstName());
        String sN = StringUtils.trimToEmpty(usersInfo.getSecondName());
        String mN = StringUtils.trimToEmpty(usersInfo.getMiddleName());
        return String.format("%s %s %s", fN, sN, mN).trim();
    }


}
