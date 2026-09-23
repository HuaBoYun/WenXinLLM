package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.GeneralEntity;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.*;
import com.huabo.fxgl.mapper.*;
import com.huabo.fxgl.service.*;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.RISKTOP10;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
@Slf4j
public class RiskServiceImpl extends ServiceImpl<RiskMapper, Risk> implements IRiskService {

    @Autowired
    private RiskcategoryMapper riskcategoryMapper;
    @Autowired
    private RiskMapper riskMapper;
    
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private FlowBussinessServiceImpl flowBussinessService;
    @Autowired
    private RiskAssplanRiskMapper riskAssplanRiskMapper;

    @Autowired
    private FlowServiceImpl flowService;
    @Autowired
    private RiskControlmatrixServiceImpl riskControlmatrixService;
    @Autowired
    private ControlmatrixServiceImpl ControlmatrixService;

    @Autowired
    private IOrganizationService organizationService;


    @Autowired
    private FlowBussinessMapper bussinessMapper;
//    @Autowired
//    private FlowMapper flowMapper;

    @Autowired
    private IControlmatrixService controlmatrixService;

    @Autowired
    private IRiskFlowService riskFlowService;
    
    @Autowired
    private RiskControlmatrixMapper riskControlmatrixMapper;
    @Autowired
    private  ControlmatrixMapper  controlmatrixMapper;
   
    @Autowired
    private  TblControlEntriesMapper tblControlEntriesMapper;
    @Autowired
	private RiskCopingMapper riskCopingMapper;
    
