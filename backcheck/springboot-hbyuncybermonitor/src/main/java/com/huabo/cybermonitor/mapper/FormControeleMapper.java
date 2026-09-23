package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.FormControele;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
public interface FormControeleMapper extends BaseMapper<FormControele> {

    @Select("SELECT COUNT(*) FROM TBL_FORM_CONTROELE WHERE RULEID = #{ruleid} " +
            "AND ELEID IS NOT NULL")
    Integer judgeControlRelationCount(@Param("ruleid") BigDecimal ruleid);



    @Update("\"UPDATE TBL_FORM_CONTROELE SET ELEID = #{eleid} WHERE RULEID = #{ruleid}" +
            " AND ELEID IS NOT NULL")
    void updateEleRuleRela(@Param("ruleid") BigDecimal ruleid,@Param("eleid") BigDecimal eleid);

    @Select("SELECT  VIDEORUL FROM TBL_COURSE WHERE COURSEID = #{videoId}")
    String selectReportUrl(@Param("videoId") Integer videoId);


    @Select("SELECT  COURSEWAREURL FROM TBL_COURSE WHERE COURSEID = #{docId}")
    String selectCOURSEWAREURL(@Param("docId")Integer docId);
}
