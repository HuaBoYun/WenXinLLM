package com.huabo.audit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.audit.service.TblNbsjProjectteamService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ProjectTeamController
 * @Description 项目团队接口
 * @Author ZiYao
 * @Date 2022/4/20 15:14
 * @Version 1.0
 */
@RestController
@Slf4j
@Tag(name="项目团队",description="项目团队")
@RequestMapping(value = "/audit/projectTeam")
public class ProjectTeamController {
    @Autowired
    private TblNbsjProjectteamService projectTeamService;

    
}
