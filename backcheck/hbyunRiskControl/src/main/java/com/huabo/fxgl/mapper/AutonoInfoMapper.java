package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.AutonoInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.OrgNo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Repository
public interface AutonoInfoMapper extends BaseMapper<AutonoInfo> {
    @Select("SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID = #{param}")
    Integer getIsUseAutoNoInfo(BigDecimal orgid);

    @Select("SELECT CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTCODE	ELSE TON.NOCODE	END AS NOCODE, "+
            "CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTSEPARTOR ELSE TON.NOSEPARTOR END AS NOSEPARTOR, " +
            "CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTNUMBER ELSE TON.NONUMBER END AS NONUMBER "+
            " FROM TBL_AUTONO_INFO TAI LEFT JOIN TBL_ORG_NO TON ON TAI.NOID = TON.NOID WHERE TON.ORGID = #{param1} AND TON.NOID = #{param2}")
    OrgNo getCodeRule(BigDecimal orgid, Integer noId);

    String selectUniqueColumn(@Param("tblName") String tblName,@Param("column") String column,@Param("orgCol") String orgCol,
                              @Param("orgid") BigDecimal orgid,@Param("noId") Integer noId,@Param("chChoiceCol") String chChoiceCol,
                              @Param("choiceVal") String choiceVal,@Param("bjf") String bjf, @Param("orgNocode") String orgNocode,
                              @Param("orgNosepartor") String orgNosepartor,@Param("jgf") String jgf);

    @Select("SELECT ${param1} FROM ${param2} WHERE ${param3} = #{param4}")
    String selectUniqueColumn1(String parentNumberCol, String parentTblName, String parentIdCol, String parentId);

    String findNumberLevelNexidByParent(@Param("noId") Integer noId,@Param("parentNumberCol") String parentNumberCol,@Param("parentTblName") String parentTblName,
                                        @Param("parentIdCol") String parentIdCol,@Param("parentId") String parentId,@Param("chilNumberCol") String chilNumberCol,
                                        @Param("chilTblName") String chilTblName,@Param("chilOrgCol") String chilOrgCol, @Param("orgid") BigDecimal orgid,
                                        @Param("choiceMap") Map<String, String> choiceMap,@Param("orgNosepartor") String orgNosepartor,@Param("choiceSql") String choiceSql,
                                        @Param("jgf") String jgf,@Param("parentNumber")String parentNumber);

    String findRootNumberByParentId(@Param("chilNumberCol") String chilNumberCol,@Param("chilTblName") String chilTblName,@Param("chilParentCol") String chilParentCol,
                                    @Param("parentIdCol") String parentIdCol,@Param("parentTblName") String parentTblName,@Param("parnetOrgCol") String parnetOrgCol,
                                    @Param("orgid") BigDecimal orgid,@Param("noId") Integer noId,@Param("middleTblname") String middleTblname,
                                    @Param("middleChilCol") String middleChilCol,@Param("middleParentCol") String middleParentCol,
                                    @Param("type") String type,@Param("orgNosepartor") String orgNosepartor,@Param("orgNocode") String orgNocode,@Param("jgf") String jgf);




    /**
     * 查询是否有自动编号
     * @param orgid
     * @return
     */
    @Select("SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID = #{orgid1}")
    Integer selectUniqueColumn2(@Param("orgid1") BigDecimal orgid);




    @Select("SELECT CASE WHEN TON.ISUSEDEFAULT = 0" +
            "       THEN TAI.NODEFAULTCODE" +
            "       ELSE TON.NOCODE" +
            "       END \"CODE\" , " +
            "       CASE WHEN TON.ISUSEDEFAULT = 0 " +
            "           THEN TAI.NODEFAULTSEPARTOR " +
            "           ELSE TON.NOSEPARTOR " +
            "           END \"SEPARTOR\" , " +
            "       CASE WHEN TON.ISUSEDEFAULT = 0 " +
            "           THEN TAI.NODEFAULTNUMBER " +
            "           ELSE TON.NONUMBER " +
            "           END \"NUMBER\" " +
            "       FROM TBL_AUTONO_INFO TAI LEFT JOIN TBL_ORG_NO TON ON TAI.NOID = TON.NOID " +
            "    WHERE TON.ORGID = #{orgid2} AND TON.NOID = #{noId} ")
    Map<String,Object> selectCodeRule(@Param("orgid2") BigDecimal orgid, @Param("noId") Integer noId);


    /**
     *
     * @param column
     * @param tblName
     * @param orgCol
     * @param noCode
     * @param jgf
     * @param noSepartor
     * @param chChoiceCol
     * @param bjf
     * @param choiceVal
     * @param orgid
     * @return
     */

    @Select("<script>" +
            "SELECT MAX(${column}) FROM ${tblName} WHERE " +
            " TO_CHAR( ${orgCol}) IN ( SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION " +
            " WHERE ORGTYPE = 0 START WITH FATHERORGID = #{orgid} " +
            " CONNECT BY PRIOR FATHERORGID = ORGID " +
            " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION WHERE ORGID = #{orgid} " +
            " UNION ALL SELECT TO_CHAR(ORGID) FROM TBL_ORGANIZATION " +
            " WHERE ORGTYPE >=100 START WITH FATHERORGID = -1 CONNECT BY PRIOR ORGID = FATHERORGID ) " +

            " <if test= 'noSepartor !=null '> and ${column} like '${jgf}'  AND (LENGTH(REPLACE(${column}," +
            " #{noCode},'')) - LENGTH(REPLACE(REPLACE(${column},#{noCode},''), '${noSepartor}',''))) =  +1 </if> " +

            " <if test= 'noSepartor ==null '> AND #{column} LIKE #{jgf} AND " +
            " (LENGTH(REPLACE(${column},#{noCode},'')) - LENGTH(REPLACE(REPLACE(${column}," +
            " #{noCode},''),'-',''))) = 0 " +
            " AND (LENGTH(REPLACE(${column},#{noCode}, " +
            " '')) - LENGTH(REPLACE(REPLACE(${column},#{noCode},''),'_','')))  = 0 " +
            " AND (LENGTH(REPLACE(${column},#{noCode},'')) - LENGTH(REPLACE(REPLACE(${column}," +
            " #{noCode},''),',','')))  = 0 " +
            " AND (LENGTH(REPLACE(${column},#{noCode}," +
            " '')) - LENGTH(REPLACE(REPLACE(${column},#{noCode},''),'.','')))  = 0" +
            " AND (LENGTH(REPLACE(${column},#{noCode},'')) - LENGTH(REPLACE(REPLACE(${column}," +
            " #{noCode},''),'`','')))  = 0 " +
            "AND (LENGTH(REPLACE(${column},#{noCode}," +
            " '')) - LENGTH(REPLACE(REPLACE(${column},#{noCode},''),'~','')))  = 0</if> " +
            " <if test= 'chChoiceCol !=null '> AND #{chChoiceCol} #{bjf} #{choiceVal} </if>" +
            "</script>")
    String  selectUniqueColumn3(@Param("column") String column ,@Param("tblName") String tblName,
                                @Param("orgCol") String orgCol, @Param("noCode") String noCode,
                                @Param("jgf")  String jgf, @Param("noSepartor") String noSepartor,
                                @Param("chChoiceCol") String chChoiceCol, @Param("bjf") String bjf,
                                @Param("choiceVal") String choiceVal ,@Param("orgid") BigDecimal orgid);

}
