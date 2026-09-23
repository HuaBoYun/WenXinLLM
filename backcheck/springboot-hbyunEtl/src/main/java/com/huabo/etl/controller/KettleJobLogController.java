package com.huabo.etl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.etl.domain.KettleJobLog;
import com.huabo.etl.service.IKettleJobLogService;
import com.huabo.etl.utils.page.TableDataInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @ClassName : KettleJobLogController
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2022-12-03 16:55:40
 */
@Tag(name="作业日志接口",description="作业日志接口")
@RestController
@RequestMapping("/kettle/job/log")
public class KettleJobLogController extends BaseController {

    @Autowired
    private IKettleJobLogService kettleJobLogService;

    /**
     * 查询作业调度列表
     */
    @Operation(summary = "查询作业调度列表")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody KettleJobLog kettleJobLog) {
        startPage();
        List<KettleJobLog> list = kettleJobLogService.selectKettleJobLogList(kettleJobLog);
        return getDataTable(list);
    }


}
