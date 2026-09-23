package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;
import com.huabo.audit.oracle.entity.TblNbsjRefopmEntity;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblNbsjRefopmMapper extends BaseMapper<TblNbsjRefopm> {
    @Delete("DELETE FROM TBL_NBSJ_REFOPM WHERE REFORMID = #{reformid}")
    void deleteNbsjRefopmById(@Param("reformid") BigDecimal reformid) throws Exception;


    @InsertProvider(method = "insertEntity", type = TblNbsjRefopmMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "reformid", keyColumn = "reformid")
    void insertEntity(TblNbsjRefopm re) throws Exception;


    @UpdateProvider(method = "updateEntity", type = TblNbsjRefopmMapperSqlConfig.class)
    void updateEntity(TblNbsjRefopm re) throws Exception;


    @SelectProvider(method = "selectNbsjReformByPageInfo", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "problemid", property = "problemid"),
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "code", property = "code"),
            @Result(column = "company", property = "company"),
            @Result(column = "source", property = "source"),
            @Result(column = "discoverer", property = "discoverer"),
            @Result(column = "details", property = "details"),
    })
    List<TblNbsjRefopm> selectNbsjReformByPageInfo(PageInfo<TblNbsjRefopm> pageInfo, BigDecimal projectid, TblNbsjRefopm re, String selectidIdsstr) throws Exception;

    /**
     * @param projectid
     * @param re
     * @param selectidIdsstr
     * @return
     * @throws Exception
     */
    List<TblNbsjRefopm> findNbsjReformList(@Param("projectid") BigDecimal projectid, @Param("re") TblNbsjRefopm re, @Param("selectidIdsstr") String selectidIdsstr) throws Exception;


    @SelectProvider(method = "selectNbsjReformByProjectCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformByProjectCount(BigDecimal projectid, TblNbsjRefopm re, String selectidIdsstr) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionPageInfo", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "problemid", property = "problemid"),
            @Result(column = "code", property = "code"),
            @Result(column = "company", property = "company"),
            @Result(column = "source", property = "source"),
            @Result(column = "discoverer", property = "discoverer"),
            @Result(column = "details", property = "details"),
            @Result(column = "zgzxxrname", property = "zgzxxrname"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "REFORMRESULT", property = "reformresult"),
            @Result(column = "REFORMMEASURE", property = "reformmeasure"),
            @Result(column = "REFORMCARRYOUT", property = "reformcarryout"),
    })
    List<TblNbsjRefopm> selectNbsjReformBySolutionPageInfo(PageInfo<TblNbsjRefopm> pageInfo, BigDecimal solutionid, TblNbsjRefopm re, String type) throws Exception;

    /**
     * @param solutionid
     * @param re
     * @param type
     * @return
     * @throws Exception
     */
    List<TblNbsjRefopm> findNbsjReformBySolution(@Param("solutionid") BigDecimal solutionid, @Param("re") TblNbsjRefopm re, @Param("type") String type) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformBySolutionCount(BigDecimal SOLUTIONID, TblNbsjRefopm re, String type) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionwt", type = TblNbsjRefopmMapperSqlConfig.class)
    List<TblNbsjRefopm> selectNbsjReformWtList(BigDecimal soultionid, String bugid, String souid) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionAll", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "problemid", property = "problemid"),
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "code", property = "code"),
            @Result(column = "company", property = "company"),
            @Result(column = "source", property = "source"),
            @Result(column = "discoverer", property = "discoverer"),
            @Result(column = "details", property = "details"),
            @Result(column = "zgzxxrname", property = "zgzxxrname"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "REFORMRESULT", property = "reformresult"),
            @Result(column = "REFORMMEASURE", property = "reformmeasure"),
            @Result(column = "REFORMCARRYOUT", property = "reformcarryout"),
    })
    List<TblNbsjRefopm> selectNbsjReformBySolutinFa(BigDecimal solutionid, TblNbsjRefopm re) throws Exception;

    /**
     * @param solutionid
     * @param re
     * @return
     * @throws Exception
     */
    List<TblNbsjRefopm> findNbsjReformBySolutinFa(BigDecimal solutionid, TblNbsjRefopm re) throws Exception;

    @SelectProvider(method = "selectNbsjReformById", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "problemid", property = "problemid"),
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "code", property = "code"),
            @Result(column = "company", property = "company"),
            @Result(column = "source", property = "source"),
            @Result(column = "discoverer", property = "discoverer"),
            @Result(column = "details", property = "details"),
            @Result(column = "zgzxxrname", property = "zgzxxrname"),
            @Result(column = "status", property = "status"),
            @Result(column = "reformmeasure", property = "reformmeasure"),
            @Result(column = "reformresult", property = "reformresult"),
            @Result(column = "reformcarryout", property = "reformcarryout"),
            @Result(column = "handling", property = "handling"),
            @Result(column = "reformdeadline", property = "reformdeadline"),
            @Result(column = "nextmeasures", property = "nextmeasures"),
            @Result(column = "nextmplancomdate", property = "nextmplancomdate"),
            @Result(column = "inspect", property = "inspect"),
            @Result(column = "wzgreason", property = "wzgreason"),
            @Result(column = "zgwdreason", property = "zgwdreason"),
            @Result(column = "threason", property = "threason"),
            @Result(column = "zgjgresult", property = "zgjgresult"),
            @Result(column = "closeyy", property = "closeyy"),
            @Result(column = "solutionid", property = "solutionid"),
    })
    TblNbsjRefopm selectNbsjReformById(@Param("reformid") BigDecimal reformid) throws Exception;


    @Insert("INSERT INTO TBL_NBSJ_REFORM_ATT(reformid,ATTID) VALUES (#{reformid},#{aid})")
    void insertAttInfoAtt(@Param("reformid") BigDecimal reformid, @Param("aid") String aid) throws Exception;

    @Insert("DELETE FROM TBL_NBSJ_REFORM_ATT where  ATTID=#{aid}")
    void deleteAttInfoAtt(@Param("aid") String aid) throws Exception;

    @Insert("DELETE FROM TBL_NBSJ_REFORM_ATT where REFORMID=#{reformid}")
    void deleteAttInfoAttByref(@Param("reformid") String reformid) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionZsCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformBySolutionzsCount(BigDecimal SOLUTIONID) throws Exception;

    @SelectProvider(method = "selectNbsjReformBySolutionYwcsCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformBySolutionYwcsCount(BigDecimal SOLUTIONID) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionRwAlls", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "status", property = "status"),
            @Result(column = "wxhstatus", property = "wxhstatus"),
    })
    List<TblNbsjRefopm> selectNbsjReformBySolutionRwAll(BigDecimal soultionid) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionRwwcAll", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "status", property = "status"),
            @Result(column = "wxhstatus", property = "wxhstatus"),
    })
    List<TblNbsjRefopm> selectNbsjReformBySolutionRwwcAll(BigDecimal soultionid) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionpjCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformBySolutionpjCount(BigDecimal SOLUTIONID) throws Exception;

    @SelectProvider(method = "selectNbsjReformBySolutionpjwcCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformBySolutionpjwcCount(BigDecimal SOLUTIONID) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionPageInfowxh", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "problemid", property = "problemid"),
            @Result(column = "code", property = "code"),
            @Result(column = "company", property = "company"),
            @Result(column = "source", property = "source"),
            @Result(column = "discoverer", property = "discoverer"),
            @Result(column = "details", property = "details"),
            @Result(column = "zgzxxrname", property = "zgzxxrname"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "reformid", property = "reformid"),
            @Result(column = "wxhstatus", property = "wxhstatus"),
            @Result(column = "zgjgresult", property = "zgjgresult"),
            @Result(column = "SOLUTIONID", property = "solutionid"),
    })
    List<TblNbsjRefopm> selectNbsjReformBySolutionPageInfowxh(PageInfo<TblNbsjRefopm> pageInfo, TblNbsjRefopm re) throws Exception;

    /**
     * @param re
     * @return
     * @throws Exception
     */
    List<TblNbsjRefopm> findNbsjReformBySolutionPageInfowxh(@Param("re") TblNbsjRefopm re) throws Exception;


    @SelectProvider(method = "selectNbsjReformBySolutionPageInfowxhcount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformBySolutionPageInfowxhcount(PageInfo<TblNbsjRefopm> pageInfo, TblNbsjRefopm re) throws Exception;


    @SelectProvider(method = "selectNbsjReformByAllPageInfo", type = TblNbsjRefopmMapperSqlConfig.class)
    @Results({
            @Result(column = "PRJOECTNAME", property = "projectname"),
            @Result(column = "oegname", property = "company"),
            @Result(column = "SHEETCODE", property = "code"),
            @Result(column = "auditDiscoverable", property = "details"),
            @Result(column = "REFORMRESULT", property = "reformresult"),
            @Result(column = "REFORMMEASURE", property = "reformmeasure"),
            @Result(column = "REALNAME", property = "zgzxxrname"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "reformid", property = "reformid"),
    })
    List<TblNbsjRefopm> selectNbsjReformByAllPageInfo(PageInfo<TblNbsjRefopm> pageInfo, BigDecimal auditorg, String code, String projectname) throws Exception;

    /**
     * @param auditorg
     * @param code
     * @param projectname
     * @return
     * @throws Exception
     */
    List<TblNbsjRefopm> findNbsjReformByAllPageInfo(@Param("auditorg") BigDecimal auditorg, @Param("code") String code, @Param("projectname") String projectname) throws Exception;


    @SelectProvider(method = "selectNbsjReformByAllCount", type = TblNbsjRefopmMapperSqlConfig.class)
    Integer selectNbsjReformByAllcount(BigDecimal auditorg, String code, String projectname) throws Exception;


    @Select("SELECT TNA.* FROM TBL_NBSJ_REFOPM TNA WHERE TNA.QUESTIONID = #{questionid}")
    List<TblNbsjRefopmEntity> getByQuestid(@Param("questionid") Integer questionid) throws Exception;


    @Select("SELECT TNA.* FROM TBL_NBSJ_REFOPM TNA WHERE TNA.reformid = #{reformid}")
    TblNbsjRefopm selectNbsjReformByIds(BigDecimal reformid) throws Exception;


}
