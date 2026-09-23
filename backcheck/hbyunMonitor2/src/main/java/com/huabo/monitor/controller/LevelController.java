package com.huabo.monitor.controller;


import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.service.TblAssesslevelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name="内控设置-等级维护",description="内控设置-等级维护")
@RequestMapping(value = "/nbkz")
public class LevelController {

    @Resource
    private TblAssesslevelService tblAssesslevelService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "等级维护列表-内容页面分页功能查询成功",
            busType = "内控设置",
            fail = "等级维护列表-内容页面分页功能查询失败",
            operationType = OperationType.SELECT,
            subType = "等级维护"
    )
    @GetMapping(value = "/gzdg/level_list")
    @Operation(summary = "等级维护列表-内容页面分页功能")
    public JsonBean gzdg_level_list(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                    @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize)throws Exception {

    	TblStaffUtil user = userProvider.get();
		if (user == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        return tblAssesslevelService.findByPageBean(pageNumber, pageSize,user.getCurrentOrg().getOrgid().toString(),user);

    }

    @OperationLog(
            success = "通过等级主键查询查询成功",
            busType = "内控设置",
            fail = "通过等级主键查询查询失败",
            operationType = OperationType.SELECT,
            subType = "等级维护"
    )
    @GetMapping(value = "/gzdg/level_findByid")
    @Operation(summary = "通过等级主键查询")
    public JsonBean levelInfo(@RequestParam(value = "asslevid", required = true) BigDecimal id, @RequestHeader("token") String token)throws Exception {
    	TblStaffUtil user = userProvider.get();
		if (user == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        return tblAssesslevelService.findById(id);
    }

    /**
     * 新增等级
     *
     * @return
     */
    @OperationLog(
            success = "新增等级成功",
            busType = "内控设置",
            fail = "新增等级失败",
            operationType = OperationType.ADD,
            subType = "等级维护"
    )
    @PostMapping(value = "/gzdg/level_add")
    @Operation(summary = "新增等级")
    public JsonBean gzdg_levle_add(@RequestBody TblAssesslevel tblAssesslevel,@RequestHeader("token") String token) throws Exception{
    	TblStaffUtil user = userProvider.get();
		if (user == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        tblAssesslevel.setModifieddate(new Date());
        tblAssesslevel.setTblcomany(user.getCurrentOrg().getOrgid().toString());
        tblAssesslevel.setAsslevid(RandomUtil.uuBigDecimalId());
        tblAssesslevel.setCreatestaffid(user.getStaffid());
        tblAssesslevel.setCreatetime(new Date());
        tblAssesslevel.setLinkdeptid(user.getLinkDetp().getOrgid());
        return tblAssesslevelService.add(tblAssesslevel);
    }

    /**
     * 修改等级
     *
     * @return
     */
    @OperationLog(
            success = "修改等级成功",
            busType = "内控设置",
            fail = "修改等级失败",
            operationType = OperationType.UPDATE,
            subType = "等级维护"
    )
    @PostMapping(value = "/gzdg/level_modify")
    @Operation(summary = "修改等级")
    public JsonBean gzdg_level_modify(@RequestBody TblAssesslevel tblAssesslevel, @RequestHeader("token") String token) throws Exception{
    	TblStaffUtil user = userProvider.get();
		if (user == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        tblAssesslevel.setModifieddate(new Date());
        return tblAssesslevelService.update(tblAssesslevel);
    }

    /**
     * 删除等级
     *
     * @param asslevid
     * @return
     */
    @OperationLog(
            success = "删除等级成功",
            busType = "内控设置",
            fail = "删除等级失败",
            operationType = OperationType.DELETE,
            subType = "等级维护"
    )
    @DeleteMapping(value = "/gzdg/level_delete")
    @Operation(summary = "删除等级")
    public JsonBean gzdg_level_deleteByIds(@RequestParam("asslevid") BigDecimal asslevid, @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil user = userProvider.get();
		if (user == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        return tblAssesslevelService.delete(asslevid);
    }

}
