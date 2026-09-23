package com.huabo.contract.mapper;

import java.math.BigDecimal;
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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractProject;
import com.huabo.contract.mappersql.TblContractProjectMapperSqlConfig;
import com.huabo.contract.vo.TblContractProjectVo;

public interface TblContractProjectMapper extends BaseMapper<TblContractProject>{
	@Select("SELECT * from TBL_CONTRACT_PROJECT WHERE PROJECTID= #{projectid} ")
    TblContractProject getById(String projectid);

    @Select("SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
    		+ "FROM TBL_CONTRACT_PROJECT TNA "
    		+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.UNDERTAKESTAFFID "
			+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.UNDERTAKEORGID "
    		+ " WHERE TNA.PROJECTID = #{projectid}")
    @Results({
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="PROJECTNAME",property="projectname"),
    	@Result(column="PROJECTCODE",property="projectcode"),
    	@Result(column="MEMO",property="memo"),
    	
    	@Result(column="REALNAME",property="undertakestaff.realname"),
    	@Result(column="ORGNAME",property="undertakeorg.orgname"),
    })
   	TblContractProject selectEntityById(@Param("projectid") BigDecimal projectid) throws Exception;
    
    @Delete("DELETE FROM TBL_CONTRACT_PROJECT WHERE PROJECTID = #{projectid}")
    void deleteById(BigDecimal projectid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblContractProjectMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblContractProject plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblContractProjectMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="projectid", keyColumn="PROJECTID")
	void insertEntity(TblContractProject plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblContractProjectMapperSqlConfig.class)
	void updateEntity(TblContractProject plan) throws Exception;

    @Delete("DELETE FROM TBL_LEGAL_CONTRACT_PROJECT_ATT WHERE PROJECTID = #{projectid}")
	void deleteFileRelation(BigDecimal projectid);

    @Delete("DELETE FROM TBL_LEGAL_CONTRACT_PROJECT_ATT WHERE ATTID = #{attId}")
	void deleteFileId(String attId);

    @SelectProvider(method="findAutoNumber",type=TblContractProjectMapperSqlConfig.class)
	Integer findAutoNumber(String projectcode, BigDecimal orgid) throws Exception;
    
    @Select("SELECT * FROM TBL_CONTRACT_PROJECT  WHERE UNIQUEID = #{id}")
	List<TblContractProject> selectbyxmid(String id) throws Exception;
    
    @UpdateProvider(method="updateEntitytb",type=TblContractProjectMapperSqlConfig.class)
   	void updateEntitytb(TblContractProject plan) throws Exception;

    @SelectProvider(method="selectListByPageInfo",type=TblContractProjectMapperSqlConfig.class)
    @Results({
    	@Result(column="PROJECTID",property="projectid"),
    	@Result(column="PROJECTNAME",property="projectname"),
    	@Result(column="PROJECTCODE",property="projectcode"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="REALNAME",property="undertakestaff.realname"),
    	@Result(column="ORGNAME",property="undertakeorg.orgname"),
    })
	IPage<TblContractProject> selectListByPageInfo(IPage<TblContractProject> page,TblContractProjectVo tblContractProjectVo,Integer staffid,Integer orgid);

}
