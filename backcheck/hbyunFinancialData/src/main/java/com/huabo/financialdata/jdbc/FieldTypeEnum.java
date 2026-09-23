package com.huabo.financialdata.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum FieldTypeEnum {

    STRING("text"),
    TEXT("text"),
    INTEGER("integer"),
    DECIMAL("decimal"),
    DATE("date"),
    TIME("time");

    String cellVal;


    public static FieldTypeEnum getColumnType(Set<FieldTypeEnum> cellValueTypeOracleEnums) {
        if (CollectionUtils.isEmpty(cellValueTypeOracleEnums)) {
            return STRING;
        }
        if (cellValueTypeOracleEnums.size() == 1) {
            return cellValueTypeOracleEnums.stream().findFirst().get();
        }

        if (cellValueTypeOracleEnums.contains(TEXT)) {
            return TEXT;
        }
        if (cellValueTypeOracleEnums.contains(STRING)) {
            return STRING;
        }
        if (cellValueTypeOracleEnums.contains(INTEGER) && cellValueTypeOracleEnums.contains(DECIMAL)) {
            return DECIMAL;
        }
        return STRING;
    }

}
