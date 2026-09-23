package com.huabo.system.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblPersonalTrain;

import java.math.BigDecimal;
import java.util.List;

public class TblPersonTrainMapperSqlConfig {


    public String updateTrain(TblPersonalTrain train) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE tbl_personal_train SET  trainwitness = "+train.getTrainwitness()+" ");

        if(train.getTrainevidence() != null && !"".equals(train.getTrainevidence() )) {
            sql.append(" , trainevidence = '"+train.getTrainevidence()+"'");
        }
        if(train.getTrainlocation() != null && !"".equals(train.getTrainlocation())) {
            sql.append(" , trainlocation = '"+train.getTrainlocation()+"'");
        }
        if(train.getTraintime() != null && !"".equals(train.getTraintime())) {
            sql.append(" ,CREATETIME = ").append(DataBaseSqlConfig.getDateStrFormat(train.getTraintime()));
        }
        sql.append(" WHERE trainid = '"+train.getTrainid()+"'");
        return sql.toString();
    }


    public String saveTrain(TblPersonalTrain ts) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO tbl_personal_train (trainid");
        StringBuffer value = new StringBuffer(" VALUES (").append(RandomUtil.uuBigDecimalId());

        if(ts.getTrainwitness() != null && !"".equals(ts.getTrainwitness())) {
            column.append(",trainwitness");
            value.append(",'"+ts.getTrainwitness()+"'");
        }
        if(ts.getTrainevidence() != null && !"".equals(ts.getTrainevidence())) {
            column.append(",trainevidence");
            value.append(",'"+ts.getTrainevidence()+"'");
        }
        if(ts.getTrainlocation() != null && !"".equals(ts.getTrainlocation())) {
            column.append(",trainlocation");
            value.append(",'"+ts.getTrainlocation()+"'");
        }
        if(ts.getTraintime() != null && !"".equals(ts.getTraintime())) {
            column.append(",traintime");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(ts.getTraintime()));
        }
        if(ts.getStaffid() != null && !"".equals(ts.getStaffid())) {
            column.append(",staffid");
            value.append(",'"+ts.getStaffid()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
    
}
