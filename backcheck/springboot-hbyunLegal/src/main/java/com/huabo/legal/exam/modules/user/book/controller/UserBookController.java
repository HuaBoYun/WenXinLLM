package com.huabo.legal.exam.modules.user.book.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exam.core.api.ApiRest;
import com.huabo.legal.exam.core.api.controller.BaseController;
import com.huabo.legal.exam.core.api.dto.BaseIdRespDTO;
import com.huabo.legal.exam.core.api.dto.BaseIdsReqDTO;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.user.book.dto.UserBookDTO;
import com.huabo.legal.exam.modules.user.book.service.UserBookService;
import com.huabo.legal.exception.ServiceException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 错题本控制器
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-05-27 17:56
 */
@Tag(name="错题本",description="错题本")
@RestController
@RequestMapping("/exam/api/user/wrong-book")
public class UserBookController extends BaseController {

	@Autowired
	private UserBookService baseService;
	
	@Resource
	private UserProvider userProvider;


	/**
	 * 批量删除
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "批量删除")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST})
	public ApiRest delete(@RequestBody BaseIdsReqDTO reqDTO) {
		//根据ID删除
		baseService.removeByIds(reqDTO.getIds());
		return super.success();
	}

	
	@Operation(summary = "分页查找")
	@RequestMapping(value = "/paging", method = {RequestMethod.POST})
	public JsonBean paging(@RequestHeader("token") String token, @RequestBody PagingReqDTO<UserBookDTO> reqDTO) throws Exception {
		TblStaffUtil tblStaffUtil = userProvider.get();

		JsonBean jsonBean = null;
		try {
			jsonBean = baseService.paging(reqDTO, tblStaffUtil.getStaffid().toString());
		} catch (ServiceException ex) {
			throw ex;
		} 
		return jsonBean;
	}

	/**
	 * 查找列表，每次最多返回200条数据
	 * @param reqDTO
	 * @return
	 */
	@Operation(summary = "查找列表")
	@RequestMapping(value = "/next", method = {RequestMethod.POST})
	public ApiRest<BaseIdRespDTO> nextQu(@RequestBody UserBookDTO reqDTO, @RequestHeader("token") String token) throws Exception {
		TblStaffUtil tblStaffUtil = userProvider.get();
		//转换并返回
		String quId = baseService.findNext(reqDTO.getExamId(), reqDTO.getQuId(), tblStaffUtil.getStaffid().toString());
		return super.success(new BaseIdRespDTO(quId));
	}
}
