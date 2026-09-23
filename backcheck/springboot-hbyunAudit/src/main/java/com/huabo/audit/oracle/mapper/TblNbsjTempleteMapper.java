package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;

public interface TblNbsjTempleteMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjTempleteEntity> {
    @SelectProvider(method="selectNbsjTempleteListByPageInfo",type=TblNbsjTempleteMapperSqlConfig.class)
    @Results({
    	@Result(column="TEMPLETEID",property="templeteId"),
    	@Result(column="TEMPLETECODE",property="templeteCode"),
    	@Result(column="TEMPLETENAME",property="templeteName"),
    	@Result(column="TEMPLETETYPE",property="templeteType"),
    	@Result(column="CREATEDATE",property="createDate"),
    	@Result(column="STATUS",property="status"),
    })
	List<TblNbsjTempleteEntity> selectNbsjTempleteListByPageInfo(PageInfo<TblNbsjTempleteEntity> pageInfo,BigDecimal orgId, TblNbsjTempleteVo templete) throws Exception;
    
    @SelectProvider(method="selectNbsjTempleteListCountByPageInfo",type=TblNbsjTempleteMapperSqlConfig.class)
   	Integer selectNbsjTempleteListCountByPageInfo(PageInfo<TblNbsjTempleteEntity> pageInfo,BigDecimal orgId, TblNbsjTempleteVo templete) throws Exception;
    
    @Insert("INSERT INTO TBL_NBSJ_TEMP_ORG VALUES(#{templeteid},#{orgid})")
    Integer insertTemOrg(String templeteid,String orgid) throws Exception;
    
    @Select("SELECT ORGID FROM TBL_NBSJ_TEMP_ORG WHERE TEMPLATEID= #{templeteid}")
    List<String> selectOrgidByTempleteid(String templeteid) throws Exception;
    
    @Select("SELECT wm_concat (ORGNAME) TEMORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN ( SELECT ORGID FROM TBL_NBSJ_TEMP_ORG WHERE TEMPLATEID = #{templeteid})")
    String findOrgNameByTempId(BigDecimal templeteid) throws Exception;
    
    @Delete("DELETE FROM TBL_NBSJ_TEMP_ORG WHERE TEMPLATEID =#{templeteid}")
    void deleteTemOrg(String templeteid) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjTempleteMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="templeteId", keyColumn="TEMPLETEID")
	void insertEntity(TblNbsjTempleteEntity templete) throws Exception;
    
    @UpdateProvider(method="updateEntity",type=TblNbsjTempleteMapperSqlConfig.class)
    void updateEntity(TblNbsjTempleteEntity templete) throws Exception;
    /**
     * 
     * @param templeteId
     * @throws Exception
     */
    @Update("UPDATE TBL_NBSJ_TEMPLETE SET STATUS = #{status} WHERE TEMPLETEID = #{templeteId}")
    void updateStatus(Integer status,String templeteId) throws Exception;
    
    @Select("SELECT * FROM TBL_NBSJ_TEMPLETE WHERE templeteid=#{templeteId}")
    TblNbsjTempleteEntity findbyid(String templeteId) throws Exception;
    
    @SelectProvider(method="findInfobyid",type=TblNbsjTempleteMapperSqlConfig.class)
    TblNbsjTempleteEntity findInfobyid(String templeteId) throws Exception;
    
    @Delete("Delete from TBL_NBSJ_TEMPLETE WHERE templeteId=#{templeteId}")
    void deleteInfoById(String templeteId) throws Exception;

}
