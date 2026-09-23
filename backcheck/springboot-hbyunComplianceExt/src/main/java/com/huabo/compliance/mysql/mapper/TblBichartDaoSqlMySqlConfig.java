package com.huabo.compliance.mysql.mapper;


import com.huabo.compliance.mysql.entity.TblBiChartMySql;

public class TblBichartDaoSqlMySqlConfig {

    public String insertBichart(TblBiChartMySql biChartMySql) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_BI_CHART (CHARTID");
        StringBuffer value = new StringBuffer(" VALUES (" + biChartMySql.getChartid() + " ");

        if (biChartMySql.getChartname() != null) {
            column.append(",CHARTNAME");
            value.append(",'" + biChartMySql.getChartname() + "'");
        }
        if (biChartMySql.getCharttype() != null) {
            column.append(",CHARTTYPE");
            value.append(",'" + biChartMySql.getCharttype() + "'");
        }
        if (biChartMySql.getDatatype() != null) {
            column.append(",DATATYPE");
            value.append(",'" + biChartMySql.getDatatype() + "'");
        }
        if (biChartMySql.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + biChartMySql.getMemo() + "'");
        }
        if (biChartMySql.getPointx() != null) {
            column.append(",POINTX");
            value.append(",'" + biChartMySql.getPointx() + "'");
        }
        if (biChartMySql.getHeight() != null) {
            column.append(",HEIGHT");
            value.append(",'" + biChartMySql.getHeight() + "'");
        }
        if (biChartMySql.getWidth() != null) {
            column.append(",WIDTH");
            value.append(",'" + biChartMySql.getWidth() + "'");
        }
        if (biChartMySql.getTitle() != null) {
            column.append(",TITLE");
            value.append(",'" + biChartMySql.getTitle() + "'");
        }
        if (biChartMySql.getPointy() != null) {
            column.append(",POINTY");
            value.append(",'" + biChartMySql.getPointy() + "'");
        }
        if (biChartMySql.getAxisx() != null) {
            column.append(",AXISX");
            value.append(",'" + biChartMySql.getAxisx() + "'");
        }
        if (biChartMySql.getAxisy() != null) {
            column.append(",AXISY");
            value.append(",'" + biChartMySql.getAxisy() + "'");
        }
        if (biChartMySql.getConfiguration() != null) {
            column.append(",CONFIGURATION");
            value.append(",'" + biChartMySql.getConfiguration() + "'");
        }
        if (biChartMySql.getDivid() != null) {
            column.append(",DIVID");
            value.append(",'" + biChartMySql.getDivid() + "'");
        }
        if (biChartMySql.getPageType() != null) {
            column.append(",PAGETYPE");
            value.append(",'" + biChartMySql.getPageType() + "'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }
}

