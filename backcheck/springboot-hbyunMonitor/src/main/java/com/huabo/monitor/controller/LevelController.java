package com.huabo.monitor.controller;


import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblAssesslevel;
import com.huabo.monitor.service.TblAssesslevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@Api(value = "等级维护", tags = {"等级维护相关"})
@RequestMapping(value = "/nbkz")
public class LevelController {

    @Resource
    private TblAssesslevelService tblAssesslevelService;

    @GetMapping(value = "/gzdg/level_list")
    public JsonBean gzdg_level_list(@ApiParam(name = "token", value = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @ApiParam(name = "pageNumber", value = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                    @ApiParam(name = "pageSize", value = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                    @RequestBody TblAssesslevel tblAssesslevel) {

        return tblAssesslevelService.findByPageBean(pageNumber, pageSize, tblAssesslevel);

    }

    @GetMapping(value = "/gzdg/level_findByid")
    public JsonBean levelInfo(@RequestParam(value = "asslevid", required = true) BigDecimal id) {
        return tblAssesslevelService.findById(id);
    }

    /**
     * 等级添加
     *
     * @return
     */
    @PostMapping(value = "/gzdg/level_add")
    public JsonBean gzdg_levle_add(@RequestBody TblAssesslevel tblAssesslevel) {
        return tblAssesslevelService.add(tblAssesslevel);
    }

    /**
     * 修改等级
     *
     * @return
     */
    @PostMapping(value = "/gzdg/level_modify")
    public JsonBean gzdg_level_modify(@RequestBody TblAssesslevel tblAssesslevel) {
        return tblAssesslevelService.update(tblAssesslevel);
    }

    @DeleteMapping(value = "/gzdg/level_delete")
    public JsonBean gzdg_level_deleteByIds(@RequestParam("asslevid") BigDecimal asslevid) {
        return tblAssesslevelService.delete(asslevid);
    }

    ;

}
