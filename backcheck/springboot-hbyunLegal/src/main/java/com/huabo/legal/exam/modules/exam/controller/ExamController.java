package com.huabo.legal.exam.modules.exam.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.exam.core.api.ApiRest;
import com.huabo.legal.exam.core.api.controller.BaseController;
import com.huabo.legal.exam.core.api.dto.BaseIdReqDTO;
import com.huabo.legal.exam.core.api.dto.BaseIdsReqDTO;
import com.huabo.legal.exam.core.api.dto.BaseStateReqDTO;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.exam.dto.ExamDTO;
import com.huabo.legal.exam.modules.exam.dto.request.ExamSaveReqDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamOnlineRespDTO;
import com.huabo.legal.exam.modules.exam.dto.response.ExamReviewRespDTO;
import com.huabo.legal.exam.modules.exam.entity.Exam;
import com.huabo.legal.exam.modules.exam.service.ExamService;
import com.huabo.legal.exam.modules.paper.service.PaperService;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.service.ExamStaffService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.vo.TblFwglStaffExamParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 考试控制器
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-07-25 16:18
 */
@Tag(name="考试",description="考试")
@RestController
@RequestMapping("/exam/api/exam/exam")
public class ExamController extends BaseController {

	@Autowired
	private ExamService baseService;

	@Autowired
	private PaperService paperService;

	@Autowired
	private ExamStaffService examStaffService;

	@Operation(summary = "指定考试人员")
	@PostMapping("/examStaff")
	public JsonBean ExamStaffAdd(@RequestBody TblFwglStaffExamParam staffexamVo) {
		JsonBean jsonBean = null;

		try {
			jsonBean = examStaffService.addExamStaff(staffexamVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return jsonBean;
	}

	@Operation(summary = "查询考试人员")
	@PostMapping("/ExamStaffInfo")
	public JsonBean ExamStaffInfo(@RequestBody TblFwglStaffExamParam staffexamVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当每页记录数", required = false) Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examStaffService.ExamStaffInfo(staffexamVo, pageNumber, pageSize);
		} catch (ServiceException ex) {
			throw ex;
		}
		return jsonBean;
	}

	@Operation(summary = "指定考试人员id返回，前端做回填")
	@PostMapping("/examStaffList")
	public JsonBean TblFwglexamStaffList(@RequestBody TblFwglStaffExamParam staffexamVo) {
		JsonBean jsonBean = null;

		try {
			jsonBean = examStaffService.ExamStaffList(staffexamVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return jsonBean;
	}

	@Operation(summary = "删除考试人员（正在考试中不能删除）")
	@DeleteMapping("/ExamStaffDelete")
	public JsonBean ExamStaffDelete(@RequestBody TblFwglStaffExamParam staffexamVo) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examStaffService.examStaffDelete(staffexamVo.getStaffId());
		} catch (ServiceException ex) {
			throw ex;
		}
		return jsonBean;
	}

	@Operation(summary = "考试状态修改")
	@PostMapping("/ExamUpdate")
	public JsonBean ExamUpdate(@RequestBody TblFwglStaffExamParam staffexamVo) {
		JsonBean jsonBean = null;
		try {
			jsonBean = examStaffService.examUpdate(staffexamVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 添加或修改
	 * 
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "添加或修改")
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiRest save(@RequestBody ExamSaveReqDTO reqDTO) {
		// 复制参数
		baseService.save(reqDTO);
		return super.success();
	}

	/**
	 * 批量删除
	 * 
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "批量删除")
	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public JsonBean edit(@RequestBody BaseIdsReqDTO reqDTO) {
		// boolean flag = false;
		Integer flag1 = examStaffService.getExamStatus(reqDTO.getIds().toString());
		if (flag1 == 0) {
			// 判断考试是否有人已经使用
//		if (CollectionUtils.isNotEmpty(reqDTO.getIds())) {
//			for (String str : reqDTO.getIds()) {
//				QueryWrapper<Paper> wrapper = new QueryWrapper<>();
//				wrapper.lambda().eq(Paper::getExamId, str);
//				List<Paper> list = paperService.list(wrapper);
//				if (CollectionUtils.isNotEmpty(list)) {
//					flag = true;
//					break;
//				}
//			}
//			if (flag) {
//				return ResponseFormat.retParam(1, "该考试试题已有考生作答，无法删除", null);
//			}
//			// 根据ID删除
//			baseService.removeByIds(reqDTO.getIds());
//		}
			// 根据ID删除
			baseService.removeByIds(reqDTO.getIds());
			return ResponseFormat.retParam(200, 200, null);
		}
		return ResponseFormat.retParam(200, "该考试正在进行中，无法删除", null);
	}

	/**
	 * 查找详情
	 * 
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "查找详情")
	@RequestMapping(value = "/detail", method = { RequestMethod.POST })
	public ApiRest<ExamSaveReqDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
		ExamSaveReqDTO dto = baseService.findDetail(reqDTO.getId());
		return super.success(dto);
	}

	/**
	 * 查找详情
	 * 
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "查找详情")
	@RequestMapping(value = "/state", method = { RequestMethod.POST })
	public ApiRest state(@RequestBody BaseStateReqDTO reqDTO) {

		QueryWrapper<Exam> wrapper = new QueryWrapper<>();
		wrapper.lambda().in(Exam::getId, reqDTO.getIds());
		Exam exam = new Exam();
		exam.setState(reqDTO.getState());
		exam.setUpdateTime(new Date());

		baseService.update(exam, wrapper);
		return super.success();
	}


	/**
	 * 分页查找
	 * 
	 * @param reqDTO
	 * @return
	 * @throws Exception
	 */
	@Operation(summary = "考试视角")
	@RequestMapping(value = "/online-paging", method = { RequestMethod.POST })
	public ApiRest<PageResult<ExamOnlineRespDTO>> myPaging(@RequestBody PagingReqDTO<ExamDTO> reqDTO,
			@RequestHeader String token) throws Exception {

		// 分页查询并转换
		return super.success(baseService.onlinePaging(reqDTO, token));
	}
	/**
	 * 分页查找
	 *
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "分页查找  (demo的分页测试用例)")
	@RequestMapping(value = "/paging", method = { RequestMethod.POST })
	public ApiRest<PageResult<ExamDTO>> paging1(@RequestBody PagingReqDTO<ExamDTO> reqDTO) {
		// 分页查询并转换
		return super.success(baseService.paging1(reqDTO));
	}

	/**
	 * 分页查找
	 * 
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "待阅试卷")
	@RequestMapping(value = "/review-paging", method = { RequestMethod.POST })
	public ApiRest<PageResult<ExamReviewRespDTO>> reviewPaging(@RequestBody PagingReqDTO<ExamDTO> reqDTO) {

		// 分页查询并转换
		//IPage<ExamReviewRespDTO> page = baseService.reviewPaging(reqDTO);

		return super.success(baseService.reviewPaging(reqDTO));
	}

}
