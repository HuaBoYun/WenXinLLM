package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;

public interface TblNbsjTargettypeMapper extends BaseMapper<TblNbsjTargettypeEntity> {
	
    @InsertProvider(method="insertEntity",type=TblNbsjTargettypeMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="targetId", keyColumn="TARGETID")
	void insertEntity(TblNbsjTargettypeEntity target) throws Exception;
    
    @UpdateProvider(method="updateEntity",type=TblNbsjTargettypeMapperSqlConfig.class)
    void updateEntity(TblNbsjTargettypeEntity target) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_TARGETTYPE WHERE TARGETID=#{targetId}")
    TblNbsjTargettypeEntity findbyid(String targetId) throws Exception;
    
    @Delete("Delete from TBL_NBSJ_TARGETTYPE WHERE TARGETID=#{targetId}")
    void deleteInfoById(String targetId) throws Exception;
    
    @Delete("Delete from TBL_NBSJ_TARGETTYPE WHERE TEMPID=#{templeteId}")
    void deleteInfoByTempleteId(String templeteId) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_TARGETTYPE WHERE TEMPID=#{templeteId} and parentId IS NULL ORDER BY TEMPID ")
    TblNbsjTargettypeEntity selectFirstTarget(String templeteId) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_TARGETTYPE WHERE TEMPID=#{templeteId} order by targetid")
    List<TblNbsjTargettypeEntity> selectTargetByTempleteId(String templeteId) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_TARGETTYPE WHERE parentId=#{parentId} order by targetid")
    List<TblNbsjTargettypeEntity> selectByParentId(String parentId) throws Exception;
    
    @Select("SELECT count(*) FROM TBL_NBSJ_TARGETTYPE WHERE TEMPID=#{templeteId} and TARGETID=#{targetId}")
    Integer getCount(String templeteId, BigDecimal targetId) throws Exception;
}
