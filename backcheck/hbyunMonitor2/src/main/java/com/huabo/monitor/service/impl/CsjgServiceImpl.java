package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTesttask;
import com.huabo.monitor.entity.TblTesttemplType;
import com.huabo.monitor.entity.Tree;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblTestplanMapper;
import com.huabo.monitor.mapper.TblTesttaskMapper;
import com.huabo.monitor.mapper.TblTesttemplTypeMapper;
import com.huabo.monitor.mapper.YhrPageMapper;
import com.huabo.monitor.service.CsjgService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.result.TestElementResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    
    @Resource
    TblOrganizaService tblOrganizaService;
    
    @Resource
    TblTesttaskMapper  tblTesttaskMapper;

    @Override
    public IPage<Map<String, Object>> findAllCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal orgid,BigDecimal staffid,Integer authorityType) {
        IPage<Map<String, Object>> page=new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        String sql=" SELECT distinct te.TESTPLANID,te.planname,te.TESTEDORGS,te.PLANSTATUS,te.PLANYEAR from TBL_TESTPLAN te\n" +
                "WHERE TE.ORGID in (SELECT T.ORGID FROM TBL_ORGANIZATION T CONNECT BY PRIOR T.ORGID = T.FATHERORGID and T.ORGTYPE = 0 START WITH T.ORGID ="+orgid+" )" +
                "  and  TESTPLANID in ( SELECT DISTINCT PLANID from TBL_TESTTASK WHERE COMPLETESTAUS=1) ";
        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }

            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }

        }
        if (Objects.equals(authorityType,0)){
			sql+=" and CREATID = '"+staffid+"'";
		}
        sql+=" ORDER BY TESTPLANID desc";

        String sqlAll="select lin.*,\n" +
                "       (select count(*) from TBL_TESTTASK where PLANID=lin.TESTPLANID ) allcount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=1 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) ycount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=2 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) wcount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=3 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) bcount\n" +
                "       from(";
        sqlAll+=sql;
        sqlAll+=" ) lin ";


        return yhrPageMapper.getPage(page,sqlAll); 
    }

    @Override
    public IPage<Map<String, Object>> findAllWCSjCSHZ(TblTestplan plan, Integer pageNumber, BigDecimal staffid,Integer authorityType) {
        IPage<Map<String, Object>> page=new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        String sql="SELECT pl.TESTPLANID,pl.planname,pl.TESTEDORGS,pl.PLANSTATUS,pl.PLANYEAR FROM TBL_TESTPLAN pl  WHERE (PL.CREATID ="+staffid+" or PL.STAFFID= "+staffid+") and PL.TESTPLANID in (SELECT DISTINCT PLANID from TBL_TESTTASK WHERE COMPLETESTAUS=1) ";
        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }

            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }

        }
		if (Objects.equals(authorityType,0)){
			sql+=" and CREATID = '"+staffid+"'";
		}
        sql+=" ORDER BY TESTPLANID desc";

        String sqlAll="select lin.*,\n" +
                "       (select count(*) from TBL_TESTTASK where PLANID=lin.TESTPLANID ) allcount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=1 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) ycount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=2 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) wcount,\n" +
                "       (select count(*) from TBL_TESTTASK where TESTPOINTVALIDITY=3 and  COMPLETESTAUS=1 and PLANID=lin.TESTPLANID ) bcount\n" +
                "       from(";
        sqlAll+=sql;
        sqlAll+=" ) lin ";
        return yhrPageMapper.getPage(page,sqlAll);
    }

    @Override
    public List<Tree> getTreeLeftyx(String tempId, String planid, String url, String userid) {

        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);

        String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_TESTTASK WHERE testpointvalidity=1 and COMPLETESTAUS=1 )) connect by prior PARENTID= TYPEID";
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
        String sql1="select * from TBL_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);


        //String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1  and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
               // + " SELECT elementid FROM TBL_TESTTASK WHERE CPUSERID= " +userid+" )) connect by prior PARENTID= TYPEID";
        String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1  and TESTTEMPLETAID= "+tempId+"  and  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_TESTTASK WHERE CPUSERID= " +userid+" )) ";
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
    public PageInfo<Map<String, Object>> fingByTreeCSHZByUser(String node, String templId, String planid, String userid, Integer pageNumber, Integer pageSize) {


        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE 	when task.TESTSTATUS='已完成' then cast('已完成' as varchar(12)) else cast('未完成' as varchar(12)) END as sta,EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2) LONGSTRING2 from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid +" and TASK.CPUSERID="+userid+" ORDER BY TASK.TESTTASKID ASC";
        
		PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->this.yhrPageMapper.getPage(sql));
		return pageInfo;
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

            String sql="update TBL_TESTTASK set completestaus=0 where CPUSERID="+userid+" and PLANID="+planid;

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
    	 List<Object[]> obj = new ArrayList<Object[]>();
    	try {
    	String sql = "SELECT DISTINCT ype.TYPENAME,ENT.ELEMENTCODE,ENT.BUSINESSDESC,ENT.RISKTYPE,ENT.CONTROLTARGET "
                + ",ENT.CONTROLMEASURES,ENT.CHECKMETHOD,ASK.PROCEDURES,ENT.LONGSTRING1,ENT.LONGSTRING2,ASK.TESTRESULT, "
                + "EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,(CASE WHEN ASK.TESTPOINTVALIDITY='1' THEN '有效' WHEN ASK.TESTPOINTVALIDITY = '2' THEN '无效' WHEN ASK.TESTPOINTVALIDITY = '3' THEN '不适用' END) TESTPOINTVALIDITY  "
                + "FROM TBL_TESTELEMENT ENT "
                + "LEFT JOIN TBL_TESTTEMPL_TYPE ype ON ype.TYPEID=ENT.TYPEID "
                + "LEFT JOIN TBL_TESTTEMPLE PLE ON PLE.TESTTEMID=ype.TESTTEMPLETAID "
                + "LEFT JOIN TBL_TESTPLAN LAN ON LAN.TESTTEMID=PLE.TESTTEMID "
                + "LEFT JOIN TBL_TESTTASK ask ON ask.ELEMENTID=ENT.ELEMENTID "
                + "WHERE ask.PLANID= " + planid
                + " ORDER BY ENT.ELEMENTCODE ";
//        List<LinkedHashMap<String, Object>> mapList = this.yhrPageMapper.queryBySqlToListLinkMap(sql);
//        for (Map<String, Object> map : mapList) {
//            System.out.println(map.values());
//            Collection values = map.values();
//            List list = new ArrayList(values);
//            obj.add(list.toArray());
//        }
    	List<TestElementResult> mapList= this.yhrPageMapper.getMap(sql);
          obj = mapList.stream()
                .map(r -> new Object[]{r.getTYPENAME(),r.getELEMENTCODE(), r.getBUSINESSDESC(), r.getRISKTYPE(),r.getCONTROLTARGET()
              		  ,r.getCONTROLMEASURES() ,r.getCHECKMETHOD(), r.getPROCEDURES(), r.getLONGSTRING1(),r.getLONGSTRING2(),
             		 r.getTESTRESULT(),r.getDESIGNPOINTVALIDITY(),r.getEXECUTEPOINTVALIDITY(),r.getTESTPOINTVALIDITY()})
                .collect(Collectors.toList());
  	} catch (Exception e) {
		// TODO: handle exception
  		e.printStackTrace();
	}
        return obj;
    }


    @Override
    public List<Tree> getTreeLeftwx(String tempId, String planid, String url, String userid) {

        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);

       // String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
           //     + " SELECT elementid FROM TBL_TESTTASK WHERE testpointvalidity=2 and COMPLETESTAUS=1 )) connect by prior PARENTID= TYPEID";
        String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+" and  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
         + " SELECT elementid FROM TBL_TESTTASK WHERE testpointvalidity=2 and COMPLETESTAUS=1 ))";
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
        String sql1="select * from TBL_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);

        //String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
          //      + " SELECT elementid FROM TBL_TESTTASK WHERE testpointvalidity=3 and COMPLETESTAUS=1  )) connect by prior PARENTID= TYPEID";
        
        String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+" and  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_TESTTASK WHERE testpointvalidity=3 and COMPLETESTAUS=1  )) ";
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
    public PageInfo<Map<String, Object>> fingByTreeCSRW(String node, String templId, String planid, Integer pageNumber,Integer pageSize) {

        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->this.yhrPageMapper.getPage(sql));
		return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> fingByTreeCSJGY(String node, String templId, String planid, Integer pageNumber,Integer pageSize) {
        String sql;
        if(StringUtils.isBlank(templId)) {
            sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2   from TBL_TESTELEMENT emt " +
                    " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                    " WHERE TASK.TESTPOINTVALIDITY=1  and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        }else {
            sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2   from TBL_TESTELEMENT emt " +
                    " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                    " WHERE EMT.TYPEID="+node+" AND TASK.TESTPOINTVALIDITY=1 AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        }
        PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->this.yhrPageMapper.getPage(sql));
		return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> fingByTreeCSJGW(String node, String templId, String planid, Integer pageNumber,Integer pageSize) {

        String sql=" SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE,EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND TASK.TESTPOINTVALIDITY=2 AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";

        PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->this.yhrPageMapper.getPage(sql));
      	return pageInfo;
    }

    @Override
    public PageInfo<Map<String, Object>> fingByTreeCSJGB(String node, String templId, String planid, Integer pageNumber,Integer pageSize) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
       String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,TASK.attid,TASK.attname,EMT.ELEMENTCODE,EXECUTEPOINTVALIDITY,DESIGNPOINTVALIDITY,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE EMT.TYPEID="+node+" AND TASK.TESTPOINTVALIDITY=3 AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid+" ORDER BY TASK.TESTTASKID ASC";
       PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->this.yhrPageMapper.getPage(sql));
       return pageInfo;    
       }

    @Override
    public IPage<Map<String, Object>> findAllnoSjResult(BigDecimal planid, Integer pageNumber) {
        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT COUNT (*) zs,PLANID,CPUSERID,STA.REALNAME,PL.PLANNAME,PL.PLANNUMBER FROM TBL_TESTTASK task "
                + "	LEFT JOIN TBL_STAFF sta ON TASK.CPUSERID = STA.STAFFID"
                + " LEFT JOIN TBL_TESTPLAN pl ON TASK.PLANID = PL.TESTPLANID "
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

	@Override
	public PageInfo<Map<String, Object>> findAllCSHZNew(TblTestplan plan, Integer pageNumber, BigDecimal orgid,
			BigDecimal staffid, Integer authorityType,TblStaffUtil user) throws Exception{
		List<BigDecimal> orgList=tblOrganizaService.getTblOrganizationAll(orgid);
		plan.setOrgids(orgList);
		plan.setStaffid(staffid);
		plan.setAuthorityType(authorityType);
		plan.setOrgid(orgid);
		 String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "te.LINKORGID", "te.LINKDEPTID", "te.CREATID", "te.SECRECTLEVELID", "te.STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
		PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllCSHZNew(plan,sql));
		return pageInfo;
	}

	@Override
	public PageInfo<Map<String, Object>> findAllWCSjCSHZNew(TblTestplan plan, Integer pageNumber, BigDecimal staffid,
			Integer authorityType,TblStaffUtil user) throws Exception{
		// TODO Auto-generated method stub
		plan.setStaffid(staffid);
		plan.setAuthorityType(authorityType);
		  StringBuffer querySql=new StringBuffer();
		   if(plan.getCreatid()!=null){
		    	querySql.append(" or (PL.CREATID =").append(plan.getCreatid()).append(" or PL.STAFFID=").append(plan.getCreatid()).append(")");
		    }
		 String sql = GeneralSQLConcatConfig.concatSecrectSqlCase2(user.getCurrentOrg().getUseSecrect(), true, "pl.LINKORGID", "pl.LINKDEPTID", "pl.CREATID", "pl.SECRECTLEVELID", "pl.STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds(),querySql.toString());
		PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllWCSjCSHZNew(plan,sql));
		return pageInfo;
	}

	@Override
	public PageInfo<Map<String, Object>> findAllnoSjResultNew(BigDecimal planid, Integer pageNumber) {
		// TODO Auto-generated method stub
		PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblTesttaskMapper.findAllnoSjResultNew(planid));
		return pageInfo;
	}

	@Override
	public PageInfo<Map<String, Object>> findAll(TblTestplan plan, Integer pageNumber, TblStaffUtil user)
			throws Exception {
		PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAll(plan));
		return pageInfo;
	}

}
