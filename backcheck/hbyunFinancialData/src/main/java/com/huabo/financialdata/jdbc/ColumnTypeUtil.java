package com.huabo.financialdata.jdbc;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ColumnTypeUtil {

    private static final Pattern TEXT_PATTERN = Pattern.compile("[a-zA-Z]+");
    private static final Pattern DECIMAL_PATTERN = Pattern.compile("[-+]?\\d*\\.?\\d+");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\d+");

    public static boolean isText(String str) {
        return TEXT_PATTERN.matcher(str).matches();
    }

    public static boolean isDecimal(String str) {
        return DECIMAL_PATTERN.matcher(str).matches();
    }

    public static boolean isDate(String str) {
        return DATE_PATTERN.matcher(str).matches();
    }

    public static boolean isTime(String str) {
        return TIME_PATTERN.matcher(str).matches();
    }

    public static boolean isInteger(String str) {
        return INTEGER_PATTERN.matcher(str).matches();
    }

    public static FieldTypeEnum getCellValueType(String value) {
        if (StringUtils.isBlank(value)) {
            return FieldTypeEnum.STRING;
        }

        if (isInteger(value)) {
            return FieldTypeEnum.INTEGER;
        } else if (isTime(value)) {
            return FieldTypeEnum.TIME;
        } else if (isDate(value)) {
            return FieldTypeEnum.DATE;
        } else if (isDecimal(value)) {
            return FieldTypeEnum.DECIMAL;
        } else if (value.length() > 255) {
            return FieldTypeEnum.TEXT;
        }else{
            return FieldTypeEnum.STRING;
        }
    }


    public static void main(String[] args) {
        String[] samples = {"汉字"};

        for (String sample : samples) {
            System.out.println(sample + ":");
            System.out.println("Text: " + isText(sample));
            System.out.println("Decimal: " + isDecimal(sample));
            System.out.println("Date: " + isDate(sample));
            System.out.println("Time: " + isTime(sample));
            System.out.println("Integer: " + isInteger(sample));
        }
    }
}