    @Autowired
   	private StaffMapper staffMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public PageInfo<Risk> getRiskList(String parentId, Risk risk,Integer authorityType,Integer pageNo,Integer pageSize, TblStaffUtil staffUtil) throws Exception {
    	//根据风险分类ID 获取线面所有的子集
       // List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);

        // 改为java递归处理（多数据融合）start
        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
        List<BigDecimal> ids = list.stream().filter(o -> new BigDecimal(parentId).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
       ids.add(new BigDecimal(parentId));
        if (!ids.isEmpty()){
            List<Riskcategory> subDepartments = new ArrayList<>();

            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(parentId), list,subDepartments).stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
            ids.addAll(bigDecimals);
        }
        // parentId 124182
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", 1);

        if (ids != null && ids.size() != 0) {
            queryWrapper.in("r.riskcatid", ids);
        }
        if (StringUtils.isNotEmpty(risk.getRisknumber()))
            queryWrapper.like("r.risknumber", risk.getRisknumber());
        if (StringUtils.isNotEmpty(risk.getRiskname()))
            queryWrapper.like("r.riskname", risk.getRiskname());

        if(StringUtils.isNotEmpty(risk.getUnit()))
            queryWrapper.eq("r.unit", risk.getUnit());
        
        if(StringUtils.isNotEmpty(risk.getStatus()))
            queryWrapper.eq("r.status", risk.getStatus());

        if (StringUtils.isNotEmpty(risk.getMemo())) {
            if ("asc".equalsIgnoreCase(risk.getMemo()))
                queryWrapper.orderByAsc("r.riskcreatedt");
            else
                queryWrapper.orderByDesc("r.riskcreatedt");

        }
		if(risk.getIscurrentversion() != null){
			queryWrapper.eq("r.iscurrentversion", risk.getIscurrentversion());
		}
	/*
		if (Objects.equals(authorityType,0)){
			queryWrapper.eq("r.STAFFID", risk.getStaffid());
		}
*/
        queryWrapper.orderByDesc("r.riskid");
        
        String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), authorityType==0, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        
        com.github.pagehelper.PageInfo<Risk> pageInfo=pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> baseMapper.selectPage1(queryWrapper,sql));
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtil.isNotEmpty(pageInfo.getList())){
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
        return pageInfo;
    }


    public List<Riskcategory> getSubDepartments(BigDecimal parentId, List<Riskcategory> allList, List<Riskcategory> subDepartments ) {

//        List<Riskcategory> allList = riskcategoryMapper.selectList(new QueryWrapper<>());

        for (Riskcategory risk : allList) {
            if (risk.getFatherriskcatid().equals(parentId)) {
                subDepartments.add(risk);
                // 递归查询子部门的子部门
                subDepartments.addAll(getSubDepartments(risk.getRiskcatid(), allList,subDepartments));
            }
        }
        return subDepartments.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public PageInfo<Risk> getRiskList2(String parentId, Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,String flowname,String busname,TblStaffUtil staffUtil,Integer iscurrentversion) throws Exception{
//        List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);
//    	List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);
//      by 20240314  改为java递归处理（多数据融合）start
        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
        List<BigDecimal> ids = list.stream().distinct().filter(o -> new BigDecimal(parentId).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
        ids.add(new BigDecimal(parentId));
        if (!ids.isEmpty()){
            List<Riskcategory> subDepartments = new ArrayList<>();

            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(parentId), list,subDepartments).stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
            ids.addAll(bigDecimals);
        }
//        end
        String idsStr = "";
    	if(null!=ids && ids.size()>0) {
    		for (BigDecimal id : ids) {
    			idsStr += id + ",";
    		}
        	idsStr =  idsStr.substring(0,idsStr.length()-1);
    	}
    	final String idst=idsStr;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", 1);
        if (StringUtils.isNotEmpty(risk.getRisknumber()))
            queryWrapper.like("risknumber", risk.getRisknumber());
        if (StringUtils.isNotEmpty(risk.getRiskname()))
            queryWrapper.like("riskname", risk.getRiskname());
        if (StringUtils.isNotEmpty(risk.getUnit()))
            queryWrapper.eq("unit", risk.getUnit());
        if (StringUtils.isNotEmpty(risk.getBelongsto())) {
            queryWrapper.eq("BELONGSTO", risk.getBelongsto());
        }
        if (StringUtils.isNotEmpty(risk.getStatus())) {
            queryWrapper.eq("R.STATUS", risk.getStatus());
        }
        if (StringUtils.isNotEmpty(flowname)) {
            queryWrapper.like("r2.FLOWNAME", flowname);
        }
        if (iscurrentversion != null) {
            queryWrapper.like("iscurrentversion", iscurrentversion);
        }
        
        if (StringUtils.isNotEmpty(busname)) {
            queryWrapper.like("r2.BUSSINESSNAME", busname);
        }
//		if(risk.getClosestatus()!=null&&risk.getClosestatus().compareTo(new BigDecimal("0"))==0){
//		    queryWrapper.like("r.closestatus", risk.getClosestatus()) ;
//		}
//        queryWrapper.groupBy(" r.riskid", "r.risknumber", "r.riskname", "o.orgname",
//                "r.riskcreatedt", "r.version", "r.riskdes", "r.belongsto", "r.riskcatid", "r.status","l.FLOWNAME","b.BUSSINESSNAME","r.closestatus","r.staffid");
//        queryWrapper.orderByAsc("riskcreatedt");
//        if (StringUtils.isNotEmpty(risk.getMemo())) {
//            if ("asc".equalsIgnoreCase(risk.getMemo()))
//                queryWrapper.orderByAsc("r.riskcreatedt");
//            else
//                queryWrapper.orderByDesc("r.riskcreatedt");
//
//        }
        queryWrapper.orderByDesc("r.riskid");
        String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType));
       // String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        com.github.pagehelper.PageInfo<Risk> pageInfo=pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> baseMapper.selectPage2(queryWrapper, idst,risk.getClosestatus(),sql));
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtil.isNotEmpty(pageInfo.getList())){
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
        return pageInfo;
    }

    /*@Override
    public void saveNewRisk(Risk risk,
                            Organization attribute,
                            String oldRiskid, String isflow,
                            Staff user, String flownumber,
                            String flowname, Controlmatrix controlmatrix,
                            FlowBussiness riskBussiness) throws Exception {

        risk.setStaffid(user.getStaffid());
        risk.setRiskcreatedt(new Date());

        if (StringUtils.isNotEmpty(oldRiskid)) {
            Risk oldRisk = riskMapper.selectUnitByRiskId(oldRiskid);
            risk.setUnit(oldRisk.getUnit());
        }

        //判断新增时是否需要添加关联流程
        if (StringUtils.isNotEmpty(isflow)) {
            Flow f = new Flow();
            f.setFlownumber(flownumber);
            f.setFlowname(flowname);
            f.setCompany(attribute.getOrgid().toString());
            flowService.save(f);
            risk.getFlows().add(f);
            if ((riskBussiness.getBussinessname() != null && !"".equals(riskBussiness.getBussinessname()))
                    || (riskBussiness.getBussinessdes() != null && !"".equals(riskBussiness.getBussinessdes()))) {
                riskBussiness.setFlowid(Long.parseLong(f.getFlowid().toString()));
                flowBussinessService.save(riskBussiness);
            }
        }
        this.save(risk);
        if (StringUtils.isNotEmpty(isflow)) {

            ControlmatrixService.save(controlmatrix);
            RiskControlmatrix riskControlMatrix = new RiskControlmatrix();
            riskControlMatrix.setConmatid(controlmatrix.getConmatid());
            riskControlMatrix.setRiskid(risk.getRiskid());
            riskControlmatrixService.save(riskControlMatrix);
        }
    }*/

    @Override
    public String selectControlMatrixId(BigDecimal riskid) throws Exception {
//        by 20240318 多数据融合改造
//        String s = riskMapper.selectControlMatrixId(riskid);
        QueryWrapper<RiskControlmatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("RISKID", riskid);
        queryWrapper.orderByDesc("CONMATID");
        List<RiskControlmatrix> list = riskControlmatrixMapper.selectList(queryWrapper);
        return list.size() > 0 ? String.valueOf(list.get(0).getConmatid()) : null;
    }

    /*@Override
    public Risk geTblRiskBySave(BigDecimal riskid) {
        BigDecimal bigDecimal = riskMapper.max1();
        QueryWrapper queryWrapper = new QueryWrapper();
        if (riskid == null) {
            queryWrapper.eq("r.riskid", bigDecimal);
        } else {
            queryWrapper.eq("r.riskid", riskid);
        }
        List<Risk> riskList = list(queryWrapper);

        return riskList != null && riskList.size() > 0 ? riskList.get(0) : null;
    }*/

    @Override
    public Risk findById(String riskid) {
        List list = riskMapper.findById(riskid);

        Risk risk = new Risk();
        if (list != null && list.size() > 0) {
            risk = (Risk) list.get(0);
        }
        return risk;
    }


    @Override
    @Transactional
    public void delRiskInfoAll(String riskIds) {

    	try {
			
	
	        //1.删除自定义表单相关信息
	        riskMapper.deleteTBLFORMCONTROELE(riskIds);
	        riskMapper.deleteTBLFORMELEMENTS(riskIds);
	        riskMapper.deleteTBLFORMVALUE(riskIds);
	        riskMapper.deleteTBLFORMINFO(riskIds);
	        //2.删除控制措施;
	        String resultId1 = riskMapper.selectByRiskId1(riskIds);
	        if (resultId1 != null) {
	            riskMapper.deleteTBLRISKCONTROLMATRIX(riskIds);
	            riskMapper.deleteTBLCONTROLMATRIX(resultId1);
	        }
	
	        //3.删除风险事件
	        riskMapper.deleteTBLRISKRISKEVENT(riskIds);
	        //4.删除内规
	        riskMapper.deleteTBLRISKINNERRULE(riskIds);
	        //删除外规关系
	        riskMapper.deleteTBLRISKOUTERRULE(riskIds);
	        //3.删除流程相关信息
	        String resultId2 = riskMapper.selectByRiskId2(riskIds);
	        if (resultId2 != null) {
	            riskMapper.deleteTBLFLOWDES(resultId2);
	            riskMapper.deleteTBLRISKFLOW(riskIds);
	            riskMapper.deleteTBLFLOWBUSSINESS(resultId2);
	            riskMapper.deleteTBLFLOW(resultId2);
	        }
	        
	        try {
				QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
				queryWrapper.in("RISKID", riskIds);
				List<RiskCoping> copings = riskCopingMapper.selectList(queryWrapper);
				if(copings!=null&&copings.size()>0){
					 controlmatrixMapper.deleteControlmatrix(copings.get(0).getRiskcopingid());
					 controlmatrixMapper.deleTblriskcopingcmatrix(copings.get(0).getRiskcopingid());
					 tblControlEntriesMapper.deleByCopingId(copings.get(0).getRiskcopingid());
				
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          //删除风险应对
	        riskCopingMapper.deleteRiskCoping(riskIds);
	        riskMapper.deleteTBLRISK(riskIds);
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    }

    @Override
    public List<Object[]> findexport(List<BigDecimal> riskcatid, String riskid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(riskid)) {
            queryWrapper.in("r.riskid", riskid.split(","));
        } else {
            queryWrapper.in("r.riskcatid", riskcatid);
        }
        List<Risk> list = riskMapper.findList(queryWrapper);
        List<Object[]> exportInfoList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (Risk risk : list) {
            Object[] objects = new Object[7];
            objects[0] = risk.getRisknumber();
            objects[1] = risk.getRiskname();
            objects[2] = risk.getVersion();
            objects[3] = risk.getRiskcreatedt() != null ? dateFormat.format(risk.getRiskcreatedt()) : "";
            objects[4] = risk.getBelongsto();
            String orgIds = risk.getReorg();
            if (orgIds != null) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.select("orgname");
                queryWrapper1.in("orgid", orgIds.split(","));
               List<Object> orgNameList = organizationService.listObjs(queryWrapper1);
                log.info(orgNameList.toString());
                objects[5] = orgNameList.toString();
            } else {
                objects[5] = "";
            }
            objects[6] = risk.getRiskdes();
            exportInfoList.add(objects);
        }
        return exportInfoList;
    }




    @Override
    public IPage<Innerrule> getInnerRuleList(IPage page, BigDecimal riskid, Innerrule innerrule) {
        QueryWrapper queryWrapper  = new QueryWrapper();
        queryWrapper.eq("1", "1");
        if (innerrule != null && innerrule.getRulename() != null && !"".equals(innerrule.getRulename())) {
            queryWrapper.like("RULENAME",innerrule.getRulename());
        }
        if (innerrule != null && innerrule.getRulenumber() != null && !"".equals(innerrule.getRulenumber())) {
            queryWrapper.like("RULENUMBER",innerrule.getRulenumber());
        }
        return riskMapper.selectInnerRulePage(riskid,page,queryWrapper);
    }

	@Override
	public Page<Risk> findRiskByHistoricalVersion(Page<Risk> page,BigDecimal riskid) {
		Risk risk = this.getById(riskid);
		//递归出所有的 原风险ID值
		List<BigDecimal> list = new ArrayList<>();
		list.add(riskid);
		if (Objects.nonNull(risk.getRiskextid())) {
			List<BigDecimal> recursionIds = getRecursionRiskExtId(riskid);
			if (CollectionUtil.isNotEmpty(recursionIds)) {
				list.addAll(recursionIds);
			}
		}
		List<String> collect = list.stream().distinct().map(String::valueOf).collect(Collectors.toList());
		return riskMapper.findRecursionRiskExtId(page,StringUtils.join(collect,","));
	}

	public List<BigDecimal> getRecursionRiskExtId(BigDecimal riskextid){
		List<Risk> list = riskMapper.recursionRiskExtId(riskextid);
		if (CollectionUtil.isEmpty(list)) {
			return Collections.emptyList();
		}
		List<BigDecimal> collect = list.stream().filter(item -> Objects.nonNull(item.getRiskextid()))
				.map(Risk::getRiskextid).collect(Collectors.toList());
		if (CollectionUtil.isEmpty(collect)) {
			return Collections.emptyList();
		}
		return collect;
	}


    @Override
    public IPage<Outerrule> findOuterRuleByRiskidPageBean(IPage page, BigDecimal riskid, Outerrule outerrule) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", "1");
        if (outerrule != null && outerrule.getRulename() != null && !"".equals(outerrule.getRulename())) {
            queryWrapper.like("RULENAME",outerrule.getRulename());
        }
        if (outerrule != null && outerrule.getRulenumber() != null && !"".equals(outerrule.getRulenumber())) {
            queryWrapper.like("RULENUMBER",outerrule.getRulenumber());
        }
        return riskMapper.findOuterRuleByRiskidPageBean(riskid, page, queryWrapper);
    }

    @Override
    public Integer findRiskByRisknumberAndOrgid(String risknumber,String orgid) {
        Integer riskList = riskMapper.checkRiskNumber(risknumber, orgid);
        log.info("--------------------------------" + riskList);
        Integer num = 0;
        if (riskList!=null) {
            num = riskList;
        }
        return num;
    }

    @Override
    public IPage<Innerrule> findInnerRuleByRiskId(String riskid, Innerrule innerrule,IPage page) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("PUBLISHORG",innerrule.getPublishorg());
        /*IPage page = new Page();*/
        if (innerrule != null && innerrule.getRulename() != null && !"".equals(innerrule.getRulename())) {
            queryWrapper.like("RULENAME", innerrule.getRulename());
        }
        if (innerrule != null && innerrule.getRulenumber() != null && !"".equals(innerrule.getRulenumber())) {
                queryWrapper.like("RULENUMBER", innerrule.getRulenumber());
        }
        IPage innerRuleByRiskId = riskMapper.findInnerRuleByRiskId(riskid, page, queryWrapper);
        return innerRuleByRiskId;
    }

    @Override
    public Integer isexist(RiskInnerrule riskInnerRule) {
        return riskMapper.isexist(riskInnerRule.getRiskid().toString(),riskInnerRule.getInnrulid().toString());
    }

    @Override
    public IPage findAll(BigDecimal orgid, Page page, Outerrule tblOuterrule) {
        log.info("开始查询法律规章");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1",1);
        // 如果提供了外部法规对象，根据对象的属性构建查询条件
        if (tblOuterrule != null) {
            // 拼接条件
            // 如果外部法规名称不为空，添加名称模糊匹配条件
            if (tblOuterrule.getRulename() != null) {
                queryWrapper.like("t.rulename",tblOuterrule.getRulename());
            }
            // 如果外部法规编号不为空，添加编号模糊匹配条件
            if (tblOuterrule.getRulenumber() != null) {
                queryWrapper.like(" t.rulenumber",tblOuterrule.getRulenumber());
            }
            // 如果有效级别不为空，添加有效级别模糊匹配条件
            if (tblOuterrule.getEffectivelevel() != null) {
                queryWrapper.like(" t.effectivelevel",tblOuterrule.getEffectivelevel());
            }
            // 如果时效性不为空，添加时效性模糊匹配条件
            if (tblOuterrule.getTimeliness() != null) {
                queryWrapper.like(" t.timeliness",tblOuterrule.getTimeliness());

            }
            // 如果发文内容不为空，添加内容模糊匹配条件
            if (tblOuterrule.getBodyinfo() != null) {
                queryWrapper.like(" t.BODYINFO",tblOuterrule.getBodyinfo());
            }
        }
        queryWrapper.orderByDesc("t.outrulid");
        return riskMapper.findAll(orgid,page,queryWrapper);
    }

    @Override
    public List<Outerrule> findOuterRuleByRiskId(String riskid, Outerrule outerrule) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1",1);


        if (outerrule != null && outerrule.getRulename() != null && !"".equals(outerrule.getRulename())) {
            queryWrapper.like("RULENAME",outerrule.getRulename());
        }
        if (outerrule != null && outerrule.getRulenumber() != null && !"".equals(outerrule.getRulenumber())) {
            queryWrapper.like("RULENUMBER",outerrule.getRulenumber());

        }
        return riskMapper.findOuterRuleByRiskId(riskid,queryWrapper);
    }

    @Override
    public Integer isexist2(RiskOuterrule riskOuterRule) {
        return riskMapper.isexist2(riskOuterRule);


    }

    @Override
    @Transactional
    public void saveNewRisk(Risk risk,
                            TblOrganizationUtil attribute,
                            String oldRiskid, String isflow,
                            Staff user, Flow f, Controlmatrix controlmatrix,
                            FlowBussiness riskBussiness,
                            String attids,RiskCoping cop) throws Exception {
        if(risk.getStaffid() == null || risk.getStaffid().equals("")){
            risk.setStaffid(user.getStaffid());
        }
        if(risk.getRiskcreatedt() == null || risk.getRiskcreatedt().equals("")){
            risk.setRiskcreatedt(new Date());
        }

        if (StringUtils.isNotEmpty(oldRiskid)) {
            Risk oldRisk = riskMapper.selectUnitByRiskId(oldRiskid);
            if(null == oldRisk) {
            	risk.setUnit(null);
            }else {
            	risk.setUnit(oldRisk.getUnit());
            }
            
        }
        
        if(null==risk.getRiskid()){
            riskMapper.insert(risk);//保存risk数据
        }else {
        	riskMapper.updateById(risk);
        }
        
        if (StringUtils.isNotBlank(attids)) {
            baseMapper.deleteRiskAtt(risk.getRiskid());
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                baseMapper.insertRiskAtt(risk.getRiskid(), new BigDecimal(ids[i]));
            }
        }
        
        //判断新增时是否需要添加关联流程
        System.out.println("isFlow"+isflow);
      if (StringUtils.isNotEmpty(isflow)) {
          if ( null == f.getFlowid()){
              flowService.save(f);//保存flow表数据
              riskFlowService.save(new RiskFlow(risk.getRiskid(), f.getFlowid()));//保存中间表数据
          }else {
              flowService.updateById(f);
          }
            risk.getFlows().add(f);//将flow添加到risk中
            riskBussiness.setFlowid(Long.parseLong(f.getFlowid().toString()));
            flowBussinessService.saveOrUpdate(riskBussiness);

        }


        //监控措施修改方法  20230801注释 smf
       /* if (StringUtils.isNotEmpty(isflow)) {
            //监控措施新增
            controlmatrixService.save(controlmatrix);//保存控制点信息
            riskControlmatrixService.save(new RiskControlmatrix(controlmatrix.getConmatid(), risk.getRiskid()));//保存中间表数据
        }*/

    //中核调整风险应对内容到风险创建
      QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
      queryWrapper.eq("RISKID", risk.getRiskid());
      List<RiskCoping> list = riskCopingMapper.selectList(queryWrapper);
      try {
    	   if (list != null && list.size()>0) {
    	          if((cop.getRiskcopingid()!=null&cop.getRiskcopingid().compareTo(BigDecimal.ZERO) != 0)){
    	              RiskCoping riskCoping =  riskCopingMapper.selectById(cop.getRiskcopingid());
    	              riskCoping.setRiskhopevalue(cop.getRiskhopevalue());
    	              riskCoping.setCopinghead(cop.getCopinghead());
    	              riskCoping.setCopingplot(cop.getCopingplot());
    	              riskCoping.setYddes(cop.getYddes());
    	              riskCoping.setRiskid(risk.getRiskid());
    	              riskCopingMapper.updateById(riskCoping);
    	          }
    	      } else{
    	          cop.setRiskid(risk.getRiskid());
    	          riskCopingMapper.insert(cop);
    	      }
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
   

  /* if(riskBussiness.getBussinessid() != null) {
      riskBussiness.setFlowid(Long.parseLong(flow.getFlowid().toString()));
      tblFlowBussinessService.modify(riskBussiness);
    }*/
    }

    /*@Override
    public String selectControlMatrixId(BigDecimal riskid) throws Exception {
        String s = riskMapper.selectControlMatrixId(riskid);
        return s;
    }

    @Override
    public Risk geTblRiskBySave(BigDecimal riskid) {
        BigDecimal bigDecimal = riskMapper.max1();
        QueryWrapper queryWrapper = new QueryWrapper();
        if (riskid == null) {
            //取最大的值
            queryWrapper.eq("riskid", bigDecimal);
        } else {
            //取当前ID
            queryWrapper.eq("riskid", riskid);
        }
        return baseMapper.geTblRiskBySave(queryWrapper);//根据riskID查找所有
    }

    @Override
    public Risk findById(String riskid) {
        List list = riskMapper.findById(riskid);

        Risk risk = new Risk();
        if (list != null && list.size() > 0) {
            risk = (Risk) list.get(0);
        }
        return risk;
    }*/


    @Autowired
    private IRiskRiskeventService riskRiskeventService;

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过EventId查询Risk
     * @Date 2022/8/10
     * @param eventid
     * @return java.util.List<com.huabo.fxgl.entity.Risk>
     * @url:
     **/
    @Override
    public List<Risk> getByEventId(String eventid) {
        List<BigDecimal> riskIds = riskRiskeventService.getRiskIdsByEventId(eventid);
        return riskIds != null && riskIds.size() > 0 ? listByIds(riskIds) : null;
    }

    @Override
    public Integer checkRiskNumber(String risknumber, String orgid) {
        return riskMapper.checkRiskNumber(risknumber,orgid);
    }

    @Override
    public PageInfo<Risk> findRiskByPGRisks(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,List<BigDecimal> riskcatids,TblStaffUtil staffUtil) throws Exception {
//    	QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("1", "1");
//        if (StringUtils.isNotEmpty(risk.getRisknumber())){
//            queryWrapper.like("RISKNUMBER",risk.getRisknumber());
//        }
//
//        if (StringUtils.isNotEmpty(risk.getRiskname())){
//            queryWrapper.like("RISKNAME",risk.getRiskname());
//        }
//
//        if(StringUtils.isNotEmpty(risk.getUnit())){
//            queryWrapper.eq("UNIT",risk.getUnit());
//
//        }
//		if(StringUtils.isNotEmpty(risk.getStatus())){
//			queryWrapper.eq("status",risk.getStatus());
//
//		}
//        if(StringUtils.isNotEmpty(risk.getLevel())){
//            queryWrapper.notExists("level","未评估");
//
//        }
//		if(Objects.equals(authorityType,0)){
//			queryWrapper.eq("r.STAFFID",risk.getStaffid());
//		}
//
//        queryWrapper.orderByDesc("RISKCREATEDT");
//
//        if(StringUtils.isNotEmpty(risk.getMemo())){
//            queryWrapper.orderByAsc("MEMO");
//
//        }
    	//authorityType == 0
    	//风险管理员验证
        String sql = "";
//    	String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(),false, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType));
    //	String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        com.github.pagehelper.PageInfo<Risk> pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> riskMapper.selectRisk(risk,authorityType,riskcatids,sql));
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtil.isNotEmpty(pageInfo.getList())){
			pageInfo.getList().forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
					TblOrganization o=organizationMapper.selectFatherOrgIdInfoByOrgId(entity.getLinkDeptId());
					entity.setZrbm(o.getOrgname());
					if(o.getOrgtype()==0){
					  o=organizationMapper.selectFatherOrgIdInfoByOrgId(o.getOrgid());
					  entity.setZrdw(o.getOrgname());
					}else{
					  entity.setZrdw(o.getOrgname());
					}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
        
        return pageInfo;
    }

    @Override
    public Risk findTblRiskByFlowId(String flowid) {
        List<Risk> riskList = baseMapper.findTblRiskByFlowId(flowid);
        return riskList!=null && riskList.size()>0 ? riskList.get(0) : null;
    }

    @Autowired
    private RiskAssplanRiskMapper assplanRiskMapper;

    @Override
    public String getMaxLevelById(BigDecimal riskid) {
        return assplanRiskMapper.selectMaxLevelByRiskId(riskid);
    }

    @Override
    public Integer queryNumberRisksYiBan(BigDecimal orgid,String yiban) {
        return assplanRiskMapper.queryNumberRisksYiBan(orgid,yiban);
    }

    @Override
    public Integer queryNumberRisksZhongDa(BigDecimal orgid,String zhongda) {
        return assplanRiskMapper.queryNumberRisksZhongDa(orgid,zhongda);
    }
    
    @Override
	public JsonBean get_risk_process(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		return ResponseFormat.retParam(1, 200, this.riskMapper.get_risk_process());
	}
    
    @Override
	public JsonBean get_risk_business(String token,String processname) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblRiskBusinessDic> list=new ArrayList<TblRiskBusinessDic>();
        if(StringUtils.isNotBlank(processname)){
           list=this.riskMapper.get_risk_business(processname);
        }
		return ResponseFormat.retParam(1, 200, list);
	}
    
    @Override
	public JsonBean get_risk_no(String token,String businessno) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
       Organization org= organizationService.getById(loginStaff.getLinkOrg().getOrgid());
        //拼接风险编号
        String yearStr = String.valueOf(DateUtil.thisYear());
        String riskno = "R"+yearStr+businessno+"-";
        Integer maxno = this.riskMapper.get_risk_no("'"+riskno+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        riskno = riskno+maxno;
        
		return ResponseFormat.retParam(1, 200, riskno);
	}
    
    @Override
	public JsonBean get_riskcontrol_no(String token,String riskno) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        //拼接控制措施编号
        String riskcontrolno = riskno+"-C";
        Integer maxno = this.riskMapper.get_riskcontrol_no("'"+riskcontrolno+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        riskcontrolno = riskcontrolno+maxno;
        
		return ResponseFormat.retParam(1, 200, riskcontrolno);
	}


	@Override
	public Map<String, Object> getGroupRiskTrendChart(String token, String year) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		 try {
				List<TblOrganization> orgList=riskAssplanRiskMapper.getRiskCompanyList();
				if(orgList!=null){
					List<JSONObject> obj=new ArrayList<JSONObject>();
				    Map<String, int[]> m=new HashMap<>();
				    int[] c1=new int[orgList.size()];
				    int[] c2=new int[orgList.size()];
				    String[] orgName=new String[orgList.size()];
			        for(int i=0;i<orgList.size();i++){
			        	TblOrganization o=orgList.get(i);
			        	int count1=riskMapper.getGroupRiskTrendChart(o.getOrgid(),year,"1");
			        	int count2=riskMapper.getGroupRiskTrendChart(o.getOrgid(),year,"2");
			        	c1[i]=count1;
			        	c2[i]=count2;
			        	orgName[i]=o.getOrgname();
			        }
			        m.put("一般", c1);
			        m.put("重大", c2);
			        map.put("yAxis", m);
			        map.put("xAxis", orgName);
					}
				   
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return map;
	}


	@Override
	public Map<String, Object> getAnnualRiskGroup(String token) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> map=new HashMap<String, Object>();
		 try {
				List<TblOrganization> orgList=riskAssplanRiskMapper.getRiskCompanyList();
				 List<Integer> yearList=riskMapper.getRiskYearList();
				if(orgList!=null){
					List<JSONObject> obj=new ArrayList<JSONObject>();
				    Map<String, String> m=new HashMap<>();
				    Map<Integer, Map<String, String>> y=new HashMap<Integer, Map<String,String>>();
				    int[] c1=new int[orgList.size()];
					int[] c2=new int[orgList.size()];
					  for(int j=0;j<=yearList.size()-1;j++){
			        for(int i=0;i<orgList.size();i++){
			        	TblOrganization o=orgList.get(i);
			        	int count1=riskMapper.getGroupRiskTrendChart(o.getOrgid(),yearList.get(j)+"","1");
			        	int count2=riskMapper.getGroupRiskTrendChart(o.getOrgid(),yearList.get(j)+"","2");
			        	c1[i]=count1;
			        	c2[i]=count2;
			        }
			        m.put("一般", Arrays.toString(c1));
				    m.put("重大", Arrays.toString(c2));  
				    y.put(yearList.get(j), m);
			       }
			        map.put("yAxis", y);
			         map.put("yearList", yearList);
			        map.put("orgList", orgList);
					}
				   
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return map;
	}


	@Override
	public Integer queryNumberRisksByType(BigDecimal orgid, String year, String type) {
		// TODO Auto-generated method stub
	     return riskMapper.getGroupRiskTrendChart(orgid,year,type);

	}

	@Override
    public PageInfo<Risk> getRiskList3(Risk risk,Integer pageNo,Integer pageSize,String token) throws Exception{
//        List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);
//    	List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);
//      by 20240314  改为java递归处理（多数据融合）start
		TblStaffUtil staffUtil = userProvider.get();
        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
       BigDecimal[] cat=list.stream().map(Riskcategory::getRiskcatid).toArray(BigDecimal[]::new);
       StringBuffer buf=new StringBuffer();
       for(Riskcategory g:list){
    	   buf.append(g.getRiskcatid()+",");
       }
    	final String idst=buf.toString().substring(0,buf.toString().length()-1);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", 1);
        if (StringUtils.isNotEmpty(risk.getRisknumber()))
            queryWrapper.like("risknumber", risk.getRisknumber());
        if (StringUtils.isNotEmpty(risk.getRiskname()))
            queryWrapper.like("riskname", risk.getRiskname());
       
        String groupCompanyID = SysConfig.get("groupCompanyID");
      
        //首页穿透查询公司数据
        if (StringUtils.isNotEmpty(risk.getUnitname())) {
            queryWrapper.like("o.orgname", risk.getUnitname());
        }else {
        	//风险台账左侧树为公司部门树 ,需要判断传入的数据是公司ID还是部门ID
      		Organization org=organizationMapper.selectById(risk.getUnit());
      		if (StringUtils.isNotEmpty(risk.getUnit())&&org.getOrgtype().compareTo(new BigDecimal(0))!=0){
    	    	List<BigDecimal> idList=organizationService.getIdsByFathersId(new BigDecimal(risk.getUnit()));
    	    	 if(idList!=null&&idList.size()>0){
    	    	   queryWrapper.in("r.unit", idList);
    	    	 }else{
    	    		  queryWrapper.eq("r.unit", risk.getUnit());
    	    	 }
      		}else{
   	    	 List<BigDecimal> idList=organizationService.getIdsByFatherId(new BigDecimal(risk.getUnit()));
   	    	 if(idList!=null&&idList.size()>0){
      			queryWrapper.in("r.LINKDEPTID", idList);
   	    	 }else{
   	    		queryWrapper.eq("r.LINKDEPTID", risk.getUnit());
   	    	 }
      		}
        }
        if (StringUtils.isNotEmpty(risk.getBelongsto())) {
            queryWrapper.eq("BELONGSTO", risk.getBelongsto());
        }
        
        if (StringUtils.isNotEmpty(risk.getRiskcatname())) {
            queryWrapper.like("r.riskcatname", risk.getRiskcatname());
        }
        if (StringUtils.isNotEmpty(risk.getRiskcatidname())) {
            queryWrapper.like("cat.riskcatname", risk.getRiskcatidname());
        }
        //添加筛选条件风险等级
        if (StringUtils.isNotEmpty(risk.getSize())) {
            queryWrapper.eq("r2.size", risk.getSize());
        }
        //添加筛选条件是否已评估
        if (StringUtils.isNotEmpty(risk.getSfypg())) {
            if(risk.getSfypg().equals("是")){
                queryWrapper.ne("r2.size",0);
            }else{
                queryWrapper.eq("r2.size",0);
            }

        }
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
        queryWrapper.orderByDesc("r2.size");
        String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds()); 
      StringBuffer buffer=new StringBuffer();
      buffer.append(sql);
      if(StringUtils.isNotBlank(risk.getYear())){
    	  buffer.append(" AND year(riskcreatedt)=").append(risk.getYear());
      }
        com.github.pagehelper.PageInfo<Risk> pageInfo=pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> baseMapper.selectPage3(queryWrapper, idst,buffer.toString()));
        List<Risk> riskList=pageInfo.getList();
        if(!CollectionUtils.isEmpty(pageInfo.getList())){
        	riskList.forEach(item->{
        		if(Objects.nonNull(item.getLinkDeptId())){
        			//获取上级部门
        		 item.setSuperiorDepartment(organizationMapper.selectFatherOrgIdInfoByOrgId(item.getLinkDeptId()).getOrgname());
        		}
        		//对灵活字段中的姓名名称及机构名称赋值
				fieldOrgStaffId field=new fieldOrgStaffId();
				BeanUtils.copyProperties(item,field); 
				fieldOrgStaffName nameEntity=ment.setOpenName(field);
				BeanUtils.copyProperties(nameEntity,item ); 
				field=null; // 处理并解除引用
				nameEntity=null; // 处理并解除引用
        	});
        }
        
        
        return pageInfo;
    }


	@Override
    public List<Object[]> exportRiskList3(Risk risk,String token,String ids) throws Exception{
		TblStaffUtil staffUtil = userProvider.get();
		  List<Object[]> exportInfoList = new ArrayList<>();
		try {
        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
       BigDecimal[] cat=list.stream().map(Riskcategory::getRiskcatid).toArray(BigDecimal[]::new);
       StringBuffer buf=new StringBuffer();
       for(Riskcategory g:list){
    	   buf.append(g.getRiskcatid()+",");
       }
    	final String idst=buf.toString().substring(0,buf.toString().length()-1);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", 1);
        if (StringUtils.isNotEmpty(risk.getRisknumber()))
            queryWrapper.like("risknumber", risk.getRisknumber());
        if (StringUtils.isNotEmpty(risk.getRiskname()))
            queryWrapper.like("riskname", risk.getRiskname());
        //首页穿透查询公司数据
        if (StringUtils.isNotEmpty(risk.getUnitname())) {
            queryWrapper.like("o.orgname", risk.getUnitname());
        }
        String groupCompanyID = SysConfig.get("groupCompanyID");
        //风险台账左侧树为公司部门树 ,需要判断传入的数据是公司ID还是部门ID
  		Organization org=organizationMapper.selectById(risk.getUnit());
  		if (StringUtils.isNotEmpty(risk.getUnit()) &&org.getOrgtype().compareTo(new BigDecimal(0))!=0){
	    	List<BigDecimal> idList=organizationService.getIdsByFathersId(new BigDecimal(risk.getUnit()));
	    	 if(idList!=null&&idList.size()>0){
	    	   queryWrapper.in("r.unit", idList);
	    	 }else{
	    		  queryWrapper.eq("r.unit", risk.getUnit());
	    	 }
  		}else{
	    	 List<BigDecimal> idList=organizationService.getIdsByFatherId(new BigDecimal(risk.getUnit()));
	    	 if(idList!=null&&idList.size()>0){
  			    queryWrapper.in("r.LINKDEPTID", idList);
	    	 }else{
	    		queryWrapper.eq("r.LINKDEPTID", risk.getUnit());
	    	 }
  		}
        if (StringUtils.isNotEmpty(risk.getBelongsto())) {
            queryWrapper.eq("BELONGSTO", risk.getBelongsto());
        }
        if (StringUtils.isNotEmpty(risk.getRiskcatname())) {
            queryWrapper.like("r.riskcatname", risk.getRiskcatname());
        }
        if (StringUtils.isNotEmpty(risk.getRiskcatidname())) {
            queryWrapper.like("cat.riskcatname", risk.getRiskcatidname());
        }
        queryWrapper.orderByDesc("r.riskid");
        StringBuffer buffer=new StringBuffer();
        String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds()); 
        buffer.append(sql);
        if(StringUtils.isNotBlank(ids)){
        	buffer.append(" and r.riskid in(").append(ids).append(")");
        }
        if(StringUtils.isNotBlank(risk.getYear())){
      	  buffer.append(" AND year(riskcreatedt)=").append(risk.getYear());
        }
        List<Risk> riskList=  baseMapper.selectPage3(queryWrapper, idst,buffer.toString());
        if(!CollectionUtils.isEmpty(riskList)){
        	riskList.forEach(item->{
        		if(Objects.nonNull(item.getLinkDeptId())){
        			//获取上级部门
        		 item.setSuperiorDepartment(organizationMapper.selectFatherOrgIdInfoByOrgId(item.getLinkDeptId()).getOrgname());
        		}
        	});
        }
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         for (Risk r : riskList) {
             Object[] objects = new Object[10];
             objects[0] = r.getRisknumber();
             objects[1] = r.getRiskname();
             objects[2] = r.getRiskdes();
             objects[3] = r.getRiskcreatedt() != null ? dateFormat.format(r.getRiskcreatedt()) : "";
             objects[4] = r.getRiskcatname();
             objects[5] = r.getRiskcatidname();
             objects[6] = r.getUnitname();
             objects[7] = r.getSuperiorDepartment();
             objects[8] = r.getLinkDeptName();
             objects[9] = r.getStaffname();
             exportInfoList.add(objects);
         }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
         return exportInfoList;
    }

	
	@Override
	public JsonBean get_riskNo(String token) throws Exception {
		// TODO Auto-generated method stub
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Organization org= organizationService.getById(loginStaff.getLinkOrg().getOrgid());
        //拼接风险编号
        String yearStr = String.valueOf(DateUtil.thisYear());
        String riskno = "RISK-"+org.getOrgnumber()+"-"+yearStr+"-";
        Integer maxno = this.riskMapper.get_risk_no("'"+riskno+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        riskno = riskno+String.format("%03d", maxno);
		return ResponseFormat.retParam(1, 200, riskno);
	}


	@Override
	public List<Object[]> exportFindRiskByPGRisks(Risk risk, Integer authorityType, List<BigDecimal> riskcatids,
			TblStaffUtil staffUtil) throws Exception {
		// TODO Auto-generated method stub
		 List<Object[]> exportInfoList = new ArrayList<>();
		try {
	//	String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	   String sql = GeneralSQLConcatConfig.concatSecrectSqlEntity(new GeneralEntity(staffUtil.getCurrentOrg().getUseSecrect(), authorityType == 0, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds(),authorityType));
	   List<Risk> riskList= riskMapper.selectRisk(risk,authorityType,riskcatids,sql);
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          for (Risk r : riskList) {
        		TblOrganization o=organizationMapper.selectFatherOrgIdInfoByOrgId(r.getLinkDeptId());
				r.setZrbm(o.getOrgname());
				if(o.getOrgtype()==0){
				  o=organizationMapper.selectFatherOrgIdInfoByOrgId(o.getOrgid());
				  r.setZrdw(o.getOrgname());
				}else{
				  r.setZrdw(o.getOrgname());
				}
              Object[] objects = new Object[12];
              objects[0] = r.getRisknumber();
              objects[1] = r.getRiskname();
              objects[2] = r.getRiskcatidname();
              objects[3] = r.getRiskdes();
              objects[4] = r.getZrdw();
              objects[5] = r.getZrbm();
              objects[6] = r.getLinkDeptName();
              objects[7] = r.getZrbmName();
              String level = assplanRiskMapper.selectMaxLevelByRiskId(r.getRiskid());
              if (level == null) {
            	  level="未评估";
	            } else {
	                switch (level) {
	                    case "1": level="很低" ; break;
	                    case "2": level="较低"; break;
	                    case "3": level="中等"; break;
	                    case "4": level="较高"; break;
	                    case "5": level="很高"; break;
	                    default: level="未评估";
	                }
	            }
              objects[8] = level;
              if(r.getRiskstatus()!=null&&r.getRiskstatus().compareTo(new BigDecimal("0"))==0){
            	  objects[9] = "已关闭";
              }else{
            	  objects[9] ="未关闭";
              }
              objects[10] = r.getRiskcreatedt() != null ? dateFormat.format(r.getRiskcreatedt()) : "";
              objects[11] = r.getStaffname();
             
           
              exportInfoList.add(objects);
          }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return exportInfoList;
	}


	@Override
	public PageInfo<Risk> getTesttaskList2(String parentId, Risk risk, Integer authorityType, Integer pageNo,
			Integer pageSize, String flowname, String busname, TblStaffUtil staffUtil) throws Exception {
		  // List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);
//	    	List<BigDecimal> ids = riskcategoryMapper.selectChildCatIds(parentId);
//	      by 20240314  改为java递归处理（多数据融合）start
	        List<Riskcategory> list = riskcategoryMapper.selectList(new QueryWrapper<>());
	        List<BigDecimal> ids = list.stream().distinct().filter(o -> new BigDecimal(parentId).equals(o.getFatherriskcatid())).map(Riskcategory::getRiskcatid).collect(Collectors.toList());
	        ids.add(new BigDecimal(parentId));
	        if (!ids.isEmpty()){
	            List<Riskcategory> subDepartments = new ArrayList<>();

	            List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(parentId), list,subDepartments).stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
	            ids.addAll(bigDecimals);
	        }
//	        end
	        String idsStr = "";
	    	if(null!=ids && ids.size()>0) {
	    		for (BigDecimal id : ids) {
	    			idsStr += id + ",";
	    		}
	        	idsStr =  idsStr.substring(0,idsStr.length()-1);
	    	}
	    	final String idst=idsStr;
	        QueryWrapper queryWrapper = new QueryWrapper();
	        queryWrapper.eq("1", 1);
	        if (StringUtils.isNotEmpty(risk.getRisknumber()))
	            queryWrapper.like("risknumber", risk.getRisknumber());
	        if (StringUtils.isNotEmpty(risk.getRiskname()))
	            queryWrapper.like("riskname", risk.getRiskname());
	        if (StringUtils.isNotEmpty(risk.getUnit()))
	            queryWrapper.eq("r.unit", risk.getUnit());
	        if (StringUtils.isNotEmpty(risk.getBelongsto())) {
	            queryWrapper.eq("BELONGSTO", risk.getBelongsto());
	        }
	        if (StringUtils.isNotEmpty(risk.getStatus())) {
	            queryWrapper.eq("R.STATUS", risk.getStatus());
	        }
	        if (StringUtils.isNotEmpty(flowname)) {
	            queryWrapper.like("r2.FLOWNAME", flowname);
	        }
	        if (StringUtils.isNotEmpty(busname)) {
	            queryWrapper.like("r2.BUSSINESSNAME", busname);
	        }
	        queryWrapper.orderByDesc("r.riskid");
	        String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	       StringBuffer buf=new StringBuffer();
	       buf.append(sql);
	        if(risk.getSecrectLevelId()!=null){
	        	List<String> levels=riskMapper.getSecrectLevel(risk.getSecrectLevelId().toString());
	        	final String levelsStr=sql+String.join(",", levels);
	          	 buf.append(" and (R.SECRECTLEVELID IN (").append(String.join(",", levels)).append(") ").append(" or ").append("R.SECRECTLEVELID").append(" IS NULL OR ").append("R.SECRECTLEVELID").append(" = ''  )");;
	        }
	        com.github.pagehelper.PageInfo<Risk> pageInfo = PageMethod.startPage(pageNo, pageSize)
					.doSelectPageInfo(() -> baseMapper.selectPage2(queryWrapper, idst,risk.getClosestatus(),buf.toString()));
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
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
	        return pageInfo;
	}


	 @Override
	    public PageInfo<Risk> findRiskByPGRisksGroup(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil) throws Exception {
	    	String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	    	com.github.pagehelper.PageInfo<Risk> pageInfo =null;
	    	try {
	    		pageInfo = PageMethod.startPage(pageNo, pageSize)
					.doSelectPageInfo(() -> riskMapper.selectRisk(risk,authorityType,null,sql));
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
			
				} );
				
			}
	    	} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return pageInfo;
	    }
	 
	 
	 @Override
	    public PageInfo<Risk> getTjfxpgjgList(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil) throws Exception {
	    	String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	    	com.github.pagehelper.PageInfo<Risk> pageInfo =null;
	    	try {
	    		pageInfo = PageMethod.startPage(pageNo, pageSize)
					.doSelectPageInfo(() -> riskMapper.getTjfxpgjgList(risk,authorityType,null,sql));
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
			
				} );
				
			}
	    	} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return pageInfo;
	    }
    
	 
	 @Override
	    public PageInfo<Risk> getRiskPointTaskList(Risk risk,Integer authorityType,Integer pageNo,Integer pageSize,TblStaffUtil staffUtil) throws Exception {
	    	String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	    	com.github.pagehelper.PageInfo<Risk> pageInfo =null;
	    	try {
	    		pageInfo = PageMethod.startPage(pageNo, pageSize)
					.doSelectPageInfo(() -> riskMapper.getRiskPointTaskList(risk,authorityType,null,sql));
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtil.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
			
				} );
				
			}
	    	} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	        return pageInfo;
	    }



    @Override
		public List<Object[]> exportFindRiskByPGRisksGroup(Risk risk, Integer authorityType,
				TblStaffUtil staffUtil) throws Exception {
			// TODO Auto-generated method stub
			 List<Object[]> exportInfoList = new ArrayList<>();
			try {
			String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(),authorityType == 0 , "r.UNIT", "r.LINKDEPTID", "r.STAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
	       List<Risk> riskList= riskMapper.selectRisk(risk,authorityType,null,sql);
	       
	       
	       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	          for (Risk r : riskList) {
	              Object[] objects = new Object[12];
	              objects[0] = r.getRisknumber();
	              objects[1] = r.getRiskname();
	              objects[2] = r.getRiskcatname();
	              objects[3] = r.getRiskcatidname();
	              objects[4] = r.getRiskdes();
	              objects[5] = r.getUnit();
	              objects[6] = r.getLinkDeptName();
	              objects[7] = r.getZrbmName();
	              String level = assplanRiskMapper.selectMaxLevelByRiskId(r.getRiskid());
	              if (level == null) {
	            	  level="未评估";
		            } else {
		                switch (level) {
		                    case "1": level="很低" ; break;
		                    case "2": level="较低"; break;
		                    case "3": level="中等"; break;
		                    case "4": level="较高"; break;
		                    case "5": level="很高"; break;
		                    default: level="未评估";
		                }
		            }
	              objects[8] = level;
	              if(r.getRiskstatus()!=null&&r.getRiskstatus().compareTo(new BigDecimal("0"))==0){
	            	  objects[9] = "已关闭";
	              }else{
	            	  objects[9] ="未关闭";
	              }
	              objects[10] = r.getRiskcreatedt() != null ? dateFormat.format(r.getRiskcreatedt()) : "";
	              objects[11] = r.getStaffname();
	             
	             
	             
	              exportInfoList.add(objects);
	          }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return exportInfoList;
		}

	
	public Map<String, Object> importRiskInfo(String token, MultipartFile file) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil loginStaff = userProvider.get();
		StringBuffer buffer=new StringBuffer();
	    try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	        Sheet sheet = workbook.getSheetAt(0);
	        Row headerRow = sheet.getRow(4);  
	        Map<Integer, String> columnMap = new HashMap<>();
	        DataFormatter formatter = new DataFormatter();
	        for (int i = 5; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            Risk risk = new Risk();
	            risk.setRisknumber(get_riskNo(token).getData().toString());//编号
	            String riskname=formatter.formatCellValue(row.getCell(4));
	            String riskdes=formatter.formatCellValue(row.getCell(5));
	            if(StringUtils.isBlank(formatter.formatCellValue(row.getCell(2)))){
	            	System.out.println("数据为空");
		        	   continue;
		           }
	            String leaderShip=formatter.formatCellValue(row.getCell(1));
	            if(riskMapper.getCountByRiskname(riskname,riskdes,loginStaff.getCurrentOrg().getOrgid())>0){
	            	// buffer.append("序号第"+formatter.formatCellValue(row.getCell(0))+"行数据已存在!");
	            	continue;
	            }
                BigDecimal orgid=null;
                List<TblOrganization> orgids = null;
//	            BigDecimal orgid=organizationMapper.getOrgidForOrgname(loginStaff.getCurrentOrg().getOrgid(),leaderShip);
                orgids=organizationMapper.getOrgidForOrgname(loginStaff.getCurrentOrg().getOrgid(),orgids);
                while (orgids!= null && orgids.size()>0 ){
                    for(TblOrganization tblOrganization:orgids){
                        if(tblOrganization.getOrgname().equals(leaderShip)){
                            orgid=tblOrganization.getOrgid();
                            break;
                        }
                    }
                    orgids=organizationMapper.getOrgidForOrgname(null,orgids);
                }
	            if(orgid==null||orgid.compareTo(new BigDecimal(0))==0){
		        	   buffer.append("第"+i+"行数据,责任单位部门不存在!");
		        	   break;
		           }
	            risk.setLeadership(orgid.toString());
	            risk.setBelongsto(orgid.toString());
	            risk.setRiskcatname(formatter.formatCellValue(row.getCell(2))); //一级风险
	            BigDecimal riskOne= riskcategoryMapper.getRiskCatid(loginStaff.getCurrentOrg().getOrgid(),formatter.formatCellValue(row.getCell(2)));
	            if(riskOne==null||riskOne.compareTo(new BigDecimal(0))==0){
		        	   buffer.append("第"+i+"行数据,一级风险类型不存在!");
		        	   break;
		           }
	            risk.setRiskcatidone(riskOne);
	            String typeName=formatter.formatCellValue(row.getCell(3));//二级风险
	            BigDecimal catid= riskcategoryMapper.getRiskCatid(loginStaff.getCurrentOrg().getOrgid(),typeName);
	           if(catid==null||catid.compareTo(new BigDecimal(0))==0){
	        	   buffer.append("第"+i+"行数据,二级风险类型不存在!");
                   continue;
//	        	   break;
	           }
	            risk.setRiskcatid(catid);
	            risk.setRiskcatnametwo(typeName);
	            //验证风险名称的名字是否已经存在
	            risk.setRiskname(riskname);//三级风险
	            risk.setVersion("1.0");
	            risk.setRevisiontype(1);
	            risk.setUnit(loginStaff.getCurrentOrg().getOrgid().toString());
	            risk.setRiskcause(formatter.formatCellValue(row.getCell(6)));
	            risk.setRiskdes(riskdes); //风险描述
	            risk.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
	            risk.setStaffid(loginStaff.getStaffid());
	            risk.setRiskcreatedt(new Date());
	            riskMapper.insert(risk);
	            RiskCoping riskCoping = new RiskCoping();
	            riskCoping.setYddes(formatter.formatCellValue(row.getCell(7)));
	            riskCoping.setRiskid(risk.getRiskid());
	            riskCoping.setUnit(loginStaff.getCurrentOrg().getOrgid());
	            riskCoping.setCreatedate(new Date());
	            riskCopingMapper.insert(riskCoping);
	    }
	        resultMap.put("data", buffer.toString());
	    }catch (Exception e) {
				// TODO: handle exception
	        	e.printStackTrace();
			}
	    return resultMap;
	}


	@Override
	public Map<String, Object> riskCatnameRisks(String token, BigDecimal company) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			list=riskMapper.riskCatnameRisks(company);
			resultMap.put("data", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}


	@Override
	public Map<String, Object> riskNumbers(String token, BigDecimal company) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			Integer zs=riskMapper.getZs(company);
			Integer ysp=riskMapper.getYsp(company);
			Integer wsp=riskMapper.getWsp(company);
			resultMap.put("zs", zs);
			resultMap.put("ysp", ysp);
			resultMap.put("wsp", wsp);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	
	

	@Override
	public Map<String, Object> getCountByOrg(String year) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<Map<String,Object>> list = riskMapper.getCountByOrg(year);
			resultMap.put("list", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}
	


	@Override
	public Map<String, Object> reportByOrg(String year) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			List<Map<String,Object>> list = riskMapper.reportByOrg(year);
			resultMap.put("list", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}


	@Override
	public BigDecimal getMaxVersion(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return riskMapper.getMaxVersion(id);
	}

	/**
	 * 获取近12个月风险趋势统计
	 * 统计每个月创建的风险数量，不区分一般风险和重大风险
	 */
	@Override
	public Map<String, Object> getRiskTrendLast12Months(String orgid) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 获取近12个月的月份列表
			List<String> monthList = new ArrayList<>();
			List<Integer> countList = new ArrayList<>();

			// 使用Calendar计算近12个月
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

			// 从11个月前开始，到当前月
			for (int i = 11; i >= 0; i--) {
				Calendar tempCal = (Calendar) calendar.clone();
				tempCal.add(Calendar.MONTH, -i);
				String month = monthFormat.format(tempCal.getTime());
				monthList.add(month);

				// 查询该月的风险数量
				Integer count = riskMapper.countRiskByMonth(orgid, month);
				countList.add(count == null ? 0 : count);
			}

			// 转换月份格式为 "1月", "2月" 等
			List<String> monthLabels = new ArrayList<>();
			for (String month : monthList) {
				String[] parts = month.split("-");
				monthLabels.add(Integer.parseInt(parts[1]) + "月");
			}

			resultMap.put("months", monthLabels);
			resultMap.put("counts", countList);
			resultMap.put("total", countList.stream().mapToInt(Integer::intValue).sum());

		} catch (Exception e) {
			log.error("获取近12个月风险趋势统计失败", e);
			throw e;
		}
		return resultMap;
	}


    @Override
    public void updateRiskOrder(BigDecimal riskId,BigDecimal riskOrder) {
        riskMapper.updateOrderById(riskId, riskOrder);
    }

    @Override
    public JsonBean getRiskTopList() throws Exception {
        List<RISKTOP10> risktop10 = riskMapper.selectRiskTop();
        return ResponseFormat.retParam(1, 200, risktop10);
    }
}
