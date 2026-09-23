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
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;

public interface TblNbsjBorrowRecordMapper extends BaseMapper<TblNbsjBorrowRecordEntity> {
	@Select("SELECT * from TBL_NBSJ_BORROWRECORD WHERE BORROWID= #{borrowid} ")
	TblNbsjBorrowRecordEntity getById(String borrowid);

	@SelectProvider(method = "selectCountByPageInfo", type = TblNbsjBorrowRecordMapperSqlConfig.class)
	Integer selectCountByPageInfo(PageInfo<TblNbsjBorrowRecordEntity> pageInfo, Integer projectid) throws Exception;

	@Select("SELECT TNA.* FROM TBL_NBSJ_BORROWRECORD TNA  WHERE TNA.BORROWID = #{borrowid}")
	@Results({ @Result(column = "BORROWID", property = "borrowid"),
			@Result(column = "CREATEDATE", property = "createDate"),
			@Result(column = "RETURNDATE", property = "returnDate"), @Result(column = "MEMO", property = "memo"),
//    	@Result(column="STAFFID",property="staffid"),
	})
	TblNbsjBorrowRecordEntity selectById(@Param("borrowid") Integer borrowid) throws Exception;

	@SelectProvider(method = "selectListByInfo", type = TblNbsjBorrowRecordMapperSqlConfig.class)
	@Results({ @Result(column = "BORROWID", property = "borrowid"),
			@Result(column = "CREATEDATE", property = "createDate"),
			@Result(column = "RETURNDATE", property = "returnDate"), @Result(column = "MEMO", property = "memo"),
//    	@Result(column="STAFFID",property="staffid"),
			@Result(column = "REALNAME", property = "tblstaff.realname"),

	})
	List<TblNbsjBorrowRecordEntity> selectListByInfo(Integer projectid) ;

	@Delete("DELETE FROM TBL_NBSJ_BORROWRECORD WHERE BORROWID = #{borrowid}")
	void deleteById(Integer BORROWID) throws Exception;

	@SelectProvider(method = "selectPlanCodeByOrgid", type = TblNbsjBorrowRecordMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjBorrowRecordEntity plan) throws Exception;

	@InsertProvider(method = "insertEntity", type = TblNbsjBorrowRecordMapperSqlConfig.class)
	@Options(useGeneratedKeys = true, keyProperty = "borrowid", keyColumn = "BORROWID")
	void insertEntity(TblNbsjBorrowRecordEntity plan) throws Exception;

	@UpdateProvider(method = "updateEntity", type = TblNbsjBorrowRecordMapperSqlConfig.class)
	void updateEntity(TblNbsjBorrowRecordEntity plan);
	
    @Select("SELECT  TNA.* ,TNP.PROJECT_NAME,TNP.QDCODE,TS.REALNAME AS staffname FROM TBL_NBSJ_BORROWRECORD TNA "
    		+ " LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN TNP  ON  TNA.PROJECTID = TNP.ID  "
    		+ "LEFT JOIN TBL_STAFF TS  ON  TNA.STAFFID = TS.STAFFID "
    		+ " WHERE  TNA.BORROWID = #{borrowid}")
    @Results({ 
		@Result(column = "PROJECT_NAME", property = "prjoectname"),
		@Result(column = "QDCODE", property = "projectcode"),
    })
	TblNbsjBorrowRecordEntity selectByIdBorrw(Integer borrowId);
}
