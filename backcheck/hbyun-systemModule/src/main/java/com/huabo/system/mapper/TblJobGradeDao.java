package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblJobGrade;

public interface TblJobGradeDao extends BaseMapper<TblJobGrade> {
	
    @Select("select * from TBL_JOB_GRADE where gradeid= #{gradeid}")
    List<TblJobGrade> listBySql(BigDecimal gradeid);
    
    @UpdateProvider(type=TblJobGradeDaoSqlConfig.class,method="updateJobGrade")
    void updateJobGrade(TblJobGrade grade);
    
    @InsertProvider(method = "saveJobGrade",type = TblJobGradeDaoSqlConfig.class)
    void saveJobGrade(TblJobGrade grade);
    
    @Select("SELECT * FROM TBL_JOB_GRADE WHERE HISTORYCODE = #{historycode}")
    TblJobGrade findJobGradeByHis(String historycode);
    
    @Delete("DELETE FROM TBL_JOB_GRADE WHERE GRADEID = #{id}")
    void deleteGrade(String id);
    
     
    
}
