package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.platform.commons.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblBug;
import com.huabo.monitor.entity.TblBugCriterionEntity;
import com.huabo.monitor.service.ITblBugService;
import com.huabo.monitor.service.TblBugCriterionService;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.util.PageBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * ICS: Internal Control Setting
 * defect standard controller
 */

@RestController
@Slf4j
@Tag(name="内控设置-缺陷标准",description="内控设置-缺陷标准")
@RequestMapping(value = "/nbkz")
public class ICSDefectStandardController {

    @Resource
    public PageBean pageBean;
    @Resource
    TblBugCriterionService tblBugCriterionService;

    @Resource
    ITblBugService tblBugService;
    
    @Resource
    private UserProvider userProvider;
    

    @OperationLog(
            success = "gzdg/def_quexian_list查询成功",
            busType = "内控设置",
            fail = "gzdg/def_quexian_list查询失败",
            operationType = OperationType.SELECT,
            subType = "缺陷标准"
    )
    @Operation(summary = "缺陷标准列表")
    @GetMapping(value = "/gzdg/def_quexian_list")
    public JsonBean qxbz(
            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页行数") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @Parameter(name = "orgid", description = "组织id") @RequestParam(value = "orgid") String orgId,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

		// TblOrganization attribute = (TblOrganization)
		// request.getSession().getAttribute("hbOrgEntity");// 选则的机构
		// IPage<TblBugCriterionEntity> iPage =
		// tblBugCriterionService.findAll(pageNumber, orgId,
		// pageBean.getPageSize());
		PageInfo<TblBugCriterionEntity> pageinfo = tblBugCriterionService.findAllNewPage(pageNumber, orgId, pageSize,token);
        IPageResult<TblBugCriterionEntity> pageInfo=new IPageResult<TblBugCriterionEntity>().buildIpage(pageinfo);

		Map<String, Object> map = new HashMap<>();
		// map.put("pageBean", iPage);
		map.put("pageBean", pageInfo);
		return ResponseFormat.retParam(1, 200, map);
    }

    @OperationLog(
            success = "缺陷标准-保存成功",
            busType = "内控设置",
            fail = "缺陷标准-保存失败",
            operationType = OperationType.ADD,
            subType = "缺陷标准"
    )
    @Operation(summary = "缺陷标准-保存")
    @PostMapping(value = "/gzdg/def_quexian_save")
    public JsonBean qxbz_add(
            @Parameter(name = "con", description = "TblBugCriterionEntity实体类")@RequestBody  TblBugCriterionEntity con,
            @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        TblOrganizationUtil currentOrg = user.getCurrentOrg();
        BigDecimal orgid = currentOrg.getOrgid();
        if ((con != null && con.getBugcriid() == null)||(con.getBugcriid().compareTo(BigDecimal.ZERO)==0)) {
            con.setVersion(1);
            con.setOrgid(orgid);
            con.setCreatestaffid(user.getStaffid());
            con.setCreatetime(new Date());
            con.setLinkdeptid(user.getLinkDetp().getOrgid());
            con.setBugcriid(RandomUtil.uuBigDecimalId());
            tblBugCriterionService.saveEntity(con);
        } else {
            con.setOrgid(orgid);
            con.setVersion(con.getVersion() + 1);
            tblBugCriterionService.update(con);
        }
        return ResponseFormat.retParam(1, 200, "success");
    }


    @OperationLog(
            success = "缺陷标准-查看及修改时获取数据查询成功",
            busType = "内控设置",
            fail = "缺陷标准-查看及修改时获取数据查询失败",
            operationType = OperationType.SELECT,
            subType = "缺陷标准"
    )
    @Operation(summary = "缺陷标准-查看及修改时获取数据")
    @GetMapping(value = "/gzdg/def_quexian_detail")
    public JsonBean qxbz_detail(
            @Parameter(name = "selectid", description = "selectid") @RequestParam(value = "selectid") String selectid,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(selectid)) {
            TblBugCriterionEntity con = tblBugCriterionService.findByid(selectid);
            map.put("con", con);
        }

        return ResponseFormat.retParam(1, 200, map);
    }

    @OperationLog(
            success = "缺陷标准-删除成功",
            busType = "内控设置",
            fail = "缺陷标准-删除失败",
            operationType = OperationType.DELETE,
            subType = "缺陷标准"
    )
    @Operation(summary = "缺陷标准-删除")
    @DeleteMapping(value = "/gzdg/def_quexian_del")
    public JsonBean qxbz_del(
            @Parameter(name = "ids", description = "ids") @RequestParam(value = "ids") String[] ids,
            @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        String is = "ok";
        for (int i = 0; i < ids.length; i++) {
            TblBugCriterionEntity con = tblBugCriterionService.findByid(ids[i]);
            List<TblBug> findByCriterionId = tblBugService.findByCriterionId(con.getBugcriid());
            if (null != findByCriterionId && findByCriterionId.size() > 0) {
                return ResponseFormat.retParam(0, 50001, "error");

            }
            try {
                tblBugCriterionService.del(con.getBugcriid());
            } catch (Exception e) {
                is = con.getBugcrilevel();
            }
        }
        return ResponseFormat.retParam(1, 200, is);
    }

}
