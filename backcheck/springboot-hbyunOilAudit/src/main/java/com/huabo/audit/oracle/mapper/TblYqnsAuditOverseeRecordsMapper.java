package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import org.apache.ibatis.annotations.Insert;

/**
 * @Classname TblYqnsAuditWorkRecordsMapper
 * @Description TODO 央企内审-审计实施-审计督导记录
 * @Date 2023/10/9 10:16
 * @Created by GJ.C
 */
public interface TblYqnsAuditOverseeRecordsMapper extends BaseMapper<TblYqnsAuditOverseeRecordsEntity> {



    @Insert("DELETE FROM TBL_YQNS_OVERSEE_RECORDS_ATT WHERE DUDAOID = #{DUDAOID}")
    void delFileRelation(Long DUDAOID);

    @Insert("INSERT INTO TBL_YQNS_OVERSEE_RECORDS_ATT(ATTID, DUDAOID) VALUES (#{attid}, #{id})")
    void insetFileRelation(String attid, Long id);

}