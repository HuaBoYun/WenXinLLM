package com.huabo.audit.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ExecPhraseController
 * @Description 审计实施
 * @Author yan
 * @Date 2022/4/12 16:27
 * @Version 1.0
 */
@OperationLog(
        success = "审计实施",
        busType = "智能审计",
        fail = "审计实施",
        operationType = OperationType.SELECT,
        subType = "审计实施——获取审计实施所有接口"
)
@RestController
@Slf4j
@Tag(name="审计实施",description="审计实施")
@RequestMapping(value = "/audit/execPharse")
public class ExecPhraseController {
}
