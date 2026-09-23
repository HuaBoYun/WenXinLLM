package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.mapper.*;
import com.huabo.compliance.service.CsgzService;
import com.huabo.compliance.util.ConstClass;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author：yhr
 * @date:2022-09-13 11:34
 * @description:
 */
@Service
public class CsgzServiceImpl implements CsgzService {

    @Resource
    TblTestplanMapper testplanMapper;

    @Resource
    YhrPageMapper yhrPageMapper;

    @Resource
    TblTesttaskMapper testtaskMapper;

    @Resource
    TblTesttaskAttMapper testtaskAttMapper;


    @Resource
    TblTestplanMatrixMapper testplanMatrixMapper;

    @Resource
    TblTestelementMapper  testelementMapper;


    @Override
    public IPage<TblTestplan> findAllTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,Integer pageSize) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, pageSize);

        String sql="SELECT DISTINCT * from TBL_COM_EXT_TESTPLAN te WHERE TE.PLANSTATUS!='已完成' and TE.PLANSTATUS!='未启动' and TE.ORGID in ( select ORGID from TBL_ORGANIZATION where (FATHERORGID="+orgid+" AND ORGTYPE=0 OR ORGID="+orgid+"))";

        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }
            if (plan.getPlannumber()!=null) {
                sql+=" and PLANNUMBER like '%"+plan.getPlannumber()+"%' ";

            }
            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }
            if (plan.getPlanstatus()!=null) {
                sql+=" and PLANSTATUS like '%"+plan.getPlanstatus()+"%'";

            }
        }

        if (StringUtils.isNotBlank(starttime_min)) {
            sql+=" and STARTTIME >=to_date('"+starttime_min+"','yyyy-MM-dd') ";

        }
        if (StringUtils.isNotBlank(starttime_max)) {
            sql+=" and ENDTIME <=to_date('"+starttime_max+"','yyyy-MM-dd') ";

        }

        sql+=" ORDER BY TESTPLANID desc";

        return  this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public IPage<TblTestplan> findAllnoSjTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String userid,Integer pageSize) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, pageSize);

        String sql=" SELECT * FROM TBL_COM_EXT_TESTPLAN pl  WHERE PL.PLANSTATUS!='已完成' and PL.PLANSTATUS!='未启动' and (PL.CREATID ="+userid+" or PL.STAFFID= "+userid+") ";

        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }
            if (plan.getPlannumber()!=null) {
                sql+=" and PLANNUMBER like '%"+plan.getPlannumber()+"%' ";

            }
            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }
            if (plan.getPlanstatus()!=null) {
                sql+=" and PLANSTATUS like '%"+plan.getPlanstatus()+"%'";

            }
        }
        if (StringUtils.isNotBlank(starttime_min)) {
            sql+=" and STARTTIME >=to_date('"+starttime_min+"','yyyy-MM-dd') ";

        }
        if (StringUtils.isNotBlank(starttime_max)) {
            sql+=" and ENDTIME <=to_date('"+starttime_max+"','yyyy-MM-dd') ";

        }

        sql+=" ORDER BY TESTPLANID desc";
        return   this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public IPage<Map<String, Object>> findAllnoSjTrack2(BigDecimal selectProjectid, Integer pageNumber) {
        IPage<Map<String, Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="select  AA.PLANNUMBER, aa.PLANNAME, AA.REALNAME,CASE WHEN AA.ZS=AA.wc then cast('已完成' as varchar(12)) else cast('未完成' as varchar(12))  END as zt,"
                + " CASE WHEN AA.ZS = AA.tj THEN cast('是' as varchar(12)) ELSE cast('否' as varchar(12)) END AS sftj  from ( SELECT COUNT (*) zs, PLANID, CPUSERID, STA.REALNAME, PL.PLANNAME, PL.PLANNUMBER," +
                " (select count(*) from TBL_COM_EXT_TESTTASK t1 WHERE   T1.CPUSERID=TASK.CPUSERID and TASK.PLANID=T1.PLANID and T1.TESTSTATUS='已完成') as wc ,"
                + " ( SELECT COUNT (*) FROM TBL_COM_EXT_TESTTASK t1 WHERE T1.CPUSERID = TASK.CPUSERID AND TASK.PLANID = T1.PLANID AND T1.COMPLETESTAUS = 1 ) AS tj " +
                " FROM TBL_COM_EXT_TESTTASK task LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID  " +
                " LEFT JOIN TBL_COM_EXT_TESTPLAN pl on TASK.PLANID=PL.TESTPLANID WHERE PLANID ="+selectProjectid+
                " GROUP BY TASK.CPUSERID, PLANID,STA.REALNAME,PL.PLANNAME, PL.PLANNUMBER ) aa ";
        return this.yhrPageMapper.getPage(page,sql);
    }
}
