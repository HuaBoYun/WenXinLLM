package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblCourse;
import com.huabo.system.entity.TblVideoType;
import com.huabo.system.mappersql.TblCourseMapperSqlConifg;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
public interface TblCourseMapper extends BaseMapper<TblCourse> {



    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and ORGID = #{orgid}")
    List<TblVideoType> findByorgid(String orgid);

    @Select("SELECT * FROM TBL_VIDEO_TYPE where 1=1  and TYPENAME = #{typename}")
    List<TblVideoType> findBytypename(String typename);

    @Select("SELECT * from TBL_COURSE WHERE PARENTID= #{pid} order by COURSENUMBER")
    List<TblCourse> listBypid(String pid);

    @SelectProvider(method="selectListByPageInfo",type=TblCourseMapperSqlConifg.class)
    IPage<TblCourse> selectListByPageInfo(IPage<TblCourse> page, BigDecimal orgid, String coursename1, String coursetype1);

    @Select("SELECT * FROM TBL_COURSE where COURSEID = #{id}")
    TblCourse selectByCourseId(String id);
    
    @Delete("DELETE FROM TBL_COURSE where COURSEID = #{id}")
    void deleteByCourseId(String id);

    @UpdateProvider(type=TblCourseMapperSqlConifg.class,method="updatetblCourse")
    void updatetblCourse(TblCourse tblCourse);
    @InsertProvider(type=TblCourseMapperSqlConifg.class,method="savetblCourse")
    void savetblCourse(TblCourse tblCourse);
}
