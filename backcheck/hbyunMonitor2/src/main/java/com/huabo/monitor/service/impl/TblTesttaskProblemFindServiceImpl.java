package com.huabo.monitor.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.CatVo;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblProblemTransfer;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.entity.TblTaskFindAtt;
import com.huabo.monitor.entity.TblTesttask;
import com.huabo.monitor.entity.TblTesttaskProblemFind;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblAttachmentMapper;
import com.huabo.monitor.mapper.TblProblemTransferMapper;
import com.huabo.monitor.mapper.TblStaffMapper;
import com.huabo.monitor.mapper.TblTesttaskProblemFindMapper;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTesttaskProblemFindService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import cn.hutool.core.collection.CollectionUtil;


@Service
public class TblTesttaskProblemFindServiceImpl extends ServiceImpl<TblTesttaskProblemFindMapper, TblTesttaskProblemFind> implements ITblTesttaskProblemFindService {
   
	
	 
	@Value("${role1}")
	private String role1;

	@Resource
	private TblTesttaskProblemFindMapper tblTesttaskProblemFindMapper;

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;

	@Resource
	TblOrganizaService tblOrganizaService;

	@Resource
	ITblStaffService staffService;

	@Resource
	TblStaffMapper tblStaffMapper;
	@Resource
	TblProblemTransferMapper tblProblemTransferMapper;

	@Resource
	private UserProvider userProvider;

	@Override
	public void saveTesttsak(TblTesttaskProblemFind task,String attids) throws Exception{
		tblTesttaskProblemFindMapper.insert(task);
		 if (StringUtils.isNotBlank(attids)) {
             String[] ids = attids.split(",");
             TblTaskFindAtt  att=new TblTaskFindAtt();
             for (int i = 0; i < ids.length; i++) {
            	 if(StringUtils.isNotBlank(ids[i])){
                 this.tblAttachmentMapper.insertTaskFindAtt(new BigDecimal(ids[i].toString()),task.getFindid());
             }
             }
          }
	}

	@Override
	public void updateTesttsak(TblTesttaskProblemFind task,String attids) throws Exception{
		//tblTesttaskProblemFindMapper.updateEntity(task);
		tblTesttaskProblemFindMapper.updateById(task);
		 if (StringUtils.isNotBlank(attids)) {
             String[] ids = attids.split(",");
             TblTaskFindAtt  att=new TblTaskFindAtt();
             for (int i = 0; i < ids.length; i++) {
            	 if(StringUtils.isNotBlank(ids[i])){
                 this.tblAttachmentMapper.insertTaskFindAtt(new BigDecimal(ids[i].toString()),task.getFindid());
             }
             }
          }
	}

	@Override
	public void delTesttsak(BigDecimal testtaskid) throws Exception{
		tblTesttaskProblemFindMapper.delTesttaskProblemFind(testtaskid);
	}
	
