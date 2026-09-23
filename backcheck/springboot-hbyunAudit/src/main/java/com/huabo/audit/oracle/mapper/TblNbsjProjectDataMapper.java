package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.util.PageInfo;

@Mapper
public interface TblNbsjProjectDataMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjProjectDataEntity>{
	@Delete("DELETE from TBL_PROJECT_DATAPRE WHERE ID= #{dataId} ")
    void deleteone(String dataId);
	
	@Select("SELECT * from TBL_PROJECT_DATAPRE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjProjectDataEntity> findAll(String projectId);
	
	@Select("SELECT * from TBL_PROJECT_DATAPRE WHERE NOTEID = #{noteid} ")
    TblNbsjProjectDataEntity get(String noteid);
	
	@Select("SELECT * from TBL_PROJECT_DATAPRE WHERE PROJECTID= #{projectId} ")
    List<TblNbsjProjectDataEntity> isNoteCode(String code,String projectId);
	
	
	
	//==
	@Select("SELECT * from TBL_PROJECT_DATAPRE WHERE ID= #{dataId} ")
    TblNbsjProjectDataEntity getById(String dataId);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjProjectDataMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjProjectDataEntity> pageInfo,DataProVo dataProVo,BigDecimal orgid,BigDecimal projectId,BigDecimal type) throws Exception;

    @Select("SELECT TNA.* FROM TBL_PROJECT_DATAPRE TNA  WHERE TNA.ID = #{dataId}")
    @Results({
    	@Result(column="ID",property="id"),
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="DATA_NAME",property="dataName"),
    	@Result(column="DATA_CAPACITY",property="dataCapacity"),
    	@Result(column="DATA_DATE",property="dataDate"),
    	
    	@Result(column="PROJECTNAME",property="projectname"),
    	@Result(column="USERNAME",property="username"),
    	
    	@Result(column="PROJECT_DATAPRE_ID",property="projectDatapreId"),
    	
    })
   	TblNbsjProjectDataEntity selectById(@Param("dataId") BigDecimal dataId) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjProjectDataMapperSqlConfig.class)
    @Results({
    	@Result(column="ID",property="id"),
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="DATA_NAME",property="dataName"),
    	@Result(column="DATA_CAPACITY",property="dataCapacity"),
    	@Result(column="DATA_DATE",property="dataDate"),
    	@Result(column="PROJECTNAME",property="projectname"),
    	@Result(column="USERNAME",property="username"),
    	@Result(column="PROJECT_DATAPRE_ID",property="projectDatapreId"),
    })
	List<TblNbsjProjectDataEntity> selectListByPageInfo(PageInfo<TblNbsjProjectDataEntity> pageInfo,DataProVo dataProVo,BigDecimal orgid,BigDecimal projectId,BigDecimal staffid) throws Exception;

    @Delete("DELETE FROM TBL_PROJECT_DATAPRE WHERE ID = #{dataId}")
    void deleteById(BigDecimal dataId) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjProjectDataMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjProjectDataEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjProjectDataMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="dataId", keyColumn="ID")
	void insertEntity(TblNbsjProjectDataEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjProjectDataMapperSqlConfig.class)
	void updateEntity(TblNbsjProjectDataEntity plan) throws Exception;
    
    
    @InsertProvider(method="saveOld",type=TblNbsjProjectDataMapperSqlConfig.class)
	void saveOld(TblNbsjProjectDataEntity data) throws Exception;
    
    @Delete("DELETE FROM TBL_LEGAL_DATAPROJECT_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);
    
    
    
    @Select("select count(*) from TBL_DATAPROJECT_ISSUE where DATAID=#{dataid} and staffid=#{staffid}")
    Integer checkIssue(BigDecimal dataid,BigDecimal staffid);
    
    
    @Insert("INSERT INTO TBL_DATAPROJECT_ISSUE(PROJECTID,STAFFID,DATAID) VALUES(#{projectid},#{staffid},#{dataid})")
    public void saveIssue(String projectid,String staffid,String dataid);

	List<TblNbsjProjectDataEntity>  selectListByPageInfoXml(@Param("dataProVo") DataProVo dataProVo,@Param("secrectSql") String secrectSql);
}
