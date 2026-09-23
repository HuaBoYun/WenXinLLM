package com.huabo.financialdata.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.entity.TblAccBkpf;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfRequestVo;
import com.huabo.financialdata.entity.vo.accBkpf.AccBkpfResponseVo;
import com.huabo.financialdata.service.AccBkpfService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 凭证库  接口请求类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-22
 */
@RestController
@RequestMapping(value = "/accBkpf")
@Tag(name="凭证库",description="凭证库")
public class AccBkpfController {

    @Resource
    AccBkpfService accBkpfService;

    /**
     * 凭证库 列表分页查询
     *
     * @param token            用户登录token
     * @param accBkpfRequestVo 请求参数封装公共类
     * @return
     */
    @PostMapping("/list")
    @Operation(summary="凭证库 - 列表分页查询",description="凭证库 - 列表分页查询")
    public ApiResponse<PageInfo<AccBkpfResponseVo>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "accBkpfRequestVo", description = "凭证库信息请求公共类accBkpfRequestVo", required = true)
            @Validated @RequestBody AccBkpfRequestVo accBkpfRequestVo) {

        return accBkpfService.getList(token, accBkpfRequestVo);
    }

    /**
     * 凭证库 记账凭证
     *
     * @param token 用户登录token
     * @return
     */
    @PostMapping("/bookkeepingDetail")
    @Operation(summary="凭证库 - 记账凭证",description="凭证库 - 记账凭证")
    public ApiResponse<List<TblAccBkpf>> bookkeepingDetail(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Validated @RequestBody AccBkpfRequestVo accBkpfRequestVo) throws Exception {

        return accBkpfService.findAccBkpfAmonth(token, accBkpfRequestVo.getPzh(), accBkpfRequestVo.getPzDate(), accBkpfRequestVo.getBook(), accBkpfRequestVo.getYear());
    }


}
