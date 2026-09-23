package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.mapper.ControlmatrixMapper;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.RiskMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.mapper.TblRiskImprovementDetailsMapper;
import com.huabo.fxgl.mapper.TblRiskMonthlyEvaluationMapper;
import com.huabo.fxgl.service.IRiskCopingService;
import com.huabo.fxgl.service.TblRiskImprovementDetailsService;
import com.huabo.fxgl.service.TblRiskMonthlyEvaluationService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.FillAttribute;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
@Slf4j
public class TblRiskMonthlyEvaluationServiceImpl  extends ServiceImpl<TblRiskMonthlyEvaluationMapper, TblRiskMonthlyEvaluationEntity> implements TblRiskMonthlyEvaluationService {

    @Resource
    private TblRiskMonthlyEvaluationMapper tblRiskMonthlyEvaluationMapper;

    @Autowired
    private IRiskCopingService copingService;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private AttachmentMapper attachmentMapper;

    @Resource
    private TblRiskImprovementDetailsService tblRiskImprovementDetailsService;

    @Resource
    private RiskMapper riskMapper;

    @Resource
    private TblRiskImprovementDetailsMapper tblRiskImprovementDetailsMapper;
    
    @Resource
    private TblControlEntriesMapper  tblControlEntriesMapper;

    @Autowired
    private ControlmatrixMapper controlmatrixMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Value("${application.auditlegaldepartment:}")
   	private String auditlegaldepartment;