	@Override
	public List<TblTesttaskProblemFind> getByTesttaskid(BigDecimal testtaskid) throws Exception{
		List<TblTesttaskProblemFind> list=null;
		try {
			list=tblTesttaskProblemFindMapper.getTesttaskProblemFindByTesttaskid(testtaskid);
			FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(list)){
				list.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
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
		return list;
	}
	
	@Override
	public TblTesttaskProblemFind getById(BigDecimal findid) throws Exception{
		TblTesttaskProblemFind find=tblTesttaskProblemFindMapper.getById(findid);
		FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(find!=null){
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(find,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,find ); 
					if(find.getIssuedStaffid()!=null&&find.getIssuedStaffid().compareTo(new BigDecimal("0"))!=0){
						find.setIssuedStaffidName(tblStaffMapper.getOrgNameByScopeids(find.getIssuedStaffid().toString()).get(0).getRealname());

					}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return find;
		
	}
	
	@Override
    public IPage<TblTesttaskProblemFind> findAllRwToOrg(Integer pageNumber, String userid, String orgid) {
        IPage<TblTesttaskProblemFind> page=new Page<TblTesttaskProblemFind>(pageNumber, ConstClass.DEFAULT_SIZE);
        String sql=" SELECT TTP.*,TS.REALNAME,TORG.ORGNAME"
        		+ " from TBL_TESTTASK_PROBLEMFIND TTP"
        		+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID "
        		+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG "
                + " where 1=1 ";

        sql+=" ORDER BY TTP.FINDID desc ";

        return  this.tblTesttaskProblemFindMapper.getSqlPage(page,sql);
    }

	
	@Override
    public IPage<TblTesttaskProblemFind> findALLProblemLedgerList(Integer pageNumber,Integer pageSize, String userid, String orgid,TblTesttaskProblemFind tblTesttaskProblemFind,Integer authorityType) {
        IPage<TblTesttaskProblemFind> page=new Page<TblTesttaskProblemFind>(pageNumber, ConstClass.DEFAULT_SIZE);
		StringBuffer sbSql = new StringBuffer("SELECT TTP.*,TS.REALNAME,TORG.ORGNAME,TASK.PLANID,PL.plannumber,PL.planname,RS.RISKNUMBER from TBL_TESTTASK_PROBLEMFIND TTP "
				+ " LEFT JOIN TBL_TESTTASK task on TTP.TESTTASKID=TASK.TESTTASKID "
				+ " LEFT JOIN TBL_RISK rs on TTP.RISKNUMBERID=RS.RISKID "
				+ " LEFT JOIN TBL_TESTPLAN pl on task.planid=pl.testplanid "
				+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID"
				+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG"
				+ " where 1=1 and TTP.status in (6)");
		if (StringUtils.isNotBlank(tblTesttaskProblemFind.getDefectlevel())) {
			sbSql.append(" and TTP.DEFECTLEVEL LIKE '%" + tblTesttaskProblemFind.getDefectlevel() +"%' ");
		}
		if (StringUtils.isNotBlank(tblTesttaskProblemFind.getProblemtype())) {
			sbSql.append(" and TTP.PROBLEMTYPE LIKE '%" + tblTesttaskProblemFind.getProblemtype() +"%' ");
		}
		if (StringUtils.isNotBlank(tblTesttaskProblemFind.getOneprocess())) {
			sbSql.append(" and TTP.ONEPROCESS LIKE '%" + tblTesttaskProblemFind.getOneprocess() +"%' ");
		}
		if (tblTesttaskProblemFind.getTestYear()>0) {
			sbSql.append(" and TTP.TESTYEAR = " + tblTesttaskProblemFind.getTestYear() +" ");
		}
		if (Objects.equals(authorityType, 0)) {
			sbSql.append(" and TTP.CREATESTAFFID = '" + userid +"' ");
		}
		if (tblTesttaskProblemFind.getZgstatus()!=null) {
			sbSql.append(" and TTP.ZGSTATUS = " + tblTesttaskProblemFind.getZgstatus() +" ");
		}
		
		sbSql.append(" ORDER BY TTP.FINDID desc ");
        return  this.tblTesttaskProblemFindMapper.getSqlPage(page,sbSql.toString());
    }

	@Override
	public String saveProblem(String token,TblTesttask task, String findid) throws Exception {
        TblStaffUtil user = userProvider.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TblTesttaskProblemFind profind = null;
        if (null != findid&&StringUtils.isNotBlank(findid)) {
        	profind=tblTesttaskProblemFindMapper.findById(new BigDecimal(findid));
        	profind.setTesttaskid(task.getTesttaskid());
        	profind.setOneprocess(task.getOneprocess());
        	profind.setQuabasis(task.getQuabasis());
        	//tblTesttaskProblemFindMapper.updateEntity(profind);
        	tblTesttaskProblemFindMapper.updateById(profind);
        } else {
        	profind=new TblTesttaskProblemFind();
        	profind.setCreatestaffid(user.getStaffid());
        	profind.setCreatetime(new Date());
        	profind.setLinkOrg(user.getCurrentOrg().getOrgid());
        	profind.setTesttaskid(task.getTesttaskid());
        	profind.setOneprocess(task.getOneprocess());
        	profind.setQuabasis(task.getQuabasis());
        	//tblTesttaskProblemFindMapper.insertEntity(profind);
        	tblTesttaskProblemFindMapper.insert(profind);

        }
        return profind.getFindid().toString();
	}

	@Override
	public void delFjByTypeAndId(String type, String attid) throws Exception {
		// TODO Auto-generated method stub
		if(type.equals("wtfx")){
			tblAttachmentMapper.deleteTaskFindAtt(new BigDecimal(attid));
			tblAttachmentMapper.deleteEntity(new BigDecimal(attid));
		}
	}
	
	  @Override
	    public List<TblAttachment> getRepAttById(String id) {
	        String sql="select * from TBL_ATTACHMENT where attid in (select attid from Tbl_TASKFIND_ATT where findid="+id+"  )";
	        return this.tblAttachmentMapper.getListBySql(sql);
	    }
	  
	  
	  @Override
	  public	List<TblTesttaskProblemFind> getExportProblemLedgerList(String staffid, String orgid,TblTesttaskProblemFind tblTesttaskProblemFind,String ids) throws Exception{
	        String sql=" SELECT TTP.*,TS.REALNAME,TORG.ORGNAME"
	        		+ " from TBL_TESTTASK_PROBLEMFIND TTP"
	        		+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID "
	        		+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG "
	                + " where 1=1 and TTP.status in (6)";
			if (staffid != null){
				sql+=" AND TTP.CREATESTAFFID = '" + staffid +"' ";
			}
			if (StringUtils.isNotBlank(tblTesttaskProblemFind.getOneprocess())) {
				sql+=" AND TTP.ONEPROCESS like '%" + tblTesttaskProblemFind.getOneprocess() +"%' ";
			}
		  if (StringUtils.isNotBlank(tblTesttaskProblemFind.getDefectlevel())) {
			  sql+=" AND TTP.DEFECTLEVEL like '%" + tblTesttaskProblemFind.getDefectlevel() +"%' ";
		  }
		  if (StringUtils.isNotBlank(tblTesttaskProblemFind.getProblemtype())) {
			  sql+=" AND TTP.PROBLEMTYPE like '%" + tblTesttaskProblemFind.getProblemtype() +"%' ";
		  }

		  if (tblTesttaskProblemFind.getTestYear() != 0) {
			  sql+=" AND TTP.TESTYEAR= " + tblTesttaskProblemFind.getTestYear();
		  }
		  
		  if (StringUtils.isNotBlank(ids)) {
			  sql+=" AND TTP.findid in ("+ids+") ";
		  }
	        sql+=" ORDER BY TTP.FINDID desc";
	        return this.tblTesttaskProblemFindMapper.getListBySql(sql);
	    }

	@Override
	public void updateProfind(TblTesttaskProblemFind profind) throws Exception{
		// this.tblTesttaskProblemFindMapper.updateEntity(profind);
	  this.tblTesttaskProblemFindMapper.updateById(profind);
	}
	
	@Override
	public void sendreform(BigDecimal findid) throws Exception{
		 this.tblTesttaskProblemFindMapper.sendreform(findid);
	}

	@Override
	public PageInfo<TblTesttaskProblemFind> findALLProblemLedgerListNew(Integer pageNumber, Integer pageSize,
			String userid, String orgid, TblTesttaskProblemFind tblTesttaskProblemFind, Integer authorityType,TblStaffUtil user) throws Exception{
	
		
		//列表权限:内控管理人员查询全部，下发人员自己的、创建人自己的
		  if(StringUtils.isNotBlank(userid)){
			  tblTesttaskProblemFind.setCreatestaffid(new BigDecimal(userid));
		  }
          if(StringUtils.isNotBlank(orgid)){
        	  tblTesttaskProblemFind.setLinkOrg(new BigDecimal(orgid));
		  }
          if(Objects.nonNull(authorityType)){
        	  tblTesttaskProblemFind.setAuthorityType(authorityType);
          }
//          tblTesttaskProblemFind.setUseSecrect(user.getCurrentOrg().getUseSecrect());
//          tblTesttaskProblemFind.setSecrectStaff(user.getStaffid());
//  		  tblTesttaskProblemFind.setSecrectScopeIds(user.getSecrectScopeIds());
          String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), authorityType == 0, "TTP.LINKORG", "TTP.LINKDEPTID", "TTP.CREATESTAFFID", "TTP.SECRECTLEVELID", "TTP.STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
          StringBuffer  buffer=new StringBuffer();
          buffer.append(sql);
       	if (!JudgeRoleRight.judgeRoleRight(role1, user.getRoleNames())) {
            buffer.append(" and TTP.linkorg=").append(user.getCurrentOrg().getOrgid());
            buffer.append(" or TTP.ISSUEDSTAFFID=").append(user.getStaffid());
       	}
          PageInfo<TblTesttaskProblemFind> info=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblTesttaskProblemFindMapper.findALLProblemLedgerListNew(tblTesttaskProblemFind,buffer.toString()));
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(info.getList())){
				info.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
						if(entity.getIssuedStaffid()!=null&&entity.getIssuedStaffid().compareTo(new BigDecimal("0"))!=0){
							entity.setIssuedStaffidName(tblStaffMapper.getOrgNameByScopeids(entity.getIssuedStaffid().toString()).get(0).getRealname());

						}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
		  
		  return  info;
	}

