package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblCirculation;
import com.huabo.monitor.mysql.entity.TblCirculationMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblCirculationMySqlMapper extends BaseMapper<TblCirculationMySql> {

    @SelectProvider(type = TblCirculationMapperSqlMySqlConfig.class, method = "findAll")
    List<TblCirculation> findAll(PageInfo<TblCirculation> pageInfo, BigDecimal staffid, TblCirculation tca);

    @SelectProvider(type = TblCirculationMapperSqlMySqlConfig.class, method = "findAllCount")
    Integer findAllCount(BigDecimal staffid, TblCirculation tca);

    @Select("SELECT * FROM TBL_CIRCULATION WHERE CYID = #{cyid}")
    TblCirculationMySql selectByCyid(String cyid);

    @Select("SELECT  DISTINCT TCR.*,TCU.RECORDTYPE recordtype,TCU.CONTRACTSTATUS contractstatus FROM TBL_CIRCULATION TCR LEFT JOIN TBL_CYHW_UNIT TCU ON TCR.TASKID=TCU.CONTRACTID where TCR.DEFINITIONID not  like '%SJ_JHTZD%' AND TCR.DEFINITIONID not like '%SJ_SSQRS%' AND TCR.DEFINITIONID not like '%SJ_XMGL%' AND TCR.DEFINITIONID not like '%SJ_DGFH%' AND TCR.DEFINITIONID not like '%SJ_JHGL%' AND TCR.DEFINITIONID not like '%SJ_SJBG%'  and  TCR.CYSTATE!='已完成' AND TCR.CYSTATE!='终止'  AND TCR.CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCR.CYURL)    order BY TCR.CYID desc")
    List<TblCirculationMySql> findStaffid(String taskid);

    @Select("SELECT  DISTINCT TCR.*,TCU.RECORDTYPE recordtype,TCU.CONTRACTSTATUS contractstatus FROM TBL_CIRCULATION TCR LEFT JOIN TBL_CYHW_UNIT TCU ON TCR.TASKID=TCU.CONTRACTID where TCR.DEFINITIONID not  like '%SJ_JHTZD%' AND TCR.DEFINITIONID not like '%SJ_SSQRS%' AND TCR.DEFINITIONID not like '%SJ_XMGL%' AND TCR.DEFINITIONID not like '%SJ_DGFH%' AND TCR.DEFINITIONID not like '%SJ_JHGL%' AND TCR.DEFINITIONID not like '%SJ_SJBG%'  and  TCR.CYSTATE!='已完成' AND TCR.CYSTATE!='终止'  AND TCR.CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCR.CYURL)    order BY TCR.CYID desc ")
    List<TblCirculationMySql> findAllStaffid(String taskid);

    @Select("SELECT  DISTINCT * FROM  TBL_CIRCULATION TCR where TCR.DEFINITIONID not  like '%SJ_JHTZD%' AND TCR.DEFINITIONID not like '%SJ_SSQRS%' AND TCR.DEFINITIONID not like '%SJ_XMGL%' AND TCR.DEFINITIONID not like '%SJ_DGFH%' AND TCR.DEFINITIONID not like '%SJ_JHGL%' AND TCR.DEFINITIONID not like '%SJ_SJBG%'  and  TCR.CYSTATE!='已完成' AND TCR.CYSTATE!='终止'  AND TCR.CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCR.CYURL)    order BY TCR.CYID desc")
    List<TblCirculationMySql> findAllString(String s);
}
