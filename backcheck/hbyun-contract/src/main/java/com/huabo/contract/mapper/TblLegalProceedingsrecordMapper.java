package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblLegalProceedingsrecord;
import com.huabo.contract.mappersql.TblLegalProceedingsrecordMapperSqlConfig;

import net.sf.json.JSONObject;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-03-21
 */
public interface TblLegalProceedingsrecordMapper extends BaseMapper<TblLegalProceedingsrecord> {

    @InsertProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "saveProceedingRecord")
    @Options(useGeneratedKeys=true, keyProperty="proceedid", keyColumn="PROCEEDID")
    void saveProceedingRecord(TblLegalProceedingsrecord proceed) throws Exception;

    @Select("SELECT * FROM TBL_LEGAL_PROCEEDINGSRECORD WHERE LITIGATIONINFO = #{litigationid}")
    List<TblLegalProceedingsrecord> findListByLitigationid(BigDecimal litigationid) throws Exception;

    @Select("SELECT * FROM TBL_LEGAL_PROCEEDINGSRECORD WHERE PROCEEDID = #{proceedid}")
    TblLegalProceedingsrecord findByProceedid(BigDecimal proceedid) throws Exception;

    @UpdateProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "updateModifyNegotiateRecord")
    void updateModifyNegotiateRecord(TblLegalProceedingsrecord oldproceed) throws Exception;

    @Delete("DELETE FROM TBL_LEGAL_PROCEEDINGSRECORD WHERE PROCEEDID = #{proceedid}")
    void deleteProceedid(BigDecimal proceedId);

    @SelectProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "findListByPageInfo")
    IPage<TblLegalProceedingsrecord> findListByPageInfo(IPage<TblLegalProceedingsrecord> page, TblLegalProceedingsrecord record) throws Exception; 

    @SelectProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "findByNegotiaId")
    IPage<TblLegalProceedingsrecord> findByNegotiaId(IPage<TblLegalProceedingsrecord> page, BigDecimal litigationId) throws Exception;

    @Insert("INSERT INTO TBL_LEGAL_PRECORD_ATT (ATTID, PROCEEDID) VALUES (#{attid},#{proceedId})")
    void inertatt(BigDecimal proceedId,String attid) throws Exception;
    
    @Delete("DELETE FROM TBL_LEGAL_PRECORD_ATT where  PROCEEDID=#{proceedId}")
    void deleteatt(BigDecimal proceedId);
    
    @Delete("DELETE FROM TBL_LEGAL_PRECORD_ATT where  ATTID=#{attid}")
    void deletebyattid(BigDecimal attid);
    
   /* @Select("select ROW_NUMBER() OVER ( ORDER BY createtime ASC ) AS indexs ,to_char(createtime,'YYYY-MM-DD') year,PORCEEDSTAGE condation   from TBL_LEGAL_PROCEEDINGSRECORD where litigationinfo=#{litigationId} order by createtime  ")
    List<JSONObject> getproceedTimeAxis(String litigationId);*/
    
    @SelectProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "getproceeddis")
    List<JSONObject> getproceeddis(String litigationId) throws Exception;
    
    @SelectProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "getproceedex")
    List<JSONObject> getproceedex(String litigationId) throws Exception;
    
    @SelectProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "getproceedja")
    List<JSONObject> getproceedja(String litigationId) throws Exception;
    
    @SelectProvider(type = TblLegalProceedingsrecordMapperSqlConfig.class,method = "getproceedTim")
    List<JSONObject> getproceedTim(String litigationId) throws Exception;
    

}


