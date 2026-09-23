package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.compliance.entity.*;
import com.huabo.compliance.mapper.*;
import com.huabo.compliance.service.CsfaService;
import com.huabo.compliance.util.ConstClass;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service

public class CsfaServiceImpl implements CsfaService {


    @Resource
    TblTestplanMapper  testplanMapper;

    @Resource
    YhrPageMapper  yhrPageMapper;

    @Resource
    TblTesttaskMapper   testtaskMapper;

    @Resource
    TblTesttaskAttMapper  testtaskAttMapper;


    @Resource
    TblTestplanMatrixMapper testplanMatrixMapper;

    @Resource
    TblTestelementMapper  testelementMapper;

    @Override
    public boolean isSJByOrgId(String userOrgid) {
        String sql = "select count(*) from TBL_ORGANIZATION where audittype = '1' and orgid = " + userOrgid;
        Long num = yhrPageMapper.queryCount(sql);
        if (num == 0) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public IPage<TblTestplan> findAll(TblTestplan plan,
                                      Integer pageNumber,
                                      String starttime_min,
                                      String starttime_max,
                                      BigDecimal orgid,Integer pageSize) {

        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, pageSize);//ConstClass.DEFAULT_SIZE

        String sql="select * from TBL_COM_EXT_TESTPLAN where\n" +
                "                                 orgid in (\n" +
                "    SELECT T.orgid\n" +
                "    FROM TBL_ORGANIZATION T\n" +
                "    CONNECT BY PRIOR T.ORGID = T.FATHERORGID\n" +
                "           and T.ORGTYPE = 0\n" +
                "    START WITH T.ORGID = "+orgid+"\n" +
                "\n" +
                ")";

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
    public IPage<TblTestplan> findAllnoSj(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String userid,Integer pageSize) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, pageSize);

        String sql=" SELECT * FROM TBL_COM_EXT_TESTPLAN pl  WHERE PL.CREATID ="+userid+" or PL.STAFFID= "+userid;

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
    public TblTestplanVo queryOneTestPlanVo(BigDecimal selectProjectid) {
        return this.testplanMapper.getOneTblTestplanVo(selectProjectid);
    }

    @Override
    public IPage<TblTesttempleVo> getTestTemp(BigDecimal orgid, Integer pageNumber, String templeNumber, String templename) {
        IPage<TblTesttempleVo> ipage=new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        String sql="select * from TBL_COM_EXT_TESTTEMPLE t where 1=1  and t.tblcomany="+orgid;

        if(StringUtils.isNotBlank(templename)){
            sql += " and t.templename like '%"+templename+"%'";
        }
        if(StringUtils.isNotBlank(templeNumber)){
            sql += " and t.templeNumber like '%"+templeNumber+"%'";
        }
        sql += " order by t.testtemid desc";

        return this.testplanMapper.getTesttempleVoSqlPage(ipage,sql);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delTesttasksByPanId(BigDecimal testplanid) {
        String sql="delete from TBL_COM_EXT_TESTTASK where planid="+testplanid;
        this.yhrPageMapper.delete(sql);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteBytaskId(BigDecimal testtaskid) {

//        String sql=" delete from TBL_COM_EXT_TESTTASK_ATT where testtaskid="+testtaskid;
//        this.yhrPageMapper.delete(sql);
//
//        sql="update TBL_COM_EXT_TESTPLAN set testtaskid=null where testtaskid="+testtaskid;
//
//        this.yhrPageMapper.update(sql);

        this.testtaskMapper.deleteById(testtaskid);

    }

    @Override
    public List<Tree> getTreeC(BigDecimal testtemid) {

        return  this.testplanMapper.getTreeListByTemid(testtemid);
    }

    @Override
    public IPage<Map<String, Object>> fingByTree(BigDecimal node, BigDecimal templId, BigDecimal planid, Integer pageNumber) {

        IPage<Map<String, Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="\n" +
                "SELECT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,EMT.CONTROLREQ,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID\n" +
                "from TBL_COM_EXT_TESTELEMENT emt\n" +
                "         left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID\n" +
                "         LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID\n" +
                "where  TASK.PLANID="+planid+" and EMT.TEMPLID="+templId;
                if(node!=null){
                	sql+= " and EMT.TYPEID="+node;
                }
               
        return this.yhrPageMapper.getPage(page,sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTesttsak(String task, String planid, String userid) {
        String[] emeid = task.split(",");
        if (emeid != null) {
            for (String string : emeid) {
            	if(StringUtils.isNotBlank(string)){
                QueryWrapper<TblTesttask> qw = new QueryWrapper<>();
                qw.eq("elementId", string);
                qw.eq("PLANID", planid);
                TblTesttask taskTblTesttask = this.testtaskMapper.selectOne(qw);
                if (taskTblTesttask != null) {
                    taskTblTesttask.setCpuserid(new BigDecimal(userid));
                    testtaskMapper.updateById(taskTblTesttask);
                } else {
                    TblTesttask newtask = new TblTesttask();
                    newtask.setCpuserid(new BigDecimal(userid));
                    newtask.setPlanid(new BigDecimal(planid));
                    newtask.setElementid(new BigDecimal(string));
                    newtask.setCompletestaus(new BigDecimal(0));
                    testtaskMapper.insert(newtask);
                }
            }
            }
        }
    }

    @Override
    public Integer findByPlan(BigDecimal planid) {

        String sql="SELECT count(DISTINCT ment.ELEMENTID) from TBL_COM_EXT_TESTELEMENT ment  left join TBL_COM_EXT_TESTPLAN pa on ment.templId=pa.TESTTEMID WHERE pa.testplanid="+planid;
        Long count=this.yhrPageMapper.queryCount(sql);
        if(count>0){
           return count.intValue();
        }else{
           return null;
        }

    }

    @Override
    public Integer findByPlanwStra(BigDecimal planid) {
        String sql="SELECT count(DISTINCT ment.ELEMENTID) from TBL_COM_EXT_TESTELEMENT ment inner join TBL_COM_EXT_TESTTASK task on ment.elementId=task.elementId  left join TBL_COM_EXT_TESTPLAN pa on ment.templId=pa.TESTTEMID WHERE TASK.CPUSERID is not NULL   AND task.PLANID="+planid;
        Long count=this.yhrPageMapper.queryCount(sql);
        if(count>0){
            return count.intValue();
        }else{
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTestPlan(BigDecimal selectProjectid) {
        this.testplanMapper.deleteTestPlan(selectProjectid);
    }

}