package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.JsonBean;
import com.huabo.compliance.entity.*;
import com.huabo.compliance.mapper.*;
import com.huabo.compliance.service.CsrwService;
import com.huabo.compliance.util.ConstClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author：yhr
 * @date:2022-09-13 13:53
 * @description:
 */
@Service
public class CsrwServiceImpl implements CsrwService {



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

    @Resource
    TblTesttemplTypeMapper   testtemplTypeMapper;

    @Override
    public IPage<TblTestplan> findAllRwToOrg(Integer pageNumber, String userid, String orgid,Integer pageSize) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, pageSize);
        String sql=" SELECT DISTINCT pl.* from TBL_COM_EXT_TESTPLAN pl LEFT JOIN TBL_COM_EXT_TESTTASK task on PL.TESTPLANID=TASK.PLANID "
                + " where  TASK.COMPLETESTAUS<>1 AND PL.PLANSTATUS!='未启动' and TASK.CPUSERID="+userid
                + " and (pl.ORGID in (select ORGID from TBL_ORGANIZATION where 1=1 and STATUS = 0 start with  FATHERORGID= "+orgid+" and ORGTYPE=0 connect by prior orgid= fatherorgid) OR pl.ORGID ="+ orgid +")";

        sql+=" ORDER BY pl.TESTPLANID ";

        return  this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public List<Tree> getTreeCSRWByChildsReturn(String tempId, String userid, String planid) {
          List<Tree> treelist = new ArrayList<>();
          String sql1="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
          List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);
          String sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_COM_EXT_TESTTASK WHERE CPUSERID= " +userid+" AND returnstatus=1 )) connect by prior PARENTID= TYPEID";
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
                    tree.setUrl("/nbkz/csrw/def_list?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    //int count = findByuser(tempId,userid,tblTestTempType.getTypeId().toString());
                    int count = list.size();
                    if(count>0){

                        tree.setChildren(getchildCSRWchild(tblTestTempType,tempId,planid,list));

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


    public List<Tree> getchildCSRWchild(TblTesttemplType tempType,String tempId,String planid,List<TblTesttemplType> lists){
        List<Tree> treelist = new ArrayList<>();
        String sql="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid  = "+tempType.getTypeid();
        List<TblTesttemplType> list  = this.testtemplTypeMapper.getListBySql(sql);
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
                    tree.setUrl("/nbkz/csrw/def_list?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    String sqlcount="select count(*) from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid  = "+tempType.getTypeid();
                    long count = this.yhrPageMapper.queryCount(sqlcount);
                    if(count>0){
                        tree.setChildren(getchildCSRWchild(tblTestTempType,tempId,planid,lists));
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
    public List<Tree> getTreeCSRWByChilds(String tempId, String userid, String planid) {
        List<Tree> treelist = new ArrayList<>();
        String sql1="select * from TBL_COM_EXT_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);
        //String sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID IN ("
        //	+ " SELECT elementid FROM TBL_COM_EXT_TESTTASK WHERE CPUSERID= " +userid+"  )) connect by prior PARENTID= TYPEID";
        String sql;
        List<TblTesttemplType> list = null;

            sql="select DISTINCT * from TBL_COM_EXT_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_COM_EXT_TESTELEMENT   ent inner JOIN TBL_COM_EXT_TESTTASK task on ent.ELEMENTID=task.ELEMENTID WHERE CPUSERID = "+userid+") connect by prior PARENTID= TYPEID";
            list = this.testtemplTypeMapper.getListBySql(sql);

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
                    tree.setUrl("/nbkz/csrw/def_list?node="+tblTestTempType.getTypeid()+"&templId="+tempId+"&planid="+planid);
                    //int count = findByuser(tempId,userid,tblTestTempType.getTypeId().toString());
                    int count = list.size();
                    if(count>0){
                        tree.setChildren(getchildCSRWchild(tblTestTempType,tempId,planid,list));
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
    public IPage<Map<String, Object>> fingByTreeCSRWByUserReturn(String node, String templId, String planid, BigDecimal userid, Integer pageNumber) {
        IPage<Map<String,Object>> pageBean=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE WHEN TASK.RETURNSTATUS =1 THEN  '退回' 	when task.TESTSTATUS='已完成' then '已完成' else '未完成'  END as sta,TASK.RETURNSTATUS,TASK.PROPOSAL  from TBL_COM_EXT_TESTELEMENT emt " +
                " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE  TASK.RETURNSTATUS=1 ";
                if(StringUtils.isNotBlank(node)){
                	sql+="AND EMT.TYPEID="+node;
                }
                sql+= "AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid +" and TASK.CPUSERID="+userid+"  ORDER BY TASK.TESTTASKID ASC ";

        return yhrPageMapper.getPage(pageBean,sql);
    }

    @Override
    public IPage<Map<String, Object>> fingByTreeCSRWByUser(String node, String templId, String planid, BigDecimal userid, Integer pageNumber) {
        IPage<Map<String,Object>> pageBean=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE WHEN TASK.RETURNSTATUS =1 THEN  '退回' 	when task.TESTSTATUS='已完成' then '已完成'  else '未完成'  END as sta  from TBL_COM_EXT_TESTELEMENT emt " +
                " left  JOIN TBL_COM_EXT_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE  1=1 ";
     if(StringUtils.isNotBlank(node)){
    	sql+="and  EMT.TYPEID="+node;
     }
     sql+="and  TASK.PLANID="+planid +" and TASK.CPUSERID="+userid+" ORDER BY TASK.TESTTASKID ASC";
        return yhrPageMapper.getPage(pageBean,sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAll(BigDecimal planid, BigDecimal userid) throws Exception{

        String sql="SELECT * from TBL_COM_EXT_TESTTASK where   TESTPOINTVALIDITY is not NULL and PLANID="+planid+" and CPUSERID="+userid;
        List<TblTesttask> listw =  this.testtaskMapper.getListBySql(sql);
        sql="SELECT * from TBL_COM_EXT_TESTTASK where   PLANID="+planid+" and CPUSERID="+userid;
        List<TblTesttask> list =  this.testtaskMapper.getListBySql(sql);

        if (listw != null && list != null && list.size() == listw.size()) {
            for (TblTesttask tblTesttask : list) {
                tblTesttask.setCompletestaus(new BigDecimal(1));
                tblTesttask.setReturnstatus(new BigDecimal(0));
                testtaskMapper.updateEntity(tblTesttask);
            }
            sql="SELECT * from TBL_COM_EXT_TESTTASK where   PLANID="+planid;
            List<TblTesttask> alls = this.testtaskMapper.getListBySql(sql);
            sql="SELECT * from TBL_COM_EXT_TESTTASK where  COMPLETESTAUS=1 and  PLANID="+planid;
            List<TblTesttask> wnc = this.testtaskMapper.getListBySql(sql);
            String all = alls.size() + "";
            String wc = wnc.size() + "";
            if (alls != null && wnc != null && all.equals(wc)) {
                TblTestplan plan = (TblTestplan)testplanMapper.findById(planid);
                plan.setPlanstatus("已完成");
                plan.setReturnstatus(new BigDecimal(0));
                testplanMapper.updateEntity(plan);
            }
            return JsonBean.success();

        } else {
            return JsonBean.error("信息未填完整");
        }
    }
}
