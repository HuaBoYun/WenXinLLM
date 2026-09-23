package com.huabo.fxgl.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.user.UserProvider;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Redis组织缓存控制器
 * <p>提供风险管控模块组织机构的Redis缓存管理接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/redisorg")
//@Tag(name="树",description="树")
public class RedisorgController {

//    @Autowired
//    private IOrganizationService organizationService;
	@Resource
    private UserProvider userProvider;
//
//    /**
//     * <p>
//     *  树
//     * </p>
//     *
//     *@author LiHongXu
//     *@version 1.0.1
//     *@since 2022-08-17
//     */
//    @RequestMapping(value = "/findOrganizationByTreeAllbm", produces = "application/json; charset=utf-8")
//    @Operation(summary = "/redisorg/findOrganizationByTreeAllbm")
//    public String findOrganizationByTrees(@Parameter(name="nodeId",description="nodeId") BigDecimal nodeId,
//                                          @Parameter(name="type",description="type") String type,
//                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
//        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
//        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
//        if (null == nodeId) {
//            nodeId = selectOrg.getOrgid();
//        }
//        String str;
//        HashMap<String, Object> fields = new HashMap<String, Object>();
//        fields.put("nodeId",nodeId);
//        try {
//            str = HttpClient.request(HttpClient.getDeptUrl, fields, null);
//            if(!StringUtils.isNotBlank(str)) {
//                List<Tree> list = organizationService.getNodeAllbm(nodeId);
//                str = JSONObject.toJSONString(list);
//            }
//            return str;
//        } catch (Exception e) {
//            List<Tree> list = organizationService.getNodeAllbm(nodeId);
//            str = JSONObject.toJSONString(list);
//        }
//        return str;
//    }
}
