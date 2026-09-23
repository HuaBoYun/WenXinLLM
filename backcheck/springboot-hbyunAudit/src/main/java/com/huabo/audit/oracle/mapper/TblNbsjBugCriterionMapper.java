package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjBugCriterionMapper extends BaseMapper<TblNbsjBugCriterion> {

	    @InsertProvider(method="insertEntity",type=TblNbsjBugCriterionMapperSqlConfig.class)
	    @Options(useGeneratedKeys=true, keyProperty="bugcriid", keyColumn="BUGCRIID")
		void insertEntity(TblNbsjBugCriterion bug) throws Exception;

	    @UpdateProvider(method="updateEntity",type=TblNbsjBugCriterionMapperSqlConfig.class)
		void updateEntity(TblNbsjBugCriterion bug) throws Exception;
    
	    
	    @Select("select * from  TBL_NBSJ_BUGCRITERION  WHERE bugcriid = #{id} ")
	    TblNbsjBugCriterion selectTblNbsjCriterion(@Param("id")String id);
	    
	    @Delete("delete Tbl_Nbsj_BugCriterion  WHERE bugcriid = #{id} ")
        void delTblBugCriterion(@Param("id")String id);
	     
		@SelectProvider(method="selectNbsjBugCriterionListByPageInfo",type=TblNbsjBugCriterionMapperSqlConfig.class)
		List<TblNbsjBugCriterion> selectNbsjBugCriterionListByPageInfo(PageInfo<TblNbsjBugCriterion> pageInfo,String orgid) throws Exception ;
	     
		@SelectProvider(method="selectNbsjBugCriterionListCountByPageInfo",type=TblNbsjBugCriterionMapperSqlConfig.class)
		Integer selectNbsjBugCriterionListCountByPageInfo(PageInfo<TblNbsjBugCriterion> pageInfo, String orgid) throws Exception ;

		@Select("SELECT * FROM TBL_NBSJ_BUGCRITERION WHERE ORGID = #{orgid} ")
		List<TblNbsjBugCriterion> findListByOrgid(BigDecimal orgid) throws Exception;

		List<TblNbsjBugCriterion> selectNbsjBugCriterionListByPageInfoXml(@Param("orgid") String orgid,@Param("bugtype") String bugtype,@Param("sql") String sql);
}
