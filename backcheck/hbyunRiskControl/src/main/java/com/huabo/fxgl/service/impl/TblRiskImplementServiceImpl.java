package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.TblMajorRiskbranchCreateDto;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblMajorRiskbranchCreate;
import com.huabo.fxgl.entity.TblMajorTransfer;
import com.huabo.fxgl.entity.TblOrganization;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskImprovementEntiry;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblMajorRiskCreationMapper;
import com.huabo.fxgl.mapper.TblMajorRiskbranchCreateMapper;
import com.huabo.fxgl.mapper.TblMajorTransferMapper;
import com.huabo.fxgl.mapper.TblRiskImplementMapper;
import com.huabo.fxgl.mapper.TblRiskReportingMapper;
import com.huabo.fxgl.service.TblRiskImplementService;
import com.huabo.fxgl.service.TblRiskImprovementService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TblRiskImplementServiceImpl extends ServiceImpl<TblRiskImplementMapper, TblRiskImplementEntity> implements TblRiskImplementService {

    @Resource
    private TblRiskImplementMapper tblRiskImplementMapper;
    
    @Resource
    private TblRiskReportingMapper tblRiskReportingMapper;
    
    @Resource
    private TblMajorRiskbranchCreateMapper tblMajorRiskbranchCreateMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private OrganizationMapper organizationMapper;
    

    @Resource
    private TblMajorTransferMapper tblMajorTransferMapper;

    @Resource
    private TblRiskImprovementService tblRiskImprovementService;
    
    @Resource
    private UserProvider userProvider;
    
    //审计法务部人员
    @Value("${application.auditlegaldepartment:}")
	private String auditlegaldepartment;


    /**
     * 下发表单-下发
     * @param token
     * @param tblRiskImplementEntity
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public JsonBean issuedAndSubmit(TblRiskImplementEntity tblRiskImplementEntity, String token) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskImplementEntity.setImpLssuedStaffid(staffUtil.getStaffid());//下发人名称
            tblRiskImplementEntity.setImpLssuedDate(new Date());//下发时间
            tblRiskImplementEntity.setId(RandomUtil.uuBigDecimalId());
            tblRiskImplementMapper.insert(tblRiskImplementEntity);
            TblRiskImplementEntity entity = tblRiskImplementMapper.selectById(tblRiskImplementEntity.getId());
            final TblRiskImprovementEntiry improvementEntiry = new TblRiskImprovementEntiry();
            improvementEntiry.setId(RandomUtil.uuBigDecimalId());
            Organization org=organizationMapper.selectById(entity.getImpLssuedUnit());
            improvementEntiry.setBranchName(org.getOrgname());
            improvementEntiry.setScore(new BigDecimal(100));
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();
            Month month = currentDate.getMonth();
            improvementEntiry.setYear(new BigDecimal(year));
            improvementEntiry.setMonth(new BigDecimal(month.getValue()));
            improvementEntiry.setStatus("未上报");
            improvementEntiry.setRiskImplementID(entity.getId());
            improvementEntiry.setBranchId(entity.getImpLssuedUnit());
            tblRiskImprovementService.insertImprovement(improvementEntiry);
            //修改风险报送-相关风险事件填报 下发状态
            TblRiskReportingEntity reportEntity=tblRiskReportingMapper.selectById(entity.getReportingId());
            reportEntity.setState(new BigDecimal("1"));
            tblRiskReportingMapper.updateById(reportEntity);
            hashMap.put("pageInfo",entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 下发表单列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @Override
    public JsonBean issuedImplementList(String token, Integer pageNumber, Integer pageSize, String impRiskName,String majorid,String id) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
          final  TblRiskImplementEntity eEntity=new TblRiskImplementEntity();
          eEntity.setImpRiskName(impRiskName);
          if(StringUtils.isNotBlank(majorid)){
          eEntity.setMajorId(new BigDecimal(majorid));
          }
          if (!JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
        	  eEntity.setImpLssuedStaffid(staffUtil.getStaffid());
       	}  
          String[] ids=null;
		  if(StringUtils.isNotBlank(id)){
			  if(id.endsWith(","))
			  id=id.substring(0,id.length()-1);
			   ids=id.split(",");
		  }
		  String[] idNew=ids;
            com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() -> tblRiskImplementMapper.selectAllList(eEntity,idNew));
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
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}

            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
            PageResult<TblRiskImplementEntity> build = new PageResult<TblRiskImplementEntity>().build(pageInfo);

            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 下发表单详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean issuedImplementDetails(String id, String token) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            TblRiskImplementEntity implementEntity = tblRiskImplementMapper.selectById(id);
            if (!Objects.isNull(implementEntity)){
                //implementEntity.setImpWayStaffName(this.queryTableFieldFalseName(implementEntity).getImpWayStaffName()); //牵头领导
               // implementEntity.setImpWayDeptName(this.queryTableFieldFalseName(implementEntity).getImpWayDeptName()); //牵头部门
               // implementEntity.setImpDutyUnitName(this.queryTableFieldFalseName(implementEntity).getImpDutyUnitName()); //公司相关责任单位名称
                implementEntity.setImpLssuedStaffidName(this.queryTableFieldFalseName(implementEntity).getImpLssuedStaffidName()); //下发人名称
                implementEntity.setImpLssuedUnitName(this.queryTableFieldFalseName(implementEntity).getImpLssuedUnitName()); //下发单位（分公司）
                FiexibleNameAssignment ment=new FiexibleNameAssignment();
            	//对灵活字段中的姓名名称及机构名称赋值
    			fieldOrgStaffId item=new fieldOrgStaffId();
    			BeanUtils.copyProperties(implementEntity,item); 
    			fieldOrgStaffName nameEntity=ment.setOpenName(item);
    			BeanUtils.copyProperties(nameEntity,implementEntity ); 
            }
            hashMap.put("pageInfo",implementEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 分公司判断是否可新建
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean isCreateSubmit(String token) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            QueryWrapper<TblRiskImplementEntity> queryWrapper = new QueryWrapper<TblRiskImplementEntity>();
            queryWrapper.eq("IMP_LSSUED_UNIT",staffUtil.getLinkOrg().getOrgid());
            List<TblRiskImplementEntity> entityList = tblRiskImplementMapper.selectList(queryWrapper);
            if (entityList.size() > 0){
                hashMap.put("pageInfo",entityList);
                hashMap.put("is",1); //1 可新建
            }else {
                hashMap.put("pageInfo",null);
                hashMap.put("is",0); //1 不可新建
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    public TblRiskImplementEntity  queryTableFieldFalseName(TblRiskImplementEntity entity){
        final TblRiskImplementEntity implementEntity;
        try {
            implementEntity = new TblRiskImplementEntity();
//            Staff ImpWayStaffName = staffMapper.selectById(entity.getImpWayStaff()); //牵头领导
//            Organization ImpWayDeptName = organizationMapper.selectById(entity.getImpWayDept()); //牵头部门
//            Organization ImpDutyUnitName = organizationMapper.selectById(entity.getImpDutyUnit()); //公司相关责任单位名称
            Staff ImpLssuedStaffidName = staffMapper.selectById(entity.getImpLssuedStaffid());//下发人名称
            Organization ImpLssuedUnitName = organizationMapper.selectById(entity.getImpLssuedUnit()); //下发单位（分公司）
//            implementEntity.setImpWayStaffName(ImpWayStaffName.getRealname());
//            implementEntity.setImpWayDeptName(ImpWayDeptName.getOrgname());
//            implementEntity.setImpDutyUnitName(ImpDutyUnitName.getOrgname());
            implementEntity.setImpLssuedStaffidName(ImpLssuedStaffidName.getRealname());
            implementEntity.setImpLssuedUnitName(ImpLssuedUnitName.getOrgname());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return implementEntity;
    }

	@Override
	public List<TblRiskImplementEntity> exportIssuedImplement(String token,String id,String impRiskName) {
		List<TblRiskImplementEntity> entityList;
		  try {
			  String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				   id=id.substring(0,id.length()-1);
				   ids=id.split(",");
			  }
			  TblStaffUtil staffUtil = userProvider.get();
			  final  TblRiskImplementEntity entity=new TblRiskImplementEntity();
	          entity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
	          entity.setImpRiskName(impRiskName);
			  entityList=tblRiskImplementMapper.selectAllList(entity,ids);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		  return entityList;
	}

	@Override
	public JsonBean issuedImplementSummary(String token, Integer pageNumber, Integer pageSize,
			String impRiskName, String id,String orgname,String jd) throws Exception {
		  Map<String, Object> hashMap = null;
	        try {
	            hashMap = new HashMap<>();
	            //得到了当前登录的用户信息
	            TblStaffUtil staffUtil = userProvider.get();
	            if (staffUtil == null){
	                return  ResponseFormat.retParam(0, 20006, hashMap);
	            }

	          final  TblRiskImplementEntity eEntity=new TblRiskImplementEntity();
	          eEntity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
	          eEntity.setImpRiskName(impRiskName);
	          eEntity.setImpLssuedUnitName(orgname);
	          eEntity.setJd(jd);
	          String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				  id=id.substring(0,id.length()-1);
				   ids=id.split(",");
			  }
			  
			  String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "cr.LINKORGID", "cr.LINKDEPTID", "cr.createstaffid", "cr.SECRECTLEVELID", "cr.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
			  final String[] idString=ids;
				//List<TblRiskImplementEntity> list= tblRiskImplementMapper.selectAllSummaryList(eEntity,ids,sql);
 	            com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
 	                    .doSelectPageInfo(() ->tblRiskImplementMapper.selectAllSummaryList(eEntity,idString,sql));
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
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					} );
					
				}
	          //  PageMethod.startPage(pageNumber, pageSize);
	          //  com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo=new PageInfo<>(list) ;

//	            for (TblRiskImplementEntity implementEntity : pageInfo.getList()) {
//	                implementEntity.setImpWayStaffName(this.queryTableFieldFalseName(implementEntity).getImpWayStaffName()); //牵头领导
//	                implementEntity.setImpWayDeptName(this.queryTableFieldFalseName(implementEntity).getImpWayDeptName()); //牵头部门
//	                implementEntity.setImpDutyUnitName(this.queryTableFieldFalseName(implementEntity).getImpDutyUnitName()); //公司相关责任单位名称
//	                implementEntity.setImpLssuedStaffidName(this.queryTableFieldFalseName(implementEntity).getImpLssuedStaffidName()); //下发人名称
//	                implementEntity.setImpLssuedUnitName(this.queryTableFieldFalseName(implementEntity).getImpLssuedUnitName()); //下发单位（分公司）
//	            }

	            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
	            PageResult<TblRiskImplementEntity> build = new PageResult<TblRiskImplementEntity>().build(pageInfo);

	            hashMap.put("pageInfo",build);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ResponseFormat.retParam(1, 200, hashMap);
	}
	
	@Override
	public List<TblRiskImplementEntity> exportIssuedImplementSummary(String token,String id,String impRiskName,String orgname,String jd) {
		List<TblRiskImplementEntity> entityList;
		  try {
			  String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				   id=id.substring(0,id.length()-1);
				   ids=id.split(",");
			  }
			  TblStaffUtil staffUtil = userProvider.get();
			  final  TblRiskImplementEntity eEntity=new TblRiskImplementEntity();
			  eEntity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
			  eEntity.setImpRiskName(impRiskName);
			  eEntity.setImpLssuedUnitName(orgname);
			  eEntity.setJd(jd);
			  String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "cr.LINKORGID", "cr.LINKDEPTID", "cr.createstaffid", "cr.SECRECTLEVELID", "cr.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
			  entityList=tblRiskImplementMapper.selectAllSummaryList(eEntity,ids,sql);
			  FiexibleNameAssignment ment=new FiexibleNameAssignment();
				if(CollectionUtils.isNotEmpty(entityList)){
					entityList.forEach(entity->{
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
	            throw new RuntimeException(e);
	        }
		  return entityList;
	}

	
	@Override
	public List<TblRiskImplementEntity> exportIssuedImplementSummaryOrder(String token,String id,TblRiskImplementEntity ementEntity) {
		List<TblRiskImplementEntity> entityList;
		  try {
			  String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				   id=id.substring(0,id.length()-1);
				   ids=id.split(",");
			  }
			  TblStaffUtil staffUtil = userProvider.get();
			  ementEntity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
			  String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "cr.LINKORGID", "cr.LINKDEPTID", "cr.createstaffid", "cr.SECRECTLEVELID", "cr.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
			  entityList=tblRiskImplementMapper.selectAllSummaryListOrder(ementEntity,ids,sql);
			  FiexibleNameAssignment ment=new FiexibleNameAssignment();
				if(CollectionUtils.isNotEmpty(entityList)){
					entityList.forEach(entity->{
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
	            throw new RuntimeException(e);
	        }
		  return entityList;
	}

	
	@Override
	public JsonBean reportToLeader(String token, BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		TblMajorRiskbranchCreate create=tblMajorRiskbranchCreateMapper.selectById(id);
		create.setToreport(new BigDecimal(1));//修改上报状态为已经上报
		create.setToreportdate(new Date());
		tblMajorRiskbranchCreateMapper.updateById(create);
		List<TblRiskImplementEntity> list=tblRiskImplementMapper.getMajorIssList(id);
		for(TblRiskImplementEntity t:list){
		t.setToreport(new BigDecimal(1));//修改上报状态为已经上报
		t.setToreportdate(new Date());
		tblRiskImplementMapper.updateById(t);
		}
	    JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
	    return jsonBean;
	}

	@Override
	public JsonBean issuedSaveOrUpdate(TblRiskImplementEntity tblRiskImplementEntity, String token) throws Exception {
		  Map<String, Object> hashMap = null;
	        try {
	            hashMap = new HashMap<>();
	            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
	            if (staffUtil == null){
	                return  ResponseFormat.retParam(0, 20006, hashMap);
	            }
	            if(tblRiskImplementEntity.getId()!=null&&tblRiskImplementEntity.getId().compareTo(new BigDecimal(0))==1){
	            	tblRiskImplementMapper.updateById(tblRiskImplementEntity);
	            }else{
	            	tblRiskImplementEntity.setImpLssuedStaffid(staffUtil.getStaffid());//创建人名称
	 	            tblRiskImplementEntity.setImpLssuedDate(new Date());//创建时间
	 	           tblRiskImplementEntity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());//创建单位
	 	            tblRiskImplementEntity.setId(RandomUtil.uuBigDecimalId());
	 	            tblRiskImplementMapper.insert(tblRiskImplementEntity);
	            }
	            hashMap.put("pageInfo",tblRiskImplementEntity);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ResponseFormat.retParam(1, 200, hashMap);
	}

	@Override
	public JsonBean getMajorRiskCreateList(String token, Integer pageNumber, Integer pageSize, TblMajorRiskbranchCreateDto dto,Integer authorityType)
			throws Exception {
		 Map<String, Object> hashMap = new HashMap<>();
		 BigDecimal staffid=null;
       try {
           //得到了当前登录的用户信息
           TblStaffUtil staffUtil = userProvider.get();
           if (staffUtil == null){
               return  ResponseFormat.retParam(0, 20006, hashMap);
           }
         //填报是集团下发数据  属于做任务，自己看自己数据--如果是风险管理员就可以看到本公司下的所有数据
         //有转派的功能，所以转派的人员也要能看到--如果是风险管理员就可以看到本公司下的所有数据
           //如果是审计法务部人员就可以看到所有数据
      
          // String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), authorityType==0, "LINKORGID", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
           String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(), authorityType==0, "LINKORGID", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType));
           StringBuffer  buffer=new StringBuffer();
            buffer.append(sql);
         	if (!JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
                buffer.append(" and b.linkorgid=").append(staffUtil.getCurrentOrg().getOrgid());
         	}  
           PageInfo<TblMajorRiskbranchCreate> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                   .doSelectPageInfo(() -> tblMajorRiskbranchCreateMapper.getMajorRiskCreateList(dto,buffer.toString(),  authorityType==0?staffUtil.getStaffid():null));
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
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
           hashMap.put("pageInfo",pageInfo);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
       return ResponseFormat.retParam(1, 200, hashMap);
	}
	
	@Override
	public void issuedImplementDelete(String id)
			throws Exception {
		// TODO Auto-generated method stub
	      try {
	    	  tblRiskImplementMapper.deleteById(id);
//	            //删除所有附件
//	            List<BigDecimal> attIdList = this.tblRiskReportingMapper.findAttIdListByReporting(id);
//	            for (BigDecimal attId : attIdList) {
//	                this.deleteRealtionAttInfo(attId);
//	            }
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	}
	
	
	@Override
	public JsonBean mjorDetail(String id) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> hashMap = new HashMap<>();
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
		try {
			  TblMajorRiskbranchCreate tbl=tblMajorRiskbranchCreateMapper.selectById(id);
			  if(tbl!=null){
		        	//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(tbl,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,tbl ); 
			  }
              //风险内层List
			  List<TblRiskImplementEntity> tList=tblRiskImplementMapper.selectMajorRiskList(id);
				if(CollectionUtils.isNotEmpty(tList)){
					tList.forEach(entity->{
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
		      hashMap.put("data", tbl);
		      hashMap.put("list", tList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, hashMap);

	}
	
	
	@Override
	public JsonBean issuedImplementSummaryOrder(String token, Integer pageNumber, Integer pageSize,
			  String id,TblRiskImplementEntity entity) throws Exception {
		  Map<String, Object> hashMap = null;
	        try {
	            hashMap = new HashMap<>();
	            //得到了当前登录的用户信息
	            TblStaffUtil staffUtil = userProvider.get();
	            if (staffUtil == null){
	                return  ResponseFormat.retParam(0, 20006, hashMap);
	            }

	          entity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
	          String[] ids=null;
			  if(StringUtils.isNotBlank(id)){
				  if(id.endsWith(","))
				  id=id.substring(0,id.length()-1);
				   ids=id.split(",");
			  }
			  final String[] idSq=ids;
			  String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "cr.LINKORGID", "cr.LINKDEPTID", "cr.createstaffid", "cr.SECRECTLEVELID", "cr.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	    		com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.tblRiskImplementMapper.selectAllSummaryListOrder(entity,idSq,sql));
	    		List<TblRiskImplementEntity> list1 = pageInfo.getList();
	    		
	    		Map<String, List<TblRiskImplementEntity>> groupEntity =list1.stream()
	                .collect(Collectors.groupingBy(
	                		TblRiskImplementEntity::getImpRiskName,TreeMap::new,Collectors.toList()));
	         // 统计合并的行数
	            Map<String, Integer> mergeCountMap = new HashMap<>();
	            List<TblRiskImplementEntity> resultList = new ArrayList<>();
	            for (Map.Entry<String, List<TblRiskImplementEntity>> entry : groupEntity.entrySet()) {
	            	List<TblRiskImplementEntity> imp = entry.getValue();
	                // 首先检查是否为null或空列表
	                if (imp == null || imp.isEmpty()) {
	                    continue; // 跳过空条目
	                }
	                // 处理列表
	                TblRiskImplementEntity en = imp.get(0); // 直接获取第一个元素
	                if (imp.size() > 1) {
	                	try {
	                	 en.setRelaCount(imp.size());
		                 List<TblRiskImplementEntity> mentList= imp.stream()
                                 .skip(1) // 跳过第一个元素（即第一行）
                                 .collect(Collectors.toList());
		                en.setRelaList(mentList);
	                    resultList.add(en); // 只保留第一行
	                	} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
	                } else {
	                    // 如果姓名不重复，直接添加到结果列表
	                    resultList.addAll(imp);
	                }
	            }
                pageInfo.setList(resultList);
               // pageInfo.setTotal(resultList.size());
	            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
	            PageResult<TblRiskImplementEntity> build = new PageResult<TblRiskImplementEntity>().build(pageInfo);
	            hashMap.put("pageInfo",build);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ResponseFormat.retParam(1, 200, hashMap);
	}

	@Override
	public JsonBean majorTransfer(String token, String ids, BigDecimal staffId) throws Exception {
		// TODO Auto-generated method stub
		String[] idList=ids.split(",");
		for(String s:idList){
        TblRiskImplementEntity implementEntity = tblRiskImplementMapper.selectById(new BigDecimal(s));
		try {
			//新建转派记录
			Staff sta=staffMapper.selectById(implementEntity.getImpLssuedStaffid());
			 QueryWrapper queryWrapper = new QueryWrapper();
             queryWrapper.eq("IMPLEMENTID", implementEntity.getId());
             queryWrapper.eq("TOSTAFFID", staffId);
             System.out.println(tblMajorTransferMapper.selectCount(queryWrapper));
             if (tblMajorTransferMapper.selectCount(queryWrapper).equals(0L)) {
			TblMajorTransfer tran=new TblMajorTransfer();
			tran.setImpRiskName(implementEntity.getImpRiskName());
			tran.setCreatetime(new Date());
			tran.setInitialName(sta.getRealname());
			tran.setInitialStaffid(implementEntity.getImpLssuedStaffid());
			tran.setToStaffid(staffId);
			Staff staff=staffMapper.selectById(staffId);
			tran.setToName(staff.getRealname());
			tran.setMajorId(implementEntity.getMajorId());
			tran.setImplementId(implementEntity.getId());
			tran.setId(RandomUtil.uuBigDecimalId());
			tblMajorTransferMapper.insert(tran);
			//修改填报表表单转派人员
			implementEntity.setTransferStaffid(staffId);
            tblRiskImplementMapper.updateById(implementEntity);
             }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		}
	    JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
	    return jsonBean;
	}

	@Override
	public JsonBean getMajorTransferList(String token, BigDecimal id) throws Exception {
		// TODO Auto-generated method stub
		  Map<String, Object> hashMap = new HashMap<String, Object>();
		  JsonBean jsonBean =null;
		try {
			  List<TblMajorTransfer> list= tblMajorTransferMapper.getMajorTransferList(id);
			  hashMap.put("data", list);
			  jsonBean = new JsonBean(1, "success", hashMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 return jsonBean;
	}

	@Override
	public JsonBean getMajorRiskTrack(String token, Integer pageNumber, Integer pageSize, String nd, String jd,
			String orgName) throws Exception {
		 Map<String, Object> hashMap = new HashMap<>();
	       try {
	           //得到了当前登录的用户信息
	           TblStaffUtil staffUtil = userProvider.get();
	           if (staffUtil == null){
	               return  ResponseFormat.retParam(0, 20006, hashMap);
	           }
	           TblMajorRiskbranchCreate mEntity=new TblMajorRiskbranchCreate();
	           mEntity.setLinkOrgName(orgName);
	           if(StringUtils.isNotBlank(nd)){
	        	   mEntity.setRiskyear(new BigDecimal(nd));
	           }
	           mEntity.setQuartername(jd);
	           
	          String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "cr.LINKORGID", "cr.LINKDEPTID", "cr.CREATESTAFFID", "cr.SECRECTLEVELID", "cr.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	            PageInfo<TblMajorRiskbranchCreate> pageInfo = PageMethod.startPage(pageNumber, pageSize)
	                   .doSelectPageInfo(() -> tblMajorRiskbranchCreateMapper.getMajorRiskTrack(mEntity,sql));
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
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					} );
					
				}
	            
	           hashMap.put("pageInfo",pageInfo);
	       } catch (Exception e) {
	           throw new RuntimeException(e);
	       }
	       return ResponseFormat.retParam(1, 200, hashMap);
	}
	
}



