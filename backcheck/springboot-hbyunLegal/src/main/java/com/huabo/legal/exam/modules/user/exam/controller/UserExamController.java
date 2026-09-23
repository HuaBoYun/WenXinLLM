package com.huabo.legal.exam.modules.user.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.legal.exam.core.api.controller.BaseController;
import com.huabo.legal.exam.core.api.dto.PagingReqDTO;
import com.huabo.legal.exam.modules.user.exam.dto.request.UserExamReqDTO;
import com.huabo.legal.exam.modules.user.exam.service.UserExamService;
import com.huabo.legal.exception.ServiceException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
* <p>
* 考试记录控制器
* </p>
*
* @author 聪明笨狗
* @since 2020-09-21 15:13
*/
@Tag(name="考试记录",description="考试记录")
@RestController
@RequestMapping("/exam/api/user/exam")
public class UserExamController extends BaseController {

    @Autowired
    private UserExamService baseService;


    /**
     * 分页查找
     * @param reqDTO
     * @return
     
    @Operation(summary = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public ApiRest<IPage<UserExamRespDTO>> paging(@RequestBody PagingReqDTO<UserExamReqDTO> reqDTO) {

        //分页查询并转换
        IPage<UserExamRespDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }*/
    /**
     * 考试详情分页查找
     * @param reqDTO
     * @return
     */
    @Operation(summary = "分页查找")
    @RequestMapping(value = "/paging", method = { RequestMethod.POST})
    public JsonBean paging(@RequestBody PagingReqDTO<UserExamReqDTO> reqDTO) {
        JsonBean jsonBean = null;
		try {
			jsonBean = baseService.paging(reqDTO);
		} catch (ServiceException ex) {
			throw ex;
		} 
		return jsonBean;
    }


    /**
    * 分页查找
    * @param reqDTO
    * @return
    */
    @Operation(summary = "分页查找")
    @RequestMapping(value = "/my-paging", method = { RequestMethod.POST})
    public JsonBean MyPaging(@RequestBody PagingReqDTO<UserExamReqDTO> reqDTO) {
    	
    	JsonBean jsonBean = null;
		try {
			jsonBean = baseService.myPaging(reqDTO);
		} catch (ServiceException ex) {
			throw ex;
		} 
		return jsonBean;
    }
}
