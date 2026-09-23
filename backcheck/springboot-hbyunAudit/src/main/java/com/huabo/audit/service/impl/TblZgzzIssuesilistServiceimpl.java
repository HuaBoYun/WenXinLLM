package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.entity.Pamas;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.service.TblZgzzIssuesilistService;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:02
 */
@Service
public class TblZgzzIssuesilistServiceimpl implements TblZgzzIssuesilistService {
	@Resource
	private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;
	
	@Resource
	private TblNbsjWbProjectMapper tblNbsjWbProjectMapper;

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	public TblNbsjSheetService tBlNbsjSheetService;
	
	@Resource
    private TblNbsjSheetMapper tblNbsjSheetMapper;
	
	@Resource
	private TblTesttaskProblemFindMapper tblTesttaskProblemFindMapper;
	
	@Resource
	private TblRectificationIssuesMapper tblRectificationIssuesMapper;
	
	@Resource
    private UserProvider userProvider;
    @Autowired
    private TblNbsjDoubtfulpointMapper tblNbsjDoubtfulpointMapper;

	@Override
	public JsonBean getProblemLedgerList(String token, TblTesttaskProblemFind problem, Integer pageNumber,
			Integer pageSize) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//分页查询内控问题
		PageInfo<TblTesttaskProblemFind> pageInfo = new PageInfo<TblTesttaskProblemFind>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setCondition(problem);
		
