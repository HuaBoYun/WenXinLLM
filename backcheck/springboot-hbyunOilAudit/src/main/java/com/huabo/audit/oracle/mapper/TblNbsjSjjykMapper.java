package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjSjjyk;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblNbsjSjjykMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjSjjyk> {


    @Delete("DELETE FROM TBL_NBSJ_SJJYK WHERE JYKID = #{jykid}")
    void deleteJykById(@Param("jykid") BigDecimal jykid) throws Exception;


    @InsertProvider(method = "insertEntity", type = TblNbsjSjjykMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "jykid", keyColumn = "JYKID")
    void insertEntity(TblNbsjSjjyk jyk) throws Exception;


    @UpdateProvider(method = "updateEntity", type = TblNbsjSjjykMapperSqlConfig.class)
    void updateEntity(TblNbsjSjjyk jyk) throws Exception;


    @SelectProvider(method = "selectNbsjjykByPageInfo", type = TblNbsjSjjykMapperSqlConfig.class)
    @Results({
            @Result(column = "JYKID", property = "jykid"),
            @Result(column = "TATLE", property = "tatle"),
            @Result(column = "CODE", property = "code"),
            @Result(column = "EXPERIENCETYPE", property = "experiencetype"),
            @Result(column = "UPDATEDTIME", property = "updatedtime"),
            @Result(column = "STAFFID", property = "createStaff.staffid", id = true),
            @Result(column = "REALNAME", property = "createStaff.realname"),
    })
    List<TblNbsjSjjyk> selectNbsjjykByPageInfo(PageInfo<TblNbsjSjjyk> pageInfo, BigDecimal orgId, TblNbsjSjjyk jyk) throws Exception;

    /**
     * 
     * @param orgId
     * @param re
     * @return
     * @throws Exception
     */
    List<TblNbsjSjjyk> findList(@Param("orgId") BigDecimal orgId,@Param("re") TblNbsjSjjyk re) throws Exception;

    @SelectProvider(method = "selectNbsjjykByPageInfoCount", type = TblNbsjSjjykMapperSqlConfig.class)
    Integer selectNbsjjykByPageInfoCount(BigDecimal orgId, TblNbsjSjjyk jyk) throws Exception;


    @Select("SELECT s.*,STA.REALNAME FROM TBL_NBSJ_SJJYK s LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID    WHERE JYKID = #{jykid}")
    @Results({
            @Result(column = "JYKID", property = "jykid"),
            @Result(column = "TATLE", property = "tatle"),
            @Result(column = "CODE", property = "code"),
            @Result(column = "EXPERIENCETYPE", property = "experiencetype"),
            @Result(column = "EXPERIENCETATLE", property = "experiencetatle"),
            @Result(column = "OVERVIEW", property = "overview"),
            @Result(column = "JYKCONTENT", property = "jykcontent"),
            @Result(column = "UPDATEDTIME", property = "updatedtime"),
            @Result(column = "STAFFID", property = "createStaff.staffid", id = true),
            @Result(column = "REALNAME", property = "createStaff.realname"),
    })
    TblNbsjSjjyk selectNbsjJykByID(@Param("jykid") BigDecimal jykid) throws Exception;


    @Insert("INSERT INTO TBL_NBSJ_SJJYK_ATT(JYKID,ATTID) VALUES (#{jykid},#{aid})")
    void insertAttInfoAtt(@Param("jykid") BigDecimal jykid, @Param("aid") String aid) throws Exception;

    @Insert("INSERT INTO TBL_NBSJ_SJJYK_DATAPRE(JYKID,DATAOREID) VALUES (#{jykid},#{datepreid})")
    void insertjykdatapre(String jykid, Integer datepreid) throws Exception;

    @Insert("DELETE FROM TBL_NBSJ_SJJYK_DATAPRE where  DATAOREID=#{datepreid}")
    void deletetjykdatapre(Integer datepreid) throws Exception;

    @Insert("DELETE FROM TBL_NBSJ_SJJYK_ATT where  JYKID=#{jykid}")
    void deleteAttInfoAttByjykid(@Param("jykid") BigDecimal jykid) throws Exception;


    @Insert("DELETE FROM TBL_NBSJ_SJJYK_ATT where  ATTID=#{attid}")
    void deleteAttInfoAttid(@Param("attid") BigDecimal attid) throws Exception;


    @Insert("DELETE FROM TBL_NBSJ_SJJYK_DATAPRE where  JYKID=#{jykid}")
    void deletetjykdid(BigDecimal jykid) throws Exception;


    @SelectProvider(method = "selectNbsjjykByyy", type = TblNbsjSjjykMapperSqlConfig.class)
    Integer selectNbsjjykByyy(BigDecimal orgId, BigDecimal jykid) throws Exception;


    @SelectProvider(method = "selectNbsjjykByDateperidPageInfo", type = TblNbsjSjjykMapperSqlConfig.class)
    @Results({
            @Result(column = "JYKID", property = "jykid"),
            @Result(column = "TATLE", property = "tatle"),
            @Result(column = "CODE", property = "code"),
            @Result(column = "EXPERIENCETYPE", property = "experiencetype"),
            @Result(column = "UPDATEDTIME", property = "updatedtime"),
            @Result(column = "STAFFID", property = "createStaff.staffid", id = true),
            @Result(column = "REALNAME", property = "createStaff.realname"),
    })
    List<TblNbsjSjjyk> selectNbsjjykByDateperidPageInfo(Integer dataperid) throws Exception;


}
