package com.huabo.es.controller;

import java.util.List;

import javax.annotation.Resource;

import org.frameworkset.elasticsearch.entity.ESDatas;
import org.frameworkset.elasticsearch.entity.MetaMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.es.domain.AccEntity;
import com.huabo.es.service.AccService;
import com.huabo.es.utils.page.TableDataInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @ClassName : AccController
 * @Description : 账簿es接口
 * @Author : zhibo.cao
 * @Date: 2023-03-29 10:55:57
 */
@Tag(name="账簿es接口",description="账簿es接口")
@RestController
@RequestMapping("/acc")
public class AccController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(AccController.class);

    @Resource
    private AccService accService;

    /**
     * 账簿分页查询
     */
    @Operation(summary = "账簿分页查询")
    @PostMapping("/accDataByPage")
    public TableDataInfo outDataByPage(@RequestBody AccEntity acc) {
        List<MetaMap> accs = CollUtil.newArrayList();
        if (StrUtil.isEmpty(acc.getKeyWord())) {
            return getDataTable(accs);
        }
        if (acc.getPageNum() == null || acc.getPageNum() == 0L) {
            acc.setPageNum(1L);
        }
        if (acc.getPageSize() == null || acc.getPageSize() == 0L) {
            acc.setPageSize(20L);
        }
        ESDatas<MetaMap> accEsData = accService.getAccESDatas(acc.getKeyWord(), acc.getPageNum(), acc.getPageSize());
        accs = accService.accHandler(accEsData);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.OK.value());
        rspData.setMsg("查询成功");
        rspData.setRows(accs);
        rspData.setTotal(accEsData.getTotalSize());
        return rspData;
    }
}
