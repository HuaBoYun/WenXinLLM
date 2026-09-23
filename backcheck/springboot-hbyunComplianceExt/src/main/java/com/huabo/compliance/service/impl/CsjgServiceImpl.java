package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTesttask;
import com.huabo.compliance.entity.TblTesttemplType;
import com.huabo.compliance.entity.Tree;
import com.huabo.compliance.mapper.TblTestplanMapper;
import com.huabo.compliance.mapper.TblTesttaskMapper;
import com.huabo.compliance.mapper.TblTesttemplTypeMapper;
import com.huabo.compliance.mapper.YhrPageMapper;
import com.huabo.compliance.service.CsjgService;
import com.huabo.compliance.util.ConstClass;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author：yhr
 * @date:2022-09-15 13:01
 * @description:
 */
@Service
public class CsjgServiceImpl implements CsjgService {

    @Resource
    YhrPageMapper  yhrPageMapper;

    @Resource
    TblTesttemplTypeMapper testtemplTypeMapper;

    @Resource
    TblTesttaskMapper testtaskMapper;
    @Resource
    TblTestplanMapper testplanMapper;

    @Override
    public IPage<Map<String, Object>> findAllCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal orgid,Integer pageSize) {
        IPage<Map<String, Object>> page=new Page<>(pageNumber,pageSize);
        String sql=" SELECT distinct te.TESTPLANID,te.planname,te.TESTEDORGS,te.PLANSTATUS,te.PLANYEAR from TBL_COM_EXT_TESTPLAN te\n" +
                "WHERE TE.ORGID in (SELECT T.ORGID FROM TBL_ORGANIZATION T CONNECT BY PRIOR T.ORGID = T.FATHERORGID and T.ORGTYPE = 0 START WITH T.ORGID ="+orgid+" )" +
                "  and  TESTPLANID in ( SELECT DISTINCT PLANID from TBL_COM_EXT_TESTTASK WHERE COMPLETESTAUS=1) ";
        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }

            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }

        }
        sql+=" ORDER BY TESTPLANID desc";

        String sqlAll="select lin.*,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where PLANID=lin.TESTPLANID ) allcount,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where TESTPOINTVALIDITY=1 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) ycount,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where TESTPOINTVALIDITY=2 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) wcount,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where TESTPOINTVALIDITY=3 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) bcount\n" +
                "       from(";
        sqlAll+=sql;
        sqlAll+=" ) lin ";


        return yhrPageMapper.getPage(page,sqlAll);
    }

    @Override
    public IPage<Map<String, Object>> findAllWCSjCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal staffid,Integer pageSize) {
        IPage<Map<String, Object>> page=new Page<>(pageNumber, pageSize);

        String sql="SELECT pl.TESTPLANID,pl.planname,pl.TESTEDORGS,pl.PLANSTATUS,pl.PLANYEAR FROM TBL_COM_EXT_TESTPLAN pl  WHERE (PL.CREATID ="+staffid+" or PL.STAFFID= "+staffid+") and PL.TESTPLANID in (SELECT DISTINCT PLANID from TBL_COM_EXT_TESTTASK WHERE COMPLETESTAUS=1) ";
        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }

            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }

        }
        sql+=" ORDER BY TESTPLANID desc";

        String sqlAll="select lin.*,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where PLANID=lin.TESTPLANID ) allcount,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where TESTPOINTVALIDITY=1 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) ycount,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where TESTPOINTVALIDITY=2 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) wcount,\n" +
                "       (select count(*) from TBL_COM_EXT_TESTTASK where TESTPOINTVALIDITY=3 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) bcount\n" +
                "       from(";
        sqlAll+=sql;
        sqlAll+=" ) lin ";
        return yhrPageMapper.getPage(page,sqlAll);
    }

    @Override
    public List<Tree> getTreeLeftyx(String tempId, String planid, String url, String userid) {

        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);

        String sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_COM_EXT_TESTTASK WHERE testpointvalidity=1 and COMPLETESTAUS=1 )) connect by prior PARENTID= TYPEID";
        List<TblTesttemplType> list = this.testtemplTypeMapper.getListBySql(sql);

        for (TblTesttemplType tblTestTempType : root) {
            boolean flag=false;
            if(list!=null && list.size()>0){
                for (TblTesttemplType addtype : list) {
                    if(addtype.getTypeid().toString().equals(tblTestTempType.getTypeid().toString())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    Tree tree = new Tree();
                    tree.setId(tblTestTempType.getTypeid());
                    tree.setName(tblTestTempType.getTypename());
                    tree.setTarget("mainFramex");
                    tree.setUrl(url+"?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    //int count = findByuser(tempId,userid,tblTestTempType.getTypeId().toString());
                    long count = this.testtemplTypeMapper.getChildCount(tblTestTempType.getTypeid());
                    if(count>0){
                        tree.setIsParent(true);
                        tree.setChildren(getchildLeftAll(tblTestTempType,tempId,planid,url,list));

                    }
                    if(tree.getChildren()!=null && tree.getChildren().size()>0){
                        tree.setIsParent(true);
                    }
                    treelist.add(tree);
                }
            }
        }
        return treelist;

    }


    @Override
    public List<Tree> getTreeCSHZByChilds(String tempId, String userid, String planid) {
        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);


        String sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1  and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_COM_EXT_TESTTASK WHERE CPUSERID= " +userid+" )) connect by prior PARENTID= TYPEID";
        List<TblTesttemplType> list = this.testtemplTypeMapper.getListBySql(sql);

        for (TblTesttemplType tblTestTempType : root) {
            boolean flag=false;
            if(list!=null && list.size()>0){
                for (TblTesttemplType addtype : list) {
                    if(addtype.getTypeid().toString().equals(tblTestTempType.getTypeid().toString())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    Tree tree = new Tree();
                    tree.setId(tblTestTempType.getTypeid());
                    tree.setName(tblTestTempType.getTypename());
                    tree.setTarget("mainFramex");
                    tree.setUrl("/nbkz/cshz/def_list?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid+"&userid="+userid);
                    long count = this.testtemplTypeMapper.getChildCount(tblTestTempType.getTypeid());
                    if(count>0){
                        tree.setIsParent(true);
                        tree.setChildren(getchildCSHZchild(tblTestTempType,tempId,planid,list,userid));
                    }
                    treelist.add(tree);
                }
            }
        }
        return treelist;
    }

    @Override
    public IPage<Map<String, Object>> fingByTreeCSHZByUser(String node, String templId, String planid, String userid, Integer pageNumber) {

        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);

        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE 	when task.TESTSTATUS='已完成' then cast('已完成' as varchar(12)) else cast('未完成' as varchar(12)) END as sta  from TBL_COM_EXT_TESTELEMENT emt " +
                " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid +" and TASK.CPUSERID="+userid+" ORDER BY TASK.TESTTASKID ASC";

        return this.yhrPageMapper.getPage(page,sql);

    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public void saveBack(String taskid, String proposal, String planid, String userid) {
        TblTesttask task = this.testtaskMapper.selectById(taskid);
        task.setReturnstatus(new BigDecimal(1));
        task.setProposal(proposal);
        testtaskMapper.updateById(task);
        if (StringUtils.isNotBlank(planid) && StringUtils.isNotBlank(userid)) {
            TblTestplan plan = (TblTestplan)this.testplanMapper.selectById(planid);
            plan.setReturnstatus(new BigDecimal(1));
            plan.setPlanstatus("执行中");
            testplanMapper.updateById(plan);

            String sql="update TBL_COM_EXT_TESTTASK set completestaus=0 where CPUSERID="+userid+" and PLANID="+planid;

            this.yhrPageMapper.update(sql);

//            QueryWrapper<TblTesttask> qw=new QueryWrapper<>();
//            qw.eq("CPUSERID",userid).eq("PLANID",planid);
//            List<TblTesttask> list = testtaskMapper.list(qw);
//            if (list != null && list.size() > 0) {
//                for (TblTesttask tblTesttask : list) {
//                    tblTesttask.setCompletestaus(new BigDecimal(0));
//                    testtaskMapper.updateById(tblTesttask);
//                }
//            }
        }
    }

    @Override
    public List<Object[]> findElementByPlanid(String planid) {
        String sql = "SELECT DISTINCT ype.TYPENAME,ENT.ELEMENTCODE,ENT.BUSINESSDESC,ENT.RISKTYPE,ENT.CONTROLTARGET "
                + ",ENT.CONTROLMEASURES,ENT.CHECKMETHOD,ASK.PROCEDURES,ASK.TESTRESULT "
                + ",(CASE WHEN ASK.TESTPOINTVALIDITY='1' THEN '有效' WHEN ASK.TESTPOINTVALIDITY = '2' THEN '无效' WHEN ASK.TESTPOINTVALIDITY = '3' THEN '不适用' END) TESTPOINTVALIDITY  "
                + "FROM TBL_COM_EXT_TESTELEMENT ENT "
                + "LEFT JOIN TBL_COM_EXT_TESTTEMPL_TYPE ype ON ype.TYPEID=ENT.TYPEID "
                + "LEFT JOIN TBL_COM_EXT_TESTTEMPLE PLE ON PLE.TESTTEMID=ype.TESTTEMPLETAID "
                + "LEFT JOIN TBL_COM_EXT_TESTPLAN LAN ON LAN.TESTTEMID=PLE.TESTTEMID "
                + "LEFT JOIN TBL_COM_EXT_TESTTASK ask ON ask.ELEMENTID=ENT.ELEMENTID "
                + "WHERE LAN.TESTPLANID= " + planid
                + "ORDER BY ENT.ELEMENTCODE ";
        List<LinkedHashMap<String, Object>> mapList = this.yhrPageMapper.queryBySqlToListLinkMap(sql);
        List<Object[]> obj = new ArrayList<Object[]>();
        for (Map<String, Object> map : mapList) {
            System.out.println(map.values());
            Collection values = map.values();
            List list = new ArrayList(values);
            obj.add(list.toArray());
        }
        return obj;
    }


    @Override
    public List<Tree> getTreeLeftwx(String tempId, String planid, String url, String userid) {

        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);

        String sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_COM_EXT_TESTTASK WHERE testpointvalidity=2 and COMPLETESTAUS=1 )) connect by prior PARENTID= TYPEID";
        List<TblTesttemplType> list = this.testtemplTypeMapper.getListBySql(sql);

        for (TblTesttemplType tblTestTempType : root) {
            boolean flag=false;
            if(list!=null && list.size()>0){
                for (TblTesttemplType addtype : list) {
                    if(addtype.getTypeid().toString().equals(tblTestTempType.getTypeid().toString())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    Tree tree = new Tree();
                    tree.setId(tblTestTempType.getTypeid());
                    tree.setName(tblTestTempType.getTypename());
                    tree.setTarget("mainFramex");
                    tree.setUrl(url+"?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    //int count = findByuser(tempId,userid,tblTestTempType.getTypeId().toString());
                    long count = this.testtemplTypeMapper.getChildCount(tblTestTempType.getTypeid());
                    if(count>0){
                        tree.setIsParent(true);
                        tree.setChildren(getchildLeftAll(tblTestTempType,tempId,planid,url,list));

                    }
                    if(tree.getChildren()!=null && tree.getChildren().size()>0){
                        tree.setIsParent(true);
                    }
                    treelist.add(tree);
                }
            }
        }
        return treelist;
    }

    @Override
    public List<Tree> getTreeLeftbxy(String tempId, String planid, String url, String userid) {

        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);

        String sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_COM_EXT_TESTTASK WHERE testpointvalidity=3 and COMPLETESTAUS=1  )) connect by prior PARENTID= TYPEID";
        List<TblTesttemplType> list = this.testtemplTypeMapper.getListBySql(sql);

        for (TblTesttemplType tblTestTempType : root) {
            boolean flag=false;
            if(list!=null && list.size()>0){
                for (TblTesttemplType addtype : list) {
                    if(addtype.getTypeid().toString().equals(tblTestTempType.getTypeid().toString())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    Tree tree = new Tree();
                    tree.setId(tblTestTempType.getTypeid());
                    tree.setName(tblTestTempType.getTypename());
                    tree.setTarget("mainFramex");
                    tree.setUrl(url+"?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    //int count = findByuser(tempId,userid,tblTestTempType.getTypeId().toString());
                    long count = this.testtemplTypeMapper.getChildCount(tblTestTempType.getTypeid());
                    if(count>0){
                        tree.setIsParent(true);
                        tree.setChildren(getchildLeftAll(tblTestTempType,tempId,planid,url,list));

                    }
                    if(tree.getChildren()!=null && tree.getChildren().size()>0){
                        tree.setIsParent(true);
                    }
                    treelist.add(tree);
                }
            }
        }
        return treelist;
    }

    @Override
    public IPage<Map<String, Object>> fingByTreeCSRW(String node, String templId, String planid, Integer pageNumber) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);

        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE  from TBL_COM_EXT_TESTELEMENT emt " +
                " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        return this.yhrPageMapper.getPage(page,sql);
    }

    @Override
    public IPage<Map<String, Object>> fingByTreeCSJGY(String node, String templId, String planid, Integer pageNumber) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql;

        if(StringUtils.isBlank(templId)) {
            sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE  from TBL_COM_EXT_TESTELEMENT emt " +
                    " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                    " WHERE TASK.TESTPOINTVALIDITY=1  and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        }else {
            sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE  from TBL_COM_EXT_TESTELEMENT emt " +
                    " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                    " WHERE EMT.TYPEID="+node+" AND TASK.TESTPOINTVALIDITY=1 AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        }
        return this.yhrPageMapper.getPage(page,sql);
    }

    @Override
    public IPage<Map<String, Object>> fingByTreeCSJGW(String node, String templId, String planid, Integer pageNumber) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);

        String sql=" SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE  from TBL_COM_EXT_TESTELEMENT emt " +
                " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND TASK.TESTPOINTVALIDITY=2 AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        return this.yhrPageMapper.getPage(page,sql);
    }

    @Override
    public IPage<Map<String, Object>> fingByTreeCSJGB(String node, String templId, String planid, Integer pageNumber) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
       String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE  from TBL_COM_EXT_TESTELEMENT emt " +
                " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND TASK.TESTPOINTVALIDITY=3 AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";
        return this.yhrPageMapper.getPage(page,sql);
    }

    @Override
    public IPage<Map<String, Object>> findAllnoSjResult(BigDecimal planid, Integer pageNumber) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT COUNT (*) zs,PLANID,CPUSERID,STA.REALNAME,PL.PLANNAME,PL.PLANNUMBER FROM TBL_COM_EXT_TESTTASK task "
                + "	LEFT JOIN TBL_STAFF sta ON TASK.CPUSERID = STA.STAFFID"
                + " LEFT JOIN TBL_COM_EXT_TESTPLAN pl ON TASK.PLANID = PL.TESTPLANID "
                + " WHERE PLANID = "+planid+" and TASK.COMPLETESTAUS=1  "
                + "	GROUP BY TASK.CPUSERID, PLANID, STA.REALNAME, PL.PLANNAME, PL.PLANNUMBER ";

        return this.yhrPageMapper.getPage(page,sql);
    }



    public List<Tree> getchildCSHZchild(TblTesttemplType tempType,String tempId,String planid,List<TblTesttemplType> lists,String userid){
        List<Tree> treelist = new ArrayList<>();
        List<TblTesttemplType> list  = this.testtemplTypeMapper.getchild(tempType.getTypeid());
        for (TblTesttemplType tblTestTempType : list) {
            boolean flag=false;
            if(list!=null && list.size()>0){
                for (TblTesttemplType addtype : lists) {
                    if(addtype.getTypeid().toString().equals(tblTestTempType.getTypeid().toString())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                    Tree tree = new Tree();
                    tree.setId(tblTestTempType.getTypeid());
                    tree.setName(tblTestTempType.getTypename());
                    tree.setTarget("mainFramex");
                    tree.setUrl("/nbkz/cshz/def_list?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid+"&userid="+userid);
                    long count = this.testtemplTypeMapper.getChildCount(tblTestTempType.getTypeid());
                    if(count>0){
                        tree.setIsParent(true);
                        tree.setChildren(getchildCSHZchild(tblTestTempType,tempId,planid,lists,userid));
                    }
                    treelist.add(tree);
                }
            }
        }
        return treelist;
    }


    public List<Tree> getchildLeftAll(TblTesttemplType tempType,String tempId,String planid,String url,List<TblTesttemplType> lists){
        List<Tree> treelist = new ArrayList<>();
        List<TblTesttemplType> list  = this.testtemplTypeMapper.getchild(tempType.getTypeid());
        for (TblTesttemplType tblTestTempType : list) {
            boolean flag=false;
            if(list!=null && list.size()>0){
                for (TblTesttemplType addtype : lists) {
                    if(addtype.getTypeid().toString().equals(tblTestTempType.getTypeid().toString())){
                        flag=true;
                        break;
                    }
                }
                if(flag){
                Tree tree = new Tree();
                tree.setId(tblTestTempType.getTypeid());
                tree.setName(tblTestTempType.getTypename());
                tree.setTarget("mainFramex");
                tree.setUrl(url+"?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    long count = this.testtemplTypeMapper.getChildCount(tblTestTempType.getTypeid());
                    if(count>0){
                        tree.setIsParent(true);
                        tree.setChildren(getchildLeftAll(tblTestTempType,tempId,planid,url,lists));
                    }
                    treelist.add(tree);
                }
            }
        }
        return treelist;
    }

}
