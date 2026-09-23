package com.huabo.audit.controller;
import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblWgzzWgjyYs;
import com.huabo.audit.service.TblWgjyYsService;
import com.huabo.audit.service.TblWgzzWghcService;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.controller
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:15
 */
@RestController
@Slf4j
@Tag(name="违规追责",description="违规追责")
@RequestMapping(value = "/wgjyys/projectTeam")
public class TblWgjyYsController {
    @Autowired
    TblWgjyYsService tblWgjyYsService;

    @Autowired
    TblWgzzWghcService tblWgzzWghcService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "违规经营移送列表",
            busType = "整改追责",
            fail = "违规经营移送列表",
            operationType = OperationType.SELECT,
            subType = "违规追责——获取违规经营移送列表相关信息"
    )
    @GetMapping("/wgjyysList")
    @Operation(summary = "违规经营移送列表")
    public JsonBean wgjyysList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value = "clueNaber",required=false) String clueNaber,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize
                             ){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgjyYsService.getByWgjyYsList(token,pageNumber,pageSize,clueNaber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规经营移送列表新增/修改",
            busType = "整改追责",
            fail = "违规经营移送列表新增/修改",
            operationType = OperationType.ADD,
            subType = "违规追责——违规经营移送列表新增/修改列表页"
    )
    @RequestMapping(value = "/wgjyysSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "违规经营移送新增/修改")
    public JsonBean wgjyysSave(	@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                      @Parameter(name = "tblWgzzWgjyYs", description = "实体", required = false) TblWgzzWgjyYs tblWgzzWgjyYs)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgjyYsService.addlist(token,tblWgzzWgjyYs);
//            jsonBean = tblWgzzWghcService.updateStatus(token,tblWgzzWgjyYs.getWghcid(),3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规经营移送删除",
            busType = "整改追责",
            fail = "违规经营移送删除",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规经营移送删除指定记录"
    )
    @PostMapping("/wgjyysRemove")
    @Operation(summary = "违规经营移送删除")
    public JsonBean wgjyysRemove(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id
    )
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgjyYsService.removeList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规经营移送附件删除",
            busType = "整改追责",
            fail = "违规经营移送附件删除",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规经营移送删除指定附件"
    )
	@PostMapping("/wgjyysRemovefilue")
	@Operation(summary = "违规经营移送附件删除")
	public R getByWghcdelete(
			@Parameter(description="token")@RequestHeader("token")String token,
			@Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return R.fail("用户已失效！");
		}
		return this.tblWgjyYsService.deleteAttInfoByAttId(attId);
	}

    @OperationLog(
            success = "违规核实查询移送函详情",
            busType = "整改追责",
            fail = "违规核实查询移送函详情",
            operationType = OperationType.SELECT,
            subType = "违规追责——违规核实查询移送函详情信息"
    )
    @GetMapping("/wghcysDetail")
    @Operation(summary = "违规核实查询移送函详情")
    public JsonBean wghcysDetail(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                 @Parameter(name="wghcid",required=false)@RequestParam("wghcid")BigDecimal wghcid){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgjyYsService.detail(token,wghcid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }



    @OperationLog(
            success = "移送函详情",
            busType = "整改追责",
            fail = "移送函详情",
            operationType = OperationType.SELECT,
            subType = "违规追责——获取移送函详情信息"
    )
    @GetMapping("/getByidDetail")
    @Operation(summary = "移送函详情")
    public JsonBean onewghcysDetail(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                 @Parameter(name="id",required=false)@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgjyYsService.getbyiddetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
}
