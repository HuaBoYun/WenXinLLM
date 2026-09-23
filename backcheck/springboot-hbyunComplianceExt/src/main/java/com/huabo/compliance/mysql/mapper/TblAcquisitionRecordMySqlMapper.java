package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblAcquisitionRecordMySql;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;


public interface TblAcquisitionRecordMySqlMapper extends BaseMapper<TblAcquisitionRecordMySql> {


    @SelectProvider(type = TblAcquisitionRecordMapperSqlMySqlConfig.class, method = "selectListByPageInfo")
    @Results({
            @Result(column = "RECORDID", property = "recordId"),
            @Result(column = "RECORDYEAR", property = "recordYear"),
            @Result(column = "RECORDSTART", property = "recordStart"),
            @Result(column = "RECORDEND", property = "recordEnd"),
            @Result(column = "RECORDTIME", property = "recordTime"),
            @Result(column = "REALNAME", property = "staffName"),
            @Result(column = "ORGNAME", property = "orgName"),
            @Result(column = "RETYPE", property = "reType"),
    })
    List<TblAcquisitionRecordMySql> selectListByPageInfo(PageInfo<TblAcquisitionRecordMySql> pageInfo, BigDecimal orgid);

    @Select("SELECT COUNT(*) FROM TBL_ACQUISITION_RECORD R LEFT JOIN TBL_STAFF F ON R.STAFFID = F.STAFFID LEFT JOIN TBL_ORGANIZATION O ON R.ORGID = O.ORGID WHERE R.ORGID = #{orgid}")
    Integer selectCountByPageInfo(BigDecimal orgid);
}
