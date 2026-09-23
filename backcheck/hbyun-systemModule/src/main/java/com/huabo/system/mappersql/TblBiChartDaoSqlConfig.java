package com.huabo.system.mappersql;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblBiChart;

public class TblBiChartDaoSqlConfig {

    public String insertBichart(TblBiChart biChart) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_BI_CHART (CHARTID");
        StringBuffer value = new StringBuffer(" VALUES ( ").append(RandomUtil.uuBigDecimalId());

        if(biChart.getChartname() != null) {
            column.append(",CHARTNAME");
            value.append(",'"+biChart.getChartname()+"'");
        }
        if(biChart.getCharttype() != null) {
            column.append(",CHARTTYPE");
            value.append(",'"+biChart.getCharttype()+"'");
        }
        if(biChart.getDatatype() != null) {
            column.append(",DATATYPE");
            value.append(",'"+biChart.getDatatype()+"'");
        }
        if(biChart.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'"+biChart.getMemo()+"'");
        }
        if(biChart.getPointx() != null) {
            column.append(",POINTX");
            value.append(",'"+biChart.getPointx()+"'");
        }
        if(biChart.getHeight() != null) {
            column.append(",HEIGHT");
            value.append(",'"+biChart.getHeight()+"'");
        }
        if(biChart.getWidth() != null) {
            column.append(",WIDTH");
            value.append(",'"+biChart.getWidth()+"'");
        }
        if(biChart.getTitle() != null) {
            column.append(",TITLE");
            value.append(",'"+biChart.getTitle()+"'");
        }
        if(biChart.getPointy() != null) {
            column.append(",POINTY");
            value.append(",'"+biChart.getPointy()+"'");
        }
        if(biChart.getAxisx() != null) {
            column.append(",AXISX");
            value.append(",'"+biChart.getAxisx()+"'");
        }
        if(biChart.getAxisy() != null) {
            column.append(",AXISY");
            value.append(",'"+biChart.getAxisy()+"'");
        }
        if(biChart.getConfiguration() != null) {
            column.append(",CONFIGURATION");
            value.append(",'"+biChart.getConfiguration()+"'");
        }
        if(biChart.getDivid() != null) {
            column.append(",DIVID");
            value.append(",'"+biChart.getDivid()+"'");
        }
        if(biChart.getPageType() != null) {
            column.append(",PAGETYPE");
            value.append(",'"+biChart.getPageType()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
