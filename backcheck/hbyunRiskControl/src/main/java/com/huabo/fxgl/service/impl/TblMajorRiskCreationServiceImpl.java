package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblMajorRiskbranchCreate;
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
import com.huabo.fxgl.mapper.TblRiskImplementGroupMapper;
import com.huabo.fxgl.mapper.TblRiskImplementMapper;
import com.huabo.fxgl.mapper.TblRiskReportingMapper;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.TblMajorRiskCreationService;
import com.huabo.fxgl.service.TblRiskImplementService;
import com.huabo.fxgl.service.TblRiskImprovementService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.ls.LSInput;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TblMajorRiskCreationServiceImpl extends ServiceImpl<TblMajorRiskCreationMapper, TblMajorRiskCreate> implements TblMajorRiskCreationService {

    @Resource
    private TblMajorRiskCreationMapper tblMajorRiskCreationMapper;
    
    @Resource
    private TblMajorRiskbranchCreateMapper tblMajorRiskbranchCreateMapper;
    @Resource
    private  TblRiskImplementGroupMapper tblRiskImplementGroupMapper;
    @Resource
    private OrganizationMapper tblOrganizationMapper;
    @Resource
    private  TblRiskImplementMapper tblRiskImplementMapper;
    
    @Resource
    private UserProvider userProvider;
     
    @Resource
    private IOrganizationService organizationService;
    
    
    @Resource
    private IStaffService staffService;
    @Resource
    private StaffMapper staffMapper;

    @Resource
    private OrganizationMapper organizationMapper;
//
//    /**
//     * 下发表单-下发
//     * @param token
//     * @param tblRiskImplementEntity
//     * @return
//     * @throws Exception
//     */
//    @Transactional
//    @Override
//    public JsonBean issuedAndSubmit(TblRiskImplementEntity tblRiskImplementEntity, String token) throws Exception {
//        Map<String, Object> hashMap = null;
//        try {
//            hashMap = new HashMap<>();
//            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
//            if (staffUtil == null){
//                return  ResponseFormat.retParam(0, 20006, hashMap);
//            }
//            tblRiskImplementEntity.setImpLssuedStaffid(staffUtil.getStaffid());//下发人名称
//            tblRiskImplementEntity.setImpLssuedDate(new Date());//下发时间
//            tblRiskImplementEntity.setId(RandomUtil.uuBigDecimalId());
//            tblRiskImplementMapper.insert(tblRiskImplementEntity);
//            TblRiskImplementEntity entity = tblRiskImplementMapper.selectById(tblRiskImplementEntity.getId());
//            final TblRiskImprovementEntiry improvementEntiry = new TblRiskImprovementEntiry();
//            improvementEntiry.setId(RandomUtil.uuBigDecimalId());
//            Organization org=organizationMapper.selectById(entity.getImpLssuedUnit());
//            improvementEntiry.setBranchName(org.getOrgname());
//            improvementEntiry.setScore(new BigDecimal(100));
//            LocalDate currentDate = LocalDate.now();
//            int year = currentDate.getYear();
//            Month month = currentDate.getMonth();
//            improvementEntiry.setYear(new BigDecimal(year));
//            improvementEntiry.setMonth(new BigDecimal(month.getValue()));
//            improvementEntiry.setStatus("未上报");
//            improvementEntiry.setRiskImplementID(entity.getId());
//            improvementEntiry.setBranchId(entity.getImpLssuedUnit());
//            tblRiskImprovementService.insertImprovement(improvementEntiry);
//            //修改风险报送-相关风险事件填报 下发状态
//            TblRiskReportingEntity reportEntity=tblRiskReportingMapper.selectById(entity.getReportingId());
//            reportEntity.setState(new BigDecimal("1"));
//            tblRiskReportingMapper.updateById(reportEntity);
//            hashMap.put("pageInfo",entity);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseFormat.retParam(1, 200, hashMap);
//    }
//
//    /**
//     * 下发表单列表
//     * @param token
//     * @param pageNumber
//     * @param pageSize
//     * @return
//     * @throws Exception
//     */
//
//    @Override
//    public JsonBean issuedImplementList(String token, Integer pageNumber, Integer pageSize, String impRiskName,String id) throws Exception {
//        Map<String, Object> hashMap = null;
//        try {
//            hashMap = new HashMap<>();
//            //得到了当前登录的用户信息
//            TblStaffUtil staffUtil = userProvider.get();
//            if (staffUtil == null){
//                return  ResponseFormat.retParam(0, 20006, hashMap);
//            }
//
////            QueryWrapper<TblRiskImplementEntity> queryWrapper = new QueryWrapper<TblRiskImplementEntity>();
////            if (reportingId != null){
////                queryWrapper.eq("REPORTING_ID",reportingId);
////            }else {
////                queryWrapper.eq("IMP_LSSUED_UNIT",staffUtil.getLinkOrg().getOrgid());
////            }
//
//          final  TblRiskImplementEntity entity=new TblRiskImplementEntity();
//          entity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
//          entity.setImpRiskName(impRiskName);
//          String[] ids=null;
//		  if(StringUtils.isNotBlank(id)){
//			  if(id.endsWith(","))
//			  id=id.substring(0,id.length()-1);
//			   ids=id.split(",");
//		  }
//		  String[] idNew=ids;
//            com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
//                    .doSelectPageInfo(() -> tblRiskImplementMapper.selectAllList(entity,idNew));
//
////            for (TblRiskImplementEntity implementEntity : pageInfo.getList()) {
////                implementEntity.setImpWayStaffName(this.queryTableFieldFalseName(implementEntity).getImpWayStaffName()); //牵头领导
////                implementEntity.setImpWayDeptName(this.queryTableFieldFalseName(implementEntity).getImpWayDeptName()); //牵头部门
////                implementEntity.setImpDutyUnitName(this.queryTableFieldFalseName(implementEntity).getImpDutyUnitName()); //公司相关责任单位名称
////                implementEntity.setImpLssuedStaffidName(this.queryTableFieldFalseName(implementEntity).getImpLssuedStaffidName()); //下发人名称
////                implementEntity.setImpLssuedUnitName(this.queryTableFieldFalseName(implementEntity).getImpLssuedUnitName()); //下发单位（分公司）
////            }
//
//            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
//            PageResult<TblRiskImplementEntity> build = new PageResult<TblRiskImplementEntity>().build(pageInfo);
//
//            hashMap.put("pageInfo",build);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseFormat.retParam(1, 200, hashMap);
//    }
//
//    /**
//     * 下发表单详情
//     * @param token
//     * @param id
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public JsonBean issuedImplementDetails(String id, String token) throws Exception {
//        Map<String, Object> hashMap = null;
//        try {
//            hashMap = new HashMap<>();
//            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
//            if (staffUtil == null){
//                return  ResponseFormat.retParam(0, 20006, hashMap);
//            }
//            TblRiskImplementEntity implementEntity = tblRiskImplementMapper.selectById(id);
//            if (!Objects.isNull(implementEntity)){
//                implementEntity.setImpWayStaffName(this.queryTableFieldFalseName(implementEntity).getImpWayStaffName()); //牵头领导
//                implementEntity.setImpWayDeptName(this.queryTableFieldFalseName(implementEntity).getImpWayDeptName()); //牵头部门
//                implementEntity.setImpDutyUnitName(this.queryTableFieldFalseName(implementEntity).getImpDutyUnitName()); //公司相关责任单位名称
//                implementEntity.setImpLssuedStaffidName(this.queryTableFieldFalseName(implementEntity).getImpLssuedStaffidName()); //下发人名称
//                implementEntity.setImpLssuedUnitName(this.queryTableFieldFalseName(implementEntity).getImpLssuedUnitName()); //下发单位（分公司）
//            }
//            hashMap.put("pageInfo",implementEntity);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseFormat.retParam(1, 200, hashMap);
//    }
//
//    /**
//     * 分公司判断是否可新建
//     * @param token
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public JsonBean isCreateSubmit(String token) throws Exception {
//        Map<String, Object> hashMap = null;
//        try {
//            hashMap = new HashMap<>();
//            //得到了当前登录的用户信息
//            TblStaffUtil staffUtil = userProvider.get();
//            if (staffUtil == null){
//                return  ResponseFormat.retParam(0, 20006, hashMap);
//            }
//            QueryWrapper<TblRiskImplementEntity> queryWrapper = new QueryWrapper<TblRiskImplementEntity>();
//            queryWrapper.eq("IMP_LSSUED_UNIT",staffUtil.getLinkOrg().getOrgid());
//            List<TblRiskImplementEntity> entityList = tblRiskImplementMapper.selectList(queryWrapper);
//            if (entityList.size() > 0){
//                hashMap.put("pageInfo",entityList);
//                hashMap.put("is",1); //1 可新建
//            }else {
//                hashMap.put("pageInfo",null);
//                hashMap.put("is",0); //1 不可新建
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseFormat.retParam(1, 200, hashMap);
//    }
//
//    public TblRiskImplementEntity  queryTableFieldFalseName(TblRiskImplementEntity entity){
//        final TblRiskImplementEntity implementEntity;
//        try {
//            implementEntity = new TblRiskImplementEntity();
//            Staff ImpWayStaffName = staffMapper.selectById(entity.getImpWayStaff()); //牵头领导
//            Organization ImpWayDeptName = organizationMapper.selectById(entity.getImpWayDept()); //牵头部门
//            Organization ImpDutyUnitName = organizationMapper.selectById(entity.getImpDutyUnit()); //公司相关责任单位名称
//            Staff ImpLssuedStaffidName = staffMapper.selectById(entity.getImpLssuedStaffid());//下发人名称
//            Organization ImpLssuedUnitName = organizationMapper.selectById(entity.getImpLssuedUnit()); //下发单位（分公司）
//            implementEntity.setImpWayStaffName(ImpWayStaffName.getRealname());
//            implementEntity.setImpWayDeptName(ImpWayDeptName.getOrgname());
//            implementEntity.setImpDutyUnitName(ImpDutyUnitName.getOrgname());
//            implementEntity.setImpLssuedStaffidName(ImpLssuedStaffidName.getRealname());
//            implementEntity.setImpLssuedUnitName(ImpLssuedUnitName.getOrgname());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return implementEntity;
//    }
//
//	@Override
//	public List<TblRiskImplementEntity> exportIssuedImplement(String token,String id,String impRiskName) {
//		List<TblRiskImplementEntity> entityList;
//		  try {
//			  String[] ids=null;
//			  if(StringUtils.isNotBlank(id)){
//				  if(id.endsWith(","))
//				   id=id.substring(0,id.length()-1);
//				   ids=id.split(",");
//			  }
//			  TblStaffUtil staffUtil = userProvider.get();
//			  final  TblRiskImplementEntity entity=new TblRiskImplementEntity();
//	          entity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
//	          entity.setImpRiskName(impRiskName);
//			  entityList=tblRiskImplementMapper.selectAllList(entity,ids);
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);
//	        }
//		  return entityList;
//	}
//
//	@Override
//	public JsonBean issuedImplementSummary(String token, Integer pageNumber, Integer pageSize,
//			String impRiskName, String id,String orgname) throws Exception {
//		  Map<String, Object> hashMap = null;
//	        try {
//	            hashMap = new HashMap<>();
//	            //得到了当前登录的用户信息
//	            TblStaffUtil staffUtil = userProvider.get();
//	            if (staffUtil == null){
//	                return  ResponseFormat.retParam(0, 20006, hashMap);
//	            }
//
//	          final  TblRiskImplementEntity entity=new TblRiskImplementEntity();
//	          entity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
//	          entity.setImpRiskName(impRiskName);
//	          entity.setImpLssuedUnitName(orgname);
//	          String[] ids=null;
//			  if(StringUtils.isNotBlank(id)){
//				  if(id.endsWith(","))
//				  id=id.substring(0,id.length()-1);
//				   ids=id.split(",");
//			  }
//				List<TblRiskImplementEntity> list= tblRiskImplementMapper.selectAllSummaryList(entity,ids);
////	            com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
////	                    .doSelectPageInfo(() -> list);
//	            PageMethod.startPage(pageNumber, pageSize);
//	            com.github.pagehelper.PageInfo<TblRiskImplementEntity> pageInfo=new PageInfo<>(list) ;
//
////	            for (TblRiskImplementEntity implementEntity : pageInfo.getList()) {
////	                implementEntity.setImpWayStaffName(this.queryTableFieldFalseName(implementEntity).getImpWayStaffName()); //牵头领导
////	                implementEntity.setImpWayDeptName(this.queryTableFieldFalseName(implementEntity).getImpWayDeptName()); //牵头部门
////	                implementEntity.setImpDutyUnitName(this.queryTableFieldFalseName(implementEntity).getImpDutyUnitName()); //公司相关责任单位名称
////	                implementEntity.setImpLssuedStaffidName(this.queryTableFieldFalseName(implementEntity).getImpLssuedStaffidName()); //下发人名称
////	                implementEntity.setImpLssuedUnitName(this.queryTableFieldFalseName(implementEntity).getImpLssuedUnitName()); //下发单位（分公司）
////	            }
//
//	            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
//	            PageResult<TblRiskImplementEntity> build = new PageResult<TblRiskImplementEntity>().build(pageInfo);
//
//	            hashMap.put("pageInfo",build);
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);
//	        }
//	        return ResponseFormat.retParam(1, 200, hashMap);
//	}
//	
//	@Override
//	public List<TblRiskImplementEntity> exportIssuedImplementSummary(String token,String id,String impRiskName,String orgname) {
//		List<TblRiskImplementEntity> entityList;
//		  try {
//			  String[] ids=null;
//			  if(StringUtils.isNotBlank(id)){
//				  if(id.endsWith(","))
//				   id=id.substring(0,id.length()-1);
//				   ids=id.split(",");
//			  }
//			  TblStaffUtil staffUtil = userProvider.get();
//			  final  TblRiskImplementEntity entity=new TblRiskImplementEntity();
//	          entity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());
//	          entity.setImpRiskName(impRiskName);
//	          entity.setImpLssuedUnitName(orgname);
//			  entityList=tblRiskImplementMapper.selectAllSummaryList(entity,ids);
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);
//	        }
//		  return entityList;
//	}
//
//	@Override
//	public JsonBean reportToLeader(String token, BigDecimal id) throws Exception {
//		// TODO Auto-generated method stub
//		TblRiskImplementEntity entity = tblRiskImplementMapper.selectById(id);
//		entity.setToreport(new BigDecimal(1));//修改上报状态为已经上报
//		entity.setToreportdate(new Date());
//		tblRiskImplementMapper.updateById(entity);
//	    JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
//	    return jsonBean;
//	}
//
//	@Override
//	public JsonBean issuedSaveOrUpdate(TblRiskImplementEntity tblRiskImplementEntity, String token) throws Exception {
//		  Map<String, Object> hashMap = null;
//	        try {
//	            hashMap = new HashMap<>();
//	            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
//	            if (staffUtil == null){
//	                return  ResponseFormat.retParam(0, 20006, hashMap);
//	            }
//	            if(tblRiskImplementEntity.getId()!=null&&tblRiskImplementEntity.getId().compareTo(new BigDecimal(0))==1){
//	            	tblRiskImplementMapper.updateById(tblRiskImplementEntity);
//	            }else{
//	            	tblRiskImplementEntity.setImpLssuedStaffid(staffUtil.getStaffid());//创建人名称
//	 	            tblRiskImplementEntity.setImpLssuedDate(new Date());//创建时间
//	 	           tblRiskImplementEntity.setImpLssuedUnit(staffUtil.getLinkOrg().getOrgid());//创建单位
//	 	            tblRiskImplementEntity.setId(RandomUtil.uuBigDecimalId());
//	 	            tblRiskImplementMapper.insert(tblRiskImplementEntity);
//	            }
//	            hashMap.put("pageInfo",tblRiskImplementEntity);
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);
//	        }
//	        return ResponseFormat.retParam(1, 200, hashMap);
//	}
//
//	@Override
//	public void issuedImplementDelete(String id)
//			throws Exception {
//		// TODO Auto-generated method stub
//	      try {
//	    	  tblRiskImplementMapper.deleteById(id);
////	            //删除所有附件
////	            List<BigDecimal> attIdList = this.tblRiskReportingMapper.findAttIdListByReporting(id);
////	            for (BigDecimal attId : attIdList) {
////	                this.deleteRealtionAttInfo(attId);
////	            }
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);
//	        }
//	}

	@Override
	public JsonBean getMajorRiskCreateList(String token, Integer pageNumber, Integer pageSize, String nd, String jd)
			throws Exception {
		 Map<String, Object> hashMap = new HashMap<>();
       try {
           //得到了当前登录的用户信息
           TblStaffUtil staffUtil = userProvider.get();
           if (staffUtil == null){
               return  ResponseFormat.retParam(0, 20006, hashMap);
           }
           
           String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "LINKORGID", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
  		 
            PageInfo<TblMajorRiskCreate> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                   .doSelectPageInfo(() -> tblMajorRiskCreationMapper.getMajorRiskCreateList(nd,jd,sql,staffUtil.getStaffid()));
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
	public JsonBean mjorSaveOrUpdate(TblMajorRiskCreate tblMajorRiskCreate, String token) throws Exception {
		  Map<String, Object> hashMap = new HashMap<>();
	        try {
	            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
	            if (staffUtil == null){
	                return  ResponseFormat.retParam(0, 20006, hashMap);
	            }
	            if(tblMajorRiskCreate.getId()!=null&&tblMajorRiskCreate.getId().compareTo(new BigDecimal(0))==1){
	            	tblMajorRiskCreationMapper.updateById(tblMajorRiskCreate);
	            }else{
	            	tblMajorRiskCreate.setCreatestaffid(staffUtil.getStaffid());//创建人名称
	            	tblMajorRiskCreate.setCreatename(staffUtil.getRealname());
	            	tblMajorRiskCreate.setCreatetime(new Date());//创建时间
	            	tblMajorRiskCreate.setLinkOrgId(tblMajorRiskCreate.getLinkOrgId());//创建单位
	            	tblMajorRiskCreate.setLinkOrgName(tblMajorRiskCreate.getLinkOrgName());
	            	tblMajorRiskCreate.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
	            	tblMajorRiskCreate.setId(RandomUtil.uuBigDecimalId());
	            	tblMajorRiskCreationMapper.insert(tblMajorRiskCreate);
	            }
	            hashMap.put("data",tblMajorRiskCreate);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ResponseFormat.retParam(1, 200, hashMap);
	}

	@Override
	public JsonBean mjorDelete(String id) throws Exception {
		// TODO Auto-generated method stub
		  Map<String, Object> hashMap = new HashMap<>();
		  try {
			  TblMajorRiskCreate tbl=tblMajorRiskCreationMapper.selectById(id);
			  //if(tbl!=null&&tbl.getStatus()!=null&&tbl.getStatus()==0){
				  tblMajorRiskCreationMapper.deleteById(id);
//			  }else{
//				  return ResponseFormat.retParam(0, 201, null);
//			  }
			  
//	            //删除所有附件
//	            List<BigDecimal> attIdList = this.tblRiskReportingMapper.findAttIdListByReporting(id);
//	            for (BigDecimal attId : attIdList) {
//	                this.deleteRealtionAttInfo(attId);
//	            }
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		  return ResponseFormat.retParam(1, 200, hashMap);
	}

	@Override
	public JsonBean mjorRiskSaveOrUpdate(TblRiskImplementGroupEntity tblRiskImplementGroupEntity, String token)
			throws Exception {
		// TODO Auto-generated method stub
		  Map<String, Object> hashMap = new HashMap<>();
	        try {
	            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
	            if (staffUtil == null){
	                return  ResponseFormat.retParam(0, 20006, hashMap);
	            }
	            if(tblRiskImplementGroupEntity.getId()!=null&&tblRiskImplementGroupEntity.getId().compareTo(new BigDecimal(0))==1){
	            	tblRiskImplementGroupMapper.updateById(tblRiskImplementGroupEntity);
	            }else{
	            	tblRiskImplementGroupEntity.setId(RandomUtil.uuBigDecimalId());
	            	tblRiskImplementGroupEntity.setCreatestaffid(staffUtil.getStaffid());//创建人名称
	            	tblRiskImplementGroupEntity.setCreatetime(new Date());//创建时间
	            	tblRiskImplementGroupMapper.insert(tblRiskImplementGroupEntity);
	            }
	            hashMap.put("data",tblRiskImplementGroupEntity);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	        return ResponseFormat.retParam(1, 200, hashMap);
	}

	@Override
	public JsonBean mjorRiskDelete(String id) throws Exception {
		// TODO Auto-generated method stub
		try {
			for(String s:id.split(",")){
				tblRiskImplementGroupMapper.deleteById(s);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean mjorDetail(String id) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> hashMap = new HashMap<>();
		 FiexibleNameAssignment ment=new FiexibleNameAssignment();
		try {
			  TblMajorRiskCreate tbl=tblMajorRiskCreationMapper.selectById(id);
			  if(tbl!=null){
		        	//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(tbl,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,tbl); 
			  }
              //风险内层List
			  List<TblRiskImplementGroupEntity> tList=tblMajorRiskCreationMapper.selectMajorRiskList(id);
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
	public JsonBean mjorRiskDetail(String id) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> hashMap = new HashMap<>();
		try {
			TblRiskImplementGroupEntity tbl=tblRiskImplementGroupMapper.getOneById(id);
			if(tbl!=null){
				  FiexibleNameAssignment ment=new FiexibleNameAssignment();
		        	//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(tbl,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,tbl ); 
			}
			hashMap.put("data",tbl);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, hashMap);

	}
	
	public BigDecimal getDeptLinkCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		Organization fatherOrg = this.tblOrganizationMapper.selectById(deptId);
		if(fatherOrg.getOrgtype().compareTo(new BigDecimal(0))==0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}
	
	public BigDecimal getCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.tblOrganizationMapper.selectFatherOrgIdInfoByOrgId(deptId);
		if(fatherOrg.getOrgtype() == 0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}

/*
 *  批量选择下发人员
 * */
	@Override
	public JsonBean majorIssued(String majorid,String ids,String staffids,String issuedStaffName) throws Exception {
		// TODO Auto-generated method stub
		try {
			//获取下发的季度表 ，需要新建到每个创建人员处
			  TblMajorRiskCreate major=tblMajorRiskCreationMapper.selectById(majorid);
              String[] list=ids.split(","); //循环内层风险
              for(String id:list){
            	  //修改重大风险创建内层风险信息下发状态、时间
            	  TblRiskImplementGroupEntity entity=tblRiskImplementGroupMapper.selectById(id);
            	  entity.setImpLssuedStaffid(staffids);
            	  entity.setLssuedStaffName(issuedStaffName);
            	  Map<String, String> map=setUnitName(staffids);
            	  entity.setImpLssuedUnit(map.get("id"));
            	  entity.setImpLssuedUnitName(map.get("name"));
            	  tblRiskImplementGroupMapper.updateById(entity);
              }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}

/*
 * 重大风险创建审批完成后，集团人员进行下发操作，此方法用于列表中，操作列的确认按钮；
 * 确认进行下发后，查询该季度、该风险信息、下发至哪些人员处循环创建分公司的下发数据
 * */
	@Override
	public JsonBean majorIssuedValidate(String majorid,String ids) throws Exception {
		// TODO Auto-generated method stub
		try {
			//外层判断是否内层风险列表都进行过下发人员操作
			List<TblRiskImplementGroupEntity> staffList=tblRiskImplementGroupMapper.selectAllIssStaff(majorid);
			if(staffList!=null&&staffList.size()>0){
				  return ResponseFormat.retParam(0, 10002, null);
			}
			  TblMajorRiskCreate major=tblMajorRiskCreationMapper.selectById(majorid);
             if(major.getLssuedstatus()==null || major.getLssuedstatus()!=1){
            	  major.setImplssuedDate(new Date());//下发时间
    			  major.setLssuedstatus(1);//下发
    			  tblMajorRiskCreationMapper.updateById(major);
             }
			List<TblRiskImplementGroupEntity> allList=tblRiskImplementGroupMapper.selectAllEntityByMajrId(majorid);
			//获取下发的季度表 ，需要新建到每个创建人员处
             // String[] list=ids.split(","); //循环内层风险
              for(TblRiskImplementGroupEntity e:allList){
            	  //修改重大风险创建内层风险信息下发状态、时间
            	  TblRiskImplementGroupEntity entity=e;
            	  if(e.getToIssued().compareTo(new BigDecimal("0"))==0){
            		  entity.setToIssued(new BigDecimal(1));//确认下发状态
            		  entity.setImpLssuedDate(new Date()); //下发时间
                	  tblRiskImplementGroupMapper.updateById(entity);
            	  }
            	  String staffids=entity.getImpLssuedStaffid();
            	//将下发人员信息填充到风险表中，通过人员信息循环新建子公司季度数据、子公司风险数据
            	  if(StringUtils.isNotBlank(staffids)){
            		  for(String s:staffids.split(",")){
            			  Staff  staff=staffMapper.selectById(s);
            			  BigDecimal orgid=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
            			  String orgname=organizationMapper.selectById(orgid).getOrgname();
            			  //已下发过的数据不能再新建  子公司季度数据
            			  System.out.println("****"+tblMajorRiskbranchCreateMapper.getCountByMajorId(majorid, s)+"%%%");
                          if(tblMajorRiskbranchCreateMapper.getCountByMajorId(majorid, s)==null||tblMajorRiskbranchCreateMapper.getCountByMajorId(majorid, s)==0){
                        	  TblMajorRiskbranchCreate branch=new TblMajorRiskbranchCreate();
                        	  branch.setId(RandomUtil.uuBigDecimalId());
                        	  branch.setMajorid(new BigDecimal(majorid));
                        	  branch.setCreatestaffid(staff.getStaffid());
                        	  branch.setCreatename(staff.getRealname());
                        	  branch.setQuartername(major.getQuartername());
                        	  branch.setRiskyear(major.getRiskyear());
                        	  branch.setCreatetime(new Date());
                        	  branch.setLinkOrgId(orgid);
                        	  branch.setLinkDeptId(staff.getOrgid());
                        	  branch.setLinkOrgName(orgname);
                        	  branch.setSecrectlevelid(major.getSecrectlevelid());
                        	  tblMajorRiskbranchCreateMapper.insert(branch);
                          }
                    	  TblMajorRiskbranchCreate branch=tblMajorRiskbranchCreateMapper.getBranchByMajorId(majorid, s);
            			  //已下发过的数据不能再新建  子公司风险数据
            			 if(tblRiskImplementMapper.getCountByMajorId(branch.getId().toString() ,e.getId().toString())==0){
            			  TblRiskImplementEntity impEntity=new TblRiskImplementEntity();
                    	  impEntity.setMajorId(branch.getId());//存子公司的id 
                    	  impEntity.setMajorRiskId(e.getId()); //存集团公司风险中的id
                    	  impEntity.setId(RandomUtil.uuBigDecimalId());
                    	  impEntity.setImpRiskName(entity.getImpRiskName());
                    	  impEntity.setImpRiskDetails(entity.getImpRiskDetails());
                    	  impEntity.setImpWayStaff(entity.getImpWayStaff());
                    	  impEntity.setImpWayStaffName(entity.getImpWayStaffName());
                    	  impEntity.setImpWayDept(entity.getImpWayDept());
                    	  impEntity.setImpWayDeptName(entity.getImpWayDeptName());
                    	  impEntity.setImpDutyUnit(entity.getImpDutyUnit());
                    	  impEntity.setImpDutyUnitName(entity.getImpDutyUnitName());
                    	  impEntity.setImpLssuedStaffid(new BigDecimal(s));
                    	  impEntity.setImpLssuedUnit(orgid);
                    	  impEntity.setImpKeyIssues(entity.getPriorities());//重点事项
                    	  tblRiskImplementMapper.insert(impEntity);
            		  }
            		  }
            	  }
              }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}
	
	public Map<String, String> setUnitName(String staffids)throws Exception{
		Map<String, String> map=new HashMap<String, String>();
		try {
		StringBuffer buffId=new StringBuffer();
		StringBuffer buffName=new StringBuffer();
		String[] stList=staffids.split(",");
		for(String s :stList){
			Staff staff=staffMapper.selectById(s);
			BigDecimal id=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
			String name=organizationMapper.selectById(id).getOrgname();
			buffId.append(id+",");
			buffName.append(name+",");
		}
		int index = buffId.lastIndexOf(",");
		int index2 = buffName.lastIndexOf(",");
		 if (index != -1) {
			 buffId.deleteCharAt(index);
	        }
		 if (index2 != -1) {
			 buffName.deleteCharAt(index2);
	        }
		 map.put("id", buffId.toString());
		 map.put("name", buffName.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> importMjorRisk(String token, MultipartFile file,BigDecimal id) {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
	    try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	    	TblStaffUtil loginStaff = userProvider.get();
			StringBuffer buffer=new StringBuffer();
	        Sheet sheet = workbook.getSheetAt(0);
	        Row headerRow = sheet.getRow(1);  
	        Map<Integer, String> columnMap = new HashMap<>();
	        DataFormatter formatter = new DataFormatter();
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            TblRiskImplementGroupEntity tblRiskImplementGroupEntity=new TblRiskImplementGroupEntity();
	            if(row!=null){
	             if(StringUtils.isBlank(formatter.formatCellValue(row.getCell(1)))){
	 	            	System.out.println("数据为空");
	 		        	   continue;
	 		           }
	   	    	 List<BigDecimal> idList=organizationService.getAllDeptIds(loginStaff.getLinkOrg().getOrgid());

	            String impRiskName=formatter.formatCellValue(row.getCell(0));
	            tblRiskImplementGroupEntity.setImpRiskName(impRiskName);
	           //牵头领导 
	            String impWayStaffName=formatter.formatCellValue(row.getCell(1));
	            List<Staff> staffList=staffService.getStaffsByOrgids(impWayStaffName,idList);
	            if(StringUtils.isNotBlank(impWayStaffName)){
	            String	names = staffList.stream()
	                        .map(Staff::getRealname)
	                        .collect(Collectors.joining(","));  // 以逗号分隔
	            	 tblRiskImplementGroupEntity.setImpWayStaffName(names);
	            }
	            if(StringUtils.isNotBlank(impWayStaffName)){
	            	String	ids = staffList.stream()
	                        .map(Staff::getStaffid)
	                        .map(BigDecimal::toString)// 提取name字段
	                        .collect(Collectors.joining(","));  // 以逗号分隔
	            	 tblRiskImplementGroupEntity.setImpWayStaff(ids);
	            }
	           String buff= compareText(impWayStaffName,tblRiskImplementGroupEntity.getImpWayStaffName());
	           if(buff.length()>0){
		           buffer.append("第"+i+"行,第2列,录入内容异常：").append(buff+";");
	           }
	            //牵头责任部门
	            String impWayDeptName=formatter.formatCellValue(row.getCell(2));
	            List<Organization> orgids=organizationService.getOrgsByOrgids(impWayDeptName,idList);
	            if(StringUtils.isNotBlank(impWayDeptName)){
	            	String names = orgids.stream()
	                        .map(Organization::getOrgname)  
	                        .collect(Collectors.joining(","));  // 以逗号分隔
	            	tblRiskImplementGroupEntity.setImpWayDeptName(names);
	            }
	            if(StringUtils.isNotBlank(impWayDeptName)){
	            	String ids = orgids.stream()
	                        .map(Organization::getOrgname)  
	                        .collect(Collectors.joining(","));  // 以逗号分隔
	            	 tblRiskImplementGroupEntity.setImpWayDept(ids);
	            }
	            buff= compareText(impWayDeptName,tblRiskImplementGroupEntity.getImpWayDeptName());
		           if(buff.length()>0){
			           buffer.append("第"+i+"行,第3列,录入内容异常：").append(buff+";");
		           }
	          //公司相关责任部门
	            String impDutyUnitName=formatter.formatCellValue(row.getCell(3));
	             orgids=organizationService.getOrgsByOrgids(impDutyUnitName,idList);
	            if(StringUtils.isNotBlank(impDutyUnitName)){
	            	String names = orgids.stream()
	                        .map(Organization::getOrgname)  
	                        .collect(Collectors.joining(","));  // 以逗号分隔
	            	tblRiskImplementGroupEntity.setImpDutyUnitName(names);
	            }
	            if(StringUtils.isNotBlank(impDutyUnitName)){
	            	String ids = orgids.stream()
	                        .map(Organization::getOrgname)  
	                        .collect(Collectors.joining(","));  // 以逗号分隔
	            	 tblRiskImplementGroupEntity.setImpDutyUnit(ids);
	            }
	            buff= compareText(impDutyUnitName,tblRiskImplementGroupEntity.getImpDutyUnitName());
		           if(buff.length()>0){
			           buffer.append("第"+i+"行,第4列,录入内容异常：").append(buff+";");
		           }
	            String impRiskDetails=formatter.formatCellValue(row.getCell(5));
	            tblRiskImplementGroupEntity.setImpRiskDetails(impRiskDetails);
	            String priorities=formatter.formatCellValue(row.getCell(4));
	            tblRiskImplementGroupEntity.setPriorities(priorities);
	            tblRiskImplementGroupEntity.setMajorid(id);
	            tblRiskImplementGroupEntity.setToIssued(new BigDecimal(0));
	            tblRiskImplementGroupEntity.setId(RandomUtil.uuBigDecimalId());
            	tblRiskImplementGroupEntity.setCreatestaffid(loginStaff.getStaffid());//创建人名称
            	tblRiskImplementGroupEntity.setCreatetime(new Date());//创建时间
            	tblRiskImplementGroupMapper.insert(tblRiskImplementGroupEntity);
	            }
	        }
	        if(buffer.toString().length()>2){
		        resultMap.put("json", buffer.append("以上异常，请核对是否为当前导入组织架构人员及组织信息").toString());

	        }
	        //风险内层List
	  			  List<TblRiskImplementGroupEntity> tList=tblMajorRiskCreationMapper.selectMajorRiskList(id.toString());
	  			 FiexibleNameAssignment ment=new FiexibleNameAssignment();
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
	  	        resultMap.put("list",tList);

	    }catch (Exception e) {
				// TODO: handle exception
	        	e.printStackTrace();
			}
	    return resultMap;
	}

	
	public String compareText(String a,String b){
	//                                                          =*——*=
		String aString="";
		if(!a.equals(b)){
			String[] bList=b.split(",");
			for(String s:bList){
				a=a.replace(s.trim(), "");
			}
			aString= a.replaceAll(",", "").toString();
		}
		return aString;
	}
}
