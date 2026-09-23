package com.huabo.monitor.mapper;

import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblTesttaskAtt;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-09
 */
public interface TblTesttaskAttMapper extends BaseMapper<TblTesttaskAtt> {

    
     @Insert("INSERT INTO TBL_TESTTASK_ATT(ATTID,TESTTASKID) VALUES(#{attid},#{testtaskid} )")
     void insertEntity(@Param("attid")BigDecimal attid,@Param("testtaskid")BigDecimal testtaskid);
     
     @Select("select * from  TBL_TESTTASK_ATT where attid=#{attid}")
     TblTesttaskAtt getOne(@Param("attid")BigDecimal attid);
     
}
