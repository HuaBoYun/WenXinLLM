package com.huabo.etl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.etl.domain.KettleTransLog;
import com.huabo.etl.service.IKettleTransLogService;
import com.huabo.etl.utils.page.TableDataInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @ClassName : KettleJobLogController
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 16:55:40
 */
@Tag(name="转换日志接口",description="转换日志接口")
@RestController
@RequestMapping("/kettle/trans/log")
public class KettleTransLogController extends BaseController {

    @Autowired
    private IKettleTransLogService kettleTransLogService;

    /**
     * 查询转换调度列表
     */
    @Operation(summary = "查询转换调度列表")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody KettleTransLog kettleTransLog) {
        startPage();
        List<KettleTransLog> list = kettleTransLogService.selectKettleTransLogList(kettleTransLog);
        return getDataTable(list);
    }


}
