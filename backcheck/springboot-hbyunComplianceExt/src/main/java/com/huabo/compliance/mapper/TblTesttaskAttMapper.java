package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblTesttaskAtt;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

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

    
     @Insert("INSERT INTO TBL_COM_EXT_TESTTASK_ATT(ATTID,TESTTASKID) VALUES(#{attid},#{testtaskid} )")
     void insertEntity(@Param("attid")BigDecimal attid,@Param("testtaskid")BigDecimal testtaskid);
     
}
