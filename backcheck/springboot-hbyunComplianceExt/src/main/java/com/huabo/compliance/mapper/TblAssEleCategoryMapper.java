package com.huabo.compliance.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.entity.TblAssEleCategory;
import com.huabo.compliance.entity.TblAssesselement;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssEleCategoryMapper extends BaseMapper<TblAssEleCategory> {

    @Select("select  * from TBL_ASSELE_CATEGORY " +
            "where asscatid in (select asscatid from TBL_ASSESSCATEGORY where asstemid=#{id})")
    List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id);
    
    //@Select("select  a.*,b.ELEMENTNUMBER from TBL_ASSELE_CATEGORY a  , TBL_ASSESSELEMENT b where a.ASSELEID = b.ASSELEID and  a.ASSELEID=#{id}")
    @Select("select  * from TBL_ASSELE_CATEGORY a  left JOIN TBL_ASSESSELEMENT b on a.ASSELEID = b.ASSELEID \n" +
            "where a.ASSELEID=#{id}")
    List<TblAssEleCategory> getAssesscategoryBytmplId(BigDecimal id);
    

    @Select("select  * from TBL_ASSESSELEMENT where ASSELEID=#{asseleid}")
	TblAssesselement getAssesselementBycatid(BigDecimal asseleid);
    
    @Select("select * from TBL_ASSELE_CATEGORY where  elementcategoryid=#{id}")
    TblAssEleCategory get(BigDecimal id);
    
    @Select("select * from TBL_ASSELE_CATEGORY tac left join TBL_ASSESSCATEGORY taca  on taca.asscatid=tac.asscatid left join TBL_ASSESSELEMENT tass on tass.asseleid=taca.asscatid where tac.asseleid=#{eleId} and tac.asscatid=#{cateId}")
    List<TblAssEleCategory> getScore(BigDecimal eleId, BigDecimal cateId);

    @Select("select  * from TBL_ASSELE_CATEGORY   where  asscatid=#{id}")
    List<TblAssEleCategory> getAssesscategoryByNodeId(BigDecimal id);


    @InsertProvider(type=TblAssEleCategoryMapperSqlConfig.class,method="insertEle")
    @Options(useGeneratedKeys=true, keyProperty="elementcategoryid", keyColumn="ELEMENTCATEGORYID")
	void insertEle(TblAssEleCategory assEleCategory) throws Exception;

    @UpdateProvider(type=TblAssEleCategoryMapperSqlConfig.class,method="updateEle")
	void updateEle(TblAssEleCategory assEleCategory) throws Exception;
}
