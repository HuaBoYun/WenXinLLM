package com.huabo.fxgl.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.service.IYyTeamService;

import lombok.extern.slf4j.Slf4j;

/**
 * 供应商监控控制器（已废弃）
 * <p>提供风险监控中供应商监控的相关接口，已停用</p>
 *
 * @author hbyun
 */
//@RestController
//@RequestMapping(value = "/fxyj", method = {RequestMethod.GET, RequestMethod.POST})
//@Tag(name="风险监控 - 供应商监控",description="风险监控 - 供应商监控")
@Slf4j
public class YyTeamController {

    @Autowired
    private IYyTeamService yyTeamService;

    @Resource
    private UserProvider userProvider;

    /**
     * http://huabao.example.com/fxyj/saveteam?groupname=%E6%B5%8B%E8%AF%95%E5%88%86%E7%BB%84&teamid=654887
     * 请求方法: POST
     * groupname: 测试分组
     * teamid: 654887
     *
     * @param teamid    分组ID
     * @param groupname 分组名称
     * @return
     */
   /* @RequestMapping("/saveteam")
    @Operation(summary = "风险监控 - 供应商监控 - 保存分组（新增和修改） /fxyj/saveteam")
    public String saveTeam(@Parameter(name = "teamid", description = "teamid") @RequestParam(required = false) String teamid,
                           @Parameter(name = "groupname", description = "groupname") @RequestParam String groupname,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                           ) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        log.info("----------------------------------staffUtil Info: " + staffUtil);
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        if (StringUtils.isNotBlank(teamid)) {
            //若teamID不为空即为修改其分组名称
            YyTeam team = yyTeamService.getById(teamid);
            team.setTeamname(groupname);//修改分组名称
            yyTeamService.updateById(team);
            return teamid;
        } else {
            //若teamid为提供，认为是新增分组

            YyTeam team = new YyTeam( groupname, LocalDateTime.now(), staffUtil.getStaffid(), selectOrg.getOrgid(), 0L);
//            log.info("Team info: " + team);
//            YyTeam team = new YyTeam(groupname, LocalDateTime.now(), new BigDecimal(staffid), new BigDecimal(orgid), 0L);
            yyTeamService.save(team);
            return String.valueOf(team.getTeamid());
        }
    }*/
}
