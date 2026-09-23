package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblCourseMySql;
import com.huabo.monitor.mysql.entity.TblVideoTypeMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblCourseMySqlMapper extends BaseMapper<TblCourseMySql> {


    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and ORGID = #{orgid}")
    List<TblVideoTypeMySql> findByorgid(String orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and TYPENAME = #{typename}")
    List<TblVideoTypeMySql> findBytypename(String typename);

    @Select("SELECT * from TBL_COURSE WHERE PARENTID= #{pid} order by COURSENUMBER")
    List<TblCourseMySql> listBypid(String pid);

    @SelectProvider(method = "selectListByPageInfo", type = TblCourseMapperSqlMySqlConifg.class)
    List<TblCourseMySql> selectListByPageInfo(PageInfo<TblCourseMySql> pageInfo, BigDecimal orgid, String coursename1, String coursetype1);

    @SelectProvider(method = "selectCountByPageInfo", type = TblCourseMapperSqlMySqlConifg.class)
    Integer selectCountByPageInfo(PageInfo<TblCourseMySql> pageInfo, BigDecimal orgid, String coursename1, String coursetype1);

    @Select("SELECT * FROM TBL_COURSE where COURSEID = #{id}")
    TblCourseMySql selectByCourseId(String id);

    @Delete("DELETE FROM TBL_COURSE where COURSEID = #{id}")
    void deleteByCourseId(String id);

    @UpdateProvider(type = TblCourseMapperSqlMySqlConifg.class, method = "updatetblCourse")
    void updatetblCourse(TblCourseMySql tblCourse);

    @InsertProvider(type = TblCourseMapperSqlMySqlConifg.class, method = "savetblCourse")
    void savetblCourse(TblCourseMySql tblCourse);
}
