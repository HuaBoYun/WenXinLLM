package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.service.CsrwService;
import com.huabo.monitor.service.ITblTesttaskProblemFindService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    ITblTesttaskProblemFindService iTblTesttaskProblemFindService;
    
    @Resource
    TblOrganizaService tblOrganizaService;

    @Override
    public IPage<TblTestplan> findAllRwToOrg(Integer pageNumber, String userid, String orgid) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, ConstClass.DEFAULT_SIZE);
        String sql=" SELECT DISTINCT pl.* from TBL_TESTPLAN pl LEFT JOIN TBL_TESTTASK task on PL.TESTPLANID=TASK.PLANID "
                + " where  TASK.COMPLETESTAUS<>1 AND PL.PLANSTATUS!='未启动' and TASK.CPUSERID="+userid//+" and PL.CREATID="+userid
                + " and (pl.ORGID in (select ORGID from TBL_ORGANIZATION where 1=1 and STATUS = 0 start with  FATHERORGID= "+orgid+" and ORGTYPE=0 connect by prior orgid= fatherorgid) OR pl.ORGID ="+ orgid +")";

        sql+=" ORDER BY pl.TESTPLANID ";

        return  this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public List<Tree> getTreeCSRWByChildsReturn(String tempId, String userid, String planid) throws Exception {
          List<Tree> treelist = new ArrayList<>();
          String sql1="select * from TBL_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
          List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);
       //   String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
          //      + " SELECT elementid FROM TBL_TESTTASK WHERE CPUSERID= " +userid+" AND returnstatus=1 )) connect by prior PARENTID= TYPEID";
         String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  and  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
                + " SELECT elementid FROM TBL_TESTTASK WHERE CPUSERID= " +userid+" AND returnstatus=1 ))";
         
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
        String sql="select * from TBL_TESTTEMPL_TYPE t where t.parentid  = "+tempType.getTypeid();
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
                    String sqlcount="select count(*) from TBL_TESTTEMPL_TYPE t where t.parentid  = "+tempType.getTypeid();
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
        String sql1="select * from TBL_TESTTEMPL_TYPE t where t.parentid is null and t.testtempletaid = "+tempId;
        List<TblTesttemplType> root = this.testtemplTypeMapper.getListBySql(sql1);
        //String sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT WHERE ELEMENTID IN ("
        //	+ " SELECT elementid FROM TBL_TESTTASK WHERE CPUSERID= " +userid+"  )) connect by prior PARENTID= TYPEID";
        String sql;
        List<TblTesttemplType> list = null;
       // sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  start with  TYPEID in ( select TYPEID from TBL_TESTELEMENT   ent inner JOIN TBL_TESTTASK task on ent.ELEMENTID=task.ELEMENTID WHERE CPUSERID = "+userid+") connect by prior PARENTID= TYPEID";
        //新系统没有父子级别关系  替换如下接口   
        sql="select DISTINCT * from TBL_TESTTEMPL_TYPE where 1=1 and TESTTEMPLETAID= "+tempId+"  and  TYPEID in ( select TYPEID from TBL_TESTELEMENT   ent inner JOIN TBL_TESTTASK task on ent.ELEMENTID=task.ELEMENTID WHERE CPUSERID = "+userid+") ";
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
    public PageInfo<Map<String, Object>> fingByTreeCSRWByUserReturn(String node, String templId, String planid, BigDecimal userid, Integer pageNumber) {
        IPage<Map<String,Object>> pageBean=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE WHEN TASK.RETURNSTATUS =1 THEN  '退回' 	when task.TESTSTATUS='已完成' then '已完成' else '未完成'  END as sta,TASK.RETURNSTATUS,TASK.PROPOSAL,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE  TASK.RETURNSTATUS=1 ";
                if(StringUtils.isNotBlank(node)){
                	sql+="AND EMT.TYPEID="+node;
                }
                sql+= "AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid +" and TASK.CPUSERID="+userid+"  ORDER BY TASK.TESTTASKID ASC ";
      final String sqlNew=sql;
                PageInfo<Map<String, Object>> info=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->yhrPageMapper.getPage(sqlNew));
       return info;
    }
    
    @Override
    public PageInfo<Map<String, Object>> fingByTreeCSRWByUserReturngz(String node, String templId, String planid, BigDecimal userid, Integer pageNumber) {
        IPage<Map<String,Object>> pageBean=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE WHEN TASK.RETURNSTATUS =1 THEN  '退回' 	when task.TESTSTATUS='已完成' then '已完成' else '未完成'  END as sta,TASK.RETURNSTATUS,TASK.PROPOSAL,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE  TASK.RETURNSTATUS=1 ";
                if(StringUtils.isNotBlank(node)){
                	sql+="AND EMT.TYPEID="+node;
                }
                sql+= "AND EMT.TEMPLID="+templId +" and  TASK.PLANID="+planid +" ORDER BY TASK.TESTTASKID ASC ";
      final String sqlNew=sql;
                PageInfo<Map<String, Object>> info=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->yhrPageMapper.getPage(sqlNew));
       return info;
    }

    @Override
    public PageInfo<Map<String, Object>> fingByTreeCSRWByUser(String node, String templId, String planid, BigDecimal userid, Integer pageNumber) {
        IPage<Map<String,Object>> pageBean=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE WHEN TASK.RETURNSTATUS =1 THEN  '退回' 	when task.TESTSTATUS='已完成' then '已完成'  else '未完成'  END as sta,task.COMPLETESTAUS,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
                " WHERE  1=1 ";
     if(StringUtils.isNotBlank(node)){
    	sql+="and  EMT.TYPEID="+node;
     }
     sql+="and  TASK.PLANID="+planid +" and TASK.CPUSERID="+userid+" ORDER BY TASK.TESTTASKID ASC";
     final String sqlNew=sql;
        PageInfo<Map<String, Object>> info=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->yhrPageMapper.getPage(sqlNew));
        return info;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAll(BigDecimal planid, BigDecimal userid) throws Exception{

        //查询测试任务的测试有效性不能为null，
        String sql="SELECT * from TBL_TESTTASK where   TESTPOINTVALIDITY is not NULL and PLANID="+planid+" and CPUSERID="+userid;
        List<TblTesttask> listw =  this.testtaskMapper.getListBySql(sql);
        sql="SELECT * from TBL_TESTTASK where   PLANID="+planid+" and CPUSERID="+userid;
        List<TblTesttask> list =  this.testtaskMapper.getListBySql(sql);

        if (listw != null && list != null && list.size() == listw.size()) {
            //增加问题发现未审批完成的不能全部提交；20230817
            for (TblTesttask tblTesttasks :list){
                List<TblTesttaskProblemFind> profind = this.iTblTesttaskProblemFindService.getByTesttaskid(tblTesttasks.getTesttaskid());
                for (TblTesttaskProblemFind testtaskProblemFind:profind) {
                   if (testtaskProblemFind.getStatus() == null || testtaskProblemFind.getStatus().intValue() != 6){
                       return JsonBean.error("问题发现存在未审批完成！");
                   }
                }
            }
            for (TblTesttask tblTesttask : list) {
                tblTesttask.setCompletestaus(new BigDecimal(1));
                tblTesttask.setReturnstatus(new BigDecimal(0));
                //testtaskMapper.updateEntity(tblTesttask);
               testtaskMapper.updateById(tblTesttask);
            }
            sql="SELECT * from TBL_TESTTASK where   PLANID="+planid;
            List<TblTesttask> alls = this.testtaskMapper.getListBySql(sql);
            sql="SELECT * from TBL_TESTTASK where  COMPLETESTAUS=1 and  PLANID="+planid;
            List<TblTesttask> wnc = this.testtaskMapper.getListBySql(sql);
            String all = alls.size() + "";
            String wc = wnc.size() + "";
            if (alls != null && wnc != null && all.equals(wc)) {
                TblTestplan plan = (TblTestplan)testplanMapper.findById(planid);
                plan.setPlanstatus("已完成");
                plan.setReturnstatus(new BigDecimal(0));
                testplanMapper.updateById(plan); //updateEntity
            }
            return JsonBean.success();

        } else {
            return JsonBean.error("信息未填完整");
        }
    }

	@Override
	public PageInfo<TblTestplan> findAllRwToOrgNew(Integer pageNumber, String userid, String orgid,TblStaffUtil user) throws Exception{
		// TODO Auto-generated method stub
	        TblTestplan plan=new TblTestplan();
	        PageInfo<TblTestplan> info=null;
	        try {
	        if(StringUtils.isNotBlank(orgid)){
	           List<BigDecimal> orgList=tblOrganizaService.getTblOrganizationAll(new BigDecimal(orgid));
	           plan.setOrgids(orgList);
	           plan.setOrgid(new BigDecimal(orgid));
	        }
	        if(StringUtils.isNotBlank(userid)){
	        	 plan.setCreatid(new BigDecimal(userid));
	        }

	       // String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "pl.LINKORGID", "pl.LINKDEPTID", "pl.CREATID", "pl.SECRECTLEVELID", "pl.STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
			info = PageMethod.startPage(pageNumber, ConstClass.DEFAULT_SIZE)
					.doSelectPageInfo(() -> testplanMapper.getCsrwFindAllRwToOrgNew(plan, null));
			FiexibleNameAssignment ment = new FiexibleNameAssignment();
			if (CollectionUtil.isNotEmpty(info.getList())) {
				info.getList().forEach(entity -> {
					try {
						// 对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item = new fieldOrgStaffId();
						BeanUtils.copyProperties(entity, item);
						fieldOrgStaffName nameEntity = ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity, entity);
						item = null; // 处理并解除引用
						nameEntity = null; // 处理并解除引用
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				});
			}
	        } catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return info;
	}

	@Override
	public PageInfo<Map<String, Object>> fingByTreeCSRWByUsergz(String node, String templId, String planid,
			BigDecimal staffid, Integer pageNumber) {
		// TODO Auto-generated method stub
		 IPage<Map<String,Object>> pageBean=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
	        String sql="SELECT DISTINCT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,TASK.PROCEDURES,TASK.TESTRESULT,TASK.TESTPOINTVALIDITY,TASK.MEMO,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,EMT.BUSINESSDESC,EMT.CONTROLTARGET,EMT.CONTROLMEASURES,emt.ELEMENTCODE,CASE WHEN TASK.RETURNSTATUS =1 THEN  '退回' 	when task.TESTSTATUS='已完成' then '已完成'  else '未完成'  END as sta,task.COMPLETESTAUS,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2)  LONGSTRING2  from TBL_TESTELEMENT emt " +
	                " left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID  LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID " +
	                " WHERE  1=1 ";
	     if(StringUtils.isNotBlank(node)){
	    	sql+="and  EMT.TYPEID="+node;
	     }
	     sql+="and  TASK.PLANID="+planid +"  ORDER BY TASK.TESTTASKID ASC";
	     final String sqlNew=sql;
	        PageInfo<Map<String, Object>> info=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->yhrPageMapper.getPage(sqlNew));
	        return info;
	}

//	public List<BigDecimal> getTypeId(List<TblTesttemplType> list) throws Exception {
//		if (CollectionUtil.isEmpty(list)) {
//			return Collections.emptyList();
//		}
//		List<BigDecimal> addList = new ArrayList<>();  
//		List<BigDecimal> collect = list.stream().filter(item -> Objects.equals(item.getParentid(), item.getTypeid())).map(TblTesttemplType::getTypeid)
//				.collect(Collectors.toList());
//		if (CollectionUtil.isEmpty(collect)) {
//			return addList;
//		}
//		return collect;
//	}
}
