package com.huabo.legal.exam.modules.paper.controller;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exam.core.api.ApiRest;
import com.huabo.legal.exam.core.api.controller.BaseController;
import com.huabo.legal.exam.core.api.dto.BaseIdReqDTO;
import com.huabo.legal.exam.core.api.dto.BaseIdRespDTO;
import com.huabo.legal.exam.core.api.dto.BaseIdsReqDTO;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.core.utils.BeanMapper;
import com.huabo.legal.exam.modules.paper.dto.PaperDTO;
import com.huabo.legal.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import com.huabo.legal.exam.modules.paper.dto.request.PaperAnswerDTO;
import com.huabo.legal.exam.modules.paper.dto.request.PaperCreateReqDTO;
import com.huabo.legal.exam.modules.paper.dto.request.PaperListReqDTO;
import com.huabo.legal.exam.modules.paper.dto.request.PaperQuQueryDTO;
import com.huabo.legal.exam.modules.paper.dto.response.ExamDetailRespDTO;
import com.huabo.legal.exam.modules.paper.dto.response.ExamResultRespDTO;
import com.huabo.legal.exam.modules.paper.dto.response.PaperListRespDTO;
import com.huabo.legal.exam.modules.paper.entity.Paper;
import com.huabo.legal.exam.modules.paper.service.PaperService;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 试卷控制器
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-05-25 16:33
 */
@Tag(name="试卷",description="试卷")
@RestController
@RequestMapping("/exam/api/paper/paper")
public class PaperController extends BaseController {

	@Autowired
	private PaperService baseService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Resource
	private UserProvider userProvider;

	/**
	 * 添加或修改
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "添加或修改")
	@RequestMapping(value = "/save", method = {RequestMethod.POST})
	public ApiRest<BaseIdRespDTO> save(@RequestBody PaperDTO reqDTO) {
		//复制参数
		Paper entity = new Paper();
		BeanMapper.copy(reqDTO, entity);
		baseService.saveOrUpdate(entity);
		return super.success(new BaseIdRespDTO(entity.getId()));
	}

	/**
	 * 批量删除
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "批量删除")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
		//根据ID删除
		baseService.removeByIds(reqDTO.getIds());
		return super.success();
	}

	/**
	 * 查找详情
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "查找详情")
	@RequestMapping(value = "/detail", method = {RequestMethod.POST})
	public ApiRest<PaperDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
		Paper entity = baseService.getById(reqDTO.getId());
		PaperDTO dto = new PaperDTO();
		BeanUtils.copyProperties(entity, dto);
		return super.success(dto);
	}


	/**
	 * 分页查找
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "分页查找")
	@RequestMapping(value = "/paging", method = {RequestMethod.POST})
	public ApiRest<PageResult<PaperListRespDTO>> paging(@RequestBody PagingReqDTO<PaperListReqDTO> reqDTO) {
		return super.success(baseService.paging(reqDTO));
	}


	/**
	 * 创建试卷
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "创建试卷")
	@RequestMapping(value = "/create-paper", method = {RequestMethod.POST})
	public JsonBean save(@RequestHeader("token") String token, @RequestBody PaperCreateReqDTO reqDTO) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null || loginStaff.getLinkOrg() == null || loginStaff.getLinkOrg().getOrgid() == null) {
			throw new ServiceException(401, 20006);
		}
//		int staffId = Integer.parseInt(loginStaff.getStaffid().toString());
//		int orgid = Integer.parseInt(loginStaff.getLinkOrg().getOrgid().toString());
//		StaffResult userInfoExam = tblStaffOracleService.getUserInfoExam(staffId, orgid, PersonnelType.EXAM_PERSONNEL);
//		if (userInfoExam == null) {
//			return ResponseFormat.retParam(1, "非考试人员，不允许考试，请联系管理人员申请权限", null);
//		}
		//复制参数
		String paperId = baseService.createPaper(String.valueOf(loginStaff.getStaffid()), reqDTO.getExamId());
		return ResponseFormat.retParam(200, 200, new BaseIdRespDTO(paperId));
	}

	/**
	 * 批量删除
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "试卷详情")
	@RequestMapping(value = "/paper-detail", method = {RequestMethod.POST})
	public ApiRest<ExamDetailRespDTO> paperDetail(@RequestBody BaseIdReqDTO reqDTO) {
		//根据ID删除
		ExamDetailRespDTO respDTO = baseService.paperDetail(reqDTO.getId());
		return super.success(respDTO);
	}

	/**
	 * 批量删除
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "试题详情")
	@RequestMapping(value = "/qu-detail", method = {RequestMethod.POST})
	public ApiRest<PaperQuDetailDTO> quDetail(@RequestBody PaperQuQueryDTO reqDTO) {
		//根据ID删除
		PaperQuDetailDTO respDTO = baseService.findQuDetail(reqDTO.getPaperId(), reqDTO.getQuId());
		return super.success(respDTO);
	}

	/**
	 * 填充答案
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "填充答案")
	@RequestMapping(value = "/fill-answer", method = {RequestMethod.POST})
	public ApiRest<PaperQuDetailDTO> fillAnswer(@RequestBody PaperAnswerDTO reqDTO) {
		//根据ID删除
		baseService.fillAnswer(reqDTO);
		return super.success();
	}


	/**
	 * 交卷操作
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "交卷操作")
	@RequestMapping(value = "/hand-exam", method = {RequestMethod.POST})
	public ApiRest<PaperQuDetailDTO> handleExam(@RequestBody BaseIdReqDTO reqDTO, @RequestHeader String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		//根据ID删除
		baseService.handExam(reqDTO.getId(), loginStaff.getStaffid().toString());
		return super.success();
	}


	/**
	 * 批量删除
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "试卷详情")
	@RequestMapping(value = "/paper-result", method = {RequestMethod.POST})
	public ApiRest<ExamResultRespDTO> paperResult(@RequestBody BaseIdReqDTO reqDTO) {
		//根据ID删除
		ExamResultRespDTO respDTO = baseService.paperResult(reqDTO.getId());
		return super.success(respDTO);
	}


	/**
	 * 检测用户有没有中断的考试
	 * @return
	 */
	@Operation(summary = "检测进行中的考试")
	@RequestMapping(value = "/check-process", method = {RequestMethod.POST})
	public JsonBean checkProcess(@RequestHeader("token") String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null || loginStaff.getLinkOrg() == null || loginStaff.getLinkOrg().getOrgid() == null) {
			throw new ServiceException(401, 20006);
		}
//		int staffId = Integer.parseInt(loginStaff.getStaffid().toString());
//		int orgid = Integer.parseInt(loginStaff.getLinkOrg().getOrgid().toString());
//		StaffResult userInfoExam = tblStaffOracleService.getUserInfoExam(staffId, orgid, PersonnelType.EXAM_PERSONNEL);
//		if (userInfoExam == null) {
//			return ResponseFormat.retParam(1, "非考试人员，不允许考试，请联系管理人员申请权限", null);
//		}
		//复制参数
		PaperDTO dto = baseService.checkProcess(String.valueOf(loginStaff.getStaffid()));
		return ResponseFormat.retParam(200, 200, dto);
	}
}
