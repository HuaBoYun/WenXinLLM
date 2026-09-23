package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.JavaBean;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-07-18
 */
public interface TblNbsjStaffscoreDetailsMapper extends BaseMapper<TblNbsjStaffscoreDetails> {


    /**
     * 查询StaffscoreDetails
     * @param staffScoreId
     * @return
     */
	@Select("SELECT *  FROM TBL_NBSJ_STAFFSCORE_DETAILS  WHERE staffScore_id = #{staffScoreid}")
	@Results(value={
            @Result(column = "STAFFSCORE_DETAILS_ID", property = "staffScore_details_id"),
            @Result(column = "STAFFSCORE_ID", property = "staffScore_id"),
    })
    List<TblNbsjStaffscoreDetails> findTblNbsjStaffscoreDetails(@Param("staffScoreid") BigDecimal staffScoreid);

    /**
     * 添加StaffscoreDetails
     * @return
     */
    @InsertProvider(method="insertEntity",type=TblNbsjStaffscoreDetailsMapperSqlconfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="staffScore_details_id", keyColumn="staffScore_details_id")
	 void insertEntity(TblNbsjStaffscoreDetails re) throws Exception;
    /**
     * 修改StaffscoreDetails
     * @return
     */
    @UpdateProvider(method="updateEntity",type=TblNbsjStaffscoreDetailsMapperSqlconfig.class)
	 void updateEntity(TblNbsjStaffscoreDetails re) throws Exception;
    
    @Insert("DELETE FROM TBL_NBSJ_STAFFSCORE_DETAILS where staffScore_id=#{staffScoreid}")
	 void deleteInfoAttByScoreid(@Param("staffScoreid") BigDecimal staffScoreid) throws Exception;
}