		com.github.pagehelper.PageInfo<TblTesttaskProblemFind> page = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.tblTesttaskProblemFindMapper.selectPageInfoByIssues(problem));
        
        pageInfo.setTlist(page.getList());
        pageInfo.setTotalRecord((int)page.getTotal());
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}
	
	@Override
	public JsonBean getSummaryAudit_list(String token, TBlNbsjSheetVo tBlNbsjSheetVo, Integer pageNumber,
			Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		tBlNbsjSheetVo.setStaffid(loginStaff.getStaffid().toString());
		
		//分页查询审计问题
		PageInfo<TblNbsjSheetEntity> pageInfo = new PageInfo<TblNbsjSheetEntity>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
		
    	
    	com.github.pagehelper.PageInfo<TblNbsjSheetEntity> page = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> this.tblNbsjSheetMapper.selectPageInfoListByIssues(tBlNbsjSheetVo));
         
        pageInfo.setTlist(page.getList());
        pageInfo.setTotalRecord((int)page.getTotal());
    	
    	return ResponseFormat.retParam(1,200,pageInfo);
	}
	//exportIssuesList
	@Override
	public JsonBean getIssuesList(String token, TblZgzzIssuesilistVo issues) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        PageInfo<TblZgzzIssuesilistVo> pageInfo = new PageInfo<TblZgzzIssuesilistVo>();
        pageInfo.setPageSize(issues.getPageSize());
        pageInfo.setCurrentPage(issues.getPageNum());
        pageInfo.setCondition(issues);
        
        issues.setCreateStaff(loginStaff.getStaffid());
        issues.setDeptIds(loginStaff.getDeptIds());
        issues.setStatusStr("0,1,2,3,4,5,6,11");
        
        com.github.pagehelper.PageInfo<TblZgzzIssuesilistVo> page = PageMethod.startPage(issues.getPageNum(), issues.getPageSize()).doSelectPageInfo(() -> this.tblZgzzIssuesilistMapper.selectListByPageInfo(issues,loginStaff));
        
        pageInfo.setTlist(page.getList());
        pageInfo.setTotalRecord((int)page.getTotal());
        
		return ResponseFormat.retParam(1, 200, pageInfo);
	}
	
	
	@Override
	public JsonBean removeIssuesFile(String token, String issuesId, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        //附件表不删除保留  中间关系表删除连接关系
        this.tblZgzzIssuesilistMapper.deleteFileRelationByAttId(issuesId,attId);
        
        return ResponseFormat.retParam(1, 200, null);
	}
	
	
	@Override
	public JsonBean saveIssues(String token, TblZgzzIssuesilist issues, String projectName, String projectNo, String[] attIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		if(issues.getIssuesType().compareTo(3) == 0  || issues.getIssuesType().compareTo(4) == 0) {
			//判断为题来源为外部 保存现有的项目名称和编号
			List<TblNbsjWbProject> list = tblNbsjWbProjectMapper.findNbsjxmBynameAll(projectName,issues.getIssuesType());
			if(list!=null && list.size()>0) {
				//如果项目已存在 则直接保存关联关系
				issues.setProjectId(list.get(0).getProjectid());
			}else {
				//如果项目不存，则新增项目后保存关联关系
				TblNbsjWbProject pro=new TblNbsjWbProject();
				pro.setCreatestaff(loginStaff.getStaffid());
				pro.setProjectname(projectName);
				pro.setProjectcode(projectNo);
				pro.setCreatetime(new Date());
				pro.setLinkOrgId(loginStaff.getCurrentOrg().getOrgid());
				pro.setProjectid(RandomUtil.uuBigDecimalId());
				pro.setProjecttype(issues.getIssuesType());
				tblNbsjWbProjectMapper.insertSelective(pro);
				issues.setProjectId(pro.getProjectid());
			}
		}

		//判断新增还是修改
        if(StringUtils.isNotBlank(issues.getIssuesId())) {
        	//判断当前修改人是否是创建人如果不是则拒绝修改
        	TblZgzzIssuesilistVo oldIssues = this.tblZgzzIssuesilistMapper.selectEntityById(issues.getIssuesId().toString());
        	if(oldIssues.getCreateStaff().compareTo(loginStaff.getStaffid())!=0) {
        		return ResponseFormat.retParam(0, 10001, null);
        	}

			//验证业务编号是否重复
        	Integer count = this.tblZgzzIssuesilistMapper.selectCountByIssuesCode(issues.getIssuesCode(),issues.getQuesitionId(),issues.getIssuesId().toString());
        	if(count.compareTo(0) > 0) {
        		return ResponseFormat.retParam(0, 50003, null);
        	}
        	//id不为空则是修改
        	issues.setUpdateTime(new java.util.Date());
        	this.tblZgzzIssuesilistMapper.updateByPrimaryKeySelective(issues);
        }else {
        	//验证业务编号是否重复
        	Integer count = this.tblZgzzIssuesilistMapper.selectCountByIssuesCode(issues.getIssuesCode(),issues.getQuesitionId(),null);
        	if(count.compareTo(0) > 0) {
        		return ResponseFormat.retParam(0, 50003, null);
        	}
        	
        	//id为空则是新增
        	issues.setLinkOrgId(loginStaff.getCurrentOrg().getOrgid());
            issues.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
            issues.setIssuesId(RandomUtil.uuStringId());
            issues.setCreateStaff(loginStaff.getStaffid());
            issues.setCreateTime(new Date());
            issues.setStatus(0);
            issues.setIssuesVersion(1);
            this.tblZgzzIssuesilistMapper.insertSelective(issues);
        }
        
        //保存附件关系
        if(attIds != null) {
        	for (String attid : attIds) {
        		if(StringUtils.isBlank(attid)) {
        			continue;
        		}
        		
    			this.tblZgzzIssuesilistMapper.saveFileRelation(attid,issues.getIssuesId().toString());
    		}
        }

        return ResponseFormat.retParam(1, 200, issues);
	}

	@Override
	public JsonBean getIssues(String token, String issuesId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //根据主键查询实体
        TblZgzzIssuesilistVo issues = this.tblZgzzIssuesilistMapper.selectEntityById(issuesId);
        
        if(issues == null) {
        	return ResponseFormat.retParam(0, 50001, null);
        }
        
        List<TblRectificationIssuesVo> relaList = this.tblRectificationIssuesMapper.selectListByIssuesId(issues.getIssuesId(),null);
        issues.setRelaList(relaList);
        
        /*if(StringUtils.isNotBlank(issues.getIssuesParent())) {
        	TblZgzzIssuesilistVo parentIssues = this.tblZgzzIssuesilistMapper.selectEntityById(issues.getIssuesParent());
        	issues.setParentIssues(parentIssues);
        }*/
        
        //查询所属列表集合
        List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyIssuesId(issuesId);
        issues.setAttList(attList);
        return ResponseFormat.retParam(1, 200, issues);
	}

	@Override
	public JsonBean removeIssues(String token, String issuesId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
		//1.获取实体判断状态
        TblZgzzIssuesilistVo issues = this.tblZgzzIssuesilistMapper.selectEntityById(issuesId);
        
        if(issues.getStatus().compareTo(0) > 0) {
        	return ResponseFormat.retParam(0, "问题整改中，无法删除", null);
        }
        
        //2.删除附件关系表中的数据
        this.tblZgzzIssuesilistMapper.deleteFileRelation(issuesId);
		//删除实体数据
		this.tblNbsjWbProjectMapper.deleteByIssuesId(issues.getProjectId());
        
        //3.删除实体数据
        this.tblZgzzIssuesilistMapper.deleteByPrimaryKey(issues.getIssuesId());
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean exportIssuesList(String token, TblZgzzIssuesilistVo issues, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        //1.根据输入条件获取所选择的整改清单列表 为空默认所有清单导出
        issues.setCreateStaff(loginStaff.getStaffid());
        issues.setDeptIds(loginStaff.getDeptIds());
//        issues.setStatusStr("0,1,2,3,4,5,6");
        issues.setStatusStr("0,1,2,3,4,5,6,11");
		List<TblZgzzIssuesilistVo> issuesList= this.tblZgzzIssuesilistMapper.selectListByPageInfo(issues,loginStaff);

//		List<TblZgzzIssuesilistVo> issuesList = this.tblZgzzIssuesilistMapper.selectAllListByIssues(issues,null);
        
        if(issuesList == null) {
        	return ResponseFormat.retParam(0, 50007, null);
        }
        List<Object[]> objList = new ArrayList<Object[]>(0);
        Object[] objs = null;
        for (TblZgzzIssuesilistVo vo : issuesList) {
			objs = new Object[13];
			objs[0] = vo.getProjectNo();
			objs[1] = vo.getProjectName();
			objs[2] = vo.getIssuesCode();
			objs[3] = vo.getIssuesName();
			objs[4] = vo.getIssuesType().compareTo(1)==0?"审计":vo.getIssuesType().compareTo(2)==0?"内控":vo.getIssuesType().compareTo(3)==0?"非系统实施":vo.getIssuesType().compareTo(4)==0?"外部审计":"";
			objs[5] = vo.getAuditObjectName();
			objs[6] = vo.getIssuesItem();
			objs[7] = vo.getCreateStaffName();
			objs[8] = vo.getResponsiblePersonName();
			objs[9] = vo.getResponsibleDeptName();
			objs[10] = vo.getIssuesTitle();
			objs[11] = DateUtil.parseDate(vo.getCreateTime(), DateUtil.DATE_SMALL_STR);
			objs[12] = vo.getQuestionMemo();
			objList.add(objs);
		}
        
        //导出设置 excel表头
        String[] titles = {"项目编号", "项目名称 ", "业务编号", "业务名称","业务类别","被审计/评价对象", "事项", "创建人员","责任人","责任部门", " 问题标题","创建时间", "问题详情"};
        response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("整改清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportExcel(titles, objList, response.getOutputStream(), null);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getAfterProjectIssues(String token, TblZgzzIssuesilistVo issues) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        // 通过方案类别与关联项目主键 获取该项目下所有的问题清单
        // 只查询未整改的和未销号的问题
        issues.setStatusStr("0,1,2,3,4,5,6");
        List<TblZgzzIssuesilistVo> issuesList = this.tblZgzzIssuesilistMapper.selectAllListByIssues(issues,null);
        
        return ResponseFormat.retParam(1, 200, issuesList);
	}
	
	@Override
	public JsonBean getRectificationIssuesLedgetList(String token, TblZgzzIssuesilistVo issu) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        PageInfo<TblZgzzIssuesilistVo> pageInfo = new PageInfo<TblZgzzIssuesilistVo>();
        issu.setCreateStaff(loginStaff.getStaffid());
        issu.setDeptIds(loginStaff.getDeptIds());
		pageInfo.setPageSize(issu.getPageSize());
		pageInfo.setCurrentPage(issu.getPageNumber());
		pageInfo.setCondition(issu);
		
		
		com.github.pagehelper.PageInfo<TblZgzzIssuesilistVo> page = PageMethod.startPage(issu.getPageNumber(), issu.getPageSize()).doSelectPageInfo(() -> this.tblZgzzIssuesilistMapper.selectListByPageInfo(issu,loginStaff));
        
		
		List<TblZgzzIssuesilistVo> issuesList = page.getList();
		List<TblRectificationIssuesVo> relaList = new ArrayList<TblRectificationIssuesVo>(0);
		for (TblZgzzIssuesilistVo iss : issuesList) {
			 relaList = this.tblRectificationIssuesMapper.selectListByIssuesId(iss.getIssuesId(),null);
			 iss.setRelaCount(relaList.size());
			 iss.setRelaList(relaList);
		}
		
		pageInfo.setTlist(issuesList);
		pageInfo.setTotalRecord((int)page.getTotal());
		return ResponseFormat.retParam(1,200,pageInfo);
	}

	
	@Override
	public JsonBean exportIssuesLedgetList(String token, TblZgzzIssuesilistVo issu, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		issu.setCreateStaff(loginStaff.getStaffid());
		issu.setDeptIds(loginStaff.getDeptIds());
        List<TblZgzzIssuesilistVo> issuesList = this.tblZgzzIssuesilistMapper.selectAllListByIssues(issu,loginStaff);
        
        if(issuesList == null) {
        	return ResponseFormat.retParam(0, 50007, null);
        }
        
        //导出设置 excel表头
        String[] titles = {"问题编号","问题详情","责任人","责任部门","整改方案","整改措施","成果体现","完成时限","整改状态"};
        int[] cWidths = new int[]{4000,9500, 4000,4000,9500,9500,9500,4000,4000};
        
        List<List<Object>> objLists = new ArrayList<List<Object>>(0);
        List<Object> objList = null;
        List<TblRectificationIssuesVo> relaList = new ArrayList<TblRectificationIssuesVo>(0);
        int i = 0;
        for (TblZgzzIssuesilistVo vo : issuesList) {
        	i = 0;
        	objList = new ArrayList<Object>(0);
        	objList.add(vo.getIssuesCode());
        	objList.add(vo.getQuestionMemo());
        	objList.add(vo.getResponsiblePersonName());
			objList.add(vo.getResponsibleDeptName());
			relaList = this.tblRectificationIssuesMapper.selectListByIssuesId(vo.getIssuesId(),null);
			for (TblRectificationIssuesVo riVo : relaList) {
				if(i > 0) {
					if(i > 1) {
						objList.add(0);
					}else {
						objList.add(relaList.size());
					}
					objLists.add(objList);
					objList = new ArrayList<Object>(0);
		        	objList.add("");
		        	objList.add("");
					objList.add("");
					objList.add("");
				}
				if(riVo.getReimpl() != null){
					objList.add(StringUtils.isNotBlank(riVo.getRectificationPlan())?riVo.getRectificationPlan():"");
					objList.add(StringUtils.isNotBlank(riVo.getReimpl().getRectificationMeasures())?riVo.getReimpl().getRectificationMeasures():"");
					objList.add(StringUtils.isNotBlank(riVo.getReimpl().getAchivement())?riVo.getReimpl().getAchivement():"");
				}else {
					objList.add("");
		        	objList.add("");
					objList.add("");
				}
				objList.add(riVo.getDeadline() != null?DateUtil.parseDate(riVo.getDeadline(),DateUtil.DATE_SMALL_STR):"");
				if(riVo.getValua() != null) {
					objList.add(riVo.getValua().getResultStatusStr(riVo.getValua().getResultStatus()));
				}else {
					objList.add("未整改");
				}
				i++;
			}
			
			if(relaList == null || relaList.size() == 0) {
				objList.add("");
				objList.add("");
				objList.add("");
				objList.add("");
				objList.add("未整改");
			}
			
			if(i > 0) {
				objList.add(0);
			}else {
				objList.add(relaList.size());
			}
			objLists.add(objList);
		}
        
       
        response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("整改清单台账".getBytes(), "iso-8859-1") + ".xls");
		ImportOrExportExcelUtil.exportExcelIssuesLedgetList(titles, objLists, response.getOutputStream(),cWidths, null);
		return ResponseFormat.retParam(1, 200, null);
	}
	
	
	@Override
	public JsonBean issuesRectificationAgain(String token, String issuesId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //查找发起整改的整改清单然
        TblZgzzIssuesilist issues = this.tblZgzzIssuesilistMapper.selectByPrimaryKey(issuesId);
        //修改整改清单状态,为再次发起整改
        issues.setStatus(0);
        this.tblZgzzIssuesilistMapper.updateByPrimaryKeySelective(issues);
        
        //复制发起整改的整改清单 为新的整改清单 并初始化数据信息
        /*issues.setCreateTime(new Date());
        issues.setStatus(0);
        issues.setIssuesVersion(2);
        issues.setIssuesParent(issues.getIssuesId());
        issues.setIssuesId(RandomUtil.uuStringId());
        this.tblZgzzIssuesilistMapper.insertSelective(issues);*/
        return ResponseFormat.retParam(1, 200, issues);
	}

	@Override
	public JsonBean getAuditedObjectList(String token, BigDecimal sheetId, BigDecimal auditOrgId,BigDecimal projectId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
		
        //获取当前底稿下所有的被审计单位
        String orgIds ="";
		String orgName = "";
        if(sheetId!=null) {
        	orgIds = this.tblNbsjSheetMapper.selectOrgIdsById(sheetId);
			orgName = this.tblNbsjSheetMapper.selectOrgIdsByName(sheetId);
        }
        if(projectId!=null) {
        	orgIds = this.tblNbsjSheetMapper.selectOrgIdsByProjectId(projectId);
        }
		List<TblOrganization> orgList = new ArrayList<>();
		if (tblNbsjSheetMapper.selectCountByName(orgName) ==0){
			orgList = this.tblZgzzIssuesilistMapper.selectNoChooseAuditOrg(orgIds,auditOrgId,sheetId);
		}else {
			TblOrganization tblOrganization = new TblOrganization();
			BigDecimal orgid = new BigDecimal(orgIds);
			tblOrganization.setOrgid(orgid);
			tblOrganization.setOrgname(orgName);
			orgList.add(tblOrganization);
		}
        //获取所有未选中的本审计单位
        return ResponseFormat.retParam(1, 200, orgList);
	}

	@Override
	public JsonBean modifyIssuesStatus(String token, String issuesId, Integer status) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblZgzzIssuesilistMapper.updateStatusBySavePlan(issuesId, status);
		return ResponseFormat.retParam(1, 200, null);
	}

	//JsonBean saveIssues
	@Override
	public void resolveSheet(Sheet sheet, String token) throws Exception {
		Row row = null;
		Cell cell = null;
		for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++){
			row = sheet.getRow(i);
			if (row != null) {
				TblZgzzIssuesilist tblZgzzIssuesilist = new TblZgzzIssuesilist();

				TblStaffUtil user = userProvider.get();
				TblStaff tblStaff = new TblStaff();
				if(user != null) {
					tblStaff.setStaffid(user.getStaffid());
					//创建人ID
					tblZgzzIssuesilist.setCreateStaff(tblStaff.getStaffid());
				}
				//创建日期
				tblZgzzIssuesilist.setCreateTime(new Date());

				//问题来源 业务来源
				cell = row.getCell(1);
				if(cell != null){
					cell.setCellType(1);
					String type = cell.getStringCellValue();
					switch (type) {
						case "审计":
							tblZgzzIssuesilist.setIssuesType(1);
							break;
						case "内部":
							tblZgzzIssuesilist.setIssuesType(2);
							break;
						case "非系统实施":
							tblZgzzIssuesilist.setIssuesType(3);
							break;
						case "外部审计":
								tblZgzzIssuesilist.setIssuesType(4);
								break;
						case "内部缺陷":
								tblZgzzIssuesilist.setIssuesType(5);
								break;
						default:
							tblZgzzIssuesilist.setIssuesType(3);

					}
				}else {
					tblZgzzIssuesilist.setIssuesType(3);
				}

				//业务编号
				cell = row.getCell(2);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					tblZgzzIssuesilist.setIssuesCode(cell.getStringCellValue());
				}
				//项目编号
				cell = row.getCell(3);
				String projectNo = "";
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					projectNo =cell.getStringCellValue();
				}
				//项目名称
				cell = row.getCell(4);
				String projectName = "";
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					projectName = cell.getStringCellValue();
				}
				//问题名称
				cell = row.getCell(5);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					tblZgzzIssuesilist.setIssuesName(cell.getStringCellValue());
				}
				//被审计对象类型
				cell = row.getCell(6);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					if (cell.getStringCellValue().equals("公司")){
						tblZgzzIssuesilist.setAuditObjectType(1);
					}else if (cell.getStringCellValue().equals("部门")){
						tblZgzzIssuesilist.setAuditObjectType(2);
					}else if (cell.getStringCellValue().equals("用户")){
						tblZgzzIssuesilist.setAuditObjectType(3);
					}
				}
				//依次获取 被审计对象 的公司 部门 人员信息
				String org = "";
				String dept = "";
				String staff ="";
				cell = row.getCell(7);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					org = cell.getStringCellValue();
				}
				cell = row.getCell(8);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					dept = cell.getStringCellValue();
				}
				cell = row.getCell(9);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					staff = cell.getStringCellValue();
				}
				//获取公司组织ID
				if(org !=""){
					BigDecimal orgID = tblNbsjWbProjectMapper.selectauditOrgId(org);
					tblZgzzIssuesilist.setAuditObjectId(orgID);
					if (tblZgzzIssuesilist.getAuditObjectType() !=1){
						//获取部门组织ID
						BigDecimal deptID = null;
						if(dept!= "" && dept!=null){
							List<TblOrganization> orgids = new ArrayList<TblOrganization>();
							orgids=tblNbsjWbProjectMapper.getOrgidForOrgname1(orgID);
							while (orgids!= null && orgids.size()>0 ){
								for(TblOrganization tblOrganization:orgids){
									if(tblOrganization.getOrgname().equals(dept)){
										deptID=tblOrganization.getOrgid();
										break;
									}
								}
								orgids=tblNbsjWbProjectMapper.getOrgidForOrgname2(orgids);
							}
//							deptID = tblNbsjWbProjectMapper.selectauditDeptId(orgID, dept);
							tblZgzzIssuesilist.setAuditObjectId(deptID);
							if (tblZgzzIssuesilist.getAuditObjectType() ==3 && staff!=""){
									BigDecimal staffID = tblNbsjWbProjectMapper.selectauditStaffId(deptID, staff);
									List<BigDecimal> list = new ArrayList<>();
									list.add(deptID);
									while (staffID == null && list.size()>0){
										staffID = tblNbsjWbProjectMapper.selectauditStaffIds(list, staff);
										list = tblNbsjWbProjectMapper.getOrgidForOrgname(list);
									}
									tblZgzzIssuesilist.setAuditObjectId(staffID);
							}
						} else {
							BigDecimal staffID = tblNbsjWbProjectMapper.selectauditStaffId(orgID, staff);
							tblZgzzIssuesilist.setAuditObjectId(staffID);
						}
					}
				}
				//获取公司名称，确定是哪个公司的部门
				String orgname = "";
				cell = row.getCell(10);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					orgname = cell.getStringCellValue();
				}
				cell = row.getCell(11);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					String deptname = cell.getStringCellValue();
					if (orgname != ""){
						BigDecimal orgID = tblNbsjWbProjectMapper.selectauditOrgId(orgname);
						if (deptname != ""){
							List<TblOrganization> orgids = new ArrayList<TblOrganization>();
							orgids=tblNbsjWbProjectMapper.getOrgidForOrgname1(orgID);
							while (orgids!= null && orgids.size()>0 ){
								for(TblOrganization tblOrganization:orgids){
									if(tblOrganization.getOrgname().equals(deptname)){
										orgID=tblOrganization.getOrgid();
										break;
									}
								}
								orgids=tblNbsjWbProjectMapper.getOrgidForOrgname2(orgids);
							}
//							orgID = tblNbsjWbProjectMapper.selectauditDeptId(orgID, deptname);
						}
						tblZgzzIssuesilist.setResponsibleDept(orgID);
					}
				}
				//获取责任人
				cell = row.getCell(12);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					String staffname = cell.getStringCellValue();
					//如果责任人不为空 根据部门查出对应人员ID
					if (staffname != ""){
						BigDecimal staffID = tblNbsjWbProjectMapper.selectauditStaffId(tblZgzzIssuesilist.getResponsibleDept(), staffname);
						List<BigDecimal> list = new ArrayList<>();
						list.add(tblZgzzIssuesilist.getResponsibleDept());
						while (staffID == null && list.size()>0){
							staffID = tblNbsjWbProjectMapper.selectauditStaffIds(list, staffname);
							list = tblNbsjWbProjectMapper.getOrgidForOrgname(list);
						}
						tblZgzzIssuesilist.setResponsiblePerson(staffID);
					}
				}
				//问题标题
				cell = row.getCell(13);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					tblZgzzIssuesilist.setIssuesTitle(cell.getStringCellValue());
				}
				//问题详情
				cell = row.getCell(14);
				if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
					cell.setCellType(1);
					tblZgzzIssuesilist.setQuestionMemo(cell.getStringCellValue());
				}
				saveIssues(token,tblZgzzIssuesilist,projectName,projectNo,null);
			}
		}
	}

	@Override
	public String resolveSheetBefore(Sheet sheet, String token) throws Exception {
			Row row = null;
			Cell cell = null;
			for (int i = 6; i < sheet.getPhysicalNumberOfRows(); i++){
				row = sheet.getRow(i);
				if (row.getCell(3).getStringCellValue().equals("")){
					break;
				}
				if (row != null) {
					TblBeforeZgzzListEntity tblBeforeZgzzListEntity = new TblBeforeZgzzListEntity();

					TblStaffUtil user = userProvider.get();
					TblStaff tblStaff = new TblStaff();
					BigDecimal orgid = null;
					if(user != null) {
						tblStaff.setStaffid(user.getStaffid());
						orgid = user.getLinkOrg().getOrgid();
					}
					//创建日期
//					tblZgzzIssuesilist.setCreateTime(new Date());
					//序号
					// 获取当前日期
					LocalDate currentDate = LocalDate.now();
					// 获取当前年份（四位数字）
					int currentYear = currentDate.getYear();
					String ZGBG = "ZGBG-"+orgid+"-"+ currentYear+"-%";
					String REZGBG = tblZgzzIssuesilistMapper.getMaxId(ZGBG);
					if (REZGBG == null){
						REZGBG = "ZGBG-"+orgid+"-"+ currentYear+"-0000";
					}

					Integer result= Integer.valueOf(REZGBG.substring(REZGBG.length()-4))+1;
					System.out.println(result);
					ZGBG = "ZGBG-"+orgid+"-"+ currentYear+"-";
					if (result>999){
						ZGBG = ZGBG+result.toString();
					}else if (result>99){
						ZGBG = ZGBG+"0"+result.toString();
					} else if (result>9){
						ZGBG = ZGBG+"00"+result.toString();
					}else if (result>0){
						ZGBG = ZGBG+"000"+result.toString();
					}
					tblBeforeZgzzListEntity.setID(ZGBG);
//					cell = row.getCell(0);
//					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
//						cell.setCellType(1);
//						tblBeforeZgzzListEntity.setID(cell.getStringCellValue());
//					}else {
//						tblBeforeZgzzListEntity.setID(ZGBG);
//					}

					//单位名称——一级单位
					cell = row.getCell(1);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						cell.setCellType(1);
						Integer count = tblZgzzIssuesilistMapper.selectOrgCount(cell.getStringCellValue());
						if (count > 0){
							tblBeforeZgzzListEntity.setFIRSTORG(cell.getStringCellValue());
						}else {
							return "第" + (i+1) + "行一级单位填写有误！";
						}
					}
					//单位名称——具体责任部门
					cell = row.getCell(2);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						cell.setCellType(1);
						Integer count = tblZgzzIssuesilistMapper.selectOrgCount(cell.getStringCellValue());
						if (count > 0){
							tblBeforeZgzzListEntity.setSPECIFICDEPT(cell.getStringCellValue());
						}else {
							return "第" + (i+1) + "行具体责任部门填写有误！";
						}
					}
					//问题来源
					cell = row.getCell(3);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						cell.setCellType(1);
						tblBeforeZgzzListEntity.setSOURCEPROBLEM(cell.getStringCellValue());
					}else {
						return "第" + i + "行问题来源不能为空！";
					}
					//审计报告出具年份
					cell = row.getCell(4);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setISSUANCEYEAR(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行审计报告出具年份不能为空！";
					}
					//问题类别
					cell = row.getCell(5);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						String value = "贯彻国家重大决策部署及国资委监管任务、公司治理（战略执行、改革改制）、财务资金与会计核算、投资管理、采购及销售管理、金融业务、信息化管理、境外国有资产管理、八项规定和廉洁从业、其他";
						if (value.indexOf(cell.getStringCellValue())==-1){
							return "第" + (i+1) + "行问题类别填写有误！";
						}
						tblBeforeZgzzListEntity.setPROBLEMTYPE(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行问题类别不能为空！";
					}
					//一级标题
					cell = row.getCell(6);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setFIRSTTITLE(cell.getStringCellValue());
					}

					//二级标题
					cell = row.getCell(7);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setSECONDTITLE(cell.getStringCellValue());
					}
					//三级标题
					cell = row.getCell(8);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setTHIRDTITLE(cell.getStringCellValue());
					}

					//在审计报告中的表述
					cell = row.getCell(9);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setAUDITREPORTDESC(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行在审计报告中的表述不能为空！";
					}

					//具体责任单位
					cell = row.getCell(10);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						Integer count = tblZgzzIssuesilistMapper.selectOrgCount(cell.getStringCellValue());
						if (count > 0){
							tblBeforeZgzzListEntity.setSPECIFICDEPTLIST(cell.getStringCellValue());
						}else {
							return "第" + (i+1) + "行整改责任清单—具体责任单位填写有误！";
						}
					}

					//具体问题表述
					cell = row.getCell(11);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setPROBLEMDESC(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行具体问题表述不能为空！";
					}
					//问题金额 （万元）
					cell = row.getCell(12);
					if(cell != null){
						if(cell.getCellType() ==0){
							BigDecimal money = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setPROBLEMMONEY(money);
						}else {
							tblBeforeZgzzListEntity.setPROBLEMMONEY(new BigDecimal(cell.getStringCellValue()));
						}
					}else{
						tblBeforeZgzzListEntity.setPROBLEMMONEY(null);
					}
					//负有监督管理责任的主管部门（可以列出多个）
					cell = row.getCell(13);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						String[] split = cell.getStringCellValue().split(",");
						for (int j = 0; j < split.length; j++) {
							Integer count = tblZgzzIssuesilistMapper.selectOrgCount(split[j]);
							if (count > 0){
								continue;
							}else {
								return "第" + (i+1) + "行负有监督管理责任的主管部门部分部门填写有误！";
							}
						}
						tblBeforeZgzzListEntity.setSUPERMANAGDEPT(cell.getStringCellValue());
					}
					//整改类型
					cell = row.getCell(14);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						String value = "立行立改、分阶段整改、持续整改";
						if (value.indexOf(cell.getStringCellValue())==-1){
							return "第" + (i+1) + "行整改类型填写有误！";
						}
						tblBeforeZgzzListEntity.setCORTYPE(cell.getStringCellValue());
					}else{
						return "第" + (i+1) + "行整改类型不能为空！";
					}
					//法规政策依据
					cell = row.getCell(15);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setLAWSPOCOR(cell.getStringCellValue());
					}
					//整改要求
					cell = row.getCell(16);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCORREQUIRE(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行整改要求不能为空！";
					}
					//整改时限
					cell = row.getCell(17);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCORTIME(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行整改时限不能为空！";
					}
					//整改完成标准
					cell = row.getCell(18);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						String value = "落实审计意见和建议、完善制度与优化流程、收回资金或挽回损失、完成追责问责";
						if (value.indexOf(cell.getStringCellValue())==-1){
							return "第" + (i+1) + "行整改标准填写有误！";
						}
						tblBeforeZgzzListEntity.setCORSTANDARD(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行整改标准不能为空！";
					}
					//细化的整改措施
					cell = row.getCell(19);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCORMEASURE(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行细化的整改措施不能为空！";
					}

					//对应的完成时间
					cell = row.getCell(20);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCOMPLETIONTIME(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行对应的完成时间不能为空！";
					}
					//整改第一负责人
					cell = row.getCell(21);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						Integer count = tblZgzzIssuesilistMapper.selectauditStaffId(cell.getStringCellValue());
						if (count > 0){
							tblBeforeZgzzListEntity.setFIRSTPERSON(cell.getStringCellValue());
						}else {
							return "第" + (i+1) + "行整改第一负责人填写有误！";
						}
					}else {
						return "第" + (i+1) + "行整改第一负责人不能为空！";
					}

					//协助整改工作的领导
					cell = row.getCell(22);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						Integer count = tblZgzzIssuesilistMapper.selectauditStaffId(cell.getStringCellValue());
						if (count > 0){
							tblBeforeZgzzListEntity.setASSISTLEADER(cell.getStringCellValue());
						}else {
							return "第" + (i+1) + "行协助整改工作的领导填写有误！";
						}
					}
					//牵头整改部门责任人及联系电话
					cell = row.getCell(23);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setLEADERPERSON(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行牵头整改部门责任人及联系电话不能为空！";
					}
					//配合整改部门责任人及联系电话
					cell = row.getCell(24);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCOOPERSON(cell.getStringCellValue());
					}
					//审计部门责任人及联系电话
					cell = row.getCell(25);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setAUDITPERSON(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行审计部门责任人及联系电话不能为空！";
					}
					//已采取的整改措施
					cell = row.getCell(26);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setTAKEMEASURE(cell.getStringCellValue());
					}else {
						return "第" + (i+1) + "行已采取的整改措施不能为空！";
					}
					//项目数（个）
					cell = row.getCell(27);
					if(cell != null){
						if(cell.getCellType() ==0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setITEMQUANTITY(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setITEMQUANTITY(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setITEMQUANTITY(null);
							}
						}
					}
					//问题整改金额（万元）
					cell = row.getCell(28);
					if(cell != null){
						if(cell.getCellType()==0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setPROCORMONEY(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setPROCORMONEY(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setPROCORMONEY(null);
							}
						}

					}
					if (tblBeforeZgzzListEntity.getPROCORMONEY()!=null){
						//追缴资金（万元）
						cell = row.getCell(29);
						if(cell != null){
							if(cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setRECOMONEY(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setRECOMONEY(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setRECOMONEY(null);
								}
							}

						}
						//归还原渠道（万元）
						cell = row.getCell(30);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setRETURNMONEY(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setRETURNMONEY(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setRETURNMONEY(null);
								}
							}
						}
						//统筹盘活（万元）
						cell = row.getCell(31);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setOVERALLAMOUNT(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setOVERALLAMOUNT(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setOVERALLAMOUNT(null);
								}
							}
						}
						//加快拨付（万元）
						cell = row.getCell(32);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setACCDISBUR(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setACCDISBUR(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setACCDISBUR(null);
								}
							}

						}
						//退抵税费或补缴补发（万元）
						cell = row.getCell(33);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setREFUNDTAXES(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setREFUNDTAXES(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setREFUNDTAXES(null);
								}
							}
						}
						//调整账表（万元）
						cell = row.getCell(34);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setADJUSTSTATEMENT(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setADJUSTSTATEMENT(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setADJUSTSTATEMENT(null);
								}
							}
						}
						//终止或调整金融业务服务（万元）
						cell = row.getCell(35);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setADJUSTMONEY(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setADJUSTMONEY(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setADJUSTMONEY(null);
								}
							}
						}
						//补办手续、重签协议、停止收费等加强管理（万元）
						cell = row.getCell(36);
						if(cell != null){
							if (cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setSUPPAMOUNT(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setSUPPAMOUNT(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setSUPPAMOUNT(null);
								}
							}
						}
						//方式
						cell = row.getCell(37);
						if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
							tblBeforeZgzzListEntity.setWAY(cell.getStringCellValue());
						}
						//金额（万元）
						cell = row.getCell(38);
						if(cell != null){
							if(cell.getCellType()==0){
								BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
								tblBeforeZgzzListEntity.setAMOUNT(itemquantity);
							}else {
								if (!cell.getStringCellValue().equals("")){
									tblBeforeZgzzListEntity.setAMOUNT(new BigDecimal(cell.getStringCellValue()));
								}else {
									tblBeforeZgzzListEntity.setAMOUNT(null);
								}
							}
						}
					}

					//土地、森林等面积（公顷）
					cell = row.getCell(39);
					if(cell != null ){
						if(cell.getCellType() == 0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setLANDAREA(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setLANDAREA(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setLANDAREA(null);
							}
						}
					}
					//矿产资源、产能等（万吨）
					cell = row.getCell(40);
					if(cell != null){
						if(cell.getCellType() == 0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setMINERESOURCE(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setMINERESOURCE(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setMINERESOURCE(null);
							}
						}

					}

					//单位（个）
					cell = row.getCell(41);
					if(cell != null){
						if(cell.getCellType()==0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setUNIT(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setUNIT(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setUNIT(null);
							}
						}
					}

					//家庭（户）
					cell = row.getCell(42);
					if(cell != null){
						if (cell.getCellType()==0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setFAMILY(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setFAMILY(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setFAMILY(null);
							}
						}

					}
					//人数（人）
					cell = row.getCell(43);
					if(cell != null ){
						if (cell.getCellType()==0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setNUMBERPEOPLE(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setNUMBERPEOPLE(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setNUMBERPEOPLE(null);
							}
						}
					}
					//住房
					cell = row.getCell(44);
					if(cell != null){
						if (cell.getCellType()==0){
							BigDecimal itemquantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setHOUSING(itemquantity);
						}else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setHOUSING(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setHOUSING(null);
							}
						}
					}
					//情形
					cell = row.getCell(45);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCLEARFORM(cell.getStringCellValue());
					}
					//人数
					cell = row.getCell(46);
					if(cell != null){
						Integer cellType = cell.getCellType();
						if(cellType == 1){
							tblBeforeZgzzListEntity.setACCNUMBERPEO(cell.getStringCellValue());
						}else {
							BigDecimal numericCellValue = BigDecimal.valueOf(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setACCNUMBERPEO(numericCellValue.toString());
						}
					}
					//数量（个）
					cell = row.getCell(47);
					if(cell != null){
						if(cell.getCellType() ==0){
							BigDecimal accQuantity = new BigDecimal(cell.getNumericCellValue());
							tblBeforeZgzzListEntity.setACCQUANTITY(accQuantity);
                        }else {
							if (!cell.getStringCellValue().equals("")){
								tblBeforeZgzzListEntity.setACCQUANTITY(new BigDecimal(cell.getStringCellValue()));
							}else {
								tblBeforeZgzzListEntity.setACCQUANTITY(null);
							}
							tblBeforeZgzzListEntity.setACCQUANTITY(new BigDecimal(cell.getStringCellValue()));
						}
					}
					//（分修订、制定，文件名称）
					cell = row.getCell(48);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setFILENAME(cell.getStringCellValue());
					}
					//是否已完成整改
					cell = row.getCell(50);
					if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
						tblBeforeZgzzListEntity.setCORSTATUS(cell.getStringCellValue());
					}else{
						return "第" + (i+1) + "行是否已完成整改不能为空！";
					}
					if (cell.getStringCellValue().equals("否")){
						//未整改到位问题原因及下一步计划
						cell = row.getCell(49);
						if(cell != null  && StringUtils.isNotBlank(cell.getStringCellValue())){
							tblBeforeZgzzListEntity.setNOTRECTREASON(cell.getStringCellValue());
						}else {
							return "第" + (i+1) + "行未整改到位问题原因及下一步计划需要填写！";
						}
					}
					tblZgzzIssuesilistMapper.saveBeforeZgzzList(tblBeforeZgzzListEntity);
			}
		}
		return "完成";
	}

	@Override
	public JsonBean getbeforeZgzzList(String token, TblBeforeZgzzListEntity tblBeforeZgzzListEntity, Integer pageNum, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		//分页查询审计问题
		PageInfo<TblBeforeZgzzListEntity> pageInfo = new PageInfo<TblBeforeZgzzListEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNum);

		com.github.pagehelper.PageInfo<TblBeforeZgzzListEntity> page = PageMethod.startPage(pageNum, pageSize).doSelectPageInfo(() -> this.tblZgzzIssuesilistMapper.getBeforeZgzzList(tblBeforeZgzzListEntity));

		pageInfo.setTlist(page.getList());
		pageInfo.setTotalRecord((int)page.getTotal());

		return ResponseFormat.retParam(1,200,pageInfo);
	}

	@Override
	public JsonBean deleteBeforeList(String token, String ids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		String[] split = ids.split(",");
		for (String id : split) {
			this.tblZgzzIssuesilistMapper.deleteBeforeList(id);
		}
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean exportBeforeIssuesList(TblStaffUtil loginStaff, TblBeforeZgzzListEntity tblBeforeZgzzListEntity, HttpServletResponse response)
			throws Exception {
		
		List<TblBeforeZgzzListEntity> beforeList = this.tblZgzzIssuesilistMapper.getBeforeZgzzList(tblBeforeZgzzListEntity);
		
	    if(beforeList == null) {
	        return ResponseFormat.retParam(0, 50007, null);
	    }
	    
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		String titleone = "往期问题清单";
		String titletwo = "截至"+DateUtil.parseDate(new Date(), "yyyy年MM月dd日");
		String[] titlethree = {"序号", "单位名称 ","问题来源","审计报告出具年份","问题类别","问题在审计报告中的序号及表述","整改责任清单","是否已整改完成"};
		String[] titlefour = {"一级单位", "具体责任单位 ","一级标题","二级标题","三级标题",  "在审计报告中的表述","具体责任单位","具体问题表述","问题金额（万元）","负责监督管理责任的主管部门"};
		Integer no = 1;
		for (TblBeforeZgzzListEntity vo : beforeList) {
			objs = new Object[15];
			objs[0] = no;
			objs[1] = StringUtils.isBlank(vo.getFIRSTORG())?"":vo.getFIRSTORG();
			objs[2] = StringUtils.isBlank(vo.getSPECIFICDEPT())?"":vo.getSPECIFICDEPT();
			objs[3] = StringUtils.isBlank(vo.getSOURCEPROBLEM())?"":vo.getSOURCEPROBLEM();
			objs[4] = StringUtils.isBlank(vo.getISSUANCEYEAR())?"":vo.getISSUANCEYEAR();
			objs[5] = StringUtils.isBlank(vo.getPROBLEMTYPE())?"":vo.getPROBLEMTYPE();
			objs[6] = StringUtils.isBlank(vo.getFIRSTTITLE())?"":vo.getFIRSTTITLE();
			objs[7] = StringUtils.isBlank(vo.getSECONDTITLE())?"":vo.getSECONDTITLE();
			objs[8] = StringUtils.isBlank(vo.getTHIRDTITLE())?"":vo.getTHIRDTITLE();
			objs[9] = StringUtils.isBlank(vo.getAUDITREPORTDESC())?"":vo.getAUDITREPORTDESC();
			objs[10] = StringUtils.isBlank(vo.getSPECIFICDEPTLIST())?"":vo.getSPECIFICDEPTLIST();
			objs[11] = StringUtils.isBlank(vo.getPROBLEMDESC())?"":vo.getPROBLEMDESC();
			objs[12] = vo.getPROBLEMMONEY()==null?"":vo.getPROBLEMMONEY();
			objs[13] = StringUtils.isBlank(vo.getSUPERMANAGDEPT())?"":vo.getSUPERMANAGDEPT();
			objs[14] = StringUtils.isBlank(vo.getCORSTATUS())?"":vo.getCORSTATUS();
			objList.add(objs);
			no++;
		}
		//导出设置 excel表头
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("往期问题清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportZglsZgqdExcel(titleone,titletwo,titlethree,titlefour, objList, response.getOutputStream(), null);
		return ResponseFormat.retParam(1, 200, null);
	}

	
	@Override
	public JsonBean exportBeforeResponseList(TblStaffUtil loginStaff, HttpServletResponse response,
			TblBeforeZgzzListEntity tblBeforeZgzzListEntity) throws Exception {
		
		List<TblBeforeZgzzListEntity> beforeList = this.tblZgzzIssuesilistMapper.getBeforeZgzzList(tblBeforeZgzzListEntity);
	    if(beforeList == null) {
	        return ResponseFormat.retParam(0, 50007, null);
	    }
		
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		String titleone = "往期责任清单";
		String titletwo = "截至"+DateUtil.parseDate(new Date(), "yyyy年MM月dd日");
		String[] titlethree = {"序号", "单位名称 ","问题来源","审计报告出具年份","问题类别","问题在审计报告中的序号及表述","整改责任清单","整改目标清单","具体责任单位细化的整改要求","具体责任单位整改责任人","是否已完成整改"};
		String[] titlefour = {"一级单位", "具体责任单位 ","具体责任单位","具体问题表述","问题金额（万元）","负责监督管理责任的主管部门","整改类型","法规政策依据","整改要求","整改时限","整改完成标准","细化的整改措施","对应的完成时间","整改第一责任人","协助整改工作的领导","牵头整改部门责任人及联系电话","配合整改部门责任人及联系电话","审计部门责任人及联系电话"};
		
		Integer no = 1;
		for (TblBeforeZgzzListEntity vo : beforeList) {
			objs = new Object[24];
			objs[0] = no;
			objs[1] = StringUtils.isBlank(vo.getFIRSTORG())?"":vo.getFIRSTORG();
			objs[2] = StringUtils.isBlank(vo.getSPECIFICDEPT())?"":vo.getSPECIFICDEPT();
			objs[3] = StringUtils.isBlank(vo.getSOURCEPROBLEM())?"":vo.getSOURCEPROBLEM();
			objs[4] = StringUtils.isBlank(vo.getISSUANCEYEAR())?"":vo.getISSUANCEYEAR();
			objs[5] = StringUtils.isBlank(vo.getPROBLEMTYPE())?"":vo.getPROBLEMTYPE();
			objs[6] = StringUtils.isBlank(vo.getAUDITREPORTDESC())?"":vo.getAUDITREPORTDESC();
			objs[7] = StringUtils.isBlank(vo.getSPECIFICDEPTLIST())?"":vo.getSPECIFICDEPTLIST();
			objs[8] = StringUtils.isBlank(vo.getPROBLEMDESC())?"":vo.getPROBLEMDESC();
			objs[9] = vo.getPROBLEMMONEY()==null?"":vo.getPROBLEMMONEY();
			objs[10] = StringUtils.isBlank(vo.getSUPERMANAGDEPT())?"":vo.getSUPERMANAGDEPT();
			objs[11] = StringUtils.isBlank(vo.getCORTYPE())?"":vo.getCORTYPE();
			objs[12] = StringUtils.isBlank(vo.getLAWSPOCOR())?"":vo.getLAWSPOCOR();
			objs[13] = StringUtils.isBlank(vo.getCORREQUIRE())?"":vo.getCORREQUIRE();
			objs[14] = StringUtils.isBlank(vo.getCORTIME())?"":vo.getCORTIME();
			objs[15] = StringUtils.isBlank(vo.getCORSTANDARD())?"":vo.getCORSTANDARD();
			objs[16] = StringUtils.isBlank(vo.getCORMEASURE())?"":vo.getCORMEASURE();
			objs[17] = StringUtils.isBlank(vo.getCOMPLETIONTIME())?"":vo.getCOMPLETIONTIME();
			objs[18] = StringUtils.isBlank(vo.getFIRSTPERSON())?"":vo.getFIRSTPERSON();
			objs[19] = StringUtils.isBlank(vo.getASSISTLEADER())?"":vo.getASSISTLEADER();
			objs[20] = StringUtils.isBlank(vo.getLEADERPERSON())?"":vo.getLEADERPERSON();
			objs[21] = StringUtils.isBlank(vo.getCOOPERSON())?"":vo.getCOOPERSON();
			objs[22] = StringUtils.isBlank(vo.getAUDITPERSON())?"":vo.getAUDITPERSON();
			objs[23] = StringUtils.isBlank(vo.getCORSTATUS())?"":vo.getCORSTATUS();
			objList.add(objs);
			no++;
		}
		//导出设置 excel表头
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("往期责任清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportZglsZerqdExcel(titleone,titletwo,titlethree,titlefour, objList, response.getOutputStream(), null);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean exportBeforeZgList(TblStaffUtil loginStaff, TblBeforeZgzzListEntity tblBeforeZgzzListEntity,
			HttpServletResponse response) throws Exception {
		List<TblBeforeZgzzListEntity> beforeList = this.tblZgzzIssuesilistMapper.getBeforeZgzzList(tblBeforeZgzzListEntity);
	    if(beforeList == null) {
	        return ResponseFormat.retParam(0, 50007, null);
	    }
	    
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		String titleone = "整改清单";
		String titletwo = "截至"+DateUtil.parseDate(new Date(), "yyyy年MM月dd日");
		String[] titlethree = {"序号", "单位名称 ","问题来源","审计报告出具年份","问题类别","问题在审计报告中的序号及表述","整改责任清单","整改目标清单","具体责任单位细化的整改要求","具体责任单位整改责任人","具体责任单位整改情况"};
		String[] titlefour = {"一级单位", "具体责任单位 ","一级标题","二级标题","三级标题",  "在审计报告中的表述","具体责任单位","具体问题表述","问题金额（万元）","负责监督管理责任的主管部门","整改类型","法规政策依据","整改要求","整改时限","整改完成标准","细化的整改措施","对应的完成时间","整改第一责任人","协助整改工作的领导","牵头整改部门责任人及联系电话","配合整改部门责任人及联系电话","审计部门责任人及联系电话","已采取的整改措施","项目数(个)","问题整改金额（万元）","其中","其他","土地、森林等面积（公顷）","矿产资源、产能等（万吨）","单位（个）","家庭（户）","人数（人）","住房（套）","追责问题情况","完善制度情况","未整改到位问题原因及下一步计划","是否已完成整改","是否销号"};
		String[] titlefive = {"追缴资金（万元）", "归还原渠道（万元） ","统筹盘活（万元）","加快拨付（万元）","退抵税费或补缴补发（万元）","调整账表（万元）","终止或调整金融业务服务(万元)","补办手续、重签协议、停止收费等加强管理（万元）",  "方式","金额（万元）","情形","人数","数量（个）","（分修订、制定，文件名称）"};
		Integer no = 1;
		for (TblBeforeZgzzListEntity vo : beforeList) {
			objs = new Object[52];
			objs[0] = no;
			objs[1] = StringUtils.isBlank(vo.getFIRSTORG())?"":vo.getFIRSTORG();
			objs[2] = StringUtils.isBlank(vo.getSPECIFICDEPT())?"":vo.getSPECIFICDEPT();
			objs[3] = StringUtils.isBlank(vo.getSOURCEPROBLEM())?"":vo.getSOURCEPROBLEM();
			objs[4] = StringUtils.isBlank(vo.getISSUANCEYEAR())?"":vo.getISSUANCEYEAR();
			objs[5] = StringUtils.isBlank(vo.getPROBLEMTYPE())?"":vo.getPROBLEMTYPE();
			objs[6] = StringUtils.isBlank(vo.getFIRSTTITLE())?"":vo.getFIRSTTITLE();
			objs[7] = StringUtils.isBlank(vo.getSECONDTITLE())?"":vo.getSECONDTITLE();
			objs[8] = StringUtils.isBlank(vo.getTHIRDTITLE())?"":vo.getTHIRDTITLE();
			objs[9] = StringUtils.isBlank(vo.getAUDITREPORTDESC())?"":vo.getAUDITREPORTDESC();
			objs[10] = StringUtils.isBlank(vo.getSPECIFICDEPTLIST())?"":vo.getSPECIFICDEPTLIST();
			objs[11] = StringUtils.isBlank(vo.getPROBLEMDESC())?"":vo.getPROBLEMDESC();
			objs[12] = vo.getPROBLEMMONEY()==null?"":vo.getPROBLEMMONEY();
			objs[13] = StringUtils.isBlank(vo.getSUPERMANAGDEPT())?"":vo.getSUPERMANAGDEPT();
			objs[14] = StringUtils.isBlank(vo.getCORTYPE())?"":vo.getCORTYPE();
			objs[15] = StringUtils.isBlank(vo.getLAWSPOCOR())?"":vo.getLAWSPOCOR();
			objs[16] = StringUtils.isBlank(vo.getCORREQUIRE())?"":vo.getCORREQUIRE();
			objs[17] = StringUtils.isBlank(vo.getCORTIME())?"":vo.getCORTIME();
			objs[18] = StringUtils.isBlank(vo.getCORSTANDARD())?"":vo.getCORSTANDARD();
			objs[19] = StringUtils.isBlank(vo.getCORMEASURE())?"":vo.getCORMEASURE();
			objs[20] = StringUtils.isBlank(vo.getCOMPLETIONTIME())?"":vo.getCOMPLETIONTIME();
			objs[21] = StringUtils.isBlank(vo.getFIRSTPERSON())?"":vo.getFIRSTPERSON();
			objs[22] = StringUtils.isBlank(vo.getASSISTLEADER())?"":vo.getASSISTLEADER();
			objs[23] = StringUtils.isBlank(vo.getLEADERPERSON())?"":vo.getLEADERPERSON();
			objs[24] = StringUtils.isBlank(vo.getCOOPERSON())?"":vo.getCOOPERSON();
			objs[25] = StringUtils.isBlank(vo.getAUDITPERSON())?"":vo.getAUDITPERSON();
			objs[26] = StringUtils.isBlank(vo.getTAKEMEASURE())?"":vo.getTAKEMEASURE();
			objs[27] = vo.getITEMQUANTITY()==null?"":vo.getITEMQUANTITY();
			objs[28] = vo.getPROCORMONEY()==null?"":vo.getPROCORMONEY();
			objs[29] = vo.getRECOMONEY()==null?"":vo.getRECOMONEY();
			objs[30] = vo.getRETURNMONEY()==null?"":vo.getRETURNMONEY();
			objs[31] = vo.getOVERALLAMOUNT()==null?"":vo.getOVERALLAMOUNT();
			objs[32] = vo.getACCDISBUR()==null?"":vo.getACCDISBUR();
			objs[33] = vo.getREFUNDTAXES()==null?"":vo.getREFUNDTAXES();
			objs[34] = vo.getADJUSTSTATEMENT()==null?"":vo.getADJUSTSTATEMENT();
			objs[35] = vo.getADJUSTMONEY()==null?"":vo.getADJUSTMONEY();
			objs[36] = vo.getSUPPAMOUNT()==null?"":vo.getSUPPAMOUNT();
			objs[37] = StringUtils.isBlank(vo.getWAY())?"":vo.getWAY();
			objs[38] = vo.getAMOUNT()==null?"":vo.getAMOUNT();
			objs[39] = vo.getLANDAREA()==null?"":vo.getLANDAREA();
			objs[40] = vo.getMINERESOURCE()==null?"":vo.getMINERESOURCE();
			objs[41] = vo.getUNIT()==null?"":vo.getUNIT();
			objs[42] = vo.getFAMILY()==null?"":vo.getFAMILY();
			objs[43] = vo.getNUMBERPEOPLE()==null?"":vo.getNUMBERPEOPLE();
			objs[44] = vo.getHOUSING()==null?"":vo.getHOUSING();
			objs[45] = StringUtils.isBlank(vo.getCLEARFORM())?"":vo.getCLEARFORM();
			objs[46] = StringUtils.isBlank(vo.getACCNUMBERPEO())?"":vo.getACCNUMBERPEO();
			objs[47] = vo.getACCQUANTITY()==null?"":vo.getACCQUANTITY();
			objs[48] = StringUtils.isBlank(vo.getFILENAME())?"":vo.getFILENAME();
			objs[49] = StringUtils.isBlank(vo.getNOTRECTREASON())?"":vo.getNOTRECTREASON();
			objs[50] = StringUtils.isBlank(vo.getCORSTATUS())?"":vo.getCORSTATUS();
			objs[51] = "";
			
			objList.add(objs);
			no++;
		}
		//导出设置 excel表头
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("整改清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportZglsZgqdExcel(titleone,titletwo,titlethree,titlefour,titlefive, objList, response.getOutputStream(), null);
		
		return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public JsonBean importdealSendDoubtful(String token, Pamas pamas, HttpServletResponse response) throws Exception {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			//获取疑点编号
			String selectIds = pamas.getSelectIds();
			List<TblNbsjDoubtfulpointEntity> list = this.tblAttachmentMapper.exclTblDoubtfulpointByids(selectIds);
			if(list == null) {
				return ResponseFormat.retParam(0, 50007, null);
			}

			List<Object[]> objList = new ArrayList<Object[]>(0);
			Object[] objs = null;
			for (TblNbsjDoubtfulpointEntity vo : list) {
				objs = new Object[7];
				objs[0] = vo.getDpnumber();
				objs[1] = vo.getDpname();
				objs[2] = vo.getEditor();

				Date editTime = vo.getEdittime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String formattedTime = sdf.format(editTime);

				objs[3] = formattedTime;
				objs[4] = vo.getDpdescribe();
				objs[5] = vo.getTestresult();
				objs[6] = vo.getMemo();
				objList.add(objs);
			}

			//导出设置 excel表头
			String[] titles = new String[]{"疑点编号","疑点名称","编制人","编制时间","疑点描述","测试结果","备注"};
			response.setContentType("application/binary;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; filename=" + new String("疑点管理".getBytes(), "iso-8859-1") + ".xlsx");
			ImportOrExportExcelUtil.exportExcel(titles, objList, response.getOutputStream(), null);
			return ResponseFormat.retParam(1, 200, null);
	}


}
