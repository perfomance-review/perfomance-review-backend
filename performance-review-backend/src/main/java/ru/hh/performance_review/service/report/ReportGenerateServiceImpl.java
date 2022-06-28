package ru.hh.performance_review.service.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.dto.response.UsersInfoResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseRawDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.service.UserService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportGenerateServiceImpl implements ReportGenerateService {

    private final UserService userService;
    private static String[] columns = {"id", "email", "FIO", "role"};

    @Override
    public byte[] getReport(String userId) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Users");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(font);

        Row header = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(columns[i]);
            headerCell.setCellStyle(headerStyle);
        }

        UsersInfoResponseDto usersInfoResponseDto = userService.getAllUsers();
        List<UsersInfoResponseRawDto> usersInfos = usersInfoResponseDto.getUsersInfo();


        for (int i = 0 , c = 1; i < usersInfos.size(); i++ , c++) {
            Row row = sheet.createRow(c);
            row.createCell(0).setCellValue(usersInfos.get(i).getUserId().toString());
            row.createCell(1).setCellValue(usersInfos.get(i).getEmail());
            row.createCell(2).setCellValue(getFullName(usersInfos.get(i)));
            row.createCell(3).setCellValue(usersInfos.get(i).getRole().name());
        }

        for (int i = 0; i < columns.length ; i++) {
            sheet.autoSizeColumn(i);
        }

        try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
            workbook.write(os);
            return os.toByteArray();
        } catch (IOException e){
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
