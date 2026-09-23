package com.huabo.system.mapper;


import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblProcessAnalysis;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-26
 */
@Mapper
public interface TblProcessAnalysisMapper  extends BaseMapper<TblProcessAnalysis> {
	
    @Insert("INSERT INTO TBL_PROCESS_ANALYSIS VALUES(#{id},#{processname},#{processid},#{userid},null,#{usertaskid})")
    void insertUser(String processname,String processid,String userid,String rolename,String usertaskid, BigDecimal id) throws Exception;
    
    @Insert("INSERT INTO TBL_PROCESS_ANALYSIS VALUES(#{id},#{processname},#{processid},null,#{rolename},#{usertaskid})")
    void insertRole(String processname,String processid,String userid,String rolename,String usertaskid, BigDecimal id) throws Exception;
}
