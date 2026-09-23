package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblCirculation;
import com.huabo.system.mappersql.TblCirculationMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblCirculationMapper extends BaseMapper<TblCirculation> {

    @SelectProvider(type=TblCirculationMapperSqlConfig.class,method="findAll")
    IPage<TblCirculation> findAll(IPage<TblCirculation> page, BigDecimal staffid, TblCirculation tca);

    @Select("SELECT * FROM TBL_CIRCULATION WHERE CYID = #{cyid}")
    TblCirculation selectByCyid(String cyid);

    @Select("SELECT  DISTINCT TCR.*,TCU.RECORDTYPE recordtype,TCU.CONTRACTSTATUS contractstatus FROM TBL_CIRCULATION TCR LEFT JOIN TBL_CYHW_UNIT TCU ON TCR.TASKID=TCU.CONTRACTID where TCR.DEFINITIONID not like '%SJ_JHTZD%' AND TCR.DEFINITIONID not like '%SJ_SSQRS%'  AND TCR.DEFINITIONID not like '%SJ_SJBG%'  and  TCR.CYSTATE NOT IN( '已完成','终止','已通过') AND TCR.CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCR.CYURL)    order BY TCR.CYID desc")
    List<TblCirculation> findStaffid(String taskid);

    @Select("SELECT  DISTINCT TCR.*,TCU.RECORDTYPE recordtype,TCU.CONTRACTSTATUS contractstatus FROM TBL_CIRCULATION TCR LEFT JOIN TBL_CYHW_UNIT TCU ON TCR.TASKID=TCU.CONTRACTID where TCR.DEFINITIONID not  like '%SJ_JHTZD%' AND TCR.DEFINITIONID not like '%SJ_SSQRS%'   AND TCR.DEFINITIONID not like '%SJ_SJBG%'  and  TCR.CYSTATE!='已完成' AND TCR.CYSTATE!='终止'  AND TCR.CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCR.CYURL)    order BY TCR.CYID desc ")
    List<TblCirculation> findAllStaffid(String taskid);

    @Select("SELECT  DISTINCT * FROM  TBL_CIRCULATION TCR where TCR.DEFINITIONID not  like '%SJ_JHTZD%' AND TCR.DEFINITIONID not like '%SJ_SSQRS%'   AND TCR.DEFINITIONID not like '%SJ_SJBG%'  and  TCR.CYSTATE!='已完成' AND TCR.CYSTATE!='终止'  AND TCR.CYID = (SELECT MAX(CYID) FROM TBL_CIRCULATION WHERE CYURL = TCR.CYURL)    order BY TCR.CYID desc")
    List<TblCirculation> findAllString(String s);

	@Select("select * from TBL_CIRCULATION where CYID = #{cyid}")
	TblCirculation findById(String cyid);
}