	@Override
	public JsonBean forwardPersonnel(String token, String ids, BigDecimal staffId) {
		// TODO Auto-generated method stub
		String[] idList=ids.split(",");
		for(String s:idList){
			TblTesttaskProblemFind find=tblTesttaskProblemFindMapper.selectById(s);
			System.out.println("");
		try {
			//新建转派记录
			//转派人员一开始是问题台账创建人转发，之后应该是被转发人转发
			TblStaff sta=null;
			if(find.getIssuedStaffid()==null||find.getIssuedStaffid().compareTo(new BigDecimal("0"))==0){
				sta=tblStaffMapper.selectById(find.getCreatestaffid());
			}else{
				sta=tblStaffMapper.selectById(find.getIssuedStaffid());
			}

			tblStaffMapper.selectById(find.getCreatestaffid());
			 QueryWrapper queryWrapper = new QueryWrapper();
             queryWrapper.eq("PROBLEMID", ids);
             queryWrapper.eq("TOSTAFFID", staffId);
             System.out.println(tblProblemTransferMapper.selectCount(queryWrapper));
             if (tblProblemTransferMapper.selectCount(queryWrapper).equals(0L)) {
			TblProblemTransfer tran=new TblProblemTransfer();
			tran.setOneprocess(find.getOneprocess());
			tran.setCreatetime(new Date());
			tran.setInitialName(sta.getRealname());
			if(find.getIssuedStaffid()==null||find.getIssuedStaffid().compareTo(new BigDecimal("0"))==0){
				tran.setInitialStaffid(find.getCreatestaffid());
			}else{
				tran.setInitialStaffid(find.getIssuedStaffid());
			}
			tran.setToStaffid(staffId);
			TblStaff staff=tblStaffMapper.selectById(staffId);
			tran.setToName(staff.getRealname());
			tran.setProblemId(find.getFindid());
			tran.setId(RandomUtil.uuBigDecimalId());
			tblProblemTransferMapper.insert(tran);
             }
            find.setIssuedDate(new Date());
  		   find.setIssuedStaffid(staffId);
  		   tblTesttaskProblemFindMapper.updateById(find);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		}
	    JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
	    return jsonBean;
	}

	@Override
	public JsonBean getProblemTransferList(String token, BigDecimal id) {
		 Map<String, Object> hashMap = new HashMap<String, Object>();
		  JsonBean jsonBean =null;
		try {
			  List<TblProblemTransfer> list= tblProblemTransferMapper.getProblemTransferList(id);
			  hashMap.put("data", list);
			  jsonBean = new JsonBean(1, "success", hashMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return jsonBean;
	}
	 
}
