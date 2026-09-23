package com.huabo.central.enterprises.audit.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectAppraising;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclare;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectDeclareGroup;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNotice;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNoticeExt;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectQuality;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessment;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectDeclareMapper;
import com.huabo.central.enterprises.audit.service.CeaProjectNoticeService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectAppraisingQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareGroupQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectDeclareSortQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeExtQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQualityQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectQueryVoParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UpdateTblCeaProjectDeclareSortParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.TblCeaProjectVoResult;
import com.huabo.central.enterprises.audit.vo.result.UserInfo;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="项目评优-通知",description="项目评优-通知")
@RequestMapping(value = "/api-auth/project/notice")
@Slf4j
public class CeaProjectNoticeController {

	@Resource
	private CeaProjectNoticeService ceaProjectNoticeService;
	
	@Resource
	private TblCeaProjectDeclareMapper tblCeaProjectDeclareMapper;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "项目评优-通知 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaProjectNotice> getTblCeaProjectNoticeList(@RequestBody TblCeaProjectNoticeQueryParam param) {
		MyJsonBean<TblCeaProjectNotice> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectNoticeList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-通知 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaProjectNotice> saveOrUpdateTblCeaProjectNoticeService(@RequestBody @Validated TblCeaProjectNotice param) {
		MyJsonBean<TblCeaProjectNotice> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.saveOrUpdateTblCeaProjectNoticeService(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-通知 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaProjectNoticeService(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.deleteTblCeaProjectNoticeService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-通知 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaProjectNotice>> getTblCeaProjectNoticeService(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaProjectNotice>> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectNoticeService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-通知-首页展示 列表查询")
	@PostMapping("/ext/home/getList")
	public MyJsonBean<TblCeaProjectNoticeExt> getTblCeaProjectNoticeExtHomeList(@RequestBody TblCeaProjectNoticeExtQueryParam param) {
		MyJsonBean<TblCeaProjectNoticeExt> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectNoticeExtHomeList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知-首页展示 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-通知-首页展示-同意")
	@PostMapping("/ext/home/submit/{id}")
	public MyJsonBean<TblCeaProjectNoticeExt> submitTblCeaProjectNoticeExtHomeList(@PathVariable Long id) {
		MyJsonBean<TblCeaProjectNoticeExt> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.submitTblCeaProjectNoticeExtHomeList(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知-首页展示-同意 ...接口 异常", e);
		}
		return myJsonBean;
	}


	@Operation(summary = "项目评优-通知-下发 列表查询")
	@GetMapping("/ext/getList/{projectNoticeId}")
	public MyJsonBean<List<TblCeaProjectNoticeExt>> getTblCeaProjectNoticeExtList(@PathVariable Long projectNoticeId) {
		MyJsonBean<List<TblCeaProjectNoticeExt>> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectNoticeExtList(projectNoticeId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知-下发 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-通知-下发-批量新增")
	@PostMapping("/ext/updates/{projectNoticeId}")
	public MyJsonBean<Void> updatesTblCeaProjectNoticeExt(@PathVariable Long projectNoticeId, @RequestBody List<TblCeaProjectNoticeExt> param) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.updatesTblCeaProjectNoticeExt(projectNoticeId, param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-通知-下发-批量新增 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报 列表查询")
	@PostMapping("/declare/getList")
	public MyJsonBean<TblCeaProjectDeclare> getTblCeaProjectDeclareList(@RequestBody TblCeaProjectDeclareQueryParam param) {
		MyJsonBean<TblCeaProjectDeclare> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}
	
	@Operation(summary = "项目申报汇总-导出")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody TblCeaProjectDeclareQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareList(param);
		PageResult<TblCeaProjectDeclare> result = (PageResult<TblCeaProjectDeclare>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean ceaAssetMgtList = ceaProjectNoticeService.getTblCeaProjectDeclareList(param);
			PageResult<TblCeaProjectDeclare> tempList = (PageResult<TblCeaProjectDeclare>) ceaAssetMgtList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		
		for (int i = 0; i < result.getTlist().size(); i++) {
			//参审人员
			TblCeaProjectDeclare dataCell = result.getTlist().get(i);
			List<UserInfo> listCsrz = dataCell.getImplementationPlanReviewers();
		    if(null!=listCsrz) {
		    	String csrz = "";
		    	for (int j = 0; j < listCsrz.size(); j++) {
		    		csrz = csrz+listCsrz.get(j).getRealName();
		    		if(listCsrz.size()!=(j+1)) {
		    			csrz = csrz+"，";
		    		}
				}
		    	dataCell.setCsry(csrz);
		    }
		    //核减金额
		    Integer hjje = tblCeaProjectDeclareMapper.selectSumHjjeByProjectid(dataCell.getImplementationPlanId());
		    dataCell.setHjje(hjje);
//		    //项目类型
//		    String projectType = dataCell.getImplementationProjectType();
//		    if(null!=projectType && "1".equals(projectType)) {
//		    	dataCell.setImplementationProjectType("计划内");
//		    }
//		    if(null!=projectType && "2".equals(projectType)) {
//		    	dataCell.setImplementationProjectType("计划外");
//		    }
		}
		
		// 生成excel下载
		String filename = System.currentTimeMillis() + "项目申报汇总.xlsx";
		try (ExcelExport export = new ExcelExport(TblCeaProjectDeclare.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
	

	@Operation(summary = "项目评优-申报 新增/更新")
	@PostMapping("/declare/saveOrUpdate")
	public MyJsonBean<TblCeaProjectDeclare> saveOrUpdateTblCeaProjectDeclareService(@RequestBody @Validated TblCeaProjectDeclare param) {
		MyJsonBean<TblCeaProjectDeclare> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.saveOrUpdateTblCeaProjectDeclareService(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报 刪除")
	@DeleteMapping("/declare/{id}")
	public MyJsonBean<Void> deleteTblCeaProjectDeclareService(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.deleteTblCeaProjectDeclareService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报 详情 查询")
	@GetMapping("/declare/{id}")
	public MyJsonBean<FileVo<TblCeaProjectDeclare>> getTblCeaProjectDeclareService(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaProjectDeclare>> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
	
	@Operation(summary = "项目评优申报-导出word")
	@GetMapping("/declare/exportword")
	public void ceaProjectDeclareExportword(@PathVariable Long id) {
		try {
//			
//			https://blog.csdn.net/a35336000/article/details/140013466
//			
//			// 创建一个空的Word文档
//	        XWPFDocument doc = new XWPFDocument();
//	 
//	        // 创建一个段落
//	        XWPFParagraph p1 = doc.createParagraph();
//	        XWPFRun r1 = p1.createRun();
//	        r1.setText("优秀审计项目申报表");
//	 
//	        // 写入文件
//	        try (FileOutputStream out = new FileOutputStream("项目评优申报.docx")) {
//	            doc.write(out);
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        }
//	 
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优申报-导出  ...接口 异常", e);
		}
	}
	
	

	@Operation(summary = "项目评优-评优 列表查询")
	@PostMapping("/appraising/getList")
	public MyJsonBean<TblCeaProjectAppraising> getTblCeaProjectAppraisingList(@RequestBody TblCeaProjectAppraisingQueryParam param) {
		MyJsonBean<TblCeaProjectAppraising> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectAppraisingList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-评优 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-评优 新增/更新")
	@PostMapping("/appraising/saveOrUpdate")
	public MyJsonBean<TblCeaProjectAppraising> saveOrUpdateTblCeaProjectAppraisingService(@RequestBody @Validated TblCeaProjectAppraising param) {
		MyJsonBean<TblCeaProjectAppraising> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.saveOrUpdateTblCeaProjectAppraisingService(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-评优 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-评优 刪除")
	@DeleteMapping("/appraising/{id}")
	public MyJsonBean<Void> deleteTblCeaProjectAppraisingService(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.deleteTblCeaProjectAppraisingService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-评优 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-评优 详情 查询")
	@GetMapping("/appraising/{id}")
	public MyJsonBean<FileVo<TblCeaProjectAppraising>> getTblCeaProjectAppraisingService(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaProjectAppraising>> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectAppraisingService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-评优 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-质量 列表查询")
	@PostMapping("/quality/getList")
	public MyJsonBean<TblCeaProjectQuality> getTblCeaProjectQualityList(@RequestBody TblCeaProjectQualityQueryParam param) {
		MyJsonBean<TblCeaProjectQuality> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectQualityList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-质量 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-质量 新增/更新")
	@PostMapping("/quality/saveOrUpdate")
	public MyJsonBean<TblCeaProjectQuality> saveOrUpdateTblCeaProjectQuality(@RequestBody @Validated TblCeaProjectQuality param) {
		MyJsonBean<TblCeaProjectQuality> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.saveOrUpdateTblCeaProjectQuality(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-质量 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-质量 刪除")
	@DeleteMapping("/quality/{id}")
	public MyJsonBean<Void> deleteTblCeaProjectQuality(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.deleteTblCeaProjectQuality(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-质量 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-质量 详情 查询")
	@GetMapping("/quality/{id}")
	public MyJsonBean<TblCeaProjectQuality> getTblCeaProjectQuality(@PathVariable Long id) {
		MyJsonBean<TblCeaProjectQuality> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectQuality(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-质量 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目管理-实施方案 列表查询")
	@PostMapping("/quality/ext/getList")
	public MyJsonBean<TblCeaProjectVoResult> getTblCeaProjectList(@RequestBody TblCeaProjectQueryVoParam param) {
		MyJsonBean<TblCeaProjectVoResult> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-实施方案 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报-分组 列表查询")
	@PostMapping("/declare/group/getList")
	public MyJsonBean<TblCeaProjectDeclareGroup> getTblCeaProjectDeclareGroupList(@RequestBody TblCeaProjectDeclareGroupQueryParam param) {
		MyJsonBean<TblCeaProjectDeclareGroup> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareGroupList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报-分组 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报-分组 新增/更新")
	@PostMapping("/declare/group/saveOrUpdate")
	public MyJsonBean<TblCeaProjectDeclareGroup> saveOrUpdateTblCeaProjectDeclareGroupService(
			@RequestBody @Validated TblCeaProjectDeclareGroup param) {
		MyJsonBean<TblCeaProjectDeclareGroup> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.saveOrUpdateTblCeaProjectDeclareGroupService(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报-分组 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报-分组 刪除")
	@DeleteMapping("/declare/group/{id}")
	public MyJsonBean<Void> deleteTblCeaProjectDeclareGroupService(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.deleteTblCeaProjectDeclareGroupService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报-分组 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报-分组 详情 查询")
	@GetMapping("/declare/group/{id}")
	public MyJsonBean<FileVo<TblCeaProjectDeclareGroup>> getTblCeaProjectDeclareGroupService(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaProjectDeclareGroup>> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareGroupService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报-分组 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优-申报-分组 是否可以审批判断")
	@GetMapping("/declare/group/is-approval/{id}")
	public MyJsonBean<Boolean> getTblCeaProjectDeclareGroupIsApproval(@PathVariable Long id) {
		MyJsonBean<Boolean> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareGroupIsApproval(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优-申报-分组 是否可以审批判断  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "项目评优排序 列表查询")
	@PostMapping("/declare/sort/getList")
	public MyJsonBean<TblCeaProjectDeclare> getTblCeaProjectDeclareSortList(@RequestBody TblCeaProjectDeclareSortQueryParam param) {
		MyJsonBean<TblCeaProjectDeclare> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaProjectDeclareSortList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优排序 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}


	@Operation(summary = "项目评优排序-更新")
	@PostMapping("/declare/sort/update")
	public MyJsonBean<Void> updateTblCeaProjectDeclareSort(@RequestBody @Validated UpdateTblCeaProjectDeclareSortParam param) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.updateTblCeaProjectDeclareSort(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("项目评优排序-更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "审计工质量评估 列表查询")
	@PostMapping("/quality/assessment/getList")
	public MyJsonBean<TblCeaQualityAssessment> getTblCeaQualityAssessmentList(@RequestBody TblCeaQualityAssessmentQueryParam param) {
		MyJsonBean<TblCeaQualityAssessment> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaQualityAssessmentList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计工质量评估 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "审计工质量评估 新增/更新")
	@PostMapping("/quality/assessment/saveOrUpdate")
	public MyJsonBean<TblCeaQualityAssessment> saveOrUpdateTblCeaQualityAssessment(@RequestBody @Validated TblCeaQualityAssessment param) {
		MyJsonBean<TblCeaQualityAssessment> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.saveOrUpdateTblCeaQualityAssessment(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计工质量评估 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "审计工质量评估 刪除")
	@DeleteMapping("/quality/assessment/{id}")
	public MyJsonBean<Void> deleteTblCeaQualityAssessment(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.deleteTblCeaQualityAssessment(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计工质量评估 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "审计工质量评估 详情 查询")
	@GetMapping("/quality/assessment/{id}")
	public MyJsonBean<TblCeaQualityAssessment> getTblCeaQualityAssessmentService(@PathVariable Long id) {
		MyJsonBean<TblCeaQualityAssessment> myJsonBean = null;
		try {
			myJsonBean = ceaProjectNoticeService.getTblCeaQualityAssessmentService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审计工质量评估 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