    /**
     * 月度汇总
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean queryMonthlyEvaluationList(String token, Integer pageNumber, Integer pageSize,BigDecimal riskid) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }

            final TblRiskMonthlyEvaluationEntity entity=new TblRiskMonthlyEvaluationEntity();
            entity.setRiskid(riskid);
            entity.setCreateUnitid(staffUtil.getLinkOrg().getOrgid());
            entity.setCreateStaffid(staffUtil.getStaffid());

//            String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "k.UNIT", "k.LINKDEPTID", "k.STAFFID", "k.SECRECTLEVELID", "k.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
            String sql = "";

            com.github.pagehelper.PageInfo<TblRiskMonthlyEvaluationEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() ->  tblRiskMonthlyEvaluationMapper.queryMonthlyEvaluationList(entity,sql));
            //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
            PageResult<TblRiskMonthlyEvaluationEntity> build = new PageResult<TblRiskMonthlyEvaluationEntity>().build(pageInfo);

            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 单位月度风险评估表新增
     * @param attIds
     * @param tblRiskMonthlyEvaluationEntity
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean insertOrUpdateReporting(TblRiskMonthlyEvaluationEntity tblRiskMonthlyEvaluationEntity, String token, String attIds) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
//            //继承风险的密级及范围
//            if(tblRiskMonthlyEvaluationEntity.getRiskid()!=null){
//            	Risk risk=riskMapper.selectById(tblRiskMonthlyEvaluationEntity.getRiskid());
//            	tblRiskMonthlyEvaluationEntity.setSecrectLevelId(risk.getSecrectLevelId());
//            	tblRiskMonthlyEvaluationEntity.setStaffScopeIds(risk.getStaffScopeIds());
//            	tblRiskMonthlyEvaluationEntity.setStaffScopeNames(risk.getStaffScopeNames());
//            }
            if (tblRiskMonthlyEvaluationEntity.getId() != null &&tblRiskMonthlyEvaluationEntity.getId().compareTo(new BigDecimal(0))!=0){
            	 Risk risk=riskMapper.selectById(tblRiskMonthlyEvaluationEntity.getRiskid());
            	 tblRiskMonthlyEvaluationEntity.setSecrectLevelId(risk.getSecrectLevelId());
                 tblRiskMonthlyEvaluationEntity.setStaffScopeIds(risk.getStaffScopeIds());
                 tblRiskMonthlyEvaluationEntity.setStaffScopeNames(risk.getStaffScopeNames());
                tblRiskMonthlyEvaluationMapper.updateById(tblRiskMonthlyEvaluationEntity);
                if(tblRiskMonthlyEvaluationEntity.getRiskid()!=null&&tblRiskMonthlyEvaluationEntity.getRiskid().compareTo(new BigDecimal(0))!=0){
                    if(tblRiskMonthlyEvaluationEntity.getIsNewRisk()!=null&&tblRiskMonthlyEvaluationEntity.getIsNewRisk().compareTo(new BigDecimal(3))==0){
                        risk.setRiskstatus(new BigDecimal(0));
                    }else{
                        risk.setRiskstatus(new BigDecimal(2));
                    }
                    riskMapper.updateById(risk);
                }
            }else {
            	Risk risk=riskMapper.selectById(tblRiskMonthlyEvaluationEntity.getRiskid());
                tblRiskMonthlyEvaluationEntity.setCreateTime(new Date());
                tblRiskMonthlyEvaluationEntity.setCreateStaffid(staffUtil.getStaffid());
                tblRiskMonthlyEvaluationEntity.setCreateDeptid(staffUtil.getLinkDetp().getOrgid());
                tblRiskMonthlyEvaluationEntity.setCreateUnitid(staffUtil.getLinkOrg().getOrgid());
                tblRiskMonthlyEvaluationEntity.setStatus(new BigDecimal(0));
                tblRiskMonthlyEvaluationEntity.setId(RandomUtil.uuBigDecimalId());
                tblRiskMonthlyEvaluationEntity.setSecrectLevelId(risk.getSecrectLevelId());
                tblRiskMonthlyEvaluationEntity.setStaffScopeIds(risk.getStaffScopeIds());
                tblRiskMonthlyEvaluationEntity.setStaffScopeNames(risk.getStaffScopeNames());
                tblRiskMonthlyEvaluationMapper.insert(tblRiskMonthlyEvaluationEntity);
                // 风险状态：0已经关闭  1 未关闭  2 开启 用于重大风险月度评估关闭风险
                if(tblRiskMonthlyEvaluationEntity.getRiskid()!=null&&tblRiskMonthlyEvaluationEntity.getRiskid().compareTo(new BigDecimal(0))!=0){
                    
                    if(tblRiskMonthlyEvaluationEntity.getIsNewRisk()!=null&&tblRiskMonthlyEvaluationEntity.getIsNewRisk().compareTo(new BigDecimal(3))==0){
                        risk.setRiskstatus(new BigDecimal(0));
                    }else{
                        risk.setRiskstatus(new BigDecimal(2));
                    }
                    riskMapper.updateById(risk); 
                }
            }
            if(attIds != null && !"".equals(attIds)) {
                String[] attId = attIds.split(",");
                for (String aid : attId) {
                    this.tblRiskMonthlyEvaluationMapper.insertAttInfoReporting(tblRiskMonthlyEvaluationEntity.getId(),aid);
                }
            }
            TblRiskMonthlyEvaluationEntity monthlyEvaluationEntity = tblRiskMonthlyEvaluationMapper.selectById(tblRiskMonthlyEvaluationEntity.getId());
            monthlyEvaluationEntity.setUnitDeptidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getUnitDeptidName());
            monthlyEvaluationEntity.setCreateDeptidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateDeptidName());
            monthlyEvaluationEntity.setCreateUnitidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateUnitidName());
            monthlyEvaluationEntity.setCreateStaffidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateStaffidName());
            monthlyEvaluationEntity.setRisknumber(tblRiskMonthlyEvaluationEntity.getRisknumber());
            hashMap.put("pageInfo",monthlyEvaluationEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }
    /**
     * 单位月度风险评估表详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean monthlyEvaluationDetails(String id, String token) throws Exception {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
            TblRiskMonthlyEvaluationEntity monthlyEvaluationEntity = tblRiskMonthlyEvaluationMapper.selectById(id);
            if (!Objects.isNull(monthlyEvaluationEntity)){
                monthlyEvaluationEntity.setUnitDeptidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getUnitDeptidName());
                monthlyEvaluationEntity.setCreateDeptidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateDeptidName());
                monthlyEvaluationEntity.setCreateUnitidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateUnitidName());
                monthlyEvaluationEntity.setCreateStaffidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateStaffidName());

                if(monthlyEvaluationEntity.getRiskid()!=null){
                    Risk risk=riskMapper.selectById(monthlyEvaluationEntity.getRiskid());
                    monthlyEvaluationEntity.setRisknumber(risk.getRisknumber());
                }
            	//对灵活字段中的姓名名称及机构名称赋值
    			fieldOrgStaffId item=new fieldOrgStaffId();
    			BeanUtils.copyProperties(monthlyEvaluationEntity,item); 
    			fieldOrgStaffName nameEntity=ment.setOpenName(item);
    			BeanUtils.copyProperties(nameEntity,monthlyEvaluationEntity ); 
                
            }
            hashMap.put("pageInfo",monthlyEvaluationEntity);
            QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
            queryWrapper.eq("RISKID", monthlyEvaluationEntity.getRiskid());
            List<RiskCoping> copings = copingService.list(queryWrapper);
            Map<String, Object> result = new HashMap<String, Object>();
            if(null!=copings && copings.size()>0){
                RiskCoping coping = copings.get(0);
            	//对灵活字段中的姓名名称及机构名称赋值
    			fieldOrgStaffId item=new fieldOrgStaffId();
    			BeanUtils.copyProperties(coping,item); 
    			fieldOrgStaffName nameEntity=ment.setOpenName(item);
    			BeanUtils.copyProperties(nameEntity,coping ); 
                hashMap.put("copings", coping);
                List<Controlmatrix> cons = controlmatrixMapper.selectByCopingId(coping.getRiskcopingid().toString());
    			if(CollectionUtils.isNotEmpty(cons)){
    				cons.forEach(entity->{
    					try {
    						//对灵活字段中的姓名名称及机构名称赋值
    						fieldOrgStaffId field=new fieldOrgStaffId();
    						BeanUtils.copyProperties(entity,field); 
    						fieldOrgStaffName nameEntity1=ment.setOpenName(field);
    						BeanUtils.copyProperties(nameEntity1,entity ); 
    						field=null; // 处理并解除引用
    						nameEntity1=null; // 处理并解除引用
    						List<TblControlEntries> entitys=tblControlEntriesMapper.getList(entity.getConmatid());
    						entity.setEntries(entitys);
    				} catch (Exception e) {
    					// TODO: handle exception
    					e.printStackTrace();
    				}
    				} );
    				
    			}
                hashMap.put("controls", cons);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return ResponseFormat.retParam(1, 200, hashMap);
    }

    @Override
    public JsonBean getMonthlyEvaluationAttInfo(String token, String id) throws Exception {
        List<Attachment> attList = null;
        try {
            TblStaffUtil user = userProvider.get();
            if(user == null) {
                return ResponseFormat.retParam(0,20006,null);
            }
            Map<String,Object> resultMap = new HashMap<String,Object>(0);

            attList = this.attachmentMapper.selectAttListByMonthlyEvaluation(id);
            resultMap.put("pageInfo", attList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1,200,attList);
    }

    /**
     * 单位月度风险评估表详情删除
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public void monthlyEvaluationADelete(String id) {
        try {
            tblRiskMonthlyEvaluationMapper.deleteById(id);
            //删除所有附件
            List<BigDecimal> attIdList = this.tblRiskMonthlyEvaluationMapper.findAttIdListByMonthlyEvaluation(id);
            for (BigDecimal attId : attIdList) {
                this.deleteRealtionAttInfo(attId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分公司上报到总公司
     * @param token
     * @return
     */
    @Transactional
    @Override
    public JsonBean monthlyEvaluationSubmit(String token,String id) {
        try {
            TblStaffUtil user = userProvider.get();
            if(user == null) {
                return ResponseFormat.retParam(0,20006,null);
            }
            final TblRiskMonthlyEvaluationEntity evaluationEntity = new TblRiskMonthlyEvaluationEntity();
            evaluationEntity.setId(new BigDecimal(id));
            evaluationEntity.setReportButtonStatus(new BigDecimal(1));
            //根据id修改状态，1为总公司列表
            tblRiskMonthlyEvaluationMapper.updateById(evaluationEntity);
            //根据id查询实体类
            TblRiskMonthlyEvaluationEntity monthlyEvaluationEntity = tblRiskMonthlyEvaluationMapper.selectById(id);
            Organization createUnitidName = organizationMapper.selectById(monthlyEvaluationEntity.getCreateUnitid());//创建单位名称
            //先验证防止二次上报
            final TblRiskImprovementDetailsEntiry riskImprovementDetailsEntiry = new TblRiskImprovementDetailsEntiry();
            riskImprovementDetailsEntiry.setId(RandomUtil.uuBigDecimalId());
            //风险监督改进与月度评估关联
            riskImprovementDetailsEntiry.setImprovementId(monthlyEvaluationEntity.getId());
            riskImprovementDetailsEntiry.setRiskid(monthlyEvaluationEntity.getRiskid());
            riskImprovementDetailsEntiry.setScoreDetails("0");
            LocalDate currentDate = LocalDate.now();
            int year = currentDate.getYear();
            riskImprovementDetailsEntiry.setYears(new BigDecimal(year));
            riskImprovementDetailsEntiry.setMonth(new BigDecimal(currentDate.getMonth().getValue()));
            riskImprovementDetailsEntiry.setIsReport("已上报");
            riskImprovementDetailsEntiry.setBranchName(createUnitidName.getOrgname());
            riskImprovementDetailsEntiry.setBranchId(monthlyEvaluationEntity.getCreateUnitid());
            riskImprovementDetailsEntiry.setCreatTime(new Date());
            List<TblRiskImprovementDetailsEntiry> list= tblRiskImprovementDetailsMapper.selectRiskDetail(riskImprovementDetailsEntiry);
            if(list!=null&&list.size()>0){
            	TblRiskImprovementDetailsEntiry entity=list.get(0);
            	 entity.setImprovementId(monthlyEvaluationEntity.getId());
            	 entity.setCreatTime(new Date());
            	 tblRiskImprovementDetailsMapper.updateById(entity);
            }else{
            	tblRiskImprovementDetailsService.insertTblRiskImprovementDetails(riskImprovementDetailsEntiry);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1,200,null);
    }




    /**
     * 风险评估汇总列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean queryRiskAssessmentSummaryList(String token, Integer pageNumber, Integer pageSize,TblRiskMonthlyEvaluationEntity eEntity) {
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            String sas = "";
//            if(staffUtil.getCurrentOrg().getUseSecrect() == 1){
//            	sas = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "k.UNIT", "k.LINKDEPTID", "k.STAFFID", "k.SECRECTLEVELID", "k.STAFFSCOPEIDS", staffUtil.getStaffid(), null, staffUtil.getSecrectScopeIds());
//            }
            String sql = sas;

            com.github.pagehelper.PageInfo<TblRiskMonthlyEvaluationEntity> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() ->  tblRiskMonthlyEvaluationMapper.selectAllList(eEntity,sql));
//            for (TblRiskMonthlyEvaluationEntity monthlyEvaluationEntity : pageInfo.getList()) {
//                monthlyEvaluationEntity.setUnitDeptidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getUnitDeptidName());
//                monthlyEvaluationEntity.setCreateDeptidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateDeptidName());
//                monthlyEvaluationEntity.setCreateUnitidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateUnitidName());
//                monthlyEvaluationEntity.setCreateStaffidName(this.queryTableFieldFalseName(monthlyEvaluationEntity).getCreateStaffidName());
//            }
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
            PageResult<TblRiskMonthlyEvaluationEntity> build = new PageResult<TblRiskMonthlyEvaluationEntity>().build(pageInfo);

            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    private void deleteRealtionAttInfo(BigDecimal attId) throws Exception {
        try {
            Attachment att = this.attachmentMapper.selectEntityById(attId);
            this.tblRiskMonthlyEvaluationMapper.deleteFileInfoByAttId(att.getAttid());
            this.attachmentMapper.deleteEntity(att.getAttid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TblRiskMonthlyEvaluationEntity  queryTableFieldFalseName(TblRiskMonthlyEvaluationEntity entity){
        try {
            final TblRiskMonthlyEvaluationEntity monthlyEvaluationEntity = new TblRiskMonthlyEvaluationEntity();
            Organization unitDeptidName = organizationMapper.selectById(entity.getUnitDeptid()); //责任单位（部门））名称
            Organization createDeptidName = organizationMapper.selectById(entity.getCreateDeptid());//创建部门名称
            Organization createUnitidName = organizationMapper.selectById(entity.getCreateUnitid());//创建单位名称
            Staff staffName = staffMapper.selectById(entity.getCreateStaffid()); //创建人名称
            monthlyEvaluationEntity.setUnitDeptidName(unitDeptidName.getOrgname()); //责任单位（部门））名称
            monthlyEvaluationEntity.setCreateDeptidName(createDeptidName.getOrgname());//创建部门名称
            monthlyEvaluationEntity.setCreateUnitidName(createUnitidName.getOrgname());//创建单位名称
            monthlyEvaluationEntity.setCreateStaffidName(staffName.getRealname());//创建人名称
            return monthlyEvaluationEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TblRiskMonthlyEvaluationEntity> exportMonthlyEvaluationSummary(String id,TblRiskMonthlyEvaluationEntity eEntity,String token) throws Exception {
        // TODO Auto-generated method stub
        List<TblRiskMonthlyEvaluationEntity> list=null;
        try {
            String[] ids=null;
            if(StringUtils.isNotBlank(id)){
                if(id.endsWith(","))
                    id=id.substring(0,id.length()-1);
                ids=id.split(",");
            }
            TblStaffUtil staffUtil = userProvider.get();
//            String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "k.UNIT", "k.LINKDEPTID", "k.STAFFID", "k.SECRECTLEVELID", "k.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
            String sql = "";
            list= tblRiskMonthlyEvaluationMapper.exportMonthlyEvaluationSummary(ids,eEntity,sql);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(list)){
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
        }
        return list;
    }

    @Override
    public JsonBean queryAssessedRiskList(String token, Risk risk, String id, Integer pageNumber, Integer pageSize,Integer authorityType) throws Exception {
        // TODO Auto-generated method stub
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            //得到了当前登录的用户信息
            String[] ids=null;
            if(StringUtils.isNotBlank(id)){
                if(id.endsWith(","))
                    id=id.substring(0,id.length()-1);
                ids=id.split(",");
            }
            String[] idNew=ids;
            TblStaffUtil staffUtil = userProvider.get();
            
            String orgids=staffUtil.getDeptIds();
            if(staffUtil.getRoleNames().contains("风险管理员")){
 				Organization organization = organizationMapper.selectById(staffUtil.getLinkDetp().getOrgid());
 				List<Organization> list = organizationMapper.selectbyfathidall(organization.getOrgid());
 				if(list!=null && list.size()>0) {
 					for (Organization org : list) {
						orgids+=","+org.getOrgid();
					}
 				}
 				orgids+=","+organization.getOrgid();
 			}

	         String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "s.UNIT", "s.LINKDEPTID", "s.STAFFID", "s.SECRECTLEVELID", "s.STAFFSCOPEIDS", staffUtil.getStaffid(), orgids, staffUtil.getSecrectScopeIds());
//            String sql = "";
	         BigDecimal company;
     		if (JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
     			company=null;
     		} else {
     			
     			company=staffUtil.getLinkOrg().getOrgid();
     		}
     		
            com.github.pagehelper.PageInfo<Risk> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                    .doSelectPageInfo(() ->  tblRiskMonthlyEvaluationMapper.queryAssessedRiskList(risk,idNew,company,sql));
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
//            if(CollectionUtils.isNotEmpty(pageInfo.getList())){
//				pageInfo.getList().forEach(entity->{
//					try {
//						//对灵活字段中的姓名名称及机构名称赋值
//						fieldOrgStaffId item=new fieldOrgStaffId();
//						BeanUtils.copyProperties(entity,item); 
//						fieldOrgStaffName nameEntity=ment.setOpenName(item);
//						BeanUtils.copyProperties(nameEntity,entity ); 
//						item=null; // 处理并解除引用
//						nameEntity=null; // 处理并解除引用
//						BigDecimal month=tblRiskMonthlyEvaluationMapper.getMaxMonthy(entity.getRiskid(),year);
//						entity.setReportmonth(FillAttribute.monthMap.get(month));
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}
//				} );
//				
//			}
            PageResult<Risk> build = new PageResult<Risk>().build(pageInfo);
            hashMap.put("pageInfo",build);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    @Override
    public List<TblRiskMonthlyEvaluationEntity> exportMonthlyEvaluation(String token,String id, String createUnitidName,
                                                                        String riskid) throws Exception {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        List<TblRiskMonthlyEvaluationEntity> list=null;
        try {
            TblStaffUtil staffUtil = userProvider.get();
            String[] ids=null;
            if(StringUtils.isNotBlank(id)){
                if(id.endsWith(","))
                    id=id.substring(0,id.length()-1);
                ids=id.split(",");
            }
            String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "k.UNIT", "k.LINKDEPTID", "k.STAFFID", "k.SECRECTLEVELID", "k.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
            list= tblRiskMonthlyEvaluationMapper.exportMonthlyEvaluation(ids,riskid,sql);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    @Override
    public List<Risk> exportRiskEvaluated(String token, Risk risk, String id,Integer authorityType) throws Exception {
        // TODO Auto-generated method stub
        List<Risk> riskList=null;
        try {
            //得到了当前登录的用户信息
            String[] ids=null;
            if(StringUtils.isNotBlank(id)){
                if(id.endsWith(","))
                    id=id.substring(0,id.length()-1);
                ids=id.split(",");
            }
            String[] idNew=ids;
            TblStaffUtil staffUtil = userProvider.get();
            String orgids=staffUtil.getDeptIds();
            if(staffUtil.getRoleNames().contains("风险管理员")){
 				Organization organization = organizationMapper.selectById(staffUtil.getLinkDetp().getOrgid());
 				List<Organization> list = organizationMapper.selectbyfathidall(organization.getOrgid());
 				if(list!=null && list.size()>0) {
 					for (Organization org : list) {
						orgids+=","+org.getOrgid();
					}
 				}
 				orgids+=","+organization.getOrgid();
 			}
             String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "s.UNIT", "s.LINKDEPTID", "s.STAFFID", "s.SECRECTLEVELID", "s.STAFFSCOPEIDS", staffUtil.getStaffid(), orgids, staffUtil.getSecrectScopeIds());
            BigDecimal company;
     		if (JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
     			company=null;
     		} else {
     			company=staffUtil.getLinkOrg().getOrgid();
     		}
             riskList= tblRiskMonthlyEvaluationMapper.queryAssessedRiskList(risk,idNew,company,sql);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            if(CollectionUtils.isNotEmpty(riskList)){
            	riskList.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
						BigDecimal month=tblRiskMonthlyEvaluationMapper.getMaxMonthy(entity.getRiskid(),year);
						entity.setReportmonth(FillAttribute.monthMap.get(month));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
        } catch (Exception e) {
            // TODO: handle exception
        }
        return riskList;
    }

    /**
     * 系统每月15日进行提醒，每个公司的风控管理员，去填报月度评估信息；
     * @return
     * @throws Exception
     */
    @Transactional
    @Override
    public void isReportVersionT() {
        log.info("定时任务开始！开始时间"+new Date() +"");
        try {
            //获取上月
            LocalDate currentDate = LocalDate.now();
            int month = currentDate.getMonthValue();
            int year = currentDate.getYear();
            //查询公司风险表有没有开启状态的风险源，如果有开启状态的风险源，根据风险所在的公司查询监督表中是否有当月上报的记录，没有的进行扣分
            List<Risk> riskList=riskMapper.selectOpenRisk();
            for(Risk s:riskList){
                String company=s.getUnit();
                final TblRiskImprovementDetailsEntiry entity=tblRiskMonthlyEvaluationMapper.selectRiskReport(company,month,s.getRiskid(),year);
                if(entity==null){
                    final TblRiskImprovementDetailsEntiry detailsEntiry = new TblRiskImprovementDetailsEntiry();
                    detailsEntiry.setIsReport("未上报");
                    detailsEntiry.setMonth(new BigDecimal(month));
                    detailsEntiry.setScoreDetails("-0.1");
                    detailsEntiry.setImprovementId(new BigDecimal(0));
                    Organization org=organizationMapper.selectById(company);
                    detailsEntiry.setYears(new BigDecimal(year));
                    detailsEntiry.setBranchName(org.getOrgname());
                    detailsEntiry.setBranchId(org.getOrgid());
                    detailsEntiry.setRiskid(s.getRiskid());
                    detailsEntiry.setId(RandomUtil.uuBigDecimalId());
                    detailsEntiry.setCreatTime(new Date());
                    tblRiskImprovementDetailsMapper.insert(detailsEntiry);
                }
            }
        } catch (Exception e) {
            log.info("定时任务错误信息！错误时间"+new Date() +",错误内容："+e.getLocalizedMessage()+"");
            throw new RuntimeException(e);
        }
        log.info("定时任务结束！结束时间"+new Date() +"");
    }

    @Override
    public JsonBean remindMonthlyEvaluation(String token) throws Exception {
        // TODO Auto-generated method stub
        Map<String, Object> hashMap = null;
        try {
            TblStaffUtil staffUtil = userProvider.get();
            //判断登录人是否是风控管理员
            hashMap = new HashMap<>();
            if(JudgeRoleRight.judgeRoleRight("风控管理员",staffUtil.getRoleNames())){
                hashMap.put("data", "请在15日前进行风险月度评估上报！");
            }else{
                hashMap.put("data", "");
            }
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);

    }

    @Override
    public JsonBean test(String token,int number){
        // TODO Auto-generated method stub
        Map<String, Object> hashMap = null;
        try {
            hashMap = new HashMap<>();
            if(number==1){
                tblRiskMonthlyEvaluationMapper.sql1();
                tblRiskMonthlyEvaluationMapper.sql2();
            }else{
                String a="";
                String b="";
                String c="";
                //tblRiskMonthlyEvaluationMapper.updatesql("delete from TBL_RISK_IMPROVEMENT_DETAILS");
            }

        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }
        return ResponseFormat.retParam(1, 200, hashMap);

    }
}
