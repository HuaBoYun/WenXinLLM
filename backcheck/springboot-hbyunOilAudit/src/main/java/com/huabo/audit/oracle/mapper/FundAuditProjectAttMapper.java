package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.FundAuditProjectAttEntity;

import io.lettuce.core.dynamic.annotation.Param;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-15 00:00
 **/
public interface FundAuditProjectAttMapper extends BaseMapper<FundAuditProjectAttEntity> {

    @Select("SELECT FUND_PROJECT_ATT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

    @Insert("INSERT INTO TBL_YQNS_FUND_AUDIT_PROJECT_GL(CWID,GLID) VALUES(#{id},#{glid})")
	void insertgl(Long id,String glid);
  
    @Delete("DELETE FROM TBL_YQNS_FUND_AUDIT_PROJECT_GL WHERE CWID=#{id}")
   	void deletegl(Long id);
}
