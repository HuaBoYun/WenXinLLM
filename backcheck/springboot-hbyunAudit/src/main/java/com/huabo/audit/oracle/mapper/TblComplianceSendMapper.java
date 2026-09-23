package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblComplianceWeekly;

@Repository
public interface TblComplianceSendMapper extends BaseMapper<TblComplianceSendMapper>{
 
    
    //合规周刊BEGIN
    @SelectProvider(method="selectWeeklyListByPageInfo",type=TblComplianceSendMapperSqlConfig.class)
	List<TblComplianceWeekly> selectWeeklyListByPageInfo(PageInfo<TblComplianceWeekly> pageInfo,TblComplianceWeekly tblComplianceWeekly,TblStaffUtil loginStaff) throws Exception;
    
    @SelectProvider(method="selectWeeklyCountByPageInfo",type=TblComplianceSendMapperSqlConfig.class)
   	Integer selectWeeklyCountByPageInfo(PageInfo<TblComplianceWeekly> pageInfo,TblComplianceWeekly tblComplianceWeekly,TblStaffUtil loginStaff) throws Exception;
    
    @InsertProvider(method="insertWeeklyEntity",type=TblComplianceSendMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	void insertWeeklyEntity(TblComplianceWeekly tblComplianceWeekly) throws Exception;

    @UpdateProvider(method="updateWeeklyEntity",type=TblComplianceSendMapperSqlConfig.class)
	void updateWeeklyEntity(TblComplianceWeekly tblComplianceWeekly) throws Exception;
    
    @Delete("DELETE FROM TBL_COMPLIANCE_WEEKLY WHERE ID = #{id}")
    void deleteWeeklyById(String id) throws Exception;
    
    @Select("SELECT TNA.*,STAFF.REALNAME creatorName "
			+ "FROM TBL_COMPLIANCE_WEEKLY TNA " 
			+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATOR "
    		+ " WHERE TNA.ID = #{id}")
    TblComplianceWeekly selectWeeklyById(@Param("id") String id) throws Exception;
    
    //合规周刊END
    
}
