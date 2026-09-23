package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.service.CsfaService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTesttaskService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service

public class CsfaServiceImpl extends ServiceImpl<TblTestplanMapper, TblTestplan> implements CsfaService{
	
	
	@Resource
	TblGroupTestplanMapper tblGroupTestplanMapper;
    @Resource
    TblTestplanMapper  testplanMapper;
    @Resource
    private TblAssessMapper tblAssessMapper;
    
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
    
    @Resource
    TblTesttempleMapper tblTesttempleMapper;
    
    @Resource
    TblOrganizaService tblOrganizaService;

    @Resource
    ITblStaffService staffService;
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
                                      BigDecimal orgid,
                                      BigDecimal staffid,Integer authorityType) {

        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, ConstClass.DEFAULT_SIZE);

        String sql="select * from TBL_TESTPLAN where\n" +
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
		if (Objects.equals(authorityType,0)){
			sql+=" and CREATID = '"+staffid+"'";
		}
        sql+=" ORDER BY TESTPLANID desc";
        return  this.testplanMapper.getSqlPage(page,sql);

    }

    @Override
    public IPage<TblTestplan> findAllnoSj(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String userid,Integer authorityType) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, ConstClass.DEFAULT_SIZE);

        String sql=" SELECT * FROM TBL_TESTPLAN pl  WHERE PL.CREATID ="+userid+" or PL.STAFFID= "+userid;

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
		if (Objects.equals(authorityType,0)){
			sql+=" and CREATID = '"+userid+"' or staffid = '"+userid+"'";
		}
        sql+=" ORDER BY TESTPLANID desc";
        return   this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public TblTestplanVo queryOneTestPlanVo(BigDecimal selectProjectid) {
    	TblTestplanVo vo=this.testplanMapper.getOneTblTestplanVo(selectProjectid);
    	FiexibleNameAssignment ment=new FiexibleNameAssignment();
    	try {
    		if(vo!=null){
				//对灵活字段中的姓名名称及机构名称赋值
				fieldOrgStaffId item=new fieldOrgStaffId();
				BeanUtils.copyProperties(vo,item); 
				fieldOrgStaffName nameEntity=ment.setOpenName(item);
				BeanUtils.copyProperties(nameEntity,vo ); 
				item=null; // 处理并解除引用
				nameEntity=null; // 处理并解除引用
        	}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return vo;
        
        
    }

    @Override
    public PageInfo<TblTesttempleVo> getTestTemp(BigDecimal orgid, Integer pageNumber, String templeNumber, String templename, Integer pageSize,BigDecimal secrectLevelId,TblStaffUtil loginStaff) {
        String sql="select * from TBL_TESTTEMPLE t where 1=1  and t.tblcomany="+orgid;
        try {
        	String sqls = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "t.TBLCOMANY", "t.LINKDEPTID", "t.STAFFID", "t.SECRECTLEVELID", "t.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
        	 StringBuffer buf=new StringBuffer();
             buf.append(sqls);
              if(secrectLevelId!=null){
              	List<String> levels=tblAssessMapper.getSecrectLevel(secrectLevelId.toString());
              	final String levelsStr=sql+String.join(",", levels);
              	 buf.append(" and (t.SECRECTLEVELID IN (").append(String.join(",", levels)).append(") ").append(" or ").append("t.SECRECTLEVELID").append(" IS NULL OR ").append("t.SECRECTLEVELID").append(" = ''  )");;
              }
              if(StringUtils.isNotBlank(templename)){
                  sql += " and t.templename like '%"+templename+"%'";
              }
              if(StringUtils.isNotBlank(templeNumber)){
                  sql += " and t.templeNumber like '%"+templeNumber+"%'";
              }
              sql+= buf.toString();
              sql += " order by t.testtemid desc";

        
        } catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        final String sqlNew=sql;
        PageInfo<TblTesttempleVo> info=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->testplanMapper.getTesttempleVoSqlPage(sqlNew));
        return info;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delTesttasksByPanId(BigDecimal testplanid) {
        String sql="delete from TBL_TESTTASK where planid="+testplanid;
        this.yhrPageMapper.delete(sql);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteBytaskId(BigDecimal testtaskid) {

//        String sql=" delete from TBL_TESTTASK_ATT where testtaskid="+testtaskid;
//        this.yhrPageMapper.delete(sql);
//
//        sql="update TBL_TESTPLAN_MATRIX set testtaskid=null where testtaskid="+testtaskid;
//
//        this.yhrPageMapper.update(sql);

        this.testtaskMapper.deleteById(testtaskid);

    }

    @Override
    public List<Tree> getTreeC(BigDecimal testtemid) {

       // return  this.testplanMapper.getTreeListByTemid(testtemid);
        //新系统中已经去掉父级节点 精简之后sql
        return  this.testplanMapper.getTreeListByTemidNew(testtemid);

        
    }

    @Override
    public PageInfo<Map<String, Object>> fingByTree(BigDecimal node, BigDecimal templId, BigDecimal planid, Integer pageNumber, Integer pageSize) {

        String sql="\n" +
                "SELECT EMT.RISKTYPE,EMT.CHECKMETHOD,EMT.CONTROLMETHOD,EMT.CONTROLTYPE,EMT.CONTROLREQ,TASK.TESTTASKID,STA.REALNAME,EMT.ELEMENTID,emt.MATERIAL,TO_CHAR(emt.LONGSTRING1) LONGSTRING1,TO_CHAR(emt.LONGSTRING2) LONGSTRING2  " +
                "from TBL_TESTELEMENT emt\n" +
                "         left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID\n" +
                "         LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID\n" +
                "where  TASK.PLANID="+planid+" and EMT.TEMPLID="+templId;
                if(node!=null){
                	sql+= " and EMT.TYPEID="+node;
                }
                final String sqlNew=sql;
       		 PageInfo<Map<String, Object>> pageInfo=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->yhrPageMapper.getPage(sqlNew));

        return pageInfo;
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
                    newtask.setTesttaskid(RandomUtil.uuBigDecimalId());
                    testtaskMapper.insert(newtask);
                }
            }
            }
        }
    }

    @Override
    public Integer findByPlanJhn(BigDecimal planid) {

        String sql="SELECT count(DISTINCT ment.ELEMENTID) from TBL_TESTELEMENT ment right join TBL_GROUP_TEMPLATEDETAIL t on t.typeid=ment.typeid  left join TBL_TESTPLAN pa on pa.groupid=t.groupid  WHERE  pa.testplanid="+planid+" and t.staffid=pa.creatid ";
        Long count=this.yhrPageMapper.queryCount(sql);
        if(count>0){
           return count.intValue();
        }else{
           return null;
        }

    }

    @Override
    public Integer findByPlan(BigDecimal planid) {

        String sql="SELECT count(DISTINCT ment.ELEMENTID) from TBL_TESTELEMENT ment  left join TBL_TESTPLAN pa on ment.templId=pa.TESTTEMID WHERE pa.testplanid="+planid;
        Long count=this.yhrPageMapper.queryCount(sql);
        if(count>0){
           return count.intValue();
        }else{
           return null;
        }

    }
    
    @Override
    public Integer findByPlanwStra(BigDecimal planid) {
        String sql="SELECT count(DISTINCT ment.ELEMENTID) from TBL_TESTELEMENT ment inner join TBL_TESTTASK task on ment.elementId=task.elementId  left join TBL_TESTPLAN pa on ment.templId=pa.TESTTEMID WHERE TASK.CPUSERID is not NULL   AND task.PLANID="+planid;
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

	@Override  
	public PageInfo<TblTestplan> findAllNew(TblTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, BigDecimal orgid, BigDecimal staffid, Integer authorityType,TblStaffUtil user) throws Exception{
		// TODO Auto-generated method stub
		PageInfo<TblTestplan> pageInfo=null;
		try {  
		List<BigDecimal> orgList= tblOrganizaService.getTblOrganizationAll(orgid);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(starttime_min)){
				 plan.setStarttime(sdf.parse(starttime_min));
			}
			if(StringUtils.isNotBlank(starttime_max)){
				 plan.setEndtime(sdf.parse(starttime_max));
			}
		  plan.setCreatid(staffid);
		 plan.setOrgids(orgList);
		 //String t,String staff,String org,String dept,
		 String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "LINKORGID", "LINKDEPTID", "CREATID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
		 pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllNew(plan,sql));
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
						StringBuffer buffer=new StringBuffer();
						buffer.append("[");
						List<String> list=testplanMapper.findList(entity.getTestplanid(),entity.getTesttemid());
                        for(String str:list){
                        	buffer.append("{\"formId\":"+entity.getTestplanid()+",\"distributionTitle\":\""+entity.getPlanname()+"\",\"reciver\":"+str+",\"isread\":0,\"moduleType\":\"nkhg\"}");
                        }
                        entity.setJsonString(buffer.append("]").toString());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}

	@Override
	public PageInfo<TblTestplan> findAllnoSjNew(TblTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, String creatid, Integer authorityType,TblStaffUtil user) throws Exception{
		// TODO Auto-generated method stub
		 PageInfo<TblTestplan> pageInfo=null;
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(starttime_min)){
				 plan.setStarttime(sdf.parse(starttime_min));
			}
			if(StringUtils.isNotBlank(starttime_max)){
				 plan.setEndtime(sdf.parse(starttime_max));
			}
		if(StringUtils.isNotBlank(creatid)){
		plan.setCreatid(new BigDecimal(creatid));
		}
		//创建人和科室负责人都可以看到列表，但是科室负责人得在issstatus=1的时候才能看到
		  StringBuffer querySql=new StringBuffer();
		//String sql = GeneralSQLConcatConfig.concatSecrectSqlCase2(user.getCurrentOrg().getUseSecrect(), true, "LINKORGID", "LINKDEPTID", "CREATID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds(),querySql.toString());
		//querySql.append(sql);
		if(authorityType!=null && authorityType==0){
		    	querySql.append(" and (creatid=").append(plan.getCreatid()).append(" or (staffid =").append(plan.getCreatid()).append(" and ISSSTATUS=1 ))");
		    }
		pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllnoSjNew(plan,querySql.toString()));
		FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
						StringBuffer buffer=new StringBuffer();
						buffer.append("[");
						List<String> list=testplanMapper.findList(entity.getTestplanid(),entity.getTesttemid());
                        for(String str:list){
                        	buffer.append("{\"formId\":"+entity.getTestplanid()+",\"distributionTitle\":\""+entity.getPlanname()+"\",\"reciver\":"+str+",\"isread\":0,\"moduleType\":\"nkhg\"}");
                        }
                        entity.setJsonString(buffer.append("]").toString());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}

	@Override
	public void confirmIssuance(BigDecimal id) {
		// TODO Auto-generated method stub
		try {
			TblTestplan t=testplanMapper.selectById(id);
			t.setIssStatus(1);
			testplanMapper.updateById(t);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@Override
	public PageInfo<TblGroupTestplan> getGroupPlanList(Integer pageNumber, BigDecimal orgid, BigDecimal staffid) {
		// TODO Auto-generated method stub
		PageInfo<TblGroupTestplan> pageInfo=null;
		try {  
			TblGroupTestplan plan=new TblGroupTestplan();
			plan.setToIssued(new BigDecimal("1"));
			plan.setIssuedStaffid(staffid.toString());
		 pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblGroupTestplanMapper.findAllNew(plan,null));
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
						if(entity.getTesttemid()!=null){
						TblTesttemple tem=tblTesttempleMapper.selectById(entity.getTesttemid());
						entity.setTesttemple(tem);
						}
						
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return pageInfo;
	}

 
	 

}