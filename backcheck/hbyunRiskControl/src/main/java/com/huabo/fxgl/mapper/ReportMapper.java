package com.huabo.fxgl.mapper;
import com.huabo.fxgl.entity.Report;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
import java.util.Map;
@Mapper
public interface ReportMapper extends BaseMapper<Report> {
	
	List<Map<String, Object>> getRiskReportTypeCountByCompany(@Param("company")String company);
	
    @Insert("insert into tbl_rep_att(ATTID, REPORTID) values (#{attid}, #{reportid})")
    int insertReportAtt(@Param("reportid") BigDecimal reportid, @Param("attid") BigDecimal attid);

    @Delete("DELETE from tbl_rep_att WHERE REPORTID= #{reportid} ")
    int deleteReportAtt(@Param("reportid") BigDecimal reportid);
    
    List<Report> selectAllList(@Param("ew") Wrapper queryWrapper,@Param("sql")String sql);

    @Select("${sql}")
    List<Report> selectListByPageInfo(@Param("sql")String sql);

}
