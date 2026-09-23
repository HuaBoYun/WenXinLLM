package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.ZhFormEntity;
import com.huabo.audit.oracle.vo.ZhFormVo;
import com.huabo.audit.util.PageInfo;

public interface ZhFormMapper extends BaseMapper<ZhFormEntity>{
	@Select("SELECT * from TBL_ZH_FORM WHERE FORMID= #{formid} ")
    ZhFormEntity getById(String formid);
    
    @SelectProvider(method="selectCountByPageInfo",type=ZhFormMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<ZhFormEntity> pageInfo,ZhFormVo zhFormVo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_ZH_FORM TNA  WHERE TNA.FORMID = #{formid}")
    @Results({
    	@Result(column="FORMID",property="formid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"),
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
    	@Result(column="REPORTTEMPID",property="reporttempid"),
    	@Result(column="REPORTSTATUS",property="reportstatus"),
    	@Result(column="MEMO",property="memo"),
    })
   	ZhFormEntity selectById(@Param("formid") Integer formid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=ZhFormMapperSqlConfig.class)
    @Results({
    	@Result(column="FORMID",property="formid"),
    	@Result(column="REPORTNAME",property="reportname"),
    	@Result(column="REPORTTIME",property="reporttime"),
    	@Result(column="REPORTTYPE",property="reporttype"),
    	@Result(column="REPORTMODE",property="reportmode"),
    	@Result(column="REPORTTEMPID",property="reporttempid"),
    	@Result(column="REPORTSTATUS",property="reportstatus"),
    	@Result(column="MEMO",property="memo"),
    })
	List<ZhFormEntity> selectListByPageInfo(PageInfo<ZhFormEntity> pageInfo,ZhFormVo zhFormVo) throws Exception;

    @Delete("DELETE FROM TBL_ZH_FORM WHERE FORMID = #{formid}")
    void deleteById(Integer formid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=ZhFormMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(ZhFormEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=ZhFormMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="formid", keyColumn="FORMID")
	void insertEntity(ZhFormEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=ZhFormMapperSqlConfig.class)
	void updateEntity(ZhFormEntity plan) throws Exception;
}
