package ru.hh.performance_review.service.report.utils;

import lombok.experimental.UtilityClass;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@UtilityClass
public class FontUtils {

    public static CellStyle buildBodyStyle(XSSFWorkbook workbook, int fontHeight) {
        XSSFFont fontBody = workbook.createFont();
        fontBody.setFontName("Calibri");
        fontBody.setFontHeightInPoints((short) fontHeight);
        fontBody.setScheme(FontScheme.NONE);
        fontBody.setFamily(FontCharset.ANSI.getNativeId());

        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        bodyStyle.setFillPattern(FillPatternType.NO_FILL);
        bodyStyle.setFont(fontBody);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setAlignment(HorizontalAlignment.LEFT);
        bodyStyle.setVerticalAlignment(VerticalAlignment.TOP);
        bodyStyle.setWrapText(true);

        return bodyStyle;
    }

    public static CellStyle buildHeaderTableStyleCenterAlign(XSSFWorkbook workbook) {
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontName("Calibri");
        fontHeader.setFontHeightInPoints((short) 11);
        fontHeader.setScheme(FontScheme.NONE);
        fontHeader.setBold(true);
        fontHeader.setFamily(FontCharset.ANSI.getNativeId());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(fontHeader);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        return headerStyle;
    }

    public static CellStyle buildHeaderTableStyleLeftAlign(XSSFWorkbook workbook) {
        CellStyle headerStyle = buildHeaderTableStyleCenterAlign(workbook);
        headerStyle.setAlignment(HorizontalAlignment.LEFT);
        return headerStyle;
    }

    public static CellStyle buildHeaderManagerStyle(XSSFWorkbook workbook) {
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontName("Calibri");
        fontHeader.setFontHeightInPoints((short) 12);
        fontHeader.setScheme(FontScheme.NONE);
        fontHeader.setBold(true);
        fontHeader.setFamily(FontCharset.ANSI.getNativeId());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFont(fontHeader);
        headerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return headerStyle;
    }

    public static CellStyle buildHeaderNameStyle(XSSFWorkbook workbook, int fontHeight) {
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontName("Calibri");
        fontHeader.setFontHeightInPoints((short) fontHeight);
        fontHeader.setScheme(FontScheme.NONE);
        fontHeader.setBold(true);
        fontHeader.setFamily(FontCharset.ANSI.getNativeId());

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        headerStyle.setFont(fontHeader);
        headerStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
        return headerStyle;
    }


}
